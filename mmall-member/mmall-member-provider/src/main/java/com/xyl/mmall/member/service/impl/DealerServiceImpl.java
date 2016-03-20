/**
 * 
 */
package com.xyl.mmall.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.member.dao.AccountDao;
import com.xyl.mmall.member.dao.DealerDao;
import com.xyl.mmall.member.dao.DealerRoleDao;
import com.xyl.mmall.member.dao.RoleDao;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.dto.DealerRoleDTO;
import com.xyl.mmall.member.dto.RoleDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.DealerType;
import com.xyl.mmall.member.meta.Account;
import com.xyl.mmall.member.meta.Dealer;
import com.xyl.mmall.member.meta.DealerRole;
import com.xyl.mmall.member.meta.Role;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.member.service.PermissionService;

/**
 * @author lihui
 *
 */
public class DealerServiceImpl implements DealerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DealerServiceImpl.class);

	@Autowired
	private DealerDao dealerDao;

	@Autowired
	private DealerRoleDao dealerRoleDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private AccountDao accountDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#assignNewDealerOwner(java.lang.String,
	 *      long)
	 */
	@Override
	@Transaction
	public DealerDTO assignNewDealerOwner(String name, long supplierId) {
		Dealer dealer = dealerDao.findByName(name);
		// 1.如数据库表中有此用户名下的用户,创建失败,返回空
		if (dealer != null) {
			LOGGER.info("User {} is already exist!", name);
			return null;
		}
		List<Dealer> supplierAccountList = dealerDao.findBySupplierId(supplierId);
		// 如果该供应商ID无任何账号，为首次创建商家账号
		// 否则改供应商商家账号已存在，更新账号名
		if (CollectionUtils.isEmpty(supplierAccountList)) {
			// 2.创建新的账号
			dealer = addNewDealerOwner(name, supplierId, DealerType.OWNER);
			// 3.创建新的商家用户组管理
			Role adminRole = roleDao.getBackendAdmin();
			List<RoleDTO> roleList = new ArrayList<>();
			roleList.add(new RoleDTO(adminRole));
			assignRoleToDealer(dealer.getId(), roleList, dealer.getId());
		} else {
			// 2.找到商家账号
			for (Dealer existDealer : supplierAccountList) {
				if (existDealer.getDealerType() == DealerType.OWNER) {
					// 3.更新账号名
					existDealer.setName(name);
					existDealer.setLastModifiedBy(existDealer.getId());
					existDealer.setLastModifiedTime(System.currentTimeMillis());
					dealerDao.updateObjectByKey(existDealer);
					dealer = existDealer;
				}
			}
		}
		// 4.创建返回的DTO
		return new DealerDTO(dealer);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#upsertDealerEmployee(com.xyl.mmall.member.dto.DealerDTO,
	 *      long)
	 */
	@Override
	@Transaction
	public DealerDTO upsertDealerEmployee(DealerDTO dealerDTO, long userId) {
		DealerDTO currentUser = findDealerById(userId);
		if (dealerDTO.getId() == 0) {
			//Dealer dealer = dealerDao.findByName(dealerDTO.getName());
		    Account account = accountDao.getAccountByUserName(dealerDTO.getName());
			// 1. 如数据库表中有此用户名下的用户,创建失败,返回空
			if (account != null) {
				LOGGER.info("User {} is already exist!", dealerDTO.getName());
				return null;
			}
			Dealer dealer = null;
			long id = dealerDao.allocateRecordId();
			if (id < 1l) {
				throw new DBSupportRuntimeException("Get generateId failed!");
			}
			// 2.创建新的账号
			dealerDTO.setAccountStatus(AccountStatus.NORMAL);
			dealerDTO.setDealerType(DealerType.EMPLOYEE);
			dealerDTO.setRegTime(System.currentTimeMillis());
			dealerDTO.setLastLoginTime(0L);
			dealerDTO.setLastModifiedBy(userId);
			dealerDTO.setLastModifiedTime(System.currentTimeMillis());
			dealerDTO.setSupplierId(currentUser.getSupplierId());
			dealerDTO.setId(id);
			account = new Account();
			account.setEmail(dealerDTO.getName());
			account.setUsername(dealerDTO.getName());
			account.setPassword(dealerDTO.getPassword()==null?"123456":dealerDTO.getPassword());//for dev
			try {
				//创建密码账号
				accountDao.addAccount(account);
				dealer = dealerDao.addObject(dealerDTO);
			} catch (DBSupportRuntimeException e) {
				dealer = dealerDao.findByName(dealerDTO.getName());
			}
			// 3.分配新的用户组
			List<RoleDTO> roleList = assignRoleToDealer(dealer.getId(), dealerDTO.getRoleList(), userId);
			// 4.返回DTO
			DealerDTO newDealer = new DealerDTO(dealer);
			newDealer.setRoleList(roleList);
			return newDealer;
		} else {
			// 1.根据id获取已有的账号,并判断账号是否修改
			Dealer existingDealer = dealerDao.getObjectById(dealerDTO.getId());
//			if (!existingDealer.getName().equalsIgnoreCase(dealerDTO.getName())) {
//				Dealer dealer = dealerDao.findByName(dealerDTO.getName());
//				// 2. 如数据库表中有此用户名下的用户,创建失败,返回空
//				if (dealer != null) {
//					LOGGER.info("User {} is already exist!", dealerDTO.getName());
//					return null;
//				}
//			}
			if(existingDealer==null){
				LOGGER.info("User {} is not exist!", dealerDTO.getId());
				return null;
			}
			// 3.检查是否同一供应商
			if (currentUser.getSupplierId() == existingDealer.getSupplierId()) {
				// 4. 更新已有的账号
				existingDealer.setLastModifiedBy(userId);
				existingDealer.setLastModifiedTime(System.currentTimeMillis());
				existingDealer.setDepartment(dealerDTO.getDepartment());
				existingDealer.setEmpNumber(dealerDTO.getEmpNumber());
				existingDealer.setMobile(dealerDTO.getMobile());
				//不允许修改登录账号
				//existingDealer.setName(dealerDTO.getName());
				existingDealer.setRealName(dealerDTO.getRealName());
				dealerDao.updateObjectByKey(existingDealer);
				if(dealerDTO.getPasswordIsChange().equalsIgnoreCase("Y")){
					Account account = new Account();
					account.setUsername(existingDealer.getName());
					account.setPassword(dealerDTO.getPassword());
					if (!accountDao.updateAccount(account)) {
						throw new DBSupportRuntimeException("Update Account password failed! upsertDealerEmployee.");
					}
					LOGGER.info("change dealer {} password! {} / {}", dealerDTO.getId(),account.getUsername(),account.getPassword());
				}
				// 5.分配新的用户组
				List<RoleDTO> roleList = assignRoleToDealer(existingDealer.getId(), dealerDTO.getRoleList(), userId);
				// 6.返回DTO
				DealerDTO newDealer = new DealerDTO(existingDealer);
				newDealer.setRoleList(roleList);
				return newDealer;
			} else {
				// 4.非当前供应商账号，不能更新
				LOGGER.info("User {} is not belong to current dealer {}!", dealerDTO.getName(),
						existingDealer.getName());
				return null;
			}
		}
	}

	/**
	 * @param id
	 * @param roleList
	 * @param userId
	 * @return
	 */
	private List<RoleDTO> assignRoleToDealer(long id, List<RoleDTO> roleList, long userId) {
		if (CollectionUtils.isEmpty(roleList)) {
			return null;
		}
		// 删除旧的用户角色组数据
		dealerRoleDao.deleteByDealerId(id);
		Long currentTime = System.currentTimeMillis();
		for (RoleDTO roleDTO : roleList) {
			DealerRole newRole = new DealerRole();
			newRole.setDealerId(id);
			newRole.setRoleId(roleDTO.getId());
			newRole.setLastModifiedBy(userId);
			newRole.setLastModifiedTime(currentTime);
			dealerRoleDao.addObject(newRole);//
		}
		return roleList;
	}

	/**
	 * 
	 * @param name
	 * @param supplierId
	 * @param dealerType
	 * @return
	 */
	private Dealer addNewDealerOwner(String name, long supplierId, DealerType dealerType) {
		long id = dealerDao.allocateRecordId();
		if (id < 1l) {
			throw new DBSupportRuntimeException("Get generateId failed!");
		}
		Dealer dealer = new Dealer();
		dealer.setName(name);
		dealer.setRegTime(System.currentTimeMillis());
		dealer.setSupplierId(supplierId);
		dealer.setDealerType(dealerType);
		dealer.setAccountStatus(AccountStatus.NORMAL);
		dealer.setLastLoginTime(0L);
		dealer.setId(id);
		return dealerDao.addObject(dealer);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#findDealerById(long)
	 */
	@Override
	public DealerDTO findDealerById(long id) {
		Dealer dealer = dealerDao.getObjectById(id);
		if (dealer == null) {
			return null;
		}
		return new DealerDTO(dealer);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#findDealerByName(java.lang.String)
	 */
	@Override
	public DealerDTO findDealerByName(String name) {
		Dealer dealer = dealerDao.findByName(name);
		if (dealer == null) {
			return null;
		}
		return new DealerDTO(dealer);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#findAllDealerEmployee(long)
	 */
	@Override
	public List<DealerDTO> findAllDealerEmployee(long supplierId) {
		List<Dealer> dealerList = dealerDao.findBySupplierId(supplierId);
		if (CollectionUtils.isEmpty(dealerList)) {
			return null;
		}
		List<DealerDTO> dealerDTOList = new ArrayList<>();
		for (Dealer dealer : dealerList) {
			if (dealer.getDealerType() == DealerType.EMPLOYEE) {
				dealerDTOList.add(new DealerDTO(dealer));
			}
		}
		return dealerDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#upsertDealerRole(com.xyl.mmall.member.dto.DealerRoleDTO)
	 */
	@Override
	@Transaction
	public DealerRoleDTO upsertDealerRole(DealerRoleDTO dealerRole) {
		dealerRole.setExtraPermissions(permissionService.buildPermissionStr(dealerRole.getPermissionList()));
		DealerRole existingDealerRole = dealerRoleDao.findByDealerIdAndRoleId(dealerRole.getDealerId(),
				dealerRole.getRoleId());
		if (null == existingDealerRole) {
			dealerRole = addNewDealerRole(dealerRole);
		} else {
			dealerRole = updateExistingDealerRole(dealerRole);
		}
		return dealerRole;
	}

	/**
	 * @param dealerRoleDTO
	 * @return
	 */
	private DealerRoleDTO updateExistingDealerRole(DealerRoleDTO dealerRoleDTO) {
		dealerRoleDao.updateObjectByKey(dealerRoleDTO);
		return dealerRoleDTO;
	}

	/**
	 * @param dealerRoleDTO
	 * @return
	 */
	private DealerRoleDTO addNewDealerRole(DealerRoleDTO dealerRoleDTO) {
		DealerRole dealerRole = null;
		try {
			dealerRole = dealerRoleDao.addObject(dealerRoleDTO);
		} catch (DBSupportRuntimeException e) {
			dealerRole = dealerRoleDao.findByDealerIdAndRoleId(dealerRoleDTO.getDealerId(), dealerRoleDTO.getRoleId());
		}
		return new DealerRoleDTO(dealerRole);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#deleteDealerRole(com.xyl.mmall.member.dto.DealerRoleDTO)
	 */
	@Override
	@Transaction
	public void deleteDealerRole(DealerRoleDTO dealerRole) {
		dealerRoleDao.deleteByDealerIdAndRoleId(dealerRole.getDealerId(), dealerRole.getRoleId());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#findDealerByRoleOwner(long,
	 *      int, int)
	 */
	@Override
	public List<DealerDTO> findDealerByRoleOwner(long userId, int limit, int offset) {
		DDBParam param = DDBParam.genParam1();
		param.setLimit(limit);
		param.setOffset(offset);
		param.setAsc(false);
		List<Dealer> dealerList = null;
		DealerDTO currentDealer = findDealerById(userId);
		if (DealerType.OWNER == currentDealer.getDealerType()) {
			param.setOrderColumn("lastModifiedTime");
			dealerList = dealerDao.findBySupplierId(currentDealer.getSupplierId(), param);
		} else {
			param.setOrderColumn("r.lastModifiedTime");
			dealerList = dealerDao.findByRoleOwnerId(userId, param);
		}
		if (CollectionUtils.isEmpty(dealerList)) {
			return null;
		}
		List<DealerDTO> dealerDTOList = new ArrayList<>();
		for (Dealer dealer : dealerList) {
			// 只返回员工的账号
			if (dealer.getDealerType() == DealerType.EMPLOYEE) {
				DealerDTO dealerDTO = new DealerDTO(dealer);
				List<Role> roleList = roleDao.findByDealerId(dealer.getId());
				if (!CollectionUtils.isEmpty(roleList)) {
					List<RoleDTO> roleDTOList = new ArrayList<>();
					for (Role role : roleList) {
						roleDTOList.add(new RoleDTO(role));
					}
					dealerDTO.setRoleList(roleDTOList);
				}
				dealerDTOList.add(dealerDTO);
			}
		}
		return dealerDTOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#countDealerByRoleOwner(long)
	 */
	@Override
	public int countDealerByRoleOwner(long userId) {
		DealerDTO currentDealer = findDealerById(userId);
		if (DealerType.OWNER == currentDealer.getDealerType()) {
			int result = dealerDao.countBySupplierId(currentDealer.getSupplierId());
			// 减去当前主账号
			return result > 0 ? result - 1 : result;
		} else {
			return dealerDao.countByRoleOwnerId(userId);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#findDealerWithRoleById(long)
	 */
	@Override
	public DealerDTO findDealerWithRoleById(long id) {
		DealerDTO dealerDTO = findDealerById(id);
		List<Role> roleList = roleDao.findByDealerId(id);
		if (!CollectionUtils.isEmpty(roleList)) {
			List<RoleDTO> roleDTOList = new ArrayList<>();
			for (Role role : roleList) {
				roleDTOList.add(new RoleDTO(role));
			}
			dealerDTO.setRoleList(roleDTOList);
		}
		return dealerDTO;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#deleteDealerById(long)
	 */
	@Override
	@Transaction
	public void deleteDealerById(long dealerId) {
		Dealer dealer = dealerDao.getObjectById(dealerId);
		if(dealer==null){
			return;
		}
		accountDao.deleteAccountByUserName(dealer.getName());
		dealerDao.deleteById(dealerId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#deleteDealerRoleByRoleId(long)
	 */
	@Override
	@Transaction
	public void deleteDealerRoleByRoleId(long roleId) {
		dealerRoleDao.deleteByRoleId(roleId);
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#suspendDealerOwner(java.lang.String,
	 *      long, long)
	 */
	@Override
	@Transaction
	public void suspendDealerOwner(String name, long supplierId, long userId) {
		Dealer dealerOwner = dealerDao.findByName(name);
		if (dealerOwner != null && dealerOwner.getSupplierId() == supplierId) {
			List<Dealer> dealerList = dealerDao.findBySupplierId(supplierId);
			for (Dealer dealer : dealerList) {
				dealer.setAccountStatus(AccountStatus.SUSPEND);
				dealer.setLastModifiedBy(userId);
				dealer.setLastModifiedTime(System.currentTimeMillis());
				dealerDao.updateObjectByKey(dealer);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#updateDealerAccountStatus(long,
	 *      long, com.xyl.mmall.member.enums.AccountStatus)
	 */
	@Override
	@Transaction
	public void updateDealerAccountStatus(long userId, long id, AccountStatus status) {
		Dealer dealer = dealerDao.getObjectById(id);
		if (dealer != null) {
			// 如果操作为锁定商家管理员，将会冻结同一商家的所有账号
			if (DealerType.OWNER == dealer.getDealerType() && AccountStatus.LOCKED == status) {
				dealerDao.lockAllSupplierAccount(dealer.getSupplierId(), userId);
			} else {
				dealer.setAccountStatus(status);
				dealer.setLastModifiedTime(System.currentTimeMillis());
				dealer.setLastModifiedBy(userId);
				dealerDao.updateObjectByKey(dealer);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.member.service.DealerService#findDealerById(java.util.List)
	 */
	@Override
	public List<DealerDTO> findDealerById(List<Long> idList) {
		List<Dealer> dealerList = dealerDao.findDealerByIdList(idList);
		List<DealerDTO> dtoList = new ArrayList<>();
		if (CollectionUtils.isEmpty(dealerList)) {
			return dtoList;
		}
		for (Dealer dealer : dealerList) {
			dtoList.add(new DealerDTO(dealer));
		}
		return dtoList;
	}

	@Override
	@Transaction
	public void deleteDealerInfoByBusinessID(long businessId) throws ServiceException {
		List<Dealer> dealerList = dealerDao.findBySupplierId(businessId);
		if(CollectionUtil.isEmptyOfList(dealerList)){
			return;
		}
		try{
			for(Dealer dealer:dealerList){
				dealerRoleDao.deleteByDealerId(dealer.getId());
				dealerDao.deleteById(dealer.getId());
			}
		}catch(Exception e){
			LOGGER.error("delete dealer info error while deleting businesser "+ businessId, e);
			throw new ServiceException("delete dealer info error while deleting businesser "+ businessId);
		}
	}
}
