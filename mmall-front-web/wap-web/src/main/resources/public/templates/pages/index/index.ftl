<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
		<#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-首页</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
		<link rel="stylesheet" type="text/css" href="/src/less/swiper-3.2.7.min.css"/>
        <@less />
  		<link rel="stylesheet" type="text/css" href="/src/css/page/search.css?v=1.0.0.3"/>
    </head>
    <body>
        <div class="wrap" id="wrap">
            <header class="header">
                <div class="location">
                    <a href="/location/page/province">
                        <i><#--<img src="/src/img/svg/location.svg" />--></i>
                    </a>
                </div>
                <div class="search">
                    <form action="">
                    <span class="searchIcon"></span>
                    <input class="form-control searchPlace" type="text" placeholder="输入关键字查找" id="key"/>
                    <span class="cancel">取消</span>
                    </form>
                </div>
                <div class="myInfo">
                    <a href="/profile/index/">
                        <i><img src="/src/img/svg/head-my.svg" /></i>
                    </a>
                </div>
                <div class="category">
                    <a href="/page/category/">
                        <i><img src="/src/img/svg/head-cate.svg" /></i>
                    </a>
                </div>
            </header>
            <#include "index.html" />

			<#if mainsiteIndexVO??>
                <#list mainsiteIndexVO.mainsiteStoreVOs as store>
    			    <div class='home-tit'>
    			        <span>${store.storeName}</span>
    			        <#if store.storeUrl??>
    			       	    <a href="${store.storeUrl}" class="more">更多<i></i></a>
    					</#if>
    			    </div>
                    <div class='home-list'>
                        <ul>
                            <#list store.mainsiteProductVOs as product>
                                <li>
                                    <a href="/product/detail?skuId=${product.skuId}">
                                        <img src="${product.showPicPath}?imageView&quality=70&thumbnail=160x160">
                                        <span class="name">${product.productName}</span>
                                        <span class="tit">${product.productTitle}</span>
                                        <span class="price">
                                            ￥<em>${product.productPriceVOs[0].prodPrice}</em>
                                        </span>
                                    </a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#list>
			</#if>
            <footer class="footer">
                <p class='tel'><span>客服0571-87651759</span><a href='http://023.baiwandian.cn?from=wap'>电脑版</a></p>
                <p class='copyright'>&copy;2014-2015 023.baiwandian.cn 版权所有</p>
            </footer>
           <#-- <@downApp /> -->
            <@fixRight />
        </div>
		<div class="searchMode">
            <div id="result" class="tab-pane">
                <ul class="list" id="searchList">
                </ul>
                <div id='clearHistory'>清除历史搜索</div>
            </div>
        </div>
        <div class="searchMask"></div>
    </body>
    <@jsFrame />
    <script src="/src/frame/swiper-3.2.7.jquery.min.js"></script>
    <script type="text/javascript">
        /* banner　*/
        var banner = new Swiper ('.swiper-container', {
		    loop: true,
		    pagination: '.swiper-pagination',
		    autoplay: 3000,
		    autoplayDisableOnInteraction: false
		});
		       
        /* get android or ios */
        var u = navigator.userAgent,
            mobile = "";
        if (u.indexOf("Android") >= 0) {
            mobile = "Android";
        } else if (u.indexOf("iPhone") >= 0)  {
            mobile = "IOS";
        }

        if (mobile != "IOS") {
            $(".down-app").hide();
            $(".footer").css({
                paddingBottom: "0.4rem"
            });
        }

        /* close app down */
        $(".down-app .close").on("click", function () {
            $(this).closest(".down-app").remove();
            $(".footer").css({
                paddingBottom: 0.4 + "rem"
            });
        });

        /* hot set width */
        var len = $(".hot .list li").length;
        $(".hot .list").css({
            width: len * 3.8 + "rem"
        });
        
        /* banner module */
        var bannerImg = $(".swiper-slide");

        bannerImg.on("click", function () {
            var link = $(this).data("link");
            window.location.href = link;
        });

        $(window).resize(function() {
            wrapWidth   = $(".wrap").width();
        });

        /* get cart number */
        getCartNum();
    </script>
    <script type="text/javascript" src="${jslib}define.js?${jscnf}"></script>
    <script type="text/javascript" src="${jspro}page/search.js?v=1.0.0.3"></script>
</html>
</@compress>
</#escape>