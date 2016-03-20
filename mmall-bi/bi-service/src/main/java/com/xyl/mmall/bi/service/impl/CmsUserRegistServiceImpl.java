/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.bi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.facade.promotion.CouponFacade;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.util.UUIDUtil;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.promotion.dto.CouponConfigDTO;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.enums.BinderType;
import com.xyl.mmall.promotion.enums.CouponConfigType;
import com.xyl.mmall.promotion.enums.TimesType;
import com.xyl.mmall.promotion.meta.UserCoupon;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.promotion.service.UserCouponService;

/**
 * CmsUserRegistServiceImpl.java created by yydx811 at 2015年12月30日 上午9:52:45
 * cms添加用户
 *
 * @author yydx811
 */
@Service("cmsUserRegistService")
public class CmsUserRegistServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(CmsUserRegistServiceImpl.class);
	
	private static final long SITE_ID = 1000002l;
	
	@Resource
	private UserProfileService userProfileService;
	
	@Resource
	private CouponService couponService;
	
	@Autowired
	private CouponFacade couponFacade;

	@Resource
	private UserCouponService userCouponService;
	
	@Override
	@Transaction
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		// 判断userId
		if (StringUtils.isBlank(otherKey) || !RegexUtils.isAllNumber(otherKey)) {
			logger.warn("Wrong otherKey : {}!", otherKey);
		} else {
			// 获取用户
			long userId = Long.parseLong(otherKey);
			UserProfileDTO profileDTO = userProfileService.findUserProfileById(userId);
			if (profileDTO.getUserId() != userId) {
				logger.warn("Can't find user! UserId : {}.", userId);
			} else {
				// 判断是否已经发过红包
				if (profileDTO.getHasNoobCoupon() == 1) {
					logger.info("User had noob coupons! UserId : {}.", userId);
					return;
				}
				// 获取发券配置
				CouponConfigDTO couponConfigDTO = 
						couponService.getCouponConfigByType(SITE_ID, CouponConfigType.NOOB.getIntValue());
				if (couponConfigDTO.getId() < 1l) {
					logger.info("No noob coupon config at this site. SiteId : {}, UserId : {}.", SITE_ID, userId);
					return;
				}
				if (couponConfigDTO.getValidFlag() != 1) {
					logger.info("Noob coupon config is closed at this site. SiteId : {}, UserId : {}", SITE_ID, userId);
					return;
				}
				if (StringUtils.isBlank(couponConfigDTO.getCouponCodes()) 
						|| couponConfigDTO.getCouponCodes().split(",").length < 1) {
					logger.info("Noob coupon code is empty at this site. SiteId : {}, UserId : {}", SITE_ID, userId);
					return;
				}
				// 判断模板券的有效性
				List<CouponDTO> couponList = new ArrayList<CouponDTO>();
				Map<String, String> resultMap = 
						couponFacade.getUseFulCouponList(couponConfigDTO.getCouponCodes(), couponList);
				for (String result : resultMap.values()) {
					logger.warn(result);
				}
				List<String> successCodes = new ArrayList<String>();
				if (!CollectionUtils.isEmpty(couponList)) {
					boolean isRelativeTime = couponConfigDTO.getIsRelativeTime() == 1;
					long now = DateUtil.getDateBegin(System.currentTimeMillis());
					// 生成新的红包并绑定
					for (CouponDTO couponDTO : couponList) {
						if (isRelativeTime) {
							couponDTO.setEndTime(now + (couponDTO.getEndTime() - couponDTO.getStartTime()));
							couponDTO.setStartTime(now);
						}
						couponDTO.setBinderType(BinderType.USER_BINDER);
						couponDTO.setUsers(profileDTO.getUserName());
						couponDTO.setCouponCode(UUIDUtil.generateShortUUID());
						couponDTO.setTimesType(TimesType.TOTAL);
						if (couponService.addCoupon(couponDTO) == null) {
							throw new RuntimeException("Add noob coupon error! UserId : " + userId + ".");
						}
						UserCoupon userCoupon = new UserCoupon();
						userCoupon.setCouponCode(couponDTO.getCouponCode());
						userCoupon.setUserId(userId);
						userCoupon.setValidStartTime(couponDTO.getStartTime());
						userCoupon.setValidEndTime(couponDTO.getEndTime());
						for (int i = 0; i < couponDTO.getTimes(); i++) {
							userCoupon = userCouponService.addUserCoupon(userCoupon);
							if (userCoupon == null) {
								throw new RuntimeException("Bind noob coupon error! UserId : " + userId 
										+ ", couponCode : " + couponDTO.getCouponCode() + ".");
							}
						}
						successCodes.add(couponDTO.getCouponCode());
					}
				}
				// 已领取红包
				profileDTO.setHasNoobCoupon(1);
				if (userProfileService.updateUserBaseInfo(profileDTO).getUserId() < 1l) {
					throw new RuntimeException("Set user noob coupon flag error! UserId : " + userId + ".");
				}
				logger.info("Distribute noob coupon successful! UserId : {}, CouponCodes : {}.",
						userId, successCodes.toString());
			}
		}
	}

}
