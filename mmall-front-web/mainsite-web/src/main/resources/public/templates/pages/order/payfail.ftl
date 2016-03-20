<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <#include "../../wrap/common.ftl">
    <#include "../../wrap/cartcommon.ftl">
  <head>
    <@title type="payfail"/>
    <@css/>
  </head>
  <body>
    <@topbar>
    </@topbar>
    <@cartHeader/>
    	<div class="ord-fail wrap">
          <div class="layout">
              <div class="order-tips ord-fail-icon">
              	支付失败
              </div>
              <div class="info">
              	<#if order??>
	              <#list order as item>
	              	<p>
	              		订单号为${item.orderId}，支付金额为<b>${item.realCash}</b>元，支付失败!
	              	</p>
	              	<p>
	              		<a href="/myorder/detail?orderId=${item.orderId}">查看订单</a>
	              	</p>
	              	</#list>
            	</#if>
              </div>
              <div class="go-shop">
	            <a href="/">继续购物</a>
	          </div>
          </div>
      </div>
    <@footer/>
    <@copyright />
    <@cityChange />
    <div class="popup"></div>
    <@js />
  </body>
</html>
</@compress>
</#escape>
