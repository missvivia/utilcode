<#if pendingPage == true>
<#assign pageName="order-pendingorderlist">
<#else>
<#assign pageName="order-orderlist">
</#if>
<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" type="text/css" href="/src/css/page/order/orderList.css?v=1.0.0.0">
	<style type="text/css">
		.orderListBox{padding-top:10px;}
		.orderListBox .form-inline{padding:0 10px 10px 10px;}
		#sendForm label{text-align:right;}
		#sendForm .form-group{margin:0 0 10px 0;}
		span.js-error, .js-error{left: 210px;top: 20px;}
		.coupon-icon{
		    display: inline-block;
		    width:30px;
		    height:30px;
		    background:url(/res/images/coupon.png) no-repeat center;
		    margin-bottom:-10px;
		}
		.proxy-icon{
		    display: inline-block;
		    width:30px;
		    height:30px;
		    background:url(/res/images/proxy.png) no-repeat center;
		    margin-bottom:-10px;
		}
	</style>
</head>

<body>
<!-- 左边目录列表 -->
<@side/>
<!-- 右边内容区域 -->
<@wrap>
<#if pendingPage == true>
<@crumbs parent={"txt":"订单管理","url":'/order/orderlist'} sub={"txt":"待发货订单"}/>
<#else>
<@crumbs parent={"txt":"订单管理","url":'/order/orderlist'} sub={"txt":"订单列表"}/>
</#if>
<div class="orderListBox m-card">
	<div class="card_c">
		<form id="search-form">
			<div class="form-inline">
			  <div class="form-group">
			    <label for="searchType">搜索类型：</label>
			    <select id="searchType" name="searchType" class="form-control">
			    	<option value="orderId">订单号</option>
			    	<option value="userName">买家帐号</option>
			    </select>
			  </div>
			  <div class="form-group">
			    <label for="searchKey">搜索关键字：</label>
			    <input type="text" class="form-control" id="searchKey" name="searchKey" placeholder="请输入关键字">
			  </div>
			  
			</div>
			<div class="form-inline">
			    <label class="control-label">下单时间：</label>	
			    <div class="form-group">
					<div class="j-datepick" id="startTime" data-name="stime" data-value=""></div>
				<!--<datepicker name="stime" />-->
				</div>
				一
				<div class="form-group">
					<div class="j-datepick" id="endTime" data-name="etime" data-value=""></div>
					<!--<datepicker name="etime"/>-->
				</div>
				<div class="form-group">
					<select class="form-control" name="orderColumn" id="orderColumn">
						<option value="CreateTime|false">日期由近到远</option>
						<option value="CreateTime|true">日期由远到近</option>
						<option value="CartRPrice|true">订单金额升序</option>
						<option value="CartRPrice|false">订单金额降序</option>
					</select>
				</div>
				<div class="form-group">
					<#if pendingPage == false>
					<select class="form-control" name="orderStatus" id="orderStatus">
						<option value="">所有订单状态</option>
						<option value="0">等待付款</option>
						<option value="6">待发货</option>
						<option value="10">已发货</option>
						<option value="11">交易完成</option>
						<option value="21">已取消</option>
					</select>
					</#if>
				</div>
<#--
				<div class="form-group">
					<select class="form-control" name="payMethod" id="payMethod">
						<option value="">所有支付方式</option>
						<option value="0">网易宝支付</option>
						<option value="1">货到现金付款</option>
						<option value="2">支付宝支付</option>
						<option value="3">货到POS机付款</option>
					</select>
				</div>
-->
				<div class="form-group">
					<#-- 在线支付-未付款和货到付款-已支付，这两种状态不能进行发货操作 -->
					<select class="form-control" name="payStatus" id="payStatus">
						<option value="">所有支付状态</option>
						<#if pendingPage == false><option value="20">在线支付-未付款</option></#if>
						<option value="21">货到付款-未支付</option>
						<option value="30">在线支付-已支付</option>
						<#if pendingPage == false><option value="31">货到付款-已支付</option></#if>
					</select>
				</div>
				<input type="button" value="查找" class="btn btn-primary j-flag" name="btn-submit" id="submitBtn"/>
		   </div>
		</form>
		
		<div id="orderListCon" class="j-list"></div>
		<div id="expressBox" style="display:none;"><#list expressCompany as item>${item.code}-${item.getName()}|</#list></div>
	</div>
</div>
</@wrap>
<#noparse>
<script id="orderListTpl" type="text/regular" name='orderListTpl'>
	<div class="m-wtable" id="wtable">
	  <table class="table table-striped">
	    <tbody>
		    <tr>
		        <td>订单号</td>
		        <td>买家账号</td>
		        <td>订单状态</td>
		        <td>支付方式</td>
		        <td>支付状态</td>
		        <td>下单时间</td>
		        <td>订单金额(元)</td>
	          	<td>实付金额(元)</td>
		        <td>操作</td>
		      </tr>
	      {{#list list as item}}
	        <tr>
	          <td><a href="/order/detail?orderId={{item.orderId}}" target="_blank">{{item.orderId}}</a></td>
	          <td>{{item.userName}}</td>
	          <td>{{orderStatus[item.orderFormState]}}</td>
	          <td>{{payMethod[item.orderFormPayMethod]}}</td>
	          <td>{{payStatus[item.payState]}}</td>
	          <td>{{item.orderCreateTime}}</td>
	          <td>{{item.cartOriRPrice}}</td>
	          <td>{{item.cartRPrice}}{{#if item.useCoupon == true}}<i class="coupon-icon"></i>{{/if}}{{#if item.spSource == 7}}<i class="proxy-icon"></i>{{/if}}</td>
	          <td>
	            	{{#if item.orderFormState == 6}}
	            	<a href="javascript:;" on-click={{this.send(item)}}>发货</a>
	            	{{/if}}
	          </td>
	        </tr>
	      {{/list}}
	    </tbody>
	  </table>
	</div>
	
	<div class="text-right m-wpager f-cb">
	  <pager total={{Math.ceil(total / limit)}} current={{current}} ></pager>
	</div>	
</script>
</#noparse>
<#-- jst模板 -->
<#noparse>
<textarea id="sendFormTemplate" style="display:none;">
	<form class="form-horizontal" id="sendForm">
		<div class="form-group">
			<label class="col-sm-4">订单号：</label>
			<span class="col-md-6">${item.orderId}</span>
		</div>
		<div class="form-group">
			<label class="col-sm-4">订单金额：</label>
			<span class="col-md-6">${item.cartRPrice}</span>
		</div>
		<div class="form-group">
			<label class="col-sm-4">付款方式：</label>
			<span class="col-md-6">${item.payMethodFormat}</span>
		</div>
		<div class="form-group">
			<label class="col-sm-4 required">请选择物流公司：</label>
			<div class="col-md-6">
				<select id="express" class="form-control">
					{list item.expressData as i}
					<option value='${i.code}'>${i.name}</option>
					{/list}
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 required">填写物流单号：</label>
			<div class="col-md-6">
				<input type="text" class="form-control" id="mailNO" name="mainNO" data-required="true"  data-type="number" data-message="只能输入数字！"/>
			</div>
		</div>
	</form>	
</textarea>
</#noparse>
<#-- /jst模板 -->
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/orderList.js?v=1.0.0.2"></script>
</body>
</html>