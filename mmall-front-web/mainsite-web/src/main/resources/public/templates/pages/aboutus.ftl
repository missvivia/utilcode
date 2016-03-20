<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
  	<meta charset="utf-8"/>
    <@title content="新云联百万店-关于我们"/>
  	<@css/>
    <!-- @STYLE -->
	<style>
    body{background-color: #f9f9f9;}
    .p-bd{color: #666;line-height: 28px;text-align: center;font-size: 14px;}
    .p-banner{height: 418px;background: url(/res/images/page/aboutus/banner.jpg) top center no-repeat;}
    .p-section{background-color: #fff;width: 100%;height: 470px;}
    .p-i{margin: 55px auto 22px auto;width: 139px; height: 75px;}
    .p-i1{background: url(/res/images/page/aboutus/i1.png) no-repeat;}
    .p-i2{background: url(/res/images/page/aboutus/i2.png) no-repeat;}
    .p-i3{height: 76px;background: url(/res/images/page/aboutus/i3.png) no-repeat;}
    .p-i4{width: 158px; height: 158px;margin: -70px auto 30px auto;background: url(/res/images/page/aboutus/i4.png) no-repeat;}
    .p-i5{width: 312px; height: 312px;margin: 55px auto 30px auto;background: url(/res/images/page/aboutus/i5.png) no-repeat;}
	</style>
</head>
<body>
	<@topbar />
  <@sidebar/>
    <@navbar index=0/>
	<@module>
  <div class="p-banner"></div>
  <div class="p-section">
    <div class="g-bd">
      <img src="/res/images/page/aboutus/p1.png" alt=""/>
    </div>
  </div>
	<div class="g-bd p-bd">
    <div class="p-i4"></div>
    <p>mmall是网易旗下专注于专柜同步新款闪购的综合类女性电商网站，主营女装，同时覆盖男装、童装、鞋包、家纺、美妆等类目。</p>
    <p>区别于其他电商平台及闪购类网站，mmall的最大亮点是“专柜同步新款闪购”，用户可以在mmall上找到各大主流商场专柜正在同步热卖的最新款商品，并</p>
    <p>且享受独家的线上超值折扣。</p>
    <p>mmall与国内外知名服装品牌商及其省级代理商进行三方合作，能够向不同省区的消费者提供最热销的专柜同步新款的超值折扣。将线下代理商的门店库存</p>
    <p>与互联网电子商务的流量打通，尽最大可能为消费者带来最高性价比的当季新款。</p>
    <div class="p-i p-i1"></div>
    <p>mmall每日更新，为消费者提供海量当季商场专柜在售新品的折扣闪购，消费者不用出门，就可以在家选购各大潮流品牌的最新款服饰，同时mmall独特的三</p>
    <p>方合作供货模式，可以最大程度避免热门款式的缺色断码情况，让消费者轻松购物。</p>
    <div class="p-i p-i2"></div>
    <p>传统线下专柜的新品在上市时一般都以原价销售，随着线下租金及人力成本的日渐上涨，动辄千元的专柜新款往往让普通消费者望而却步。mmall通过与品</p>
    <p>牌代理商直接合作，最大程度减少中间成本，在新品上市当季就为消费者提供与专柜同质同款的超值折扣闪购，让消费者体验最高性价比时尚购物体验。</p>
    <div class="p-i p-i3"></div>
    <p>mmall分区域销售，根据用户所在地，提供最新的应季商品；同省发货模式，保证用户能够最快时间收到商品。</p>
    <div class="p-i5"></div>
  </div>
	</@module>
    <@footer/>

	<#noparse>
        <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/aboutus.js"></script>
</body>
</html>
</@compress>
</#escape>