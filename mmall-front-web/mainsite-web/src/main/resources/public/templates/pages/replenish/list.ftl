<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="replenish"/>
    <@css/>
    <link href="/src/css/front.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/member.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
    <@fixedTop />
    <@topbar />
    <@top />
    <@mainCategory />
    <div class="clearfix">
	    <@crumbs>
			 <a href="/index">首页</a><span>&gt;</span><a href="/profile/index">个人中心</a><span>&gt;</span><span class="selected">货架补货</span>
	    </@crumbs>
	    <div class="replenishment wrap">
	    	<div class="table"></div>
	    </div>
	    <div class="replenish-result">
	    	<div class="wrap">
	    		<em>店铺数量：<span class="store-num">0</span>个</em>
	    		<em>商品种类：<span class="pro-num">0</span>种</em>         
	    		<em>数量总计：<span class="total-num">0</span>件</em>
	    		<a href="javascrip:void(0);" class="btn-base btn-submit">加入进货单结算</a>
    		</div>
    	</div>
    </div>
    <@footer/>
    <@copyright/>
    <@js />
    <script>
    	$(function () {

    		var obj = $(".replenishment"),
				skuNum = "",
				allProNum = 0,
				allProBox = $(".total-num");

    		function getReplenishList() {
	    		$.ajax({
	    			type : "GET",
	    			url : "/replenish/list/",
	    			dataType : "json",
	    			data : {},
	    			success : function (data) {
	    				console.log(data);
	    				if (data.code == 200) {
	    					var list = data.result.list,
	    						dom = "",
	    						box = $(".replenishment .table");
	    					if (list != null) {
	    						for (var i = 0; i < list.length; i++) {

	    							dom += "<table class='table-ui'>"+
												"<thead>"+
													"<tr>"+
														"<th width='50%'>"+
															"<span class='storeName'>"+ list[i].storeName +"</span>"+
														"</th>"+
														"<th width='10%'>"+
														"</th>"+
														"<th width='20%'>"+
														"</th>"+
														"<th width='20%'>"+
															"<span class='total'>小计<em>1</em>件</span>"+
														"</th>"+
													"</tr>"+
												"</thead>"+
												"<tbody>";
												for (var j = 0; j < list[i].replenishVOList.length; j++) {
													var obj = list[i].replenishVOList[j],
														snapshot = obj.snapshot,
														snapshot = JSON.parse(snapshot),
														status = obj.skuStatus,
														floatPrice = obj.priceUpsAndDowns;
													console.log(obj);
													console.log(floatPrice);
													if (status == 0 || status == 1 || status == 2 || status == 3) {
														// 不存在 未审核 审核中 审核未通过
														dom += "<tr class='no-has'";
													} else if (status == 4) {
														// 已上架
														dom += "<tr class='putaway'";
													} else {
														// 已下架
														dom += "<tr class='sold-out'";
													}
													dom +=  " data-skuid='"+ obj.productId +"'>"+
																"<td>"+
																	"<a href='" + snapshot.linkUrl + "' class='picurl'>"+
																		"<img src='"+ snapshot.picUrl +"' />"+
																	"</a>"+
																	"<a href='" + snapshot.linkUrl + "' class='productName'>"+ snapshot.productName +"</a>"+
																	"<span class='des'>暂无描述信息</span>"+
																"</td>"+
																"<td>"+
																"</td>";
													if (status == 0 || status == 1 || status == 2 || status == 3) {
														// 不存在 未审核 审核中 审核未通过
														dom += "<td><span class='pro-status'>商品已失效</span><span class='del'>删除</span></td>";
													} else if (status == 4) {
														// 已上架
														if (floatPrice != null) {
															dom += "<td><span class='float-info'>"+ floatPrice +"</span></td>";
														} else {
															dom += "<td></td>";
														}
													} else {
														// 已下架
														dom += "<td><span class='pro-status'>商品已下架</span><span class='del'>删除</span></td>";
													}
													dom += 		"<td data-skunum='"+ obj.skuStockNum +"'>"+
																	"<div class='item'>"+
																		"<span class='prev'>-</span>"+
																		"<input type='text' value='1' class='num-input'>"+
																		"<span class='next'>+</span>"+
																	"</div>"+
																"</td>"+
															"</tr>";
												}
												dom += "</tobdy>"+
											"</table>";
	    						}
	    						box.append(dom);
	    						$(".store-num").html(list.length);
	    						$(".pro-num").html($("table tbody tr").length);
	    						$(".total-num").html(list.length);
	    					}
	    				} else {
	    					return;
	    				}
	    			}
	    		});
			}
			getReplenishList();

			// 增减功能增加按钮
			obj.on("click", ".next", function () {
				var _this = $(this),
					inputText = _this.prev("input"),
					defValue = inputText.val(),
					skuNum = _this.closest('td').data("skunum");
				if (defValue >= skuNum) {
					$.message.alert("购买数量不能超过库存总数！", "info");
					return;
				} else {
					defValue++;
				}
				inputText.val(defValue);
				
				storeProTotal(_this);
				allStoreProTotal();
			});

			// 增减功能减少按钮
			obj.on("click", ".prev", function () {
				var _this = $(this),
					inputText = _this.next("input"),
					defValue = inputText.val();
				if (defValue <= 1) {
					$.message.alert("购买数量不能少于1件！", "info");
					return;
				} else {
					defValue--;
				}
				inputText.val(defValue);

				storeProTotal(_this);
				allStoreProTotal();
			});

			// 增减功能输入框
			obj.on("change", ".num-input", function () {
				var _this = $(this),
					inputText = _this,
					defValue = inputText.val(),
					skuNum = _this.closest('td').data("skunum");
				if(isNaN(defValue)) {
					$.message.alert("请输入数字！", "info");
					defValue = 1;
				}
				if (defValue < 1) {
					$.message.alert("请输入大于1的数字！", "info");
					defValue = 1;
				}
				if (defValue >= skuNum) {
					$.message.alert("购买数量不能超过库存总数！", "info");
					defValue = skuNum;
				} 
				inputText.val(defValue);

				storeProTotal(_this);
				allStoreProTotal();
			});

			// 计算一个店铺内产品的件数
			function storeProTotal(_this) {
				var parent = _this.closest('table'),
					totalNum = 0,
					totalBox = parent.find(".total em");
				parent.find(".num-input").each( function () {
					totalNum += Number($(this).val());
				});
				totalBox.html(totalNum);
			}

			// 计算所有店铺的产品总数
			function allStoreProTotal() {
				allProNum = 0;
				obj.find(".total em").each( function () {
					allProNum += Number($(this).html());
				});
				allProBox.html(allProNum);
			}

			// 全部列表加入进货单
			$(".replenish-result a").on("click", function () {
				var skuid = "",
					count = "",
					tempObj = {},
					tempArr = [],
					list = $(".replenishment tbody tr");
				for (var i = 0; i < list.length; i++) {
					skuid = list.eq(i).data("skuid");
					count = list.eq(i).find(".num-input").val();
					tempObj = {"skuid": skuid, "count" : count};
					tempArr.push(tempObj);
					console.log(tempArr);
				}
				addToCart(tempArr);
			});
			
			function addToCart(tempArr) {
				$.ajax({
					type : "POST",
					url: "/cart/updateCartAmount",
					async: false,
					contentType: "application/json",
					data : JSON.stringify({
						"cartItemDTOs" : tempArr
					}),
					success: function (data) {
						if (data.code == 200) {
							$.message.alert("加入进货单成功！", "success");
							getSkuNum();
						} else {
							$.message.alert("加入进货单失败！", "fail")
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

    	});
    </script>
  </body>
</html>
</@compress>
</#escape>