<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include '../../wrap/3g.common.ftl'>
<html>
  <head>
    <title>物流详情页</title>
    <@meta/>
    <@css/>
    <link rel='stylesheet' type='text/css' href='/src/css/page/express.css'>
  </head>
  <body id='index-netease-com'>
    <@topbar>
    </@topbar>
    <div class='g-bd g-index-bd'>
      <div id="package-box">
      </div>
    </div>

    <@template>

      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->

    </@template>
	<@ga/>
    <script src='${jslib}define.js?${jscnf}'></script>
    <script src='${jspro}page/order/express.js'></script>

    <!--script src="http://10.240.140.216:8080/target/target-script-min.js"></script-->
  </body>
</html>
</@compress>
</#escape>