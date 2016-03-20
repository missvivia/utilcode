<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
		<title>新云联百万店-首页</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection"content="telephone=no, email=no" />
        <link rel="stylesheet" href="src/swiper-3.2.7.min.css" />
        <link rel="stylesheet" href="src/ios.css" />
    </head>
    <body>
        <div class="wrap" id="wrap">
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
                                    <a href="/m/productDetail?skuId=${product.skuId}">
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
        </div>
    </body>
    <script src="/src/jquery-1.11.3.min.js"></script>
    <script src="/src/hammer.min.js"></script>
    <script src="/src/swiper-3.2.7.jquery.min.js"></script>
    <script src="/src/ios.js"></script>
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
</html>
</@compress>
</#escape>