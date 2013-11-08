
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh" lang="zh">
<#assign context=rc.getContextPath()/>
  <head>
    <meta http-equiv="Pragma" content="no-cache">
    <link rel="stylesheet" href="/static/styles/screen.css" type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href="/static/styles/print.css" type="text/css" media="print"/>
    <link rel="stylesheet" href="/static/styles/ECOTree.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/static/styles/custom.css" type="text/css" media="screen, projection"/>
    <link rel="stylesheet" type="text/css" href="/static/styles/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/static/styles/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/static/js/upload/uploadify.css" media="screen" />

    <script type="text/javascript" src="/static/js/ECOTree.js"></script>
    <script type="text/javascript" src="/static/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.easyui.min.js"></script>

    <script type="text/javascript" src="/static/js/upload/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/upload/jquery.uploadify.v2.1.4.js"></script>
    <script type="text/javascript" src="/static/js/util.js"></script>


    <link rel="stylesheet" type="text/css" href="/static/styles/imgareaselect-default.css" />
    <script type="text/javascript" src="/static/js/jquery.imgareaselect.min.js"></script>
    
    
    <link rel="stylesheet" type="text/css" href="/static/js/timer/datePicker.css" />
    <script type="text/javascript" src="/static/js/timer/jquery.datePicker.min-2.1.2.js"></script>



    <!--表情控件-->
    <link rel="stylesheet" type="text/css" href="/static/js/plugins/exp/css/style.css" />
    <script type="text/javascript" src="/static/js/plugins/exp/exp.js"></script>
    <!--浮动层控件-->
    <script type="text/javascript" src="/static/js/plugins/exp/layer.js"></script>

    <title>Mini Blog</title>
  </head>

    <body>

      <div class="container">
          <div style="background-color:#B1D3EC">
              <div class="span-15 prepend-1">
                 <h2 class="alt"><a href="/">Mini Blog</a></h2>
                 <div class="span-8 last right-align">
          <#if userRoleHelper.getUserAuthUser()??>
             <p>
              <span style="float:right">
            <a href="${context}/main"><@spring.message "home"/></a> |
            <a href="javascript:void(0)" id="mb6" class="easyui-menubutton" menu="#mm6">微博管理</a> |
            <a href="javascript:void(0)" id="mb5" class="easyui-menubutton" menu="#mm5">搜索</a> |
            <a href="javascript:void(0)" id="mb1" class="easyui-menubutton" menu="#mm1">我的管理</a> |
            <a href="javascript:void(0)" id="mb3" class="easyui-menubutton" menu="#mm3">@我的</a> |
            <a href="javascript:void(0)" id="mb4" class="easyui-menubutton" menu="#mm4">评论</a> |
            <a href="javascript:void(0)" id="mb2" class="easyui-menubutton" menu="#mm2">我的账户</a> |

            <#--<a href="/bind/${userRoleHelper.getUserAuthUser().uid}">Bind To TianJi</a>|-->
            <a href="${context}/j_spring_security_logout"><@spring.message "logout"/></a>
            </span>
            <span style="overflow: hidden; padding-left: 27px;">
            <#if userRoleHelper.getUserAuthUser().name==name><@spring.message "welcome"/> <b><i>${userRoleHelper.getUserAuthUser().name}</i></b></#if>
            </span>
            </p>
          </#if>
        <#if !userRoleHelper.getUserAuthUser()??>
<!--  				 <div class="span-7 last" id="rightcol"> -->
          <a href="/signIn"><@spring.message "signin"/></a>
          <!-- </div> -->
        </#if>
        </div>
              </div>

        <hr/>
      </div>

  <#if userRoleHelper.getUserAuthUser()??>
      <div id="mm1" style="width:100px;display:none">
    <div><a href="${context}/manage/blog/blogs">我的微博</a></div>
    <div><a href="${context}/manage/collect/blogs">我的收藏</a></div>
    <div><a href="${context}/manage/follower/fans">我的粉丝</a></div>
    <div><a href="${context}/manage/following/followings">我的关注</a></div>
    </div>

   <div id="mm2" style="width:100px;display:none">
    <div><a href="${context}/user/showedituser">基本信息</a></div>
    <div><a href="${context}/user/userPortrait">我的头像</a></div>
    </div>

    <div id="mm3" style="width:100px;display:none">
    <div><a href="${context}/u/mentions">@我的微博</a></div>
    <div><a href="${context}/u/commentmentions">@我的评论</a></div>
    </div>

    <div id="mm4" style="width:100px;display:none">
    <div><a href="${context}/u/receivecomments">收到的评论</a></div>
    <div><a href="${context}/u/postcomments">发出的评论</a></div>
    </div>

    <div id="mm5" style="width:100px;display:none">
    <div><a href="${context}/user/searchhome">找人</a></div>
    <div><a href="${context}/blog/searchhome">找微博</a></div>
    <div><a href="${context}/blog/photos">oauth2</a></div>
    </div>

    <div id="mm6" style="width:100px;display:none">
    <div><a href="${context}/blog/sina/hometimeline">新浪微博</a></div>
    </div>

    </#if>

    <div style="overflow: hidden; padding-left: 5px;">

<script type="text/javascript">

$(document).ready(function(){

     $.layer.initExp({
        /*
        //包含textarea和表情触发的exp-holder
        holder: '.holder',
        //exp-holder中的textarea输入dom，默认为textarea,
        textarea : 'textarea',
        //触发dom
        trigger : '.layer-trigger',
        //每页显示表情的组数
        grpNum : 5,
        //位置相对页面固定(absolute)||窗口固定(fixed)
        posType : 'absolute',
        //浮层层数
        zIndex : '9999'
        */
      });
})

 </script>



