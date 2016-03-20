<#-- 用户登录边栏-->
<#macro sideNav index>
  <div class="m-side l" id="m-side">
	<div class="person-info">
      <span class="portrait">
        <img src="/src/test-pic/pic-1.png">
      </span>
      <!--<a href="/profile/index/" class="name">${(profile.nickname)?default('')}</a>-->
      <span class="grade">会员</span>
    </div> 
    <div class="list">
      <ul>
        <li class="myorder">
          <i></i>
          <a href="/myorder">我的订单</a>
        </li>
        <li class="cartlist">
          <i></i>
          <a href="/cartlist/" target="_blank">我的进货单</a>
        </li>
        <li class="coupon">
          <i></i>
          <a href="/coupon/">优惠券</a>
        </li>
        <#--<li class="replenish">
          <i></i>
          <a href="/replenish/">货架补货</a>
        </li>-->
        <li class="follow">
          <i></i>
          <a href="/profile/focus">我的收藏</a>
        </li>
        <li class="info">
          <i></i>
          <a href="/profile/basicinfo">个人资料</a>
        </li>
        <li class="address">
          <i></i>
          <a href="/profile/address">收货地址</a>
        </li>
        <li class="modifypwd">
          <i></i>
          <a href="/password/password">修改密码</a>
        </li>
      </ul>
    </div>
  </div>
</#macro>

<#macro mSide>
	<div class="m-side l" id="m-side">
	    <div class="person-info">
	      <span class="portrait">
	        <img src="/src/test-pic/pic-1.png">
	      </span>
	      <!--<a href="/profile/index/" class="name">${(profile.nickname)?default('')}</a>-->
	      <span class="grade">会员</span>
	    </div> 
	    <div class="list">
	      <ul>
	        <li class="myorder">
	          <i></i>
	          <a href="/myorder">我的订单</a>
	        </li>
	        <li class="cartlist">
	          <i></i>
	          <a href="/cartlist/" target="_blank">我的进货单</a>
	        </li>
	        <li class="coupon">
	          <i></i>
	          <a href="/coupon/">优惠券</a>
	        </li>
	        <#--<li class="replenish">
	          <i></i>
	          <a href="/replenish/">货架补货</a>
	        </li>-->
	        <li class="follow">
	          <i></i>
	          <a href="/profile/focus">我的收藏</a>
	        </li>
	        <!--
	        <li class="ticket">
	          <i></i>
	          <a href="/profile/wallet">收支详情</a>
	        </li>
	        -->
	        <li class="info">
	          <i></i>
	          <a href="/profile/basicinfo">个人资料</a>
	        </li>
	        <li class="address">
	          <i></i>
	          <a href="/profile/address">收货地址</a>
	        </li>
	        <li class="modifypwd">
	          <i></i>
	          <a href="/password/password">修改密码</a>
	        </li>
	      </ul>
	    </div>
	  </div>
</#macro>
<#macro crumbs>
<!-- 面包削 -->
  <div class="m-crumbs wrap">
  	<#nested/>
  </div>
</#macro>

<#macro myModule sideIndex>
<!-- 登录用户主模块  -->
	<div class="wrap m-wrap clearfix">
		<@sideNav index=sideIndex />
		<#nested>
	</div>
</#macro>