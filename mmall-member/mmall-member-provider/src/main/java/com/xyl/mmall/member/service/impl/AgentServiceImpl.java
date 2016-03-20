/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.member.dao.AccountDao;
import com.xyl.mmall.member.dao.AgentAreaDao;
import com.xyl.mmall.member.dao.AgentDao;
import com.xyl.mmall.member.dao.AgentRoleDao;
import com.xyl.mmall.member.dao.PermissionDao;
import com.xyl.mmall.member.dao.RoleDao;
import com.xyl.mmall.member.dto.AgentAreaDTO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.dto.AgentRoleDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.meta.Account;
import com.xyl.mmall.member.meta.Agent;
import com.xyl.mmall.member.meta.AgentArea;
import com.xyl.mmall.member.meta.AgentRole;
import com.xyl.mmall.member.meta.Permission;
import com.xyl.mmall.member.meta.Role;
import com.xyl.mmall.member.param.AgentAccountSearchParam;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.member.service.PermissionService;

/**
 * @author lihui
 *
 */
public class AgentServiceImpl implements AgentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgentServiceImpl.class);

	@Autowired
	private AgentDao agentDao;

	@Autowired
	private AgentRoleDao agentRoleDao;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PermissionDao permissionDao;

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private AgentAreaDao agentAreaDao;
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentById(long)
	 */
	@Override
	public AgentDTO findAgentById(long id) {
		Agent agent = agentDao.getObjectById(id);
		return agent == null ? null : new AgentDTO(agent);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentByName(java.lang.String)
	 */
	@Override
	public AgentDTO findAgentByName(String name) {
		Agent agent = agentDao.findByName(name);
		return agent == null ? null : new AgentDTO(agent);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#upsertAgentRole(com.xyl.mmall.member.dto.AgentRoleDTO)
	 */
	@Override
	@Transaction
	public AgentRoleDTO upsertAgentRole(AgentRoleDTO agentRole) {
		agentRole.setExtraPermissions(permissionService.buildPermissionStr(agentRole.getPermissionList()));
		AgentRole existingAgentRole = agentRoleDao
				.findByAgentIdAndRoleId(agentRole.getAgentId(), agentRole.getRoleId());
		if (null == existingAgentRole) {
			agentRole = addNewAgentRole(agentRole);
		} else {
			agentRole = updateExistingAgentRole(agentRole);
		}
		return agentRole;
	}

	/**
	 * @param agentRole
	 * @return
	 */
	private AgentRoleDTO addNewAgentRole(AgentRoleDTO agentRoleDTO) {
		AgentRole agentRole = null;
		try {
			agentRole = agentRoleDao.addObject(agentRoleDTO);
		} catch (DBSupportRuntimeException ex) {
			agentRole = agentRoleDao.findByAgentIdAndRoleId(agentRoleDTO.getAgentId(), agentRoleDTO.getRoleId());
		}
		return new AgentRoleDTO(agentRole);
	}

	/**
	 * @param agentRole
	 * @return
	 */
	private AgentRoleDTO updateExistingAgentRole(AgentRoleDTO agentRole) {
		agentRoleDao.updateObjectByKey(agentRole);
		return agentRole;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#deleteAgentRole(com.xyl.mmall.member.dto.AgentRoleDTO)
	 */
	@Override
	@Transaction
	public void deleteAgentRole(AgentRoleDTO agentRole) {
		agentRoleDao.deleteByAgentIdAndRoleId(agentRole.getAgentId(), agentRole.getRoleId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#deleteAgentById(long)
	 */
	@Override
	@Transaction
	public void deleteAgentById(long agentId, String name) {
		accountDao.deleteAccountByUserName(name);
		agentAreaDao.deleteAgentArea(agentId);
		agentDao.deleteById(agentId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#upsertAgent(com.xyl.mmall.member.dto.AgentDTO,
	 *      long)
	 */
	@Override
	@Transaction
	public AgentDTO upsertAgent(AgentDTO agentDTO, long userId) {
		if (agentDTO.getId() == 0) {
			Agent agent = agentDao.findByName(agentDTO.getName());
			// 1.如数据库表中有此用户名下的用户,创建失败,返回空
			if (agent != null) {
				LOGGER.info("User {} is already exist!", agentDTO.getName());
				return null;
			}
			// 2.创建新的账号
			long id = agentDao.allocateRecordId();
			if (id < 1l) {
				throw new DBSupportRuntimeException("Get generateId failed!");
			}
			agentDTO.setId(id);
			agentDTO.setAccountStatus(AccountStatus.NORMAL);
			agentDTO.setRegTime(System.currentTimeMillis());
			agentDTO.setLastLoginTime(0L);
			agentDTO.setLastModifiedBy(userId);
			agentDTO.setLastModifiedTime(System.currentTimeMillis());
			agentDTO.setEmail(agentDTO.getName());
			try {
				agent = agentDao.addObject(agentDTO);
			} catch (DBSupportRuntimeException ex) {
				agent = agentDao.findByName(agentDTO.getName());
			}
			// 3.分配新的用户组
			List<RoleDTO> roleList = assignRoleToAgent(agent.getId(), agentDTO.getRoleList(), userId);
			// 4.添加密码表
			Account account = new Account();
			account.setUsername(agent.getName());
			account.setEmail(agent.getEmail());
			account.setPassword(agentDTO.getPassword());
			if (accountDao.addAccount(account) == null) {
				throw new DBSupportRuntimeException("Add Account failed! CMS saveAgent.");
			}
			// 5.返回DTO
			AgentDTO newAgent = new AgentDTO(agent);
			newAgent.setRoleList(roleList);
			return newAgent;
		} else {
			// 1.根据id获取已有的账号
			Agent agent = agentDao.getObjectById(agentDTO.getId());
			if (!agent.getName().equalsIgnoreCase(agentDTO.getName())) {
				Agent newAgent = agentDao.findByName(agentDTO.getName());
				// 2. 如数据库表中有此用户名下的用户,创建失败,返回空
				if (newAgent != null) {
					LOGGER.info("User {} is already exist!", agentDTO.getName());
					return null;
				}
			}
			// 3. 更新已有的账号
			agent.setLastModifiedBy(userId);
			agent.setLastModifiedTime(System.currentTimeMillis());
			agent.setMobile(agentDTO.getMobile());
			agent.setName(agentDTO.getName());
			agent.setRealName(agentDTO.getRealName());
			agent.setDepartment(agentDTO.getDepartment());
			agent.setEmpNumber(agentDTO.getEmpNumber());
			agent.setAgentType(agentDTO.getAgentType());
			agent.setEmail(agentDTO.getEmail());
			agentDao.updateObjectByKey(agent);
			// 4.分配新的用户组
			List<RoleDTO> roleList = assignRoleToAgent(agent.getId(), agentDTO.getRoleList(), userId);
			// 5.修改密码
			if (agentDTO.getIsModifyPassword() == 1) {
				LOGGER.info("Update Account password! UserId : {}, agentId : {}.", userId, agent.getId());
				Account account = new Account();
				account.setUsername(agent.getName());
				account.setPassword(agentDTO.getPassword());
				if (!accountDao.updateAccount(account)) {
					throw new DBSupportRuntimeException("Update Account password failed! CMS saveAgent.");
				}
			}
			// 6.返回DTO
			AgentDTO newAgent = new AgentDTO(agent);
			newAgent.setRoleList(roleList);
			return newAgent;
		}
	}

	/**
	 * @param id
	 * @param roleList
	 * @param userId
	 * @return
	 */
	private List<RoleDTO> assignRoleToAgent(long id, List<RoleDTO> roleList, long userId) {
		if (CollectionUtils.isEmpty(roleList)) {
			return null;
		}
		// 删除旧的用户角色组数据
		agentRoleDao.deleteByAgentId(id);
		agentAreaDao.deleteAgentArea(id);
		Long currentTime = System.currentTimeMillis();
		for (RoleDTO roleDTO : roleList) {
			StringBuilder sb = new StringBuilder(256);
			if (!CollectionUtils.isEmpty(roleDTO.getSiteList())) {
				for (Long siteId : roleDTO.getSiteList()) {
					sb.append(siteId);
					sb.append(",");
				}
				for (AgentArea area : roleDTO.getAreaList()) {
					area.setAgentId(id);
				}
			}
			AgentRole newRole = new AgentRole();
			newRole.setAgentId(id);
			newRole.setRoleId(roleDTO.getId());
			newRole.setLastModifiedBy(userId);
			newRole.setLastModifiedTime(currentTime);
			if (sb.length() > 0) {
				newRole.setSites(sb.substring(0, sb.length() - 1));
			}
			try {
				agentRoleDao.addObject(newRole);
				agentAreaDao.addObjects(roleDTO.getAreaList());
			} catch (DBSupportRuntimeException ex) {
				LOGGER.error(ex.getMessage());
			}
		}
		return roleList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentWithRoleById(long)
	 */
	@Override
	public AgentDTO findAgentWithRoleById(long id) {
		AgentDTO agentDTO = findAgentById(id);
		List<Role> roleList = roleDao.findByAgentId(id);
		if (!CollectionUtils.isEmpty(roleList)) {
			List<RoleDTO> roleDTOList = new ArrayList<>();
			for (Role role : roleList) {
				RoleDTO roleDTO = new RoleDTO(role);
				AgentRole agentRole = agentRoleDao.findByAgentIdAndRoleId(id, role.getId());
				if (null != agentRole && !StringUtils.isEmpty(agentRole.getSites())) {
					List<Long> siteList = new ArrayList<>();
					String[] siteArr = agentRole.getSites().split(",");
					for (String siteId : siteArr) {
						siteList.add(Long.parseLong(siteId));
					}
					roleDTO.setSiteList(siteList);
				}
				roleDTOList.add(roleDTO);
			}
			agentDTO.setRoleList(roleDTOList);
		}
		return agentDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentByRoleOwner(long,
	 *      int, int)
	 */
	@Override
	public List<AgentDTO> findAgentByRoleOwner(long userId, int limit, int offset, AgentAccountSearchParam searchParam) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		Agent currentAgent = findAgentById(userId);
		List<Agent> agentList = null;
		if (currentAgent.getAgentType() == AgentType.ROOT) {
			param.setOrderColumn("lastModifiedTime");
			if (searchParam.isHashParam()) {
				agentList = agentDao.findByRoleOwnerId(0l, param, searchParam);
			} else {
				agentList = agentDao.getListByDDBParam(param);
			}
		} else {
			param.setOrderColumn("r.lastModifiedTime");
			agentList = agentDao.findByRoleOwnerId(userId, param, searchParam);
		}
		if (CollectionUtils.isEmpty(agentList)) {
			return null;
		}
		List<AgentDTO> agentDTOList = new ArrayList<>();
		for (Agent agent : agentList) {
			AgentDTO agentDTO = new AgentDTO(agent);
			List<Role> roleList = roleDao.findByAgentId(agent.getId());
			if (!CollectionUtils.isEmpty(roleList)) {
				List<RoleDTO> roleDTOList = new ArrayList<>();
				for (Role role : roleList) {
					RoleDTO roleDTO = new RoleDTO(role);
					AgentRole agentRole = agentRoleDao.findByAgentIdAndRoleId(agent.getId(), role.getId());
					if (!StringUtils.isEmpty(agentRole.getSites())) {
						List<Long> siteList = new ArrayList<>();
						String[] siteArr = agentRole.getSites().split(",");
						for (String id : siteArr) {
							siteList.add(Long.parseLong(id));
						}
						roleDTO.setSiteList(siteList);
					}
					roleDTOList.add(roleDTO);
				}
				agentDTO.setRoleList(roleDTOList);
			}
			agentDTOList.add(agentDTO);
		}
		return agentDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#countAgentByRoleOwner(long)
	 */
	@Override
	public int countAgentByRoleOwner(long userId, AgentAccountSearchParam searchParam) {
		Agent currentAgent = findAgentById(userId);
		if (currentAgent.getAgentType() == AgentType.ROOT) {
			if (searchParam.isHashParam()) {
				return agentDao.countByRoleOwnerId(0l, searchParam);
			} else {
				return (int) agentDao.getCount();
			}
		} else {
			return agentDao.countByRoleOwnerId(userId, searchParam);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#deleteAgentRoleByRoleId(long)
	 */
	@Override
	@Transaction
	public void deleteAgentRoleByRoleId(long roleId) {
		List<Long> idList = agentRoleDao.getAgentIdByRoleId(roleId);
		if (idList == null) {
			return;
		}
		for (long agentId : idList) {
			List<Role> roleList = roleDao.findByAgentId(agentId);
			if (roleList.size() == 1) {
				if (roleList.get(0).getId() == roleId) {
					Agent agent = agentDao.getObjectById(agentId);
					agentDao.deleteById(agentId);
					accountDao.deleteAccountByUserName(agent.getName());
				}
			}
		}
		agentRoleDao.deleteByRoleId(roleId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#updateAgentAccountStatus(long,
	 *      long, com.xyl.mmall.member.enums.AccountStatus)
	 */
	@Override
	@Transaction
	public void updateAgentAccountStatus(long userId, long id, AccountStatus status) {
		Agent agent = agentDao.getObjectById(id);
		if (agent != null) {
			agent.setAccountStatus(status);
			agent.setLastModifiedTime(System.currentTimeMillis());
			agent.setLastModifiedBy(userId);
			agentDao.updateObjectByKey(agent);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentByAgentType(com.xyl.mmall.member.enums.AgentType)
	 */
	@Override
	public List<AgentDTO> findAgentByAgentType(AgentType agentType) {
		List<Agent> agentList = agentDao.findByAgentType(agentType.getIntValue());
		if (CollectionUtils.isEmpty(agentList)) {
			return null;
		}
		List<AgentDTO> agentDTOList = new ArrayList<>();
		for (Agent agent : agentList) {
			AgentDTO agentDTO = new AgentDTO(agent);
			agentDTOList.add(agentDTO);
		}
		return agentDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentSiteIdsByPermission(long,
	 *      java.lang.String)
	 */
	@Override
	public List<Long> findAgentSiteIdsByPermission(long userId, String permission) {
		List<AgentRole> agentRoleList = agentRoleDao.findByAgentId(userId);
		List<Role> roleList = roleDao.findByAgentId(userId);
		Permission entity = permissionDao.findByCategoryAndPermission(AuthzCategory.CMS.getIntValue(), permission);
		if (!CollectionUtils.isEmpty(agentRoleList) && !CollectionUtils.isEmpty(roleList) && entity != null) {
			Set<Long> areaSet = new HashSet<>();
			agentRoleList: for (AgentRole agentRole : agentRoleList) {
				for (Role role : roleList) {
					if (role.getId() == agentRole.getRoleId()) {
						areaSet.addAll(findAgentRoleAreaHasPermission(role.getPermissions(), permission,
								agentRole.getSites()));
						continue agentRoleList;
					}
				}
			}
			return new ArrayList<>(areaSet);
		}
		return null;
	}

	/**
	 * 根据指定的权限查找对应运维用户关联相关角色时指定的站点列表。
	 * 
	 * @param permissions
	 *            对应角色权限ID集合
	 * @param permission
	 *            指定权限
	 * @param sites
	 *            对应角色站点ID集合
	 * @return 站点列表
	 */
	private List<Long> findAgentRoleAreaHasPermission(String permissions, String permission, String sites) {
		List<Long> areaList = new ArrayList<>();
		List<PermissionDTO> permissionList = permissionService.findPermissionsByIdsAndCategory(permissions,
				AuthzCategory.CMS.getIntValue());
		if (!CollectionUtils.isEmpty(permissionList) && !StringUtils.isEmpty(sites)) {
			for (PermissionDTO permissionDTO : permissionList) {
				if (permissionDTO.getPermission().equals(permission)) {
					String[] sitesArr = StringUtils.split(sites, ",");
					if (sitesArr != null && sitesArr.length > 0) {
						for (String site : sitesArr) {
							areaList.add(Long.parseLong(site));
						}
						return areaList;
					}
				}
			}
		}
		return areaList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentByIdList(java.util.List)
	 */
	@Override
	public List<AgentDTO> findAgentByIdList(List<Long> idList) {
		List<Agent> agentList = agentDao.findAgentByIdList(idList);
		List<AgentDTO> dtoList = new ArrayList<>();
		if (CollectionUtils.isEmpty(agentList)) {
			return dtoList;
		}
		for (Agent agent : agentList) {
			dtoList.add(new AgentDTO(agent));
		}
		return dtoList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.AgentService#findAgentByPermissionList(java.util.List)
	 */
	@Override
	public List<AgentDTO> findAgentByPermissionList(List<String> permissionList) {
		List<AgentDTO> agentDTOList = new ArrayList<>();
		if (CollectionUtils.isEmpty(permissionList)) {
			return agentDTOList;
		}
		Map<Long, PermissionDTO> permMap = permissionService.findAllCmsPermissions();
		if (CollectionUtils.isEmpty(permMap)) {
			return agentDTOList;
		}
		Set<Long> requiredPermIdSet = new HashSet<>();
		for (Long key : permMap.keySet()) {
			PermissionDTO permissionDTO = permMap.get(key);
			for (String requiredPerm : permissionList) {
				if (permissionDTO.getPermission().equalsIgnoreCase(requiredPerm)) {
					requiredPermIdSet.add(key);
				}
			}
		}
		List<Role> agentRoleList = roleDao.findByCategory(AuthzCategory.CMS.getIntValue(), DDBParam.genParamX(1000));
		if (CollectionUtils.isEmpty(agentRoleList)) {
			return agentDTOList;
		}
		Set<Long> roleIdSet = new HashSet<>();
		for (Role role : agentRoleList) {
			String[] rolePerm = StringUtils.split(role.getPermissions(), ",");
			if (rolePerm == null || rolePerm.length == 0) {
				continue;
			}
			for (String permId : rolePerm) {
				for (Long requiredPermId : requiredPermIdSet) {
					if (requiredPermId.toString().equalsIgnoreCase(permId)) {
						roleIdSet.add(role.getId());
					}
				}
			}
		}
		List<Agent> agentList = agentDao.findByRoleIdList(new ArrayList<>(roleIdSet));
		if (CollectionUtils.isEmpty(agentList)) {
			return agentDTOList;
		}
		for (Agent agent : agentList) {
			agentDTOList.add(new AgentDTO(agent));
		}
		return agentDTOList;
	}

	@Override
	public boolean isContainPermission(long userId, String permission) {
		boolean flag = false;
		Permission entity = permissionDao.findByCategoryAndPermission(AuthzCategory.CMS.getIntValue(), permission);
		if (entity == null) {
			return flag;
		}
		List<Role> roleList = roleDao.findByAgentId(userId);
		for (Role role : roleList) {
			String permissions = role.getPermissions();
			if (StringUtils.isBlank(permissions)) {
				continue;
			}
			if (Arrays.asList(permissions.split(",")).contains(String.valueOf(entity.getId()))) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public List<AgentAreaDTO> getAgentAreaList(long userId, long siteId) {
		List<AgentArea> agentAreaList = agentAreaDao.getAgentAreaList(userId, siteId);
		return convertToAgentAreaDTO(agentAreaList);
	}
	
	private List<AgentAreaDTO> convertToAgentAreaDTO(List<AgentArea> agentAreaList) {
		if (CollectionUtils.isEmpty(agentAreaList)) {
			return null;
		}
		List<AgentAreaDTO> retList = new ArrayList<AgentAreaDTO>(agentAreaList.size());
		for (AgentArea agentArea : agentAreaList) {
			retList.add(new AgentAreaDTO(agentArea));
		}
		return retList;
	}

	@Override
	public List<AgentRoleDTO> findByAgentId(long agentId) {
		List<AgentRole> agentRoleList = agentRoleDao.findByAgentId(agentId);
		if (CollectionUtils.isEmpty(agentRoleList)) {
			return null;
		}
		List<AgentRoleDTO> retList = new ArrayList<AgentRoleDTO>(agentRoleList.size());
		for (AgentRole agentRole : agentRoleList) {
			retList.add(new AgentRoleDTO(agentRole));
		}
		return retList;
	}
	
	@Override
	public List<Long> getAgentIdListByAreaIds(List<Long>areaIds){
		return agentAreaDao.getAgentIdListByAreaIds(areaIds);
	}
}
