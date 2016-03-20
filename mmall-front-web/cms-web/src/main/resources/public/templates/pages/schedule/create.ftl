<#assign pageName="schedule-create"/>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/po.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"档期创建"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        档期创建
      </h2>
      <div class="card_c" id="pocreate">
        <div>
        	<h3>档期申请填写内容</h3>
        	<div>
            <form class="form-horizontal" id="dataform">
            <div class="form-group">
                <label class="col-sm-2 control-label">
                  商家帐号
                </label>
                <div class="col-sm-3">
              	  <input type="text" class="form-control" name ="supplierAcct" maxLength=50 value="${(schedule.supplierAcct)!''}" data-max-length=50 data-required="true" data-message="请输入商家帐号"/ >
                </div>
                <div class="col-sm-1">
                	 <span class="btn btn-primary" id="supplierAcct-btn">确定</span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">品牌</label>
                <div class="col-sm-3">
                	<label id="brandNname" class="control-label">${(schedule.brandName)!''}</label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">销售站点</label>
                <div class="col-sm-10" data-dft="${(schedule.saleSiteIds)!''}" id="saleSiteIds">
                 <#-- <select class="j-flag form-control" data-dft="${(schedule.saleSiteIds)!''}" name="saleSiteIds" multiple="multiple" data-required="true">
                  </select>
                  -->
                </div>
            </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">PO名称</label>
                <div class="col-sm-4">
                  <input type="text" class="form-control" name ="title" maxLength=50 value="${(schedule.title)!''}" data-max-length=24 data-required="true" data-message="不能为空，且最多12个中文，24个字符"/ >
                </div>
                <div class="col-sm-4">最多12个中文，24个字符</div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">品购页title</label>
                <div class="col-sm-4">
                  <input type="text" class="form-control" name ="pageTitle" value="${(schedule.pageTitle)!''}" data-max-length=180 data-required="true" data-message="不能为空，且最多90个中文，180个字符"/ >
                </div>
                <div class="col-sm-4">最多90个中文，180个字符</div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">展示分类</label>
                <div class="col-sm-10">
                  <#function isCategorySelect catList itm>
                    <#list catList as item>
                      <#if item==itm>
                        <#return true />
                      </#if>
                    </#list>
                    <#return false />
                  </#function>
                  <#list categoryList as item>
                    <input type="checkbox" name="adPosition" id="catgory_${item_index}" value="${item.name}" <#if (schedule.adPosition)??&&isCategorySelect(schedule.adPosition,item.name)>checked = checked</#if>  <#if item.name="首页">disabled="true" checked="checked"</#if>><label for="catgory_${item_index}">${item.name}</label>
                  </#list>
                </div>
              </div>
              <h3>档期时间</h3>
              <div class="form-group">
                <label class="col-sm-2 control-label">档期开始时间</label>
                <div class="col-sm-2">
                  <#--
                  <span class="m-date" ><input type="text"  class="j-control  form-control" data-type="date" value="<#if (schedule.startTime)??>${schedule.startTime?number_to_date?string("yyyy-MM-dd")}</#if>" name="startTime" data-required="true" data-message="请输入档期开始时间" data-ignore="true"/></span>
                -->
                <datepicker select="<#if ((schedule.startTime)??&&schedule.startTime>0)>${(schedule.startTime)?number_to_datetime?string("yyyy-MM-dd")}<#else>${(currTime+86400000*5)?number_to_datetime?string("yyyy-MM-dd")}</#if>"  name="startTime" range=${(currTime+86400000*4)}></datepicker>
                </div>
                <div class="col-sm-2">
                	<div class="sep">至</div>
                	<div class="input-group" id="endTime">
					    <input type="text" class="form-control" name="endTime" data-type="date" <#if ((schedule.endTime)??&&schedule.endTime>0)>value="${(schedule.endTime)?number_to_datetime?string("yyyy-MM-dd")}"<#else>value="${(currTime+86400000*5)?number_to_datetime?string("yyyy-MM-dd")}"</#if> readOnly>
					    <span class="input-group-btn">
					      <button class="btn btn-default" type="button">
					        <span class="glyphicon glyphicon-calendar"></span>
					      </button>
					    </span>
					  </div>
                </div>
                <!--
                <label class="col-sm-2 control-label">正常展示天数</label>
                <div class="col-sm-1">
               	 <select name="normalShowPeriod" class="form-control" autocomplete ="off">
               	 <#list 5..1 as num>
               	 <option value="${num}" <#if schedule??&&schedule.normalShowPeriod==num>selected="selected"</#if> >${num}</option>
               	 </#list>
               	 </select>
                </div>
                <label class="col-sm-2 control-label">延期展示天数</label>
                <div class="col-sm-1">
               	 <select name="extShowPeriod" class="form-control" autocomplete ="off">
               	 <#list 3..0 as num>
               	 <option value="${num}" <#if schedule??&&schedule.extShowPeriod==num>selected="selected"</#if> >${num}</option>
               	 </#list>
               	 </select>
                </div>
                -->
              </div>
              <h3>合作条件</h3>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">平台服务费率(%)</label>
                <div class="col-sm-1">
                  <input class="mark-num form-control" type="text"  data-type="number" maxLength=2 name="platformSrvFeeRate" value="${(schedule.platformSrvFeeRate)!''}" data-required="true" data-message="请输入平台服务费率"/>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">销售价格区间</label>
                <div class="col-sm-1">
                  <input type="text"   class="mark-num form-control"  data-float="1" maxLength=6 value="${(schedule.minPriceAfterDiscount)!''}" name="minPriceAfterDiscount" data-required="true" data-message="请输入销售价格区间"/>
                </div>
                <div class="col-sm-1">
                  <div class="sep">~</div>
                  <input type="text"   class="mark-num form-control" data-float="1" maxLength=6 value="${(schedule.maxPriceAfterDiscount)!''}" name="maxPriceAfterDiscount" data-required="true" data-message="请输入销售价格区间"/>
                </div>
              </div>
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">折扣率区间（%）</label>
                <div class="col-sm-1">
                  <input type="text" maxLength=3 class="mark-num form-control"   name="minDiscount"  value="${(schedule.minDiscount)!''}" data-required="true" data-message="请输入折扣区间" data-pattern="^[\d\.\d]+$"/>
                </div>
                <div class="col-sm-1">
                  <div class="sep">~</div>
                  <input type="text" maxLength=3 class="mark-num form-control" name="maxDiscount"  value="${(schedule.maxDiscount)!''}"  data-required="true" data-message="请输入折扣区间" data-pattern="^[\d\.\d]+$"/>
                </div>
			  </div>
			  <div class="form-group">
                <label class="col-sm-2 control-label">总件数</label>
                <div class="col-sm-1">
                  <input type="text" class="mark-num form-control" maxLength=6 name="productTotalCnt" value="${(schedule.productTotalCnt)!''}"  data-required="true" data-message="请输入总件数" data-type="number"/>
                </div>
                 <label class="col-sm-1 control-label">款数</label>
                <div class="col-sm-1">
                  <input type="text" class="mark-num form-control" maxLength=6 value="${(schedule.unitCnt)!''}" name="unitCnt" data-required="true" data-message="请输入款数" data-type="number"/>
                </div>
                <label class="col-sm-1 control-label">SKU数</label>
                <div class="col-sm-1">
                  <input type="text" class="mark-num form-control" maxLength=4 value="${(schedule.skuCnt)!''}" name="skuCnt" data-required="true" data-message="请输入SKU数" data-type="number"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">供货方式</label>
                <div class="col-sm-2">
                  <label class=" control-label"><input type="radio" class="" name="supplyMode" value="1" id="supplyMode-1" autocomplete ="off"/>  商家自供货</label>
                </div>
                <div class="col-sm-2">
                  <label class=" control-label"><input type="radio" class="" name="supplyMode" value="2"  id="supplyMode-2" autocomplete ="off"/>  共同供货</label>
                </div>
              </div>
              <h3>入库信息</h3>
              <div class="form-group" id="ruku-xinxi">
                <label class="col-sm-2 control-label">代理商入库仓库</label>
                <div class="col-sm-2">
                  <select class="j-flag form-control" disabled="disabled" name="supplierStoreId" id="supplierStoreId" data-type="number" autocomplete ="off">
                    <#list warehouseList as item>
                      <option value="${item.id}" <#if schedule??&&schedule.supplierStoreId?string==item.id>selected="selected"</#if>>${item.name}</option>
                    </#list>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">品牌商入库仓库</label>
                <div class="col-sm-2">
                  <select class="j-flag form-control" disabled="disabled"  name="brandStoreId" id="brandStoreId" data-type="number" autocomplete ="off">
                  	<#list warehouseList as item>
                      <option value="${item.id}" <#if schedule??&&schedule.brandStoreId?string==item.id>selected="selected"</#if>>${item.name}</option>
                    </#list>
                  </select>
                </div>
              </div>
              <h3>商务信息</h3>
              <div class="form-group">
                <label class="col-sm-2 control-label">
                  PO跟进人
                </label>
                <div class="col-sm-3">
              	  <input type="text" class="form-control" name ="poFollowerUserName"  value="<#if (schedule.poFollowerUserName)??>${schedule.poFollowerUserName!''}<#else>${poFollower.name!''}</#if>"  data-required="true" data-message="请输入PO跟进人"/ >
                </div>
                <div class="col-sm-1">
                	 <span class="btn btn-primary" id="poFollowerUserName-btn">确定</span>
                </div>
                <div class="col-sm-4">
                	 请填写跟进人帐号，并确认该进人有PO审核权限
                </div>
              </div>
              <div class="form-group">
	             <div class="col-sm-2">
	              <span class="btn btn-primary btn-block" id="submit">提交</span>
	             </div>
	             <div class="col-sm-2">
	            	 <span class="btn btn-primary btn-block" id="save">保存</span>
	             </div>
	          </div>
            </form>
          </div>
        </div>
      </div>
      
      
    </div>
  </div>
</div>
</@wrap>
	<!-- @NOPARSE -->
	<#noescape>
	<script>
	var g_schedule=${schedule_json!'null'},
		g_currTime=${currTime};
	</script>
	</#noescape>
	<!-- /@NOPARSE -->

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/create.js?v=1.0.0.0"></script>

</body>
</html>
</#escape>