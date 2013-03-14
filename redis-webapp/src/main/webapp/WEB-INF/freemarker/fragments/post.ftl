<#import "/templates/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
   <#list posts.list as p>
   <#if p.user?? && p.user.name??>
   <div class="post" id="blog_${p.pid}">
    <hr/>
    <table><tr>
     <#if pagelist!='manage'><td style="width:50px;vertical-align: top;"><div><img src="${context}${p.user.portraitUrl}" width="50"></div></td></#if>
     <td style="vertical-align: top;">
      <div>
      <div id="blogcontent_${p.pid}"><#if pagelist!='manage'><a class="user" href="${context}/u/${p.user.id}"><b>${p.user.nickname}</b></a></#if> ${p.content}</div>
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
      <div id="forward_${p.pid}">
      <span id="commentShow_${p.pid}" style="float:right;display:''">
        <a href="javascript:showComments('${p.pid}','${p.user.id}');">评论(${p.commentNum})</a>&nbsp;
      </span>
      <span id="commenthiden_${p.pid}" style="float:right;display:none">
        <a href="javascript:hidenComments('${p.pid}');">评论(${p.commentNum})</a>&nbsp;
      </span>
      <span id="" style="float:right">
        <a href="javascript:collect('${p.pid}');">收藏</a>&nbsp;
      </span>
      <span id="commentShow_${p.pid}" style="float:right">
      <#--<a href="javascript:openWindow('<#if p.transmitid!='null' && p.webPost?? && p.webPost.content?has_content>${p.transmitid}<#else>${p.pid}</#if>');">Forward(${p.transmitNum})</a>-->
       <a href="javascript:openWindow('${p.pid}');">引用(${p.transmitNum})</a>
      <#if pagelist=='manage'><a href="javascript:removeBlogByMe('${p.pid}');">Delete</a></#if>&nbsp;
      </span>

      <#assign args=["${p.timeArg}"]/>
       <a href="/status?pid=${p.pid}"><@spring.messageArgs "${p.time}",args/></a>
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
      <#if pagelist=='mention'>
       <div><span style="float:right"><a href="javascript:removeMention('${p.pid}');">Delete</a></span></div>
       <#else>
       <div><span style="float:right"><a href="javascript:removeBlogByOther('${p.pid}');">Delete</a></span></div>
      </#if>
     </p>
    </div>
   </#if>
   </#list>

   <div style="text-align: center;padding-top: 15px;">
    <#if pagelist=='manage'>
     <@w.ajaxPageBar listPage=posts pageMethod="mini.blog.managelist" params="{}"/>
    <#elseif pagelist=='mention'>
     <@w.ajaxPageBar listPage=posts pageMethod="mini.blog.mentionlist" params="{}"/>
    <#elseif pagelist=='blogsearch'>
     <@w.ajaxPageBar listPage=posts pageMethod="mini.search.list" params="{keyword:'${keyword}'}"/>
    <#else>
     <@w.ajaxPageBar listPage=posts pageMethod="mini.blog.list" params="{}"/>
    </#if>
   </div>

