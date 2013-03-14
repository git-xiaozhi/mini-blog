<#import "/templates/mod.ftl" as w/>
<#assign context=rc.getContextPath()/>


<div id="posts" class="span-15 prepend-1 append-bottom">
    <#if !favoritesWapper??>
    <#else>
     <span id="post"><#include "/blog/sina/favoritespage.ftl" /><span>
    </#if>

</div>


 <@w.layer title="Forward Blog"  width=500 height=250></@w.layer>

 <script type="text/javascript">

    function openWindow(pid){
        var offset = $('#forward_'+pid).offset();

        $.get('${context}/blog/showForwardForm/'+pid,null,function(data){
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

     $.post('${context}/blog/forwardBlog/'+pid,params,function(data){
         $('#w').window('close');
         $('#forwardForm').html('');
         $(data).prependTo($('#post'));
   })
   }

  function removeBlogByMe(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('${context}/blog/sina/favorites/removeFavorite/'+pid,null,function(data){
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
       $.get('${context}/blog/sina/favorites/favorites/page',params,function(data){
            $('#post').html(data);
       })
    }


 </script>
