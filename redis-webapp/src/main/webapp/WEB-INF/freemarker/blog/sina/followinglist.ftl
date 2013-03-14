<#import "/blog/sina/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
<div id="posts" class="span-15 prepend-1 append-bottom">
 <#list users.users as fan>
   <div class="post" id="fan_${fan.id}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${fan.profileImageUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
     <div>
      <a class="user" href="${context}/blog/sina/friend/usertimeline/${fan.id}"><b>${fan.screenName}</b></a>
      <br/>
      <!-- link to post -->
      <div>

        <span id="follow_${fan.id}" style="float:right;display:''"><a href="javascript:stopfollowing('${fan.id}');">取消关注</a></span>
      <#if fan.followMe>
        <span id="followed_${fan.id}" style="float:right"><img title="相互关注" src="/static/images/th.jpeg" height="17" width="25"/></span>
       </#if>
        <div>关注 <font color="blue">${fan.friendsCount}</font>&nbsp;&nbsp粉丝 <font color="blue">${fan.followersCount}</font>&nbsp;&nbsp;微博 <font color="blue">${fan.statusesCount}</font></div>
        <#if fan.status??><div id="" style="width:97%;background:#F3F3F3;padding-left:3%">${fan.status.text}</div></#if>
	  </div>
    </div>
    </td>
    </tr></table>
   </div>
  </#list>
   <div style="text-align: center;padding-top: 15px;">
     <@w.ajaxPageBar listPage=page pages=pages pageMethod="mini.fans.list" params="{}"/>
   </div>
 </div>
