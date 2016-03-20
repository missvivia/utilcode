<#include "app.common.ftl">

<!DOCTYPE html>
<html>
    <head>
        <#--meta标签-->
        <@headMeta/>
        <#--@CSS-->
        <link type="text/css" href="/src/css/page/webview/app_base.css" rel="stylesheet"/>
        <link type="text/css" href="/src/css/page/webview/app_logistics2.css" rel="stylesheet"/>
        <title>物流追踪</title>
        <!--[if lt IE 8]>      <![endif]-->
    </head>
    <body>
        <#--container-->
        <div class="g-container">
			<@topbar></@topbar>
            <ul class="g-package-list">
            <#if !orderTrace?? || orderTrace?size == 0 >
                <div class="g-noinfo">
                                                                       还没有物流信息呢
                </div>
            <#else>
                <#list orderTrace as t>
                    <li class="m-package-dropdown">
                        <#--一级菜单名称-->
                        <a href="javascript:;" class="m-package-tip1">包裹${t_index + 1}<i class="m-package-icon1"></i></a>
                         <#--二级菜单-->
                        <div class="g-package-detail">
                            <#--快递信息-->
                            <#if t.ExpressCompany == '' || t.MailNO == '' >
                                <div class="g-noinfo">
                                                                                                                             还没有物流信息哦~
                                </div>
                            <#else>
                                <div class="g-info">
                                    <p>快递公司<span>${(t.ExpressCompany)!}</span></p>

                                    <p>运单号<span>${(t.MailNO)!}</span></p>
                                </div>
                                <#--快递轨迹-->
                                <div class="g-order">
                                    <div class="g-order-flow">
                                    	<ul class="g-order-storey">
	                                    	<#if !t.OrderTrace?? || t.OrderTrace?size == 0>
			                                     <li style="margin-left:-25px;background:#fff;">
				                                    <div class="g-noinfo">
					                                                                                                                             暂无物流信息
					                                </div>
					                            </li>
			                            	<#elseif t.OrderTrace?size == 1>
	                                        	<li class="m-order-detail">
	                                            	<#--标记-->
	                                                <span class="m-order-icon"></span>
	                                                <#--信息：有当前操作人员信息-->
	                                                <#if t.OrderTrace[0].operater?? &&  t.OrderTrace[0].operater!=''>
	                                                    <span>${t.OrderTrace[0].operateOrg}&emsp;${t.OrderTrace[0].operateDesc!}&emsp;当前操作人员:${t.OrderTrace[0].operater}&emsp;<a
	                                                            href=${"tel:"+(t.OrderTrace[0].operaterPhone)!}>${(t.OrderTrace[0].operaterPhone)!}</a>&emsp;${t.OrderTrace[0].note}</span>
	                                                <#--信息：无当前操作人员信息-->
	                                                <#else>
	                                                    <span>${t.OrderTrace[0].operateOrg!}&emsp;${t.OrderTrace[0].operateDesc!}&emsp;${t.OrderTrace[0].note!}</span>
	                                                </#if>
	                                                <#--时间-->
	                                                <span class="m-order-time">${t.OrderTrace[0].time}</span>
	                                            </li>
	                                        <#else>
	                                        	<#list t.OrderTrace as tc>
	                                                <li class="m-order-details">
	                                                    <span class="m-order-icon"></span>
	                                                    <#--信息：有当前操作人员信息-->
	                                                    <#if tc.operater?? && tc.operater!='' >
	                                                        <span>${tc.operateOrg}&emsp;${tc.operateDesc!}&emsp;当前操作人员:${tc.operater}&emsp;<a
	                                                                href=${"tel:"+(tc.operaterPhone)!}>${(tc.operaterPhone)!}</a>&emsp;${tc.note}</span>
	                                                    <#--信息：无当前操作人员信息-->
	                                                    <#else>
	                                                        <span>${tc.operateOrg!}&emsp;${tc.operateDesc!}&emsp;${tc.note!}</span>
	                                                    </#if>
	                                                    <#--时间-->
	                                                    <span class="m-order-time">${tc.time}</span>
	                                                </li>
	                                            </#list>
	                                   		</#if>
                                    	</ul>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </li>
                </#list>
            </#if>
            </ul>
        </div>
        <script src="${jslib}define.js?${jscnf}"></script>
        <script src="${jspro}page/webview/logistics.js"></script>
    </body>
</html>