<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
    	<#include "/wrap/3g.common.ftl" />
        <title>新云联百万店-送货地址</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
        <@less />
    </head>
    <body>
        <div class="wrap location-page">
            <header class='back-head fixed-head'>
                <span class='go-back'></span>
                <em>请选择送货地址</em>
            </header>
            <nav class="letter-location">
                <ul>
                    <a href="#A">A</a>
                    <a href="#B">B</a>
                    <a href="#C">C</a>
                    <a href="#D">D</a>
                    <a href="#E">E</a>
                    <a href="#F">F</a>
                    <a href="#G">G</a>
                    <a href="#H">H</a>
                    <a href="#I">I</a>
                    <a href="#J">J</a>
                    <a href="#K">K</a>
                    <a href="#L">L</a>
                    <a href="#M">M</a>
                    <a href="#N">N</a>
                    <a href="#O">O</a>
                    <a href="#P">P</a>
                    <a href="#Q">Q</a>
                    <a href="#R">R</a>
                    <a href="#S">S</a>
                    <a href="#T">T</a>
                    <a href="#U">U</a>
                    <a href="#V">V</a>
                    <a href="#W">W</a>
                    <a href="#X">X</a>
                    <a href="#Y">Y</a>
                    <a href="#Z">Z</a>
                </ul>
            </nav>
            <section class="list">
                <h2 class="curr-area"><span>当前定位：</span><span class="province"></span><span class="city"></span><span class="district"></span></h2>
                <ul></ul>
            </section>
        </div>
    </body>
    <script src="/src/frame/jquery.js"></script>
    <script src="/src/frame/hammer.min.js"></script>
    <script src="/src/js/wap.js"></script>
    <script type="text/javascript">
        $(function () {

            /* fixed letter location */
            var wrapWidth = $(".wrap").width();
            function fixedLetterLocation() {
                var windowWidth = $(window).width();
                if (windowWidth > 640) {
                    $(".letter-location").css({
                        right : (windowWidth - wrapWidth) / 2 - wrapWidth + 30,
                        width : 640
                    });
                }
            }
            fixedLetterLocation();

            $(window).resize(function() {
                fixedLetterLocation();
            });

            $(".letter-location a").on("click", function () {
                $(".back-head").hide();
                $(".location-page .list").css({
                    marginTop: 0
                })
            });

            /* get curr location */
            function getCurrArea() {
                $.ajax({
                    url: "/location/current",
                    type: "GET",
                    dataType: "json",
                    data: {},
                    success: function (data) {
                        console.log(data);
                        $(".curr-area .province").html(data.provinceName);
                        $(".curr-area .city").html(data.cityName);
                        $(".curr-area .district").html(data.districtName);
                    }
                });
            }
            getCurrArea();

            /* get and create province list */
            function getProvince() {
                var wrap = $(".location-page .list ul"),
                    dom  = "";
                $.ajax({
                    url: "/location/province",
                    type: "GET",
                    dataType: "json",
                    data: {},
                    success: function (data) {
                        console.log(data);
                        if (data.code == 200) {
                            var list = data.result,
                                len = list.length;
                            for (var i = 0; i < len; i++) {
                                dom += "<li data-code='"+ list[i].code +"' data-id='"+ list[i].id +"' id='"+ list[i].provinceHead +"'><span>"+ list[i].locationName +"</span><i></i></li>";
                            }
                        } else {
                            dom = "<li>暂无省份列表</li>";
                        }
                        console.log(dom);
                        wrap.append(dom);
                    }
                });
            }
            getProvince();

            /* goto city page */
            $(".list ul").on("click", "li", function () {
                var code = $(this).data("code"),
                    name = $(this).find("span").html();
                console.log(code);
                console.log(name);
                window.location.href = "/location/page/city?code=" + code + "&name=" + name;
            });
        });
    </script>
</html>
</@compress>
</#escape>