<#include "../app.common.ftl">
<#--声明销量起始值-->
<#assign salenum = 0>
<#--声明剩余数起始值-->
<#assign resnum = 0>
<#--销量计算  PS:避免使用JS计算敏感值-->
<#list product.sizeSpecList as p>
	<#assign salenum = salenum + p.total - p.num> 
	<#assign resnum = resnum + p.num>
</#list>

<!DOCTYPE html>
<html>
	<head>
		<#--meta标签-->
		<@headMeta/>
		<#--@CSS-->
		<link type="text/css" href="/src/css/page/webview/app_base.css" rel="stylesheet" />
		<link type="text/css" href="/src/css/page/webview/util_swiper.css" rel="stylesheet" />
		<link type="text/css" href="/src/css/page/webview/share_product.css" rel="stylesheet" />
		<title>${(product.productName)!}-mmall</title>
		<!--[if lt IE 8]>      <![endif]-->
	</head>
	<body>
		<noscript></noscript>
		<#--Open App-->
		<@openApp/>
		<#--container-->
		<div class="g-container">
			<@topbar></@topbar>
			<div class="swiper-container m-scontainer">
				<#--信息页-->
				<div class="m-showpage sp1">
					<div class="swiper-wrapper showpage1-wrapper">
						<div class="swiper-slide showpage1-slide">
							<div class="g-downApp-wrap"></div>
							<#--错误信息提示-->
							<#if canAccess == 0>
							<div class="g-remind-tip">
								<a href="http://023.baiwandian.cn">
									该地区暂不支持购买，去首页看看
								</a>
							</div>
							<#elseif activeFlag?? && activeFlag == 2>
							<div class="g-remind-tip">
								<a>
									对不起,该抢购活动已经结束
								</a>
							</div>
							</#if>
							<#--轮播图-->
							<div class="m-imgshow">
								<div class="swiper-container imgshow-container">
									<div class="swiper-wrapper imgshow-wrapper">
									<#if product.prodShowPicList?has_content>
										<#list product.prodShowPicList as pic>
                                            <div class="swiper-slide imgshow-slide">
                                                <a href=${"http://m.023.baiwandian.cn/detail?id="+product.productId} target="_blank">
                                                    <img src="${(pic)!""}?imageView&quality=100&thumbnail=640y640" onerror="this.src='/mobile/images/default.jpg'"/>
                                                </a>
                                            </div>
										</#list>
									<#else>
                                        <div class="swiper-slide imgshow-slide">
                                            <a href="http://023.baiwandian.cn" target="_blank">
                                                <img src="/res/3g/images/webview/default.jpg"/>
                                            </a>
                                        </div>
									</#if>
                                    </div>
									<#if resnum == 0>
										<span class="m-module-saleout">
											<img src="/res/3g/images/webview/saleout.png"/>
										</span>
									</#if>
								</div>
								<div class="imgshow-pagination"></div>
							</div>
							<#--商品信息-->
							<div class="m-ware-tip">
								<p class="m-ware-name">
									${product.productName!}
								</p>
								<#if canAccess != 0>
									<p class="m-ware-price">
										<span class="m-price-logo">￥</span><span class="m-price-now">${product.salePrice!}</span>
										<span class="m-price-discount">${(product.salePrice/product.marketPrice*10)?string("0.#")}折</span>
										<#--活动未开始-->
										<#if activeFlag == 0>
											<span id="m-tab-timer">将于${startTime?number_to_datetime?string("MM月dd日hh点")}开始</span>
										<#--活动进行中-->
										<#elseif activeFlag == 1>
											<span id="m-tab-timer">获取数据中...</span>
										</#if>
									</p>
									<p class="m-ware-info">
										专柜价：<del class="m-price-old">￥${product.marketPrice!}</del>
										 <#--<#if salenum != 0><span class="m-info-sales">销量&nbsp;${salenum}笔</span></#if>-->
									</p>
								<#else>
                                    <#--<p class="m-ware-info">
                                        销量&nbsp;${salenum}笔
                                    </p>-->
								</#if>
							</div>
							<#-- 活动信息-->
							<#if canAccess != 0 && activeFlag != 2 && activity??>
								<div class="m-ware-activity">
								${(activity.desp)!}
								</div>
							</#if>
							<#--安全信息-->
							<div class="m-guarantee">
								<div class="m-guarantee-tip">
									<div class="m-agreement-logo alogo1"></div>
									<div class="m-guarantee-words">
										<p>
											平台所销售商品均正品并获得品牌授权
										</p>
										<p>
											由中国平安保险公司为您担保
										</p>
									</div>
								</div>
								<div class="m-guarantee-tip">
									<div class="m-agreement-logo alogo2"></div>
									<div class="m-guarantee-words">
										<p>
											7天无理由退货，由mmall承担退货运费
										</p>
									</div>
								</div>
							</div>
							<#--提示按钮-->
							<div class="m-touch-tip">
								<label>上拉显示商品详情</label>
							</div>
						</div>
					</div>
					<#--自定义滚动条-->
					<div class="swiper-scrollbar m-scrollbar showpage1-scrollbar"></div>
				</div>
				<#--详情页-->
				<div class="m-showpage sp2">
					<div class="swiper-wrapper showpage2-wrapper">
						<div class="swiper-slide showpage2-slide">
							<div class="g-downApp-wrap"></div>
							<#--加载提示-->
							<div class="g-detail-loading">
								<p class="m-detail-loading">
									<span></span>加载中...
								</p>
							</div>
							<#--详情内容-->
							<div class="g-detail-container"></div>
						</div>
					</div>
					<#--回顶按钮-->
					<div class="backTop">
						<img src="/res/3g/images/webview/backTop.png" width="36" />
					</div>
					<#--自定义滚动条-->
					<div class="swiper-scrollbar m-scrollbar showpage2-scrollbar"></div>
				</div>
			</div>
		</div>
		<#--@script-->
		<script>
			<#--商品信息存储-->
			window.PRODUCTDETAIL = {
				<#if activeFlag == 1>end : ${endTime},</#if>
                product : ${JsonUtils.toJson(product)},
				activity : ${JsonUtils.toJson(activity)},
				colors : ${JsonUtils.toJson(colors)}
			}
		</script>
		<script src="${jslib}define.js?${jscnf}"></script>
        <script src="${jspro}page/webview/share/product/detail.js"></script>
	</body>
</html>