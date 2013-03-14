<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page>
<div class="span-24 prepend-1 last">
  <p style="text-align:left">
	<a href="/u/${loggedUser}/positions/1">Find Jobs</a> |
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
     <div style="text-align:right">Total Match number:${jobs.count}</div>
     <#list jobs.data as p>
       <hr/>
	   <div>
    
	      	<span style="float:right"><a href="javascript:add('${p.id}');">Add To Bookmark</a></span><h6><a href="${p.link}" target="_blank">${p.name}</a></h6>
	      	<p style="text-align:left"><span style="float:right"><em></em>${p.updated_time?datetime}</span><a href="${p.company_info.website}" target="_blank">${p.company_info.name}</a>
	      	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${p.location.city}
	      	</p>
	      	<p>${p.description}</p>
	   </div>
	 </#list>
	</#if>
	<div style="text-align:center">
		<a href="/u/${loggedUser}/positions/${page-2}?keyword=${keyword}">prev page</a>|
		<a href="/u/${loggedUser}/positions/${page}?keyword=${keyword}">next page</a>
	</div>
</div>


    <script type="text/javascript">
		function add(jobId){
		     $.post('/addjobbookmark/'+jobId,null,function(data){
               if(data){
                alert('add success!');
               }
			});
		}
	</script>
</@base.page>
