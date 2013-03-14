<#assign context=rc.getContextPath()/>
    <div id="tcontent" style="background:#F3F3F3;">
     <#if blog.webPost??>
      <a class="user" href="${context}/u/${blog.webPost.user.id}"><b>@${blog.webPost.user.nickname}</b></a>:${blog.webPost.content}
     <#else>
      <a class="user" href="${context}/u/${blog.user.id}"><b>@${blog.user.nickname}</b></a>:${blog.content}
     </#if>
    </div>
    <div class="exp-holder" >
      <textarea class="text" name="tcontent_${pid}" id="<#if blog.webPost??>tcontent_${blog.webPost.pid}<#else>tcontent_${pid}</#if>" style="width:455px;height:90px;"><#if blog.webPost??>//@${blog.user.nickname}:${blog.content}</#if></textarea>
    <div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
      <span style="float:left;display:''"><a class="exp-block-trigger" href="javascript:void(0);" >表情</a></span><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="postStatus('<#if blog.webPost??>${blog.webPost.pid}<#else>${pid}</#if>')">Forward</a>
    </div>
    </div>
