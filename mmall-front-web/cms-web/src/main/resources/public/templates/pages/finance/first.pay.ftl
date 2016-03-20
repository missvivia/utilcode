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
	        首付款对账单
	      </h2>
	      <div class="card_c f-cb">
	        <div class="col-sm-12">
	        	<table class="m-billtable">
	        		<thead>
		        		<tr>
			        		<th colspan=6 class="title">供应商首付款收入确认单</th>
		        		</tr>
	        		</thead>
	        		<tbody>
		        		<tr>
		        			<td colspan=2>供应商：</td><td colspan=4>${result.supplierName!''}</td>
		        		</tr>
		        		<tr>
		        			<td colspan=2>PO单编号：</td><td colspan=4>${result.poId!''}</td>
		        		</tr>
		        		<tr>
		        			<td colspan=2>档期开始/结束时间：</td><td colspan=4>${result.startDate!''}——${result.endDate!''}</td>
		        		</tr>
		        		<tr>
		        			<td rowspan=2>结款明细</td><td>商品销售总额(零售价)</td><td>平台技术服务费占比</td>
		        			<td>首付款占比</td><td></td><td></td>
		        		</tr>
		        		<tr>
		        			<td>${result.totalOriRPrice!''}</td><td>${result.platformSrvFeeRate!0}%</td><td>50%</td>
		        			<td></td><td></td>
		        		</tr>
		        		<tr>
		        			<td>实际收入</td><td colspan=5>${result.firstPayPrice!''}</td>
		        		</tr>
		        		<tr>
		        			<td colspan=6>收款方：${result.supplierName!''}</td>
		        		</tr>
		        		<tr>
		        			<td colspan=6>付款方：杭州网易印象科技有限公司</td>
		        		</tr>
		        		<tr>
		        			<td colspan=6>
		        				<p class="f-mgtb">
		        					&nbsp;&nbsp;&nbsp;&nbsp;${result.startDate!''}——${result.endDate!''} 档期PO${result.poId!''}, ${result.supplierName!''}返款首付款金额￥${result.firstPayPrice!''}元。付款后，则${result.startDate!''}——${result.endDate!''} 档期PO${result.poId!''}  ${result.supplierName!''}首付款结清。
		        				</p>
		        			</td>
		        		</tr>
		        		<tr>
		        			<td colspan=2>公司名称：</td><td colspan=4></td>
		        		</tr>
		        		<tr>
		        			<td colspan=2>开户银行名称：</td><td colspan=4></td>
		        		</tr>
		        		<tr>
		        			<td colspan=2>开户银行帐号：</td><td colspan=4></td>
		        		</tr>
		        		<tr>
		        			<td colspan=2>收款方确认(签名并盖章)：</td><td colspan=4></td>
		        		</tr>
		        		<tr>
		        			<td colspan=2></td><td colspan=4>年  月  日</td>
		        		</tr>
		        		<tr>
		        			<td colspan=6>
		        				备注：
		        				<ul>
		        					<li>1. 供应商档期首付款结算中，默认存在50%的退换货率;</li>
		        					<li>2. 供应商档期首付款 = (商品销量*商品零售价 - 应收技术服务费)*50%。应收技术服务费 = 商品销量*商品零售价*平台技术服务费占比。下线后五个工作日提供《供应商档期首付款收入确认单》。受到盖章对账单(允许扫描件)后10个工作日内付款; </li>
		        					<li>3. 档期首付款为供应商收入预估, 具体详细的收入款在尾款结算时按实际结算。</li>
		        				</ul>
		        			</td>
		        		</tr>
	        		</tbody>
	        	</table>
		        <div class="m-wbtns">
		        	<a target="_self" href="/finance/firstpay/detail?poId=${RequestParameters["poId"]}" class="btn btn-primary">导出商品销售明细</a>
		        </div>
	        </div>
	      </div>
	    </div>
	  </div>
	</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
</body>
</html>