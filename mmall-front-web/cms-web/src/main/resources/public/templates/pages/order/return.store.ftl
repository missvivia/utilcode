<#assign pageName="order-returnstore"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/order.css?v=1.0.0.0">
</head>
<body>
<#assign 
  data = {
  "companyList":[{"name":"顺风","id":1},{"name":"天天","id":2},{"name":"xxx","id":3}],
  "statusList":[{"name":"未收货","id":1},{"name":"部分收货","id":2},{"name":"已收货","id":3}]
}
/>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"退货管理","url":'#'} sub={"txt":"退货退款管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        退货查询
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal" id="webForm">
          <div class="form-group">
            <label class="col-sm-2 control-label">订单号</label>
            <div class="col-sm-2">
              <input name="orderNum" type="text" class="form-control">
            </div>
            <label class="col-sm-1 control-label">快递单号</label>
            <div class="col-sm-2">
              <input name="expressNum" type="text" class="form-control">
            </div>
            <label class="col-sm-1 control-label">快递公司</label>
            <div class="col-sm-2">
              <select name="expressCompany" id="" class="form-control">
                <#list data.companyList as item>
                  <option value="${item.id}">${item.name}</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="form-group" id="form-datePicker">
            <label class="col-sm-2 control-label">寄送时间</label>
            <div class="col-sm-2 j-datepick" data-name="addTime" data-value=""></div>
            <div class="col-sm-2 j-datepick" data-name="endTime" data-value="" data-time="23:59:59"></div>
            <label class="col-sm-2 control-label">收获状态</label>
            <div class="col-sm-2">
              <select name="status" id="" class="form-control">
                <option>全部</option>
                <#list data.statusList as item>
                  <option value="${item.id}">${item.name}</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">商品货号</label>
            <div class="col-sm-2">
              <input name="productCode" type="text" class="form-control">
            </div>
            <label class="col-sm-1 control-label">条形码</label>
            <div class="col-sm-2">
              <input name="barCode" type="text" class="form-control">
            </div>
            <label class="col-sm-1 control-label">商品名称</label>
            <div class="col-sm-2">
              <input name="productName" type="text" class="form-control">
            </div>
          </div>
          <div class="hr-dashed"></div>
          <div class="form-group">
            <div class="col-sm-2 col-sm-offset-2">
              <input type="button" name="btn-submit" value="查询" class="btn btn-primary btn-block">
            </div>
          </div>
        </form>
          <div id="return-list-box">
            
          </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/return.store.js?v=1.0.0.0"></script>

</body>
</html>