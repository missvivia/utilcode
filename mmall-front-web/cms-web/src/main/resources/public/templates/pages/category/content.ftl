<#assign pageName="category-content"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
  #categoryList .panel{width:200px;position:relative;float:left;margin:0 20px 20px 0;}
  #categoryList .panel-body{height:300px;padding:0 0 26px 0;}
  #categoryList .panel-inner{height:274px;overflow-y:auto;overflow-x:hidden;padding:0;}
  #categoryList ul{padding:0;}
  #contentList h2{padding:6px 15px;line-height:34px;}
  #detail h2 a{font-size:14px;}
  .selectorBox{margin:10px 0 0 0;}
  .selectorBox select{width:150px;}
  .panel-heading .remove{float:right;}
  #contentList .district{width:100%;position:absolute;right:0;bottom:3px;border-radius:2px;z-index:2;background:#fff;text-align:right;}
  #contentList .district span{background:#eee;padding:5px;}
  .list-group {
    border: 1px solid #ddd;
    border-radius: 5px;
    height: 300px;
    overflow: auto;
}
.multiArea{cursor:pointer;}
.multiArealist{display:none;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap >
<@crumbs parent={"txt":"分类管理","url":'/category/content'} sub={"txt":"內容分类"}/>
<!-- card -->
<div class="m-card" id="contentList">
	<h2 class="card_b">
		<#if siteCMSVO?exists>
		${siteCMSVO.siteName}
		</#if>
		<a class="btn btn-info" href="javascript:;" id="createTree">新建分类树</a>
	</h2>
	<div class="card_c selectorBox">
		<select id="areaSelector" class="form-control">
			<option value="0">选择区域查看</option>
			<#if siteCMSVO?exists>
			<#list siteCMSVO.areaList as area>
				<#if area.isChecked == 1>
				<option value="${area.areaId}">${area.areaName}</option>
				</#if>
			</#list>
			</#if>	
		</select>
	</div>
    <div class="card_c f-cb" id="categoryList">
      	  
    </div>
</div>

<div style="display:none" id="wgt-tpl">
<#noparse>
<textarea name="ntp" id='modifyCategoryName'>
    <div class="m-winform" style="width:400px;">
  		<div class="form-group">
			<input type="text" id="name" class="form-control"/>
		</div>
    </div>
    <div class="m-winbot">
      <div class="btns" style="text-align:center;">
        <span class="btn btn-primary j-flag">确定</span>
        <span class="f-mgl btn btn-primary j-flag">取消</span>
      </div>
    </div>
</textarea>
<textarea name="ntp" id='assignDistrictTpl'>
    <div class="m-winform form-horizontal" style="width:600px;">
  		<p>请选择需要分配的地区</p>
		<div class="form-group" id="districtBlock">
           <div class="col-sm-10" id="districtForm" style="padding-left:0;">
      			<div class="area form-group">
      				<div class="j-address col-sm-12" data-provinceId="" data-cityId="" data-districtId=""></div>
      			</div>
      	   </div>
		</div>
		<div class="form-group">
            <div class="col-sm-2">
              <label class="btn btn-link" id="districtAdd">添加区域</label>  
            </div>
        </div>		
    </div>
    <div class="m-winbot">
      <div class="form-group">
	      <div class="btns" style="text-align:center;">
	        <span class="btn btn-primary j-flag">确定</span>
	        <span class="f-mgl btn btn-primary j-flag">取消</span>
	      </div>
      </div>
    </div>
</textarea>
</#noparse>
<div>
</@wrap>
<#include "/wrap/widget.ftl" />
<!-- @script -->
<script type="text/javascript" src="${jslib}define.js?pro=${jspro}"></script>
<script type="text/javascript" src="${jspro}page/category/content.js?v=1.0.0.0"></script>
</body>
</html>