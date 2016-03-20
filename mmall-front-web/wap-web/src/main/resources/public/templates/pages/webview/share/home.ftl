<#include "../app.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/src/css/page/webview/app_base.css" rel="stylesheet" />
		<link type="text/css" href="/src/css/page/webview/share_home.css" rel="stylesheet" />
		<title>主页-mmall</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<#--Open App-->
		<@openApp/>
		<#--container-->
		<div class="g-container">
			<@topbar></@topbar>
			<iframe src="http://023.baiwandian.cn" id="m-misshome" frameborder="0"></iframe>
		</div>
		<#--@script-->
		<script src="${jslib}define.js?${jscnf}"></script>
        <script src="${jspro}page/webview/share/home/home.js"></script>
	</body>
</html>