<#assign pageName="audit-productlist"/>

<!doctype html>
<html lang="en">
  <head>
<#include "/wrap/common.ftl" />
<#include "./audit.ftl" />
    <meta charset="UTF-8">
    <title>${siteTitle} - ${page.title}</title>
    <#include "/wrap/css.ftl">
    <!-- @STYLE -->
    <link rel="stylesheet" href="/src/css/page/audit.css">
  </head>
  <body>
    <@side />
    <@wrap>
      <@crumbs parent=crumbsParent sub={"txt":"PO商品审核"}/>
      <@module class="m-product-list" title="PO商品清单审核">
        <@searchForm filters=[
          [{
            "key":"productName",
            "name":"商品名称"
          },{
            "key":"goodsNo",
            "name":"商品货号"
          },{
            "key":"barCode",
            "name":"条形码"
          }],
          [{
            "key":"status",
            "name":"商品审核状态",
            "type":"STATE"
          },{
            "type":"BTN"
          }]
        ]/>
      </@module>
    </@wrap>
    
    <!-- @NOPARSE -->
	<script>
	var g_poId=${poId!0},g_disabled=${disabled!0},g_isReady=${isReady!0};
	</script>
	<!-- /@NOPARSE -->
    <!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/audit/productlist.js"></script>
  </body>
</html>