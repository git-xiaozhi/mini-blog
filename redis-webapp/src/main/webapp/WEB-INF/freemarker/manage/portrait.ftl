<#import "/spring.ftl" as spring />
<#import "/templates/mod.ftl" as base/>

<@base.page>
<#assign context=rc.getContextPath()/>

  <div>
  <div style="float: left;width: 250px;">
    <p style="font-size: 110%; font-weight: bold; padding-left: 0.1em;">
      Selection Preview
    </p>

    <div style="margin: 0pt 1em; width: 150px; height: 350px;padding-bottom: 50px;" class="frame">
      <div style="width: 150px; height: 150px; overflow: hidden;" id="preview">
        <img id="previewimg" src="<#if user.bigPortraitUrl??>${context}${user.bigPortraitUrl}</#if>">
      </div>
      <div style="padding-bottom: 10px;padding-top: 10px;width: 230px">
      <input id="file_upload" type="file" name="filename"/>
      </div>
      <div style="padding-bottom: 50px;padding-top: 10px;width: 230px">
      <form action="${context}/user/portraithandle" method="POST">
        <input type="hidden" name="x" value="0" id="x1">
        <input type="hidden" id="w" name="width" value="150">
        <input type="hidden" value="0" name="y" id="y1">
        <input type="hidden" value="150" name="heigth" id="h">
        <input type="hidden" value="" name="url" id="u">
        <input type="submit" value="确定" id="sub">
      </form>
      </div>
    </div>
  </div>


  <div style="float: right; width: 62%;overflow:auto;">
    <p class="instructions">
      Click and drag on the image to select an area.
    </p>

    <div style="margin: 0pt 0.3em; width: 300px; height: 300px;" class="frame">
      <span id="returnPic"></span>
    </div>
  </div>

</div>

<script type="text/javascript">
 $(document).ready(function() {

   $('#file_upload').uploadify({
    'uploader'  : '/static/js/upload/uploadify.swf',
    'script'    : '${context}/user/upload;jsessionid=${sessionId}',
    'cancelImg' : '/static/js/upload/cancel.png',
    'fileDataName' : 'filename',
    'fileExt'        : '*.jpg;*.gif;*.png',
    'fileDesc'       : 'Image Files (.JPG, .GIF, .PNG)',
    'buttonText'  : 'Select Image',
    //'sizeLimit' : '1024000',
    'auto'      : true,
    'onError'     : function (event,ID,fileObj,errorObj) {
      alert(errorObj.type + ' Error: ' + errorObj.info);
     },
    'onComplete'  : function(event, ID, fileObj, response, data) {
      $('#returnPic').html('<img id="photo" src="'+response+'"/>');
      $('#u').val(response);

      $('#photo').imgAreaSelect({aspectRatio: '1:1', maxWidth: 500, maxHeight: 500, handles: true,
        fadeSpeed: 50, onSelectChange: preview,x1: 0, y1: 0, x2: 150, y2: 150});
     }
  });
});
</script>



<script type="text/javascript">


function preview(img, selection) {
    if (!selection.width || !selection.height)
        return;

    $('#x1').val(selection.x1);
    $('#y1').val(selection.y1);
    //$('#x2').val(selection.x2);
    //$('#y2').val(selection.y2);
    $('#w').val(selection.width);
    $('#h').val(selection.height);
}

</script>
</@base.page>