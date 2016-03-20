<#include "mobile.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/mobile/css/mobileHelpDetail.css" rel="stylesheet" />
		<title>客服帮助</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<#--container-->
		<div class="g-container">
			<#if detail.result??>
			<#list detail.result.list as l>
			<#--artical-->
			<div class="g-help-ans">
				<#--artical container-->
				<div class="m-help-container">
					<#--artical title-->
					<div class="m-ans-title">
						${l.title}
					</div>
					<#--artical detail-->
					<div class="m-ans-detail">
						${l.content?replace('&gt;','>')?replace('&lt;','<')}
					</div>
				</div>
			</div>
			</#list>
			<#else>
			<div class="g-no-ans">
				暂无数据
			</div>
			</#if>
		</div>
	</body>
</html>