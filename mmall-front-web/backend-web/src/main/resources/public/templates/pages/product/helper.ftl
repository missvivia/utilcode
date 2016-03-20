<#assign pageName="product-helper"/>
<#escape x as x?html>
<@compress single_line=true>

<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <!-- @STYLE -->
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/product/product.css">

</head>
<body>
<@side />
<@body>
<@crumbs parent={"txt":"商品管理","url":'/product/helper'} sub={"txt":"尺码助手"}/>


<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
       尺码助手 
      </h2>
      <div class="card_c">
        <div class="row">
          <div class="col-sm-3 u-vmargin">
           <a class="btn btn-inverse" href="/product/viewhelper">添加尺码助手</a>  
          </div>
        </div>
        <div class="row j-list"></div>
      </div>
    </div>
  </div>
</div>

</@body>
  
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/product/helper.js"></script>
</body>
</html>
</@compress>
</#escape>