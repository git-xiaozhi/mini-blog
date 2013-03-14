<#import "/spring.ftl" as spring />
<#import "/blog/sina/mod.ftl" as base/>
<@base.page >
<#assign context=rc.getContextPath()/>

<div class="span-7 box userlistDiv" style="width:100px;">
  <div><a href="/blog/sina/hometimeline"/>首页</a></div>
  <div><a href="/blog/sina/usertimeline"/>我的微博</a></div>
  <div><a href="/blog/sina/follow/followers"/>我的粉丝</a></div>
  <div><a href="/blog/sina/follow/followings"/>我的关注</a></div>
  <div>我的收藏</div>
</div>

<div class="span-15 prepend-1">

<#if userRoleHelper.getUserAuthUser()??><#include "/blog/sina/favorites.ftl" /></#if>

</div><!--<div class="span-15 prepend-1"> end-->

<div class="span-7 last" id="rightcol">

  <div class="span-7 box userlistDiv">
  	<h4>right</h4>
    <ul>
	  <li>----------------------------</li>

	</ul>
  </div>
</div>

</@base.page >
