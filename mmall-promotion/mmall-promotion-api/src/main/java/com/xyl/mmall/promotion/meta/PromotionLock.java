/*
 * @(#) 2014-10-24
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * PromotionLock.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-24
 * @since 1.0
 */
@SuppressWarnings("serial")
@AnnonOfClass(tableName = "Mmall_Promotion_Lock", desc = "活动-TCC事务锁", dbCreateTimeName = "CreateTime")
public class PromotionLock implements Serializable {
	
	@AnnonOfField(desc = "用户id", primary = true, policy = true)
	private long userId;

	public PromotionLock() {
		super();
	}

	public PromotionLock(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
