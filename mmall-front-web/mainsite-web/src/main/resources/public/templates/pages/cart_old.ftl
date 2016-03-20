  <#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
    <html>
    <head>
      <#include "../wrap/common.ftl">
      <@title content="购物袋"/>
      <meta charset="utf-8"/>
      <@css/>
      <!-- @STYLE -->
      <link rel="stylesheet" type="text/css" href="/src/css/page/cart.css">
    </head>
    <body id="index-netease-com">

    <@topbar isTrade = true>
    <@tradeStep step =1/>
    </@topbar>
    <@module>

    <#-- Page Content Here -->
      <div class="g-bd p-wrap f-ff3 s-fc1" id="cart-body"></div>
    </@module>
    <@footer/>

      <#noparse>
        <!-- @SCRIPT -->
      </#noparse>
      <script src="${jslib}define.js?${jscnf}"></script>
      <script src="${jspro}page/cart/index.js"></script>
    </body>
    </html>
  </@compress>
  </#escape>