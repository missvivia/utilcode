<#include "mobile.common.ftl">

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@webViewMeta/>
		<#--@CSS-->
		<link type="text/css" href="/mobile/css/mobileBase.css" rel="stylesheet" />
		<link type="text/css" href="/mobile/css/mobileProduct.css" rel="stylesheet" />
		<title>${(data.product.productName)!} — 新品惠</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<div class="g-container">
			<#--售后说明-->
			<div class="g-part-param">
				<div class="g-part-name">
					商品参数
				</div>
                <div class="m-param">
                    <span class="key">商品名称</span>${data.product.productName!}&emsp;
                </div>
                <div class="m-param">
                    <span class="key">商品货号</span>${data.product.goodsNo!}&emsp;
                </div>
				<div class="m-param">
					<span class="key">专柜同款</span><#if data.product.sameAsShop?? && data.product.sameAsShop==1>是<#else>否</#if>
				</div>
				<#--由于UI排版需要  此页不使用list加载页面-->
				<#--长-->
				<#if data.product.lenth?has_content>
				<div class="m-param">
					<span class="key">长</span>${data.product.lenth}cm&emsp;
				</div>
				</#if>
				<#--宽-->
				<#if data.product.width?has_content>
				<div class="m-param">
					<span class="key">宽</span>${data.product.width}cm&emsp;
				</div>
				</#if>
				<#--高-->
				<#if data.product.height?has_content>
                <div class="m-param">
					<span class="key">高</span>${data.product.height}cm&emsp;
                </div>
				</#if>
				<#--风格-->
				<#if data.product.height?has_content>
				<div class="m-param">
					<span class="key">风格</span>${data.product.height}&emsp;
				</div>
				</#if>
				<#--产地-->
				<#if data.product.producing?has_content>
				<div class="m-param">
					<span class="key">产地</span>${data.product.producing}&emsp;
				</div>
				</#if>
				<#--重量-->
				<#if data.product.weight?has_content>
				<div class="m-param">
					<span class="key">重量</span>${data.product.weight}kg&emsp;
				</div>
				</#if>
				<#--商家编辑属性-->
				<#list data.product.productDetail as param>
					<#if param.name?length < 5 >
                        <div class="m-param">
                            <span class="key">${param.name!}</span>${param.value!}&emsp;
                        </div>
					<#else>
                        <div>${param.name!}：${param.value!}&emsp;</div>
					</#if>
				</#list>
				<#--配件-->
				<#if data.product.accessory?has_content>
				<div class="m-param">
					<span class="key">配件</span>${data.product.accessory}&emsp;
				</div>
				</#if>
				<#--洗涤说明-->
				<#if data.product.careLabel?has_content>
				<div class="m-param">
					<span class="key">洗涤说明</span>${data.product.careLabel}&emsp;
				</div>
				</#if>
				<#--售后说明-->
				<#if data.product.afterMarket?has_content>
				<div class="m-param">
					<span class="key">售后说明</span>${data.product.afterMarket}&emsp;
				</div>
				</#if>
				<#--商品特点-->
				<#if data.product.productDescp?has_content>
				<div class="m-param">
					<span class="key">商品特点</span>${data.product.productDescp}&emsp;
				</div>
				</#if>
			</div>
			<#--尺码助手-->
			<#if data.product.productSize?has_content>
			<div class="g-part-size">
				<div class="g-part-name">
					商品尺码
				</div>
				<#--尺码对照表-->
				<div class="m-table-name">尺码对照表</div>
				<table class="g-size-table">
					<#--表头-->
					<tr>
						<#list data.product.productSize.header as hd>
						<th>${hd.name!}${hd.unit!}</th>
						</#list>
					</tr>
					<#--对应尺码-->
					<#list data.product.productSize.body as bd>
					<tr>
						<#list bd as item>
						<td>${item!}</td>
						</#list>
					</tr>
					</#list>
					<#if data.product.productSize.tip??>
					<tr>
						<td colspan=${(data.product.productSize.header)?size} class="m-size-tip">
							<span>温馨提示：</span>${data.product.productSize.tip}
						</td>
					</tr>
					</#if>
				</table>
				<#--尺码助手-->
				<#if data.product.helper??>
		            <div class="m-table-name">
		            	尺码助手
		            </div>
		            <table class="g-helper-table">
					<#--表头-->
					<tr>
						<#list data.product.helper.vaxis.list as hd>
						<#if hd_index == 0>
						<th class="m-helper-head">
							<div class="m-helper-corner"></div>
							<span class="m-helper-info1">身高cm</span>
							<span class="m-helper-info2">体重kg</span>
						</th>
						</#if>
						<th>${hd}</th>
						</#list>
					</tr>
					<#list data.product.helper.body as bd>
					<#if isEmpty(bd) == 0>
					<tr>
						<td>${data.product.helper.haxis.list[bd_index]}</td>
						<#list bd as item>
						<td>${item!}</td>
						</#list>
					</tr>
					</#if>
					</#list>
				</table>
	            </#if>
			</div>
			</#if>
			<#--详情图片-->
			<div class="g-part-img">
				<div class="g-part-name">
					商品图片
				</div>
				<#--商家自编辑部分-->
				<div class="g-img-wrap">
				<#list data.product.prodShowPicList as pic>
                    <img src="${(pic)!""}?imageView&quality=95&thumbnail=640y640" />
				</#list>
				<#if data.product.customEditHTML?has_content>
					${data.product.customEditHTML?replace('src="(.*?)"', 'src="$1?imageView&thumbnail=640x0&quality=95"', 'rim')}
				</#if>
				</div>
			</div>

			<div class="g-part-rmd">
				<div class="g-part-name">
					商品咨询
				</div>
				<dl>
					购买前如有问题，请向mmall客服咨询。
					<a href="tel:4008666163">联系客服</a>
				</dl>
				<dl>
					<dt>商品都是正品吗？</dt>
					<dd>请你放心，mmall（023.baiwandian.cn）上所售卖的商品均经过品牌授权，确保正品，并由中华保险（CIC）为你购买的每一件商品进行承保。</dd>
				</dl>
				<dl>
					<dt>衣服图片上搭配的腰带、项链等配件，会连商品一同送货吗？</dt>
					<dd>您可参看商品详情提供的商品参数信息。如非在配件信息内特别说明，服装类商品图片中的腰带、饰品等配件均为拍摄搭配之用，是不包含在所售商品中的。</dd>
				</dl>
				<dl>
					<dt>尺码表上的尺码标准吗？</dt>
					<dd>mmall所售商品尺寸均为人工测量，可能会存在1-2cm的正常误差范围。</dd>
				</dl>
				<dl>
					<dt>图片颜色和实物颜色是否相同？</dt>
					<dd>mmall展示的商品图片是由mmall专业拍摄或采用品牌供应商提供的统一图片，力求将最真实的信息传达至你的视线。但由于个人显示器不同，可能导致实物与图片存在色差，最终颜色以实物为准。</dd>
				</dl>
				<dl>
					<dt>为什么我收到的商品包装和图片显示的不一致？</dt>
					<dd>由于部分商品生产批次不一，你收到货品的包装有可能与网站上图片不完全一致，但mmall保证所售商品均为正品，商品包装请以实物为准。</dd>
				</dl>
				<dl>
					<dt>网站上显示的参考价和实体店的售价一致吗？</dt>
					<dd>mmall的参考价采集自品牌官网标价、专柜标价或由品牌供应商提供。由于地区或时间的差异性，可能与你购物时的参考价不一致。mmall标注的参考价仅供你参考，不作为购物依据。</dd>
				</dl>
				<dl>
					<dt>如何退货？</dt>
					<dd>你签收商品之日起的7天之内，商品未经穿着不影响二次销售的情况下，mmall为你提供七天无理由放心退服务。</dd>
				</dl>
			</div>
		</div>
	</body>
</html>