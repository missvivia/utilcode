<#assign pageName="product-size">
<!DOCTYPE html> 
<html>
<head>
  <#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
</head>
<body>
	<@side />
	<@body>
	<@crumbs parent={"txt":"商品管理","url":"/product/list"} sub={"txt":"尺寸模版"} />
	
	<div class="row">
		<div class="col-sm-12">
      <div class="m-card">
        <h2 class="card_b">
          <span class="glyphicon glyphicon-chevron-down pull-right"></span>
          尺码模版查询
        </h2>
        <div class="card_c">
          <form method="get" class="form-horizontal" id="webForm">
            <div class="form-group">
              <label class="col-sm-1 control-label">商品类目</label>
              <div class="col-sm-3">
                <select name="" class="form-control j-cate">
                	<option value="0">全部</option>
                </select>
              </div>
              <div class="col-sm-3">
                <select name="" class="form-control j-cate">
                	<option value="0">全部</option>
                </select>
              </div>
              <div class="col-sm-3">
                <select name="lowCategoryId" class="form-control j-cate">
                	<option value="0">全部</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-1 control-label">模版名称</label>
              <div class="col-sm-3">
                <input name="sizeTemplateName" type="text" class="form-control">
              </div>
              <label class="col-sm-1 control-label">模版ID</label>
              <div class="col-sm-3">
                <input name="sizeTemplateId" type="text" class="form-control">
              </div>
            </div>
            <div class="form-group" id="form-datePicker">
              <label class="col-sm-1 control-label">添加时间</label>
              <div class="col-sm-3 j-datepick" data-name="stime" data-value=""></div>
              <div class="col-sm-3 j-datepick" data-name="etime" data-value=""  data-time="23:59:59"></div>
            </div>
            <div class="hr-dashed"></div>
            <div class="form-group">
              <div class="col-sm-2 col-sm-offset-1">
                <input type="button" name="btn-submit" value="查询" class="btn btn-primary btn-block">
              </div>
              <div class="col-sm-2">
                <a class="btn btn-inverse btn-block" href="/sizeTemplate/edit?id=0&view=0">添加尺码模版</a>
              </div>
            </div>
          </form>
            <div id="size-list-box">
              
            </div>
        </div>
      </div>
    </div>
	</div>

  </@body>
  <#include "/wrap/widget.ftl" />
  <script>
    window.category = ${stringify(categoryList)};
  </script>
  
<!-- @SCRIPT -->
  <script src="${jslib}define.js?pro=${jspro}"></script>
  <script src="${jspro}page/product/sizesearch.js"></script>
</body>
</html>