<#assign pageName="app-pmessage"/>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
  .bg-model .modal-dialog{width:800px;}
  .m-error input{border: 1px solid #d44950!important;}
  .m-error label.text{color: #d44950; display: block;line-height: 34px;}
  .m-addpic{padding-bottom:10px;}
  span.text-info{cursor:pointer;}
  span.text-info:hover{color:#245269;text-decoration:underline;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"APP内容管理","url":'#'} sub={"txt":"push消息管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        PUSH消息管理
      </h2>
      <div class="card_c">
        <form id="form-id">
        <div class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-2 control-label">站点：</label>
            <div class="col-sm-2">
              <select name="areaId" class="form-control" id="areaId">
                <option value="0">请选择</option>
                <#list provinceList as province>
                <option value="${province.id}">${province.name}</option>
                </#list>
              </select>
            </div>
            <label class="col-sm-2 control-label">客户端平台：</label>
            <div class="col-sm-2">
              <select name="os" class="form-control" id="os">
                <option value="-1">请选择</option>
                <#list os as o>
                <option value="${o.name}">${o.name}</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">标题：</label>
            <div class="col-sm-3">
	             <input name="search" class="form-control"/>
            </div>
            <div class="col-sm-2">
              <a class="btn btn-primary btn-block" id="doquery">查询</a>
            </div>
          </div>
        </div>
        <form>
        <div id="module-cnt"></div>
      </div>
    </div>
  </div>
</div>
</@wrap>
<!-- @NOPARSE -->
	<#noescape>
	<script>
	var g_os=${os_json!'null'},
		g_provinceList=${provinceList_json!'null'};
	</script>
	</#noescape>
	<!-- /@NOPARSE -->
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/app/pmessage/manage.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>