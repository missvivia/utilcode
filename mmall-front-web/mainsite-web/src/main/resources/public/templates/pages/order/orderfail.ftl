<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
 	<#include "../../wrap/common.ftl">
    <#include "../../wrap/cartcommon.ftl">
  <head>
    <@title content="orderFail"/>
    <@css/>
  </head>
  <body id="index-netease-com">
    <@topbar>
    </@topbar>
    <@cartHeader/>
    <div class="ord-fail wrap">
    	<div class="layout">
        	<div class="order-tips ord-fail-icon">
			    <#if errorType==0>未知错误失败
				<#elseif errorType==1>销售火爆，该订单部分商品库存不足
				<#elseif errorType==2>收货地址为空
	            <#elseif errorType==3>该订单包含不满足起批的商品或者商品下架啦或者包含进货单不存在的商品
	            <#elseif errorType==4>订单提交失败，输入参数不对
	            <#elseif errorType==5>订单提交失败，订单总价不能超过1亿
	            <#elseif errorType==6>订单提交失败，特许经营商家不允许非指定用户下单 
	            <#elseif errorType==7>订单提交失败，您选择的商品价格发生变动
	            <#elseif errorType==8>订单提交失败，您有商品不支持当前配送地区
	            <#elseif errorType==9>订单提交失败，包括超过限购时间或者数量的商品
	            <#elseif errorType==10>订单提交失败，优惠券无效
	            </#if>
        	</div>
        	<div class="info">
        		<p>
        			请 <a href="/cartlist">返回进货单</a> 重新确认商品信息
        		</p>
        		<p>
	              <#--请进入<a>在线客服</a> 咨询 ，或进入<a>帮助中心</a>查看相关问题--> 
	                                         如有疑问， 请拨打客服热线：0571-87651759 咨询
	            </p>
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
