<#assign pageName="order-query"/>
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

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"订单查询"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        交易信息查询
        <a class="glyphicon glyphicon-search u-wsearch" href="/orders/query"></a>
      </h2>
      <div class="card_c f-cb">
        <div class="row">
          <div class="col-sm-6">
            <div class="panel panel-default">
              <div class="panel-heading">基本信息</div>
              <div class="panel-body">
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">交易序列号</div>
                  <div class="col-sm-9">${data.tradeNum}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">交易ID</div>
                  <div class="col-sm-9">${data.tradeId}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">用户名</div>
                  <div class="col-sm-9">${data.userName}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">用户ID</div>
                  <div class="col-sm-9">
                    <a href="">${data.userId}</a>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">支付状态</div>
                  <div class="col-sm-9">${data.status}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">交易支付方式</div>
                  <div class="col-sm-9">
                    <#if data.payMethod??>
                    <#list data.payMethod as item>
                      <div>${item.method} $${item.pay}</div>
                    </#list>
                    </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">交易发起时间</div>
                  <div class="col-sm-9">${data.startTime?number_to_datetime}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">交易结束时间</div>
                  <div class="col-sm-9">${data.endTime?number_to_datetime}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <div class="panel panel-default">
              <div class="panel-heading">该交易支付记录相关的订单</div>
              <div class="panel-body">
                <m-tradelist></m-tradelist>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<#include "/wrap/widget.ftl" />
<script>
  window.__typeList__ = ${stringify(data)}
  window.__type__ = '${RequestParameters["type"]}';
  window.__key__ = '${RequestParameters["key"]}';
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/trade.detail.js?v=1.0.0.0"></script>

</body>
</html>