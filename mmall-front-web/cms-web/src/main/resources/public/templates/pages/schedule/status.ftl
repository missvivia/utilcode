<#assign pageName="schedule-status"/>

<#-- <#include "/fake/schedule/polist.ftl" /> -->
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
<@crumbs parent={"txt":"档期管理","url":'#'} sub={"txt":"PO状态检查"}/>
<!-- card -->
<div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        PO状态检查
      </h2>
      <div class="card_c">
        <form id="search-form">
        <div class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-1 control-label">当前站点：</label>
            <div class="col-sm-2">
              <select name="curSupplierAreaId" class="form-control">
                <option value="0">全部</option>
                <#list provinceList as item>
                	<option value="${item.id}">${item.name}</option>
                </#list>
              </select>
            </div>
            <label class="col-sm-2 control-label">档期时间：</label>
            <div class="">
              <@dateSelect key="startDate" value="${stime?number_to_date?string('yyyy-MM-dd')}"/>
            </div>
            <div class="">
              <@dateSelect key="endDate" value="${etime?number_to_date?string('yyyy-MM-dd')}" time="23:59:59"/>
            </div>
            <div class="col-sm-1">
              <input class="btn btn-primary btn-block" type="button" name="btn-submit" value="查询">
            </div>
          </div>
        </div>
        <form>
        <div id="module-cnt">
        </div>
      </div>
    </div>
  </div>
</@wrap>

<!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/schedule/status.js?v=1.0.0.0"></script>
</body>
</html>