<#assign pageName="access-role"/>
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
<@crumbs parent={"txt":"用户权限管理","url":'#'} sub={"txt":"角色创建"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card f-cb">
      <h2 class="card_b j-list">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        角色创建
      </h2>

    </div>
  </div>
</div>

<div style="display:none" id="jstTemplate">
  <#noparse>
    <textarea name="ntp" id="ntp-addrole-box-window">
      <div></div>
    </textarea>
    <textarea name="jst" id="ntp-addrole-window">
      <form id="webForm">
        <div>
          <div class="m-addrole">
            <div class="line">
              <label>角色名称：</label>
              <input name="name" data-message="必须输入名称！" data-required="true" type="text" class="u-ipt"{if !!item.name} value="${item.name}" {/if}>
            </div>
            <div class="line">
              <label>角色列表：</label>
              <select name="parent" class="u-selt j-flag" data-required="true">
                {if !!item.roleList}
                {list item.roleList as a}
                  <option value="${a.id}" {if a.id==item.parent}selected="selected"{/if}>${a.name}</option>
                {/list}
                {/if}
              </select>
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
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/access/role.js?v=1.0.0.0"></script>

</body>
</html>