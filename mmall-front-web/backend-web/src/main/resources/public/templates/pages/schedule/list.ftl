<#assign pageName="schedule-manage"/>
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
<@crumbs parent={"txt":"商务管理","url":'#'} sub={"txt":"档期商品管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        档期列表
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal" id="search-form">
                  <div class="form-group">
                    <label class="col-sm-1 control-label">PO编号：</label>
                    <div class="col-sm-3">
                      <input name="ponum" type="text" class="form-control j-save">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-1 control-label">审核状态：</label>
                    <div class="col-sm-3">
                      <select name="status" id="" class="form-control j-save" data-type="number">
                      	<option value="0">全部</option>
                      	<#list statusList as item>
                      		<option value="${item.id}">${item.name}</option>
                      	</#list>
                      </select>
                    </div>
                  </div>
                  
				  <div class="hr-dashed"></div>
                  <div class="form-group" id="form-datePicker">
                    <label class="col-sm-1 control-label">PO创建日期：</label>
                    <div class="col-sm-3 j-datepick" data-name="createBegin" data-value=""></div>

                    <label class="col-sm-1 control-label">档期开始日期：</label>
                    <div class="col-sm-3 j-datepick" data-name="startBegin" data-value=""></div>
              
                  </div>
                  <div class="form-group" id="form-datePicker">
                    <label class="col-sm-1 control-label"></label>
                    <div class="col-sm-3 j-datepick" data-name="createStop" data-value="" data-time="23:59:59"></div>

                    <label class="col-sm-1 control-label"></label>
                    <div class="col-sm-3 j-datepick" data-name="startStop" data-value="" data-time="23:59:59"></div>
                    
                  </div>
 
                <div class="hr-dashed"></div>
                <div class="form-group">
                  <label class="col-sm-1 control-label"></label>
                  <div class="col-sm-2 ">
                  	<input type="button" name="btn-submit" value="查询" class="btn btn-primary btn-block j-flag">
                  </div>
                </div>
				<div></div>
            </form>
      </div>

      <div class="card_c">
        <div class="clst_action"></div>
          <#-- <m-actlist></m-actlist> -->
          <div id="m-actlist"></div>
        </div>
      </div>
    </div>
  </div>
</div>

</@wrap>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/schedule.list.js"></script>

</body>
</html>