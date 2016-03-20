<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
    <meta charset="utf-8"/>
      <@title content="新云联百万店-不支持跨区购买"/>
    <@css/>
    <style>
     .g-r504{padding-bottom:5px;background:#e3d4f5 url(/res/images/504-repeat.png) repeat-x left bottom;}
     .g-r504 img{display:block;width:100%;}
     .g-r504 .g-bd{position:relative;padding-top:60px;}
     .g-r504 .tip{position:absolute;top:54px;left:54px;width:100%;height:100px;font-size:18px;color:#9144ae;text-align:center;}
     .g-r504 .tip a{padding:5px 50px;color:#fff;background:#9144ae;}
     .g-r504 .tip .ln1{margin-top:19px;}
     
     .p-r504 .g-more{padding-top:15px;background:transparent;}
     .p-r504 .g-ft{margin-top:0;padding-top:56px;}
     .p-r504 .m-copyright{padding:0 0 150px;}
    </style>
</head>
<body class="p-r504">
    <@fixedTop />
    <@topbar />
    <@top />
    <@module>
      <div class="g-r504">
        <div class="g-bd">
          <img src="/res/images/504.png"/>
          <div class="tip f-ff3">
            <p class="ln0">你所在地区暂不能购买该页商品。去看看其他新抢购吧~</p>
            <p class="ln1"><a href="/">去逛逛</a></p>
          </div>
        </div>
      </div>
    </@module>
    <div class="g-more"><div class="m-moreslide">&nbsp;</div></div>
    <@footer/>
    <@copyright/>
    <@cityChange />
    <div class="popup"></div>
    <@js />
</body>
</html>
</@compress>
</#escape>