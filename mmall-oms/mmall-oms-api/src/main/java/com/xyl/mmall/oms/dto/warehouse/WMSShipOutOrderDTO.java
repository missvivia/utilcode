/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * 出库单（商家商品出仓）
 * 
 * @author hzzengchengyuan
 * 
 */
public class WMSShipOutOrderDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 出库单号
	 */
	private String orderId;

	/**
	 * 仓库机构编码(WMS提供)
	 */
	private String warehouseCode;

	/**
	 * 货主编码(WMS提供)
	 */
	private String ownerCode;

	/**
	 * 仓库类别
	 */
	private WarehouseType wmsType = WarehouseType.NULL;

	/**
	 * 收货人姓名
	 */
	private String receiverName;

	/**
	 * 邮编
	 */
	private String receiverPostCode;

	/**
	 * 收货人电话，包括区号、电话号码及分机号，中间用“-”分隔；
	 */
	private String receiverPhone;

	/**
	 * 收货人移动电话
	 */
	private String receiverMobile;

	/**
	 * 收货人所在省
	 */
	private String receiverProvince;

	/**
	 * 收货人所在市
	 */
	private String receiverCity;

	/**
	 * 收货人所在县（区）
	 */
	private String receiverDistrict;

	/**
	 * 收货人详细地址
	 */
	private String receiverAddress;

	/**
	 * 总件数（数量）。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int count = -1;

	/**
	 * 该单对应商品种类数。如果设置了{@link #skuDetails}，则可在设置完所以商品后调用{@link #calculateCount()}方法计算
	 */
	private int skuCount = -1;

	/**
	 * 发货单商品明细
	 */
	private List<WMSSkuDetailDTO> skuDetails;

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
	 * @return the warehouseCode
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * @param warehouseCode
	 *            the warehouseCode to set
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the ownerCode
	 */
	public String getOwnerCode() {
		return ownerCode;
	}

	/**
	 * @param ownerCode
	 *            the ownerCode to set
	 */
	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	/**
	 * @return the wmsType
	 */
	public WarehouseType getWmsType() {
		return wmsType;
	}

	/**
	 * @param wmsType the wmsType to set
	 */
	public void setWmsType(WarehouseType wmsType) {
		this.wmsType = wmsType;
	}

	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * @param receiverName
	 *            the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * @return the receiverPostCode
	 */
	public String getReceiverPostCode() {
		return receiverPostCode;
	}

	/**
	 * @param receiverPostCode
	 *            the receiverPostCode to set
	 */
	public void setReceiverPostCode(String receiverPostCode) {
		this.receiverPostCode = receiverPostCode;
	}

	/**
	 * @return the receiverPhone
	 */
	public String getReceiverPhone() {
		return receiverPhone;
	}

	/**
	 * @param receiverPhone
	 *            the receiverPhone to set
	 */
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	/**
	 * @return the receiverMobile
	 */
	public String getReceiverMobile() {
		return receiverMobile;
	}

	/**
	 * @param receiverMobile
	 *            the receiverMobile to set
	 */
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	/**
	 * @return the receiverProvince
	 */
	public String getReceiverProvince() {
		return receiverProvince;
	}

	/**
	 * @param receiverProvince
	 *            the receiverProvince to set
	 */
	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	/**
	 * @return the receiverCity
	 */
	public String getReceiverCity() {
		return receiverCity;
	}

	/**
	 * @param receiverCity
	 *            the receiverCity to set
	 */
	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	/**
	 * @return the receiverDistrict
	 */
	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	/**
	 * @param receiverDistrict
	 *            the receiverDistrict to set
	 */
	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	/**
	 * @return the receiverAddress
	 */
	public String getReceiverAddress() {
		return receiverAddress;
	}

	/**
	 * @param receiverAddress
	 *            the receiverAddress to set
	 */
	public void setReceiverAddress(String receiverDxddress) {
		this.receiverAddress = receiverDxddress;
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

	public void addSkuDetail(WMSSkuDetailDTO skuDetail) {
		if (this.skuDetails == null) {
			this.skuDetails = new ArrayList<WMSSkuDetailDTO>();
		}
		this.skuDetails.add(skuDetail);
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
			}
		}
		this.count = allNum;
		this.skuCount = skuIdList.size();
	}
	
	// 定义销售订单属性名常量列表，不全大写主要便于引用时查看
	public static final String field_orderId = "orderId";
	public static final String field_warehouseCode = "warehouseCode";
	public static final String field_ownerCode = "ownerCode";
	public static final String field_receiverName = "receiverName";
	public static final String field_receiverPostCode = "receiverPostCode";
	public static final String field_receiverPhone = "receiverPhone";
	public static final String field_receiverMobile = "receiverMobile";
	public static final String field_receiverProvince = "receiverProvince";
	public static final String field_receiverCity = "receiverCity";
	public static final String field_receiverDistrict = "receiverDistrict";
	public static final String field_receiverAddress = "receiverAddress";
	public static final String field_count = "count";
	public static final String field_skuCount = "skuCount";
	public static final String field_skuDetails = "skuDetails";
	public static final String field_note = "note";

}
