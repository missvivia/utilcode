<#assign pageName="index-home"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>运营系统首页</title>
  <#include "/wrap/css.ftl">
  
</head>
<body>
<@side />
<@wrap>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
    <div class="card_c">
     <#assign jobNum =3 />
      <div class="form-horizontal">
      	<div class="form-group">
           <div class="col-sm-12"><span id="username"></span>，你好！</div>
	    </div>
	  </div>
    </div>
    </div>
  </div>
</div>
</@wrap>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/index.js?v=1.0.0.0"></script>
</body>
</html>