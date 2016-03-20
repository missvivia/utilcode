

<!doctype html>
<html lang="en">
<head>
<#assign pageName="schedule-returndetail"/>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs1 crumbList=[{"txt":"档期管理","url":'#'},{"txt":"退货单","url":"../returnnote"},{"txt":"详情"}]/>
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
                    <div class="detail-team">
                      <label class="col-sm-2 control-buadd">基本信息</label>
                      <label class="col-sm-0 control-buadd"></label>
                    </div>
                    <label class="col-sm-4 control-buadd">退货单号：<span>${podetails.returnPoOrderId}</span></label>
                    <label class="col-sm-4 control-buadd">单据时间：<span>${podetails.createTime?number_to_date?string('yyyy-MM-dd')}</span></label>
                    <#--<label class="col-sm-4 control-buadd">所属站点：<span>${(podetails.provinceName)!'provinceName'}</span></label>-->
                    <label class="col-sm-4 control-buadd">退货状态：<span>${podetails.stateDesc}</span></label>
                    <label class="col-sm-4 control-buadd">商家帐号：<span>${(podetails.supplierAccount)!'supplierAccount'}</span></label>
                    <label class="col-sm-4 control-buadd">品牌名称：<span>${(podetails.brandName)!'brandName'}</span></label>
                    <label class="col-sm-4 control-buadd">公司名称：<span>${(podetails.companyName)!'companyName'}</span></label>
                    <label class="col-sm-4 control-buadd">PO编号：<span>${podetails.poOrderId}</span></label>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <div class="detail-team">
                      <label class="col-sm-2 control-buadd">物流信息</label>
                      <label class="col-sm-0 control-buadd"></label>
                    </div>
                    <label class="col-sm-4 control-buadd">承运商：${podetails.expressCompany}</label>
                    <label class="col-sm-4 control-buadd">运单号：${podetails.expressNO}</label>
                    <label class="col-sm-4 control-buadd">承运商联系电话：${podetails.expressPhone}</label>
                    <label class="col-sm-4 control-buadd">箱数：${podetails.shipBoxQTY}</label>
                    <label class="col-sm-4 control-buadd">件数：${podetails.count}</label>
                    <label class="col-sm-4 control-buadd">体积：${podetails.volume}立方米</label>
                    <label class="col-sm-4 control-buadd">重量：${podetails.weight}kg</label>
                    <label class="col-sm-4 control-buadd">所在仓库：${(podetails.warehouseName)!'warehouseName'}</label>
                    <label class="col-sm-4 control-buadd">收货地址：${podetails.receiverAddress}</label>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <div class="detail-team">
                      <label class="col-sm-2 control-buadd">退货商品明细</label>
                      <label class="col-sm-0 control-buadd"></label>
                    </div>
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
                                <th>PO编号</th>
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
                                  <td>${tpl.poOrderId}</td>
                                </tr>
						      </#list>
						    </tbody>
						  </table>
						</div>
                      </div>
                    </div>

                  </div>

        </form>
      </div>


    </div>
  </div>
</div>
<div style="display:none" id="jstTemplate">
    <#noparse>
      <textarea name="ntp" id="ntp-returndetail-window">
        <div class="m-returndetail">
          <div class="line">
            <label>发货仓库：</label>
            <label>华东仓</label>
          </div>
          <div class="line">
            <label>承运商：</label>
            <input type="text">
          </div>
          <div class="line">
            <label>单号：</label>
            <input type="text">
          </div>
          <div class="line">
            <label>承运商联系电话</label>
            <input type="text">
          </div>
          <div class="line">
            <label>箱数：</label>
            <input type="text">
          </div>
          <div class="line">
            <label>件数：</label>
            <input type="text">
          </div>
          <div class="line">
            <label>体积：</label>
            <input type="text">
          </div>
          <div class="line">
            <label>重量：</label>
            <input type="text">
          </div>
        </div>
        <div class="m-winbot">
          <div class="btns">
            <span class="btn btn-primary j-flag">提交</span>
            <span class="f-mgl btn btn-primary j-flag">取消</span>
          </div>
        </div>
      </textarea>
    </#noparse>
  </div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/returndetail.js?v=1.0.0.0"></script>

</body>
</html>