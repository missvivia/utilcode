<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>个人中心页</title>
    <@meta/>
    <@less />
  </head>
  <body>
      <div class="wrap">
	    	<div class="m-userinfo">
	    		<div class="info">
	    		    <div class="m-title">
	    		         <a class="curp goBack" href = "/">
						    <i class="u-menu"></i>
					     </a>
					     <span class="tt">我的</span>
	    		    </div>
	    		    <div style="padding-bottom:40px;">
		    		    <div class="user-img">
		    		       <a href="/profile/basicinfo">
		    		        <#if profile.gender == 1>
		    		            <img src="/res/3g/images/new/women_head.png" />
		    		        <#else>
		    		            <img src="/res/3g/images/new/man_head.png" />
		    		        </#if>
		    		       </a>
		    		    </div>
						<div class="name f-toe">
						     <a href="/profile/basicinfo">${profile.nickname!''}</a>
						</div>
					</div>
				</div>
			</div>
			<section class="g-bd m-pbox">
			    <div class="order">
			    	<div class="m-table m-otab m-actionblock m-actionblock-1">
				    	<ul class="tr">
				    		<li class="td b-r">
				    		    <a href="/myorder#state=1">
				    		       <i class="u-worder f-pr">
					    		       <#if orderCount[1]!=0>
					    		           <#if (orderCount[1] lt 10)>
					    		               <span class="m-count m-count-1">${orderCount[1]}</span>
					    		           <#elseif (orderCount[1] gt 99)>
					    		               <span class="m-count m-count-3">99+</span>
					    		           <#else>
					    		               <span class="m-count m-count-2">${orderCount[1]}</span>
					    		           </#if>
					    		       </#if>
				    		       </i><br/>
				    		       <span>待支付</span>
				    		    </a>
				    		</li>
				    		<li class="td b-r">
				    		    <a href="/myorder#state=2">
				    		        <i class="u-wsorder f-pr">
				    		           <#if orderCount[2]!=0>
					    		           <#if (orderCount[2] lt 10)>
					    		               <span class="m-count m-count-1">${orderCount[2]}</span>
					    		           <#elseif (orderCount[2] gt 99)>
					    		               <span class="m-count m-count-3">99+</span>
					    		           <#else>
					    		               <span class="m-count m-count-2">${orderCount[2]}</span>
					    		           </#if>
					    		       </#if>
				    		        </i><br/>
				    		        <span>待发货</span>
				    		    </a>
				    		</li>
				    		<li class="td">
				    		   <a href="/myorder#state=3">
				    		       <i class="u-sorder f-pr">
				    		          <#if orderCount[3]!=0>
					    		           <#if (orderCount[3] lt 10)>
					    		               <span class="m-count m-count-1">${orderCount[3]}</span>
					    		           <#elseif (orderCount[3] gt 99)>
					    		               <span class="m-count m-count-3">99+</span>
					    		           <#else>
					    		               <span class="m-count m-count-2">${orderCount[3]}</span>
					    		           </#if>
					    		       </#if>
				    		       </i><br/>
				    		       <span>待收货</span>
				    		   </a>
				    		</li>
				    	</ul>
			    	</div>
			    	<a class="all-order" href="/myorder#state=0"><div class="txt"><i class="u-more">查看全部订单</i></div></a>
		    	</div>
		    	<ul class="m-actionblock m-otab">
		    		<li><a href="/profile/focus">我的收藏<i class="u-more"></i></a></li>
		    		<li><a href="/coupon/coupon">优惠券  <span class="f-fr"><#if availableNum??&&availableNum!=0><span class="available f-dn"></span></#if><i class="u-more"></i></span></a></li>
		    	    <li><a href="/profile/address">地址<i class="u-more"></i></a></li>
		    	</ul>
		    	<ul class="m-actionblock m-otab">
		    		<li><a href="/src/html/helpcenter/about.html">关于我们<i class="u-more"></i></a></li>
		    	</ul>
		    	<ul class="m-actionblock m-otab">
		    		<li><a href="tel:0571-87651759">客服<i class="u-more"></i><span class="tip">周一至周日9:00-24:00</span></a></li>
		    	</ul>
		    	
		    	<ul class="m-actionblock m-otab">
		    		<#if profile??><li class="f-tac"><a href="#" class="logout">退出登录</a></li></#if>
		    	</ul>
	        </section>
	        <#-- <@nav index=2/>-->
     </div>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@jsFrame />
    <script>
          // 用户退出登陆
			$(".logout").on("click", function () {
				var url = window.location.href;
				$.ajax({
					url : "/logout",
					type : "POST",
					data : {},
					success : function (data) {
						window.location.href = "/login?redirectURL="+encodeURIComponent(url);
					}
				});
			});
    </script>
  </body>
</html>
</@compress>
</#escape>