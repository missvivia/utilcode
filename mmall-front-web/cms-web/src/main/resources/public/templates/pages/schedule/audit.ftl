<#assign pageName="schedule-audit"/>
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
<@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"PO审核"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        内容管理
      </h2>
      <div class="card_c">
      	<form id="search-form" class="form-horizontal">
      		<div class="form-group">
      		<label class="col-sm-2 control-label">站点：</label>
      		<div class="col-sm-2">
      			<select class="form-control j-flag" name="province" data-type="number"><option value="0">全部</option><#list provinceList as item><option value="${item.id}">${item.name}</option></#list></select>
      		</div>
      		<div class="col-sm-2 col-sm-offset-1">
	      	  	<select class="form-control j-flag" name="type"  data-changeDisabled="true" autocomplete ="off">
	      	  		<option value="1">po编号</option>
	      	  		<option value="2">品牌名</option>
	      	  		<option value="3">商家帐号</option>
	      	  		<option value="4">商家ID</option>
		      	</select>
      	  	</div>
      	  	<div class="col-sm-2">
	      	  	<input class="form-control j-flag" name="key" autocomplete ="off"/>
      	  	</div>
      	  	<div class="col-sm-1">
	      	  	<input type="button" class="form-control btn btn-primary" name="btn-query" id="btn-query" value="查询"/>
      	  	</div>
      		<label class="col-sm-2 control-label"><a href="/schedule/calendar" target="_blank">查看档期日历</a></label>
        	</div>
        </form>
        <div class="hr-dashed"></div>
	  </div>
      <div class="card_c m-audit" id="audit">
        <div class="j-flag"></div>
        <div class="j-flag"></div>
        <m-tab on-change={{this.onchange($event)}}></m-tab>
        
        <div id="auditlist">
        </div>  
      </div>
    </div>
  </div>
</div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/audit.js?v=1.0.0.0"></script>

</body>
</html>