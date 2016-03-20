<#include "app.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/src/css/page/webview/app_help.css" rel="stylesheet" />
		<title>客服帮助</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<#--container-->
		<div class="g-container">
			<@topbar></@topbar>
			<#--请求成功-->
			<#if navigation.code == 200>
			<#--一级菜单-->
			<ul class="g-help-list">
				<#list navigation.result.list as l>
					<#--有二级菜单展示-->
					<#if l.children??>
						<li class="m-help-dropdown">
							<#--一级菜单名称-->
							<a href="javascript:;" class="m-help-tip1">${l.name}<i class="m-help-icon1"></i></a>
							<#--二级菜单-->
							<ul class="m-help-dropdownMenu">
								<#list l.children as lc>
									<li>
										<#--二级菜单内容-->
										<a href=${"/m/help/article?categoryId="+lc.id} class="m-help-tip2" target="_blank">
											<p>${lc.name}</p>
											<i class="m-help-icon2"></i>
										</a>
									</li>
								</#list>
							</ul>
						</li>
					<#--无二级菜单展示-->
					<#else>
					<li class="m-help-check">
						<#--跳转内容-->
						<a href=${"/m/help/article?categoryId="+l.id} class="m-help-tip1" target="_blank">
							<p>${l.name}</p>
							<i class="m-help-icon3"></i>
						</a>
					</li>
					</#if>
				</#list>			
			</ul>
			<#--请求失败-->
			<#else>
				<div class="g-no-ans">请求失败，请检查您的网络环境</div>
			</#if>
		</div>
		<#--@script-->
		<script src="${jslib}define.js?${jscnf}"></script>
        <script src="${jspro}page/webview/help.js"></script>
	</body>
</html>