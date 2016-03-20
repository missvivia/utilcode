<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <@title type="proDetail"/>
    <meta charset="utf-8"/>
    <@css/>
</head>
<body>
	<#if isAllowed?? >
		<#if isAllowed=false>
		<div class="messagetip" style="width: 350px; left: 776.5px; top: 413px; z-index: 16; display: block;">
		     <div class="tiptop">
		     </div>
		     <div class="tipinfo">
		         <div class="messageIcon messageInfo"></div>
		         <div>很遗憾，您所在的地区没有售卖该商品！</div>
		     </div>
		     <div class="tipbuttom" style="width: 350px;">
		         <div class="tipbtn">
		              <a href="/" class="sure message-btn" style="padding:0px;">确定</a>
		         </div>
		     </div>
	     </div>
		<div class="messageMask" style="z-index: 15; width: 100%; height: 100%; left: 0px; top: 0px; display: block;"></div>
		</#if>
	</#if>
	<@fixedTop />
	<@topbar />
	<@top />
	<@mainCategory />
	<#if product?? >
	<div class="wrap clearfix">
		<div class="position">
			${product.categoryFullName}
		</div>
	</div>
	<div class="wrap clearfix pro-detail">
		<div class="l main pro-intro clearfix">
			<div class="l preview">
				<div class="view">
					<ul>
					<#list product.picList as piclist>
						<li><span><img src="${piclist.picPath}" /></span></li>
					</#list>
					</ul>
				</div>
				<div class="list">
					<ul>
						<#list product.picList as piclist>
							<li><img src="${piclist.picPath}" /></li>
						</#list>
					</ul>
				</div>
				<div class="collect">
				    <a href="javascript:void(0)">
				        <i></i>
				        <span>收藏商品</span>
				    </a>
				</div>
			</div>
			<div class="l item">
				<p class="name" title="${product.productName}">
					${product.productName}
				</p>
				<p class="descrip" title="${product.productTitle}">
					${product.productTitle}
				</p>
				<div class="clearfix prcie-list">
					<dl class="number clearfix">
						<dt>起批量</dt>
						<dd>${product.batchNum}${product.prodUnit}</dd>
					</dl>
					<dl class="price clearfix">
						<dt>订货价</dt>
						<#list product.priceList as priceList>
						<dd><span class="b-font">¥</span><em>${priceList.prodPrice}</em><i></i></dd>
						</#list>
					</dl>
				</div>
				<div class="inventory clearfix">
					<span class="tit">库存：</span>
					<em><i>${product.productNum}</i>${product.prodUnit}</em>
				</div>
				<#if product.speciList??>
					<#list product.speciList as item>
						<div class="type">
							<span class="tit">${item.specificationName}</span>
							<em class="active">${item.speciOption}</em>
						</div>
					</#list>
				</#if>
				<div class="num clearfix">
					<span class="tit">数量：</span>
					<div class="skip clearfix" onselectstart="return false;" style="-moz-user-select:none;">
                        <em class="down disable">-</em>
                        <#if product.batchNum gte product.productNum >
                            <input type="text" class="l num" value="0" onselectstart="event.cancelBubble=true">
                            <em class="up disable">+</em>
                        <#else>
                            <input type="text" class="l num" value="${product.batchNum}" onselectstart="event.cancelBubble=true">
                            <em class="up">+</em>
                        </#if>

					</div>
				</div>
				<div class="subtotal">
					<span class="tit">小计：</span>
					<b><em class="change-num">1</em>${product.prodUnit}</b>
					<i>|</i>
					<b>￥<em class="change-price">${product.priceList[0].prodPrice}</em>元</b>
				</div>
				<div class="clearfix btn">
                    <#if product.skuLimitConfigVO??>
                        <#if (.now?long < product.skuLimitConfigVO.limitStartTime)>
                            <input type="button" value="限购即将开始" class="bg-gray add-cart" />
                        <#elseif (.now?long > product.skuLimitConfigVO.limitEndTime)>
                            <input type="button" value="限购已经结束" class="bg-gray add-cart" />
                        <#else>
                            <#if product.batchNum gte product.productNum >
                                <input type="button" value="库存不足" class="bg-gray add-cart" />
                            <#else>
                                <input type="button" value="加入进货单" class="bg-red add-cart" />
                                <div class='limit-tips'>
                                    <#if product.skuLimitConfigVO??>
                                        ${product.skuLimitConfigVO.limitComment}
                                    </#if>
                                </div>
                            </#if>
                        </#if>
                    <#else>
                        <#if product.batchNum gte product.productNum >
                            <input type="button" value="库存不足" class="bg-gray add-cart" />
                        <#else>
                            <input type="button" value="加入进货单" class="bg-red add-cart" />
                        </#if>
                    </#if>
				</div>
				<#if product.canReturn == 1>
					<div class="return">
					    <i></i>
					    <span>支持退货</span>
					</div>
				</#if>
			</div>
		</div>
		<div class="r side shop-info">
			<div class="clearfix person-info">
				<span>
					<img src="/src/test-pic/pic-24.png" />
				</span>
				<a href="/store/${product.supplierId}/">${product.storeName}</a>
				<i></i>
			</div>
			<#if storeInfo.batchCash??>
			<div class="store-service clearfix">
				<div class="des">服务支持：</div>
				<ul>
					<li class="item-3"><i></i><span>起送金额${storeInfo.batchCash}元</span></li>
				</ul>
			</div>
			</#if>
			<div class="shop-btn">
				<a href="/store/${product.supplierId}/" class="btn-base btn-defalut">进入店铺</a>
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
				         <#if product.brandName != "">
				            <li title="${product.brandName}">品牌：<span>${product.brandName}</span></li>
				         </#if>
				         <#if product.prodUnit != "">
				            <li title="${product.prodUnit}">销售单位：<span>${product.prodUnit}</span></li>
				         </#if>
				         <#if ((product.skuId)?length > 0)>
                            <li title="${product.skuId}">商品货号：<span>${product.skuId}</span></li>
                         </#if>
                         <#if product.prodBarCode != "">
                            <li title="${product.prodBarCode}">单品条码：<span>${product.prodBarCode}</span></li>
                         </#if>
                         <#setting date_format="yyyy-MM-dd"/>
                         <#if product.prodProduceDate != 0>
                            <li title="${product.prodProduceDate?number_to_date}">生产日期：<span>${product.prodProduceDate?number_to_date}</span></li>
				         </#if>
                         <#if product.expireDate != "">
				            <li title="${product.expireDate}">保质期（月）：<span>${product.expireDate}</span></li>
				         </#if>
				         <#if ((product.salePrice)?length > 0) && (product.salePrice > 0)>
				            <li title="${product.salePrice}"> 建议零售价：¥<span class="sale-price">${product.salePrice}</span></li>
				         </#if>
				     </ul>
				</div>
				<div class="html-text clearfix">
					<#noescape>
						<#if product.prodDetail.customEditHTML??>
							 ${product.prodDetail.customEditHTML}
						</#if>
					</#noescape>
				</div>
			</div>
		</div>
	</div>
	<#else>
	    <div class="g-bd wrap nocart" style="font-size:16px;height:355px;line-height:355px;">
	        该商品已下架或者不存在
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
    <script src="/src/js/jquery.fly.min.js?2015082513" type="text/javascript"></script>
    <script type="text/javascript">
    	$(function () {

    		var proList = $(".pro-detail .list"),
    			proView = $(".pro-detail .view li"),
    			proType = $(".pro-detail .type"),
    			proTab = $(".pro-detail .tab li"),
    			up = $(".pro-detail .skip .up"),
    			down = $(".pro-detail .skip .down"),
    			input = $(".pro-detail .skip .num"),
    			priceList = $(".pro-detail .price dd"),
    			proViewBox = $(".pro-detail .view ul"),
    			proListBox = $(".pro-detail .list ul"),
    			minPicParam = "?imageView&thumbnail=70x70&quality=95",
				proViewDom = "",
				proListDom = "",
				proSizeBox = $(".pro-detail .type"),
				addCart = $(".add-cart"),
				defNum = input.val(),
				newPrice = "",
				collectFlag = 0;

				<#if product?? >
					var minNum = ${product.batchNum},
	                    inventory = ${product.productNum},
	                    salePrice = ${product.salePrice},
	                    prodPrice = ${product.priceList[0].prodPrice},
	                    proId = ${product.priceList[0].productId};
	                isCollect();
                </#if>

            //判断商品是否已收藏
            function isCollect(){
                $.ajax({
                	cache: false,
                    type : "GET",
                    url: "/attention/product/isCollect?productId="+proId,
                    async: false,
                    success: function (data) {
                        collectFlag = data.code;
                        if(data.code == 200){
                            $(".collect a").addClass("active");
                        }
                    }
                });
                return collectFlag;
            }

    		proList.on("mousemove", "li", function () {
    			var index = $(this).index();
    			$(this).addClass("active").siblings().removeClass("active");
    			$(".pro-detail .view li").eq(index).show().siblings().hide();
    		});

    		proType.on("click", "em", function () {
    			$(this).addClass("active").siblings().removeClass("active");
    		});

    		proTab.on("click", function () {
    			$(this).addClass("active").siblings().removeClass("active");
    		});

			// 获取产品ID (如果是订单页面进入商品详情会带入多余参数)
			var url = window.location.href,
				key = url.indexOf("="),
				end = url.indexOf("&");
			if (end > 0) {
				skuID = url.substring(key + 1, end);
			} else {
				skuID = url.substring(key + 1, url.length);
			}

			// 增加件数
    		up.on("click", function () {
                if ($(this).hasClass("disable")) {
                    return;
                } else {
        			var inputVal = input.val();
        			down.removeClass('disable');
        			if (inputVal >= inventory) {
        				$.message.alert("超过最大库存", "info");
        			} else {
        				inputVal++;
        			}
        			if (inputVal > minNum - 1) {
        				if (inputVal > inventory) {
        					$(this).addClass('disable');
        					$.message.alert("超过最大库存", "info");
        					return;
        				}
        				newPrice = prodPrice;
        				priceList.addClass("active");
        			} else {
        				newPrice = prodPrice;
        				priceList.removeClass("active");
        			}

        			setPrice(inputVal, newPrice);
        			input.val(inputVal);
                }
    		});

    		// 减少件数
    		down.on("click", function () {
                if ($(this).hasClass("disable")) {
                    return;
                } else {
        			var inputVal = input.val();
        			up.removeClass('disable');
        			if (inputVal <= minNum) {
        				$(this).addClass("disable");
        				return;
        			} else {
    	    			inputVal--;
    	    			if (inputVal >= minNum) {
    	    				newPrice = prodPrice;
    	    				priceList.addClass("active");
    	    			} else {
    	    				//newPrice = salePrice;
    	    				newPrice = prodPrice;
    	    				priceList.removeClass("active");
    	    			}
    	    			setPrice(inputVal, newPrice);
    	    			input.val(inputVal);
        			}
                }
    		});

    		// 输入件数
    		input.on("change", function () {
                if ($(this).siblings(".down").hasClass("disable") && $(this).siblings(".up").hasClass("disable")) {
                    return;
                } else {
        		    var reg = /^[1-9]\d*$/,
        		        inputVal = input.val();
        		    if (inputVal > inventory) {
        				$.message.alert("超过最大库存", "info");
        				inputVal = inventory;
        			}
        			if (isNaN(inputVal) || inputVal < minNum) {
        				inputVal = minNum;
        				down.addClass('disable');
        			} else {
        				down.removeClass('disable');
        			}
        			if(!reg.test(inputVal)){
        			    inputVal = 1;
        			}
        			if (inputVal >= minNum) {
        				if (inputVal > inventory) {
        					up.addClass('disable');
        					input.val(inventory);
        					$.message.alert("超过最大库存", "info");
        					return;
        				}
        				newPrice = prodPrice;
        				priceList.addClass("active");
        			} else {
        				//newPrice = salePrice;
        				newPrice = prodPrice;
        				priceList.removeClass("active");
        			}
        			setPrice(inputVal, newPrice);
        			input.val(inputVal);
                }
    		});

    		// 根据调整后的数量 判断价钱
    		function setPrice(num, price) {
    			$(".change-num").html(num);
    			$(".change-price").html((num * price).toFixed(2));
    		}

			// 加入进货单
			addCart.on("click", function(event) {
                if(!$(this).hasClass("bg-gray")) {
    				var inventoryNum = $(".inventory i").html();
    				// 库存大于0件才可以加入购物车
    				if (inventoryNum > 0) {
    				    addCart.attr("disabled", true);  //设置按钮为disabled
    				    addCart.addClass("bg-gray");
    					addToCart(event.clientX, event.clientY);
    				} else {
    					$.message.alert("库存不足！", "info");
    				}
                } else {
                    return;
                }
			});

			// 获取侧边栏购物车图标位置
			var iconCart = $(".top .cart"),
			    doc = $(document),
			    offset = iconCart.offset(),
			    left = offset.left - doc.scrollLeft(),
			    top = offset.top - doc.scrollTop();
			$(".top .num-tip").css({"left": left+90, "top": top-40});
			// 获取位置
			function getPosition(){
			    offset = iconCart.offset();
                left = offset.left - doc.scrollLeft();
                top = offset.top - doc.scrollTop();
                $(".top .num-tip").css({"left": left+90, "top": top-40});
			}

			// 窗口大小改变时重新获取位置
            $(window).resize(function(){
                getPosition();
            });

            // 滚动滚动条时重新获取位置
            $(window).scroll(function(){
                getPosition();
            });

			function addToCart(x, y) {
				var cartNum = $(".pro-detail .skip .num").val();
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
						if (data.code == 200) {  //请求成功，将商品抛入购物车
                            var img = $(".pro-intro").find(".view img").eq(0).attr('src');  //获取当前点击图片链接
                            var flyer = $('<img class="flyer-img" src="' + img + '">');     //抛物体对象
                            flyer.fly({
                                start: {
                                    left: x, //抛物体起点横坐标
                                    top: y   //抛物体起点纵坐标
                                },
                                end: {
                                    left: left+50, //抛物体终点横坐标
                                    top: top,   //抛物体终点纵坐标
                                },
                                onEnd: function() {
                                    $(".top .num-tip").show().animate({top: top + 4}, 300).fadeOut(500, function(){
                                        $(this).css("top", top-40);
                                    });               //成功加入购物车动画效果
                                    setTimeout(function(){
                                       addCart.attr("disabled", false);
                                       addCart.removeClass("bg-gray");
                                    }, 800);          //动画效果完成，恢复按钮
                                    this.destory();   //销毁抛物体
                                }
                            });
							getSkuNum();
							getCartNum();
							//getCartListmini();
							//getReplenishList();
						} else if(data.code == 403){
						    $.message.alert("您尚未登录，请先登录", "info", function(){
						        window.location.href = "/login?redirectURL="+encodeURIComponent(url);
						    });
						} else if(data.code == 404){
						    $.message.alert("进货单中最多只能加入50件不同的商品", "info");
						}
					}
				});
			}

			function getSkuNum() {
				$.ajax({
					type : "GET",
					url: "/cart/checkSkuNum",
					async: false,
					contentType: "application/json",
					data : {
						skuId : skuID
					},
					success: function (data) {
					}
				});
			}
			//getSkuNum();

			// 店铺信息
			$(".pro-detail .store-service p").hover( function () {
				$(this).siblings("dl").show();
			}, function () {
				$(this).siblings("dl").hide();
			});

			// 收藏商品
            $(".collect a").on("click", function() {
                if(collectFlag == 200) {
                    return;
                } else if(collectFlag == 400){
                    $(this).addClass("active");
                    follow(proId);
                }else if(collectFlag == 403){
                    $.message.alert("您尚未登录，请先登录", "info", function(){
                        window.location.href = "/login?redirectURL="+encodeURIComponent(url);
                    });
                }
            });

            // 收藏接口
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
                            $.message.alert("收藏成功！", "success");
                        }
                    }
                });
            }

            // 取消收藏接口
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
                            $.message.alert("已取消收藏！", "success");
                        }
                    }
                });
            }

    	});
    </script>
</body>
</html>
</@compress>
</#escape>