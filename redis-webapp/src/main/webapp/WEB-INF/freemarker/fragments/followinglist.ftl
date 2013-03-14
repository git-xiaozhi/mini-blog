<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>

 <#list pages.list as f>
   <div class="post" id="fan_${f.id}">
    <hr/>
    <table>
    <tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${context}${f.portraitUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
     <div>
      <a class="user" href="${context}/u/${f.id}"><b>${f.nickname}</b></a>
      <span id="showGroup_${f.id}" style="float:right"><a href="javascript:showGroup('${f.id}');" style="float:right"> <font color="red"> + </font></a></span>
      <span id="groups_${f.id}" style="float:right">
       <#list f.groups as g>
        <span id="ug_${g.groupId}_${f.id}" style="background-color:#DFDFDF;padding:1px;border:1px solid #ccc;">
          ${g.groupName}<a href="javascript:pullGroup('${f.id}','${g.groupId}');"> <font color="red"> x </font></a>
        </span>
        <span style="padding:2px;"></span>
       </#list>
      </span>
      <br/>
      <!-- link to post -->
      <div>
       <#if f.getLink()>
        <span id="followed_${f.id}" style="float:right">已相互关注</span>
       </#if>
        ${f.school}&nbsp;&nbsp;&nbsp;&nbsp;${f.company}&nbsp;&nbsp;&nbsp;&nbsp;粉丝： ${f.followerNum}
	  </div>
     </div>
    <div id=""><span id="follow_${f.id}" style="float:right;display:''"><a href="javascript:stopFollowing('${f.id}');">取消关注</a></span> </div>
    </tr></table>
   </div>
  </#list>

   <div style="text-align: center;padding-top: 15px;">
     <@w.ajaxPageBar listPage=pages pageMethod="mini.following.list" params="{}"/>
   </div>


