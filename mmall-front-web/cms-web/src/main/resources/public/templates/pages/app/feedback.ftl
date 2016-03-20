<#assign pageName="app-feedback"/>
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
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"APP内容管理","url":'#'} sub={"txt":"意见反馈管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        意见反馈管理
      </h2>
      <div class="card_c">
        <form id="form-id">
        <div class="form-horizontal m-form-polist">
          <div class="form-group">
            <div class="col-sm-3">
             	<datepicker select="${(currTime-2592000000*3)?number_to_datetime?string("yyyy-MM-dd")}" value="" name="startTime"></datepicker>
            </div>
            <div class="col-sm-1 f-tac">
             	<label class=" control-label ">至</label>
            </div>
            <div class="col-sm-3">
             	<datepicker select="${(currTime)?number_to_datetime?string("yyyy-MM-dd")}"  name="endTime" ></datepicker>
            </div>
            <div class="col-sm-2">
              <select name="areaId" class="form-control" id="areaId">
                <option value="0">全部地区</option>
                <#list provinceList as province>
                <option value="${province.id}">${province.name}</option>
                </#list>
              </select>
            </div>
            <div class="col-sm-2">
              <select name="system" class="form-control" id="system">
                <option value="-1">全部系统</option>
                <#list systems as system>
                <option value="${system}">${system}</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="form-group">
          	 <div class="col-sm-2">
              <select name="version" class="form-control" id="version">
                <option value="-1">全部版本</option>
                <#list versions as version>
	                <#if version??>
	                <option value="${version}">${version}</option>
	                </#if>
                </#list>
              </select>
            </div>
            <div class="col-sm-3">
	             <input name="key" class="form-control" placeholder="按内容关键字搜索"/>
            </div>
            <div class="col-sm-2">
              <a class="btn btn-primary btn-block" id="doquery">查询</a>
            </div>
          </div>
           <div class="form-group">
            <div class="col-sm-2">
              <a class="btn btn-primary" id="export" href="javascript:void(0);">导出EXCEL</a>
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

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/app/feedback/manage.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>