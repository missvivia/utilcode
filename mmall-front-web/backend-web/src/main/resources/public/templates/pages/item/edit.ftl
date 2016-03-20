<#assign pageName="item-productedit">
<#escape x as x?html>
<!--商品状态 : 1未审核，2审核中，3审核未通过，4已上架，5已下架-->
<#assign status={"1":"未审核","2":"审核中","3":"审核未通过","4":"已上架","5":"已下架"}>
<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" type="text/css" href="/src/css/page/product_manage.css">
	<style type="text/css">
	#dataform .form-control{display:inline-block;}
    #limitList .sep-limit, #limitArea .sep-limit{
        left:14px;
     }
	</style>
</head>


<body>

<!-- 左边目录列表 -->
<@side />
<!-- 右边内容区域 -->
<@body>

<@crumbs parent={"txt":"商品管理","url":'/item/product/list?type=1'} sub={"txt":"编辑商品"}/>


<!-- form 搜索面板 -->
<div class="row add-product">
	<div class="col-sm-12">

		<div class="form-horizontal">
			<div class="m-card">
				<div class="card_c">
					<div class="form-group">					
						<label class="col-sm-4 control-label">商品条形码：${productSKU.prodBarCode!}</label>
						<label class="col-sm-4 control-label">状态：${status["${productSKU.prodStatus!1}"]}</label>
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
						<div class="col-sm-11 j-image-product">
							<label><strong>商品大图（商品详情页展示）</strong>最多5张/尺寸1242x1242 px/单张2M以内</label>
						</div>
					</div>
			      	<div class="hr-dashed"></div>
				    
				    <!-- 单品ID（itemSPUId）需要上传 -->
					<input type="hidden" name="itemSPUId" value="${productSKU.itemSPUId!''}" id="itemSPUId"/>
					
					<!-- 商品ID（skuId）需要上传 -->
					<input type="hidden" name="skuId" value="${productSKU.skuId!}" id="skuId"/>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">商品名称：</label>
						<label class="col-sm-4">
							<input type="text" class="form-control" name="productName" maxlength="200" value="${productSKU.productName!''}"/>
						</label>
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
						<div class="col-sm-2"><input type="text" class="form-control" name="productTitle" value="${productSKU.productTitle}" maxLength=50 data-pattern="^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9]|\-|\_)*$" data-message="请不要输入特殊字符"/></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">销售单位：</label>
						<div class="col-sm-2"><input type="text" class="form-control" maxLength=6 name="prodUnit" value="${productSKU.prodUnit!''}" data-required="true" data-message="请填写销售单位"/></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">保质期：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="expireDate" value="${productSKU.expireDate!''}" data-pattern="^\s*\d{0,4}(\.\d{1,2})?\s*$"></div>
						<div class="col-sm-1"><div class="sep">个月</div></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">生产日期：</label>
						<div class="col-sm-2">
		                 	<div class="j-datepick" data-name="prodProduceDate" data-value="${productSKU.prodProduceDate!}"></div>
		                </div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">退货政策：</label>
						<div class="col-sm-2">
							<label class="radio-inline">
							<input type="radio" name="canReturn"  <#if productSKU.canReturn==1>checked="checked"</#if> value=1> 允许
							</label>

							<label class="radio-inline">
							<input type="radio" name="canReturn"  <#if productSKU.canReturn==2>checked="checked"</#if> value=2> 拒绝
							</label>
						</div>
					</div>
				</div>
			</div>	
			
			<div class="m-card">
				<h2 class="card_b">销售属性</h2>
				<div class="card_c">
					<div class="form-group">
						<label class="col-sm-2 control-label">起批数量：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="batchNum" value="${productSKU.batchNum!0}" data-pattern="^\s*\d{1,9}\s*$" data-required="true" data-message="最大9位数字，不能为空"></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">建议零售价：</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" name="salePrice" value="${productSKU.salePrice!''}" data-pattern="^\s*\d{0,9}(\.\d{1,2})?\s*$">
						</div>
						<div class="col-sm-1"><div class="sep">元</div></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">批发价：</label>
						<#if productSKU.priceList??>
						<#list productSKU.priceList as price>
							<div class="col-sm-2"><input type="text" class="form-control" name="price" value="${price.prodPrice!0}" data-pattern="^\s*\d{1,9}(\.\d{1,2})?\s*$" data-required="true" data-message="最大9位数字，小数点后最多两位，不能为空"></div>
						</#list>
						</#if>
						<div class="col-sm-1"><div class="sep">元</div></div>
					</div>
					<#if productSKU.prodStatus == 4>
					    <#if productSKU.isSKULimited == 1>
						    <div class="form-group" id="limitList">
								<label class="col-sm-2 control-label">限购：</label>
								<div class="col-sm-2">
								     <div class="sep sep-limit">限购</div>
								</div>
							</div>
							<div id="limitArea">
								<div class="form-group">
									<label class="col-sm-2 control-label">限购说明：</label>
									<div class="col-sm-4">
									    <div class="sep sep-limit" id="limitComment">${productSKU.skuLimitConfigVO.limitComment!''}</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">限购时间：</label>
									<div class="col-sm-4">
										<div class="sep sep-limit">
										${(productSKU.skuLimitConfigVO.limitStartTime?number_to_date)!''}
									     至
										${(productSKU.skuLimitConfigVO.limitEndTime?number_to_date)!''}
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">限购规则：</label>
									<div class="col-sm-4" >
									    <div class="sep sep-limit">
										每人${productSKU.skuLimitConfigVO.limitPeriod!''}天限购
									     ${productSKU.skuLimitConfigVO.skuLimitNum!''}件
									     </div>
									</div>
								</div>
		                    </div>
	                    <#else>
	                        <div class="form-group" id="limitList">
								<label class="col-sm-2 control-label">限购：</label>
								<div class="col-sm-2">
								    <div class="sep sep-limit">
									      不限购
									</div>
								</div>
							</div>
	                    </#if>
	                <#elseif productSKU.prodStatus == 5>
	                    <div class="form-group" id="limitList">
							<label class="col-sm-2 control-label">限购：</label>
							<div class="col-sm-2">
								<label class="radio-inline">
								     <input type="radio" name="prodLimit" class="prodLimit" checked="checked">不限购
								</label>
	
								<label class="radio-inline">
								     <input type="radio" name="prodLimit" class="prodLimit"> 限购
								</label>
							</div>
						</div>
						<div id="limitArea" class="hidden">
							<div class="form-group">
								<label class="col-sm-2 control-label">限购说明：</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" name="limitComment" <#if productSKU.skuLimitConfigVO?exists>value="${productSKU.skuLimitConfigVO.limitComment!''}"<#else>value=""</#if>  maxlength="20" placeholder="文案将显示在商品详情页中，限20个字符">
								</div>
								<div class="col-sm-2"><div class="sep">（必填）&nbsp;&nbsp;&nbsp;&nbsp;例：限购3件</div></div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">限购时间：</label>
								<div class="col-sm-2">
									<div class="j-datepick" id="stime" data-name="limitStartTime" <#if productSKU.skuLimitConfigVO?exists>data-value="${productSKU.skuLimitConfigVO.limitStartTime!}"<#else>data-value=""</#if> ></div>
								</div>
								<div style="float:left;line-height:34px;">至</div>
								<div class="col-sm-2">
									<div class="j-datepick" id="etime" data-name="limitEndTime" <#if productSKU.skuLimitConfigVO?exists>data-value="${productSKU.skuLimitConfigVO.limitEndTime!}"<#else>data-value=""</#if> ></div>
								</div>
	                            <div class="col-sm-2"><div class="sep">仅限购期内可以购买</div></div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">限购规则：</label>
								<div class="col-sm-1" style="width:45px;min-width:45px;">
									<div class="sep sep-limit">每人</div>
								</div>
								<div class="col-sm-1">
									<input type="text" class="form-control" name="limitPeriod" <#if productSKU.skuLimitConfigVO?exists>value="${productSKU.skuLimitConfigVO.limitPeriod!''}"<#else>value=""</#if> >
								</div>
								<div class="col-sm-1" style="min-width:35px;width:35px;">
									<div class="sep">天限购</div>
								</div>
								<div class="col-sm-1">
									<input type="text" class="form-control" name="skuLimitNum" <#if productSKU.skuLimitConfigVO?exists>value="${productSKU.skuLimitConfigVO.skuLimitNum!''}"<#else>value=""</#if> >
								</div>
								<div class="col-sm-1">
									<div class="sep">件</div>
								</div>
							</div>
	                    </div>
					</#if>
				</div>
			</div>	

			<div class="m-card">
				<h2 class="card_b">库存</h2>
				<div class="card_c">
					<div class="form-group">
						<#list productSKU.speciList as speci>
						<div class="col-sm-12">
							<label class="form-control-static f-fl">库存：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="productCount" value="${speci.productNum!0}" data-pattern="^\s*\d{1,9}\s*$" data-required="true"/ data-message="最大9位数字，不能为空"></div>
							<label class="form-control-static f-fl">件&nbsp;&nbsp;&nbsp;&nbsp;</label>
					
							<label class="form-control-static f-fl">小于：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="productMinCount" value="${speci.attentionNum!0}" data-pattern="^\s*\d{1,9}\s*$" data-required="true"/ data-message="最大9位数字，不能为空"></div>
							<label class="form-control-static f-fl">件提醒&nbsp;&nbsp;&nbsp;&nbsp;</label>
					
							<label class="form-control-static f-fl">商品内码：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="productInnerCode" value="${speci.prodInnerCode!''}" maxLength=18/></div>
						</div>
						</#list>
					</div>
				</div>
			</div>	

			<!--<div class="m-card">
				<h2 class="card_b">模型</h2>
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-10" id="productModelList">
							<#if productSKU.paramList??>
							<#list productSKU.paramList as item>
							<div class="form-group" data-parameterid="${item.parameterId!''}">
								<label class="col-sm-1 control-label">${item.paramName!''}</label>
								<div class="col-sm-10">
									<#if item.optionList??>
									<#if item.single==1>
									<#list item.optionList as option>
									<label class="radio-inline">
									  <input type="radio" class="j-flag" name="${item.parameterId!''}" value=${option.paramOptionId!''} <#if option.isCheck==1>checked="checked"</#if>> ${option.paramOption}
									</label>
									</#list>
									<#else>
									<#list item.optionList as option>
									<label class="checkbox-inline">
									  <input type="checkbox" class="j-flag" name="${item.parameterId!''}" value=${option.paramOptionId!''} <#if option.isCheck==1>checked="checked"</#if>> ${option.paramOption}
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
					<span class="u-required">*</span> 图片限宽790px，高不限，图片大小不超过2M 
				    <div class="m-editor"></div>
				    <textarea class='j-editor' style="display:none" data-scr="${productSKU.prodDetail.customEditHTML!}" id="productDetail">${productSKU.prodDetail.customEditHTML!}</textarea>
				   
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
							<button class="btn btn-primary" id="onSave">提交</button>
							<button class="btn btn-primary" id="unShelve" style="background:#555;" data-skuId="${productSKU.skuId!}">下架</button>
							<#elseif productSKU.prodStatus ==3 || productSKU.prodStatus==5>
							<button class="btn btn-primary" id="onDelete" data-skuId="${productSKU.skuId!}">删除</button>
							<button class="btn btn-primary" id="onSave">提交</button>
							<button class="btn btn-primary" id="onShelve" data-skuId="${productSKU.skuId!}">上架</button>
							<#elseif productSKU.prodStatus ==2>
							<#elseif productSKU.prodStatus ==1>
							<button class="btn btn-primary" id="onSave">提交</button>
							<!--<button class="btn btn-primary" id="onAudit">提交审核</button>-->
							<#else>
							<button class="btn btn-primary" id="onSave">提交</button>
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

<!-- 商品图片列表 -->
<script>
<#noescape>
    window.listType = ${RequestParameters.type!0}
	window.picList = ${stringify(productSKU.picList)}
	window.isSKULimited = ${productSKU.isSKULimited}
	window.prodStatus = ${productSKU.prodStatus}
	<#if productSKU.isSKULimited == 1>
		 window.limitStartTime = ${productSKU.skuLimitConfigVO.limitStartTime!''}
		 window.limitEndTime = ${productSKU.skuLimitConfigVO.limitEndTime!''}
		 window.limitPeriod = ${productSKU.skuLimitConfigVO.limitPeriod!''}
		 window.skuLimitNum = ${productSKU.skuLimitConfigVO.skuLimitNum!''}
	</#if>
</#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/edit.js?v=1.0.0.5"></script>
</body>
</html>
</#escape>