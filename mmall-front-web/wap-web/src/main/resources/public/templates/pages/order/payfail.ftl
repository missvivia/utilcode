<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>支付失败</title>
    <@meta/>
    <@css/>
  </head>
  <body id="index-netease-com">
  	<@topbar title="网上支付">
    </@topbar>
    <@module>
    <div class="m-pay">
      <i class="u-payfail"></i>
      <div class="text">
        <p class="s-fc1 f-fs16"><#if failType==0>支付失败<#elseif failType==1>支付超时<#elseif (failType==2 || failType==3)>支付失败</#if></p>
        <p class="s-fc4"><#if (failType==0 || failType==1)>该订单已失效，<#elseif failType==2>该订单已被取消，<#elseif failType==3>订单未能完成支付，</#if>请进入 <a class="s-fc11" href="/myorder">我的订单</a> 查看订单状态</p>
      </div>
    </div>
    </@module>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/order/payfail.js"></script>
  </body>
</html>
</@compress>
</#escape>