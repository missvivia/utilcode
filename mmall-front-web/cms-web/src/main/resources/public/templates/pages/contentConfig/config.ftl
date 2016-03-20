<#assign pageName="content-config"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <style type="text/css">
       .table thead tr th, .table tbody tr td{
           text-align:center;
           vertical-align:middle;
       }
       .bg-gray{
           background: #f2f2f2;
       }
       .m-card .card_c {
		   padding: 30px 15px;
	   }
	   .btn-gray{
		   background:#e4e4e4;
		   border:1px solid #bfbfbf;
		   color:#474747;
		}
		.m-t{
		   margin-top:5px;
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
         <form>
	         <table class="table table-bordered">
				  <thead>
					    <tr class="bg-gray">
					      <th></th>
					      <th>Web</th>
					      <th>IOS</th>
					      <th>Wap</th>
					      <th>AOS</th>
					    </tr>
				  </thead>
				  <tbody>
				        <tr>
				           <td  class="bg-gray">首页</td>
				           <td>
				                <label class="btn btn-primary j-img" data-name="importIndexWeb" id="importIndexWeb">上传数据文件</label>
			                    <input type="hidden" name="importIndexWeb" data-message="上传数据文件">
			                    <#if (backupFileMap['1'])??>
			       		           <div class="m-t"><span class="btn btn-gray recover" data-type='1'>恢复到上一次操作<br/>${backupFileMap['1']}</span></div>
			       		        </#if>
			       		        <div>
			       		            <a href="/download/web-index-template.xlsx">模板文件下载</a>
			       		        </div>
			               </td>
			               <td>
				                <label class="btn btn-primary j-img" data-name="importIndexIOS" id="importIndexIOS">上传数据文件</label>
			                    <input type="hidden" name="importIndexIOS" data-message="上传数据文件">
			                    <#if (backupFileMap['3'])??>
			       		           <div class="m-t"><span class="btn btn-gray recover" data-type='3'>恢复到上一次操作<br/>${backupFileMap['3']}</span></div>
			       		        </#if>
			                    <div>
			       		            <a href="/download/app-index-template.xlsx">模板文件下载</a>
			       		        </div>
			               </td>
			               <td>
				                <label class="btn btn-primary j-img" data-name="importIndexWap" id="importIndexWap">上传数据文件</label>
			                    <input type="hidden" name="importIndexWap" data-message="上传数据文件">
			                    <#if (backupFileMap['2'])??>
			       		          <div class="m-t"><span class="btn btn-gray recover" data-type='2'>恢复到上一次操作<br/>${backupFileMap['2']}</span></div>
			       		        </#if>
			                    <div>
			       		            <a href="/download/wap-index-template.xlsx">模板文件下载</a>
			       		        </div>
			               </td>
			               <td>暂未开放</td>
				        </tr>
				        <tr>
				           <td class="bg-gray">分类</td>
				           <td>
				                <label class="btn btn-primary j-img" data-name="importCateWeb" id="importCateWeb">上传数据文件</label>
			                    <input type="hidden" name="importCateWeb" data-message="上传数据文件">
			                    <#if (backupFileMap['4'])??>
			       		           <div class="m-t"><span class="btn btn-gray recover" data-type='4'>恢复到上一次操作<br/>${backupFileMap['4']}</span></div>
			       		        </#if>
			                    <div>
			       		            <a href="/download/web-category-template.xlsx">模板文件下载</a>
			       		        </div>
			               </td>
			               <td>
				                <label class="btn btn-primary j-img" data-name="importCateIOS" id="importCateIOS">上传数据文件</label>
			                    <input type="hidden" name="importCateIOS" data-message="上传数据文件">
			                    <#if (backupFileMap['6'])??>
			       		           <div class="m-t"><span class="btn btn-gray recover" data-type='6'>恢复到上一次操作<br/>${backupFileMap['6']}</span></div>
			       		        </#if>
			                    <div>
			       		            <a href="/download/app-category-template.xlsx">模板文件下载</a>
			       		        </div>
			               </td>
			               <td>
				                <label class="btn btn-primary j-img" data-name="importCateWap" id="importCateWap">上传数据文件</label>
			                    <input type="hidden" name="importCateWap" data-message="上传数据文件">
			                    <#if (backupFileMap['5'])??>
			       		           <div class="m-t"><span class="btn btn-gray recover" data-type='5'>恢复到上一次操作<br/>${backupFileMap['5']}</span></div>
			       		        </#if>
			                    <div>
			       		            <a href="/download/wap-category-template.xlsx">模板文件下载</a>
			       		        </div>
			               </td>
			               <td>暂未开放</td>
				        </tr>
				  </tbody>
			 </table>
	    </form>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/contentConfig/config.js?v=1.0.0.1"></script>

</body>
</html>