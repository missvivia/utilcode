package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.enums.SupplyMode;

/**
 * PO副表。 主要为CMS和backend等对性能要求不高的系统使用。
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "档期副表", tableName = "Mmall_SaleSchedule_ScheduleVice", policy = "scheduleId")
public class ScheduleVice implements Serializable {

	private static final long serialVersionUID = 3503426746616113879L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "档期id")
	private long scheduleId;

	@AnnonOfField(desc = "PO创建人Id")
	private long userId;

	@AnnonOfField(desc = "PO创建人名称", type = "VARCHAR(64)", notNull = false)
	private String createUser;

	@AnnonOfField(desc = "PO跟进人id", notNull = false)
	private long poFollowerUserId;

	@AnnonOfField(desc = "PO跟进人名称", notNull = false)
	private String poFollowerUserName;

	@AnnonOfField(desc = "檔期商家账号", notNull = false)
	private String supplierAcct;
	
	@AnnonOfField(desc = "商家类型1-代理商 2-品牌商", notNull = false, type="tinyint(3)")
	private int supplierType;
	
	@AnnonOfField(desc = "品牌商账号", notNull = false)
	private String brandSupplierName;
	
	@AnnonOfField(desc = "品牌商Id", notNull = false)
	private long brandSupplierId;
	
	@AnnonOfField(desc = "PO类型  1-代理商自己供货  2品牌商自己供货  3-代理商品牌商共同供货", notNull = false, type="tinyint(3)")
	private int poType;
	
//	// TODO to be deleted begin
//	@AnnonOfField(desc = "毛利率", notNull = false)
//	private BigDecimal grossProfitRate;
//
//	@AnnonOfField(desc = "供货价", notNull = false)
//	private BigDecimal cPrice;
//
//	@AnnonOfField(desc = "保底销售标记", notNull = false)
//	private boolean unlossFlag;
//	
//	@AnnonOfField(desc = "抵押比例", notNull = false)
//	private BigDecimal mortgageRate;
//	@AnnonOfField(desc = "押金", notNull = false)
//	private BigDecimal deposit;
//	// TODO to be deleted end
	
	@AnnonOfField(desc = "平台服务费比例", notNull = false)
	private BigDecimal platformSrvFeeRate;

	@AnnonOfField(desc = "销售价格区间(最高)", notNull = false)
	private BigDecimal maxPriceAfterDiscount;

	@AnnonOfField(desc = "销售价格区间(最低)", notNull = false)
	private BigDecimal minPriceAfterDiscount;

	@AnnonOfField(desc = "商品总件数", notNull = false)
	private int productTotalCnt;

	@AnnonOfField(desc = "款数", notNull = false)
	private int unitCnt;

	@AnnonOfField(desc = "SKU数", notNull = false)
	private int skuCnt;

	/**
	 * 推广位。如首页/女装。
	 */
	@AnnonOfField(desc = "推广位", notNull = false, type = "VARCHAR(64)")
	private String adPosition;

	@AnnonOfField(desc = "档期品购页审核是否通过.1-通过。0-未通过", defa = "0")
	private int flagAuditPage;

	@AnnonOfField(desc = "档期品购页id", notNull = false)
	private long pageId;

	@AnnonOfField(desc = "档期banner审核是否通过.1-通过。0-未通过", defa = "0")
	private int flagAuditBanner;

	@AnnonOfField(desc = "档期Banner id", notNull = false)
	private long bannerId;
	
	@AnnonOfField(desc = "档期商品资料审核状态，参考StatusType", notNull = false, defa = "1")
	private int flagAuditPrdzl;
	
	@AnnonOfField(desc = "档期商品清单审核状态，参考StatusType", notNull = false, defa = "1")
	private int flagAuditPrdqd;

	/**
	 * 1-商家自供货 2-共同供货
	 */
	@AnnonOfField(desc = "供货模式", notNull = false)
	private SupplyMode supplyMode;

	@AnnonOfField(desc = "代理商入库仓库", notNull = false)
	private long supplierStoreId;

	@AnnonOfField(desc = "品牌商入库仓库", notNull = false)
	private long brandStoreId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public long getPoFollowerUserId() {
		return poFollowerUserId;
	}

	public void setPoFollowerUserId(long poFollowerUserId) {
		this.poFollowerUserId = poFollowerUserId;
	}

	public String getPoFollowerUserName() {
		return poFollowerUserName;
	}

	public void setPoFollowerUserName(String poFollowerUserName) {
		this.poFollowerUserName = poFollowerUserName;
	}

	public String getSupplierAcct() {
		return supplierAcct;
	}

	public void setSupplierAcct(String supplierAcct) {
		this.supplierAcct = supplierAcct;
	}

	public int getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(int supplierType) {
		this.supplierType = supplierType;
	}

	public String getBrandSupplierName() {
		return brandSupplierName;
	}

	public void setBrandSupplierName(String brandSupplierName) {
		this.brandSupplierName = brandSupplierName;
	}

	public long getBrandSupplierId() {
		return brandSupplierId;
	}

	public void setBrandSupplierId(long brandSupplierId) {
		this.brandSupplierId = brandSupplierId;
	}
	
	public int getPoType() {
		return poType;
	}

	public void setPoType(int poType) {
		this.poType = poType;
	}

//	public BigDecimal getGrossProfitRate() {
//		return grossProfitRate;
//	}
//
//	public void setGrossProfitRate(BigDecimal grossProfitRate) {
//		this.grossProfitRate = grossProfitRate;
//	}
//
//	public BigDecimal getCPrice() {
//		return cPrice;
//	}
//
//	public void setCPrice(BigDecimal cPrice) {
//		this.cPrice = cPrice;
//	}
//
//	public boolean isUnlossFlag() {
//		return unlossFlag;
//	}
//
//	public void setUnlossFlag(boolean unlossFlag) {
//		this.unlossFlag = unlossFlag;
//	}

	public BigDecimal getPlatformSrvFeeRate() {
		return platformSrvFeeRate;
	}

	public void setPlatformSrvFeeRate(BigDecimal platformSrvFeeRate) {
		this.platformSrvFeeRate = platformSrvFeeRate;
	}

//	public BigDecimal getMortgageRate() {
//		return mortgageRate;
//	}
//
//	public void setMortgageRate(BigDecimal mortgageRate) {
//		this.mortgageRate = mortgageRate;
//	}
//
//	public BigDecimal getDeposit() {
//		return deposit;
//	}
//
//	public void setDeposit(BigDecimal deposit) {
//		this.deposit = deposit;
//	}

	public BigDecimal getMaxPriceAfterDiscount() {
		return maxPriceAfterDiscount;
	}

	public void setMaxPriceAfterDiscount(BigDecimal maxPriceAfterDiscount) {
		this.maxPriceAfterDiscount = maxPriceAfterDiscount;
	}

	public BigDecimal getMinPriceAfterDiscount() {
		return minPriceAfterDiscount;
	}

	public void setMinPriceAfterDiscount(BigDecimal minPriceAfterDiscount) {
		this.minPriceAfterDiscount = minPriceAfterDiscount;
	}

	public int getProductTotalCnt() {
		return productTotalCnt;
	}

	public void setProductTotalCnt(int productTotalCnt) {
		this.productTotalCnt = productTotalCnt;
	}

	public int getUnitCnt() {
		return unitCnt;
	}

	public void setUnitCnt(int unitCnt) {
		this.unitCnt = unitCnt;
	}

	public int getSkuCnt() {
		return skuCnt;
	}

	public void setSkuCnt(int skuCnt) {
		this.skuCnt = skuCnt;
	}

	public String getAdPosition() {
		return adPosition;
	}

	public void setAdPosition(String adPosition) {
		this.adPosition = adPosition;
	}

	public int getFlagAuditPage() {
		return flagAuditPage;
	}

	public void setFlagAuditPage(int flagAuditPage) {
		this.flagAuditPage = flagAuditPage;
	}

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public int getFlagAuditBanner() {
		return flagAuditBanner;
	}

	public void setFlagAuditBanner(int flagAuditBanner) {
		this.flagAuditBanner = flagAuditBanner;
	}

	public long getBannerId() {
		return bannerId;
	}

	public void setBannerId(long bannerId) {
		this.bannerId = bannerId;
	}

	public int getFlagAuditPrdzl() {
		return flagAuditPrdzl;
	}

	public void setFlagAuditPrdzl(int flagAuditPrdzl) {
		this.flagAuditPrdzl = flagAuditPrdzl;
	}

	public int getFlagAuditPrdqd() {
		return flagAuditPrdqd;
	}

	public void setFlagAuditPrdqd(int flagAuditPrdqd) {
		this.flagAuditPrdqd = flagAuditPrdqd;
	}

	public SupplyMode getSupplyMode() {
		return supplyMode;
	}

	public void setSupplyMode(SupplyMode supplyMode) {
		this.supplyMode = supplyMode;
	}

	public long getSupplierStoreId() {
		return supplierStoreId;
	}

	public void setSupplierStoreId(long supplierStoreId) {
		this.supplierStoreId = supplierStoreId;
	}

	public long getBrandStoreId() {
		return brandStoreId;
	}

	public void setBrandStoreId(long brandStoreId) {
		this.brandStoreId = brandStoreId;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
