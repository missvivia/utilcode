/**
 * 
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.vo.LeftNavItemVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.enums.PermissionFunctionType;
import com.xyl.mmall.member.service.PermissionService;

/**
 * @author lihui
 *
 */
@Facade("cmsLeftNavigationFacade")
public class LeftNavigationFacadeImpl implements LeftNavigationFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityFacadeImpl.class);

	@Resource
	private PermissionService permissionService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.LeftNavigationFacade#getLeftNavigationMenu(long)
	 */
	@Override
	public List<LeftNavItemVO> getLeftNavigationMenu(long userId) {
		List<LeftNavItemVO> permissionVOList = new ArrayList<>();
		List<Long> parentIds = new ArrayList<>();
		List<PermissionDTO> totalPermissionList = permissionService.findAgentPermissionsByAgentId(userId);
		List<PermissionDTO> permissionList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(totalPermissionList)) {
			for (PermissionDTO permissionDTO : totalPermissionList) {
				if (permissionDTO.getFunctionType() == null
						|| permissionDTO.getFunctionType() == PermissionFunctionType.LEFTNAVE) {
					permissionList.add(permissionDTO);
				}
			}
		}
		if (!CollectionUtils.isEmpty(permissionList)) {
			Map<Long, List<LeftNavItemVO>> childrenPermissionMap = new HashMap<>();
			for (PermissionDTO permissionDTO : permissionList) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Agent (id = {}) has left nav item:{}.", userId, permissionDTO.getName());
				}
				boolean findParent = false;
				for (Long parentId : parentIds) {
					if (parentId.equals(permissionDTO.getParentId())) {
						findParent = true;
						break;
					}
				}
				if (!findParent) {
					// 未找到parent节点，根据parentId查找父节点权限。
					parentIds.add(permissionDTO.getParentId());
				}
				if (childrenPermissionMap.containsKey(permissionDTO.getParentId())) {
					childrenPermissionMap.get(permissionDTO.getParentId()).add(new LeftNavItemVO(permissionDTO));
				} else {
					List<LeftNavItemVO> childrenList = new ArrayList<>();
					childrenList.add(new LeftNavItemVO(permissionDTO));
					childrenPermissionMap.put(permissionDTO.getParentId(), childrenList);
				}
			}
			List<PermissionDTO> parentDTOList = permissionService.findPermissionsIncludeParentByIdListAndCategory(
					parentIds, AuthzCategory.CMS.getIntValue());
			for (PermissionDTO parentDTO : parentDTOList) {
				LeftNavItemVO parentVO = new LeftNavItemVO(parentDTO);
				parentVO.setChildren(childrenPermissionMap.get(parentDTO.getId()));
				permissionVOList.add(parentVO);
			}
		} else {
			LOGGER.error("Current user(id = {}) dosen't have any permission!", userId);
		}
		return permissionVOList;
	}

}
