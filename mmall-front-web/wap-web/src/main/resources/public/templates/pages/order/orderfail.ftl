<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>下单失败</title>
    <@meta/>
	<!--@STYLE-->
    <@css/>
    <style type="text/css">
    	.u-payfail{width:2.84rem;}
    	.m-pay .order-tips{font-size:0.56rem;line-height:0.75rem;}
    	.info a{width:6.6rem;background:#F40000;}
    </style>
  </head>
  <body id="index-netease-com">
  	<@topbar title="下单失败" parentPage="/cartlist">
    </@topbar>
    <@module>
    <div class="m-pay">
      <i class="u-payfail"></i>
      <div class="text text-1">
        <p>您的订单提交失败...</p>
      </div>
      <div class="text">
	    	<div class="order-tips ord-fail-icon">
				<p>可能由于如下原因造成：</p>
	            <#if errorType==0>未知错误失败
				<#elseif errorType==1><p>销售火爆，该订单部分商品库存不足</p>
				<#elseif errorType==2><p>收货地址为空</p>
	            <#elseif errorType==3><p>1.不满足起批数量</p><p>2.商品已下架或者不存在</p>
	            <#elseif errorType==4><p>包括非法商品参数</p>
	            <#elseif errorType==5><p>订单总价不能超过1亿</p>
	            <#elseif errorType==6><p>特许经营商家不允许非指定用户下单 </p>
	            <#elseif errorType==7><p>您选择的商品价格发生变动</p>
	            <#elseif errorType==8><p>您有商品不支持当前配送地区</p>
	            <#elseif errorType==9><p>包括超过限购时间或者数量的商品</p>
	            <#elseif errorType==10><p>优惠券无效</p>
	            </#if>
	    	</div>
	    	<div class="info">
	    		<a href="/cartlist">返回进货单</a>
	    	</div>      
      </div>
    </div>
    </@module>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@ga/>
	<!--@DEFINE-->    
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/order/orderfail.js"></script>
  </body>
</html>
</@compress>
</#escape>