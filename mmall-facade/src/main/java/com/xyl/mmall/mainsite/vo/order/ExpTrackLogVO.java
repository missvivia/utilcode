package com.xyl.mmall.mainsite.vo.order;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 快递物流信息记录
 * 
 * @author dingmingliang
 * 
 */
public class ExpTrackLogVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "处理时间", primary = true, primaryIndex = 3)
	private long acceptTime;

	/**
	 * 处理时间(yyyy-MM-dd HH:mm:ss)
	 */
	private String acceptDate;

	@AnnonOfField(desc = "处理地址", type = "VARCHAR(64)")
	private String acceptAddress;

	@AnnonOfField(desc = "备注", type = "VARCHAR(64)")
	private String remark;

	public String getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}

	public long getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(long acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getAcceptAddress() {
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
