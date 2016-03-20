package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 用户黑名单信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月22日 上午9:35:51
 * 
 */
@AnnonOfClass(desc = "用户黑名单信息", tableName = "Mmall_Order_CODBlacklistUser")
public class CODBlacklistUser implements Serializable {

	private static final long serialVersionUID = 6089407781680214886L;

	@AnnonOfField(desc = "购买用户Id", primary = true, policy = true)
	private long userId;

	@AnnonOfField(desc = "审核人员用户Id")
	private long auditUserId;

	@AnnonOfField(desc = "审核日志创建时间")
	private long ctime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public long getCtime() {
		return ctime;
	}

	public void setCtime(long ctime) {
		this.ctime = ctime;
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