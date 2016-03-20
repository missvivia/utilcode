<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>年终迎双节-每天送百元</title>
	<#include "../../wrap/common.ftl">
    <@css/>
	<style>
		html,body,div,span,h1,h2,h3,h4,h5,h6,p,a,em,img,b,i,dl,dt,dd,ol,ul,li{margin:0;padding:0;border:0;font-family:\5FAE\8F6F\96C5\9ED1, \9ED1\4F53, arial, \5B8B\4F53, sans-serif}body{width:100%;font-size:12px;background:#fff}ol,ul,li{list-style:none}em,i{font-style:normal}a{color:#444;text-decoration:none}a:hover{color:#f00;text-decoration:underline}

		.clearfix:after{content:" ";display:block;height:0;clear:both;}
		.clearfix{*zoom:1;}




		.bwd-wrap a, .bwd-wrap a:hover {
		    text-decoration: none;
		}
		.bwd-wrap img {
		    display: block;
		    border: none;
		}


		.w-1190 {
		    width: 1190px;
		    margin: 0 auto;
		}

		.bwd-wrap {
		    width: 100%;
		    max-width: 1920px;
		    min-width: 1190px;
		    margin: 0 auto;
		    font: 12px/150% 'Microsoft Yahei', Arial,Verdana,"\5b8b\4f53";
		}
		.bwd-banner {
		    width: 100%;
		    min-width: 1190px;
		    background: url(/src/activity-img/20151123/00_01.jpg) center top no-repeat;
		}

		/*商品楼层导航部分*/
		.bwd-nav {
		    width: 1190px;
		    margin: 0 auto;
		    padding: 40px 0 30px 60px;
		    background: url(/src/activity-img/20151123/00_04.png) no-repeat;
		    overflow: hidden;
		}
		.nav-item {
		    float: left;
		    width: 250px;
		    height: 86px;
		    margin-left: 80px;
		    padding-top: 50px;
		    background: url(/src/activity-img/20151123/00_05.png) no-repeat;
		    font-size: 24px;
		    color: #fffe94;
		    font-weight: 700;
		    line-height: 36px;
		    text-align: center;
		    text-indent: 20px;
		}
		.nav-item a {
		    display: block;
		    color: #fffe94;
		}
		.nav-item a:hover {
		    color: #feb81b;
		}

		/*商品模块*/
		.bwd-listCont {
		    position: relative;
		    width: 100%;
		    min-width: 1190px;
		    padding-bottom: 100px;
		    background: url(/src/activity-img/20151123/00_06.jpg) top center no-repeat;
		}
		.bwd-module {
		    width: 1190px;
		    margin: 0 auto;
		    padding-top: 66px;
		    overflow: hidden;
		}
		/*title*/
		.bwd-h3 {
		    position: relative;
		    height: 69px;
		    background-color: #d76300;
		    font-size: 34px;
		    color: #fff;
		    line-height: 69px;
		    text-indent: 174px;
		}
		.bwd-more {
		    font-weight: normal;
		    position: absolute;
		    right: 30px;
		    top: 0;
		    padding: 0 20px;
		    font-size: 20px;
		    color: #e0e0e0;
		    text-decoration: none;
		    z-index: 3;
		}
		a.bwd-more:hover {
			color: #fff;
		}
		.bwd-h3 i {
		    display: block;
		    position: absolute;
		    left: 0;
		    bottom: 0;
		    width: 150px;
		    height: 75px;
		    background-image: url(/src/activity-img/20151123/00_03.png);
		}
		.bwd-i1 {
		    background-position: 0 0;
		}

		.bwd-i2 {
		    background-position: 0 -75px;
		}

		.bwd-i3 {
		    background-position: 0 -150px;
		}

		/*item*/
		.bwd-items {
		    width: 1210px;
		}
		.bwd-item {
		    float: left;
		    width: 288px;
		    margin: 12px 10px 0 0;
		    border: 1px solid transparent;
		    overflow: hidden;
		}
		.bwd-item:hover {
		    border-color: red;
		}
		.bwd-items-3 .bwd-item {
		    width: 388px;
		}
		.bwd-item img {
		    width: 288px;
		    height: 357px;
		}
		.bwd-items-3 img {
		    width: 388px;
		    height: 357px;
		}
		.item-name {
		    display: block;
		    background-color: #eac573;
		    font-size: 32px;
		    color: #be630a;
		    font-family: 'Microsoft Yahei';
		    line-height: 72px;
		    text-align: center;
		}
		/*规则*/
		.bwd-rules {
		    width: 100%;
		    min-width: 1190px;
		    padding-top: 212px;
		    padding-bottom: 100px;
		    background: url(/src/activity-img/20151123/00_07.jpg) #eca121 top center no-repeat;
		}
		.bwd-h4 {
		    padding-bottom: 20px;
		    font-size: 34px;
		    color: #fff;
		    line-height: 50px;
		    text-indent: 20px;
		}
		.bwd-rule {
		    position: relative;
		    padding-right: 20px;
		    padding-left: 145px;
		    font-size: 18px;
		    color: #fff;
		    line-height: 36px;
		}
		.bwd-red {
		    position: absolute;
		    left: 0; top: 0;
		    width: 200px;
		    font-size: 18px;
		    color: #E50D48;
		    line-height: 36px;
		    text-indent: 20px;
		}
		.bwd-ab {
		    padding-top: 36px;
		}
		.bwd-ab dd {
		    padding-left: 24px;
		}
		.bwd-ab span {
		    margin-left: -24px;
		}

		.wireless {
			display: none;
		    position: fixed;
		    left: 50%;
		    bottom: 100px;
		    margin-left: 595px;
		}
		.wireless a {
			display: block;
			position: absolute;
			width: 143px;
			height: 150px;
			background-color: #fff;
			filter: alpha(opacity=0);
			opacity: 0;
		}
	</style>

</head>
<body>
	<@fixedTop />
	<@topbar />
	<@top />
	<@mainCategory />
	<div class="bwd-wrap">
		<div class="bwd-banner">

			<!-- bannner -->
			<div class="w-1190">
				<img src="/src/activity-img/20151123/00_02.jpg" width="1190" height="220" alt="年终迎双节" />
				<img src="/src/activity-img/20151123/00_03.jpg" width="1190" height="244" alt="每天送百元" />
			</div>

			<!-- 楼层导航模块 -->
			<ul class="bwd-nav" id="J_nav">
				<li class="nav-item">
					<a href="#f1" target="_self">瑞悦酒水</a>
				</li>
				<li class="nav-item">
					<a href="#f2" target="_self">飞恒洗化</a>
				</li>
				<li class="nav-item">
					<a href="#f3" target="_self">星作副食</a>
				</li>
			</ul>

		</div>

		<!-- 商品模块 -->
		<div class="bwd-listCont">

			<!-- 瑞悅酒水 -->
			<div class="bwd-module">
				<h3 class="bwd-h3" id="f1">
					<i class="bwd-i1"></i>
					瑞悅酒水
					<a class="bwd-more" href="http://023.baiwandian.cn/store/1000033/" target="_blank">进入店铺 ></a>
				</h3>
				<ul class="bwd-items bwd-items-3 clearfix"><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000470" target="_blank"><img src="/src/activity-img/20151123/img_12_01.jpg" alt="饮料" /><b class="item-name">饮 料</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000471" target="_blank"><img src="/src/activity-img/20151123/img_12_02.jpg" alt="酒类" /><b class="item-name">酒 类</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000492" target="_blank"><img src="/src/activity-img/20151123/img_12_03.jpg" alt="方便面" /><b class="item-name">方便面</b></a></li></ul>
			</div>

			<!-- 飞恒洗化 -->
			<div class="bwd-module">
				<h3 class="bwd-h3" id="f2">
					<i class="bwd-i2"></i>
					飞恒洗化
					<a class="bwd-more" href="http://023.baiwandian.cn/store/1000035/" target="_blank">进入店铺 ></a>
				</h3>
				<ul class="bwd-items clearfix"><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000473" target="_blank"><img src="/src/activity-img/20151123/img_21_01.jpg" alt="个人洗护" /><b class="item-name">个人洗护</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000474" target="_blank"><img src="/src/activity-img/20151123/img_21_02.jpg" alt="厨卫清洁" /><b class="item-name">厨卫清洁</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000517" target="_blank"><img src="/src/activity-img/20151123/img_21_03.jpg" alt="洗衣用品" /><b class="item-name">洗衣用品</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000475" target="_blank"><img src="/src/activity-img/20151123/img_21_04.jpg" alt="家居用品" /><b class="item-name">家居用品</b></a></li></ul>
			</div>

			<!-- 星作副食 -->
			<div class="bwd-module">
				<h3 class="bwd-h3" id="f3">
					<i class="bwd-i3"></i>
					星作副食
					<a class="bwd-more" href="http://023.baiwandian.cn/store/1000034/" target="_blank">进入店铺 ></a>
				</h3>
				<ul class="bwd-items clearfix"><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000488" target="_blank"><img src="/src/activity-img/20151123/img_31_01.jpg" alt="饼干糕点" /><b class="item-name">饼干糕点</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000489" target="_blank"><img src="/src/activity-img/20151123/img_31_02.jpg" alt="膨化食品" /><b class="item-name">膨化食品</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000490" target="_blank"><img src="/src/activity-img/20151123/img_31_03.jpg" alt="肉类零食" /><b class="item-name">肉类零食</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000491" target="_blank"><img src="/src/activity-img/20151123/img_31_04.jpg" alt="坚果炒货" /><b class="item-name">坚果炒货</b></a></li></ul>
				<ul class="bwd-items bwd-items-3 clearfix"><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000493" target="_blank"><img src="/src/activity-img/20151123/img_32_01.jpg" alt="豆干素食" /><b class="item-name">豆干素食</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000486" target="_blank"><img src="/src/activity-img/20151123/img_32_02.jpg" alt="糖果" /><b class="item-name">糖果</b></a></li><li class="bwd-item"><a href="http://023.baiwandian.cn/list/1000487" target="_blank"><img src="/src/activity-img/20151123/img_32_03.jpg" alt="蜜饯" /><b class="item-name">蜜 饯</b></a></li></ul>
			</div>

		</div>

		<!-- 活动详情 -->
		<div class="bwd-rules" id="r">
			<div class="w-1190">
				<h4 class="bwd-h4">活动详情</h4>
				<div class="bwd-rule">
					<h5 class="bwd-red">红包活动时间：</h5>
					<dl>
						<dd>
							2015年11月23日00:00:00至2015年12月31日23:59:59
						</dd>
					</dl>
				</div>
				<div class="bwd-rule">
					<h5 class="bwd-red">红包发放规则：</h5>
					<dl>
						<dd>
							新老账户每天有100元面值红包组合（5元面值*2张，10元面值*3张，20元面值*3张），当日的红包当日使用有效，隔日作废。同时隔日会有新的100面面值红包组合发放至账户。
						</dd>
					</dl>
				</div>
				<div class="bwd-rule bwd-ab">
					<h5 class="bwd-red">红包使用条件：</h5>
					<dl>
						<dd>
							<span>1、</span>5元面值：订单满300元使用
						</dd>
						<dd>
							<span>2、</span>10元面值：订单满500元使用
						</dd>
						<dd>
							<span>3、</span>20元面值：订单满800元使用
						</dd>
					</dl>
				</div>
				<div class="bwd-rule bwd-ab">
					<h5 class="bwd-red">红包使用规则：</h5>
					<dl>
						<dd>
							<span>1、</span>活动期间，每日100元红包组合将于当天00：00：00前发到用户账户，用户可进入“我的百万店——我的优惠券”中查看已发放的红包，红包在相应有效期内可以使用。
						</dd>
						<dd>
							<span>2、</span>当天新注册用户的100元组合红包将在注册完成后1个小时内发放完毕。
						</dd>
						<dd>
							<span>3、</span>100元红包组合仅限在“百万店”平台线上采购商品使用，线下联系相应供货商采购商品无法使用红包。
						</dd>
						<dd>
							<span>4、</span>每个订单可以使用一张红包，用户可以自主选择使用不同面值红包，系统默认优先使用面值最高红包进行抵扣，一个订单不可以叠加使用多个红包。
						</dd>
						<dd>
							<span>5、</span>任何面值红包不得提现，不得转赠他人，不得为他人付款。
						</dd>
						<dd>
							<span>6、</span>本次活动发放的100元组合红包用于支付后, 若订单发生退货, 红包将作废，不再补发当日红包，请用户退货前慎重考虑。
						</dd>
						<dd>
							<span>7、</span>在获取和使用100元组合红包过程中，如果用户出现违规行为（如作弊领取、恶意套现、恶意囤货，扰乱市场价格体系等 ），百万店将取消违规用户的红包资格，并有权撤销违规交易，并收回全部红包（含已使用的及未使用的）,必要时追究法律责任。
						</dd>
						<dd>
							<span>8、</span>红包的发放可能因为服务器拥堵等原因出现延迟。
						</dd>
						<dd>
							<span>9、</span>最终解释权归百万店平台所有。
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>

	<div class="wireless" id="J_wireless">
		<a href="#r" target="_self">活动详情</a>
		<img src="/src/activity-img/20151123/wireless.png" width="143" height="360" alt="百万店" />
	</div>
	<@footer />
	<@copyright />
	<@fixedSide />
	<@cityChange />
    <@js />
    <script>
		$(function() {
			$(window).scroll(function () {
				var WinH = $(window).height();
			    if ( $(document).scrollTop() > WinH / 2)
			    {
			        $("#J_wireless").fadeIn();
			    } else {
			        $("#J_wireless").fadeOut();
			    }
			});
		})
			
		</script>
</body>
</html>
</@compress>
</#escape>