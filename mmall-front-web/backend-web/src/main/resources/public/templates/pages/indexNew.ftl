<#assign pageName="index-home"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>商家管理系统首页</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/page/index.css">
</head>
<body>
<@side />
<@wrap>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <div class="card_c">
        <div>用户：<span id="username"></span>,您好!</div>
        <div id="module-cnt">
        <#assign poStatusMap ={"1":"未完备","2":"待上线","3":"待开场","4":"结束","5":"售卖中","6":"失效"}/>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/index.js"></script>
</body>
</html>