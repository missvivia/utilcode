<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../wrap/common.ftl">
    <#include "../wrap/front.ftl">
    <@title type="unopen"/>
    <@css/>
</head>
<body>
	<div class="wrap">
		<a href="/"><img src="/src/images/logo.png" class="err-logo" /></a>
	</div>
	<div class="err-line">
		<div class="wrap err-page">
			<img src="/src/images/unopen.png"/>
			<a href="/">返回首页</a>
		</div>
	</div>
	<@footer/>
	<@copyright/>
</body>
</html>
</@compress>
</#escape>