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
                <em></em>
            </header>
            <#-- <nav class="letter-location">
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
            </nav> -->
            <section class="list">
                <ul></ul>
            </section>
        </div>
    </body>
    <script src="/src/frame/jquery.js"></script>
    <script src="/src/frame/hammer.min.js"></script>
    <script src="/src/js/wap.js"></script>
    <script type="text/javascript">
        $(function () {

            var url     = window.location.href,
                codeS   = url.indexOf("="),
                codeE   = url.indexOf("&"),
                code    = url.substring(codeS + 1, codeE),
                nameS   = url.lastIndexOf("="),
                nameE   = url.length,
                name    = decodeURI(url.substring(nameS + 1, nameE));

            /* set title */
            $(".back-head em").html(name);

            /* letter location */
            var wrapWidth = $(".wrap").width();
            function letterLocation() {
                var windowWidth = $(window).width();
                if (windowWidth > 640) {
                    $(".letter-location").css({
                        right : (windowWidth - wrapWidth) / 2 - wrapWidth + 30,
                        width : 640
                    });
                }
            }
            letterLocation();

            $(window).resize(function() {
                letterLocation();
            });

            /* get and create district list */
            function getDistrict() {
                var wrap = $(".location-page .list ul"),
                    dom  = "";
                $.ajax({
                    url: "/location/district",
                    type: "GET",
                    dataType: "json",
                    data: {
                        code : code
                    },
                    success: function (data) {
                        console.log(data);
                        if (data.code == 200) {
                            var list = data.result.list,
                                len = list.length;
                            for (var i = 0; i < len; i++) {
                                dom += "<li data-id='"+ list[i].id +"'><span>"+ list[i].name +"</span><i></i></li>";
                            }
                        } else {
                            dom = "<li>暂无地区列表</li>";
                        }
                        console.log(dom);
                        wrap.append(dom);
                    }
                });
            }
            getDistrict();

            /* set curr location */
            $(".list ul").on("click", "li", function () {
                var id = $(this).data("id");
                $.ajax({
                    url: "/location/current/set",
                    type: "GET",
                    dataType: "json",
                    data: {
                        curSupplierAreaCode : id
                    },
                    success: function (data) {
                        console.log(data);
                        if (data.code == 200) {
                            window.location.href = "/";
                        } else {
                            $.messgae.alert("提示", "设置失败！");
                        }
                    }
                });
            });
        });
    </script>
</html>
</@compress>
</#escape>