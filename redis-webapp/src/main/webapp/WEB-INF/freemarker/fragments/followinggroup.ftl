<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>

 <#list following as f>
   <div class="post" id="fan_${f.id}">
    <hr/>
    <table>
    <tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${context}${f.portraitUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
     <div>
      <a class="user" href="${context}/u/${f.id}"><b>${f.nickname}</b></a>
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


