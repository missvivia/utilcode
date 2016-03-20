<#assign pageName="order-orderdetail">
<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" href="/src/css/page/orderdetail.css?v=1.0.0.1">
</head>

<body>
<!-- 左边目录列表 -->
<@side />

<!-- 右边内容区域 -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'/order/orderlist'} sub={"txt":"订单详情"}/>
<div class="m-card">
	<div class="card_c">
		<div class="order-d">
		    <div class="col-sm-3" style="width:auto;">
		    	<span>订单号：</span>${order2VO.orderId}
		    </div>
		    <#if order2VO.payCloseCD==0>
				
			<#else>
				<div class="col-sm-5" id="timer" style="width:auto;">
					请等待买家付款，剩余付款时间<span class="digit-d">00</span>天
					<span class="digit-h">00</span>小时
					<span class="digit-m">00</span>分
					<span class="digit-s">00</span>秒
				</div>
			</#if>
			<#if order2VO.orderFormState.intValue==6>
				<div class="col-sm-4" style="width:auto;">
					<#if order2VO.orderFormPayMethod.intValue==0 || order2VO.orderFormPayMethod.intValue==2>已付款,</#if>
					等待您发货
				</div>
			</#if>
			<#if order2VO.orderFormState.intValue==10>
				<div class="col-sm-4" style="width:auto;">
					已发货,等待买家收货
				</div>
			</#if>
			<#if order2VO.orderFormState.intValue==21>
				<div class="col-sm-4" style="width:auto;">
					交易结束，买家取消订单
				</div>
			</#if>
			<#if order2VO.orderFormState.intValue==11>
				<div class="col-sm-4" style="width:auto;">
					交易完成，买家已确认收货
				</div>
			</#if>
			<div class="col-sm-3">
				<#if ((order2VO.orderFormPayMethod.intValue==1 || order2VO.orderFormPayMethod.intValue==3) && order2VO.orderFormState.intValue==0)|| order2VO.orderFormState.intValue==6>
				<a href="javascript:void(0);" class="btn btn-primary sendGood">发货</a>
				</#if>
				<!--
				<a href="javascript:void(0);" class="btn btn-primary" id="closeOrder">关闭订单</a>
				-->
			</div>
		</div>
		<div class="order-d1">
			<ul class="clearfix">
			<#-- 在线支付 -->
			<#if order2VO.orderFormPayMethod.intValue==0 || order2VO.orderFormPayMethod.intValue==2>
				<#if order2VO.orderFormState.intValue==0>
				<li class="col-sm-3"><p><span class="state focus">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state">2</span></p><p>付款成功</p><p></p></li>
				<li class="col-sm-3"><p><span class="state">3</span></p><p>商家发货</p><p></p></li>
				<li class="col-sm-3"><p><span class="state">4</span></p><p>买家收货，交易完成</p><p></p></li>
				<#elseif order2VO.orderFormState.intValue == 6>
				<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state focus">2</span></p><p>付款成功</p><p>${order2VO.payDate}</p></li>
				<li class="col-sm-3"><p><span class="state">3</span></p><p>商家发货</p><p></p></li>
				<li class="col-sm-3"><p><span class="state">4</span></p><p>买家收货，交易完成</p><p></p></li>
				<#elseif order2VO.orderFormState.intValue == 10>
				<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state hover">2</span></p><p>付款成功</p><p>${order2VO.payDate}</p></li>
				<li class="col-sm-3"><p><span class="state focus">3</span></p><p>商家发货</p><p>${order2VO.expDate}</p></li>
				<li class="col-sm-3"><p><span class="state">4</span></p><p>买家收货，交易完成</p><p></p></li>
				<#elseif order2VO.orderFormState.intValue == 11>
				<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state hover">2</span></p><p>付款成功</p><p>${order2VO.payDate}</p></li>
				<li class="col-sm-3"><p><span class="state hover">3</span></p><p>商家发货</p><p>${order2VO.expDate}</p></li>
				<li class="col-sm-3"><p><span class="state focus">4</span></p><p>买家收货，交易完成</p><p>${order2VO.confirmDate}</p></li>
				<#elseif order2VO.orderFormState.intValue == 21>
					<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
					<#if order2VO.payDate != "">
						<li class="col-sm-3"><p><span class="state hover">2</span></p><p>付款成功</p><p>${order2VO.payDate}</p></li>
						<#if order2VO.expDate !="">
							<li class="col-sm-3"><p><span class="state hover">3</span></p><p>商家发货</p><p>${order2VO.expDate}</p></li>
							<#if order2VO.cancelTimeStr != "">
								<li class="col-sm-3"><p><span class="state focus">4</span></p><p>交易结束，买家取消订单</p><p>${order2VO.cancelTimeStr}</p></li>
							</#if>
						<#else>
							<li class="col-sm-3"><p><span class="state focus">3</span></p><p>交易结束，买家取消订单</p><p>${order2VO.cancelTimeStr}</p></li>
						</#if>
					<#else>
						<li class="col-sm-3"><p><span class="state focus">2</span></p><p>交易结束，买家取消订单</p><p>${order2VO.cancelTimeStr}</p></li>
					</#if>
					
				</#if>
			</#if>
			<#-- 货到付款 -->
			<#if order2VO.orderFormPayMethod.intValue==1 || order2VO.orderFormPayMethod.intValue==3>
				<#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6>
				<li class="col-sm-3"><p><span class="state focus">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state">2</span></p><p>商家发货</p><p></p></li>
				<li class="col-sm-3"><p><span class="state">3</span></p><p>买家收货，交易完成</p><p></p></li>
				<#elseif order2VO.orderFormState.intValue == 10>
				<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state focus">2</span></p><p>商家发货</p><p>${order2VO.expDate}</p></li>
				<li class="col-sm-3"><p><span class="state">3</span></p><p>买家收货，交易完成</p><p></p></li>
				<#elseif order2VO.orderFormState.intValue == 11>
				<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
				<li class="col-sm-3"><p><span class="state hover">2</span></p><p>商家发货</p><p>${order2VO.expDate}</p></li>
				<li class="col-sm-3"><p><span class="state focus">3</span></p><p>买家收货，交易完成</p><p>${order2VO.confirmDate}</p></li>
				<#elseif order2VO.orderFormState.intValue == 21>
					<li class="col-sm-3"><p><span class="state hover">1</span></p><p>提交订单</p><p>${order2VO.orderDate}</p></li>
					<#if order2VO.expDate !="">
						<li class="col-sm-3"><p><span class="state hover">2</span></p><p>商家发货</p><p>${order2VO.expDate}</p></li>
						<#if order2VO.cancelTimeStr != "">
							<li class="col-sm-3"><p><span class="state focus">3</span></p><p>交易结束，买家取消订单</p><p>${order2VO.cancelTimeStr}</p></li>
						</#if>
					<#else>
						<li class="col-sm-3"><p><span class="state focus">2</span></p><p>交易结束，买家取消订单</p><p>${order2VO.cancelTimeStr}</p></li>
					</#if>
					
				</#if>
			</#if>
			</ul>
		</div>
		<div class="row order-d2 clearfix">
			<div class="col-sm-3 d2-wrap">
				<p class="d2-title"><label>订单信息</label></p>
				<div class="d2-box">
					<div class="d2-p">
						 <label>买家帐号：</label>
						 <div class="d2-ptext">${order2VO.userName}</div>
					</div>
					<div class="d2-p">
						 <label>买家昵称：</label>
						 <div class="d2-ptext">${order2VO.nickName}</div>
					</div>
					<div class="d2-p">
						 <label>买家手机号：</label>
						 <div class="d2-ptext">${(order2VO.phoneNo)!''}</div>
					</div>
					<div class="d2-p">
						 <label>订单状态：</label>
						 <div class="d2-ptext d2-status">
						 	<#if order2VO.orderFormState.intValue == 21>
						 	交易结束,买家取消订单
						 	<#else>
						 	${order2VO.orderFormState.desc}
						 	</#if>
						 </div>
					</div>
					<div class="d2-p">
						 <label>支付类型：</label>
						 <div class="d2-ptext">
						 	${order2VO.orderFormPayMethod.desc}
						 </div>
					</div>
					<div class="d2-p">
						 <label>支付状态：</label>
						 <div class="d2-ptext">${order2VO.payState.desc}</div>
					</div>
					<div class="d2-p">
						 <label>下单时间：</label>
						 <div class="d2-ptext">${order2VO.orderDate}</div>
					</div>
					<#if order2VO.orderFormState.intValue == 6 && (order2VO.orderFormPayMethod.intValue==0 || order2VO.orderFormPayMethod.intValue==2)>
					<div class="d2-p">
						 <label>付款时间：</label>
						 <div class="d2-ptext">${order2VO.payDate}</div>
					</div>
					</#if>
					<#if order2VO.orderFormState.intValue == 10 && (order2VO.orderFormPayMethod.intValue==0 || order2VO.orderFormPayMethod.intValue==2)>
					<div class="d2-p">
						 <label>付款时间：</label>
						 <div class="d2-ptext">${order2VO.payDate}</div>
					</div>
					<div class="d2-p">
						 <label>发货时间：</label>
						 <div class="d2-ptext">${order2VO.expDate}</div>
					</div>
					</#if>
					<#if order2VO.orderFormState.intValue == 10 && (order2VO.orderFormPayMethod.intValue==1 || order2VO.orderFormPayMethod.intValue==3)>
					<div class="d2-p">
						 <label>发货时间：</label>
						 <div class="d2-ptext">${order2VO.expDate}</div>
					</div>
					</#if>
					<#if order2VO.proxyAccount?exists>
					<div class="d2-p">
						 <label>代下单账号：</label>
						 <div class="d2-ptext">${order2VO.proxyAccount}</div>
					</div>
					</#if>
				</div>
			</div>
			<div class="col-sm-3 d2-wrap">
				<p class="d2-title">
					<label>发货信息</label>
<#--
					<#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6 || order2VO.orderFormState.intValue==10>
					<a href="javascript:void(0);" id="modifyInfo" class="orderbtn">修改</a>
					</#if>
-->
				</p>
				<div class="d2-box">
					<div class="d2-p">
						<label style="margin-bottom:0;">收货人：</label>
						${order2VO.expInfo.consigneeName},
						${order2VO.expInfo.consigneeMobile},
						${order2VO.expInfo.province}${order2VO.expInfo.city}${order2VO.expInfo.section}${order2VO.expInfo.street}${order2VO.expInfo.address},
						${order2VO.expInfo.areaCode!''} - ${order2VO.expInfo.consigneeTel!''}
					</div>
					<#if order2VO.orderLogisticsVOs??>
					<#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6 || order2VO.orderFormState.intValue==10>
					<div class="d2-p">
						<a href="javascript:void(0);" class="btn btn-primary addWl">新增物流</a>
					</div>
					</#if>
					<#list order2VO.orderLogisticsVOs as s>
					<div class="d2-box">
						<div class="d2-p">
							 <label>发货日期:</label>
							 <div class="d2-ptext">
							 		${s.deliverDate}
							 </div>
						</div>
						<div class="d2-p">
							 <label>物流公司:</label>
							 <div class="d2-ptext">
							 		${s.expressCompany.getName()}
							 </div>
						</div>
						<div class="d2-p">
							 <label>物流单号:</label>
							 <div class="d2-ptext">
							 		${s.mailNO}
							 </div>
						</div>
					</div>
					</#list>
					</#if>
				</div>
			</div>
			<div class="col-sm-3 d2-wrap">
				<p class="d2-title"><label>发票信息</label></p>
				<div class="d2-box">
					<#if order2VO.invoiceInOrdVOs??>
						<div class="d2-p">
							 <label>发票状态：</label>
							 <div class="d2-ptext">
							 		${order2VO.invoiceInOrdVOs[0].state.desc}
							 </div>
						</div>
						<div class="d2-p">
							 <label>发票抬头：</label>
							 <div class="d2-ptext">
							 		${order2VO.invoiceInOrdVOs[0].title}
							 </div>
						</div>
						<#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6 || order2VO.orderFormState.intValue==10>
						<div class="d2-p">
						 	<a href="javascript:void(0);" class="btn btn-primary modifyInvoice">开发票</a>
						</div>
						</#if>
						<#list order2VO.invoiceInOrdVOs as c>
						<div class="d2-box">
							<div class="d2-p">
								 <label>台头：</label>
								 <div class="d2-ptext">
								 		${c.title}
								 </div>
							</div>
							<div class="d2-p">
								 <label>开票时间：</label>
								 <div class="d2-ptext">
								 		${c.createDate}
								 </div>
							</div>
							<div class="d2-p">
							 <label>发票号：</label>
							 <div class="d2-ptext">
							 		${c.invoiceNo}
							 </div>
							</div>
						</div>
						</#list>
					<#else>
						<#if order2VO.invoiceTitle??>
    						<div class="d2-p">
                                 <label>发票状态：</label>
                                 <div class="d2-ptext">
                                                                                                                    未开
                                 </div>
                            </div>
    						<div class="d2-p">
    							 <label>发票抬头：</label>
    							 <div class="d2-ptext">
    							 		${order2VO.invoiceTitle}
    							 </div>
    						</div>
    						<#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6 || order2VO.orderFormState.intValue==10>
    						<div class="d2-p">
    						 	<a href="javascript:void(0);" class="btn btn-primary modifyInvoice">开发票</a>
    						</div>
    						</#if>
						<#else>
							<div class="d2-p">
                                                                                            无需开发票
                            </div>
						</#if>
					</#if>
				</div>
			</div>
			<div class="col-sm-3 d2-wrap">
				<p class="d2-title"><label>备注</label><#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6 || order2VO.orderFormState.intValue==10><#if order2VO.comment??><a href="javascript:void(0);" class="orderbtn" id="addBz">修改备注</a><#else><a href="javascript:void(0);" class="orderbtn" id="addBz">新增备注</a></#if></#if></p>
				<div class="d2-box">
					<div class="d2-p" id="bz-box">
						 <#if order2VO.comment??>
							<div class="d2-p">
								<label>备注时间：</label>
								<div class="d2-ptext">${order2VO.updateDate}</div>
							</div>
							<!--
							<div class="d2-p">
								<label>操作人：</label>
								<div class="d2-ptext">${order2VO.userName}</div>
							</div>
							-->
							<div class="d2-p">
								<label>备注内容：</label>
								<div class="d2-ptext">${order2VO.comment}</div>
							</div>
						</#if>
					</div>
				</div>
			</div>
		</div>
		<div class="order-d3">
			<div class="gl-box">
				<div class="row gl-t">
					<div class="col-sm-3 gl-tt">商品清单</div>
					<div class="col-sm-9 gl-tr cartPrice">
						<!--<span>商品种类：9种，共计数量</span>-->
						<span>原价：¥${order2VO.cartOriRPrice?string("0.00")}</span>
						<#if order2VO.couponSPrice?? && order2VO.couponSPrice gt 0>
							<span id="viewCoupon">优惠：¥${order2VO.couponSPrice?string("0.00")}</span>
						</#if>
						<span>实付:¥${order2VO.cartRPrice?string("0.00")}</span>
<#--
						<#if order2VO.orderFormState.intValue==0 || order2VO.orderFormState.intValue==6>
						<a href="javascript:;"  id="modifyPrice" class="orderbtn">改总价</a>
						</#if>
-->
						<div class="couponBox" style="display:none;">
		          			<div class="info">
		          				<#if order2VO.couponVO??>
		          				<span>优惠券名称：${order2VO.couponVO.couponName!''}</span>
		          				<span>券号：${order2VO.couponVO.couponCode!''}</span>
		          				<span>开始时间：${order2VO.couponVO.start!''}</span>
		          				<span>结束时间：${order2VO.couponVO.end!''}</span>
		          				</#if>
		          			</div>
		          		</div>
					</div>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th>商品</th>
							<th>数量</th>
							<th>单价</th>
							<th>金额</th>
						</tr>
					</thead>
					<tbody>
						<#list order2VO.cartList as x>
	                      <tr>
	                        <td class="goods2">
	                          <div class="details f-cb">
	                            <a target="_blank" href="${x.orderSkuList[0].skuSPDTO.linkUrl}">
	                            <img src="<#if x.orderSkuList[0].skuSPDTO.picUrl??>${x.orderSkuList[0].skuSPDTO.picUrl}<#else> </#if>" alt="">
	                            </a>
	                            <div class="f-cb f-fl word">
	                              <p class="link">
	                              	<#if x.storeName?exists>
	                              	<a class="clr" target="_blank" href="${(x.storeUrl)!''}">【${(x.storeName)!''}】</a>
	                              	</#if>
	                              	<a target="_blank" href="${x.orderSkuList[0].skuSPDTO.linkUrl}">${(x.orderSkuList[0].skuSPDTO.productName)!''}</a>
	                              	<a target="_blank" href="${x.orderSkuList[0].skuSPDTO.snapShotUrl}">[交易快照]</a>
	                              </p>
	                              <span class="cs">
	                              			<#if x.orderSkuList[0].skuSPDTO.skuSpeciDTOs??> 
	                              					<#list x.orderSkuList[0].skuSPDTO.skuSpeciDTOs as skuSpeciDTO>
	                              						${skuSpeciDTO.speciOptionName!''}  ${skuSpeciDTO.specificationName!''}
	                              					</#list>
	                              		    </#if>
	                              </span>
	                            </div>
	                          </div>
	                        </td>
	                        <td>
	                          ${x.count}
	                        </td>
	                        <td>
	                          ¥${x.orderSkuList[0].rprice?string("0.00")}
	                        </td>
	                        <td>
	                          <p>¥${(x.totalRPrice)?string("0.00")}</p>
	                          <#--
	                          <#if x.totalSPrice?? && x.totalSPrice &gt; 0 >
	                          <p class="coupon">已优惠：¥${x.totalSPrice?string("0.00")}</p>
	                          </#if>
	                          -->
	                        </td>
	                      </tr>
                  		</#list>
					</tbody>
				</table>
			</div>
		</div>
		<div class="order-d4">
			<div class="gl-box">
				<div class="gl-t">订单修改日志</div>
				<table class="table">
					<thead>
						<th>操作者</th>
						<th>操作内容</th>
						<th>操作日期</th>
					</thead>
					<tbody id="operationwrap">
					
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#noparse>
<textarea name="jst" id="operationlog" style="display:none;">
	{if !!result}
	{list result as item}
		<tr>
			<td>${item.operator}</td>
			<td>
				<p><label>修改前:</label>${item.perOperateContent}</p>
				<p><label>修改后:</label>${item.curOperateContent}</p>
			</td>
			<td>${item.showTime}</td>
		</tr>
	{/list}
	{else}
		没有任何操作数据
	{/if}
	
</textarea>
</#noparse>
<form id="orderform">
	<input type="hidden" name="orderId" value="${order2VO.orderId}"/>
	<input type="hidden" name="userId" value="${order2VO.userId}"/>
	<input type="hidden" name="userName" value="${order2VO.userName}"/>
	<input type="hidden" name="cartRPrice" value="${order2VO.cartOriRPrice?string("0.00")}"/>
	<input type="hidden" name="realCash" value="${order2VO.realCash?string("0.00")}"/>
	
	<input type="hidden" name="orderFormPayMethod" value="${order2VO.orderFormPayMethod.desc}"/>
	<input type="hidden" name="consigneeName" value="${order2VO.expInfo.consigneeName}"/>
	<input type="hidden" name="consigneeMobile" value="${order2VO.expInfo.consigneeMobile}"/>
	<input type="hidden" name="address" value="${order2VO.expInfo.address}"/>
	<input type="hidden" name="zipcode" value="${order2VO.expInfo.zipcode}"/>
	<input type="hidden" name="province" value="${order2VO.expInfo.province}"/>
	<input type="hidden" name="city" value="${order2VO.expInfo.city}"/>
	<input type="hidden" name="section" value="${order2VO.expInfo.section}"/>
	<input type="hidden" name="street" value="${order2VO.expInfo.street}"/>
	<#if order2VO.comment??>
		<input type="hidden" name="comment" value="${order2VO.comment}"/>
	</#if>
	<input type="hidden" name="payCloseCD" value="${order2VO.payCloseCD}"/>
	<#if order2VO.invoiceInOrdVOs??>
		<input type="hidden" name="invoiceTitle" value="${order2VO.invoiceInOrdVOs[0].title}"/>
	<#else>
		<#if order2VO.invoiceTitle??>
			<input type="hidden" name="invoiceTitle" value="${order2VO.invoiceTitle}"/>
		</#if>
	</#if>
	<input type="hidden" name="consigneeTel" value="${(order2VO.expInfo.consigneeTel)!''}"/>
	<input type="hidden" name="areaCode" value="${(order2VO.expInfo.areaCode)!''}"/>
	<#list expressCompany as vs >
	<input type ="hidden" name="expressCompany" value="${vs.getName()}"/>
	<input type ="hidden" name="expressValue" value="${vs.code}"/>
	</#list>
</form>
</@wrap>
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/orderDetail.js?v=1.0.0.0"></script>
</body>
</html>