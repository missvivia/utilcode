<#assign pageName="business-account"/>
<!doctype html>
<html lang="en">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/business.css">
  <style type="text/css">
       .wrap{
           text-align:center;
           height:300px;
           padding-top:100px;
       }
       .title{
           font-weight:bold;
           font-size:20px;
           margin:10px;
       }
       .btn-box{
           margin-top:30px;
       }
   </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
	<@crumbs parent={"txt":"商家管理","url":'#'} sub={"txt":"新建商家"}/>  
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       		创建商家
      </h2>
      <div class="card_c">
         <form class="form-horizontal">
      		<div class="form-group wrap">
	           <div class="title">商家创建成功！</div>
	           <div>您可以</div>
	           <div class="btn-box">
			      <span class="btn btn-primary" id="manageUser">管理指定用户</span>&nbsp;&nbsp;
			      <a class="btn btn-gray" href="/business/account">以后再说</a>
	           </div>
	        </div>
	     </form>
      </div>
    </div>
  </div>
</div>
</@wrap>

<script type="text/javascript">
     window.businessId = ${businessId};
</script>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/business/accountSuccess.js"></script>

</body>
</html>