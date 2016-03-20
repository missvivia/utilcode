<#assign pageName="jit-polist"/>

<!doctype html>
<html lang="zh-cn">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/jit/podetail.css">
</head>
<body>
<@side />
<@wrap>
    <@crumbs parent={"txt":"JIT管理","url":"/jit/polist"} sub={"txt":"PO列表","url":"/jit/polist"} sub2={"txt":"PO详情"} />
    <div class="row">
       <div class="col-sm-12">
          <div class="m-card">
            <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span><label>PO单编号：${poOrderId?default('')}</label></h2>
            <div class="card_c">
                <div class="form-horizontal m-form1">
                  <div class="form-group">
                    <label class="col-sm-4">档期：${(poOrder.commodityStartTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}--${(poOrder.commodityEndTime?number_to_time?string("yyyy-MM-dd HH:mm:ss"))!''}</label>
                    <div class="col-sm-8"><a href="/jit/polist">返回PO列表&gt;&gt;</a></div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4">供货方式：<#if poOrder.supplyType?? && poOrder.supplyType == "SELF">商家自供<#else>品牌商参与供货</#if></label>
                  </div>
                </div>         
            </div>
          </div>
       </div>
    </div>
    
    <div class="row">
       <div class="col-sm-12">
          <div class="m-card">
            <div class="card_c">
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th>总供货量</th>
                      <#if poOrder.supplyType?? && poOrder.supplyType == "TOGETHOR">
                      <th>代理商自供货量</th> 
                      <th>品牌参与供货量</th>
                      </#if> 
                      <th>销售总量</th>
                      <th>未拣货数量</th>
                      <th>已拣货数量</th>
                      <th>到货数量</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>${(poOrder.totalStock)?default('')}</td>
                      <#if poOrder.supplyType?? && poOrder.supplyType == "TOGETHOR">
                      <td>${(poOrder.selfStock)?default('')}</td>
                      <td>${(poOrder.backupStock)?default('')}</td>
                      </#if> 
                      <td>${(poOrder.totalSales)?default('')}</td>
                      <td>${(poOrder.unPickedAmount)?default('')}</td>
                      <td>${(poOrder.pickedAmount)?default('')}</td>
                      <td>${(poOrder.arrivedAmount)?default('')}</td>
                    </tr>
                  </tbody>
                </table>
            </div>
          </div>
       </div>
    </div>
      
    <div class="row">
      <div class="col-sm-12">
        <div class="m-card">
          <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>查询</h2>
          <div class="card_c">
            <form method="get" class="form-horizontal m-form2" id="searchform" style="display:none;">
              <input type="hidden" value="${poOrder.poOrderId}" name="poOrderId"/>
              <div class="form-group">
                <label class="col-sm-1 control-label">拣货单号：</label>
                <div class="col-sm-2">
                  <input type="text" class="form-control ztag" name="pickOrderId"/>
                </div>
                <label class="col-sm-1 control-label">发货单号：</label>
                <div class="col-sm-2">
                  <input type="text" class="form-control ztag" name="shipOrderId"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label">拣货状态：</label>
                <div class="col-sm-2">
                  <select class="form-control ztag" name="pickStates" data-type="number">
                      <option value="3">请选择</option>
                      <option value="0">未拣货</option>
                      <option value="1">拣货中</option>
                      <option value="2">已拣货</option>
                  </select>
                </div>
                <label class="col-sm-1 control-label">发货状态：</label>
                <div class="col-sm-2">
                  <select class="form-control ztag" name="shipStates" data-type="number">   
                      <option value="2">请选择</option>
                      <option value="0">未发货</option>
                      <option value="1">已发货</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label">拣货时间：</label>
                <div class="col-sm-2">
                  <input type="text" class="form-control datePicker ztag" placeholder="起始时间" name="pickStartTime"/>
                </div>
                <div class="col-sm-2">
                  <span class="sep">至</span><input type="text" class="form-control datePicker ztag" placeholder="终止时间" name="pickEndTime" data-time="23:59:59"/>
                </div>
                <label class="col-sm-1 control-label">发货时间：</label>
                <div class="col-sm-2">
                  <input type="text" class="form-control datePicker ztag" placeholder="起始时间" name="shipStartTime"/>
                </div>
                <div class="col-sm-2">
                  <span class="sep">至</span><input type="text" class="form-control datePicker ztag" placeholder="终止时间" name="shipEndTime" data-time="23:59:59"/>
                </div>
              </div>
              <div class="form-group">
                <div class="col-sm-2 col-sm-offset-1">
                  <a class="btn btn-primary btn-block ztag">查询</a>
                </div>
                <div class="col-sm-2">
                  <a class="btn btn-primary btn-block ztag">导出数据</a>
                </div>
              </div>
            </form>
            <div class="hr-dashed" style="display:none;"></div>
            <table class="table table-striped">
                <thead>
                  <tr>
                    <th>条形码</th>
                    <th>SKU</th>
                    <th>商品名称</th>
                    <th>SKU数量</th>
                  </tr>
                </thead>
                <tbody id="poskulist">
                </tbody>
            </table>
            <div class="text-right">
                <div id="pager"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
              
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/podetail.js"></script>

</body>
</html>