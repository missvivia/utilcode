<#assign pageName="user-query"/>
<#if RequestParameters.Callerid??>
  <#assign mobile=RequestParameters.Callerid/>
</#if>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
    .m-card td{word-break:break-all;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"用户资料查询","url":'#'} sub={"txt":"用户信息查询"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        用户资料查询
      </h2>
      <div class="card_c">
        <div>
          <div class="col-md-12">按用户资料查询</div>
          <div class="col-md-2">
          <select name="queryType" class="form-control j-node">
            <#assign queryList=[{"name":"绑定手机号","id":"4"},{"name":"收货人手机号","id":"6"},{"name":"用户名","id":"1"},{"name":"昵称","id":"2"},{"name":"用户ID","id":"3"},{"name":"绑定邮箱","id":"5"}] />
            <#list queryList as x>
              <option value="${x.id}">${x.name}</option>
            </#list>
          </select>
          </div>
          <form id="form">
            <div class="col-md-2">
              <input data-required="true" data-message="请输入查询条件" name="queryValue" type="text" class="form-control j-node" value="${mobile!''}"/>
            </div>
          </form>
          <button class="btn btn-primary j-node">查询</button>
        </div>
      </div>
      <div class="j-node"></div>
    </div>
  </div>
</div>
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/userinfo/index.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>