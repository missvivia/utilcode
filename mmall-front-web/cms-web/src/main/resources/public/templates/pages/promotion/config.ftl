<#assign pageName="promotion-config"/>

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
  	.wrapper{width:700px;margin:100px auto 10px auto;}
  	.configWrap{border:1px solid #ccc;background:#fff;}
  	.site{width:120px;}
  	.btn-disabled{background:#ccc;cursor:not-allowed;color:#fff;}
  	.error{color:red;}
  	.coupon{background: url(/res/images/coupon_bg01.png) no-repeat left top;padding: 0;margin-bottom:10px;}
  	.coupon_inner{border: 1px solid #ccc;border-left:none;background:url(/res/images/coupon_bg02.png) no-repeat right top;height:100px;padding-right:60px;}
  	.header{position:relative;padding:0 10px 0 14px;background:#f6f6f6;}
  	.header h4{margin:0;font-size:18px;color:#333;background:url(/res/images/coupon_icon02.png) no-repeat left center;padding-left:40px;height:50px;line-height:52px;}
  	.header .couponBtn{position:absolute;right:10px;top:10px;width:60px;height:34px;background:url(/res/images/coupon_btn.png) no-repeat left top;}
  	.header .active{background:url(/res/images/coupon_btn_active.png) no-repeat left top;}
  	.configBody{display:none;padding:36px;border-top:1px solid #ccc;}
  	.m-header{display:none;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"促销管理","url":'#'} sub={"txt":"新用户配置"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       		新用户配置
        </h2>
      <div class="card_c j-it" style="height:700px;">
			<div class="wrapper">
				<div class="form-group">
					<select id="siteControl" class="form-control site"><option>重庆站</option></select>
				</div>
				<div class="configWrap">
					<div class="header">
						<h4>新用户赠送优惠券</h4>
						<a href="javascript:;" class="couponBtn"><span></span></a>
					</div>
					<div class="configBody">
						<div class="form-group">请输入优惠券代码，多张优惠券以英文','隔开，最多10张优惠券</div>
						<div class="form-group clearfix">
							<div class="col-sm-10" style="padding-left:0;"><input type="text" class="form-control" id="code" name="code"/></div>
							<div class="col-sm-2"><button class="btn btn-disabled preview">预览</button></div>
						</div>
						<div class="checkbox form-group">
							<label>
								<input type="checkbox" id="isRelativeTime"/>使用相对时间 (相对时间具体说明)
							</label>
						</div>
						<div class="error hide">设置内容有变动，请保存</div>
						<button class="btn center-block btn-disabled" id="submit">保存</button>
					</div>
				</div>
			</div>
      </div>
    </div>
  </div>
</div>


</@wrap>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/config.js?v=1.0.0.3"></script>
</body>
</html>