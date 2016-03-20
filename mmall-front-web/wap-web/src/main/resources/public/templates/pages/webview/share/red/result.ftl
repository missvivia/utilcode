<#escape x as x?html>
	<@compress>
    <!DOCTYPE html>
		<#include '../../../../wrap/3g.common.ftl'>
    <html>
    <head>
        <title>抢红包</title>
		<@meta/>
		<@css/>
        <link rel='stylesheet' type='text/css' href='/src/css/page/webview/share_red.css'>
    </head>
    <body>
    <div class="m-grab">
        <@topbar external=true/>
        <div class="content">
            <#--已过期-->
            <#if state==0>
                <div>
                    <p>抱歉</p>
                    <p>该红包已过期</p>
                </div>
            <#--已领取-->
            <#elseif receive==1>
                <div class="received">
                    <p>这是您抢过的红包</p>
                    <p>手气不错</p>
                    <p>抢到<span>${amount}</span>元红包</p>
                </div>
            <#elseif receive==0>
            <#--已抢光-->
                <#if amount<=0>
                    <div>
                        <p>您来晚了</p>
                        <p>红包被抢光了</p>
                    </div>
                <#--抢到红包-->
                <#else>
                    <div class="success">
                        <p>手气不错~</p>
                        <p class="txt-1">抢到<span  class="txt-2">${amount}</span>元红包！</p>
                        <a href="/coupon/redpacketlist"><div class="useBtn"></div></a>
                    </div>
                </#if>
            </#if>
        </div>
    </div>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/webview/share/red/result.js"></script>
    </body>
    </html>
	</@compress>
</#escape>