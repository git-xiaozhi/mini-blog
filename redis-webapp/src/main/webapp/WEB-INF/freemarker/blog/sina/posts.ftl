<#import "/templates/mod.ftl" as w/>
<#assign context=rc.getContextPath()/>

<div id="posts" class="span-15 prepend-1 append-bottom">
    <#if !statusWapper??>
    <#else>
     <span id="post"><#include "/blog/sina/post.ftl" /><span>
    </#if>

</div>


 <@w.layer title="Forward Blog"  width=500 height=250></@w.layer>

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

   //转发微博
   function postStatus(pid){
     var params;
   if (params == null)params = {};
   params["content"] = $('#tcontent_'+pid).val();

   $('#w').window('close');
     $('#forwardForm').html('');

     $.post('${context}/blog/sina/repostStatus/'+pid,params,function(data){
         $('#w').window('close');
         $('#forwardForm').html('');
         $(data).prependTo($('#post'));
   })
   }

  function removeBlogByMe(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('${context}/blog/sina/removeBlogByMe/'+pid,null,function(data){
    });
   });
   }

  function removeBlogByOther(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('${context}/manage/blog/removeBlogByOther/'+pid,null,function(data){
    });
   });
   }


   function showComments(pid){
    $('#comments_'+pid).slideDown(500,function(){
       $.get('${context}/blog/sina/comment/getCommentsById/'+pid,null,function(data){
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

   function postComment(pid){
     var params;
   if (params == null)params = {};
   params["content"] = $('#content_'+pid).val();

     $.post('${context}/blog/sina/comment/postcomment/'+pid,params,function(data){
           //var template = '<div id="'+data.id+'_Item" style="width:97%"><hr style="width:97%"/><b><a href="#">'+data.user.screenName+'</a></b>：'+data.text+'('+data.time+')</div>';
           var template = data
           $(template).prependTo($('#'+pid+'_comment'));
   });
   }

   function delComment(commentId){
    $('#'+commentId+'_Item').slideUp(500,function(data){
           $.post('${context}/blog/sina/comment/delcomment/'+commentId,null,function(data){
    });
   });
   }

  //收藏微薄
  function collect(pid){
     var params;
     $.post('${context}/blog/sina/favorites/createFavorite/'+pid,null,function(data){
         if(data)$.messager.alert('Message','Collect Success!');
   })
   }

  //关注某人
  function follow(id){
     $.get('${context}/blog/sina/follow/following/'+id,null,function(data){
       if(data){
         $.messager.alert('Message','关注成功!');
        }
	  });
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

  </script>



  <script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.blog == null)mini.blog = {};

    mini.blog.list = function(params) {
       if (params == null)params = {};
       $.get('${context}/blog/sina/timeLinepage/page',params,function(data){
            $('#post').html(data);
       })
    }

    mini.blog.managelist = function(params) {
       if (params == null)params = {};
       $.get('${context}/blog/sina/usertimeline/page',params,function(data){
            $('#post').html(data);
       })
    }

  mini.blog.friendblogs = function(params) {
       if (params == null)params = {};
       $.get('${context}/blog/sina/friend/usertimeline/page/${uid}',params,function(data){
            $('#post').html(data);
       })
    }

    mini.blog.mentionlist = function(params) {
       if (params == null)params = {};
       $.get('${context}/u/mentions/page',params,function(data){
            $('#post').html(data);
       })
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
      $.expBlock.getRemoteExp('/blog/sina/faces');
})

 </script>
