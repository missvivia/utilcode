package com.xyl.mmall.saleschedule.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.enums.CheckState;

/**
 * 品购页
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "品购表", tableName = "Mmall_SaleSchedule_SchedulePage", policy = "scheduleId")
public class SchedulePage implements Serializable {

	private static final long serialVersionUID = 5127413810562834980L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	// 编辑品购页的backend登陆人员id。第一次编辑的时候会填充该字段
	@AnnonOfField(desc = "用户Id", notNull = false)
	private long userId;

	// 从档期表获得
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
	@AnnonOfField(desc = "品牌名称", notNull = false, type="VARCHAR(64)")
	private String brandName;

	// 从档期表中得到
	@AnnonOfField(desc = "品牌名称(英文)", notNull = false, type="VARCHAR(64)")
	private String brandNameEn;

	// 从档期表中得到
	@AnnonOfField(desc = "品牌ID", notNull = false)
	private long brandId;

	@AnnonOfField(desc = "创建日期")
	private long createDate;

	// 编辑品购页的backend登陆人员名字。第一次编辑的时候会填充该字段
	@AnnonOfField(desc = "创建人", notNull = false, type="VARCHAR(64)")
	private String createPerson;

	@AnnonOfField(desc = "品购页title", notNull = false, type="VARCHAR(64)")
	private String title;

	@AnnonOfField(desc = "品购页状态")
	private CheckState status;

	@AnnonOfField(desc = "状态更新状态")
	private long statusUpdateDate;

	/**
	 * 如“图片缺失”等。
	 */
	@AnnonOfField(desc = "审核理由", notNull = false, type="VARCHAR(255)")
	private String statusMsg;

	@AnnonOfField(desc = "备注", notNull = false, type="VARCHAR(255)")
	private String comment;

	@AnnonOfField(desc = "品购页审核提交次数", notNull = false)
	private int updateCnt;

	@AnnonOfField(desc = "产品id列表，逗号分隔", notNull = false, type = "varchar(512)")
	private String udProductIds;

	@AnnonOfField(desc = "用户自定义组件设置JSON字符串", notNull = false, type = "text")
	private String udSetting;

	@AnnonOfField(desc = "组件涉及到的图片id列表，逗号分隔", notNull = false, type = "varchar(512)")
	private String udImgIds;

	// *******************
	// 背景模板组件
	// *******************
	@AnnonOfField(desc = "背景图片id", notNull = false)
	private long bgImgId;

	@AnnonOfField(desc = "背景设置相关JSON字符串", notNull = false, type = "varchar(512)")
	private String bgSetting;

	// *******************
	// 头图组件
	// *******************
	@AnnonOfField(desc = "头图图片id", notNull = false)
	private long headerImgId;

	@AnnonOfField(desc = "头图设置相关JSON字符串", notNull = false, type = "text")
	private String headerSetting;

	// *******************
	// 全部商品组件模块。
	// 对应的商品存于ScheduleSkuRela表中
	// *******************
	@AnnonOfField(desc = "全部商品列表组件是否可见", defa = "true")
	private boolean allListPartVisiable = true;

	// 格式根据前台自己定义。可能是JSON格式
	@AnnonOfField(desc = "全部商品列表组件其他信息", notNull = false, type = "text")
	private String allListPartOthers;

	// *******************
	// 商品地图组件模块
	// 对应的店面信息从品牌模块查询
	// *******************
	@AnnonOfField(desc = "商品地图组件是否可见", defa = "true")
	private boolean mapPartVisiable = true;

	// 格式根据前台自己定义。可能是JSON格式
	@AnnonOfField(desc = "商品地图组件其他信息", notNull = false, type = "text")
	private String mapPartOthers;

	// 格式根据前台自己定义。
	@AnnonOfField(desc = "品购页各个组件之间的排序顺序", notNull = false)
	private String partDisplayOrderList;

	@AnnonOfField(desc = "商品列表排序类型", defa = "0")
	private int prdListOrderType;

	// getter and setter

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

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CheckState getStatus() {
		return status;
	}

	public void setStatus(CheckState status) {
		this.status = status;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getUpdateCnt() {
		return updateCnt;
	}

	public void setUpdateCnt(int updateCnt) {
		this.updateCnt = updateCnt;
	}

	public String getUdProductIds() {
		return udProductIds;
	}

	public void setUdProductIds(String udProductIds) {
		this.udProductIds = udProductIds;
	}

	public String getUdSetting() {
		return udSetting;
	}

	public void setUdSetting(String udSetting) {
		this.udSetting = udSetting;
	}

	public String getUdImgIds() {
		return udImgIds;
	}

	public void setUdImgIds(String udImgIds) {
		this.udImgIds = udImgIds;
	}

	public long getBgImgId() {
		return bgImgId;
	}

	public void setBgImgId(long bgImgId) {
		this.bgImgId = bgImgId;
	}

	public String getBgSetting() {
		return bgSetting;
	}

	public void setBgSetting(String bgSetting) {
		this.bgSetting = bgSetting;
	}

	public long getHeaderImgId() {
		return headerImgId;
	}

	public void setHeaderImgId(long headerImgId) {
		this.headerImgId = headerImgId;
	}

	public String getHeaderSetting() {
		return headerSetting;
	}

	public void setHeaderSetting(String headerSetting) {
		this.headerSetting = headerSetting;
	}

	public boolean isAllListPartVisiable() {
		return allListPartVisiable;
	}

	public void setAllListPartVisiable(boolean allListPartVisiable) {
		this.allListPartVisiable = allListPartVisiable;
	}

	public String getAllListPartOthers() {
		return allListPartOthers;
	}

	public void setAllListPartOthers(String allListPartOthers) {
		this.allListPartOthers = allListPartOthers;
	}

	public boolean isMapPartVisiable() {
		return mapPartVisiable;
	}

	public void setMapPartVisiable(boolean mapPartVisiable) {
		this.mapPartVisiable = mapPartVisiable;
	}

	public String getMapPartOthers() {
		return mapPartOthers;
	}

	public void setMapPartOthers(String mapPartOthers) {
		this.mapPartOthers = mapPartOthers;
	}

	public String getPartDisplayOrderList() {
		return partDisplayOrderList;
	}

	public void setPartDisplayOrderList(String partDisplayOrderList) {
		this.partDisplayOrderList = partDisplayOrderList;
	}

	public int getPrdListOrderType() {
		return prdListOrderType;
	}

	public void setPrdListOrderType(int prdListOrderType) {
		this.prdListOrderType = prdListOrderType;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
