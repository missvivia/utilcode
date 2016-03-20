/**
 * 发货单sku表
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.util.Comparator;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.ShipStateType;

/**
 * @author hzzengdan
 * 
 */
@AnnonOfClass(desc = "商品表", tableName = "Mmall_Oms_ShipSkuItem")
public class ShipSkuItemForm implements Serializable, Comparator<ShipSkuItemForm> {
	
	/** 序列. */
	private static final long serialVersionUID = -5842561767513957755L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商品编号")
	private String skuId = "";

	@AnnonOfField(desc = "po单编号")
	private String poOrderId = "";

	@AnnonOfField(desc = "发货单编号")
	private String shipOrderId = "";

	@AnnonOfField(desc = "商品数量")
	private int skuQuantity;

	@AnnonOfField(desc = "发货状态")
	private ShipStateType shipStates;

	@AnnonOfField(desc = "发货时间")
	private long shipTime;

	@AnnonOfField(desc = "jit标志(0:JIT模式，1:非JIT模式)")
	private JITFlagType JITFlag;

	@AnnonOfField(desc = "颜色", type = "varchar(128)")
	private String color = "";

	@AnnonOfField(desc = "尺码", type = "varchar(128)")
	private String size = "";

	@AnnonOfField(desc = "商品货号")
	private String codeNO = "";

	@AnnonOfField(desc = "商品名称", type = "varchar(128)")
	private String productName = "";

	@AnnonOfField(desc = "商家ID", policy = true)
	private long supplierId;
	
	@AnnonOfField(desc = "仓库id")
	private long storeAreaId;
	
	// ////////////////
	// 以下是仓库接收入库单的反馈数据
	// ////////////////
	@AnnonOfField(desc = "实际到货-总数量")
	private int arrivedQuantity;

	@AnnonOfField(desc = "实际到货-不良品数量")
	private int arrivedDefectiveCount;

	@AnnonOfField(desc = "实际到货-良品数量")
	private int arrivedNormalCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public int getSkuQuantity() {
		return skuQuantity;
	}

	public void setSkuQuantity(int skuQuantity) {
		this.skuQuantity = skuQuantity;
	}

	public ShipStateType getShipStates() {
		return shipStates;
	}

	public void setShipStates(ShipStateType shipStates) {
		this.shipStates = shipStates;
	}

	public long getShipTime() {
		return shipTime;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public JITFlagType getJITFlag() {
		return JITFlag;
	}

	public void setJITFlag(JITFlagType jITFlag) {
		JITFlag = jITFlag;
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

	public String getCodeNO() {
		return codeNO;
	}

	public void setCodeNO(String codeNO) {
		this.codeNO = codeNO;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getArrivedQuantity() {
		return arrivedQuantity;
	}

	public void setArrivedQuantity(int arrivedQuantity) {
		this.arrivedQuantity = arrivedQuantity;
	}

	public int getArrivedDefectiveCount() {
		return arrivedDefectiveCount;
	}

	public void setArrivedDefectiveCount(int arrivedDefectiveCount) {
		this.arrivedDefectiveCount = arrivedDefectiveCount;
	}

	public int getArrivedNormalCount() {
		return arrivedNormalCount;
	}

	public void setArrivedNormalCount(int arrivedNormalCount) {
		this.arrivedNormalCount = arrivedNormalCount;
	}

	@Override
	public int compare(ShipSkuItemForm o1, ShipSkuItemForm o2) {
		return (int) (o1.getShipTime() - o2.getShipTime());
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

}
