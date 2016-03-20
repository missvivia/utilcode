<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include '../wrap/3g.common.ftl'>
<#if notAllowed?? == false>
  <#assign notAllowed = false/>
</#if>

<html>
  <head>
    <title>${product.productName} - mmall网</title>
    <@meta/>
    <@css/>
    <link rel='stylesheet' type='text/css' href='/src/css/page/detail.css'>
  </head>
  <body id='index-netease-com'>
      <div class="g-box">
    <@topbar>
    <#if  notAllowed == true>
    <div class="m-msgbox z-active">对不起，该地区不支持购买此商品</div>
    <#else>
    <div class="m-msgbox"></div>
    </#if>
    </@topbar>


  <div class='g-hd g-index-hd'>
    <div class='m-touchslide m-touchslide-2 j-slide'>
      <div class='mimg'>
        <ul class='j-node'>
          <#list product.prodShowPicList as pic >
            <li style="left: ${pic_index*100}%">
              <a class="m-img" href="<#if notAllowed == true>/index<#else>${(pic!'')?replace('\\?.*$','','r')!}</#if>" >
                <img class='u-loading-1' draggable='false'
                 data-src="${(pic!'')?replace('\\?.*$','','r')!}?imageView&quality=100&thumbnail=640x640" />
                <#if product.sameAsShop==1><div class="sameasshop"></div></#if>
              </a>
            </li>
          </#list>
        </ul>
      </div>
      <div class='m-pointer'>
        <div class='pointer'>
          <ul class='j-node'>
          <#list product.prodShowPicList as pic >
            <li></li>
            </#list>
          </ul>
        </div>
      </div>
    <div class="u-flag j-flag" style="display:none" >
      <div class="rect"></div>
      <span class='text'>已售罄</span> 
    </div>

    </div>
  </div>
  <div class='g-bd g-bd-detail'>
    <div class="m-dsections">
      <div class="m-sect m-sect-top">
        <div class="head row">
          <h2><span class="brandName">[${(product.brand.nameZH)!}]</span>${product.productName!}</h2>
          <#if  notAllowed == false >
          <div class="price head-sec">
            <div class="main">
              <span class="symbol">&yen; <em>
                <#if product.status==3>
                ？
                <#else>
                ${product.salePrice?string('0.00')}
                </#if>
                </em></span>
              <#if product.status!=3>
              <span class="cut">
                ${((product.salePrice/product.marketPrice)*10)?string('#.#')}折</span>
                </#if>

            </div>
            <div class="dcount j-count">
                <#if product.status==3>
              <span class="u-icon u-icon-time"></span> ${(product.schedule.startTime)?number_to_datetime?string("MM月dd日HH点")}开售
              </#if>
              </div>
          </div>
          <div class="price price-2">
            <span class="iprice">专柜价 : <span class="del">&yen;${ (product.marketPrice!0)?string('0.00')}</span></span>
            <#--<#if product.sellNum?? && product.sellNum &gt; 0>-->
            <#--<span class="sell">销量 <span class="count">${product.sellNum}</span>笔</span>-->
            <#--</#if>-->
          </div>
          </#if>
        </div>
        <#if  notAllowed == false >
        <#if activity??>
        <div class="active row">
          <span class="u-icon-tag u-icon"></span>
          <ul class='tags'>
            <#if activity?? && activity.tipList??>
              <#list activity.tipList as tip>
              <li>${tip.name!},${tip.desp!};</li>
              </#list>
            </#if>
          </ul>
        </div>
        </#if>

        <div class="store row">
          <div class="size u-formctr">
            <span class="name">尺码</span>
            <div class="field j-size">
            </div>
          </div>
          <div class="num u-formctr">
            <span class="name">数量</span>
            <div class="field j-numcount"></div>
          </div>
        </div>
      </div>
      </#if>
      <hr class="u-sep"/>
      <div class="m-sect m-sect-secure">
        <div class="item">
          <div class="wrap">
          <span class="u-icon u-icon-secure icon"></span>
            <p class="content">mmall所售均为品牌授权正品 <br>由中国人保（PICC）为您承保</p>
          </div>
        </div>
        <div class="item">
          <div class="wrap wrap-2">
            <span class="u-icon u-icon-7 icon"></span>
            <p class='content'>7天无理由退货，由mmall承担退货运费</p>
          </div>
        </div>
      </div>
      <hr class="u-sep"/>
      <div class="m-sect m-sect-detail">
        <div class="section">
          <h2 class='title'>商品参数</h2>
          <div class="cnt">
            <div class="m-tparam">
<div class="m-tparam">
                  <ul>
                   <li><span class="key">商品名称：</span> <span class="value">${product.productName!}</span></li>
                   <#--<li><span class="key">商品货号：</span> <span class="value">${product.goodsNo!}</span></li>-->
                   <li><span class="key">专柜同款：</span> <span class="value">
                      <#if product.sameAsShop?? && product.sameAsShop==1>是<#else>否</#if></span>
                    </li>
                    <#if product.length?? && product.length != "">
                    <li><span class="key">长：</span> <span class="value">${product.length}cm</span></li>
                    </#if>
                    <#if product.width?? && product.width != "">
                    <li><span class="key">宽：</span> <span class="value">${product.width}cm</span></li>
                    </#if>
                    <#if product.height?? && product.height != "" >
                    <li><span class="key">高：</span> <span class="value">${product.height}cm</span></li>
                    </#if>
                    <#if product.producing?? && product.producing != "" >
                    <li><span class="key">产地：</span> <span class="value">${product.producing}</span></li>
                    </#if>
                    <#if product.weight?? && product.weight != "">
                    <li><span class="key">重量：</span> <span class="value">${product.weight}kg</span></li>
                    </#if>
                  <#if product.productDetail??>
                  <#list product.productDetail as param>
                    <li title="${param.value!}" <#if param.type==4>class="line" </#if>>
                      <span class="key">${param.name!}：</span>
                      <span class="value">${param.value!}</span>
                    </li>
                  </#list>
                  </#if>
                  <#if product.accessory?? && product.accessory != "">
                  <li class="line">
                    <span class="key">配 &nbsp;&nbsp;&nbsp; 件：</span>
                    <span class="value">${product.accessory!} </span>
                  </li>
                  </#if>
                  <#if product.careLabel?? && product.careLabel != "">
                  <li class="line">
                    <span class="key">洗涤/使用说明：</span>
                    <span class="value">${product.careLabel!} </span>
                  </li>
                  </#if>
                  <#if product.afterMarket?? &&  product.afterMarket != "">
                  <li class="line">
                    <span class="key">售后说明：</span>
                    <span class="value">${product.afterMarket!} </span>
                  </li>
                  </#if>
                  <#if product.productDescp?? && product.productDescp != "">
                  <li class="line">
                    <span class="key">商品特点：</span>
                    <span class="value">${product.productDescp!} </span>
                  </li>
                  </#if>
                  </ul>
              </div>
            </div>
          </div>
        </div>
      <#if product.productSize??>
      <div class="section">
        <h2 class="title">商品尺码</h2>
        <div class="cnt">
          <table class="m-table m-table-size sitem">
          <thead>
            <tr>
              <#list product.productSize.header as hd>
              <th>${hd.name!}${hd.unit!}</th>
              </#list>
            </tr>
          </thead>
          <tbody>
                <#list product.productSize.body as bd>
                  <tr>
                    <#list bd as item>
                      <td>${item!}</td>
                    </#list>
                  </tr>
                </#list>
          </tbody>
        </table>
        <div class="j-helper"></div>
        </div>
      </div>
      </#if>
      <div class="section">
        <h2 class='title'>商品图片</h2>
        <div class="cnt">
          <ul class="m-plist">
            <#if product.prodShowPicList??>
            <#list product.prodShowPicList as pic>
            <li><img src="${(pic!"")?replace('\\?.*$','','r')!}?imageView&thumbnail=390x390&quality=100" alt=""></li>
            </#list>
            </#if>
          </ul>
          <div class="html"><#noescape>${product.customEditHTML!}</#noescape></div>
        </div>
      </div>

      </div>
    </div>
    <#-- 未被允许的地区 -->
      <#if notAllowed == false>
      <div class="m-cartbar">
        <a class="u-cbtn j-cart" href="javascript:;">${(statusMap[product.status?string])!"放入购物袋"}</a>
      </div>
      </#if>

        <@footer/>
  </div>

  </div>

    <script>
    window.__data__ = {
      <!-- @REMOVE -->
      <#noescape>
       product: ${stringify(product)},
       colors: ${stringify(colors)},
       isSnapshot: ${(isSnapshot?? && isSnapshot == true)?string},
       notAllowed: ${notAllowed?string}
       </#noescape>
    }
    </script>
    <@ga/>
    <script src='${jslib}define.js?${jscnf}'></script>
    <script src='${jspro}page/detail/detail.js'></script>
  </body>
</html>
</@compress>
</#escape>
