<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "../../wrap/3g.common.ftl">
    <title>预告</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/trailer.css">
  </head>
  <body id="trailer-netease-com" class="p-trailer">
    <@topbar title="预告"/>
    <@module>
      <div class="m-trailer" id="trailer-box">
        <#assign hasSchedule = false/>
        <#assign dayText = ["明天","后天","",""]/>
        <#list (preSchedule!{})?keys as key>
          <#assign poList = (preSchedule[key])![]/>
          <#if poList?size&gt;0>
            <#assign hasSchedule = true/>
            <div class="m-block">
              <div class="m-time j-timer">
                <span class="x dot">&nbsp;</span>
                <span class="x txt">
                  <i class="m-tlbg ak">&nbsp;</i>
                  <#assign day = dayText[key_index]/>
                  <time><#if day!="">${day}<#else>${key?substring(5)?replace("-","月")}日</#if></time>
                  <time>10点开始</time>
                </span>
              </div>
              <div class="m-list">
                <#list poList as x>
                <div class="m-it f-cb">
                  <a class="log" href="/mainbrand/story?id=${x.brandId}">
                    <div class="m-logo">
                      <img src="${x.brandLogoSmall}?imageView&quality=100&thumbnail=165x70"/>
                    </div>
                    <p class="zk">${x.minDiscount/10}折起</p>
                  </a>
                  <a class="img" href="/schedule?scheduleId=${x.id}">
                    <div class="m-img m-bnr">
                      <img class="u-loading-1" src="/res/3g/images/blank.gif" data-src="${x.banner.homeBannerImgUrl}"/>
                      <div class="msk"><p>${x.title}</p></div>
                    </div>
                  </a>
                </div>
                </#list>
              </div>
            </div>
          </#if>
        </#list>
        <#if !hasSchedule>
          <@coming/>
        </#if>
      </div>
    </@module>
	<@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/schedule/trailer.js"></script>
  </body>
</html>
</@compress>
</#escape>