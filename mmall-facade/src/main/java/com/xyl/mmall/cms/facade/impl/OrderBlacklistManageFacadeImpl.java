package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.facade.OrderBlacklistManageFacade;
import com.xyl.mmall.cms.vo.order.BlacklistAddressVO;
import com.xyl.mmall.cms.vo.order.BlacklistUserVO;
import com.xyl.mmall.cms.vo.order.BlacklistAddressVO.BlackAddress;
import com.xyl.mmall.cms.vo.order.BlacklistUserVO.BlackUser;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO.BlacklistSearchType;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.UserSearchType;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.order.dto.CODBlacklistAddressDTO;
import com.xyl.mmall.order.dto.CODBlacklistUserDTO;
import com.xyl.mmall.order.service.CODAuditService;

@Facade("OrderBlacklistManageFacade")
public class OrderBlacklistManageFacadeImpl implements OrderBlacklistManageFacade {
	
	@Resource
	private CODAuditService codAuditService;
	
	@Resource
	private UserProfileService userProfileService;
	
	@Resource
	private AgentService agentService;

	@Override
	public BlacklistQueryCategoryListVO getBlacklistQueryTypeList() {
		return BlacklistQueryCategoryListVO.getInstance();
	}

	private Map<Long, UserProfileDTO> getUserIdListBySearchType(BlacklistSearchType bt, String value) {
		Map<Long, UserProfileDTO> users = new HashMap<Long, UserProfileDTO>();
		if(BlacklistSearchType.USER_ID == bt) {
			long userId = Long.parseLong(value);
			UserProfileDTO user = userProfileService.findUserProfileById(userId);
			if(null != user) {
				users.put(userId, user);
			}
			return users;
		}
		UserSearchType type = null;
		switch(bt) {
		case CONSIGNEE_NAME:
			type = UserSearchType.USER_NAME;
			break;
		case CONSIGNEE_TEL:
			type = UserSearchType.MOBILE;
			break;
		default:
			return users;
		}
		Map<Integer, String> searchParams = new HashMap<>();
		//Convert search type
		searchParams.put(type.getIntValue() + 1, value);
		List<UserProfileDTO> userList = userProfileService.searchUserByParams(searchParams, Integer.MAX_VALUE, 0);
		if (CollectionUtils.isNotEmpty(userList)) {
			for (UserProfileDTO user : userList) {
				if(null == user) {
					continue;
				}
				users.put(user.getUserId(), user);
			}
		}
		return users;
	}
	
	List<Long> extractUserIdList(Map<Long, UserProfileDTO> users) {
		List<Long> idList = new ArrayList<Long>();
		if(!CollectionUtil.isEmptyOfMap(users)) {
			for(Entry<Long, UserProfileDTO> entry : users.entrySet()) {
				UserProfileDTO user = null;
				if(null == entry || null == (user = entry.getValue())) {
					continue;
				}
				idList.add(user.getUserId());
			}
		}
		return idList;
	}
	
	private Map<Long, AgentDTO> getAgentDTOListByAgentIdList(List<Long> idList) {
		Map<Long, AgentDTO> ret = new HashMap<Long, AgentDTO>();
		if(CollectionUtil.isEmptyOfList(idList)) {
			return ret;
		}
		List<AgentDTO> retList = agentService.findAgentByIdList(idList);
		if(!CollectionUtil.isEmptyOfList(retList)) {
			for(AgentDTO agent : retList) {
				if(null == agent) {
					continue;
				}
				ret.put(agent.getId(), agent);
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderBlacklistManageFacade#queryUserBlacklist(com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO.BlacklistSearchType, java.lang.String, int, int)
	 */
	@Override
	public BlacklistUserVO queryUserBlacklist(BlacklistSearchType bt, String value, int limit, int offset) {
		BlacklistUserVO ret = new BlacklistUserVO();
		Map<Long, UserProfileDTO> users = getUserIdListBySearchType(bt, value);
		if(CollectionUtil.isEmptyOfMap(users)) {
			return ret;
		}
		List<Long> userIdList = extractUserIdList(users);
		DDBParam param = new DDBParam("ctime", false, limit, offset);
		List<CODBlacklistUserDTO> blackUsers = codAuditService.queryBlacklistUserByUserIdList(userIdList, param);
		if(CollectionUtil.isEmptyOfList(blackUsers)) {
			return ret;
		}
		Set<Long> auditUserIdSet = new HashSet<Long>();
		for(CODBlacklistUserDTO bu : blackUsers) {
			if(null == bu) {
				continue;
			}
			auditUserIdSet.add(bu.getAuditUserId());
		}
		List<Long> auditUserIdList = new ArrayList<Long>(auditUserIdSet.size());
		for(Long id : auditUserIdSet) {
			auditUserIdList.add(id);
		}
		Map<Long, AgentDTO> auditUsers = getAgentDTOListByAgentIdList(auditUserIdList);
		for(CODBlacklistUserDTO bu : blackUsers) {
			if(null == bu) {
				continue;
			}
			UserProfileDTO user = users.get(bu.getUserId());
			AgentDTO auditUser = auditUsers.get(bu.getAuditUserId());
			ret.getBlackUserList().add((new BlackUser()).fillWithInfo(user, auditUser, bu.getCtime()));
		}
		ret.setTotal(param.getTotalCount());
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderBlacklistManageFacade#queryAddressBlackList(com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.BlacklistQueryCategoryListVO.BlacklistSearchType, java.lang.String, int, int)
	 */
	@Override
	public BlacklistAddressVO queryAddressBlackList(BlacklistSearchType bt, String value, int limit, int offset) {
		BlacklistAddressVO ret = new BlacklistAddressVO();
		Map<Long, UserProfileDTO> users = getUserIdListBySearchType(bt, value);
		if(CollectionUtil.isEmptyOfMap(users)) {
			return ret;
		}
		List<Long> userIdList = extractUserIdList(users);
		DDBParam param = new DDBParam("ctime", false, limit, offset);
		List<CODBlacklistAddressDTO> blackAddresses = codAuditService.queryBlacklistAddressByUserIdList(userIdList, param);
		if(CollectionUtil.isEmptyOfList(blackAddresses)) {
			return ret;
		}
		Set<Long> auditUserIdSet = new HashSet<Long>();
		for(CODBlacklistAddressDTO address : blackAddresses) {
			if(null == address) {
				continue;
			}
			auditUserIdSet.add(address.getAuditUserId());
		}
		List<Long> auditUserIdList = new ArrayList<Long>(auditUserIdSet.size());
		for(Long id : auditUserIdSet) {
			auditUserIdList.add(id);
		}
		Map<Long, AgentDTO> auditUsers = getAgentDTOListByAgentIdList(auditUserIdList);
		for(CODBlacklistAddressDTO address : blackAddresses) {
			if(null == address) {
				continue;
			}
			UserProfileDTO user = users.get(address.getUserId());
			String userName = null == user ? "" : user.getUserName();
			AgentDTO auditUser = auditUsers.get(address.getAuditUserId());
			String auditUserName = null == auditUser ? "" : auditUser.getName();
			ret.getBlackAddressList().add((new BlackAddress()).fillWithInfo(address, userName, auditUserName));
		}
		ret.setTotal(param.getTotalCount());
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderBlacklistManageFacade#removeFromBlacklistUser(long)
	 */
	@Override
	public boolean removeFromBlacklistUser(long userId) {
		return codAuditService.removeFromBlacklistUser(userId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.cms.facade.OrderBlacklistManageFacade#removeFromBlacklistAddress(long, long)
	 */
	@Override
	public boolean removeFromBlacklistAddress(long id, long userId) {
		return codAuditService.removeFromBlacklistAddress(id, userId);
	}

}
