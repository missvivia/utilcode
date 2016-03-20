<#assign pageName="jit-poreport"/>

<!doctype html>
<html lang="zh-cn">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/jit/poreport.css">
</head>
<body>
<@side />
<@wrap>
    <@crumbs parent={"txt":"JIT管理","url":"#"} sub={"txt":"PO单报表"}/>
    <div class="row">
      <div class="col-sm-12">
        <div class="m-card">
          <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>PO单报表查询</h2>
          <div class="card_c">
            <form method="get" class="form-horizontal m-form1" id="searchform">
              <div class="form-group">
                <label class="col-sm-1 control-label">PO单编号：</label>
                <div class="col-sm-7">
                  <input type="text" class="form-control ztag" placeholder="多个PO，请用逗号（,）隔开" name="poOrderId"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label">创建日期：</label>
                <div class="col-sm-3 j-datepick" data-name="createStartTime" data-value=""></div>
                <label class="col-sm-1 control-label">开售日期：</label>
                <div class="col-sm-3 j-datepick" data-name="saleStartTime" data-value=""></div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label"></label>
                <div class="col-sm-3 j-datepick" data-name="createEndTime" data-value="" data-time="23:59:59"></div>
                <label class="col-sm-1 control-label"></label>
                <div class="col-sm-3 j-datepick" data-name="saleEndTime" data-value="" data-time="23:59:59"></div>
              </div>
              <div class="form-group">
                <div class="col-sm-2 col-sm-offset-1">
                  <input type="button" value="查询" class="btn btn-primary btn-block ztag" name="btn-submit"/>
                </div>
              </div>
            </form>
            <div class="hr-dashed"></div>
            <div id="size-list-box"></div>
          </div>
        </div>
      </div>
    </div>
         
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/poreport.js"></script>

</body>
</html>