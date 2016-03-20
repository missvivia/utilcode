<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../wrap/3g.common.ftl">
<html>
  <head>
    <title>购物袋</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/cart.css">
  </head>
  <body id="index-netease-com">
  	<@topbar title="购物袋">
    </@topbar>
    <@module>
    <div id="cart-body"></div>
    </@module>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/cart/index.js"></script>
  </body>
</html>
</@compress>
</#escape>