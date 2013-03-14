<#import "/spring.ftl" as spring />
<#assign context=rc.getContextPath()/>

      <input type="hidden" name="pic" id="pic"/>

      <div class="exp-holder" >
       <textarea class="text" name="content" id="content" rows="3" cols="60" style="margin: 0 auto; width: 660px; "></textarea>
        <span style="float:right;display:''"><input type="button" class="blogsubmit" value="<@spring.message "update"/>"/></span>
        <a class="exp-block-trigger" href="javascript:void(0);" style="padding-right: 5px;">表情</a>
        <a class="layer-trigger" href="javascript:showimageLayer();" id="showImgurl" style="padding-right: 5px;">Image</a>
      </div>
