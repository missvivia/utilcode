package com.xyl.mmall.oms.dto.warehouse;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WMSPackageState;

/**
 * @author hzzengchengyuan
 *
 */
public class WMSPackageDTO {
	/**
	 * 快递公司
	 */
	private String expressCompany;

	/**
	 * 包裹运单号
	 */
	private String shipNo;

	/**
	 * 长（厘米，箱包装尺寸）
	 */
	private long length;

	/**
	 * 宽（厘米，箱包装尺寸）
	 */
	private long width;

	/**
	 * 高（厘米，箱包装尺寸）
	 */
	private long height;

	/**
	 * 重量（克，箱包装重量）
	 */
	private long weight;

	/**
	 * 包裹状态
	 */
	private WMSPackageState state = WMSPackageState.NULL;

	/**
	 * 商品明细
	 */
	private List<WMSSkuDetailDTO> skuDetails;

	/**
	 * 商品数量
	 */
	private long count = -1;

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	/**
	 * @return the shipNo
	 */
	public String getShipNo() {
		return shipNo;
	}

	/**
	 * @param shipNo
	 *            the shipNo to set
	 */
	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}

	/**
	 * @return the width
	 */
	public long getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(long width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public long getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(long height) {
		this.height = height;
	}

	/**
	 * @return the weight
	 */
	public long getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(long weight) {
		this.weight = weight;
	}

	/**
	 * @return the skuDetails
	 */
	public List<WMSSkuDetailDTO> getSkuDetails() {
		return skuDetails;
	}

	public void addSkuDetail(WMSSkuDetailDTO sku) {
		if (sku != null) {
			if(this.skuDetails ==null) {
				this.skuDetails  = new ArrayList<WMSSkuDetailDTO> ();
			}
			this.skuDetails.add(sku);
		}
	}

	/**
	 * @param skuDetails
	 *            the skuDetails to set
	 */
	public void setSkuDetails(List<WMSSkuDetailDTO> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public long getCount() {
		if (this.count == -1) {
			calculateCount();
		}
		return this.count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void calculateCount() {
		int allNum = 0;
		if (this.skuDetails != null) {
			for (WMSSkuDetailDTO skuD : this.skuDetails) {
				skuD.calculateCount();
				allNum += skuD.getCount();
			}
		}
		this.count = allNum;
	}

	public WMSPackageState getState() {
		return state;
	}

	public void setState(WMSPackageState state) {
		this.state = state;
	}

	public static final String field_expressCompany = "expressCompany";

	public static final String field_shipNo = "shipNo";

	public static final String field_length = "length";

	public static final String field_width = "width";

	public static final String field_height = "height";

	public static final String field_weight = "weight";

	public static final String field_skuDetails = "skuDetails";

	public static final String field_count = "count";
}
