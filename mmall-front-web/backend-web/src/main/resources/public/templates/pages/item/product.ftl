<#assign pageName="item-product">

<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
</head>

<body>

<!-- 左边目录列表 -->
<@side />

<script>
<!-- item/product 服务器接口没有下发分类信息，跳转到 /item/product/list请求-->
window.onload = function(){
	location.href = "/item/product/list?type=1";
};
</script>

<script src="${jslib}define.js?pro=${jspro}"></script>
</body>
</html>