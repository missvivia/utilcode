/**
 * 
 */
package com.xyl.mmall.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.security.user.SimpleRole;
import com.netease.print.security.user.SimpleUser;
import com.netease.print.security.user.SimpleUserService;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.mobile.facade.MobileAuthcFacade;
import com.xyl.mmall.mobile.facade.vo.MobileUserVO;
import com.xyl.mmall.security.exception.UnauthenticatedAccountException;

/**
 * 手机用户信息服务，为安全验证提供用户信息和权限信息等。
 * 
 * @author lihui
 *
 */
@Service("applicationAuthUserService")
public class MobileAuthUserService extends SimpleUserService {
	
	private static Logger logger = LoggerFactory.getLogger(MobileAuthUserService.class);

	@Autowired
	private MobileAuthcFacade mobileAuthcFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getUser(long)
	 */
	@Override
	protected SimpleUser getUser(long id) {
		MobileUserVO mobileUserVO = mobileAuthcFacade.findUserByUserId(id);
		return buildMobileUser(mobileUserVO);
	}

	/**
	 * 组装用户的基本数据。
	 * 
	 * @param mobileUserVO
	 * @return
	 */
	private SimpleUser buildMobileUser(MobileUserVO mobileUserVO) {
		SimpleUser simpleUser = new SimpleUser();
		simpleUser.setId(mobileUserVO.getUserProfile().getUserId());
		simpleUser.setUserName(mobileUserVO.getUserProfile().getUserName());
		simpleUser.setLocked(mobileUserVO.getUserProfile().getIsValid() != 1);
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
		MobileUserVO mobileUserVO = mobileAuthcFacade.findUserByUserName(userName);
		if (mobileUserVO == null 
				|| mobileUserVO.getUserProfile().getUserId() < 1l 
				|| mobileUserVO.getUserProfile().getIsValid() != 1) {
			if (logger.isWarnEnabled()) {
				logger.warn("User {} is unauthenticated!", userName);
			}
			throw new UnauthenticatedAccountException("用户'" + userName + "'无访问mainsite的权限！");
		}
		return buildMobileUser(mobileUserVO);
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
