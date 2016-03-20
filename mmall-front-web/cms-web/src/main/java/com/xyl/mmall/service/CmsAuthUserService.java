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
import com.xyl.mmall.cms.facade.CmsAuthcFacade;
import com.xyl.mmall.cms.vo.AgentPermissionVO;
import com.xyl.mmall.cms.vo.AgentVO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.security.exception.UnauthenticatedAccountException;

/**
 * 运维平台用户相关服务，为安全验证提供用户信息和权限信息等。
 * 
 * @author lihui
 *
 */
@Service("applicationAuthUserService")
public class CmsAuthUserService extends SimpleUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsAuthUserService.class);

	@Autowired
	private CmsAuthcFacade authcFacade;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getUser(long)
	 */
	@Override
	protected SimpleUser getUser(long id) {
		return buildSimpleUser(authcFacade.findAgentById(id));
	}

	/**
	 * 组装用户的基本数据。
	 * 
	 * @param agentVO
	 * @return
	 */
	private SimpleUser buildSimpleUser(AgentVO agentVO) {
		if (agentVO == null) {
			return null;
		}
		SimpleUser user = new SimpleUser();
		user.setId(agentVO.getAgent().getId());
		user.setUserName(agentVO.getAgent().getName());
		user.setLocked(AccountStatus.NORMAL != agentVO.getAgent().getAccountStatus());
		return user;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#upsertUserWithTransaction(java.lang.String)
	 */
	@Override
	protected SimpleUser upsertUserWithTransaction(String userName) {
		saveUserNameToRequest(userName);
		AgentVO agent = authcFacade.findAgentByName(userName);
		if (agent == null || agent.getId() == 0) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("User {} is unauthenticated!", userName);
			}
			throw new UnauthenticatedAccountException("用户'" + userName + "'无访问运营后台的权限！");
		}
		return buildSimpleUser(agent);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netease.print.security.user.SimpleUserService#getRoles(java.lang.String)
	 */
	@Override
	protected List<SimpleRole> getRoles(String userName) {
		AgentVO agent = authcFacade.findAgentByName(userName);
		saveUserNameToRequest(userName);
		if (agent == null || AccountStatus.LOCKED == agent.getAgent().getAccountStatus()) {
			throw new LockedAccountException("用户" + userName + "帐号已冻结！");
		}
		List<AgentPermissionVO> permissionList = authcFacade.findAgentPermissionByName(userName);
		if (CollectionUtils.isEmpty(permissionList)) {
			throw new LockedAccountException("用户" + userName + "帐号无任何的权限！");
		}
		StringBuilder psb = new StringBuilder(512);
		for (AgentPermissionVO vo : permissionList) {
			psb.append(vo.getPermission().getPermission());
			psb.append(SimpleRole.PERMISSION_SPLITE);
		}
		return buildSimpleRole(psb.toString());
	}

	/**
	 * 在reqeust中保存用户名
	 * 
	 * @param userName
	 * 
	 */
	private void saveUserNameToRequest(String userName) {
		Subject subject = SecurityUtils.getSubject();
		HttpServletRequest request = WebUtils.getHttpRequest(subject);
		request.setAttribute("authentication.username", userName);
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
		simpleRole.setName("cmsUser");
		simpleRole.setDisplayName("cmsUser");
		simpleRole.setPermissions(permissions);
		List<SimpleRole> list = new ArrayList<>();
		list.add(simpleRole);
		return list;
	}

}
