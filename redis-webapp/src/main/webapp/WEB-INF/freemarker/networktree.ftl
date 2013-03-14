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
		<xml:namespace ns="urn:schemas-microsoft-com:vml" prefix="v"/>
		<style>v\:*{ behavior:url(#default#VML);}</style> 			
		<style>
			.copy {
				font-family : "Verdana";				
				font-size : 10px;
				color : #CCCCCC;
			}
		</style>

		<script>
			var t = null;
			
			function CreateTree() {
				t = new ECOTree('t','sample2');						
				t.config.iRootOrientation = ECOTree.RO_LEFT;
				t.config.defaultNodeWidth = 49;
				t.config.defaultNodeHeight = 69;
				t.config.iSubtreeSeparation = 10;//
				t.config.iSiblingSeparation = 5;
                t.config.iLevelSeparation=100;	//width									
				t.config.linkType = 'B';
				t.config.useTarget = false;
				//t.config.nodeFill = ECOTree.NF_GRADIENT;
				t.config.colorStyle = ECOTree.CS_LEVEL;
				t.config.levelColors = ["#B1D3EC","#B1D3EC","#B1D3EC","#B1D3EC"];
				//t.config.levelBorderColors = ["#FFD700","#D9B100","#BC9400","#966E00"];
				t.config.nodeColor = "#B1D3EC";
				t.config.nodeBorderColor = "#2C404F";
				t.config.linkColor = "#2C404F";
				
				t.add('${me.id}',-1,'<a href="javascript:getUserFriends(\'${me.id}\',\'${me.id}\',1)">${me.name}<a/><img src="${me.picture_small}"/>',null,null,"#F08080");
				
				<#if myfriends??>
			  	 <#list myfriends.data as f>
			  	   t.add('${f.id}','${me.id}','<a href="javascript:getUserFriends(\'${f.id}\',\'${f.id}\',1)">${f.name}<a/><a href="javascript:showLayer(\'${f.id}\');"><img src="${f.picture_small}"/></a>');
			  	 </#list>
			    </#if>
			    										
				t.UpdateTree();
			}	
			
			
	   $(document).ready(function()
		{
		  CreateTree();
		});
	   
	   
		function getUserFriends(userid,parentNodeId,page){
            $.get('/getUserFridends/'+userid+'/'+page,null,function(data){
               for(var i=0; i<=data.data.length-1; i=i+1){
            	   var f = data.data[i];
            	   var dsc = "<a href=\"javascript:getUserFriends(\'"+f.id+"\',\'"+f.id+"\',1)\">"+f.name+"<a/><a href=\"javascript:showLayer(\'"+f.id+"\');\"><img src=\'"+f.picture_small+"\'/></a>";
            	   //alert(dsc);
            	   t.add(f.id,parentNodeId,dsc);  
               }
               t.UpdateTree();
			});	
        }
	   
		</script>
		
		<div id="sample2"></div>
		
		
	<#include "/fragments/messageForm.ftl" />

</@base.page >		