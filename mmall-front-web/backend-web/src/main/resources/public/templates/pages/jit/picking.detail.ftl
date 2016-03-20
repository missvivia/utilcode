<#assign pageName="jit-pkList"/>
<@compress single_line=true>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <#escape x as x?html>
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
<@wrap>
<@crumbs parent={"txt":"JIT管理","url":'#'} sub={"txt":"拣货单详情"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>拣货单详情</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal">
          <div class="form-group">
            <label class="col-sm-2 control-label">拣货单号：</label>
            <div class="col-sm-3">
              <span class="sep">${(pkObject.pickDto.pickOrderId)!''}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">创建时间：</label>
            <div class="col-sm-3">
              <span class="sep">${(pkObject.pickDto.createTime?number_to_time?string("yyyy-MM-dd"))!}</span>
            </div>
            <label class="col-sm-1 control-label">拣货总数：</label>
            <div class="col-sm-2">
              <span class="sep">${(pkObject.pickDto.pickTotalQuantity)!}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">首次导出时间：</label>
            <div class="col-sm-3">
              <span class="sep"><#if pkObject.pickDto.firstExportTime!=0>${(pkObject.pickDto.firstExportTime?number_to_time?string("yyyy-MM-dd"))!}</#if></span>
            </div>
            <label class="col-sm-1 control-label">导出数：</label>
            <div class="col-sm-2">
              <span class="sep">${(pkObject.pickDto.exportTimes)!}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">入库仓库：</label>
            <div class="col-sm-3">
              <span class="sep">${(pkObject.pickDto.warehouseForm.warehouseName)!}</span>
            </div>
          </div>
          <div class="form-group">
              <label class="col-sm-2 control-label">出库状态：</label>
              <div class="col-sm-3">
                  <span class="sep"><#if pkObject.pickDto.pickState=='UNPICK'>未出仓<#elseif pkObject.pickDto.pickState=='PICKING'>拣货中<#elseif pkObject.pickDto.pickState=='PICKED'>已出仓</#if></span>
              </div>
              <label class="col-sm-1 control-label">入库状态：</label>
              <div class="col-sm-2">
                  <span class="sep"><#if pkObject.pickDto.shipState=='RECEIVED'>已入库，仓库已接收<#elseif pkObject.pickDto.shipState=='UNSEDN'>未入库，未推送给仓库
                  <#elseif pkObject.pickDto.shipState=='SEND'>未入库，已推送给仓库<#elseif pkObject.pickDto.shipState=='CANCEL'>未入库，超时取消</#if></span>
              </div>
          </div>
          <#-- 
          <div class="form-group">
            <label class="col-sm-1 control-label">拣货单号：</label>
            <div class="col-sm-2">
              <span class="sep">${(pkObject.pickDto.pickOrderId)!''}</span>
            </div>
            <label class="col-sm-1 control-label">发货单号：</label>
            <div class="col-sm-2">
              <span class="sep">${(pkObject.pickDto.shipOrderId)!''}</span>
            </div>
            <label class="col-sm-1 control-label">发货状态：</label>
            <div class="col-sm-2">
              <span class="sep"><#if pkObject.pickDto.shipOrderId??>已发货<#else>未发货</#if></span>
            </div>
          </div>-->
        </form>
      </div>
    </div>
    <!-- /card -->
  </div>
  <#-- 
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>发货信息</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal">
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
  </div>-->
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>发货明细</h2>
      <div class="card_c">
        <div id="skuListInPick-box"></div>
        <div class="text-left">
          <a class="btn btn-primary" id="exportBtn" data-id="${(pkObject.pickDto.pickOrderId)!''}" href="/jit/exportPK/${pkObject.pickDto.pickOrderId}" target="_blank">导出</a>
          <#if pkObject.pickDto.pickState=='UNPICK'><a class="btn btn-primary" id="confirmPick" data-id="${(pkObject.pickDto.pickOrderId)!''}"  href="javascript:void(0)">确认出仓</a></#if>
          <a class="btn btn-primary" href="/jit/pkList">返回</a>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>
<script type="text/javascript">
  <#noescape>
  var g_skuList = ${JsonUtils.toJson((pkObject.pickSkuDtoList)![])};
  </#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/pkdetail.js"></script>
</body>
</html>
</#escape>
</@compress>
