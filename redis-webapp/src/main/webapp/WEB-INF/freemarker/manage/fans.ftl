<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
</div>
<span id="post"><#include "/fragments/fanslist.ftl" /></span>

<!--你的粉丝-->
<div class="span-7 last" id="rightcol">
  <div class="span-7 box">
  	<h4>
  	<#if followers?size==1>
  	<@spring.message "follower"/>
  	<#else>
  	<@spring.message "followers"/>: ${followers?size}
  	</#if>
  	</h4>

   <ul class="user-list">
	<#list followers as f>
	     <li><a href="!${f.name}"/>${f.nickname}</a></li>
	</#list>
	</ul>
	<#if more_followers><@spring.message "and.more"/></#if>
  </div>

  <!--你关注的人-->
  <div class="span-7 box">
  	<h4><@spring.message "following"/>: ${following?size}</h4>

    <ul class="user-list">
	<#list following as f>
       <li><a href="!${f.name}"/>${f.nickname}</a></li>
	</#list>
    </ul>
	<#if more_following><@spring.message "and.more"/></#if>
  </div>
</div>

 <script type="text/javascript">

  function follow(id){
     $.get('${context}/manage/following/follow/'+id,null,function(data){
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
	     $.get('${context}/manage/follower/fans/page',params,function(data){
            $('#post').html(data);
	     })
		}

 </script>

</@base.page >
