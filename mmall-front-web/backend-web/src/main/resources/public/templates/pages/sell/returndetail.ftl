<#assign pageName="sell-return"/>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"销售管理","url":'/sell/return'} sub={"txt":"退货管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        退货单详情
      </h2>
      <div class="card_c">
       <form method="get" class="form-horizontal">
                  <div class="form-group form-returndetail">
                    <h3 class="selldetail-dh">基本信息</h3>
                    <label class="col-sm-4 control-buadd">退货单号：<span>${(podetails.returnPoOrderId)!''}</span></label>
                    <label class="col-sm-4 control-buadd">单据时间：<span>${podetails.createTime?number_to_date?string('yyyy-MM-dd')}</span></label>
                    <label class="col-sm-4 control-buadd">退货状态：<span>${podetails.stateDesc}</span></label>
                    <label class="col-sm-4 control-buadd">PO单编号：<span>${podetails.poOrderId}</span></label>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <h3 class="selldetail-dh">物流信息</h3>
                    <label class="col-sm-4 control-buadd">承运商：${podetails.expressCompany}</label>
                    <label class="col-sm-4 control-buadd">运单号：${podetails.expressNO}</label>
                    <label class="col-sm-4 control-buadd">承运商联系电话：${podetails.expressPhone}</label>
                    <label class="col-sm-4 control-buadd">箱数：${podetails.shipBoxQTY}</label>
                    <label class="col-sm-4 control-buadd">件数：${podetails.count}</label>
                    <label class="col-sm-4 control-buadd">体积：${podetails.volume}</label>
                    <label class="col-sm-4 control-buadd">重量：${podetails.weight}</label>
                    <label class="col-sm-4 control-buadd">所在仓库：${podetails.warehouseName}</label>
                    <label class="col-sm-4 control-buadd">所在仓库：${(podetails.warehouseName)!'warehouseName'}</label>
                    <label class="col-sm-4 control-buadd">收货地址：${podetails.receiverAddress}</label>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <h3 class="selldetail-dh">退货商品明细</h3>
                    <div class="card_c">
                     	<div class="clst_action">
          					<div class="m-wtable" id="wtable">
							  <table class="table table-striped">
							    <thead>
							      <tr>
							        <th>商品条码</th>
							        <th>商品名称</th>
							        <th>应退数量</th>
                                    <th>实退数量</th>
                                    <th>箱号</th>
							        <th>退货类型</th>
							      </tr>
							    </thead>
							    <tbody>
							      <#list podetails.skuDetails as tpl>
							        <tr>
							          <td>${(tpl.barCode)!'barCode'}</td>
							          <td>${tpl.productName}</td>
							          <td>${tpl.skuCount}</td>
                                      <td>${tpl.realCount}</td>
                                      <td>${(tpl.boxNo)!'boxNo'}</td>
							          <td>${(tpl.typeDesc)!'typeDesc'}</td>
							        </tr>
							      </#list>
							    </tbody>
							  </table>
							</div>
        				</div>
                    </div>
                  </div>
                  <div id="action" class="form-group">
                  <#if podetails.state=="NEW">
                    <div class="col-sm-4">
			        	<a class="btn btn-primary j-btn" target="_blank" href="/oms/returnOrder/export/${(podetails.returnPoOrderId)!''}">导出</a>
			        	<span class="btn btn-primary j-btn f-mgl" id="return" data-id="${(podetails.returnPoOrderId)!''}" data-url="/oms/returnOrder/confirm/${(podetails.returnPoOrderId)!''}" data-message="确认退货后，新云联百万店会将退货商品打包发货到您的仓库">确认退货</span>
			        </div>
			        
                  <#elseif podetails.state=="CONFIRM">
                  	<div class="col-sm-4">
			        	<a class="btn btn-primary j-btn" target="_blank" href="/oms/returnOrder/export/${(podetails.returnPoOrderId)!''}">导出</a>
			        	<span class="btn btn-primary j-btn f-mgl" id="received" data-id="${(podetails.returnPoOrderId)!''}" data-url="/oms/returnOrder/ok/${(podetails.returnPoOrderId)!''}" data-message="确认已收到退货商品？">确认已收货</span>
			        </div>
                  <#elseif podetails.state=="SHIPPED">
                    <div class="col-sm-4">
			        	<a class="btn btn-primary j-btn" target="_blank" href="/oms/returnOrder/export/${(podetails.returnPoOrderId)!''}">导出</a>
			        	<span class="btn btn-primary j-btn f-mgl" id="received" data-id="${(podetails.returnPoOrderId)!''}" data-url="/oms/returnOrder/ok/${(podetails.returnPoOrderId)!''}" data-message="确认已收到退货商品？">确认已收货</span>
			        </div>
                  <#elseif podetails.state=="RECEIPTED">
                  <div class="col-sm-2">
			        	<a class="btn btn-primary j-btn" target="_blank" href="/oms/returnOrder/export/${(podetails.returnPoOrderId)!''}">导出</a>
			        </div>
                  </#if>
		        </div>
        </form>
        
      </div>


    </div>
  </div>
</div>
</@wrap>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/sell/returndetail.js"></script>

</body>
</html>