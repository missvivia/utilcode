<#assign pageName="content-helpcenter"/>
<#escape x as x?html>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
  .bg-model .modal-dialog{width:800px;}
  .m-error input{border: 1px solid #d44950!important;}
  .m-error label.text{color: #d44950; display: block;line-height: 34px;}
  .m-addpic{padding-bottom:10px;}
  #editor{height:440px;width:100%}
  #editor .zarea{height:400px;}
  .f-c{color:#999;}
  .m-colorPick{z-index:10;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"帮助中心管理","url":'/content/helpcenter'} sub={"txt":"编辑文章"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        编辑文章
      </h2>
      <div class="card_c">
        <form id="form-id">
        <div class="form-horizontal m-form-polist">
          <div class="form-group">
            <label class="col-sm-2 control-label">标题：</label>
            <div class="col-sm-5">
	             <input  value="${(helpArticle.title)!''}" name="title" data-max-lengt="25"  data-required="true" class="form-control" placeholder="不超过25个字"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">关键字：</label>
            <div class="col-sm-5">
	             <input value="${(helpArticle.keywords)!''}" name="keywords"  data-required="true" class="form-control" placeholder="用英文逗号分隔"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">类目：</label>
            <div class="col-sm-2">
              <select autocomplete ="off" name="categoryId" class="form-control" id="category">
                <option value="-1">请选择</option>
                <#list categoryList as category>
                <option value="${category.id}" <#if helpArticle??&&(category.id==helpArticle.categoryId||category.id==helpArticle.parentCategoryId)>selected="selected"</#if>>${category.name}</option>
                </#list>
              </select>
            </div>
            <div class="col-sm-2" id="c-child">
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 f-tar">平台：</label>
            <div class="col-sm-1">
            	<input type="checkbox" <#if helpArticle??&&helpArticle.publishType!=2>checked="checked"</#if> name="publishType" value="1"/> <label class="f-vam">WEB</label>
            </div>
            <div class="col-sm-1">
            	<input type="checkbox" <#if helpArticle??&&helpArticle.publishType!=1>checked="checked"</#if> value="2" name="publishType" /> <label class="f-vam">APP</label>
            </div>
          </div>
          <div class="form-group">
          	<div class="col-sm-2">
            </div>
          	<div class="col-sm-6">
          		<p>正文<span class="f-fr f-c f-fs1">正文不超过20000个字</span></p>
              	<div id="editor" class="editor"></div>
            </div>
          </div>
          <div class="form-group">
          	<div class="col-sm-2">
            </div>
          	<div class="col-sm-2">
              <a class="btn btn-primary btn-block" id="save">保存</a>
            </div>
            <div class="col-sm-2">
               <a class="btn btn-primary btn-block" id="release">发布</a>
            </div>
            <div class="col-sm-1">
              <a class="text-info" id="cancel">取消</a>
            </div>
          </div>
        </div>
        <form>
      </div>
    </div>
  </div>
</div>
</@wrap>
	<!-- @NOPARSE -->
	<#noescape>
	<script>
	var g_categoryList=${categoryList_json!'null'},
		g_helpArticle=${helpArticle_json!'null'};
	</script>
	</#noescape>
	<!-- /@NOPARSE -->

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/content/helpcenter/edit.js?v=1.0.0.0"></script>
</body>
</html>
</#escape>