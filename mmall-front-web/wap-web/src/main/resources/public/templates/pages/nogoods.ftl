<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include "../wrap/3g.common.ftl">
    <html>
    <head>
        <title>${title!"新云联百万店-无商品售卖"}</title>
        <@meta/>
        <@css/>
        <style>
            .p-nogoods{background:#f4f4f4;}
            .m-nogoods{text-align:center;}
            .m-nogoods .msg{color:#999;}
            .m-nogoods .msg .pic{margin:130px auto 15px;}
            .m-nogoods .msg h2{font-size:17px;font-weight:normal;}
            .m-nogoods .msg p{font-size:14px;}
        </style>
    </head>
    <body id="schedule-netease-com" class="p-nogoods">

        <@topbar></@topbar>

        <@module>
        <div class="m-nogoods">
            <div class="msg">
                <img class="pic" src="/res/3g/images/error_nogoods.png" width="73" />
                <h2>对不起</h2>
                <p>您所在的地区不支持购买</p>
            </div>
        </div>
        </@module>
		<@ga/>
        <script src="${jslib}define.js?${jscnf}"></script>
        <script src="${jspro}page/error/nogoods.js"></script>

    </body>
    </html>
    </@compress>
</#escape>