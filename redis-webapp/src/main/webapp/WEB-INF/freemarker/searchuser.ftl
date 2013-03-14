<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<div class="span-24 prepend-1 last">
   <div style="text-align:left">
	<a href="/searchuser/q">Find Friends</a> |
	<a href="/tree">NetWork</a> |
	<a href="/userbookmark">Bookmark</a>
  </div>
</div>
<br/>

<div id="posts" class="span-15 prepend-1 append-bottom">
   <div id="updateform" class="box">

	<form method="post" action="/searchuser/q">
	  <b>keyword :</b>
      <input type="text" name="keyword" value="${keyword}"/>
      <input type="submit" value="Search"/>
	</form>
  </div>
   
   <#if users??>
     <#list users.data as u>
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
             <tr><td><span style="float:right"><a href="javascript:add('${u.id}');">Add To Bookmark</a></span>${u.location.country} ${u.location.city}</td></tr>
             <tr><td>${u.introduction}</td></tr>
            </table>
           </td>
           </tr>
         </table>
	  </#list>
	  <#if (page>0)>
	  <div style="text-align:center">
		<a href="/searchuser/${query}?keyword=${keyword}&page=${page-1}">prev page</a> | 
		<a href="/searchuser/${query}?keyword=${keyword}&page=${page+1}">next page</a>
	  </div>
	  </#if>
	  
   </#if>
</div>


<div class="span-7 last" id="rightcol">
  <div class="span-7 box">
    <h4>Alumnas in TianJi.com</h4>
     <a href="/searchuser/school?keyword=${me.school}">The Alumnas</a>
    <h4>Colleagues in TianJi.com</h4>
    <a href="/searchuser/company?keyword=${me.company}">The Colleagues</a>
   
  </div>
</div>

    <script type="text/javascript">
		function add(userid){
		     $.post('/adduserbookmark/'+userid,null,function(data){
               if(data){
                alert('add success!');
               }
			});
		}
	</script>
	
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
	