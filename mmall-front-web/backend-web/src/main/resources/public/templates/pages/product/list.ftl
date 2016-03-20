<#assign pageName="product-list"/>
<#escape x as x?html>
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
<@body>
<@crumbs parent={"txt":"商品管理","url":'/product/list'} sub={"txt":"商品列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <div class="card_c">
        <form method="get" class="form-horizontal" id="search-form">
        		<div class="form-group">
        			<div class="f-cb">
        				<a class="btn btn-primary f-fl  m-downlbtn" href="/product/edit">添加商品</a>
        				<div class="f-pr f-fl " style="margin-right:10px;">
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
                    <label class="col-sm-1 control-label">商品类目</label>
                    <div class="col-sm-3">
                      <select id="category1" name="category1" id="" class="form-control j-cate">
                      	<option value="0">全部</option>
                      </select>
                    </div>
                    <label class="col-sm-1 control-label">商品类目</label>
                    <div class="col-sm-3 ">
                      <select id="category2" id="" name="category2" class="form-control j-cate">
                      	<option value="0">全部</option>
                      </select>
                    </div>
                    <label class="col-sm-1 control-label">商品类目</label>
                    <div class="col-sm-3">
                      <select name="lowCategoryId" id="" class="form-control j-save j-cate">
                      	<option value="0">全部</option>
                      </select>
                    </div>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <label class="col-sm-1 control-label">基本信息</label>
                    <div class="col-sm-3">
                      <select name="isBaseInfo" id="" class="form-control j-save" data-type="number">
                        <option value="0">请选择</option>
                        <option value="1">已录入</option>
                        <option value="2">未录入</option>
                      </select>
                    </div>
                    <label class="col-sm-1 control-label">商品图片</label>
                    <div class="col-sm-3">
                      <select name="isPicInfo" id="" class="form-control j-save" data-type="number">
                        <option value="0">请选择</option>
                        <option value="1">已录入</option>
                        <option value="2">未录入</option>
                      </select>
                    </div>
                    <label class="col-sm-1 control-label">模块详情</label>
                    <div class="col-sm-3">
                      <select name="isDetailInfo" id="" class="form-control j-save" data-type="number">
                        <option value="0">请选择</option>
                        <option value="1">已录入</option>
                        <option value="2">未录入</option>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                  	<label class="col-sm-1 control-label">尺码设置</label>
                    <div class="col-sm-3">
                      <select name="isSizeSet" id="" class="form-control j-save" data-type="number">
                        <option value="0">请选择</option>
                        <option value="1">已录入</option>
                        <option value="2">未录入</option>
                      </select>
                    </div>
                  </div>
                  
                  <div class="hr-dashed"></div>
                  <div class="form-group">
                    <label class="col-sm-1 control-label">商品名称</label>
                    <div class="col-sm-3">
                      <input name="productName" type="text" class="form-control j-save" name="text">
                    </div>

                    <label class="col-sm-1 control-label">商品货号</label>
                    <div class="col-sm-3">
                      <input name="goodsNo" type="text" class="form-control j-save">
                    </div> 
                    <label class="col-sm-1 control-label">条形码</label>
                    <div class="col-sm-3">
                      <input name="barCode" type="text" class="form-control j-save">
                    </div>
                  </div>
                  <div class="hr-dashed"></div>
                  <div class="form-group" id="form-datePicker">
                    <label class="col-sm-1 control-label">添加时间</label>
                    <div class="col-sm-3 j-datepick" data-name="stime" data-value=""></div>
                  </div>
                  <div class="form-group" id="form-datePicker">
                    <label class="col-sm-1 control-label"></label>
                    <div class="col-sm-3 j-datepick" data-name="etime" data-value="" data-time="23:59:59"></div>
                  </div>
                <div class="hr-dashed"></div>
                <div class="form-group">
                  <div class="col-sm-2">
                  	<input type="button" name="btn-submit" value="查询" class="btn btn-primary btn-block " />
                  </div>
                  <div class="f-fr f-cb">
                    <a class="btn btn-primary f-fr m-downlbtn" href="/product/exportAll" target="_blank" id="exportall">全部导出</a>
		            <a class="btn btn-primary f-fr j-export m-downlbtn" href="/product/edit" target="_blank" id="export">批量导出商品资料</a>
                  	<a class="btn btn-primary f-fr  j-batch-name" href="javascript:void(0);">批量修改商品名</a>
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
      <div class="card_c">
        <div id="m-productlist">
              
        </div>
        <#-- <m-productlist></m-productlist> -->
        
      </div>
    </div>
  </div>
</div>

</@body>


<script>
<#noescape>
	window.category = ${stringify(catetoryList)}
</#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/product/list.js"></script>

</body>
</html>
</#escape>