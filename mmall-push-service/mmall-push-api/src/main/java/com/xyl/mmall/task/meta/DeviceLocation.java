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
@AnnonOfClass(desc = "设备对应的地点区域", tableName = "TB_Task_DeviceLocation")
public class DeviceLocation implements Serializable {

	private static final long serialVersionUID = -4914353226193236544L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "设备ID，唯一键", type = "varchar(128)", uniqueKey = true)
	private String deviceId;

	@AnnonOfField(desc = "用户账户", policy = true)
	private long userId;

	@AnnonOfField(desc = "ios,android")
	private String platformType;

	@AnnonOfField(desc = "区域ＩＤ")
	private long areaCode;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "更新时间")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(long areaCode) {
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
