<#assign pageName="schedule-calendar"/>
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
<@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"档期日历"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        内容管理
      </h2>
      <div class="card_c m-audit" id="audit">
      <form class="form-horizontal" id="search-form">
      	<div class="form-group" >
        	<label class="col-sm-2 control-label">站点：</label>
  	  		<div class="col-sm-2"><select class="form-control j-flag" name="province" id="province"><#list provinceList as item><option value="${item.id}">${item.name}</option></#list></select></div>
  	  	</div>
        <div class="j-flag"></div>
        <div class="j-flag"></div>
      </form>
      </div>
    </div>
  </div>
</div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/calendar.js?v=1.0.0.0"></script>

</body>
</html>