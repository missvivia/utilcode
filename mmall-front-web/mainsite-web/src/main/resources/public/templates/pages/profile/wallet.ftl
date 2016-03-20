<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <@title type="wallet"/>
    <@css/>
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
    <@fixedTop />
    <@topbar />
    <@top />
    <@mainCategory />
    
    <div class="bg-french-gray clearfix">
    	<@crumbs>
		 <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">收支详情</span>
    	</@crumbs>
      <@myModule sideIndex=4>
        <div class="m-box m-main l">
          <div class="m-info">
            <p>网易宝余额：<span class="s-fc5">￥<span class="f-fs4">1299</span>.28</span><span class="w-btn w-btn-1"><a href="#"><span>提现</span></a></span></p>
          </div>
          <div class="m-addr">
            <div class="hd">
              <h3>收支详情</h3>
            </div>
            <div class="bd">
              <table class="m-table" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <th>时间</th><th>类型</th><th>订单号</th><th>金额</th>
                  </tr>
                  <tr>
                    <td>2014-9-2</td><td>订单支付</td><td>SER45987335</td><td>- ￥586</td>
                  </tr>
                 <tr>
                    <td>2014-9-2</td><td>订单支付</td><td>SER45987335</td><td>- ￥586</td>
                  </tr>
                  <tr>
                    <td>2014-9-2</td><td>订单支付</td><td>SER45987335</td><td>- ￥586</td>
                  </tr>
                  <tr>
                    <td>2014-9-2</td><td>订单支付</td><td>SER45987335</td><td>- ￥586</td>
                  </tr>
                  <tr>
                    <td>2014-9-2</td><td>订单支付</td><td>SER45987335</td><td>- ￥586</td>
                  </tr>
              </table>
              <div class="m-page2">
                <div class="pointer">
                  <a class="prev" href="#">上一页</a>
                  <ul>
                    <li><a href="#">1</a></li>
                    <li class="z-active"><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li class="ellips">...</li>
                  </ul>
                  <a class="next" href="#">下一页</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </@myModule>
    </div>
    <@footer/>
    <@copyright/>
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/profile/myWallet.js"></script>
  </body>
</html>
</@compress>
</#escape>