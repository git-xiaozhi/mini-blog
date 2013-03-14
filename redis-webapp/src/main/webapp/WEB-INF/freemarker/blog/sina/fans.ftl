<#import "/spring.ftl" as spring />
<#import "/blog/sina/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
  <div><a href="/blog/sina/hometimeline"/>首页</a></div>
  <div><a href="/blog/sina/usertimeline"/>我的微博</a></div>
  <div>我的粉丝</div>
  <div><a href="/blog/sina/follow/followings"/>我的关注</a></div>
  <div><a href="/blog/sina/favorites/favoritetimeline"/>我的收藏</a></div>
</div>

<span id="post"><#include "/blog/sina/fanslist.ftl" /></span>


<div class="span-7 last" id="rightcol">
  <div class="span-7 box userlistDiv">
  	<h4>right</h4>
    <ul>
	  <li>----------------------------</li>

	</ul>
  </div>
</div>

 <script type="text/javascript">

  function follow(id){
     $.get('${context}/blog/sina/follow/following/'+id,null,function(data){
       if(data){
         $('#follow_'+id).hide();
         $('#followed_'+id).show();
        }
	  });
   }

  </script>


 <script type="text/javascript">

		if (mini == null)var mini = {};
		if (mini.fans == null)mini.fans = {};

		mini.fans.list = function(params) {
	     if (params == null)params = {};
	     $.get('${context}/blog/sina/follow/followers/page',params,function(data){
            $('#post').html(data);
	     })
		}

 </script>

</@base.page >
