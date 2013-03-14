<#import "/spring.ftl" as spring />
   <div id="${c.id}_Item" style="width:97%;overflow: hidden">
     <hr style="width:97%"/>
     <p><span style="padding-right: 10px"><img src="${c.user.profileImageUrl}" width="30"/></span><b><a href="#">${c.user.screenName}</a></b>：${c.text}	(${c.createdAt?string('yyyy-MM-dd hh:mm')})
     <p style="align:center;width:97%"><span style="float:right;"><#if c.canDelete><a href="javascript:delComment('${c.id}');">删除</a></#if></span></p>
   </div>

