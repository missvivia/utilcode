<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
		<#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-搜索列表</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
        <@less />
        <link rel="stylesheet" type="text/css" href="/src/css/page/search.css?v=1.0.0.2"/>
    </head>
    <body class="other-search">
        <div class="wrap" id="wrap">
            <header class='back-head fixed-head'>
                <span class='go-back'></span>
                <em></em>
                <span class="search" style='right: 0.5rem;'></span>
            </header>
            <div class="filter list-filter back-head-height">
                <ul>
                    <li data-type='sale' class='active'>销量</li>
                    <li data-type='time'>上架时间</li>
                    <li data-type='filler'>筛选</li>
                </ul>
            </div>
            <div class="category-brand popup-local">
                <#if brandList??>
                    <dl id="brand-list">
                        <dt>品牌<span class='more'></span></dt>
                        <#list brandList as item>
                            <#if item.index="all">
                                <#list item.list as list>
                                    <dd data-brandId="${list.brandId}"><span>${list.brandNameAuto}</span></dd>
                                </#list>
                            </#if>
                        </#list>
                    </dl>
                </#if>
                <div class='action'>
                    <span class="clear-select">清空所有选项</span>
                    <span id="btn-true">确定</span>
                </div>
            </div>
            <div class="product-list" id='proList'>
                <ul></ul>
            </div>
            <div class="load-page">
                <span>
                    <em class='curr'>1</em>/<em class='total'>20</em>
                </span>
            </div>
            <div class="loading">
                <i></i>
                <p>小百正在玩命加载中，请耐心等待一会儿哦~</p>
            </div>
            <div class="pull-down">
                <i></i>
                <span>正在加载...</span>
            </div>
            <@fixRight />
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
    <script src="/src/frame/jquery.js"></script>
    <script src="/src/frame/hammer.min.js"></script>
    <script src="/src/js/wap.js"></script>
    <script type="text/javascript">
        $(function () {

    		var url 				= window.location.href,
				key1 				= url.indexOf("search/"),
				urlLen 				= url.length,
				box 				= $(".product-list ul"),
				searchVal 			= decodeURI(location.search.replace("?keyword=","")),
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
            $("header em").text(searchVal);

            /* format param */
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
                /* remove null param */
                for (var i in obj) {
                    if (obj[i] === "") {
                        delete obj[i];
                    }
                }
                return obj;
            }

            /* get product list */
            function getListData() {
                createObj();
                if (flag) {
                    $.ajax({
                        url: "/rest/product/list",
                        type: "GET",
                        dataType: "json",
                        data: {
                            pageSize            : obj.pageSize,
                            currentPage         : obj.currentPage,
                            isPage              : obj.isPage,
                            categoryContentId   : obj.categoryContentId,
                            categoryNormalId    : obj.categoryNormalId,
                            sortColumn          : obj.sortColumn,
                            isAsc               : obj.isAsc,
                            brandIds            : obj.brandIds,
                            searchValue         : obj.searchVal
                        },
                        beforeSend: function () {
                            flag = false;
                            if (obj.currentPage == 1) {
                                $(".loading").show();
                            } else {
                                $(".pull-down").show();
                                $(".load-page").show();
                            }
                        },
                        success: function (data) {
                            if (data.code == 200) {
                                createList(data);
                                $(".load-page").hide();
                            } else {
                                return;
                            }
                        }
                    });
                }
                return currentPage;
            }
            getListData();

            /* create product list */
            function createList(data) {
                console.log(data);
                var list = data.result.list,
                    dom = "",
                    nTime = Date.parse(new Date());

                if (list != null) {
                    total = Math.ceil(data.result.total / pageSize);
                    for ( var i = 0; i < list.length; i++) {

                        if (list[i].skuLimitConfigVO != null) {
                            var sTime = list[i].skuLimitConfigVO.limitStartTime,
                                eTime = list[i].skuLimitConfigVO.limitEndTime,
                                allowBuyNum = list[i].skuLimitConfigVO.allowBuyNum;
                        }

                        dom += "<li data-skuid='"+ list[i].skuId +"' data-skunum='"+ list[i].skuNum +"' data-minNum='"+ list[i].batchNum +"'>"+
                                    "<a class='pic' href='/product/detail?skuId="+ list[i].skuId +"'>"+
                                        "<img imgsrc='/src/img/icon/down-logo.png' src='"+ list[i].picList[0].picPath + thumbnailParam('70', '155', '155')+"' />";

                        if (list[i].skuLimitConfigVO != null) {
                            if (nTime < sTime) {
                                dom += "<span class='limit'>限购即将开始</a>";
                            } else if (nTime > eTime) {
                                dom += "<span class='limit'>限购已结束</a>";
                            } else {
                                dom += "";
                            }
                        } else {
                            dom += "";
                        }

                        dom += "</a>"+
                            "<a href='/product/detail?skuId="+ list[i].skuId +"' class='name' title='"+ list[i].productName +"'>"+ list[i].productName +"</a>"+
                            "<span class='title'>"+ list[i].productTitle +"</span>"+
                            "<p>"+
                                "<span class='price'><em>¥</em>"+ list[i].priceList[0].prodPrice +"</span>"+
                                "<a href='/store/"+ list[i].supplierId +"/' class='storeName'>"+ list[i].storeName +"</a>"+
                            "</p>";

                        if (list[i].skuLimitConfigVO != null) {
                            if (nTime < sTime) {
                                dom += "<span class='add-cart disable'></span>";
                            } else if (nTime > eTime) {
                                dom += "<span class='add-cart disable'></span>";
                            } else {
                                dom += "<span class='add-cart'></span>";
                            }
                        } else {
                            dom += "<span class='add-cart'></span>";
                        }


                        if (list[i].skuNum < list[i].batchNum) {
                            dom += "<span class='sold-out'><i></i></span>";
                        }
                        dom += "</li>";
                    }
                    currentPage++;
                    $(".load-page .curr").html(currentPage);
                    $(".load-page .total").html(total);
                } else {
                    total = null;
                    dom += "<div class='null-page'>"+
                        "<i class='category-list-null'></i>"+
                        "<p>很遗憾，您搜索的“<span>"+ searchVal +"</span>”暂无商品！</p>"+
                    "</div>";
                }

                $(".loading").hide();
                $(".pull-down").hide();
                flag = true;
                box.append(dom);
                nullPage();
            }

           /* filter tool */
            var windowHeight = $(window).height(),
                headHeight   = $(".back-head").height(),
                filterHeight = $(".filter").height();

            $(".filter li").on("click", function () {
                var type = $(this).data("type");

                // sort list
                if(type == "sale") {  // sale number sort
					
					$(".pull-down").hide();
                    $(this).siblings().removeClass("active");
                    $(this).closest(".filter").removeClass("fixed");
                    $("#proList").css({
                        marginTop: 0 + "px"
                    });
                    currentPage = 1;
                    sortColumn = "productSaleNum";
                    isAsc = 0;
                    box.empty();
                    getListData();
                    clearPopup();
                    $(".cate-filter").hide();
                    $(".category-brand").hide();
                    
                    /* reset height */    
                    $("#wrap").css({"height":"auto"});

                } else if (type == "time") { // update time sort
					
					$(".pull-down").hide();
                    $(this).addClass("active").siblings().removeClass("active");
                    $(this).closest(".filter").removeClass("fixed");
                    $("#proList").css({
                        marginTop: 0 + "px"
                    });
                    currentPage = 1;
                    sortColumn = "updateTime";

                    if (isAsc == 0) {
                        isAsc = 1;
                    } else {
                        isAsc = 0;
                    }

                    box.empty();
                    getListData();
                    clearPopup();
                    $(".cate-filter").hide();
                    $(".category-brand").hide();
                    
                    /* reset height */    
                    $("#wrap").css({"height":"auto"});

                } else {  // show brand list and category list

                    $(this).addClass("active").siblings().removeClass("active");
                    $(this).closest(".filter").addClass("fixed");
                    $("#proList").css({
                        marginTop: headHeight + filterHeight + "px"
                    });
                    $(".category-brand").css({
                        height: windowHeight - headHeight - filterHeight + "px"
                    }).fadeIn();
                    
                    /* reset height */
                    $("#wrap").css({"height":document.body.clientHeight+"px","overflow":"hidden"});

                    /* more */
                    var size = $("html").css("font-size"),
                        H  = $("#brand-list").height();
                    size = size.substr(0, size.indexOf("px"));
                    if (H > size * 6) {
                        $("#brand-list").css({
                            height: "6rem",
                            overflow: "hidden"
                        }).find(".more").addClass("hide").html("更多");
                    }
                }
            });

            /* brand more */
            $('.more').on("click", function () {
                if ($(this).hasClass("hide")) {
                    $(this).html("收起").addClass("show").removeClass("hide").closest("#brand-list").css({
                        height: "auto"
                    });
                } else {
                    $(this).html("更多").addClass("hide").removeClass("show").closest("#brand-list").css({
                        height: "6rem"
                    });
                }
            });

            /* brand event */
            $(".category-brand dd").on("click", function () {
                $(this).toggleClass("active");
            });

            /* clear select */
            $(".clear-select").on("click", function () {
                $("#brand-list dd").removeClass();
            });

            /* submit button */
            $("#btn-true").on("click", function () {
                brandIds = "";
                $("#brand-list dd.active").each( function () {
                    brandIds += $(this).data("brandid") + ",";
                });

                brandIds = brandIds.substring(0, brandIds.length - 1);
                box.empty();
                currentPage = 1;
                sortColumn = "";
                isAsc = 0;
                getListData();
                clearPopup();

                $(".category-brand").hide();
                $('.filter li').removeClass("active");
                $(this).closest(".category-brand").siblings(".filter").removeClass("fixed");
                $("#proList").css({
                    marginTop: 0 + "px"
                });
                
                /* reset height */    
                $("#wrap").css({"height":"auto"});
            });

            /* pull down refresh */
            $(window).scroll(function () {
                if (flag) {
                    var scrollTop = $(this).scrollTop(),
                        scrollHeight = $(document).height(),
                        boxHeight = $(".product-list li").height(),
                        windowHeight = $(this).height();

                    if (scrollTop >= scrollHeight - windowHeight - boxHeight) {
                        if (total != null) {
                            if (currentPage <= total) {
                                getListData();
                            } else {
                            	$(".pull-down").show().find("span").html("没有更多商品了哦~").end().find("i").addClass("over");
                            }
                        } else {
                            return;
                        }
                    }
                }
            });

            /* get cart number */
            getCartNum();

        });
    </script>
	<script type="text/javascript" src="${jslib}define.js?${jscnf}"></script>
	<script type="text/javascript" src="${jspro}page/other-search.js?v=1.0.0.3"></script>
</html>
</@compress>
</#escape>