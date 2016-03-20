<#assign jslib = "/src/javascript/lib/nej/src/" />
<#assign jspro ="/src/javascript/"/>

<#assign siteTitle = "商家平台" />
<#assign mainsite = "http://023.baiwandian.cn"/>

<#if pages?? == false>
<#assign pages = [
 {
    "name": "brand", "title": "品牌管理", "icon": "briefcase" ,
    "children": [
      {"name": "display", "title": "品牌介绍页管理"}
    ]
  },
  {
    "name": "product", "title": "商品管理", "icon": "barcode" ,
    "children": [
      {"name": "list", "title": "商品列表"},
      {"name": "edit", "title": "商品新建"},
      {"name": "size", "title": "尺寸模板"}
    ]
  },
  {
    "name": "schedule", "title": "商务管理", "icon": "folder-close",
    "children": [
      {"name": "manage", "title": "档期管理"},
      {"name": "pages", "title": "品购页列表"},
      {"name": "blist", "title": "档期banner管理"}
    ]
  },
  {
    "name": "jit", "title": "JIT管理", "icon": "record",
    "children": [
      <#--{"name": "polist", "title": "po列表"},-->
      {"name": "pkList", "title": "拣货单列表"},
      <#--{"name": "inList", "title": "发货管理"},-->
      {"name": "poreport", "title": "PO单报表"}
      <#--{"name": "phones", "title": "短信接收号码绑定"}-->
    ]
  },
  {
    "name": "supply", "title": "供货管理", "icon": "shopping-cart",
    "children": [
      {"name": "pkList", "title": "商品清单"},
      {"name": "inList", "title": "发货管理"}
    ]
  },
  {
    "name": "sell", "title": "销售管理", "icon": "picture",
    "children": [
      {"name": "return", "title": "退货管理"}
      {"name": "invoice", "title": "发票"}
    ]
  },
  
  {
    "name": "authority", 
    "title": "权限管理", 
    "icon": "wrench",
    "children": [
      {"name": "authority", "title": "权限管理"},
      {"name": "account", "title": "账户管理"}
    ]
  },
  {
    "name": "image", "title": "图片空间", "icon": "picture",
    "children": [
      {"name": "index", "title": "图片上传"},
      {"name": "manage", "title": "图片管理"},
      {"name": "category", "title": "分类管理"}
    ]
  },
  {
    "name":"item","title":"商品管理","icon":"barcode",
    "children":[
      {"name":"product","title":"商品列表"}
    ]
  }
  {
    "name":"order","title":"订单管理","icon":"list-alt",
    "children":[
      {"name":"orderlist","title":"订单列表"},
      {"name":"pendingorderlist","title":"待发货列表"}
    ]
  }
] />
</#if>

<#-- preprocess -->

<#if !pageName??>
<#assign pageName="product"/>
</#if>

<#assign titles = pageName?split("-") />
	<#assign pageName1 = titles[0] />
<#if titles[1]??>
	<#assign pageName2 = titles[1] />
</#if>

<#assign pageMap = {} />

<#list pages as x>
   <#assign pageMap = pageMap + {x.name: x} />
</#list>

<#if pageName??>
  <#assign page = pageMap[pageName1] />
</#if>

<#assign cfg_develop = false/>
<!-- @IGNORE -->
<#assign cfg_develop = true/>
<!-- /@IGNORE -->

