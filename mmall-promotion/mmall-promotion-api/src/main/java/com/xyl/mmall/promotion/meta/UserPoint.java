/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * UserPoint.java created by yydx811 at 2015年12月24日 上午10:55:45
 * 用户积分
 *
 * @author yydx811
 */
@AnnonOfClass(desc = "用户积分表", tableName = "Mmall_Promotion_UserPoint", dbCreateTimeName = "CreateTime")
public class UserPoint implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -5785242233150290305L;

	@AnnonOfField(desc = "id", autoAllocateId = true, primary = true)
	private long id;

	@AnnonOfField(desc = "用户ID", policy = true, uniqueKey = true)
	private long userId;
	
	@AnnonOfField(desc = "积分")
	private int point;

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

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
