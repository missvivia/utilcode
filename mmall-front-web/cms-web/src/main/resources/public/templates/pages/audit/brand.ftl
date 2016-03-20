<#assign pageName="audit-brand"/>
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
      <@crumbs parent=crumbsParent sub={"txt":"品牌页装修审核"}/>
      <@module class="m-brand" title="品牌页装修审核">
        <m-tab on-change={{this.onchange($event)}}></m-tab>
        <@searchForm filters=[
          [{
            "key":"curSupplierAreaId",
            "name":"站点",
            "type":"SITE"
          },{
            "key":"brandName",
            "name":"品牌名称"
          },{
            "type":"BTN",
            "margin":false
          }]
        ]/>
      </@module>
    </@wrap>

    <!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/audit/brand.js?v=1.0.0.0"></script>
  </body>
</html>