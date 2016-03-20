<#assign pageName="sell-return"/>
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
<@crumbs parent={"txt":"销售管理","url":'/sell/return'} sub={"txt":"退货管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        退货单
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal" id="search-form">
                  <div class="form-group">
                  	<label class="col-sm-1 control-label">退货状态：</label>
                    <div class="col-sm-3">
                      <select name="state" id="" class="form-control">
                      	<option value="0">全部</option>
                        <#list statusList as item>
                        <option value="${item.code}">${item.desc}</option>
                        </#list>
                      </select>
                    </div>
	                
                    <label class="col-sm-1 control-label">退货单号：</label>
                    <div class="col-sm-3">
                      <input name="returnPoOrderId" type="text" class="form-control">
                    </div>
                    
                    <label class="col-sm-1 control-label">PO编号：</label>
                    <div class="col-sm-3">
                      <input name="poOrderId" type="text" class="form-control">
                    </div>
                  </div>
 
                  <div class="form-group">
                    <label class="col-sm-1 control-label">单据时间：</label>
                    <div class="col-sm-3 j-datepick" data-name="timestar" data-value=""></div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-1 control-label"></label>
                    <div class="col-sm-3 j-datepick" data-name="timeend" data-value="" data-time="23:59:59"></div>
                  </div>
                   <div class="form-group">
                    <div class="col-sm-3 col-sm-offset-1">
                    	<input type="button" class="btn btn-primary btn-block " name="btn-submit" value="查询"/>
                    </div>
                  </div>
            </form>
      </div>

      <div class="card_c">
        <div class="clst_action">
          <div id="module-box"></div>
        </div>
      </div>


    </div>
  </div>
</div>

</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/sell/return.js"></script>

</body>
</html>