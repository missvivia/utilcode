<#assign pageName="storage-warehouse"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>

<@crumbs parent={"txt":"物流仓库管理","url":'#'} sub={"txt":"仓库统计"}/>

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
                       仓库统计
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal" id="search-form" action="/storage/warehouse/export">
          <div class="form-group">
            <label class="col-sm-1 control-label">报表类型：</label>
            <div class="col-sm-3">
              <select name="type" class="form-control" data-type="number">
                <#list typeListOfWare as key>
                  <option value="${key?substring(2,3)}">${key}</option>
                </#list>
              </select>
            </div>
          </div>
          <div id="warehouse-sel" class="form-group <#if typeListOfWare?has_content && typeListOfWare[0] =="报表1-全国订单生产概况">f-dn</#if>">
            <label class="col-sm-1 control-label">仓库：</label>
            <div class="col-sm-3">
              <select name="warehouse" class="form-control" data-type="number">
                <option value="0">全部</option>
                <#list warehouseList as item>
                  <option value="${item.warehouseId}">${item.warehouseName}</option>
                </#list>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">报表日期：</label>
            <div class="col-sm-3 j-datepick" data-name="startTime" data-value=""></div>
            <div class="col-sm-3 j-datepick" data-name="endTime" data-value="" data-time="23:59:59">
              <span class="sep">至</span>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-1 col-md-offset-1">
              <input type="button" class="form-control btn btn-primary" name="btn-submit" value="查询"/>
            </div>
            <div id="export-ct" class="col-sm-1 <#if typeListOfWare?has_content && (typeListOfWare[0] !="0")>f-dn</#if>">
              <input type="submit" class="form-control btn btn-primary" value="导出"/>
            </div>
          </div>
        </form>
        <div id="m-warehouselist"></div>
      </div>
    </div>
  </div>
</div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/storage/warehouse.js?v=1.0.0.0"></script>

</body>
</html>