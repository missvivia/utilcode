
<#assign pageName="site-site"/>

<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
  #search{cursor:pointer;}
  .part{border-left:5px solid red;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"订单管理","url":'/site/site'} sub={"txt":"订单查询"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">

      <div class="card_c j-it"></div>
    </div>
  </div>
</div>


</@wrap>
<#include "/wrap/widget.ftl" />
<script id="siteListTpl" type="text/regular" name='siteListTpl'>
<form id="abc" method="post" action='/site/delete'>
<div class="m-clst table">
  <div class="clst_action form-inline" style="margin-bottom:10px;">
    <a class="btn btn-primary" href="/site/create">添加站点</a>
    <a class="btn btn-primary" href="javascript:;" on-click={{this.batchDelete()}}>删除</a>
    <div class="input-group form-group" style="float:right;">
     	<input type="text" class="form-control" id="key" placeholder="输入站点名称/ID查找" on-keyup={{this.onKeyUp($event)}}/>
     	<div style="display:none;"><input type="text"/></div>
     	<div class="input-group-addon" id="search" on-click={{this.onSearch($event)}}><span class="glyphicon glyphicon-search"></span></div>
    </div>    
  </div>
  <table class="table  table-striped">
    <thead>
      <tr>
        <th width="10%"><input type="checkbox" id="all" on-click={{this.__setAllCheck($event)}}> 全选</th>
        <th width="10%">站点名称</th>
        <th width="10%">ID</th>
        <th width="60%">区域</th>
        <th width="10%">操作</th>
      </tr>
    </thead>
    <tbody>
	  {{#list list as act}}
	    <tr>
	      <td><input type="checkbox" value="{{act.siteId}}" name="check" on-click={{this.__setItemCheck($event)}}/></td>
		  <td>{{ act.siteName }}</td>
          <td>{{ act.siteId}}</td>
          <td>
          	{{#list act.areaList as area}}
          		{{area.areaName}};
          	{{/list}}
          </td>
          <td>
            <a href="/site/edit/{{act.siteId}}" class='btn' title='编辑'>
            	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
            </a>
            <a href="javascript:;" class='btn' on-click={{this.del(act.siteId)}} title='删除'>
            	<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
            </a>
          </td>
	        </tr>
	      {{/list}}
	    </tbody>
	  </table>
	</div>
	<div class="text-right m-wpager">
	  <pager total={{Math.ceil(total / 10)}} current={{current}} ></pager>
	</div>
</form>
</script>
</script>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/site/site.js?v=1.0.0.0"></script>

</body>
</html>