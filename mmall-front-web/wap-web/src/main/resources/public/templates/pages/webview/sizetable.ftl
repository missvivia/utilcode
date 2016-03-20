<#include "app.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/src/css/page/webview/app_base.css" rel="stylesheet" />
		<link type="text/css" href="/src/css/page/webview/app_sizetable.css" rel="stylesheet" />
		<title>商品尺码</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<#--container-->
		<div class="g-container">
			<#--尺码助手-->
			<#if product.productSize?has_content>
			<div class="g-part-size">
				<div class="g-part-name">
					商品尺码
				</div>
				<#--尺码对照表-->
				<div class="m-table-name">尺码对照表</div>
				<table class="g-size-table">
					<#--表头-->
					<tr>
						<#list product.productSize.header as hd>
						<th>${hd.name!}</th>
						</#list>
					</tr>
					<#--对应尺码-->
					<#list product.productSize.body as bd>
					<tr>
						<#list bd as item>
						<td>${item!}</td>
						</#list>
					</tr>
					</#list>
					<#--温馨提示-->
					<#if product.productSize.tip??>
					<tr>
						<td colspan=${(product.productSize.header)?size} class="m-size-tip">
							<span>温馨提示：</span>${product.productSize.tip}
						</td>
					</tr>
					</#if>
				</table>
				<#--尺码助手-->
				<#if product.helper??>
		            <div class="m-table-name">
		            	尺码助手
		            </div>
		            <table class="g-helper-table">
					<#--表头-->
					<tr>
						<#list product.helper.vaxis.list as hd>
						<#if hd_index == 0>
						<th class="m-helper-head">
							<div class="m-helper-corner"></div>
							<span class="m-helper-info1">身高cm</span>
							<span class="m-helper-info2">体重kg</span>
						</th>
						</#if>
						<th>${hd}</th>
						</#list>
					</tr>
					<#list product.helper.body as bd>
					<#if isEmpty(bd) == 0>
					<tr>
						<td>${product.helper.haxis.list[bd_index]}</td>
						<#list bd as item>
						<td>${item!}</td>
						</#list>
					</tr>
					</#if>
					</#list>
				</table>
	            </#if>
			</div>
			</#if>
		</div>
	</body>
</html>