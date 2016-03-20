<#assign pageName="order-query"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
		.orderListBox{padding-top:10px;}
		.orderListBox .form-inline{padding:0 10px 10px 10px;}
		#orderColumn-menu .coup a{
		    border-top:1px solid #eee;
		}
		.dropdown-menu{
		    top:32px;
		}
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

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'#'} sub={"txt":"订单查询"}/>
<!-- card -->
<div class="row orderListBox">
  <div class="col-sm-12">
    <div class="m-card">

      <div class="card_c f-cb">
        <div class="form-inline">
			  <div class="form-group">
			    <label for="queryType">搜索类型：</label>
			    <select id="queryType" name="queryType" class="form-control">
			        <option value="">请选择</option>
			    	<option value="5">订单号</option>
			    	<option value="6">买家帐号</option>
			    	<option value="7">卖家帐号</option>
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
					<span id="startTime"></span>
				</div>
				一
				<div class="form-group">
					<span id="endTime"></span>
				</div>
				<div class="form-group">
					<div class="btn-group orderColumn-group">
					    <button type="button" id="orderColumn-btn" class="btn btn-default dropdown-toggle" 
					      data-toggle="dropdown">
					      <span id="orderColumn-btn-txt" data-asc="orderTime|false" data-coupon="false">日期由近到远</span>
					      <span class="caret"></span>
					    </button>
					    <ul class="dropdown-menu" id="orderColumn-menu">
					      <li class="orderColumn-asc-li"><a href="#" class="orderColumn-asc-txt" data-value = "orderTime|false">日期由近到远</a></li>
					      <li class="orderColumn-asc-li"><a href="#" class="orderColumn-asc-txt" data-value = "orderTime|true">日期由远到近</a></li>
					      <li class="orderColumn-asc-li price"><a href="#" class="orderColumn-asc-txt" data-value = "CartRPrice|true">订单金额升序</a></li>
					      <li class="orderColumn-asc-li price"><a href="#" class="orderColumn-asc-txt" data-value = "CartRPrice|false">订单金额降序</a></li>
					      <li class="coup"><a href="#"><input type="checkbox" id="useCoup"/>使用优惠券</a></li>
					    </ul>
					 </div>
				</div>
				<div class="form-group">
					<select class="form-control" name="orderStatus" id="orderStatus">
						<option value="">所有订单状态</option>
						<option value="0">等待付款</option>
						<option value="6">待发货</option>
						<option value="10">已发货</option>
						<option value="11">交易完成</option>
						<option value="21">已取消</option>
					</select>
				</div>
				<div class="form-group">
					<#-- 在线支付-未付款和货到付款-已支付，这两种状态不能进行发货操作 -->
					<select class="form-control" name="payStatus" id="payStatus">
						<option value="">所有支付状态</option>
						<option value="20">在线支付-未付款</option>
						<option value="21">货到付款-未支付</option>
						<option value="30">在线支付-已支付</option>
						<option value="31">货到付款-已支付</option>
					</select>
				</div>
      		<button class="btn btn-primary j-flag" id="search">查找</button>
      	</div>
      </div>
      <div class="card_c f-cb">
      	<div id="orderListCon" class="j-list"></div>
      </div>
    </div>
  </div>
</div>


</@wrap>
<script id="orderListTpl" type="text/regular" name='orderListTpl'>
	<div class="m-wtable" id="wtable">
	  <table class="table table-striped">
	    <thead>
	      <tr>
	        <th>订单ID</th>
	        <th>买家帐号</th>
	        <th>卖家帐号</th>
	        <th>订单状态</th>
	        <th>支付方式</th>
	        <th>支付状态</th>
	        <th>下单时间</th>
	        <th>订单原价</th>
	        <th>实付金额</th>
	      </tr>
	    </thead>
	    <tbody>
	      {{#list list as tpl}}
	        <tr>
	          <td><a target="_blank" href="/order/getorderdetail?orderId={{tpl.id}}">{{tpl.id}}</a></td>
	          <td>{{tpl.userName}}</td>
	          <td>{{tpl.businessAccount}}</td>
	          <td>{{statusMap[tpl.status]}}</td>
	          <td>{{payMethod[tpl.payMethod]}}</td>
	          <td>{{payStatus[tpl.payStatus]}}</td>
	          <td>{{tpl.orderTime}}</td>
	          <td>￥{{tpl.price}}</td>
	          <td>￥{{tpl.orderPay}}{{#if tpl.useCoupon == true}}<i class="coupon-icon"></i>{{/if}}{{#if tpl.spsource == 7}}<i class="proxy-icon"></i>{{/if}}</td>
	        </tr>
	      {{/list}}
	    </tbody>
	  </table>
	</div>
	<div class="text-right m-wpager">
	  <pager total={{Math.ceil(total / 10)}} current={{current}} ></pager>
	</div>
</script>

<script>
  window.__typeList__ = ${stringify(data)}
  <#if RequestParameters["key"]??>
  	window.__key__ = '${RequestParameters["key"]}';
  <#else>
  window.__key__ = 0;
  window.__startTime__ = '1306598400000';
  window.__endTime__ = '1434038399999';
  </#if>
  window.__type__ = 0;
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/order/order.query.js?v=1.0.0.2"></script>

</body>
</html>