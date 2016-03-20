<#include "var.ftl">
<#include "function.ftl">

<#-- 通用样式 -->
<#macro css>
  <link rel="stylesheet" href="/src/css/base.css"/>
  <link rel="stylesheet" href="/src/css/grid.css"/>
  <link rel="stylesheet" href="/src/css/unit.css?v=1.0.0.3"/>
  <link rel="stylesheet" href="/src/css/module.css?v=1.0.0.5"/>
</#macro>

<#-- less -->
<#macro less>
    <link rel="stylesheet" href="/src/less/common.css${versions}" />
    <link rel="stylesheet" href="/src/less/wap.css${versions}" />
    <link rel="stylesheet" href="/src/less/dialog.css${versions}" />
</#macro>

<#-- js frame -->
<#macro jsFrame>
    <script src="/src/frame/jquery-1.11.3.min.js"></script>
    <script src="/src/frame/hammer.min.js"></script>
    <script src="/src/js/wap.js${versions}"></script>
</#macro>

<#macro meta>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <meta name="format-detection"content="telephone=no, email=no" />
  <link rel="apple-touch-startup-image" href="/res/3g/startup.png">
  <link rel="shortcut icon" href="/favicon.ico?v=2" />
  <!-- @noparse -->
  <!--AdMaster tracking code begin --->
  <script type="text/javascript">
      var g_ursname = (function(){
          var _getcookie = function(_name){
              var _cookie = document.cookie,
                      _search = '\\b'+_name+'=',
                      _index1 = _cookie.search(_search);
              if (_index1<0) return '';
              _index1 += _search.length-2;
              var _index2 = _cookie.indexOf(';',_index1);
              if (_index2<0) _index2 = _cookie.length;
              return _cookie.substring(_index1,_index2)||'';
          };
          var _userId = _getcookie("P_INFO") || _getcookie("P_OINFO");
          if(_userId){
              try{
                  return decodeURIComponent(_userId).split("|")[0];
              }catch(ex){
              }
          }
      })();
      var _smq = _smq || [];
      _smq.push(['_setAccount', '5b9caac', new Date()]);
      _smq.push(['_setDirectoryIndex', 'index.html']);
      _smq.push(['_setCustomVar', 2, g_ursname?"login":"logout", 1]);
      _smq.push(['_setCustomVar', 1, g_ursname, 1]);
      _smq.push(['pageview']);
      
  </script>
  <!-- /@noparse -->
</#macro>
<#-- 顶栏 -->
<#macro topbar title="mmall" external=false parentPage=""><#--external:是否为外部分享页面，如果是，隐藏topbar-box工具栏-->
<header class="g-hd m-topbox hdhide" id="paopao-header">
  <div  <#if !external>class="headerholder"</#if>>
  </div>
  <div class="headerbox">
<#--
	  <div class="m-download1 f-cb">
	  	<span id="closedld" class="closebox  f-fl"><i class="close"></i></span>
	  	<img class="f-fl" src="/res/3g/images/download.png"/>
	  	<div class="desc f-fl">
	  		<div class="sitename">mmall</div>
	  		<div class="slogan">时尚新品抢购</div>
	  	</div>
	  	<span class="m-dldbtn" id="dldbtn">打开</span>
	  </div>
	  <div class="m-wxmask" id="wx_mask">
	    <img src="/res/3g/images/webview/wx_ios.jpg" width="270" id="wx_ios"/>
	    <img src="/res/3g/images/webview/wx_android.jpg" width="306" id="wx_aos"/>
	  </div>
-->
      <div  <#if external>class="m-topnav f-tac f-dn"<#else>class="m-topnav f-tac"</#if> id="topbar-box">
	    <a href="${parentPage}"><i class="f-fl u-menu" id="menu" ><img src="/src/img/svg/go-back.svg"/></i></a>
	    <span class="tt f-toe" id="title"><#if title="首页"><i class="u-paopao" ></i><#else>${title}</#if></span>

      <#nested>
  	  </div>
   </div>
<#--   <div class="u-top" id="gotop"></div>-->
</header>
</#macro>
<#-- 导航 -->
<#macro navbar index=0>
<div class="g-hd">
    <div class="m-nav">
    <div class="g-bd">
      <ul class="nav">
        <li <#if index==0> class="selected"</#if>><a href="/">首页</a></li>
        <li <#if index==1> class="selected"</#if>><a href="/dress">女装</a></li>
        <li <#if index==2> class="selected"</#if>><a href="/gentlemen">男装</a></li>
        <li <#if index==3> class="selected"</#if>><a href="/kidswear">童装</a></li>
        <li <#if index==4> class="selected"</#if>><a href="/case">鞋包</a></li>
        <li <#if index==5> class="selected"</#if>><a href="/house">家纺</a></li>
        <li class="last"></li>
      </ul>
    </div>
  </div>
</div>
</#macro>
<#-- 主体部分 -->
<#macro module class="">
	<section class="g-bd ${class}">
  		<#nested>
	</section>
</#macro>
<#-- 底栏 -->
<#macro footer>
<div class="g-ft f-tac" >
<div class="m-table">
  <ul class="m-ptype tr">
  	<li class="td"><a href="http://023.baiwandian.cn/m/download"><i class="u-download"></i><span>客户端</span></a></li>
  	<li class="td"><a href="http://023.baiwandian.cn/?from=wap"><i class="u-pc"></i><span>电脑版</span></a></li>
  </ul>
</div>
  <div class="m-copyright">网新新云联技术有限公司版权所有©2015-2016</div>
</div>
</#macro>

<#-- 模版集合 -->
<#macro template>
  <div id="template-box" style="display:none;">
    <#nested>
  </div>
</#macro>

<!-- @noparse -->
<#macro baiduMap>
 <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=${cfg_develop?string("e2b435804fdaf1de44d3e1fea8382954","e2b435804fdaf1de44d3e1fea8382954")}"></script>
</#macro>
<!-- /@noparse -->

<#macro pricePrint price=0>
&yen;${price?floor}<span class="small">.${((price-price?floor)*100)?string["00"]}</span>
</#macro>

<!-- 滑动模块 -->
<#macro touchslide promotionContent>
<div class='m-touchslide'<#if promotionContent?? && promotionContent?size == 0>style="display:none;"</#if> >
  <div class='mimg'>
    <ul class='j-node'>
      <#list promotionContent as p>
        <li style='left:${p_index*100}%;'>
          <a class='m-img' href='${(p.activityUrl)!""}'>
            <img class='u-loading-1' draggable='false' data-src='${(p.imgUrl)!''}' data-link="${(p.activityUrl)!""}" alt=''/>
          </a>
        </li>
      </#list>
    </ul>
  </div>
  <div class='m-pointer' <#if promotionContent?? && promotionContent?size == 1>style="display:none;"</#if> >
    <div class='pointer'>
      <ul class='j-node'>
        <#list promotionContent as p>
        <li></li>
        </#list>
      </ul>
    </div>
  </div>
</div>
</#macro>

<!-- 首页频道页通用结构 -->
<#macro indexcnt>
<div class='g-bd g-index-bd'>
  <header class='m-head'>
    <div class='new'></div><span class='word'>最新抢购 每日<i class='time'>10点</i>开始</span>
  </header>
  <#list scheduleToday as s>
    <section class='w-schedule'>
      <a href='/schedule?scheduleId=${s.banner.scheduleId}'>
        <div class='img'>
            <#if (s.promotionDesc)?has_content>
                <div class="promotion">
                    <span>${s.promotionDesc}</span>
                </div>
            </#if>
            <div class='time'><i class='u-time'></i><span data-countdown='${s.endTime-.now?long}' class='j-bcd'></span>后结束</div>
            <div class='m-img m-bnr'>
            <img class='u-loading-1' data-src='${(s.banner.homeBannerImgUrl)!''}' />
            </div>
        </div>
      </a>
      <div class='cnt f-cb'>
        <div class='logo-box'>
          <div class='box'>
            <div class='logo'>
              <image src='${(s.brandLogo)!''}?imageView&quality=100&thumbnail=140x60' />
            </div>
          </div>
        </div>
        <div class='word'>
          <p title='${s.title}'>${s.title}</p>
          <p><#--含${s.productTotalCnt}款新品--><i class='price'>${s.minDiscount/10}<span class="discount">折起</span></i></p>
        </div>
      </div>
    </section>
  </#list>
</div>
</#macro>

<#macro coming class="">
  <div class="m-coming ${class}">
    <div class="img">&nbsp;</div>
    <p class="ln1">暂无活动，敬请期待</p>
    <p class="ln2">关注品牌，及时获得抢购通知</p>
  </div>
</#macro>
<#assign pkgStateMap = {'0':{'nickname':'待发货'},'1':{'nickname':'待发货'},'5':{'nickname':'已发货'},'6':{'nickname':'交易完成'},'7':{'nickname':'已发货'},'8':{'nickname':'交易完成'},'9':{'nickname':'待发货'},'13':{'nickname':'交易完成'},'14':{'nickname':'交易完成'},'15':{'nickname':'已取消','case':'商品缺货'},'16':{'nickname':'已取消','case':'包裹拒收'},'17':{'nickname':'已取消'},'18':{'nickname':'已取消','case':'包裹丢件'},'19':{'nickname':'已取消'}} />

<#assign orderStateMap = {'0':{'nickname':'等待付款'},'1':{'nickname':'待审核'},'2':{'nickname':'待发货'},'5':{'nickname':'待发货'},'6':{'nickname':'待发货'},'9':{'nickname':'已发货'},'10':{'nickname':'已发货'},'15':{'nickname':'交易完成'},'20':{'nickname':'取消中'},'21':{'nickname':'已取消'},'22':{'nickname':'已取消'},'25':{'nickname':'审核未通过(货到付款)'}} />


<#macro ga>
<!-- @noparse -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-56703531-2', 'auto');
  ga('send', 'pageview');
</script>

<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?dda2c77cdce4693607fb9456039b957d";
  var s = document.getElementsByTagName("script")[0];
  s.parentNode.insertBefore(hm, s);
})();
</script>

<script type="text/javascript">var _gaq = _gaq || [];_gaq.push(['_setAccount', 'UA1421388246726'],['_setLocalGifPath', '/UA1421388246726/__utm.gif'],['_setLocalServerMode']);_gaq.push(['_addOrganic','baidu','word']);_gaq.push(['_addOrganic','soso','w']);_gaq.push(['_addOrganic','youdao','q']);_gaq.push(['_addOrganic','sogou','query']);_gaq.push(['_addOrganic','so.360.cn','q']);_gaq.push(['_trackPageview']);_gaq.push(['trackPageLoadTime']);(function() {var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;ga.src = 'http://wr.da.netease.com/ga.js';var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);})();</script>
<!--AdMaster tracking code begin --->
<script type="text/javascript">
    var g_ursname = (function(){
        var _getcookie = function(_name){
            var _cookie = document.cookie,
                _search = '\\b'+_name+'=',
                _index1 = _cookie.search(_search);
            if (_index1<0) return '';
            _index1 += _search.length-2;
            var _index2 = _cookie.indexOf(';',_index1);
            if (_index2<0) _index2 = _cookie.length;
            return _cookie.substring(_index1,_index2)||'';
        };
        var _userId = _getcookie("P_INFO") || _getcookie("P_OINFO");
        if(_userId){
            try{
                return decodeURIComponent(_userId).split("|")[0];
            }catch(ex){
            }
        }
    })();
    var _smq = _smq || [];
    _smq.push(['_setAccount', '65edc6e', new Date()]);
    _smq.push(['_setDirectoryIndex', 'index.html']);
    _smq.push(['_setCustomVar', 2, g_ursname?"login":"logout", 1]);
    _smq.push(['_setCustomVar', 1, g_ursname, 1]);
    _smq.push(['pageview']);
</script>
<script type="text/javascript">
    var _mvq = _mvq || [];
    _mvq.push(['$setAccount', 'm-120524-0']);


    _mvq.push(['$logConversion']);
    (function() {
        var mvl = document.createElement('script');
        mvl.type = 'text/javascript'; mvl.async = true;
        mvl.src = ('https:' == document.location.protocol ? 'https://static-ssl.mediav.com/mvl.js' : 'http://static.mediav.com/mvl.js');
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(mvl, s);
    })();
</script>
<script type="text/javascript">
    var baseFontSize = 50,
    	baseWidth = 640,
    	screenWidth = document.body.clientWidth;
    	screenFontSize = baseFontSize * screenWidth / baseWidth;
    	screenFontSize = screenFontSize > 50 ? 50 : screenFontSize;
		document.body.style.fontSize = screenFontSize + "px";
		document.getElementsByTagName("html")[0].style.fontSize = screenFontSize + "px";
</script>
<!-- /@noparse -->
</#macro>


<#-- nav -->
<#macro nav index=0>
<nav class="nav">
    <a href="/" class="nav-home <#if index==0>active</#if>">
        <i></i>
        <span>首页</span>
    </a>
    <a href="/page/category/" class="nav-cate <#if index==1>active</#if>">
        <i></i>
        <span>分类</span>
    </a>
    <a href="/profile/index" class="nav-my <#if index==2>active</#if>">
        <i></i>
        <span>我的</span>
    </a>
    <a href="/cartlist" class="nav-cart <#if index==3>active</#if>">
        <i></i>
        <span>进货单</span>
    </a>
</nav>
</#macro>

<#-- fixed right -->
<#macro fixRight>
    <div class="fix-right">
        <span class='gotop' style='display: none;'></span>
        <a class="fix-cart" href="/cartlist">
            <em class="CartNum">0</em>
        </a>
    </div>
</#macro>

<#-- down app -->
<#macro downApp>
    <div class="down-app">
        <i class="close"></i>
        <i class="logo"></i>
        <span class="text">App新用户专享福利，快来领取！</span>
        <a href="####" class="btn">立即领取</a>
    </div>
</#macro>

