<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
    	<#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-产品详情</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
        <@less />
        <link rel="stylesheet" type="text/css" href="/src/css/page/search.css?v=1.0.0.2"/>
    </head>
    <body class="other-search">
        <@ga />
        <div class="wrap" id="wrap">
            <header class='back-head fixed-head'>
                <span class='go-back'></span>
                <em>产品详情</em>
                <span class="search"></span>
            </header>
            <#if isAllowed?? >
				<#if isAllowed=false>
				<div class="messagetip" style="position:absolute; width: 11em; left: 0.9em; top: 5em; z-index: 12; display: block;">
				      <div class="tiptop">提示</div>
				      <div class="tipinfo">
				          <div>很遗憾，您所在的地区没有售卖该商品！</div>
				      </div>
				      <div class="tipbuttom" style="width: 11em;">
				           <ul class="tipbtn">
				               <li class="ok">
				                   <a href="/" class="sure message-btn">确定</a>
				               </li>
				           </ul>
				      </div>
				</div>
				<div class="messageMask" style="z-index: 11; width: 100%; height: 100%; left: 0px; top: 0px; display: block;"></div>
				</#if>
			</#if>
            <#if product?? >
                <div class="pro-detail">
                    <div class="pic" id='scroll'>
                        <ul class="img">
                            <#list product.picList as piclist>
                                <li>
                                	<img src='${piclist.picPath}' />
                                </li>
                            </#list>
                        </ul>
                        <ul class="btn">
                        </ul>
                    </div>
                    <div class="line"></div>
                    <p class="name">
                        ${product.productName}
                    </p>
                    <p class="title">
                        ${product.productTitle}
                    </p>
                    <div class="price">
                        <#list product.priceList as priceList>
                            <em>¥</em>${priceList.prodPrice}
                        </#list>
                        <#if product.skuLimitConfigVO??>
                            <span class="discount">${product.skuLimitConfigVO.limitComment}</span>
                        </#if>
                    </div>
                    <div class="item">
                        <div class='service'>
                            <span class='start'>
                                <i>起</i>
                                <em>${product.batchNum}</em>${product.prodUnit}起批
                            </span>
                            <span class='end'>
                                <i>到</i>
                                支持货到付款
                            </span>
                            <#if product.canReturn == 1>
                                <span class='return'>
                                    <i>退</i>
                                    支持退货
                                </span>
                            </#if>
                        </div>
                        <div class="store">
                            <a href="/store/${product.supplierId}/">
                                <span class='store-name'><i></i>${product.storeName}</span>
                                <span class='store-start'>${storeInfo.batchCash}元起送</span>
                                <em></em>
                            </a>
                        </div>
                    </div>
                    <div class="division">
                        <span class="text">继续向上拖动查看图文详情</span>
                        <span class="line"></span>
                    </div>
                    <div class="detail-info">
                        <h2>商品详情</h2>
                        <div class="list">
                            <ul>
                                <#if product.brandName != "">
                                    <li>品牌：<span>${product.brandName}</span></li>
                                </#if>
                                <#if product.prodUnit != "">
                                    <li>销售单位：<span>${product.prodUnit}</span></li>
                                </#if>
                                <#if ((product.skuId)?length > 0)>
                                    <li>商品货号：<span>${product.skuId}</span></li>
                                </#if>
                                <#if product.prodBarCode != "">
                                    <li>单品条码：<span>${product.prodBarCode}</span></li>
                                </#if>
                                <#setting date_format="yyyy-MM-dd"/>
                                <#if product.prodProduceDate != 0>
                                    <li>生产日期：<span>${product.prodProduceDate?number_to_date}</span></li>
                                </#if>
                                <#if product.expireDate != "">
                                    <li>保质期（月）：<span>${product.expireDate}</span></li>
                                </#if>
                                <#if ((product.salePrice)?length > 0) && (product.salePrice > 0)>
                                    <li> 建议零售价：¥<span class="sale-price">${product.salePrice}</span></li>
                                </#if>
                            </ul>
                        </div>
                        <div class='html-info'>
                            <#noescape>
                                <#if product.prodDetail.customEditHTML??>
                                    ${product.prodDetail.customEditHTML}
                                </#if>
                            </#noescape>
                        </div>
                    </div>
                    <div class="other">
                        <div class="info">
                            <span class="img">
                                <img src='${product.picList[0].picPath}' />
                            </span>
                            <span class="num p-t">
                                价格：<i class="red">￥</i><em class="price">${product.priceList[0].prodPrice}</em>
                            </span>
                            <span class="num">
                                库存：<em class="inventory">${product.productNum}</em>${product.prodUnit}
                            </span>
                            <i class="close"></i>
                        </div>
                        <div class="buy-num">
                            <span class="tit">购买数量</span>
                            <div class='adjust-num'>
                                <span class="minus">-</span>
                                <input type="number" value="${product.batchNum}" />
                                <span class="plus">+</span>
                            </div>
                        </div>
                        <div class="submit" id='addCart'>
                            确定
                        </div>
                    </div>
                    <div class="detail-popup"></div>
                </div>
                <div class="fixed-foot">
                    <span class='collect'>
                        <i></i>
                        <em>收藏商品</em>
                    </span>
                    <#if product.skuLimitConfigVO??>
                        <#if (.now?long < product.skuLimitConfigVO.limitStartTime)>
                            <span class="add-cart disable">
                                <em>限购即将开始</em>
                            </span>
                        <#elseif (.now?long > product.skuLimitConfigVO.limitEndTime)>
                            <span class="add-cart disable">
                                <em>限购已经结束</em>
                            </span>
                        <#else>
                            <#if product.batchNum gte product.productNum >
                                <span class="add-cart disable">
                                    <em>库存不足</em>
                                </span>
                            <#else>
                                <span class="add-cart">
                                    <i></i>
                                    <em>加入进货单</em>
                                </span>
                            </#if>
                        </#if>
                    <#else>
                        <#if product.batchNum gte product.productNum >
                            <span class="add-cart disable">
                                <em>库存不足</em>
                            </span>
                        <#else>
                            <span class="add-cart">
                                <i></i>
                                <em>加入进货单</em>
                            </span>
                        </#if>
                    </#if>
                </div>
                <@fixRight />
            <#else>
                <div class="pro-detail pro-detail-null">
                    <i></i>
                    <span>该商品已下架或者不存在</span>
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
    <@jsFrame />
    <script type="text/javascript">

        $(function () {

            var wrapWidth = $(".wrap").width();

            /* create btn */
            function createBtn() {
                var len = $(".pro-detail .img").find("li").length,
                    box = $(".pro-detail .btn"),
                    dom = "",
                    btnWidth = "";

                if (len > 1) {
                    for (var i = 0; i < len; i++) {
                        dom += "<li></li>";
                    }
                }
                box.append(dom);
                box.find("li:first").addClass("active");
                btnWidth = box.width();
                box.css({
                    left : (wrapWidth - btnWidth) / 2 + "px"
                });
            }
            createBtn();

            /* prodcut detail touch move */
            var scroll      = $("#scroll"),
                scrollImg   = scroll.find(".img"),
                scrollBtn   = scroll.find(".btn"),
                scrollLen   = scroll.find(".img li").length,
                scrollEl    = document.querySelector("#scroll"),
                scrollHam   = new Hammer(scrollEl),
                wrapWidth   = $(".wrap").width(),
                flag        = 0;

            scrollHam.on("swipeleft", function() {
                if (!scrollImg.is(":animated")) {
                    flag++;
                    if (flag >= scrollLen) {
                        flag = scrollLen - 1;
                    } else {
                        scrollBtn.find("li").eq(flag).addClass("active").siblings().removeClass("active");
                        scrollImg.animate({
                            left : -wrapWidth * flag
                        }, 500);
                    }
                }
            });

            scrollHam.on("swiperight", function() {
                if (!scrollImg.is(":animated")) {
                    flag--;
                    if (flag < 0) {
                        flag = 0;
                    } else {
                        scrollBtn.find("li").eq(flag).addClass("active").siblings().removeClass("active");
                        scrollImg.animate({
                            left : -wrapWidth * flag
                        }, 500);
                    }
                }
            });

            var url = window.location.href,
                key = url.indexOf("="),
                skuID = url.substring(key + 1, url.length),
                collectFlag = 0,
                minus = $(".adjust-num .minus"),
                plus = $(".adjust-num .plus"),
                input = $(".adjust-num input"),
                newPrice = "";

            <#if product?? >
                var minNum = ${product.batchNum},
                    inventory = ${product.productNum},
                    salePrice = ${product.salePrice},
                    prodPrice = ${product.priceList[0].prodPrice},
                    proId = ${product.priceList[0].productId};
                isCollect();
            </#if>

            // plus number
            plus.on("click", function () {
                var inputVal = input.val();
                minus.removeClass('disable');
                if (inputVal >= inventory) {
                    $.message.alert("提示", "超过最大库存");
                } else {
                    inputVal++;
                }
                input.val(inputVal);
            });

            // minus number
            minus.on("click", function () {
                var inputVal = input.val();
                plus.removeClass('disable');
                if (inputVal <= 1 || inputVal <= minNum) {
                    $(this).addClass("disable");
                    return;
                } else {
                    inputVal--;
                    input.val(inputVal);
                }
            });

            // input number
            input.on("change", function () {
                var reg = /^[1-9]\d*$/,
                    inputVal = input.val();
                if (inputVal > inventory) {
                    $.message.alert("提示", "超过最大库存");
                    inputVal = inventory;
                }
                if (isNaN(inputVal) || inputVal < 1 || inputVal <= minNum) {
                    inputVal = minNum;
                    minus.addClass('disable');
                } else {
                    minus.removeClass('disable');
                }
                input.val(inputVal);
            });

            // load product detial
            $(window).scroll(function () {
                var scrollTop = $(this).scrollTop(),
                    scrollHeight = $(document).height(),
                    windowHeight = $(this).height();

                if (scrollTop + windowHeight == scrollHeight) {
                    $(".detail-info").show();
                    //$(".division").hide();
                }
                if (scrollTop == 0) {
                    $(".detail-info").hide();
                }
            });

            // show other info
            $(".add-cart").on("click", function () {
                if ($(this).hasClass("disable")) {
                    return;
                } else {
                    $(".detail-popup").show();
                    $(".other").fadeIn();
                }
            });

            // hide other info
            $(".close").on("click", function () {
                $(".detail-popup").hide();
                $(".other").fadeOut();
            });

            // add cart
            $("#addCart").on("click", function () {
                addToCart();
                $(".detail-popup").hide();
                $(".other").fadeOut();
            });

            function addToCart() {
                var cartNum = input.val();
                $.ajax({
                    cache: false,
                    type : "POST",
                    url: "/cart/addToCart",
                    async: false,
                    contentType: "application/json",
                    data : JSON.stringify({
                        "skuId" : skuID,
                        "diff" : cartNum
                    }),
                    success: function (data) {
                        if (data.code == 200) {
                            $.message.alert("提示", "加入进货单成功");
                            /* get cart number */
                            getCartNum();
                        } else if(data.code == 403){
                            $.message.alert("提示", "您尚未登录，请先登录", function(){
                                window.location.href = "/login?redirectURL="+encodeURIComponent(url);
                            });
                        } else if(data.code == 404){
                            $.message.alert("提示", "进货单中最多只能加入50件不同的商品");
                        }
                    }
                });
            }

            /* collect */
            $(".collect").on("click", function() {
                if(collectFlag == 200) {
                    unFollow(proId);
                    $(this).removeClass("active");
                } else if(collectFlag == 400){
                    $(this).addClass("active");
                    follow(proId);
                }else if(collectFlag == 403){
                    $.message.alert("提示", "您尚未登录，请先登录", function(){
                        window.location.href = "/login?redirectURL="+encodeURIComponent(url);
                    });
                }
            });

            function isCollect(){
                $.ajax({
                    cache: false,
                    type : "GET",
                    url: "/attention/product/isCollect?productId="+proId,
                    async: false,
                    success: function (data) {
                        collectFlag = data.code;
                        console.log(collectFlag)
                        if(data.code == 200){
                            $(".collect").addClass("active");
                        }
                    }
                });
                return collectFlag;
            }

            function follow(id) {
                $.ajax({
                    type : "POST",
                    url : "/attention/product/follow",
                    async: true,
                    contentType: "application/json",
                    data: JSON.stringify({
                        "poId" : id
                    }),
                    success : function (data) {
                        if (data.code == 200) {
                            $.message.alert("提示", "收藏成功！");
                        }
                    }
                });
                collectFlag = 200;
            }

            function unFollow(id) {
                $.ajax({
                    type : "POST",
                    url : "/attention/product/unfollow",
                    async: true,
                    contentType: "application/json",
                    data: JSON.stringify({
                        "poId" : id
                    }),
                    success : function (data) {
                        if (data.code == 200) {
                            $.message.alert("提示", "已取消收藏！");
                        }
                    }
                });
                collectFlag = 400;
            }

            /* get cart number */
            getCartNum();

        });
    </script>
	<script type="text/javascript" src="${jslib}define.js?${jscnf}"></script>
	<script type="text/javascript" src="${jspro}page/other-search.js?v=1.0.0.3"></script>
</html>
</@compress>
</#escape>