<!DOCTYPE html>
<html>
<head>
<#assign pageName="authority-authority">
<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/authority.css?v=1.0.0.1">
</head>
<body>
	<@side />
	<@wrap>
	<@crumbs parent={"txt":"权限管理","url":"/authority/authority"} sub={"txt":"角色管理"} />
	<#-- card -->
	<div class="row">
		<div class="col-sm-12">
      <div class="m-card f-cb">
        <h2 class="card_b">
          <span class="glyphicon glyphicon-chevron-down pull-right"></span>
         角色管理
        </h2>
        <m-authorityform></m-authorityform>
      </div>
    </div>
	</div>
  <div style="display:none" id="jstTemplate">
    <#noparse>
      <textarea name="ntp" id="ntp-add-usergroup-window">
        <form id="webForm" data-focus-mode="1" class="j-flag">
          <div>
            <div class="m-addusergp">
              <div class="line">
                <label style="width:60px;">角色名称:</label>
                <input type="text" name="groupName" data-required="true" data-pattern="^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9]|\-|\_)*$" data-message="请输入字母、数字、下划线或中文！"  class="u-ipt j-flag" maxlength="20">
              </div>
              <div>
                <label>权限设置:</label>
                <div class="j-flag"></div>
              </div>
            </div>
            <div class="m-winbot">
              <div class="btns">
                <span class="btn btn-primary j-flag">提交</span>
                <span class="f-mgl btn btn-primary j-flag">重置</span>
              </div>
            </div>
          </div>
        </form>
      </textarea>
    </#noparse>
  </div>
  </@wrap>
  <#include "/wrap/widget.ftl" />

  <!-- @SCRIPT -->
  <script src="${jslib}define.js?pro=${jspro}"></script>
  <script src="${jspro}page/authority/authority.js?v=1.0.0.1"></script>
</body>
</html>