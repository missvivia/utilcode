<#assign pageName="jit-polist"/>

<!doctype html>
<html lang="zh-cn">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/jit/polist.css">
</head>
<body>
<@side />
<@wrap>
    <@crumbs parent={"txt":"JIT管理","url":"/jit/polist"} sub={"txt":"PO列表"} />
    <div class="row">
      <div class="col-sm-12">
        <div class="m-card">
          <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>PO单查询</h2>
          <div class="card_c">
            <form method="get" class="form-horizontal m-form-polist" id="searchform">
              <div class="form-group">
                <label class="col-sm-1 control-label">PO单编号：</label>
                <div class="col-sm-2">
                  <input type="text" class="form-control ztag" name="poOrderId"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label">创建日期：</label>
                <div class="col-sm-2 j-datepick" data-name="createStartTime" data-value=""></div>
                <div class="col-sm-2 j-datepick" data-name="createEndTime" data-value="" data-time="23:59:59">
                  <span class="sep">至</span>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label">开售日期：</label>
                <div class="col-sm-2 j-datepick" data-name="saleStartTime" data-value=""></div>
                <div class="col-sm-2 j-datepick" data-name="saleEndTime" data-value="" data-time="23:59:59">
                  <span class="sep">至</span>
                </div>
              </div>
              <div class="form-group">
                <div class="col-sm-2 col-sm-offset-1">
                  <a class="btn btn-primary btn-block" id="searchBtn">查询</a>
                </div>
              </div>
            </form>
            <div class="m-info ztag"></div>
            <div class="hr-dashed"></div>
            <table class="table table-striped ztag">
              <thead>
                <tr>
                  <th>PO单编号</th>
                  <th>档期时间</th>
                  <th>总供货量</th>
                  <th>销售总数</th>
                  <th>未拣货数量</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody id="polist">
              </tbody>
            </table>
            <div class="text-right">
                <div id="pager"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/polist.js"></script>

</body>
</html>