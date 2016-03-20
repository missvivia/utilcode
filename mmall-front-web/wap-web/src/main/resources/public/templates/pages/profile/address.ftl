<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>地址</title>
    <@meta/>
    <@less />
  </head>
  <body>
      <div class="wrap">
	    	 <header class="headerbox">
			     <div class="m-topnav">
			        <a class="curp go-back">
					    <i class="f-fl u-menu"></i>
				    </a>
				    <span class="tt">地址</span>
			  	 </div>
		    </header>
			<section class="address-box">
	        </section>
	        <footer class="addr-foot">
	             <div class="f-c">如需修改地址，请联系客服人员</div>
	        </footer>
     </div>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@jsFrame />
    <script>
         $(function(){
                // 获取地址信息
                function getAddress() {
                    $.ajax({
                        url : "/profile/address/list",
                        type: "POST",
                        cache: false,
                        contentType: "application/json",
                        data : JSON.stringify({
                           limit: "10",
                           offset: "0"
                        }),
                        success: function (data) {
                            if (data.code == 200) {
                                if (data.result.length > 0) {
                                    var list = data.result;
                                    createAddressList(list);
                                } else {
                                    $(".address-box").empty();
                                    $(".address-box").append("<div class='no-addr'>暂无地址</div>");
                                }
                            }
                        }
                    });
                }
                getAddress();

                // 创建地址列表
                var addressList = "";
                function createAddressList(list) {
                    for (var i = 0; i < list.length; i++) {
                        addressList +=
                            "<ul><li>"+list[i].province + list[i].city + list[i].section+"</li>"+
                                "<li><p class='n-mp'><span class='name'>"+ list[i].consigneeName + "</span><span>"+list[i].consigneeMobile + "</span>";
                        if (list[i].isDefault) {
                            addressList += "<i class='default'>默认</i></p>";
                        } else {
                            addressList += "</p>";
                        }
                        addressList += "<div class='addr-detail'>"+list[i].province + list[i].city + list[i].section + list[i].address+"</div></li></ul>";
                    }
                    $(".address-box").empty();
                    $(".address-box").append(addressList);
                }
                
         });
    </script>
  </body>
</html>
</@compress>
</#escape>