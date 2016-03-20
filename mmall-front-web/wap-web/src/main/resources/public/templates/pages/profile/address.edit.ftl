<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>个人中心页</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/profile.css">
  </head>
  <body id="index-netease-com">
  	<@topbar title="新增地址">
    </@topbar>
    <@module class="m-addaddress">
    	<div id="address" >
    	</div>
    </@module>
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <#noescape>
    <script>
    	<#if address??>
    		var useraddress = {
    			"id":${address.id},
    			"consigneeName":'${address.consigneeName}',
    			"consigneeMobile":'${address.consigneeMobile}',
    			"consigneeTel":'${address.consigneeTel}',
    			"consigneeTel":'${address.consigneeTel}',
    			"province":'${address.province}',
    			"city":'${address.city}',
    			"section":'${address.section}',
    			"street":'${address.street}',
    			"address":'${address.address}',
    			"isDefault":${(address.default)?string}
    		}
    	</#if>
    </script>
    </#noescape>
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/profile/address.edit.js"></script>
  </body>
</html>
</@compress>
</#escape>