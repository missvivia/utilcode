<#assign pageName="supply-inList"/>
<#escape x as x?html>
<@compress single_line=true>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style>
  .m-form-polist .sep{position:absolute;left:-7px;top:7px;}
  table th, table td{text-align:center;}
  table a{margin-left:5px;}
  </style>
</head>
<body>
<@side />
<@body>
<@crumbs parent={"txt":"供货管理","url":'/supply/pkList'} sub={"txt":"拣货单详情"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>发货信息</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-1 control-label">送货单号：</label>
            <div class="col-sm-2">
              <span class="sep">${(invoice.shipOrderDTO.shipOrderId)!''}</span>
            </div>
            <label class="col-sm-2 control-label">PO单号：</label>
            <div class="col-sm-4">
              <span class="sep">${(invoice.shipOrderDTO.poOrderId)!''}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">送货时间：</label>
            <div class="col-sm-2">
              <span class="sep"><#if invoice.shipOrderDTO.shipTime &gt; 0>${(invoice.shipOrderDTO.shipTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}</#if></span>
            </div>
            <label class="col-sm-2 control-label">预计到货时间：</label>
            <div class="col-sm-2">
              <span class="sep"><#if invoice.shipOrderDTO.expectArrivalTime &gt;0>${(invoice.shipOrderDTO.expectArrivalTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}</#if></span>
            </div>
          </div>
        </form>
      </div>
    </div>
    <!-- /card -->
  </div>
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>发货人信息</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-1 control-label">运单号：</label>
            <div class="col-sm-2">
              <span class="sep">${(invoice.shipOrderDTO.expressOddNO)!''}</span>
            </div>
            <label class="col-sm-1 control-label">发货箱数：</label>
            <div class="col-sm-1">
              <span class="sep">${(invoice.shipOrderDTO.shipBoxQTY)!}</span>
            </div>
            <label class="col-sm-2 control-label">承运商公司名称：</label>
            <div class="col-sm-2">
              <span class="sep">${(invoice.shipOrderDTO.courierCompany)!''}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">联系电话：</label>
            <div class="col-sm-2">
              <span class="sep"> ${(invoice.shipOrderDTO.courierComPhone)!''}</span>
            </div>
            <label class="col-sm-1 control-label">司机姓名：</label>
            <div class="col-sm-1">
              <span class="sep">${(invoice.shipOrderDTO.driverName)!''}</span>
            </div>
            <label class="col-sm-2 control-label">司机联系电话：</label>
            <div class="col-sm-2">
              <span class="sep"> ${(invoice.shipOrderDTO.driverPhone)!''}</span>
            </div>
            <label class="col-sm-1 control-label">车牌：</label>
            <div class="col-sm-1">
              <span class="sep">${(invoice.shipOrderDTO.licensePlate)!''}</span>
            </div>
          </div>
        </form>
      </div>
    </div>
    <!-- /card -->
  </div>
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>出仓/发货明细</h2>
      <div class="card_c">
        <div id="skuListInInvoice-box"></div>
        <div class="text-left">
          <a class="btn btn-primary" href="/supply/exportIn/${(invoice.shipOrderDTO.shipOrderId)!''}/">导出出仓明细</a>
          <#if invoice.shipOrderDTO.shipState=="UNSHIPPED">
          <a class="btn btn-primary" data-id="${(invoice.shipOrderDTO.shipOrderId)!''}" id="confirmBtn">确认出仓</a>
          </#if>
          <a class="btn btn-primary" href="/supply/inList">返回发货列表</a>
        </div>
      </div>
    </div>
  </div>
</div>
</@body>
  
<script type="text/javascript">
  <#noescape >
  var g_skuList = ${JsonUtils.toJson(invoice.shipSkuDTOs)![]};
  </#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/supply/indetail.js"></script>
</body>
</html>
</@compress>
</#escape>