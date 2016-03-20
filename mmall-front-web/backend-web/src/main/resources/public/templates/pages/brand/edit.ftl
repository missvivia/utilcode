<#assign pageName="brand-edit"/>
<#escape x as x?html>
<@compress single_line=true>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"品牌介绍页管理","url":'/brand/display'} sub={"txt":"编辑品牌介绍页"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        基本信息
      </h2>
      <div class="card_c" id="basicinfo">

      </div>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        门店信息
      </h2>
      <div class="card_c">
        <div class="from-group j-table">
          <div class="col-sm-2 row">
            <a class="btn btn-inverse btn-block" id="addShop" >添加门店</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="form-group"></div>
<div class="row btn-b1s">
    <div class="f-tac f-cb"><div class="col-sm-2"><label class="btn btn-primary btn-block j-action col-sm-2">保存</label></div><div class="col-sm-2"><label class="btn btn-primary btn-block j-action">提交</label></div>
    <div class="col-sm-2"><a class="btn btn-primary btn-block j-flag" href="javascript:void(0)" id="preview2main">预览</a></div></div>
</div>
<form action="" ref=pform target="_blank" method="post" class="j-2form">
    <input type="hidden" name="data">
</form>
</@wrap>

<#noparse>
<div style="display:none" id="jst-template">
  <textarea name="ntp" id="ntp-preview-image">
    <div class="item">
      <canvas class="j-flag"></canvas>
      <div class="j-flag name"></div>
      <a class="delete j-flag" href="#">删除</a>
    </div>
  </textarea>
  <textarea name="jst" id="jst-preview-image">
  	<ul>
    {list data as item}
    	<li class="item"><img src="${item}" width="150" height="150" /><a href="javascript:void(0)" class="delete" data-index=${item_index}>删除</a></li>
    {/list}
    </ul>
  </textarea>
  
</div>
</#noparse>

<!-- @NOPARSE -->
<script type="text/javascript">
  <#noescape>
  var g_brand = ${JsonUtils.toJson(data)};
  </#noescape>
</script>
<!-- /@NOPARSE -->

<@baiduMap/>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/brand/edit.js"></script>

</body>
</html>
</@compress>
</#escape>
