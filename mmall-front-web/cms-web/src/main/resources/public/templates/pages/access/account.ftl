<#assign pageName="access-account"/>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/access.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
	<@crumbs parent={"txt":"用户权限管理","url":'#'} sub={"txt":"帐号权限分配"}/>  
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card f-cb">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        帐号权限分配
      </h2>
      <div class="col-sm-12 j-list">
      </div>
    </div>
  </div>
</div>
<div style="display:none" id="jstTemplate">
  <#noparse>
    <textarea name="ntp" id="add-account-win-box">
      <div></div>
    </textarea>
    <textarea name="jst" id="add-account-win">
      <form id="webForm">
        <div>
          <div class="m-wincnt">
            <div class="line">
              <label>管理员:</label>
              <select name="adminId" class="u-selt j-flag">
                {if !!item.adminList}
                  {list item.adminList as a}
                    <option value="${a.id}" {if a.id == item.adminId}selected="selected" {/if}>${a.name}</option>
                  {/list}
                {/if}
              </select>
            </div>
            <div class="line">
              <label>帐号:</label>
              <input name="displayName" data-message="必须！" data-required="true" type="text" class="u-ipt"{if !!item.displayName} value="${item.displayName}" disabled{/if} >
            </div>
            <div class="line">
            	<label>站点：</label>
        		<select class="u-selt j-flag" id='account-siteList-box' name="site"></select>	
            </div>
            <div class="line" id="account-areaList-box">
    			<label>区域：</label>
    			<div id="area" class="list-group" style="display:inline-block;"></div>              
            </div>
			<div class="line">
              <label>密码:</label>
              {if !!item.id}
              <input name="password" type="password" data-id="${item.id}" data-required="true" class="u-ipt" value="******"  placeholder="6-20位字母和数字">
              {else}
              <input name="password" data-message="必须！" data-required="true" type="password" class="u-ipt"  placeholder="6-20位字母和数字">
              {/if}
            </div>
            <div class="line">
              <label>姓名:</label>
              <input name="name" data-message="必须！" data-required="true" type="text" class="u-ipt"{if !!item.name} value="${item.name}" {/if}>
            </div>
			<div class="line">
              <label>工号:</label>
              <input name="accountNum" data-message="必须！" data-required="true" type="text" class="u-ipt"{if !!item.accountNum} value="${item.accountNum}" {/if}>
            </div>
            <div class="line">
              <label>部门:</label>
              <input name="department" type="text" class="u-ipt"{if !!item.department} value="${item.department}" {/if}>
            </div>
            <div class="line">
              <label>手机:</label>
              <input name="mobile" data-message="请输入正确的手机号！" data-pattern="^(13|14|15|17|18)[0-9]{9}$" type="text" class="u-ipt"{if !!item.mobile} value="${item.mobile}" {/if}>
            </div>
            <div class="line">
            	<label>选择角色：</label>
            	<div id="account-roleList-box" class="j-flag" style="display:inline-block;">

            	</div>
            </div>
          </div>
          <div class="m-winbot">
            <div class="btns">
              <span class="btn btn-primary j-flag">提交</span>
              <span class="f-mgl btn btn-primary j-flag">取消</span>
            </div>
          </div>
        </div>
      </form>
    </textarea>
    <textarea name="jst" id="account-roleList">
      {list items as item}
      <div class="line j-li">
        <select class="u-selt" name='role'>
          {if !!item.roleList}
          {list item.roleList as a}
            <option value="${a.id}" {if a.id==item.id}selected="selected"{/if}>${a.name}</option>
          {/list}
          {/if}
        </select>
        </div>
      </div>
      {/list}
    </textarea>
    <textarea id="account-siteList" name="jst">
      {list items as item}
          <option value="">请选择站点</option>
          {if !!item.siteList}
          {list item.siteList as a}
            <option value="${a.siteId}" {if a.siteId == item.currentSiteId}selected{/if}>${a.siteName}</option>
          {/list}
          {/if}
      {/list}    	
    </textarea>
    <textarea id="area-box" name="jst">

	    {if !!items}
	          {list items as a}
	            <div class='list-group-item'>
	            	<input type="checkbox" id='${a_index}' name='area' value="${a.areaId}" text="${a.areaName}" {if a.isChecked == 1}checked{/if}/>
	            	<label for='${a_index}'>${a.areaName}</label>
	            </div>
	          {/list}
	    {else}
	    	<div>该站点没有绑定区域</div>
	    {/if}      		
    </textarea>
  </#noparse>
</div>

</@wrap>
<#include "/wrap/widget.ftl" />
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/access/account.js?v=1.0.0.2"></script>

</body>
</html>