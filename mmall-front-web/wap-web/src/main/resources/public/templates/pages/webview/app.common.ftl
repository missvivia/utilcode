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

<#-- 顶栏 -->
<#macro topbar title="mmall">
<header class="g-hd m-topbox hdhide" id="paopao-header" style="display:none;">
  <div class="headerholder">
  </div>
  <div class="headerbox">
	  <div class="m-download1 f-cb">
	  	<span id="closedld" class="closebox  f-fl"><i class="close"></i></span>
	  	<img class="f-fl" src="/res/3g/images/download.png"/>
	  	<div class="desc f-fl">
	  		<div class="sitename">mmall</div>
	  		<div class="slogan">时尚新品抢购</div>
	  	</div>
	  	<span class="m-dldbtn" id="dldbtn">打开</span>
	  </div>
	  <div class="m-topnav f-tac" id="topbar-box">
	    <i class="f-fl u-menu" id="menu"></i>
	    <ul class="f-fr m-uinfo">
	    	<li class="f-fr m-cart" id="topcart"><a class="u-bag" href="/cart"></a><span class="cart"></span></li>
	    	<li class="f-fr"><a class="u-face" href="/profile/index"></a></li>
	    </ul>
	    <span class="tt f-toe" id="title"><#if title="首页"><i class="u-paopao" ></i><#else>${title}</#if></span>
	    <ul class="m-menu" id="menupop">
		  	<li><i class="icon u-index"></i><a class="itm" href="/index">首页<i class="u-more"></i></a></li>
		  	<li><i class="icon u-dress"></i><a class="itm" href="/dress">女装<i class="u-more"></i></a></li>
		  	<li><i class="icon u-gentlemen"></i><a class="itm" href="/gentlemen">男装<i class="u-more"></i></a></li>
		  	<li><i class="icon u-kidswear"></i><a class="itm" href="/kidswear">童装<i class="u-more"></i></a></li>
	  	</ul>
  	  </div>
   </div>
   <div class="u-top" id="gotop"></div>
</header>
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
			<img src="/res/3g/images/webview/app_icon.png" width="45" />
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
	<img src="/res/3g/images/webview/wx_ios.jpg" width="270" class="iosRemind"/>
	<img src="/res/3g/images/webview/wx_android.jpg" width="306" class="androidRemind"/>
</div>
</#macro>

<#--mobile常用路径定义-->
<#assign jslib = "http://fed.hz.netease.com/git/nej2/src/"/>
<#assign jslib = "/src/javascript/lib/nej/src/"/>
<#assign jspro = "/src/javascript/"/>
<#assign jscnf = "pro="+jspro+"&p=wk"/>

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