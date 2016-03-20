/**
 * 
 */
package com.xyl.mmall.backend.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.xyl.mmall.backend.facade.BackendAuthcFacade;
import com.xyl.mmall.backend.vo.BackendFilterChainResourceVO;
import com.xyl.mmall.backend.vo.DealerPermissionVO;
import com.xyl.mmall.backend.vo.DealerVO;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.member.dto.DealerDTO;
import com.xyl.mmall.member.dto.FilterChainResourceDTO;
import com.xyl.mmall.member.dto.PermissionDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AuthzCategory;
import com.xyl.mmall.member.service.DealerService;
import com.xyl.mmall.member.service.FilterChainResourceService;
import com.xyl.mmall.member.service.PermissionService;
import com.xyl.mmall.security.utils.DigestUtils;

/**
 * @author lihui
 *
 */
@Facade
public class BackendAuthcFacadeImpl implements BackendAuthcFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendAuthcFacadeImpl.class);

	private static final String SEP = "|";

	@Resource
	private DealerService dealerService;

	@Resource
	private PermissionService permissionService;

	@Resource
	private FilterChainResourceService filterChainResourceService;

	@Resource
	private BusinessService businessService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.BackendAuthcFacade#findDealerByName(java.lang.String)
	 */
	@Override
	public DealerVO findDealerByName(String name) {
		DealerDTO dealerDTO = dealerService.findDealerByName(name);
		if (dealerDTO == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Dealer {} is not exsiting.", name);
			}
			return null;
		}
		return new DealerVO(dealerDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.BackendAuthcFacade#findDealerById(long)
	 */
	@Override
	public DealerVO findDealerById(long id) {
		DealerDTO dealerDTO = dealerService.findDealerById(id);
		if (dealerDTO == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Dealer (id={}) is not exsiting.", id);
			}
			return null;
		}
		return new DealerVO(dealerDTO);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.BackendAuthcFacade#findDealerPermissionByName(java.lang.String)
	 */
	@Override
	public List<DealerPermissionVO> findDealerPermissionByName(String name) {
		List<PermissionDTO> permissions = permissionService.findDealerPermissionsByDealerName(name);
		List<DealerPermissionVO> permissionVOList = new ArrayList<>();
		if (CollectionUtils.isEmpty(permissions)) {
			LOGGER.error("Dealer {} dosen't have any permissions.", name);
			return permissionVOList;
		}
		for (PermissionDTO permissionDTO : permissions) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Dealer {} has permission: {}", name, permissionDTO.getName());
			}
			permissionVOList.add(new DealerPermissionVO(permissionDTO));
		}
		return permissionVOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.BackendAuthcFacade#getBackendFilterChainResource()
	 */
	@Override
	public List<BackendFilterChainResourceVO> getBackendFilterChainResource() {
		List<FilterChainResourceDTO> resourceDTOList = filterChainResourceService
				.findResourceByCategory(AuthzCategory.VIS.getIntValue());
		if (CollectionUtils.isEmpty(resourceDTOList)) {
			LOGGER.error("Should not reach here because there are always filter chain defines required.");
			return null;
		}
		List<BackendFilterChainResourceVO> resourceVOList = new ArrayList<>();
		for (FilterChainResourceDTO resourceDTO : resourceDTOList) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Add filter chain define: {} = {}", resourceDTO.getUrl(), resourceDTO.getPermission());
			}
			resourceVOList.add(new BackendFilterChainResourceVO(resourceDTO));
		}
		return resourceVOList;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.BackendAuthcFacade#checkDataAuthority(java.lang.String,
	 *      long, java.lang.String)
	 */
	@Override
	public Map<String, Object> checkDataAuthority(String userName, long timestamp, String key) {
		Map<String, Object> result = new HashMap<>();
		String keyDecrypted = DigestUtils.decryptDES(DigestUtils.hex2Byte(key),
				new String("showpin netease inc").getBytes());
		if (StringUtils.isNotBlank(keyDecrypted)) {
			String[] keyParams = StringUtils.split(keyDecrypted, SEP);
			if (keyParams.length != 2) {
				return failedDataAuthority("无效的key。");
			}
			String keyUserId = keyParams[0];
			String keyTimestamp = keyParams[1];
			if (!userName.equalsIgnoreCase(keyUserId)) {
				return failedDataAuthority("用户名不一致。");
			}
			if (!String.valueOf(timestamp).equalsIgnoreCase(keyTimestamp)) {
				return failedDataAuthority("时间戳不一致。");
			}
			long currentTime = System.currentTimeMillis();
			if (currentTime - timestamp > 5 * 60 * 1000) {
				return failedDataAuthority("请求已超时。");
			}
		} else {
			return failedDataAuthority("无效的key。");
		}
		DealerDTO dealer = dealerService.findDealerByName(userName);
		// 检查后台用户是否存在并且状态正常
		if (dealer == null || AccountStatus.NORMAL != dealer.getAccountStatus()) {
			return failedDataAuthority("该商家账号不存在或已冻结。");
		}
		// 检查后台用户是否有数据分析功能的权限
		List<PermissionDTO> permissionList = permissionService.findDealerPermissionsByDealerId(dealer.getId());
		if (CollectionUtils.isEmpty(permissionList)) {
			return failedDataAuthority("该商家账号无访问数据分析功能的权限。");
		}
		for (PermissionDTO permission : permissionList) {
			if (permission.getPermission().equalsIgnoreCase("data:compass")) {
				BusinessDTO business = businessService.getBusinessById(dealer.getSupplierId(), 0);
				Map<String, Object> relatedObject = new HashMap<>();
				relatedObject.put("supplierId", business.getId());
				relatedObject.put("supplierName", business.getCompanyName());
				List<AreaDTO> areaList = businessService.getAreadByIdList(business.getAreaIds());
				StringBuilder allArea = new StringBuilder();
				if (CollectionUtils.isEmpty(areaList)) {
					for (AreaDTO areaDTO : areaList) {
						allArea.append(areaDTO.getAreaName());
						allArea.append(",");
					}
				}
				relatedObject.put("area", allArea.toString());
				relatedObject.put("actingBrandId", business.getActingBrandId());
				result.put("success", true);
				result.put("relatedObject", relatedObject);
				return result;
			}
		}
		return failedDataAuthority("该商家账号无访问数据分析功能的权限。");
	}

	/**
	 * @param string
	 * @return
	 */
	private Map<String, Object> failedDataAuthority(String message) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("message", message);
		return result;
	}

}
