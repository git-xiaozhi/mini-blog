<#import "/spring.ftl" as spring />
<#import "/blog/sina/mod.ftl" as base/>
<@base.page >
<#assign context=rc.getContextPath()/>

<div class="span-7 box userlistDiv" style="width:100px;">
  <div>首页</div>
  <div><a href="/blog/sina/usertimeline"/>我的微博</a></div>
  <div><a href="/blog/sina/follow/followers"/>我的粉丝</a></div>
  <div><a href="/blog/sina/follow/followings"/>我的关注</a></div>
  <div><a href="/blog/sina/favorites/favoritetimeline"/>我的收藏</a></div>
</div>

<div class="span-15 prepend-1">
 <form>
  <div id="updateform" class="box">

      <input type="hidden" name="pic" id="pic"/>

      <div class="exp-holder" >
       <textarea class="text" name="content" id="content" rows="3" cols="60" style="margin: 0 auto; width: 580px; "></textarea>
        <span style="float:right;display:''"><input type="button" class="blogsubmit" value="<@spring.message "update"/>"/></span>
        <a class="exp-block-trigger" href="javascript:void(0);" style="padding-right: 5px;">表情</a>
        <a class="layer-trigger" href="javascript:showimageLayer('图片');" id="showImgurl" style="padding-right: 5px;">Image</a>
      </div>

  </div>
 </form>
<#if userRoleHelper.getUserAuthUser()??><#include "/blog/sina/posts.ftl" /></#if>

</div><!--<div class="span-15 prepend-1"> end-->

<div class="span-7 last" id="rightcol">

  <div class="span-7 box userlistDiv">
  	<h4>right</h4>
    <ul>
	  <li>----------------------------</li>

	</ul>
  </div>
</div>



<script type="text/javascript">

var imgArray;

$(function(){
 //发微薄
 $('.blogsubmit').live('click',function(){
   var params;
   if (params == null)params = {};
   params=$("form").serializeArray();
     $.post('${context}/blog/sina/updateStatus',params,function(data){
       $(data).prependTo($('#post'));
       //$("#updateform").load("/postform");
    });
  });
})


function showimageLayer(title){

   $.get('${context}/imagelayer',null,function(data){
    $('.main').html(data);
    $.layer.setTitle(title);
    $('.file_upload').uploadify({
    'uploader'  : '/static/js/upload/uploadify.swf',
    'script'    : '${context}/blog/sina/upload;jsessionid=${sessionId}',
    'cancelImg' : '/static/js/upload/cancel.png',
    'fileDataName' : 'filename',
    'fileExt'        : '*.jpg;*.gif;*.png',
    'fileDesc'       : 'Image Files (.JPG, .GIF, .PNG)',
    //'sizeLimit' : '1024000',
    'auto'      : true,
    'onError'     : function (event,ID,fileObj,errorObj) {
      alert(errorObj.type + ' Error: ' + errorObj.info);
     },
    'onComplete'  : function(event, ID, fileObj, response, data) {
      $('#returnPic').html('<img src="'+response+'"/>');
      $('#content').val('分享图片');
      $('#pic').val(response);
     }
  });
 })
}

</script>


</@base.page >
