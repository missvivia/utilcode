<#assign pageName="finance-salequery"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/finance.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"财务管理","url":'#'} sub={"txt":"销售查询"}/>
<!-- card -->
  <div class="row">
	  <div class="col-sm-12">
	    <div class="m-card">
	      <h2 class="card_b">
	        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
	        全款对账单
	      </h2>
	      <div class="card_c f-cb">
	        <div class="col-sm-12">
	        	<div class="m-tabletop">
	        		<p class="title">杭州网易印像科技有限公司 供应商档期全款对账单</p>
	        		<div class="f-cb">
	        			<div class="f-fl">
	        				<p>尊敬的xxxx</p>
	        				<p>&nbsp;&nbsp;&nbsp;&nbsp;首先感谢贵公司对我司业务的大力支持，现就双方账务进行确认如下：</p>
	        			</div>
	        			<div class="f-fr">
	        				<p>&nbsp;</p>
	        				<p><span class="f-mr40">本次结算</span> <span class="f-mr40">100%款</span></p>
	        			</div>
	        		</div>
	        	</div>
	        	<table class="m-billtable m-billtable-1">
	        		<thead>
		        		<tr class="f-bc1">
			        		<th colspan=19 class="title">应付贵公司金额</th>
		        		</tr>
	        		</thead>
	        		<tbody>
	        			<tr>
	        				<td>PO单号</td><td>下线时间</td><td>品牌</td><td>来货数</td><td>退货数</td>
	        				<td>实收数量</td><td>采购金额</td><td>分成比(%)</td><td>活动应扣金额(一)</td><td>红包应扣金额(一)</td><td>优惠券应扣金额(一)</td>
	        				<td>来货/退货差异(一)</td><td>本期应付金额</td>
	        				<td>预付冲货款金额 </td><td>已付款项 </td><td> 本期实际应付金额（元）</td>
	        			</tr>
	        			<#if vo??>
		        		<tr>
		        			<td>${vo.poId!''}</td>
		        			<td>${vo.offLineTime!''}</td>
		        			<td>${vo.brandName!''}</td>
		        			<td>${vo.inCount!''}</td>
		        			<td>${vo.refundCount!''}</td>
		        			<td>${vo.actualCount!''}</td>
		        			<td>${vo.purchaseAmount!''}</td>
		        			<td>${vo.shareRatio!''}</td>
		        			<td>${vo.promotionDiscountAmount!''}</td>
		        			<td>${vo.redPacketDiscountAmount!''}</td>
		        			<td>${vo.couponDiscountAmount!''}</td>
		        			<td>${vo.inRefundDiffer!''}</td>
		        			<td>${vo.realPayAmount!''}</td>
		        			<td></td><td ></td><td ></td>
		        		</tr>
		        		<#else>
		        		<tr>
		        			<td></td><td></td><td></td><td></td><td></td><td></td>
		        			<td></td><td></td><td></td><td></td><td></td><td></td>
		        			<td></td><td></td><td></td><td></td>	
		        		</tr>
		        		</#if>
		        		<tr>
		        			<td>贷款结算小计</td><td colspan=2></td><td ></td><td ></td>
		        			<td ></td><td ></td ><td ></td><td ></td><td ></td><td ></td>
		        			<td ></td><td ></td><td ></td><td ></td><td ></td>
		        		</tr>
		        		<tr class="f-bc1">
		        			<td>帐扣项目</td><td colspan=6>帐扣内容</td>
		        			<td colspan=5></td><td>金额</td><td></td><td></td><td>减应付金额</td>
		        		</tr>
		        		<tr>
		        			<td></td><td  colspan=6></td><td  colspan=6></td>
		        			<td ></td><td ></td><td ></td>
		        		</tr>
		        		<tr>
		        			<td colspan=15>实付金额合计</td><td></td>
		        		</tr>
		        		<tr>
		        			<td colspan=3>本期实付金额</td><td colspan=13>xxxxxxxxxxx</td>
		        		</tr>
	        		</tbody>
	        	</table>
	        	<div>***推货数量未确定，暂按此时的数据*** 如有异议，请收到账单后三个工作日回复，否则以我司账单为准！</div>
	        	<div class="m-bottomtitle">杭州网易印象科技有限公司</div>
	        	<div class="f-cb m-bottomlist">
	        		<div class="f-fl">
	        			<ul>
	        				<li>供应商确认：</li>
	        				<li>联系资料：</li>
	        				<li>银行帐号：</li>
	        				<li>开户银行：</li>
	        				<li>供应商盖章</li>
	        			</ul>
	        		</div>
	        		<div class="f-fr">
	        			<ul class="f-mr160">
	        				<li>会计</li>
	        				<li>商助邮箱:<#if podto??>${podto.scheduleDTO.scheduleVice.poFollowerUserName!''}</#if></li>
	        				<li>电话</li>
	        				<li>邮箱</li>
	        				<li>日期</li>
	        			</ul>
	        		</div>
	        	</div>
		        <div class="m-wbtns">
		        	<a target="_self" href="/finance/inDetail?poId=${RequestParameters["poId"]}" class="btn btn-primary">导出来货明细</a>
		        	<a target="_self" href="/finance/exportDetail?poId=${RequestParameters["poId"]}" class="btn btn-primary">导出商品销售明细</a>
		        	<a target="_self" href="/finance/refundDetail?poId=${RequestParameters["poId"]}" class="btn btn-primary">导出退货明细</a>
		        </div>
	        </div>
	      </div>
	    </div>
	  </div>
	</div>
</@wrap>

<!-- @script -->
</body>
</html>