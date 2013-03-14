<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page >
<#assign context=rc.getContextPath()/>
<div class="span-7 box userlistDiv" style="width:100px;">
</div>
<div id="posts" class="span-15 prepend-1 append-bottom">
 <div style="padding-bottom: 10px;">
  <span id="g_all" style="padding:5px;border:1px solid #ccc;"><a href="javascript:mini.following.list();">全部</a></span>
  <span id="grouplist">
   <#list groups as g>
    <span id="g_${g.groupId}" style="background-color:#DFDFDF;padding:5px;border:1px solid #ccc;">
     <a href="javascript:group('${g.groupId}');">${g.groupName}</a><a href="javascript:deleteGroup('${g.groupId}');"> <font color="red"> x </font></a>
    </span>
    <span style="padding:2px;"></span>
   </#list>
  </span>
  <span id="addForm" style="float:right"><a href="javascript:openWindow();">新增分组</a></span>
 </div>
  <span id="post"><#include "/fragments/followinglist.ftl" /></span>
 </div>
<!--你的粉丝-->
<div class="span-7 last" id="rightcol">
  <div class="span-7 box">
    <h4>
    <#if followers?size==1>
    <@spring.message "follower"/>
    <#else>
    <@spring.message "followers"/>: ${followers?size}
    </#if>
    </h4>

   <ul class="user-list">
  <#list followers as f>
       <li><a href="${context}/u/${f.name}"/>${f.nickname}</a></li>
  </#list>
  </ul>
  <#if more_followers><@spring.message "and.more"/></#if>
  </div>

  <!--你关注的人-->
  <div class="span-7 box">
    <h4><@spring.message "following"/>: ${following?size}</h4>

    <ul class="user-list">
  <#list following as f>
       <li><a href="${context}/u/${f.name}"/>${f.nickname}</a></li>
  </#list>
    </ul>
  <#if more_following><@spring.message "and.more"/></#if>
  </div>
</div>


<@w.layer title="新增分组"  width=320 height=110></@w.layer>

<@w.showGroup title="分组"  width=200 height=200></@w.showGroup>



 <script type="text/javascript">
  var gId = 'all';

  function stopFollowing(id){
   $('#fan_'+id).slideUp(500,function(data){
     $.get('${context}/manage/following/stopfollowing/'+id,null,function(data){
       if(data){
         $.messager.alert('Message','Remove Success!!');
        }
    });
  });
   }
   //查看组用户列表
   function group(groupId){
     $.get('${context}/manage/following/followinggroup/'+groupId,null,function(data){
       if(data){
          $('#g_'+gId).css({backgroundColor: '#DFDFDF'});
          $('#g_'+groupId).css({backgroundColor: '#ffffff'});
          gId = groupId;
          $('#post').html(data);
        }
      });
   }

   //新增组表单
   function openWindow(){
        var offset = $('#addForm').offset();

        $.get('${context}/manage/group/addForm',null,function(data){
             $('#data').html(data);
             $('#w').window({
                top: offset.top,
                draggable: false,
                modal: true,
                shadow: true,
                closed: false,
            });
       });
  }

 //新增一个组
 function addGroup(){
   var params;
   if (params == null)params = {};
   params["groupName"] = $('#groupName').val();
   $('#w').window('close');

     $.post('${context}/manage/group/addGroup',params,function(data){
         $('#w').window('close');
         var template ="<span style='padding:2px;'></span><span id='g_"+data.groupId+"' style='background-color:#DFDFDF;padding:5px;border:1px solid #ccc;'><a href=javascript:group('"+data.groupId+"');>"+data.groupName+"</a><a href=javascript:deleteGroup('"+data.groupId+"');> <font color='red'>x</font></a></span>";
         $(template).appendTo($('#grouplist'));
    })
  }
 //删除一个组
 function deleteGroup(id){
   $('#g_'+id).slideUp(100,function(data){
     $.post('${context}/manage/group/removeGroup/'+id,null,function(data){
       if(data){
         $.messager.alert('分组','删除成功!!');
        }
    });
  });
 }

  //显示该成员用户组情况
  function showGroup(uid){
        var offset = $('#showGroup_'+uid).offset();

        $.get('${context}/manage/group/showMemberGroups/'+uid,null,function(data){
             $('#gdata').html(data);
             $('#gw').window({
                top: offset.top,
                draggable: false,
                modal: true,
                shadow: true,
                closed: false,
            });
       });
  }

 //通过表单改变成员用户组
 function changeGroup(uid){
   var params;
   if (params == null)params = {};

   var groupIds=new Array();
   $('input[name="g_checkbox"]:checked').each(function(i){
     groupIds[i]=$(this).val();
   });

   params["memberId"] = uid;
   params["groupIds"] = groupIds.join(',');
   $('#gw').window('close');
   $.post('${context}/manage/group/saveGroup',params,function(data){
       $('#w').window('close');
       $('#groups_'+uid).html(data);
   })
  }

  //从组中移除一个人
  function pullGroup(uid,groupId){
   $('#ug_'+groupId+'_'+uid).slideUp(100,function(data){
     $.post('${context}/manage/group/pullGroups/'+uid+'/'+groupId,null,function(data){
       if(data){
         $.messager.alert('分组','删除成功!!');
        }
    });
  });
 }


  </script>

  <script type="text/javascript">

    if (mini == null)var mini = {};
    if (mini.following == null)mini.following = {};

    mini.following.list = function(params) {
       if (params == null)params = {};
       $('#g_'+gId).css({backgroundColor: '#DFDFDF'});
       $('#g_all').css({backgroundColor: '#ffffff'});
       gId = 'all';
       $.get('${context}/manage/following/followings/page',params,function(data){
            $('#post').html(data);
       })
    }

 </script>

</@base.page >
