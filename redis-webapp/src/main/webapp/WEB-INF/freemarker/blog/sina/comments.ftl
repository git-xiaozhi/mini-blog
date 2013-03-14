<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
<div id="${pid}" style="width:97%;background:#F3F3F3;padding-left:3%">
   <div class="exp-holder" id="holder" style="width:100%;white-space:nowrap;">
     <a class="exp-block-trigger" id="face" holder="#holder" trigger="#face" href="javascript:void(0);">表情</a>
     <input class="text" type="text" name="content_${pid}" id="content_${pid}" value="" style="width:60%;height:30px"/>
     <input type="button" onclick="postComment('${pid}');" value="Post">
   </div>
  <div id="${pid}_comment">
  <#if comments??>
    <#list comments as c>
       <div id="${c.id}_Item" style="width:97%;overflow: hidden">
         <hr style="width:97%"/>
         <p><span style="padding-right: 10px"><img src="${c.user.profileImageUrl}" width="30"/></span><b><a href="#">${c.user.screenName}</a></b>：${c.text}	(${c.createdAt?string('yyyy-MM-dd hh:mm')})
         <p style="align:center;width:97%"><span style="float:right;"><#if c.canDelete><a href="javascript:delComment('${c.id}');">删除</a></#if></span></p>
       </div>
    </#list>
  </#if>
  <div>
</div>
