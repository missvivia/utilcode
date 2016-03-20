<#assign pageName="business-business"/>

<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
<#include "/fake/seller/seller.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/business.css?v=1.0.0.0">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商家管理","url":'#'} sub={"txt":"帐号详情"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       创建商家帐号
      </h2>
      <div class="card_c">
        <div>
        	<div>
            <form class="form-horizontal" id="dataform">
              <div class="form-group">
                <label class="col-sm-2 control-label">
                    	登录帐号
                </label>
                <div class="col-sm-4">
                	${(business.businessAccount)!''}
                </div>
              </div>
              
              
         <!--     <div class="form-group">
                <label class="col-sm-2 control-label">
                  	入驻品牌
                </label>
                <div class="col-sm-4">
                  ${business.actingBrandName}
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                    	商家类型
                </label>
                <div class="col-sm-4">
                	<#if business.type==1>代理商<#elseif business.type==2>品牌商</#if>
                </div>
              </div>-->
              <div class="form-group">
                <label class="col-sm-2 control-label">
                    	销售站点
                </label>
                <div class="col-sm-4">
                	<#list business.areaNames as name>${name}&nbsp;</#list>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	 公司名称
                </label>
                <div class="col-sm-4">
                	${(business.companyName)!''} 
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	法人姓名
                </label>
                <div class="col-sm-4">
                	${(business.legalPerson)!''} 
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	法人证件
                </label>
                <div class="col-sm-4">
                	${(business.legalPersonID)!''}  
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	开户人证件扫描件
                </label>
                <div class="col-sm-2">
                	<div class="m-imgbox">
                		<#if (business.holderIDPositiveImg)??>
                			<a href="${business.holderIDPositiveImg}" data-lightbox="holderIDPositiveImg">
                				<img src="${business.holderIDPositiveImg}">
                			</a>
                		</#if>
                	</div>
                </div>
                <div class="col-sm-2">
                	<div class="m-imgbox">
                		<#if (business.holderIDNegativeImg)??>
                			<a href="${business.holderIDNegativeImg}" data-lightbox="holderIDNegativeImg">
                				<img src="${business.holderIDNegativeImg}">
                			</a>
                		</#if>
                	</div>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	企业营业执照编号
                </label>
                <div class="col-sm-4">
                	${(business.registrationNumber)!''}
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	营业执照有效期
                </label>
                <div class="col-sm-4">
                	${(business.registrationNumberStart?number_to_datetime)!''} 至  <#if business.registrationNumberAvaliable == 1>长期有效<#else>${(business.registrationNumberEnd?number_to_datetime)!''}</#if>
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                   	企业营业执照扫描件
                </label>
                <div class="col-sm-2">
                	<div class="m-imgbox">
                		<#if (business.registrationImg)??>
                			<a href="${business.registrationImg}" data-lightbox="registrationImg">
                				<img src="${business.registrationImg}">
                			</a>
                		</#if>
                	</div>
                </div>
                <div class="col-sm-2">
                	<div class="m-imgbox">
                		<#if (business.registrationCopyImg)??>
                			<a href="${business.registrationCopyImg}" data-lightbox="registrationCopyImg">
                				<img src="${business.registrationCopyImg}">
                			</a>
                		</#if>
                	</div>
                </div>
              </div>
   <!--             <div class="form-group">
                  <label class="col-sm-2 control-label">
                      商标注册登记证扫描件
                  </label>
                  <#if business.type == 2>
                    <div class="col-sm-2">
                    	<div class="m-imgbox">
                    		<#if (business.brandImg)??>
                    			<a href="${business.brandImg}" data-lightbox="brandImg">
                    				<img src="${business.brandImg}">
                    			</a>
                    		</#if>
                    	</div>
                    </div>
                  <#else>
                    <div class="col-sm-2">
                      无  
                    </div>
                  </#if>
                </div>-->
                <div class="form-group">
                  <label class="col-sm-2 control-label">
                      品牌使用授权扫描件
                  </label>
                  <#if business.type == 1>
                  <div class="col-sm-2">
                  	<div class="m-imgbox">
                  		<#if (business.brandAuthImg)??>
                  			<a href="${business.brandAuthImg}" data-lightbox="brandAuthImg">
                  				<img src="${business.brandAuthImg}">
                  			</a>
                  		</#if>
                  	</div>
                  </div>
                  <#else>
                    <div class="col-sm-2">
                      无  
                    </div>
                  </#if>
                </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                  	联系人姓名
                </label>
                <div class="col-sm-4">
                  	${(business.contactName)!''}
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
               	  联系电话
                </label>
                <div class="col-sm-4">
                  ${(business.contactTel)!''}
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">
                 	联系地址
                </label>
                <div class="col-sm-4">
                  ${(business.contactProvince)!''} ${(business.contactCity)!''} ${(business.contactCountry)!''} ${(business.contactAddress)!''}
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">
                 	联系邮箱
                </label>
                <div class="col-sm-4">
                  ${(business.contactEmail)!''}
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">
               	退货联系人
                </label>
                <div class="col-sm-4">
                  ${(business.returnContactName)!''}
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
               	 退货联系电话
                </label>
                <div class="col-sm-4">
                  ${(business.returnContactTel)!''}
                </div>
              </div>
              
              <div class="form-group">
                <label class="col-sm-2 control-label">
                	退货地址
                </label>
                <div class="col-sm-6">
                  ${(business.returnProvince)!''} ${(business.returnCity)!''} ${(business.returnCountry)!''} ${(business.returnAddress)!''}
                </div>
              </div>
            </form>
          </div>
          <div class="m-btns f-tac">
            <a class="btn btn-primary f-mgl" href="/business/edit/${business.id}" >修改</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</@wrap>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/business/detail.js?v=1.0.0.0"></script>

</body>
</html>