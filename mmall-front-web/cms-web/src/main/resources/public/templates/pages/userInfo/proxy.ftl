<#assign pageName="userInfo-proxy"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="${jspro}lib/bootstrap/fonts/glyphicons-halflings-regular.svg">
  <!-- @STYLE -->
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"用户管理","url":'#'} sub={"txt":"代客下单"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	代客下单
      </h2>
      <div class="card_c">
			<form class="form-inline" id="search-form" onsubmit="return false">
				<div class="form-group">
					<input type="text" class="form-control" id="searchValue" name="searchValue" placeholder="请输入 账号/昵称/手机号/邮箱  查找" style="width:300px;"/>
					<input type="button" value="查找" class="btn btn-primary j-flag" name="btn-search" id="searchBtn"/>
				</div>
			</form>
      </div>
      <div id="proxyList"></div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/userinfo/proxy.js?v=1.0.0.1"></script>
</body>
</html>