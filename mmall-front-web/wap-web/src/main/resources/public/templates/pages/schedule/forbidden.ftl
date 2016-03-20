<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>${pageTitle!"品购页"}</title>
    <@meta/>
    <@css/>
    <style>
      .p-over{background:#f2f2f2;}
      .m-over{text-align:center;}
      .m-over .link{padding:8px 20px;background:#fff;border-bottom:1px solid #d6d6d6;}
      .m-over .logo{float:left;width:40px;height:40px;border-radius:8px;background:#68449e;}
      .m-over .open{float:right;width:70px;padding-top:6px;}
      .m-over .open a{display:block;height:28px;line-height:26px;color:#68449e;border:1px solid #a37fd8;border-radius:3px;}
      .m-over .desc{padding-left:8px;overflow:hidden;text-align:left;}
      .m-over .desc .l1{line-height:22px;font-size:18px;color:#333;}
      .m-over .desc .l2{font-size:12px;color:#666;}
      .m-over .fbd{display:block;line-height:24px;margin:80px 50px;padding:35px 8px;font-size:15px;background:#3d383d;color:#cccccc;border-radius:4px;}
    </style>
  </head>
  <body id="schedule-netease-com" class="p-over">
    <@topbar title=pageTitle!"品购页"/>
    
    <@module>
      <div class="m-over">
<#--
        <div class="link f-cb">
          <div class="logo">&nbsp;</div>
          <div class="open"><a href="#">打开</a></div>
          <div class="desc">
            <p class="l1">新品购</p>
            <p class="l2">专注当季新品抢购</p>
          </div>
        </div>
-->
        <a class="fbd" href="/">
          <p class="l1">该地区暂不支持查看此闪购</p>
          <p class="l2">去首页看看</p>
        </a>
      </div>
    </@module>
    <@ga/>
    
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/schedule/forbidden.js"></script>
  </body>
</html>
</@compress>
</#escape>