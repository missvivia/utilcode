<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
    <html>
    <head>
		<#include "/wrap/3g.common.ftl" />
        <title>404</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<meta name="format-detection"content="telephone=no, email=no" />
        <@less />
  		<link rel="stylesheet" type="text/css" href="/src/css/page/search.css?v=1.0.0.2"/>
    </head>
    <body>
        <div class="wrap">
            <header class="header">
                <div class="location">
                    <a href="/location/page/province">
                        <i><img src="/src/img/svg/location.svg" /></i>
                    </a>
                </div>
                <div class="search">
                    <input class="form-control searchPlace" type="text" placeholder="输入关键字查找" id="key"/>
                    <span class="cancel">取消</span>
                </div>
                <div class="myInfo">
                    <a href="/profile/index/">
                        <i><img src="/src/img/svg/head-my.svg" /></i>
                    </a>
                </div>
                <div class="category">
                    <a href="/page/category/">
                        <i><img src="/src/img/svg/head-cate.svg" /></i>
                    </a>
                </div>
            </header>
            <div class="err-content">
                <i class="err-img bg-404"></i>
                <p class="tip">对不起，您访问的页面不在地球上...</p>
                <div class="btnbox"><a href="/" class="btn">返回首页</a></div>     
            </div>
        </div>
		<div class="searchMode">
            <div id="result" class="tab-pane">
                <ul class="list" id="searchList">
                </ul>
                <div id='clearHistory'>清除历史搜索</div>
            </div>
        </div>
        <div class="searchMask"></div>
    </body>
    <@jsFrame />
    <script type="text/javascript" src="${jslib}define.js?${jscnf}"></script>
    <script type="text/javascript" src="${jspro}page/search.js?v=1.0.0.2"></script>
    </html>
    </@compress>
</#escape>