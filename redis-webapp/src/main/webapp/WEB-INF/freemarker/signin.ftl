<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.loginpage >
<#assign context=rc.getContextPath()/>
<h2 ><@spring.message "welcome"/></h2>

<div class="span-11 box">
  <h2><@spring.message "signin"/></h2>

  <form action="${context}/j_spring_security_check" method="post">
     <#if errorpass>
      <div class="error">
         <@spring.message "error.pass"/>
    </div>
    </#if>

    <table>
      <tr>
        <td><@spring.message "username"/></td>
       <td><input  name="j_username" /></td>
      </tr>
      <tr>
        <td><@spring.message "password"/></td>
        <td><input type="password" name="j_password" /></td>
      </tr>
      <tr>
        <td></td>
        <td><input id="_spring_security_remember_me" name="_spring_security_remember_me" type="checkbox" checked />两周之内不必登陆</td>
      </tr>
    </table>
    <input type="submit" value="<@spring.message "signin"/>" />

  </form>
</div>

<div class="span-11 box last">
  <h2><@spring.message "signup"/></h2>


  <form action="signUp" method="post">
    <table>
      <tr>
        <td width=40><@spring.message "username"/></td>
        <td>
          <@spring.formInput "userForm.name" /><b>*</b>
          <b id="nameDupliate" style="display:none">此邮件地址已被注册！</b>
          <@spring.showErrors "<br>"/>
        </td>
      </tr>
      <tr>
        <td><@spring.message "nickname"/></td>
        <td>
            <@spring.formInput "userForm.nickname" /><b>*</b>
            <b id="nicknameDupliate" style="display:none">用户昵称重复!</b>
            <@spring.showErrors "<br>"/>
        </td>
      </tr>
      <tr>
        <td><@spring.message "password"/></td>
        <td>
            <@spring.formPasswordInput "userForm.pass" /><b>*</b>
            <@spring.showErrors "<br>"/>
        </td>
      </tr>
      <tr>
        <td><@spring.message "password.again"/></td>
        <td>
           <@spring.formPasswordInput "userForm.pass2" /><b>*</b>
           <@spring.showErrors "<br>"/>
        </td>
      </tr>
      <tr>
        <td>company</td>
        <td><@spring.formInput "userForm.company" /></td>
      </tr>
      <tr>
        <td>school</td>
        <td><@spring.formInput "userForm.school" /></td>
      </tr>
    </table>
    <input type="submit" value="<@spring.message "signup"/>">
  </form>
</div>

<p>&nbsp;</p>
<p>&nbsp;</p>

<script>
$(document).ready(function() {
   $('#name').focusout(
    function() {
     var params;
     if (params == null)params = {};
     params["name"] = $('#name').val();
     $.get('${context}/isNameDuplicate',params,function(data){
       if(data){
        $('#nameDupliate').show();
       }else{
        $('#nameDupliate').hide();
       }
     })
   });

   $('#nickname').focusout(
    function() {
     var params;
     if (params == null)params = {};
     params["nickname"] = $('#nickname').val();
     $.get('${context}/isNickNameDuplicate',params,function(data){
       if(data){
         $('#nicknameDupliate').show();
        }else{
         $('#nicknameDupliate').hide();
        }
     })
   });
});



</script>

</@base.loginpage >