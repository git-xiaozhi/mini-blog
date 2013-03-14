/**
 * 功能： 浮层插件
 */
(function($){
	$.layer = {};
		//表情的控制参数
		expEnable = true,
		//配置
		config = {
			//用浮层标题
			layerTitle: '',
			//包含textarea和表情触发的exp-holder
			holder: '.exp-holder',
			//exp-holder中的textarea输入dom，默认为textarea
			textarea : 'textarea',
			//触发dom
			trigger : '.layer-trigger',
			//每页显示表情的组数
			grpNum : 5,
			//位置相对页面固定(absolute)||窗口固定(fixed)
			posType : 'absolute',
			//浮动层层数
			zIndex : '9999'
		},
		//矫正插件位置偏离
		pos_correct_left = 30,
		//关闭triggerpos_correct_left
		exp_close_tri = '.exp-close',
		//group panel可容纳的最发group数量
		grp_num_per_panel = 1,
		win = window || document,
		bd = 'body';
	
	/**
	 * 初始化表情插件
	 */
	function init(cfg){
		//参数整合
		//$.extend(config,cfg);
		//var triggers = $(config.trigger);
                /**这里取消循环bind方式绑定事件，用live绑定为ajax后来新增元素能得到事件绑定*/
		//triggers.each(function(){
			$(config.trigger).live('click',function(){
				//大量参数预定义,获取
				var me = $(this),
					exp = $(_genrt_html()),
					off = me.offset(), me_l = off.left - 50, me_t = off.top, me_w = me.width(), me_h = me.height(),
					exp_t = me_t + me_h, exp_l = me_l + (me_w + pos_correct_left)/2,
					exp_close = exp.find(exp_close_tri);
					
				if(config.posType == 'fixed'){
					me_t = off.top - $(win).scrollTop();
					exp_t = me_t + me_h;
				}

				//如果允许表情
				if(expEnable){
					//确定表情插件的位置
					exp.css({position: config.posType, zIndex: config.zIndex, left:exp_l+'px', top: exp_t+'px'});
					//窗口重置时重新调整插件位置
					$(win).resize(function(){
						off = me.offset(), me_l = off.left - 50, me_t = off.top;
						exp_t = me_t + me_h, exp_l = me_l + (me_w + pos_correct_left)/2;
						exp.css({left:exp_l+'px', top: exp_t+'px'});
					});
					
					/*各种事件绑定*/
					
					//关闭X事件
					exp_close.click(function(){
						$(bd).unbind('click');
						me.unbind('mouseout');
						$(win).unbind('resize');
						exp.remove();
					});
					
					//trigger的鼠标移出事件（点击之后就删除）
					me.mouseout(function(){
						$(bd).click(function(e){
							var clickDOM = $(e.target);
							var a = clickDOM.parents('.exp-layer');
							if(!a.hasClass('exp-layer')){
								exp.remove();
								$(bd).unbind('click');
								me.unbind('mouseout');
								$(win).unbind('resize');
							}
						})
					});

									
					//往页面插入dom
					$('body').append(exp);
					exp.show();
				}
			});
		//})
	}
	/**
	 * 使所有的添加了表情触发类的click事件在表情上失效
	 */
	function disableExp(){
		expEnable = false;
	}
	/**
	 * 重新启用表情
	 */
	function enableExp(){
		expEnable = true;
	}


        function showLayer(url,title){
               $('#layertitle').html(title);
               //ajax获取动态内容
               $.get(url,null,function(data){
               $('.main').html(data);             
             })
	}

       //设置标题
       function setTitle(title){
          $('#layertitle').html(title);
        }

	/**
	 * 生成浮动层的html代码
	 */
	function _genrt_html(){
		var html = '<div class="exp-layer"><div class="holder"><div class="content"><div class="exp-tab clearfix"><a href="javascript:;"><span id="layertitle">'+config.layerTitle+'</span></a></div><div>';

		html += '<div class="main" style="text-align:center;width:100%"></div></div><ul class="exp-detail clearfix">';
		
		html +='</ul></div><a class="exp-close" href="javascript:;"></a></div><a class="exp-tri" href="javascript:;"></a></div>';
		return html;
	}
			
	//扩展到jquery
	$.layer = {
			initExp : init,
                        showLayer :showLayer,
                        setTitle  :setTitle,
			disableExp : disableExp,
			enableExp : enableExp
	};
				
})(jQuery)
