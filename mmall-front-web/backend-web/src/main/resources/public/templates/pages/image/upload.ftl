<#assign pageName="image"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/image.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"图片空间","url":"/image/upload"} sub={"txt":"图片上传"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        	图片上传
      </h2>
      <div class="card_c" id="upload">
      	<ul class="nav nav-tabs j-box f-dn">
      		<li><a>普通上传</a></li>
      		<li><a>水印设置</a></li>
      	</ul>
      	<div class="j-box">
	      	<div class="panel-body">上传到分类<select class="f-mgl" id="category"><#list categoryList as item><option value="${item.id}">${item.name}</options></#list></select></div>
	        <div id="uploadbox" class="m-uplodbox j-flag">
	        	<div class="j-flag">
		        	<div class="uploadicn"><image src="/res/images/icon_upload.png"></div>
		        	<label class="j-flag btn btn-primary btn-block">上传图片</label>
	        	</div>
	        </div>
	        <div class="f-tac"><input class="j-flag btn btn-primary" value="开始上传" type="button"><input  type="button" class="j-flag btn btn-primary f-mgl" value="清空图片"></div>
        </div>
        <#assign waterPrint ={
	"url":"/res/images/watermark.jpg",
	"base":1,
	"left":2,
	"top":2}
/>
        <div class="j-box panel-body m-watermark f-dn">
	      	<div class="row">
	      		<div class="col-md-4">
	      			<div class="m-model">
	      				<img src="${(waterPrint.url)!'/res/images/watermark.jpg'}" id="waterpringimg"/>
	      			</div>
	      		</div>
	      		<div class="col-md-7">
	      			<div>
	      				<div class="form-horizontal">
	      				<form id="waterpring">
	      					<div class="">
		      					<div class="form-group">浏览上传水印图片</div>
		      					<div class="form-group"><label class="btn btn-primary" name="upload">上传</label></div>
		      					<div class="form-group">Tips：目前仅支持上传png格式的水印图，文件大小不超过2M，文件不能超过700px*700px。</div>
	      					</div>
	      					<div  class="form-group"> 
	      						<div class="col-md-2">基准点:</div> 
		      						<input type="radio" name="base" id="top-left" <#if (waterPrint.base)??&&waterPrint.base==1>checked="checked"</#if>>
		      						<label for="top-left">左上</label>
		      						<input type="radio" name="base" id="middle" <#if (waterPrint.base)??&&waterPrint.base==2>checked="checked"</#if>>
		      						<label for="middle">中间</label>
		      						<input type="radio" name="base" id="right-bottom" <#if (waterPrint.base)??&&waterPrint.base==3>checked="checked"</#if>>
		      						<label for="right-bottom">右下</label>
		      					</div>
			      					<input type="hidden" name="oleft" value="${(waterPrint.left)!2}"/>
			      					<input type="hidden" name="otop" value="${(waterPrint.top)!2}"/>
			      					<input type="hidden" name="obase" value="${(waterPrint.base)!1}"/>
		      					<div class="form-group"> <div class="col-md-2">位置:</div><label>左边距</label><input type="text" manxlength="2" class="f-mgl pos" name="left">% </div>
		      					<div class="form-group"> <div class="col-md-2"></div><label>右边距</label><input type="text" manxlength="2" class="f-mgl pos"  name="top">% </div>
		      				</div>
	      				</form>
      					</div>
	      			</div>
	      		</div>
	      	</div>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/image/upload.js?v=1.0.0.1"></script>

</body>
</html>