<#include "../app.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/src/css/page/webview/app_base.css" rel="stylesheet" />
		<link type="text/css" href="/src/css/page/webview/share_po.css" rel="stylesheet" />
		<title>品购页-mmall</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body >
		<noscript></noscript>
		<#--Open App-->
		<@openApp/>
		<@topbar></@topbar>
		<#--判断地理位置是否满足需求-->
		<#if canAccess == 0>
			<#--地域错误提示-->
			<#include "po.end.ftl">
		<#elseif activeFlag == 2>
			<#--活动结束提示-->
			<#include "po.error.ftl">
		<#else>
			<#--商品列表展示-->
			<#include "po.list.ftl">
		</#if>
	</body>
</html>