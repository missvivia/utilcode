<#assign pageName="content-helpcenter"/>
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
<@crumbs parent={"txt":"内容管理","url":'#'} sub={"txt":"帮助中心管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        帮助中心管理
      </h2>
      <div class="card_c">
        <form id="form-id">
        <div class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-1 control-label">平台：</label>
            <div class="col-sm-2">
              <select name="publishType" class="form-control" id="platform">
                <option value="-1">请选择</option>
                <option value="1">WEB</option>
                <option value="2">APP</option>
              </select>
            </div>
            <label class="col-sm-1 control-label">分类：</label>
            <div class="col-sm-2">
              <select autocomplete ="off"  name="categoryId" class="form-control" id="category">
                <option value="-1">请选择</option>
                <#list categoryList as category>
                <option value="${category.id}">${category.name}</option>
                </#list>
              </select>
            </div>
            <div class="col-sm-2" id="c-child">
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
	var g_categoryList=${categoryList_json!'null'};
	</script>
	</#noescape>
	<!-- /@NOPARSE -->
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/content/helpcenter/manage.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>