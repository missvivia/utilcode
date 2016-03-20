<#assign pageName="item-productdetail">
<#--商品状态 : 1未审核，2审核中，3审核未通过，4已上架，5已下架-->
<#assign status={"1":"未审核","2":"审核中","3":"审核未通过","4":"已上架","5":"已下架"}>

<#escape x as x?html>

<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" type="text/css" href="/src/css/page/product_manage.css">
</head>


<body>

<!-- 左边目录列表 -->
<@side />
<!-- 右边内容区域 -->
<@body>
<@crumbs parent={"txt":"商品管理","url":'/item/product/list?type=1'} sub={"txt":"查看商品"}/>

<!-- form 搜索面板 -->
<div class="row add-product">
	<div class="col-sm-12">

		<div class="form-horizontal">
			<div class="m-card">
				<div class="card_c">
					<div class="form-group">					
						<label class="col-sm-4 control-label">商品条形码：${productSKU.prodBarCode!''}</label>
						<label class="col-sm-4 control-label">状态：${status["${productSKU.prodStatus!}"]}</label>
					</div>
				</div>
			</div>
		</div>

		<form method="get" class="form-horizontal" id="dataform">
			<div class="m-card">
				<h2 class="card_b">基本信息</h2>
				<div class="card_c">
					<div class="form-group">
						<label class="col-sm-1"></label>
						<div class="col-sm-11">
							<label>商品图片</label>
						</div>
					</div>
					<div class="form-group">
					<#list productSKU.picList as pic>
						<div class="col-sm-2">
		                  	<div class="m-imgbox">
		                      	<a href="${pic.picPath}" data-lightbox="registrationImg">
		                        	<img src="${pic.picPath}" style="height:150px;width:150px;">
		                      	</a>
		                    </div>
		                </div>
		            </#list>
					</div>
			      	<div class="hr-dashed"></div>
				    
					<div class="form-group">
						<label class="col-sm-2 control-label">商品名称：</label>
						<label class="col-sm-4 form-control-static" id="spuName">${productSKU.productName!''}</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">所属分类：</label>
						<label class="col-sm-4 form-control-static" id="categoryNormalName">${productSKU.categoryFullName!''}</label>
						
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">品牌：</label>
						<label class="col-sm-4 form-control-static" id="brandName">${productSKU.brandName!''}</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">副标题：</label>
						<label class="col-sm-4 form-control-static" id="productTitle">${productSKU.productTitle!''}</label>
					</div>					
					<div class="form-group">
						<label class="col-sm-2 control-label">销售单位：</label>
						<label class="col-sm-4 form-control-static">${productSKU.prodUnit!''}</label>
						
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">保质期：</label>
						<label class="col-sm-2 form-control-static">${productSKU.expireDate!0}个月</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">生产日期：</label>
						<label class="col-sm-2 form-control-static">${(productSKU.prodProduceDate?number_to_time?string("yyyy-MM-dd"))!''}</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">退货政策：</label>
						<label class="col-sm-2 form-control-static">${(productSKU.canReturn==1)?string("允许","拒绝")}</label>
					</div>
				</div>
			</div>	
			
			<div class="m-card">
				<h2 class="card_b">销售价格</h2>
				<div class="card_c">
					<div class="form-group">
						<label class="col-sm-2 control-label">起批数量：</label>
						<label class="col-sm-2 form-control-static">${productSKU.batchNum!1} 件起批</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">建议零售价：</label>
						<label class="col-sm-2 form-control-static">${productSKU.salePrice!0} 元</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">批发价：</label>
						<div class="col-sm-6">
							<#if productSKU.priceList??>
							<#list productSKU.priceList as price>
							<label class="form-control-static">${price.prodMinNumber!0} 件起 ${price.prodPrice!0} 元</label><br/>
							</#list>
							</#if>
						</div>
					</div>
                    <div class="form-group">
						<label class="col-sm-2 control-label">限购：</label>
						<label class="col-sm-2 form-control-static"><#if productSKU.isSKULimited == 1>限购<#else>不限购</#if></label>
					</div>
					<#if productSKU.isSKULimited == 1>
					<div class="form-group">
						<label class="col-sm-2 control-label">限购说明：</label>
						<label class="col-sm-2 form-control-static"><#if productSKU.skuLimitConfigVO?exists>${productSKU.skuLimitConfigVO.limitComment!''}</#if></label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">限购时间：</label>
						<label class="col-sm-4 form-control-static"><#if productSKU.skuLimitConfigVO?exists>${(productSKU.skuLimitConfigVO.limitStartTime?number_to_date)!''}&nbsp;至&nbsp;${(productSKU.skuLimitConfigVO.limitEndTime?number_to_date)!''}</#if></label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">限购规则：</label>
						<label class="col-sm-2 form-control-static"><#if productSKU.skuLimitConfigVO?exists>每人${productSKU.skuLimitConfigVO.limitPeriod!''}天限购${productSKU.skuLimitConfigVO.skuLimitNum!''}件</#if></label>
					</div>
					</#if>
				</div>
			</div>	

			<div class="m-card">
				<h2 class="card_b">库存</h2>
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-12">
							<#list productSKU.speciList as speci>
							<div class="form-group">
							<label class="form-control-static">${speci.speciOptionName!''}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库存: ${speci.productNum!0} 件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;小于： ${speci.attentionNum!0} 件提醒&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;商品内码：${speci.prodInnerCode}</label><br/>
							</div>
							</#list>
						</div>
					</div>
				</div>
			</div>	

			<!--<div class="m-card">
				<h2 class="card_b">模型</h2>
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-10">
							<#if productSKU.paramList??>
							<#list productSKU.paramList as item>
							<div class="form-group">
								<label class="col-sm-1 control-label">${item.paramName!''}</label>
								<div class="col-sm-8">
									<#if item.optionList??>
									<#if item.single==1>
									<#list item.optionList as option>
									<label class="radio-inline">
									  <input type="radio" name="${item.parameterId!''}" value=${option.paramOptionId!''} disabled="disabled" <#if option.isCheck==1>checked="checked"</#if>> ${option.paramOption}
									</label>
									</#list>
									<#else>
									<#list item.optionList as option>
									<label class="checkbox-inline">
									  <input type="checkbox" name="${item.parameterId!''}" value=${option.paramOptionId!''} disabled="disabled"<#if option.isCheck==1>checked="checked"</#if>> ${option.paramOption}
									</label>
									</#list>
									</#if>
									</#if>
								</div>
							</div>
							</#list>
							</#if>
						</div>
					</div>
				</div>
			</div>-->	

			<div class="m-card">
				<h2 class="card_b">商品详情</h2>
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-12" id="productDetail"></div>
					</div>
				</div>
			</div>
		</form>
		
		<div class="form-horizontal" id="buttonTool">
			<div class="m-card">
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-4"><a class="btn btn-link f-fl" id="onBack">返回</a></div>
						<div class="col-sm-4">
							<#if productSKU.prodStatus ==4>
							<a class="btn btn-primary" href="/item/product/edit?skuId=${productSKU.skuId!''}&type=${RequestParameters.type!0}">编辑</a>
							<button class="btn btn-primary" id="unShelve" data-skuId="${productSKU.skuId!''}">下架</button>
							<#elseif productSKU.prodStatus ==3 || productSKU.prodStatus==5>
							<button class="btn btn-primary" id="onDelete" data-skuId="${productSKU.skuId!}">删除</button>
							<a class="btn btn-primary" href="/item/product/edit?skuId=${productSKU.skuId!''}&type=${RequestParameters.type!0}">编辑</a>
							<button class="btn btn-primary" id="onShelve" data-skuId="${productSKU.skuId!''}">上架</button>
							<#elseif productSKU.prodStatus ==2>
							<#elseif productSKU.prodStatus ==1>
							<a class="btn btn-primary" href="/item/product/edit?skuId=${productSKU.skuId!''}&type=${RequestParameters.type!0}">编辑</a>
							<button class="btn btn-primary" id="">提交审核</button>
							<#else>
							</#if>
						</div>
						<label class="col-sm-4"></label>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

</@body>

<!-- 商品详情 -->
<script>
<#noescape>
	window.prodDetail = ${stringify(productSKU.prodDetail.customEditHTML)}
	window.listType = ${RequestParameters.type!0}
</#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/scan.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>