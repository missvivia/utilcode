<#assign pageName="finance-salequery"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/finance.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"财务管理","url":'#'} sub={"txt":"销售查询"}/>
<!-- card -->
  <m-queryForm></m-queryForm>
</@wrap>

<script>
  window.__companyList__ = ${stringify(express)};
  window.__storeList__ = ${stringify(warehouses)};
  window.__orderPermitted__ = ${stringify(orderPermitted) };
  window.__freightPermitted__ = ${stringify(freightPermitted)};
  window.__payPermitted__ = ${stringify(payPermitted)};
</script>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/finance/salequery.js?v=1.0.0.0"></script>
</body>
</html>