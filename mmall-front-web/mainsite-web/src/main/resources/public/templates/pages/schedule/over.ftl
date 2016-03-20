<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <@title content="档期已结束"/>
    <meta charset="utf-8"/>
    <script>
        window.config = {
            brandId:${(brand.id)!"0"}
        };
    </script>
    <@css/>
    <style type="text/css">
      .g-over{margin-top:30px;padding:135px 0 131px;text-align:center;font-size:14px;color:#333;background:#ffffff;}
      .g-over .brd{float:left;width:340px;margin-right:-340px;border-right:1px solid #f0f0f0;}
      .g-over .brd img{display:block;width:100%;height:60px;}
      .g-over .brd .shw{width:140px;margin:0 auto;}
      .g-over .brd .bnm{margin-top:35px;color:#999;}
      .g-over .brd .flw{display:block;height:28px;line-height:28px;width:78px;padding-left:18px;margin:10px auto 0;font-size:12px;color:#68449e;background:url(/res/images/icon2.png) no-repeat 0 -685px;}
      .g-over .brd .flw:hover{background-position:0 -725px;color:#9f32b7;}
      .g-over .brd .j-follow{background-position:0 -759px;}
      .g-over .brd .j-follow:hover{background-position:0 -791px;}
      .g-over .msg{margin-left:340px;padding-top:5px;}
      .g-over .msg img{display:block;width:70px;height:58px;margin:0 auto;}
      .g-over .msg .ln0{margin-top:18px;font-size:24px;}
      .g-over .msg .ln1 a{color:#5893cd;margin:0 3px;}
    </style>
  </head>
  <body id="schedule-netease-com">
    
    <@topbar/>
    <@sidebar/>
    <@navbar/>
    <@module>
      <div class="g-over g-bd f-ff3 f-cb">
        <div class="brd">
          <div class="shw">
            <img src="${(brand.logo)!"/res/images/blank.gif"}"/>
            <div class="bnm">${(brand.brandNameZh)!"品牌名称"}</div>
            <#if followed!false>
            <a href="#" class="flw j-follow" id="brand-follow-box">取消关注</a>
            <#else>
            <a href="#" class="flw" id="brand-follow-box">关注品牌</a>
            </#if>
          </div>
        </div>
        <div class="msg">
          <img src="/res/images/over.png"/>
          <p class="ln0">您来晚了一步，抢购活动已经结束了</p>
          <p class="ln1">去<a href="/">首页</a>看看最新抢购吧</p>
        </div>
      </div>
    </@module>
    <@footer/>

    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/schedule/over.js"></script>
  </body>
</html>
</@compress>
