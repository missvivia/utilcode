<#assign pageName="supply-pkList"/>

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
<@crumbs parent={"txt":"供货管理","url":'/supply/pkList'} sub={"txt":"拣货单查询"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>拣货单查询</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist" id="searchform">
          <div class="form-group">
            <label class="col-sm-1 control-label">PO单编号：</label>
            <div class="col-sm-2">
              <input name="poOrderId" type="text" class="form-control"/>
            </div>
            <label class="col-sm-1 control-label">商品清单编号：</label>
            <div class="col-sm-2">
              <input name="pickOrderId" type="text" class="form-control"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">导出状态：</label>
            <div class="col-sm-2">
              <select name="exportStatus" class="form-control">
                <option value="-1">请选择</option>
                <option value="0">未导出</option>
                <option value="1">已导出</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">创建日期：</label>
            <div class="col-sm-2">
              <input name="createStartTime" type="text" class="form-control datePicker"/>
            </div>
            <div class="col-sm-2">
              <span class="sep">至</span><input name="createEndTime" type="text" class="form-control datePicker" data-time="23:59:59"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">开售日期：</label>
            <div class="col-sm-2">
              <input name="commodityStartTimeOfStart" type="text" class="form-control datePicker"/>
            </div>
            <div class="col-sm-2">
              <span class="sep">至</span><input name="commodityStartTimeOfEnd" type="text" class="form-control datePicker" data-time="23:59:59"/>
            </div>
            <label class="col-sm-1 control-label">停售日期：</label>
            <div class="col-sm-2">
              <input name="commodityEndTimeOfStart" type="text" class="form-control datePicker"/>
            </div>
            <div class="col-sm-2">
              <span class="sep">至</span><input name="commodityEndTimeOfEnd" type="text" class="form-control datePicker" data-time="23:59:59"/>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-1 col-sm-offset-1">
              <a class="btn btn-primary btn-block" id="submitBtn">查询</a>
            </div>
          </div>
        </form>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>导出状态</th>
              <th class="col-sm-2">操作</th>
              <th>商品清单编号</th>
              <th>商品总数</th>
              <th>PO单编号</th>
              <th>档期时期</th>
              <th>首次导出时间</th>
              <th>导出数</th>
            </tr>
          </thead>
          <tbody id="pklist">
          </tbody>
        </table>
        <div class="text-right">
          <div id="pager"></div>
        </div>
      </div>
    </div>
    <!-- /card -->
  </div>
</div>
</@body>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/supply/pklist.js"></script>
</body>
</html>