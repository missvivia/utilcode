<!doctype html>
<#assign pageName="schedule-decorate"/>
<html lang="en">
  <head>
<#include "/wrap/common.ftl" />
    <meta charset="UTF-8">
    <title>${siteTitle} - ${page.title}</title>
    <#include "/wrap/css.ftl">
    <!-- @STYLE -->
    <link rel="stylesheet" href="/src/css/page/builder.css">
  </head>
  <body class="p-schedule">
  
    <#noparse>
    <div style="display:none;" id="template-box">
      <!-- @TEMPLATE -->
      <textarea name="html" data-src="frame/side/module.html"></textarea>
      <textarea name="html" data-src="frame/layout/module.html"></textarea>
      <textarea name="html" data-src="image/module.html"></textarea>
      <textarea name="html" data-src="widget/module.html"></textarea>
      <textarea name="html" data-src="product/module.html"></textarea>
      <textarea name="html" data-src="main/module.html"></textarea>
      <!-- /@TEMPLATE -->
    </div>
    </#noparse>
    
    <script>
      window.config = {
          layout:${stringify(layout!{})},
          images:${stringify(images![])},
          products:${stringify(products![])},
          imageCategory:${stringify(imgCategory![])}
      };
    </script>
    <!-- @VERSION -->
    <script>
    location.config = {root:'/src/html/builder/'};
    </script>
   
    <!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/schedule/decorate.js"></script>
  </body>
</html>