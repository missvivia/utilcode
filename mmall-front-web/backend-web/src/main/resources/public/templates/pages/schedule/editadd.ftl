<#assign pageName="schedule-edit"/>
<#if productVO?? == false>
  <#assign productVO = {} />
</#if>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/product_manage.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"商品新建"}/>
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
         <div class="form-group j-select">
           <label class="col-sm-2 control-label">商品类目</label>
           <div class="col-sm-3">
             <select name="category1" id="" class="form-control" data-value="${categories[0]!}">
             </select>
           </div>
           <div class="col-sm-3">
             <select name="category2" id="" class="form-control"  data-value="${categories[1]!}">
             </select>
           </div>
           <div class="col-sm-3">
             <select name="category3" 
             
             id="" class="form-control" data-value="${categories[2]!}">
           
             </select>
           </div>
         </div>
         <div class="form-group">
           <label class="col-sm-2 control-label">商品品牌</label>
           <div class="col-sm-3">
             <select name="brandId" id="" class="form-control">
                <option value="${productVO.brandId!}" selected>${productVO.brandName!}</option>
             </select>
           </div>
         </div>
         <div class="form-group">
           <label class="col-sm-2 control-label">增值税率</label>
           <div class="col-sm-3">
             <select name="addedTax" id="" class="form-control">
               <option value="17" <#if (productVO.addedTax!17) == 17>select </#if> >17%</option>
               <option value="13" <#if (productVO.addedTax!17) == 13>select </#if> >13%</option>
             </select>
           </div>
         </div>
         <div class="hr-dashed"></div>

         <div class="form-group">
           <label class="col-sm-2 control-label">商品货号</label>
           <div class="col-sm-2">
             <input type="text" class="form-control" name="goodsNo" 
             data-required="true"
             data-msg="商品货号不能为空"
             value="${productVO.goodsNo!}">

           </div>
           <label class="col-sm-2 control-label" >商品名称</label>
           <div class="col-sm-2">
             <input type="text" class="form-control" name="productName" value="${productVO.productName!}"

             data-msg = "商品名称不能为空"
             data-required="true"
             >
           </div>
           <label class="col-sm-2 control-label">商品描述</label>
           <div class="col-sm-2">
             <input type="text" class="form-control" name="desp" 
  
             data-msg = "商品描述不能为空"
             data-required="true"
             value="${productVO.desp!}">
           </div>
         </div>
         <div class="form-group">
           <label class="col-sm-2 control-label">商品色号</label>
           <div class="col-sm-2">
             <input type="text" class="form-control" name="colorNum" value="${productVO.colorNum!}"
             data-msg = "商品色号不能为空"
             data-required="true"
             >
           </div>
           <label class="col-sm-2 control-label">商品颜色</label>
           <div class="col-sm-2">
             <input type="text" class="form-control" name="colorName" value="${productVO.colorName!}"
             data-msg = "商品颜色不能为空"
             data-required="true"
             >
           </div>
         </div>
         <div class="hr-dashed"></div>
         <div class="form-group form-group-vertical">
           <label class="col-sm-2 control-label">价格</label>
           <div class="col-sm-3">
             <div class="input-group">
               <div class="input-group-addon">正品</div>
               <input class="form-control" type="text" name="marketPrice" 
               data-required="true"
               data-type= "number"
               data-msg = "正品价格格式错误"

               value="${productVO.marketPrice!}" >
             </div>
           </div>
           <div class="col-sm-3">
             <div class="input-group">
               <div class="input-group-addon">销售</div>
               <input class="form-control" type="text" name="salePrice" value="${productVO.salePrice!}" 
               data-required="true"
               data-type= "number"
               data-msg = "销售价格格式错误"

               ></div>
           </div>
           <div class="col-sm-3">
             <div class="input-group">
               <label class="input-group-addon" for="basePrice">供货</label>
               <input class="form-control"  type="text" name="basePrice" value="${productVO.basePrice!}"

                data-required="true"
               data-type= "number"
                data-msg = "供货价格格式错误"
               >
             </div>
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
       尺码模板 
      </h2>
      <div class="card_c form j-sizeTemplate">

      </div>

    </div>
  </div>
</div>

<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        规格图片上传
      </h2>
      <div class="card_c">
        <form method="get" class="form m-form-product">
          <div class="form-group j-image-product">
            <label>商品展示, 之多上传3张</label>
          </div>
          <div class="hr-dashed"></div>
          <div class="form-group j-image-list">
            <label>列表展示, 之多上传2张</label>
          </div>
          <div class="hr-dashed"></div>
          <div class="form-group j-image-detail">
            <label>详情展示, 之多上传10张</label>
          </div>
        </form>
      </div>
    </div>
  </div>
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        商品详情
      </h2>
      <div class="card_c j-fieldgen"></div>
    </div>
  </div>
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        图文详情html自定义da
      </h2>
      <div class="card_c">
        <div class="m-editor">
        </div>
        <textarea class='j-editor' style="display:none" >${productVO.customEditHTML!}</textarea>

      </div>
    </div>
  </div>

  <div class="col-sm-12 m-subtns">
    <a  class="btn btn-primary btn-lg btn-fix1 j-submit" role="button">提交商品</a>
    <a href="/product" class="btn btn-default btn-lg btn-fix1" role="button">返回商品列表</a>
  </div>

</div>



</@wrap>

<script>
	window.__data__ = ${stringify(productVO)}
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/editadd.js"></script>

</body>
</html>