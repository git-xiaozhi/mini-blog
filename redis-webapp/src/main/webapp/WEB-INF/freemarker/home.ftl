<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>
<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
</div>
<div class="span-15 prepend-1">
 <#if userRoleHelper.getUserAuthUser().uid==targetUid>
 <form>
  <div id="updateform" class="box">
      <input type="hidden" name="video.title" id="videotitle"/>
      <input type="hidden" name="video.flash" id="videoflash"/>
      <input type="hidden" name="video.pic" id="videopic"/>
      <input type="hidden" name="video.htmlpath" id="videohtmlpath"/>

      <input type="hidden" name="page.title" id="pagetitle"/>
      <input type="hidden" name="page.url" id="pageurl"/>
      <input type="hidden" name="page.img" id="pageimg"/>
      <input type="hidden" name="pic" id="pic"/>


      <div class="exp-holder" >

       <textarea class="text" name="content" id="content" rows="3" cols="60" style="margin: 0 auto; width: 580px; "></textarea>
        <span style="float:right;display:''"><input type="button" class="blogsubmit" value="<@spring.message "update"/>"/></span>
        <a class="exp-block-trigger" href="javascript:void(0);" style="padding-right: 5px;">表情</a>

        <a class="layer-trigger" href="javascript:$.layer.showLayer('${context}/videolayer','视频');" id="showVideourl" style="padding-right: 5px;" >Video</a>
        <a class="layer-trigger" href="javascript:$.layer.showLayer('${context}/pagelayer','网址');" id="showPageurl" style="padding-right: 5px;">Page</a>
        <a class="layer-trigger" href="javascript:showimageLayer('图片');" id="showImgurl" style="padding-right: 5px;">Image</a>

      </div>

  </div>
 </form>

 <#else>
    <#if userRoleHelper.getUserAuthUser()??>
      <#include "/fragments/userFollow.ftl" />
    </#if>
  </#if>
<#if userRoleHelper.getUserAuthUser()??><#include "/fragments/posts.ftl" /></#if>

</div><!--<div class="span-15 prepend-1"> end-->


<#if userRoleHelper.getUserAuthUser()??><#include "/fragments/network.ftl" /></#if>


<script type="text/javascript">

var imgArray;

$(function(){
 //发微博
 $('.blogsubmit').live('click',function(){
   var params;
   if (params == null)params = {};
   params=$("form").serializeArray();
     $.post('${context}/u/${userRoleHelper.getUserAuthUser().uid}/post',params,function(data){
       $(data).prependTo($('#post'));
       $("#updateform").load("/postform");
    });
  });
})

function getVideInfo(){
  var params;
  if (params == null)params = {};
  params["url"] = $('#videourl').val();

    $.post('${context}/blog/getVideoInfo',params,function(data){
           $('#videotitle').val(data.title);
           $('#videoflash').val(data.flash);
           $('#videopic').val(data.pic);
           $('#videohtmlpath').val(data.htmlpath);
           $('#content').val(data.title);
   });
   }

 function getPageInfo(){
    var params;
    if (params == null)params = {};
    params["url"] = $('#pagebutton').val();

    $.post('${context}/blog/getPageInfo',params,function(data){
           if(data.title!=null && data.title!=''){
             $('#pagetitle').val(data.title);
             $('#content').val(data.title);
           }
           $('#pageurl').val(data.url);
           if(data.imgs!=null && data.imgs!=''){
             imgArray = $.makeArray(data.imgs);
             if(imgArray.length>1){
              $('#imgs').html('<img src="'+imgArray[0]+'" width="200px" /><p><a href="javascript:nextpage(1);">>></a></p>');
              $('#pageimg').val(imgArray[0]);
             }else if(imgArray){
              $('#imgs').html('<img src="'+imgArray[0]+'" width="200px" />');
              $('#pageimg').val(imgArray[0]);
             }
           }
   });
   }


 function nextpage(page){
   if(page>0 && page<imgArray.length-1){
    $('#imgs').html('<img src="'+imgArray[page]+'" width="150px" /><div style="text-align:center"><a href="javascript:nextpage('+(page-1)+');"><<</a> | <a href="javascript:nextpage('+(page+1)+');">>></a></div>');
   }else if(page==0){
    $('#imgs').html('<img src="'+imgArray[page]+'" width="150px" /><div style="text-align:center"> | <a href="javascript:nextpage('+(page+1)+');">>></a></div>');
   }else if(page==imgArray.length-1){
    $('#imgs').html('<img src="'+imgArray[page]+'" width="150px" /><div style="text-align:center"><a href="javascript:nextpage('+(page-1)+');"><<</a> | ');
   }
   $('#pageimg').val(imgArray[page]);
 }

function showimageLayer(title){

   $.get('${context}/imagelayer',null,function(data){
    $('.main').html(data);
    $.layer.setTitle(title);
    $('.file_upload').uploadify({
    'uploader'  : '/static/js/upload/uploadify.swf',
    'script'    : '${context}/blog/upload;jsessionid=${sessionId}',
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
