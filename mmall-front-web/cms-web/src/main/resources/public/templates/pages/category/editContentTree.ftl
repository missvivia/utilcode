<#assign pageName="category-content"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
#area{height:350px;}
#districtForm .col-sm-2{width:150px;}
.editAddress{padding:6px 10px;width:200px;border:1px solid #ccc;margin-left:15px;}
.editAddress span{float:right;margin-top:9px;}
.btn-clicked{background:#0d47a1;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap >
<@crumbs parent={"txt":"分类管理","url":'/category/content'} sub={"txt":"內容分类"}/>
<!-- card -->

<div class="row" id="detail">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span><span id="detailHeader">${result.name}</span>
      	<a href="javascript:;" class="editCategoryName">修改</a>
      </h2>
      <div class="card_c">
      	<div class="row">
			<div class="col-sm-3 f-fl">
				<a class="btn btn-primary" href="/category/content/createPage?id=0&rootId=${result.id}">新建分类</a>
				<button class="btn btn-primary"  id="openall">全部收起</button>
				<button class="btn btn-primary btn-clicked"  id="openclose">全部展开</button>
				<button class="btn btn-primary"  id="assignDistrict">分配到区域</button>
			</div>
			<form id="search-form" onsubmit="return false;">
				<div class="col-sm-3 f-fr">
					<span class="form-inline">
						<input type="text" class="form-control" id="searchValue" name="name" placeholder="请输入关键字搜索"/>
					</span>
					<input type="button" value="查找" class="btn btn-primary j-flag" name="btn-search" id="btn-search"/>
				</div>
			</form>
		  </div>
      </div>
      <div class="card_c" id="categrid">
      	  
      </div>
      <div id="anchorp"></div>
      <div id="sendDistrictDTOs" style="display:none;"><#if result.sendDistrictDTOs?exists><#list result.sendDistrictDTOs as area>${(area.edit)?c}-${area.provinceId}-${area.cityId}-${area.districtId}-${area.provinceName}-${area.cityName}-${area.distName}#</#list></#if></div>
    </div>
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
           <div class="col-sm-10" id="districtForm" style="padding-left:0;max-height:200px;overflow:auto;">
      			
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
</div>
</@wrap>
<!-- @script -->


<script type="text/javascript">
window.__rootId = ${(RequestParameters["rootId"])!'0'};
</script>
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/category/detail.js?v=1.0.0.4"></script>
</body>
</html>