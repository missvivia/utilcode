<#assign pageName="schedule-audit"/>

<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
<#include "/fake/schedule/polist.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/po.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"商品新建"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        内容管理
      </h2>
      <div class="card_c m-audit">
        <div>站点<select class="j-flag"><#list province as item><option value="${item.id}">${item.name}</option></#list></select></div>
        <div class="j-flag"></div>
        <div class="j-flag"></div>
        <ul class="nav nav-tabs" role="tablist">
          <li><a href="/schedule/audit">待审核</a></li>
          <li  class="active"><a href="#">已通过</a></li>
        </ul>
        <table class="table table-hover">
            <tr>
              <td>品牌编号</td>
              <td>品牌名称</td>
              <td>档期开始时间</td>
              <td>审核状态</td>
              <td>操作</td>
              <td>备注</td>
            </tr>

            <#list polist as item>
              <tr>
                <td>${item.brandId}</td>
                <td>${item.name}</td>
                <td>${item.date}</td>
                <td><#if item.state==0>待审核<#elseif item.state==1>已通过<#elseif item.state==2>未通过</#if></td>
                <td><#if item.state==2><a href="/schedule/create?id=${item.id}">修改</a></#if></td>
                <td>${item.desc}</td>
              </tr>
            </#list>
        </table>
      </div>
    </div>
  </div>
</div>
</@wrap>



</body>
</html>