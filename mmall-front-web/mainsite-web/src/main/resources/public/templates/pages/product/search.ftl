<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <#include "../../wrap/cartcommon.ftl">
    <@title type="catrgoryList" />
    <meta charset="utf-8"/>
    <@css/>
</head>
<body>
	<@fixedTop />
	<@topbar />
	<@top />
	<@mainCategory />
	<div class="content-bg">
		<div class="wrap clearfix">
			<div class="position">
				<#if categoryNav??>
					<#if categoryNav.id??>
						<span id="${categoryNav.id}">${categoryNav.name}</span>
						<i>></i>
						<a href="/list/${categoryNav.sub.id}">${categoryNav.sub.name}</a>
						<#if categoryNav.sub.sub??>
							<i>></i>
							<a href="/list/${categoryNav.sub.sub.id}">${categoryNav.sub.sub.name}</a>
						</#if>
					</#if>
				</#if>
			</div>
			<div class="brand-fillter">
				<ul></ul>
			</div>
		</div>
		<div class="second-category wrap clearfix">
			<table class="filter">
				<#if parameterList??>
					<#list parameterList as item>
						<tr class="item1">
							<th width='7%'>${item.parameterName}</dt>
							<td>
								<div class='item-box clearfix'>
									<i class='action'>展开</i>
									<#list item.optionList as list>
										<em data-title="${list.paramOption}">${list.paramOption}</em>
									</#list>
								</td>
							</td>
						</tr>
					</#list>
				</#if>
				<!-- 属性 -->
				<#if specificationList??>
					<#list specificationList as item>
						<tr class='item1'>
							<th width='7%'>${item.specificationName}</th>
							<td>
								<div class='item-box clearfix'>
									<i class='action'>展开</i>
									<#list item.speciOptionList as list>
										<span data-title="${list.speciOption}">${list.speciOption}</span>
									</#list>
								</div>
							</td>
						</tr>
					</#list>
				</#if>
				<!-- 规则 -->
				<#if brandList??>
					<tr class="brand-item">
						<th width='7%'>品牌</th>
						<#list brandList as item>
							<#if item.index="all">
								<td>
									<div class='item-box clearfix'>
										<i class='action'>展开</i>
										<#list item.list as list>
											<span data-brandId="${list.brandId}" data-brandNameHead="${list.brandNameHead!''}" data-title="${list.brandNameAuto}">
												<b>
													<em>${list.brandNameAuto}</em>
													<i>x</i>
												</b>
											</span>
										</#list>
									</div>
								</td>
							</#if>
						</#list>
					</div>
				</#if>
				<!-- 品牌列表 -->
			</table>
			<div class="content">
				<div class="clearfix sort">
					<ul>
						<li class="item2 curr">销量<i></i></li>
						<li class="item1">上架时间<i></i></li>
						<!--
						<li class="item3">回头率</li>
						<li class="item4">价格</li>
						-->
					</ul>
				</div>
				<div class="clearfix pro-list">
					<ul></ul>
				</div>
			</div>
			<div class="down-loading-page">
				<div class="num">
					<span class="curr"></span>/<span class="max"></span>
				</div>
			</div>
			<div class="down-loading">
			</div>
		</div>
	</div>
	<@footer />
	<@copyright />
	<@cityChange />
	<@fixedSide />
    <@js />
    <script type="text/javascript">
    	$(function () {

    		var url 				= window.location.href,
				key1 				= url.indexOf("search/"),
				urlLen 				= url.length,
				box 				= $(".pro-list ul"),
				searchVal 			= url.substring(key1 + 7, urlLen);
				obj 				= {},
				categoryContentId 	= "",
				categoryNormalId  	= "",
				pageSize 			= 20,
				currentPage 		= 1,
				isPage 				= 1,
				brandIds 			= "",
				sortColumn			= "productSaleNum",
				isAsc				= "0",
				total 				= "",
				index 				= "",
				flag  				= true;


				searchVal = decodeURIComponent(decodeURIComponent(searchVal));

			$(".top .search input").val(searchVal);
			$(".fixed-top .search .sear-input").val(searchVal);

			// 接口参数
			function createObj() {
				obj = {
					categoryContentId 	: categoryContentId,
					categoryNormalId 	: categoryNormalId,
					pageSize 			: pageSize,
					currentPage			: Number(currentPage),
					isPage 				: isPage,
					brandIds			: brandIds,
					searchVal			: searchVal,
					sortColumn			: sortColumn,
					isAsc				: isAsc
				};
				// 去除空字段
				for (var i in obj) {
					if (obj[i] === "") {
						delete obj[i];
					}
				}
				return obj;
			}

			// 获取产品列表接口
			function getProCategory() {
				createObj();
				//console.log(obj);
				if (flag) {
					$.ajax({
						type : "GET",
						url: "/rest/product/list",
						async: false,
						cache: false,
						dataType: "json",
						data : {
							categoryContentId 	: obj.categoryContentId,
							categoryNormalId  	: obj.categoryNormalId,
							pageSize 			: obj.pageSize,
							currentPage  		: obj.currentPage,
							isPage 				: obj.isPage,
							brandIds 			: obj.brandIds,
							searchValue			: obj.searchVal,
							sortColumn			: obj.sortColumn,
							isAsc				: obj.isAsc
						},
						beforeSend: function () {
	                        flag = false;
	                        if (obj.currentPage != 1) {
                                $(".down-loading").html("<p class='loading'>正在加载中......</p>");
                            }
	                    },
						success: function (data) {
							if (data.code == 200) {
								createPro(data);
								$(".down-loading").html("");
							}
						}
					});
				}
			}
			getProCategory();

			// 创建单个商品dom
			function createPro(data) {
				//console.log(data);
				var proDom = "",
					list = data.result.list,
					nTime = Date.parse(new Date());

				if (list != null) {
					total = Math.ceil(data.result.total / pageSize);

					for ( var i = 0; i < list.length; i++) {

						if (list[i].skuLimitConfigVO != null) {
							var sTime = list[i].skuLimitConfigVO.limitStartTime,
								eTime = list[i].skuLimitConfigVO.limitEndTime,
								allowBuyNum = list[i].skuLimitConfigVO.allowBuyNum;
						}

						proDom += "<li>"+
									"<div class='pro-box' data-skuid='"+ list[i].skuId +"' data-skunum='"+ list[i].skuNum +"' data-saleNum='"+ list[i].productSaleNum +"' data-minNum='"+ list[i].priceList[0].prodMinNumber +"' data-allowBuyNum='"+ allowBuyNum +"'>"+
										"<span class='pic'>"+
											"<a href='/product/detail?skuId="+ list[i].skuId +"' target='_blank'>"+
												"<img src='"+ list[i].picList[0].picPath + thumbnailParam('70', '208', '208')+"' />"+
											"</a>";
						if (list[i].skuNum < list[i].priceList[0].prodMinNumber) {
							proDom += "<em class='sold-out-icon'></em>";
						}
						proDom += "</span>"+
							"<a href='/product/detail?skuId="+ list[i].skuId +"' class='tit' target='_blank' title='"+ list[i].productName +"'>"+ list[i].productName +"</a>"+
							"<span class='des' title='"+ list[i].productTitle +"'>"+ list[i].productTitle +"</span>"+
							"<span class='price'><em>¥</em>"+ list[i].priceList[0].prodPrice +"</span>"+
							"<a href='/store/"+ list[i].supplierId  +"/' class='store-name' target='_blank'>"+ list[i].storeName +"</a>"+
							"<p class='tag'><span>标签1</span><span>标签2</span><span>标签3</span></p>";

						if (list[i].skuLimitConfigVO != null) {
							if (nTime < sTime) {
								proDom += "<div class='action sold-out-action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='0' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>限购即将开始</div>"+
								"</div>";
							} else if (nTime > eTime) {
								proDom += "<div class='action sold-out-action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='0' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>限购已结束</div>"+
								"</div>";
							} else {
								if (list[i].skuNum < list[i].priceList[0].prodMinNumber) {
									proDom += "<div class='action sold-out-action'>"+
										"<div class='jump'>"+
											"<span class='minus'>-</span>"+
											"<input class='num' value='0' />"+
											"<span class='add'>+</span>"+
										"</div>"+
										"<div class='add-cart'>卖完了</div>"+
									"</div>";
								} else {
									proDom += "<div class='action'>"+
										"<div class='jump'>"+
											"<span class='minus'>-</span>"+
											"<input class='num' value='"+ list[i].priceList[0].prodMinNumber +"' />"+
											"<span class='add'>+</span>"+
										"</div>"+
										"<div class='add-cart'>加入进货单</div>"+
									"</div>";
								}
							}
						} else {
							if (list[i].skuNum < list[i].priceList[0].prodMinNumber) {
								proDom += "<div class='action sold-out-action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='0' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>卖完了</div>"+
								"</div>";
							} else {
								proDom += "<div class='action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='"+ list[i].priceList[0].prodMinNumber +"' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>加入进货单</div>"+
								"</div>";
							}
						}
						proDom += "</div></li>";
					}
					currentPage++;
				} else {
					total = null;
                    proDom += "<div class='null-product'>"+
                        "<i></i>"+
                        "<p>很遗憾，您搜索的“<span>"+ searchVal +"</span>”暂无商品！</p>"+
                    "</div>";
				}
				flag = true;
				box.append(proDom);
				$(".down-loading-page").hide();
			}

			box.on("mousemove", ".pro-box", function () {
				$(this).addClass("active").siblings().removeClass("active");
				if (!$(this).find(".action").is(":animated")) {
					$(this).find(".action").animate({
						bottom: -1 + "px",
						opacity: 1,
						zIndex : 2
					}, 200);
				}
			});

			box.on("mouseleave", ".pro-box", function () {
				$(this).removeClass("active");
				$(this).find(".action").animate({
					bottom: 40 + "px",
					opacity: 0,
					zIndex : -1
				}, 200);
			});

			box.on("click", ".add", function () {
				if ($(this).closest(".action").hasClass("sold-out-action")) {
					return;
				} else {
					var numInput = $(this).siblings(".num"),
						numVal = numInput.val(),
						maxNum = $(this).closest(".pro-box").data("skunum");

					if (numVal < maxNum) {
						numVal++;
						numInput.val(numVal);
					} else {
						$.message.alert("库存不足！", "info");
						numInput.val(maxNum);
					}
				}
			});

			box.on("click", ".minus", function () {
				if ($(this).closest(".action").hasClass("sold-out-action")) {
					return;
				} else {
					var numInput = $(this).siblings(".num"),
						numVal = numInput.val(),
						minNum 	= $(this).closest(".pro-box").data("minnum");
					if (numVal <= 1 || numVal <= minNum) {
						return;
					} else {
						numVal--;
						numInput.val(numVal);
					}
				}
			});

			box.on("change", ".num", function () {
				if ($(this).closest(".action").hasClass("sold-out-action")) {
					return;
				} else {
					var numInput = $(this),
						numVal = numInput.val(),
						maxNum = $(this).closest(".pro-box").data("skunum"),
						minNum 	= $(this).closest(".pro-box").data("minnum");

					if (numVal < 1 || isNaN(numVal) || numVal < minNum) {
						numVal = minNum;
						numInput.val(numVal);
					}
					if (numVal <= maxNum) {
						numInput.val(numVal);
					} else {
						$.message.alert("库存不足！", "info");
						numInput.val(maxNum);
					}
				}
			});

			// 加入进货单
			box.on("click", ".add-cart", function () {
				var skuId = $(this).closest(".pro-box").data("skuid"),
					numVal = $(this).siblings(".jump").find(".num").val(),
					maxNum = $(this).closest(".pro-box").data("skunum");

				if (maxNum > 0) {
					addToCart(skuId, numVal);
				} else {
					$.message.alert("库存不足！", "info");
				}
			});

			function addToCart(id, num) {
				$.ajax({
					type : "POST",
					url: "/cart/addToCart",
					async: false,
					contentType: "application/json",
					data : JSON.stringify({
						"skuId" : id,
						"diff" : num
					}),
					success: function (data) {
						if (data.code == 200) {
							$.message.alert("加入进货单成功！", "success");
							getCartNum();
                        } else if(data.code == 403){
						    $.message.alert("您尚未登录，请先登录！", "info", function(){
						        window.location.href = "/login?redirectURL="+encodeURIComponent(url);
						    });
						} else if(data.code == 404){
						    $.message.alert("进货单中最多只能加入50件不同的商品！", "info");
						}
					}
				});
			}

			// 分类筛选列表
			$(".filter .item-box").each( function () {
				var h = $(this).height();
				if (h > 72) {
					$(this).addClass("active");
					$(this).find(".action").show();
				}
			});

			/* 展开 隐藏 */
			$(".item-box").on("click", ".action", function () {
				if ($(this).hasClass("active")) {
					$(this).closest(".item-box").addClass("active");
					$(this).removeClass("active").html("展开");
				} else {
					$(this).closest(".item-box").removeClass("active");
					$(this).addClass("active").html("收起");
				}
			});

			/* 品牌分类 */
			$(".item-box").on("click", "b", function () {
				$(this).closest("span").toggleClass("active");
				createLabel($(this));
				box.empty();
				getProCategory();
			});

			function createLabel(_this) {
				var brandIdArr = [],
					brandNames = "",
					title = _this.closest("tr").find("th").html();
				brandIds = ""
				currentPage = 1;

				$(".brand-item span.active").each( function () {
					var brandid = $(this).data("brandid"),
						brandName = $(this).data("title");
					brandIds += brandid + ",";
					brandNames += brandName + ",";
					brandIdArr.push(brandid);
				});
				brandIds = brandIds.substring(0, brandIds.length - 1);
				brandNames = brandNames.substring(0, brandNames.length - 1);
			}

			/* 删除品牌筛选 */
			$(".brand-fillter").on("click", "li", function () {
				$(".brand-fillter ul").empty();
				$(".item-box").find("span").removeClass();
				brandIds = "";
				box.empty();
				getProCategory();
			});

			/* 下拉刷新 */
			var scrollFunc = function (e) {
		        e = e || window.event;
		        if (e.wheelDelta) {
		            if (e.wheelDelta < 0) {
		                pullDown()
		            }
		        } else if (e.detail) {
		            if (e.detail< 0) {
		                pullDown();
		            }
		        }
		    }
		    if (document.addEventListener) {
		        document.addEventListener('DOMMouseScroll', scrollFunc, false);
		    }
		    window.onmousewheel = document.onmousewheel = scrollFunc;

			function pullDown() {
				$(window).scroll(function () {
	                if (flag) {
	                    var scrollTop = $(this).scrollTop(),
	                        scrollHeight = $(document).height(),
	                        boxHeight = $(".pro-list li").height(),
	                        windowHeight = $(this).height();

	                    if (scrollTop >= scrollHeight - windowHeight - boxHeight) {
	                        if (total != null) {
	                        	//console.log(total);
	                            if (currentPage <= total) {
	                                getProCategory();
	                                $(".down-loading-page").show().find(".curr").html(currentPage - 1);
	                                $(".down-loading-page").show().find(".max").html(total);
	                            } else {
	                                $(".down-loading").html("<p class='ending'>没有更多....</p>");
	                                $(".down-loading-page").hide();
	                            }
	                        } else {
	                            return;
	                        }
	                    }
	                }
	            });
			}

			// 排列条件事件
			$(".sort li").on("click", function () {
				var _this = $(this);
				_this.addClass("curr").siblings().removeClass("curr active");

				// 销量正序倒序
				if (_this.hasClass("item2")){
					_this.toggleClass("active");
					sortColumn = "productSaleNum";
					isAsc = "0";
				}

				// 综合排序
				if (_this.hasClass("item1")){
					_this.toggleClass("active");
					sortColumn = "updateTime";
					if (isAsc == "1") {
						isAsc = "0";
					} else {
						isAsc = "1";
					}
				}

				currentPage = 1;
				box.empty();
				getProCategory();
			});
    	});
    </script>
</body>
</html>
</@compress>
</#escape>