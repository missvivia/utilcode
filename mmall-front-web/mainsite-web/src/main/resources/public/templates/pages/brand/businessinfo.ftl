<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
  	<meta charset="utf-8"/>
    <@title content="品牌故事"/>
  	<@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/brandIntro.css">
</head>
<body>
	<@topbar/>
	<@sidebar/>
    <@navbar index=0/>
	<@module>
	<div class="g-bd">
	  <div class="m-binfo-box">
	  	<h1 class="info">商家详细信息及营业执照查询</h1>
	  	<h2 class="info2">根据国家工商总局《网络交易管理办法》要求对商家营业执照信息公示如下：</h2>
		<div class="m-merchant2">
			<div class="item">
				<div class="label">公司名称 </div>
				<div class="concrete"><#if data??>${data.businessInfo.companyName}</#if></div>
			</div>
			<div class="item">
				<div class="label">法人姓名 </div>
				<div class="concrete"><#if data??>${data.businessInfo.legalPerson}</#if></div>
			</div>
			<div class="item">
				<div class="label">联系地址 </div>
				<div class="concrete"><#if data??>${data.businessInfo.contactAddress}</#if></div>
			</div>
			<div class="item">
				<div class="label">营业执照编号</div>
				<div class="concrete"><#if data??>${data.businessInfo.registrationNumber}</#if></div>
			</div>
			<div class="item">
				<div class="label">营业执照有效期 </div>
				<div class="concrete">
				<#if data.businessInfo.registrationNumberAvaliable ==1>长期有效<#else>
				<#assign da1=data.businessInfo.registrationNumberStart?number_to_date />
				<#assign da2=data.businessInfo.registrationNumberEnd?number_to_date/>
				${da1?iso_utc}至${da2?iso_utc}
				</#if>
				</div>
			</div>
		</div>
		<div class="m-img-box">
			<img src="${data.businessInfo.registrationImg}?imageView&quality=95&thumbnail=600x0" class="m-img m-img1"></div>
			<img src="${data.businessInfo.brandAuthImg}?imageView&quality=95&thumbnail=600x0" class="m-img m-img2"></div>
		</div>
	  </div>
	</div>
	</@module>
    <@footer/>

	<#noparse>
        <!-- @SCRIPT -->
    </#noparse>
     <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/brand/businessinfo.js"></script>
</body>
</html>
</@compress>
</#escape>