<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include '../../wrap/3g.common.ftl'>
    <html>
    <head>
        <title>填写订单</title>
        <@meta/>
        <!--@STYLE-->
        <@css/>
        <link rel='stylesheet' type='text/css' href='/src/css/page/profile.css'>
        <link rel='stylesheet' type='text/css' href='/src/css/page/buy.css?v=1.0.0.1'>
    </head>
    <body id='index-netease-com'>
        <@topbar title="填写订单">
        </@topbar>

    <div class="g-bd">
        <div class="m-tipbar">
            <i class="u-time"></i><span id="remain"></span>
        </div>
        <div>
            <div id="address"></div>
            <div id="address-none" class="m-addaddress"></div>
        </div>
        <div id="order-detail">
        </div>
    </div>
        <@template>

        <#-- Template Content Here -->
        <#-- Remove @template if no templates -->

        </@template>

        <#noescape>
        <script>
            var cartIds = '${cartIds}';
            var requestId = '${requestId}';
            var currTime = ${currTime!0};
            var cartEndTime = ${cartEndTime!0};
        </script>
        </#noescape>
	<@ga/>
	<!--@DEFINE-->
    <script src='${jslib}define.js?${jscnf}'></script>
    <script src='${jspro}page/buy/index.js'></script>

    <!--script src="http://10.240.140.216:8080/target/target-script-min.js"></script-->
    </body>
    </html>
    </@compress>
</#escape>