<#assign pageName="exports"/>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"明细导出","url":'#'} sub={"txt":"明细导出"}/>
<!-- card -->
<div>
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	拣货单查询
      </h2>
      <div class="card_c">
      	<form class="form-horizontal" id="search-form" action="/storage/exports/excel" method="get">
      	
      	  <div class="col-md-2">
          <select name="type" class="form-control j-node">
            <#assign queryList=[{"name":"入库单","id":"1"},{"name":"出库单","id":"2"},{"name":"退货单","id":"3"},{"name":"拒退单","id":"4"}] />
            <#list queryList as x>
              <option value="${x.id}">${x.name}</option>
            </#list>
          </select>
          </div>
      	
          <div class="form-group">
            <label class="col-sm-1 control-label">日期：</label>
            <div class="col-sm-3 j-datepick" data-name="startTime" data-value=""></div>
            <div class="col-sm-3 j-datepick" data-name="endTime" data-value="" data-time="23:59:59">
              <span class="sep">至</span>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-1 col-md-offset-1">
              <input type="submit" class="form-control btn btn-primary"  value="导出"/>
            </div>
          </div>
        </form>
      </div>
      
       <div class="card_c">
       	<p>
       		注意事项：<br>
       		1.导出某天入库单，起始日期和结束日期选择同一天即可。比如两个时间选择为2015-01-28，即导出2015-01-28日0点至2015-01-29日0点之间的入库单<br>
       		2.导出某天出库单，起始日期和结束日期选择同一天即可。比如两个时间选择为2015-01-28，即导出2015-01-28日16点至2015-01-29日16点之间的出库单<br>
       		3.导出已经通知到仓库的销退入库单，不用选日期。即选了日期也没用，默认导出所有已经推送给仓库，仓库还没有反馈的销退入库单<br>
       		4.拒退单导出的时间是根据“设为拒件”的时间的进行查询的<br>
       	</p>
       </div>
  </div>
</div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/storage/exports.js?v=1.0.0.0"></script>

</body>
</html>
</#escape>