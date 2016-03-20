<#assign pageName="schedule-exhibition"/>
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
        档期展示管理
      </h2>
      <div class="card_c">
        <div class="m-exhibitionact" id="exhibition">
        	<div class="form-group">当前设置省区<select class="j-flag"><#list province as item><option value="${item.id}">${item.name}</option></#list></select></div>
        	<div class="form-group"><select class="j-flag"><#list categoryList as item><option value="${item.id}">${item.name}</option></#list></select> <input type="text" placeholder="输入档期编号搜索" class="j-flag  f-mgl"><span class="btn btn-primary j-flag f-mgl">搜索</span></div>
        	<div>当前正在进行中的档期 XX个   等待开始的档期XX个</div>
        	<table class="table" id="table">
        		
        	</table>
        	<div class="m-btns"><span class="btn btn-primary j-flag">保存排序</span></div>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>
<#noparse>
<div id="jst-template">

<textarea name="jst" id="jst-po-items" style="display:none">
<tr>
  <td>序号</td>
  <td>品牌编号</td>
  <td>品牌名称</td>
  <td>档期开始时间</td>
  <td>档期状态</td>
  <td>推文位</td>
  <td>操作</td>
</tr>
{list polist as item}
  <tr>
  	<td>${item_index+1}</td>
    <td>${item.brandId}</td>
    <td>${item.name}</td>
    <td>${item.date}</td>
    <td>{if item.state==3}未开始{else if item.state==4}已开始{/if}</td>
    <td></td>
    <td>{if (item.order)&&item.order=="1"}<a class="j-clear" data-index=${item_index}>取消置顶</a>{else}<a class="j-up f-mgl" data-index=${item_index}>上移</a><a class="j-down f-mgl" data-index=${item_index}>下移</a><a class="j-top f-mgl" data-index=${item_index}>置顶</a>{/if}</td>
  </tr>
{/list}
</textarea>
</div>
</#noparse>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/schedule/exhibition.js?v=1.0.0.0"></script>

</body>
</html>