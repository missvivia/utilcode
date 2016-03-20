package com.xyl.mmall.vo;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import com.xyl.mmall.excelparse.ExcelExportField;
import com.xyl.mmall.ip.meta.UserFeedback;

public class UserFeedbackVO implements Serializable {

	private static final long serialVersionUID = 7578247858010706058L;
	
	@ExcelExportField(cellIndex = 0, desc = "反馈id")
	private long id;
	
	@ExcelExportField(cellIndex = 1, desc = "反馈内容")
	private String feedBackContent;
	
	@ExcelExportField(cellIndex = 2, desc = "用户区域code")
	private long areaId;
	
	@ExcelExportField(cellIndex = 3, desc = "用户区域名称")
	private String areaName;
	
	@ExcelExportField(cellIndex = 4, desc = "用户使用的系统")
	private String system;
	
	@ExcelExportField(cellIndex = 5, desc = "用户使用的app版本")
	private String version;
	
	@ExcelExportField(cellIndex = 6, desc = "用户账户")
	private String userAccount;
	
	@ExcelExportField(cellIndex = 7, desc = "用户联系方式")
	private String userContact;
	
	@ExcelExportField(cellIndex = 8, desc = "提交时间")
	private String submitTime;

	public long getId() {
		return id;
	}

	public void genDataFromMeta(UserFeedback feedback) {
		if (feedback != null) {
			this.id = feedback.getId();
			this.feedBackContent = feedback.getFeedBackContent();
			this.areaId = feedback.getAreaId();
			this.areaName = feedback.getAreaName();
			this.system = feedback.getSystem();
			this.version = feedback.getVersion();
			this.userAccount = feedback.getUserAccount();
			this.userContact = feedback.getUserContact();
			Date date = new Date(feedback.getSubmitTime());
			this.submitTime = DateFormat.getInstance().format(date);
		}
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

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

}
