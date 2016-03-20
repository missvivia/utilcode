package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.enums.CheckState;

/**
 * 档期(PO)Banner表. PO 1:1 Banner
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "档期(PO)Banner表", tableName = "Mmall_SaleSchedule_ScheduleBanner", policy = "scheduleId")
public class ScheduleBanner implements Serializable {

	private static final long serialVersionUID = 8034420929815801412L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	// 这里是商家的id
	@AnnonOfField(desc = "用户Id")
	private long userId;

	// 这里是商家的名称。
	@AnnonOfField(desc = "用户名称", notNull = false, type="VARCHAR(64)")
	private String userName;

	@AnnonOfField(desc = "档期id")
	private long scheduleId;

	// 从档期表中得到
	@AnnonOfField(desc = "销售站点id", notNull = false)
	private long saleAreaId;

	// 从档期获取
	@AnnonOfField(desc = "供应商标识", notNull = false)
	private long supplierId;

	// 从档期表中得到
	@AnnonOfField(desc = "供应商名称", notNull = false, type="VARCHAR(64)")
	private String supplierName;

	// 从档期表中得到
	@AnnonOfField(desc = "品牌ID", notNull = false)
	private long brandId;

	// 从档期表中得到
	@AnnonOfField(desc = "品牌名称", notNull = false, type="VARCHAR(64)")
	private String brandName;

	// 从档期表中得到
	@AnnonOfField(desc = "品牌名称(英文)", notNull = false, type="VARCHAR(64)")
	private String brandNameEn;

	// id和name可以二选一
	@AnnonOfField(desc = "首页上新Banner图片id", notNull = false)
	private long homeBannerImgId;

	@AnnonOfField(desc = "首页上新Banner图片URL", notNull = false, type="VARCHAR(255)")
	private String homeBannerImgUrl;

	@AnnonOfField(desc = "预告Banner图片id", notNull = false)
	private long preBannerImgId;

	@AnnonOfField(desc = "预告Banner图片URL", notNull = false, type="VARCHAR(255)")
	private String preBannerImgUrl;

	// 商家把banner体检审核时的说明
	@AnnonOfField(desc = "备注", notNull = false, type="VARCHAR(255)")
	private String comment;

	@AnnonOfField(desc = "审核状态")
	private CheckState status;

	// 当status=CheckState.CHECKING的时候填充该字段
	@AnnonOfField(desc = "最后一次提交审核的时间", notNull = false)
	private long submitDate;

	// status改变时填充该字段
	@AnnonOfField(desc = "状态更新时间", notNull = false)
	private long statusUpdateDate;

	// 审核拒绝时的理由
	@AnnonOfField(desc = "审核理由", notNull = false, type="VARCHAR(255)")
	private String statusMsg;

	// CMS审核人员的id
	@AnnonOfField(desc = "审核人id", notNull = false)
	private long auditUserId;

	// CMS审核人员的name
	@AnnonOfField(desc = "审核人名字", notNull = false, type="VARCHAR(64)")
	private String auditUserName;

	@AnnonOfField(desc = "创建日期")
	private long createDate;

	@AnnonOfField(desc = "更新日期")
	private long updateDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Deprecated
	public long getSaleAreaId() {
		return saleAreaId;
	}

	@Deprecated
	public void setSaleAreaId(long saleAreaId) {
		this.saleAreaId = saleAreaId;
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

	public long getHomeBannerImgId() {
		return homeBannerImgId;
	}

	public void setHomeBannerImgId(long homeBannerImgId) {
		this.homeBannerImgId = homeBannerImgId;
	}

	public String getHomeBannerImgUrl() {
		return homeBannerImgUrl;
	}

	public void setHomeBannerImgUrl(String homeBannerImgUrl) {
		this.homeBannerImgUrl = homeBannerImgUrl;
	}

	public long getPreBannerImgId() {
		return preBannerImgId;
	}

	public void setPreBannerImgId(long preBannerImgId) {
		this.preBannerImgId = preBannerImgId;
	}

	public String getPreBannerImgUrl() {
		return preBannerImgUrl;
	}

	public void setPreBannerImgUrl(String preBannerImgUrl) {
		this.preBannerImgUrl = preBannerImgUrl;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public CheckState getStatus() {
		return status;
	}

	public void setStatus(CheckState status) {
		this.status = status;
	}

	public long getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(long submitDate) {
		this.submitDate = submitDate;
	}

	public long getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(long statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
