<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>支付成功</title>
    <@meta/>
    <@css/>
  </head>
  <body id="index-netease-com" style="background:#fff;">
  	<@topbar title="货到付款" parentPage="/myorder#state=0">
    </@topbar>
    <@module>
    <div class="m-pay">
      <i class="u-succ"></i>
      <div class="text text-1">
        <p>您的订单提交成功！</p>
      </div>
      <div class="orderInfo">
      	<p>
      		<label>收货人：</label>
      		<span style="margin-right:10px;">${expInfo.consigneeName}</span><span>${expInfo.consigneeMobile}</span>
      	</p>
      	<p>
      		<label>收货地址：</label>
      		<span>
      			${expInfo.province}
      			<#if (expInfo.cityId>0)>${expInfo.city}</#if>
      			<#if (expInfo.sectionId>0)>${expInfo.section}</#if>
      			<#if (expInfo.streetId>0)>${expInfo.street}</#if>
      			${expInfo.address}
      		</span>
      	</p>
      	<p><label>订单金额：</label><span style="color:#ee7000;">￥${total}</span></p>
      </div>
      <div class="btns" style="height:1.6rem;margin:1.6rem 0;">
        <a class="u-btn u-btn-1 s-fc5" style="width:45%;float:left;padding:0;margin:0;" href="/myorder#state=0">查看订单</a>
        <a class="u-btn u-btn-1 s-fc5" style="width:45%;float:right;padding:0;margin:0;" href="/index">返回首页</a>
      </div>
    </div>
    </@module>

    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    
    <@ga/>
  </body>
</html>
</@compress>
</#escape>