/**
 * 
 */
package com.xyl.mmall.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.security.util.FullUserNameUtils;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.member.dto.MobileInfoDTO;
import com.xyl.mmall.mobile.facade.MobileAuthcFacade;
import com.xyl.mmall.mobile.facade.vo.MobileUserVO;
import com.xyl.mmall.security.service.MobileAuthcService;
import com.xyl.mmall.security.service.MobileTokenService;
import com.xyl.mmall.security.service.URSAuthUserInfoService;
import com.xyl.mmall.security.token.MobileAccessToken;

/**
 * 手机应用访问Token相关服务接口实现。
 * 
 * @author lihui
 *
 */
@Service("mobileTokenService")
public class MobileTokenServiceImpl implements MobileTokenService {

	@Autowired
	private MobileAuthcFacade mobileAuthcFacade;

	@Autowired
	private MobileAuthcService mobileAuthcService;

	@Autowired
	private URSAuthUserInfoService ursAuthUserInfoService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileTokenService#validateMobleToken(java.lang.String)
	 */
	@Override
	public String validateMobleToken(String token) {
		MobileUserVO userVO = mobileAuthcFacade.findAuthencatedUser(token);
		return userVO == null ? null : userVO.getUserProfile().getUserName();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.security.service.MobileTokenService#mobileSeamlessRelogin()
	 */
	@Override
	public Map<String, Object> mobileSeamlessRelogin(String id, String params, String userIp) {
		// 根据Id获取对应的key等信息
		MobileInfoDTO mobileInfoDTO = mobileAuthcFacade.findMobileInfoByInitId(id);
		if (mobileInfoDTO == null) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("code", MobileErrorCode.LOGIN_FAIL.getIntValue());
			resultMap.put("message", "无效的初始化ID！");
			return resultMap;
		}
		Map<String, String> ursTokenMap = mobileAuthcService.decryptURSToken(params, mobileInfoDTO.getInitKey());
		if (ursTokenMap.containsKey("username")) {
			return mobileUrsRelogin(mobileInfoDTO, id, params, userIp);
		} else if (ursTokenMap.containsKey("token")) {
			return mobileOauthRelogin(mobileInfoDTO, id, params, userIp);
		}
		return null;
	}

	/**
	 * @param mobileInfoDTO
	 * @param id
	 * @param params
	 * @param userIp
	 * @return
	 */
	private Map<String, Object> mobileUrsRelogin(MobileInfoDTO mobileInfoDTO, String id, String params, String userIp) {
		Map<String, String> resultMap = mobileAuthcService.loginForMob(id, params, mobileInfoDTO.getInitKey(), userIp);
		if (resultMap.containsKey("code")) {
			// 返回urs登录失败的错误信息。
			Map<String, Object> ursResultMap = new HashMap<>();
			ursResultMap.put("code", resultMap.get("code"));
			ursResultMap.put("message", resultMap.get("message"));
			return ursResultMap;
		} else {
			// 登录成功时，解密返回的结果获取urs token
			Map<String, String> ursTokenMap = mobileAuthcService.decryptURSToken(resultMap.get("result"),
					mobileInfoDTO.getInitKey());
			// 获取用户的用户名
			MobileAccessToken ursToken = new MobileAccessToken();
			ursToken.setUserName(FullUserNameUtils.getFullUserName(ursTokenMap.get("username")));
			if (StringUtils.isBlank(ursToken.getUserName())) {
				Map<String, Object> ursResultMap = new HashMap<>();
				ursResultMap.put("code", MobileErrorCode.LOGIN_FAIL.getIntValue());
				ursResultMap.put("message", MobileErrorCode.LOGIN_FAIL.getDesc());
				return ursResultMap;
			}
			// 获取用户的昵称
			String nickName = ursAuthUserInfoService.getNicknameFromURS(ursToken.getUserName());
			// 保存或更新用户的信息，并生成手机应用的访问token
			return mobileAuthcFacade.upsertURSUser(ursToken.getUserName(), nickName, id, ursTokenMap.get("token"),
					genMobileToken(ursToken.getUserName(), mobileInfoDTO), userIp);
		}
	}

	private Map<String, Object> mobileOauthRelogin(MobileInfoDTO mobileInfoDTO, String id, String params, String userIp) {
		// 解密重新登录的params，获取URS 重登录的参数
		Map<String, String> ursTokenMap = mobileAuthcService.decryptURSToken(params, mobileInfoDTO.getInitKey());
		// 使用urs访问token和Id获取第三方accessToken和UID
		// 使用accessToken和UID访问第三方接口获取昵称
		MobileAccessToken oAuthToken = mobileAuthcService.validateOAuthToken(id, ursTokenMap.get("token"),
				ursTokenMap.get("username"), mobileInfoDTO.getInitKey());
		if (0 != oAuthToken.getCode()) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("code", oAuthToken.getCode());
			resultMap.put("message", oAuthToken.getMessage());
			return resultMap;
		}
		// 获取之前一次登录的user信息
		MobileUserVO reloginUser = mobileAuthcFacade.findUserByUserId(mobileInfoDTO.getUserId());
		// 保存或更新用户的信息，并生成手机应用的访问token
		return mobileAuthcFacade.upsertOauthUser(reloginUser.getUserName(), oAuthToken.getNickName(),
				oAuthToken.getProfileImage(), id, ursTokenMap.get("token"),
				genMobileToken(reloginUser.getUserName(), mobileInfoDTO), userIp);
	}

	/**
	 * 根据目前的token决定是否生成新的token
	 * 
	 * @param userName
	 * @param ursResultMap
	 * @return
	 */
	private String genMobileToken(String userName, MobileInfoDTO mobileInfoDTO) {
		return System.currentTimeMillis() > mobileInfoDTO.getExpiredTime() ? mobileAuthcService.genMobileToken(
				userName, mobileInfoDTO.getInitKey()) : mobileInfoDTO.getMobileToken();
	}

}
