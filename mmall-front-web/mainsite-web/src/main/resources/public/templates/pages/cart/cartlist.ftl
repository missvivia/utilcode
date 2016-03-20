<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <#include "../../wrap/cartcommon.ftl">
    <@title type="cartList" />
    <meta charset="utf-8"/>
    <@css/>
</head>
<body>
	<@topbar />
    <@cartHeader/>
	<div class="wrap order-list">
		<h2>
			我的进货单
		</h2>
		<table>
			<thead>
				<tr>
					<th width="5%">
						<input type="checkbox" class="check-reverse check-all-header" checked="checked"/>
					</th>
					<th width="25%">
						商品
					</th>
					<th width="15%">
						价格
					</th>
					<th width="15%">
						数量
					</th>
					<th width="10%">
						金额
					</th>
					<th width="10%">
						操作
					</th>
				</tr>
			</thead>
			<tbody class="check-all-list">
			    <#if cartVO.cartStoreList?? >
				    <#list cartVO.cartStoreList as item>
				    	<tr data-sort='store' data-index='${item_index}'>
			                <td class='title t-c'>
								<input type='checkbox' class="check-all-item" checked="checked" />
							</td>
							<td colspan='6' class='title'>
						  		<a href='${item.storeUrl}' target="_blank">${item.storeName!''}</a>
						  		<#if item.storeBatchCash??>
						  			<span class="store-explain">
						  				起送金额：<em>${item.storeBatchCash}</em>元
						  			</span>
						  			<span class='price-differences'>
						  				还差<em></em>元，<a href='${item.storeUrl}' target="_blank">去凑单></a>
						  			</span>
						  		</#if>
							</td>
						</tr>
						<#list item.skulist as sku>
							<tr data-skuId=${sku.id} data-proId=${sku.productId} data-count = ${sku.inventroyCount} data-sort='product' data-index='${item_index}'>
								<td class="content">
									<input type='checkbox' class="check-all-item" checked="checked" />
							    </td>
								<td class='pro-info'>
									<a href='${sku.url}' target="_blank">
									  	<img src="${sku.thumb}" />
									</a>
							    	<#if sku.name??>
							    		<a href="${sku.url}" target="_blank" class="name">${sku.name}</a>
							    	</#if>
							    	<#if sku.limitConfigVO??>
                                        <#if (.now?long >= sku.limitConfigVO.startTime) && (.now?long <= sku.limitConfigVO.endTime)>
                                            <div class='limit' data-sTime='${sku.limitConfigVO.startTime}' data-eTime='${sku.limitConfigVO.endTime}' data-buyNum='${sku.limitConfigVO.allowBuyNum}'>
                                                ${sku.limitConfigVO.limitDescrp}<br />还可购买${sku.limitConfigVO.allowBuyNum}${sku.unit}
                                            </div>
                                        </#if>
                                        <#if (.now?long < sku.limitConfigVO.startTime)>
                                            <div class='limit' data-sTime='${sku.limitConfigVO.startTime}' data-eTime='${sku.limitConfigVO.endTime}' data-buyNum='${sku.limitConfigVO.allowBuyNum}' style='display: none;'>
                                            </div>
                                            <div class='limit-tips'>即将开始</div>
                                        </#if>
                                        <#if (.now?long > sku.limitConfigVO.endTime)>
                                            <div class='limit' data-sTime='${sku.limitConfigVO.startTime}' data-eTime='${sku.limitConfigVO.endTime}' data-buyNum='${sku.limitConfigVO.allowBuyNum}' style='display: none;'>
                                            </div>
                                            <div class='limit-tips'>已经结束</div>
                                        </#if>
							    	</#if>
								</td>
								<td class="only-price">
								  	<#if cartVO.productMap??&&cartVO.productMap?size gt 0>
								   	<#assign productId = '${sku.productId}'>
								   	<#if cartVO.productMap[productId]??>
									   	<#list cartVO.productMap[productId] as productPrice>
				                          	<p data-minNumber = ${productPrice.minNumber} data-price=${productPrice.price}>
					                          	<span><i class="min-num">${productPrice.minNumber}</i>${sku.unit}起批</span>
					                          	<em><i class="pro-price">${productPrice.price}</i>元/${sku.unit}</em>
				                          	</p>
										</#list>
									<#else>
										<p data-minNumber="0" data-price = ${sku.marketPrice} class="price">${sku.marketPrice}</p>
									</#if>
								 	<#else>
										<p data-minNumber = ${sku.marketPrice}>${sku.marketPrice}</p>
									</#if>
								</td>
								<td unselectable="on" onselectstart="return false;" style="-moz-user-select:none;">
									<div class='clearfix group'>
										<div class='item'>
										    <#if sku.count == 1>
	                                             <span class='prev disable'>-</span>
	                                        <#else>
	                                             <span class='prev'>-</span>
	                                        </#if>
											<input type='text' value='${sku.count}' class="num-input" onselectstart="event.cancelBubble=true"/>
										    <span class='next'>+</span>
										</div>
										<div class='label'>
											<#if sku.speciList??>
												<#list sku.speciList as specif>
												  	<#if specif.type==2>
												  		<span class='active'><em>${specif.specificationName}</em>:<em> <img src="${specif.speciOptionName}" /></em><i></i></span>
												  	<#else>
												  		<span class='active'><em>${specif.specificationName}</em>:<em>${specif.speciOptionName}</em><i></i></span>
												  	</#if>
												 </#list>
											</#if>
										</div>
								   </div>
								   <div class="tip">
									   	<#if sku.offline>
											<p class="tip-offline">商品已下架</p>
									   	</#if>
								       <p class="tip-error"></p> <!-- 不满足混批要求提示 -->
								       <p class="tip-count"></p> <!-- 库存不足提示 -->
								   </div>
								</td>
								<td>
									<b class="total-item"><#if sku.cartPrice==0>${sku.count*sku.marketPrice}<#else>${sku.count*sku.cartPrice}</#if></b>
								</td>
								<td>
								  	<#assign productId = '${sku.productId}'>
								  	<#if cartVO.poUsrFavMap[productId]??>
										<span class='cancelcollect collect'>取消收藏</span>
									<#else>
										<span class='collect'>收藏</span>
									</#if>
										<span class='del'>删除</span>
								</td>
							</tr>
						</#list>
				 	</#list>
			 	</#if>
			 	<form action="/purchase/index" method="post" id="postForm">
			        <input type="hidden" name="cartIds" value="" class="cartIds" />
		          	<input type="hidden" value="${formToken}" name="formToken" />
		         	<input type="hidden" name="skusPrice" value="" class="skusPrice" />
       			</form>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="7">
						<div class="clearfix order-info">
							<div class="l">
								<label class="check-all-box">
									<input type="checkbox" checked="checked" class="check-all-footer" />
									全选
								</label>
								<span class="del-order">删除</span>
							</div>
							<div class="r">
								<!--货品金额¥<b class="total">${cartVO.cartInfoVO.totalPrice}</b>元   ＋  运费¥<b class="freight">0.00</b>元（以结算页面为准）-->
								<span class="result">总计：¥<span>${cartVO.cartInfoVO.totalPrice}</span>元</span>
							</div>
						</div>
						<div class="order-btn">
							<a href="/" class="go-shop"><em>&lt;</em>继续订货</a>
							<a href="javascript:void(0);" class="go-result">去结算</a>
						</div>
					</td>
				</tr>
			</tfoot>
		</table>
	</div>
	<@footer />
	<@copyright />
    <@cityChange />
    <div class="popup"></div>
    <@js />
	<script type="text/javascript">
		$(function () {

			// 当前步骤
            $(".m-top .step li").eq(0).addClass('active');

            // 点击浏览器后退按钮返回时，重新计算当前商品购买数量及总价
            $(".order-list tbody tr[data-sort='product']").each(function(){
                var _this = $(this),
                    numInput = _this.find(".num-input").val(),
                    proPrice = _this.find(".pro-price").html(),
                    numText = _this.find(".num-text"),
                    totalItem = _this.find(".total-item"),
                    totalPrice = Number(numInput).mul(Number(proPrice));

                numText.html(numInput);
                totalItem.html(totalPrice.toFixed(2));
            });

            // 进货单内所有商品总价格，没有满足起批价时，提示去凑单
            var goFlag = true;
            function isGoshop() {
            	var startArr = [],
            		newArr = [];
            	$(".order-list tbody tr[data-sort='store']").each( function () {
            		startArr.push(Number($(this).find(".store-explain em").html()));
            	});

            	for(var i = 0; i < startArr.length; i++) {
            		var val = 0;
            		function result() {
	            		$(".order-list tbody tr[data-sort='product'][data-index='"+ i +"']").each( function () {
	            			if($(this).find(".check-all-item").prop("checked")) {
	            				val += Number($(this).find(".total-item").html());
	            			}
	            		});
	            		return val;
            		}
            		result();
            		newArr.push(val);
            	}

            	for(var i = 0; i < startArr.length; i++) {
            		if (startArr[i] > newArr[i]) {
            			$(".order-list tbody tr[data-sort='store'][data-index='"+ i +"']").find(".price-differences").show();
            			$(".order-list tbody tr[data-sort='store'][data-index='"+ i +"']").find(".price-differences em").html(Number(startArr[i] - newArr[i]).toFixed(2));
            		} else {
            			$(".order-list tbody tr[data-sort='store'][data-index='"+ i +"']").find(".price-differences").hide();
            		}
            	}

                var temLen = 0;
                for (var i = 0; i < $(".price-differences").length; i++) {
                    if ($($(".price-differences")[i]).is(':visible')) {
                        temLen++;
                    } else {
                        if (newArr[i] > 0) {
                            temLen = temLen;
                        } else {
                            temLen++;
                        }
                    }
                }

                if (temLen != startArr.length) {
                    for (var i = 0; i < $(".price-differences").length; i++) {

                        if ($($(".price-differences")[i]).is(':visible')) {
                            if (Number($($(".price-differences")[i]).find("em").html()) != Number($($(".price-differences")[i]).closest("td").find(".store-explain em").html())) {
                                $(".go-result").addClass("bg-gray");
                                goFlag = false;
                                return;
                            }
                        } else {
                            $(".go-result").removeClass("bg-gray");
                            setBtn();
                            goFlag = true;
                        }
                    }
                } else {
                    $(".go-result").addClass("bg-gray");
                    goFlag = false;
                }
            }

            // 勾选列表之后添加选中样式
            function addActive() {
                $(".order-list .total-item").each(function () {
                    var _this = $(this);
                    if(_this.closest("tr").find("input[type='checkbox']").prop("checked")){
                        _this.closest("tr").addClass("active");
                    } else {
                        _this.closest("tr").removeClass("active");
                    }
                });
            }

            // 初始化判断购物数量是否已经满足起批数量
            $(".order-list tbody tr").each( function () {
            	if ($(this).data("sort") == "product") {
	            	var num = $(this).find(".num-input").val(),
	            		minNumber = $(this).find(".min-num").html(),
	            		inventCount = $(this).data("count");
	            	if (Number(num) >= Number(minNumber)) {
	            		$(this).find("p").addClass("active");
	            	} else {
	            		$(this).find("p").removeClass("active");
	            	}
	            	//库存不足，取消选中并提示
	            	if(Number(num) > Number(inventCount)){
	            	    $(this).find(".tip .tip-count").html("库存不足");
	            	    $(this).find("input[type='checkbox']").removeAttr("checked");
	            	}
            	}
            });

            // 初始化时判断商品是否满足起批数量或起批金额
            $(".order-list tbody tr[data-sort='store']").each( function () {
                var _this = $(this),
                     storeStartPrice = _this.find(".store-price em").html(),
                     index = _this.data("index"),
                     sum = 0,
                     product = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']");
                if (storeStartPrice == undefined) {
                      storeStartPrice = 0;
                }
                product.each( function () {
                     if ($(this).find("input").prop("checked")) {
                        sum += parseFloat($(this).find(".total-item").html());
                     }
                });
                 // 当有店铺起批价时，若不满足店铺起批价，提示
                if(storeStartPrice > sum){
                    product.each( function () {
                        if ($(this).find("input").prop("checked")) {
                            $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                            //$(this).find("input").removeAttr("checked");
                        }
                    });
                }
                 // 当没有店铺起批价钱，或当店铺的起批价小于计算后的商品总价时，判断是否符合起批数量
                if (storeStartPrice <= sum) {
                    product.each( function () {
                        if ($(this).find("input").prop("checked")) {
                            var minNum = $(this).find(".min-num").html(),
                                currNum = $(this).find(".num-input").val();
                            if (Number(currNum) < Number(minNum)) {
                                $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                //$(this).find("input").removeAttr("checked");
                            }
                        }
                    });
                }
            });

            //判断店铺中商品是否全部选中，若未全部选中，则取消勾选店铺
            function cancelStoreSelect(){
                $(".order-list tbody tr[data-sort='store']").each(function(){
                    var index = $(this).data("index"),
                        products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                        checkedStatus = $(this).find("input[type='checkbox']").prop("checked"),
                        count = 0;
                    if(checkedStatus){
                        products.each(function(){
                            if(!$(this).find("input[type='checkbox']").prop("checked")){
                                count++;
                            }
                        });
                        if(count > 0){
                            $(this).find("input[type='checkbox']").removeAttr("checked");
                        }
                    }
                });
            }
            cancelStoreSelect();

            //判断店铺是否全部选中，若未全部选中，则取消勾选全选按钮
            function cancelAllSelect(){
                 var count = 0;
                 $(".order-list tbody tr[data-sort='store']").each(function(){
                     if(!$(this).find("input[type='checkbox']").prop("checked")){
                         count++;
                     }
                 });
                 // if(count > 0){
                 //     $(".check-all-header, .check-all-footer").removeAttr("checked");
                 // }else{
                 //     $(".check-all-header, .check-all-footer").prop("checked", "true");
                 // }
            }
            cancelAllSelect();

            //判断选中的商品是否符合要求，不符合，则结算按钮不能点击
            function setBtn(){
              var tag = 0;
               $(".order-list tbody tr[data-sort='store']").each( function () {
                     var _this = $(this),
                         storeStartPrice = _this.find(".store-price em").html(),
                         index = _this.data("index"),
                         sum = 0,
                         product = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']");

                     if (storeStartPrice == undefined) {
                          storeStartPrice = 0;
                     }
                     product.each( function () {
                         if ($(this).find("input").prop("checked")) {
                            sum += parseFloat($(this).find(".total-item").html());
                         }
                     });
                     // 当店铺没有起批价格时
                     if(storeStartPrice == 0){
                         product.each(function(){
                             if($(this).find("input[type='checkbox']").prop("checked")){
                                 var minNum = $(this).find(".min-num").html(),
                                     currNum = $(this).find(".num-input").val();
                                 if(Number(currNum) < Number(minNum)){
                                     tag++;
                                 }
                             }
                         });
                     }
                     // 当有店铺起批价时，若不满足店铺起批价，提示
                     if(storeStartPrice > sum){
                        product.each( function () {
                            if ($(this).find("input").prop("checked")) {
                                tag++;
                            }
                        });
                     }
                     // 当没有店铺起批价钱，或当店铺的起批价小于计算后的商品总价时，判断是否符合起批数量
                    if (storeStartPrice <= sum) {
                        product.each( function () {
                            if ($(this).find("input").prop("checked")) {
                                var minNum = $(this).find(".min-num").html(),
                                    currNum = $(this).find(".num-input").val();
                                if (Number(currNum) < Number(minNum)) {
                                    tag++;
                                }
                            }
                        });
                    }
                });
                if(tag > 0){
                    $(".go-result").addClass("bg-gray");
                }else{
                    $(".go-result").removeClass("bg-gray");
                }
                return tag;
            }
            setBtn();

			// 收藏操作
			$(".order-list td .collect").on("click", function () {
				var id = $(this).closest("tr").data("proid");
				if($(this).hasClass('follow')) {
					$(this).removeClass("follow").html("收藏");
					unFollow(id);
				} else {
					$(this).addClass("follow").html("取消收藏");
				    follow(id);
				}
			});

			// 删除操作
			$(".order-list td .del").on("click", function () {
			    var _this = $(this),
			        parentTr = _this.closest("tr"),
				    id = parentTr.data("skuid"),
				    ids = [id];
				$.message.confirm("确定要删除该商品吗？", "del", function(data){
				     if(data){
				         delProduct(ids);
				     }
				});

			});

			// 关联店铺名称和商品
			$(".check-all-list input[type='checkbox']").on("click", function () {
			    var trElement = $(this).closest('tr'),
				    index = trElement.data("index"),
					sort  = trElement.data("sort"),
					checkedStatus = $(this).prop("checked"),
					inventCount = trElement.data("count"),
					num = trElement.find(".num-input").val(),
					store = $(".order-list tbody tr[data-sort='store'][data-index='"+index+"']"),
                    products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                    storeStartPrice = store.find(".store-price em").html();
                if(storeStartPrice == undefined){
                    storeStartPrice = 0;
                }
				// 取消或者勾选店铺标签，店铺下所有产品改变选中状态
				if (sort == "store") {
				    var totalPrice = 0;
				    products.each( function () {
				        var inventCount = $(this).data("count"),
                            num = $(this).find(".num-input").val();
                        if(Number(num) <= Number(inventCount)){
                            totalPrice += parseFloat($(this).find(".total-item").html());
                        }
                    });
					if (checkedStatus) {
					    var count = 0;
						products.each( function () {
						    var inventCount = $(this).data("count"),
						        num = $(this).find(".num-input").val(),
						        flag1 = false,
						        flag2 = false,
						        flag3 = false;

						    if(Number(num) <= Number(inventCount)){  //购买数量小于等于库存时，才能选中
						        flag1 = true;
						    }else{
						        $(this).find(".tip .tip-count").html("库存不足");
						    }

						    if(flag1){
						        if(storeStartPrice == 0){
                                //没有店铺起批价格，当前购买数量大于起批数量，可以选中
                                    var minNum = $(this).find(".min-num").html(),
                                        currNum = $(this).find(".num-input").val();
                                    if (Number(currNum) >= Number(minNum)) {
                                       flag2 = true;
                                       $(this).find(".tip .tip-error").html("");
                                    }else{
                                       $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                    }
                                }
                                if(storeStartPrice > 0 && storeStartPrice <= totalPrice){  // 当前店铺选中商品总价格大于等于店铺起批价格时，可以选中
                                    var minNum = $(this).find(".min-num").html(),
                                        currNum = $(this).find(".num-input").val();
                                    if (Number(currNum) >= Number(minNum)) {
                                       flag3 = true;
                                       $(this).find(".tip .tip-error").html("");
                                    }else{
                                       $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                    }
                                }
                                if(storeStartPrice > totalPrice){
                                    $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                }
						        $(this).find("input[type='checkbox']").prop("checked",'true');
						    }
						});

						var total = 0;
						products.each(function(){
                            if(!$(this).find("input[type='checkbox']").prop("checked")){
                                count++;
                            }else{
                                total += parseFloat($(this).find(".total-item").html());
                            }
                        });
                        if(count > 0){
                            $(this).removeAttr("checked");
                        }

					} else {
						$(".order-list tr").each( function () {
							if($(this).data("index") == index) {
								$(this).find("input[type='checkbox']").removeAttr("checked");
							}
						});
					}
				}

				//不满足起批数量或起送金额，提示
				if(sort == "product" && checkedStatus){
                     // 当没有店铺起批价钱，或店铺的起批价大于计算后的起批价，无法形成混批模式时，判断是否符合起批数量
                    var sum = 0,
                        count = 0,
                        flag = true,
                        skuid = trElement.data("skuid");
                    //库存不足，无法选中
                    if(Number(num) > Number(inventCount)){
                        $(this).removeAttr("checked");
                        trElement.find(".tip .tip-count").html("库存不足");
                        flag = false;
                    }
                    products.each( function () {
                        if ($(this).find("input").prop("checked")) {
                            sum += parseFloat($(this).find(".total-item").html());
                        }
                    });

                    if(storeStartPrice > 0){
                        if(storeStartPrice > sum){
                            products.each(function(){
                               if ($(this).find("input").prop("checked")) {
                                 var id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }
                                 }else{
                                     $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                 }
                               }
                            });
                        }else if (storeStartPrice <= sum) {
                            products.each(function(){
                               if ($(this).find("input").prop("checked")) {
                                 var minNum = $(this).find(".min-num").html(),
                                     currNum = $(this).find(".num-input").val(),
                                     id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         if (Number(currNum) < Number(minNum)) {
                                             $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                         }else{
                                             $(this).find(".tip .tip-error").html("");
                                         }
                                     }
                                 }else{
                                     if (Number(currNum) < Number(minNum)) {
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }else{
                                         $(this).find(".tip .tip-error").html("");
                                     }
                                 }
                               }
                            });
                        }
                    }else{
                        var minNum = trElement.find(".min-num").html(),
                            currNum = trElement.find(".num-input").val();
                        if (Number(currNum) < Number(minNum)) {
                            trElement.find(".tip .tip-error").html("起批数量或起送金额不满足");
                        }else{
                            trElement.find(".tip .tip-error").html("");
                        }
                    }

                    if($(this).prop("checked")){
                        products.each(function(){
                            if(!$(this).find("input[type='checkbox']").prop("checked")){
                                count++;
                            }
                        });
                        if(count > 0){
                            store.find("input[type='checkbox']").removeAttr("checked");
                        }else{
                            store.find("input[type='checkbox']").prop("checked", "true");
                        }
                    }
				}

				//取消选中时，判断店铺其它商品是否满足起批数量或起批金额
				if(sort == "product" && !checkedStatus){
				    var sum = 0,
				        total = 0;
                    products.each( function () {
                        if ($(this).find("input").prop("checked")) {
                            sum += parseFloat($(this).find(".total-item").html());
                        }
                    });
                    if(storeStartPrice > sum){
                        products.each(function(){
                           if ($(this).find("input").prop("checked")) {
                              $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                           }
                        });
                    }
                    store.find("input[type='checkbox']").removeAttr("checked");
				}

				cancelAllSelect();
				getTotal();
				setBtn();
				isGoshop();
                addActive();
			});

			//点击顶部全选按钮
		    $(".check-all-header").on("click", function(){
                if($(this).prop("checked")) {
                     $(".check-all-list tr").each(function(){
                         var sort = $(this).data("sort"),
                             index = $(this).data("index"),
                             inventCount = $(this).data("count"),
                             num = $(this).find(".num-input").val(),
                             flag1 = false,
                             flag2 = false,
                             flag3 = false;

                         if(sort == "product"){
                             var store = $(".order-list tbody tr[data-sort='store'][data-index='"+index+"']"),
                                 products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                                 storeStartPrice = store.find(".store-price em").html(),
                                 totalPrice = 0;

                             if(storeStartPrice == undefined){
                                 storeStartPrice = 0;
                             }

                             products.each( function () {
                                 var inventCount = $(this).data("count"),
                                     num = $(this).find(".num-input").val();
                                 if(Number(num) <= Number(inventCount)){
                                     totalPrice += parseFloat($(this).find(".total-item").html());
                                 }
                             });
                             if(Number(num) <= Number(inventCount)){ //购买数量小于等于库存，才能选中
                                 flag1 = true;
                             }else{
                                 $(this).find(".tip .tip-count").html("库存不足");
                             }

                             if(flag1){
                                 if(storeStartPrice == 0){
                                //没有店铺起批价格，或店铺起批价格大于购买总价格时，当前购买数量大于起批数量，可以选中
                                    var minNum = $(this).find(".min-num").html(),
                                        currNum = $(this).find(".num-input").val();
                                    if (Number(currNum) >= Number(minNum)) {
                                       flag2 = true;
                                       $(this).find(".tip .tip-error").html("");
                                    }else{
                                       $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                    }
                                 }
                                 if(storeStartPrice > totalPrice){
                                     $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                 }
                                 if(storeStartPrice > 0 && storeStartPrice <= totalPrice){  // 当前店铺选中商品总价格大于等于店铺起批价格时，可以选中
                                    var minNum = $(this).find(".min-num").html(),
                                        currNum = $(this).find(".num-input").val();
                                    if (Number(currNum) >= Number(minNum)) {
                                       flag3 = true;
                                       $(this).find(".tip .tip-error").html("");
                                    }else{
                                       $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                    }
                                 }
                                 $(this).find("input[type='checkbox']").prop("checked",'true');
                             }
                         }
                     });

                     $(".order-list tbody tr[data-sort='store']").each(function(){
                         var index = $(this).data("index"),
                             products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                             count = 0,
                             total = 0,
                             storeStartPrice = $(this).find(".store-price em").html();
                         if(storeStartPrice == undefined){
                             storeStartPrice = 0;
                         }
                         products.each(function(){
                             if(!$(this).find("input[type='checkbox']").prop("checked")){
                                 count++;
                             }else{
                                 total += parseFloat($(this).find(".total-item").html());
                             }
                         });
                         if(count == 0){
                             $(this).find("input[type='checkbox']").prop("checked",'true');
                         }

                     });

                     cancelAllSelect();
                     if($(".check-all-header").prop("checked")){
                         $(".check-all-footer").prop("checked",'true');
                     }
                } else {
                     $(".check-all-list").find("input[type='checkbox']").removeAttr("checked");
                     $(".check-all-footer").removeAttr("checked");
                }
                getTotal();
                setBtn();
                isGoshop();
                addActive();
            });

            //点击底部全选按钮
            $(".check-all-footer").on("click", function(){
                if($(this).prop("checked")) {
                     $(".check-all-list tr").each(function(){
                         var sort = $(this).data("sort"),
                             index = $(this).data("index"),
                             inventCount = $(this).data("count"),
                             num = $(this).find(".num-input").val(),
                             flag1 = false,
                             flag2 = false,
                             flag3 = false;

                         if(sort == "product"){
                             var store = $(".order-list tbody tr[data-sort='store'][data-index='"+index+"']"),
                                 products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                                 storeStartPrice = store.find(".store-price em").html(),
                                 totalPrice = 0;

                             if(storeStartPrice == undefined){
                                 storeStartPrice = 0;
                             }

                             products.each( function () {
                                 var inventCount = $(this).data("count"),
                                     num = $(this).find(".num-input").val();
                                 if(Number(num) <= Number(inventCount)){
                                     totalPrice += parseFloat($(this).find(".total-item").html());
                                 }
                             });
                             if(Number(num) <= Number(inventCount)){ //购买数量小于等于库存，才能选中
                                 flag1 = true;
                             }else{
                                 $(this).find(".tip .tip-count").html("库存不足");
                             }

                             if(flag1){
                                 if(storeStartPrice == 0){
                                    //没有店铺起批价格，或店铺起批价格大于购买总价格时，当前购买数量大于起批数量，可以选中
                                    var minNum = $(this).find(".min-num").html(),
                                        currNum = $(this).find(".num-input").val();
                                    if (Number(currNum) >= Number(minNum)) {
                                       flag2 = true;
                                       $(this).find(".tip .tip-error").html("");
                                    }else{
                                       $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                    }
                                 }
                                 if(storeStartPrice > totalPrice){
                                    $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                 }
                                 if(storeStartPrice > 0 && storeStartPrice <= totalPrice){  // 当前店铺选中商品总价格大于等于店铺起批价格时，可以选中
                                    var minNum = $(this).find(".min-num").html(),
                                        currNum = $(this).find(".num-input").val();
                                    if (Number(currNum) >= Number(minNum)) {
                                       flag3 = true;
                                       $(this).find(".tip .tip-error").html("");
                                    }else{
                                       $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                    }
                                 }
                                 $(this).find("input[type='checkbox']").prop("checked",'true');
                             }
                         }
                     });
                     $(".order-list tbody tr[data-sort='store']").each(function(){
                         var index = $(this).data("index"),
                             products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                             count = 0,
                             total = 0,
                             storeStartPrice = $(this).find(".store-price em").html();
                         if(storeStartPrice == undefined){
                             storeStartPrice = 0;
                         }
                         products.each(function(){
                             if(!$(this).find("input[type='checkbox']").prop("checked")){
                                 count++;
                             }else{
                                 total += parseFloat($(this).find(".total-item").html());
                             }
                         });
                         if(count == 0){
                             $(this).find("input[type='checkbox']").prop("checked",'true');
                         }

                     });

                     cancelAllSelect();
                     if($(".check-all-footer").prop("checked")){
                         $(".check-all-header").prop("checked",'true');
                     }
                } else {
                     $(".check-all-list").find("input[type='checkbox']").removeAttr("checked");
                     $(".check-all-header").removeAttr("checked");
                }
                getTotal();
                setBtn();
                isGoshop();
                addActive();
            });

			// 增减功能按钮和输入框
			var nextBtn = $(".order-list .group .item .next"),
				prevBtn = $(".order-list .group .item .prev"),
				inputBtn = $(".order-list .group .item input");

			// 增减功能增加按钮
			nextBtn.on("click", function () {
			    prevBtn.removeClass('disable');
				var _this = $(this),
					inputObj = _this.prev("input"),
					initialVal = inputObj.val(),
					inventCount = _this.closest("tr").data("count"),
					trElement = _this.closest("tr"),
                    index = trElement.data("index"),
                    skuid = trElement.data("skuid"),
                    store = $(".order-list tbody tr[data-sort='store'][data-index='"+index+"']"),
                    products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                    storeStartPrice = store.find(".store-price em").html(),
                    count = 0,
                    checkedStatus = _this.closest("tr").find("input[type='checkbox']").prop("checked"),
                    price = parseFloat(trElement.find(".total-item").html()),
                    flag = true;

                if(storeStartPrice == undefined){
                    storeStartPrice = 0;
                }

				publicVar(inputObj, _this);
				inputValue++;
				inputObj.val(inputValue);
                numText.html(inputValue);
                totalItem.html((inputValue * proNumber).toFixed(2));
				if (inputValue >= minNumber) {
					_this.closest('tr').find("p").addClass("active");
					_this.closest("td").find(".tip .tip-error").html("");
				}

				//库存不足，取消选中并提示
				if(inputValue > Number(inventCount)){
                    _this.closest("td").find(".tip .tip-count").html("库存不足");
                    _this.closest("tr").find("input[type='checkbox']").removeAttr("checked");
                    flag = false;
                }else{
                    _this.closest("td").find(".tip .tip-count").html("");
                }

				if(flag){
                    trElement.find("input[type='checkbox']").prop("checked",'true');
                }
                if(storeStartPrice == 0){
                    var minNum = trElement.find(".min-num").html(),
                        currNum = trElement.find(".num-input").val();
                    if (Number(currNum) < Number(minNum)) {
                        trElement.find(".tip .tip-error").html("起批数量或起送金额不满足");
                    }else{
                        trElement.find(".tip .tip-error").html("");
                    }
                }

                if(storeStartPrice > 0){
                     var totalPrice = 0;
                     products.each( function () {
                         if ($(this).find("input").prop("checked")) {
                             totalPrice += parseFloat($(this).find(".total-item").html());
                         }
                     });

                     if(storeStartPrice <= totalPrice){
                         products.each(function(){
                             if ($(this).find("input").prop("checked")) {
                                 var minNum = $(this).find(".min-num").html(),
                                     currNum = $(this).find(".num-input").val(),
                                     id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         if (Number(currNum) < Number(minNum)) {
                                             $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                         }else{
                                             $(this).find(".tip .tip-error").html("");
                                         }
                                     }
                                 }else{
                                     if (Number(currNum) < Number(minNum)) {
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }else{
                                         $(this).find(".tip .tip-error").html("");
                                     }
                                 }
                              }
                         });
                     }

                     if(storeStartPrice > totalPrice){
                         products.each(function(){
                             if ($(this).find("input").prop("checked")) {
                                 var id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }
                                 }else{
                                     $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                 }
                              }
                         });
                     }
                }

                 products.each(function(){
                     if(!$(this).find("input[type='checkbox']").prop("checked")){
                         count++;
                     }
                 });
                 if(count > 0){
                     store.find("input[type='checkbox']").removeAttr("checked");
                 }else{
                     store.find("input[type='checkbox']").prop("checked", "true");
                 }

                 cancelAllSelect();
				 getTotal();
				 setBtn();
				 isGoshop();
                 addActive();
			});

			// 增减功能减少按钮
			prevBtn.on("click", function () {
				var _this = $(this),
				    trElement = _this.closest("tr"),
					inputObj = _this.next("input"),
					initialVal = inputObj.val(),
					inventCount = trElement.data("count"),
					index = trElement.data("index"),
					skuid = trElement.data("skuid"),
					store = $(".order-list tbody tr[data-sort='store'][data-index='"+index+"']"),
					products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                    storeStartPrice = store.find(".store-price em").html(),
                    count = 0,
                    checkedStatus = trElement.find("input").prop("checked"),
                    price = parseFloat(trElement.find(".total-item").html()),
                    flag = true;
                if(storeStartPrice == undefined){
                    storeStartPrice = 0;
                }

				publicVar(inputObj, _this);
				if (inputValue <= minNumber) {
					_this.addClass("disable");
					inputValue = minNumber;
					return;
				} else {
					inputValue--;
				}

				inputObj.val(inputValue);
                numText.html(inputValue);
                totalItem.html((inputValue * proNumber).toFixed(2));

				if(inputValue <= Number(inventCount)){
				    _this.closest("td").find(".tip .tip-count").html("");
				} else {
				    _this.closest("td").find(".tip .tip-count").html("库存不足");
				    flag = false;
				}

				if(inputValue < minNumber){
				    _this.closest('tr').find("p").removeClass("active");
				}

                if(flag){
                    trElement.find("input[type='checkbox']").prop("checked",'true');
                }
                if(storeStartPrice == 0){
                    var minNum = trElement.find(".min-num").html(),
                        currNum = trElement.find(".num-input").val();
                    if (Number(currNum) < Number(minNum)) {
                        trElement.find(".tip .tip-error").html("起批数量或起送金额不满足");
                    }else{
                        trElement.find(".tip .tip-error").html("");
                    }
                }

                if(storeStartPrice > 0){
                     var totalPrice = 0;
                     products.each( function () {
                         if ($(this).find("input").prop("checked")) {
                             totalPrice += parseFloat($(this).find(".total-item").html());
                         }
                     });

                     if(storeStartPrice <= totalPrice){
                         products.each(function(){
                             if ($(this).find("input").prop("checked")) {
                                 var minNum = $(this).find(".min-num").html(),
                                     currNum = $(this).find(".num-input").val(),
                                     id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         if (Number(currNum) < Number(minNum)) {
                                             $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                         }else{
                                             $(this).find(".tip .tip-error").html("");
                                         }
                                     }
                                 }else{
                                     if (Number(currNum) < Number(minNum)) {
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }else{
                                         $(this).find(".tip .tip-error").html("");
                                     }
                                 }
                              }
                         });
                     }

                     if(storeStartPrice > totalPrice){
                         products.each(function(){
                             if ($(this).find("input").prop("checked")) {
                                 var id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }
                                 }else{
                                     $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                 }
                              }
                         });
                     }
                }

                products.each(function(){
                     if(!$(this).find("input[type='checkbox']").prop("checked")){
                         count++;
                     }
                });
                if(count > 0){
                     store.find("input[type='checkbox']").removeAttr("checked");
                }else{
                     store.find("input[type='checkbox']").prop("checked", "true");
                }

                cancelAllSelect();
				getTotal();
				setBtn();
				isGoshop();
                addActive();
			});

			// 增减功能输入框
			inputBtn.on("change", function () {
				var _this = $(this),
					inputObj = _this,
					reg = /^[1-9]\d*$/,
					trElement = _this.closest("tr"),
					inventCount = trElement.data("count"),
					index = trElement.data("index"),
					skuid = trElement.data("skuid"),
                    store = $(".order-list tbody tr[data-sort='store'][data-index='"+index+"']"),
                    products = $(".order-list tbody tr[data-sort='product'][data-index='"+index+"']"),
                    storeStartPrice = store.find(".store-price em").html(),
                    count = 0,
                    checkedStatus = trElement.find("input").prop("checked"),
                    price = parseFloat(trElement.find(".total-item").html()),
                    flag = true,
                    tempObj = {};


                if(storeStartPrice == undefined){
                    storeStartPrice = 0;
                }
				publicVar(inputObj, _this);

				if(isNaN(inputValue)) {
					inputValue = minNumber;
				}
				if (inputValue <= minNumber) {
					prevBtn.addClass('disable');
					inputValue = minNumber;
				}else{
				    prevBtn.removeClass('disable');
				}
				if(!reg.test(inputValue)){
                    inputValue = minNumber;
                }

                inputObj.val(inputValue);
                numText.html(inputValue);
                totalItem.html((inputValue * proNumber).toFixed(2));

				if (inputValue >= minNumber) {
					_this.closest('tr').find("p").addClass("active");
					_this.closest("td").find(".tip .tip-error").html("");
				} else {
					_this.closest('tr').find("p").removeClass("active");
				}
				if(inputValue > Number(inventCount)){  //库存不足，取消选中并提示
				    _this.closest("td").find(".tip .tip-count").html("库存不足");
				    _this.closest("tr").find("input[type='checkbox']").removeAttr("checked");
				    flag = false;
				}else{
				    _this.closest("td").find(".tip .tip-count").html("");
				}

				if(inputValue < minNumber){
                    _this.closest("td").find(".tip .tip-error").html("起批数量或起送金额不满足");
                }

                if(flag){
                    trElement.find("input[type='checkbox']").prop("checked",'true');
                }
                if(storeStartPrice == 0){
                    var minNum = trElement.find(".min-num").html(),
                        currNum = trElement.find(".num-input").val();
                    if (Number(currNum) < Number(minNum)) {
                        trElement.find(".tip .tip-error").html("起批数量或起送金额不满足");
                    }else{
                        trElement.find(".tip .tip-error").html("");
                    }
                }

                if(storeStartPrice > 0){
                     var totalPrice = 0;
                     products.each( function () {
                         if ($(this).find("input").prop("checked")) {
                             totalPrice += parseFloat($(this).find(".total-item").html());
                         }
                     });

                     if(storeStartPrice <= totalPrice){
                         products.each(function(){
                             if ($(this).find("input").prop("checked")) {
                                 var minNum = $(this).find(".min-num").html(),
                                     currNum = $(this).find(".num-input").val(),
                                     id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         if (Number(currNum) < Number(minNum)) {
                                             $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                         }else{
                                             $(this).find(".tip .tip-error").html("");
                                         }
                                     }
                                 }else{
                                     if (Number(currNum) < Number(minNum)) {
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }else{
                                         $(this).find(".tip .tip-error").html("");
                                     }
                                 }
                              }
                         });
                     }

                     if(storeStartPrice > totalPrice){
                         products.each(function(){
                             if ($(this).find("input").prop("checked")) {
                                 var id = $(this).data("skuid");
                                 if(id == skuid){
                                     if(flag){
                                         $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                     }
                                 }else{
                                     $(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
                                 }
                              }
                         });
                     }
                }

                products.each(function(){
                     if(!$(this).find("input[type='checkbox']").prop("checked")){
                         count++;
                     }
                });
                if(count > 0){
                     store.find("input[type='checkbox']").removeAttr("checked");
                }else{
                     store.find("input[type='checkbox']").prop("checked", "true");
                }

                cancelAllSelect();
				getTotal();
				setBtn();
				isGoshop();
                addActive();
			});

			//存储原来的数量
			var initNumber = 0;

			$(".order-list .group .item .prev, .order-list .group .item .next").on("mouseenter", function () {
				initNumber = $(this).siblings(".num-input").val();
			});

			$(".order-list .group .item .prev, .order-list .group .item .next").on("mouseleave", function () {
				var num = $(this).siblings(".num-input").val();
				if (num <= 0) {
					num = 1;
				}
				if(Number(num) != Number(initNumber)){
					var skuid = $(this).closest("tr").data("skuid");
					tempObj = {"skuid": skuid, "count" : num};
					updateCart(tempObj);
				}
			});

			$(".order-list .group .item input").on("keyup", function () {
				var num = $(this).val();
				if (num <= 0) {
					num = 1;
				}
				if(Number(num) != Number(initNumber)){
					var skuid = $(this).closest("tr").data("skuid");
					tempObj = {"skuid": skuid, "count" : num};
					updateCart(tempObj);
				}
			});

			$(".order-list .group .item input").on("blur", function () {
				var num = $(this).val();
				if (num <= 0) {
					num = 1;
				}
				var skuid = $(this).closest("tr").data("skuid");
				tempObj = {"skuid": skuid, "count" : num};
				updateCart(tempObj);
			});

			//更新购物车数量
			function updateCart(tempArr){
			    $.ajax({
                    type : "POST",
                    url: "/cart/updateCartAmount",
                    async: true,
                    contentType: "application/json",
                    data : JSON.stringify({
                        "cartItemDTOs" : [tempArr]
                    }),
                    success: function (data) {
                       // console.log(data);
                    }
                });
			}

			// 增减功能公用变量
			function publicVar(inputObj, _this) {
				inputValue = inputObj.val(),
				numText = _this.closest("tr").find(".num-input"),
				minNumber = _this.closest("tr").find(".min-num").html(),
				minNumber = Number(minNumber),
				proNumber = _this.closest("tr").find(".pro-price").html(),
				proNumber = Number(proNumber),
				totalItem = _this.closest('tr').find(".total-item");
			}

			// 计算总价
			var total = $(".order-info .total"),
				freight = $(".order-info .freight"),
				result = $(".order-info .result span"),
				resultUnit = 0,
				freightVal = 0;

			// 根据每条订单的单价计算总价
			function getTotal() {
				resultUnit = 0;
				$(".order-list .total-item").each(function () {
				   var _this = $(this);
				   if(_this.closest("tr").find("input[type='checkbox']").prop("checked")){
				      resultUnit = resultUnit.add(parseFloat(Number(_this.html())));
				   }
				});

				total.html(resultUnit.toFixed(2));
				freight.html(freightVal);
				result.html(resultUnit.add(freightVal).toFixed(2));
			}
			getTotal();

			// 底部删除按钮
			var delAll = $(".order-info .del-order");
			delAll.on("click", function () {
			    var ids = [],
                    flag = false;
                if ($(".check-all-footer[type='checkbox']").prop("checked")) {
                    flag = true;
                } else {
                    flag = false;
                }
			    $(".order-list tbody tr[data-sort='product']").each(function (){
                    if($(this).find("input[type='checkbox']").prop("checked")){
                       var _this = $(this),
                           id = _this.data("skuid");
                       ids.push(id);
                    }
                });
                if(ids.length > 0){
                    $.message.confirm("确定要删除所选商品吗？", "del", function(data){
                         if(data){
                            if (flag) {
                                delAllOrder();
                            } else {
                                delProduct(ids);
                            }
                         }
                    });
                }else{
                    $.message.alert("您还没选择任何商品", "info");
                }
			});

			// 全部删除接口
			function delAllOrder() {
				$.ajax({
					type : "GET",
					url : "/cart/deleteCart",
					async : true,
					contentType : "application/json",
					data : {},
					success : function (data) {
						if(data.code == 200) {
							//$.message.alert("删除成功！", "success");
							window.location.href = "/cartlist";
						}
					}
				});
			}

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
                        	$.message.alert("已添加到<span style='font-weight:bold;'>&nbsp;我的收藏</span>", "success");
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

            // 删除接口
            function delProduct(id) {
            	$.ajax({
                    type : "POST",
                    url : "/cart/deleteProduct",
                    async: true,
                    contentType: "application/json",
                    data: JSON.stringify({
                    	"ids" : id
                    }),
                    success : function (data) {
                        if (data.code == 200) {
							window.location.reload(true);
                        }
                    }
                });
            }

            // 获取购物车数量
            function getCartNum() {
                var cartNum = 0;
                $.ajax({
                    type : "GET",
                    url: " /cart/getcount",
                    async: false,
                    dataType: "json",
                    data : {},
                    success: function (data) {
                        if (data.code == 200) {
                            cartNum = data.result;
                        }
                    }
                });
                return cartNum;
            }
			isGoshop();
            addActive();

            // 限购
            var limit = true;
            function proLimit() {
                var len = $("tr.active .limit").length;
                if (len > 0) {
                    for (var i = 0; i < len; i++) {
                        var sTime = $($("tr.active .limit")[i]).data("stime"),
                            eTime = $($("tr.active .limit")[i]).data("etime"),
                            nTime = Date.parse(new Date()),
                            buyNum = $($("tr.active .limit")[i]).data("buynum"),
                            minNum = $($("tr.active .limit")[i]).closest('tr').find(".min-num").html(),
                            currNum = $($("tr.active .limit")[i]).closest('tr').find(".num-input").val();

                        if (nTime < sTime) {
                            limit = false;
                            $.message.alert("您所选购的商品购买时限还没开始，无法继续购买", "info");
                            return;
                        } else if (nTime > eTime) {
                            limit = false;
                            $.message.alert("所选购的商品购买时限已经结束，无法继续购买", "info");
                            return;
                        } else {
                            if (Number(currNum) >= Number(minNum) && Number(currNum) <= Number(buyNum)) {
                                limit = true;
                            } else {
                                limit = false;
                                $.message.alert("您所选购的商品超过了限定数量", "info");
                                return;
                            }
                        }
                    }
                } else {
                    limit = true;
                }
                return limit;
            }


			// 结算订单
			var skusPrice = "";
	        $(".go-result").click( function() {
	            if (!goFlag) {
	            	return;
	            }
	            var tag = setBtn();
	            if(tag > 0){
	               return;
	            }
                proLimit();
                if (!limit) {
                    return;
                }
	        	var skuid = "",
					count = "",
					tempObj = {},
					tempArr1 = [],
					tempArr2 = [],
					tempArr3 = [],
					tempArr = [],
					cartIds = "",
					prices = [],
				    prices1 = [],
				    prices2 = [],
					// 店铺起批价钱数组
					storeStartPriceArr = [],
					// 实际店铺起批价钱数组
					realStorePriceArr = [],
					flag = true,
					t = false;


			    //判断有没有选中商品
			    $(".order-list tbody tr[data-sort='product']").each(function (){
			        if($(this).find("input[type='checkbox']").prop("checked")){
			           t = true;
			        }
			        if(t) return false;
			    });
			    if(!t){
			       $.message.alert("请选择要结算的商品！", "info");
			       return;
			    }

				// 结算前先判断是否满足店铺起批价钱
				$(".order-list tbody tr[data-sort='store']").each( function () {
					var storeStartPrice =  $(this).find(".store-price em").html();
					if (storeStartPrice == undefined) {
						storeStartPrice = 0;
					}
					storeStartPriceArr.push(storeStartPrice);
				});
				//console.log(storeStartPriceArr);

				var storeList = $(".order-list tbody tr[data-sort='store']");
				for (var i = 0; i < storeList.length; i++) {
					var index = $(storeList[i]).data("index")
						sum = 0;
					$(".order-list tbody tr[data-sort='product'][data-index='"+index+"']").each( function () {
						if ($(this).find("input").prop("checked")) {
							sum += parseFloat($(this).find(".total-item").html());
						}
					});
					realStorePriceArr.push(sum);
				}

				for (var i = 0; i < storeStartPriceArr.length; i++) {
				    if(!flag) break;
					/*
						* 8.18修改
						* 需求由原来的混批，改成必须满足同时满足起批量和起批价
					*/
					// 当没有店铺起批价钱时，判断是否符合起批数量
					if (storeStartPriceArr[i] == 0) {
						var index = $(".order-list tbody tr[data-sort='store']").eq(i).data("index");
						//var flag1 = false;
						$(".order-list tbody tr[data-sort='product'][data-index='"+index+"']").each( function () {
							//if (flag1) return false;
							if ($(this).find("input").prop("checked")) {
								var minNum = $(this).find(".min-num").html(),
									currNum = $(this).find(".num-input").val();
								if (Number(currNum) < Number(minNum)) {
									$(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
									//flag1 = true;
									//flag = false;
								} else {
									var skuid = $(this).closest('tr').data("skuid"),
										count = $(this).closest('tr').find(".num-input").val(),
										price = $(this).closest('tr').find(".pro-price").html();
										tempObj = {"skuid": skuid, "count" : count};
										tempArr1.push(tempObj);
										prices1.push(price);
										//console.log(prices1);
									//flag = true;
								}
							}
						});
					// 当有店铺起批价钱时
					} else {
						// 总价必须大于店铺的起批价，同时数量也要大于起批量
						if (realStorePriceArr[i] >= storeStartPriceArr[i]) {
							var index = $(".order-list tbody tr[data-sort='store']").eq(i).data("index");
							//var flag1 = false;
	 						$(".order-list tbody tr[data-sort='product'][data-index='"+index+"']").each( function () {
	 						   // if (flag1) return false;
								if ($(this).find("input").prop("checked")) {
									var minNum = $(this).find(".min-num").html(),
										currNum = $(this).find(".num-input").val();
										if (Number(currNum) < Number(minNum)) {
											$(this).find(".tip .tip-error").html("起批数量或起送金额不满足");
										} else {
											var skuid = $(this).closest('tr').data("skuid"),
												count = $(this).closest('tr').find(".num-input").val(),
												price = $(this).closest('tr').find(".pro-price").html();
												tempObj = {"skuid": skuid, "count" : count};
												tempArr3.push(tempObj);
												prices2.push(price);
										}
								}
							});
						}
					}
				}
				tempArr = tempArr1.concat(tempArr3);
				prices = prices1.concat(prices2);

				var skusId = [];
				for ( var i = 0; i < tempArr.length; i++) {
					cartIds += tempArr[i].skuid + ",";
					skusId.push(tempArr[i].skuid);
				}
				cartIds = cartIds.substring(0, cartIds.length-1);

				for (var i = 0; i < prices.length; i++) {
					skusPrice = "";
					skusPrice += skusId[i] + "|" + prices[i] + ",";
				}
				skusPrice = skusPrice.substring(0, skusPrice.length - 1);
				addToCart(cartIds);
	        });

	        // 结算前将修改过的skuid对应的  数量重新添加到购物车
			function addToCart(cartIds) {
	        	$(".cartIds").val(cartIds);
	        	$(".skusPrice").val(skusPrice);
	            $("#postForm").submit();
			}

		});
	</script>
</body>
</html>
</@compress>
</#escape>