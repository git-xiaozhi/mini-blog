<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
<div id="${pid}" style="width:97%;background:#F3F3F3;padding-left:3%">
   <div class="exp-holder" id="holder" style="width:100%;white-space:nowrap;">
     <a class="exp-block-trigger" id="face" holder="#holder" trigger="#face" href="javascript:void(0);" style="padding-right: 5px;">表情</a>
     <input class="text" type="text" name="content_${pid}" id="content_${pid}" value="" style="width:70%;height:30px"/>
     <input type="button" onclick="postComment('${pid}','${blogOwner}');" value="Post">
   </div>

  <div id="${pid}_comment">
  <#if comments??>
    <#list comments as c>
       <#assign args=["${c.timeArg}"]/>
       <div id="${c.commentId}_Item" style="width:97%;overflow: hidden">
         <hr style="width:97%"/>
         <p><span style="padding-right: 10px"><img src="${context}${c.user.portraitUrl}" width="30"/></span><b><a href="#">${c.user.nickname}</a></b>：${c.content}	(<@spring.messageArgs "${c.time}",args/>)
         <p style="align:center;width:97%"><span style="float:right;"><#if c.canDelete><a href="javascript:delComment('${pid}','${c.commentId}','${blogOwner}');">删除</a></#if></span></p>
       </div>
    </#list>
  </#if>
  <div>
</div>
