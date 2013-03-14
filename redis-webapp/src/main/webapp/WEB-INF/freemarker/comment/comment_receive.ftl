<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
  <#list comments.list as p>
   <div class="post" id="blog_${p.commentId}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><#if p.user??><div><img src="${p.user.portraitUrl}" width="50"></div></#if></td>
     <td style="vertical-align: top;">
      <div>
      <div id="blogcontent_${p.commentId}" style="padding-bottom:10px"><#if p.user??><a class="user" href="/u/${p.user.id}"><b>${p.user.nickname}</b></a></#if> ${p.content}</div>
      <#if p.webPost??>
        <div id="target_${p.commentId}" style="padding-bottom:10px">评论&nbsp;我&nbsp;的微博: “${p.webPost.content}”</div>
      <#else>
        此微博已经被作者删除！
      </#if>
      <!-- link to post -->
      <div id="forward_${p.commentId}">
      <#if p.user??>
       <span id="commentShow_${p.commentId}" style="float:right;display:''">
        <a href="javascript:showComments('${p.commentId}','${p.user.id}');">回复</a>&nbsp;
       </span>
       <#assign args=["${p.timeArg}"]/>
       <a href="/status?pid=${p.commentId}"><@spring.messageArgs "${p.time}",args/></a>
      </#if>
    </div>

    <div id="comments_${p.commentId}"></div>
    </div>
    </td>
    </tr></table>
   </div>
  </#list>

   <div style="text-align: center;padding-top: 15px;">
     <@w.ajaxPageBar listPage=comments pageMethod="mini.blog.receivecommentslist" params="{}"/>
   </div>

