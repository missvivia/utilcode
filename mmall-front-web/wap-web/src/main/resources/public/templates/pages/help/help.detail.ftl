<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include "../../wrap/3g.common.ftl">
    <html>
    <head>
        <title>${title!"客服帮助"}</title>
        <@meta/>
        <@css/>
        <style>
            .p-helpDetail{background:#f2f2f2;}
            .m-detail{position:relative;}
            .m-detail .detail .cnt{margin-left:17px;padding:22px 17px 22px 0;border-bottom:1px solid #dedede;}
            .m-detail .detail .til{font-size:18px;color:#333;}
            .m-detail .detail .del{margin-top:10px;font-size:16px;line-height:20px;color:#999;word-break:break-all;}
            .m-detail .detail .del img{display:block;max-width:100%;}
            .m-detail .noAns{margin-top:10px;text-align:center;}
        </style>
    </head>
    <body id="schedule-netease-com" class="p-helpDetail">
        <@topbar title=title!"客服帮助"/>

        <@module>
        <div class="m-detail">
            <#if detail.result??>
                <#list detail.result.list as l>
                    <div class="detail">
                        <div class="cnt">
                            <div class="til">
                            ${l.title}
                            </div>
                            <div class="del">
                            <#noescape>${l.content?replace('&gt;','>')?replace('&lt;','<')}</#noescape>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="noAns">
                    暂无数据
                </div>
            </#if>
        </div>
        </@module>
	<@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/help/page.js"></script>
    </body>
    </html>
    </@compress>
</#escape>