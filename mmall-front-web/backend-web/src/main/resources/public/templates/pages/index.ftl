<#assign pageName="index-status"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>商家管理系统首页</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/index.css">
</head>
<body>
<@side />
<@wrap>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        	任务概览
      </h2>
      <div class="card_c">
        <div>用户：<span id="username"></span></div>
        <div id="module-cnt">
        <#assign poStatusMap ={"1":"未完备","2":"待上线","3":"待开场","4":"结束","5":"售卖中","6":"失效"}/>
        <#if onlineScheduleList??&&onlineScheduleList?size!=0>
       	<h5 class="f-mgt">最近3个月已上线PO的状态</h5>
        <table class="table table-bordered table-condensed f-tac">
        		<#--
        		<tr>
			      <th colspan="6">待开票订单数：<a href="/sell/invoice">${receipt}</a></th>
			    </tr>
			    -->
			    <tr>
			      <td>PO编号</td>
			      <td class='th-xl' >PO名称</td>
			      <td class='th-xl' >PO的状态</td>
			      <td class='th-xl'>待拣货商品数</td>
			      <#--<td>待发货商品数</td>-->
			      <td>待退货确认商品数</td>
			    </tr>
			  <#--
			  <#assign onlineScheduleList = [
							    {"id":1,"status":3,"pick":123,"waiting":123,"return":2,"receipt":1243},
							    {"id":1,"status":3,"pick":123,"waiting":123,"return":2,"receipt":1243},
							    {"id":1,"status":3,"pick":123,"waiting":123,"return":2,"receipt":1243},
							    {"id":1,"status":3,"pick":123,"waiting":123,"return":2,"receipt":1243},
							    {"id":1,"status":3,"pick":123,"waiting":123,"return":2,"receipt":1243}
							  ]/>
				<#assign unlineScheduleList = [
									    {"id":1,"startTime":1413806631926,"pageStatus":123,"bannerStatus":123,"spreadStatus":2},
									    {"id":1,"startTime":1413806631926,"pageStatus":123,"bannerStatus":123,"spreadStatus":2},
									    {"id":1,"startTime":1413806631926,"pageStatus":123,"bannerStatus":123,"spreadStatus":2},
									    {"id":1,"startTime":1413806631926,"pageStatus":123,"bannerStatus":123,"spreadStatus":2},
									    {"id":1,"startTime":1413806631926,"pageStatus":123,"bannerStatus":123,"spreadStatus":2}
									  ]/>
				<#assign summary =   {"sale":123384,"saleCount":1243,"buyCount":1000,"saleRate":70,"supply":120000,"skuCount":800,"UVCount":1200043,"PVCount":2400043}
									  />
			  -->
			  <#assign statusMap = {"1":"售卖中","2":"结束"}/>
			  <#list onlineScheduleList as item>
			 	 <tr>
			      <td>${item.id}</td>
			      <td class='th-xl' >${item.title}</td>
			      <td class='th-xl' >${poStatusMap[item.status?string]}</td>
			      <td class='th-xl'>${item.pick}</td>
			      <#--<th>${item.waiting}</th>-->
			      <td>${item.return}</td>
			    </tr>
			  </#list>
        </table>
        </#if>
        <#if unlineScheduleList??&&unlineScheduleList?size!=0>
        <h5 class="f-mgt">最近3个月未上线PO的状态</h5>
        <table class="table table-bordered table-condensed f-tac">
			    <tr>
			      <th>PO编号</th>
			      <th>PO名称</th>
			      <th>PO状态</th>
			      <th class='th-xl' >上线时间</th>
			      <th class='th-xl'>待PO商品清单/资料</th>
			      <th>品购页状态</th>
			      <th>推广资料状态</th>
			    </tr>
			  <#assign statusMap ={"0":"待提交","1":"待提交","2":"已提交","3":"审核通过","4":"审核未通过"}/>
			  <#-- status: 1-待提交  2-审核中  3-审核通过 4-审核未通过-->
			  <#assign classMap ={"0":"pending","1":"pending","2":"pending","3":"pass","4":"fail"}/>
			  <#list unlineScheduleList as item>
			 	 <tr>
			      <td>${item.id}</td>
			      <td>${(item.title)!''}</td>
			      <td>${poStatusMap[(item.pageStatus)?string]}</td>
			      <td class='th-xl' >${item.startTime?number_to_date?string('yyyy-MM-dd')}</td>
			      <td class='th-xl ${classMap[item.prdStatus?string]}'>${statusMap[item.prdStatus?string]}</td>
			      <td class="${classMap[item.pageStatus?string]}">${statusMap[item.pageStatus?string]}</td>
			      <td class="${classMap[item.bannerStatus?string]}">${statusMap[item.bannerStatus?string]}</td>
			    </tr>
			  </#list>
        </table>
        </#if>
        <div>
        <a href="http://sj.baiwandian.cn" class="f-fr">查看更多</a>
        <h5 class="f-mgt">最近PO概览</h5>
        </div>
        <#setting number_format=",##0.00">
		<#setting locale="en_US">
        <table class="table table-bordered table-condensed f-tac">
			  <tr class="m-tblhd">
			  	<td>销售额（￥）</td>
			  	<td>供货指（￥）</td>
			  	<td>销售量</td>
			  	<td>SKU数</td>
			  	<td>购买人数</td>
			  	<td>售卖比</td>
			  	<td>UV</td>
			  	<td>PV</td>
			  </tr>
			  <tr>
			      <td>${(summary.sale)!''}</td>
			      <td>${(summary.supply)!''}</td>
			      
			      <td class='th-xl' >${(summary.saleCount)!''}</td>
			      <td class='th-xl' >${(summary.skuCount)!''}</td>
			      <td class='th-xl' >${(summary.buyCount)!''}</td>
			      
			      <td class='th-xl' >${(summary.saleRate)!''}</td>
			      <td class='th-xl' >${(summary.UVCount)!''}</td>
			      <td class='th-xl' >${(summary.PVCount)!''}</td>
			  </tr>
        </table>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/index.js"></script>
</body>
</html>