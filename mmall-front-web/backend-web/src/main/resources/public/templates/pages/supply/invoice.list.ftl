<#assign pageName="supply-inList"/>

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
  .op{margin-bottom:10px;}
  </style>
</head>
<body>
<@side />
<@body>
<@crumbs parent={"txt":"供货管理","url":'/supply/inList'} sub={"txt":"发货单列表"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>发货单查询</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist" id="searchform">
          <div class="form-group">
            <label class="col-sm-2 control-label">发货单号：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="shipOrderId" />
            </div>
            <label class="col-sm-2 control-label">PO单号：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control" name="poOrderId"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">状态：</label>
            <div class="col-sm-2">
              <select name="shipStatus" id="poid" class="form-control">
                <option value="-1">请选择</option>
                <option value="0">等待出仓</option>
                <option value="1">已出仓</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">送货时间：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control datePicker" name="shipStartTime"/>
            </div>
            <div class="col-sm-2">
              <span class="sep">至</span><input type="text" class="form-control datePicker" name="shipEndTime" data-time="23:59:59"/>
            </div>
            <label class="col-sm-2 control-label">预计到货时间：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control datePicker" name="expectedArrivalTimeOfStart"/>
            </div>
            <div class="col-sm-2"> 
              <span class="sep">至</span><input type="text" class="form-control datePicker" name="expectedArrivalTimeOfEnd" data-time="23:59:59"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">实际到货时间：</label>
            <div class="col-sm-2">
              <input type="text" class="form-control datePicker" name="actualArrivalTimeOfStart"/>
            </div>
            <div class="col-sm-2">
              <span class="sep">至</span><input type="text" class="form-control datePicker" name="actualArrivalTimeOfEnd" data-time="23:59:59"/>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-1 col-sm-offset-2">
              <a class="btn btn-primary btn-block" id="submitBtn">查询</a>
            </div>
          </div>
        </form>
        <div class="text-left op">
          <a class="btn btn-warning btn-lg" href="/supply/invoice/new">新建发货单</a>
        </div>
          <table class="table table-striped">
            <thead>
              <tr>
                <th class="col-sm-2">操作</th>
                <th>发货单号</th>
                <th>PO单号</th>
                <th>发货时间</th>
                <th>预计到货时间</th>
                <th>实际到货时间</th>
                <th>状态</th>
                <th>创建时间</th>
              </tr>
            </thead>
            <tbody id="inlist">
            </tbody>
          </table>
          <div class="text-right">
              <div id="pager"></div>
            </div>
          </div>
      </div>
    </div>
    <!-- /card -->
  </div>
</div>
</@body>
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/supply/inlist.js"></script>
</body>
</html>