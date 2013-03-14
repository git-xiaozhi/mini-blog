<#assign context=rc.getContextPath()/>
<div class="box">
	  <#if follows>
	    <a href="${context}/u/${user.id}/stopfollowing"><@spring.message "follow.stop"/></a>
	  <#else>
	    <a href="${context}/u/${user.id}/follow"><@spring.message "follow"/></a>
	  </#if>
	<#if !no_mentions> | <a href="${context}/u/${user.id}/mentions"><@spring.message "Mentions"/></a></#if>
</div>





