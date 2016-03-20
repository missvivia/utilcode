package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 地址黑名单信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:35:51
 *
 */
@AnnonOfClass(desc = "地址黑名单信息", tableName = "Mmall_Order_CODBlacklistAddress")
public class CODBlacklistAddress implements Serializable {
	
	private static final long serialVersionUID = -5459245384005905196L;
	
	@AnnonOfField(desc = "黑名单Id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "审核人员用户Id")
	private long auditUserId;
	
	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "审核日志创建时间")
	private long ctime;
	
	@AnnonOfField(desc = "快递地址-省份")
	private String province;
	
	@AnnonOfField(desc = "快递地址-市")
	private String city;
	
	@AnnonOfField(desc = "快递地址-区")
	private String section;
	
	@AnnonOfField(desc = "快递地址-街道")
	private String street;
	
	@AnnonOfField(desc = "快递地址-详细地址")
	private String address;
	
	@AnnonOfField(desc = "收货人电话")
	private String consigneeMobile;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String mergeAddress() {
		StringBuffer strBuf = new StringBuffer(128);
		if(null != province) {
			strBuf.append(province);
		}
		if(null != city) {
			strBuf.append(city);
		}
		if(null != section) {
			strBuf.append(section);
		}
		if(null != street) {
			strBuf.append(street);
		}
		if(null != address) {
			strBuf.append(address);
		}
		return strBuf.toString();
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}