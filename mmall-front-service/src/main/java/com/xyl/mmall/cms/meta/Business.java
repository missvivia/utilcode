package com.xyl.mmall.cms.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

@AnnonOfClass(desc = "商家信息", tableName = "Mmall_CMS_Business")
public class Business implements Serializable {

	private static final long serialVersionUID = 20140915L;

	@AnnonOfField(desc = "商家信息id", primary = true, autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "店铺名称", notNull = false)
	private String storeName;

	@AnnonOfField(desc = "起批金额")
	private BigDecimal batchCash;

	@AnnonOfField(desc = "登录账号")
	private String businessAccount;

	@AnnonOfField(desc = "品牌名称", notNull = false)
	private long actingBrandId;

	@AnnonOfField(desc = "公司名称", notNull = false)
	private String companyName;

	@AnnonOfField(desc = "公司介绍")
	private String companyDesc;

	@AnnonOfField(desc = "注册资金")
	private BigDecimal registerFund = BigDecimal.ZERO;

	@AnnonOfField(desc = "法人姓名", notNull = false)
	private String legalPerson;

	@AnnonOfField(desc = "法人身份证", notNull = false)
	private String legalPersonID;

	@AnnonOfField(desc = "开户人姓名", notNull = false)
	private String holderName;

	@AnnonOfField(desc = "开户人身份证", notNull = false)
	private String holderID;

	@AnnonOfField(desc = "开户人身份证扫描件正面")
	private String holderIDPositiveImg;

	@AnnonOfField(desc = "开户人身份证扫描件反面")
	private String holderIDNegativeImg;

	@AnnonOfField(desc = "营业执照编号")
	private String registrationNumber;

	@AnnonOfField(desc = "营业执照有效期开始")
	private long registrationNumberStart;

	@AnnonOfField(desc = "营业执照编号有效期结束")
	private long registrationNumberEnd;

	@AnnonOfField(desc = "营业执照编号有效期是否长期有效", notNull = false)
	private int registrationNumberAvaliable;

	@AnnonOfField(desc = "营业执照扫描件正本")
	private String registrationImg;

	@AnnonOfField(desc = "营业执照扫描件副本")
	private String registrationCopyImg;

	@AnnonOfField(desc = "银行开户证明扫描件")
	private String accountLicense;

	@AnnonOfField(desc = "商品标注册登记扫描件", notNull = false)
	private String brandImg;

	@AnnonOfField(desc = "品牌使用授权扫描件")
	private String brandAuthImg;

	@AnnonOfField(desc = "联系人姓名", notNull = false)
	private String contactName;

	@AnnonOfField(desc = "联系人手机号", notNull = false)
	private String contactTel;

	@AnnonOfField(desc = "联系人邮箱")
	private String contactEmail;

	@AnnonOfField(desc = "联系人省份")
	private String contactProvince;

	@AnnonOfField(desc = "联系人市")
	private String contactCity;

	@AnnonOfField(desc = "联系人区县")
	private String contactCountry;

	@AnnonOfField(desc = "联系人地址")
	private String contactAddress;

	@AnnonOfField(desc = "退货联系人")
	@Deprecated
	private String returnContactName;

	@AnnonOfField(desc = "退货联系人电话")
	@Deprecated
	private String returnContactTel;

	@AnnonOfField(desc = "退货省份")
	@Deprecated
	private String returnProvince;

	@AnnonOfField(desc = "退货市")
	@Deprecated
	private String returnCity;

	@AnnonOfField(desc = "退货区县")
	@Deprecated
	private String returnCountry;

	@AnnonOfField(desc = "退货地址")
	@Deprecated
	private String returnAddress;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "更新时间")
	private long updateTime;

	@AnnonOfField(desc = "创建人ID")
	private long creatorId;

	@AnnonOfField(desc = "是否激活状态,0激活,1冻结")
	private int isActive;

	@AnnonOfField(desc = "商家类型")
	private int type;

	@AnnonOfField(desc = "经营类别")
	private String manageType;

	@Deprecated
	private long areaId;

	@AnnonOfField(desc = "站点id")
	private long siteId;

	@AnnonOfField(desc = "修改人")
	private long updateBy;

	@AnnonOfField(desc = "店铺首页显示权重")
	private int indexWeight;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getReturnContactName() {
		return returnContactName;
	}

	public void setReturnContactName(String returnContactName) {
		this.returnContactName = returnContactName;
	}

	public String getReturnContactTel() {
		return returnContactTel;
	}

	public void setReturnContactTel(String returnContactTel) {
		this.returnContactTel = returnContactTel;
	}

	public String getReturnProvince() {
		return returnProvince;
	}

	public void setReturnProvince(String returnProvince) {
		this.returnProvince = returnProvince;
	}

	public String getReturnCity() {
		return returnCity;
	}

	public void setReturnCity(String returnCity) {
		this.returnCity = returnCity;
	}

	public String getReturnCountry() {
		return returnCountry;
	}

	public void setReturnCountry(String returnCountry) {
		this.returnCountry = returnCountry;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
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

	@Deprecated
	public long getAreaId() {
		return areaId;
	}

	@Deprecated
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public BigDecimal getBatchCash() {
		return batchCash;
	}

	public void setBatchCash(BigDecimal batchCash) {
		this.batchCash = batchCash;
	}

	public long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}

	public int getIndexWeight() {
		return indexWeight;
	}

	public void setIndexWeight(int indexWeight) {
		this.indexWeight = indexWeight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountLicense == null) ? 0 : accountLicense.hashCode());
		result = prime * result + (int) (actingBrandId ^ (actingBrandId >>> 32));
		result = prime * result + (int) (siteId ^ (siteId >>> 32));
		result = prime * result + (int) (areaId ^ (areaId >>> 32));
		result = prime * result + ((brandAuthImg == null) ? 0 : brandAuthImg.hashCode());
		result = prime * result + ((brandImg == null) ? 0 : brandImg.hashCode());
		result = prime * result + ((businessAccount == null) ? 0 : businessAccount.hashCode());
		result = prime * result + ((companyDesc == null) ? 0 : companyDesc.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((contactAddress == null) ? 0 : contactAddress.hashCode());
		result = prime * result + ((contactCity == null) ? 0 : contactCity.hashCode());
		result = prime * result + ((contactCountry == null) ? 0 : contactCountry.hashCode());
		result = prime * result + ((contactEmail == null) ? 0 : contactEmail.hashCode());
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		result = prime * result + ((contactProvince == null) ? 0 : contactProvince.hashCode());
		result = prime * result + ((contactTel == null) ? 0 : contactTel.hashCode());
		result = prime * result + (int) (createTime ^ (createTime >>> 32));
		result = prime * result + (int) (creatorId ^ (creatorId >>> 32));
		result = prime * result + ((holderID == null) ? 0 : holderID.hashCode());
		result = prime * result + ((holderIDNegativeImg == null) ? 0 : holderIDNegativeImg.hashCode());
		result = prime * result + ((holderIDPositiveImg == null) ? 0 : holderIDPositiveImg.hashCode());
		result = prime * result + ((holderName == null) ? 0 : holderName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + isActive;
		result = prime * result + ((legalPerson == null) ? 0 : legalPerson.hashCode());
		result = prime * result + ((legalPersonID == null) ? 0 : legalPersonID.hashCode());
		result = prime * result + ((manageType == null) ? 0 : manageType.hashCode());
		result = prime * result + ((registerFund == null) ? 0 : registerFund.hashCode());
		result = prime * result + ((registrationCopyImg == null) ? 0 : registrationCopyImg.hashCode());
		result = prime * result + ((registrationImg == null) ? 0 : registrationImg.hashCode());
		result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
		result = prime * result + registrationNumberAvaliable;
		result = prime * result + (int) (registrationNumberEnd ^ (registrationNumberEnd >>> 32));
		result = prime * result + (int) (registrationNumberStart ^ (registrationNumberStart >>> 32));
		result = prime * result + ((returnAddress == null) ? 0 : returnAddress.hashCode());
		result = prime * result + ((returnCity == null) ? 0 : returnCity.hashCode());
		result = prime * result + ((returnContactName == null) ? 0 : returnContactName.hashCode());
		result = prime * result + ((returnContactTel == null) ? 0 : returnContactTel.hashCode());
		result = prime * result + ((returnCountry == null) ? 0 : returnCountry.hashCode());
		result = prime * result + ((returnProvince == null) ? 0 : returnProvince.hashCode());
		result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
		result = prime * result + ((batchCash == null) ? 0 : batchCash.hashCode());
		result = prime * result + type;
		result = prime * result + (int) (updateTime ^ (updateTime >>> 32));
		result = prime * result + (int) (updateBy ^ (updateBy >>> 32));
		result = prime * result + indexWeight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Business other = (Business) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
