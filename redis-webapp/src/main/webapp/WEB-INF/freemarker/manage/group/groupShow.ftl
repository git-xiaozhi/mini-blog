<div class="exp-holder" >
   <ul class="user-list">
      <#list groups as g>
       <li><input name="g_checkbox" id="g_checkbox" type="checkbox" value="${g.groupId}" <#if g.selected>checked</#if> />${g.groupName}</li>
      </#list>
    </ul>

    <div><input type="button" name="submit" value="确定" onclick="changeGroup('${userid}');"/></div>
</div>