<#assign pageName="schedule-view"/>

<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/schedule.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"档期查看"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        档期查看
      </h2>
      <div class="card_c">
        <div class="m-exhibitionact" id="exhibition">
          <h3>基本信息</h3>
          <div class="hr-dashed"></div>
        	<#if schedule??>
        	<div class="row">
            <div class="col-md-4">PO编辑： ${schedule.id} </div>
            <div class="col-md-4">商家帐号： ${schedule.supplierAcct} </div>
        		<div class="col-md-4">品牌： ${schedule.brandName} </div>
        		<div class="col-md-4">销售站点： <#list schedule.saleSiteList as item>${item.name}<#if item_has_next>,</#if></#list></div>
        		<div class="col-md-4">PO名称： ${schedule.title} </div>
        		<div class="col-md-4">品购页title： ${schedule.pageTitle} </div>
        		<div class="col-md-4">展示分类：  <#list schedule.adPosition as item>${item}<#if item_has_next>,</#if></#list> </div>
        	</div>
        	<h3>档期时间</h3>
        	<div class="hr-dashed"></div>
        	<div class="row">
        		<div class="col-md-4">开始日期：${schedule.startTime?number_to_datetime?string('yyyy-MM-dd')}</div>
        		<div class="col-md-4">正常展示天数：${schedule.normalShowPeriod}</div>
        		<div class="col-md-4">延期展示天数：${schedule.extShowPeriod}</div>
        	</div>
        	<h3>合作条件</h3>
        	<div class="hr-dashed"></div>
        	<div class="row">
        		<div class="col-md-4">平台服务费率：${schedule.platformSrvFeeRate}%</div>
        		<div class="col-md-4">销售价格区间：${schedule.minPriceAfterDiscount}~${schedule.maxPriceAfterDiscount}</div>
        		<div class="col-md-4">折扣区间：${schedule.minDiscount}%~${schedule.maxDiscount}%</div>
        		<div class="col-md-4">总件数：${schedule.productTotalCnt}</div>
        		<div class="col-md-4">款数：${schedule.unitCnt}</div>
        		<div class="col-md-4">SKU数：${schedule.skuCnt}</div>
        		<div class="col-md-4">供货方式：<#if schedule.poType==1>代理商自己供货<#elseif schedule.poType==2>品牌商自己供货<#elseif schedule.poType==3>代理商和品牌商共同供货</#if></div>
        	</div>
        	<h3>入库信息</h3>
          <div class="hr-dashed"></div>
          <div class="row">
            <#if schedule.poType==1>
            <div class="col-md-4">代理商入库仓库：${(schedule.supplierStoreName)!''}</div>
            <#elseif schedule.poType==2>
            <div class="col-md-4">品牌商入库仓库：${(schedule.brandStoreName)!''}</div>
            <#elseif schedule.poType==3>
            <div class="col-md-4">代理商入库仓库：${(schedule.supplierStoreName)!''}</div>
            <div class="col-md-4">品牌商入库仓库：${(schedule.brandStoreName)!''}</div>
            </#if>
          </div>
          <h3>商务信息</h3>
          <div class="hr-dashed"></div>
          <div class="row">
            <div class="col-md-4">PO跟进人：${schedule.poFollowerUserName}</div>
          </div>
        	
        	<#elseif noAccess??&&noAccess>
        		<div class="m-empty">
        			你没有该PO的管理权限
        		</div>
        	<#else>
        		<div class="m-empty">
        			请输入PO编号查询要管理的PO
        		</div>
        	</#if>
        	<div class="hr-dashed"></div>
        	<div class="form-group row">
               <div class="col-sm-2">
                <a class="btn btn-primary btn-block" href="javascript:window.close()">关闭</a>
               </div>
            </div>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>
<!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/schedule/view.js?v=1.0.0.0"></script>
</body>
</html>