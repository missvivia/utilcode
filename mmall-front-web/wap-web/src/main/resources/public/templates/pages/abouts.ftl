<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../wrap/3g.common.ftl">
<html>
  <head>
    <title>页面标题</title>
    <@meta/>
    <@css/>
    <style>
    .p-abouts .a-1{width:100%;}
    .p-abouts .a-2{width:100%;}
    .p-abouts .a-3{width:100%;}
    .p-abouts .a-4{width:100%;}
    img{vertical-align:top;}
    </style>
  </head>
  <body id="index-netease-com" class="p-abouts">
  	<@topbar>
    </@topbar>
      <div class="a-1"><img src="/res/3g/images/abouts/b-1.jpg" alt="" width="100%"></div>
      <div class="a-3"><img src="/res/3g/images/abouts/b-2.jpg" alt="" width="100%"></div>
      <div class="a-2"><img src="/res/3g/images/abouts/b-3.jpg" alt="" width="100%"></div>
      <div class="a-4"><img src="/res/3g/images/abouts/b-4.jpg" alt="" width="100%"></div>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/abouts.js"></script>
    
  </body>
</html>
</@compress>
</#escape>