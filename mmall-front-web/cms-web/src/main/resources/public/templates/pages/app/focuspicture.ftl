<#assign pageName='app-focuspicture'/>
<#escape x as x?html>
<!doctype html>
<html lang='en'>
<head>
<#include '/wrap/common.ftl' />
  <meta charset='UTF-8'>
  <title>${siteTitle} - ${page.title}</title>
  <#include '/wrap/css.ftl'>
  <style type='text/css'>
  .bg-model .modal-dialog{width:800px;}
  .m-error input{border: 1px solid #d44950!important;}
  .m-error label.text{color: #d44950; display: block;line-height: 34px;}
  .table-bordered th, .table-bordered td{max-width:100px;word-wrap: break-word;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={'txt':'首页焦点图','url':'#'} sub={'txt':'首页焦点图管理'}/>
<!-- card -->
<div class='row'>
  <div class='col-sm-12'>
    <div class='m-card'>
      <h2 class='card_b'>
        <span class='glyphicon glyphicon-chevron-down pull-right'></span>
        首页焦点图管理
      </h2>
      <div class='card_c'>
        <form id='form-id'>
        <div class='form-horizontal m-form-polist'>
          <div class='form-group'>
            <label class='col-sm-2 control-label'>当前站点：</label>
            <label class='col-sm-1 control-label' id='dcity'></label>
            <div class='col-sm-2' id='citys'></div>
            <label class='col-sm-2 control-label'>在线日期：</label>
            <@dateSelect key='qtime' value='${.now?long?number_to_datetime}'/>
            <div class='col-sm-1'>
              <a class='btn btn-primary btn-block' id='doquery'>查询</a>
            </div>
          </div>
        </div>
        <form>
        <div id='module-cnt'></div>
      </div>
    </div>
  </div>
</div>

<#noparse>
<textarea name='jst' id='template-box' style='display:none;'>
  <select name='city' id='city' class='form-control'>
      {list result as x}
      <option value='${x.id}'>${x.areaName}</option>
      {/list}
  </select>
</textarea>
</#noparse>
</@wrap>

<!-- @SCRIPT -->
<script src='${jslib}define.js?pro=${jspro}'></script>
<script src='${jspro}page/focuspicture/index.js?v=1.0.0.0'></script>
</body>
</html>
</#escape>