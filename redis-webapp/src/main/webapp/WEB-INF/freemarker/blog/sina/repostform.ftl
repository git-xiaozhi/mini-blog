<#assign context=rc.getContextPath()/>
    <div id="tcontent" style="background:#F3F3F3;">
     <#if p.retweetedStatus??>
      <a class="user" href="${context}/blog/sina/friend/usertimeline/${p.retweetedStatus.user.id}"><b>@${p.retweetedStatus.user.screenName}</b></a>:${p.retweetedStatus.text}
     <#else>
      <a class="user" href="${context}/blog/sina/friend/usertimeline/${p.user.id}"><b>@${p.user.screenName}</b></a>:${p.text}
     </#if>
    </div>
    <div class="exp-holder" >
      <textarea class="text" name="tcontent_${p.id}" id="<#if p.retweetedStatus??>tcontent_${p.retweetedStatus.id}<#else>tcontent_${p.id}</#if>" style="width:455px;height:90px;"><#if p.retweetedStatus??>//@${p.user.screenName}:${p.text}</#if></textarea>
    <div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
      <span style="float:left;display:''"><a class="exp-block-trigger" href="javascript:void(0);" >表情</a></span><a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="postStatus('<#if p.retweetedStatus??>${p.retweetedStatus.id}<#else>${p.id}</#if>')">转发</a>
    </div>
    </div>
