<#include "../mobile.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/mobile/css/mobileBase.css" rel="stylesheet" />
		<link type="text/css" href="/mobile/css/mobileShareHome.css" rel="stylesheet" />
		<title>主页-新品购</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<#--Open App-->
		<@openApp/>
		<#--container-->
		<div class="g-container">
			<iframe src="http://023.baiwandian.cn" id="m-misshome" frameborder="0"></iframe>
		</div>
		<#--@script-->
		<script src="/mobile/javascript/lib/nej/src/define.js?pro=/mobile/javascript"></script>
		<script src="/mobile/javascript/page/share/home/home.js"></script>
	</body>
</html>