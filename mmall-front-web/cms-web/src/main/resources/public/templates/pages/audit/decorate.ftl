<#assign pageName="audit-decorate"/>
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
      <@crumbs1 crumbList=[{"txt":"PO品购页审核"}]/>
      <@module class="m-decorate" title="PO品购页审核">
        <@searchForm filters=[
          [{
            "key":"curSupplierAreaId",
            "name":"站点",
            "type":"SITE"
          },{
            "key":"scheduleId",
            "name":"PO编号"
          },{
            "key":"pageId",
            "name":"品购页编号"
          }],

          [{
            "key":"stauts",
            "name":"状态",
            "type":"STATE"
          },{
            "key":"supplierName",
            "name":"商家帐号"
          },{
            "key":"brandName",
            "name":"品牌名称"
          }],

          [{
            "name":"提交日期",
            "type":"FROMTO",
            "from":{"key":"startDate","value":""},
            "to":{"key":"endDate","value":""}
          },{
            "type":"BTN"
          }]
        ]/>
      </@module>
    </@wrap>

    <!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/audit/decorate.js?v=1.0.0.0"></script>
  </body>
</html>