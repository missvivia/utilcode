/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.facade.AuthorityFacade;
import com.xyl.mmall.cms.facade.SiteCMSFacade;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.member.dto.AgentAreaDTO;
import com.xyl.mmall.member.dto.AgentDTO;
import com.xyl.mmall.member.enums.AccountStatus;
import com.xyl.mmall.member.enums.AgentType;
import com.xyl.mmall.member.service.AgentService;

/**
 * SiteCMSFacadeImpl.java created by yydx811 at 2015年7月16日 下午5:07:37
 * 站点facade接口实现
 *
 * @author yydx811
 */
@Facade
public class SiteCMSFacadeImpl implements SiteCMSFacade {

	private static Logger logger = LoggerFactory.getLogger(SiteCMSFacadeImpl.class);
	
	@Resource
	private SiteCMSService siteCMSService;
	
	@Resource
	private LocationService locationService;
	
	@Resource
	private AgentService agentService;
	
	@Autowired
	private AuthorityFacade authorityFacade;

	@Override
	public List<SiteCMSVO> getSiteCMSList(String searchValue, BasePageParamVO<SiteCMSVO> basePageParamVO) {
		BasePageParamVO<SiteCMSDTO> pageParamVO = new BasePageParamVO<SiteCMSDTO>();
		if (basePageParamVO != null) {
			basePageParamVO.copy(pageParamVO);
		}
		pageParamVO = siteCMSService.getSiteCMSList(searchValue, pageParamVO);
		if (CollectionUtils.isEmpty(pageParamVO.getList())) {
			return null;
		}
		if (basePageParamVO != null) {
			pageParamVO.copy(basePageParamVO);
		}
		return convertToVO(pageParamVO.getList());
	}
	
	private List<SiteCMSVO> convertToVO(List<SiteCMSDTO> siteCMSDTOList) {
		if (CollectionUtils.isEmpty(siteCMSDTOList)) {
			return null;
		}
		List<SiteCMSVO> retList = new ArrayList<SiteCMSVO>(siteCMSDTOList.size());
		for (SiteCMSDTO siteCMSDTO : siteCMSDTOList) {
			SiteCMSVO siteCMSVO = new SiteCMSVO(siteCMSDTO);
			List<SiteAreaDTO> siteAreaList = siteCMSService.getSiteAreaList(siteCMSVO.getSiteId());
			if (CollectionUtils.isNotEmpty(siteAreaList)) {
				List<SiteAreaVO> areaList = new ArrayList<SiteAreaVO>(siteAreaList.size());
				for (SiteAreaDTO siteAreaDTO : siteAreaList) {
					long areaId = siteAreaDTO.getAreaId();
					String areaName = locationService.getLocationNameByCode(areaId, false);
					SiteAreaVO siteAreaVO = new SiteAreaVO();
					siteAreaVO.setAreaId(areaId);
					siteAreaVO.setAreaName(areaName);
					siteAreaVO.setIsChecked(1);
					areaList.add(siteAreaVO);
				}
				siteCMSVO.setAreaList(areaList);
			}
			retList.add(siteCMSVO);
		}
		return retList;
	}

	@Override
	public List<SiteAreaVO> getSiteAreaList(long parentId, long siteId) {
		List<LocationCode> locationCodeList = null;
		if (parentId == 0) {
			locationCodeList = locationService.getAllProvince();
		} else {
			locationCodeList = locationService.getCityListByProvinceCode(parentId);
		}
		if (CollectionUtils.isEmpty(locationCodeList)) {
			return null;
		}
		List<SiteAreaVO> siteAreaList = new ArrayList<SiteAreaVO>(locationCodeList.size());
		if (siteId == 0) {
			for (LocationCode locationCode : locationCodeList) {
				siteAreaList.add(new SiteAreaVO(locationCode));
			}
		} else {
			// 如果是省
			if (parentId == 0) {
				for (LocationCode locationCode : locationCodeList) {
					SiteAreaVO siteAreaVO = new SiteAreaVO(locationCode);
					List<LocationCode> cityList = locationService.getCityListByProvinceCode(siteAreaVO.getAreaId());
					// 城市列表不为空
					if (CollectionUtils.isNotEmpty(cityList)) {
						int i = 0;
						for (LocationCode city : cityList) {
							// 站点包括该城市计数器+1
							if (siteCMSService.getSiteAreaCount(city.getCode(), siteId) > 0) {
								++i;
								continue;
							} else {
								// 如果计数器已经有计数，但查询到站点所不包含的城市，那么表示省里不全包含ischecked为2，跳出
								if (i > 0) {
									siteAreaVO.setIsChecked(2);
									break;
								}
							}
						}
						// 如果计数器与列表size相等表示全包含
						if (i == cityList.size()) {
							siteAreaVO.setIsChecked(1);
						}
					}
					siteAreaList.add(siteAreaVO);
				}
			} else {
				// 目前最小单位为市，如果站点包含ischecked都为1
				for (LocationCode locationCode : locationCodeList) {
					SiteAreaVO siteAreaVO = new SiteAreaVO(locationCode);
					if (siteCMSService.getSiteAreaCount(siteAreaVO.getAreaId(), siteId) > 0) {
						siteAreaVO.setIsChecked(1);
					}
					siteAreaList.add(siteAreaVO);
				}
			}
		}
		return siteAreaList;
	}

	@Override
	public int getSiteAreaCount(long areaId, long siteId) {
		return siteCMSService.getSiteAreaCount(areaId, siteId);
	}

	@Override
	public long addSiteCMS(SiteCMSDTO siteCMSDTO) {
		return siteCMSService.addSiteCMS(siteCMSDTO);
	}

	@Override
	public SiteCMSVO getSiteCMS(long siteId, boolean isContainArea) {
		SiteCMSDTO siteCMSDTO = siteCMSService.getSiteCMS(siteId, isContainArea);
		if (siteCMSDTO == null) {
			return null;
		}
		SiteCMSVO siteCMSVO = new SiteCMSVO(siteCMSDTO);
		if (isContainArea) {
			List<SiteAreaDTO> siteAreaDTOList = siteCMSDTO.getAreaList();
			if (CollectionUtils.isNotEmpty(siteAreaDTOList)) {
				List<SiteAreaVO> areaList = new ArrayList<SiteAreaVO>(siteAreaDTOList.size());
				for (SiteAreaDTO siteAreaDTO : siteAreaDTOList) {
					LocationCode locationCode = locationService.getLocationCode(siteAreaDTO.getAreaId());
					areaList.add(new SiteAreaVO(locationCode));
				}
				siteCMSVO.setAreaList(areaList);
			}
		}
		return siteCMSVO;
	}

	@Override
	public int updateSiteCMS(SiteCMSDTO siteCMSDTO, List<Long> delList) {
		return siteCMSService.updateSiteCMS(siteCMSDTO, delList);
	}

	@Override
	public int deleteBulkSiteCMS(List<Long> siteIds) {
		return siteCMSService.deleteBulkSiteCMS(siteIds);
	}

	@Cacheable(value = "siteAreaCache", key = "#userId")
	public RetArg getAgentAreaInfoByUserId(long userId) {
		RetArg retArg = new RetArg();
		AgentDTO curAgent = agentService.findAgentById(userId);
		if (curAgent == null || curAgent.getAccountStatus() != AccountStatus.NORMAL) {
			logger.error("Agent is unavailable! UserId {}.", userId);
			return null;
		}
		// 超级管理员可以获取站点下全部区域
		if (curAgent.getAgentType() == AgentType.ROOT) {
			return RetArgUtil.put(retArg, true);
		} else {
			List<Long> siteidList = agentService.findAgentSiteIdsByPermission(userId, "location:site");
			if(CollectionUtil.isEmptyOfList(siteidList)){
				return null;
			}
			List<AgentAreaDTO> agentAreaList = agentService.getAgentAreaList(userId, siteidList.get(0));
			if (CollectionUtils.isEmpty(agentAreaList)) {
				return null;
			}
			List<Long> retList = new ArrayList<Long>(agentAreaList.size());
			for (AgentAreaDTO agentArea : agentAreaList) {
				retList.add(agentArea.getAreaId());
			}
			//用户的站点区域Id
			RetArgUtil.put(retArg, new HashSet<Long>(retList));
			List<Long> agentIdList = agentService.getAgentIdListByAreaIds(retList);
			//同一站点下和userId权限有交集的用户
			RetArgUtil.put(retArg,agentIdList);
			//非root用户
			RetArgUtil.put(retArg, false);
			//站点Id
			RetArgUtil.put(retArg, siteidList.get(0));
			return retArg;
		}
	}
	
	@Override
	public List<SiteCMSVO> getAgentSiteOf(long userId, boolean isContainArea) {
		List<Long> siteidList = agentService.findAgentSiteIdsByPermission(userId, "location:site");
		if (siteidList == null) return null;
		List<SiteCMSVO> result = new ArrayList<>(siteidList.size());
		for (Long siteId : siteidList) {
			result.add(this.getSiteCMS(siteId, isContainArea));
		}
		return result;
	}
}
