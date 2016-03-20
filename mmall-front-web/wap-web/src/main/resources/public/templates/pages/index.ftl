<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../wrap/3g.common.ftl">
<html>
  <head>
    <title>页面标题</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/index.css">
  </head>
  <body id="index-netease-com">
  	<@topbar>
    </@topbar>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    
  </body>
</html>
</@compress>
</#escape>