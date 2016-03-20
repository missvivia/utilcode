<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include '../../wrap/3g.common.ftl'>
    <html>
    <head>
        <title>反馈</title>
        <@meta/>
        <@css/>
        <link rel='stylesheet' type='text/css' href='/src/css/page/feedback.css'>
    </head>
    <body id="coupon-netease-com">
        <@topbar title="反馈"/>
        <div class="g-bd">
	        <div id="form"></div>
	        <div id="success"></div>
	        <div id="tips"></div>
        </div>
    <@ga/>
    
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/feedback/index.js"></script>
    </body>
    </html>
    </@compress>
</#escape>