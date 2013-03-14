<#import "/templates/mod.ftl" as w/>


<div class="span-7 last" id="rightcol">


  <@w.userInfo user></@w.userInfo>

  <!--你的粉丝-->
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
	     <li><img src="${context}${f.portraitUrl}" width="50"><div><a href="${context}/u/${f.id}"/>${f.nickname}</a></div></li>
	</#list>
	</ul>
	<#if more_followers><@spring.message "and.more"/></#if>
  </div>

  <!--你关注的人-->
  <div class="span-7 box userlistDiv">
  	<h4><@spring.message "following"/>: ${following?size}</h4>

    <ul>
	<#list following as f>
       <li><img src="${context}${f.portraitUrl}" width="50"><div><a href="${context}/u/${f.id}"/>${f.nickname}</a></div></li>
	</#list>
    </ul>
	<#if more_following><@spring.message "and.more"/></#if>
  </div>

  <#if also_followed?? && (also_followed.allResults>0)>
  <!--我关注的人关注他的-->
  <div class="span-7 box userlistDiv">
  	<h4><@spring.message "follow.also"/>: ${also_followed.allResults}</h4>
    <ul>
	<#list also_followed.list as f>
       <li><img src="${context}${f.portraitUrl}" width="50"><div><a href="${context}/u/${f.id}"/>${f.nickname}</a></div></li>
	</#list>
    </ul>
	<#if more_following><@spring.message "and.more"/></#if>
  </div>
  </#if>


  <#if common_followers?? && (common_followers.allResults>0)>
  <!--我和他共同关注的人-->
  <div class="span-7 box userlistDiv">
  	<h4><@spring.message "follow.both"/>: ${common_followers.allResults}</h4>
    <ul>
	<#list common_followers.list as f>
       <li><img src="${context}${f.portraitUrl}" width="50"><div><a href="${context}/u/${f.id}"/>${f.nickname}</a></div></li>
	</#list>
    </ul>
	<#if more_following><@spring.message "and.more"/></#if>
  </div>
  </#if>

</div>


