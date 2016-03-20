<#assign pageName="schedule-schedulelist"/>
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
<@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"PO列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        PO列表
      </h2>
      <div class="card_c">
      	<form class="form-horizontal" id="search-form">
      	  <div class="form-group" >
      	  	<label class="col-sm-1 control-label">选择省：</label>
      	  	<div class="col-sm-2"><select class="form-control j-flag" name="province"><option value="0">全部</option><#list provinceList as item><option value="${item.id}">${item.name}</option></#list></select></div>
      	  	<label class="col-sm-1 control-label">状态：</label>
      	  	<div class="col-sm-2">
	      	  	<select class="form-control j-flag" name="status">
	      	  		<option value="0">全部</option>
	      	  		<#list statusList as item>
		      			<option value="${item.id}">${item.name}</option>
		      		</#list>
		      	</select>
      	  	</div>
      	  	<div class="col-sm-2 col-sm-offset-1">
	      	  	<select class="form-control j-flag" name="type">
	      	  		<option value="1">po编号</option>
	      	  		<option value="2">品牌名</option>
	      	  		<option value="3">商家帐号</option>
	      	  		<option value="4">商家ID</option>
		      	</select>
      	  	</div>
      	  	<div class="col-sm-2">
	      	  	<input class="form-control j-flag" name="key"/>
      	  	</div>
      	  	<div class="col-sm-1">
	      	  	<input type="button" class="form-control btn btn-primary" name="btn-submit" value="查询"/>
      	  	</div>
      	  </div>
	      <div class="hr-dashed"></div>
	     <div class="form-group">
	     	<label class="col-sm-2 control-label"><a href="/schedule/create" class="btn btn-primary">创建PO</a></label>
	     	<label class="col-sm-2 control-label"><a href="/schedule/calendar" class="">查看档期日历</a></label>
	     	
	     </div>
	    </form>
      </div>
      <div class="card_c" id="schedulelist">
        
      </div>
      
      
    </div>
  </div>
</div>
</@wrap>

<!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/schedule/list.js?v=1.0.0.0"></script>
</body>
</html>