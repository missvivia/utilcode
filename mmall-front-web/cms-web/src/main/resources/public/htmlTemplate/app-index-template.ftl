<#if banner??>
<div class="swiper-container">
    <div class="swiper-wrapper">
        <#list banner as item>
              <div class="swiper-slide" style="background-image: url('${item.picURL}?imageView&quality=70&thumbnail=640x300')" <#if item.linkURL??>data-link="${item.linkURL}"</#if>></div>
        </#list>
    </div>
    <div class="swiper-pagination"></div>
</div>
</#if>
<#if (goodsList?size > 0) >
    <div class='home-tit'>
        <#if goodsName??><span>${goodsName}</span></#if>
        <#if goodsMoreURL??>
        	<a href="${goodsMoreURL}" class="more">更多<i></i></a>
        </#if>
    </div>
    <#if (goodsList?size > 0) >
	    <div class="hot">
	        <div class="list">
	            <ul>
	                <#list goodsList as item>
	                    <li>
	                        <a href="/m/productDetail?skuId=${item.id?c}">
	                            <img src="${item.showPicPath}?imageView&quality=70&thumbnail=180x158">
	                            <span class="name">${item.name}</span>
	                        </a>
	                        <span class="price">
	                           	 ￥<em>${item.priceList[0].price}</em>
	                        </span>
	                        <a href="/m/store/searchProduct?businessId=${item.businessId?c}" class="store">${item.storeName}</a>
	                    </li>
	                </#list>
	            </ul>
	        </div>
    	</div>
    </#if>
</#if>