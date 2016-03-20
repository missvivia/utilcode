<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include "../../wrap/3g.common.ftl">
    <html>
    <head>
        <title>${title!"客服帮助"}</title>
        <@meta/>
        <@css/>
        <link rel="stylesheet" type="text/css" href="/src/css/page/help.css">
    </head>
    <body id="schedule-netease-com" class="p-helpCenter">
        <@topbar title=title!"客服帮助"/>

        <@module>
        <div class="m-tel">
            拨打热线<a href="tel:4008666163">400-866-6163</a>
        </div>
        </@module>

        <@module>
        <div class="m-help">
            <ul class="list">
            <#--加载帮助菜单-->
            <#list navigation.result.list as l>
                <#--无二级菜单展示-->
                <#if !l.children??>
                    <li class="tip">
                        <a href=${"/help/article?categoryId="+l.id} class="cnt1" target="_blank">
                            <p>${l.name}</p>
                            <i class="icon1"></i>
                        </a>
                    </li>
                <#--有二级菜单展示-->
                <#else>
                    <li class="pack">
                        <a href="javascript:void(0);" class="cnt1">${l.name}<i class="icon1"></i></a>
                        <ul class="mList">
                        <#--加载二级菜单-->
                        <#list l.children as lc>
                            <li>
                                <a href=${"/help/article?categoryId="+lc.id} class="cnt2" target="_blank">
                                    <p>${lc.name}</p>
                                    <i class="icon2"></i>
                                </a>
                            </li>
                        </#list>
                        </ul>
                    </li>
                </#if>
            </#list>
            </ul>
        </div>
        </@module>
	<@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/help/help.js"></script>
    </body>
    </html>
    </@compress>
</#escape>