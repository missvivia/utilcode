<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <meta charset="utf-8"/>
    <@title type="myOrderDetail" />
    <@css/>
    <link href="/src/css/base.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/member.css?2015082518" rel="stylesheet" type="text/css" />
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
   <@topbar />
    <@top />
    <@mainCategory />
    <div class="bg-french-gray clearfix">
      <@crumbs>
      <a href="/index">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><a href="/myorder">我的订单</a><span>&gt;</span>订单详情
    </@crumbs>
      <#-- Page Content Here -->
      <div id="order-detail" class="wrap">
        <div class="p-order-detail">
         <div class="f-cb p-step-box order-status">
            <div class="clearfix step">
              <#if order2VO.orderFormState.intValue == 0>
                  <#-- 未付款 -->
                  <div class="box dot first complete">
	                <span class="status">提交订单</span>
	                <span class="time">${order2VO.orderDate}</span>
	              </div>
                  <div class="connect success"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">付款成功</span>
                  </div>
                  <div class="connect"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">商家发货</span>
                  </div>
                 <#--<div class="connect"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">等待收货</span>
                  </div>
                  --> 
                  <div class="connect"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">确认收货，交易完成</span>
                  </div>
              <#elseif order2VO.orderFormState.intValue == 6>
                  <#-- 待发货 -->
                 
                  <#if order2VO.orderFormPayMethod.desc == "货到付款">
                  	<div class="box dot first complete" style="margin-left:310px;">
		                <span class="status">提交订单</span>
		                <span class="time">${order2VO.orderDate}</span>
		              </div>
	                  <div class="connect success"></div>
	                  <div class="connect"></div>
	                  <div class="box dot">
	                    <span class="status">商家发货</span>
	                  </div>
	                  <div class="connect"></div>
	                  <div class="connect"></div>
	                  <div class="box dot">
	                    <span class="status">确认收货，交易完成</span>
	                  </div>
                  <#else>
	                  <div class="box dot first complete">
		                <span class="status">提交订单</span>
		                <span class="time">${order2VO.orderDate}</span>
		              </div>
	                  <div class="connect success"></div>
	                  <div class="connect success"></div>
	                  <div class="box dot complete">
	                    <span class="status">付款成功</span>
	                    <span class="time">${order2VO.payDate}</span>
	                  </div>
	                  <div class="connect success"></div>
	                  <div class="connect"></div>
	                  <div class="box dot">
	                    <span class="status">商家发货</span>
	                  </div>
	                  <div class="connect"></div>
	                  <div class="connect"></div>
	                  <div class="box dot">
	                    <span class="status">确认收货，交易完成</span>
	                  </div>
                   </#if>
              <#elseif order2VO.orderFormState.intValue == 10>
                <#-- 已发货 -->
                <#if order2VO.orderFormPayMethod.desc == "货到付款">
                  <div class="box dot first complete" style="margin-left:310px;">
                    <span class="status">提交订单</span>
                    <span class="time">${order2VO.orderDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">商家发货</span>
                    <span class="time">${order2VO.expDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">确认收货，交易完成</span>
                  </div>
                <#else>
                  <div class="box dot first complete">
	                <span class="status">提交订单</span>
	                <span class="time">${order2VO.orderDate}</span>
	              </div>
	              <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">付款成功</span>
                    <span class="time">${order2VO.payDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">商家发货</span>
                    <span class="time">${order2VO.expDate}</span>
                  </div>
                  <#-- <div class="connect success"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">等待收货</span>
                  </div> -->
                  <div class="connect success"></div>
                  <div class="connect"></div>
                  <div class="box dot">
                    <span class="status">确认收货，交易完成</span>
                  </div>
                 </#if>
              <#elseif order2VO.orderFormState.intValue == 11>
                  <#-- 交易完成 -->
                <#if order2VO.orderFormPayMethod.desc == "货到付款">
                  <div class="box dot first complete" style="margin-left:310px;">
                    <span class="status">提交订单</span>
                    <span class="time">${order2VO.orderDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">商家发货</span>
                    <span class="time">${order2VO.expDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">交易完成</span>
                    <span class="time">${order2VO.confirmDate}</span>
                  </div>
                <#else>
                  <div class="box dot first complete">
	                <span class="status">提交订单</span>
	                <span class="time">${order2VO.orderDate}</span>
	              </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">付款成功</span>
                    <span class="time">${order2VO.payDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">商家发货</span>
                    <span class="time">${order2VO.expDate}</span>
                  </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                    <span class="status">交易完成</span>
                    <span class="time">${order2VO.confirmDate}</span>
                  </div>
                </#if>
              <#elseif order2VO.orderFormState.intValue == 21 || order2VO.orderFormState.intValue == 23>
                  <#-- 已取消 -->
                   <div class="box dot first complete" style="margin-left:400px;">
	                <span class="status">提交订单</span>
	                <span class="time">${order2VO.orderDate}</span>
	              </div>
                  <div class="connect success"></div>
                  <div class="connect success"></div>
                  <div class="box dot complete">
                     <span class="status">交易关闭</span>
                     <span class="time">
                     	<#if order2VO.cancelTimeStr??>
                     		${order2VO.cancelTimeStr}
                     	</#if>
                     </span>
                  </div>
              </#if>
            </div>
          </div>
          <div class="f-cb itemTop">

            <div class="f-fl order-detail-info">
              <#assign key = order2VO.orderFormState.intValue?string >
              	<em id="parentId" data-parentid="${order2VO.parentId}">订单号：${order2VO.orderId}</em>
				<em>订购日期：${order2VO.orderDate}</em>
            </div>
            <div class="f-fr">
            	<#if order2VO.orderFormState.intValue == 23>
				   <em>订单当前状态：已取消</em>
				<#else>
				   <em>订单当前状态：${order2VO.orderFormState.desc}</em>
				</#if>
            	<#if order2VO.orderFormState.intValue == 0>
					<#--<em class="m-btn-cancel" data-id="${order2VO.orderId}" data-simple="${order2VO.payState.intValue}" data-action="cancel">取消订单</em>-->
				<#elseif order2VO.orderFormState.intValue == 6>
				<#--<em class="m-btn-cancel" data-id="${order2VO.orderId}" data-simple="${order2VO.payState.intValue}" data-action="cancel">申请退款</em>-->
				<#--<#elseif order2VO.orderFormState.intValue == 10>
				    <em class="m-btn-cancel" data-id="${order2VO.orderId}" data-simple="${order2VO.payState.intValue}" data-action="cancel">申请退货/退款</em>
				    -->
				</#if>
				<#if order2VO.orderFormState.intValue == 11>
                    <em class="rightInfo">(买家已确认收货)</em>
                </#if>
				<span id="countdown-box"></span>
            </div>
          </div>
         
          <div class="f-cb p-block pro-detail-list">
            <div class="p-body p-body2 f-cb">
              <table class="gtable" width="100%">
                <thead>
                  <tr>
                    <th class="goods">商品</th>
                    <th class="price">单价</th>
                    <th class="number">数量</th>
                    <th class="sum">小计</th>
                    <!--<th class="status">状态</th>-->
                  </tr>
                </thead>
                <tbody>
                    <#list order2VO.cartList as x>
                      <tr>
                        <td class="goods2">
                          <div class="details f-cb">
	                          <#if x.orderSkuList??>
	                          	<#list x.orderSkuList as i>
	                          		<a target="_blank" href="${i.skuSPDTO.snapShotUrl}">
	                          			<img src="${i.skuSPDTO.picUrl}" />
	                          		</a>
	                          		<div class="f-cb f-fl word">
	                              		<p class="link">
		                              		<a class="clr" target="_blank" href="/product/detail?skuId=${i.productId}" title="${i.skuSPDTO.productName!''}">${i.skuSPDTO.productName!''}</a>
		                              		<a target="_blank" href="/product/snapShot?skuId=${i.productId}&orderId=${order2VO.orderId}&userId=${order2VO.userId}">[交易快照]</a>
	                              		</p>
	                              	</div>
	                          	</#list>
	                          </#if>
                          </div>
                        </td>
                        <td>
                          ¥${x.orderSkuList[0].rprice?string("0.00")}
                        </td>
                        <td>
                          ${x.count}
                        </td>
                        <td>
                          <p>¥${(x.totalRPrice)?string("0.00")}</p>
                          <#if x.totalSPrice?? && x.totalSPrice &gt; 0 >
                          <p class="coupon">已优惠：¥${x.totalSPrice?string("0.00")}</p>
                          </#if>
                          <#if order2VO.proxyAccount??>	
                          <p>（客服代下单）</p>
                          </#if>
                        </td>
                      </tr>
                  </#list>
                </tbody>
              </table>
            </div>

            <div class="p-body">
              <div class="f-cb rp">
                <div class="f-fr">
                  <table class="tmoney">
                    <tr>
                      <td class="td0">商品总额</td>
                      <td class="td1">¥${order2VO.cartOriRPrice?string("0.00")}</td>
                    </tr>
                    <#--<tr>
                      <td class="td0">运费</td>
                      <td class="td1">¥${order2VO.expOriPrice?string("0.00")}</td>
                    </tr>
                    <tr>
                      <td class="td0">免邮</td>
                      <td class="td1">- ¥${(order2VO.expOriPrice - order2VO.expUserPrice)?string("0.00")}</td>
                    </tr>-->
                    <#if order2VO.hdSPrice?? && order2VO.hdSPrice &gt; 0>
                    <tr>
                      <td class="td0">活动优惠</td>
                      <td class="td1">- ¥${order2VO.hdSPrice?string("0.00")}</td>
                    </tr>
                    </#if>
                    <#if order2VO.couponSPrice?? && order2VO.couponSPrice &gt; 0>
                    <tr>
                      <td class="td0"><span id="coupon-hover">优惠券抵用</div></td>
                      <td class="td1 rel">- ¥${order2VO.couponSPrice?string("0.00")}
	                      <#if order2VO.couponVO?? >
	                      	<div class="abs order-coupon" id="order-coupon">
	                      		<span><em>开始时间：</em>${order2VO.couponVO.start}</span>
	                      		<span><em>结束时间：</em>${order2VO.couponVO.end}</span>
	                      		<span><em>优惠券券号：</em>${order2VO.couponVO.couponCode}</span>
	                      		<span><em>优惠券名称：</em>${order2VO.couponVO.couponName}</span>
	                      	</div>
	                      </#if>
                     </td>
                    </tr>
                    </#if>
                    <#if order2VO.redPacketSPrice?? && order2VO.redPacketSPrice &gt; 0>
                      <tr>
                        <td class="td0">红包抵用</td>
                        <td class="td1">- ¥${order2VO.redPacketSPrice?string("0.00")}</td>
                      </tr>
                    </#if>
                    <tr>
                      <#assign pay = order2VO.realCash />
                      <#assign suffix = pay?string('0.00') />
                      <#assign ss = suffix?split('.')?size />
                      <td class="td2">支付金额</td>
                      <td class="td3">
                        <span class="big">¥${suffix?split('.')[0]}.</span>
                        <span class="small">
                        <#if ss == 2>
                        ${suffix?split('.')[1]}
                        <#else>
                        00
                        </#if>
                        </span>
                      </td>
                    </tr>
                  </table>
                  
				<#if order2VO.orderFormState.intValue == 0>
					<#--<div class="go-pay">
						<div data-action='gopay' data-id='${order2VO.orderId}' data-flag="0" class="m-btn-gopay f-fl">马上支付</div>
					</div>
					-->
				</#if>
				<#if order2VO.orderFormState.intValue == 10>
					<div class="btnbox f-cb">
						<div data-action='getGoods' data-id='${order2VO.orderId}' data-flag="0" class="m-btn-getGoods f-fl" id="getGoods">确认收货</div>
					</div>
				</#if>
		         
                </div>
              </div>
            </div>

            <div class="m-order-detail">
              <div class="body">
                <div class="detail">
                     <div class="content">
                         <p class="first">
                            <span class="title">交&nbsp;&nbsp;易&nbsp;&nbsp;号</span>
                            <span>${order2VO.payOrderSn!''}</span>
                         </p>
                     </div>
                     <#if order2VO.orderLogisticsVOs??>
                         <div class="content">
                            <#list order2VO.orderLogisticsVOs as item>
                              <#if item_index == 0>
                                <p class="first"> 
                                    <span class="title">物流信息</span>
                              <#else>
                                <p> 
                              </#if>
                                    <label>发货日期：</label><span>${item.deliverDate}</span>
                                </p>
                                <!--
                                <p>
                                    <label>物流公司：</label><span>${item.expressCompany.getName()}</span>
                                </p>
                                <p>
                                    <label>物流单号：</label><span>${item.mailNO}</span>
                                </p>
                                -->
                            </#list>
                         </div>
                    </#if>
                    <div class="content">
                         <p class="first"><span class="title">收货信息</span><span>${order2VO.expInfo.consigneeName}，<#if (order2VO.expInfo.consigneeMobile)?? && order2VO.expInfo.consigneeMobile!=''>${order2VO.expInfo.consigneeMobile}，</#if><#if (order2VO.expInfo.consigneeTel)?? && order2VO.expInfo.consigneeTel!=''> ${order2VO.expInfo.consigneeTel} ，</#if><#if (order2VO.expInfo.province)?? && (order2VO.expInfo.province == '北京市' || order2VO.expInfo.province == '上海市' || order2VO.expInfo.province == '天津市' || order2VO.expInfo.province == '重庆市')>
                          ${order2VO.expInfo.province}
                         <#else>
                           ${order2VO.expInfo.province} ${order2VO.expInfo.city}
                         </#if>${(order2VO.expInfo.section)!''} ${(order2VO.expInfo.street)!''} ${order2VO.expInfo.address} <#if (order2VO.expInfo.zipcode)?? && order2VO.expInfo.zipcode!=''>，${order2VO.expInfo.zipcode}</#if></span>
                    </div>
                    <#if order2VO.invoiceInOrdVOs??>
                    <div class="content">
                         <#list order2VO.invoiceInOrdVOs as item>
                            <#if item_index == 0>
                              <p class="first">
                                <span class="title">发票信息</span>
                            <#else>
                              <p>
                            </#if>
                                <label>发票状态：</label><span>${item.state.desc}</span>
                              </p>
                              <p>
                                <label>发票抬头：</label><span>${item.title}</span>
                              </p>
                              <p>
                                <label>发票时间：</label><span>${item.createDate}</span>
                              </p>
                              <p>
                                <label>发票号：</label><span>${item.invoiceNo}</span>
                              </p>
                          </#list>
                    </div>
                    </#if>
                    <div class="content end">
                         <p class="first">
                             <span class="title">付款信息</span>
                             <label>支付方式：</label><span>${order2VO.orderFormPayMethod.desc}</span>
                         </p>
                    </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <@footer/>
    <@copyright />
    <@js />
    <script type="text/javascript">
      var countdownTime = '${order2VO.payCloseCD}';
    </script>
    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/order/detail.js?20151117"></script>
  </body>
</html>
</@compress>
</#escape>