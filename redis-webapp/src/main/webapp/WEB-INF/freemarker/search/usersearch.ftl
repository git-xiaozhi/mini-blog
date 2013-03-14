<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page>
<#assign context=rc.getContextPath()/>

<div class="span-7 box userlistDiv" style="width:100px;">
</div>

  <div id="posts" class="span-15 prepend-1 append-bottom">
     <form>找人：<input type="test" name="keyword" id="keyword"/><input type="button" id="searchsubmit" value="<@spring.message "update"/>"/></form>
     <span id="post"></span>
  </div>


<!--你的粉丝-->
  <div class="span-7 last" id="rightcol">
  <div class="span-7 box">
    xxxxxx
  </div>

  <!--你关注的人-->
  <div class="span-7 box">
   xxxxxx
  </div>

</@base.page >

 <script type="text/javascript">

  function following(id){
     $.get('${context}/manage/following/follow/'+id,null,function(data){
       if(data){
         $('#followed_'+id).fadeIn(500,function(data){
           $('#followed_'+id).html('<span style="float:right">Linked</span>');
         });
       }
    });
   }

  </script>

  <script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.search == null)mini.search = {};

    mini.search.list = function(params) {
       if (params == null)params = {};
       $.post('${context}/user/search',params,function(data){
            $('#post').html(data);
       })
    }

   $('#searchsubmit').click(
      function () {
       var params;
       if (params == null)params = {};
       params=$("form").serializeArray();
       $.post('${context}/user/search',params,function(data){
         $('#post').html(data);
      });
   });
 </script>

