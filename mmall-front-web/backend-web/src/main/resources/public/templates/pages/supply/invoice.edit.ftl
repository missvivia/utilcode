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
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/javascript/lib/uploadify/uploadify.css">
  <style>
  .m-form-polist .sep{position:absolute;left:-7px;top:7px;}
  table th, table td{text-align:center;}
  table a{margin-left:5px;}
  .op{margin-bottom:20px;}
  </style>
</head>
<body>
<@side />
<@body>
<@crumbs parent={"txt":"供货管理","url":'/supply/inList'} sub={"txt":"修改发货单"}/>
<div class="row" id="g-bd">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>编辑发货单</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist" id="searchform">
           <input type="hidden" value="${(invoice.shipOrderDTO.shipOrderId)!}" name="shipOrderId" data-required="true"/>
          <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color:red;">*</span>PO单号：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="poOrderId" value="${(invoice.shipOrderDTO.poOrderId)!}" data-required="true" disabled />
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">送货时间：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control datePicker" name="viewShipTime" value="${(invoice.shipOrderDTO.shipTime?number_to_time?string('yyyy-MM-dd'))!''}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color:red;">*</span>预计到货时间：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control datePicker" name="viewExpectArrivalTime" value="${(invoice.shipOrderDTO.expectArrivalTime?number_to_time?string('yyyy-MM-dd'))!''}" data-required="true"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color:red;">*</span>承运商公司名称：</label>
            <div class="col-sm-2">
              <select name="courierCompany" class="form-control"  data-pattern="^((?!请选择).)*$">
                <option value="请选择">请选择</option>
                <option value="顺丰" <#if (invoice.shipOrderDTO.courierCompany)?? && invoice.shipOrderDTO.courierCompany=="顺丰">selected</#if>>顺丰</option>
                <option value="EMS" <#if (invoice.shipOrderDTO.courierCompany)?? && invoice.shipOrderDTO.courierCompany=="EMS">selected</#if>>EMS</option>
              </select>
            </div>
            <label class="col-sm-2 control-label"><span style="color:red;">*</span>运单号：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="expressOddNO" value="${(invoice.shipOrderDTO.expressOddNO)!''}"  data-required="true"/>
            </div>
            <label class="col-sm-1 control-label"><span style="color:red;">*</span>发货箱数：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="shipBoxQTY" value="${(invoice.shipOrderDTO.shipBoxQTY)!}"  data-required="true" disabled/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color:red;">*</span>联系电话：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="courierComPhone" value="${(invoice.shipOrderDTO.courierComPhone)!''}"  data-required="true" disabled/>
            </div>
            <label class="col-sm-1 control-label"><span style="color:red;">*</span>司机姓名：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="driverName" value="${(invoice.shipOrderDTO.driverName)!''}"  data-required="true" disabled/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label"><span style="color:red;">*</span>司机联系电话：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="driverPhone" value="${(invoice.shipOrderDTO.driverPhone)!''}"  data-required="true" disabled/>
            </div>
            <label class="col-sm-1 control-label"><span style="color:red;">*</span>车牌：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="licensePlate" value="${(invoice.shipOrderDTO.licensePlate)!''}"  data-required="true" disabled/>
            </div>
          </div>
          <div class="col-sm-offset-2">
            <a class="btn btn-primary" id="nextBtn" href="javascript:void(0);">下一步</a>
            <a class="btn btn-primary" href="/supply/inList">返回列表</a>
          </div>
        </form>
      </div>
    </div>
    <!-- /card -->
  </div>
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>导出商品明细</h2>
      <div class="card_c f-dn">
        <div class="text-left op">
          <a class="btn btn-primary" id="exportBtn">导入出仓商品明细</a>
        </div>
        <div id="skuListInInvoice-box"></div>
        <div class="text-left op">
          <a class="btn btn-primary" id="prevBtn" href="javascript:void(0);">上一步</a>
          <a class="btn btn-primary" href="/supply/inList">返回列表</a>
          <a class="btn btn-primary" id="submitBtn" href="javascript:void(0);">提交</a>
        </div>
      </div>
    </div>
    <!-- /card -->
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
<script src="${jspro}page/supply/invoice.new.js"></script>
</body>
</html>
</@compress>
</#escape>