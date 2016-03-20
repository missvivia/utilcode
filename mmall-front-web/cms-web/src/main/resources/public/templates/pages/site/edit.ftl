<#assign pageName="site-create"/>


<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" href="/src/css/base.css?v=1.0.0.2">
  <style type="text/css">
  .list-group{height:300px;overflow:auto;border-radius:5px;border:1px solid #ddd;}
  .part{background:#ddd;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"站点管理","url":'/site/site'} sub={"txt":"站点编辑"}/>
<!-- card -->

  <div class="row">
    <div class="col-sm-12">
      <div class="m-card">

        <div class='card_c'>
          <form method="get" class="form-horizontal m-form-product j-baseform">
            <div class="form-group">
              <label class="col-sm-2 control-label">
                <span class="u-required">*</span>
                站点名称
              </label>
              <div class="col-sm-6">
                <input type="text" class="form-control" name="name" id="name" value="${siteCMS.siteName}" 
                data-required=true data-max-length=30
                data-msg="站点名称格式错误"
                >
                <input type="hidden" id="siteId" value="${siteCMS.siteId}"/>
                <span class="help-block"></span>
              </div>
            </div>
            
			  <div class="form-group">
			    <label class="col-sm-2 control-label"><span class="u-required">*</span>覆盖地区</label>
			    <div class="col-sm-4" id="area">
			    	
			    </div>
			  </div>            
              
          </form>
        </div>
      
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-8 col-sm-offset-2">
      <div class="m-card-btn m-card">
        <div class="cbtn_left"><a href="#" class="btn  btn-primary j-submit" name="edit">确定</a></div>
        <div class="cbtn_right"><a href="/site/site;" class="btn  btn-default">返回</a></div>
      </div>
    </div>
  </div>
</@wrap>
<script type="text/javascript">

</script>
</div>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/site/create.js?v=1.0.0.0"></script>

</body>
</html>