<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
    	<#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-${businesser.storeName}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="no" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection"content="telephone=no, email=no" />
        <@less />
        <link rel="stylesheet" type="text/css" href="/src/css/page/search.css?v=1.0.0.3"/>
    </head>
    <body class="other-search">
        <div class="wrap" id="wrap">
            <header class='back-head fixed-head'>
                <span class='go-back'></span>
                <em>${businesser.storeName}</em>
                <span class="search"></span>
            </header>
            <#--
            <div class="add-cart-tips">
                <i></i>
                <span>已成功加入一件商品</span>
                <a href="/cartlist/">去订货单</a>
            </div>
            <div class="store-banner back-head-height"></div>
            -->
            <div class="filter store-filter back-head-height">
                <ul>
                    <li data-type='sale' class='active'>销量</li>
                    <li data-type='price'>价格</li>
                    <li data-type='time'>上架时间</li>
                    <li data-type='filler'>筛选</li>
                </ul>
            </div>
            <#if category??>
                <div class="category-brand popup-local">
                	<div class='all-cate'><span>全部商品</span></div>
                    <div class="cate-list">
                        <#list category as first>
                            <#if first.isvisible=true>
                                <dl>
                                    <dt>${first.name}</dt>
                                    <#list first.subCategoryContentDTOs as secend>
                                        <#if secend.isvisible=true>
                                            <dd data-cateid='${secend.id}'  data-rootId='${secend.rootId}'>
                                                <span>${secend.name}</span>
                                            </dd>
                                        </#if>
                                    </#list>
                                </dl>
                            </#if>
                        </#list>
                    </div>
                    <div id="brand-list">
                        <dl>
                            <dt>品牌<span class='more'></span></dt>
                            <div></div>
                        </dl>
                    </div>
                    <div class='pro-name'>
                        <dl>
                            <dt>产品名称</dt>
                            <input type="text" placeholder="请输入产品名称" maxlength="32" />
                        </dl>
                    </div>
                    <div class="pro-price">
                        <dl>
                            <dt>价格区间</dt>
                            <input type="number" class='min-price' /><span>——</span><input type="number" class='max-price' />
                        </dl>
                    </div>
                    <div class='action'>
                        <span class="clear-select">清空所有选项</span>
                        <span id="btn-true">确 定</span>
                    </div>
                </div>
            </#if>
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
    <@jsFrame />
    <script type="text/javascript">
        $(function () {

            var url             = window.location.href,
                locationHash    = window.location.hash,
                hashId          = locationHash.substr(1, locationHash.length),
                key1            = url.lastIndexOf("/", url.lastIndexOf("/") - 1) + 1,
                key2            = url.lastIndexOf("/"),
                storeID         = url.substring(key1, key2),
                box             = $("#proList ul"),
                orderColumn     = "saleNum",
                pageCurr        = 0,
                pageSize        = 10,
                flag            = true,
                asc             = false,
                obj             = {},
                total           = "",
                level           = "",
                sprice          = "",
                eprice          = "",
                rootId          = "",
                contentCategoryId = "",
                productName     = "",
                brandIdList     = "",
                storeName 		= "${businesser.storeName}";
         
            /* format param */
            function createObj() {
                obj = {
                    id              : storeID,
                    limit           : pageSize,
                    offset          : Number(pageCurr) * pageSize,
                    productName     : productName,
                    lowCategoryId   : hashId,
                    level           : level,
                    sprice          : sprice,
                    eprice          : eprice,
                    orderColumn     : orderColumn,
                    asc             : asc,
                    rootId          : rootId,
                    contentCategoryId : contentCategoryId,
                    brandIdList     : brandIdList
                };
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
                        url: "/store/searchProduct/",
                        type: "GET",
                        dataType: "json",
                        data: {
                            businessId      : obj.id,
                            limit           : obj.limit,
                            offset          : obj.offset,
                            productName     : obj.productName,
                            lowCategoryId   : obj.lowCategoryId,
                            level           : obj.level,
                            sprice          : obj.sprice,
                            eprice          : obj.eprice,
                            orderColumn     : obj.orderColumn,
                            asc             : obj.asc,
                            rootId          : obj.rootId,
                            brandIdList     : obj.brandIdList
                        },
                        beforeSend: function () {
                            flag = false;
                            if (obj.offset == 1) {
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
                return pageCurr;
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
                    console.log(total);
                    for ( var i = 0; i < list.length; i++) {
                    
                        if (list[i].skuLimitConfigVO != null) {
                            var sTime = list[i].skuLimitConfigVO.limitStartTime,
                                eTime = list[i].skuLimitConfigVO.limitEndTime,
                                allowBuyNum = list[i].skuLimitConfigVO.allowBuyNum;
                        }
                        
                        dom += "<li data-skuid='"+ list[i].skuId +"' data-skunum='"+ list[i].skuNum +"'>"+
                                    "<a class='pic' href='/product/detail?skuId="+ list[i].skuId +"'>"+
                                        "<img src='"+ list[i].showPicPath + thumbnailParam('70', '155', '155')+"' />";
                                        
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
                                "<a href='/store/${businesser.id}/' class='storeName'>"+ storeName +"</a>"+
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
                    pageCurr++;
                    $(".load-page .curr").html(pageCurr);
                    $(".load-page .total").html(total);
                } else {
                    total = null;
                    dom += "<div class='null-page'>"+
                        "<i class='store-list-null'></i>"+
                        "<p>很遗憾，该店铺下没有商品</p>"+
                    "</div>";
                }

                $(".loading").hide();
                $(".pull-down").hide();
                flag = true;
                box.append(dom);
                nullPage();
            }

            /* get brand list */
            function getBrandList() {
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

            /* create brand list */
            function createBrandList(data) {
                console.log(data);
                var list = data.list,
                    dom = "",
                    box = $("#brand-list div");
                for (var i = 0; i < list.length;i ++ ) {
                    dom += "<dd data-brandid='"+ list[i].brandId +"'><span>"+ list[i].brandNameZh +"</span></dd>";
                }
                box.empty();
                box.append(dom);
            }

            /* reset brand list */
            $(".all-cate").on("click", function () {
            	rootId = "";
                contentCategoryId = "";
                brandIdList = "";
                $(".category-brand").hide();
                $('.filter li').eq(0).addClass("active").siblings().removeClass("active");
                $(".cate-list dd").removeClass("active");
                $(this).closest(".category-brand").siblings(".filter").removeClass("fixed");
                $("#proList").css({
                    marginTop: 0 + "px"
                });
                
                /* reset height */    
                $("#wrap").css({"height":"auto"});
                
                getBrandList();

                pageCurr = 0;
                orderColumn = "saleNum";
                isAsc = false;

                box.empty();
                getListData();
                clearPopup();
            });

            $(".cate-list dd").on("click", function () {
                brandIdList = "";
                $(this).addClass("active").siblings().removeClass("active");
                $(this).closest("dl").siblings().find("dd").removeClass("active");
                rootId = $(this).data("rootid"),
                contentCategoryId = $(this).data("cateid");
                
                hashId = contentCategoryId;
                window.location.hash = contentCategoryId;
                $(".category-brand").hide();
                $(this).closest(".category-brand").siblings(".filter").removeClass("fixed");
                $("#proList").css({
                    marginTop: 0 + "px"
                });
                
                /* reset height */    
                $("#wrap").css({"height":"auto"});
                    
                getBrandList();

                pageCurr = 0;
                orderColumn = "saleNum";
                isAsc = false;

                box.empty();
                getListData();
                clearPopup();
            });

            /* filter tool */
            var bannerHeight = $(".store-banner").outerHeight(true),
                headHeight   = $(".back-head").height();


            var windowHeight = $(window).height(),
                headHeight   = $(".back-head").height(),
                filterHeight = $(".filter").height();

                 $(".filter").setWidth();
                $(".store-category").setWidth();
                $(".store-filter").setWidth();

            $(".filter li").on("click", function () {
                var type = $(this).data("type");

                // sort list
                if(type == "sale") {  // sale number sort
					
					$(".pull-down").hide();
                    $(this).addClass("active").siblings().removeClass("active");
                    $(this).closest(".filter").removeClass("fixed");
                    $("#proList").css({
                        marginTop: 0 + "px"
                    });

                    pageCurr = 0;
                    orderColumn = "saleNum";
                    asc = false;

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

                    pageCurr = 0;
                    orderColumn = "updateTime";

                    if (asc) {
                        asc = false;
                    } else {
                        asc = true;
                    }

                    box.empty();
                    getListData();
                    clearPopup();
                    $(".cate-filter").hide();
                    $(".category-brand").hide();
                    
                    /* reset height */    
                    $("#wrap").css({"height":"auto"});

                } else if (type == "price") {
					
					$(".pull-down").hide();
                    $(this).addClass("active").siblings().removeClass("active");
                    $(this).closest(".filter").removeClass("fixed");
                    $("#proList").css({
                        marginTop: 0 + "px"
                    });

                    pageCurr = 0;
                    orderColumn = "price";

                    if (asc) {
                        asc = false;
                    } else {
                        asc = true;
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
                    
                    /* reset height */
                    $("#wrap").css({"height":document.body.clientHeight+"px","overflow":"hidden"});
                    
                    // $(".category-brand").css({
                    //     height: windowHeight - headHeight - filterHeight + "px"
                    // }).fadeIn();
                    $(".category-brand").fadeIn();

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

            //* brand event */
             $("#brand-list").on("click", "dd", function () {
                $(this).toggleClass("active");
            });

            $(".min-price").on("change", function () {
                var val = $(this).val();
                if (isNaN(val) || val < 0 ) {
                    val = 0;
                    $(this).val(val);
                }
            });

            /* clear select */
            $(".clear-select").on("click", function () {
                $("#brand-list dd").removeClass();
                $(".pro-name input").val("");
                $(".min-price").val("");
                $(".max-price").val("");
            });

            /* submit button */
            $("#btn-true").on("click", function () {
                brandIdList = "";
                $("#brand-list dd.active").each( function () {
                    brandIdList += $(this).data("brandid") + ",";
                });

                brandIdList = brandIdList.substring(0, brandIdList.length - 1);
                productName = $(".pro-name input").val();
                sprice = $(".min-price").val();
                eprice = $(".max-price").val();
                pageCurr = 0;
                orderColumn = "saleNum";
                isAsc = false;

                box.empty();
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
                            if (pageCurr + 1 <= total) {
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
    <script type="text/javascript" src="${jspro}page/other-search.js?v=1.0.0.4"></script>
</html>
</@compress>
</#escape>