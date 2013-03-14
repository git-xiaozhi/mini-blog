<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page>

<div class="span-24 prepend-1 last">
  <p style="text-align:left">
	<a href="!${loggedUser}/positions/1">Find Jobs</a> |
	<a href="/jobbookmark">Bookmark</a>
  </p>
</div>
<br/>

<div id="posts" style="overflow: hidden; padding-right: 27px;">
	<div id="updateform" class="box">
	<form method="post" action="/u/${loggedUser}/positions/1">
	  <b>keyword :</b>
      <input type="text" name="keyword" value="${keyword}"/>
      <input type="submit" value="Search"/>
	</form>
    </div>
	
    <#if jobs??>
     <#list jobs as p>
      <div id="${p.id}">
       <hr/>
	      	<span style="float:right"><a href="javascript:remove('${p.id}');">Remove</a></span><h6><a href="${p.link}" target="_blank">${p.title}</a></h6>
	      	<p style="text-align:left"><span style="float:right"><em></em>${p.updated_time?datetime}</span><a href="${p.company_info.website}" target="_blank">${p.company_info.name}</a>
	      	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${p.location.city}
	      	</p>
            <p>${p.description}</p>
	   </div>
	  </#list>
	</#if>
	<#--
	<div style="text-align:center">
		<a href="/u/${loggedUser}/positions/${page-2}?keyword=${keyword}">prev page</a>|
		<a href="/u/${loggedUser}/positions/${page}?keyword=${keyword}">next page</a>
	</div>
	-->
</div>

<#include "/fragments/messageForm.ftl" />

    <script type="text/javascript">
    
		function remove(jobId){
		  $('#'+jobId).slideUp(500,function(){
            $.post('/removejobbookmark/'+jobId,null,function(data){
              if(data){
               alert("remove success!");
              }
               
			});
		 });	
        }
	</script>
</@base.page>	