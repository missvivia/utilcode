<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<#setting datetime_format="yyyy-MM-dd HH:mm"/>
<html>
  <head>
    <title>订单详情页</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/order.css?v=1.0.0.0">
  </head>
  <body id="index-netease-com">
    <@topbar title="${order2VO.orderFormState.desc}" parentPage="/myorder#state=0">
    </@topbar>

    <div class="g-bd m-order-detail">
        <div class="m-state">
          <section class="u-state">
            <i class="f-fl">订单号</i>
            <i class="f-fl" id="orderId">${order2VO.orderId}</i>
            <#if order2VO.payCloseCD &gt; 0>
            <div class="paytime">订单支付 <span id="countdown" data-countdown="${order2VO.payCloseCD}"></span></div>
            </#if>
            </p>
          </section>
			<#--已取消-->
			<#if order2VO.orderFormState.intValue == 21>
	          <section class="u-state"><i>取消原因</i><i>${order2VO.cancelReason}</i></section>
	          <section class="u-state"><i>取消时间</i><i>${order2VO.cancelTimeStr}</i></section>
	        <#elseif order2VO.orderFormState.intValue == 11>
	        <#--交易完成-->
	        	 <section class="u-state"><i>收货时间</i><i>${order2VO.confirmTime?number_to_datetime}</i></section>
	        	 <section class="u-state"><i>发货时间</i><i>${order2VO.omsTime?number_to_datetime}</i></section>
	        </#if>
           
          
          <section class="u-state u-state-1 f-cb">
            <i class="address">收货信息</i>
            <div class="address">
              <p class="one">
              	<span>${order2VO.expInfo.consigneeName}</span>
              	<span><#if (order2VO.expInfo.consigneeMobile)?? && order2VO.expInfo.consigneeMobile!=''>${order2VO.expInfo.consigneeMobile}</#if></span>
              </p>
              <p><#if (order2VO.expInfo.consigneeTel)?? && order2VO.expInfo.consigneeTel!='--'>${order2VO.expInfo.consigneeTel}</#if></p>
              <p><#if (order2VO.expInfo.province)?? && (order2VO.expInfo.province == '北京市' || order2VO.expInfo.province == '上海市' || order2VO.expInfo.province == '天津市' || order2VO.expInfo.province == '重庆市')>
                    ${order2VO.expInfo.province}
                  <#else>
                    ${order2VO.expInfo.province} ${order2VO.expInfo.city}
                  </#if>${(order2VO.expInfo.section)!''} ${(order2VO.expInfo.street)!''} ${order2VO.expInfo.address} <#if (order2VO.expInfo.zipcode)?? && order2VO.expInfo.zipcode!=''>，${order2VO.expInfo.zipcode}</#if></p>
            </div>
          </section>


        </div>

      </div>


      <div class="g-bd m-order-detail" id="detail">

          <div class="w-order">
				<div class="store"><a href="${order2VO.cartList[0].storeUrl}">${order2VO.cartList[0].storeName}</a></div>
              <#list order2VO.cartList as x>
                <div class="m-order-box">
                <#list x.orderSkuList as sku>  
                  <div class="w-good f-cb">
                    <div class="f-fl f-cb goods">
						<div class="img">
							<div class="m-img">
								<a href="/product/detail?skuId=${sku.productId}"><img class="u-loading-1" data-src="${sku.skuSPDTO.picUrl}"/></a>
							</div>
						</div>
						<div class="note f-cb">
							<div class="up">
								<a href="/product/snapShot?skuId=${sku.productId}&orderId=${order2VO.orderId}&userId=${order2VO.userId}" class="snapShot">[交易快照]</a>
								<a href="/product/detail?skuId=${sku.productId}">${sku.skuSPDTO.productName!""}</a>
							</div>
							<div class="down"><p class="num">x ${x.count}</p></div>
						</div>
                    </div>
                    <div class="price"><i>¥${sku.oriRPrice?string("0.00")}</i></div>
                  </div>
                </#list>
                </div>
              </#list>
        </div>
        <#-- <#if couponName??>
            <div class="m-fav">
              <i class="u-quan"></i><section>${couponName}</section>
            </div>
        </#if>
        <#list promotionList as p>
          <div class="m-fav">
            <i class="u-quan"></i><section>${p}</section>
          </div>
        </#list> -->
    </div>
	<div class="g-hd m-order-detail" style="margin-bottom:10px;">
		<div class="m-state">
          <section class="u-state">
            <i>支付方式</i>
            <i class="right">${order2VO.orderFormPayMethod.desc}</i>
            <#if !!order2VO.canCOD>
            <a id="modify" data-orderId="${order2VO.orderId}" href="javascript:void(0);" class="u-btn">修改</a>
            </#if>
          </section>
    	</div>	
	</div>
	<div class="g-hd m-order-detail" style="margin">
          <div class="last m-state">
              <section class="u-state">
                <i>商品金额</i>
                <i class="right price">¥${order2VO.cartOriRPrice?string("0.00")}</i>
              </section>
          </div>
	        <#if order2VO.couponVO??>
	        <div class="m-promotion m-state" style="margin:0;">
	          <div class="item f-cb">
	            <div class="u-quan"></div>
	            <div class="tt">优惠</div>
	            <div class="right red">${order2VO.couponSPrice?string("0.00")}</div>
	          </div>
	        </div>
	        </#if>              
            <div class="m-state">
                <div class="realPay">实付款：<span class="high">¥${order2VO.realCash?string("0.00")}</span></div>
              	<div class="orderTime">下单时间：${order2VO.orderDate}</div>
            </div>
          </div>	
	</div>
	<#if order2VO.orderFormState.intValue == 0 || order2VO.orderFormState.intValue == 10 || order2VO.orderFormState.intValue == 21>
	<div style="height:75px;">
		<div class="btn-box">
		 	<#if order2VO.orderFormState.intValue == 0>
		 		<a class="u-btn u-btn-two" id="cancel-btn" data-oid="${order2VO.orderId}" intValue="${order2VO.orderFormState.intValue}" href="javascript:void(0);">取消订单</a>
		 		<a class="u-btn u-btn-two pay-btn"id="pay-btn" data-oid="${order2VO.parentId}" href="javascript:;">需去电脑端完成支付哦！</a>
		 	</#if>
		 	<#if order2VO.orderFormState.intValue == 21>
		 		<a class="u-btn u-btn-two" id="delete-btn" data-oid="${order2VO.orderId}" href="javascript:void(0);">删除</a>
		 	<#elseif order2VO.orderFormState.intValue == 10>
		 		<a class="u-btn u-btn-two" id="confirm-btn" data-oid="${order2VO.orderId}" href="javascript:void(0);">确认收货</a>
		 	</#if>
		</div>
	</div>
	</#if>
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
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/order/orderDetail.js"></script>
  </body>
</html>
</@compress>
</#escape>