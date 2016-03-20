<#assign pageName="category-content"/>
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
<@wrap >
<#if result?? && result.id??>
<@crumbs parent={"txt":"分类管理","url":"/category/content/editContentTree?rootId=${(RequestParameters['rootId'])!'0'}"} sub={"txt":"编辑分类"}/>
<#else>
<@crumbs parent={"txt":"分类管理","url":"/category/content/editContentTree?rootId=${(RequestParameters['rootId'])!'0'}"} sub={"txt":"新建分类"}/>
</#if>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        <#if result?? &&result.id??>
       		  编辑分类
       	<#else>
       	 	新建分类
       	</#if>
      </h2>
      <div class="card_c">
      		<form method="post" class="form-horizontal" id="createCategory">
      		    <#if result?? && result.id??>
      		    <input type="hidden" name="id" value="${result.id}"/>
      		    </#if>
      		    <div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>显示顺序</label>
      				<div class="col-sm-2">
      					<input type="text" class="form-control" id="showIndex" value="${(result.showIndex)!''}" placeholder="请输入显示顺序字数" data-focus="true" data-required="true" data-pattern="^\d+$" name="showIndex" data-message="请输入数字"/>
      				</div>
      			</div>
      			<div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>内容分类名称:</label>
      				<div class="col-sm-2">
      					<input type="text" class="form-control" id="name" value="${(result.name)!''}" placeholder="请输入名称" data-focus="true" data-required="true" name="name" data-message="必填项"/>
      				</div>
      			</div>
      			<div style="display:none;"><input type="text"/></div>
      			<div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>上级分类:</label>
      				<div class="col-sm-2">
      					<#if result?? &&result.parentId??>
	      					<select  class="form-control" id="category" data-value="${(result.parentId)!''}" name="superCategoryId"  data-required="true"  data-message="必选项" disabled>
	      						<option value="0" level="0">无上级</option>
	      					</select>
      					<#else>
	      					<select  class="form-control" id="category" data-value="${(RequestParameters["id"])!'0'}" name="superCategoryId"  data-required="true"  data-message="必选项">
	      						<option value="0" level="0">无上级</option>
	      					</select>
      					</#if>
      				</div>
      			</div>
      			<div class="form-group" id="districtBlock" style="display:none;">
      			   <label class="col-sm-2 control-label"><span class="u-required">*</span>区域:</label>
                   <div class="col-sm-10" id="districtForm" style="padding-left:0;">
	      			  <#if result?? &&result.sendDistrictDTOs??&&(result.sendDistrictDTOs?size>0)>
	                    <#list result.sendDistrictDTOs as district>
	                      <div class="form-group">
	                        <div class="j-address col-sm-12" data-id="${(district.id)!''}" data-provinceId="${(district.provinceId)!''}" data-cityId="${(district.cityId)!''}" data-districtId="${(district.districtId)!''}"></div>
	                      </div>
	                    </#list>
	                  <#else>
		      			<div class="area form-group">
		      				<div class="j-address col-sm-12" data-provinceId="" data-cityId="" data-districtId=""></div>
		      			</div>
		      		  </#if>
		      	   </div>
      			</div>
      			<div class="form-group" style="display:none;">
                   <label class="col-sm-2 control-label"></label>
                    <div class="col-sm-2">
                      <label class="btn btn-link" id="districtAdd">添加区域</label>  
                    </div>
                </div>
      			<div class="form-horizontal" id="relevanceForm" style="display:none;">
	      			<div class="form-group">
	      				<label class="col-sm-2 control-label"><span class="u-required">*</span>关联分类：</label>
	      				<div class="col-sm-2">
	      					<a class="btn btn-default" id="addRelevance">添加关联分类</a>
	      				</div>
	      			</div>
	      			<div class="form-group">
	      				<div class="col-sm-3 col-sm-offset-2">
	      					<ul id="categrod-wrap" class="categrod-wrap">
	      						<#if result?? &&result.categoryNormalVOs??&&(result.categoryNormalVOs?size>0)>
	      							<#list result.categoryNormalVOs as vo>
	      							<li data-id="${vo.categoryId}"} data-name="${vo.categoryName}"}>${vo.categoryName}<span class="glyphicon glyphicon-remove-circle close" style="top:4px;"></span></li>
	      							</#list>
	      						</#if>
	      					</ul>
	      				</div>
	      			</div>
	      		</div>
	      		<div class="form-group">
				    <div class="col-sm-3 col-sm-offset-2">
				    	<#if result?? && result.id??>
				      		<a class="btn btn-primary" id="updateBtn">保存</a>
				      	<#else>
				      		<a class="btn btn-primary" id="submitBtn">提交</a>
				      	</#if>
				      	<a class="btn btn-primary" id="goToDetail" style="margin-left:15px;">取消</a>
				    </div>
				 </div>
      		</form>
      </div>
    </div>
  </div>
</div>
</@wrap>
<!-- @script -->
<script type="text/javascript">
window.__rootId = ${(RequestParameters["rootId"])!'0'};
</script>
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/category/createCategory.js?v=1.0.0.0"></script>
</body>
</html>