package com.xyl.mmall.ip.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.enums.LocationLevel;

@AnnonOfClass(desc = "偏远区域表", tableName = "Mmall_IP_RemoteArea")
public class RemoteArea implements Serializable {

	private static final long serialVersionUID = -1677463500667219140L;
	
	@AnnonOfField(desc = "偏远区域表id", primary = true, autoAllocateId = true)
	private long id;
	
	@AnnonOfField(desc = "区域名称", type = "VARCHAR(32)")
	private String locationName;
	
	@AnnonOfField(desc = "区域代码", unsigned = false)
	private long code;
	
	@AnnonOfField(desc = "父节点的区域代码", policy = true, unsigned = false)
	private long parentCode;
	
	@AnnonOfField(desc = "区域级别")
	private LocationLevel level;
	
	@AnnonOfField(desc = "偏远的区域code是否有效")
	private boolean valid;
	
	@AnnonOfField(desc = "更新时间")
	private long updateTime;
	
	@AnnonOfField(desc = "快递公司的类型")
	private ExpressType type;
	
	@AnnonOfField(desc = "偏远地区的快递费")
	private BigDecimal price;

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

	public LocationLevel getLevel() {
		return level;
	}

	public void setLevel(LocationLevel level) {
		this.level = level;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getParentCode() {
		return parentCode;
	}

	public void setParentCode(long parentCode) {
		this.parentCode = parentCode;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public ExpressType getType() {
		return type;
	}

	public void setType(ExpressType type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
