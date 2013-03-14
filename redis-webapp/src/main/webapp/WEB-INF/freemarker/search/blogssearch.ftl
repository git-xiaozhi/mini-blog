<#import "/spring.ftl" as spring />

<#import "/templates/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>

<div class="span-7 box userlistDiv" style="width:100px;">
</div>

  <div id="posts" class="span-15 prepend-1 append-bottom">
     <form>搜微博：<input type="test" name="keyword" id="keyword"/><input type="button" id="searchsubmit" value="<@spring.message "update"/>"/></form>
     <span id="post"></span>
  </div>


<!--你的粉丝-->
  <div class="span-7 last" id="rightcol">
  <div class="span-7 box">
    xxxxxx
  </div>

  <!--你关注的人-->
  <div class="span-7 box">
   xxxxxx
  </div>



 <script type="text/javascript">

  function following(id){
     $.get('${context}/manage/following/follow/'+id,null,function(data){
       if(data){
         $('#followed_'+id).fadeIn(500,function(data){
           $('#followed_'+id).html('<span style="float:right">Linked</span>');
         });
       }
    });
   }

  </script>

  <script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.search == null)mini.search = {};

    mini.search.list = function(params) {
       if (params == null)params = {};
       $.post('${context}/blog/search',params,function(data){
            $('#post').html(data);
       })
    }

   $('#searchsubmit').click(
      function () {
       var params;
       if (params == null)params = {};
       params=$("form").serializeArray();
       $.post('${context}/blog/search',params,function(data){
         $('#post').html(data);
      });
   });
 </script>



 <@base.layer title="Forward Blog"  width=500 height=250></@base.layer>

 <script type="text/javascript">
    $(document).ready(function() {
      $('#w').window('close');
    });

    function openWindow(pid){
        var offset = $('#forward_'+pid).offset();

        $.get('${context}/blog/showForwardForm/'+pid,null,function(data){
             $('#data').html(data);
             $('#w').window({
                top: offset.top-150,
                draggable: false,
        modal: true,
        shadow: false,
        closed: false,
       });
      });
  }

   //转发微博
   function postStatus(pid){
     var params;
   if (params == null)params = {};
   params["content"] = $('#tcontent_'+pid).val();

   $('#w').window('close');
     $('#forwardForm').html('');

     $.post('${context}/blog/forwardBlog/'+pid,params,function(data){
         $('#w').window('close');
         $('#forwardForm').html('');
         $(data).prependTo($('#post'));
   })
   }

  function removeBlogByMe(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('${context}/manage/blog/removeBlogByMe/'+pid,null,function(data){
    });
   });
   }

  function removeBlogByOther(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('${context}/manage/blog/removeBlogByOther/'+pid,null,function(data){
    });
   });
   }


   function showComments(pid,blogOwner){
    $('#comments_'+pid).slideDown(500,function(){
       $.get('${context}/comment/list/'+blogOwner+'/'+pid,null,function(data){
           $('#commentShow_'+pid).hide();
           $('#commenthiden_'+pid).show();
           $('#comments_'+pid).html(data);
    });
   });
   }

  function hidenComments(pid){
    $('#comments_'+pid).slideUp(500,function(){
        $('#commenthiden_'+pid).hide();
        $('#commentShow_'+pid).show();
   });
   }

   function postComment(pid,blogOwner){
     var params;
   if (params == null)params = {};
   params["content"] = $('#content_'+pid).val();

     $.post('${context}/comment/postcomment/'+blogOwner+'/'+pid,params,function(data){
          //var template = '<div id="'+data.commentId+'_Item" style="width:97%"><hr style="width:97%"/><b><a href="#">'+data.user.name+'</a></b>：'+data.content+'('+data.time+')</div>';
           var template = data
           $(template).prependTo($('#'+pid+'_comment'));
   });
   }

   function delComment(pid,commentId,blogOwner){
    $('#'+commentId+'_Item').slideUp(500,function(data){
           $.post('${context}/comment/delcomment/'+blogOwner+'/'+pid+'/'+commentId,null,function(data){
    });
   });
   }

  //收藏微薄
  function collect(pid){
     var params;
     $.get('${context}/manage/collect/collectblog/'+pid,null,function(data){
         if(data)$.messager.alert('Message','Collect Success!');
   })
   }


 function playvideo(pid,flash){
     $('#videopic_'+pid).hide();
     var video = '<embed width="440" height="356" wmode="transparent" type="application/x-shockwave-flash" src="'+flash+'" quality="hight" allowfullscreen="true" flashvars="playMovie=true&amp;auto=1&amp;adss=0" pluginspage="http://get.adobe.com/cn/flashplayer/" style="visibility: visible;" allowscriptaccess="never">';
     $('#videoplay_'+pid).show();
     $('#videoflash_'+pid).html(video);

   }

 function videohide(pid){
     $('#videopic_'+pid).show();
     $('#videoflash_'+pid).html("");
     $('#videoplay_'+pid).hide();
   }

 function  showBigImg(pid,img){
   $('#pagepic_'+pid).html("<a href=javascript:showSmallImg('"+pid+"','"+img+"');><img src='"+img+"' width='500px'/></a>");
 }

  function showSmallImg(pid,img){
   $('#pagepic_'+pid).html("<a href=javascript:showBigImg('"+pid+"','"+img+"');><img src='"+img+"' width='200px'/></a>");
 }

  function  showBigPic(pid,img,bigpic){
   $('#pic_'+pid).html("<a href=javascript:showSmallPic('"+pid+"','"+img+"','"+bigpic+"');><img src='"+bigpic+"'/></a>");
 }

  function showSmallPic(pid,img,bigpic){
   $('#pic_'+pid).html("<a href=javascript:showBigPic('"+pid+"','"+img+"','"+bigpic+"');><img src='"+img+"' /></a>");
 }


 $(document).ready(function(){
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
      //$.expBlock.getRemoteExp('/blog/sina/faces');
})

  </script>



</@base.page >
