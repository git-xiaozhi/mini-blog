
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

<#macro showGroup title=""  width=500 height=400>
   <div id="gw" class="easyui-window" closed="true" title="${title}" iconCls="icon-save" style="width:${width}px;height:${height}px;padding:5px;background: #fafafa;">
  <div class="easyui-layout" fit="true">
    <div id="gdata" region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">

    </div>
  </div>
  </div>
</#macro>



<#macro ajaxPageBar listPage pageMethod maxPages=10 params="{}">
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




<#macro transmitblog  parent p="">
  <div id="" style="width:97%;background:#F3F3F3;padding-left:3%">
   <#if p!="" && p??>
   <div class="post">
    <hr/>
    <p>
      <span><a class="user" href="${rc.getContextPath()}/u/${p.user.id}"><b>@${p.user.nickname}</b></a> ${p.content} </span>
      <#if p.video?? && p.video.pic?has_content>
       <div id="videopic_${parent.pid}"><a href="javascript:playvideo('${parent.pid}','${p.video.flash}');"><img src="${p.video.pic}"/></a></div>



       <div id="videoplay_${parent.pid}" style="display:none;background:none repeat scroll 0 0 #F3F3F3;padding-left: 5%;padding-bottom: 15px;padding-top: 10px;width:80%">
       <p><a href="javascript:videohide('${parent.pid}');">收起</a>&nbsp;&nbsp;&nbsp;&nbsp;
          <a href="${p.video.htmlpath}" target="_blank">${p.video.title}</a>
       </p>
       <div id="videoflash_${parent.pid}"></div>
       </div>

      </#if>

      <#if p.page?? && p.page.img?has_content>
       <div id="pagepic_${parent.pid}"><a href="javascript:showBigImg('${parent.pid}','${p.page.img}');"><img src="${p.page.img}" width="200px"/></a></div>
      </#if>

      <#if p.pic?? && p.pic!=null && p.pic!='null'>
       <div id="pic_${parent.pid}"><a href="javascript:showBigPic('${parent.pid}','${p.pic}','${p.bigpic}');"><img src="${p.pic}"/></a></div>
      </#if>

      <br/>
      <!-- link to post -->
      <div>
      <span style="float:right;padding-right:10px">
        <a href="#">引用(${p.transmitNum})</a>&nbsp;
        <a href="#">评论(${p.commentNum})</a>
      </span>

      <#assign args=["${p.timeArg}"]/>
       <a href="/status?pid=${p.pid}"><@spring.messageArgs "${p.time}",args/></a>
    </div>
    </p>
   </div>
   <#else>
    <div class="post">
     <hr/>
     <p>
      This Blog is Deleted By Original Author!
      <br/>
     </p>
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
    <td width="100"><img width="100" src="${rc.getContextPath()}${user.bigPortraitUrl}"/></td>
    <td>
     <table border=0 height="30">
       <tr><td height="10">粉丝:${user.followerNum}</td></tr>
       <tr><td height="10">关注:${user.followingNum}</td></tr>
       <tr><td height="10">微博:${user.blogNum}</td></tr>
     </table>
    </td>
   </tr>
   </table>
   </div>
</div>
</#if>
</#macro>



