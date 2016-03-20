<#assign pageName="brand-display"/>


<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/brand/display.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"品牌管理","url":'/brand'} sub={"txt":"品牌介绍页管理"}/>
<!-- card -->

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        品牌列表
      </h2>
      <div class="card_c">
        <div class="from-group">
          <div class="col-sm-3 f-fl">
            <a class="btn btn-inverse btn-block" href="/brand/create">新建</a>
          </div>
        </div>
        <div id="list"></div>
      </div>
    </div>
  </div>
</div>

</@wrap>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/brand/display.js"></script>
</body>
</html>