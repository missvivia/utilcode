/*
 * 2014-9-22
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.backend.facade.promotion.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RandomUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.backend.facade.promotion.CouponFacade;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.cms.vo.AreaVO;
import com.xyl.mmall.cms.vo.CouponConfigVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.member.dto.AgentAreaDTO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.AgentRoleDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.promotion.constants.ActivationConstants;
import com.xyl.mmall.promotion.constants.StateConstants;
import com.xyl.mmall.promotion.dto.CouponConfigDTO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.CodeType;
import com.xyl.mmall.promotion.enums.TimesType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * CouponFacadeImpl.java
 *
 * @author     <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version    1.0 2014-9-22
 * @since      1.0
 */
@Facade
public class CouponFacadeImpl implements CouponFacade {
	
	private static Logger logger = LoggerFactory.getLogger(CouponFacadeImpl.class);
	
	@Resource
	private CouponService couponService;

	@Resource
	private AgentService agentService;
	
	@Resource
	private UserCouponService userCouponService;
	
	@Resource
	private UserProfileService userProfileService;

	@Resource
	private SiteCMSService siteCMSService;

	@Resource
	private LocationService locationService;
	
	@Autowired
	private SiteCMSFacade siteCMSFacade;
	
	@Override
	public List<CouponDTO> getCouponList(long userId, int state, String qvalue, int limit, int offset) {
		List<Coupon> coupons = null;
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if (retArg == null) {
			return null;
		}
		// 超级管理员可以获取全部
		if (RetArgUtil.get(retArg, Boolean.class)) {
			coupons = couponService.getCouponList(new ArrayList<Long>(0), state, qvalue, limit, offset);
		} else {
			@SuppressWarnings("unchecked")
			List<Long> ids = RetArgUtil.get(retArg, ArrayList.class);
			coupons = couponService.getCouponList(ids, state, qvalue, limit, offset);
			if (CollectionUtils.isEmpty(coupons)) {
				return null;
			}
		}
		
		List<CouponDTO> couponDTOs = new ArrayList<>(coupons.size());
		for (Coupon coupon : coupons) {
			CouponDTO couponDTO = new CouponDTO(coupon);
			//根据id获取名称
			AgentDTO applyDTO = agentService.findAgentById(coupon.getApplyUserId());
			if (applyDTO != null) {
				couponDTO.setApplyUserName(applyDTO.getRealName());
			}
			
			AgentDTO audit = agentService.findAgentById(coupon.getAuditUserId());
			if (audit != null) {
				couponDTO.setAuditUserName(audit.getRealName());
			}
			
			couponDTOs.add(couponDTO);
		}
		
		return couponDTOs;
	}

	@Override
	public int getCouponCount(long userId, int state, String qvalue) {
		RetArg retArg = siteCMSFacade.getAgentAreaInfoByUserId(userId);
		if (retArg == null) {
			return 0;
		}
		// 超级管理员可以获取全部
		if (RetArgUtil.get(retArg, Boolean.class)) {
			return couponService.getCouponCount(new ArrayList<Long>(0), state, qvalue);
		} else {
			@SuppressWarnings("unchecked")
			List<Long> ids = RetArgUtil.get(retArg, ArrayList.class);
			return couponService.getCouponCount(ids, state, qvalue);
		}
	}

	@Override
	public CouponDTO addCoupon(CouponDTO couponDTO) {
		if (couponDTO == null) {
			return null;
		}
		
		if (couponDTO.getCodeType() == CodeType.RANDOM) {
			String code = "RC" + RandomUtil.getRandString(8);
			couponDTO.setCouponCode(code);
			couponDTO.setTimesType(TimesType.TOTAL);
			while (couponService.addCoupon(couponDTO) == null) {
				code = "RC" + RandomUtil.getRandString(8);
				couponDTO.setCouponCode(code);
			}
		} else {
			couponDTO.setTimesType(TimesType.EACH);
			couponService.addCoupon(couponDTO);
		}
		
		return couponDTO;
	}

	@Override
	public boolean updateCoupon(CouponDTO couponDTO) {
		if (couponDTO == null) {
			return false;
		}
		
		//审核通过对优惠券进行处理
		if (couponDTO.getAuditState() == StateConstants.PASS) {
			//将用户名称转换成用户id
			if (!getBinderUserList(couponDTO)) {
				return false;
			}
			if (couponDTO.getBinderType() == BinderType.USER_BINDER 
					&& couponDTO.getCodeType() == CodeType.PUBLIC) {
				List<Long> binderUserList = couponDTO.getBinderUserList();
				if (CollectionUtils.isEmpty(binderUserList)) {
					logger.error("Coupon bindList is null! CouponId : {}.", couponDTO.getId());
					return false;
				}

				if (couponService.updateCoupon(couponDTO)) {
					// 导入用户
					for (long userId : binderUserList) {
						int count = userCouponService.getUserCouponCountByCode(userId, couponDTO.getCouponCode());
						if (count >= couponDTO.getTimes()) {
							continue;
						}
						for (int i = 0; i < couponDTO.getTimes(); i++) {
							UserCoupon userCoupon = new UserCoupon();
							userCoupon.setCouponCode(couponDTO.getCouponCode());
							userCoupon.setUserId(userId);
							userCoupon.setValidStartTime(couponDTO.getStartTime());
							userCoupon.setValidEndTime(couponDTO.getEndTime());
							userCoupon.setState(ActivationConstants.STATE_CAN_USE);
							if (userCouponService.addUserCoupon(userCoupon) == null) {
								logger.error("Bind userCoupon error! UserId : {}, couponId : {}.", userId, couponDTO.getId());
							}
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				if (couponDTO.getCodeType() == CodeType.RANDOM) {
					if (couponService.updateCoupon(couponDTO)) {
						// 生成随机券
						int randomCount = couponDTO.getRandomCount();
						for (int i = 0; i < randomCount; i++) {
							Coupon c = couponDTO.cloneObject();
							c.setId(0);
							c.setAuditState(StateConstants.PASS);
							c.setRootCode(couponDTO.getCouponCode());
							c.setCouponCode("R" + RandomUtil.getRandString(7));
							c.setTimesType(TimesType.EACH);
							while (couponService.addCoupon(c) == null) {
								c.setCouponCode("R" + RandomUtil.getRandString(7));
							}
						}
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return couponService.updateCoupon(couponDTO);
	}

	private boolean getBinderUserList(CouponDTO couponDTO) {
		if (couponDTO.getBinderType() == BinderType.USER_BINDER && couponDTO.getCodeType() == CodeType.PUBLIC) {
			
			String users = couponDTO.getUsers();
			if (StringUtils.isBlank(users)) {
				return false;
			}
			
			String[] userArray = users.split(",");
			if (userArray == null || userArray.length == 0) {
				return false;
			}
			
			for (String user : userArray) {
				if (StringUtils.isBlank(user)) {
					continue;
				}
				
				UserProfileDTO userProfileDTO = userProfileService.findUserProfileByUserName(user);
				if (userProfileDTO == null || userProfileDTO.getUserId() < 1l) {
					continue;
				}
				// 去重
				if (!couponDTO.getBinderUserList().contains(userProfileDTO.getUserId())) {
					couponDTO.getBinderUserList().add(userProfileDTO.getUserId());
				}
			}
		}
		
		return true;
	}

	@Override
	public CouponDTO getCouponByCode(String couponCode, boolean isValidOnly) {
		Coupon coupon = couponService.getCouponByCode(couponCode, isValidOnly);
		if (coupon == null) {
			return null;
		}
		CouponDTO couponDTO = new CouponDTO(coupon);
		return couponDTO;
	}

	@Override
	public CouponDTO getCouponById(long id, boolean isValidOnly) {
		Coupon coupon = couponService.getCouponById(id, isValidOnly);
		if (coupon == null) {
			return null;
		}
		CouponDTO couponDTO = new CouponDTO(coupon);
		return couponDTO;
	}

	@Override
	public List<Coupon> getRamdomCoupons(String couponCode) {
		return couponService.getRandomCoupons(couponCode, Integer.MAX_VALUE);
	}

	@Override
	public boolean discardCoupon(CouponDTO couponDTO) {
		getBinderUserList(couponDTO);
		return couponService.discardCoupon(couponDTO);
	}
	
	@Override
	public List<AreaVO> getAreaList(long userId) {
		AgentDTO curAgent = agentService.findAgentById(userId);
		if (curAgent == null || curAgent.getAccountStatus() != AccountStatus.NORMAL) {
			logger.error("Agent is unavailable! UserId : " + userId + ".");
			return null;
		}
		// 超级管理员可以获取站点下全部区域
		if (curAgent.getAgentType() == AgentType.ROOT) {
			List<SiteAreaDTO> siteAreaList =  siteCMSService.getSiteAreasList(null);
			if (CollectionUtils.isEmpty(siteAreaList)) {
				return null;
			}
			List<AreaVO> retList = new ArrayList<AreaVO>(siteAreaList.size());
			for (SiteAreaDTO siteArea : siteAreaList) {
				AreaVO area = new AreaVO();
				area.setAreaId(siteArea.getAreaId());
				area.setAreaName(locationService.getLocationNameByCode(siteArea.getAreaId(), false));
				retList.add(area);
			}
			return retList;
		} else {
			List<AgentRoleDTO> agentRoleList = agentService.findByAgentId(userId);
			if (CollectionUtils.isEmpty(agentRoleList) || !StringUtils.isNumeric(agentRoleList.get(0).getSites())) {
				return null;
			}
			long siteId = Long.parseLong(agentRoleList.get(0).getSites());
			List<AgentAreaDTO> agentAreaList = agentService.getAgentAreaList(userId, siteId);
			if (CollectionUtils.isEmpty(agentAreaList)) {
				return null;
			}
			List<AreaVO> retList = new ArrayList<AreaVO>(agentAreaList.size());
			for (AgentAreaDTO agentArea : agentAreaList) {
				AreaVO area = new AreaVO();
				area.setAreaId(agentArea.getAreaId());
				area.setAreaName(locationService.getLocationNameByCode(agentArea.getAreaId(), false));
				retList.add(area);
			}
			return retList;
		}
	}

	@Override
	public CouponConfigVO getCouponConfigByType(long siteId, int type) {
		return new CouponConfigVO(couponService.getCouponConfigByType(siteId, type));
	}

	@Override
	public int addCouponConfig(CouponConfigDTO couponConfigDTO) {
		return couponService.addCouponConfig(couponConfigDTO);
	}

	@Override
	public int updateCouponConfig(CouponConfigDTO couponConfigDTO) {
		return couponService.updateCouponConfig(couponConfigDTO);
	}

	@Override
	public Map<String, String> getUseFulCouponList(String couponCodes, List<CouponDTO> couponList) {
		Map<String, String> resultMap = new HashMap<String, String>();
		long now = System.currentTimeMillis();
		for (String code : couponCodes.split(",")) {
			CouponDTO couponDTO = new CouponDTO(couponService.getCouponByCode(code, false));
			if (couponDTO.getId() < 1l) {
				resultMap.put(code, "优惠券 " + code + " 不存在");
				continue;
			}
			if (couponDTO.getBinderType() != BinderType.DISTRIBUTE_BINDER) {
				resultMap.put(code, "优惠券 " + code + " 绑定类型错误");
				continue;
			}
			if (couponDTO.getAuditState() != StateConstants.PASS) {
				resultMap.put(code, "优惠券 " + code + " 未审核通过");
				continue;
			}
			if (couponDTO.getEndTime() < now) {
				resultMap.put(code, "优惠券 " + code + " 已过期");
				continue;
			}
			couponList.add(couponDTO);
		}
		return resultMap;
	}
}
