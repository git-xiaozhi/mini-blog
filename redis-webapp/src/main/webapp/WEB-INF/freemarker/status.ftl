<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page>
<div class="span-15" id="maincol">
<#include "/fragments/posts.ftl" />
</div>
</@base.page>
<!-- 
<c:if test="${!loggedIn}">
 <div class="span-7 last" id="rightcol">
  <a href="signIn"><fmt:message key="signin"/></a>
  </div>
</c:if>
<c:if test="${!loggedIn}">
 <div class="span-7" id="rightcol">
  <a href="timeline"><fmt:message key="timeline"/></a>
  </div>
</c:if>
 -->