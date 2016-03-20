<#assign pageName="item-model"/>
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
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"商品模型列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	商品模型管理
      </h2>
      <div class="card_c">
      	  <div class="row from-group">
				<div class="col-sm-3 f-fl">
					<a class="btn btn-primary" href="/item/createmodel">新建模型</a>
					<!--
					<button class="btn btn-primary">删除</button>
					-->
				</div>
				<form id="search-form">
					<div class="col-sm-9 f-fr">
						<span id="starttime" class="form-inline"></span>
						<span id="endtime" class="form-inline"></span>
						<span class="form-inline">
							<input type="text" class="form-control" id="searchValue" name="searchValue" placeholder="请输入名称或ID"/>
						</span>
						<!--
						<input type="hidden" name="startTime" id="startTime"/>
						<input type="hidden" name="endTime" id="endTime"/>
						-->
						<input type="button" value="查找" class="btn btn-primary j-flag" name="btn-search" id="searchBtn"/>
					</div>
				</form>
		  </div>
	      <div id="modellist"></div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/promodel.js?v=1.0.0.1"></script>
</body>
</html>