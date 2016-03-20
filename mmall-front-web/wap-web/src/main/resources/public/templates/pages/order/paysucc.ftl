<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>支付成功</title>
    <@meta/>
    <@css/>
  </head>
  <body id="index-netease-com">
  	<@topbar title="网上支付">
    </@topbar>
    <@module>
    <div class="m-pay">
      <i class="u-succ"></i>
      <div class="text text-1">
        <p class="s-fc10 f-fs16"><#if type==0>支付成功<#elseif type=1>订单已支付，无需再次支付</#if></span></p>
        <#if hasHB><span class="s-fc1">获得${hbValue!""}元红包<a class="s-fc11" href="/coupon/redpacketlist">去查看</a></span></#if>
      </div>
      <div class="btns">
        <a class="u-btn u-btn-1 s-fc5" href="/myorder/detail?orderId=${order.orderId}">查看订单</a>
        <a class="u-btn u-btn-1 s-fc5" href="/index">回首页逛逛</a>
      </div>
    </div>
    </@module>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <#if type!=2>
    <!-- @noparse -->
    <script>
        window['_smq'].push(['custom', '结算成功', window['g_ursname'],${order.orderId} , ${order.totalCash}]);
    </script>
    <!-- /@noparse -->
    </#if>
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/order/paysucc.js"></script>
  </body>
</html>
</@compress>
</#escape>