<#assign pageName="image-manage"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/image.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"图片空间","url":"/image/upload"} sub={"txt":"图片管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        	图片管理
      </h2>
      <div class="card_c">
      		<form class="form-horizontal" id="searchFrm">
		      	<div class="form-group">
			      <label class="col-sm-2 control-label">图片分类：</label><div class="col-sm-2">
			      	<select class="form-control j-flag" id="category" name="categoryId" data-type="number"><#list categoryList as item><option value="${item.id}">${item.name}</option></#list></select>
			      </div>
			     </div>
			     <div class="hr-dashed"></div>
		        <div id="managebox" class="managebox">
		        	<div class="row">
		        		<div class="">
		        			<div class="m-act panel-body" id="act">
		        				<ul class="list-inline f-fr">
									<li>按图片名称搜索<input type="text" class="name" name="name"/></li>
									<li>上传时间
										<span class="m-date">
											<input type="text" name="startTime"  class="m-ipt" data-type="date"/>
									      	<button type="button" class="btn btn-default" name="startTime1">            
									      		<span class="glyphicon glyphicon-calendar"></span>          
									      	</button>        
								    	</span>
								    </li>
								    <li> 一 </li>
									<li><span class="m-date">
											<input type="text" name="endTime" class="m-ipt" data-type="date" data-time="23:59:59"/>
										<button type="button" class="btn btn-default" name="endTime1">            
								      		<span class="glyphicon glyphicon-calendar"></span>          
								      	</button> 
								      	</span>
									</li>
									<li><input type="button" class="btn btn-primary m-ipt" value="搜索" name="searchbtn"/></li>
								</ul>
								</form>
		        				<ul class="list-inline f-fl">
								  <li><input type="checkbox" id="select-all" class=" j-flag"><label class="f-mgl" for="select-all">全选</label></li>
								  <li class=" j-flag f-csp">移动</li>|
								  <li class="j-flag f-csp">删除</li>
								</ul>
		        			</div>
		        			<div class="m-imagebox" id="imagebox">
		        				<ul class="j-flag box f-cb"></ul>
		        				<div class="j-flag"></div>
		        			</div>
		        			
		        		</div>
		        	</div>
		        </div>
        	</form>
      </div>
    </div>
  </div>
</div>

</@wrap>
<#noparse>
<div style="display:none" id="jst-template">
	<textarea name="ntp" id="ntp-image-item">
		<li class="m-imgitem f-fl">
			<div class="imgcse">
				<img class="j-flag" src="/res/images/cut.jpg" data-height="4381" data-width="1440"/>
				<a class="j-flag hover"></a>
				<a class="j-flag checked"></a>
			</div>
			<div class="j-flag f-tac">1440*4381</div>
			<div class="act"><a class="j-flag">复制链接</a><a class="f-mgl j-flag">复制代码</a></div>
		</li>
	</textarea>
	<textarea name="ntp" id="ntp-move-image-window">
		<div class="m-movewin">
			<div class="wincnt">
				图片分类 <select class="j-flag"></select>
			</div>
			<div class="f-tac winbnts"><span class="btn btn-primary j-flag">确定</span><span class="f-mgl btn btn-primary j-flag">取消</span></div>
		</div>
	</textarea>
</div>
</#noparse>
<#include "/wrap/widget.ftl" />

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/image/manage.js?v=1.0.0.0"></script>

</body>
</html>