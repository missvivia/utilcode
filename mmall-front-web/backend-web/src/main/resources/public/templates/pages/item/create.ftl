<#assign pageName="item-productcreate">
<#escape x as x?html>

<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" type="text/css" href="/src/css/page/product_manage.css">
	<style type="text/css">
  .modal-title,.modal-body{text-align:left;}
  .modal-body{height:200px;overflow:auto;}
  .btn-default{display:none;}
  .modal  .modal-content{border: 1px solid rgba(255, 255, 255, 0.5);box-shadow:0;border-radius:0;}
  .modal {
	  z-index: 10;
	  overflow-y: auto;
	  overflow-x: hidden;
	  text-align: center;
	  position: fixed;
	  top: 0;
	  left: 0;
	  right: 0;
	  bottom: 0px;
	  vertical-align: middle;
	  white-space: nowrap;
	  z-index: 5000;
	  background-color: rgba(0,0,0,0.5);  
}
	.loading .zbar,.loading .zcls{display:none;}
	.loading .zcnt{text-align:center;}
	</style>
</head>


<body>

<!-- 左边目录列表 -->
<@side />
<!-- 右边内容区域 -->
<@body>

<@crumbs parent={"txt":"商品管理","url":'/item/product/list?type=1'} sub={"txt":"添加商品"}/>


<!-- form 搜索面板 -->
<div class="row add-product">
	<div class="col-sm-12">

		<div class="form-horizontal">
			<div class="m-card">
				<h2 class="card_b">新增商品</h2>
				<div class="card_c">
					<div class="form-group">					
						<label class="col-sm-2 control-label"><span class="u-required">*</span>请输入商品条形码数字：</label>
						
						<div class="col-sm-2">
							<input class="form-control" placeholder="请输入商品条形码数字" id="searchBarCodeField"/>
						</div>
						
						<button class="btn btn-primary" id="searchBarCodeBtn">确定</button>
						<label class="btn btn-primary j-img" data-name="importItem" id="importItemBtn" style="margin-left:20px;">商品导入</label>
						<input type="hidden" name="importItem" data-message="商品导入">
						<a href="/download/sku-template.xlsx" style="text-decoration:underline;">模板下载</a>
					</div>
				</div>
			</div>
		</div>

		<form method="get" class="form-horizontal hidden" id="dataform">
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
					<input type="hidden" name="itemSPUId" id="itemSPUId"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">商品名称：</label>
						<label class="col-sm-4">
							<input type="text" class="form-control" id="spuName" maxlength="200"/>
						</label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">所属分类：</label>
						<label class="col-sm-4 form-control-static" id="categoryNormalName"></label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">品牌：</label>
						<label class="col-sm-4 form-control-static" id="brandName"></label>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">副标题：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="productTitle" maxLength=50 data-pattern="^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9]|\-|\_)*$" data-message="请不要输入特殊字符"/></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">销售单位：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="prodUnit" maxLength=6 data-required="true" data-message="请填写销售单位"/></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">保质期：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="expireDate" data-pattern="^\s*\d{0,4}(\.\d{1,2})?\s*$"></div>
						<div class="col-sm-1"><div class="sep">个月</div></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">生产日期：</label>
						<div class="col-sm-2">
		                 	<div class="j-datepick" data-name="prodProduceDate" data-value=""></div>
		                </div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">退货政策：</label>
						<div class="col-sm-2">
							<label class="radio-inline">
							 	<input type="radio" name="canReturn" value=1 checked="checked"> 允许
							</label>
							<label class="radio-inline">
							 	<input type="radio" name="canReturn" value=2> 拒绝
							</label>
						</div>
					</div>
				</div>
			</div>	
			
			<div class="m-card">
				<h2 class="card_b">销售价格</h2>
				<div class="card_c">
					<div class="form-group">
						<label class="col-sm-2 control-label">起批数量：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="batchNum" data-pattern="^\s*\d{1,9}\s*$" data-required="true"  data-message="最大9位数字，不能为空"></div>
						<!--<div class="sep">月</div>-->
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">建议零售价：</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" name="salePrice" data-pattern="^\s*\d{0,9}(\.\d{1,2})?\s*$">
						</div>
						<div class="col-sm-1"><div class="sep">元</div></div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">批发价：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="price" data-pattern="^\s*\d{1,9}(\.\d{1,2})?\s*$" data-required="true"  data-message="最大9位数字，小数点后最多两位，不能为空"></div>
						<div class="col-sm-1"><div class="sep">元</div></div>
					</div>

				</div>
			</div>	

			<div class="m-card">
				<h2 class="card_b">库存</h2>
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-12">
							<label class="form-control-static f-fl">库存：</label>
							<div class="col-sm-2">
							<input type="text" class="form-control" name="productCount" data-pattern="^\s*\d{1,9}\s*$" data-required="true"  data-message="最大9位数字，不能为空"/>
							</div>
							<label class="form-control-static f-fl">件&nbsp;&nbsp;&nbsp;&nbsp;</label>
					
							<label class="form-control-static f-fl">小于：</label>
							<div class="col-sm-2">
							<input type="text" class="form-control" name="productMinCount" data-pattern="^\s*\d{1,9}\s*$" data-required="true"  data-message="最大9位数字，不能为空"/>
							</div>
							<label class="form-control-static f-fl">件提醒&nbsp;&nbsp;&nbsp;&nbsp;</label>
					
							<label class="form-control-static f-fl">商品内码：</label>
							<div class="col-sm-2">
							<input type="text" class="form-control" name="productInnerCode" maxLength=18/>
							</div>
						</div>
					</div>
				</div>
			</div>	

			<!--<div class="m-card">
				<h2 class="card_b">模型</h2>
				<div class="card_c">
					<div class="form-group">
						<div class="col-sm-10" id="productModelList"></div>
					</div>
				</div>
			</div>-->

			<div class="m-card">
				<h2 class="card_b">商品详情</h2>
				<div class="card_c">
					<span class="u-required">*</span> 图片限宽790px，高不限，图片大小不超过2M 
				    <div class="m-editor"></div>
				    <textarea class='j-editor' style="display:none"></textarea>
				</div>
			</div>
		</form>
		
		<div class="form-horizontal hidden" id="buttonTool">
			<div class="m-card">
				<div class="card_c">
					<div class="form-group">
						<label class="col-sm-4"></label>
						<div class="col-sm-4">
							<button class="btn btn-primary" id="submit">提交</button>
							<button class="btn btn-primary" id="applySale">申请上架</button>
						</div>
						<label class="col-sm-4"></label>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

</@body>
<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/create.js?v=1.0.0.5"></script>
</body>
</html>
</#escape>