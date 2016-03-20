package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;

/**
 * ReturnPoOrderForm里的订单明细
 * 
 */
@AnnonOfClass(desc = "退供单明细表", tableName = "Mmall_Oms_PoReturnOrderSku")
public class ReturnPoOrderFormSku implements Serializable {

	private static final long serialVersionUID = 20140920L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "sku所属退供单Id")
	private long poReturnOrderId;

	@AnnonOfField(desc = "商家id", policy = true)
	private long supplierId;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "po编号", type = "VARCHAR(128)")
	private String poOrderId;

	@AnnonOfField(desc = "Skuid")
	private long skuId;
	
	@AnnonOfField(desc = "退供单当前状态.0:创建,1:已确认,2:已发货,3:已收货")
	private PoReturnOrderState state = PoReturnOrderState.NEW;

	@AnnonOfField(desc = "商品名称")
	private String productName = "";

	@AnnonOfField(desc = "Sku数量")
	private int count;

	@AnnonOfField(desc = "不良品数量")
	private int defectiveCount;

	@AnnonOfField(desc = "良品数量")
	private int normalCount;

	@AnnonOfField(desc = "出仓 时间")
	private long shipTime;

	@AnnonOfField(desc = "sku实际出仓总数量")
	private int realCount;

	@AnnonOfField(desc = "sku实际出仓次品数量")
	private int realDefectiveCount;

	@AnnonOfField(desc = "sku实际出仓正品数量")
	private int realNormalCount;

	@AnnonOfField(desc = "退货状态,0:1退,1:2退,2:3退")
	private ReturnType type = ReturnType.NULL;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public ReturnPoOrderFormSku() {

	}

	public ReturnPoOrderFormSku(WarehouseReturn warehouseReturn) {
		ReflectUtil.convertObj(this, warehouseReturn, false);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the poReturnOrderId
	 */
	public long getPoReturnOrderId() {
		return poReturnOrderId;
	}

	/**
	 * @param poReturnOrderId
	 *            the poReturnOrderId to set
	 */
	public void setPoReturnOrderId(long poReturnOrderId) {
		this.poReturnOrderId = poReturnOrderId;
	}

	/**
	 * @return the supplierId
	 */
	public long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the warehouseId
	 */
	public long getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId
	 *            the warehouseId to set
	 */
	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the poOrderId
	 */
	public String getPoOrderId() {
		return poOrderId;
	}

	/**
	 * @param poOrderId
	 *            the poOrderId to set
	 */
	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	/**
	 * @return the skuId
	 */
	public long getSkuId() {
		return skuId;
	}

	/**
	 * @param skuId
	 *            the skuId to set
	 */
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public PoReturnOrderState getState() {
		return state;
	}

	public void setState(PoReturnOrderState state) {
		this.state = state;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the skuCount
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param skuCount
	 *            the skuCount to set
	 */
	public void setCount(int skuCount) {
		this.count = skuCount;
	}

	public int getDefectiveCount() {
		return defectiveCount;
	}

	public void setDefectiveCount(int defectiveCount) {
		this.defectiveCount = defectiveCount;
	}

	public int getNormalCount() {
		return normalCount;
	}

	public void setNormalCount(int normalCount) {
		this.normalCount = normalCount;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getShipTime() {
		return shipTime;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public int getRealCount() {
		return realCount;
	}

	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	public int getRealDefectiveCount() {
		return realDefectiveCount;
	}

	public void setRealDefectiveCount(int realDefectiveCount) {
		this.realDefectiveCount = realDefectiveCount;
	}

	public int getRealNormalCount() {
		return realNormalCount;
	}

	public void setRealNormalCount(int realNormalCount) {
		this.realNormalCount = realNormalCount;
	}

	public ReturnType getType() {
		return type;
	}

	public void setType(ReturnType type) {
		this.type = type;
	}

}
