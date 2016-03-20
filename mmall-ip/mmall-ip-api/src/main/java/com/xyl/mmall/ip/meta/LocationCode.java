package com.xyl.mmall.ip.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.ip.enums.LocationLevel;

/**
 * 省市区区域code meta
 * @author chengximing
 *
 */
@AnnonOfClass(desc = "省市区区域code表", tableName = "Mmall_IP_LocationCode")
public class LocationCode implements Serializable {

	private static final long serialVersionUID = 5658773821400319661L;
	
	@AnnonOfField(desc = "省市区区域code表主键id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "区域名称", type = "VARCHAR(32)")
	private String locationName;
	
	@AnnonOfField(desc = "区域代码", unsigned = false, policy = true)
	private long code;
	
	@AnnonOfField(desc = "父节点的区域代码", policy = true, unsigned = false)
	private long parentCode;
	
	@AnnonOfField(desc = "区域级别")
	private LocationLevel level;
	
	@AnnonOfField(desc = "区域code是否有效")
	private boolean valid;
	
	@AnnonOfField(desc = "更新时间")
	private long updateTime;
	
	@AnnonOfField(desc = "省份区域的拼音首字母，只有省份区域这个字段才有效", notNull = false, type = "CHAR(4)")
	private String provinceHead;

	@AnnonOfField(desc = "档期销售站点异或值", notNull = false)
	private long siteFlag;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getParentCode() {
		return parentCode;
	}

	public void setParentCode(long parentCode) {
		this.parentCode = parentCode;
	}

	public LocationLevel getLevel() {
		return level;
	}

	public void setLevel(LocationLevel level) {
		this.level = level;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getProvinceHead() {
		return provinceHead;
	}

	public void setProvinceHead(String provinceHead) {
		this.provinceHead = provinceHead;
	}

	public long getSiteFlag() {
		return siteFlag;
	}

	public void setSiteFlag(long siteFlag) {
		this.siteFlag = siteFlag;
	}
}
