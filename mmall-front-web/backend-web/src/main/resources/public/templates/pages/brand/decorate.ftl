<#assign pageName="po-decorate"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/decorate.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"po","url":"/image/upload"} sub={"txt":"档期装修"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        	档期装修
      </h2>
      <div class="card_c">
        <div id="body" class="managebox">
        	<div class="row">
        		<div class="m-decoratemdl j-flag"><div class="lbl">自定义模块</div><div class="mdlact"><a href="/decorate/custom?id=${RequestParameters['id']}" class="edit  f-mgl">编辑</a></div></div>
	<div class="m-decoratemdl j-flag"><div class="lbl">全部商品</div><div class="mdlact"><a class="f-ib hideshow">隐藏</a><a class="pedit f-mgl">编辑</a></div></div>
	<div class="m-decoratemdl j-flag"><div class="lbl">商家地图</div><div class="mdlact"><a class="f-ib hideshow">隐藏</a></div></div>
        		
        	</div>
        </div>
      </div>
    </div>
  </div>
</div>

</@wrap>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/brand/decorate.js"></script>

</body>
</html>