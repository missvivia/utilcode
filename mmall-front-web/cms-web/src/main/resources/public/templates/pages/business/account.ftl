<#assign pageName="business-account"/>
<#--定义商家状态-->
<#assign buStatus = ["正常","暂停","关店"]/>
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
<@crumbs parent={"txt":"商家管理","url":'#'} sub={"txt":"商家列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       商家列表
      </h2>
      <div class="card_c">
      	<form class="form-horizontal" id="search-form">
      		<div class="form-group">
      			<div class="col-sm-4">
		    	<a class="btn btn-primary" href="/business/create">创建帐号</a>
		    	<!--<button class="btn btn-primary" id="deleteAll" disabled = "disabled">删除</button>-->
		    	</div>
		    </div>
		    <div class="hr-dashed"></div>
		    
      		<div class="form-group">
      			<!--<label class="col-sm-1 control-label">筛选：</label>
	      		
		        <div class="col-sm-2">
		          <select class="form-control j-flag" id="province" name="province">
		          	<option value="0">全部配送区域</option>
		          <#if provinceList??>
		            <#list provinceList as item>
		              <option value="${item.id}">${item.districtName}</option>
		            </#list>
		          	</#if>
		          </select>
		        </div>
		        
	        	<div class="col-sm-2">
		          <select class="form-control j-flag" id="status">
		          	<option value="0">全部状态</option>
		          	<#if provinceList??>
		            <#list provinceList as item>
		              <option value="${item.id}">${item.districtName}</option>
		            </#list>
		          	</#if>
		          </select>
		        </div>-->
		        
		        <div class="col-sm-2">
		        	<select class="form-control" id="search-list" data-changeDisabled=true>
			        	<option value="0">商家ID：</option>
						<option value="1">商家帐号：</option>
		        	</select>
		        </div>		        
		        <div class="col-sm-2">
		       		<input type="text" class="form-control" name="businessId" id="search-text">
		        </div>
		        <div class="col-sm-2">
		        	<select class="form-control" name="asc" data-changeDisabled=true><option value="false">权重从高到低</option><option value="true">权重从低到高</option></select>
		        </div>		        
		        <div class="col-sm-1">
		          <input type="button" value="搜索" class="btn btn-primary j-flag" name="btn-search" id="searchBtn"/>
		        </div>
	     	</div>
		    <div class="hr-dashed"></div>
	    </form>
      </div>
      <div class="card_c">
       <div id="accountlist"></div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/business/account.js?v=1.0.0.2"></script>

</body>
</html>