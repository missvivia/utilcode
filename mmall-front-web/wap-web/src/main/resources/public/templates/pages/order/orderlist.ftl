<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include '../../wrap/3g.common.ftl'>
<html>
  <head>
    <title>我的订单页</title>
    <@meta/>
    <@css/>
    <link rel='stylesheet' type='text/css' href='/src/css/page/orderList.css?v=1.0.0.4'>
  </head>
  <body id='orderlist-netease-com'>
    <@topbar title="我的订单" parentPage = "/profile/index">
    </@topbar>

    <div class="g-hd g-orderlist-hd navParent">
      <div class="m-nav" id="tab-box"></div>
    </div>
    <div class="g-bd g-orderlist-bd orderParent">
      <div id="order-body"></div>
    </div>
	<div id="page"><span></span></div>
	<form action="" method="post" name="orderForm" id="orderForm">
	     <input type="hidden" name="version"  value="">
	     <input type="hidden" name="serial_id"  value="">
	     <input type="hidden" name="start_time"  value="">
	     <input type="hidden" name="expire_time"  value="">
	     <input type="hidden" name="customer_ip"  value="">
	     <input type="hidden" name="order_details"  value="">
	     <input type="hidden" name="total_amount"  value="">
	     <input type="hidden" name="type"  value="">
	     <input type="hidden" name="buyer_id"  value="">
	     
	     <input type="hidden" name="paymethod"  value="">
	     <input type="hidden" name="org_code"  value="">
	     <input type="hidden" name="currency_code"  value="">
	     <input type="hidden" name="direct_flag"  value="">
	     <input type="hidden" name="borrowing_marked"  value="">
	     <input type="hidden" name="coupon_flag"  value="">
	     <input type="hidden" name="least_pay"  value="">
	     <input type="hidden" name="coupon"  value="">
	     
	     <input type="hidden" name="return_url"  value="">
	     <input type="hidden" name="notice_url"  value="">
	     <input type="hidden" name="partner_id"  value="">
	     <input type="hidden" name="cashier_type"  value="">
	     <input type="hidden" name="split_rule_code"  value="">
	     <input type="hidden" name="split_rule"  value="">
	     <input type="hidden" name="bonus"  value="">
	     <input type="hidden" name="settle_amount"  value="">
	     <input type="hidden" name="token"  value="">
	     <input type="hidden" name="remark"  value="">
	     <input type="hidden" name="charset"  value="">
	      
	     <input type="hidden" name="sign_type"  value="">
	     <input type="hidden" name="sign_msg"  value="">
	</form>	
    <@template>

      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->

    </@template>
	<@ga/>
    <script src='${jslib}define.js?${jscnf}'></script>
    <script src='${jspro}page/order/index.js'></script>

    <!--script src="http://10.240.140.216:8080/target/target-script-min.js"></script-->
  </body>
</html>
</@compress>
</#escape>