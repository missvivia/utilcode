<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include '../../wrap/3g.common.ftl'>
<html>
  <head>
    <title>频道页</title>
    <@meta/>
    <@css/>
    <link rel='stylesheet' type='text/css' href='/src/css/page/index.css'>
  </head>
  <body id='index-netease-com'>
    <@topbar title=title>
    </@topbar>
    <div class='g-hd g-index-hd'>
      <@touchslide promotionContent=promotionContent />
    </div>
    <@indexcnt/>
	<@footer/>
    <@template>

      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->

    </@template>
	<@ga/>
    <script src='${jslib}define.js?${jscnf}'></script>
    <script src='${jspro}page/index/subpage.js'></script>

    <!--script src="http://10.240.140.216:8080/target/target-script-min.js"></script-->
  </body>
</html>
</@compress>
</#escape>