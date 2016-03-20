<#assign pageName="product-size">
<!DOCTYPE html> 
<html>
<head>
  <#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
</head>
<body>
	<@side />
	<@body>
	<@crumbs parent={"txt":"商品管理","url":"#"} sub={"txt":"尺寸模版"} />
	<#-- card -->
	<div class="row">
		<div class="col-sm-12">
      <div class="m-card">
        <h2 class="card_b">
          <span class="glyphicon glyphicon-chevron-down pull-right"></span>
          添加尺码模版
        </h2>
        <div>
          <div class="card_c" id="pagebox">
            <form id="webForm" class="form-horizontal">
              <div class="row">
                <div class="col-sm-12">
                  <div class="form-group">
                    <label class="col-sm-2 control-label">商品类目<span class="u-required">*</span></label>
                    <div class="col-sm-3">
                      <select class="form-control j-cate j-view" data-required="true" data-value="${sizeTemplateVO.categories[0]}">
                      </select>
                    </div>
                    <div class="col-sm-3">
                      <select class="form-control j-cate j-view" data-required="true" data-value="${sizeTemplateVO.categories[1]}">
                      </select>
                    </div>
                    <div class="col-sm-3">
                      <select name="lowCategoryId" class="form-control j-cate j-view" data-message="必须！" data-value="${sizeTemplateVO.categories[2]}">
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">模版名称<span class="u-required">*</span></label>
                    <div class="col-sm-3">
                      <input name="templateName" data-required="true" data-required="true" data-message="必须！" <#if sizeTemplateVO.templateName??> value="${sizeTemplateVO.templateName}" </#if> type="text" class="form-control j-view">
                    </div>
                    <label class="col-sm-2 control-label">尺码提示</label>
                    <div class="col-sm-3">
                      <input name="remindText" <#if sizeTemplateVO.remindText??> value="${sizeTemplateVO.remindText}" </#if> type="text" class="form-control j-view">
                    </div>
                  </div>
                </div>
              </div>
            </form>
              <div class="row">
                <div id="size-tmp-box">

                </div>
              </div>
              <div class="row">
                <div class="col-sm-2">
                  <button class="btn btn-white j-flag j-view">保存</button>
                  <a href="/product/size" class="btn btn-primary">返回列表</a>
                </div>
              </div>
          </div>
          
        </div>
      </div>
    </div>
	</div>

  </@body>

  <script>
    window.__sizeTemplateVO__ = ${stringify(sizeTemplateVO)};
    window.__sizeTemplateId__ = ${RequestParameters["id"]};
    window.__view__ = ${RequestParameters["view"]};
  </script>
  
  <!-- @SCRIPT -->
  <script src="${jslib}define.js?pro=${jspro}"></script>
  <script src="${jspro}page/product/addtmp.js"></script>
</body>
</html>