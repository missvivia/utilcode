<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>支付订单</title>
    <@meta/>
    <@css/>
    <style type="text/css">
		.order-success-tips{text-align:center;font-size:16px;margin-top:100px;}
	</style>
  </head>
  <body id="index-netease-com">
  	<@topbar title="支付订单" parentPage="/myorder#state=0"></@topbar>
    <@module>
	<div class="wrap order-success">
		<div class="order-success-tips">
			<p><b>订单已成功提交，请尽快完成付款！</b></p>
			<p>订单总计：<span>${grossPrice}</span>元
		</div>
      <div class="btns" style="height:1.6rem;margin:1.6rem 15px;">
        <a class="u-btn u-btn-1 s-fc5" style="width:45%;float:left;padding:0;margin:0;background:#fff;" href="/myorder#state=1">查看订单</a>
        <a class="u-btn u-btn-1 s-fc5" style="width:45%;float:right;padding:0;margin:0;background:#fff;" href="/index">返回首页</a>
      </div>
	</div>
	<form action="${payOrderParam.requestUrl}" method="post" name="orderForm" id="orderForm">
             
        <input type="hidden" name="version"  value="${payOrderParam.version}">
        <input type="hidden" name="serial_id"  value="${payOrderParam.serial_id}">
        <input type="hidden" name="start_time"  value="${payOrderParam.start_time}">
        <input type="hidden" name="expire_time"  value="${payOrderParam.expire_time}">
        <input type="hidden" name="customer_ip"  value="${payOrderParam.customer_ip!''}">
        <input type="hidden" name="order_details"  value="${payOrderParam.order_details}">
        <input type="hidden" name="total_amount"  value="${payOrderParam.total_amount}">
        <input type="hidden" name="type"  value="${payOrderParam.type}">
        <input type="hidden" name="buyer_id"  value="${payOrderParam.buyer_id}">
        
        <input type="hidden" name="paymethod"  value="${payOrderParam.paymethod!''}">
        <input type="hidden" name="org_code"  value="${payOrderParam.org_code!''}">
        <input type="hidden" name="currency_code"  value="${payOrderParam.currency_code!''}">
        <input type="hidden" name="direct_flag"  value="${payOrderParam.direct_flag!''}">
        <input type="hidden" name="borrowing_marked"  value="${payOrderParam.borrowing_marked!''}">
        <input type="hidden" name="coupon_flag"  value="${payOrderParam.coupon_flag!''}">
        <input type="hidden" name="least_pay"  value="${payOrderParam.least_pay!''}">
        <input type="hidden" name="coupon"  value="${payOrderParam.coupon!''}">
        
        <input type="hidden" name="return_url"  value="${payOrderParam.return_url}">
        <input type="hidden" name="notice_url"  value="${payOrderParam.notice_url}">
        <input type="hidden" name="partner_id"  value="${payOrderParam.partner_id}">
        <input type="hidden" name="cashier_type"  value="${payOrderParam.cashier_type}">
        <input type="hidden" name="split_rule_code"  value="${payOrderParam.split_rule_code}">
        <input type="hidden" name="split_rule"  value="${payOrderParam.split_rule}">
        <input type="hidden" name="bonus"  value="${payOrderParam.bonus}">
        <input type="hidden" name="settle_amount"  value="${payOrderParam.settle_amount}">
        <input type="hidden" name="token"  value="${payOrderParam.token!''}">
        <input type="hidden" name="remark"  value="${payOrderParam.remark}">
        <input type="hidden" name="charset"  value="${payOrderParam.charset}">
         
        <input type="hidden" name="sign_type"  value="${payOrderParam.sign_type}">
        <input type="hidden" name="sign_msg"  value="${payOrderParam.sign_msg}">
        
    </form>
    <input type="hidden" id="timeout"/>
    </@module>

    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    
    <@ga/>
	<script type="text/javascript">
/*		window.onload = function(){
	        function submitForm(){
	             var t = document.getElementById("timeout").value;
	             if(t == "1"){
	                 return;
	             }
	             document.getElementById("timeout").value = 1;
	            
	             setTimeout(function(){
	             	document.getElementById("orderForm").submit();
	             }, 1000);
	        }
	        submitForm();
	    }
*/
	</script>
  </body>
</html>
</@compress>
</#escape>