<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include "../../wrap/3g.common.ftl">
    <html>
    <head>
        <title>${title!"活动规则"}</title>
        <@meta/>
        <@css/>
        <style>
            .p-rule{background:#ffa6d6;}
            .m-rule{text-align:center;}
            .m-rule .detail img{margin:0;max-width:100%;}
        </style>
    </head>
    <body id="schedule-netease-com" class="p-rule">
		<@topbar>
    	</@topbar>
        <@module>
        <div class="m-rule">
            <div class="detail">
                <img src="/res/3g/images/activity/0228/rule_1.jpg" />
                <img src="/res/3g/images/activity/0228/rule_2.jpg" />
                <img src="/res/3g/images/activity/0228/rule_3.jpg" />
            </div>
        </div>
        </@module>
	<@ga/>
	<script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/activity/rule0228.js"></script>
    </body>
    </html>
    </@compress>
</#escape>