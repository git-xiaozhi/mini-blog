
<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">

</div>

<div id="posts" class="span-15 prepend-1 append-bottom">
     <span id="post"><#include "/mention/comment_m.ftl" /><span>
</div>

<#include "/fragments/mention_right.ftl" />


<script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.blog == null)mini.blog = {};

    mini.blog.mentionlist = function(params) {
       if (params == null)params = {};
       $.get('${context}/u/commentmentions/page',params,function(data){
            $('#post').html(data);
       })
    }

 </script>
</@base.page>

