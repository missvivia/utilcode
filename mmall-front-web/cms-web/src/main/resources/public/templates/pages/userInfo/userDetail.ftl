<#assign pageName="userInfo-userOrder"/>
<#assign platMap = {"0" : "运营","1" : "商家","2" : "主站","3" : "手机","4" :  "wap","5" :  "ERP","6" : "地推"}>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/userinfo.css?v=1.0.0.0">
  <style type="text/css">
       .tab-table tr td{
           padding-right:30px;
       }
       .tab-table tr td.active .btn{
           color: #fff;
		   background-color: #3276d6;
		   border-color: #3276d6;
	    }
           
  </style>
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"用户管理","url":'#'} sub={"txt":"用户详情"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	<label>用户详情</label>
       	<a href="/userInfo/userList">返回</a>
      </h2>
      <div class="card_c">
            <div class="m-row">
                <div id="tab-box"></div>
            </div>
	        <div class="m-row f-db" id="baseInfo">
	          <div class='m-user'>
				 <div class="tit"><label>个人资料</label><a href="javascript:void(0);" id="updateinfo">修改</a></div>
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>用户名</th>
								<th>用户ID</th>
								<th>开户来源</th>
								<th>用户类型</th>
								<th>绑定手机</th>
								<th>绑定邮箱</th>
								<th>昵称</th>
								<th>性别</th>
								<th>生日</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>${baseInfo.account}</td>
								<td>${baseInfo.uid}</td>
								<td><#if baseInfo.platType gte 0 && baseInfo.platType lte 6>${platMap["${baseInfo.platType}"]}</#if></td>
								<td><#if baseInfo.userType == 0>普通用户<#else>中烟用户</#if></td>
								<td>${(baseInfo.mobileNumber)!''}</td>
								<td>${(baseInfo.emailAddress)!''}</td>
								<td>${(baseInfo.nick)!''}</td>
								<td><#if baseInfo.sex==0>男<#else>女</#if></td>
								<td>${(baseInfo.birth)!''}</td>
							</tr>
						</tbody>
						<tfoot>
							<tr><td colspan="9">
								<div class="f-fl"><b>上次登录IP:</b>
								<#if baseInfo.lastLoginIp?exists>
									<label>${baseInfo.lastLoginIp}</label>&nbsp;&nbsp;
								</#if>
								</div>
							    <div class="f-mgl f-fl"><b>上次登录所属区域:</b>
							    <#if baseInfo.lastLoginAddress?exists>
							    	<label>${baseInfo.lastLoginAddress}</label>
							    </#if>
							    </div>
							</td>
							</tr>
						</tfoot>
					</table>
				</div>
	        </div>
	        <div class="m-row f-db" id="addressInfo">
	        	<div class="m-user">
				    <div class="tit"><label>收货地址</label><a href="javascript:void(0);" id="updateAddress">修改</a></div>
				    <div class="cnt">
				        <table class="table table-bordered">
				            <thead>
				                <tr>
				                    <th>收货人姓名</th>
				                    <th>收货地址</th>
				                    <th>邮编</th>
				                    <th>收货人电话</th>
				                    <th></th>
				                </tr>
				            </thead>
				            <tbody>
				            	<#if consigneeAddress??>
				            		
				            		<#list consigneeAddress as item>
						                <tr>
						                    <td>${item.consigneeName}</td>
						                    <td>${item.province}${item.city}${item.section}${item.street}${item.address}</td>
						                    <#if item.consigneeAddress.zipcode?exists>
						                    <td>${item.consigneeAddress.zipcode}</td>
						                    <#else>
						                    <td></td>
						                    </#if>
						                    <td>${item.consigneeMobile}</td>
						                    <td><#if item.defaultAddress>默认</#if></td>
						                </tr>
				                	</#list>
				            	<#else>
				                	<tr><td colspan="5">还没有添加地址信息</td></tr>
				                </#if>
				            </tbody>
				        </table>
				    </div>
				</div>
	        </div>
	        <div class="m-row f-dn" id="couponInfo"></div>
	        <div class="m-row f-dn" id="orderlist"></div>
      </div>
    </div>
    <div class="row">
	    <div class="col-sm-8 col-sm-offset-2">
	      <div class="m-card-btn m-card">
	        <#if baseInfo.isActive == 1>
	        <div class="cbtn_left"><a href="javascript:void(0);" class="btn  btn-primary j-submit" id="lockUser">冻结</a></div>
	        <#else>
	        <div class="cbtn_left"><a href="javascript:void(0);" class="btn  btn-primary j-submit" id="unlockUser">已冻结,解冻账户</a></div>
	        </#if>
	        <div class="cbtn_right"><a href="/userInfo/userList" class="btn btn-default">返回</a></div>
	      </div>
	    </div>
    </div>
  </div>
</div>
<form id="userinfo">
	<input type="hidden" name="uid" value="${baseInfo.uid}"/>
	<input type="hidden" name="account" value="${baseInfo.account}"/>
	<input type="hidden" name="mobileNumber" value="${(baseInfo.mobileNumber)!''}"/>
	<input type="hidden" name="emailAddress" value="${(baseInfo.emailAddress)!''}"/>
	<input type="hidden" name="nick" value="${(baseInfo.nick)!''}"/>
	<input type="hidden" name="userType" value="${baseInfo.userType}"/>
	<input type="hidden" name="sex" value="${baseInfo.sex}"/>
	<input type="hidden" name="birth" value="${baseInfo.birth}"/>
	<#if baseInfo.password?exists>
	<input type="hidden" name="password" value="${baseInfo.password}"/>
	<#else>
	<input type="hidden" name="password" value=""/>
	</#if>
	<input type="hidden" name="licence" value="${(baseInfo.licence)!''}"/>
</form>
<form id="addrinfo">
<#if consigneeAddress??>
	<#list consigneeAddress as item>
		<#if  item_index == 0>
			<#if item.province?exists>
				<input type="hidden" name="province" value="${item.province}"/>
				<input type="hidden" name="provinceId" value="${item.consigneeAddress.provinceId}"/>
				<input type="hidden" name="city" value="${item.city}"/>
				<input type="hidden" name="cityId" value="${item.consigneeAddress.cityId}"/>
				<input type="hidden" name="section" value="${item.section}"/>
				<input type="hidden" name="sectionId" value="${item.consigneeAddress.sectionId}"/>
				<input type="hidden" name="street" value="${item.street}"/>
				<input type="hidden" name="streetId" value="${item.consigneeAddress.streetId}"/> 
			<#else>
				<input type="hidden" name="province" value=""/>
				<input type="hidden" name="provinceId" value=""/>
				<input type="hidden" name="city" value=""/>
				<input type="hidden" name="cityId" value=""/>
				<input type="hidden" name="section" value=""/>
				<input type="hidden" name="sectionId" value=""/>
				<input type="hidden" name="street" value=""/>
				<input type="hidden" name="streetId" value=""/>	
			</#if>
			<input type="hidden" name="address" value="${item.address}"/>
			<input type="hidden" name="consigneeName" value="${item.consigneeName}"/>
			<input type="hidden" name="consigneeMobile" value="${item.consigneeMobile}"/>
			<input type="hidden" name="consigneeTel" value="-"/>
			<input type="hidden" name="id" value="${item.consigneeAddress.id}"/>
			<#if item.consigneeAddress.zipcode?exists>
			<input type="hidden" name="zipcode" value="${item.consigneeAddress.zipcode}"/>
			<#else>
			<input type="hidden" name="zipcode" value=""/>
			</#if>
		</#if>
	</#list>
<#else>
	<input type="hidden" name="province" value=""/>
	<input type="hidden" name="provinceId" value=""/>
	<input type="hidden" name="city" value=""/>
	<input type="hidden" name="cityId" value=""/>
	<input type="hidden" name="section" value=""/>
	<input type="hidden" name="sectionId" value=""/>
	<input type="hidden" name="street" value=""/>
	<input type="hidden" name="streetId" value=""/>
	<input type="hidden" name="address" value=""/>
	<input type="hidden" name="consigneeName" value=""/>
	<input type="hidden" name="consigneeMobile" value=""/>
	<input type="hidden" name="consigneeTel" value=""/>
	<input type="hidden" name="zipcode" value=""/>
</#if>
</form>
</@wrap>
<#noparse>
<script id="orderListTpl" type="text/regular" name='orderListTpl'>
<div class="m-user">
    <div class="tit">订单信息</div>
    <div class="m-order">
        <table class="table table-bordered">
        	<thead>
                <tr>
                    <th>订单号</th>
                    <th>订单金额(元)</th>
                    <th>订单状态</th>
                    <th>下单时间</th>
                </tr>
            </thead>
            <tbody>
                {{#if !list}}
            		<tr><td colspan="4">还没有添加订单</td></tr>
            	{{#else}}
                {{#list list as item}}
	                <tr>
	                    <td>{{item.orderId}}</td>
	                    <td>{{item.totalCash}}</td>
	                    <td>
	                    	{{item.orderFormState.desc}}
	                    </td>
	                    <td>{{item.orderDate}}</td>
	                </tr>
                {{/list}}
                {{/if}}
            </tbody>
            <tfoot>
            	<tr>
            		<td colspan="4">
            			<div class="text-right m-wpager f-cb">
						  <pager total={{Math.ceil(total / limit)}} current={{current}} ></pager>
						</div>	
            		</td>
            	</tr>
            </tfoot>
        </table>
        
    </div>
</div>
</script>
<script id="couponListTpl" type="text/regular" name='couponListTpl'>
<div class="m-user">
    <div class="tit">优惠券</div>
    <div class="cnt">
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>类型</th>
                    <th>内容/金额</th>
                    <th>券号</th>
                    <th>有效时间</th>
                    <th>状态</th>
                </tr>
            </thead>
            <tbody>
                {{#if list.length == 0}}
            		<tr><td colspan="5">还没有添加优惠券信息</td></tr>
            	{{#else}}
            		{{#list list as item}}
		                <tr>
		                    <td>优惠券</td>
		                    <td>
		                    	满{{((item.items)|eval)[0].condition.value}}{{((item.items)|eval)[0].result[0].value}}元
		                    </td>
		                    <td>{{item.couponCode}}</td>
		                    <td>{{item.startTime|format}} 至 {{item.endTime|format}}</td>
		                    <td>{{couponStatus[item.couponState]}}</td>
		                </tr>
                	{{/list}}
                {{/if}}
            </tbody>
            <tfoot>
            	<tr>
            		<td colspan="5">
            			<div class="text-right m-wpager f-cb">
						  <pager total={{Math.ceil(total / limit)}} current={{current}} ></pager>
						</div>	
            		</td>
            	</tr>
            </tfoot>
        </table>
     </div>
</div>
</script>
</#noparse>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/user/userdetails.js?v=1.0.0.4"></script>
</body>
</html>