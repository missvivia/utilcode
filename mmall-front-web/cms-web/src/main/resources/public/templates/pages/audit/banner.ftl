<#assign pageName="audit-banner"/>
<!doctype html>
<html lang="en">
  <head>
<#include "/wrap/common.ftl"/>
<#include "./audit.ftl"/>
    <meta charset="UTF-8">
    <title>${siteTitle} - ${page.title}</title>
    <#include "/wrap/css.ftl">
    <!-- @STYLE -->
    <link rel="stylesheet" href="/src/css/page/audit.css?v=1.0.0.0">
  </head>
  <body>
    <@side/>
    <@wrap>
      <@crumbs parent=crumbsParent sub={"txt":"PO BANNER审核"}/>
      <@module class="m-banner" title="PO BANNER审核">
        <m-tab on-change={{this.onchange($event)}}></m-tab>
        <div class="m-search f-cb">
          <form class="form-horizontal form-search-l" role="form" id="search-form-0">
            <input type="hidden" name="url" value="/schedule/banner/audit/search"/>
            <div class="form-group">
              <label class="col-sm-1 control-label">站点</label>
              <div class="col-sm-2"><@siteSelect key="curSupplierAreaId"/></div>
              <label class="col-sm-1 control-label">档期时间</label>
              <@ftTimeSelect from={"key":"startDate","value":"2014-09-15"} to={"key":"endDate","value":"2015-10-15"}/>
              <@searchButton margin=false/>
            </div>
          </form>
          <form class="form-horizontal form-search-r" role="form" id="search-form-1">
            <input type="hidden" name="url" value="/schedule/banner/audit/search_by_key"/>
            <div class="form-group">
              <button type="button" class="btn btn-primary btn-sm f c" name="btn-submit">搜索</button>
              <input class="form-control f b" type="text" name="value"/>
              <select class="form-control f a" name="key">
                <option value="1">PO编号</option>
                <option value="2">商家</option>
                <option value="3">品牌</option>
              </select>
            </div>
          </form>
        </div>
      </@module>
    </@wrap>

    <!-- @SCRIPT -->
    <script src="${jslib}define.js?pro=${jspro}"></script>
    <script src="${jspro}page/audit/banner.js?v=1.0.0.0"></script>
  </body>
</html>