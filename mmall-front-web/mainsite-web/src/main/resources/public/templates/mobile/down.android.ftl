<#include "mobile.common.ftl">

<!DOCTYPE html>
<html>
    <head>
    <#--meta标签-->
    <@headMeta/>
        <#--@CSS-->
        <link type="text/css" href="/mobile/css/mobileBase.css" rel="stylesheet" />
        <link type="text/css" href="/mobile/css/mobileDownAndroid.css" rel="stylesheet" />
        <title>下载页</title>
        <!--[if lt IE 8]> <![endif]-->
    </head>
    <body>
        <#--container-->
        <div class="g-container">
            <#--内容铺底-->
            <img src="/mobile/images/down/down_android_01.jpg" />
            <img src="/mobile/images/down/down_android_02.jpg" />
            <img src="/mobile/images/down/down_android_03.jpg" />
            <img src="/mobile/images/down/down_android_04.jpg" />
            <#--App描述-->
            <div class="m-App-info">上mmall 抢专柜折扣</div>
            <#--下载按钮1-->
            <a class="down m-down-btn1" href="http://023.baiwandian.cn/res/apps/mmall_v1.0.0_normal.apk"></a>
             <#--下载按钮2-->
            <a class="down m-down-btn2" href="http://023.baiwandian.cn/res/apps/mmall_v1.0.0_normal.apk"></a>
        </div>
        <div class="g-downApp-remind">
            <img src="/mobile/images/androidweixin.jpg" width="306" class="androidRemind"/>
		</div>
        <script src="/mobile/javascript/lib/nej/src/define.js?pro=/mobile/javascript"></script>
        <script src="/mobile/javascript/page/down.js"></script>
    </body>
</html>