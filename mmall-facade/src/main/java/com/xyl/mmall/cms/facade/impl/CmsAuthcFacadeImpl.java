/**
 * 
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.cms.facade.CmsAuthcFacade;
import com.xyl.mmall.cms.vo.AgentPermissionVO;
import com.xyl.mmall.cms.vo.AgentVO;
import com.xyl.mmall.cms.vo.CmsFilterChainResourceVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.FilterChainResourceDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.FilterChainResourceService;
import com.xyl.mmall.member.service.PermissionService;

/**
 * @author lihui
 *
 */
@Facade
public class CmsAuthcFacadeImpl implements CmsAuthcFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsAuthcFacadeImpl.class);

	@Resource
	private AgentService agentService;

	@Resource
	private PermissionService permissionService;

	@Resource
	private FilterChainResourceService filterChainResourceService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.CmsAuthcFacade.AuthcFacade#findAgentByName(java.lang.String)
	 */
	@Override
	public AgentVO findAgentByName(String name) {
		AgentDTO agentDTO = agentService.findAgentByName(name);
		if (agentDTO == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Agent {} is not exsiting.", name);
			}
			return null;
		}
		return new AgentVO(agentDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.CmsAuthcFacade.AuthcFacade#findAgentById(long)
	 */
	@Override
	public AgentVO findAgentById(long id) {
		AgentDTO agentDTO = agentService.findAgentById(id);
		if (agentDTO == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Agent (id={}) is not exsiting.", id);
			}
			return null;
		}
		return new AgentVO(agentDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.CmsAuthcFacade#getCmsFilterChainResource()
	 */
	@Override
	public List<CmsFilterChainResourceVO> getCmsFilterChainResource() {
		List<FilterChainResourceDTO> resourceDTOList = filterChainResourceService
				.findResourceByCategory(AuthzCategory.CMS.getIntValue());
		if (CollectionUtils.isEmpty(resourceDTOList)) {
			LOGGER.error("Should not reach here because there are always filter chain defines required.");
			return null;
		}
		List<CmsFilterChainResourceVO> resourceVOList = new ArrayList<>();
		for (FilterChainResourceDTO resourceDTO : resourceDTOList) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add filter chain define: {} = {}", resourceDTO.getUrl(), resourceDTO.getPermission());
			}
			resourceVOList.add(new CmsFilterChainResourceVO(resourceDTO));
		}
		return resourceVOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.cms.facade.CmsAuthcFacade#findAgentPermissionByName(java.lang.String)
	 */
	@Override
	public List<AgentPermissionVO> findAgentPermissionByName(String userName) {
		List<PermissionDTO> permissions = permissionService.findAgentPermissionsByAgentName(userName);
		List<AgentPermissionVO> permissionVOList = new ArrayList<>();
		if (CollectionUtils.isEmpty(permissions)) {
			LOGGER.error("Agent {} dosen't have any permissions.", userName);
			return permissionVOList;
		}
		for (PermissionDTO permissionDTO : permissions) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Agent {} has permission: {}", userName, permissionDTO.getName());
			}
			permissionVOList.add(new AgentPermissionVO(permissionDTO));
		}
		return permissionVOList;
	}

}
