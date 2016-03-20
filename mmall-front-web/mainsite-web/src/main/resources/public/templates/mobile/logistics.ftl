<#include "mobile.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/mobile/css/mobileBase.css" rel="stylesheet" />
		<link type="text/css" href="/mobile/css/mobileLogistics.css" rel="stylesheet" />
		<title>物流追踪</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<#--container-->
		<div class="g-container">
			<#--快递信息-->
			<#if expressCompany != 'null' && expressNO != 'null'>
			<div class="g-info">
				<p>快递公司<span>${(expressCompany)!}</span></p>
				<p>运单号<span>${(expressNO)!}</span></p>
			</div>
			</#if>
			<#if !orderTrace?? || orderTrace?size == 0 >
				<div class="g-noinfo">
					还没有物流信息呢
				</div>
			</#if>
			<#--快递轨迹-->
			<div class="g-order">
				<div class="g-order-flow">
					<ul class="g-order-storey">
						<#--当前包裹无数据-->
						<#if !orderTrace?? || orderTrace?size == 0>
						
						<#--仅有一条数据-->
						<#elseif orderTrace?size == 1>
						<li class="m-order-detail">
							<#--标记-->
							<span class="m-order-icon"></span>
							<#--信息：有当前操作人员信息-->
							<#if orderTrace[0].operater??>
							<span>${orderTrace[0].operateOrg}&emsp;${orderTrace[0].operate}&emsp;当前操作人员:${orderTrace[0].operater}&emsp;<a href=${"tel:"+(orderTrace[0].operaterPhone)!}>${(orderTrace[0].operaterPhone)!}</a>&emsp;${orderTrace[0].note}</span>
							<#--信息：无当前操作人员信息-->
							<#else>
							<span>${orderTrace[0].operateOrg}&emsp;${orderTrace[0].operate}&emsp;${orderTrace[0].note}</span>
							</#if>
							<#--时间-->
							<span class="m-order-time">${orderTrace[0].time}</span>
						</li>
						<#--快递轨迹-->
						<#else>
						<#list orderTrace as t>
						<li class="m-order-details">
							<span class="m-order-icon"></span>
							<#--信息：有当前操作人员信息-->
							<#if t.operater??>
							<span>${t.operateOrg}&emsp;${t.operate}&emsp;当前操作人员:${t.operater}&emsp;<a href=${"tel:"+(t.operaterPhone)!}>${(t.operaterPhone)!}</a>&emsp;${t.note}</span>
							<#--信息：无当前操作人员信息-->
							<#else>
							<span>${t.operateOrg}&emsp;${t.operate}&emsp;${t.note}</span>
							</#if>
							<#--时间-->
							<span class="m-order-time">${t.time}</span>
						</li>
						</#list>
						</#if>
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>