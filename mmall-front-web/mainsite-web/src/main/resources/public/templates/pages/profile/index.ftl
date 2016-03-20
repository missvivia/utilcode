<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="myIndex" />
    <@css/>
    <link href="/src/css/base.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/member.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
    <@fixedTop />
    <@topbar />
    <@top />
    <@mainCategory />
    <div class="bg-french-gray clearfix">
	    <@crumbs>
	     <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">我的百万店</span>
	    </@crumbs>
      <@myModule sideIndex=0>
      <div class="m-box m-main l">
          <div class="my-info">
          <!--<div class="m-welcome">
              <#if profile.nickname??>
                <#assign nickName = profile.nickname/>
              <#else>
                <#assign nickName = profile.account.split('@')[0]/>
              </#if>
              <p class="itm name">${nickName}<span class="wlc">欢迎您！</span></p>
            </div>-->
            <div id="index"></div>
          </div>
      </div>
      </@myModule>
    </div>
    <@footer />
	<@copyright />
	<@cityChange />
    <div class="popup"></div>
    <@js />
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/profile/index.js"></script>
  </body>
</html>
</@compress>
</#escape>