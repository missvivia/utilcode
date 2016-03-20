<#--head meta标签-->
<#macro  headMeta>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no,email=no" />
<meta name="viewport" content="width=device-width, initial-scale=1.0,user-scalable=no">
<meta http-equiv="x-dns-prefetch-control" content="on" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</#macro>

<#--head web view标签-->
<#macro  webViewMeta>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no,email=no" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="x-dns-prefetch-control" content="on" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</#macro>

<#--唤醒App 浮动层-->
<#macro openApp>
<div class="g-downApp">
	<div class="m-downApp-wrap">
		<div class="m-downApp-logo">
			<img src="/mobile/images/app_icon.png" width="45" />
		</div>
		<div class="m-downApp-alogo">
			<p class="m-client-name">
                上mmall，抢专柜折扣！
			</p>
		</div>
		<div class="m-downApp-open">
			<a id="openMISS"><span class="m-open-btn">打开</span></a>
		</div>
	</div>
</div>
<div class="g-downApp-remind">
	<img src="/mobile/images/iosweixin.jpg" width="270" class="iosRemind"/>
	<img src="/mobile/images/androidweixin.jpg" width="306" class="androidRemind"/>
</div>
</#macro>

<#--mobile常用路径定义-->
<#assign jslib = "/src/javascript/mobile/lib/nej/src/"/>
<#assign jspro = "/src/javascript/mobile/"/> 
<#assign jscnf = "pro="+jspro/>

<#--尺码助手      数组元素全空判断-->
<#function isEmpty Array>
	<#local flag = 0>
	<#list Array as A>
		<#if A == "">
			<#local flag = flag + 1>
		</#if>
	</#list>
	<#if flag == Array?size>
		<#return 1>
	<#else>
		<#return 0>
	</#if>
</#function>