<#assign pageName="item-product">

<#escape x as x?html>
<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
	<link rel="stylesheet" type="text/css" href="/src/css/page/product_manage.css?v=1.0.0.1">
	<style type="text/css">
	    .pd{
	       padding: 1px;
	    }
	    .l-unstart{
	       background:#959595; 
	       color:#fff;
	    }
	    .l-active{
	       background:#ec312c; 
	       color:#fff;
	    }
	    .l-end{
	       border:1px solid #c7c7c7; 
	       color:#c7c7c7;
	    }
	</style>
</head>

<body>
<!-- 左边目录列表 -->
<@side />

<!-- 右边内容区域 -->
<@body>
<@crumbs parent={"txt":"商品管理","url":'/item/product/list?type=1'} sub={"txt":"商品列表"}/>

<!-- form 搜索面板 -->
<div class="row">
	<div class="col-sm-12">
		<div class="m-card card_c">
			<div class="card_c">
				<form method="get" class="form-horizontal" id="search-form">
					
					<!--
					<div class="form-group">
						<div class="col-sm-6">
						
							<a class="btn btn-primary" href="/item/product/create">+新增商品</a>
							
							<div class="btn-group" id="cardbtn">
			                   	<button type="button" class="btn btn-primary">批量导入</button>
			                   	<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" >
			                   		<span class="caret"></span>
			                   		<span class="sr-only">Toggel Dropdown</span>
			                   	</button>
			                    
			                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
			                    	<li><label data-url="/rest/batchUploadProductInfo">商品资料</label></li>
			                   		<li><label data-url="/rest/batchUploadPic">商品图</label></li>
			                   		<li><label data-url="/rest/batchUploadSizeInfo">商品尺码</label></li>
			                   		<li><label data-url="/rest/batchUploadCustomHtml">详情HTML模块</label></li>
			                   	</ul>
		                	</div>

							<div class="btn-group" id="cardbtn1">
			                   	<button type="button" class="btn btn-primary">模板下载</button>
			                   	<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" >
			                   		<span class="caret"></span>
			                   	</button>
			                   	<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
			                   		<li><a target="_self" href="/res/files/批量导入商品基本资料模板.zip">商品基本资料模板</a></li>
			                   		<li><a target="_self" href="/res/files/批量导入商品尺码模板.xlsx">商品尺码模板</a></li>
			                   		<li><a target="_self" href="/res/files/批量导入商品图片说明.docx">商品图片说明</a></li>
			                   		<li><a target="_self" href="/res/files/批量导入商品详情HTML说明.docx">商品详情HTML说明</a></li>
			                   	</ul>
		                     </div>
						</div>
					</div>

					<div class="hr-dashed"></div>-->
<#--
					<div class="form-group">
						<div class="col-sm-1"><a class="btn btn-primary" href="/item/product/create">+新增商品</a></div>
					</div>
					<div class="hr-dashed"></div>
-->
					<div class="form-group">
						<div class="col-sm-1 noRight">
							<label class="control-label">搜索：</label>	
						</div>
						<div class="col-sm-2">
						    <#if (RequestParameters.flag)?exists>
						        <select class="form-control" id="search-select">
									<option value="0">商品名</option>
									<option value="1">商品货号</option>
									<option value="2">条形码</option>
								</select>
						    <#else>
								<select class="form-control" id="search-select">
									<option value="0" <#if (searchParam.productName)?exists>selected="selected"</#if> >商品名</option>
									<option value="1" <#if (searchParam.goodsNo)?exists>selected="selected"</#if> >商品货号</option>
									<option value="2" <#if (searchParam.barCode)?exists>selected="selected"</#if>>条形码</option>
								</select>
							</#if>
						</div>
						
						<div class="col-sm-2">
						    <#if (RequestParameters.flag)?exists>
						        <input type="text" class="form-control" name="productName" id="search-text" />
						    <#else>
								<input type="text" class="form-control" name="productName" id="search-text" <#if (searchParam.productName)?exists>value="${searchParam.productName}"<#elseif (searchParam.goodsNo)?exists> value="${searchParam.goodsNo}"<#elseif (searchParam.barCode)?exists>value="${searchParam.barCode}"</#if>/>
						    </#if>
						</div>
					</div>

					<div class="hr-dashed"></div>
					<div class="form-group">
						<div class="col-sm-1 noRight">
							<label class="control-label">分类筛选：</label>	
						</div>
						<div class="col-sm-2">
							<select class="j-cate form-control" name="category1" id=""><option value="0">全部</option></select>
						</div>
						<div class="col-sm-2">
							<select class="j-cate form-control" name="category2" id=""><option value="0">全部二级分类</option></select>
						</div>
						<div class="col-sm-2">
							<select class="j-cate form-control" name="category3" id=""><option value="0">全部三级分类</option></select>
						</div>
						<div class="col-sm-2">
						     <#if (RequestParameters.flag)?exists>
						        <select class="j-cate form-control" name="isLimited" id="limit-select">
								      <option value="-1" >所有商品</option>
								      <option value="0">不限购商品</option>
								      <option value="1">限购商品</option>
								</select>
						     <#else>
								<select class="j-cate form-control" name="isLimited" id="limit-select">
								      <option value="-1" >所有商品</option>
								      <option value="0" <#if searchParam.isLimited == 0>selected="selected"</#if> >不限购商品</option>
								      <option value="1" <#if searchParam.isLimited == 1>selected="selected"</#if>>限购商品</option>
								</select>
							</#if>
						</div>
					</div>
					
					<div class="hr-dashed"></div>
					<div class="form-group">
						<div class="col-sm-1 noRight">
							<label class="control-label">日期筛选：</label>	
						</div>
						
						<div class="col-sm-2">
						    <#if (RequestParameters.flag)?exists>
						        <div class="j-datepick" data-name="stime" data-value=""></div>
						    <#else>
							    <div class="j-datepick" data-name="stime" <#if searchParam.stime == 0>data-value=""<#else>data-value="${searchParam.stime}"</#if> ></div>
						    </#if>
						</div>
						<div style="float:left;line-height:34px;">一</div>
						<div class="col-sm-2">
						   <#if (RequestParameters.flag)?exists>
						      <div class="j-datepick" data-name="etime" data-value=""></div>
						   <#else>
							  <div class="j-datepick" data-name="etime" <#if searchParam.etime == 0>data-value=""<#else>data-value="${searchParam.etime}"</#if> ></div>
						   </#if>
						</div>
						<div class="col-sm-2">
						    <#if (RequestParameters.flag)?exists>
						        <select class="form-control" name="asc">
									<option value="false">日期由近到远</option>
									<option value="true">日期由远到近</option>
								</select>
						    <#else>
								<select class="form-control" name="asc">
									<option value="false" <#if searchParam.asc == false>selected="selected"</#if> >日期由近到远</option>
									<option value="true" <#if searchParam.asc == true>selected="selected"</#if> >日期由远到近</option>
								</select>
							</#if>
						</div>
					</div>
					
					<div class="hr-dashed"></div>
					<div class="form-group">
						<div class="col-sm-1">
							<input type="button" name="btn-submit" id="submitBtn" value="搜索" class="btn btn-primary form-control"></a>
						</div>
					</div>
				</form>
				
				<!-- 商品列表 -->
				<div class="row">
					<div class="col-sm-12">
							<div id="m-productlist"></div>
					</div>
				</div>					
			</div>
		</div>	
	</div>
</div>



</@body>

<script>
<#noescape>
	window.category = ${stringify(catetoryList)}
	window.listType = ${RequestParameters.type!0}
	window.flag = ${RequestParameters.flag!0}
	window.lowCategoryId = ${searchParam.lowCategoryId}
	window.firstCategoryId = ${searchParam.firstCategoryId}
	window.secondCategoryId = ${searchParam.secondCategoryId}
	window.level = ${searchParam.level}
	window.offset = ${searchParam.offset}
</#noescape>
</script>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/list.js?v=1.0.0.7"></script>
</body>
</html>
</#escape>