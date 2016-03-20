<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "/wrap/common.ftl">
    <#include "/wrap/my.common.ftl">
    <meta charset="utf-8"/>
    <@title type="myOrder"/>
    <@css/>
    <link href="/src/css/base.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/member.css" rel="stylesheet" type="text/css" />
    <link href="/src/css/core.css?20150828" rel="stylesheet" type="text/css" />
  </head>
  <body id="index-netease-com">
    <@fixedTop />
    <@topbar />
    <@top />
    <@mainCategory />
    <@crumbs>
		 <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">收货地址</span>
    </@crumbs>
    <div class="bg-french-gray">
      <@myModule sideIndex=3>
    		<div class="m-main l">
                <div class="title clearfix">
                    <div class="m-status">
                        <ul>                             
                            <li class="bl active">全部订单</li>
                            <li>待付款</li>
                            <li>待发货</li>
                            <li>已发货</li>
                        </ul>
                    </div>
                    <div class="m-search r">
                        <input type="text" value="商品名称、订单编号" class="clear-input">
                        <span></span>
                    </div>
                </div>
                <div class="m-table">
                    <table>
                        <thead>
                            <tr>
                                <td width="55%">订单信息</td>
                                <td width="15%">订单状态</td>
                                <td width="15%">订单金额</td>
                                <td width="15%">操作</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <div class="m-info">
                                        <div class="m-base-info clearfix">
                                            <span class="date">2015-03-24</span>
                                            <span class="number">订单号：1003854590997324</span>
                                            <a href="#" class="name">潮航数码专营店</a>
                                        </div>
                                        <div class="m-product-info">
                                            <ul>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p style="display: none;">
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p style="display: none;">
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-status-hint">
                                        <span>订单关闭</span>
                                        <span>超时未付款</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-price">
                                        862.00
                                    </div>
                                </td>
                                <td>
                                    <div class="m-operation">
                                        <a href="#">订单详情</a>
                                        <a href="#">删除</a>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="m-info">
                                        <div class="m-base-info clearfix">
                                            <span class="date">2015-03-24</span>
                                            <span class="number">订单号：1003854590997324</span>
                                            <a href="#" class="name">潮航数码专营店</a>
                                        </div>
                                        <div class="m-product-info">
                                            <ul>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-status-hint">
                                        <span>订单关闭</span>
                                        <span>超时未付款</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-price">
                                        862.00
                                    </div>
                                </td>
                                <td>
                                    <div class="m-operation">
                                        <a href="#">订单详情</a>
                                        <a href="#">删除</a>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="m-info">
                                        <div class="m-base-info clearfix">
                                            <span class="date">2015-03-24</span>
                                            <span class="number">订单号：1003854590997324</span>
                                            <a href="#" class="name">潮航数码专营店</a>
                                        </div>
                                        <div class="m-product-info">
                                            <ul>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-status-hint">
                                        <span>订单关闭</span>
                                        <span>超时未付款</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-price">
                                        862.00
                                    </div>
                                </td>
                                <td>
                                    <div class="m-operation">
                                        <a href="#">订单详情</a>
                                        <a href="#">删除</a>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="m-info">
                                        <div class="m-base-info clearfix">
                                            <span class="date">2015-03-24</span>
                                            <span class="number">订单号：1003854590997324</span>
                                            <a href="#" class="name">潮航数码专营店</a>
                                        </div>
                                        <div class="m-product-info">
                                            <ul>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                                <li>
                                                    <img src="../test-pic/pic-11.jpg">
                                                    <p>
                                                        <span>12.00/件</span>
                                                        <span>共10件</span>
                                                    </p>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-status-hint">
                                        <span>订单关闭</span>
                                        <span>超时未付款</span>
                                    </div>
                                </td>
                                <td>
                                    <div class="m-price">
                                        862.00
                                    </div>
                                </td>
                                <td>
                                    <div class="m-operation">
                                        <a href="#">订单详情</a>
                                        <a href="#">删除</a>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="page">
                    <span class="prev">上一页</span>
                        <ul>
                            <li>1</li>
                            <li>2</li>
                            <li>3</li>
                            <li>4</li>
                            <li>5</li>
                            <li>...</li>
                            <li>7</li>
                        </ul>
                    <span class="next">下一页</span>
                    <span>共53页，到第</span>
                    <div class="skip">
                        <input tyep="text" value="1">
                        <div class="arrow">
                            <i class="up"></i>
                            <i class="down"></i>
                        </div>
                    </div>
                    <span>页</span>
                    <input type="button" value="确定">
                </div>
            </div>
      </@myModule>
    </div>
    <@footer/>
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/profile/address.js"></script>
  </body>
</html>
</@compress>
</#escape>