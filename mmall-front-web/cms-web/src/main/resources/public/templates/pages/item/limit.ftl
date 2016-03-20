<#assign pageName="item-limit"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <link rel="stylesheet" type="text/css" href="/src/css/page/limit.css">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"商品管理","url":'#'} sub={"txt":"商品限购"}/>
<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
       	商品限购管理
      </h2>
      <div class="card_c limitBox">
      	  <form class="" id="search-form">
      		  <div class="form-inline">
				  <div class="form-group">
				      <label for="queryType">用户名：</label>
				      <input type="text" class="form-control" id="userName" />
				  </div>
				  <div class="form-group">
				    <label for="searchKey">商品SKU：</label>
				    <input type="text" class="form-control" id="skuId" />
				  </div>
				  <div class="form-group">
				    <input type="button" value="查询" class="btn btn-primary j-flag" name="btn-search" id="searchBtn"/>
				  </div>
		      </div>
	     </form>
	     <div class="hr-dashed"></div>
	     <div class="row">
	        <div id="limitList"></div>
         </div>
      </div>
      
    </div>
  </div>
</div>
</@wrap>
<script id="limitListTpl" type="text/regular" name='orderListTpl'>
	<div class="col-sm-12">
		{{#list list as item}}
	        <div class="userName"><img src="/res/images/defaulthead.png" />&nbsp;&nbsp;{{userName}}</div>
	        <div class="productList clearfix b-d">
	            <div class="storeName">{{item.skuInfo.storeName}}</div>
	            <div>
	                <div class="productInfo">
		                <img src="{{item.skuInfo.picUrl}}" />
		                <div class="proDetail">
		                     <p class="title">{{item.skuInfo.skuName}}</p>
		                     <p class="sub_title">{{item.skuInfo.skuTitle}}</p>
		                     <p class="price">￥ {{item.skuInfo.priceList[0].prodPrice}}</p>
		                </div>
		            </div>
		            {{#if item.skuLimitConfig}}
		            <div class="limitInfo">
		                <p>限购时间：{{item.skuLimitConfig.limitStartTime|format}} 到 {{item.skuLimitConfig.limitEndTime|format}}</p>
		                <p>限购循环：每{{item.skuLimitConfig.limitPeriod}}天</p>
		                <p>限购数量：{{item.skuLimitConfig.skuLimitNum}}件</p>
		            </div>
		            {{#else}}
		            <div class="limitInfo">
		                <p>&nbsp;</p>
		                <p>暂无限购信息</p>
		                <p>&nbsp;</p>
		            </div>
		            {{/if}}
	            </div>
	        </div>
	        {{#if item.skuLimitRecord}}
	             <div class="setLimit-tit">限购设置</div>
		         <div class="setLimit b-d" id="setLimitPrimary">
		              <div class="pur-num-area">
		                  <span>当前已购数量（件）：{{item.skuLimitRecord.buyNum}}</span>
		                  <input type="button" value="查看订单" class="btn btn-primary j-flag" id="searchOrder" on-click={{this.searchOrder(item)}}/>
		              </div>
		              <div class="num-left-area">
		                  <div class="inline">
		                                                         剩余可购数量（系统缓存值）：{{item.skuLimitConfig.skuLimitNum-item.skuLimitRecord.buyNum}}
		                      {{#if item.skuLimitRecord.buyCacheNum > 0}}
		                                                                                                   （<span id="cacheNum">{{item.skuLimitConfig.skuLimitNum-item.skuLimitRecord.buyCacheNum}}</span>）
		                      {{#else}}
		                                                                       （<span id="cacheNum">获取缓存失败或不存在</span>）
		                      {{/if}}
		                      <span class="tip" title="当剩余可购数量与系统缓存值不一致时，请点击更新">?</span>
		                  </div>
		              </div>
		              <div class="btn-area clearfix">
		                  <input type="button" value="更新" class="btn btn-primary j-flag" id="updateBtn" on-click = {{this.updateCache($event,item)}}/>
		                  <input type="button" value="修改数量" class="btn btn-primary j-flag" id="modifyNumBtn" on-click = {{this.hidePrimary()}}/>
		              </div>
		         </div>
		         <div class="setLimit b-d hide" id="setLimitModify">
		              <div class="pur-num-area">
		                  <span>当前已购数量（件）：{{item.skuLimitRecord.buyNum}}</span>
		              </div>
		              <div class="num-left-area">
		                  <div class="inline">剩余可购数量（件）：<input type="text" class="form-control" id="leftNum" /></div>
		              </div>
		              <div class="btn-area clearfix">
		                  <input type="button" value="确定" class="btn btn-primary j-flag" id="sureBtn" on-click={{this.modifyNum($event)}}/>
		                  <input type="button" value="取消" class="btn btn-primary j-flag" id="cancelBtn" on-click={{this.cancelModify()}}/>
		              </div>
		         </div>
	        {{/if}}
	    {{/list}}
        <div id="orderList"></div>
    </div>
    
</script>
<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/item/limit.js?v=1.0.0.0"></script>
</body>
</html>