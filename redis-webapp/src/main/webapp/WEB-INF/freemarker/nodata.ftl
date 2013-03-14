<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page>
<h2><@spring.message "nodata"/> <@spring.message "${nodatatype}"/>: ${name}</h2>
</@base.page>