<#assign jslib ="/src/javascript/lib/nej/src/"/>
<#assign jspro ="/src/javascript/"/>


<#assign siteTitle = "运营平台" />
<#if pages?? == false>
<#assign pages = [
  {
    "name": "content", "title": "内容管理", "icon": "inbox" ,
    "children": [
      {"name": "spread", "title": "推广内容管理"},
      {"name": "helpcenter","title": "帮助中心管理"}
    ]
  },
  {
    "name": "schedule", "title": "档期管理", "icon": "record",
    "children": [
      {"name": "create", "title": "档期创建"},
      {"name": "audit", "title": "档期审核"},
      {"name": "place", "title": "档期展示管理"},
      {"name": "return", "title": "档期退货管理"}
    ]
  },
  {
    "name": "promotion", "title": "促销管理", "icon": "briefcase",
    "children": [
      {"name": "activity", "title": "活动管理"},
      {"name": "coupon", "title": "优惠券管理"},
      {"name": "packet", "title": "红包管理"},
    	{"name": "activityEdit", "title": "活动编辑"},
    	{"name": "couponEdit", "title": "优惠券编辑"},
    	{"name": "packetEdit", "title": "红包编辑"}
    ]
  },
  {
    "name": "order", "title": "订单管理","icon": "list-alt",
    "children": [
      {"name": "query", "title": "订单查询"},
      {"name": "topay", "title": "到付审核"},
      {"name": "return", "title": "退货退款(客服)"},
      {"name": "returnstore", "title": "退货退款(仓库)"},
      {"name": "sell", "title": "销售查询"},
      {"name": "refund", "title": "退款查询"}
    ]
  },
  {
    "name": "finance", "title": "财务管理","icon": "credit-card",
    "children": [
      {"name": "salequery", "title": "销售查询"},
      {"name": "return", "title": "退款查询"}
    ]
  },
  {
    "name": "user", "title": "用户资料查询"
    <#--"children": [{"name": "info", "title": "用户信息详情"}]-->
  },
  {
    "name": "business", "title": "商家管理","icon": "magnet",
    "children": [
      {"name": "account", "title": "商家列表"}
    ]
  },
  {
    "name": "access", "title": "用户权限管理","icon": "magnet",
    "children": [
      {"name": "role", "title": "角色创建"},
      {"name": "account", "title": "帐号权限分配"}
    ]
  },
  {
    "name": "audit", "title": "审核管理","icon": "saved",
    "children": [
      {"name": "productlist", "title": "档期商品清单审核"},
      {"name": "product", "title": "档期商品资料审核"},
      {"name": "decorate", "title": "档期装修审核"},
      {"name": "banner", "title": "档期BANNER审核"},
      {"name": "brand", "title": "品牌页装修审核"}
    ]
  },
  {
    "name": "app", "title": "APP内容管理","icon": "phone",
    "children": [
      {"name": "pmessage", "title": "push消息管理"},
      {"name": "feedback", "title": "意见反馈管理"}
    ]
  },

  {
    "name": "focuspicture", "title": "首页焦点图",
    "children": [
      {"name": "manage", "title": "首页焦点图管理"}
    ]
  },
  {
    "name": "message", "title": "消息通知",
    "children": [
      {"name": "list", "title": "消息通知列表"}
    ]
  },
  {
    "name": "category", "title": "分类管理",
    "children": [
      {"name": "normal", "title": "商品分类"},
      {"name": "content", "title": "内容分类"}
    ]
  },
  {
    "name": "item", "title": "商品管理","icon": "gift",
    "children": [
      {"name": "brand", "title": "品牌资料管理"},
      {"name":"spu","title":"单品库管理"},
      {"name":"model","title":"商品模型"}
    ]
  },
  {
    "name": "userInfo", "title": "用户管理",
    "children": [
      {"name": "userList", "title": "用户列表"}
    ]
  },
    {
    "name": "site", "title": "站点管理",
    "children": [
      {"name": "site", "title": "站点列表"}
    ]
  }
] />
</#if>

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
  <#if pageMap[pageName1]??>
  	<#assign page = pageMap[pageName1] />
  </#if>
</#if>



<#-- <#assign provinceList = [{"name":"浙江省","id":"1"},{"name":"上海","id":"2"},{"name":"北京","id":"3"}] /> -->
<#-- <#assign province = [{"name":"浙江省","id":"1"},{"name":"上海","id":"2"},{"name":"北京","id":"3"}] /> -->
<#--<#assign categoryList = [
    {
        "name":"首页","id":"1",
        "list":[
            {"name":"首页-1","id":"1"},
            {"name":"首页-2","id":"2"},
            {"name":"首页-3","id":"3"}
        ]
    },
    {
        "name":"女装","id":"2",
        "list":[
            {"name":"女装-1","id":"1"},
            {
                "name":"女装-2","id":"2",
                "list":[
                    {"name":"女装-21","id":"1"},
                    {"name":"女装-22","id":"2"},
                    {"name":"女装-23","id":"3"}
                ]
            },
            {"name":"女装-3","id":"3"}
        ]
    },
    {
        "name":"男装","id":"3",
        "list":[
            {"name":"男装-1","id":"1"},
            {"name":"男装-2","id":"2"},
            {
                "name":"男装-3","id":"3",
                "list":[
                    {"name":"男装-31","id":"1"},
                    {"name":"男装-32","id":"2"},
                    {"name":"男装-33","id":"3"}
                ]
            }
        ]
    },
    {
        "name":"儿童","id":"4",
        "list":[
            {"name":"儿童-1","id":"1"},
            {"name":"儿童-2","id":"2"},
            {"name":"儿童-3","id":"3"}
        ]
    },
    {
        "name":"家居","id":"5",
        "list":[
            {"name":"家居-1","id":"1"},
            {"name":"家居-2","id":"2"},
            {"name":"家居-3","id":"3"}
        ]
    }
] />
-->


