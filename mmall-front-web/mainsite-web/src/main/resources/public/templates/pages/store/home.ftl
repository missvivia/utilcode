<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/front.ftl">
    <title>新云联百万店-${businesser.storeName}</title>
    <@css/>
</head>
<body>
	<@fixedTop />
    <@topbar />
    <@top />
	<div class="store-banner">
		<div class="store-name wrap">
			${businesser.storeName}
		</div>
	</div>
	<#--
	<div class="pro-menu clearfix">
		<div class="wrap">
			<div class="pro-home"><a href="/store/${businesser.id}/">首页</a></div>
			<ul class="l list">
				<#if category??>
					<#list category as first>
						<#if first.isvisible=true>
							<li><a href="javascript:void(0);" data-cateid='${first.id}' data-level='${first.level}' data-rootId='${first.rootId}'>${first.name}</a></li>
						</#if>
					</#list>
				</#if>
			</ul>
		</div>
	</div>
	-->
	<div class="pro-bar clearfix">
		<div class="search l">
			<input type="text" class="input-clear search-input" placeholder="请输入产品名" maxlength="32"/>

			价格：
			<input type="text" class="text-input min-price" />
			到
			<input type="text" class="text-input max-price" />
			<input type="button" value="搜索" class="btn-input" />
		</div>
		<div class="main-category r">
		</div>
	</div>
	<div class="all-product wrap clearfix">
		<div class="side l">
			<#if category??>
				<div class="menu">
					<a href="/store/${businesser.id}/" class="first">首页</a>
					<#list category as first>
						<#if first.isvisible=true>
							<a class="first" href="javascript:void(0);" data-rootId="${first.rootId}" data-cateid='${first.id}' data-level='${first.level}'>${first.name}</a>
							<#list first.subCategoryContentDTOs as secend>
								<#if secend.isvisible=true>
									<a href="javascript:void(0);" data-cateid='${secend.id}'  data-rootId='${secend.rootId}' data-level='${secend.level}'>${secend.name}</a>
								</#if>
							</#list>
						</#if>
					</#list>
				</div>
			</#if>
		</div>
		<div class="main r store-pro-list">
			<div class="brand">
				<div class="content">
					<table width="100%">
						<tr class="brand-item">
							<th width='7%'>品牌</th>
							<td>
								<div class='item-box clearfix'>
								</div>
								<i class="action">展开</i>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="sort">
				<ul>
					<li class="item2 curr">销量<i></i></li>
					<li class="item4">价格<i></i></li>
					<li class="item1">上架时间<i></i></li>
				</ul>
			</div>
			<div class="pro-list clearfix">
				<ul></ul>
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

    		var url 			= location.href,
    			locationHash 	= window.location.hash,
    			hashId 			= locationHash.substr(1, locationHash.length);
				soteListBox 	= $(".pro-list ul"),
				obj 			= {},
				storeID 		= "",
				pageLimit 		= 8,
				pageIndex 		= 0,
				productName 	= "",
				lowCategoryId 	= "",
				level 			= "",
				sprice       	= "",
				orderColumn 	= "saleNum",
				eprice        	= "",
				isAsc 			= false,
				brandIdList		= "",
				rootId 			= "",
				total 			= "",
				flag  			= true;

			// 从url中取得店铺id,hashid代表所属分类id
			var key1 = url.lastIndexOf("/", url.lastIndexOf("/") - 1) + 1;
				key2 = url.lastIndexOf("/");
			if (locationHash == "") {
				if (key2 + 1 == url.length) {
					storeID = url.substring(key1, key2);
				} else {
					storeID = url.substring(key2 + 1, url.length);
				}
			} else {
				storeID = url.substring(key1, key2);
			}

			// 导航分类菜单
			$(".all-category").on("mousemove", function () {
				$(".sec-category").show();
			});
			$(".all-category").on("mouseleave", function () {
				$(".sec-category").hide();
			});

			$(".sec-category span").on("mousemove", function () {
				$(this).next("div").show();
			});

			$(".sec-category .thir-wrap").on("mouseleave", function () {
                $(this).find(".thir-category").hide();
            });

			// 搜索按钮事件
			$(".btn-input").on("click", function () {
				var _this = $(this);
				pageIndex = 0;
				soteListBox.empty();
				getStoreList();
			});

            $(document).keyup(function(e){
                 var curKey = e.which;
                 if(curKey==13){
                    $(".btn-input").click();
                 }
            });

			// 导航菜单事件
			$(".list a").on("click", function () {
				var _this = $(this),
					hash = _this.data("cateid");
					rootId = _this.data("rootid");

				$(".menu a").removeClass("active");
				hashId = _this.data("cateid");
				window.location.hash = hash;
				pageIndex = 0;
				brandIdList = "";
				soteListBox.empty();
				getStoreList();
				getBrandList(hash);
			});

			$(".menu a").on("click", function () {
				var _this = $(this),
					hash = _this.data("cateid");
					rootId = _this.data("rootid");

				_this.addClass("active").siblings().removeClass("active");
				hashId = _this.data("cateid");
				window.location.hash = hash;
				pageIndex = 0;
				brandIdList = "";
				soteListBox.empty();
				getStoreList();
				getBrandList(hash);
			});

			// 获取品牌
			function getBrandList(contentCategoryId) {
				$.ajax({
						cache: false,
						type : "GET",
						dataType : "json",
						url : "/store/getBrandsByContentCategoryId/",
						data : {
							businessId : storeID,
							rootId     : rootId,
							contentCategoryId : contentCategoryId
						},
						success: function (data) {
							if (data.code == 200) {
								createBrandList(data.result[0]);
							} else {
								return;
							}
						}
					});
			}
			getBrandList();

			function createBrandList(data) {
				var list = data.list,
					dom = "",
					box = $(".item-box");
				for (var i = 0; i < list.length;i ++ ) {
					dom += "<span data-brandid='"+ list[i].brandId +"'>"+
						"<b>"+
							"<em>"+ list[i].brandNameZh +"</em>"+
							"<i>x</i>"+
						"</b>"+
						"</span>";
				}
				box.empty();
				box.append(dom);
				sideBrand();
			}

			// 排列条件事件
			$(".sort li").on("click", function () {
				var _this = $(this);
				_this.addClass("curr").siblings().removeClass("curr active");

				// 价钱正序倒序
				if (_this.hasClass("item4")){
					_this.toggleClass("active");
					orderColumn = "price";
					if (isAsc) {
						isAsc = false;
					} else {
						isAsc = true;
					}
				}

				// 销量正序倒序
				if (_this.hasClass("item2")){
					_this.toggleClass("active");
					orderColumn = "saleNum";
					isAsc = false;
				}

				// 综合排序
				if (_this.hasClass("item1")){
					_this.toggleClass("active");
					orderColumn = "updateTime";
					if (isAsc) {
						isAsc = false;
					} else {
						isAsc = true;
					}
				}

				pageIndex = 0;
				soteListBox.empty();
				getStoreList();
			});

			$(".min-price").on("change", function () {
				var val = $(this).val();
				if (isNaN(val) || val < 0) {
					val = "";
					$(".min-price").val("");
				}
			});

			$(".max-price").on("change", function () {
				var val = $(this).val();
				if (isNaN(val) || val < 0) {
					val = "";
					$(".max-price").val("");
				}
			});

			// 接口参数
			function createObj() {

				productName = $.trim($(".search-input").val()),
				lowCategoryId = hashId,
				sprice = $.trim($(".min-price").val()),
				eprice = $.trim($(".max-price").val());

				obj = {
					businessId 		: storeID,
					limit      		: pageLimit,
					offset     		: pageIndex * pageLimit,
					productName 	: productName,
					lowCategoryId 	: lowCategoryId,
					level			: level,
					sprice       	: sprice,
					eprice        	: eprice,
					orderColumn  	: orderColumn,
					asc        		: isAsc,
					brandIdList		: brandIdList,
					rootId 			: rootId
				};

				// 去除空字段
				for (var i in obj) {
					if (obj[i] === "") {
						delete obj[i];
					}
				}
				return obj;
			}

			// 获取店铺数据列表接口
			function getStoreList() {
				createObj();

				if(flag) {
					$.ajax({
						cache: false,
						type : "GET",
						dataType : "json",
						url : "/store/searchProduct/",
						data : {
							businessId 		: obj.businessId,
							limit      		: obj.limit,
							offset     		: obj.offset,
							productName 	: obj.productName,
							lowCategoryId 	: obj.lowCategoryId,
							level			: obj.level,
							sprice       	: obj.sprice,
							eprice        	: obj.eprice,
							orderColumn  	: obj.orderColumn,
							asc        		: obj.asc,
							brandIdList		: obj.brandIdList,
							rootId   		: obj.rootId
						},
						beforeSend: function () {
	                        flag = false;
	                        if (pageIndex != 0) {
	                            $(".down-loading").html("<p class='loading'>正在加载中......</p>");
	                        }
	                    },
						success: function (data) {
							if (data.code == 200) {
								crerteStoreList(data);
								$(".down-loading").html("");
							} else {
								return;
							}
						}
					});
				}
			}
			getStoreList();

			// 创建数据列表
			function crerteStoreList(data) {
				//console.log(data);
				var storeDom = "",
					list = data.result.list,
					nTime = Date.parse(new Date());

				if (list != null) {
					total = Math.ceil(data.result.total / pageLimit);
					for ( var i = 0; i < list.length; i++) {

						if (list[i].skuLimitConfigVO != null) {
							var sTime = list[i].skuLimitConfigVO.limitStartTime,
								eTime = list[i].skuLimitConfigVO.limitEndTime,
								allowBuyNum = list[i].skuLimitConfigVO.allowBuyNum;
						}

						storeDom += " <li><div class='pro-box' data-skuid='"+ list[i].skuId +"' data-skunum='"+ list[i].skuNum +"' data-saleNum='"+ list[i].productSaleNum +"' data-minNum='"+ list[i].priceList[0].prodMinNumber +"' data-allowBuyNum='"+ allowBuyNum +"'>"+
										"<div class='item'>"+
											"<span class='pic'>"+
												"<a href='/product/detail?skuId="+ list[i].skuId +"' target='_blank'>"+
													"<img src='"+ list[i].showPicPath + thumbnailParam('70', '208', '208')+"' />"+
												"</a>";
						if (list[i].skuNum < list[i].priceList[0].prodMinNumber) {
							storeDom += "<em class='sold-out-icon'></em>";
						}
						storeDom += "</span>"+
							"<a href='/product/detail?skuId="+ list[i].skuId +"' class='tit' target='_blank' title='"+list[i].productName+"'>"+ list[i].productName +"</a>"+
							"<span class='des' title='"+list[i].productTitle+"'>"+ list[i].productTitle +"</span>"+
							"<span class='price'><i>￥</i>"+ list[i].priceList[0].prodPrice  +"</span>"+
							"<a href='"+ list[i].storeUrl +"' class='store-name'>${businesser.storeName}</a>"+
							"<p class='tag'><span>标签1</span><span>标签2</span><span>标签3</span></p>";
						if (list[i].skuLimitConfigVO != null) {
							if (nTime < sTime) {
								storeDom += "<div class='action sold-out-action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='0' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>限购即将开始</div>"+
								"</div>";
							} else if (nTime > eTime) {
								storeDom += "<div class='action sold-out-action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='0' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>限购已结束</div>"+
								"</div>";
							} else {
								if (list[i].skuNum < list[i].priceList[0].prodMinNumber) {
									storeDom += "<div class='action sold-out-action'>"+
										"<div class='jump'>"+
											"<span class='minus'>-</span>"+
											"<input class='num' value='0' />"+
											"<span class='add'>+</span>"+
										"</div>"+
										"<div class='add-cart'>卖完了</div>"+
									"</div>";
								} else {
									storeDom += "<div class='action'>"+
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
								storeDom += "<div class='action sold-out-action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='0' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>卖完了</div>"+
								"</div>";
							} else {
								storeDom += "<div class='action'>"+
									"<div class='jump'>"+
										"<span class='minus'>-</span>"+
										"<input class='num' value='"+ list[i].priceList[0].prodMinNumber +"' />"+
										"<span class='add'>+</span>"+
									"</div>"+
									"<div class='add-cart'>加入进货单</div>"+
								"</div>";
							}
						}
						storeDom += "</div></li>";
					}
					pageIndex++;
				} else {
					total = null;
                    storeDom += "<div class='null-product'>"+
                        "<i></i>"+
                        "<p>很遗憾，该筛选下暂无商品！</p>"+
                    "</div>";
				}

				flag = true;
				soteListBox.append(storeDom);
				$(".down-loading-page").hide();
			}

			soteListBox.on("mousemove", ".pro-box", function () {
				$(this).addClass("active").siblings().removeClass("active");
				if (!$(this).find(".action").is(":animated")) {
					$(this).find(".action").animate({
						bottom: -1 + "px",
						opacity: 1,
						zIndex : 2
					}, 200);
				}
			});

			soteListBox.on("mouseleave", ".pro-box", function () {
				$(this).removeClass("active");
				$(this).find(".action").animate({
					bottom: 40 + "px",
					opacity: 0,
					zIndex : -1
				}, 200);
			});

			soteListBox.on("click", ".add", function () {
				if ($(this).closest(".action").hasClass("sold-out-action")) {
					return;
				} else {
					var numInput = $(this).siblings(".num"),
						numVal 	= numInput.val(),
						maxNum 	= $(this).closest(".pro-box").data("skunum");

					if (numVal < maxNum) {
						numVal++;
						numInput.val(numVal);
					} else {
						$.message.alert("库存不足！", "info");
						numInput.val(maxNum);
					}
				}
			});

			soteListBox.on("click", ".minus", function () {
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

			soteListBox.on("change", ".num", function () {
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
			soteListBox.on("click", ".add-cart", function () {
				if ($(this).closest(".action").hasClass("sold-out-action")) {
					return;
				} else {
					var skuId = $(this).closest(".pro-box").data("skuid"),
						numVal = $(this).siblings(".jump").find(".num").val(),
						maxNum = $(this).closest(".pro-box").data("skunum");

					if (maxNum > 0) {
						addToCart(skuId, numVal);
					} else {
						$.message.alert("库存不足！", "info");
					}
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
	                            if (pageIndex + 1 <= total) {
	                                getStoreList();
	                                $(".down-loading-page").show().find(".curr").html(pageIndex);
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

			/* 展开 隐藏 */
			function sideBrand() {
				$(".item-box").removeClass("active");
				if ($(".item-box").height() > 72) {
					$(".item-box").addClass("active");
					$(".item-box").closest("td").find(".action").show();
				} else {
					$(".item-box").closest("td").find(".action").hide();
				}
			}

			$(".brand-item").on("click", ".action", function () {
				if ($(this).hasClass("active")) {
					$(this).closest("td").find(".item-box").addClass("active");
					$(this).removeClass("active").html("展开");
				} else {
					$(this).closest("td").find(".item-box").removeClass("active");
					$(this).addClass("active").html("收起");
				}
			});

			/* 品牌分类 */
			$(".item-box").on("click", "b", function () {
				$(this).closest("span").toggleClass("active");
				createLabel($(this));
				soteListBox.empty();
				getStoreList();
			});

			function createLabel(_this) {
				var brandIdArr = [],
					brandNames = "",
					title = _this.closest("tr").find("th").html();
				brandIdList = "";
				pageIndex = 0;

				$(".brand-item span.active").each( function () {
					var brandid = $(this).data("brandid"),
						brandName = $(this).data("title");
					brandIdList += brandid + ",";
					brandNames += brandName + ",";
					brandIdArr.push(brandid);
				});
				brandIdList = brandIdList.substring(0, brandIdList.length - 1);
				brandNames = brandNames.substring(0, brandNames.length - 1);
			}
    	});
    </script>
</body>
</html>
</@compress>
</#escape>
