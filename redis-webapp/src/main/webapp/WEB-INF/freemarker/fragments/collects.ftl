<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
   <#list pages.list as p>
   <#if p.user?? && p.user.name??>
   <div class="post" id="blog_${p.pid}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><div><img src="${context}${p.user.portraitUrl}" width="50"></div></td>
     <td style="vertical-align: top;">
      <div id="blogcontent_${p.pid}"><a class="user" href="${context}/u/${p.user.id}"><b>${p.user.nickname}</b></a> ${p.content}</div>
      <#if p.video?? && p.video.pic?has_content>
       <div id="videopic_${p.pid}" style="padding-top: 15px;"><a href="javascript:playvideo('${p.pid}','${p.video.flash}');"><img src="${p.video.pic}"/></a></div>

       <div id="videoplay_${p.pid}" style="display:none;background:none repeat scroll 0 0 #F3F3F3;padding-left: 5%;padding-bottom: 15px;padding-top: 10px;width:80%">
       <p><a href="javascript:videohide('${p.pid}');">收起</a>&nbsp;&nbsp;&nbsp;&nbsp;
          <a href="${p.video.htmlpath}" target="_blank">${p.video.title}</a>
       </p>
       <div id="videoflash_${p.pid}"></div>
       </div>
      </#if>

      <#if p.page?? && p.page.img?has_content>
       <div id="pagepic_${p.pid}" style="padding-top: 15px;"><a href="javascript:showBigImg('${p.pid}','${p.page.img}');"><img src="${p.page.img}" width="200px"/></a></div>
      </#if>

      <#if p.pic?? && p.pic!=null && p.pic!='null'>
       <div id="pic_${p.pid}" style="padding-top: 15px;"><a href="javascript:showBigPic('${p.pid}','${context}${p.pic}','${context}${p.bigpic}');"><img src="${context}${p.pic}"/></a></div>
      </#if>

      <br/>
      <!-- 转发内容 -->
      <#if p.transmitid??><@w.transmitblog p p.webPost></@w.transmitblog><br/></#if>

      <!-- link to post -->
      <div>
      <span id="commentShow_${p.pid}" style="float:right;display:''">
        <a href="javascript:showComments('${p.pid}');">Comment(${p.commentNum})</a>&nbsp;
      </span>
      <span id="commenthiden_${p.pid}" style="float:right;display:none">
        <a href="javascript:hidenComments('${p.pid}');">Comment(${p.commentNum})</a>&nbsp;
      </span>
      <span id="commentShow_${p.pid}" style="float:right">
      <a href="javascript:openWindow('<#if p.transmitid!='null' && p.webPost?? && p.webPost.content?has_content>${p.transmitid}<#else>${p.pid}</#if>');">Forward(${p.transmitNum})</a>&nbsp;
      <a href="javascript:removeCollect('${p.pid}');">Remove</a>&nbsp;
      </span>

      <#assign args=["${p.timeArg}"]/>
       <a href="/status?pid=${p.pid}"><@spring.messageArgs "${p.time}",args/></a>
    <!-- reply connection -->
    <#if p.replyPid?has_content>
       <a href="/status?pid=${p.replyPid}"><@spring.message "inreplyto"/> ${p.replyTo}</a>
    </#if>
    &nbsp;&nbsp;&nbsp;
    <#if loggedIn>
      <a href="/u/${loggedUser}?replyto=${p.name}&replypid=${p.pid}"><@spring.message "reply"/></a>
    </#if>
    </div>
    <div id="comments_${p.pid}"></div>
    </div>
    </td>
    </tr></table>
   </div>
   <#else>
    <div class="post" id="blog_${p.pid}">
     <hr/>
     <p>
      This Blog is Deleted By Original Author!
      <br/>
      <div><span style="float:right"><a href="javascript:removeBlogByOther('${p.pid}');">Delete</a></span></div>
     </p>
    </div>
   </#if>
   </#list>

   <div style="text-align: center;padding-top: 15px;">
     <@w.ajaxPageBar listPage=pages pageMethod="mini.blog.list" params="{}"/>
   </div>
