/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.dto.ProductSKULimitRecordDTO;

/**
 * ProductSKULimitRecordVO.java created by yydx811 at 2015年11月20日 下午3:41:33
 * 商品限购记录vo
 *
 * @author yydx811
 */
public class ProductSKULimitRecordVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 178071119500452308L;

	/** 记录id. */
	private long recordId;

	/** 商品id. */
	private long skuId;

	/** 用户id. */
	private long userId;

	/** 购买总数. */
	private int buyNum;
	
	/** 缓存购买总数. */
	private int buyCacheNum;

	/** 周期内第一次购买时间. */
	private long createTime;

	/** 最后一次购买时间. */
	private long lastBuyTime;

	public ProductSKULimitRecordVO() {
	}

	public ProductSKULimitRecordVO(ProductSKULimitRecordDTO obj) {
		if (obj != null) {
			this.recordId = obj.getId();
			this.skuId = obj.getProductSKUId();
			this.userId = obj.getUserId();
			this.buyNum = obj.getTotalNumber();
			this.createTime = obj.getCreateTime();
			this.lastBuyTime = obj.getLastBuyTime();
		}
	}
	
	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getBuyCacheNum() {
		return buyCacheNum;
	}

	public void setBuyCacheNum(int buyCacheNum) {
		this.buyCacheNum = buyCacheNum;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(long lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}
}
