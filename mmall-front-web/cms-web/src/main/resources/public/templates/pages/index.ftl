<#assign pageName=""/>

<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>运营系统首页</title>
  <#include "/wrap/css.ftl">
  
</head>
<body>
<@side />
<@wrap>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
    <div class="card_c">
     <#assign jobNum =3 />
      <div class="form-horizontal">
      	<div class="form-group">
		     <div class="col-sm-12" id="username"><span></span>,你好，你有${jobNum}项任务待处理~</div>
	    </div>
	  </div>
	   
      <div class="form-horizontal">
      	<div class="form-group f-cb">
	      	<div class="col-sm-12">
	      		<#if (webEmptyList??&&webEmptyList?size!=0)||(IOSEmptyList??&&IOSEmptyList?size!=0)||(androidEmpeyList??&&androidEmpeyList?size!=0)>
			     <h3> 内容管理报警：</h3>
			     <table class="table table-bordered table-condensed">
			     	<tr>
			     		<td>日期</td>
			     		<td>平台</td>
			     		<td>站点</td>
			     	</tr>
			     	<#if webEmptyList??&&webEmptyList?size=0>
				     	<#list webEmptyList as item>
				     		<tr>
					     		<td>${item.date?number_to_date?string('yyyy-MM-dd')}</td>
					     		<td>WEB</td>
					     		<td><#list item.siteList as site> ${site}<#if item_has_next>、</#if></#list></td>
				     		</tr>
				     	</#list>
			     	</#if>
			     	<#if IOSEmptyList??&&IOSEmptyList?size=0>
				     	<#list IOSEmptyList as item>
				     		<tr>
					     		<td>${item.date?number_to_date?string('yyyy-MM-dd')}</td>
					     		<td>IOS</td>
					     		<td><#list item.siteList as site>${site}<#if item_has_next>、</#if></#list></td>
				     		</tr>
				     	</#list>
			     	</#if>
			     	<#if androidEmpeyList??&&androidEmpeyList?size=0>
				     	<#list androidEmpeyList as item>
				     		<tr>
					     		<td>${item.date?number_to_date?string('yyyy-MM-dd')}</td>
					     		<td>ANDROID</td>
					     		<td><#list item.siteList as site>${site}<#if item_has_next>、</#if></#list></td>
				     		</tr>
				     	</#list>
			     	</#if>
			     </table>
			     </#if>
			     <#--
			     <div class="f-fl">
			     
			     <#assign alarm={"webEmptyList":[{"siteName":"浙江"},{"siteName":"江苏"}],
			     				"IOSEmptyList":[{"siteName":"浙江"},{"siteName":"江苏"}],
			     				"androidEmpeyList":[{"siteName":"浙江"},{"siteName":"江苏"}]}>
			     
			     	<div>WEB端以下站点内容即将轮空:<#if webEmptyList??&&webEmptyList?size!=0><#list webEmptyList as item>${item.siteName}<#if item_has_next>、</#if></#list></#if></div>
			     	<div>IOS客户端以下站点内容即将轮空:<#if IOSEmptyList??&&IOSEmptyList?size!=0><#list IOSEmptyList as item>${item.siteName}<#if item_has_next>、</#if></#list></#if></div>
			     	<div>android客户端以下站点内容即将轮空:<#if androidEmpeyList??&&androidEmpeyList?size!=0><#list androidEmpeyList as item>${item.siteName}<#if item_has_next>、</#if></#list></#if></div>
			     </div>
			     -->
			 </div>
	    </div>
	   </div>
	   <#--
		<#assign pendingPOList=[{"siteName":"浙江江","count":2},{"siteName":'浙江江',"count":2}]/>
		<#assign pendingAuditList=[{"siteName":"浙江","productListCount":2,"meterialCount":2,"pageCount":2,"brandCount":2,"spreadCount":2},
									{"siteName":"江苏","productListCount":2,"meterialCount":2,"pageCount":2,"brandCount":2,"spreadCount":2}]/>	
		<#assign promotionAuditList = {"couponCount":2, "giftCount":2, "activitys":[{"siteName":"浙江","activity":2},{{"siteName":"江苏","activity":2}]}/>
		<#assign orderAuditList =[{"siteName":'浙江江',"toPayCount":2,"returnGoodsCount":2,"returnMoneyCount":2},
									{"siteName":'江苏',"toPayCount":2,"returnGoodsCount":2,"returnMoneyCount":2}]>	
	-->										
	    <h3>待完成工作</h3>
	    <#if pendingPOList??&&pendingPOList?size!=0>
        <table class="table table-bordered table-condensed">
			 	<tr>
			      <th colspan="2">待审核PO</th>
			    </tr>
			    <tr>
			      <td class="th-xl">站点</td>
			      <td class='th-xl' >PO数</td>
			    </tr>
			    <#list pendingPOList as item>
			    <tr>
			      <td class="th-xl">${item.siteName}</td>
			      <td class='th-xl' >${item.count}</td>
			    </tr>
			    </#list>
        </table>
        </#if>
        
        
		<#if pendingAuditList??&&pendingAuditList?size!=0>
        <table class="table table-bordered table-condensed">
			 	 <tr>
			      <th colspan="6">PO/品牌页审核清单</th>
			    </tr>
			    <tr>
			      <td>站点</td>
			      <td class='th-xl' >清单</td>
			      <td class='th-xl'>资料</td>
			      <td class='th-xl' >品购页</td>
			      <td class='th-xl' >推广</td>
			      <td class='th-xl' >品牌页</td>
			    </tr>
			    <#list pendingAuditList as item>
			    <tr>
			      <td class='th-xl' >${item.siteName}</td>
			      <td class='th-xl'>${item.productListCount}</td>
			      <td class='th-xl' >${item.meterialCount}</td>
			      <td class='th-xl' >${item.pageCount}</td>
			      <td class='th-xl' >${item.spreadCount}</td>
			      <td class='th-xl' >${item.brandCount}</td>
			    </tr>
			    </#list>
        </table>
        </#if>
        
		<#if promotionAudit??>
		优惠券:${promotionAudit.couponCount} 红包：${promotionAudit.giftCount}  
        <table class="table table-bordered table-condensed">
			 	 <tr>
			      <th colspan="4">促销审核</th>
			    </tr>
			    <tr>
			      <td>站点</td>
			      <td class='th-xl' >活动</td>
			    </tr>
			    <#list promotionAudit.activitys as item>
			    <tr>
			      <td>${item.siteName}</td>
			      <td class='th-xl' >${item.activity}</td>
			    </tr>
			    </#list>
        </table>
        </#if>
        
        <#if orderAuditList??&&orderAuditList?size!=0>
        <table class="table table-bordered table-condensed">
			 	 <tr>
			      <th colspan="4"> 订单审核任务列表</th>
			    </tr>
			    <tr>
			      <td>站点</td>
			      <td class='th-xl' >到付审核</td>
			      <td class='th-xl'>退货确认（客服）</td>
			      <td class='th-xl' >退款确认（财务</td>
			    </tr>
			    <#list orderAuditList as item>
			    <tr>
			      <td>${item.siteName}</td>
			      <td class='th-xl' >${item.toPayCount}</td>
			      <td class='th-xl'>${item.returnGoodsCount}</td>
			      <td class='th-xl' >${item.returnMoneyCount}</td>
			    </tr>
			   </#list>
        </table>
        </#if>
       </div>
    </div>
  </div>
</div>
</@wrap>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/index.js?v=1.0.0.0"></script>
</body>
</html>