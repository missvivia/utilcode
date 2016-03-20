<#assign pageName="order-query"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/order.css?v=1.0.0.1">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<div class="inner">
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"订单查询"}/>
</div>

<div class="orderDetail">
	<div class="inner">
		<!-- card -->
		<div class="row">
		  <div class="col-sm-12">
		    <div class="m-card">
		      <div class="card_c f-cb style="padding:15px;">
			      <div class="input-group col-sm-2"">
				      <input type="text" class="form-control" id="key" placeholder="订单号">
				      <div class="input-group-addon glyphicon glyphicon-search" id="search"></div>
				   </div>
			   </div>
		     </div>
		     <#if data.basicInfo.method?exists>
		      <div class="row" style="padding:15px;">
					<!-- Single button -->
					<#if data.basicInfo.status.intValue lt 11>
					<div class="btn-group">
					  <button id="chageOrderType" type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-expanded="false" value="${(data.basicInfo.status.getIntValue())!''}">
					    更改订单状态 <span class="caret"></span>
					  </button>
					  <ul class="dropdown-menu" role="menu" id="orderStatusList">
					    <#if (data.basicInfo.status.intValue == 0)><li><a href="#" data="6">待发货</a></li></#if>
					    <#if data.basicInfo.status.intValue == 6><li><a href="#" data="10">已发货</a></li></#if>
					    <li><a href="#" data="21">取消</a></li>
					    <#if data.basicInfo.status.intValue == 10><li><a href="#" data="11">交易完成</a></li></#if>
					  </ul>
					</div>
					</#if>
<#--
					<div class="btn-group" role="group" aria-label="...">
					  <button type="button" class="btn btn-default" id="exportExcel">导出Excel</button>
					  <button type="button" class="btn btn-default" id="print">打印</button>
					</div>
-->
					<div class="btn-group">
					  <button type="button" class="btn btn-primary" id="viewLog">
					    	查看操作日志 <span class="caret"></span>
					  </button>
					</div>		     		
		      </div>
		      <div class="card_c f-cb">
		        <div class="col-sm-3 noLeft">
		          <div class="panel panel-default" id="basicInfo">
		<#--          	<div class="toolbar">
			          	<div class="detail"><a href="#" class="glyphicon glyphicon-pencil">编辑</a></div>
			          	<div class="editing">
			          		<a href="#" class="save">保存</a>
			          		<a href="#" class="cancel">取消</a>
			          	</div>
		          	</div>
		-->
		            <div class="panel-body">
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">订单号</div>
		                <div class="col-sm-9">${data.basicInfo.orderId!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">交易号</div>
		                <div class="col-sm-9">${data.basicInfo.payOrderSn!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">用户帐号</div>
		                <div class="col-sm-9">${data.basicInfo.userName!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">用户昵称</div>
		                <div class="col-sm-9">${data.basicInfo.nickName!''}</div>
		              </div>
		               <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">卖家帐号</div>
		                <div class="col-sm-9">${data.basicInfo.businessAccount!''}</div>
		              </div>
		               <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">卖家店铺</div>
		                <div class="col-sm-9">${data.basicInfo.storeName!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">下单时间</div>
		                <div class="col-sm-9">${data.basicInfo.bookTime?number_to_datetime!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">付款时间</div>
		                <div class="col-sm-9"><#if data.basicInfo.payTime != 0>${data.basicInfo.payTime?number_to_datetime!''}</#if></div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">付款方式</div>
		                <div class="col-sm-9">${data.basicInfo.method.desc!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">订单金额</div>
		                <div class="col-sm-9">${data.basicInfo.price!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">实付金额</div>
		                <div class="col-sm-9">${data.basicInfo.orderPay!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">付款备注</div>
		                <div class="col-sm-9">${data.comment!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">状态</div>
		                <div class="col-sm-9">
		                  ${data.basicInfo.status.desc}
		                  <#if !!data.basicInfo.canCancel>
		                    <a class="f-mgl" <#if !!data.basicInfo.payed>data-payed="true"</#if> id="cancelOrder">取消订单</a>
		                  </#if>
		                </div>
		              </div>
		              <#if data.basicInfo.proxyAccount?exists>
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">代下单账号</div>
		                <div class="col-sm-9">
		                  ${data.basicInfo.proxyAccount}
		                </div>
		              </div>
		              </#if>
<#--
		              <div class="row f-mgtb">
		                <div class="col-sm-3 f-tar">是否开票</div>
		                <div class="col-sm-9">
		                	<div class="detail">
			                	<#if data.basicInfo.title?exists>
			                		是
			                	<#else>
			                	否
			                	</#if>
		                	</div>
		                	<div class="editing">
								<label class="radio-inline">
								  <input type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1"> 是
								</label>
								<label class="radio-inline">
								  <input type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2"> 否
								</label>                	
			                </div>
		                </div>
		              </div>
-->		              
		            </div>
		          </div>
		        </div>
		        <div class="col-sm-9 noRight">
		          <div class="panel panel-default" id="deliveryInfo" style="height:340px;">
		          	<div class="toolbar">
			          	<#if data.basicInfo.status.getIntValue()==0 || data.basicInfo.status.getIntValue()==6>
			          	<div class="detail"><a href="#" class="glyphicon glyphicon-pencil" id="addressEdit">编辑</a></div>
		          		</#if>
		          	</div>
		            <div class="panel-body">
		              <div class="row f-mgtb">
		                <div class="col-sm-1 f-tar minWidth">收货人姓名</div>
		                <div class="col-sm-9 j-addr">${data.orderExpInfoVO.consigneeName!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-1 f-tar minWidth">收货人手机</div>
		                <div class="col-sm-9 j-addr">${data.orderExpInfoVO.consigneeMobile!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-1 f-tar minWidth">邮编</div>
		                <div class="col-sm-9 j-addr">${data.orderExpInfoVO.zipcode!''}</div>
		              </div>
		              <div class="row f-mgtb">
		                <div class="col-sm-1 f-tar minWidth">收货地址</div>
		                <div class="col-sm-9 j-addr">${data.orderExpInfoVO.province!''}${data.orderExpInfoVO.city!''}${data.orderExpInfoVO.section!''}${data.orderExpInfoVO.street!''}${data.orderExpInfoVO.address!''}</div>
		              </div>
					  <#if data.orderLogisticsVOs?exists>
		              <#list data.orderLogisticsVOs as item>
		              <div class="row f-mgtb">
		              	<div class="col-sm-1 f-tar">物流公司</div>
		              	<div class="col-sm-3 j-addr">
		              		<div class="detail">${item.expressCompany.getName()}</div>
		              		<div class="editing"></div>
		              	</div>
		              	<div class="col-sm-1 f-tar">物流单号</div>
		              	<div class="col-sm-3 j-addr">
		          			<div class="detail">${item.mailNO}</div>
		          			<div class="editing"></div>
		              	</div>
		              	<div class="col-sm-1 f-tar">发货时间</div>
		              	<div class="col-sm-3 j-addr">
		          			<div class="detail">${item.deliverDate}</div>
		          			<div class="editing"></div>
		              	</div>
		              </div>
		              </#list>
		              </#if>
<#--		              <div class="row f-mgtb"><a href="#" id="addLogistics">添加物流信息</a></div>-->
		            </div>
		            
		          </div>
		
		        </div>
		      </div>
		    </div>
		  </div>
		
		
		
		    <div class="m-card" >
		      <div class="card_c f-cb">
		        <div class="col-sm-12">
		          <#if data.cartList?exists>
		          
		          	<table class="table">
		          		<tr>
		          			<th>商品
		          				<#-->
		          				<#if data.basicInfo.status.getIntValue() == 0>
		          				<a href="javascript:;" id="editPrice">编辑</a>
		          				</#if>
		          				-->
		          			</th>
		          			<th>价格</th>
		          			<th>数量</th>
		          			<th>小计</th>
		          			<th>优惠</th>
		          			<th>配送方式</th>
		          		</tr>
		          		<#list data.cartList as cart>
		          		<tr>
		          			<td>
		          				<a class="imgBox" target="_blank" href="${cart.orderSkuList[0].skuSPDTO.linkUrl}">
		          					<img src="${cart.orderSkuList[0].skuSPDTO.picUrl}" width="50" height="50"/>
		          				</a>
		          				<a href="${cart.storeUrl}" target="_blank">【${cart.storeName}】</a>
		          				<a target="_blank" href="${cart.orderSkuList[0].skuSPDTO.linkUrl}">${cart.orderSkuList[0].skuSPDTO.productName!''}</a>
		          				<a target="_blank" href="${cart.orderSkuList[0].skuSPDTO.snapShotUrl}">[交易快照]</a>
		          			</td>
		          			<td>${cart.orderSkuList[0].rprice?string("0.00")}</td>
		          			<td>${cart.orderSkuList[0].totalCount}</td>
		          			<td>${cart.orderSkuList[0].totalRPrice?string("0.00")}</td>
		          			<td></td>
		          			<td></td>
		          		</tr>
		          		</#list>
		          	</table>
		          	<div class="cartPrice">
		          		<#if data.basicInfo.couponSPrice?exists><p id="viewCoupon">共优惠：${data.basicInfo.couponSPrice?string("0.00")}元</p></#if>
		          		<div  class="clearfix"></div>
		          		<p>总计：${data.basicInfo.price?string("0.00")}元</p>
		          		<p>实付：${data.basicInfo.orderPay?string("0.00")}元</p>
		          		<div class="couponBox" style="display:none;">
		          			<div class="info">
		          				<#if data.basicInfo.coupon??>
		          				<span>优惠券名称：${data.basicInfo.coupon.couponName}</span>
		          				<span>券号：${data.basicInfo.coupon.couponCode}</span>
		          				<span>开始时间：${data.basicInfo.coupon.start}</span>
		          				<span>结束时间：${data.basicInfo.coupon.end}</span>
		          				</#if>
		          			</div>
		          		</div>
		          	</div>
					
		          </#if>
		        </div>
		      </div>
		    </div> 
<#--		         
			<div class="m-card">
			    <div id="invoice-add-box">
			      <div class="panel panel-default" id="invoice">
			        <div class="panel-heading">发票信息</div>
			        <div class="panel-body">
			          <#if data.invoiceInOrdVOs?exists>
			          <table class="table">
			          	<tr>
			          		<th>发票抬头</th>
			          		<th>发票号</th>
			          		<th>状态</th>
			          		<th></th>
			          	</tr>
			          	<#list data.invoiceInOrdVOs as item>
			          	<tr>
			          		<td>${item.title}</td>
			          		<td>${item.invoiceNo}</td>
			          		<#if item.state == "INIT">
			          		<td>未开票</td>
			          		<#elseif item.state == "KP_ING">
			          		<td>已开票</td>
			          		<#elseif item.state == "UN_KP">
			          		<td>不开票</td>
			          		</#if>
			          		<td>
			          			<#if data.basicInfo.status.getIntValue() < 11>
			          			<a href="javascript:;" invoiceId="${item.id}" index="${item_index}" class="edit">编辑</a>
			          			</#if>
			          		</td>
			          	</tr>
			          	</#list>
			          </table>
			          <#else>
			          	该订单未开发票
			          </#if>
			          	<#if data.basicInfo.status.getIntValue() < 11>
			          	<div>
			          	<a href="javascript:;" id="addInvoice">新增发票</a>
			          	</div>
			          	</#if>
			        </div>
			      </div>
			    </div>
-->				
			  
			</div>
			<div class="m-card">
				<div class="panel panel-default">
					<div class="panel-heading">订单附言</div>
		          	<div class="toolbar">
			          	<div class="detail"><a href="javascript:;" class="glyphicon glyphicon-pencil" id="commentEdit">编辑</a></div>
		          	</div>					
					<div class="panel-body">
						<#if data.comment??>
				   		 <p>${data.comment}</p>
				   		<#else> 
				   		 暂无订单留言
				   		</#if>
					</div>
				</div>
			</div>
			</#if>
		</div>
	</div>
<#-- print section -->
<div class="print">     
  <div class="panel panel-default">
    <div class="panel-body">
      <#if data.basicInfo.method?exists>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">订单号</div>
        <div class="col-sm-3">${data.basicInfo.orderId!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">用户名</div>
        <div class="col-sm-3">${data.basicInfo.userName!''}</div>
      </div>
      <div class="row f-mgtb">
      	<div class="col-sm-1 f-tar">付款时间</div>
        <div class="col-sm-3"><#if data.basicInfo.payTime != 0>${data.basicInfo.payTime?number_to_datetime!''}</#if></div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">付款方式</div>
        <div class="col-sm-3">${data.basicInfo.method.desc!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">订单金额</div>
        <div class="col-sm-3">${data.basicInfo.price!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">实付金额</div>
        <div class="col-sm-3">${data.basicInfo.orderPay!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">付款备注</div>
        <div class="col-sm-3">${data.basicInfo.userId!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">状态</div>
        <div class="col-sm-3">
          ${data.basicInfo.status.desc}
          (${(data.basicInfo.status.getName())!''})
          <#if !!data.basicInfo.canCancel>
            <a class="f-mgl" <#if !!data.basicInfo.payed>data-payed="true"</#if> id="cancelOrder">取消订单</a>
          </#if>
        </div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">是否开票</div>
        <div class="col-sm-3">
        	<div class="detail">
            	<#if data.basicInfo.title?exists>
            		是
            	<#else>
            	否
            	</#if>
        	</div>
        </div>
      </div>
      </#if>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">收货人姓名</div>
        <div class="col-sm-3 j-addr">${data.orderExpInfoVO.consigneeName!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">收货人手机</div>
        <div class="col-sm-3 j-addr">${data.orderExpInfoVO.consigneeMobile!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">收货人电话</div>
        <div class="col-sm-3 j-addr">${data.orderExpInfoVO.areaCode!''} - ${data.orderExpInfoVO.consigneeTel!''}</div>
      </div>
      <div class="row f-mgtb">
        <div class="col-sm-1 f-tar">收货地址</div>
        <div class="col-sm-3 j-addr">${data.orderExpInfoVO.province!''}${data.orderExpInfoVO.city!''}${data.orderExpInfoVO.section!''}${data.orderExpInfoVO.street!''}${data.orderExpInfoVO.address!''}</div>
      </div>
		<#if data.orderLogisticsVOs?exists>
	      <div class="logisticsBox">
	      <#list data.orderLogisticsVOs as item>
	      <div class="row f-mgtb">
	      	<div class="col-sm-1 f-tar">物流公司</div>
	      	<div class="col-sm-3 j-addr">
	      		<div class="detail">${item.expressCompany.getName()}</div>
	      	</div>
	      	<div class="col-sm-1 f-tar">物流单号</div>
	      	<div class="col-sm-3 j-addr">
	  			<div class="detail">${item.mailNO}</div>
	      	</div>
	      	<div class="col-sm-1 f-tar">发货时间</div>
	      	<div class="col-sm-3 j-addr">
	  			<div class="detail">${item.deliverDate}</div>
	      	</div>
	      </div>
	      </#list>
	      </div>
	      </#if>
	            
        </div>
        
      </div>
	    <div class="m-card panel" >
	      <div class="card_c f-cb">
	        <div class="col-sm-12">
	          <#if data.cartList?exists>
	          
	          	<table class="table">
	          		<tr>
	          			<th width="50%">商品</th>
	          			<th>价格</th>
	          			<th>数量</th>
	          			<th>小计</th>
	          			<th>优惠</th>
	          			<th>配送方式</th>
	          		</tr>
	          		<#list data.cartList as cart>
	          		<#if cart.orderSkuList?exists>
	          		<tr>
	          			<td><img src="${cart.orderSkuList[0].skuSPDTO.picUrl}" width="50" height="50""/>【<#if cart.storeName?exists>${cart.storeName}</#if>】${cart.orderSkuList[0].skuSPDTO.productName}</td>
	          			<td>${cart.orderSkuList[0].rprice?string("0.00")}</td>
	          			<td>${cart.orderSkuList[0].totalCount}</td>
	          			<td>${cart.orderSkuList[0].totalRPrice?string("0.00")}</td>
	          			<td></td>
	          			<td></td>
	          		</tr>
	          		</#if>
	          		</#list>
	          	</table>
	          	<div class="cartPrice">
	          		<#if data.basicInfo.couponSPrice?exists><p id="viewCoupon">共优惠：${data.basicInfo.couponSPrice?string("0.00")}元</p></#if>
	          		<div  class="clearfix"></div>
	          		<p>总计：${data.basicInfo.price?string("0.00")}元</p>
	          		<p>实付：${data.basicInfo.orderPay?string("0.00")}元</p>
	          	</div>
			  
	          </#if>
	        </div>
	      </div>
	    </div>
		    <div id="invoice-add-box">
		      <div class="panel panel-default" id="invoice">
		        <div class="panel-heading">发票信息</div>
		        <div class="panel-body">
		          <#if data.invoiceInOrdVOs?exists>
		          <table class="table">
		          	<tr>
		          		<th>发票抬头</th>
		          		<th>发票号</th>
		          		<th>状态</th>
		          	</tr>
		          	<#list data.invoiceInOrdVOs as item>
		          	<tr>
		          		<td>${item.title}</td>
		          		<td>${item.invoiceNo}</td>
		          		<#if item.state == "INIT">
			          		<td>未开票</td>
			          		<#elseif item.state == "KP_ING">
			          		<td>已开票</td>
			          		<#elseif item.state == "UN_KP">
			          		<td>不开票</td>
		          		</#if>
		          	</tr>
		          	</#list>
		          </table>
		          <#else>
		          	该订单未开发票
		          </#if>
		        </div>
		      </div>
		    </div>	  
		<div class="panel panel-default">
			<div class="panel-heading">订单附言</div>
			<div class="panel-body">
				<#if data.commentList?exists>
		   		<#list data.commentList as item>
		   		<p>${item}</p>
		   		</#list>
		   		<#else> 
		   		 暂无订单留言
		   		</#if>
			</div>
		</div>
			     	
    </div>
  </div>	


</div>

</@wrap>

<#include "/wrap/widget.ftl" />

<script>
  window.__type__ = 0;
  window.__key__ = 2002029;
  window.__address__ = ${JsonUtils.toJson(data.orderExpInfoVO)};
  window.__basicInfo__ = ${JsonUtils.toJson(data.basicInfo)};
  window.__invoice__ = ${JsonUtils.toJson(data.invoiceInOrdVOs)};
  window.__expressCompany__ = ${JsonUtils.toJson(expressCompany)};
  window.__invoice__ = ${JsonUtils.toJson(data.invoiceInOrdVOs)};
  window.__data__ = ${JsonUtils.toJson(data)};
  window.__businessId__ = window.__basicInfo__.businessId;
  window.__pages__ = ${JsonUtils.toJson(pages)};
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/order.detail.js?v=1.0.0.0"></script>
<script src="${jspro}lib/jquery/dist/jquery.js?v=1.0.0.0"></script>
<script src="${jspro}lib/bootstrap/dist/js/bootstrap.js?v=1.0.0.0"></script>
</body>
</html>