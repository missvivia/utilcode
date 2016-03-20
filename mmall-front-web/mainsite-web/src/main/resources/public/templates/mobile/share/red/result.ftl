<#include "../../mobile.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/mobile/css/mobileRed.css" rel="stylesheet" />
		<title>抢红包-新品购</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body >
		<noscript></noscript>
		<#--Open App-->
		<@openApp/>
		<#--container-->
		<div class="g-container">
			<#--红包内容-->
			<div class="g-hongbao">
				<#--未抢到红包或错误-->
				<#if !amount?? || amount == 0>
					<p class="m-hongbao-title">
						您来晚了，
					</p>
					<div class="m-hongbao-content">
						<span>红包被抢光了~</span>
					</div>
				<#--显示红包金额-->
				<#else>
					<#if receive == 1>
                        <div class="m-hongbao-content">
                            <span>您已抢到</span><span class="hongbao-gift">${amount}</span><span>元!</span>
                        </div>
						<#--使用红包-->
                        <a href="javascript:;" class="m-goshop"><img src="/mobile/images/gobtn.png" width="143" /></a>
					<#else>
                        <p class="m-hongbao-title">
                            手气不错~
                        </p>
                        <div class="m-hongbao-content">
                            <span>抢到</span><span class="hongbao-gift">${amount}</span><span>元红包!</span>
                        </div>
						<#--使用红包-->
                        <a href="javascript:;" class="m-goshop"><img src="/mobile/images/gobtn.png" width="143" /></a>
				 	</#if>
				</#if>
			</div>
		</div>
		<#--@script-->
		<script src="/mobile/javascript/lib/nej/src/define.js?pro=/mobile/javascript"></script>
		<script src="/mobile/javascript/page/share/red/result.js"></script>
	</body>
</html>