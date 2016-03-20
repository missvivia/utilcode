<!DOCTYPE html> 
<html>
<head>
<#assign pageName="authority-account">
<#include "/wrap/common.ftl">
	<meta charset="UTF-8">
	<title>${siteTitle} - ${page.title}</title>
	<#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/authority.css?v=1.0.0.1">
</head>
<body>
	<@side />
	<@wrap>
	<@crumbs parent={"txt":"权限管理","url":"/authority/authority"} sub={"txt":"账户管理"} />
	<#-- card -->
	<div class="row">
		<div class="col-sm-12">
      <div class="m-card f-cb">
        <h2 class="card_b">
          <span class="glyphicon glyphicon-chevron-down pull-right"></span>
          账户管理
        </h2>
        <m-accountform></m-accountform>
      </div>
    </div>
	</div>
  <div style="display:none" id="jstTemplate">
    <#noparse>
      <textarea name="ntp" id="ntp-adduser-box-window">
        <div></div>
      </textarea>
      <textarea name="jst" id="ntp-adduser-window">
        <form id="webForm">
          <div class="m-adduser">
            <div class="line">
              <label>员工姓名：</label>
              <input name="name" data-message="请输入字母、数字、下划线或中文！" data-pattern="^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9]|\-|\_)*$" data-required="true" maxlength="20" type="text"{if !!items.name} value="${items.name}"{/if}>
            </div>
            <div class="line">
              <label>员工登录名：</label>
              {if !! items.id}
              	<input name="displayName" data-message="必须！" data-required="true" type="text"  disabled  {if !!items.displayName} value="${items.displayName}"{/if}>
              {else}
              	<span id="displayNamePrefix">${items.namePrefix}</span>
              	<span class="point">.</span>
              	<input name="displayName" style="width:50px;" data-message="请输入两位字母或数字！" data-required="true" type="text" maxlength="2" data-pattern="^(\w){2}$" />
              {/if} 
            </div>
            <div class="line">
				<label>密码：</label>
              	{if !!items.id}
              		<input name="password" data-message="必须！" data-id="${items.id}" value="******" data-required="true" type="password" class="u-ipt"  placeholder="6-20位字母和数字"/> 
              	{else}
              		<input name="password" data-message="必须！" data-required="true" type="password" class="u-ipt"  placeholder="6-20位字母和数字"/>          	
            	{/if}
            </div>
            <div class="line">
              <label>员工工号：</label>
              <input name="accountNum" type="text"{if !!items.id} value="${items.accountNum}" {/if}>
            </div>
            <div class="line">
              <label>用户所在部门：</label>
              <input name="department" maxlength="20" type="text"{if !!items.department} value="${items.department}"{/if}>
            </div>
            <div class="line">
              <label>手机：</label>
              <input name="mobile" data-pattern="^(13|14|15|17|18)[0-9]{9}$" data-message="请输入正确的手机号！" type="text"{if !!items.mobile} value="${items.mobile}"{/if}>
            </div>
            <div class="line f-cb">
              <label class="f-fl">用户组：</label>
              <div class="lineri f-fr">
              {if !!items.group}
              {list items.group as item}
                <span class="item">
                  <input type="checkbox" id="${item.id}" class="chkbox j-check">
                  <label for='${item.id}' class="itemCheck"><span class="f-fr itemfr">${item.name}</span></label>
                </span>
              {/list}
              {/if}
              </div>
            </div>
          </div>
          <div class="m-winbot">
            <div class="btns">
              <span class="btn btn-primary j-flag">确定</span>
              <span class="f-mgl btn btn-primary j-flag">取消</span>
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
  <script src="${jspro}page/authority/account.js?v=1.0.0.1"></script>
</body>
</html>