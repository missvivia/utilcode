<#include 'var.ftl'>
<#include 'function.ftl'>
<#-- 预热活动-首页 -->
<#macro activity_foot>
  <#include '../pages/index/act_foot.ftl'>
</#macro>
<#-- 预热活动-首页结束 -->

<#-- 公用样式 -->
<#macro css>
  <link rel="shortcut icon" href="/favicon.ico?v=2" />
  <link rel="stylesheet" href="/src/css/base.css" />
</#macro>

<#-- 公用脚本 -->
<#macro js>
	<script src="/src/js/jquery.js" type="text/javascript"></script>
    <script src="/src/js/common.js" type="text/javascript"></script>
</#macro>

<#-- 固定顶部 -->
<#macro fixedTop>
	<div class="fixed-top">
		<div class="wrap">
			<div class="main-category wrap">
				<div class="category-item"> 
					<dl>
						<dt>
							全部分类<i></i>
						</dt>
					</dl>
				</div>
				<div class="category-side">
					<ul></ul>
				</div>
				<div class="category-nav">
					<ul></ul>
				</div>
				<div class="l search">
					<input type="text" value="请输入关键词" class="sear-input input-clear" />
					<input type="button" value="搜索" class="sub-input" />
				</div>
				<div class="cart r">
					<span>
						<i></i>
					</span>
					进货单
					<a href="/cart"></a>
				</div>
			</div>
		</div>
	</div>
</#macro>

<#-- 顶部条 -->
<#macro topbar>
	<div class="clearfix top-bar">
		<div class="wrap">
			<div class="l region-select">
				<span>
					送货至
				</span>
				<select>
				</select>
				<i></i>
			</div>
			<div class="r top-bar-menu">
				<ul>
					<li class="pull-down">
						<span>
							Hi，<em>emmanel</em>
						</span>
						<dl>
							<dd class="person">
								<i></i><a href="#">个人中心</a>
							</dd>
							<dd class="ticket">
								<i></i><a href="#">优惠券</a>
							</dd>
							<dd class="collect">
								<i></i><a href="#">我的收藏</a>
							</dd>
							<dd class="loginout">
								<i></i><a href="#">退出登录</a>
							</dd>
						</dl>
					</li>
					<li>
						<span>｜</span>
					</li>
					<li>
						<a href="/cart">我的订单</a>
					</li>
					<li>
						<span>｜</span>
					</li>
					<li>
						<a href="#">帮助中心</a>
					</li>
				</ul>             
			</div>
		</div>
	</div>
</#macro>

<#-- 顶部 -->
<#macro top>
	<div class="wrap clearfix top">
		<h1 class="l logo">
			<a href="/" title="百万店">
				<img src="/src/images/logo.png" alt="百万店" />
			</a>
		</h1>
		<div class="l">
			<div class="search">
				<i></i>
				<input type="text" value="热门搜索" class="input-clear" />
				<button>搜 索</button>
			</div>
			<div class="menu">
				<a href="#">智能手机</a>
				<span>|</span>
				<a href="#" class="active">运动鞋女</a>
				<span>|</span>
				<a href="#">雪纺衫</a>
				<span>|</span>
				<a href="#">手表男</a>
				<span>|</span>
				<a href="#" class="active">春夏童装</a>
				<span>|</span>
				<a href="#">运动裤</a>
				<span>|</span>
				<a href="#">四件套</a>
				<span>|</span>
				<a href="#">双肩包</a>
			</div>
		</div>
	</div>
</#macro>

<#-- banner -->
<#macro banner>
	<div class="category-banner">
		<div class="pic">
			<ul>
			</ul>
		</div>
		<div class="btn round">
			<ul>
				<li class="active"></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
			</ul>
		</div>
	</div>
</#macro>

<#-- 底栏 -->
<#macro footer>
	<div class="wrap clearfix footer">
		<dl>
			<dt>购物指南</dt>
			<dd>
				<a href="#">订单状态</a>
			</dd>
			<dd>
				<a href="#">交易条款</a>
			</dd>
			<dd>
				<a href="#">积分说明</a>
			</dd>
			<dd>
				<a href="#">会员制度</a>
			</dd>
			<dd>
				<a href="#">购物流程</a>
			</dd>
		</dl>
		<dl>
			<dt>支付帮助</dt>
			<dd>
				<a href="#">支付帮助</a>
			</dd>
			<dd>
				<a href="#">余额支付</a>
			</dd>
			<dd>
				<a href="#">银行电汇</a>
			</dd>
			<dd>
				<a href="#">在线支付</a>
			</dd>
			<dd>
				<a href="#">货到付款</a>
			</dd>
		</dl>
		<dl>
			<dt>配送帮助</dt>
			<dd>
				<a href="#">EMS/邮政普包</a>
			</dd>
			<dd>
				<a href="#">商品验货与签收</a>
			</dd>
			<dd>
				<a href="#">加急快递</a>
			</dd>
			<dd>
				<a href="#">上门自提</a>
			</dd>
			<dd>
				<a href="#">配送范围及运费</a>
			</dd>
		</dl>
		<dl>
			<dt>配送方式</dt>
			<dd>
				<a href="#">全场满288元免运费</a>
			</dd>
			<dd>
				<a href="#">配送范围及运费</a>
			</dd>
			<dd>
				<a href="#">验货与签收</a>
			</dd>
		</dl>
		<dl>
			<dt>售后服务</dt>
			<dd>
				<a href="#">售后服务</a>
			</dd>
			<dd>
				<a href="#">发票制度</a>
			</dd>
			<dd>
				<a href="#">退货政策</a>
			</dd>
			<dd>
				<a href="#">退货流程</a>
			</dd>
			<dd>
				<a href="#">退款方式和时效</a>
			</dd>
			<dd>
				<a href="#">余额的使用与提现</a>
			</dd>
		</dl>
		<dl>
			<dt>手机百万店</dt>
			<dd>
				<img src="/src/test-pic/pic-10.jpg" />
			</dd>
		</dl>
	</div>
</#macro>

<#-- 版权信息 -->
<#macro copyright>
	<div class="copyright">
		<a href="#">关于我们</a>
		<span>|</span>
		<a href="#">常见问题</a>
		<span>|</span>
		<a href="#">安全交易</a>
		<span>|</span>
		<a href="#">购买流程</a>
		<span>|</span>
		<a href="#">如何付款</a>
		<span>|</span>
		<a href="#">联系我们</a>
		<span>|</span>
		<a href="#">合作提案</a>
	</div>
</#macro>

<#-- 侧边栏固定 -->
<#macro fixedSide>
	<div class="fixed-side">
		<ul>
			<li class="card">
				<a href="#" title="购物车"><i></i></a>
				<p>
					<span>购物车</span>
					<em></em>
				</p>
			</li>
			<li class="collect">
				<a href="#" title="收藏">
					<i></i>
				</a>
				<p>
					<span>收藏</span>
					<em></em>
				</p>
			</li>
			<li class="download">
				<a href="#" title="下载app">
					<i></i>
				</a>
				<p>
					<span>下载APP</span>
					<em></em>
				</p>
			</li>
		</ul>
		<div class="gotop">
			<i></i>
		</div>
	</div>
</#macro>

<#-- 单个产品 -->
<#macro proBox>
	<div class="pro-box">
		<div class="number">
			<ul>
				<li class="min-num active" data-num="5">5件起批</li>
				<li class="min-num" data-num="100">100以上</li>
				<li class="min-num" data-num="500">500以上</li>
				<li class="more">￥</li>
			</ul>
		</div>
		<div class="item">
			<img src="../test-pic/pic-4.jpg" />
			<span class="price">¥56.8</span>
			<a href="#" class="describe">威露士衣物除菌液 2.5L+1.5L加量装 杀菌率高达99.999% 有效抑菌</a>
			<div class="action clearfix">
				<div class="skip l">
					<input type="text" class="num l" value="5" />
					<div class="arrow r">
						<em class="up"></em>
						<em class="down disable"></em>
					</div>
				</div>
				<div class="btn square">
					<i></i>
					<a href="javascript:void(0);">进货</a>
				</div>
			</div>
			<a href="#" class="name">楼兰蜜语品牌旗舰店</a>
		</div>
		<div class="shade"> 
			<i></i> 
			<span>
				¥7.20 
			</span>
			<em>|</em>
			<span>
				¥7.00
			</span>
			<em>|</em>
			<span>
				¥6.90 
			</span>
		</div>
	</div>
</#macro>

<#-- 设置标题 -->
<#macro title type="" content="">
  <#if type=="index">
    <title>新云联百万店-首页</title>
  <#elseif type=="schedule">
    <title>${content}新云联百万店-其他页面</title>
  <#else>
    <title>${content}新云联百万店-其他页面</title>
  </#if>
</#macro>