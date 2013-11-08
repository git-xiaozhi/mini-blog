
<#macro page title="">
 <#include "/templates/header.ftl" />
   <#nested>
 <#include "/templates/footer.ftl" />
</#macro>

<#macro loginpage title="">
 <#include "/templates/loginheader.ftl" />
   <#nested>
 <#include "/templates/footer.ftl" />
</#macro>



<#macro layer title=""  width=500 height=400>
   <div id="w" class="easyui-window" closed="true" title="${title}" iconCls="icon-save" style="width:${width}px;height:${height}px;padding:5px;background: #fafafa;">
  <div class="easyui-layout" fit="true">
    <div id="data" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">

    </div>
  </div>
  </div>
</#macro>


<#macro ajaxPageBar listPage pages pageMethod maxPages=10 params="{}">
  <#if pages gt 1>
      <#if (listPage.page>1)>
        <a href="#" onclick="efn.util.ajaxPage(${params}, ${listPage.page-1}, ${pageMethod}); return false;">上一页</a>
      <#else>
        上一页
      </#if>
      <#if (listPage.page > maxPages/2)>
        <#local start=(listPage.page-maxPages/2)/>
      <#else>
        <#local start=1/>
      </#if>
      <#local end=(start+maxPages-1)/>
      <#if (end > pages)>
        <#local end=pages/>
      </#if>
      <#if (start < listPage.page)>
        <#list start..(listPage.page-1) as i>
          [<a href="#" onclick="efn.util.ajaxPage(${params}, ${i}, ${pageMethod}); return false;">${i}</a>]
        </#list>
      </#if>
      [${listPage.page}]
      <#if (end > listPage.page)>
        <#list (listPage.page+1)..end as i>
          [<a href="#" onclick="efn.util.ajaxPage(${params}, ${i}, ${pageMethod}); return false;">${i}</a>]
        </#list>
      </#if>
    <#if (listPage.page < pages)>
      <a href="#" onclick="efn.util.ajaxPage(${params}, ${listPage.page+1}, ${pageMethod}); return false;">下一页</a>
    <#else>
       下一页
    </#if>
  </#if>
</#macro>



<#macro ajaxTimerPageBar listPage pageMethod maxPages=10 params="{}">
  <#if listPage.pages gt 1>
      <#if (listPage.currPage>1)>
        <a href="#" onclick="efn.util.ajaxPage(${params}, ${listPage.currPage-1}, ${pageMethod}); return false;">上一页</a>
      <#else>
        上一页
      </#if>
      <#if (listPage.currPage > maxPages/2)>
        <#local start=(listPage.currPage-maxPages/2)/>
      <#else>
        <#local start=1/>
      </#if>
      <#local end=(start+maxPages-1)/>
      <#if (end > listPage.pages)>
        <#local end=listPage.pages/>
      </#if>
      <#if (start < listPage.currPage)>
        <#list start..(listPage.currPage-1) as i>
          [<a href="#" onclick="efn.util.ajaxPage(${params}, ${i}, ${pageMethod}); return false;">${i}</a>]
        </#list>
      </#if>
      [${listPage.currPage}]
      <#if (end > listPage.currPage)>
        <#list (listPage.currPage+1)..end as i>
          [<a href="#" onclick="efn.util.ajaxPage(${params}, ${i}, ${pageMethod}); return false;">${i}</a>]
        </#list>
      </#if>
    <#if (listPage.currPage < listPage.pages)>
      <a href="#" onclick="efn.util.ajaxPage(${params}, ${listPage.currPage+1}, ${pageMethod}); return false;">下一页</a>
    <#else>
       下一页
    </#if>
  </#if>
</#macro>


<#macro transmitblog p parent>

  <div id="" style="width:97%;background:#F3F3F3;padding-left:3%">
  <#if p?? && p.user??>
   <div class="post">
    <hr/>
    <p>
      <span><a class="user" href="${context}/blog/sina/friend/usertimeline/${p.user.id}"><b>@${p.user.screenName}</b></a> ${p.text} </span>

      <#if p.thumbnailPic?? && p.thumbnailPic!=null && p.thumbnailPic!='null'>
       <div id="pic_${parent.id}"><a href="javascript:showBigPic('${parent.id}','${p.thumbnailPic}','${p.bmiddlePic}');"><img src="${p.thumbnailPic}"/></a></div>
      </#if>

      <br/>
      <!-- link to post -->
      <div>
      <span style="float:right;padding-right:10px">
        <a href="#">转发(${p.repostsCount})</a>&nbsp;
        <a href="#">评论(${p.commentsCount})</a>
      </span>

      ${p.createdAt?string('yyyy-MM-dd hh:mm')}
    </div>
    </p>
   </div>
   <#else>
   <div class="post">
    此微博已经被作者删除！
    </div>
   </#if>
 </div>

</#macro>


<#macro userInfo user>
<#if user??>
<div class="span-7 last" id="rightcol">
 <div class="span-7 box">
  <!--我的头像-->
   <table border=0 height="30">
   <tr>
    <td width="85"><img width="85" src="${user.avatarLarge}"/></td>
    <td>
     <table border=0 height="30">
       <tr><td height="10" style="padding: 2px">粉丝:${user.followersCount}</td></tr>
       <tr><td height="10" style="padding: 2px">关注:${user.friendsCount}</td></tr>
       <tr><td height="10" style="padding: 2px">微博:${user.statusesCount}</td></tr>
       <#if !user.following><tr><td height="10" style="padding: 2px"><a href="javascript:follow('${user.id}');">加关注</a></td></tr></#if>
     </table>
    </td>
   </tr>
   </table>
   </div>
</div>
</#if>
</#macro>



