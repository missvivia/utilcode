<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "/wrap/common.ftl">
  	<meta charset="utf-8"/>
      <@title content="新云联百万店-服务保证"/>
  	<@css/>
    <!-- @STYLE -->
	<style>
    body{background-color: #fff;}
    .p-bd{text-align: center;}
    .p-banner{height: 418px;background: url(/res/images/page/service/banner.jpg) top center no-repeat;}
    .p-section{padding: 0 30px 50px 30px;line-height: 28px;color: #666;text-align: left;font-size: 14px;}
    .p-split{background-color: #e5e5e5;width: 100%;height: 1px; overflow: hidden;}
	</style>
</head>
<body>
	<@topbar/>
  <@sidebar/>
    <@navbar index=0/>
	<@module>
  <div class="p-banner"></div>
	<div class="p-bd">
	  <img src="/res/images/page/service/p1.jpg?v=1" class="f-vat" width="1090" height="653"/>
    <div class="p-split"></div>
    <img src="/res/images/page/service/p2.jpg" class="f-vat" width="1090" height="778"/>
    <img src="/res/images/page/service/p3.jpg" class="f-vat" width="1090" height="407"/>
    <div class="p-split"></div>
    <img src="/res/images/page/service/p4.jpg" class="f-vat" width="1090" height="513"/>
    <div class="p-split"></div>
    <img src="/res/images/page/service/p5.jpg" class="f-vat" width="1090" height="299"/>
    <div class="g-bd">
    <div class="p-section">
      <p>您的退货申请提交后，请您在收货后七天内寄回商品</p>
      <p>1、寄回的包裹中包括退货的商品、所有单据（如遗失送货单可用白纸注明订单号，退货人联系电话）、发票（如有）、赠品（如有）、包装，如有遗漏将可能无法办理退货，敬请原谅！</p>
      <p>2、请您先垫付运费，办理退款时最高可补贴10元运费，以红包形式发放至您的账户。</p>
      <p>3、寄回的商品必须为原订购商品，请勿调换商品，否则将无法办理退款。</p>
      <p><a href="http://023.baiwandian.cn/help#/help/articlelist/?categoryId=6&subCategoryId=61" class="s-fc9" target="_blank">查看详细退货政策>></a></p>
    </div>
    </div>
	</div>
	</@module>
    <@footer/>

	<#noparse>
        <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/index.js"></script>
</body>
</html>
</@compress>
</#escape>