/**
 * SKU meta
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.util.Comparator;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.BuHuoType;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.PickStateType;

/**
 * Sku
 * 
 * @author hzzengdan
 * @date 2014-09-09
 */
@AnnonOfClass(desc = "商品表", tableName = "Mmall_Oms_PickSkuItem")
public class PickSkuItemForm implements Serializable, Comparator<PickSkuItemForm> {
	
	/** 序列ID. */
	private static final long serialVersionUID = 670141491018608019L;

	@AnnonOfField(desc = "自增主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "商品编号")
	private String skuId = "";

	@AnnonOfField(desc = "po单编号")
	private String poOrderId = "";

	@AnnonOfField(desc = "产品的供应商Id", policy = true)
	private long supplierId;
	
	@AnnonOfField(desc = "入库站点")
	private long storeAreaId;

	@AnnonOfField(desc = "拣货单编号", type = "varchar(128)")
	private String pickOrderId = "";

	@AnnonOfField(desc = "商品数量")
	private int skuQuantity;

	@AnnonOfField(desc = "拣货状态(0:未拣货,1:拣货中)")
	private PickStateType pickStates;

	@AnnonOfField(desc = "拣货时间")
	private long pickTime;

	@AnnonOfField(desc = "jit标志(0:JIT模式，1:非JIT模式)")
	private JITFlagType JITFlag;

	@AnnonOfField(desc = "颜色", type = "varchar(128)")
	private String color = "";

	@AnnonOfField(desc = "尺码", type = "varchar(128)")
	private String size = "";

	// omsOrderSku.getBarCode()
	@AnnonOfField(desc = "条形码")
	private String codeNO = "";

	@AnnonOfField(desc = "商品名称", type = "varchar(128)")
	private String productName = "";

	@AnnonOfField(desc = "拣货类型单品或多品")
	private PickMoldType pickMoldType;

	@AnnonOfField(desc = "对应的原始订单Id")
	private long omsOrderFormId;

	@AnnonOfField(desc = "是否补货,0为正常,1为补货")
	private BuHuoType buhuo;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long updateTime;
	
	@AnnonOfField(desc = "原始的产品的供应商Id")
	private long oriSupplierId;
	
	private long collectTime;
	
	private int boci;
	
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

	public int getSkuQuantity() {
		return skuQuantity;
	}

	public void setSkuQuantity(int skuQuantity) {
		this.skuQuantity = skuQuantity;
	}

	public long getPickTime() {
		return pickTime;
	}

	public void setPickTime(long pickTime) {
		this.pickTime = pickTime;
	}

	public PickStateType getPickStates() {
		return pickStates;
	}

	public void setPickStates(PickStateType pickStates) {
		this.pickStates = pickStates;
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

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}

	@Override
	public int compare(PickSkuItemForm o1, PickSkuItemForm o2) {
		return (int) (o1.getPickTime() - o2.getPickTime());
	}

	public PickMoldType getPickMoldType() {
		return pickMoldType;
	}

	public void setPickMoldType(PickMoldType pickMoldType) {
		this.pickMoldType = pickMoldType;
	}

	public long getOmsOrderFormId() {
		return omsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		this.omsOrderFormId = omsOrderFormId;
	}

	public BuHuoType getBuhuo() {
		return buhuo;
	}

	public void setBuhuo(BuHuoType buhuo) {
		this.buhuo = buhuo;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
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

	public long getOriSupplierId() {
		return oriSupplierId;
	}

	public void setOriSupplierId(long oriSupplierId) {
		this.oriSupplierId = oriSupplierId;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public int getBoci() {
		return boci;
	}

	public void setBoci(int boci) {
		this.boci = boci;
	}

	
	
}
