<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <@title type="snapShot"/>
    <meta charset="utf-8"/>
    <@css/>
</head>
<body>
	<@fixedTop />
	<@topbar />
	<@top />
	<@mainCategory />
	<#if snapshot?? >
	<div class="wrap clearfix">
		<div class="position">
			<#if snapshot.categoryFullName??>
				${snapshot.categoryFullName}
			</#if>                           
		</div>
	</div>
	<div class="wrap clearfix pro-detail">
		<div class="l main pro-intro clearfix">
			<div class="l preview">
				<div class="view">
					<ul>
					<#if snapshot.picPath??>
						<#list snapshot.picPath as piclist>
							<li><span><img src="${piclist}" /></span></li>
						</#list>
					<#elseif snapshot.picUrl??>
							<li><span><img src="${snapshot.picUrl}" /></span></li>
					<#else>
						<li><span>暂无图片</span></li>
					</#if>
					</ul>
				</div>
				<div class="list">
					<ul>
						<#if snapshot.picPath??>
							<#list snapshot.picPath as piclist>
								<li><span><img src="${piclist}" /></span></li>
							</#list>
						<#elseif snapshot.picUrl??>
								<li><span><img src="${snapshot.picUrl}" /></span></li>
						<#else>
							<li><span>暂无图片</span></li>
						</#if>
					</ul>
				</div>
			</div>	
			<div class="l item">
				<#if snapshot.productName??>
					<p class="name" title="${snapshot.productName}">
						${snapshot.productName}
					</p>
				</#if>
				<#if snapshot.productTitle??>
					<p class="descrip" title="${snapshot.productTitle}">
						${snapshot.productTitle}
					</p>
				</#if>
				<p class="initial">
					<#--建议零售价：¥<span class="sale-price">${snapshot.salePrice}</span>-->
					<#--（<span class="batch-num">${snapshot.batchNum}</span>${snapshot.prodUnit}起批）-->
				</p>
				<div class="clearfix prcie-list">
					<#if snapshot.batchNum??>
						<dl class="number clearfix">
							<dt>起批量</dt>
							<dd>${snapshot.batchNum}</dd>
						</dl>
					</#if>
					<#if snapshot.priceList??>
						<dl class="price clearfix">
							<dt>订货价</dt>
								<#list snapshot.priceList as priceList>
								<dd><span class="b-font">¥</span><em>${priceList.prodPrice}</em><i></i></dd>
								</#list>
						</dl>
					</#if>
				</div>
				<#if snapshot.speciList??>
					<#list snapshot.speciList as item>
						<div class="type">
							<span class="tit">${item.specificationName}</span>
							<em class="active">${item.speciOption}</em>                              
						</div>
					</#list>
				</#if>
				<div class="snapshot">
					<i></i>
					<p>您现在查看的是<span>交易快照</span></p>
					<#if snapshot.updateTime??>
						<p>该商品在${snapshot.updateTime}已被编辑。</p>
					</#if>
					<p>
						<#if snapshot.skuId??>
							<a href="/product/detail?skuId=${snapshot.skuId}" target="_blank">点击查看最新商品详情</a>
						</#if>
					</p>
				</div>
			</div>
		</div>
		<div class="r side shop-info">
			<div class="clearfix person-info">
				<span>
					<img src="/src/test-pic/pic-24.png" />
				</span>
				<#if storeInfo.id?? && storeInfo.storeName??>
					<a href="/store/${storeInfo.id}/">${storeInfo.storeName}</a>
				</#if>
				<i></i>
			</div>
			<#if storeInfo.batchCash??>
				<div class="store-service clearfix">
					<div class="des">服务支持：</div>
					<ul>
						<li class="item-3"><i></i>
							<#if storeInfo.batchCash??>
								<span>起送金额${storeInfo.batchCash}元</span>
							</#if>
						</li>
					</ul>
				</div>
			</#if>
			<div class="shop-btn">
				<#if storeInfo.id??>
					<a href="/store/${storeInfo.id}/" class="btn-base btn-defalut">进入店铺</a>
				</#if>
			</div>
		</div>
		<br class="clear" />
		<div class="pro-descrip clearfix">
			<div class="clearfix tab">
				<ul>
					<li class="active bl">商品详情</li>
				</ul>
			</div>
			<div class="con">
				<div class="clearfix parameter">
				     <ul>
				     	 <#if snapshot.brandName??>
					         <#if snapshot.brandName != "">
					            <li title="${snapshot.brandName}">品牌：<span>${snapshot.brandName}</span></li>
					         </#if>
				         </#if>
				         <#if snapshot.unit??>
					         <#if snapshot.unit != "">
					            <li title="${snapshot.unit}">销售单位：<span>${snapshot.unit}</span></li>
					         </#if>
				         </#if>
				         <#if snapshot.skuId??>
					         <#if ((snapshot.skuId)?length > 0)>
	                            <li title="${snapshot.skuId}">商品货号：<span>${snapshot.skuId}</span></li>
	                         </#if> 
                         </#if>
                         <#if snapshot.prodBarCode??> 
	                         <#if snapshot.prodBarCode != "">                                           
	                            <li title="${snapshot.prodBarCode}">单品条码：<span>${snapshot.prodBarCode}</span></li> 
	                         </#if>  
                         </#if>                                        
                         <#setting date_format="yyyy-MM-dd"/>
                         <#if snapshot.prodProduceDate??> 
	                         <#if snapshot.prodProduceDate != 0> 
	                            <li title="${snapshot.prodProduceDate?number_to_date}">生产日期：<span>${snapshot.prodProduceDate?number_to_date}</span></li>
					         </#if>
				         </#if>
				         <#if snapshot.expireDate??> 
	                         <#if snapshot.expireDate != "">
					            <li title="${snapshot.expireDate}">保质期（月）：<span>${snapshot.expireDate}</span></li>               
					         </#if>
				         </#if>
				         <#if snapshot.salePrice??> 
					         <#if ((snapshot.salePrice)?length > 0) && (snapshot.salePrice > 0)>
					            <li title="${snapshot.salePrice}"> 建议零售价：¥<span class="sale-price">${snapshot.salePrice}</span></li>              
					         </#if>
				         </#if>
				     </ul>
				</div>
				
				<div class="html-text clearfix">
					<#noescape>
						<#if snapshot.customEditHTML??>
							${snapshot.customEditHTML}
						</#if>
					</#noescape>
				</div>
			</div>
		</div>
	</div>
	</#if>
	<@footer />
	<@copyright />
	<@fixedSide />
	<@fixedSideOrder />
	<@fixedSideReplenish />
	<@cityChange />
	<div class="gotop"></div>
    <@js />
    <script>
    	$(function () {
    		$(".pro-detail .list").on("mousemove", "li", function () {
    			var index = $(this).index();
    			$(this).addClass("active").siblings().removeClass("active");
    			$(".pro-detail .view li").eq(index).show().siblings().hide();
    		});
    	});
    </script>
</body>
</html>
</@compress>
</#escape>