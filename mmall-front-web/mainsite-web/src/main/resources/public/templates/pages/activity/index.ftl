<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
    <html>
        <head>
            <#include "../../wrap/common.ftl">
            <title>新宠会</title>
            <meta charset="utf-8"/>
            <@css/>
            <link rel="stylesheet" href="/src/css/page/activity/activity/mask.css">
            <link rel="stylesheet" href="/src/css/page/activity/activity/index.css">
        </head>
        <body id="activity-index">
            <@topbar/>
            <@sidebar/>
            <@navbar index=0/>
            <@module>
                <div class="g-activity">
                    <div class="pg_part m-banner">
                        <div class="cnt_part cnt"></div>
                    </div>
                    <div class="cnt_part m-activity1" id="activity1">
                    	<div class="j-coupon"></div>
                    	<div class="f-fl rules">
                    		<h1>活动规则</h1>
                    		<p class="rule">1. 礼包领取时间为2015年1月16日10时至26日24时止；</p>
                    		<p class="rule">2. 用户注册登入立即自动绑定4重礼包，可进入我的优惠券进行查看；</p>
                    		<p class="rule">3. 优惠券在下单结算时可选择使用；</p>
                    		<p class="rule">4. 礼包均为平台上线让利，活动期间限量50000份，每个会员ID仅有一次领取机会，送完即止；</p>
                    	</div>
                    </div>
                    <div class="cnt_part m-activity2" id="activity2">
                    	<div class="f-fl rules">
                    		<h1>活动规则</h1>
                    		<p class="rule">1. 活动时间1月16日10时至26日24时止；</p>
                    		<p class="rule">2. 活动期间pc端用户每日10时、12时、14时、16时、18时、20时，共6个整点会在mmall全站各个页面（主页，品牌专题页）随机出现mmall形象。用户用鼠标在限定时间内找到并戳破10个普通mmall即可兑换活动优惠券。若该时段未集齐兑换数量，mmall将会被累积，用户可下时段继续收集；</p>
                    		<p class="rule">3. 活动期间pc端用户每日12时、18时，每个整点对所有已下单付款用户（不支持货到付款）在普通mmall基础上额外出现彩虹mmall机会，用户只要在限定时间内找到其中一个彩虹mmall并第一个戳破即将获得免单特权（免单特权：平台将为用户退还获得免单特权前所有已付款订单商品中最高价的单件商品金额）；</p>
                    		<p class="rule">4. 找mmall环节优惠券兑换金额为20元优惠券，无使用门槛，累计发放200万元；</p>
                    		<p class="rule">5. 彩虹mmall每时段出现18个，每日36个，活动期间共设396个彩虹mmall平台免单特权，客服将在一个工作日内联系中奖用户安排退款事宜；</p>
                    		<p class="rule">6. 每个会员活动期间最多享受一次免单特权；</p>
                    	</div>
                    </div>
                    <div class="cnt_part m-activity3" id="activity3">
                    	<div class="f-fl rules">
                    		<h1>活动规则</h1>
                    		<p class="rule">1. 活动时间1月16日10时至26日24时止；</p>
                    		<p class="rule">2. 活动期间单笔订单实付金额满1288元即可赠送价值699网易登机箱一个；</p>
                    		<p class="rule">3. 活动期间每日共58个赠送名额，先到先得，送完即止；</p>
                    	</div>
                    </div>
                    <div class="cnt_part m-activity4" id="activity4">
                    	<div class="f-fl rules">
                    		<h1>活动规则</h1>
                    		<p class="rule">1. 活动时间1月16日10时至26日24时止；</p>
                    		<p class="rule">2. 活动期间用户每在线成功下单一笔（不支持货到付款）即可获得一次平台抽奖机会；</p>
                    		<p class="rule">3. 移动端下单用户可登入PC页面进行活动抽奖；</p>
                    		<p class="rule">4. 抽奖活动将设置特等奖1名，一等奖2名，二等奖10名，三等奖20名，四等奖1000名。每份礼品数量有限，送完即止；</p>
                    	</div>
                    	<div class="f-fl lottery">
                    		<h1>巴厘岛双飞双人游</h1>
                    		<p class="gift"><span>一等奖</span>&emsp;美图KISS自拍神器</p>
                    		<p class="gift"><span>二等奖</span>&emsp;美国clarisonic洗脸神器</p>
                    		<p class="gift"><span>三等奖</span>&emsp;韩国soc超声波补水神器</p>
                    		<p class="gift"><span>四等奖</span>&emsp;100元mmall优惠券</p>
                    	</div>
                    	<div class="f-fl explains">
                    		<p class="explain">1. 活动期，优惠券可在mmall全网全品类商品进行抵扣，每笔订单同时仅能享受一种优惠券抵扣；</p>
                    		<p class="explain">2. 优惠券及免单特权仅限在1月16日10时至26日24时使用，过期无效。使用优惠的订单发生退款时，优惠券金额将不予退还；</p>
                    		<p class="explain">3. 平台优惠券将在用户兑换/抽中后1小时内发放至用户账户；</p>
                    		<p class="explain">4. 所有实物奖品mmall客服将在5个工作日内与用户确认寄送信息，并于活动期结束后7个工作日，在用户确认收货且无退款情况后单独寄送，不随单派送；</p>
                    	</div>
                    </div>
                </div>
            </@module>
            <@footer/>
            <#noparse></#noparse>
            <!-- @SCRIPT -->
            <script src="${jslib}define.js?${jscnf}"></script>
            <script src="${jspro}page/activity/index.js"></script>
        </body>
    </html>
    </@compress>
</#escape>
