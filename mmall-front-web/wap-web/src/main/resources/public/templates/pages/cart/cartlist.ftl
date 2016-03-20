<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>进货单</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/cart.css?v=1.0.0.1">
  </head>
  <body id="index-netease-com">
  	<@topbar title="进货单" parentPage="/index">
    </@topbar>
    <@module>
	<div class="cart-loading">
	  <img src="/src/img/icon/loading.gif"/>
	</div>
    <div id="cart-body"></div>
    </@module>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/cart/index.js?v=1.0.0.2"></script>
  </body>
</html>
</@compress>
</#escape>