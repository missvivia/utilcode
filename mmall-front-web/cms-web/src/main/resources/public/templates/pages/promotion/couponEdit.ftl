<#assign pageName="promotion-couponEdit"/>

<#if couponVO?? == false>
  <#assign couponVO = {} />
</#if>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/base.css?v=1.0.0.2">
  <style type="text/css">
  .m-card .card_c {
    padding: 0 15px;
  }
  .m-couponcond {
    padding: 7px 0;
    width:500px;
}
.form-inline .input-group > .form-control {
    width: 100px;
}
.m-couponcond .form-group,.m-couponcond .row{
	margin:0;
}
.m-couponcond .cond{
	margin-bottom:10px;
}
.m-couponcond .input-group-addon{
	border:none;
	background:none;
}
.table > tbody > tr > td.rightTd{
	width:150px;
	text-align:right;
}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<#if editable == 1>
    <#if couponVO.name??>
         <@crumbs parent={"txt":"优惠券管理","url":'#'} sub={"txt":"优惠券编辑"}/>
    <#else>
         <@crumbs parent={"txt":"优惠券管理","url":'#'} sub={"txt":"优惠券新建"}/>
    </#if>
<#elseif editable == 0>
    <@crumbs parent={"txt":"优惠券管理","url":'#'} sub={"txt":"优惠券查看"}/>
</#if>
<!-- card -->

  <div class="row">
    <div class="col-sm-12">
      <div class="m-card">
        <h2 class="card_b">
          <span class="glyphicon glyphicon-chevron-down pull-right"></span>
          基本信息
        </h2>
        <#if editable == 1>
        <div class='card_c'>
          <form method="get" class="form-horizontal m-form-product j-baseform">
            <div class="form-group">
              <label class="col-sm-2 control-label">
                <span class="u-required">*</span>
                优惠券名称
              </label>
              <div class="col-sm-6">
                <input type="text" class="form-control" name="name" value="${couponVO.name!}" 
                data-required=true maxlength=40 data-pattern="^([a-zA-Z0-9\u4e00-\u9fa5]+)$"
                data-msg="优惠券名称格式错误"
                >
                <span class="help-block">40中文字符以内，显示在购物车等处（不允许输入特殊字符）</span>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">
                <span class="u-required">*</span>
                优惠券描述
              </label>
              <div class="col-sm-6">
                <input type="text" class="form-control" name="description" value="${couponVO.description!}"
                data-required=true maxlength=40 data-pattern="^([a-zA-Z0-9\u4e00-\u9fa5]+)$"
                data-msg="优惠券描述格式错误"
                >
                <span class="help-block">40中文字符以内，用于说明优惠券使用详情（不允许输入特殊字符）</span>
              </div>
            </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label"><span class="u-required">*</span>所在地区</label>
			    <div class="col-sm-2">
			    	<select class="form-control" name="areaIds">
					<#list areaList as area>
					<#if couponVO.areaIds?exists>
						<#if area.areaId + "" == couponVO.areaIds>
						<option value="${area.areaId}" selected>${area.areaName!}</option>
						<#else>
						<option value="${area.areaId}">${area.areaName!}</option>
						</#if>
					<#else>
						<option value="${area.areaId}">${area.areaName!}</option>
					</#if>
					</#list>
			    	</select>	
			    </div>
			    <#--
			    <div class="fipt fipt-addr col-sm-6">
			      <div class="w-select w-select-addr">
			          <input type="text" class="j-flag form-control" placeholder="请选择城市">
			          <span class="arrow">&nbsp;</span>
			          <label class="s-fc3">请选择城市</label>
			      </div>
			    </div>
			    -->
			  </div>            
            <div class="form-group" id="invalidTime">
              <label class="col-sm-2 control-label"><span class="u-required">*</span>有效时间</label>
              <div class="col-sm-10 form-inline">
                <div style="display:inline;" id="form-startTime"></div>
                至
                <div style="display:inline;" id="form-endTime"></div>
              </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><span class="u-required">*</span>优惠类型</label>
                <div class="col-sm-3">
                  <label class="radio-inline">
                    <input type="radio" class="favorTypeRadio" name="favorType" value="0" <#if (couponVO.favorType?? && couponVO.favorType ==0)|| !(couponVO.favorType??)>checked</#if>>满减</label>
                 <!-- <label class="radio-inline">
                    <input type="radio" class="favorTypeRadio" name="favorType" value="1" <#if couponVO.favorType?? && couponVO.favorType ==1>checked</#if>>满折</label>
                  -->
                </div>
              </div>
              <div class="form-group">
              	<label class="col-sm-2 control-label"><span class="u-required">*</span>条件设置</label>
                <div class="col-sm-3">
					<div class='j-items j-items-0<#if couponVO.favorType?? && couponVO.favorType ==1> f-dn</#if>'></div>
					<div class='j-items j-items-1<#if (couponVO.favorType?? && couponVO.favorType ==0) || !(couponVO.favorType??)> f-dn</#if>'></div>                
                </div>
              </div>
          </form>
        </div>
		<#elseif editable == 0>
		<div class='card_c'>
			<table class="table table-striped">
				<tr><td class="rightTd">名称：</td><td>${couponVO.name!}</td></tr>
				<tr><td class="rightTd">描述：</td><td>${couponVO.description!}</td></tr>
				<tr><td class="rightTd">区域：</td><td id="areaName">
					<#if couponVO.areaList??>
					<#list couponVO.areaList as area>
						${area.areaName}；
					</#list>
					</#if>
				</td></tr>	
				<tr><td class="rightTd">有效时间：</td><td><span id="st"></span> 至  <span id="et"></span></td></tr>
				<tr><td class="rightTd">优惠券代码：</td><td><#if couponVO.codeType??><#if couponVO.codeType == 'PUBLIC'>公码<#else>随机码</#if>   ${couponVO.couponCode}</#if></td></tr>
				<tr><td class="rightTd">使用次数：</td><td>${couponVO.times!}</td></tr>
				<tr>
					<td class="rightTd">是否绑定用户 ：</td>
					<td>
						<#if couponVO.binderType??>
							<#if couponVO.binderType == "SYSTEM_BINDER">系统自动绑定
							<#elseif couponVO.binderType == "USER_BINDER">用户列表  ${couponVO.users}
							<#elseif couponVO.binderType == "DISTRIBUTE_BINDER">模板绑定
							</#if>
						</#if>
					</td>
				</tr>
				<tr><td class="rightTd">类型：</td><td><#if couponVO.favorType == 0>满减<#else>满折</#if></td></tr>
				<tr>
					<td class="rightTd">条件设置：</td>
					<td id="condition"></td>
				</tr>
			</table>
		</div>
		</#if>        
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-8 col-sm-offset-2">
      <div class="m-card-btn m-card">
        <#if editable==1><div class="cbtn_left"><a href="#" class="btn  btn-primary j-submit">保存</a></div></#if>
        <div class="cbtn_right" <#if editable!=1>style="width:100%;"</#if>><a href="javascript:history.go(-1);" class="btn  btn-default">返回</a></div>
      </div>
    </div>
  </div>
</@wrap>
<script type="text/javascript">
  window.__data__ = ${JsonUtils.toJson(couponVO)}
  window.__editable = ${editable};
  window.__areaList__ = ${JsonUtils.toJson(areaList)};
</script>
</div>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/couponEdit.js?v=1.0.0.1"></script>

</body>
</html>