package com.xyl.mmall.ip.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 用户反馈meta
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "用户反馈表", tableName = "Mmall_IP_UserFeedback")
public class UserFeedback implements Serializable {

	private static final long serialVersionUID = 8146954602493665938L;
	
	@AnnonOfField(desc = "用户反馈表主键id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "反馈内容", type = "VARCHAR(300)")
	private String feedBackContent;
	
	@AnnonOfField(desc = "区域id", notNull = false)
	private long areaId;
	
	@AnnonOfField(desc = "区域名称", notNull = false, type = "VARCHAR(255)")
	private String areaName;
	
	@AnnonOfField(desc = "用户反馈所用的系统", notNull = false, type = "VARCHAR(20)")
	private String system;
	
	@AnnonOfField(desc = "系统的版本号", notNull = false, type = "VARCHAR(10)")
	private String version;
	
	@AnnonOfField(desc = "反馈者的账户", notNull = false, type = "VARCHAR(100)", policy = true)
	private String userAccount;
	
	@AnnonOfField(desc = "反馈者的联系方式", notNull = false, type = "VARCHAR(100)")
	private String userContact;
	
	@AnnonOfField(desc = "提交时间")
	private long submitTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFeedBackContent() {
		return feedBackContent;
	}

	public void setFeedBackContent(String feedBackContent) {
		this.feedBackContent = feedBackContent;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserContact() {
		return userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}

}
