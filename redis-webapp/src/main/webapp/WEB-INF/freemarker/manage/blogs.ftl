<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
</div>
<#include "/fragments/posts.ftl" />
<@w.userInfo user></@w.userInfo>
<!--你的粉丝-->
<div class="span-7 last" id="rightcol">
  <div class="span-7 box userlistDiv">
    <h4>
    <#if followers?size==1>
    <@spring.message "follower"/>
    <#else>
    <@spring.message "followers"/>: ${followers?size}
    </#if>
    </h4>

   <ul>
  <#list followers as f>
       <li><img src="${context}${f.portraitUrl}" width="50"><div><a href="${f.id}"/>${f.nickname}</a></div></li>
  </#list>
  </ul>
  <#if more_followers><@spring.message "and.more"/></#if>
  </div>

  <!--你关注的人-->
  <div class="span-7 box userlistDiv">
    <h4><@spring.message "following"/>: ${following?size}</h4>

    <ul>
  <#list following as f>
       <li><img src="${context}${f.portraitUrl}" width="50"><div><a href="${f.id}"/>${f.nickname}</a></div></li>
  </#list>
    </ul>
  <#if more_following><@spring.message "and.more"/></#if>
  </div>
</div>

</@base.page >
