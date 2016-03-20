<#if audit==0>
<#assign pageName="promotion-activity"/>
<#else>
<#assign pageName="promotion-activityaudit"/>
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

<@crumbs parent={"txt":"促销管理","url":'#'} sub={"txt":"活动列表"}/>

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    
    <div class="m-card">
      
    </div>
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        所有活动
        </h2>
      <div class="card_c j-it">
        <#-- coupon list
        <m-actlist on-remove={{this.remove1($event)}}></m-actlist> -->
      </div>
    </div>
  </div>
</div>
</@wrap>

<script>
 var g_areaList = ${areaList![]};
 var g_audit = ${audit};
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/activity.js?v=1.0.0.0"></script>

</body>
</html>