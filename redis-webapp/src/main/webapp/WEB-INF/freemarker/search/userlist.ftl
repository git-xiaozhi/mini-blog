<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
<#if pages??>
 <#list pages.list as f>
   <div class="post" id="fan_${f.id}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${context}${f.portraitUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
     <div>
      <a class="user" href="${context}/u/${f.id}"><b>${f.nickname}</b></a>
      <br/>
      <!-- link to post -->
      <div>
       <#if f.getLink()>
        <span style="float:right" id="followed_${f.id}">Linked</span>
       <#else>
        <span style="float:right" id="followed_${f.id}"><a href="javascript:following('${f.id}');">加关注</a></span>
       </#if>
        ${f.school}&nbsp;&nbsp;&nbsp;&nbsp;${f.company}&nbsp;&nbsp;&nbsp;&nbsp;Follower ${f.followerNum}
      </div>
     </div>
    </tr></table>
   </div>
  </#list>

   <div style="text-align: center;padding-top: 15px;">
     <@w.ajaxPageBar listPage=pages pageMethod="mini.search.list" params="{keyword:'${keyword}'}"/>
   </div>
</#if>
