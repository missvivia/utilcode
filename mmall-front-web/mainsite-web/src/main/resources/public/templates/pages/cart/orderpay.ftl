<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <#include "../../wrap/cartcommon.ftl">
    <@title type="orderPay"/>
    <meta charset="utf-8"/>
    <@css/>
    <link href="/src/css/front.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/member.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<@topbar />
    <@cartHeader/>
    <div class="wrap order-success">
		<div class="order-success-tips">
			<p><b>订单已成功提交，请尽快完成付款！</b></p>
			<p>订单总计：<span>102.80</span>元   订单编号：<a href="#">10032123337</a></p>
		</div>
		<div class="order-pay">
			<div class="clearfix">
				<div class="l change-pay">
					<p>
						已选择支付方式：
					</p>
					<div class="tab clearfix">
						<img src="/src/images/pay/bank_alipay.gif" />
						<i></i>
						<a href="javascript:void(0);">修改</a>
					</div>
				</div>
				<div class="r alipay">
					<h3>使用支付宝钱包扫一扫即可付款</h3>
					<img src="../test-pic/pic-28.jpg" />
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
	</div>
	<@footer />
	<@copyright />
	<@fixedSide />
    <@fixedSideOrder />
    <@fixedSideReplenish />
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script type="text/javascript">
    	$(function () {

    		// 当前步骤
            $(".m-top .step li").eq(2).addClass('active');

    		var payType = $(".pay-type"),
    			terraceBox = payType.find(".terrace"),
    			cardBox = payType.find(".card"),
    			terraceDom = "",
    			cardDom = "",
    			terraceArr = [
	    			{
	    				"name" : "bank_alipay",
		    			"url"  : "/src/images/pay/bank_alipay.gif"
	    			},
	    			{
	    				"name" : "bank_ChinaUnionPay",
		    			"url"  : "/src/images/pay/bank_ChinaUnionPay.gif"
	    			},
	    			{
	    				"name" : "bank_Tenpay",
		    			"url"  : "/src/images/pay/bank_Tenpay.gif"
	    			}
	    		],
	    		cardArr = [
	    			{
	    				"name" : "bank_01",
		    			"url"  : "/src/images/pay/bank_01.gif"
	    			},
	    			{
	    				"name" : "bank_02",
		    			"url"  : "/src/images/pay/bank_02.gif"
	    			},
	    			{
	    				"name" : "bank_03",
		    			"url"  : "/src/images/pay/bank_03.gif"
	    			},
	    			{
	    				"name" : "bank_04",
		    			"url"  : "/src/images/pay/bank_04.gif"
	    			},
	    			{
	    				"name" : "bank_05",
		    			"url"  : "/src/images/pay/bank_05.gif"
	    			},
	    			{
	    				"name" : "bank_06",
		    			"url"  : "/src/images/pay/bank_06.gif"
	    			},
	    			{
	    				"name" : "bank_07",
		    			"url"  : "/src/images/pay/bank_07.gif"
	    			},
	    			{
	    				"name" : "bank_08",
		    			"url"  : "/src/images/pay/bank_08.gif"
	    			},
	    			{
	    				"name" : "bank_09",
		    			"url"  : "/src/images/pay/bank_09.gif"
	    			},
	    			{
	    				"name" : "bank_10",
		    			"url"  : "/src/images/pay/bank_10.gif"
	    			},
	    			{
	    				"name" : "bank_11",
		    			"url"  : "/src/images/pay/bank_11.gif"
	    			},
	    			{
	    				"name" : "bank_12",
		    			"url"  : "/src/images/pay/bank_12.gif"
	    			},
	    			{
	    				"name" : "bank_13",
		    			"url"  : "/src/images/pay/bank_13.gif"
	    			},
	    			{
	    				"name" : "bank_14",
		    			"url"  : "/src/images/pay/bank_14.gif"
	    			},
	    			{
	    				"name" : "bank_15",
		    			"url"  : "/src/images/pay/bank_15.gif"
	    			},
	    			{
	    				"name" : "bank_16",
		    			"url"  : "/src/images/pay/bank_16.gif"
	    			},
	    			{
	    				"name" : "bank_17",
		    			"url"  : "/src/images/pay/bank_17.gif"
	    			},
	    			{
	    				"name" : "bank_20",
		    			"url"  : "/src/images/pay/bank_20.gif"
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
    	});
    </script>
</body>
</html>
</@compress>
</#escape>