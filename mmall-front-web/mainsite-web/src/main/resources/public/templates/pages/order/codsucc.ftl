<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
	<head>
 	<#include "../../wrap/common.ftl">
    <#include "../../wrap/cartcommon.ftl">
    <@title type="orderSucc"/>
    <@css/>
  </head>
  <body>
  	  <@topbar />
      <@top />
      <@mainCategory />
      <div class="ord-succ wrap">
        <div class="layout">
          <div class="order-tips ord-suc-icon">恭喜您，订单提交成功！</div>
          <div class="info">
            <p>
              	请在快递上门时支付货款，订单需审核后方能发货。
            </p>
            <p>
            	您还可以 <a href="/myorder/">查看订单</a><a href='/'>继续购物</a>
            </p>
        </div>
        </div>
      </div>
      <@footer />
      <@copyright />
	  <@cityChange />
      <div class="popup"></div>
      <@js />
  </body>
</html>
</@compress>
</#escape>
