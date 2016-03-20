<#assign pageName="image-category"/>
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
<@crumbs parent={"txt":"图片空间","url":"/image/upload"} sub={"txt":"分类管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        	分类管理
      </h2>
      <div class="card_c">
      	<div id="category"></div>
      <#--
        <div id="categorybox" class="m-categorybox">
	        <div class="panel panel-default">
		       <div class="panel-heading">
			       <div class="row">
		        		<div class="col-md-7">图片分类</div><div class="col-md-3">操作</div>
		        	</div>
		       </div>
		       <#list categoryList as item>
		         <div class="row panel-body">
	        		<div class="col-md-7"><span class="j-name name" data-id="${item.id}">${item.name}</span><input type="text" value="${item.name}" data-id="${item.id}" class="j-edt edt-ipt"></div><div class="col-md-3"><span class="j-edit f-csp">修改</span> <#if item.name!='默认分类'><span class="j-del f-csp">删除</span></#if></div>
	        	 </div>
	        	</#list >
	        	<div class="j-flag" id="jstbox">
	        		
	        	</div>
		     </div>
    		<div class="btns f-tac"><span class="btn btn-primary j-flag">新建</span><span class="f-mgl btn btn-primary j-flag">保存</span></div>
        </div>
        -->
      </div>
    </div>
  </div>
</div>
</@wrap>


<script>
	var categoryList = ${stringify(categoryList)};
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/image/category.js"></script>

</body>
</html>