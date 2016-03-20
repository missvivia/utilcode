<#assign pageName="product-viewhelper"/>
<#escape x as x?html>

<#if helperVO?? == false>
  <#assign helperVO = {} />
</#if>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <!-- @STYLE -->
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/product/product.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@body>
<@crumbs parent={"txt":"商品管理","url":'/product/helper'} sub={"txt":"尺码助手编辑"}/>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
       尺码助手编辑 
      </h2>
      <div class="card_c j-hview">
      </div>
    </div>
  </div>
</div>
</@body>
<script>
  <#noescape>
  window.__data__ = ${stringify(helperVO)}
  </#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/product/vhelper.js"></script>

</body>
</html>
</#escape>