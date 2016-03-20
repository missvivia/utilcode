<#assign pageName="message-index"/>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/product_manage.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@body>

<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"商品新建"}/>

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <a class="btn btn-primary" href="/coupon/edit">添加活动</a>
    <div class="m-card m-card-msg">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        所有活动
        </h2>
      <div class="card_c">
        <#-- coupon list -->
        <div class="m-clst">
          <m-msglist></m-msglist>
        </div>
      </div>
    </div>
  </div>
</div>
</@body>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/message/index.js"></script>

</body>
</html>