/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WMSShipOrderState;

/**
 * 商品入库单
 * 
 * @author hzzengchengyuan
 */
public class WMSShipOrderUpdateDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 仓储入库单号
	 */
	private String orderId;
	
	/**
	 * 入库单当前状态
	 */
	private WMSShipOrderState state = WMSShipOrderState.NULL;
	
	/**
	 * 入库单类别
	 */
	private WMSOrderType orderType = WMSOrderType.NULL;

	/**
	 * 订单状态更新的操作时间
	 */
	private long operaterTime;
	
	/**
	 * 商品总件数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int count = -1;

	/**
	 * 该入库单对应商品种数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int skuCount = -1;

	/**
	 * 单商品明细
	 */
	private List<WMSSkuDetailDTO> skuDetails;

	/**
	 * 是否有不良品
	 */
	private boolean hasDefective = false;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * @return the state
	 */
	public WMSShipOrderState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(WMSShipOrderState state) {
		this.state = state;
	}

	public WMSOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(WMSOrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		if(this.count == -1) {
			calculateCount();
		}
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the skuCount
	 */
	public int getSkuCount() {
		if(this.skuCount == -1) {
			calculateCount();
		}
		return skuCount;
	}

	/**
	 * @param skuCount
	 *            the skuCount to set
	 */
	public void setSkuCount(int skuCount) {
		this.skuCount = skuCount;
	}

	/**
	 * @return the operaterTime
	 */
	public long getOperaterTime() {
		return operaterTime;
	}

	/**
	 * @param operaterTime
	 *            the operaterTime to set
	 */
	public void setOperaterTime(long operaterTime) {
		this.operaterTime = operaterTime;
	}

	/**
	 * @return the skuDetails
	 */
	public List<WMSSkuDetailDTO> getSkuDetails() {
		return skuDetails;
	}

	/**
	 * @param skuDetails
	 *            the skuDetails to set
	 */
	public void setSkuDetails(List<WMSSkuDetailDTO> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public void addSkuDetail(WMSSkuDetailDTO skuDetail) {
		if (this.skuDetails == null) {
			this.skuDetails = new ArrayList<WMSSkuDetailDTO>();
		}
		this.skuDetails.add(skuDetail);
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the hasDefective
	 */
	public boolean isHasDefective() {
		if(this.count == -1) {
			calculateCount();
		}
		return hasDefective;
	}

	/**
	 * @param hasDefective
	 *            the hasDefective to set
	 */
	public void setHasDefective(boolean hasDefective) {
		this.hasDefective = hasDefective;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	public void calculateCount() {
		int allNum = 0;
		List<Long> skuIdList = new ArrayList<Long>();
		if (this.skuDetails != null) {
			for (WMSSkuDetailDTO skuD : this.skuDetails) {
				skuD.calculateCount();
				if (!skuIdList.contains(skuD.getSkuId())) {
					skuIdList.add(skuD.getSkuId());
				}
				allNum += skuD.getCount();
				if (skuD.getDefectiveCount() > 0) {
					this.hasDefective = true;
				}
			}
		}
		this.count = allNum;
		this.skuCount = skuIdList.size();
	}
	
	//定义入库单属性名常量列表，不全大写主要便于引用时查看
	public static final String field_orderId = "orderId";
	public static final String field_state = "state";
	public static final String field_count = "count";
	public static final String field_skuCount = "skuCount";
	public static final String field_skuDetails = "skuDetails";
	public static final String field_hasDefective = "hasDefective";
	public static final String field_note = "note";
	public static final String field_operaterTime = "operaterTime";
	public static final String field_orderType = "orderType";
}
