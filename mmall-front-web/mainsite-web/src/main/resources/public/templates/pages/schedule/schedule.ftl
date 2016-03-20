<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#assign nowTime = .now?long>
    <#assign previewed = (layout.preview)!false>
    <#assign showPrice = previewed||nowTime&gt;=startTime>
    
    <#include "/wrap/common.ftl">
    <#include "./macro.ftl">
    <@title type="schedule" content="${pageTitle!'品购页'}"/>
    <meta charset="utf-8"/>
    <script>
        window.config = {
            id:${layout.id},
            leftTime:${(endTime!0)-nowTime},
            brandId:${layout.brandId},
            scheduleId:${layout.scheduleId},
            showPriced:${showPrice?string("true","false")}
        };
    </script>
    <@css/>
    <!-- @STYLE -->
    <link href="/src/css/page/schedule.css" rel="stylesheet" type="text/css">
    <@layoutStyle layout=layout/>
  </head>
  <body class="p-schedule" id="schedule-netease-com">
    <#if previewed><@previewBar/></#if>

    <@topbar/>
    <@sidebar/>
    <@navbar/>

    <@module>
      <div class="g-schedule">
        <div class="g-bd" id="page-widget-box">

          <#-- banner -->
          <div class="banner">
            <div class="timer m-maxk">
              <#-- count down -->
              <div class="bg xmsk">&nbsp;</div>
              
              <div class="fw">
              <#if previewed>
                <span class="u-iconlove">&#xe607;</span>
                <span>关注品牌</span>
              <#elseif followed!false>
                <span class="u-iconlove j-follow" id="brand-follow-box">&#xe606;</span>
                <span>取消关注</span>
              <#else>
                <span class="u-iconlove" id="brand-follow-box">&#xe607;</span>
                <span>关注品牌</span>
              </#if>
              </div>
              
              <#if previewed>
              <div class="cd xcnt" id="count-down-box">剩余：00天00时00分00秒</div>
              <#elseif nowTime&lt;startTime>
              <div class="cd xcnt">将于${startTime?number_to_datetime?string("MM月dd日hh点")}开始</div>
              <#elseif nowTime&lt;endTime>
              <div class="cd xcnt" id="count-down-box">剩余：00天00时00分00秒</div>
              <#else>
              <div class="cd xcnt">档期已结束</div>
              </#if>
            </div>
          </div>

          <#-- custom widget list -->
          <#assign xlist = (layout.udSetting)![]>
          <#list xlist as x>
            <#switch x.type?number>
              <#case 1> <@widgetTextHTML config=x/>      <#break>
              <#case 2> <@widgetBannerHTML config=x/>    <#break>
              <#case 3> <@widgetProduct2HTML config=x showPrice=showPrice/>  <#break>
              <#case 4> <@widgetProduct31HTML config=x showPrice=showPrice/> <#break>
              <#case 5> <@widgetProduct32HTML config=x showPrice=showPrice/> <#break>
              <#case 6> <@widgetProduct4HTML config=x showPrice=showPrice/>  <#break>
            </#switch>
          </#list>

          <#-- product list -->
          <#if layout.allListPartVisiable>
          <div class="pdlist m-maxk">
            <div class="bg xmsk">&nbsp;</div>
            <div class="bx xcnt" id="product-list-box"></div>
          </div>
          </#if>

          <#-- shop map -->
          <#assign list = shops![]/>
          <#assign visible = layout.mapPartVisiable&&list?size&gt;0/>
          <#if visible>
          <script>window.shops = ${stringify(list)};</script>
          <div class="shpmap" id="shop-map-box"></div>
          </#if>

        </div>
      </div>
    </@module>

    <@footer/>

    <#if visible><@baiduMap/></#if>
    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <#if paopao??>
     <script>window.paopao = ${stringify(paopao)};</script>
    </#if>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/schedule/schedule.js"></script>
  </body>
</html>
</@compress>
