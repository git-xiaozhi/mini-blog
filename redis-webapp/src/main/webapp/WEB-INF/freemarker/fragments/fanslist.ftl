<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
<div id="posts" class="span-15 prepend-1 append-bottom">
 <#list pages.list as fan>
   <div class="post" id="fan_${fan.id}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${context}${fan.portraitUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
     <div>
      <a class="user" href="${context}/u/${fan.id}"><b>${fan.nickname}</b></a>
      <br/>
      <!-- link to post -->
      <div>
       <#if fan.getLink()>
        <span id="followed_${fan.id}" style="float:right">已相互关注</span>
       <#else>
        <span id="followed_${fan.id}" style="float:right;display:none">已相互关注</span>
        <span id="follow_${fan.id}" style="float:right;display:''"><a href="javascript:follow('${fan.id}');">关注</a></span>
       </#if>
      ${fan.school}&nbsp;&nbsp;&nbsp;&nbsp;${fan.company}&nbsp;&nbsp;&nbsp;&nbsp;粉丝： ${fan.followerNum}
	  </div>
    </div>
    </td>
    </tr></table>
   </div>
  </#list>
   <div style="text-align: center;padding-top: 15px;">
     <@w.ajaxPageBar listPage=pages pageMethod="mini.fans.list" params="{}"/>
   </div>
 </div>
