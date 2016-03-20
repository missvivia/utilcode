<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../wrap/common.ftl">
    <#include "../wrap/front.ftl">
    <@title type="404"/>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" href="/favicon.ico?v=2" />
    <@css/>
</head>
<body>
	<@top/>
	<div class="g-bd wrap nocart">
	  <div>
	  	<img src="/src/images/error404.png"/>
	  	<p class="msg">哎呀重复提交了！</p>
	  </div>
	  <div class="btns">
	   	  <!--  <a href="javascript:history.back();" class="bg-red">返回上一页</a> -->
            <a href="/" class="bg-red">返回首页</a>
	  </div>
	</div>
    <@footer/>
    <@copyright />
    <@cityChange />
</body>
</html>
</@compress>
</#escape>