<#if audit==0>
<#assign pageName="promotion-coupon"/>
<#else>
<#assign pageName="promotion-couponaudit"/>
</#if>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/product_manage.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>

<@crumbs parent={"txt":"促销管理","url":'#'} sub={"txt":"优惠券列表"}/>

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        所有优惠券
        </h2>
      <div class="card_c j-it">
        <#-- coupon list 
        <m-couponlist></m-couponlist>-->
      </div>
    </div>
  </div>
</div>

</@wrap>

<script>
 var g_audit = ${audit};
</script>
<#include "/wrap/widget.ftl" />
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/coupon.js?v=1.0.0.0"></script>

</body>
</html>