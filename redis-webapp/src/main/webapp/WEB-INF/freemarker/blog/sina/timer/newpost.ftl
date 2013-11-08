<#import "/blog/sina/mod.ftl" as w/>
<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>
<#if p??>
  <div class="post" id="blog_${p.id}">
    <hr/>
    <table><tr>
     <td style="width:50px;vertical-align: top;"><div></div></td>
     <td style="vertical-align: top;">
      <div>
      <div id="blogcontent_${p.id}">${p.content}</div>

      <#if p.url?? && p.url!=null>
       <div id="pic_${p.id}" style="padding-top: 15px;"><img src="${p.url}" width="150"/></div>
      </#if>
      <br/>
      <!-- link to post -->
      <span id="commentShow_${p.id}" style="float:right">
       <a href="javascript:removeBlogByMe('${p.pid}');">删除</a>&nbsp;
      </span>
     发布时间：${p.futureDate?string('yyyy-MM-dd hh:mm:ss')}
    </div>
    <div id="comments_${p.id}"></div>
    </div>
    </td>
    </tr></table>
   </div>
</#if>