<#assign pageName="item-spu"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
  .modal-title,.modal-body{text-align:left;}
  .modal-body{height:200px;overflow:auto;}
  .btn-default{display:none;}
  .modal{background-color: rgba(0,0,0,0.5);}
  .modal-content{border: 1px solid rgba(255, 255, 255, 0.5);box-shadow:0;}
  .loading{width:300px;}
 	.loading .zbar,.loading .zcls{display:none;}
	.loading .zcnt{text-align:center;}
	.itemCategory{
		background:url(/res/images/select_arrow.png) no-repeat right 13px;
		overflow: hidden;
	    white-space: nowrap;
	    text-overflow: ellipsis;
	    padding-right: 20px;
	}
	#categories{
		position:relative;
	}
	.listTable{
		display:none;
		position:absolute;
		left:0;
		top:33px;
		z-index:999;
		height:300px;
		background:#fff;
		border:1px solid #ccc;
		overflow:auto;
		width:100%;
	}
	.listTable table > tbody > tr > td{
		border:none;
		padding:0;
	}
	.listTable table > tbody > tr > td > a{
	  padding: 8px;
      line-height: 1.42857143;
      display:block;
    }
    .listTable table > tbody > tr > td > a:hover{
    	text-decoration:none;
    }
    .btn-disabled,.btn-disabled:hover{background:#ccc;border:1px solid #ccc;color:#fff;}
    .j-disabled,.j-disabled:hover{color:#ccc;text-decoration:none;}
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"单品列表"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       		单品库管理
      </h2>
      <div class="card_c">
      
      	<form class="form-horizontal" id="search-form">
      		<div class="form-group">
	       		<div class="col-sm-4">
	         	 	<input type="text" class="form-control" name="searchValue" id="searchValue" placeholder="输入 单品名/条码 查找" >
		        </div>
		        <div class="col-sm-6">
        	  		<input type="button" value="搜索" class="btn btn-primary j-flag" name="btn-search" id="searchBtn"/>
       		 	</div>
	     	</div>
	     </form>
	      
      </div>
      <div class="card_c">
       <div id="spulist"></div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/spu.js?v=1.0.0.6"></script>

</body>
</html>