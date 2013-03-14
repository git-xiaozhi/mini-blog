
<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
${user.name}
<p align="center">
<form method="post" action="edituser">
nickname :<input type="text" name="nickname" value="${user.nickname}"><br/>
company :<input type="text" name="company" value="${user.company}"><br/>
school :<input type="text" name="school" value="${user.school}"><br/>
<input type="submit" name="a" value="Post">
</form>
</p>

</@base.page>