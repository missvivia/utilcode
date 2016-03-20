<#include "addpo.ftl">

<#assign pageName="schedule-manage"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style>
  	.statustip{height:40px;line-height:40px;color:red;margin-left:20px}
  	.addbtn{width:30%;}
  	.iptbox{width:70%;}
  	.iptbox .add1much{width:100%}
  	
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商务管理","url":'/schedule/manage'} sub={"txt":"档期管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        档期基本信息
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal">
                  <div class="form-group">
                    <div class="buadd-team buadd-id f-cb">
                      <label class="col-sm-4 control-buadd">档期ID：<#if schedule??><span id="poId">${schedule.id}</span></#if></label>
                      <#--status: 1-待提交  2-审核中  3-审核通过 4-审核未通过  showFlag: 1-档期结束  2-档期中 3-档期前4天内  4-档期前4填外-->
                      <#assign statusMap={"1":"待提交","2":"审核中","3":"审核通过","4":"审核未通过", "-1": "失效"}/>
                      <label class="col-sm-4 control-buadd">档期状态：${statusMap[schedule.status?string]}</label>
                    </div>
                    <div class="buadd-team f-cb">
                      <label class="col-sm-4">销售站点：<#list schedule.saleSiteList as item>${item.name}<#if item_has_next>,</#if></#list></label>
                      <label class="col-sm-4">PO名称：${schedule.title}</label>
                    </div>
                    <div class="buadd-team f-cb">
                    	<label class="col-sm-4 ">档期时间：<#if schedule??>${schedule.startTime?number_to_date?string('yyyy年MM月dd日')}</#if> ~ <#if schedule??>${schedule.endTime?number_to_date?string('yyyy年MM月dd日')}</#if></label>
                    </div>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <div class="buadd-team">
                      <label class="col-sm-2 control-buadd">合作条件</label>
                      <label class="col-sm-0 control-buadd"></label>
                    </div>
                    <#assign supplyMode ={"1":"自供货","2":"品牌商供货","3":"共同供货"}/>
                    <label class="col-sm-4 control-buadd">平台服务费率：${(schedule.platformSrvFeeRate)!''}%</label>
                    <label class="col-sm-4 control-buadd">销售价格区间：${(schedule.minPriceAfterDiscount)!''}~${(schedule.maxPriceAfterDiscount)!''}</label>
                    <label class="col-sm-4 control-buadd">折扣区间：${(schedule.minDiscount)!''}%~${(schedule.maxDiscount)!''}%</label>
                    <label class="col-sm-4 control-buadd">供货方式：<#if schedule??>${supplyMode[schedule.poType?string]}</#if></label>
                    <label class="col-sm-4 control-buadd">总件数：${(schedule.productTotalCnt)!''}</label>
                    <label class="col-sm-4 control-buadd">款数：${(schedule.unitCnt)!''}</label>
                    <label class="col-sm-4 control-buadd">SKU数：${(schedule.skuCnt)!''}</label>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <div class="buadd-team">
                      <label class="col-sm-2 control-buadd">入库信息</label>
                      <label class="col-sm-0 control-buadd"></label>
                    </div>
                    
                    <#if schedule.poType==1>
                    	<label class="col-sm-4 control-buadd">代理商入库仓库：${(schedule.supplierStoreName)!''}</label>
                    <#elseif schedule.poType==2>
                    	<label class="col-sm-4 control-buadd">品牌商入库仓库：${schedule.brandStoreName}</label>
                    <#elseif schedule.poType==3>
                      <label class="col-sm-4 control-buadd">代理商入库仓库：${(schedule.supplierStoreName)!''}</label>
                      <label class="col-sm-4 control-buadd">品牌商入库仓库：${schedule.brandStoreName}</label>
                    </#if>
                  </div>
                  <div class="hr-dashed"></div>
                 <div class="form-group">
                    <div class="buadd-team">
                      <label class="col-sm-2 control-buadd">商务信息</label>
                      <label class="col-sm-0 control-buadd"></label>
                    </div>
                    <label class="col-sm-4 control-buadd">PO跟进人：<#if schedule??>${schedule.poFollowerUserName}</#if></label>
                  </div>
        </form>
      </div>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        档期详情
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal" id="search-form">
	        <div class="form-group">
	            <label class="col-sm-1 control-label">商品类目</label>
	            <div class="col-sm-3">
	              <select id="category1" class="form-control j-cate" name="category1">
	                <option value="0">全部</option>
	              </select>
	            </div>
	            <label class="col-sm-1 control-label">二级类目</label>
	            <div class="col-sm-3 ">
	              <select id="category2" class="form-control j-cate" name="category2">
	                <option value="0">全部</option>
	              </select>
	            </div>
	            <label class="col-sm-1 control-label">三级类目</label>
	            <div class="col-sm-3">
	              <select name="lowCategoryId" id="" class="form-control j-cate">
	                <option value="0">全部</option>
	              </select>
	            </div>
	          </div>
	          
            <div class="form-group">
              <label class="col-sm-1 control-label">审核状态:</label>
              <div class="col-sm-3">
                <select name="status" class="form-control j-save" data-type="number" id="status">
                  <option value="0">全部</option>
                  <#list statusList as item>
                    <option value="${item.id}">${item.name}</option>
                  </#list>
                </select>
              </div>
            </div>

	          <div class="form-group">
	            <label class="col-sm-1 control-label">商品名称</label>
	            <div class="col-sm-3">
	              <input name="productName" type="text" class="form-control j-save">
	            </div> 
	            <label class="col-sm-1 control-label">商品货号</label>
	            <div class="col-sm-3">
	              <input name="goodsNo" type="text" class="form-control j-save" name="text">
	            </div>
	            <label class="col-sm-1 control-label">条形码</label>
	            <div class="col-sm-3">
	              <input name="barCode" type="text" class="form-control j-save" name="text">
	            </div>
	          </div>
          
          
	          <div class="form-group">
	            <div class="col-sm-2 col-sm-offset-1">
	            	<input type="button"  name="btn-submit" value="查询"  class="btn btn-primary btn-block">
	            </div>
	            <#if (schedule.status==1||schedule.status==4)&&schedule.showFlag==4>
	            <div class="col-sm-2 ">
	              <label class="btn btn-primary btn-block" id="batchimport">批量导入库存</label>
	            </div>
	            </#if>
	            <div class="col-sm-2 ">
	              <a class="btn btn-primary btn-block j-flag" target="_self" href="/schedule/product/export?id=${schedule.id}">批量导出库存</a>
	            </div>
	            <#if (schedule.status==1||schedule.status==4)&&schedule.showFlag==4>
	            <div class="col-sm-2 ">
	              <input type="button"  id="submit" data-id=${schedule.id} value="提交审核"  class="btn btn-inverse btn-block">
	            </div>
	            </#if>
	            <div class="col-sm-2 ">
	              <a class="btn btn-primary " target="_target" href="/res/files/档期商品批量导入模板.xlsx">批量导入库存模板下载</a>
	            </div>
	          </div>
        </form>
        <div class="form-group">
          <div class="addlist-hd f-cb">
            <ul id="addlist-tab" class="j-flag f-fl">
              <li class="addclick">全部商品</li>
              <li class="">已添加<i class="f-ftn j-flag"></i></li>
            </ul>
            <#if (schedule.status==1||schedule.status==4)&&schedule.showFlag==4>
              <#else>
              <div class="f-fl statustip">当前PO只能查看，不能再进行添加和取消操作，操作后将无法提交</div>
              </#if>
          </div>
          <div id="productlist" class="j-flag"></div>
          <div id="addedproductlist" class="j-flag"></div>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<script>
  window.category = ${stringify(categoryList)};
  window.status = ${schedule.status};
  window.scheduleId = ${schedule.id};
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/add.js"></script>


</body>
</html>