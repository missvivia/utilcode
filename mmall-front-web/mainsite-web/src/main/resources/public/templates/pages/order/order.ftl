<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="myOrder" />
    <@css/>
    <link href="/src/css/base.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/member.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
    <@topbar />
    <@top />
    <@mainCategory />
    <div class="bg-french-gray clearfix">
	    <@crumbs>
	    	 <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">我的订单</span>
	    </@crumbs>
      <@myModule sideIndex=0>
        <div class="m-box m-main l" id="m-main">
            <div class="m-focus-1">
                 <div id="order-body"></div>
            </div>
        </div>
    </@myModule>
    </div>
    <@footer/>
    <@cityChange />
    <div class="popup"></div>
    <div class="buy-again-popup"></div>
    <@js />
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/order/index.js?20151116"></script>
  </body>
</html>
</@compress>
</#escape>