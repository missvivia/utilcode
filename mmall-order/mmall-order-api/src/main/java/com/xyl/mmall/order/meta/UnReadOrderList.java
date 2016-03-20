package com.xyl.mmall.order.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 未读订单
 * 
 * @author hzjiangww
 * 
 */
@AnnonOfClass(desc = "未读订单统计", tableName = "Mmall_Order_UnReadList")
public class UnReadOrderList implements Serializable {

	private static final long serialVersionUID = 494836009660840011L;

	@AnnonOfField(desc = "Id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "用户Id")
	private long userId;

	@AnnonOfField(desc = "订单类型")
	private int type;

	@AnnonOfField(desc = "上次阅读时间")
	private long lastReadTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(long lastReadTime) {
		this.lastReadTime = lastReadTime;
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