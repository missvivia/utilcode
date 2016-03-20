<#include "var.ftl">

<#-- 类似于JSON.stringify功能，用于将freemarker object 传入 js object -->
<#function stringify obj>


  <#if !obj??>
    <#return 'undefined'>
  </#if>
  <#if obj?is_enumerable>
    <#local str = '['>
    <#list obj as x>
      <#local str = str + stringify(x)>
      <#if x_has_next>
        <#local str = str + ','>
      </#if>
    </#list>
    <#return str + ']'>
  </#if>
  <#if obj?is_hash || obj?is_hash_ex>
    <#local str = '{'>
    <#local arr = []>
    <#local keys = obj?keys>
    <#list keys as x>
      <#if x!='class' && x!= 'declaringClass' && obj[x]?? && !(obj[x]?is_method)>
        <#local arr = arr + [x]>
      </#if>
    </#list>
    <#list arr as x>

      <#local str = str + x + ':' + stringify(obj[x]) >
      <#if x_has_next>
        <#local str = str + ','>
      </#if>
    </#list>
    <#return str + '}'>
  </#if>
  <#if obj?is_date>
    <#return '"' + obj?string("yyyy-MM-dd HH:mm:ss") + '"'>
  </#if>
  <#if obj?is_boolean || obj?is_number>
    <#return obj?string>
  </#if>
  <#if obj?is_string>
    <#return '"' + obj?js_string + '"'>
  </#if>
  <#return ''>
</#function>

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
    <img src="/res/images/logo-xsm.png" alt="百万店-运营平台">
  </a>
  <h4>新商盟百万店 - 运营平台</h4>
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
          <span class="glyphicon glyphicon-chevron-<#if pageName1?? && pageName1==p.name>up<#else>down</#if> pull-right"></span>
        </a>
        <ul class='nav'>
          <#if p.children??>
          <#list p.children as child>
            <li class='<#if pageName2?? && pageName2==child.name>active</#if>'>
              <a href="${safeGet(child, 'link', link + '/' + child.name)}" >${child.title}</a>
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



<#macro wrap >
<div class="g-bd col-sm-10">
  <!-- 顶栏 -->
  <div class="row m-header">
    
  </div>
  <#nested>
</div>
<script type="text/javascript">
	<!--window.token=${('sdf')!''};-->
	window.token='${(formToken)!''}';
</script>
</#macro>

<#macro crumbs parent sub>
<!-- 面包削 -->
  <div class="row">
    <div class="col-sm-12">
      <div class="m-card m-card-bread">
        <ul class="breadcrumb">
          <li><a href="/index"><span class="glyphicon glyphicon-home"></span></a></li>
          <#if parent??&&parent.url!=''>
          <li><a href="${parent.url!'#'}">${parent.txt}</a></li>
          </#if>
          <li class="active">${sub.txt}</li>
        </ul>
      </div>
    </div>
  </div>
</#macro>
<#macro crumbs1 crumbList>
<!-- 面包削 -->
  <div class="row">
    <div class="col-sm-12">
      <div class="m-card m-card-bread">
        <ul class="breadcrumb">
          <li><a href="/index"><span class="glyphicon glyphicon-home"></span></a></li>
          <#list crumbList as item>
	          <#if item_has_next>
	          <li><a href="${item.url!'#'}">${item.txt}</a></li>
	          <#else>
	          <li class="active">${item.txt}</li>
	          </#if>
          </#list>
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


<#-- 模块容器封装 -->
<#macro module title="" class="">
  <div class="row">
    <div class="col-sm-12">
      <div class="m-card">
        <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>${title}</h2>
        <div class="card_c ${class}" id="module-box">
          <#nested>
        </div>
      </div>
    </div>
  </div>
</#macro>
<#-- 查询条件UI结构封装 -->
<#macro rtTypeSelect key>
  <select class="form-control" name="${key}">
    <option value="0">全部</option>
    <option value="1">一退</option>
    <option value="2">二退</option>
    <option value="3">三退</option>
  </select>
</#macro>
<#macro rtBillSelect key>
  <select class="form-control" name="${key}">
    <option value="0">未生成</option>
    <option value="1">已生成</option>
  </select>
</#macro>
<#macro rtStateSelect key value selected>
  <select class="form-control" name="${key}">
  	<option value="0">全部</option>
    <#list value as item>
    	<option value="${item.code}" <#if item.code==selected>selected</#if> >${item.desc}</option>
    </#list>
  </select>
</#macro>
<#macro siteSelect key>
  <select class="form-control" name="${key}">
    <option value="0">全部</option>
    <#if provinceList??>
	    <#list provinceList as it>
	    <option value="${it.id}">${it.name}</option>
	    </#list>
    <#else>
	    <#list province as it>
	    <option value="${it.id}">${it.name}</option>
	    </#list>
    </#if>
    
  </select>
</#macro>
<#macro categorySelect key>
  <select class="form-control" name="${key}">
    <option value="0">全部</option>
    <#list categoryList as it>
    <option value="${it.id}">${it.name}</option>
    </#list>
  </select>
</#macro>
<#macro multCategorySelect class="" keys=[] values=[]>
  <script>
      window.category = ${stringify(categoryList)};
  </script>
  <#if class==""><#local class="col-sm-2"></#if>
  <div class="${class}"><select class="form-control j-cate" name="${keys[0]!"level0"}" data-value="${values[0]!""}"></select></div>
  <div class="${class}"><select class="form-control j-cate" name="${keys[1]!"level1"}" data-value="${values[1]!""}"></select></div>
  <div class="${class}"><select class="form-control j-cate" name="${keys[2]!"level2"}" data-value="${values[2]!""}"></select></div>
</#macro>
<#macro stateSelect key>
  <select class="form-control" name="${key}">
    <option value="0">全部</option>
    <option value="1">待审核</option>
    <option value="2">审核通过</option>
    <option value="3">审核未通过</option>
  </select>
</#macro>
<#macro dateSelect key value time="0">
  <div class="col-sm-2 j-datepick" data-name="${key}" data-value="${value}" data-time="${time}"></div>
</#macro>
<#macro ftTimeSelect from={} to={}>
  <div class="col-sm-2 j-datepick" data-name="${from.key}" data-value="${from.value}"></div>
  <div class="col-sm-2 j-datepick" data-name="${to.key}" data-value="${to.value}" data-time="23:59:59">
    <span class="sep">至</span>
  </div>
</#macro>
<#macro searchButton margin=true>
  <div class="col-sm-2 <#if margin> col-sm-offset-2</#if>">
    <button class="btn btn-primary btn-block" name="btn-submit">查询</button>
  </div>
</#macro>
<#-- 
  单项配置信息：
  key   - 标识，对应数据提交字段名称
  name  - 显示名称
  type  - 类型，默认为text，预置SITE/STATE/FROMTO/CATE/BTN/MCATE/RTYPE/RSTATE
  items - 对于type是select的字段提供选择项列表，如[{"text":"是","value":"1"}]
  from  - 对于type是FROMTO类型的提供起始时间配置，如{"key":"s","value":"2014-12-15"}
  to    - 同from
-->
<#macro searchForm id="search-form" filters=[]>
  <form class="form-horizontal" role="form" id="${id}">
    <#list filters as items>
    <div class="form-group">
      <#list items as item>
        <#local type = (item.type)!"text">
        <#if type=="BTN">
          <@searchButton margin=item.margin/>
        <#else>
          <label class="col-sm-1 control-label">${item.name}：</label>
          <#if type=="FROMTO">
            <@ftTimeSelect from=item.from to=item.to/>
          <#elseif type=="MCATE">
            <@multCategorySelect class=item.class keys=item.keys values=item.values/>
          <#else>
            <div class="col-sm-2">
            <#switch type>
              <#case "SITE">
                <@siteSelect key=item.key/>
              <#break>
              <#case "STATE">
                <@stateSelect key=item.key/>
              <#break>
              <#case "CATE">
                <@categorySelect key=item.key/>
              <#break>
              <#case "RTYPE">
                <@rtTypeSelect key=item.key/>
              <#break>
              <#case "RBILL">
                <@rtBillSelect key=item.key/>
              <#break>
              
              <#case "RSTATE">
                <@rtStateSelect key=item.key value=item.value selected=item.selected!/>
              <#break>
              <#case "select">
                <select class="form-control" name="${item.key}">
                  <#list item.items as it>
                    <option value="${(it.value)!it.text}">${it.text}</option>
                  </#list>
                </select>
              <#break>
              <#case "WHSELECT">
                <select class="form-control" name="${item.key}">
                  <option value="0">全部</option>
                  <#list item.items as it>
                    <option value="${it.warehouseId}">${it.warehouseName}</option>
                  </#list>
                </select>
              <#break>
              <#default>
                <input type="text" name="${item.key}" class="form-control"/>
              <#break>
            </#switch>
            </div>
          </#if>
        </#if>
      </#list>
    </div>
    </#list>
  </form>
  <div class="hr-dashed">&nbsp;</div>
</#macro>