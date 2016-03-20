<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <@title type="index" />
    <meta charset="utf-8"/>
  	<link rel="shortcut icon" href="/favicon.ico?v=2" />
    <@css/>
</head>
<body>
	<@fixedTop />
	<@topbar />
	<div class="top-ads"></div>
	<@top />
	<div class="main-category wrap">
		<div class="category-item" id='add-category'> 
			<dl class="active">
				<dt>
					全部分类<i></i>
				</dt>
				<#if mainsiteIndexVO??>
					<#list mainsiteIndexVO.mainsiteStoreVOs as item>
						<dd><a href="${item.storeUrl}">${item.storeName}</a></dd>
					</#list>
				</#if>
			</dl>
		</div>
		<div class='home-category'>
			<#include "../../category/category.html">
		</div>
	</div>
	<div class="bg-french-gray ">
		<div class="banner">
			<div class="pic">
				<ul>
				</ul>
			</div>
			<div class="btn round">
				<ul>
				</ul>
			</div>
		</div>
		<div class="wrap clearfix">
			<div class="new-pro">
				<i>新品上架</i>
				<div class="box"></div>
			</div>
			<div class="hot-pro">
				<i>热销商品</i>
				<ul></ul>
			</div>
		</div>
		<div class="wrap clearfix brand-pro">
			<h2>品牌专区</h2>
			<div class="bg"></div>
			<ul></ul>
		</div>
		<#--
		<div class="wrap clearfix module" id='itme1'>
			<div class='title clearfix'></div>
			<div class="list">
			</div>
		</div>
		<div class="wrap clearfix module" id='itme2'>
			<div class='title clearfix'></div>
			<div class="list">
			</div>
		</div>
		<div class="wrap clearfix module" id='itme3'>
			<div class='title clearfix'></div>
			<div class="list">
			</div>
		</div>
		<div class="wrap clearfix module" id='itme4'>
			<div class='title clearfix'></div>
			<div class="list">
			</div>
		</div>
		<div class="wrap clearfix module" id='itme5'>
			<div class='title clearfix'></div>
			<div class="list">
			</div>
		</div>
		-->
		<#if mainsiteIndexVO??>
			<#list mainsiteIndexVO.mainsiteStoreVOs as item>
				<div class="wrap clearfix module">
					<div class="title clearfix">
						<i>${item_index + 1}楼</i>
						<span>${item.storeName}</span>
						<a href="${item.storeUrl}" target="_blank" class="more">更多&gt;</a>
					</div>
					<div class="list">
						<ul>
							<#list item.mainsiteProductVOs as pro>
								<#if pro_index < 5>
									<li>
										<div class="pro-box" data-skuid="${pro.skuId}" data-skunum="99999">
											<span class="pic">
												<a href="/product/detail?skuId=${pro.skuId}" target="_blank">
													<img src="${pro.showPicPath}">
												</a>
											</span>
											<a href="/product/detail?skuId=${pro.skuId}" class="tit" target="_blank" title="${pro.productName}">${pro.productName}</a>
											<span class="des" title="${pro.productTitle}">${pro.productTitle}</span>
											<span class="price"><em>¥</em>${pro.productPriceVOs[0].prodPrice}</span>
											<a href="${item.storeUrl}" class="store-name">${item.storeName}</a>
											<p class="tag"></p>
											<div class="action">
												<div class="jump">
													<span class="minus">-</span>
													<input class="num" value="1">
													<span class="add">+</span>
												</div>
												<div class="add-cart">加入进货单</div>
											</div>
										</div>
									</li>
								</#if>
							</#list>
						</ul>
					</div>
				</div>
			</#list>
		</#if>
	</div>
	<@footer />
	<@copyright />
	<@fixedSide />
	<@fixedSideOrder />
	<@fixedSideReplenish />
	<@cityChange />
    <@js />
    <script type="text/javascript">
    	$(function () {
    		
			// 首页模版数据
			//-----start repalce----
			var obj = {
				topAds : {
					url : [{0}], //0
					link : [{0}] //1
				},
				notice : {
					name : [
						{0}   //2
					],
					link : [
						{0}  //3
					]
				},
				category: {
					name: [
						{0} //4
					],
					link: [
						{0} //5
					]
				},
				banner : {
					url : [
						{0} //6
					],
					link : [
						{0} //7
					]
				},
				newPro : {
					url : [{0}], //8
					skuId : [{0}], //9
					name : [{0}], //10
					title : [{0}], //11
					storeName : [{0}], //12
					storeLink : [{0}], //13
					price : [{0}], //14
					tag : [
						{0}  //15
					]
				},
				hotPro : {
					url : [
						{0}  //16
					],
					skuId : [
						{0} //17
					],
					name : [
						{0} //18
					],
					title : [
						{0} //19
					],
					storeName : [
						{0}  //20
					],
					storeLink : [
						{0} //21
					],
					price : [
						{0} //22
					],
					tag : [
						{0} //23
					]
				},
				brandPro : {
					bakground: [{0}], //24
					list : {
						url : [
							{0} //25
						],
						skuId : [
							{0} //26
						],
						name : [
							{0} //27
						],
						title : [
							{0} //28
						],
						storeName : [
							{0} //29
						],
						storeLink : [
							{0} //30
						],
						price : [
							{0} //31
						],
						tag : [
							{0} //32
						]
					}
				},
				item1 : {
					titleName : [{0}],  //33
					ads : {
						name : [{0}], //34
						link  : [{0}] //35
					},
					titleLink : [{0}], //36
					background : [{0}], //37
					backgroundLink : [{0}], //38
					main : { 
						url : [
							{0} //39
						],
						skuId : [
							{0} //40
						],
						name : [
							{0} //41
						],
						title : [
							{0} //42
						],
						storeName : [
							{0} //43
						],
						storeLink : [
							{0} //44
						],
						price : [
							{0} //45
						],
						tag : [
							{0} //46
						]
					}
				},
				item2 : {
					titleName :[{0}], //47
					ads : {
						name : [{0}], //48
						link  : [{0}] //49
					},
					titleLink : [{0}], //50
					background : [{0}], //51
					backgroundLink : [{0}], //52
					main : {
						url : [
							{0} //53
						],
						skuId : [
							{0} //54
						],
						name : [
							{0} //55
						],
						title : [
							{0} //56
						],
						storeName : [
							{0} //57
						],
						storeLink : [
							{0} //58
						],
						price : [
							{0} //59
						],
						tag : [
							{0} //60
						]
					}
				},
				item3 : {
					titleName :[{0}], //61
					ads : {
						name : [{0}], //62
						link  : [{0}] //63
					},
					titleLink : [{0}], //64
					background : [{0}], //65
					backgroundLink : [{0}], //66
					main : {
						url : [
							{0} //67
						],
						skuId : [
							{0}  //68
						],
						name : [
							{0} //69
						],
						title : [
							{0} //70
						],
						storeName : [
							{0} //71
						],
						storeLink : [
							{0} //72
						],
						price : [
							{0} //73
						],
						tag : [
							{0} //74
						]
					}
				},
				item4 : {
					titleName :[{0}],  //75
					ads : {
						name : [{0}], //76
						link  : [{0}]  //77
					},
					titleLink : [{0}], //78
					background : [{0}], //79
					backgroundLink : [{0}], //80
					main : {
						url : [
							{0} //81
						],
						skuId : [
							{0} //82
						],
						name : [
							{0} //83
						],
						title : [
							{0} //84
						],
						storeName : [
							{0} //85
						],
						storeLink : [
							{0} //86
						],
						price : [
							{0} //87
						],
						tag : [
							{0} //88
						]
					}
				},
				item5 : {
					titleName :[{0}], //89
					ads : {
						name : [{0}], //90
						link  : [{0}] //91
					},
					titleLink : [{0}], //92
					background : [{0}], //93
					backgroundLink : [{0}], //94
					main : {
						url : [
							{0} //95
						],
						skuId : [
							{0} //96
						],
						name : [
							{0} //97
						],
						title : [
							{0} //98
						],
						storeName : [
							{0} //99
						],
						storeLink : [
							{0}  //100
						],
						price : [
							{0} //101
						],
						tag : [
							{0} //102
						]
					}
				}
			};
			//-----end repalce----
			
			// 创建头部广告
			function createTopAds(list) {
				//console.log(list);
				if (list.url.length != 0) {
					var dom = "<a href='"+ list.link +"' target='_blank'><img src='"+ list.url +"' />";
					$(".top-ads").show().append(dom);
				}
			};
			createTopAds(obj.topAds);
			// 创建公告
			function createNotice(list) {
				var dom = "";
				for (var i = 0; i < list.name.length; i++) {
					dom += "<li><a href='"+ list.link[i] +"' title='"+ list.name[i] +"' target='_blank'>"+ list.name[i] +"</a></li>"
				}
				$(".notice ul").append(dom);
			}
			createNotice(obj.notice);
			// 创建主分类旁分类
			function createCate(list) {
				var dom = '',
					len = list.name.length;
				if (len != 0) {
					for (var i = 0; i < len; i++) {
						dom += "<dd><a href='"+ list.link[i] +"'>"+ list.name[i] +"</a></dd>";
					}
				} else {
					dom = "";
				}
				$("#add-category dl").append(dom);
			}
			createCate(obj.category);
			// banner模块
			var bannerBox = $(".banner"),
				bannerBtn = bannerBox.find(".btn li"),
    			scrollBox = bannerBox.find(".pic ul"),
    			flag = 0;;
    		// 创建banner列表和按钮
			function createBanner(data) {
				var len = data.url.length,
					listDom = "",
					btnDom = "";
				for (var i = 0; i < len; i++) {
					listDom += "<li><a href='"+ data.link[i] +"' style='background-image: url("+ data.url[i] +");'></a></li>";
				}
				bannerBox.find(".pic ul").append(listDom);

				if (len >= 2) {
					for (var i = 0; i < len; i++) {
						btnDom += "<li></li>";
					}
				} else {
					btnDom = "";
				}
				bannerBox.find(".btn ul").append(btnDom);
				bannerBox.find(".btn li").eq(0).addClass("active");
			}
			createBanner(obj.banner);

			$(window).resize(function() {
				windowW = $(window).width();

				scrollBox.find("li").css({
					width : windowW
				});
				$(".banner .btn").css({
					right : (windowW - 1190) / 2 + 300
				});

				bannerBox.on("click", ".btn li", function () {
					var index = $(this).index();
					flag = index;
					$(this).addClass("active").siblings().removeClass("active");
					scrollBox.animate({ left : -index * windowW}, 400);
				});

			});

			scrollBox.find("li").css({
				width : windowW
			});
			$(".banner .btn").css({
				right : (windowW - 1190) / 2 + 300
			});

			// banner切换
			bannerBox.on("click", ".btn li", function () {
				var index = $(this).index();
				flag = index;
				$(this).addClass("active").siblings().removeClass("active");
				scrollBox.animate({ left : -index * windowW}, 400);
			});
			// 自动执行banner切换
			var timer = setInterval(bannerSwitch, 5000);
			function bannerSwitch() {
				var len = scrollBox.find("li").length;
				flag++;
				if (flag >= len) flag = 0;
				$(".banner").find(".btn li").eq(flag).trigger("click");
			}
			$(".banner").hover( function () {
				clearInterval(timer);
			}, function () {
				timer = setInterval(bannerSwitch, 5000);
			});

			// 新品上架
			function createNewPro(list) {
				var dom = "",
					tag = list.tag,
					tagArr = [];

				if (list.skuId.length != 0) {
					if (tag != null && tag.length != 0) {
						tagArr = tag.split("/");
					} else {
						tagArr = [];
					}
					dom += "<div class='pro-box' data-skuid='"+ list.skuId +"' data-skunum='99999'>"+
					"<span class='pic'>"+
						"<a href='/product/detail?skuId="+ list.skuId +"' target='_blank'>"+
							"<img src='"+ list.url +"'>"+
						"</a>"+
						"</span>"+
						"<a href='/product/detail?skuId="+ list.skuId +"' class='tit' target='_blank' title='"+ list.name +"'>"+ list.name +"</a>"+
						"<span class='des' title='"+ list.title +"'>"+ list.title +"</span>"+
						"<span class='price'><em>¥</em>"+ list.price +"</span>"+
						"<a href='"+ list.storeLink +"' class='store-name'>"+ list.storeName +"</a>"+
						"<p class='tag'>";
					for (var i = 0; i < tagArr.length; i++) {
						dom += "<span>"+ tagArr[i] +"</span>";
					}
					dom += "</p>"+
						"<div class='action'>"+
							"<div class='jump'>"+
								"<span class='minus'>-</span>"+
								"<input class='num' value='1'>"+
								"<span class='add'>+</span>"+
							"</div>"+
							"<div class='add-cart'>加入进货单</div>"+
							"</div>"+
						"</div>";
					$(".new-pro .box").html(dom);
				}
			}
			createNewPro(obj.newPro);

			// 热销商品
			function createHotPro(list) {
				var dom = "",
					tag = "",
					tagArr = [],
					len = list.url.length;
				if ( len > 4) {
					len = 4;
				}
				for (var i = 0; i < len; i++) {
					dom += "<li><div class='pro-box' data-skuid='"+ list.skuId[i] +"' data-skunum='99999'>"+
					"<span class='pic'>"+
					"<a href='/product/detail?skuId="+ list.skuId[i] +"' target='_blank'>"+
						"<img src='"+ list.url[i] +"'>"+
					"</a>"+
					"</span>"+
					"<a href='/product/detail?skuId="+ list.skuId[i] +"' class='tit' target='_blank' title='"+ list.name[i] +"'>"+ list.name[i] +"</a>"+
					"<span class='des' title='"+ list.title[i] +"'>"+ list.title[i] +"</span>"+
					"<span class='price'><em>¥</em>"+ list.price[i] +"</span>"+
					"<a href='"+ list.storeLink[i] +"' class='store-name'>"+ list.storeName[i] +"</a>"+
					"<p class='tag'>";
					tag = list.tag[i];
					if (tag != undefined && tag.length != 0) {
						tagArr = tag.split("/");
						for (var j = 0; j < tagArr.length; j++) {
							dom += "<span>"+ tagArr[j] +"</span>";
						}
					}
					dom +=	"</p>"+
					"<div class='action'>"+
						"<div class='jump'>"+
							"<span class='minus'>-</span>"+
							"<input class='num' value='1'>"+
							"<span class='add'>+</span>"+
						"</div>"+
						"<div class='add-cart'>加入进货单</div>"+
						"</div>"+
					"</div></li>";
				}
				$(".hot-pro ul").html(dom);
			}
			createHotPro(obj.hotPro);

			// 品牌专区
			function createBrandList(data) {
				var dom = "",
				    list = data.list;
					tag = "",
					tagArr = [],
					len = list.url.length;
				if ( len > 3) {
					len = 3;
				}
				for (var i = 0; i < len; i++) {
					dom += "<li><div class='pro-box' data-skuid='"+ list.skuId[i] +"' data-skunum='99999'>"+
					"<span class='pic'>"+
					"<a href='/product/detail?skuId="+ list.skuId[i] +"' target='_blank'>"+
						"<img src='"+ list.url[i] +"'>"+
					"</a>"+
					"</span>"+
					"<a href='/product/detail?skuId="+ list.skuId[i] +"' class='tit' target='_blank' title='"+ list.name[i] +"'>"+ list.name[i] +"</a>"+
					"<span class='des' title='"+ list.title[i] +"'>"+ list.title[i] +"</span>"+
					"<span class='price'><em>¥</em>"+ list.price[i] +"</span>"+
					"<a href='"+ list.storeLink[i] +"' class='store-name'>"+ list.storeName[i] +"</a>"+
					"<p class='tag'>";
					tag = list.tag[i];
					if (tag != undefined && tag.length != 0) {
						tagArr = tag.split("/");
						for (var j = 0; j < tagArr.length; j++) {
							dom += "<span>"+ tagArr[j] +"</span>";
						}
					}
					dom +=	"</p>"+
					"<div class='action'>"+
						"<div class='jump'>"+
							"<span class='minus'>-</span>"+
							"<input class='num' value='1'>"+
							"<span class='add'>+</span>"+
						"</div>"+
						"<div class='add-cart'>加入进货单</div>"+
						"</div>"+
					"</div></li>";
				}
				$(".brand-pro ul").html(dom);
				$(".brand-pro .bg").html("<img src='"+ data.bakground +"' />");
			}
			createBrandList(obj.brandPro);

			// 分类模块
			var item1 = $("#itme1"),
				item2 = $("#itme2"),
				item3 = $("#itme3"),
				item4 = $("#itme4"),
				item5 = $("#itme5");
			// 创建分类模块下面的名称、广告、更多、主列表
			function createItem(box, list) {
				var titleDom = "",
					mainDom = "",
					tag = "",
					tagArr = [],
					len = list.main.name.length;
				if (len > 4) len = 4;
				// 创建title包含 模块名称、广告、更多连接
				titleDom += "<i></i><span>"+ list.titleName +"</span>";
				for (var i = 0; i < list.ads.link.length; i++) {
					titleDom += "<a href='"+ list.ads.link[i] +"' target='_blank'>"+ list.ads.name[i] +"</a>";
				}
				titleDom += "<a href='"+ list.titleLink +"' target='_blank' class='more'>更多></a>";
				box.find(".title").append(titleDom);
				// 创建主列表包含广告图
				mainDom += "<div class='bg'><a href='"+ list.backgroundLink +"' target='_blank'><img src='"+ list.background +"'></a></div><ul>";
				for (var i = 0; i < len; i++) {
					mainDom += "<li><div class='pro-box' data-skuid='"+ list.main.skuId[i] +"' data-skunum='99999'>"+
					"<span class='pic'>"+
					"<a href='/product/detail?skuId="+ list.main.skuId[i] +"' target='_blank'>"+
						"<img src='"+ list.main.url[i] +"'>"+
					"</a>"+
					"</span>"+
					"<a href='/product/detail?skuId="+ list.main.skuId[i] +"' class='tit' target='_blank' title='"+ list.main.name[i] +"'>"+ list.main.name[i] +"</a>"+
					"<span class='des' title='"+ list.main.title[i] +"'>"+ list.main.title[i] +"</span>"+
					"<span class='price'><em>¥</em>"+ list.main.price[i] +"</span>"+
					"<a href='"+ list.main.storeLink[i] +"' class='store-name'>"+ list.main.storeName[i] +"</a>"+
					"<p class='tag'>";
					tag = list.main.tag[i];
					if (tag != undefined && tag.length != 0) {
						tagArr = tag.split("/");
						for (var j = 0; j < tagArr.length; j++) {
							mainDom += "<span>"+ tagArr[j] +"</span>";
						}
					}
					mainDom += "</p>"+
					"<div class='action'>"+
						"<div class='jump'>"+
							"<span class='minus'>-</span>"+
							"<input class='num' value='1'>"+
							"<span class='add'>+</span>"+
						"</div>"+
						"<div class='add-cart'>加入进货单</div>"+
						"</div>"+
					"</div></li>";
				}
				mainDom += "</ul>";
				box.find(".list").append(mainDom);
			}
			//createItem(item1, obj.item1);
			//createItem(item2, obj.item2);
			//createItem(item3, obj.item3);
			//createItem(item4, obj.item4);
			//createItem(item5, obj.item5);

			// 加入购物车
			function resetListaction(box) {
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
				});
				box.on("click", ".minus", function () {
					var numInput = $(this).siblings(".num"),
						numVal = numInput.val();
					if (numVal <= 1) {
						return;
					} else {
						numVal--;
						numInput.val(numVal);
					}
				});
				box.on("change", ".num", function () {
					var numInput = $(this),
						numVal = numInput.val(),
						maxNum = $(this).closest(".pro-box").data("skunum");
					if (numVal < 1 || isNaN(numVal)) {
						numVal = 1;
						numInput.val(numVal);
					}
					if (numVal <= maxNum) {
						numInput.val(numVal);
					} else {
						$.message.alert("库存不足！", "info");
						numInput.val(maxNum);
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
							} else {
								$.message.alert(data.message, "info");
							}
						}
					});
				}
			}
			resetListaction($(".new-pro"));
			resetListaction($(".hot-pro"));
			resetListaction($(".brand-pro"));
			resetListaction($(".module"));
    	});
    </script>
</body>
</html>
</@compress>
</#escape>
//-----fileEnd-----