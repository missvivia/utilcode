<#include "app.common.ftl">
<#include "../../wrap/3g.common.ftl">
<!DOCTYPE html>
<html>
    <head>
    <#--meta标签-->
    <@headMeta/>
        <!--@CSS-->
        <link type="text/css" href="/src/css/page/webview/app_base.css" rel="stylesheet" />
        <link type="text/css" href="/src/css/page/webview/down_ios.css" rel="stylesheet" />
        <title>下载页</title>
        <!--[if lt IE 8]> <![endif]-->
    </head>
    <body>
        <#--container-->
        <div class="g-container">
        	<@topbar>
   			</@topbar>
            <#--内容铺底-->
            <img src="/res/3g/images/webview/down/down_ios_01.jpg" />
            <img src="/res/3g/images/webview/down/down_ios_02.jpg" />
            <img src="/res/3g/images/webview/down/down_ios_03.jpg" />
            <img src="/res/3g/images/webview/down/down_ios_04.jpg" />
             <#--App描述-->
            <div class="m-App-info">上mmall 抢专柜折扣</div>
             <#--下载按钮1-->
            <a class="down m-down-btn1" href="https://itunes.apple.com/cn/app/id950899740"></a>
             <#--下载按钮2-->
            <a class="down m-down-btn2" href="https://itunes.apple.com/cn/app/id950899740"></a>
        </div>
        <div class="g-downApp-remind">
			<img src="/res/3g/images/webview/wx_ios.jpg" width="270" class="iosRemind"/>
		</div>
          
        <script src="${jslib}define.js?${jscnf}"></script>
        <script src="${jspro}page/webview/down.ios.js"></script>
    </body>
</html>