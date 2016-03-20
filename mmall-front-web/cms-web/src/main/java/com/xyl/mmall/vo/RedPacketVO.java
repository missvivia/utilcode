/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.vo;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.dto.RedPacketDTO;

/**
 * RedPacketVO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public class RedPacketVO extends RedPacketDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RedPacketVO() {}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public RedPacketVO(RedPacketDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
}
