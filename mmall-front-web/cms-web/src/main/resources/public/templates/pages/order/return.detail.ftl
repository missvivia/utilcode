<#assign pageName="order-return"/>
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
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"退货退款管理"}/>
<#if data?? == false>
<#assign 
  data = {
    "requestTime":1413099512510,
    "returnId":"xxxx",
    "orderId":1111,
    "pay":12,
    "expressPay":12,
    "returnPay":12,
    "expressCompany":"tiantian",
    "expressNum":"xxx",
    "returnMethod":{"name":"xx","extInfo":"xxxx"},
    "dealTime":1413099512510,
    "status":"以退货",
    "abnomalInfo":"xxx",
    "acturalReturn":12,
    "dealer":"dashu",
    "remark":"xxxxx"
  }
/>
</#if>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        退货申请详情
      </h2>
      <div class="card_c f-cb">
        <div class="row">
          <div class="col-sm-6">
            <div class="panel panel-default">
              <div class="panel-heading">基本信息</div>
              <div class="panel-body">
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">申请时间</div>
                  <div class="col-sm-9">${data.requestTime?number_to_datetime}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">售后编号</div>
                  <div class="col-sm-9">${data.returnId}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">原订单号</div>
                  <div class="col-sm-9"><a href="/order/orderdetail?type=0&key=${data.orderId}">${data.orderId}</a></div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">实付金额</div>
                  <div class="col-sm-9">￥${data.pay}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">运费</div>
                  <div class="col-sm-9">￥${data.expressPay}</div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">申请退款金额</div>
                  <div class="col-sm-9">
                    ￥${data.returnPay}
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">退货物流公司</div>
                  <div class="col-sm-9"><#if data.expressCompany??>${data.expressCompany}</#if></div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">退货物流单号</div>
                  <div class="col-sm-9"><#if data.expressNum??>${data.expressNum}</#if></div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">退款方式</div>
                  <div class="col-sm-9">
                    <#list data.returnMethod as item>
                    <span>
                      <#if item.name??>
                      ${item.name}
                      </#if>
                    </span>
                    <span>
                      <#if item.extInfo??>
                      (${item.extInfo})
                      </#if>
                    </span>
                    </#list>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-sm-6">
            <div class="panel panel-default">
              <div class="panel-heading">处理信息</div>
              <div class="panel-body">
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">最后处理时间</div>
                  <div class="col-sm-9">
                    <#if data.dealTime??>
                    ${data.dealTime?number_to_datetime}
                    </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">当前状态</div>
                  <div class="col-sm-9">
                    <#if data.status??>
                    ${data.status}
                    </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">异常说明</div>
                  <div class="col-sm-9">
                    <#if data.abnomalInfo??>
                    ${data.abnomalInfo}
                    </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">实际退款金额</div>
                  <div class="col-sm-9">
                  	<#if data.acturalReturn??> ${data.acturalReturn} </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3"></div>
                  <div class="col-sm-9">
                    <#list data.acturalReturnMethod as item>
                    ${item.name} ￥${item.price}
                    </#list>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">退回优惠券</div>
                  <div class="col-sm-9">
                  <#if data.returnOrderCoupon??>
                    ${data.returnOrderCoupon.couponCode!''}
                  </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">审核人</div>
                  <div class="col-sm-9">
                    <#if data.dealer??>
                    ${data.dealer}
                    </#if>
                  </div>
                </div>
                <div class="row f-mgtb">
                  <div class="col-sm-3 f-tar">操作说明</div>
                  <div class="col-sm-9">
                    <#if data.remark??>
                    ${data.remark}
                    </#if>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <div class="panel panel-default">
              <div class="panel-heading">商品信息</div>
              <div class="panel-body">
                <m-productlist></m-productlist>
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
  window.__returnId__ = '${RequestParameters["returnId"]}';
  window.__userId__ = '${RequestParameters["userId"]}';
</script>
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/return.detail.js?v=1.0.0.0"></script>

</body>
</html>