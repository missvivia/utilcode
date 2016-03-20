<#assign pageName="schedule-pages"/>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商务管理","url":'/schedule/manage'} sub={"txt":"品购页列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        所有品购页
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal" id="search-form">
                  <div class="form-group">
                    <div class="col-sm-2">
                      <select name="type" id="" class="form-control j-save">
                        <option value="0">名称</option>
                        <option value="1">档期编号</option>
                      </select>
                    </div>
                    <div class="col-sm-3">
                      <input name="key" type="text" class="form-control j-save">
                    </div>
                    <div class="col-sm-3 col-sm-offset-2">
                    	<input type="button" value="查询" class="btn btn-primary btn-block" name="btn-submit"/>
                    </div>

                  </div>
 
                <div class="hr-dashed"></div>
                <div class="clst_action" id="m-actlist">
                </div>
        </form>
      </div>
    </div>
  </div>
</div>

</@wrap>

<script>
	window.statusList = ${stringify(statusList)}
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/pages.js"></script>

</body>
</html>