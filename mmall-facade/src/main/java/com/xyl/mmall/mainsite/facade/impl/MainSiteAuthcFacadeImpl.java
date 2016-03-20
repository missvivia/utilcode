/**
 * 
 */
package com.xyl.mmall.mainsite.facade.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.ip.util.IPUtils;
import com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade;
import com.xyl.mmall.mainsite.vo.MainSiteFilterChainResourceVO;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;
import com.xyl.mmall.member.dto.FilterChainResourceDTO;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.service.FilterChainResourceService;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.security.utils.CookieUtils;

/**
 * @author lihui
 *
 */
@Facade
public class MainSiteAuthcFacadeImpl implements MainSiteAuthcFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainSiteAuthcFacadeImpl.class);

	@Resource
	private UserProfileService userProfileService;

	@Resource
	private FilterChainResourceService filterChainResourceService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade#findUserByUserId(long)
	 */
	@Override
	public MainSiteUserVO findUserByUserId(long id) {
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(id);
		return new MainSiteUserVO(userProfileDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade#upsertUser(java.lang.String)
	 */
	@Override
	public MainSiteUserVO upsertUser(String userName, String nickName) {
		Subject subject = SecurityUtils.getSubject();
		String userIp = null;
		nickName = StringUtils.isBlank(nickName) ? StringUtils.split(userName, "@")[0] : nickName;
		if (subject != null) {
			HttpServletRequest request = WebUtils.getHttpRequest(subject);
			userIp = IPUtils.getIpAddr(request);
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("User {} login from ip {} in {}", userName, userIp, System.currentTimeMillis());
		}
		// 如果用户名昵称为空，用该用户的用户名的前缀作为昵称
		UserProfileDTO userProfileDTO = userProfileService.upsertUserProfileWithNickName(userName, nickName, userIp);
		if (subject != null) {
			HttpServletResponse response = WebUtils.getHttpResponse(subject);
			try {
				// 更新cookie中的昵称
				Cookie cookie = CookieUtils.createCookie(MmallConstant.COOKIE_USER_NICK_NAME, 
						URLEncoder.encode(userProfileDTO.getNickName(), "UTF-8"), MmallConstant.MAIN_SITE_DOMAIN);
				cookie.setMaxAge(-1);
				response.addCookie(cookie);
			} catch (Exception e) {
				LOGGER.error("Failed to set user nick name into cookie!", nickName);
			}
		}
		return new MainSiteUserVO(userProfileDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade#getMainSiteFilterChainResource()
	 */
	@Override
	public List<MainSiteFilterChainResourceVO> getMainSiteFilterChainResource() {
		List<FilterChainResourceDTO> resourceDTOList = filterChainResourceService
				.findResourceByCategory(AuthzCategory.MAINSITE.getIntValue());
		if (CollectionUtils.isEmpty(resourceDTOList)) {
			return null;
		}
		List<MainSiteFilterChainResourceVO> resourceVOList = new ArrayList<>();
		for (FilterChainResourceDTO resourceDTO : resourceDTOList) {
			resourceVOList.add(new MainSiteFilterChainResourceVO(resourceDTO));
		}
		return resourceVOList;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade#updateNickName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String updateNickName(String userName, String nickName) {
		UserProfileDTO userProfile = userProfileService.updateNickName(userName, nickName);
		return userProfile == null ? null : userProfile.getNickName();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade#findUserByUserName(java.lang.String)
	 */
	@Override
	public MainSiteUserVO findUserByUserName(String name) {
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileByUserName(name);
		return null == userProfileDTO ? null : new MainSiteUserVO(userProfileDTO);
	}
}
