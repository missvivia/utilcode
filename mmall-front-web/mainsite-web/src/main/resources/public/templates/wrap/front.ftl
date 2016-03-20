<#-- 侧边栏订单 -->
<#macro fixedSideOrder>
	<div class="fixed-side-order" status="hide">
		<div class="clearfix tit">
			<h3>进货单</h3>
			<span class="close">X</span>
		</div>
		<div class="list">
			<ul></ul>
		</div>
		<div class="bottom">
			<p>
				<em>共<i></i>件商品</em>
				<b>¥<i></i></b>
			</p>
			<a href="/cartlist" target="_blank"><i></i><span>查看进货单</span></a>
		</div>
	</div>
</#macro>

<#-- 侧边栏补货 -->
<#macro fixedSideReplenish>
	<div class="fixed-side-replenish" status="hide">
		<div class="clearfix tit">
			<h3>货架补货</h3>
			<span class="close">X</span>
		</div>
		<div class="list">
			<ul>
			</ul>
		</div>
		<div class="bottom">
			<a href="/replenish/" class="go-replenish">查看货架补货</a>
			<a href="/cartlist/" class="go-order"><i></i><em>进货单</em></a>
		</div>
	</div>
</#macro>