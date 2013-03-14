<#list groups as g>
    <span id="ug_${g.groupId}_${memberId}" style="background-color:#DFDFDF;padding:1px;border:1px solid #ccc;">
      ${g.groupName}<a href="javascript:pullGroup('${memberId}','${g.groupId}');"> <font color="red"> x </font></a>
    </span>
    <span style="padding:2px;"></span>
</#list>