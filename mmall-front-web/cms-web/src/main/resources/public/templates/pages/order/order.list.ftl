<#assign pageName="order-query"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"订单查询"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        订单信息查询
      </h2>
      <div class="card_c f-cb">
        <div class="col-sm-12 j-list">
          <#-- <m-orderlist></m-orderlist> -->
        </div>
      </div>
    </div>
  </div>
</div>

</@wrap>
<script>
  <#if RequestParameters["key"]??>
  window.__key__ = '${RequestParameters["key"]}';
  <#else>
  window.__key__ = 0;
  window.__startTime__ = '${RequestParameters["startTime"]}';
  window.__endTime__ = '${RequestParameters["endTime"]}';
  </#if>
  window.__type__ = '${RequestParameters["type"]}';
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/order.list.js?v=1.0.0.0"></script>

</body>
</html>