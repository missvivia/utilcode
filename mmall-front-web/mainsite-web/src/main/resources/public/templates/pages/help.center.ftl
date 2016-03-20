<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../wrap/common.ftl">
<html>
  <head>
    <@title content="帮助中心"/>
    <meta charset="utf-8"/>
    <@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/helpCenter.css">
  </head>
  <body id="index-netease-com">
  
    <@topbar/>
    <@sidebar/>
    <@navbar index=0/>
    <@module>
      <div class="m-crumbs">
       <a href="/index">首页</a><span>&gt;</span><span class="selected">帮助中心</span>
      </div>
      <div class="g-bd" id="m-helpcenter-box">
      </div>
    </@module>
    <@footer/>

    <#noparse>
    <div id='template-box' style='display:none;'>
      <!-- @TEMPLATE -->
      <textarea name="html" data-src="sidenav/module.html"></textarea>
      <textarea name="html" data-src="search/module.html"></textarea>
      <textarea name="html" data-src="newer/tab/module.html"></textarea>
      <textarea name="html" data-src="layout/module.html"></textarea>
      <textarea name="html" data-src="index/module.html"></textarea>
      <textarea name="html" data-src="articlelist/module.html"></textarea>
      <textarea name="html" data-src="resultlist/module.html"></textarea>
      <textarea name="html" data-src="newer/layout/module.html"></textarea>
      <textarea name="html" data-src="newer/login/module.html"></textarea>
      <textarea name="html" data-src="newer/purchase/module.html"></textarea>
      <textarea name="html" data-src="newer/return/module.html"></textarea>
      <textarea name="html" data-src="newer/signfor/module.html"></textarea>
      <textarea name="html" data-src="newer/snapup/module.html"></textarea>
      <!-- article -->
      <textarea name="html" data-src="article/a-1.htm"></textarea>
      <!-- /@TEMPLATE -->
    </div>
    </#noparse>
    
    <!-- @VERSION -->
    <script>location.config = {root:'/src/html/helpcenter/'};</script>
    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/helpcenter/helpcenter.js"></script>
  </body>
</html>
</@compress>
</#escape>