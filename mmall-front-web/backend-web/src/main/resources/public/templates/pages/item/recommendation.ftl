<#assign pageName="item-product/recommendation">

<#escape x as x?html>
<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" type="text/css" href="/src/css/page/product_manage.css">
	<style>
	     .form-horizontal .form-group{
	          margin-left:0;
	          margin-right:0;
	     }
	     .form-horizontal .control-label{
	          text-align:center;
	     }
	     .form-control-static{
	          text-align:left;
	     }
	     .m-card .card_c{
	          padding:20px 60px;
	     }
	     .content{
	          min-height: 300px;
	          text-align:center;
	     }
	     .no-pro{
	          margin-top:100px;
	          font-size:16px;
	     }
	     .m-t{
	          margin-top:20px;
	     }
	     .err-tip{
	          color:#ff0000;
	          padding-top:7px;
	          text-align:left;
	     }
	</style>
</head>

<body>
<!-- 左边目录列表 -->
<@side />

<!-- 右边内容区域 -->
<@wrap>
<@crumbs parent={"txt":"商品管理","url":'/item/product/recommendation'} sub={"txt":"商品首页推荐"}/>

<!-- form-->
<div class="row">
	<div class="col-sm-12">
		<div class="m-card content">
			<div class="card_c">
			     <div class="form-horizontal">
			     <#if skuRecommendations??> 
			         <#list 1..10 as t>
			                <#assign flag=false /> 
					        <#list skuRecommendations as product>
					            <#if product.showIndex == t>
					                <#assign flag=true skuId=product.productSKUId msg=product.productStatusMsg!''/>
                                    <#break>
								</#if>
							</#list>
							<#if flag == true>
							    <div class="form-group">
									<label class="col-sm-2 control-label">序列${t}商品货号：</label>
									<label class="col-sm-1 form-control-static">${skuId}</label>
									<div class="col-sm-2 err-tip">${msg}</div>
								</div>
							<#else>
							    <div class="form-group">
									<label class="col-sm-2 control-label">序列${t}商品货号：</label>
									<label class="col-sm-1 form-control-static"></label>
								</div>
							</#if>
					 </#list>
			     <#else>
			          <div class="no-pro">您尚未设置首页推荐商品</div>
				 </#if>
				      <div class="form-group m-t">
				      		<a class="btn btn-primary" href="/item/product/recommendationEdit">编辑</a>
				      </div>
				 </div>
			</div>	
		</div>	
	</div>
</div>
</@wrap>

<script>
<#noescape>
	
</#noescape>
</script>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/recommendation.js"></script>
</body>
</html>
</#escape>