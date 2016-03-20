<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
 	<#include "../../wrap/common.ftl">
    <#include "../../wrap/cartcommon.ftl">
  <head>
    <@title type="paySucc"/>
    <@css/>
  </head>
  <body>
    <@topbar>
    </@topbar>
    <@cartHeader/>
      <div class="pay-succ wrap">
          <div class="layout">
              <div class="order-tips ord-suc-icon">
              	您已成功完成付款
              </div>
              <div class="info">
              	<#if order??>
	              <#list order as item>
	              	<p>
	              		您已经为订单${item.orderId}成功支付<b>${item.realCash}</b>元
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
    <script type="text/javascript">
    	$(function () {
    		$(".m-top .step li").eq(2).addClass('active');
    	});
    </script>
  </body>
</html>
</@compress>
</#escape>