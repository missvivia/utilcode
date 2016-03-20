/**
 * 
 */
package com.xyl.mmall.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.security.user.SimpleRole;
import com.netease.print.security.user.SimpleUser;
import com.netease.print.security.user.SimpleUserService;
import com.xyl.mmall.common.facade.ConsigneeAddressFacade;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.mainsite.facade.MainSiteAuthcFacade;
import com.xyl.mmall.mainsite.vo.MainSiteUserVO;
import com.xyl.mmall.order.dto.ConsigneeAddressDTO;
import com.xyl.mmall.security.exception.UnauthenticatedAccountException;
import com.xyl.mmall.security.service.URSAuthUserInfoService;
import com.xyl.mmall.util.AreaUtils;

/**
 * Wap用户相关服务，为安全验证提供用户信息和权限信息等。
 * 
 * @author lihui
 *
 */
@Service("applicationAuthUserService")
public class WapAuthUserService extends SimpleUserService {

	private static Logger logger = LoggerFactory.getLogger(WapAuthUserService.class);
	
	@Autowired
	private MainSiteAuthcFacade mainSiteAuthcFacade;

	@Autowired
	private URSAuthUserInfoService ursAuthUserInfoService;
	
	@Autowired
	private ConsigneeAddressFacade consigneeAddressFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getUser(long)
	 */
	@Override
	protected SimpleUser getUser(long id) {
		MainSiteUserVO mainSiteUserVO = mainSiteAuthcFacade.findUserByUserId(id);
		return buildMainSiteUser(mainSiteUserVO);
	}

	/**
	 * 组装用户基本信息。
	 * 
	 * @param mainSiteUserVO
	 * @return
	 */
	private SimpleUser buildMainSiteUser(MainSiteUserVO mainSiteUserVO) {
		SimpleUser simpleUser = new SimpleUser();
		simpleUser.setId(mainSiteUserVO.getUserProfile().getUserId());
		simpleUser.setUserName(mainSiteUserVO.getUserProfile().getUserName());
		simpleUser.setLocked(mainSiteUserVO.getUserProfile().getIsValid() != 1);
		return simpleUser;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#upsertUserWithTransaction(java.lang.String)
	 */
	@Override
	protected SimpleUser upsertUserWithTransaction(String userName) {
		if (!StringUtils.endsWith(userName, MmallConstant.MAINSITE_ACCOUNT_SUFFIX)) {
			userName = userName + MmallConstant.MAINSITE_ACCOUNT_SUFFIX;
		}
		MainSiteUserVO orgUserVO = mainSiteAuthcFacade.findUserByUserName(userName);
		if (orgUserVO == null 
				|| orgUserVO.getUserProfile().getUserId() < 1l 
				|| orgUserVO.getUserProfile().getIsValid() != 1) {
			if (logger.isWarnEnabled()) {
				logger.warn("User {} is unauthenticated!", userName);
			}
			throw new UnauthenticatedAccountException("用户'" + userName + "'无访问mainsite的权限！");
		}
		// 获取用户的昵称
//		String nickName = ursAuthUserInfoService.getNicknameFromURS(userName);
		String nickName = null != orgUserVO ? orgUserVO.getNickname() : null;
		MainSiteUserVO mainSiteUserVO = mainSiteAuthcFacade.upsertUser(userName, nickName);
		ConsigneeAddressDTO address = 
				consigneeAddressFacade.getDefaultConsigneeAddress(orgUserVO.getUserProfile().getUserId());
		if (address != null && address.getSectionId() > 0l) {
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				HttpServletResponse response = WebUtils.getHttpResponse(subject);
				AreaUtils.setAreaCookie((HttpServletResponse) response, address.getSectionId());
			}
		}
		return buildMainSiteUser(mainSiteUserVO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getRoles(java.lang.String)
	 */
	@Override
	protected List<SimpleRole> getRoles(String userName) {
		SimpleRole simpleRole = new SimpleRole();
		simpleRole.setName("User");
		simpleRole.setDisplayName("User");
		simpleRole.setPermissions("ROLE_USER");
		List<SimpleRole> list = new ArrayList<>();
		list.add(simpleRole);
		return list;
	}

}
