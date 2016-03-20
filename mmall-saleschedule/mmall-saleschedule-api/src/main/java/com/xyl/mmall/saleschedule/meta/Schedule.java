package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.enums.JITMode;
import com.xyl.mmall.saleschedule.enums.ScheduleState;

/**
 * 档期PO表， 根据省份进行均衡
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "档期PO表", tableName = "Mmall_SaleSchedule_Schedule", policy = "id", dbCreateTimeName = "CreateTime")
public class Schedule implements Serializable {

	private static final long serialVersionUID = -464487141446357052L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id", notNull = false)
	private long userId;

	@AnnonOfField(desc = "开始时间")
	private long startTime;

	/** 档期现在默认是5天. */
	@AnnonOfField(desc = "结束时间")
	private long endTime;

	@AnnonOfField(desc = "调整档期日期的原因", notNull = false, type="VARCHAR(255)")
	private String adjustTimeDesc;

	/** 如浙江等. */
	@AnnonOfField(desc = "当前供应商所在站点")
	private long curSupplierAreaId;

	@AnnonOfField(desc = "供应商标识")
	private long supplierId;

	@AnnonOfField(desc = "供应商公司名称", notNull = false, type="VARCHAR(64)")
	private String supplierName;

	@AnnonOfField(desc = "品牌标识")
	private long brandId;

	@AnnonOfField(desc = "品牌名称", notNull = false, type="VARCHAR(64)")
	private String brandName;

	@AnnonOfField(desc = "品牌名称(英文)", notNull = false, type="VARCHAR(64)")
	private String brandNameEn;

	@AnnonOfField(desc = "品牌Logo URL", notNull = false, type="VARCHAR(255)")
	private String brandLogo;

	@AnnonOfField(desc = "品牌Logo URL", notNull = false, type="VARCHAR(255)")
	private String brandLogoSmall;

	@AnnonOfField(desc = "档期名称", type="VARCHAR(64)")
	private String title;

	// *************************
	// cowork condition
	// *************************
	@AnnonOfField(desc = "毛利率", notNull = false)
	private BigDecimal grossProfitRate;

	@AnnonOfField(desc = "供货价", notNull = false)
	private BigDecimal cPrice;

	@AnnonOfField(desc = "保底销售标记", notNull = false)
	private boolean unlossFlag;

	@AnnonOfField(desc = "平台服务费比例", notNull = false)
	private BigDecimal platformSrvFeeRate;

	@AnnonOfField(desc = "抵押比例", notNull = false)
	private BigDecimal mortgageRate;

	@AnnonOfField(desc = "押金", notNull = false)
	private BigDecimal deposit;

	@AnnonOfField(desc = "折后价格带(最高)", notNull = false)
	private BigDecimal maxPriceAfterDiscount;

	@AnnonOfField(desc = "折后价格带(最低)", notNull = false)
	private BigDecimal minPriceAfterDiscount;

	// *************************
	// product detail info
	// *************************
	@AnnonOfField(desc = "商品总件数", notNull = false)
	private int productTotalCnt;

	@AnnonOfField(desc = "最大折扣", notNull = false)
	private BigDecimal maxDiscount;

	@AnnonOfField(desc = "最小折扣", notNull = false)
	private BigDecimal minDiscount;

	@AnnonOfField(desc = "款数", notNull = false)
	private int unitCnt;

	@AnnonOfField(desc = "SKU数", notNull = false)
	private int skuCnt;

	// *************************
	// sale and store info
	// *************************
	/**
	 * 如浙江等
	 */
	@AnnonOfField(desc = "销售区域", notNull = false)
	private long saleAreaId;

	/**
	 * 如浙江等
	 */
	@AnnonOfField(desc = "入库站点", notNull = false)
	private long storeAreaId;

	// 默认是JIT
	@AnnonOfField(desc = "仓储合作模式")
	private JITMode jitMode = JITMode.JIT;

	// --------------- end --------------

	/**
	 * 在档期列表上的显示顺序。数字越小，显示越靠上. 0-255
	 */
	@AnnonOfField(desc = "显示顺序", notNull = false)
	private int showOrder;

	/**
	 * 推广位。如首页/女装。
	 */
	@AnnonOfField(desc = "推广位", notNull = false, type="VARCHAR(64)")
	private String adPosition;
	
	@AnnonOfField(desc = "档期频道异或标识位", notNull = false)
	private long chlFlag;

	@AnnonOfField(desc = "创建时间", dbFieldName="createTimeForLogic")
	private long createTimeForLogic;

	/**
	 * 档期创建人。
	 */
	@AnnonOfField(desc = "创建人", type="VARCHAR(64)", notNull = false)
	private String createUser;

	@AnnonOfField(desc = "档期状态")
	private ScheduleState status;

	@AnnonOfField(desc = "状态更新时间", dbFieldName="updateTimeForLogic")
	private long updateTimeForLogic;

	@AnnonOfField(desc = "审核理由", notNull = false, type="VARCHAR(255)")
	private String statusMsg;

	@AnnonOfField(desc = "档期商品审核是否通过.Refer StatusType.java", defa = "0")
	private int flagAuditPrdList;

	@AnnonOfField(desc = "档期品购页审核是否通过.1-通过。0-未通过", defa = "0", notNull = false)
	private int flagAuditPage;

	@AnnonOfField(desc = "档期品购页id", notNull = false)
	private long pageId;

	@AnnonOfField(desc = "档期banner审核是否通过.1-通过。0-未通过", defa = "0", notNull = false)
	private int flagAuditBanner;

	@AnnonOfField(desc = "档期Banner id", notNull = false)
	private long bannerId;

	/**
	 * 档期更新时间。比如对档期修改了时间等操作。
	 */
	@AnnonOfField(desc = "档期更新时间")
	private long scheduleUpdateDate;
	
	// 新增字段
	@AnnonOfField(desc = "品购页title", notNull = false)
	private String pageTitle;

	@AnnonOfField(desc = "正常展示天数", notNull = false)
	private int normalShowPeriod;
	
	@AnnonOfField(desc = "延期展示天数", notNull = false)
	private int extShowPeriod;
	
	/**
	 * 档期所属各个站点异或以后的值。
	 */
	@AnnonOfField(desc = "档期销售站点异或标识位", notNull = false)
	private long saleSiteFlag;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Deprecated
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getAdjustTimeDesc() {
		return adjustTimeDesc;
	}

	public void setAdjustTimeDesc(String adjustTimeDesc) {
		this.adjustTimeDesc = adjustTimeDesc;
	}

	@Deprecated
	public long getCurSupplierAreaId() {
		return curSupplierAreaId;
	}

	@Deprecated
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

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

	public String getBrandLogoSmall() {
		return brandLogoSmall;
	}

	public void setBrandLogoSmall(String brandLogoSmall) {
		this.brandLogoSmall = brandLogoSmall;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Deprecated
	public BigDecimal getGrossProfitRate() {
		return grossProfitRate;
	}

	public void setGrossProfitRate(BigDecimal grossProfitRate) {
		this.grossProfitRate = grossProfitRate;
	}

	@Deprecated
	public BigDecimal getCPrice() {
		return cPrice;
	}

	public void setCPrice(BigDecimal cPrice) {
		this.cPrice = cPrice;
	}

	@Deprecated
	public boolean isUnlossFlag() {
		return unlossFlag;
	}

	public void setUnlossFlag(boolean unlossFlag) {
		this.unlossFlag = unlossFlag;
	}

	@Deprecated
	public BigDecimal getPlatformSrvFeeRate() {
		return platformSrvFeeRate;
	}

	public void setPlatformSrvFeeRate(BigDecimal platformSrvFeeRate) {
		this.platformSrvFeeRate = platformSrvFeeRate;
	}

	@Deprecated
	public BigDecimal getMortgageRate() {
		return mortgageRate;
	}

	public void setMortgageRate(BigDecimal mortgageRate) {
		this.mortgageRate = mortgageRate;
	}

	@Deprecated
	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	@Deprecated
	public BigDecimal getMaxPriceAfterDiscount() {
		return maxPriceAfterDiscount;
	}

	public void setMaxPriceAfterDiscount(BigDecimal maxPriceAfterDiscount) {
		this.maxPriceAfterDiscount = maxPriceAfterDiscount;
	}

	@Deprecated
	public BigDecimal getMinPriceAfterDiscount() {
		return minPriceAfterDiscount;
	}

	public void setMinPriceAfterDiscount(BigDecimal minPriceAfterDiscount) {
		this.minPriceAfterDiscount = minPriceAfterDiscount;
	}

	@Deprecated
	public int getProductTotalCnt() {
		return productTotalCnt;
	}

	public void setProductTotalCnt(int productTotalCnt) {
		this.productTotalCnt = productTotalCnt;
	}

	public BigDecimal getMaxDiscount() {
		return maxDiscount;
	}

	public void setMaxDiscount(BigDecimal maxDiscount) {
		this.maxDiscount = maxDiscount;
	}

	public BigDecimal getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(BigDecimal minDiscount) {
		this.minDiscount = minDiscount;
	}

	@Deprecated
	public int getUnitCnt() {
		return unitCnt;
	}

	public void setUnitCnt(int unitCnt) {
		this.unitCnt = unitCnt;
	}

	@Deprecated
	public int getSkuCnt() {
		return skuCnt;
	}

	public void setSkuCnt(int skuCnt) {
		this.skuCnt = skuCnt;
	}

	@Deprecated
	public long getSaleAreaId() {
		return saleAreaId;
	}

	@Deprecated
	public void setSaleAreaId(long saleAreaId) {
		this.saleAreaId = saleAreaId;
	}

	@Deprecated
	public long getStoreAreaId() {
		return storeAreaId;
	}

	@Deprecated
	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

	public JITMode getJitMode() {
		return jitMode;
	}

	public void setJitMode(JITMode jitMode) {
		this.jitMode = jitMode;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public String getAdPosition() {
		return adPosition;
	}

	public void setAdPosition(String adPosition) {
		this.adPosition = adPosition;
	}

	public long getChlFlag() {
		return chlFlag;
	}

	public void setChlFlag(long chlFlag) {
		this.chlFlag = chlFlag;
	}

	@Deprecated
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public ScheduleState getStatus() {
		return status;
	}

	public void setStatus(ScheduleState status) {
		this.status = status;
	}
	
	public long getCreateTimeForLogic() {
		return createTimeForLogic;
	}

	public void setCreateTimeForLogic(long createTime) {
		this.createTimeForLogic = createTime;
	}

	public long getUpdateTimeForLogic() {
		return updateTimeForLogic;
	}

	public void setUpdateTimeForLogic(long updateTime) {
		this.updateTimeForLogic = updateTime;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public int getFlagAuditPrdList() {
		return flagAuditPrdList;
	}

	public void setFlagAuditPrdList(int flagAuditPrdList) {
		this.flagAuditPrdList = flagAuditPrdList;
	}

	@Deprecated
	public int getFlagAuditPage() {
		return flagAuditPage;
	}

	public void setFlagAuditPage(int flagAuditPage) {
		this.flagAuditPage = flagAuditPage;
	}

	@Deprecated
	public int getFlagAuditBanner() {
		return flagAuditBanner;
	}

	public void setFlagAuditBanner(int flagAuditBanner) {
		this.flagAuditBanner = flagAuditBanner;
	}

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public long getBannerId() {
		return bannerId;
	}

	public void setBannerId(long bannerId) {
		this.bannerId = bannerId;
	}

	public long getScheduleUpdateDate() {
		return scheduleUpdateDate;
	}

	public void setScheduleUpdateDate(long scheduleUpdateDate) {
		this.scheduleUpdateDate = scheduleUpdateDate;
	}
	
	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public int getNormalShowPeriod() {
		return normalShowPeriod;
	}

	public void setNormalShowPeriod(int normalShowPeriod) {
		this.normalShowPeriod = normalShowPeriod;
	}

	public int getExtShowPeriod() {
		return extShowPeriod;
	}

	public void setExtShowPeriod(int extShowPeriod) {
		this.extShowPeriod = extShowPeriod;
	}

	public long getSaleSiteFlag() {
		return saleSiteFlag;
	}

	public void setSaleSiteFlag(long saleSiteFlag) {
		this.saleSiteFlag = saleSiteFlag;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
