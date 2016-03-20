<#--lastId 初始化-->
<#assign lastId = 0>

<#--container-->
<div class="g-container">
	<div class="g-downApp-wrap"></div>
	<#--活动信息-->
	<div class="g-tab-wrap">
		<#--活动信息提示-->
		<#if activity??>
		<div class="m-tab-desp">
			${(activity.desp)!}
		</div>
		</#if>
		<#--活动未开始-->
		<#if activeFlag == 0>
		<span id="m-tab-timer">将于${startTime?number_to_datetime?string("MM月dd日hh点")}开始</span>
		<#--活动进行中-->
		<#elseif activeFlag == 1>
		<span id="m-tab-timer">获取数据中...</span>
		<#--活动已结束-->
		<#else>
		<span id="m-tab-timer">档期已结束</span>
		</#if>
	</div>
	<#--商品列表-->
	<div class="g-tab-content">
		<div class="g-shop">
			<#--商品列表-->
			<ul class="g-shop-list">
			<#if result.total != 0>
				<#list result.list as l>
				<li>
					<div class="m-module">
						<#--点击跳转-->
						<a target="_blank" href=${"http://m.023.baiwandian.cn/detail?id="+l.id}>
							<#--商品大图-->
							<p class="m-module-img">
								<img src="${(l.listShowPicList[0])!""}?imageView&quality=100&thumbnail=320x0" onerror="this.src='/mobile/images/default.jpg'"/>
								<#--商品剩余数量计算-->
									<#assign num = 0>
									<#list l.skuList as sl>
										<#assign num = num + sl.state> 
									</#list>
								<#--商品剩余数量计算结束-->
								<#--商品售罄-->
								<#if num == l.skuList?size*3>
								<span class="m-module-saleout">
									<img src="/res/3g/images/webview/saleout.png"/>
								</span>		
								</#if>			
							</p>
							<#--商品信息-->
							<div class="m-module-info">
								<#--商品名称-->
								<p class="m-module-title">
									${l.productName}
								</p>
								<#--价格信息(售价，商场价，折扣(保留一位小数))-->
								<p class="m-module-price">
									<span class="m-price-now">￥${l.salePrice}</span>
									<del class="m-price-old">￥${l.marketPrice}</del>
									<span class="m-price-discount">
										<#if l.marketPrice != 0>
											${(l.salePrice/l.marketPrice*10)?string("0.#")}折
										<#else>
											0折
										</#if>
									</span>
								</p>
							</div> 
						</a>
					</div>
				</li>
				</#list>
				<#--保存lastId-->
				<#assign lastId = result.list[result.list?size - 1].id>
			</#if>
			</ul>
			<#--加载信息提示-->
			<div class="g-shop-loading">
				<p class="m-shop-loading">
					<span></span>加载中...
				</p>
				<p class="m-shop-nomore">
					<span>已到尾页</span>
				</p>
			</div>
		</div>
	</div>
	<#--回顶按钮-->
	<div class="backTop">
		<img src="/res/3g/images/webview/backTop.png" width="36" />
	</div>
</div>
<#--@script-->
<script>
	<#--PO信息获取-->
	window.PODETAIL ={
		<#if activeFlag == 1>end : ${endTime},</#if>
		lastId : ${lastId},
		hasNext :  ${(result.hasNext)?string}
	} 
</script>
<script src="${jslib}define.js?${jscnf}"></script>
<script src="${jspro}page/webview/share/list/list.js"></script>