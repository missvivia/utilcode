<#assign pageName="schedule-returnnote"/>

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
  <@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"退货单"}/>
  <@module class="m-returnlist" title="退货单">
  <#--remove [{-->
  <#--"key":"province",-->
  <#--"name":"站点",-->
  <#--"type":"SITE"-->
  <#--},{-->
  <#--"key":"brandName",-->
  <#--"name":"品牌名称"-->
  <#--}]-->
  <@searchForm filters=[
  [{
  "key":"state",
  "name":"退货状态",
  "type":"RSTATE",
  "value":returnStatusList,
  "selected":"NEW" <#--默认选中供方未确认状态-->
  },{
  "key":"warehouseId",
  "name":"所在仓库",
  "type":"WHSELECT",
  "items":warehouses
  }],[{
  "key":"poOrderId",
  "name":"PO单编号"
  },{
  "key":"supplierAccount",
  "name":"商家帐号"
  },{
  "key":"returnPoOrderId",
  "name":"退货单号"
  }],[{
  "name":"单据时间",
  "type":"FROMTO",
  "from":{"key":"timestar","value":""},
  "to":{"key":"timeend","value":""}
  },{
  "type":"BTN",
  "margin":false
  }]
  ]/>

  <div class="card_c">
    <div id="m-productlist"></div>
  </div>

  </@module>

</@wrap>

    

<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/returnnote.js?v=1.0.0.0"></script>

</body>
</html>