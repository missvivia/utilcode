<#assign pageName="schedule-return"/>
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
  <@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"退货管理"}/>
  <@module class="m-return" title="退货单">
    <@searchForm filters=[
      [{
        "key":"poOrderId",
        "name":"PO单编号"
      }],
      [{
        "type":"BTN",
        "margin":false
      }]
    ]/>
    <div class="card_c">
        <div id="m-productlist"></div>
    </div>

  </@module>

</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/return.js?v=1.0.0.0"></script>

</body>
</html>