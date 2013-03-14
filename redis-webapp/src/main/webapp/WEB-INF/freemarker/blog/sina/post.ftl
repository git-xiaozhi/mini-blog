<#import "/blog/sina/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>

 <#list statusWapper.statuses as p>
   <div class="post" id="blog_${p.id}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${context}${p.user.profileImageUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
      <div>
      <div id="blogcontent_${p.id}"><a class="user" href="${context}/blog/sina/friend/usertimeline/${p.user.id}"><b>${p.user.screenName}</b></a>&nbsp;${p.text}</div>

      <#if p.thumbnailPic?? && p.thumbnailPic!=null && p.thumbnailPic!='null'>
       <div id="pic_${p.id}" style="padding-top: 15px;"><a href="javascript:showBigPic('${p.id}','${p.thumbnailPic}','${p.bmiddlePic}');"><img src="${p.thumbnailPic}"/></a></div>
      </#if>

      <br/>
      <!-- 转发内容 -->
      <#if p.retweetedStatus?? && p.retweetedStatus.text?has_content><@w.transmitblog p.retweetedStatus p></@w.transmitblog><br/></#if>

      <!-- link to post -->
      <div id="forward_${p.id}">
      <span id="commentShow_${p.id}" style="float:right;display:''">
        <a href="javascript:showComments('${p.id}','${p.user.id}');">评论(${p.commentsCount})</a>&nbsp;
      </span>
      <span id="commenthiden_${p.id}" style="float:right;display:none">
        <a href="javascript:hidenComments('${p.id}');">评论(${p.commentsCount})</a>&nbsp;
      </span>
      <span id="" style="float:right">
        <a href="javascript:collect('${p.id}');">收藏</a>&nbsp;
      </span>
      <span id="commentShow_${p.id}" style="float:right">
       <a href="javascript:openWindow('${p.id}');">转发(${p.repostsCount})</a>
       <#if pagelist=='manage'><a href="javascript:removeBlogByMe('${p.id}');">删除</a></#if>&nbsp;
      </span>

      ${p.createdAt?string('yyyy-MM-dd hh:mm')}
    </div>
    <div id="comments_${p.id}"></div>
    </div>
    </td>
    </tr></table>
   </div>
  </#list>



   <div style="text-align: center;padding-top: 15px;">
    <#if pagelist=='manage'>
     <@w.ajaxPageBar listPage=page pages=pages pageMethod="mini.blog.managelist" params="{}"/>
    <#elseif pagelist=='friendblogs'>
     <@w.ajaxPageBar listPage=page pages=pages pageMethod="mini.blog.friendblogs" params="{}"/>
    <#elseif pagelist=='mention'>
     <@w.ajaxPageBar listPage=page pages=pages pageMethod="mini.blog.mentionlist" params="{}"/>
    <#elseif pagelist=='blogsearch'>
     <@w.ajaxPageBar listPage=page pages=pages pageMethod="mini.search.list" params="{keyword:'${keyword}'}"/>
    <#else>
     <@w.ajaxPageBar listPage=page pages=pages pageMethod="mini.blog.list" params="{}"/>
    </#if>
   </div>

