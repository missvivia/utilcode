<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
    <#include '../../wrap/3g.common.ftl'>
    <html>
    <head>
        <title>品牌详细</title>
        <@meta/>
        <@css/>
        <link rel='stylesheet' type='text/css' href='/src/css/page/brandStory.css'>
    </head>
    <body id="branddetail-netease-com">
        <@topbar title="${data.brandInfo.basic.brandName}"/>
        <div class="g-bd g-brandstory">
            <div class="m-header">
				<div class="header">
					<div class="m-pobanner m-img ">
						<img class="u-loading-1" src="/res/3g/images/blank.gif"  data-src="${data.brandInfo.brandVisualImgApp}" data-axis="5"/>
						 <div class="follow ${data.brandInfo.favByUser?string("j-follow","")}" id="follow-box">
				          <i class="m-pobg i">&nbsp;</i>
				          <span>${data.brandInfo.favByUser?string("取消关注","关注品牌")}</span>
				        </div>
					</div>
				</div>
				<div class="u-logo">
					<div class="m-img-wrap" style='background:url(${(data.brandInfo.basic.logoUrl)!''}?imageView&quality=100&thumbnail=84x36) center center no-repeat'>
					</div>
					<div class="m-ct">${data.brandInfo.basic.intro}</div>

				</div>
			</div>

			<div class="m-banner-box">
			<#if (data.polist?size==0) && (data.nextpolist?size==0)>
					<div class="m-banner">
						<@coming/>
					</div>
				<#else>
				<div class="m-banner">
					<header class="header header-1">正在抢购中</header>
					<#if data.polist?has_content>
					 <#list data.polist as s>
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
					            <div class='logo' style='background:url(${(s.brandLogo)!''}?imageView&quality=100&thumbnail=84x36) center center no-repeat'></div>
					          </div>
					     </div>
				        <div class='word'>
				          <p title='${s.title}'>${s.title}</p>
				          <p><#--含${s.productTotalCnt}款新品--><i class='price'>${s.minDiscount/10}折起</i></p>
				        </div>
				      </div>
				    </section>
      			 	</#list>
      			 	<#else>
      			 	<@coming/>
      			 	 </#if>
				</div>

				<div class="m-banner">
				<header class="header header-2">下期预告</header>
					<#if data.nextpolist?has_content>
					 <ul>
					 <#list data.nextpolist as item>
					<li class="m-liitem">
						<div class="logo">
							<div class="m-logo">
								<a href="/schedule?scheduleId=${item.banner.scheduleId}">
									<img src="${(item.brandLogo)!''}'?imageView&quality=100&thumbnail=174x76" class="">
								</a>
							</div>
						</div>
						<div class="brandfigure">
							<a href="/schedule?scheduleId=${item.banner.scheduleId}" class="m-img">
								<img data-src='${(item.banner.homeBannerImgUrl)!''}' data-axis="5" class="u-loading-1" src="/res/3g/images/blank.gif">
							</a>
						</div>
					</li>
      				 </#list>
      			 	</ul>
      			 	<#else>
      			 	<@coming/>
      			 </#if>
				</div>
				</#if>
			</div>

			<div class="m-map" id="map">
        	</div>


        </div>

        <script type="text/javascript">
          <#noescape>
          var g_return = ${JsonUtils.toJson(data)};
          </#noescape>
         </script>

      	<@baiduMap/>
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/brand/story.js"></script>
    </body>
    </html>
    </@compress>
</#escape>