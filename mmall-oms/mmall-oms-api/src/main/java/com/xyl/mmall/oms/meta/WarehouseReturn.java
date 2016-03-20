package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.ReturnType;

/**
 * 
 * @author hzliujie 2014年10月14日 下午5:27:52
 */
@AnnonOfClass(desc = "退货表", tableName = "Mmall_Oms_WarehouseReturn")
public class WarehouseReturn implements Serializable {

	private static final long serialVersionUID = 20140920L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商家id", policy = true)
	private long supplierId;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "po编号")
	private String poOrderId = "";

	@AnnonOfField(desc = "Skuid")
	private long skuId;

	@AnnonOfField(desc = "商品名称")
	private String productName = "";

	@AnnonOfField(desc = "总数量")
	private int count;

	@AnnonOfField(desc = "不良品数量")
	private int defectiveCount;

	@AnnonOfField(desc = "良品数量")
	private int normalCount;

	@AnnonOfField(desc = "退货状态,0:1退,1:2退,2:3退")
	private ReturnType type = ReturnType.NULL;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
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

	public int getCount() {
		return count;
	}

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

	public ReturnType getType() {
		return type;
	}

	public void setType(ReturnType type) {
		this.type = type;
	}

}
