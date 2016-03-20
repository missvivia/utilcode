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
	     .m-card .card_c{
	          padding:20px 60px;
	     }
	     .p-l{
	          text-align:center;
	          margin-top:40px;
	     }
	     .btn-primary{
	          margin-right:10px;
	     }
	     .err-tip{
	          color:#ff0000;
	          padding-top:7px;
	     }
	     
	</style>
</head>

<body>
<!-- 左边目录列表 -->
<@side />

<!-- 右边内容区域 -->
<@wrap>
<@crumbs parent={"txt":"商品管理","url":'/item/product/recommendation'} sub={"txt":"商品首页推荐编辑"}/>

<!-- form-->
<div class="row">
	<div class="col-sm-12">
		<div class="m-card">
			<div class="card_c">
			     <div class="form-horizontal">
				     <#list 1..10 as t>
					     <#if skuRecommendations??>
					        <#assign flag=false /> 
					        <#list skuRecommendations as product>
					            <#if product.showIndex == t>
					                <#assign flag=true skuId=product.productSKUId id=product.id msg=product.productStatusMsg!''/>
                                    <#break>
								</#if>
							</#list>
							<#if flag == true>
							    <div class="form-group">
									<label class="col-sm-2 control-label">序列${t}商品货号：</label>
									<div class="col-sm-2">
										<input type="text" class="form-control proInput" value="${skuId}" data-id="${id}" data-SKUId="${skuId}" data-index="${t}">
								    </div>
								    <div class="err-tip">${msg}</div>
								</div>
							<#else>
							    <div class="form-group">
									<label class="col-sm-2 control-label">序列${t}商品货号：</label>
									<div class="col-sm-2">
										<input type="text" class="form-control proInput" data-index="${t}">
								    </div>
								</div>
							</#if>
						 <#else>
						    <div class="form-group">
								<label class="col-sm-2 control-label">序列${t}商品货号：</label>
								<div class="col-sm-2">
									<input type="text" class="form-control proInput" data-index="${t}">
							    </div>
							</div>
						 </#if>
					 </#list>
				      <div class="form-group p-l">
				      	   <button class="btn btn-primary" id="onSubmit">提交</button>
						   <button class="btn" id="onCancel">取消</button>
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
<script src="${jspro}page/item/recommendationEdit.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>