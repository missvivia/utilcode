<#assign pageName="user-info"/>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/user.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"用户信息查询","url":'#'} sub={"txt":"用户信息详情"}/>
<!-- card -->
<div>
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        用户信息查询
        <a href="javascript:history.go(-1)">返回</a>
      </h2>
      <div class="card_c">
      	<div class="f-cb">
	        <div class="col-sm-5">
	          <m-user-info-user userId=${(RequestParameters["userId"])!''}></m-user-info-user>
	        </div>
	        <div class="col-sm-7">
	          <m-user-info-address userId=${(RequestParameters["userId"])!''}></m-user-info-address>
	        </div>
        </div>
        <div class="m-row">
          <m-user-info-red userId=${RequestParameters["userId"]}></m-user-info-red>
        </div>
        <div class="m-row">
          <m-user-info-ticket userId=${(RequestParameters["userId"])!''}></m-user-info-ticket>
        </div>
        <div class="m-row">
          <m-user-info-order userId=${(RequestParameters["userId"])!''}></m-user-info-order>
        </div>
      </div>
  </div>
</div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/userinfo/userinfo.js?v=1.0.0.0"></script>

</body>
</html>
</#escape>