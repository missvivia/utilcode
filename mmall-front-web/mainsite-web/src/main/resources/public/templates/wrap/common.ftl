<#include 'var.ftl'>
<#include 'function.ftl'>

<#-- 公用样式 -->

<#macro css>
  	<link rel="stylesheet" href="/src/css/base.css${versions}" />
  	<link rel="stylesheet" href="/src/css/front.css${versions}" />
  	<link rel="stylesheet" href="/src/css/member.css${versions}" />
</#macro>
<#macro cssCore>
	<link rel="stylesheet" href="/src/css/core.css${versions}" />
</#macro>

<#--
<#macro css>
  	<link rel="stylesheet" href="/src/css/main.min.css${versions}" />
</#macro>
<#macro cssCore>
</#macro>
-->

<#-- 公用脚本 -->

<#macro js>
	<script src="/src/js/jquery.js" type="text/javascript"></script>
    <script src="/src/js/common.js${versions}" type="text/javascript"></script>
    <script src="/src/js/front.js${versions}" type="text/javascript"></script>
    <script>
		var _hmt = _hmt || [];
		(function() {
		  var hm = document.createElement("script");
		  hm.src = "//hm.baidu.com/hm.js?ffcb9a21693bb5c40a3277cdddfff680";
		  var s = document.getElementsByTagName("script")[0];
		  s.parentNode.insertBefore(hm, s);
		})();
	</script>
</#macro>

 <#--
<#macro js>
	<script src="/src/js/jquery.js" type="text/javascript"></script>
    <script src="/src/js/main.min.js${versions}" type="text/javascript"></script>
    <script>
		var _hmt = _hmt || [];
		(function() {
		  var hm = document.createElement("script");
		  hm.src = "//hm.baidu.com/hm.js?ffcb9a21693bb5c40a3277cdddfff680";
		  var s = document.getElementsByTagName("script")[0];
		  s.parentNode.insertBefore(hm, s);
		})();
	</script>
</#macro>
 -->

<#-- 固定顶部 -->
<#macro fixedTop>
	<div class="fixed-top">
		<div class="wrap">
			<div class="main-category wrap main-category-top">
				<div class="category-wrap">
					<div class="category-item">
						<dl>
							<dt>
								全部分类<i></i>
							</dt>
						</dl>
					</div>
					<#include "../category/category.html">
				</div>
				<div class="l search">
					<i></i>
					<input type="text" placeholder="请输入关键词" class="sear-input" />
					<input type="button" value="搜索" class="sub-input" />
				</div>
				<div class="cart r">
					<a href="/cartlist/">
						<em></em>
						<span>进货单</span>
						<i>
							<b class="js-cart-num">0</b>
						</i>
					</a>
				</div>
			</div>
		</div>
	</div>
</#macro>

<#-- 顶部条 -->
<#macro topbar>
	<div class="clearfix top-bar">
		<div class="wrap">
			<#--
			<div class="l region-select">
				<span>送货至</span>
				<em></em>
				<i></i>
			</div>
			-->
			<div class="r top-bar-menu">
				<ul>
					<li class="no-login">
						<a href="/login">请登录！</a>
					</li>
					<li class="is-login">
						<a href="/profile/basicinfo" class="username"></a>
					</li>
					<li class="pull-down">
						<dl>
							<dt>
								<a style="cursor: default; color: #888; text-decoration: none;">我的百万店</a>
								<i></i>
							</dt>
							<dd class="myOrder">
								<a href="/myorder">我的订单</a>
							</dd>
							<dd class="ticket">
								<a href="/cartlist/">我的进货单</a>
							</dd>
							<dd class="coupon">
								<a href="/coupon/">我的优惠券</a>
							</dd>
							<dd class="focus">
								<a href="/profile/focus">我的收藏</a>
							</dd>
							<dd class="loginout">
								<a>退出登录</a>
							</dd>
						</dl>
					</li>
					<li>
						<span>｜</span>
					</li>
					<li>
						<em>客服热线：0571-87651759</em>
					</li>
				</ul>
			</div>
		</div>
	</div>
</#macro>

<#-- 顶部 -->
<#macro top>
	<div class="wrap clearfix top rel">
		<h1 class="l logo">
			<a href="/" title="百万店">
				<img src="/src/images/logo.png" alt="百万店" />
			</a>
		</h1>
		<div class='l'>
			<div class="l">
				<div class="search">
					<i></i>
					<input type="text" placeholder="热门搜索" class="sear-input" />
					<button id="search" class="sub-input">搜 索</button>
				</div>
			</div>
			<div class="cart abs">
				<a href="/cartlist/">
					<em></em>
					<span>进货单</span>
					<i><b class="js-cart-num">0</b></i>
				</a>
			</div>
			<div class="num-tip">+1</div>
		</div>
	</div>
</#macro>

<#-- 主分类 -->
<#macro mainCategory>
	<div class="main-category-box">
		<div class="main-category wrap">
			<div class="category-wrap">
				<div class="category-item">
					<dl class="active">
						<dt>
							全部分类<i></i>
						</dt>
					</dl>
				</div>
				<#include "../category/category.html">
			</div>
		</div>
	</div>
</#macro>

<#-- 底栏 -->
<#macro footer>
	<div class="footer-wrap">
		<div class="wrap clearfix footer">
			<dl>
				<dt>购物指南</dt>
				<dd>
					<a href="/src/html/helpcenter/article/a-1.htm">购物流程</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/a-2.htm">满送活动</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/a-3.htm">订单查询</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/a-4.htm">我的进货单</a>
				</dd>
			</dl>
			<dl>
				<dt>支付帮助</dt>
				<dd>
                    <a href="/src/html/helpcenter/article/b-1.htm">在线支付</a>
                </dd>
                <dd>
                    <a href="/src/html/helpcenter/article/b-2.htm">货到付款</a>
                </dd>
				<dd>
					<a href="/src/html/helpcenter/article/b-3.htm">优惠券使用</a>
				</dd>
			</dl>
			<dl>
				<dt>配送服务</dt>
				<dd>
					<a href="/src/html/helpcenter/article/c-1.htm">配送方式</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/c-2.htm">配送范围</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/c-3.htm">配送费用</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/c-4.htm">验货与签收</a>
				</dd>
			</dl>
			<dl>
				<dt>售后服务</dt>
				<dd>
					<a href="/src/html/helpcenter/article/d-1.htm">退货流程</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/d-2.htm">常见问题</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/d-3.htm">建议反馈</a>
				</dd>
				<dd>
					<a href="/src/html/helpcenter/article/d-4.htm">联系客服</a>
				</dd>
			</dl>
			<#--<dl>
				<dt>手机百万店</dt>
				<dd>
					<img src="/src/test-pic/pic-10.jpg" />
				</dd>
			</dl>-->
		</div>
	</div>
</#macro>

<#-- 版权信息 -->
<#macro copyright>
	<div class="copyright">
		<p>
			<a href="/src/html/helpcenter/article/about.htm">关于我们</a>
			<span>|</span>
			<a href="/src/html/helpcenter/article/cooperation.htm">合作提案</a>
		</p>
		<p>
			<span>&copy;2014-2015 023.baiwandian.cn 版权所有 浙ICP备 14037458号</span>
		</p>
	</div>
</#macro>

<#-- 侧边栏固定 -->
<#macro fixedSide>
	<div class="gotop"></div>
</#macro>


<#-- 城市选择弹窗 -->
<#macro cityChange>
	<div class="city-change">
		<div class="opacity"></div>
		<div class="content">
			<div class="tit">
			    <dl>
			        <dd class="cur active">您当前的位置：&nbsp;&nbsp; <span>[ <em></em> ]</span><span class="uparrow arr1"></span></dd>
			        <dd class="oth">选择其他城市<span class="uparrow arr2"></span></dd>
			    </dl>
				<span class="close"></span>
			</div>
			<div class="con">
			    <div class="current-area">
			         <p>请选择一个区</p>
			         <div class="ars">
			         	<span data-code="500112">渝北区</span>
			         	<span data-code="500105">江北区</span>
			         	<span data-code="500106">沙坪坝区</span>
			         </div>
			    </div>
			    <div class="other-city">
                    <#-- <div class="tab">
                        <dl>
                           <dd class="hot-tab active">热门城市</dd>
                           <dd>ABCD</dd>
                           <dd>EFGH</dd>
                           <dd>IJKL</dd>
                           <dd>MNOP</dd>
                           <dd>QRST</dd>
                           <dd>UVWX</dd>
                           <dd class="y-z">YZ</dd>
                        </dl>
                     </div>
                     <div class="hot">
                         <span data-code=-5001>重庆</span>
                         <span data-code=-1101>北京</span>
                         <span data-code=-1201>天津</span>
                         <span data-code=-3101>上海</span>
                         <span data-code=3201>南京</span>
                         <span data-code=3202>无锡</span>
                         <span data-code=3205>苏州</span>
                         <span data-code=3301>杭州</span>
                         <span data-code=3302>宁波</span>
                         <span data-code=4401>广州</span>
                         <span data-code=4403>深圳</span>
                         <span data-code=4406>佛山</span>
                         <span data-code=4407>江门</span>
                         <span data-code=4413>惠州</span>
                         <span data-code=4404>珠海</span>
                         <span data-code=4420>中山</span>
                         <span data-code=3204>常州</span>
                         <span data-code=3206>南通</span>
                         <span data-code=3210>扬州</span>
                         <span data-code=3211>镇江</span>
                         <span data-code=3304>嘉兴</span>
                         <span data-code=3306>绍兴</span>

                     </div> -->
                     <div class="common">该地区暂未开放！</div>
                </div>
                <div class="area-float"></div>
                <div class="uparrow2"></div>
			</div>
		</div>
	</div>
</#macro>

<#-- 设置标题 -->
<#macro title type="" content="">
  	<#if type=="index">
    	<title>新商盟百万店-首页</title>
  	<#elseif type=="proDetail">
    	<title>${content}新商盟百万店-产品详情</title>
    <#elseif type=="snapShot">
    	<title>${content}新商盟百万店-产品快照</title>
 	<#elseif type=="catrgoryList">
    	<title>${content}新商盟百万店-分类列表</title>
  	<#elseif type=="cartList">
    	<title>${content}新商盟百万店-进货单列表</title>
    <#elseif type=="orderInfo">
    	<title>${content}新商盟百万店-进货单信息</title>
    <#elseif type=="orderPay">
    	<title>${content}新商盟百万店-支付进货单</title>
    <#elseif type=="orderSucc">
    	<title>${content}新商盟百万店-订单提交成功</title>
   	<#elseif type=="orderFail">
        <title>${content}新商盟百万店-订单提交失败</title>
    <#elseif type=="gotoPay">
    	<title>${content}新商盟百万店-支付订单</title>
    <#elseif type=="paySucc">
    	<title>${content}新商盟百万店-支付成功</title>
    <#elseif type=="payfail">
    	<title>${content}新商盟百万店-支付失败</title>
    <#elseif type=="noCart">
    	<title>${content}新商盟百万店-暂无进货单</title>
    <#elseif type=="myInfo">
    	<title>${content}新商盟百万店-个人中心-个人信息</title>
    <#elseif type=="myIndex">
    	<title>${content}新商盟百万店-个人中心-我的百万店</title>
     <#elseif type=="myOrder">
    	<title>${content}新商盟百万店-个人中心-我的订单</title>
    <#elseif type=="wallet">
    	<title>${content}新商盟百万店-个人中心-我的钱包</title>
    <#elseif type=="address">
    	<title>${content}新商盟百万店-个人中心-收获地址</title>
    <#elseif type=="myOrderList">
    	<title>${content}新商盟百万店-个人中心-订单列表</title>
    <#elseif type=="myOrderDetail">
    	<title>${content}新商盟百万店-个人中心-订单详情</title>
    <#elseif type=="focus">
    	<title>${content}新商盟百万店-个人中心-我的关注</title>
    <#elseif type=="modifyPwd">
    	<title>${content}新商盟百万店-个人中心-修改密码</title>
    <#elseif type=="resetPwd">
    	<title>${content}新商盟百万店-重置密码</title>
    <#elseif type=="replenish">
    	<title>${content}新商盟百万店-个人中心-货架补货</title>
    <#elseif type=="coupon">
    	<title>${content}新商盟百万店-个人中心-优惠券</title>
    <#elseif type=="login">
    	<title>${content}新商盟百万店-会员登录</title>
    <#elseif type=="register">
    	<title>${content}新商盟百万店-会员注册</title>
    <#elseif type=="500">
    	<title>${content}新商盟百万店-500</title>
    <#elseif type=="404">
        <title>${content}新商盟百万店-404</title>
  </#if>
</#macro>



