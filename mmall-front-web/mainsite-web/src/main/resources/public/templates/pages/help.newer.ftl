<#escape x as x?html>
  <@compress>
  <!DOCTYPE html>
  <html>
  <head>
    <#include "../wrap/common.ftl">
      <@title content="帮助中心-新手指南"/>
    <meta charset="utf-8"/>
    <@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/helpNewer.css">
    <style>
      .m-help-newer .m-banner{height:420px;background:#e3d4f5 url(/res/images/help/banner.jpg) top center no-repeat;}
      .m-help-newer .m-nav{margin-top:56px;background:#f5f5f5;}
      .m-help-newer .tab{padding-left:160px;}
      .m-help-newer .item{line-height:30px;text-align:center;}
      .m-help-newer .item > a{display:block;padding-top:108px;width:154px;}
      .m-help-newer .item > a:hover{color:#7553a9;}
      .m-help-newer .item.sel > a{color:#7553a9;}
      .m-help-newer .icon-nav{height:116px;margin-bottom:-112px;}
      .m-help-newer .icon-nav-0{background:url(/res/images/help/nav-1.png) no-repeat;}
      .m-help-newer .icon-nav-1{background:url(/res/images/help/nav-2.png) no-repeat;}
      .m-help-newer .icon-nav-2{background:url(/res/images/help/nav-3.png) no-repeat;}
      .m-help-newer .icon-nav-3{background:url(/res/images/help/nav-4.png) no-repeat;}
      .m-help-newer .icon-nav-4{background:url(/res/images/help/nav-5.png) 0 -1px no-repeat;}
      .m-help-newer .part-1{height:481px;background:#f5f5f5 url(/res/images/help/1.jpg) top center no-repeat;}
      .m-help-newer .part-2{height:364px;background:#f0f0f0 url(/res/images/help/2.jpg) top center no-repeat;}
      .m-help-newer .part-3{height:365px;background:#f5f5f5 url(/res/images/help/3.jpg) top center no-repeat;}
      .m-help-newer .part-4{height:364px;background:#f0f0f0 url(/res/images/help/4.jpg) top center no-repeat;}
      .m-help-newer .part-5{height:410px;background:#f5f5f5 url(/res/images/help/5.jpg?v=1) top center no-repeat;}
      .m-help-newer .part-6{height:280px;}
      .m-help-newer .part-6 .btn{font-size:20px;width:350px;height:50px;line-height:50px;margin:144px 0 0 375px;}
    </style>
  </head>
  <body id="index-netease-com">

  <@topbar />
  <@navbar index=0/>
  <@module>
  <div class="m-help-newer">
    <div class="m-banner"></div>
    <div class="m-nav">
      <div class="g-bd j-spy">
        <div class="icon-nav" id="icon-nav"></div>
        <ul class="tab f-cb" id="tab-box">
          <li class="item f-fl active" data-target="section-1"><a href="#section-1">注册/登录</a></li>
          <li class="item f-fl" data-target="section-2"><a href="#section-2">抢购商品</a></li>
          <li class="item f-fl" data-target="section-3"><a href="#section-3">下单支付</a></li>
          <li class="item f-fl" data-target="section-4"><a href="#section-4">收货</a></li>
          <li class="item f-fl" data-target="section-5"><a href="#section-5">退货</a></li>
        </ul>
      </div>
    </div>
    <div class="part-1" id="section-1"></div>
    <div class="part-2" id="section-2"></div>
    <div class="part-3" id="section-3"></div>
    <div class="part-4" id="section-4"></div>
    <div class="part-5" id="section-5"></div>
    <div class="part-6">
      <div class="g-bd">
        <a class="u-btn btn" href="/">开启mmall之旅</a>
      </div>
    </div>
  </div>  
  </@module>
  <@footer/>
    <script>
      window.__data__ = {
        <#noescape>
         type: ${type},
         </#noescape>
      }
    </script>
    <#noparse>
      <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/helpcenter/helpnewer.js"></script>
  </body>
  </html>
</@compress>
</#escape>