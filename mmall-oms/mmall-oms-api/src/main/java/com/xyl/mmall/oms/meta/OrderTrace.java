/**
 * 
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 
 * @author hzzengchengyuan
 */
@AnnonOfClass(desc = "订单物流轨迹表", tableName = "Mmall_Oms_OrderTrace")
public class OrderTrace implements Serializable {

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "轨迹ID", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "快递号", notNull = true)
	private String expressNO = "";

	@AnnonOfField(desc = "快递公司", notNull = true)
	private String expressCompany = "";

	/** 时间戳，格式为：yyyy-MM-dd HH:mm:ss. */
	@AnnonOfField(desc = "时间戳", notNull = true)
	private String time = "";

	@AnnonOfField(desc = "当前操作人员")
	private String operater = "";

	@AnnonOfField(desc = "操作人员电话")
	private String operaterPhone = "";

	@AnnonOfField(defa = "", desc = "操作类型")
	private String operate = "";

	@AnnonOfField(defa = "", desc = "操作描述")
	private String operateDesc = "";

	@AnnonOfField(defa = "", desc = "子操作类型")
	private String childOperate = "";

	@AnnonOfField(defa = "", desc = "子操作描述")
	private String childOperateDesc = "";

	@AnnonOfField(defa = "", desc = "当前操作机构(地址)名称")
	private String operateOrg = "";

	@AnnonOfField(desc = "描述")
	private String note = "";

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the expressNO
	 */
	public String getExpressNO() {
		return expressNO;
	}

	/**
	 * @param expressNO
	 *            the expressNO to set
	 */
	public void setExpressNO(String expressNO) {
		this.expressNO = expressNO;
	}

	/**
	 * @return the expressCompany
	 */
	public String getExpressCompany() {
		return expressCompany;
	}

	/**
	 * @param expressCompany
	 *            the expressCompany to set
	 */
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the operater
	 */
	public String getOperater() {
		return operater;
	}

	/**
	 * @param operater
	 *            the operater to set
	 */
	public void setOperater(String operater) {
		this.operater = operater;
	}

	/**
	 * @return the operaterPhone
	 */
	public String getOperaterPhone() {
		return operaterPhone;
	}

	/**
	 * @param operaterPhone
	 *            the operaterPhone to set
	 */
	public void setOperaterPhone(String operaterPhone) {
		this.operaterPhone = operaterPhone;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	/**
	 * @return the operateOrg
	 */
	public String getOperateOrg() {
		return operateOrg;
	}

	/**
	 * @param operateOrg
	 *            the operateOrg to set
	 */
	public void setOperateOrg(String operateOrg) {
		this.operateOrg = operateOrg;
	}

	/**
	 * @return the desc
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setNote(String desc) {
		this.note = desc;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public String getChildOperate() {
		return childOperate;
	}

	public String getChildOperateDesc() {
		return childOperateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public void setChildOperate(String childOperate) {
		this.childOperate = childOperate;
	}

	public void setChildOperateDesc(String childOperateDesc) {
		this.childOperateDesc = childOperateDesc;
	}

}
