  <#escape x as x?html>
    <@compress >
    <!DOCTYPE html>
    <html>
    <head>
      <#include "/wrap/common.ftl">
      <@title content="品牌入驻页"/>
      <meta charset="utf-8"/>
      <@css/>
      <!-- @STYLE -->
      <link rel="stylesheet" type="text/css" href="/src/css/layer.css">
      <link rel="stylesheet" type="text/css" href="/src/css/page/brandSettledDown.css">
    </head>
    <body id="index-netease-com">

    <@topbar/>
    <@sidebar/>
    <@navbar index=0/>
    <@module>
      <div class="m-crumbs">
        <a href="/index">首页</a><span>&gt;</span><span class="selected">入驻品牌</span>
      </div>
      <div class="g-bd" id="g-bd-box">

      </div>
    </@module>
    <@footer/>

      <#noparse>
        <!-- @SCRIPT -->
      </#noparse>
      <script src="${jslib}define.js?${jscnf}"></script>
      <script src="${jspro}page/brand/settle/index.js"></script>
    </body>
    </html>
  </@compress>
  </#escape>