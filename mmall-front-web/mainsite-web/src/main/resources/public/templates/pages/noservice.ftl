<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../wrap/common.ftl">
    <#include "../wrap/front.ftl">
    <@title type="404"/>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" href="/favicon.ico?v=2" />
    <@css/>
</head>
<body>
	<@fixedTop />
	<@topbar />
	<@top />
    <div class="wrap clearfix top rel">
         <div style="text-align:center;">
             <img src="/src/images/noservice-logo.png"/>
         </div>
    </div>
    <div style='background: #8ee3fb;height:580px;margin-bottom:165px;'>
        <div class="wrap nocart" style="padding:180px 0px 60px 0px;">
          <div>
            <img src="/src/images/noservice-bg.png"/>
          </div>
          <div class="btns">
                <a href="javascript:history.back();" class="bg-red">返&nbsp;&nbsp;回</a>
          </div>
        </div>
    </div>
    <@footer/>
	<@copyright/>
	<@cityChange />
	<div class="popup"></div>
	<@js />
</body>
</html>
</@compress>
</#escape>