<#assign pageName="order-topay"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/order.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"到付审核"}/>

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        到付审核
      </h2>
      
      <div class="card_c j-topayform">
        
      </div>
    </div>
  </div>
</div>
<#include "/wrap/widget.ftl" />
</@wrap>

<script>
  <#if pages[0].name??>
  window.__typeList__ = ${JsonUtils.toJson(data.searchTypeList)};
  </#if>
  window.__Date__ = new Date();
  window.__HH__ = __Date__.getHours();
  window.__MM__ = __Date__.getMinutes();
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/topay.js?v=1.0.0.0"></script>

</body>
</html>