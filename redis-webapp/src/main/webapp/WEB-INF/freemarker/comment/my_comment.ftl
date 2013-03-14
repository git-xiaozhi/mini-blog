
<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<div class="span-7 box userlistDiv" style="width:100px;">

</div>
<div id="posts" class="span-15 prepend-1 append-bottom">
     <span id="post">
     <#if pagelist=='postComments'>
       <#include "/comment/comment_post.ftl" />
      <#else>
       <#include "/comment/comment_receive.ftl" />
      </#if>
     <span>
</div>

<#include "/fragments/mention_right.ftl" />


<script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.blog == null)mini.blog = {};

    mini.blog.postcommentslist = function(params) {
       if (params == null)params = {};
       $.get('/u/postcomments/page',params,function(data){
            $('#post').html(data);
       })
    }


    mini.blog.receivecommentslist = function(params) {
       if (params == null)params = {};
       $.get('/u/receivecomments/page',params,function(data){
            $('#post').html(data);
       })
    }



    function delComment(pid,commentId,blogOwner){
    $('#blog_'+commentId).slideUp(500,function(data){
        $.post('/comment/delcomment/'+blogOwner+'/'+pid+'/'+commentId,null,function(data){
     });
    });
   }

   function delCommentForNoBlog(commentId){
    $('#blog_'+commentId).slideUp(500,function(data){
        $.post('/comment/delCommentForNoBlog/'+commentId,null,function(data){
     });
    });
   }

 </script>
</@base.page>

