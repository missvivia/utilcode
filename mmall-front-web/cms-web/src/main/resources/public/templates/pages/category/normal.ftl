<#assign pageName="category-normal"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/item.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"分类管理","url":'#'} sub={"txt":"商品分类列表"}/>
<!-- card -->
<div class="row">
	<div class="col-sm-12">
		<div class="m-card">
			<h2 class="card_b">
				<span class="glyphicon glyphicon-chevron-down pull-right"></span>
      				商品分类管理
      		</h2>
      		
      		<div class="card_c">
      			<div id="normallist"></div>
      		</div>
     	</div>
  	</div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/category/normal.js?v=1.0.0.1"></script>

</body>
</html>