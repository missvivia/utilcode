package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 地址白名单信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:35:51
 *
 */
@AnnonOfClass(desc = "地址白名单信息", tableName = "Mmall_Order_CODWhitelistAddress")
public class CODWhitelistAddress implements Serializable {
	
	private static final long serialVersionUID = -5459245384005905196L;
	
	@AnnonOfField(desc = "白名单Id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "审核人员用户Id")
	private long auditUserId;
	
	@AnnonOfField(desc = "用户Id", policy = true)
	private long userId;
	
	@AnnonOfField(desc = "审核日志创建时间")
	private long ctime;
	
	@AnnonOfField(desc = "快递地址-详细地址")
	private String address;
	
	@AnnonOfField(desc = "快递地址-全地址(省市区+详细地址)")
	private String fullAddress;
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
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