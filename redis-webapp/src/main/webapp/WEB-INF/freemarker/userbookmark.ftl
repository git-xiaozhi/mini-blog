<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<div class="span-24 prepend-1 last">
  <p style="text-align:left">
	<a href="/searchuser/q">Find Friends</a> |
	<a href="/tree">NetWork</a> |
	<a href="/userbookmark">Bookmark</a>
  </p>
</div>
<br/>

<div>   
   <#if users??>
     <#list users as u>
      <div id="${u.id}">
       <hr/>
		 <table border=0 height="50">
		  <tr>
		   <td width="50">
		    <table border=0>
             <tr><td><img src="${u.picture_small}"/></td></tr>
             <tr><td></td></tr>
            </table> 
           </td>
           <td>
            <table border=0>
             <tr><td><span style="float:right">SimilarityRatio : ${u.similarityRatio}%</span><a href="${u.link}" target="_blank">${u.name}</a></td></tr>
             <tr><td>${u.headline}<span style="float:right"><a href="javascript:showLayer('${u.id}');">Send Message</a></span></td></tr>
             <tr><td><span style="float:right"><a href="javascript:remove('${u.id}');">Remove</a></span>${u.location.country} ${u.location.city}</td></tr>
             <tr><td>${u.introduction}</td></tr>
            </table>
           </td>
           </tr>
         </table>
         </div>
	  </#list>
	  
   </#if>
</div>


<#include "/fragments/messageForm.ftl" />

    <script type="text/javascript">
    
		function remove(userid){
		  $('#'+userid).slideUp(500,function(){
            $.post('/removeuserbookmark/'+userid,null,function(data){
              if(data){
               alert("remove success!");
              }
               
			});
		 });	
        }
	</script>
	
</@base.page >	
	
	
	
	