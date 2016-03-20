package com.xyl.mmall.mobile.web.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.netease.print.common.meta.HasShowIndex2;
import com.xyl.mmall.backend.vo.ProdSpeciBackendVO;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

/**
 * CartSkuItemVO.java
 *
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-24
 * @since 1.0
 */
public class CartSkuItemVO implements Serializable,HasShowIndex2<Long> {

	/**
	 * 有效可买sku
	 */
	public static final String STATE_NORMAL = "normal";

	/**
	 * 已经删除sku
	 */
	public static final String STATE_DELETED = "deleted";

	/**
	 * 超时sku
	 */
	public static final String STATE_OVERTIME = "overtime";
	
	/**
	 * 档期过期sku
	 */
	public static final String STATE_OVERPO = "overpo";
	
	/**
	 * 买完的sku
	 */
	public static final String STATE_SOLDOUT = "soldout";

	/**
	 * sku id
	 */
	private long id;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4977790393165208586L;

	public final String type = "cartsku";

	private String status = STATE_NORMAL;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * sku名字
	 */
	private String name;

	private String url;
	
	private long productId;
	
	private String thumb;

	@Deprecated
	private String color;

	@Deprecated
	private String size;

	private String brandLinkUrl;
	
	private String brandName;
	
	/**
	 * 是否下架
	 */
	private boolean isOffline = false;
	
	/**
	 * 是否删除
	 */
	private boolean isDelete = false;

	/**
	 * 是否选上
	 */
	private boolean isSelected = true;

	/**
	 * 是否选上
	 */
	private boolean isOverTime = false;

	/**
	 * 市场价
	 */
	private BigDecimal originalPrice;

	/**
	 * 零售价
	 */
	private BigDecimal retailPrice;

	/**
	 * 购物车价格
	 */
	private BigDecimal cartPrice;

	/**
	 * 市场价，专柜价
	 */
	private BigDecimal marketPrice;
	
	/**
	 * 活动优惠金额
	 */
	private BigDecimal promotionDiscountAmount;
	/**
	 * 
	 * 购买数量
	 */
	private int count;

	/**
	 * 库存数量
	 */
	private int inventroyCount;
	/**
	 * 店铺/商家ID
	 */
	private long storeId;
	/**
	 * 店铺名
	 */
	private String storeName;
	
	/**
	 * 店铺链接
	 */
	private String storeUrl;
	
	/**
	 * 店铺起批金额
	 */
	private BigDecimal storeBatchCash;
	
	/** 
	 * 商品规格 
	 */
	private List<ProdSpeciBackendVO> speciList;

	/**
	 * 商品单位
	 */
	private String unit;
	
	

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(BigDecimal cartPrice) {
		this.cartPrice = cartPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public boolean isDeleted() {
		return isDelete;
	}

	public void setDeleted(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isOverTime() {
		return isOverTime;
	}

	public void setOverTime(boolean isOverTime) {
		this.isOverTime = isOverTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getInventroyCount() {
		return inventroyCount;
	}

	public void setInventroyCount(int inventroyCount) {
		this.inventroyCount = inventroyCount;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getPromotionDiscountAmount() {
		return promotionDiscountAmount;
	}

	public void setPromotionDiscountAmount(BigDecimal promotionDiscountAmount) {
		this.promotionDiscountAmount = promotionDiscountAmount;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Override
	public Long getShowIndex() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getBrandLinkUrl() {
		return brandLinkUrl;
	}

	public void setBrandLinkUrl(String brandLinkUrl) {
		this.brandLinkUrl = brandLinkUrl;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<ProdSpeciBackendVO> getSpeciList() {
		return speciList;
	}

	public void setSpeciList(List<ProdSpeciBackendVO> speciList) {
		this.speciList = speciList;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public BigDecimal getStoreBatchCash() {
		return storeBatchCash;
	}

	public void setStoreBatchCash(BigDecimal storeBatchCash) {
		this.storeBatchCash = storeBatchCash;
	}

	public boolean isOffline() {
		return isOffline;
	}

	public void setOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
	

}
