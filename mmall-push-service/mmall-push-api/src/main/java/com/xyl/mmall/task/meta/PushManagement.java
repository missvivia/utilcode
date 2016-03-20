package com.xyl.mmall.task.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 订单服务的Sku销售数据
 * 
 * @author dingmingliang
 * 
 */
@AnnonOfClass(desc = "Push服务配置管理表", tableName = "Mmall_Task_PushManagement")
public class PushManagement implements Serializable {

	private static final long serialVersionUID = -8513841436556218455L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true, policy = true)
	private long id;

	@AnnonOfField(desc = "通知标题")
	private String title;

	@AnnonOfField(desc = "通知内容", type = "varchar(1024)")
	private String content;

	@AnnonOfField(desc = "ios,android不同类型用,隔开")
	private String platformType;

	@AnnonOfField(desc = "通知链接")
	private String link;

	@AnnonOfField(desc = "各个站点间用英文逗号”,”隔开")
	private String areaCode;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "更新时间")
	private long updateTime;

	@AnnonOfField(desc = "推送时间")
	private long pushTime;

	@AnnonOfField(desc = "操作者")
	private String operator;

	@AnnonOfField(desc = "0:未发送，1:推送成功，2：推送失败", defa = "0")
	private int pushSuccess;

	@AnnonOfField(desc = "推送失败地区", defa = "")
	private String pushFailArea;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	public long getPushTime() {
		return pushTime;
	}

	public void setPushTime(long pushTime) {
		this.pushTime = pushTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getPushSuccess() {
		return pushSuccess;
	}

	public void setPushSuccess(int pushSuccess) {
		this.pushSuccess = pushSuccess;
	}

	public String getPushFailArea() {
		return pushFailArea;
	}

	public void setPushFailArea(String pushFailArea) {
		this.pushFailArea = pushFailArea;
	}

}
