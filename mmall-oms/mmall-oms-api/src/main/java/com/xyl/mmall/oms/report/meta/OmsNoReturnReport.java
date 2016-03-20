package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsOrderPackageState;

@AnnonOfClass(desc = "未返货报表", tableName = "Mmall_Oms_Report_NoReturnReport")
public class OmsNoReturnReport implements Serializable {

	private static final long serialVersionUID = -1931628174231776998L;

	@AnnonOfField(desc = "主键", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "发货日期")
	private long date;

	@AnnonOfField(desc = "快递公司名称")
	private String expressCompany;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "仓库名称")
	private String warehouseName;

	@AnnonOfField(desc = "订单号")
	private long OmsOrderFormId;

	@AnnonOfField(desc = "订单金额")
	private BigDecimal price;

	@AnnonOfField(desc = "省份", notNull = false)
	private String province;

	@AnnonOfField(desc = "城市", notNull = false)
	private String city;

	@AnnonOfField(desc = "配送地址", notNull = false)
	private String consigneeAddress;

	@AnnonOfField(desc = "商品名称", notNull = false)
	private String productName;

	@AnnonOfField(desc = "skuId", notNull = false)
	private long skuId;

	@AnnonOfField(desc = "商品数量", notNull = false)
	private int productCount;

	@AnnonOfField(desc = "货到付款")
	private boolean cashOnDelivery;

	@AnnonOfField(desc = "正向运单号", notNull = false)
	private String mailNO;

	@AnnonOfField(desc = "反向运单号", notNull = false)
	private String mailNOReturn;

	@AnnonOfField(desc = "快递状态", notNull = false)
	private OmsOrderPackageState omsOrderPackageState;

	@AnnonOfField(desc = "配送类型", notNull = false)
	private String mailType;

	@AnnonOfField(desc = "电话", notNull = false)
	private String consigneeMobile;

	@AnnonOfField(desc = "姓名", notNull = false)
	private String consigneeName;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long modifyTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public long getOmsOrderFormId() {
		return OmsOrderFormId;
	}

	public void setOmsOrderFormId(long omsOrderFormId) {
		OmsOrderFormId = omsOrderFormId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public boolean isCashOnDelivery() {
		return cashOnDelivery;
	}

	public void setCashOnDelivery(boolean cashOnDelivery) {
		this.cashOnDelivery = cashOnDelivery;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public String getMailNOReturn() {
		return mailNOReturn;
	}

	public void setMailNOReturn(String mailNOReturn) {
		this.mailNOReturn = mailNOReturn;
	}

	public OmsOrderPackageState getOmsOrderPackageState() {
		return omsOrderPackageState;
	}

	public void setOmsOrderPackageState(OmsOrderPackageState omsOrderPackageState) {
		this.omsOrderPackageState = omsOrderPackageState;
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

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

}
