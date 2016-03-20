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
       .progress-group{
           width:530px;
           margin:0 auto;
           display:none;
       }
       .progress-group .progress{
           width:500px;
           float:left;
       }
       .progress-group .cancel{
           display:inline-block;
           float:left;
           margin-left:10px;
       }
       .publishfail{
           display:none;
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
			      <div class="progress-group" id="progress">
			          <div class="progress progress-striped active">
						   <div class="progress-bar progress-bar-info" role="progressbar" 
						      aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" 
						      style="width: 100%;">
						   </div>
					  </div>
					  <div style="clear:both;">
					       <p>正在发布页面，请耐心等待...</p>
					  </div>
					  <div style="margin-top:30px;">* 如页面长时间无响应，请刷新页面</div>
				  </div>
				  <div class="uploadsuccess" id="uploadsuccess">
				      <p style="font-size:16px;">文件上传成功！</p>
				      <p>您可以预览及发布页面&nbsp;&nbsp;或&nbsp;&nbsp;<a href="/content/config">重新上传</a></p>
				      <div class="btnGroup" style="margin-top:20px;">
					      <a href="http://023.baiwandian.cn/src/html/helpcenter/preview/index.htm" target="_blank" class="btn btn-primary" id="preview">预览</a>&nbsp;&nbsp;
			              <span class="btn btn-primary" id="issue">发布</span>
			          </div>
				  </div>
				  <div class="publishfail" id="publishfail">
				      <p id="msg" style="font-size:16px;">页面发布失败...</p>
				      <div class="btnGroup" style="margin-top:20px;">
					      <a class="btn btn-primary" href="/content/uploadSuccess">返回</a>&nbsp;&nbsp;
			              <span class="btn btn-primary" id="issueAgain">重新发布</span>
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
<script src="${jspro}page/contentConfig/uploadSuccess.js"></script>

</body>
</html>