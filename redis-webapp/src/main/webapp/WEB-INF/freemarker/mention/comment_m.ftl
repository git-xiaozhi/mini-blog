<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
  <#list comments.list as p>
   <div class="post" id="blog_${p.commentId}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><#if p.user??><div><img src="${context}${p.user.portraitUrl}" width="50"></div></#if></td>
     <td style="vertical-align: top;">
      <div>
      <#if p.user??>
      <div id="blogcontent_${p.commentId}" style="padding-bottom:10px"><a class="user" href="${context}/u/${p.user.id}"><b>${p.user.nickname}</b></a> ${p.content}</div>
        <#if p.webPost??>
         <div id="target_${p.commentId}" style="padding-bottom:10px">评论&nbsp;<a class="user" href="${context}/u/${p.user.id}"><b>${p.webPost.user.nickname}</b></a>&nbsp;的微博: “${p.webPost.content}”</div>
        <#else>
         此微博已经被作者删除！
        </#if>
      <#else>
       此评论已经被删除！
      </#if>
      <!-- link to post -->
      <div id="forward_${p.commentId}">
      <span id="commentShow_${p.commentId}" style="float:right;display:''">
      <#if p.user??>
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
     <@w.ajaxPageBar listPage=comments pageMethod="mini.blog.commentsmentionlist" params="{}"/>
   </div>

