/**
 * OMS的po单表
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.SupplyType;

/**
 * @author hzzengdan
 * @date 2014-09-15
 */
@AnnonOfClass(desc = "po单", tableName = "Mmall_Oms_PoOrderForm", policy = "poOrderId")
public class PoOrderForm implements Serializable {

	/** 序列. */
	private static final long serialVersionUID = 8677761173003929921L;
	
	public static final long COMMAND_GEN2RETURN = Long.parseLong("1", 2);

	@AnnonOfField(desc = "po单编号(poid)", primary = true, type = "varchar(128)")
	private String poOrderId;

	@AnnonOfField(desc = "开始时间")
	private long startTime;

	@AnnonOfField(desc = "结束时间")
	private long endTime;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long modifyTime;

	@AnnonOfField(desc = "jit标志(0:JIT模式，1:非JIT模式)")
	private JITFlagType JITFlag;

	@AnnonOfField(desc = "当前供应商所在站点")
	private long curSupplierAreaId;

	@AnnonOfField(desc = "供应商标识", policy = true)
	private long supplierId;

	@AnnonOfField(desc = "供应商名称", notNull = false)
	private String supplierName = "";

	@AnnonOfField(desc = "品牌标识")
	private long brandId;

	@AnnonOfField(desc = "品牌名称", notNull = false)
	private String brandName = "";

	@AnnonOfField(desc = "入库站点")
	private long storeAreaId;

	@AnnonOfField(desc = "虚拟库存")
	private long virtualstock;

	// 12.24 added
	@AnnonOfField(desc = "供货方式")
	private SupplyType supplyType;

	private List<PoOrderFormSku> poOrderFormSkuList;
	
	@AnnonOfField(desc = "已经执行过的命令操作标识")
	private long command;

	public long getVirtualstock() {
		return virtualstock;
	}

	public void setVirtualstock(long virtualstock) {
		this.virtualstock = virtualstock;
	}

	public static PoOrderForm makeUp(OmsOrderFormSku omsOrderSku) {
		PoOrderForm poOrderForm = new PoOrderForm();

		return poOrderForm;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public JITFlagType getJITFlag() {
		return JITFlag;
	}

	public void setJITFlag(JITFlagType jITFlag) {
		JITFlag = jITFlag;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getCurSupplierAreaId() {
		return curSupplierAreaId;
	}

	public void setCurSupplierAreaId(long curSupplierAreaId) {
		this.curSupplierAreaId = curSupplierAreaId;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}

	public List<PoOrderFormSku> getPoOrderFormSkuList() {
		return poOrderFormSkuList;
	}

	public void setPoOrderFormSkuList(List<PoOrderFormSku> poOrderFormSkuList) {
		this.poOrderFormSkuList = poOrderFormSkuList;
	}

	public long getCommand() {
		return command;
	}

	public void setCommand(long command) {
		this.command = command;
	}
	
}
