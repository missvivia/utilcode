<#assign pageName="order-return"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/order.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"退货退款管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        退货退款管理
      </h2>
      <div class="card_c">
        <m-returnform></m-returnform>
      </div>
    </div>
  </div>
</div>
<div style="display:none" id="jstTemplate">
<#noparse>
  <textarea name="ntp" id="return-pass-win">
    <div class="m-winform">
      <form id="passForm">
        <div class="line">
          <div>确定为用户的退货申请退款？</div>
        </div>
        <div class="line f-cb">
          商品退款
          <input type="text" name="cashPriceToUser" value="" data-required="true">元
        </div>
        <div class="line f-cb">
          <label class="">操作说明</label>
          <div>
            <textarea class="u-textarea" data-required="true" name="extInfo"><&#47;textarea>
          </div>
        </div>
      </form>
    </div>
    <div class="m-winbot">
      <div class="btns">
        <span class="btn btn-primary j-flag">确定</span>
        <span class="f-mgl btn btn-primary j-flag">取消</span>
      </div>
    </div>
  </textarea>
</#noparse>
</div>
<#include "/wrap/widget.ftl" />
</@wrap>

<script>
  window.__typeList__ = ${JsonUtils.toJson(data.searchTypeList)};
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/return.js?v=1.0.0.0"></script>

</body>
</html>