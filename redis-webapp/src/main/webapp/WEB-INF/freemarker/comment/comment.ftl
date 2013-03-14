<#import "/spring.ftl" as spring />
   <#assign args=["${c.timeArg}"]/>
   <div id="${c.commentId}_Item" style="width:97%;overflow: hidden">
     <hr style="width:97%"/>
     <p><span style="padding-right: 10px"><img src="${c.user.portraitUrl}" width="30"/></span><b><a href="#">${c.user.nickname}</a></b>：${c.content}	(<@spring.messageArgs "${c.time}",args/>)
     <p style="align:center;width:97%"><span style="float:right;"><#if c.canDelete><a href="javascript:delComment('${pid}','${c.commentId}','${blogOwner}');">删除</a></#if></span></p>
   </div>

