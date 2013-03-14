   <div id="layer1" style="display:none;position: fixed;width:350px;border: 1px solid #000;z-index: 50;left:50%;top:50%;margin-top:-50px;margin-left:-50px;">
		<div id="layer1_handle">			
			<a href="#" id="close" onclick="closeLayer();">[x]</a>
			Message Form
		</div>
		<div id="layer1_content">
			<form name="form1" id="layer1_form">
				subject :<input style="width:280px;" type="text" name="subject" checked="checked" value="" /><br />
				content :<textarea style="width:280px;height:70px;padding:5px" cols="15" rows="3" name="message"></textarea><br />
				send email:<input type="checkbox" name="mail_copy"><br /><br />
				<input type="hidden" name="contact_card" value="BUSINESS" />
				<input type="hidden" name="targetuserid" id="targetuserid" value="" />				
				<div style="align:center"><input type="button" name="submit" value="send" onclick="postMessage(document.form1)" />
				<input type="button" name="close" value="close" onclick="closeLayer()" /></div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function()
		{
			$('#layer1').Draggable(
					{
						zIndex: 	20,
						ghosting:	true,
						opacity: 	0.7,
						handle:	'#layer1_handle'
					}
				);	
			$('#layer1_form').ajaxForm({
				target: '#content',
				success: function() 
				{
					$("#layer1").hide();
				}				
			});			
			$("#layer1").hide();
		});
		
		function showLayer(targetuserid)
		{  
		    $("#targetuserid").val(targetuserid);
			$("#layer1").show();
		}
		function closeLayer()
		{
			$("#layer1").hide();
		}

		function postMessage(form){
			 var subject = form.elements["subject"].value;
			 var message = form.elements["message"].value;
			 var contact_card = form.elements["contact_card"].value;
			 var mail_copy = form.elements["mail_copy"].checked;
			 var targetuserid = form.elements["targetuserid"].value;
			 var params;
		     if (params == null)params = {};
		     params["subject"] = subject;
		     params["message"] = message;
		     params["contact_card"] = contact_card;
		     params["mail_copy"] = mail_copy;
		     params["targetuserid"] = targetuserid;
		     $.post('/postMessage',params,function(data){
               alert(data);
			});
		}
	</script>
