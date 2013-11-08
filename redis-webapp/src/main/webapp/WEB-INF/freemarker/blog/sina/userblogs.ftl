<#import "/spring.ftl" as spring />
<#import "/blog/sina/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
  <div><a href="/blog/sina/hometimeline"/>首页</a></div>
  <div>我的微博</div>
  <div><a href="/blog/sina/follow/followers"/>我的粉丝</a></div>
  <div><a href="/blog/sina/follow/followings"/>我的关注</a></div>
  <div><a href="/blog/sina/favorites/favoritetimeline"/>我的收藏</a></div>
  <div><a href="/blog/sina/getSinaBlogs"/>定时微博</a></div>
</div>


<#include "/blog/sina/posts.ftl" />
<@w.userInfo user></@w.userInfo>
<!--你的粉丝-->
<div class="span-7 last" id="rightcol">
  <div class="span-7 box userlistDiv">
    <h4>
    <#if followers?size==1>
    <@spring.message "follower"/>
    <#else>
    <@spring.message "followers"/>: ${user.followersCount}
    </#if>
    </h4>

   <ul>
  <#list followers.users as f>
       <li><img src="${f.profileImageUrl}" width="50"><div><a href="${context}/blog/sina/friend/usertimeline/${f.id}"/>
       <#if (f.screenName?length>4)>
         ${f.screenName[0..3]?default("")}
        <#else>
         ${f.screenName?default("")}
        </#if></a></div></li>
  </#list>
  </ul>
  <#if more_followers><@spring.message "and.more"/></#if>
  </div>

  <!--你关注的人-->
  <div class="span-7 box userlistDiv">
    <h4><@spring.message "following"/>: ${user.friendsCount}</h4>

    <ul>
  <#list following.users as f>
       <li><img src="${f.profileImageUrl}" width="50"><div><a href="${context}/blog/sina/friend/usertimeline/${f.id}"/>${f.screenName}</a></div></li>
  </#list>
    </ul>
  <#if more_following><@spring.message "and.more"/></#if>
  </div>
</div>

</@base.page >
