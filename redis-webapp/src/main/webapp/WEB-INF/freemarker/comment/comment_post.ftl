<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
  <#list comments.list as p>
   <div class="post" id="blog_${p.commentId}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><#if p.webPost??><div><img src="${p.webPost.user.portraitUrl}" width="50"></div></#if></td>
     <td style="vertical-align: top;">
      <div>
      <div id="blogcontent_${p.commentId}" style="padding-bottom:10px"> ${p.content}</div>

        <div id="target_${p.commentId}" style="padding-bottom:10px">
        <#if p.webPost??>
        评论&nbsp;<a class="user" href="/u/${p.user.id}"><b>${p.webPost.user.nickname}</b></a>&nbsp;的微博: “${p.webPost.content}”
        <#else>
        此微博已经被作者删除！
        </#if>
        </div>

      <!-- link to post -->

      <div id="forward_${p.commentId}">
      <span id="commentShow_${p.commentId}" style="float:right;display:''">
      <#if p.webPost??>
        <a href="javascript:delComment('${p.webPost.pid}','${p.commentId}','<#if p.webPost.user??>${p.webPost.user.id}</#if>');">删除</a>&nbsp;
      <#else>
        <a href="javascript:delCommentForNoBlog('${p.commentId}');">删除</a>&nbsp;
      </#if>
      </span>
      <#if p.timeArg??>
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
     <@w.ajaxPageBar listPage=comments pageMethod="mini.blog.postcommentslist" params="{}"/>
   </div>

