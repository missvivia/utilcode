<#assign pageName="item-model"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/item.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<#if modelId??>
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"修改商品模型"}/>
<#else>
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"新建商品模型"}/>
</#if>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	<#if modelId??>修改商品模型<#else>新建商品模型</#if>
      </h2>
      <div class="card_c">
      		<form method="post" class="form-horizontal" id="addModel">
      			<input type="hidden" name="modelId" value="<#if modelId??>${modelId}</#if>" id="modelId">
      			<div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>模型名称:</label>
      				<div class="col-sm-2">
      					<input type="text" class="form-control" id="modelName" placeholder="请输入名称" data-focus="true" data-required="true" name="modelName" data-message="必填项"/>
      				</div>
      			</div>
      			<div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>对应分类：</label>
      				<div class="col-sm-2">
      					<select  class="form-control" id="category"  name="categoryNormalId" data-focus="true" data-required="true"  data-message="必选项">
      						<option value="0">请选择分类</option>
      					</select>
      				</div>
      			</div>
      			<div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>扩展属性：</label>
      				<div class="col-sm-2">
      					<a class="btn btn-default" id="addextend">添加扩展属性</a>
      				</div>
      			</div>
      			<div class="form-group">
      				<div class="col-sm-offset-2 m-couponcond">
      					<p class="perror1">你还没有添加属性选项</p>
      					<table class="table table-bordered">
      						<thead style="visibility:hidden">
								<tr>
									<th>属性名</th>
									<th>样式</th>
									<th>属性数据</th>
									<th>作为商品筛选项</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="attrbox">
							</tbody>
						</table>
				    </div>
				</div>
      			<div class="form-group">
      				<label class="col-sm-2 control-label"><span class="u-required">*</span>规格：</label>
      				<div class="col-sm-2">
      					<a class="btn btn-default" id="addstand">添加规格</a>
      				</div>
      			</div>
      			<div class="form-group">
      				<div class="col-sm-offset-2 m-couponcond">
      					<p class="perror2">你还没添加规格选项</p>
      					<table class="table table-bordered">
      						<thead style="visibility:hidden">
								<tr>
									<th>规格名</th>
									<th>显示类型</th>
									<th>规格数据</th>
									<th>作为商品筛选项</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="standbox">
							</tbody>
						</table>
				    </div>
				</div>
      			<div class="form-group">
				    <div class="col-sm-1 col-sm-offset-2">
				      <#if modelId??>
				      	<a class="btn btn-primary btn-block" id="updateBtn">保存</a>
				      <#else>
				      	<a class="btn btn-primary btn-block" id="submitBtn">保存</a>
				      </#if>
				    </div>
				 </div>
      		</form>
      </div>
    </div>
  </div>
</div>
<div style="display:none" id="jstTemplate">
	<#noparse>
	<textarea name="jst" id="attr-box">
		<td>${item.parameterName}</td>
		<td>
			{if !!item.single && item.single=='1'} 单选 {/if}
			{if !!item.single && item.single=='2'} 多选 {/if}
			{if !!item.single && item.single=='3'} 输入框 {/if}
		</td>
		<td>
		 	{if !!item.optionList}
				{list item.optionList as option}
					<label>${option.paramOption}</label>
				{/list}
			 {/if}
		</td>
		<td>
			{if !!item.show && item.show=='1'}
				是
			{else}
				否
			{/if}
		</td>
		<td  class="attr-mark">
			<a href="#" class="p-flag" data-type="md" >修改</a>
			<a href="#" class="p-flag"  {if !item.parameterId} data-type="del" {/if} {if !!item.parameterId} data-type="subdel" {/if}>删除</a>
		</td>
    </textarea>
    <textarea name="jst" id="stand-box">
		<td>${item.specificationName}</td>
		<td>
			{if !!item.specificationType && item.specificationType=='1'} 文字 {/if}
			{if !!item.specificationType && item.specificationType=='2'} 图片 {/if}
		</td>
		<td>
			{if !!item.speciOptionList}
			{list item.speciOptionList as option}
				<label>${option.speciOption}</label>
			{/list}
			{/if}
		</td>
		<td>
			{if !!item.show && item.show=='1'}
				是
			{else}
				否
			{/if}
		</td>
		<td  class="attr-mark">
			<a href="#" class="p-flag" data-type="md">修改</a>
			<a href="#" class="p-flag" {if !item.specificationId} data-type="del" {/if} {if !!item.specificationId} data-type="subdel" {/if}>删除</a>
		</td>
    </textarea>
    </#noparse>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/addmodel.js?v=1.0.0.0"></script>
</body>
</html>