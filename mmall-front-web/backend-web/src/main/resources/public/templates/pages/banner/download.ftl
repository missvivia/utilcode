<!doctype html>
<html lang="en">
<head>
<#assign pageName="schedule-download"/>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style>
  .f-pr{position:relative;}
  .f-pa{position:absolute;}
  .logo{padding:1px;border:1px solid #ccc;margin-bottom:10px}
  .b-item{margin-right:50px;}
  .b-item .banner-1{padding:20px 20px 40px 20px;width:600px;background-color:#f3f3f3;}
  .b-item .banner-2{padding:20px 20px 40px 20px;width:320px;background-color:#f3f3f3;}
  .b-item .unit{left:20px;top:20px;position:absolute;}
  .b-item .cover{left:0;top:0;width:22%;height:100%;background-color:red;opacity:0.3;}
  .b-item .zk{left:30%;bottom:10%;position:absolute;font-size:20px;}
  .op{margin-top:20px;}
  </style>
 </head>
<body>
<@side />
<@wrap>
<@crumbs parent={"txt":"推广资料管理","url":'/schedule/blist'} sub={"txt":"资料下载"}/>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>LOGO</h2>
      <div class="card_c" >
        <div class="f-cb">
          <div class="row">
          	<div class="col-sm-3">
            	<img src="${brand.logo}" class="logo" width="270" height="116"/>
            	<div class="f-tac">270x116</div>
            </div>
            <p class="col-sm-4">
            	说明：<br/>
            	1、选中图片鼠标右键“另存为”可将LOGO（.png）保存至本地<br/>
            	2、LOGO使用时，尽量只做等比压缩操作
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>BANNER规范</h2>
      <div class="card_c" >
        <div class="f-cb">
          <img src="/res/images/sample/banner.png" class=""/>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/banner/download.js"></script>
</body>
</html>