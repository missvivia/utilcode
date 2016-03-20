/*
 * @(#) 2014-10-13
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.UserRedPacket;

/**
 * UserRedPacketDTO.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-10-13
 * @since 1.0
 */
public class UserRedPacketDTO extends UserRedPacket implements Comparable<UserRedPacketDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户使用红包订单记录
	 */
	private List<RedPacketOrderDTO> dtos;
	
	private String name;

	public UserRedPacketDTO() {
	}

	public UserRedPacketDTO(UserRedPacket redPacket) {
		ReflectUtil.convertObj(this, redPacket, false);
	}

	public List<RedPacketOrderDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<RedPacketOrderDTO> dtos) {
		this.dtos = dtos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(UserRedPacketDTO o) {
		if (this.getValidEndTime() > o.getValidEndTime()) {
			return 1;
		} else if (this.getValidEndTime() < o.getValidEndTime()) {
			return -1;
		} else {
			return 0;
		}
	}

}
