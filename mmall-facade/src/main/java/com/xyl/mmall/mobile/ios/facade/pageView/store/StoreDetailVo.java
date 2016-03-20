package com.xyl.mmall.mobile.ios.facade.pageView.store;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

public class StoreDetailVo {

	private long id;

	@AnnonOfField(desc = "店铺名称", notNull = false)
	private String storeName;

	@AnnonOfField(desc = "起批金额")
	private BigDecimal batchCash;

	@JsonIgnore
	@AnnonOfField(desc = "登录账号")
	private String businessAccount;

	@JsonIgnore
	@AnnonOfField(desc = "品牌名称", notNull = false)
	private long actingBrandId;
	@JsonIgnore
	@AnnonOfField(desc = "公司名称", notNull = false)
	private String companyName;
	@JsonIgnore
	@AnnonOfField(desc = "公司介绍")
	private String companyDesc;
	@JsonIgnore
	@AnnonOfField(desc = "注册资金")
	private BigDecimal registerFund = BigDecimal.ZERO;
	@JsonIgnore
	@AnnonOfField(desc = "法人姓名", notNull = false)
	private String legalPerson;
	@JsonIgnore
	@AnnonOfField(desc = "法人身份证", notNull = false)
	private String legalPersonID;
	@JsonIgnore
	@AnnonOfField(desc = "开户人姓名", notNull = false)
	private String holderName;
	@JsonIgnore
	@AnnonOfField(desc = "开户人身份证", notNull = false)
	private String holderID;
	@JsonIgnore
	@AnnonOfField(desc = "开户人身份证扫描件正面")
	private String holderIDPositiveImg;
	@JsonIgnore
	@AnnonOfField(desc = "开户人身份证扫描件反面")
	private String holderIDNegativeImg;
	@JsonIgnore
	@AnnonOfField(desc = "营业执照编号")
	private String registrationNumber;
	@JsonIgnore
	@AnnonOfField(desc = "营业执照有效期开始")
	private long registrationNumberStart;
	@JsonIgnore
	@AnnonOfField(desc = "营业执照编号有效期结束")
	private long registrationNumberEnd;
	@JsonIgnore
	@AnnonOfField(desc = "营业执照编号有效期是否长期有效", notNull = false)
	private int registrationNumberAvaliable;
	@JsonIgnore
	@AnnonOfField(desc = "营业执照扫描件正本")
	private String registrationImg;
	@JsonIgnore
	@AnnonOfField(desc = "营业执照扫描件副本")
	private String registrationCopyImg;
	@JsonIgnore
	@AnnonOfField(desc = "银行开户证明扫描件")
	private String accountLicense;
	@JsonIgnore
	@AnnonOfField(desc = "商品标注册登记扫描件", notNull = false)
	private String brandImg;
	@JsonIgnore
	@AnnonOfField(desc = "品牌使用授权扫描件")
	private String brandAuthImg;
	@JsonIgnore
	@AnnonOfField(desc = "联系人姓名", notNull = false)
	private String contactName;
	@JsonIgnore
	@AnnonOfField(desc = "联系人手机号", notNull = false)
	private String contactTel;
	@JsonIgnore
	@AnnonOfField(desc = "联系人邮箱")
	private String contactEmail;
	@JsonIgnore
	@AnnonOfField(desc = "联系人省份")
	private String contactProvince;
	@JsonIgnore
	@AnnonOfField(desc = "联系人市")
	private String contactCity;
	@JsonIgnore
	@AnnonOfField(desc = "联系人区县")
	private String contactCountry;
	@JsonIgnore
	@AnnonOfField(desc = "联系人地址")
	private String contactAddress;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "更新时间")
	private long updateTime;
	@JsonIgnore
	@AnnonOfField(desc = "创建人ID")
	private long creatorId;
	@JsonIgnore
	@AnnonOfField(desc = "是否激活状态,0激活,1冻结")
	private int isActive;
	@JsonIgnore
	@AnnonOfField(desc = "商家类型")
	private int type;
	@JsonIgnore
	@AnnonOfField(desc = "经营类别")
	private String manageType;
	@JsonIgnore
	@AnnonOfField(desc = "修改人")
	private long updateBy;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public BigDecimal getBatchCash() {
		return batchCash;
	}
	public void setBatchCash(BigDecimal batchCash) {
		this.batchCash = batchCash;
	}
	public String getBusinessAccount() {
		return businessAccount;
	}
	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}
	public long getActingBrandId() {
		return actingBrandId;
	}
	public void setActingBrandId(long actingBrandId) {
		this.actingBrandId = actingBrandId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyDesc() {
		return companyDesc;
	}
	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}
	public BigDecimal getRegisterFund() {
		return registerFund;
	}
	public void setRegisterFund(BigDecimal registerFund) {
		this.registerFund = registerFund;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getLegalPersonID() {
		return legalPersonID;
	}
	public void setLegalPersonID(String legalPersonID) {
		this.legalPersonID = legalPersonID;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	public String getHolderID() {
		return holderID;
	}
	public void setHolderID(String holderID) {
		this.holderID = holderID;
	}
	public String getHolderIDPositiveImg() {
		return holderIDPositiveImg;
	}
	public void setHolderIDPositiveImg(String holderIDPositiveImg) {
		this.holderIDPositiveImg = holderIDPositiveImg;
	}
	public String getHolderIDNegativeImg() {
		return holderIDNegativeImg;
	}
	public void setHolderIDNegativeImg(String holderIDNegativeImg) {
		this.holderIDNegativeImg = holderIDNegativeImg;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public long getRegistrationNumberStart() {
		return registrationNumberStart;
	}
	public void setRegistrationNumberStart(long registrationNumberStart) {
		this.registrationNumberStart = registrationNumberStart;
	}
	public long getRegistrationNumberEnd() {
		return registrationNumberEnd;
	}
	public void setRegistrationNumberEnd(long registrationNumberEnd) {
		this.registrationNumberEnd = registrationNumberEnd;
	}
	public int getRegistrationNumberAvaliable() {
		return registrationNumberAvaliable;
	}
	public void setRegistrationNumberAvaliable(int registrationNumberAvaliable) {
		this.registrationNumberAvaliable = registrationNumberAvaliable;
	}
	public String getRegistrationImg() {
		return registrationImg;
	}
	public void setRegistrationImg(String registrationImg) {
		this.registrationImg = registrationImg;
	}
	public String getRegistrationCopyImg() {
		return registrationCopyImg;
	}
	public void setRegistrationCopyImg(String registrationCopyImg) {
		this.registrationCopyImg = registrationCopyImg;
	}
	public String getAccountLicense() {
		return accountLicense;
	}
	public void setAccountLicense(String accountLicense) {
		this.accountLicense = accountLicense;
	}
	public String getBrandImg() {
		return brandImg;
	}
	public void setBrandImg(String brandImg) {
		this.brandImg = brandImg;
	}
	public String getBrandAuthImg() {
		return brandAuthImg;
	}
	public void setBrandAuthImg(String brandAuthImg) {
		this.brandAuthImg = brandAuthImg;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactProvince() {
		return contactProvince;
	}
	public void setContactProvince(String contactProvince) {
		this.contactProvince = contactProvince;
	}
	public String getContactCity() {
		return contactCity;
	}
	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}
	public String getContactCountry() {
		return contactCountry;
	}
	public void setContactCountry(String contactCountry) {
		this.contactCountry = contactCountry;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
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
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getManageType() {
		return manageType;
	}
	public void setManageType(String manageType) {
		this.manageType = manageType;
	}
	public long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}
	
	
}
