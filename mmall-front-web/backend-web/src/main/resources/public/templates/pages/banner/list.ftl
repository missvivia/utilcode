
<!doctype html>
<html lang="en">
<head>
<#assign pageName="schedule-blist"/>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style>
  a.link,a.link:hover{margin-right:10px;color:#fff;text-decoration:underline;}
  .f-mr20{margin-right:20px;}
  .tt{background-color:#f9f9f9;}
  .pd10{padding-left:10px;}
  .img-box{position:relative;overflow:hidden;width:124px;height:220px;text-align:center;background:url("/res/defaults/dimg.png") 50% 50% no-repeat #eee;}
  .img-box-1{width:560px;height:175px;}
  .img-box-2{width:165px;height:108px;margin-right: 85px;}
  .img-box .progress{position:relative;height:10px;width:80%;margin-left:auto;margin-right:auto;margin-top:-16%;}
  </style>
 </head>
<body>
<@side />
<@wrap>
<@crumbs parent={"txt":"推广资料管理","url":'/schedule/manage'} sub={"txt":"banner管理"}/>
<div class="row">
  <div class="col-sm-12">
    <!-- card -->
    <div class="m-card">
      <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span><a class="pull-right link" href="/schedule/download" target="_blank">推广资料规范下载</a>banner管理</h2>
      <div class="card_c" >
        <div class="alert alert-info j-bannerlist f-dn" role="alert">对于上新BANNER，商家传一个大尺寸，由系统按首页和频道页的设计等比压成两个尺寸。上新BANNER，上传950x306 的图片；预告BANNER，上传320x110 的图片。</div>
        
      </div>
    </div>
    <!-- /card -->
  </div>
</div>
</@wrap>


<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/banner/list.js"></script>
</body>
</html>