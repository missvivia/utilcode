/*
 * @(#) 2014-9-28
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dto;

import java.util.ArrayList;
import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.promotion.meta.RedPacket;

/**
 * RedPacketDTO.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-28
 * @since      1.0
 */
public class RedPacketDTO extends RedPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String applyUserName;
	
	private String auditUserName;
	
	private List<Long> binderUserList = new ArrayList<>();
	
	public RedPacketDTO(){}
	
	public RedPacketDTO(RedPacket redPacket) {
		ReflectUtil.convertObj(this, redPacket, false);
	}
	
	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public List<Long> getBinderUserList() {
		return binderUserList;
	}

	public void setBinderUserList(List<Long> binderUserList) {
		this.binderUserList = binderUserList;
	}
}
