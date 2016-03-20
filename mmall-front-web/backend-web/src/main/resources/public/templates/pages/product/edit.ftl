
<#if (productVO.schedule)??>
<#assign pageName="schedule-manage"/>
<#else>
<#assign pageName="product-edit"/>
</#if>

<#if productVO?? == false>
<#assign productVO = {} />
</#if>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
<meta charset="UTF-8">
<title>${siteTitle} - ${page.title}</title>
<#include "/wrap/css.ftl"></head>
<link rel="stylesheet" href="/src/css/page/product/product.css">
<body>

<@side />
<#-- top and footer need match -->
<@body>
  <@crumbs parent={"txt":"商品管理","url":'/product/list'} sub={"txt":"添加商品"}/>
  <!-- card -->

  <#if productVO.categories?? == false>
  <#assign categories =[] />
  <#else>
  <#assign categories = productVO.categories />
</#if>

<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        商品基本信息
      </h2>
      <div class="card_c">
        <form method="get" class="form-horizontal m-form-product j-baseform">
        	<div class="form-group">
				<div class="f-cb">
					<div class="f-pr f-fl " style="margin:0 10px 0 20px;">
	                     <div class="btn-group " id="cardbtn">
	                   	  <button type="button" class="btn btn-primary">批量导入</button>
	                   	  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" >
	                   		<span class="caret"></span>
	                   		<span class="sr-only">Toggel Dropdown</span>
	                   	  </button>
	                     </div>
	                     <ul class="m-actioncard f-dn" role="menu" id="actionCard">
	                   		<#---->
	                   		<label data-url="/rest/batchUploadProductInfo">商品资料</label>
	                   		<label data-url="/rest/batchUploadPic">商品图</label>
	                   		<label data-url="/rest/batchUploadSizeInfo">商品尺码</label>
	                   		<label data-url="/rest/batchUploadCustomHtml">详情HTML模块</label>
	                   		<#--
	                   		<label class="j-batch-name">批量修改商品名</label>
	                   		<div class="line"></div>
	                   		<a class="lbl j-export" href="/product/edit" target="_blank" id="export">批量导出商品资料</a>
	                   		-->
	                   	  </ul>
	                </div>
	              	<div class="f-pr f-fl ">
	                     <div class="btn-group" id="cardbtn1">
		                   	  <button type="button" class="btn btn-primary">模板下载</button>
		                   	  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" >
		                   		<span class="caret"></span>
		                   		<span class="sr-only">Toggel Dropdown</span>
		                   	  </button>
		                   	  <ul class="m-actioncard m-actioncard-1 f-dn" role="menu" id="actionCard1">
		                   		<a class="lbl" target="_self" href="/res/files/批量导入商品基本资料模板.zip">商品基本资料模板</a>
		                   		<a class="lbl" target="_self" href="/res/files/批量导入商品尺码模板.xlsx">商品尺码模板</a>
		                   		<a class="lbl" target="_self" href="/res/files/批量导入商品图片说明.docx">商品图片说明</a>
		                   		<a class="lbl" target="_self" href="/res/files/批量导入商品详情HTML说明.docx">商品详情HTML说明</a>
		                   	 </ul>
	                     </div>
	                  </div>
	                  
	              </div>
			</div>
          <div class="form-group">
            <label class="col-sm-1 control-label">品牌</label>
            <div class="col-sm-3">
              <span class="form-control">${productVO.brandName!}</span>
            </div>
          </div>
          <div class="form-group j-select">
            <label class="col-sm-1 control-label">
              商品类目
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <select name="category1" id="" class="form-control" data-value="${categories[0]!}" <#if productVO.schedule??> disabled</#if>></select>
            </div>
            <label class="col-sm-1 control-label">
              二级类目
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <select name="category2" id="" class="form-control"  data-value="${categories[1]!}" <#if productVO.schedule??> disabled</#if>></select>
            </div>
            <label class="col-sm-1 control-label">
              三级类目
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <select name="category3" 
             id="" class="form-control" data-value="${categories[2]!}" <#if productVO.schedule??> disabled</#if>></select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">
              商品货号
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <input type="text" class="form-control" name="goodsNo" 
                      data-required="true"
                      data-name="商品货号"
                      value="${productVO.goodsNo!}"
                      <#if productVO.schedule??> disabled</#if>
                      ></div>
            <label class="col-sm-1 control-label" >商品名称<span class="u-required">*</span></label>
            <div class="col-sm-3">
              <input type="text" class="form-control" name="productName" value="${productVO.productName!}"
                      placeholder="30字以内" 
                      data-required="true"
                      data-name="商品名称"
                      maxlength="30"
                      ></div>
            <label class="col-sm-1 control-label" >短标题<span class="u-required">*</span></label>
            <div class="col-sm-3">
              <input type="text" class="form-control" name="wirelessTitle" 
                value="${productVO.wirelessTitle!}"
                data-required="true"
                data-name="短标题"
                maxlength="20"
                placeholder="20字以内" 
                      ></div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">
              商品颜色
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <input type="text" class="form-control" name="colorName" value="${productVO.colorName!}"
                 data-name = "商品颜色"
                 data-required="true"
                 ></div>
            <label class="col-sm-1 control-label">
              商品色号
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <input type="text" class="form-control" name="colorNum" value="${productVO.colorNum!}"
                 data-name = "商品色号"
                 data-required="true"
                 <#if productVO.schedule??> disabled</#if>
                 ></div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">
              正品价格
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <input class="form-control" type="text" name="marketPrice" 
                placeholder="吊牌价格"
                data-required="true"
                data-pattern="^\s*\d+(\.\d{1,2})?\s*$"
                data-name = "正品价格"
                value="${productVO.marketPrice!}" ></div>
            <label class="col-sm-1 control-label">
              销售价格
              <span class="u-required">*</span>
            </label>
            <div class="col-sm-3">
              <input class="form-control" type="text" name="salePrice" value="${productVO.salePrice!}" 
                placeholder="网站销售价格"
                data-required="true"
                data-pattern="^\s*\d+(\.\d{1,2})?\s*$"
                data-name = "销售价格"
                 ></div>
            <label class="col-sm-1 control-label">
              供货价格
            </label>
            <div class="col-sm-3">
              <input class="form-control"  type="text" name="basePrice" value="${productVO.basePrice!}"
                        placeholder="与平台结算价格"
                        data-pattern="^\s*(\d+(\.\d{1,2})?)?\s*$"
                        data-name = "供货价格"
                         ></div>
          </div>
          <div class="hr-dashed"></div>
          <div class="form-group">
<#--             <label class="col-sm-1 control-label">推荐<span class="u-required">*</span></label>
            <div class="col-sm-3">
              <select name="isRecommend" id="" class="form-control">
                <option value="1" <#if (productVO.isRecommend!1) == 1>selected</#if>
                >是
                </option>
                  <option value="0" <#if (productVO.isRecommend!1) == 0>selected</#if>
                  >否
                </option>
              </select>
            </div>
 -->            
            <label class="col-sm-1 control-label">专柜同款<span class="u-required">*</span></label>
            <div class="col-sm-3">
              <select name="sameAsShop" id="" class="form-control">
                <option value="1" <#if ((productVO.sameAsShop)!1) == 1>selected</#if>
                >是</option>
                <option value="0" <#if ((productVO.sameAsShop)!1) == 0>selected</#if>
                >否</option>
              </select>
            </div>
            <label class="col-sm-1 control-label">增值税率</label>
            <div class="col-sm-3">
              <select name="addedTax" id="" class="form-control">
                <option value="17" <#if (productVO.addedTax!17) == 17>selected</#if>
                >17%
                </option>
                  <option value="13" <#if (productVO.addedTax!17) == 13>selected</#if>
                  >13%
                </option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-1 control-label">单位<span class="u-required">*</span></label>
            <div class="col-sm-1">
              <select name="unit" class="form-control" 
              data-name="单位"
              data-required="true" >
                <option value="">请选择</option>
                <option value="1" <#if (productVO.unit!0) == 1>selected</#if>>件</option>
                <option value="2" <#if (productVO.unit!0) == 2>selected</#if>>个</option>
                <option value="3" <#if (productVO.unit!0) == 3>selected</#if>>双</option>
                <option value="4" <#if (productVO.unit!0) == 4>selected</#if>>支</option>
                <option value="5" <#if (productVO.unit!0) == 5>selected</#if>>副</option>
                <option value="6" <#if (productVO.unit!0) == 6>selected</#if>>套</option>
                <option value="7" <#if (productVO.unit!0) == 7>selected</#if>>台</option>
                <option value="8" <#if (productVO.unit!0) == 8>selected</#if>>本</option>
                <option value="9" <#if (productVO.unit!0) == 9>selected</#if>>片</option>
                <option value="10" <#if (productVO.unit!0) == 10>selected</#if>>盒</option>
                <option value="11" <#if (productVO.unit!0) == 11>selected</#if>>束</option>
              </select>
            </div>
            <label class="col-sm-1 control-label">航空禁运<span class="u-required">*</span></label>
            <div class="col-sm-1">
              <select name="airContraband" class="form-control">
                <option value="1" <#if (productVO.airContraband!0) == 1>selected</#if>>是</option>
                <option value="0" <#if (productVO.airContraband!0) == 0>selected</#if>>否</option>
              </select>
            </div>
            <label class="col-sm-1 control-label">易碎品<span class="u-required">*</span></label>
            <div class="col-sm-1">
              <select name="fragile" class="form-control">
                <option value="1" <#if (productVO.fragile!0) == 1>selected</#if>>是</option>
                <option value="0" <#if (productVO.fragile!0) == 0>selected</#if>>否</option>
              </select>
            </div>
            <label class="col-sm-1 control-label">是否大件<span class="u-required">*</span></label>
            <div class="col-sm-1">
              <select name="big" class="form-control">
                <option value="1" <#if (productVO.big!0) == 1>selected</#if>>是</option>
                <option value="0" <#if (productVO.big!0) == 0>selected</#if>>否</option>
              </select>
            </div>
            <label class="col-sm-1 control-label">是否贵重<span class="u-required">*</span></label>
            <div class="col-sm-1">
              <select name="valuables" class="form-control">
                <option value="1" <#if (productVO.valuables!0) == 1>selected</#if>>是</option>
                <option value="0" <#if (productVO.valuables!0) == 0>selected</#if>>否</option>
              </select>
            </div>
          </div>
      <div class="hr-dashed"></div>
      <div class="form-group">
        <label class="col-sm-1 control-label">卖点描述<span class="u-required">*</span></label>
        <div class="col-sm-3">
          <textarea class="form-control" 
          placeholder="180字以内"
          data-required="true"
          maxlength="180"
          data-name="卖点描述"
          name="productDescp">${productVO.productDescp!}</textarea>
        </div>
        <label class="col-sm-1 control-label">洗涤/使用说明<span class="u-required">*</span></label>
        <div class="col-sm-3">
          <textarea class="form-control" 
            placeholder="180字以内"
            data-required="true"
            maxlength="180"
            data-name="洗涤/使用说明"
          name="careLabel">${productVO.careLabel!}</textarea>
        </div>
        <label class="col-sm-1 control-label">配件备注</label>
        <div class="col-sm-3">
          <textarea class="form-control" 
            placeholder="180字以内"
            maxlength="180"
            data-name="配件备注"
          name="accessory">${productVO.accessory!}</textarea>
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-1 control-label">售后说明</label>
        <div class="col-sm-3">
          <textarea class="form-control" 
          placeholder="售后服务说明，180字以内"
          maxlength="180"
          data-name="售后说明"
          name="afterMarket">${productVO.afterMarket!}</textarea>
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-1 control-label">长</label>
        <div class="col-sm-3">
          <input class="form-control" 
          placeholder="只以CM为单位填写数字"
          data-pattern="^\s*(\d+(\.\d{1,2})?)?\s*$"
          data-name="长"
          name="length" value="${productVO.length!}" >
        </div>
        <label class="col-sm-1 control-label">宽</label>
        <div class="col-sm-3">
          <input class="form-control" 
          placeholder="只以CM为单位填写数字"
          data-pattern="^\s*(\d+(\.\d{1,2})?)?\s*$"
          data-name="宽"
          name="width" value="${productVO.width!}" >
        </div>
        <label class="col-sm-1 control-label">高</label>
        <div class="col-sm-3">
          <input class="form-control" 
          data-name="高"
          placeholder="只以CM为单位填写数字"
          data-pattern="^\s*(\d+(\.\d{1,2})?)?\s*$"
          name="height" value="${productVO.height!}" >
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-1 control-label">产地<span class="u-required">*</span></label>
        <div class="col-sm-3">
          <input class="form-control" 
          data-required="true"
          data-name="产地"
          name="producing" value="${productVO.producing!}" >
        </div>
        <label class="col-sm-1 control-label">重量</label>
        <div class="col-sm-3">
          <input class="form-control" 
          data-name="重量"
          placeholder="只以KG为单位填写数字"
          data-pattern="^\s*(\d+(\.\d{1,2})?)?\s*$"
          name="weight" value="${productVO.weight!}" >
        </div>
      </div>
    </form>
  </div>
</div>
</div>
</div>

<div class="row">
<div class="col-sm-12">
<div class="m-card">
  <h2 class="card_b">
    <span class="glyphicon glyphicon-chevron-down pull-right"></span>
    商品属性
  </h2>
  <div class="card_c j-fieldgen"></div>
</div>
</div>

<div class="col-sm-12">
<div class="m-card">
  <h2 class="card_b">
    尺寸配置
  </h2>
  <div class="card_c form">
    <div class="j-sizeTemplate">
      <h4>尺码表</h4>
    </div>
    <div class="hr-dashed"></div>
    <div class="item">
      <h4>
        <input 
          type="checkbox" 
          name="isShowSizePic" id="isShowSizePic" <#if (productVO.isShowSizePic!)?is_number && productVO.isShowSizePic == 1>checked</#if>
                 />尺寸测量参考图（勾选使用）</h4>
       <img src="/res/defaults/sizeimg.jpg" alt="尺寸测量参考图">
    </div>
    <div class="hr-dashed"></div>
    <div class="j-helperSize item"> </div>
  </div>
</div>
</div>
</div>

<div class="row">

<div class="col-sm-12">
<div class="m-card">
  <h2 class="card_b">
    <span class="glyphicon glyphicon-chevron-down pull-right"></span>
    商品图片上传
  </h2>
  <div class="card_c">
    <form method="get" class="form m-form-product">
      <div class="form-group j-image-product">
        <label><strong>商品大图（商品详情页展示）</strong>最多16张/尺寸1242x1242 px/单张2M以内</label>
      </div>
      <div class="hr-dashed"></div>
      <div class="form-group j-image-list">
        <label><strong>商品列表图（品购页列表展示）</strong>可上传1-2张/ 尺寸563x710 px/单张2M以内/第1张请上传商品正面图，第2张请上传商品背面图</label>
      </div>
    </form>
  </div>
</div>
</div>
<div class="col-sm-12">
<div class="m-card">
  <h2 class="card_b">
    <span class="glyphicon glyphicon-chevron-down pull-right"></span>
    图文详情html自定义
  </h2>
  <div class="card_c">
    <span class="u-required">*</span> 图片限宽790px，高不限，图片大小不超过2M 
    <div class="m-editor"></div>
    <textarea class='j-editor' style="display:none" >${productVO.customEditHTML!}</textarea>
  </div>
</div>
</div>

<div class="col-sm-12 m-subtns">
<form action="" method = "post" class="j-preview preview"  enctype="application/x-www-form-urlencoded" target="_blank">
  <input type="hidden" name="product" value>
  <button  class="btn btn-primary btn-lg btn-fix1 j-submit" role="button">预览商品</button>
</form>
<a  class="btn btn-primary btn-lg btn-fix1 j-submit" role="button">提交商品</a>
<a href="/product/list" class="btn btn-default btn-lg btn-fix1 j-redirect" role="button">返回商品列表</a>
</div>
</div>
</@body>

<script>
window.__data__ = ${JsonUtils.toJson(productVO)}
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/product/edit.js"></script>

</body>
</html>