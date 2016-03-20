<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
    	<#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-交易快照</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
        <@less />
        <link rel="stylesheet" type="text/css" href="/src/css/page/search.css?v=1.0.0.2"/>
        <style type="text/css">
        html,body{font-size:25px;}
        .pro-detail .name{height:auto;}
        .snapshot{
		    margin: 0.6rem;
		    border: 1px solid #ccc;
		    background: #fff;
		    padding: 5px;
		    color: #959595;
		    font-size: 0.56rem;
        }
        .productDetail a{
			display: block;
		    text-align: center;
		    background: #323232;
		    color: #fff;
		    height: 2rem;
		    line-height: 2rem;
		    margin: 0.6rem;
		    font-size: 0.56rem;
        }
        </style>        
    </head>
    <body class="other-search">
        <div class="wrap" id="wrap">
            <header class='back-head fixed-head'>
                <a href="javascript:history.go(-1);" class='go-back'></a>
                <em>交易快照</em>
                <span class="search" style='right: 0.5rem;'></span>
            </header>
            <#if snapshot.picPath??>
                <div class="pro-detail">
                    <div class="pic" id='scroll'>
                        <ul class="img">
                            <#list snapshot.picPath 	as piclist>
                                <li>
                                	<img src='${piclist}' />
                                </li>
                            </#list>
                        </ul>
                        <ul class="btn">
                        </ul>
                    </div>
                    <#if snapshot.productName??>
                    <p class="name">
                        ${snapshot.productName}
                    </p>
                    </#if>
                    <#if snapshot.productTitle??>
	                    <#if snapshot.productTitle != "">
	                    <p class="title">
	                        ${snapshot.productTitle}
	                    </p>
	                    </#if>
                    </#if>
                    <#if snapshot.priceList??>
                    <div class="price">
                        <#list snapshot.priceList as price>
                            <em>¥</em>${price.prodPrice}
                        </#list>
                        <#if snapshot.skuLimitConfigVO??>
                            <span class="discount">${snapshot.skuLimitConfigVO.limitComment}</span>
                        </#if>
                    </div>
                    </#if>
                    <#if snapshot.batchNum??>
                    <div class="item">
                        <div class='service'>
                            <span class='start'>
                                <i>起</i>
                                <em>${snapshot.batchNum}</em>
                            </span>
                            <span class='end'>
                                <i>到</i>
                                支持货到付款
                            </span>
                            <#if snapshot.canReturn == 1>
                                <span class='return'>
                                    <i>退</i>
                                    支持退货
                                </span>
                            </#if>
                        </div>
                        <div class="store">
                            <a href="/store/${storeInfo.id}/">
                                <span class='store-name'><i></i>${storeInfo.storeName}</span>
                                <span class='store-start'>${storeInfo.batchCash}元起送</span>
                                <em></em>
                            </a>
                        </div>
                    </div>
                    </#if>
					<div class="snapshot">
						<i></i>
						<p>您现在查看的是<span>交易快照</span></p>
						<#if snapshot.updateTime??>
							<p>该商品在${snapshot.updateTime}已被编辑。</p>
						</#if>
					</div>
					<div class="productDetail">
						<#if snapshot.skuId??>
							<a href="/product/detail?skuId=${snapshot.skuId}">点击查看最新商品详情</a>
						</#if>
					</div>					
                </div>
            </#if>
        </div>
		<div class="searchMode">
			<div class="searchBar">
				<form>
				<span class="searchIcon"></span>
				<input  placeholder="搜索你想找的商品..." id="key"/>
				<span class="cancel searchModeCancel">取消</span>
				</form>
			</div>
			<div id="result" class="tab-pane">
				<ul class="list" id="searchList">

				</ul>
				<div id='clearHistory'>清除历史搜索</div>
			</div>
		</div>
		<div class="searchMask"></div>
    </body>
	<script type="text/javascript" src="${jslib}define.js?${jscnf}"></script>
	<script type="text/javascript" src="${jspro}page/other-search.js?v=1.0.0.3"></script>
</html>
</@compress>
</#escape>