/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WMSShipOutOrderState;

/**
 * 出库单（商家商品出仓）
 * 
 * @author hzzengchengyuan
 * 
 */
public class WMSShipOutOrderUpdateDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 出库单号
	 */
	private String orderId;

	private WMSShipOutOrderState state = WMSShipOutOrderState.NULL;

	/**
	 * 订单状态更新的操作时间
	 */
	private long operaterTime;

	/**
	 * 总件数（数量）。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}
	 * 方法计算
	 */
	private int count = -1;

	/**
	 * 该单对应商品种类数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}
	 * 方法计算
	 */
	private int skuCount = -1;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 物流公司代码
	 */
	private String logisticCode;

	/**
	 * 运输联系电话
	 */
	private String logisticPhone;

	/**
	 * 集装箱个数
	 */
	private int boxCount;

	/**
	 * 体积
	 */
	private double volume;

	/**
	 * 重量（单位克）
	 */
	private double weight;

	/**
	 * 发货单商品明细
	 */
	private List<WMSSkuDetailDTO> skuDetails;

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
	public WMSShipOutOrderState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(WMSShipOutOrderState state) {
		this.state = state;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		if (this.count == -1) {
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
		if (this.skuCount == -1) {
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

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the logisticCode
	 */
	public String getLogisticCode() {
		return logisticCode;
	}

	/**
	 * @param logisticCode
	 *            the logisticCode to set
	 */
	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}

	/**
	 * @return the logisticPhone
	 */
	public String getLogisticPhone() {
		return logisticPhone;
	}

	/**
	 * @param logisticPhone
	 *            the logisticPhone to set
	 */
	public void setLogisticPhone(String logisticPhone) {
		this.logisticPhone = logisticPhone;
	}

	/**
	 * @return the boxCount
	 */
	public int getBoxCount() {
		return boxCount;
	}

	/**
	 * @param boxCount
	 *            the boxCount to set
	 */
	public void setBoxCount(int boxCount) {
		this.boxCount = boxCount;
	}

	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
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

	public void calculateCount() {
		int allNum = 0;
		List<Long> skuIdList = new ArrayList<Long>();
		if (this.getSkuDetails() != null) {
			for (WMSSkuDetailDTO skuD : this.getSkuDetails()) {
				skuD.calculateCount();
				if (!skuIdList.contains(skuD.getSkuId())) {
					skuIdList.add(skuD.getSkuId());
				}
				allNum += skuD.getCount();
			}
		}
		this.count = allNum;
		this.skuCount = skuIdList.size();
	}

	public WMSSkuDetailDTO getSkuById(long skuId) {
		if (this.getSkuDetails() != null) {
			for (WMSSkuDetailDTO sku : this.getSkuDetails()) {
				if (sku.getSkuId() == skuId) {
					return sku;
				}
			}
		}
		return null;
	}

	// 定义销售订单属性名常量列表，不全大写主要便于引用时查看
	public static final String field_orderId = "orderId";

	public static final String field_state = "state";

	public static final String field_count = "count";

	public static final String field_skuCount = "skuCount";

	public static final String field_skuDetails = "skuDetails";

	public static final String field_note = "note";

	public static final String field_logisticCode = "logisticCode";

	public static final String field_logisticPhone = "logisticPhone";

	public static final String field_boxCount = "boxCount";

	public static final String field_volume = "volume";

	public static final String field_weight = "weight";

	public static final String field_operaterTime = "operaterTime";

}
