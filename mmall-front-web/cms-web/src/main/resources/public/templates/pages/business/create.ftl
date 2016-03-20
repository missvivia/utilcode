<#assign pageName="business-business"/>
<#assign manageType =["日用百货","洗涤用品","食品","保健食品","营养补充食品","家居护理用品","包装材料","工艺品","纸制品","床上用品","电脑软硬件","耗材","通讯器材","文化办公用品","照明电器","不锈钢制品","铝合金制品","厨房用具","钟表","玻璃制品","酒店用品","体育用品"]/>

<#-- 定义isAdd变量，用来判断是创建还是编辑商家 -->
<#if business??>
<#assign isAdd = false />
<#else>
<#assign isAdd = true />
</#if>

<#-- 创建商家是测试用的信息,提交时屏蔽 -->
<#if isAdd && false>
<#assign business={"businessAccount":"vtest1@163.com","companyName":"vtest1","companyDesc":"cs",
          "legalPerson":"测试法人","legalPersonID":"3302811986672701034","registrationNumber":"123434455",
          "orgCode":"1234453453","taxCertNum":"998498594885","taxCertNum":12983838374,"ePayAcount":"vtest1@163.com",
          "contactName":"vtest1","contactTel":"15268809607","contactEmail":"vtest1@163.com","returnContactName":"退货人",
          "returnContactTel":15268809607,"returnAddress":"江汉路566号",
          "returnProvince":"浙江省",
          "returnCity":"杭州市",
          "contactAddress":"江汉路567号",
          "contactProvince":"浙江省",
          "contactCity":"杭州市",
          "type":1,
          "storeName":"store",
          "areaIds":[],
          "sendDistrictDTOs":[],
          "actingBrandId":123,
          "holderName":"开户人名",
          "holderID":"1234334",
          "returnCountry":"滨江区",
          "registrationNumberAvaliable":1,
          "holderIDPositiveImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "holderIDNegativeImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          
          "legalPersonIDPositiveImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "legalPersonIDNegativeImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "registrationImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "registrationCopyImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "accountLicense":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "accountLicense":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "orgCopyImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "taxCertImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "taxCertCopyImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D",
          "brandAuthImg":"http://nos.netease.com/vstore-photocenter-bucket2/fecefeec-d0c2-43a4-afd2-87be77b90cfb?NOSAccessKeyId=5a515def0402498ea660e2ad8655fa37&Expires=4566614796&Signature=t4fak4uR0EdT4G77%2FwQvvqMLm%2B0AwOhcs2vJBelYOmc%3D"
          }/>
</#if>


<!doctype html>
<html lang="en">
<head>
<#function isExist items value>
  <#list items as item>
    <#if item == value>
      <#return true>
    </#if>
  </#list>
  <#return false>
</#function>
<#include "/wrap/common.ftl" />
<#include "/fake/seller/seller.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/business.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<#if isAdd == false>
<@crumbs parent={"txt":"商家管理","url":'/business/account'} sub={"txt":"编辑商家"}/>
<#else>
<@crumbs parent={"txt":"商家管理","url":'/business/account'} sub={"txt":"新建商家"}/>
</#if>

<!-- card -->
<div class="row">
<div class="col-sm-12">
    <div class="m-card">
        <h2 class="card_b">创建商家帐号<span class="glyphicon glyphicon-chevron-down pull-right"></span></h2>

        <div class="card_c">
            <div>
              <div>
                <form class="form-horizontal" id="dataform"  autocomplete="off">
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        帐号：<span class="m-important">*</span>
                    </label>

                    <div class="col-sm-4">
                        <#if (business.id)??>
                            <input type="hidden" value="${(business.id)!'0'}" name="id" data-type="number">
                        </#if>
                        
                        <#if isAdd == false>
                            <label>${business.businessAccount}</label>
                            <input type="text" class="form-control" value="${(business.businessAccount)!''}" name="businessAccount" style="display:none;"/>
                        <#else>
                            <input type="text" maxLength=50 class="form-control" value="${(business.businessAccount)!''}" name="businessAccount" data-required="true" data-pattern="^[a-z0-9A-Z-._@]*$" placeholder="请输入帐号" data-message="不支持汉字和特殊符号"/>
                        </#if>
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        密码：<span class="m-important">*</span>
                    </label>

                    <div class="col-sm-4">
                    	<#-- 编辑的时候密码默认给6个* 可以为空 -->
                        <input type="password" class="form-control" <#if isAdd == false> value="******" data-required="false"<#else> data-required="true"</#if> minLength=6 maxLength=20  id="bussinessPassword" name="password" placeholder="请输入密码，至少6位" data-message="密码至少6位"/>
                    </div>
                </div>
                
                  
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                    供应商类型：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4" id="businessTypeCheck">                 
                        <label class="radio-inline">
                            <input type="radio" name="type" value="0" <#if (business?? == false)||(business??&&(business.type==0))>checked="checked"</#if>>生产厂家
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="type" value="1" <#if (business??&&(business.type==1))>checked="checked"</#if>>代理商
                        </label>                                
                        <label class="radio-inline">
                            <input type="radio" name="type" value="2" <#if (business??&&(business.type==2))>checked="checked"</#if>>综合批发商
                        </label>  
                        <label class="radio-inline">
                            <input type="radio" name="type" value="3" <#if (business??&&(business.type==3))>checked="checked"</#if> id="franchisingCheck">特许经营
                        </label>                                  
                    </div>  
                </div>
                  	
              	<!-- 默认不显示，type==3显示-->
              	<div class="form-horizontal hidden" id="groupFranchising">
              			<div class="form-group">
            				<label class="col-sm-2 control-label">指定用户：<span class="m-important">*</span></label>
            				<!--<div class="col-sm-4">
            					<input type="text" class="form-control userListIpnut" id="searchUserInput" placeholder="请输入关键字查找用户帐号"/>
            					<dl class="userList-normal form-control" id="searchUserList"></dl>
            				</div>-->
            				<!--<a class="btn btn-link col-sm-1">导入模板</a>-->
            				<div class="col-sm-4">
            				<#if isAdd == false>
            				    <span class="btn btn-primary" id="manageUser">管理</span>
            				<#else>
            				    <span style="height:34px;line-height:34px;">指定用户可在商家创建完成后进行管理操作</span>
            				</#if>
            				</div>
            			</div>
            			
            			<!--<div class="form-group">
            				<label class="col-sm-2 control-label"></label>
            				<div class="col-sm-4">
            					<ul class="selectedUserList" id="selectedUserList">
            						<#if business?? && business.busiUserRelations??>
              							<#list business.busiUserRelations as user>
              								<li>
              									<label>${(user.userName)!''}</label>
              									<a class="btn btn-link j-flag" data-id="${(user.id)!''}" data-userId="${(user.userId)!''}" data-userName="${(user.userName)!''}">删除</a>
              								</li>
              							</#list>
              						</#if>
            					</dl>
            				</div>
            			</div>-->
             	</div>
              
                <div class="form-group">
                 	<label class="col-sm-2 control-label">
                    经营类别：<span class="m-important">*</span>
                  	</label>
                  	<div class="col-sm-8">
                  	<div class="dropdown" id="manageTypeDropdowMenu" data-type="${(business.manageType)!''}" data-message="请选择经营类别">
  						<button class="btn btn-default dropdown-toggle bussinessManageTypeButton" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-expanded="true"><span class="caret"></span>
  						</button>
						<ul class="dropdown-menu bussinessManageTypeMenu" role="menu" id="dropdownMenuList">
							<#list manageType as type>
                			<div class="checkbox"><label><input class="j-flag" name="manageType" type="checkbox" value=${type_index} />${type}</label></div>
         					</#list>
						</ul>
						<div>
						    <span class="js-error" id="manageTypeError"></span>
						</div>
					</div>
					</div>
                </div>
                <!--
                <div class="form-group">
                  <label class="col-sm-2 control-label">
                        经营品牌：<span class="m-important">*</span>
                  </label>
                  <div class="col-sm-4">
                    <select class="form-control" name="actingBrandId">
                        <#if brandList??>
                    	<#list brandList as brand>
              				<option value="${brand.brandId}" <#if (business??)&&(business.actingBrandId == brand.brandId)>selected="selected"</#if>>${brand.brandNameZh}</option>
        				</#list>
                        </#if>
                    </select>
                  </div>
                </div>-->
                
                <div class="form-group">
                  <label class="col-sm-2 control-label">配送区域：<span class="m-important">*</span></label>
                  <div class="row col-sm-10" id="districtForm">
                      <#if business??&&business.sendDistrictDTOs??&&(business.sendDistrictDTOs?size>0)>
                      <#list business.sendDistrictDTOs as district>
                      	  <#if district.edit>
                          <div class="form-group"><div class="j-address col-sm-12" data-id="${(district.id)!''}" data-provinceId="${(district.provinceId)!''}" data-cityId="${(district.cityId)!''}" data-districtId="${(district.districtId)!''}"></div></div>
                          <#else>
                          <div class="form-group"><label class="j-district col-sm-2" data-id="${(district.id)!''}" data-provinceId="${(district.provinceId)!''}" data-cityId="${(district.cityId)!''}" data-districtId="${(district.districtId)!''}">${district.districtName!''}</label></div>
                          </#if>
                      </#list>
                      <#else>
                          <div class="form-group"><div class="j-address col-sm-12"></div></div>
                      </#if>
                  </div>
                </div>

               	<div class="form-group">
                  <label class="col-sm-2 control-label"></label>
                    <div class="col-sm-2">	
                      <label class="btn btn-link" id="districtAdd">+ 添加配送区域</label>  
                    </div>
                    <input type="hidden" name="sendDistrictDTOs"/>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">
                       首页权重：<span class="m-important">*</span>
                  </label>
                  <div class="col-sm-1">
                    <input type="text" maxLength=6 class="form-control" value="${(business.indexWeight)!0}" name="indexWeight" data-required="true" data-type="number"/>
                  </div>
                  <div class="col-sm-4" style="line-height:32px;">该权重值用于首页上商家的推荐商品楼层的排序，权重值越大，楼层越靠前。</div>
                </div>                
                <div class="form-group">
                  <label class="col-sm-2 control-label">
                       公司名称：<span class="m-important">*</span>
                  </label>
                  <div class="col-sm-4">
                    <input type="text" maxLength=20 class="form-control" value="${(business.companyName)!''}" name="companyName" data-required="true" placeholder="请输入公司名称" data-message="请输入公司名称"/>
                  </div>
                </div>
                    
                <div class="form-group">
                    <label class="col-sm-2 control-label">公司介绍：</label>

                    <div class="col-sm-4">
                        <textarea class="form-control" name="companyDesc" rows="5" draggable="false" placeholder="请输入公司介绍">${(business.companyDesc)!''}</textarea>
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        注册资金：<span class="m-important">*</span>
                    </label>
                    
                    <div class="form-group">
                        <div class="col-sm-2">
                      	 <input type="text" class="form-control" maxLength=20 value="${(business.registerFund)!''}" name="registerFund" data-required="true" data-pattern="^\d*$" placeholder="请输入注册资金">
                        </div>
                        <div class="col-sm-1"><div class="sep">元</div></div>
                    </div>
                </div>
                
                
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        法人姓名：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                        <input type="text" maxLength=20 class="form-control" value="${(business.legalPerson)!''}" name="legalPerson" data-required="true" placeholder="请输入法人姓名" data-message="请输入法人姓名"/>
                    </div>
                </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        开户人姓名：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" maxLength=20 class="form-control" value="${(business.holderName)!''}" name="holderName" data-required="true" placeholder="请输入开户姓名" data-message="请输入开户姓名"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        店铺名称：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" maxLength=20 class="form-control" value="${(business.storeName)!''}" name="storeName" data-required="true" placeholder="请输入店铺名称" data-message="请输入店铺名称"/>
                    </div>
                  </div>
                  
                    <div class="form-group">
                    <label class="col-sm-2 control-label">
                        起批金额：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <#if (business.id)??>
                      	<input type="text" maxLength=6 class="form-control" value="${(business.batchCash)!''}" name="batchCash" data-required="true" placeholder="起批金额" data-pattern="^\d+$"  data-message="请输入起批金额"/>
                      <#else>
                      	<input type="text" maxLength=6 class="form-control" value="0" name="batchCash" data-required="true" placeholder="起批金额" data-pattern="^\d+$" data-message="请输入起批金额"/>
                      </#if>
                    </div>
                  </div>
                  
                  <!--
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        法人证件号<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" value="${(business.legalPersonID)!''}" name="legalPersonID" data-pattern="^\s*\S+\s*$" placeholder="请输入开户人证件号"/>
                    </div>
                  </div>
                  -->
                  
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        开户人身份证号：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" value="${(business.holderID)!''}" name="holderID" data-pattern="(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)" maxLength="18" placeholder="请输入开户人身份证号" data-message="请输入开户人身份证号"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        身份证扫描件：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.holderIDPositiveImg)??>
                          <a href="${business.holderIDPositiveImg}" data-lightbox="holderIDPositiveImg">
                            <img src="${business.holderIDPositiveImg}">
                          </a>
                        </#if>
                        <label class="j-img btn btn-link" data-name="holderIDPositiveImg">上传正面</label>
                      </div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.holderIDPositiveImg)!''}" name="holderIDPositiveImg" data-required="true" data-message="请上传开户人证件扫描件正面"/>
                    </div>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.holderIDNegativeImg)??>
                          <a href="${business.holderIDNegativeImg}" data-lightbox="holderIDNegativeImg">
                            <img src="${business.holderIDNegativeImg}">
                          </a>
                        </#if>
                      <label class="j-img btn btn-link f-mgl" data-name="holderIDNegativeImg">上传反面</label></div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.holderIDNegativeImg)!''}" name="holderIDNegativeImg" data-required="true" data-message="请输入开户人证件扫描件反面"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        营业执照编号：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" value="${(business.registrationNumber)!''}" name="registrationNumber" data-required="true"  maxLength="20" placeholder="请输入营业执照编号" data-message="请输入营业执照编号"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        营业执照有效期<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <div class="j-datepick" data-name="registrationNumberStart" data-value="${(business.registrationNumberStart)!''}"></div>
                    </div>
                    <div class="col-sm-2">
                    	<div class="sep">至</div>
                      	<div class="j-datepick" data-name="registrationNumberEnd" data-value="${(business.registrationNumberEnd)!''}"></div>
                    </div>
                    <div class="col-sm-2">
                    	<div class="sep">或</div>
                      	<input type="checkbox" <#if business?? && business.registrationNumberAvaliable == 1>checked="checked"</#if> name="registrationNumberAvaliable" class="" id="registrationNumberAvaliable"><label class="control-label" for="registrationNumberAvaliable">长期有效</label>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        营业执照扫描件：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.registrationImg)??>
                          <a href="${business.registrationImg}" data-lightbox="registrationImg">
                            <img src="${business.registrationImg}">
                          </a>
                        </#if>
                      <label class="j-img btn btn-link" data-name="registrationImg">上传正本</label></div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.registrationImg)!''}" name="registrationImg" data-required="true" data-message="请上传企业营业执照扫描件正本"/>
                    </div>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.registrationCopyImg)??>
                          <a href="${business.registrationCopyImg}" data-lightbox="registrationCopyImg">
                            <img src="${business.registrationCopyImg}">
                          </a>
                        </#if>
                      <label class="j-img btn btn-link f-mgl" data-name="registrationCopyImg">上传副本</label></div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.registrationCopyImg)!''}" name="registrationCopyImg" data-required="true" data-message="请上传企业营业执照扫描件副本"/>
                    </div>
                  </div>
                  
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        银行开户证明扫描件：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.accountLicense)??>
                          <a href="${business.accountLicense}" data-lightbox="accountLicense">
                            <img src="${business.accountLicense}">
                          </a>
                        </#if>
                        <label class="j-img btn btn-link" data-name="accountLicense">上传照片</label>
                      
                      </div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.accountLicense)!''}" name="accountLicense" data-required="true" data-message="请上传银行开户证明扫描件"/>
                    </div>
                  </div>
                  
                <!--   <div class="form-group" id="brandAuthImg">
                    <label class="col-sm-2 control-label">
                        品牌使用授权扫描件：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.brandAuthImg)??>
                          <a href="${business.brandAuthImg}" data-lightbox="brandAuthImg">
                            <img src="${business.brandAuthImg}">
                          </a>
                        </#if>
                      <label class="j-img btn btn-link" data-name="brandAuthImg">上传照片</label></div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.brandAuthImg)!''}" name="brandAuthImg" data-required="true" data-message="请上传品牌使用授权扫描件"/>
                    </div>
                  </div>-->
                  
                 <!-- <div class="form-group <#if business??&&business.type==1>f-dn</#if>" id="brandImg">
                    <label class="col-sm-2 control-label">
                        商标注册登记证扫描件<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <div class="m-imgbox">
                        <#if (business.brandImg)??>
                          <a href="${business.brandImg}" data-lightbox="brandImg">
                            <img src="${business.brandImg}">
                          </a>
                        </#if>
                      <label class="j-img btn btn-primary" data-name="brandImg">上传</label></div>
                      <input type="hidden" maxLength=50 class="form-control" value="${(business.brandImg)!''}" name="brandImg" data-required="true" placeholder="请上传品牌使用授权扫描件"/>
                    </div>
                  </div>-->
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                        联系人姓名：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" maxLength=20 value="${(business.contactName)!''}" name="contactName" data-required="true" placeholder="请输入联系人姓名" data-message="请输入联系人姓名"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                      联系手机：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" value="${(business.contactTel)!''}" name="contactTel" data-required="true" data-pattern="^0?(13|14|15|17|18)[0-9]{9}$" placeholder="请输入11位手机号码" data-message="请输入11位手机号码"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                      联系邮箱：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" maxLength=20 class="form-control" value="${(business.contactEmail)!''}" name="contactEmail" data-required="true" data-type="email" placeholder="请输入常用邮箱地址" data-message="请输入常用邮箱地址"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                      联系地址：
                    </label>
                    <div class="col-sm-2">
                      <select name="contactProvince" class="form-control" data-default="${(business.contactProvince)!''}"></select>
                    </div>
                    <div class="col-sm-2">
                      <select name="contactCity" class="form-control" data-default="${(business.contactCity)!''}"></select>
                    </div>
                    <div class="col-sm-2">
                      <select name="contactCountry" class="form-control" data-default="${(business.contactCountry)!''}"></select>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                  
                    </label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" name="contactAddress"  value="${(business.contactAddress)!''}" maxLength="30" placeholder="请输入乡、镇、街道等详细信息">
                    </div>
                  </div>
                  <!--
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                    退货联系人：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" maxLength=50 class="form-control" value="${(business.returnContactName)!''}" name="returnContactName" data-required="true" data-message="请输入退货联系人"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                     退货联系手机：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" value="${(business.returnContactTel)!''}" name="returnContactTel" data-required="true" data-pattern="^0?(13|14|15|17|18)[0-9]{9}$" data-message="请输入正确的手机号"/>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                      退货地址：<span class="m-important">*</span>
                    </label>
                    <div class="col-sm-2">
                      <select name="returnProvince" class="form-control" data-default="${(business.returnProvince)!''}"></select>
                    </div>
                    <div class="col-sm-2">
                      <select name="returnCity" class="form-control" data-default="${(business.returnCity)!''}"></select>
                    </div>
                    <div class="col-sm-2">
                      <select name="returnCountry" class="form-control" data-default="${(business.returnCountry)!''}"></select>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label class="col-sm-2 control-label">
                  
                    </label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" name="returnAddress"  value="${(business.returnAddress)!''}" data-required="true" data-message="请输入具体地址">
                    </div>
                    
                  </div>-->
                  
                </form>
              </div>
              <div class="m-btns f-tac">
                <span class="btn btn-primary" id="submit">提交</span><a class="btn btn-primary f-mgl" href="/business/account" >取消</a>
              </div>
            </div>
        </div>
    </div>
</div>
</div>
</@wrap>
<script type="text/javascript">
        window.businessId = ${(business.id)!0};
</script>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/business/create.js?v=1.0.0.2"></script>

</body>
</html>