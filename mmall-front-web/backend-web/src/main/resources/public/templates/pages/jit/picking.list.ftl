<#assign pageName="jit-pkList"/>

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
<@wrap>
<@crumbs parent={"txt":"JIT管理","url":'#'} sub={"txt":"拣货单查询"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>拣货单查询</h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-polist" id="searchform">
          <div class="form-group">
            <label class="col-sm-1 control-label">拣货日期：</label>
            <div class="col-sm-2 j-datepick" data-name="createStartTime" data-value=""></div>
            <div class="col-sm-2 j-datepick" data-name="createEndTime" data-value="" data-time="23:59:59">
              <span class="sep">至</span>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-1 col-sm-offset-1">
              <a class="btn btn-primary btn-block" id="submitBtn">查询</a>
            </div>
          </div>
        </form>
        <div id="pick-list-box"></div>
      </div>
    </div>
    <!-- /card -->
  </div>
</div>
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/pklist.js"></script>
</body>
</html>