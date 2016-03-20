<#assign pageName="item-brand"/>
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
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"品牌列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       品牌资料管理
      </h2>
      
      <div class="card_c">
      <form id="search-form">
      	 <#-- <div class="form-group" ><input type="text" value="" name="key" placeholder="品牌名称"/><input type="button" class="f-mgl btn btn-primary" name="btn-search" value="搜索"/></div> -->
      </form>
      <div id="brandlist"></div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/brand.js?v=1.0.0.0"></script>

</body>
</html>