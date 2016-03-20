<#assign pageName="shiporderList"/>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/user.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商家拣货单查询","url":'#'} sub={"txt":"商家拣货单查询"}/>
<!-- card -->
<div>
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	拣货单查询
      </h2>
      <div class="card_c">
      	<form class="form-horizontal" id="search-form" action="/user/shiporder/list" method="get">
          <div class="form-group">
            <label class="col-sm-1 control-label">日期：</label>
            <div class="col-sm-3 j-datepick" data-name="startTime" data-value=""></div>
            <div class="col-sm-3 j-datepick" data-name="endTime" data-value="" data-time="23:59:59">
              <span class="sep">至</span>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-1 col-md-offset-1">
              <input type="submit" class="form-control btn btn-primary"  value="查询"/>
            </div>
          </div>
        </form>
      </div>
      
      
       <div class="card_c">
       
			<table class="table">
				<tr>
				<th>品牌</th>
				<th>帐号</th>
				<th>拣货单id</th>
				</tr>
			<#list ships as ship>
				<tr>
					<th>${ship.brandName}</th>
					<th>${ship.account}</th>
				    <th><a href = "/user/shiporder/export/${ship.supplierId}/${ship.shipOrderId}" target="_blank">${ship.shipOrderId}</a></th>
			 	 </tr>
			</#list>
			</table>

       </div>
      
  </div>
</div>
</@wrap>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/userinfo/shiporderlist.js?v=1.0.0.0"></script>

</body>
</html>
</#escape>