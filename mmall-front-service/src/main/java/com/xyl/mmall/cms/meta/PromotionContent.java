package com.xyl.mmall.cms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.cms.enums.PromotionContentType;


/**
 * 
 * PromotionContent.java created by yydx811 at 2015年6月23日 上午11:04:04
 * 推广banner
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "推广内容", tableName = "Mmall_CMS_PromotionContent")
public class PromotionContent implements Serializable {
	
	private static final long serialVersionUID = 20140920L;
	
	@AnnonOfField(desc = "推广内容id", primary = true,autoAllocateId =true)
	private long id;
	
	@AnnonOfField(desc = "区域id")
	private long areaId;
	
	@AnnonOfField(desc = "tab类型，1首页，2女装，3男装，4鞋包, 5童装，6家纺", notNull=false)
	private int positionId;
	
	@AnnonOfField(desc = "推广类型，0：p0,1:活动")
	private int promotionType;
	
	@AnnonOfField(desc = "图片链接地址",type="varchar(255)", notNull=false)
	private String imgUrl;
	
	@AnnonOfField(desc = "图片2链接地址",type="varchar(255)", notNull=false)
	private String imgUrl2;
	
	@AnnonOfField(desc = "推广链接地址",type="varchar(255)", notNull=false)
	private String activityUrl;
	
	@AnnonOfField(desc = "商家id", notNull=false)
	private long businessId;
	
	@AnnonOfField(desc = "显示位置,0:web,1:mobile")
	private PromotionContentType device;

	@AnnonOfField(desc = "平台",type="varchar(50)", notNull=false)
	private String platformType;
	
	@AnnonOfField(desc = "标题",type="varchar(255)", notNull=false)
	private String title;
	
	@AnnonOfField(desc = "创建时间")
	private long createTime;
	
	@AnnonOfField(desc = "更新时间")
	private long updateTime;
	
	@AnnonOfField(desc = "上线时间")
	private long startTime;
	
	@AnnonOfField(desc = "下线时间")
	private long endTime;
	
	@AnnonOfField(desc = "排序")
	private int sequence;

	@AnnonOfField(desc = "是否在先,0:在线,1:下线", notNull=false)
	private int online;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public int getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(int promotionType) {
		this.promotionType = promotionType;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public PromotionContentType getDevice() {
		return device;
	}

	public void setDevice(PromotionContentType device) {
		this.device = device;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}
}
