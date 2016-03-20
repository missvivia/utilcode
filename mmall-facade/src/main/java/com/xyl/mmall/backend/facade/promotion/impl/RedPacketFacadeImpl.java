/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.backend.facade.promotion.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.facade.promotion.RedPacketFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.RedPacketDTO;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.meta.RedPacket;
import com.xyl.mmall.promotion.service.RedPacketService;

/**
 * RedPacketFacadeImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */

@Facade
public class RedPacketFacadeImpl implements RedPacketFacade {
	
	@Autowired
	private RedPacketService redPacketService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Override
	public List<RedPacketDTO> getRedPacketList(long userId, int state, String qvalue, int limit, int offset) {
		List<RedPacket> list = redPacketService.getRedPacketList(userId, state, qvalue, limit, offset);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		
		List<RedPacketDTO> dtos = new ArrayList<>();
		for (RedPacket packet : list) {
			RedPacketDTO dto = new RedPacketDTO(packet);
			//根据id获取用户名称
			AgentDTO applyDTO = agentService.findAgentById(packet.getApplyUserId());
			if (applyDTO != null) {
				dto.setApplyUserName(applyDTO.getName());
			}
			
			AgentDTO auditDTO = agentService.findAgentById(packet.getAuditUserId());
			if (auditDTO != null) {
				dto.setAuditUserName(auditDTO.getName());
			}
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public RedPacketDTO getRedPacketById(long id) {
		RedPacket packet = redPacketService.getRedPacketById(id);
		if (packet == null) {
			return null;
		}
		return new RedPacketDTO(packet);
	}

	@Override
	public RedPacketDTO addRedPacket(RedPacketDTO redPacket) {
		if (redPacket == null) {
			return null;
		}
		
		return new RedPacketDTO(redPacketService.addRedPacket(redPacket));
	}

	@Override
	public boolean updateRedPacket(RedPacketDTO redPacket) {
		if (redPacket == null) {
			return false;
		}
		
		//审核通过对优惠券进行处理
		if (redPacket.getAuditState() == StateConstants.PASS) {
			bindUserList(redPacket);
		}
		
		return redPacketService.updateRedPacket(redPacket);
	}

	private void bindUserList(RedPacketDTO redPacket) {
		//将用户名称转换成用户id
		if (redPacket.getBinderType() == BinderType.USER_BINDER) {
			
			String users = redPacket.getUsers();
			if (StringUtils.isBlank(users)) {
				return;
			}
			
			String[] userArray = users.split(",");
			if (userArray == null || userArray.length == 0) {
				return;
			}
			
			for (String user : userArray) {
				if (StringUtils.isBlank(user)) {
					continue;
				}
				
				UserProfileDTO userProfileDTO = userProfileService.findUserProfileByUserName(user);
				if (userProfileDTO == null || userProfileDTO.getUserId() <= 0) {
					continue;
				}
				redPacket.getBinderUserList().add(userProfileDTO.getUserId());
			}
		}
	}

	@Override
	public int getRedPacketCount(long userId, int state, String qvalue) {
		return redPacketService.getRedPacketCount(userId, state, qvalue);
	}

	@Override
	public boolean discardRedPacket(RedPacketDTO redPacketDTO) {
		bindUserList(redPacketDTO);
		return redPacketService.discardRedPacket(redPacketDTO);
	}

}
