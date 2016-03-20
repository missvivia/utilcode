<#assign pageName="content-config"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
       .wrap{
           text-align:center;
           height:300px;
           padding-top:100px;
       }
       .btn-gray{
          background:#e4e4e4;
          border:1px solid #bfbfbf;
          color:#474747;
       }
       .btn-gray:hover{
          background:#bfbfbf;
       }
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"内容管理","url":'#'} sub={"txt":"首页配置"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       		首页配置
      </h2>
      <div class="card_c">
      
      	<form class="form-horizontal">
      		<div class="form-group wrap">
				  <div class="publishsuccess" id="publishsuccess">
				      <p style="font-size:16px;">页面发布成功！</p>
				      <div class="btnGroup" style="margin-top:20px;">
					      <a target="_blank" class="btn btn-primary" href="http://023.baiwandian.cn">查看</a>&nbsp;&nbsp;
					      <a class="btn btn-gray" href="/content/config">返回</a>
			          </div>
				  </div>
	     	</div>
	     </form>
	     
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/contentConfig/publishSuccess.js"></script>

</body>
</html>