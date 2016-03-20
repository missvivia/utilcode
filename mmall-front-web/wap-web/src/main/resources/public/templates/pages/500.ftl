<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include "../wrap/3g.common.ftl">
    <html>
    <head>
        <title>${title!"新云联百万店-500"}</title>
        <@meta/>
        <@css/>
        <style>
            .p-r500{background:#ededed;}
            .m-r500{text-align:center;}
            .m-r500 .msg{color:#999;}
            .m-r500 .msg .pic{width:260px;margin:70px auto 15px;}
            .m-r500 .msg .rmd{font-size:16px;}
            .m-r500 .msg .btn{margin:5px auto;width:156px;height:42px;line-height:42px;border-radius:3px;font-size:18px;font-weight:600;background:#9144ae;}
            .m-r500 .msg .btn a{display:block;color:#fff;}
        </style>
    </head>
    <body id="schedule-netease-com" class="p-r500">

        <@module>
        <div class="m-r500">
            <div class="msg">
                <img class="pic" src="/res/3g/images/error_500.jpg" width="260" />
                <p class="rmd">出错啦，找回正确的道路吧！</p>
                <#--<div class="btn"><a href="/">返回首页</a></div>-->
            </div>
        </div>
        </@module>
	<@ga/>
    </body>
    </html>
    </@compress>
</#escape>