<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
  	<meta charset="utf-8"/>
    <@title content="新云联百万店-关于我们"/>
  	<@css/>
    <!-- @STYLE -->
	<style>
    .p-abouts .a-1{height:587px;background: url(/res/images/page/aboutus/b-1.jpg) top center no-repeat;}
    .p-abouts .a-2{height:386px;background: url(/res/images/page/aboutus/b-2.jpg) top center no-repeat;}
    .p-abouts .a-3{height:449px;background: url(/res/images/page/aboutus/b-3.jpg) top center no-repeat;}
    .p-abouts .a-4{height:401px;background: url(/res/images/page/aboutus/b-4.jpg) top center no-repeat;}
	</style>
</head>
<body class="p-abouts">
	<@topbar />
  <@sidebar/>
    <@navbar index=0/>
	<@module>
        <div class="a-1"></div>
        <div class="a-2"></div>
        <div class="a-3"></div>
        <div class="a-4"></div>
	</@module>
    <@footer/>

	<#noparse>
        <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/aboutusNew.js"></script>
</body>
</html>
</@compress>
</#escape>