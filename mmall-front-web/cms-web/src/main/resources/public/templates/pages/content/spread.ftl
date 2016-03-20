<#assign pageName="content-spread"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
    <style>
      .m-error input{border: 1px solid #d44950!important;}
      .m-error label.text{color: #d44950; display: block;line-height: 34px;}
      .link{color: #428bca;cursor: pointer;}
      .link:hover{color: #2a6496;text-decoration: underline;}
    </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap >
<@crumbs parent={"txt":"内容管理","url":'#'} sub={"txt":"推广内容管理"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        内容管理
      </h2>
      <div class="card_c">
        <m-content-spread>
        </m-content-spread>
      </div>
    </div>
  </div>
</div>
</@wrap>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/content/spread.js?v=1.0.0.0"></script>
</body>
</html>