<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <#include "../../wrap/common.ftl">
  <head>
    <@title content="下单页"/>
    <meta charset="utf-8"/>
    <@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/buy.css">
    <link rel="stylesheet" type="text/css" href="/src/css/layer.css">
  </head>
  <body id="index-netease-com">
    <@topbar isTrade = true>
    <@tradeStep step =2/>
    </@topbar>
    <@module>
      <div class="g-bd f-ff3">
        <div class="m-tipbar s-fc1" id="tipbar">
          <div>
            <b class="i-time"></b>
            <span class="text">请在 <span class="s-fc5 f-fw1" id="remain"></span> 内提交订单，超时您的商品就被别人抢走啦</span>
            <b class="i-warn"></b>
          </div>
          <div class="f-dn">
            逛超时啦，请 <a href="/cart" class="s-fc9">返回购物袋</a> 添加有库存的商品，尽快结算哦！
          </div>
        </div>
      </div>
      <div class="g-bd f-ff3">
        <div class="m-membox">
          <div id="address"></div>
          <div id="address-none"></div>
          <div id="order-detail"></div>
        </div>
      </div>
      <#-- Page Content Here -->

    </@module>
    <@footer/>
    
    <#noescape>
    <script>
      var cartIds = '${cartIds}';
      var requestId = '${requestId}';
      var currTime = ${currTime!0};
      var cartEndTime = ${cartEndTime!0};
    </script>
    </#noescape>
    
    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/buy/index.js"></script>
  </body>
</html>
</@compress>
</#escape>
