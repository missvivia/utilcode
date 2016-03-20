<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include "../../wrap/3g.common.ftl">
    <html>
    <head>
        <title>${title!"邮箱大师用户专享"}</title>
        <@meta/>
        <@css/>
        <style>
            .p-coupon{background:#ffa6d6;}
            .m-coupon{text-align:center;}
            .m-coupon .detail img{margin:0;max-width:100%;}
            .m-coupon .detail .shopping{position:absolute;top:53%;left:26%;width:47%;height:5%;cursor:pointer;}
        </style>
    </head>
    <body id="schedule-netease-com" class="p-coupon">
        <@module>
        <div class="m-coupon">
            <div class="detail">
                <img src="/res/3g/images/activity/coupon_1.jpg" />
                <img src="/res/3g/images/activity/coupon_2.jpg" />
                <img src="/res/3g/images/activity/coupon_3.jpg" />
                <div class="shopping"></div>
            </div>
        </div>
        </@module>
	<@ga/>
	<script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/activity/coupon.js"></script>
    </body>
    </html>
    </@compress>
</#escape>