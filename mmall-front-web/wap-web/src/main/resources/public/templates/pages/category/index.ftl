<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
        <#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-分类</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
        <link rel="stylesheet" href="/src/less/wap.css" />
    </head>
    <body>
        <div class="wrap ">
            <header class='back-head'>
                <a href='/' class='go-back'></a>
                <em>分类</em>
            </header>
            <div class="category" id="category">
                <div class="side">
                    <ul></ul>
                </div>
                <div class="main">
                    <ul></ul>
                </div>
            </div>
        </div>
    </body>
    <script src="/src/frame/jquery.js"></script>
    <script src="/src/frame/hammer.min.js"></script>
    <script src="/src/js/wap.js"></script>
    <script type="text/javascript">
        $(function () {

            var windowHeight = $(window).height(),
                backBarHeight = $(".back-head").height();

            /* get category */
            function getCategory() {
                $.ajax({
                    type : "GET",
                    url: "/rest/category",
                    dataType: "json",
                    data: {},
                    success: function(data) {
                        if (data.code == 200) {
                            createCategory(data.result.categoryList);
                        } else {
                            return;
                        }
                    }
                });
            }
            getCategory();

            /* create category */
            var sideBox     = $(".category .side ul"),
                mainBox     = $(".category .main ul"),
                sideDom     = "",
                mainDom     = "";

            function createCategory(list) {
                for ( var i = 0; i < list.length; i++ ) {
                    sideDom += "<li class='item"+ (i+1) +"' data-index='"+ (i+1) +"'>"+
                                    "<i></i>"+
                                    "<span>"+ list[i].name +"</span>"+
                                "</li>";
                    mainDom += "<li>";

                    var secondList = list[i].subCategoryContentDTOs;
                    if (secondList != null) {
                        for ( var j = 0; j < secondList.length; j++) {
                            mainDom += "<dl><dt data-id='"+ secondList[j].id +"'>"+ secondList[j].name +"</dt>";
                            var thirdList = secondList[j].subCategoryContentDTOs;
                            if (thirdList != null) {
                                for (var k = 0; k < thirdList.length; k++) {
                                    mainDom += "<dd data-id='"+ thirdList[k].id +"'>"+ thirdList[k].name +"</dd>";
                                }
                            }
                            mainDom += "</dl>";
                        }
                        mainDom += "</li>";
                    } else {
                        mainDom += "<div class='category-null'><i></i><span>该分类下没有任何内容</span></div></li>";
                    }
                }

                sideBox.append(sideDom);
                mainBox.append(mainDom);

                sideBox.find("li:first").addClass("active");
                mainBox.find("li:first").show();
                setHeight();
            }

            // set height
            function setHeight() {
                $(".category").height(windowHeight - backBarHeight);
                $(".side").height(windowHeight - backBarHeight);
                $(".main").find("li").height(windowHeight - backBarHeight);
            }

            /* change category */
            sideBox.on("click", "li", function () {
                var index = $(this).data("index");
                $(this).addClass("active").siblings().removeClass("active");
                mainBox.find("li").eq(index - 1).show().siblings().hide();;
            });

            /* go into cagetgory list */
            mainBox.on("click", "dt", function () {
                var id = $(this).data("id"),
                    name = $(this).html();
                window.location.href = "/list/" + id + "?name=" + name;
            });
            mainBox.on("click", "dd", function () {
                var id = $(this).data("id"),
                    name = $.trim($(this).html());
                window.location.href = "/list/"+ id +"?name=" + name;
            });

        });
    </script>
</html>
</@compress>
</#escape>