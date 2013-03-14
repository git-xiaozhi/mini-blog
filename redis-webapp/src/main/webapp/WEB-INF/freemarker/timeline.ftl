<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<div class="span-15 prepend-1">
  <@spring.message "all.posts"/>
</div>
<br/>
<h2>user : ${user}</h2>
<#--
<#include "/fragments/posts.ftl" />-->

<#--
<div class="span-7 last" id="rightcol">
  <div class="span-7 box">
    <h4>Users on Blog</h4>
    <#list users as u>
      <a href="!${u}">${u}</a>
    </#list>
  </div>

  <div class="span-7 box">
    <h4>Users on Blog & TianJi</h4>
    <#list tjusers as u>
     <table  style="margin-bottom:0em" border=0>
      <tr><td><table><tr><td><img src="${u.picture_small}"/></td></tr></table></td></tr>
      <tr><td><a href="!${u.member}">${u.member}</a>(<a href="${u.link}" target="_blank">${u.name}</a>)</td></tr>
     </table>
    </#list>
  </div>
</div>
-->
</@base.page>