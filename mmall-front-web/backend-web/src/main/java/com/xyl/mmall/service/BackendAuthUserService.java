/**
 * 
 */
package com.xyl.mmall.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.netease.print.security.user.SimpleRole;
import com.netease.print.security.user.SimpleUser;
import com.netease.print.security.user.SimpleUserService;
import com.xyl.mmall.backend.facade.BackendAuthcFacade;
import com.xyl.mmall.backend.vo.DealerPermissionVO;
import com.xyl.mmall.backend.vo.DealerVO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.security.exception.UnauthenticatedAccountException;

/**
 * 商家后台用户服务，为安全验证提供用户信息和权限信息等。
 * 
 * @author lihui
 *
 */
@Service("applicationAuthUserService")
public class BackendAuthUserService extends SimpleUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendAuthUserService.class);

	@Autowired
	private BackendAuthcFacade authcFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getUser(long)
	 */
	@Override
	protected SimpleUser getUser(long id) {
		return buildSimpleUser(authcFacade.findDealerById(id));
	}

	/**
	 * 组装用户的基本数据。
	 * 
	 * @param dealerVO
	 * @return
	 */
	private SimpleUser buildSimpleUser(DealerVO dealerVO) {
		if (dealerVO == null || dealerVO.getDealer() == null) {
			return null;
		}
		SimpleUser user = new SimpleUser();
		user.setId(dealerVO.getDealer().getId());
		user.setUserName(dealerVO.getDealer().getName());
		user.setLocked(AccountStatus.LOCKED == dealerVO.getDealer().getAccountStatus());
		return user;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#upsertUserWithTransaction(java.lang.String)
	 */
	@Override
	protected SimpleUser upsertUserWithTransaction(String userName) {
		DealerVO dealer = authcFacade.findDealerByName(userName);
		saveUserNameToRequest(userName);
		if (dealer == null || dealer.getId() == 0) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("User {} is unauthenticated!", userName);
			}
			throw new UnauthenticatedAccountException("用户'" + userName + "'无访问商家后台的权限！");
		}
		return buildSimpleUser(dealer);
	}

	/**
	 * 在request中保存用户名
	 * 
	 * @param userName
	 */
	private void saveUserNameToRequest(String userName) {
		Subject subject = SecurityUtils.getSubject();
		HttpServletRequest request = WebUtils.getHttpRequest(subject);
		request.setAttribute("authentication.username", userName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getRoles(java.lang.String)
	 */
	@Override
	protected List<SimpleRole> getRoles(String userName) {
		DealerVO dealer = authcFacade.findDealerByName(userName);
		saveUserNameToRequest(userName);
		if (dealer == null || AccountStatus.LOCKED == dealer.getDealer().getAccountStatus()) {
			throw new LockedAccountException("用户" + userName + "账号已冻结！");
		}
		List<DealerPermissionVO> permissionList = authcFacade.findDealerPermissionByName(userName);
		if (CollectionUtils.isEmpty(permissionList)) {
			throw new LockedAccountException("用户" + userName + "账号无任何的权限！");
		}
		StringBuilder psb = new StringBuilder(512);
		for (DealerPermissionVO vo : permissionList) {
			psb.append(vo.getPermission().getPermission());
			psb.append(SimpleRole.PERMISSION_SPLITE);
		}
		return buildSimpleRole(psb.toString());
	}

	/**
	 * 组装用户的权限列表。
	 * 
	 * @param permissions
	 * @return
	 */
	private List<SimpleRole> buildSimpleRole(String permissions) {
		if (StringUtils.isBlank(permissions))
			return null;
		SimpleRole simpleRole = new SimpleRole();
		simpleRole.setName("backendUser");
		simpleRole.setDisplayName("backendUser");
		simpleRole.setPermissions(permissions);
		List<SimpleRole> list = new ArrayList<>();
		list.add(simpleRole);
		return list;
	}
}
