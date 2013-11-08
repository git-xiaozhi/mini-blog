<#import "/templates/mod.ftl" as w/>
<#assign context=rc.getContextPath()/>

<div id="posts" class="span-15 prepend-1 append-bottom">
    <#if pagelist??>
     <span id="post"><#include "/blog/sina/timer/post.ftl" /><span>
    </#if>

</div>



 <script type="text/javascript">

    function openWindow(pid){
        var offset = $('#forward_'+pid).offset();

        $.get('${context}/blog/sina/showRepostForm/'+pid,null,function(data){
             $('#data').html(data);
             $('#w').window({
                top: offset.top-150,
                draggable: false,
                modal: true,
                shadow: true,
                closed: false,
            });
       });
  }

  //删除定时微博
  function removeBlogByMe(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('${context}/blog/sina/removeSinaBlog/'+pid,null,function(data){
    });
   });
   }
   

  </script>



  <script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.blog == null)mini.blog = {};

    mini.blog.list = function(params) {
       if (params == null)params = {};
       $.get('${context}/blog/sina/getSinaBlogsPage',params,function(data){
            $('#post').html(data);
       })
    }


   $(document).ready(function(){
      var date = new Date();
      
      var hour = date.getHours()+"";
      if(hour.length<2)hour = "0"+hour;
      var minute = date.getMinutes()+"";
      if(minute.length<2)minute = "0"+minute;
      
      $('#hour').val(hour);
      $('#minute').val(minute);
      
      
      Date.format = 'yyyy-mm-dd';
      $(function()
       {
		$('.date-pick').datePicker({clickInput:true}).val(date.asString()).trigger('change');
       });
       
       var mintime = 600;
       $("#popup_timer_diff").blur(function(){
        var relay_interval = $("#popup_timer_diff").val();
        if (relay_interval.search(/\D+/g) == -1 ) {
            if (relay_interval * 60 < mintime) {
            	$("#popup_timer_diff").val(mintime/60);
                //UI.dialog.alert('设置的最小间隔时间不得小于' + (mintime/60) + '分钟');
            } else {
                //$.post('/' + app + '/index.php?mod=relay&action=editRelayInterval&account=' + account + '&random=' + UI.random(), {'relay_interval':relay_interval}, function(response) {},"json");
            }
        } else {
        	$("#popup_timer_diff").val(mintime/60);
            //UI.dialog.alert('亲，请输入不小于' + (mintime/60) + '的数字！');
        }
       });
   
   
      //表情控件加载
      $.expBlock.initExp({
        /*
        //用户表情结构数据
        expData: null,
        //包含textarea和表情触发的exp-holder
        holder: '.holder',
        //exp-holder中的textarea输入dom，默认为textarea,
        textarea : 'textarea',
        //触发dom
        trigger : '.trigger',
        //每页显示表情的组数
        grpNum : 5,
        //位置相对页面固定(absolute)||窗口固定(fixed)
        posType : 'absolute',
        //表情层数
        zIndex : '9008'
        */
      });
      $.expBlock.getRemoteExp('/blog/sina/faces');
})




 </script>
