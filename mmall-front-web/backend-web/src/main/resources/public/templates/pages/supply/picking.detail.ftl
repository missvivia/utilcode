<#assign pageName="supply-pkList"/>
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
<@crumbs parent={"txt":"供货管理","url":'/supply/inList'} sub={"txt":"拣货单详情"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>拣货单详情</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-1 control-label">PO单编号：</label>
            <div class="col-sm-2">
              <span class="sep">${(pkObject.pickDto.poOrderId)!''}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">商品清单编号：</label>
            <div class="col-sm-2">
              <span class="sep">${(pkObject.pickDto.pickOrderId)!''}</span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">档期：</label>
            <div class="col-sm-3">
              <span class="sep">${(pkObject.pickDto.comStartTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}--${(pkObject.pickDto.comEndTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}</span>
            </div>
            <label class="col-sm-1 control-label">商品总数：</label>
            <div class="col-sm-1">
              <span class="sep">${(pkObject.pickDto.pickTotalQuantity)!}</span>
            </div>
            <label class="col-sm-1 control-label">导出次数：</label>
            <div class="col-sm-1">
              <span class="sep">${(pkObject.pickDto.exportTimes)!}</span>
            </div>
            <label class="col-sm-2 control-label">首次导出时间：</label>
            <div class="col-sm-2">
              <span class="sep"><#if (pkObject.pickDto.firstExportTime) &gt; 0>${(pkObject.pickDto.firstExportTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}</#if></span>
            </div>
          </div>
        </form>
        <div id="skuListInPick-box"></div>
        <div class="text-left">
          <a class="btn btn-primary" id="exportBtn" data-id="${(pkObject.pickDto.poOrderId)!''}" href="/supply/exportPK/${pkObject.pickDto.poOrderId}" target="_blank">导出</a>
          <a class="btn btn-primary" href="/supply/pkList">返回</a>
        </div>
      </div>
    </div>
    <!-- /card -->
  </div>
</div>
</@body>
<script type="text/javascript">
  <#noescape >
  var g_skuList = ${JsonUtils.toJson((pkObject.pickSkuDtoList)![])};
  </#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/supply/pkdetail.js"></script>
</body>
</html>
</@compress>
</#escape>