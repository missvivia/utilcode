<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
        <#include '../../wrap/3g.common.ftl'>
    <html>
    <head>
        <title>填写订单</title>
        <@meta/>
        <@css/>
        <link rel='stylesheet' type='text/css' href='/src/css/page/profile.css'>
        <link rel='stylesheet' type='text/css' href='/src/css/page/buy.css?v=1.0.0.0'>
    </head>
    <body id='index-netease-com'>
        <@topbar title="填写订单" parentPage="/cartlist">
        </@topbar>

    <div class="g-bd">
        <div>
            <div id="address"></div>
            <div id="address-none" class="m-addaddress"></div>
        </div>
        <div id="order-detail">
        </div>
<#--        <form action="/purchase/buy" method="post" id="postForm">
		  <input type="hidden" name="cartIds" r-model={{cartIds}}/>
		  <input type="hidden" name="addressId" r-model={{orderdetail.currentAddress.id}}/>
		  <input type="hidden" name="payMethod" r-model={{orderdetail.currentPayMethodId||''}}/>
		  <input type="hidden" name="requestId" r-model={{requestId}}/>
		  <input type="hidden" name="skusPrice" r-model={{orderdetail.selectedPrice}}/>
		  <input type="hidden" name="fomrToken" r-model={{formToken}}/>
		  <input type="button" onclick="formSubmit()" value="Submit">
		</form>
    </div>
    -->
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
            var str = location.search.replace("?","");
            if(str.indexOf("&") > -1){
            	var list = str.split("&"),
            		obj = {};
            	for(var i = 0; i < list.length;i++){
            		var temp = list[i].split("=");
            		obj[temp[0]] = temp[1];
            	}
            }
            var formToken = obj.formToken;  
//            function formSubmit()
//			  {
//			  document.getElementById("postForm").submit()
//			  }
        </script>
        </#noescape>
	<@ga/>
    <script src='${jslib}define.js?${jscnf}'></script>
    <script src='${jspro}page/buy/index.js?v=1.0.0.0'></script>

    <!--script src="http://10.240.140.216:8080/target/target-script-min.js"></script-->
    </body>
    </html>
    </@compress>
</#escape>