<#assign pageName="audit-product"/>

<!doctype html>
<html lang="en">
  <head>
<#include "/wrap/common.ftl" />
<#include "./audit.ftl" />
    <meta charset="UTF-8">
    <title>${siteTitle} - ${page.title}</title>
    <#include "/wrap/css.ftl">
    <!-- @STYLE -->
    <link rel="stylesheet" href="/src/css/page/audit.css?v=1.0.0.0">
  </head>
  <body>
    <@side />
    <@wrap>
      <@crumbs parent=crumbsParent sub={"txt":"PO商品资料审核"}/>
      <@module class="m-product" title="PO商品资料审核">
        <@searchForm filters=[
          [{
            "key":"province",
            "name":"站点",
            "type":"SITE"
          },{
            "key":"status",
            "name":"状态",
            "type":"STATE"
          },{
            "key":"poId",
            "name":"PO编号"
          }],
          [{
            "type":"BTN"
          }]
        ]/>
      </@module>
    </@wrap>
    
    <!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/audit/po.js?v=1.0.0.0"></script>
  </body>
</html>