<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <#include "../../wrap/cartcommon.ftl">
    <@title type="gotoPay"/>
    <meta charset="utf-8"/>
    <@css/>
</head>
<body>
	<@topbar />
    <@cartHeader/>
	<div class="wrap order-success">
		<div class="order-success-tips">
			<p><b>订单已成功提交，请尽快完成付款！</b></p>
			<p>订单总计：<span>${grossPrice}</span>元
		</div>
		<#--
		<div class="order-pay">
			<div class="clearfix">
				<div class="l change-pay">
					<p>
						已选择支付方式：
					</p>
					<div class="tab clearfix">
						<img src="/src/pay-images/bank_alipay.gif" />
						<i></i>
						<a href="javascript:void(0);">修改</a>
					</div>
				</div>
				<div class="r alipay">
					<h3>使用支付宝钱包扫一扫即可付款</h3>
					<img src="/src/test-pic/pic-35.jpg" />
				</div>
			</div>
			<div class="pay-type">
				<h3>支付平台：</h3>
				<ul class="clearfix terrace">
				</ul>
				<h3>银行卡：</h3>
				<ul class="clearfix card">
				</ul>
			</div>
			<div class="pay-btn">
				<input type="button" value="立即去付款" class="" />
			</div>
		</div>
		-->
		
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
        <input type="hidden" name="split_rule_code"  value="${payOrderParam.split_rule_code!''}">
        <input type="hidden" name="split_rule"  value="${payOrderParam.split_rule!''}">
        <input type="hidden" name="bonus"  value="${payOrderParam.bonus}">
        
        <input type="hidden" name="settle_amount"  value="${payOrderParam.settle_amount}">
        <input type="hidden" name="token"  value="${payOrderParam.token!''}">
        <input type="hidden" name="remark"  value="${payOrderParam.remark}">
        <input type="hidden" name="charset"  value="${payOrderParam.charset}">
         
        <input type="hidden" name="sign_type"  value="${payOrderParam.sign_type}">
        <input type="hidden" name="sign_msg"  value="${payOrderParam.sign_msg}">
        
    </form>
    <input type="hidden" id="timeout"/>
</body>
</html>
<@footer />
<@copyright />
<@fixedSide />
<@fixedSideOrder />
<@fixedSideReplenish />
<@cityChange />
<@js />
<script type="text/javascript">
    
    	$(function () {
    	
    		// 当前步骤
            $(".m-top .step li").eq(2).addClass('active');

            function submitForm(){
                 var t = $("#timeout").val();
                 if(t == "1"){
                     return;
                 }
                     
                 $("#timeout").val("1");
                
                 setTimeout(function(){
                      $("#orderForm").submit();
                 }, 1000);
            }
            submitForm();

    	<#--	var payType = $(".pay-type"),
    			terraceBox = payType.find(".terrace"),
    			cardBox = payType.find(".card"),
    			terraceDom = "",
    			cardDom = "",
    			terraceArr = [
	    			{
	    				"name" : "bank_alipay",
		    			"url"  : "/src/pay-images/bank_alipay.gif"
	    			},
	    			{
	    				"name" : "bank_ChinaUnionPay",
		    			"url"  : "/src/pay-images/bank_ChinaUnionPay.gif"
	    			},
	    			{
	    				"name" : "bank_Tenpay",
		    			"url"  : "/src/pay-images/bank_Tenpay.gif"
	    			}
	    		],
	    		cardArr = [
	    			{
	    				"name" : "bank_01",
		    			"url"  : "/src/pay-images/bank_01.gif"
	    			},
	    			{
	    				"name" : "bank_02",
		    			"url"  : "/src/pay-images/bank_02.gif"
	    			},
	    			{
	    				"name" : "bank_03",
		    			"url"  : "/src/pay-images/bank_03.gif"
	    			},
	    			{
	    				"name" : "bank_04",
		    			"url"  : "/src/pay-images/bank_04.gif"
	    			},
	    			{
	    				"name" : "bank_05",
		    			"url"  : "/src/pay-images/bank_05.gif"
	    			},
	    			{
	    				"name" : "bank_06",
		    			"url"  : "/src/pay-images/bank_06.gif"
	    			},
	    			{
	    				"name" : "bank_07",
		    			"url"  : "/src/pay-images/bank_07.gif"
	    			},
	    			{
	    				"name" : "bank_08",
		    			"url"  : "/src/pay-images/bank_08.gif"
	    			},
	    			{
	    				"name" : "bank_09",
		    			"url"  : "/src/pay-images/bank_09.gif"
	    			},
	    			{
	    				"name" : "bank_10",
		    			"url"  : "/src/pay-images/bank_10.gif"
	    			},
	    			{
	    				"name" : "bank_11",
		    			"url"  : "/src/pay-images/bank_11.gif"
	    			},
	    			{
	    				"name" : "bank_12",
		    			"url"  : "/src/pay-images/bank_12.gif"
	    			},
	    			{
	    				"name" : "bank_13",
		    			"url"  : "/src/pay-images/bank_13.gif"
	    			},
	    			{
	    				"name" : "bank_14",
		    			"url"  : "/src/pay-images/bank_14.gif"
	    			},
	    			{
	    				"name" : "bank_15",
		    			"url"  : "/src/pay-images/bank_15.gif"
	    			},
	    			{
	    				"name" : "bank_16",
		    			"url"  : "/src/pay-images/bank_16.gif"
	    			},
	    			{
	    				"name" : "bank_17",
		    			"url"  : "/src/pay-images/bank_17.gif"
	    			},
	    			{
	    				"name" : "bank_20",
		    			"url"  : "/src/pay-images/bank_20.gif"
	    			}
	    		],
	    		flag = true,
	    		changePay = $(".change-pay .tab"),
	    		payBtn = $(".order-pay .pay-btn input");

	    	// 创建支付平台列表
    		for ( var i = 0; i < terraceArr.length; i++) {
    			terraceDom += "<li data-name='"+ terraceArr[i].name +"'><img src='"+ terraceArr[i].url +"' /></li>";
    		}
    		terraceBox.append(terraceDom);

    		// 创建银行卡列表
    		for ( var i = 0; i < cardArr.length; i++) {
    			cardDom += "<li data-name='"+ cardArr[i].name +"'><img src='"+ cardArr[i].url +"' /></li>";
    		}
    		cardBox.append(cardDom);

    		payType.on("click", "li", function () {
    			var name = $(this).data("name"),
    				url = $(this).find("img").attr("src");
    			payType.find("li").each( function () {
    				$(this).removeClass("active");
    			});
    			$(this).addClass("active");
    			changePay.find("img").attr("src", url);
    			payType.toggle();
    			payBtn.attr("data-name", name);
    			if (name == "bank_alipay") {
    				$(".order-pay .alipay").show();
    			} else {
    				$(".order-pay .alipay").hide();
    			}
    		});

    		changePay.find("*").on("click", function (e) {
    			$(this).closest(".tab").toggleClass("active");
    			payType.toggle();
    			if (flag) {
    				$(this).closest(".tab").find("a").html("确定");
    				flag = false;
    			} else {
    				$(this).closest(".tab").find("a").html("修改");
    				flag = true;
    			}
    			$(".order-pay .alipay").hide();
    		});
    		-->
    	});
    	
    </script>
</@compress>
</#escape>