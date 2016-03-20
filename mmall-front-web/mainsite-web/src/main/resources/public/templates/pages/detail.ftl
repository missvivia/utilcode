<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../wrap/common.ftl">
    <@title type="index"/>
    <meta charset="utf-8"/>
    <@css/>
    <link rel="stylesheet" href="/src/css/front.css" />
</head>
<body>
	<@fixedTop />
	<@topbar />
	<@top />
	<div class="main-category-box">
		<div class="main-category wrap">
			<div class="category-item"> 
				<dl>
					<dt>
						全部分类<i></i>
					</dt>
					<dd>
						<a href="#">自营商品</a>
					</dd>
					<dd>
						<a href="#">生鲜</a>
					</dd>
					<dd>
						<a href="#">生活用品</a>
					</dd>
					<dd>
						<a href="#">酒水饮料</a>
					</dd>
				</dl>
			</div>
			<div class="category-side">
				<ul>
				</ul>
			</div>
			<div class="category-nav">
				<ul>
				</ul>
			</div>
			<div class="cart r">
				<span>
					<i></i>
				</span>
				进货单
				<a href="#">47</a>
			</div>
		</div>
	</div>
	<div class="position wrap clearfix">
		<a href="#" class="f-b">进口食品、进口牛奶</a>
		<span>></span>
		<a href="#">DEVONDALE</a>
		<span>></span>
		<a href="#">DEVONDALE德运 脱脂牛奶 纯牛奶 6*1L 澳大利亚进口</a>                            
	</div>
	<div class="wrap clearfix pro-detail">
		<div class="l main">
			<div class="clearfix pro-intro">
				<div class="l preview">
					<div class="view">
						<ul></ul>
					</div>
					<div class="list">
						<ul></ul>
					</div>
				</div>	
				<div class="l item">
					<p class="name">
					</p>
					<p class="descrip">
					</p>
					<div class="clearfix prcie-list">
						<dl class="number">
							<dt>起批量</dt>
						</dl>
						<dl class="price">
							<dt>订货价</dt>
						</dl>
					</div>
					<div class="freight">
						<span>运费</span>满¥88包邮（10kg以内） 配送和配送详情 
					</div>
					<div class="type">
						<span>尺码</span>                              
					</div>
					<div class="skip clearfix">
						<input type="text" class="l num" value="5">
						<div class="l arrow">
							<em class="up"></em>
							<em class="down disable"></em>
						</div>
						<span class="l tip">每件¥<b>66.00</b>进货车中已有<em>13</em>件</span>
					</div>
					<div class="clearfix btn">
						<input type="button" value="加入进货车" class="bg-red" />
					</div>
					<div class="service">  
						<span>保障</span>
						<em>正品保障</em>
						<em>售后无忧</em>
						<em>赠票服务</em>
						<em>支持7天无理由退货</em>
					</div>
				</div>
			</div>
			<div class="pro-collect">
				收藏商品 （8378人气）
			</div>
			<div class="pro-descrip">
				<div class="clearfix tab">
					<ul>
						<li class="active bl">商品详情</li>
						<li>规格参数</li>
						<li>售后保障</li>
					</ul>
				</div>
				<div class="con">
					<div class="clearfix parameter">
						<ul></ul>
					</div>
					<p>
						<img src="/src/test-pic/pic-23.jpg" />
					</p>
				</div>
			</div>
		</div>
		<div class="r side">
			<div class="shop-info ">
				<div class="clearfix person-info">
					<span>
						<img src="/src/test-pic/pic-24.png" />
					</span>
					<a href="#">进口奶源商行</a>
					<i></i>
				</div>
				<div class="shop-btn">
					<a href="#" class="btn-base btn-defalut">进入店铺</a>
					<a href="#" class="btn-base btn-defalut">收藏店铺</a>
				</div>
			</div>
			<div class="clearfix shop-pro">
				<div class="title">
					<span>相关商品</span>
				</div>
				<div class="shop-list">
					<li>
						<img src="/src/test-pic/pic-25.jpg" />
						<p>
							<span><i class="f-b">¥</i>69</span>
							<em>销量: 447</em>
						</p>
					</li>
					<li>
						<img src="/src/test-pic/pic-25.jpg" />
						<p>
							<span><i class="f-b">¥</i>69</span>
							<em>销量: 447</em>
						</p>
					</li>
					<li>
						<img src="/src/test-pic/pic-25.jpg" />
						<p>
							<span><i class="f-b">¥</i>69</span>
							<em>销量: 447</em>
						</p>
					</li>
					<li>
						<img src="/src/test-pic/pic-25.jpg" />
						<p>
							<span><i class="f-b">¥</i>69</span>
							<em>销量: 447</em>
						</p>
					</li>
					<li>
						<img src="/src/test-pic/pic-25.jpg" />
						<p>
							<span><i class="f-b">¥</i>69</span>
							<em>销量: 447</em>
						</p>
					</li>
				</div>
			</div>
		</div>
	</div>
	<@footer />
	<@copyright />
	<@fixedSide />
    <@js />
    <script type="text/javascript">
    	$(function () {

    		var proList = $(".pro-detail .list"),
    			proView = $(".pro-detail .view li"),
    			proType = $(".pro-detail .type"),
    			proTab = $(".pro-detail .tab li"),
    			up = $(".pro-detail .skip .arrow .up"),
    			down = $(".pro-detail .skip .arrow .down"),
    			minNum = $(".pro-detail .price dd:first").data("minnum"),
    			input = $(".pro-detail .skip .num"),
    			priceList = $(".pro-detail .price dd"),
    			tempArr = [],
    			proViewBox = $(".pro-detail .view ul"),
    			proListBox = $(".pro-detail .list ul"),
    			minPicParam = "?imageView&thumbnail=70x70&quality=95",
				proViewDom = "",
				proListDom = "",
				proSizeBox = $(".pro-detail .type");

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

    		up.on("click", function () {
    			var inputVal = input.val();
    			inputVal++;
    			if ( inputVal > minNum) {
    				down.removeClass("disable");
    			}
    			input.val(inputVal);
    			matchingSection(inputVal);
    		});

    		down.on("click", function () {
    			if ($(this).hasClass("disable")) return;
    			var inputVal = input.val();
    			if ( inputVal <= minNum + 1) {
    				down.addClass("disable");
    			}
    			inputVal--;
    			input.val(inputVal);
    			matchingSection(inputVal);
    		});

    		for ( var i = 0; i < priceList.length; i++) {
				var leastNum = $(priceList[i]).data("minnum");
				tempArr.push(leastNum);
				
			}

			function matchingSection(val) {
				for (var i = 0;  i < tempArr.length; i++ ) {
					if (val == tempArr[i]) {
						console.log(i);
						$(".pro-detail .price dd").eq(i).addClass("active").siblings().removeClass("active");
					}
				}
			}

			// 获取产品ID
			var url = window.location.href,
				key = url.indexOf("="),
				proId = url.substring(key + 1, url.length);

			// 获取商品详情
			function getProDetail() {
				$.ajax({
					type : "GET",
					url: "/detail",
					async: false,
					dataType: "json",
					data : {
						id : proId
					},
					success: function (data) {

						console.log(data);
						var obj = data.product,
							proName = obj.productName;
							proDescribe = obj.productDescp,
							proPrice = data.priceList;
							proSize = obj.sizeSpecList;
							picList = obj.prodShowPicList,
							proDetail = obj.productDetail;

						$(".pro-detail .name").html(proName);
						$(".pro-detail .descrip").html(proDescribe);

						createProPrice(proPrice);
						createProSize(proSize);
						createPicList(picList);
						createProDetail(proDetail);
					}
				});
			}
			getProDetail();

			// 创建商品价格区间
			function createProPrice(proPrice) {
				var proNumberBox = $(".pro-detail .number"),
					proPriceBox = $(".pro-detail .price"),
					numberDom = "",
					priceDom = "";
				for (var i = 0; i < proPrice.length; i++) {
					numberDom += "<dd>"+ proPrice[i].minNumber +"-"+ proPrice[i].maxNumber +"件</dd>";
					priceDom += "<dd data-minnum='"+ proPrice[i].minNumber +"' data-maxnum='"+ proPrice[i].maxNumber +"'>"+
									"<i></i>"+
									"<span class='b-font'>¥</span>"+
									"<em>"+ proPrice[i].price +"</em>"+
								"</dd>";
				}
				proNumberBox.append(numberDom);
				proPriceBox.append(priceDom);
			}

			// 创建产品尺寸
			function createProSize(proSize) {
				var sizeDom = "";
				for (var i = 0; i < proSize.length; i++) {
					sizeDom += "<em>"+ proSize[i].size +"<i></i></em>";
				}
				proSizeBox.append(sizeDom);
				// 默认第一个尺寸选中
				$(".pro-detail .type em:first").addClass("active");
			}

			// 创建商品图片列表
			function createPicList(picList) {
				for (var i = 0; i < picList.length; i++) {
					proViewDom += "<li>"+
										"<img src='"+ picList[i] +"'>"+
									"</li>";
					proListDom += "<li>"+
										"<img src='"+ picList[i] + minPicParam +"'>"+
									"</li>";
				}
				proViewBox.append(proViewDom);
				proListBox.append(proListDom);
			}

			// 创建商品详情
			function createProDetail(proDetail) {
				console.log(proDetail);
				var proDetailBox = $(".pro-detail .parameter ul"),
					proDetatlDom = "";
				for (var i = 0; i < proDetail.length; i++ ) {
					proDetatlDom += "<li>"+
										"<span>"+ proDetail[i].name +"：</span>"+
										"<em>"+ proDetail[i].value +"</em>"+
									"</li>";
				}
				proDetailBox.append(proDetatlDom);
			}

    	});
    </script>
</body>
</html>
</@compress>
</#escape>