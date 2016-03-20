<#include "var.ftl">



<#function safeGet test first alt>
  <#if test[first]??>
    <#return test[first] >
  <#else>
    <#return alt >
  </#if>
</#function>

<#macro side pages = pages>
<script src="${jspro}lib/jquery/dist/jquery.js"></script>
<script type="text/javascript">
	function logout(){
		$.ajax({
			url : "/logout",
			type : "get",
			complete : function(data){
				if(data.status == 200){
					location.href = "/login";
				}
			}
		});
	}
</script>
  <div class="g-sd col-sm-2">
  <div class="u-logo">
    <a href="/">
    	<img src="/res/images/logo-xsm.png" alt="新商盟百万店－商家平台">
    </a>
    <h4>新商盟百万店－商家平台</h4>
    <p ><span id="username-ct"></span></p>
    <p><a href="javascript:;" onClick="logout()" class="navbar-link">&nbsp;&nbsp;退出</a> </p>
  </div>
  <div class="m-snav">
    <ul class="nav" id="nav">
      <#list pages as p>
        <#local link = safeGet(p , "link", "/" + p.name) />
        <li class="snav_item  <#if pageName1?? && pageName1==p.name>active</#if>">
          <a href="javascript:void(0);" class="j-firstnav">
            <span class="glyphicon glyphicon-${safeGet(p, 'icon', p.name)}"></span>
            ${p.title}
            <#if pageName2??> 
              <span class="glyphicon glyphicon-chevron-<#if pageName1?? && pageName1==p.name>up<#else>down</#if> pull-right"></span>
            </#if>
          </a>
          <ul class='nav'>
            <#if p.children??>
            <#list p.children as child>
              <li class='<#if pageName2?? && pageName2==child.name>active</#if>'>
                <a href="${safeGet(child, 'link', link + '/' + child.name)}" <#if (link=="/image"&&pageName1??&&pageName1!="image")||link=="/data">target="_blank"</#if>>${child.title}</a>
              </li>
            </#list>
            </#if>
          </ul>
        </li>
      </#list>
    </ul>
  </div>
  </div>
</#macro>
<#macro mSide>
  <div class="m-side">
    <div class="m-tit">
      <a class="btn btn-primary" href="/item/product/create">+新增商品</a>
      <#--<div class="btn-group" id="cardbtn">
           <button type="button" class="btn btn-primary" id="auto-id-1432032698270">批量导入</button>
           <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" id="auto-id-1432032698271">
            <span class="caret"></span>
            <span class="sr-only">Toggel Dropdown</span>
          </button>
          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
            <li><label data-url="/rest/batchUploadProductInfo" id="auto-id-1432032698241" for="1432032698239-0">商品资料</label></li>
            <li><label data-url="/rest/batchUploadPic" id="auto-id-1432032698244" for="1432032698242-0">商品图</label></li>
            <li><label data-url="/rest/batchUploadSizeInfo" id="auto-id-1432032698247" for="1432032698245-0">商品尺码</label></li>
            <li><label data-url="/rest/batchUploadCustomHtml" id="auto-id-1432032698250" for="1432032698248-0">详情HTML模块</label></li>
          </ul>
      </div>-->
    </div>
    <div class="m-nav">
      <ul>
        <li>
          <a href="/item/product/list?type=1&flag=1">全部商品</a><span><#if pageName2?? && pageName2=='product'><#if onlineCount?exists && offlineCount?exists>${onlineCount?int + offlineCount?int}<#elseif !(onlineCount?exists) && !(offlineCount?exists)>0<#elseif offlineCount?exists>${offlineCount?int}<#elseif onlineCount?exists>${onlineCount?int}</#if></#if></span>
        </li>
        <li>
          <a href="/item/product/list?type=2&flag=1">库存不足</a><span><#if pageName2?? && pageName2=='product'><#if stockCount?exists>${stockCount!}<#else>0</#if></#if></span>
        </li>
        <li>
          <a href="/item/product/list?type=3&flag=1">已上架</a><span><#if pageName2?? && pageName2=='product'><#if onlineCount?exists>${onlineCount!}<#else>0</#if></#if></span>
        </li>
        <li>
          <a href="/item/product/list?type=4&flag=1">未上架</a><span><#if pageName2?? && pageName2=='product'><#if offlineCount?exists>${offlineCount!}<#else>0</#if></#if></span>
        </li>
<!--        
        <li>
          <a href="/item/product/list?type=5">审核中</a><span>${pendingCount!}</span>
        </li>
        <li>
          <a href="/item/product/list?type=6">审核未通过</a>
        </li>

        <li>
          <a href="#">商品属性</a>
        </li>
        <li>
          <a href="#">商品规格</a>
        </li>
        -->
      </ul>
    </div>
  </div>
</#macro>

<#macro body>
<div class="g-bd col-sm-10">
<#if pageName1?? && pageName1=='item'>
  <div class="col-sm-2"><@mSide /></div>
</#if>

<div class="col-sm-10">
  <#nested> 
</div>
</div>
<!--[if lte IE 8]> 
<script src="${jspro}lib/bootstrap/respond.min.js"></script> 
<![endif]-->
<script type="text/javascript">
	window.token='${(formToken)!''}';
</script>
</#macro>

<#macro wrap >
<div class="g-bd col-sm-10">
  <#nested>
</div>
<script type="text/javascript">
	window.token='${(formToken)!''}';
</script>
</#macro>

<#macro crumbs parent sub={'txt':''} sub2={'txt':''}>
<!-- 面包削 -->
  <div class="row">
    <div class="col-sm-12">
      <div class="m-card m-card-bread">
        <ul class="breadcrumb">
          <li><a href="/index"><span class="glyphicon glyphicon-home"></span></a></li>
          <#if sub2.txt!=''>
          <li><a href="${parent.url!'#'}">${parent.txt}</a></li>
          <li><a href="${sub.url!'#'}">${sub.txt}</a></li>
          <li class="active">${sub2.txt}</li>
          <#elseif sub.txt!=''>
          <li><a href="${parent.url!'#'}">${parent.txt}</a></li>
          <li class="active">${sub.txt}</li>
          <#else>
          <li class="active">${parent.txt}</li>
          </#if>
        </ul>
        
      </div>
    </div>
  </div>
</#macro>

<#-- <@pager url="/circle?type=${typeId}" limit=10 offset=20 total=60 / >后台传limit offset total过来直接把数字换掉就可 -->
<#macro pager url="" limit=10 offset=0 total=0>
  <#local pageCount=(total/limit)?ceiling />
  <#local currentPage=(offset/limit)?ceiling+1 />
  <#if pageCount!=1>
    <ul class="pagination">
      <li class="pageprv <#if currentPage==1>disabled</#if>"><a href="${url}&limit=${limit}&offset=${(currentPage-2)*limit}">上一页</a></li> 
      <#list 1..pageCount as i>
        <li class="<#if currentPage== i>active</#if>"><a href="${url}&limit=${limit}&offset=${(i-1)*limit}">${i}</a></li>
      </#list>
      <li class="pagenxt <#if currentPage==pageCount>disabled</#if>"><a href="${url}&limit=${limit}&offset=${(currentPage)*limit}">下一页</a></li>  
    </ul>
  </#if>
</#macro>

<!-- @noparse -->
<#macro baiduMap>
  <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=${cfg_develop?string('e2b435804fdaf1de44d3e1fea8382954','e2b435804fdaf1de44d3e1fea8382954')}"></script>
</#macro>
<!-- /@noparse -->
<#include "/wrap/function.ftl" />

