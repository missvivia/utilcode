<#assign backup = (layout.mapPartOthers)!{}/>
<#-- check is image id -->
<#function isImageId id="">
  <#return getImageURL(id)!="">
</#function>
<#-- check image id list -->
<#function hasImage arr=[]>
  <#list arr as id>
    <#if isImageId(id)>
      <#return true>
    </#if>
  </#list>
  <#return false>
</#function>
<#-- get image data -->
<#function getImage id="">
  <#return (images!{})[id?string]!{}>
</#function>
<#-- get image url -->
<#function getImageURL id="">
  <#return getImage(id).imgUrl!(backup[id?string])!"">
</#function>
<#-- check is product id -->
<#function isProductId id="">
  <#return getProduct(id).id??>
</#function>
<#-- check product id list -->
<#function hasProduct arr=[]>
  <#list arr as id>
    <#if isProductId(id)>
      <#return true>
    </#if>
  </#list>
  <#return false>
</#function>
<#-- get product data -->
<#function getProduct id="">
  <#return (products!{})[id?string]!{}>
</#function>
<#-- get product url -->
<#function getProductURL id="" sku="">
  <#return "/detail?id="+id+((""+sku)=="")?string("","&skuId="+sku)>
</#function>
<#--
    get product sold state
     1 - for sale
     2 - opportunity
     3 - sold out
-->
<#function getProductState id="">
  <#local data = getProduct(id)/>
  <#if !data.id??><#return 1></#if>
  <#local sku = data.skuList![]/>
  <#local sold=0 sale=0/>
  <#list sku as x>
    <#if x.state==3>
      <#local sold = sold+1/>
    <#elseif x.state==0||x.state==1>
      <#local sale = sale+1/>
    </#if>
  </#list>
  <#if sold&gt;=sku?size>
    <#return 3>
  <#elseif sale&gt;0>
    <#return 1>
  </#if>
  <#return 2>
</#function>
<#-- format price -->
<#function formatPrice price=0>
  <#local arr=(""+price)?split(".")/>
  <#return "<em>${arr[0]}</em><b>."+(arr[1]!"0")?right_pad(2,"0")?substring(0,2)+"</b>">
</#function>
<#-- layout to style -->
<#macro layoutStyle layout={}>
<style type="text/css">
  .g-schedule{
      <#local url = getImageURL(layout.bgImgId)/>
      <#if url!="">background-image:url(${url});</#if>
      <#local setting = (layout.bgSetting)!{}/>
      <#if setting.bgColor??>background-color:${setting.bgColor};</#if>
  }
  <#local setting = (layout.headerSetting)!{}/>
  .g-schedule .banner{
      height:${setting.height!390}px;
      <#local url = getImageURL(layout.headerImgId)/>
      <#if url!="">background-image:url(${url});</#if>
  }
  .g-schedule .banner .timer{
      top:${setting.top!0}px;
      left:${setting.left!420}px;
      color:${setting.fontColor!"#000"};
      font-size:${setting.fontSize!20}px;
      font-family:${setting.fontFamily!"微软雅黑"};
      font-weight:${setting.fontWeight!"normal"};
      border-width:${setting.borderWidth!"0"}px;
      border-style:${setting.borderStyle!"none"};
      border-color:${setting.borderColor!"#aaa"};
  }
  .g-schedule .banner .timer .bg{
      background-color:${setting.bgColor!"#fff"};
      <#local opacity = (setting.opacity)!50/>
      opacity:${opacity/100};
      filter:alpha(opacity=${opacity});
  }
  <#local setting = (layout.allListPartOthers)!{}/>
  .g-schedule .pdlist{
      margin-top:${setting.spaceTop!15}px;
  }
  .g-schedule .pdlist .bg{
      background-color:${setting.bgColor!"#fff"};
      <#local opacity = (setting.opacity)!100/>
      opacity:${opacity/100};
      filter:alpha(opacity=${opacity});
  }
  <#local setting = (layout.udSetting)![]/>
  <#list setting as x>
    <#switch x.type?number>
      <#case 1> <@widgetTextCSS config=x/>      <#break>
      <#case 2> <@widgetBannerCSS config=x/>    <#break>
      <#case 3> <@widgetProduct2CSS config=x/>  <#break>
      <#case 4> <@widgetProduct31CSS config=x/> <#break>
      <#case 5> <@widgetProduct32CSS config=x/> <#break>
      <#case 6> <@widgetProduct4CSS config=x/>  <#break>
    </#switch>
  </#list>
</style>
</#macro>
<#-- 自定义组件结构 -->
<#macro widget config={} class="">
  <div class="w-${config.type} w-${config.id} ${class}">
    <#nested/>
  </div>
</#macro>
<#-- 商品结构 -->
<#macro productCover url="" class="">
  <#if url=="">
	<img class="${class}" src="/res/images/default.png"/>
  <#else>
	<img class="${class} u-loading-1" data-src="${url}"/>
  </#if>
</#macro>
<#macro product class="" pid="" mid="" config={} showPrice=true>
  <#local product=getProduct(pid)/>
  <div class="m-prd m-pdi ${class} j-iprd" data-hover="j-phover">
    <div class="m-phd pic">
      <#-- image show -->
      <#local url = getImageURL(mid)/>
      <#if url!="">
          <#if product.id??>
          <a href="${getProductURL(product.id)}" target="_blank">
            <img class="u-loading-1" data-src="${url}"/>
          </a>
          <#else>
          <img class="u-loading-1" data-src="${url}"/>
          </#if>
      <#elseif product.id??>
          <a href="${getProductURL(product.id)}" target="_blank">
            <#local clist = (product.listShowPicList)![]/>
            <@productCover class="zm" url=clist[0]/>
            <@productCover class="fm" url=clist[1]/>
          </a>
      <#else>
          <p class="tpx tp0">${config.tip!"商品图"}</p>
          <p class="tpx tp1">${config.size!"254*320"}px</p>
      </#if>
      <#if product.id??>

      <#-- state -->
      <#if showPrice>
	    <#local state = getProductState(pid)/>
	    <#if state==3>
	      <div class="mxk">&nbsp;</div>
	      <div class="out">&nbsp;</div>
	    <#elseif state==2>
	      <div class="opp">&nbsp;</div>
	    </#if>
	  </#if>

      <#-- sku -->
      <#local skues = (product.skuList)![]>
      <#if skues?size&gt;0>
      <div class="size">
        <div class="mxk">&nbsp;</div>
        <div class="lst">
          <label>尺码：</label>
          <#list skues as x>
          <a target="_blank" href="${getProductURL(product.id,x.id)}" <#if x.state==3>class="iot" title="已售罄"<#elseif x.state==2>class="iop" title="还有机会"</#if>>${x.size}</a>
          </#list>
        </div>
      </div>
      </#if>

      </#if>
    </div>
    
    <#-- product information -->
    <#if product.id??>
      <div class="lnx ln0">
        <a target="_blank" class="xn pn" href="${getProductURL(product.id)}">${product.productName!"商品名称"}</a>
      </div>
      <#local sale = (product.salePrice)!100
              market = (product.marketPrice)!1000/>
      <#if market==0><#local market=1000/></#if>
      <div class="ln2">
        <#if showPrice>
        <span class="sp">¥${formatPrice(sale)}(${(sale*10/market)?string("0.#")}折)</span>
        <#else>
        <span class="sp">¥<em>?</em>　(?折)</span>
        </#if>
        <span class="mp">专柜价：<del>¥${market}</del></span>
      </div>
    <#else>
      <div class="lnx ln0">
        <span class="xn pn">商品名称</span>
      </div>
      <div class="ln2">
        <span class="sp">¥<em>---</em>　(---折)</span>
        <span class="mp">专柜价：<del>¥---</del></span>
      </div>
    </#if>
  </div>
</#macro>
<#-- 文字栏组件 type=1 -->
<#macro widgetTextCSS config={}>
  .w-${config.id}{
      color:${config.fontColor!"#000000"};
      height:${config.height}px;
      line-height:${config.height}px;
      margin-top:${config.spaceTop}px;
      text-align:${config.textAlign!"center"};
      font-size:${config.fontSize!20}px;
      font-family:${config.fontFamily!"Arial"};
      font-weight:${config.fontWeight!"normal"};
      border-width:${config.borderWidth!"0"}px;
      border-color:${config.borderColor!"#aaa"};
      border-style:${config.borderStyle!"none"};
  }
  .w-${config.id} .bg{
      background-color:${config.bgColor!"#E5E5E5"};
      <#local opacity = config.opacity!100/>
      opacity:${opacity/100};
      filter:alpha(opacity=${opacity});
  }
</#macro>
<#macro widgetTextHTML config={}>
  <@widget config=config class="m-maxk">
    <div class="bg xmsk">&nbsp;</div>
    <div class="ct xcnt">${(config.textContent!"文字栏")?html}</div>
  </@widget>
</#macro>
<#-- BANNER组件 type=2 -->
<#macro widgetBannerCSS config={}>
  .w-${config.id}{
      height:${config.height}px;
      margin-top:${config.spaceTop}px;
      <#local url = getImageURL(config.imgId)/>
      <#if url!="">background-image:url(${url});</#if>
  }
  <#list config.hotspots![] as x>
  .w-${config.id} .hs-${x_index}{
      top:${x.top}px;
      left:${x.left}px;
      width:${x.width}px;
      height:${x.height}px;
  }
  </#list>
</#macro>
<#macro widgetBannerHTML config={}>
  <@widget config=config>
    <#list config.hotspots![] as x>
      <#local product = getProduct(x.id)/>
      <#if product.id??>
      <a class="htspt htspt${getProductState(x.id)} hs-${x_index}" href="${getProductURL(x.id)}" target="_blank" title="${(product.productName)!"商品名称"}">&nbsp;</a>
      </#if>
    </#list>
  </@widget>
</#macro>
<#-- 产品组件 - 2图 type=3 -->
<#macro widgetProduct2CSS config={}>
  .w-${config.id}{
      margin-top:${config.spaceTop}px;
  }
</#macro>
<#macro widgetProduct2HTML config={} showPrice=true>
  <#local mids = config.bannerIds![]/>
  <#local pids = config.productIds![]/>
  <#if hasImage(mids)||hasProduct(pids)>
    <@widget config=config class="f-cb">
      <#local clazz="a"/>
      <#list 0..1 as x>
        <#local mid=mids[x] pid=pids[x]/>
        <#if isImageId(mid)||isProductId(pid)>
          <div class="m-c2">
            <@product mid=mid pid=pid class="m-prd-2 ${clazz}" showPrice=showPrice/>
          </div>
          <#local clazz="c"/>
        </#if>
      </#list>
    </@widget>
  </#if>
</#macro>
<#-- 产品组件 - 3图 type=4 -->
<#macro widgetProduct31CSS config={}>
  .w-${config.id}{
      margin-top:${config.spaceTop}px;
  }
</#macro>
<#macro widgetProduct31HTML config={} showPrice=true>
  <#local mids = config.bannerIds![]/>
  <#local pids = config.productIds![]/>
  <#if hasImage(mids)||hasProduct(pids)>
    <@widget config=config class="f-cb">
      <#local clazz="a"/>
      <#list 0..2 as x>
        <#local mid=mids[x] pid=pids[x]/>
        <#if isImageId(mid)||isProductId(pid)>
          <div class="m-c3">
            <@product mid=mid pid=pid class="m-prd-31 ${clazz}" showPrice=showPrice/>
          </div>
          <#if clazz=="a"><#local clazz="b"/><#elseif clazz=="b"><#local clazz="c"/></#if>
        </#if>
      </#list>
    </@widget>
  </#if>
</#macro>
<#-- 产品组件 - 3图 type=5 -->
<#macro widgetProduct32CSS config={}>
  .w-${config.id}{
      margin-top:${config.spaceTop}px;
  }
</#macro>
<#macro widgetProduct32HTML config={} showPrice=true>
  <@widget config=config class="m-c4b">
    <#local url = getImageURL(((config.bannerIds)![])[0])/>
    <div class="m-c4p f-cb">
      <div class="m-c4">
        <div class="m-phd b bnr">
          <#if url!="">
          <img src="${url}"/>
          <#else>
          <p class="tpx tp0">banner</p>
          <p class="tpx tp1">254*426px</p>
          </#if>
        </div>
      </div>
      <#local pids = config.productIds![]/>
      <#list 0..2 as x>
        <#local pid=pids[x]/>
        <#if isProductId(pid)>
          <div class="m-c4">
            <@product class="m-prd-32 b" pid=pid showPrice=showPrice/>
          </div>
        </#if>
      </#list>
    </div>
  </@widget>
</#macro>
<#-- 产品组件 - 4图 type=6 -->
<#macro widgetProduct4CSS config={}>
  .w-${config.id}{
      margin-top:${config.spaceTop}px;
  }
</#macro>
<#macro widgetProduct4HTML config={} showPrice=true>
  <#local pids = config.productIds![]/>
  <#if hasProduct(pids)>
  <@widget config=config class="m-c4b">
    <div class="m-c4p f-cb">
      <#list 0..3 as x>
        <#local pid=pids[x]/>
        <#if isProductId(pid)>
        <div class="m-c4">
          <@product class="m-prd-4 b" pid=pid showPrice=showPrice/>
        </div>
        </#if>
      </#list>
    </div>
  </@widget>
  </#if>
</#macro>
<#-- 预览关闭提示 -->
<#macro previewBar>
  <script>try{window.focus();}catch(e){}</script>
  <div class="m-pcls">您正在预览装修效果，重新预览请先<a href="#" onclick="window.close();return false;">关闭</a>此页面</div>
</#macro>
