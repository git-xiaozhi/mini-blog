<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
</div>
<div id="posts" class="span-15 prepend-1 append-bottom">
  <span id="post"><#include "/fragments/collects.ftl" /></span>
</div>

<!--你的粉丝-->
<div class="span-7 last" id="rightcol">
  <div class="span-7 box userlistDiv">
  <#if followers??>
  	<h4>
  	<#if followers?? && followers?size==1>
  	<@spring.message "follower"/>
  	<#else>
  	<@spring.message "followers"/>: ${followers?size}
  	</#if>
  	</h4>

   <ul>
	<#list followers as f>
	     <li><img src="${context}${f.portraitUrl}" width="50"><a href="${f.name}"/>${f.name}</a></li>
	</#list>
	</ul>
	<#if more_followers><@spring.message "and.more"/></#if>
	</#if>
  </div>

  <!--你关注的人-->
  <div class="span-7 box userlistDiv">
    <#if followers??>
  	<h4><@spring.message "following"/>: ${following?size}</h4>

    <ul>

	<#list following as f>
       <li><img src="${context}${f.portraitUrl}" width="50"><a href="${f.name}"/>${f.name}</a></li>
	</#list>
    </ul>
	<#if more_following><@spring.message "and.more"/></#if>
	</#if>
  </div>
</div>


 <div id="transmitForm"></div>

 <script type="text/javascript">

    function openWindow(pid){

        $.get('/blog/showForwardForm/'+pid,null,function(data){
             $('#transmitForm').html(data);
             $('#tcontent').html($('#blogcontent_'+pid).html());
             $('#w').window({
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
     $.post('/blog/forwardBlog/'+pid,params,function(data){
         if(data)alert('Transmit success!!');
	 })
   }

  function removeBlogByMe(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('/manage/blog/removeBlogByMe/'+pid,null,function(data){
		});
	 });
   }

  function removeBlogByOther(pid){
    $('#blog_'+pid).slideUp(500,function(data){
           $.post('/manage/blog/removeBlogByOther/'+pid,null,function(data){
		});
	 });
   }


   function showComments(pid){
    $('#comments_'+pid).slideDown(500,function(){
       $.get('/comment/list/'+pid,null,function(data){
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

     $.post('/comment/postcomment/'+pid,params,function(data){
           var template = '<div id="'+data.commentId+'_Item" style="width:97%"><hr style="width:97%"/><b><a href="#">'+data.user.name+'</a></b>：'+data.content+'('+data.time+')</div>';
           $(template).prependTo($('#'+pid+'_comment'));
	 });
   }

   function delComment(pid,commentId){
    $('#'+commentId+'_Item').slideUp(500,function(data){
           $.post('/comment/delcomment/'+pid+'/'+commentId,null,function(data){
		});
	 });
   }


   //取消收藏微薄
  function removeCollect(pid){
     var params;
     $('#blog_'+pid).slideUp(500,function(data){
       $.get('/manage/collect/removecllect/'+pid,null,function(data){

	   })
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

  </script>


  <script type="text/javascript">

		if (mini == null)var mini = {};
		if (mini.blog == null)mini.blog = {};

		mini.blog.list = function(params) {
	     if (params == null)params = {};
	     $.get('/manage/collect/blogs/page',params,function(data){
            $('#post').html(data);
	     })
		}

 </script>

</@base.page >
