<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="coupon"/>
    <@css/>
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
    <@fixedTop />
    <@topbar />
    <@top />
    <@mainCategory />
    <div class="bg-french-gray clearfix">
    	<@crumbs>
		 <a href="/index">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">优惠券</span>
    	</@crumbs>
	    <@myModule sideIndex=5>
		    <div class="m-box m-main l my-coupon">
		    	<div class="tit">
		    		<b>优惠券</b>
		    		<span class="add-coupon">新增优惠券</span>
		    	</div>
<#-- 		    	<div class="list">
		    		<ul>
		    		</ul>
		    	</div> -->
		    	<div class="list">
		    		<table>
		    			<thead>
		    				<tr>
		    					<th width="30%">面值</th>
		    					<th width="30%">券号</th>
		    					<th width="40%">有效时间</th>
		    				</tr>
		    			</thead>
		    			<tbody></tbody>
		    			<tfoot></tfoot>
		    		</table>
		    	</div>
		    	<div class="clearfix">
					<div class="page"></div>
				</div>
		    </div>
	    </@myModule>
    </div>
    <@footer/>
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script>
    	$(function () {

    		var pageBox = $(".page"),
    			total = "",
    			pageIndex = 0,
    			pageLimit = 10;

    		// 添加侧边栏选中样式
    		$(".bg-french-gray").find(".list li").eq(2).addClass("active");

    		// 刷新验证图片
    		$(".popup").on("click", ".verify-img", function () {
    			timestamp = Date.parse(new Date());
    			$(this).attr("src", "/brand/genverifycode?" + timestamp);
    		});

    		var addCouponDom = "",
    			timestamp = "";
    		$(".add-coupon").on("click", function () {
    			createPopup("mid-box");
    			$(".popup").popup("新增优惠券");
    			createCoupon();
    		});
			
    		// 创建绑定优惠券dom
    		function createCoupon() {
    			addCouponDom = "",
    			timestamp = Date.parse(new Date());
    			addCouponDom += "<div class='add-coupon-box'>"+
    								"<div class='code-box clearfix'>"+
    									"<input type='text' placeholder='请输入优惠券码' class='input-text coupon-code' />"+
    									"<input type='text' placeholder='请输入验证码' class='input-text verify-code' />"+
    									"<img src='/brand/genverifycode?"+ timestamp +"' class='verify-img' />"+
    								"</div>"+
    								"<div class='btn'>"+
    									"<a href='javascript:void(0);' class='btn-base btn-submit'>确定</a>"+
    								"</div>"+
    							"</div>";
    			$(".popup").find(".content").append(addCouponDom);
    		}

    		// 添加优惠券
    		$(".popup").on("click", ".btn-submit", function () {
    			var couponCode = $(".popup").find(".coupon-code").val(),
    				verifyCode = $(".popup").find(".verify-code").val();

    			if (couponCode == "") {
    				$(".coupon-code").focus().addClass("input-error");
    			} else if (verifyCode == "") {
    				$(".coupon-code").removeClass("input-error");
    				$(".verify-code").focus().addClass("input-error");
    			} else {
    				$(".coupon-code").removeClass("input-error");
    				$(".verify-code").removeClass("input-error");
    				addCoupon(couponCode, verifyCode);
    			}
    		});

    		function addCoupon(couponCode, verifyCode) {
    			$.ajax({
	    			url : "/mycoupon/bindCoupon/",
	    			type : "GET",
	    			dataType : "json",
	    			data : {
	    				couponCode : couponCode,
	    				verifyCode : verifyCode
	    			},
	    			success : function (data) {
	    				//console.log(data);
	    				//console.log(data);
	    				if (data.code == 200) {
	    					$(".popup").empty();
	    					$.message.alert("添加优惠券成功！", "success");
	    					getCouponList(pageLimit, pageIndex);
	    				} else {
	    					$(".popup").empty();
	    					if (data.result == 7) {
	    						$.message.alert("您已拥有该优惠券！", "fail");
	    					} else {
	    						$.message.alert("请输入正确的优惠券码或验证码！", "info");
	    					}
	    				}
	    			}
	    		});
    		}

    		// 获取优惠券列表
    		function getCouponList(pageLimit, pageIndex) {
    			//console.log(pageLimit);
    			//console.log(pageIndex);
    			var couponListDom = "",
    				couponListBox = $(".my-coupon .list tbody");
	    		$.ajax({
	    		 	cache: false,
	    			url : "/mycoupon/data/couponList/",
	    			type : "GET",
	    			dataType : "json",
	    			data : {
	    				limit : pageLimit,
	    				offset : pageIndex
	    			},
	    			success : function (data) {
	    				//console.log(data);
	    				if (data.code == 200) { 
		    				var list = data.result.list;
		    				if (list != null) {
		    					couponListBox.empty();
		    					for (var i = 0; i < list.length; i++) {
		    						var items 		= list[i].items,
		    							items 		= JSON.parse(items),
		    							status 		= list[i].couponState,
		    							type 		= list[i].favorType,
		    							startTime 	= list[i].startTime,
		    							endTime 	= list[i].endTime,
		    							//nowTime		= Date.parse(new Date()),
		    							Sdates 		= new Date(startTime),
		    							Syear 		= Sdates.getFullYear(),
		    							Smonth  	= Sdates.getMonth() + 1,
		    							Sdate		= Sdates.getDate(),
		    							Edates  	= new Date(endTime),
		    							Eyear 		= Edates.getFullYear(),
		    							Emonth  	= Edates.getMonth() + 1,
		    							Edate		= Edates.getDate();
		    							startTime 	= Syear + "." + Smonth + "." + Sdate;
		    							endTime   	= Eyear + "." + Emonth + "." + Edate;
		    							nowTime 	= ${currTime};

		    						if (status == 0) {
                                        // 设置提醒过期时间为5天
                                        if ( (Number(list[i].endTime) - Number(nowTime)) / 1000 <= (60 * 60 * 24) * 5 ) {
                                            status = 9;
                                        } else {
                                        	status = 0;
                                        }
                                    }

		    						// 根据优惠券状态设置不同背景
		    						if (status == 0 || status == 9 || status == 8) {
		    							couponListDom += "<tr><td><div class='suc card'>";
		    						}
		    						if (status == 2 || status == 3 || status == 5) {
		    							couponListDom += "<tr><td><div class='err card'>";
		    						}

		    						// 根据状态设置不同icon图标
		    						if (status == 2) {
					    				// 失效
					    				couponListDom += "<i class='state icon-3'></i>";
					    			} else if (status == 3) {
					    				// 使用
					    				couponListDom += "<i class='state icon-4'></i>";
					    			} else if (status == 9) {
					    				// 即将过期
					    				couponListDom += "<i class='state icon-2'></i>";
					    			} else if (status == 0) {
					    				// 未使用
					    				couponListDom += "<i class='state icon-1'></i>";
					    			}

		    						// 根据优惠券类型设置显示不同内容
		    						if (type == 0) {
		    							couponListDom += "<div class='type discount'>"+
		    							"<em>￥</em>"+
					    					"<span>"+ items[0].result[0].value +"</span>"+
					    				"</div>";
		    						} else {
		    							couponListDom += "<div class='type reduce'>"+
					    					"<span>"+ items[0].result[0].value +"</span>"+
					    					"<em>折</em>"+
					    				"</div>";
		    						}
		    						var condition = items[0].condition.value,
		    							condition = condition.substr(0, condition.length - 1);
					    			couponListDom += "<div class='info'>"+
					    					"<span class='des'>满" + condition +"元可用</span>"+
					    				"</div></td>"+
					    				"<td align='center'>"+
					    					"<span class='code'>"+ list[i].couponCode +"</span>"+
					    				"</td>"+
					    				"<td align='center'>"+
					    					"<span class='time'>"+
					    						"<em class='start'>"+ startTime +"</em>"+
					    						" - <em class='end'>"+ endTime +"</em>"+
					    					"</span>"+
					    				"</td></tr>";
		    					}
		    					couponListBox.append(couponListDom);
		    					//console.log(data.result.total);
		    					//console.log(pageLimit);
		    					total = Math.ceil(data.result.total / pageLimit);
		    					createPage(total, pageIndex);
		    				} else {
		    					couponListBox.html("<tr><td colspan='3'><span class='null-box'>暂无优惠券!</span></td></tr>");
		    				}
	    				}
	    			}
	    		});
    		}
    		getCouponList(pageLimit, pageIndex);

    		// 创建分页
    		// 上下页按钮事件
			pageBox.on("click", "span", function () {
				if ($(this).hasClass("disable")) return;
				pageIndex = $(this).data("index");
				getCouponList(pageLimit, (pageIndex - 1) * pageLimit);
			});

			// 页数按钮事件
			pageBox.on("click", "li", function () {
				pageIndex = $(this).html();
				getCouponList(pageLimit, (pageIndex - 1) * pageLimit);

			});

			// 输入页数确定按钮
			pageBox.on("click", ".page-submit", function () {
				pageIndex = pageBox.find(".skip input").val();
				getCouponList(pageLimit, (pageIndex - 1) * pageLimit);

			});

			// 分页跳转输入框
			pageBox.on("change", ".skip input", function () {
				var thisVal = $(this).val(),
					thisVal = Math.round(thisVal);
					//maxPage = $(this).closest('.skip').siblings('.next').data("max");
				if (isNaN(thisVal) || thisVal <= 0) {
					$.message.alert("请输入大于0的整数！", "info");
					thisVal = 1;
				}
				if (thisVal > total) {
					$.message.alert("输入值已超过最大分页值！", "info");
					thisVal = total;
				}
				$(this).val(thisVal);
			});

			// 分页跳转箭头
			pageBox.on("click", ".down", function () {
				var defVal = $(this).closest('.arrow').siblings('input').val(),
					thisVal = Number(defVal) - 1;
				if (thisVal < 1) {
					$.message.alert("最小值不能小于1！", "info");
					thisVal = 1;
				}
				$(this).closest('.arrow').siblings('input').val(thisVal);
			});

			pageBox.on("click", ".up", function () {
				var defVal = $(this).closest('.arrow').siblings('input').val(),
					maxPage = $(this).closest('.skip').siblings('.next').data("max"),
					thisVal = Number(defVal) + 1;
				if (maxPage == undefined) {
					maxPage = 1;
				}
				if (thisVal > maxPage) {
					$.message.alert("最大值超过分页数！", "info");
					thisVal = maxPage;
				}
				$(this).closest('.arrow').siblings('input').val(thisVal);
			});

			/**
				@ tag 创建分页
				@ param total:总页数 index:当前页数
			**/

			function createPage(total, index) {
				index = Number(index / 10) + 1;
				var pageDom = "",
					liDom = "";

				pageBox.empty();
				if (index > 1) {
					pageDom += "<span class='prev' data-index='" + (index-1) + "'>上一页</span>";
				} else {
					pageDom += "<span class='prev disable'>首页</span>";
				}
				pageDom += "<ul>";

				if (total < 5) {	
					for ( var i = 0; i < total; i++) {
						pageDom += "<li>"+ (i + 1) +"</li>";
					}
				} else {
					if ( index < total - 2) {
						liDom = "<li>"+ index +"</li><li>"+ (index + 1) +"</li><li class='num-null'>....</li><li>"+ (total - 1) +"</li><li>"+ total +"</li>";
					} else if (index == total - 2) {
						liDom = "<li>1</li><li class='num-null'>....</li><li>"+ (total - 2) +"</li><li>"+ (total - 1) +"</li><li>"+ total +"</li>";
					} else {
						liDom = "<li>1</li><li>2</li><li class='num-null'>....</li><li>"+ (total - 1) +"</li><li>"+ total +"</li>";
					}
				}

				pageDom += liDom + "</ul>";
				if (index < total) {
					pageDom += "<span class='next' data-index='" + (index + 1) + "' data-max='" + total + "'>下一页</span>";
				} else {
					pageDom += "<span class='next disable' data-max='" + total + "'>尾页</span>";
				}
				pageDom += "<em>共"+ total +"页，到第</em>"+
					"<div class='skip'>"+
						"<input tyep='text' value='"+ index +"'>"+
						"<div class='arrow'>"+
							"<i class='up'></i>"+
							"<i class='down'></i>"+
						"</div>"+
					"</div>"+
					"<em>页</em>"+
					"<input type='button' value='确定' class='page-submit' />";

				pageBox.append(pageDom);

				pageBox.find("li").each( function () {
					var thisHtml = $(this).html();
					if (index == thisHtml) {
						$(this).addClass("active");
					}
				});
			}
    	});
    </script>
  </body>
</html>
</@compress>
</#escape>
