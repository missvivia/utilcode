<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
  	<meta charset="utf-8"/>
    <@title content="验证码"/>
  	<@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/brandIntro.css">
    <style>
    	.j-error{
	      color:red;margin-left:10px;
        }
    </style>
</head>
<body>
	<@topbar/>
	<@sidebar/>
    <@navbar index=0/>
	<@module>
	<div class="g-bd">
	  <div class="m-vc-box">
	  	<div class="m-ct-box">
	  		<h1 class="info">商家详细信息及营业执照查询</h1>
	  		<h2 class="info2">根据国家工商总局《网络交易管理办法》要求对商家营业执照信息公示如下：</h2>
	  		<div class="m-rvc-box">
	  			<span class="f-ib">请输入图中验证码后查看</span>
	  			<input type="text" class="f-ff0"/>
	  			<img class="u-loading-1 f-ff1" data-src="/brand/genverifycode"/>
	  			<a class="f-ib f-ff2">看不清？换一张</a>
	  		</div>
	  		<div class="u-btn u-btn4">确定</div>
	  		<div class="f-ib j-error"></div>
	  	</div>
	  </div>
	 <form action="/brand/businessinfo" method="post" class="f-dn j-vcode">
	  <input name="id" class="j-vcode"/>
	  <input name="code" class="j-vcode"/>
	</form>
	</div>
	
	</@module>
	
    <@footer/>
	  <#if data??>
     <script type="text/javascript">
          <#noescape>
        window.verifydata = ${stringify(data)};
          </#noescape>
      </script>
    </#if>
	<#noparse>
        <!-- @SCRIPT -->
    </#noparse>
    
   
    
     <script src="${jslib}define.js?${jscnf}"></script>
     <script src="${jspro}page/brand/verifycode.js"></script>
</body>
</html>
</@compress>
</#escape>