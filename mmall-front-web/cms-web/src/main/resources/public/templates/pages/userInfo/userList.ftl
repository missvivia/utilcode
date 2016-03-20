<#assign pageName="userInfo-userList"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/item.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"用户管理","url":'#'} sub={"txt":"用户列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	 用户列表
      </h2>
      <div class="card_c">
      	  <div class="row">
			<form id="search-form">
				<div class="col-sm-1">
					<input type="button" value="新建买家" class="btn btn-primary j-flag" id="btn-create"/>
				</div>
				<div style="float:right;padding-right:15px;">
					<input type="text" class="form-control" id="searchValue" name="searchValue" placeholder="请输入 用户名/ID/手机号/邮箱  查找" style="width:300px;display:inlin-block;float:none;"/>
					<input type="button" value="查找" class="btn btn-primary j-flag" name="btn-search" id="searchBtn"/>
				</div>
			</form>
		  </div>
      </div>
      <div id="userlist"></div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/user/userinfo.js?v=1.0.0.5"></script>
</body>
</html>