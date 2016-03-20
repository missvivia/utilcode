/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

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

import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.OnlineActivityFacade;
import com.xyl.mmall.member.dto.FilterChainResourceDTO;
import com.xyl.mmall.member.dto.MobileInfoDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.enums.UserProfileType;
import com.xyl.mmall.member.service.FilterChainResourceService;
import com.xyl.mmall.member.service.MobileInfoService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.mobile.facade.MobileAuthcFacade;
import com.xyl.mmall.mobile.facade.vo.MobileConsigneeAddressVO;
import com.xyl.mmall.mobile.facade.vo.MobileFilterChainResourceVO;
import com.xyl.mmall.mobile.facade.vo.MobileUserVO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;

/**
 * @author lihui
 *
 */
@Facade
public class MobileAuthcFacadeImpl implements MobileAuthcFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileAuthcFacadeImpl.class);

	@Resource
	private UserProfileService userProfileService;

	@Resource
	private FilterChainResourceService filterChainResourceService;

	@Resource
	private MobileInfoService mobileInfoService;

	@Resource
	private ConsigneeAddressFacade consigneeAddressFacade;

	@Autowired
	private OnlineActivityFacade onlineActivityFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#findUserByUserName(java.lang.String)
	 */
	@Override
	public MobileUserVO findUserByUserName(String userName) {
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileByUserName(userName);
		return new MobileUserVO(userProfileDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#findUserByUserId(long)
	 */
	@Override
	public MobileUserVO findUserByUserId(long userId) {
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(userId);
		return new MobileUserVO(userProfileDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#getMobileFilterChainResource()
	 */
	@Override
	public List<MobileFilterChainResourceVO> getMobileFilterChainResource() {
		List<FilterChainResourceDTO> resourceDTOList = filterChainResourceService
				.findResourceByCategory(AuthzCategory.MOBILE.getIntValue());
		if (CollectionUtils.isEmpty(resourceDTOList)) {
			return null;
		}
		List<MobileFilterChainResourceVO> resourceVOList = new ArrayList<>();
		for (FilterChainResourceDTO resourceDTO : resourceDTOList) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add filter chain {} = {}", resourceDTO.getUrl(), resourceDTO.getPermission());
			}
			resourceVOList.add(new MobileFilterChainResourceVO(resourceDTO));
		}
		return resourceVOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#saveInitIdAndKey(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void saveInitIdAndKey(String id, String key) {
		mobileInfoService.upsertMobileInfo(id, key);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#findMobileInfoByInitId(java.lang.String)
	 */
	@Override
	public MobileInfoDTO findMobileInfoByInitId(String id) {
		return mobileInfoService.findMobileInfoByInitId(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#findAuthencatedUser(java.lang.String)
	 */
	@Override
	public MobileUserVO findAuthencatedUser(String token) {
		MobileInfoDTO mobileInfoDTO = mobileInfoService.findMobileInfoByInitIdAndToken(token);
		if (mobileInfoDTO == null || mobileInfoDTO.getUserId() == 0
				|| System.currentTimeMillis() > mobileInfoDTO.getExpiredTime()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Mobile token {} is invalid ot expired", token);
			}
			return null;
		}
		return findUserByUserId(mobileInfoDTO.getUserId());
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#upsertURSUser(java.lang.String,java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> upsertURSUser(String userName, String nickName, String initId, String ursToken,
			String mobileToken, String userIp) {
		Map<String, Object> userResultMap = new HashMap<>();
		// 保存/更新用户信息（用户名和昵称）
		// 如果用户名昵称为空，用该用户的用户名前缀作为昵称
		UserProfileDTO userProfile = userProfileService.upsertUserProfileWithNickName(userName,
				StringUtils.isBlank(nickName) ? StringUtils.split(userName, "@")[0] : nickName, userIp);
		// 生成新的手机应用访问token，保存userId、手机应用token、过期时间和ursToken到mobileInfo
		mobileInfoService.updateUserInfo(initId, ursToken, userProfile.getUserId(), mobileToken);
		bindOnlineCouponPack(userProfile.getUserId());
		StringBuilder result = new StringBuilder(256);
		result.append("token=").append(mobileToken).append("|").append(initId).append("&urstoken=").append(ursToken);
		userResultMap.put("result", result.toString());
		userResultMap.put("user", new MobileUserVO(userProfile));
		userResultMap
				.put("address", converterDefaultAddressToVo(consigneeAddressFacade
						.getDefaultConsigneeAddress(userProfile.getUserId())));
		return userResultMap;
	}

	public static MobileConsigneeAddressVO converterDefaultAddressToVo(ConsigneeAddressDTO dto) {
		if (dto == null) {
			return null;
		}
		MobileConsigneeAddressVO vo = new MobileConsigneeAddressVO();
		vo.setAddressId(dto.getId());
		vo.setAddressSuffix(dto.getAddress());
		vo.setCityId((int) dto.getCityId());
		vo.setCityName(dto.getCity());
		vo.setIsDefault(1);
		vo.setName(dto.getConsigneeName());
		vo.setPhone(dto.getConsigneeMobile());
		vo.setTel(dto.getConsigneeTel());
		vo.setProvinceId(dto.getProvinceId());
		vo.setProvinceName(dto.getProvince());
		vo.setDistrictId(dto.getSectionId());
		vo.setStreetId(dto.getStreetId());
		vo.setStreetName(dto.getStreet());
		vo.setDistrictName(dto.getSection());
		vo.setZipcode(dto.getZipcode());
		return vo;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mobile.facade.MobileAuthcFacade#upsertOauthUser(java.lang.String,java.lang.String,java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> upsertOauthUser(String userName, String nickName, String profileImage, String initId,
			String ursToken, String mobileToken, String userIp) {
		Map<String, Object> userResultMap = new HashMap<>();
		// 保存/更新用户信息（用户名和昵称）
		UserProfileDTO userProfile = userProfileService.upsertUserProfileWithNickNameAndImage(userName, nickName,
				profileImage, userIp);
		// 生成新的手机应用访问token，保存userId、手机应用token、过期时间和ursToken到mobileInfo
		mobileInfoService.updateUserInfo(initId, ursToken, userProfile.getUserId(), mobileToken);
		bindOnlineCouponPack(userProfile.getUserId());
		StringBuilder result = new StringBuilder(256);
		result.append("token=").append(mobileToken).append("|").append(initId);
		result.append("&urstoken=").append(ursToken);
		result.append("&userId=").append(userProfile.getUserId());
		result.append("&loginType=").append(getloginType(userProfile));
		userResultMap.put("result", result.toString());
		userResultMap.put("userName", userName);
		return userResultMap;
	}

	/**
	 * 获取账号的登录方式
	 * 
	 * @param userProfile
	 * @return
	 */
	private int getloginType(UserProfileDTO userProfile) {
		String[] userNameArr = StringUtils.split(userProfile.getUserName(), "@");
		if (userNameArr != null && userNameArr.length == 2) {
			for (UserProfileType type : UserProfileType.values()) {
				if (StringUtils.contains(userNameArr[1], type.getDesc())) {
					return type.getIntValue();
				}
			}
		}
		return UserProfileType.NULL.getIntValue();
	}

	/**
	 * 活动期间为手机登录用户绑定优惠券礼包。
	 * 
	 * @param userId
	 */
	private void bindOnlineCouponPack(long userId) {
		onlineActivityFacade.bindCouponPack(userId);
	}
	
}
