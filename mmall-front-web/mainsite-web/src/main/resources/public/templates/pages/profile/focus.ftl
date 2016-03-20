<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
        <#include "/wrap/common.ftl">
        <#include "/wrap/my.common.ftl">
        <@title type="focus"/>
        <@css/>
    </head>
    <body>
        <@fixedTop />
        <@topbar />
        <@top />
        <@mainCategory />
        <div class="m-crumbs wrap">
            <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">我的收藏</span>
        </div>
        <div class="m-wrap clearfix">
            <@mSide />
            <div class="m-main r">
                <div class="title">
                    <b>我的收藏</b>
                </div>
                <div class="content">
                    <div class="m-focus clearfix">
                        <ul>
                        </ul>
                    </div>
                    <div class="clearfix">
                        <div class="page"></div>
                    </div>
                </div>
            </div>
        </div>
        <@footer/>
        <@copyright/>
        <@cityChange />
        <div class="popup"></div>
        <@js />
        <script type="text/javascript">
            $(function () {

                var pageBox = $(".page"),
                    total = "",
                    pageIndex = 0,
                    pageLimit = 8;

                // 设置侧边栏选中
                $(".m-side li").eq(3).addClass("active");

                // 获取列表
                function getList(pageLimit, pageIndex) {
                    $.ajax({
                        url : "/attention/product/collectList",
                        type : "GET",
                        cache: false,
                        contentType: "application/json",
                        data : {
                            asc         : false,
                           // orderColumn : "createDate",
                            limit       : pageLimit,
                            offset      : pageIndex
                            //t           : 1442402426182
                        },
                        success : function (data) {
                            //console.log(data);
                            if (data.code == 200) {
                                total = Math.ceil(Number(data.result.total) / pageLimit);
                                createList(data.result.list, data.result.total, pageIndex);
                            }
                        }
                    });
                }
                getList(pageLimit, pageIndex);

                // 创建列表
                function createList(list, total, pageIndex) {
                    //console.log(list);
                    var dom = "",
                        box = $(".m-focus ul");
                    if (list != null) {
                        for (var i = 0; i < list.length; i++) {
                            dom += "<li data-poId='"+ list[i].priceList[0].productId +"'>"+
                                "<p class='img'><span><a href='"+ list[i].productUrl +"' target='_blank'><img src='"+ list[i].showPicPath +"' /></a></span></p>"+
                                "<p class='store-name'><a href='"+ list[i].storeUrl +"' target='_blank'>"+ list[i].storeName +"</a></p>"+
                                "<p class='pro-name'><a href='"+ list[i].productUrl +"' target='_blank'>"+ list[i].productName +"</a></p>"+
                                "<p class='price'>￥"+ list[i].priceList[0].prodPrice +"</p>"+
                                "<i class='un-focus'>取消收藏</i>"+
                            "</li>";
                        }
                        if (total > 8) {
                            total = Math.ceil( total / pageLimit);
                            createPage(total, pageIndex);
                        }
                    } else {
                        dom = "<div class='pro-null'>暂无收藏列表！</div>";
                    }

                    box.empty();
                    box.append(dom);
                }

                /**
                    * 创建分页
                **/

                // 上下页按钮事件
                pageBox.on("click", "span", function () {
                    if ($(this).hasClass("disable")) return;
                    pageIndex = $(this).data("index");
                    getList(pageLimit, (pageIndex - 1) * pageLimit);

                });

                // 页数按钮事件
                pageBox.on("click", "li", function () {
                    pageIndex = $(this).html();
                    getList(pageLimit, (pageIndex - 1) * pageLimit);

                });

                // 输入页数确定按钮
                pageBox.on("click", ".page-submit", function () {
                    pageIndex = pageBox.find(".skip input").val();
                    getList(pageLimit, (pageIndex - 1) * pageLimit);

                });

                // 分页跳转输入框
                pageBox.on("change", ".skip input", function () {
                    var thisVal = $(this).val(),
                        thisVal = Math.round(thisVal);
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
                    index = Number(index / 8) + 1;
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

                // 取消收藏
                $(".m-focus").on("click", ".un-focus", function () {
                    var poId = $(this).closest("li").data("poid");
                    unFocus(poId);
                });
                function unFocus(poId) {
                    $.ajax({
                        url : "/attention/product/unfollow",
                        type : "POST",
                        cache: false,
                        contentType: "application/json",
                        data : JSON.stringify({
                            poId : poId
                        }),
                        success : function (data) {
                            //console.log(data);
                            if (data.code == 200) {
                                $.message.alert("取消收藏成功！", "success", function () {
                                    window.location.href = "/profile/focus";
                                });
                            } else {
                                $.message.alert(data.message, "info");
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