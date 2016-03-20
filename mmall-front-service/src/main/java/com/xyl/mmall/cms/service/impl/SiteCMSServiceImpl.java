/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.dbsupport.exception.DBSupportRuntimeException;
import com.xyl.mmall.cms.dao.SiteAreaDao;
import com.xyl.mmall.cms.dao.SiteCMSDao;
import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.meta.SiteArea;
import com.xyl.mmall.cms.meta.SiteCMS;
import com.xyl.mmall.cms.service.SiteCMSService;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * SiteCMSServiceImpl.java created by yydx811 at 2015年7月16日 下午5:11:23
 * 站点service接口实现
 *
 * @author yydx811
 */
@Service
public class SiteCMSServiceImpl implements SiteCMSService {

	@Autowired
	private SiteCMSDao siteCMSDao;
	
	@Autowired
	private SiteAreaDao siteAreaDao;
	
	@Override
	public BasePageParamVO<SiteCMSDTO> getSiteCMSList(String searchValue, BasePageParamVO<SiteCMSDTO> pageParamVO) {
		return siteCMSDao.getSiteCMSList(searchValue, pageParamVO);
	}
	
	@Override
	public List<SiteAreaDTO> getSiteAreaList(long siteId) {
		List<SiteArea> siteAreaList = siteAreaDao.getSiteAreaList(siteId);
		return siteAreaConvertToDTO(siteAreaList);
	}
	
	private List<SiteAreaDTO> siteAreaConvertToDTO(List<SiteArea> siteAreaList) {
		if (CollectionUtils.isEmpty(siteAreaList)) {
			return null;
		}
		List<SiteAreaDTO> retList = new ArrayList<SiteAreaDTO>(siteAreaList.size());
		for (SiteArea siteArea : siteAreaList) {
			retList.add(new SiteAreaDTO(siteArea));
		}
		return retList;
	}

	@Override
	public int getSiteAreaCount(long areaId, long siteId) {
		return siteAreaDao.getSiteAreaCount(areaId, siteId);
	}

	@Override
	@Transaction
	public long addSiteCMS(SiteCMSDTO siteCMSDTO) {
		long siteId = siteCMSDao.addSiteCMS(siteCMSDTO);
		if (siteId == 0l) {
			throw new DBSupportRuntimeException("Add siteCMS failed, return id is 0!");
		}
		List<SiteAreaDTO> addList = siteCMSDTO.getAreaList();
		List<SiteArea> siteAreaList = convertToMeta(addList, siteId);
		if (!siteAreaDao.addObjects(siteAreaList)) {
			throw new DBSupportRuntimeException("Add siteArea failed!");
		}
		return siteId;
	}
	
	private List<SiteArea> convertToMeta(List<SiteAreaDTO> addList, long siteId) {
		List<SiteArea> retList = new ArrayList<SiteArea>(addList.size());
		for (SiteAreaDTO siteAreaDTO : addList) {
			SiteArea siteArea = new SiteArea();
			siteArea.setAreaId(siteAreaDTO.getAreaId());
			siteArea.setSiteId(siteId);
			retList.add(siteArea);
		}
		return retList;
	}

	@Override
	public SiteCMSDTO getSiteCMS(long siteId, boolean isContainArea) {
		SiteCMS siteCMS = siteCMSDao.getObjectById(siteId);
		if (siteCMS == null) {
			return null;
		}
		SiteCMSDTO siteCMSDTO = new SiteCMSDTO(siteCMS);
		if (isContainArea) {
			List<SiteArea> siteAreaList = siteAreaDao.getSiteAreaList(siteId);
			if (CollectionUtils.isNotEmpty(siteAreaList)) {
				List<SiteAreaDTO> areaList = new ArrayList<SiteAreaDTO>(siteAreaList.size());
				for (SiteArea siteArea : siteAreaList) {
					areaList.add(new SiteAreaDTO(siteArea));
				}
				siteCMSDTO.setAreaList(areaList);
			}
		}
		return siteCMSDTO;
	}

	@Override
	@Transaction
	public int updateSiteCMS(SiteCMSDTO siteCMSDTO, List<Long> delList) {
		long siteId = siteCMSDTO.getId();
		// 先删除
		if (CollectionUtils.isNotEmpty(delList)) {
			siteAreaDao.deleteBulkSiteArea(siteId, delList);
		}
		// 再添加
		List<SiteAreaDTO> addList = siteCMSDTO.getAreaList();
		if (CollectionUtils.isNotEmpty(addList)) {
			if (!siteAreaDao.addObjects(addList)) {
				throw new DBSupportRuntimeException("Add siteArea failed! SiteId : " + siteId);
			}
		}
		// 最后更新siteCMS
		int res = siteCMSDao.updateSiteCMS(siteCMSDTO);
		if (res > 0) {
			return res;
		} else {
			throw new DBSupportRuntimeException("Update siteArea failed! SiteId : " + siteId);
		}
	}

	@Override
	@Transaction
	public int deleteBulkSiteCMS(List<Long> siteIds) {
		// 先删除area
		siteAreaDao.deleteBulkSiteArea(siteIds);
		// 再删除site
		return siteCMSDao.deleteBulkSiteCMS(siteIds);
	}

	@Override
	public List<SiteCMSDTO> getSiteCMSList(List<Long> siteIds) {
		List<SiteCMS> siteCMSList = siteCMSDao.getSiteCMSList(siteIds);
		return convertToSiteCMSDTO(siteCMSList);
	}

	@Override
	public List<SiteAreaDTO> getSiteAreasList(List<Long> siteIds) {
		List<SiteArea> siteAreaList = siteAreaDao.getSiteAreasList(siteIds);
		return siteAreaConvertToDTO(siteAreaList);
	}

	@Override
	public List<SiteCMSDTO> getAllSiteCMSList() {
		List<SiteCMS> siteCMSList = siteCMSDao.getAllSiteCMSList();
		return convertToSiteCMSDTO(siteCMSList);
	}
	
	/**
	 * 站点dto转换
	 * @param siteCMSList
	 * @return
	 */
	private List<SiteCMSDTO> convertToSiteCMSDTO(List<SiteCMS> siteCMSList) {
		if (CollectionUtils.isNotEmpty(siteCMSList)) {
			List<SiteCMSDTO> retList = new ArrayList<SiteCMSDTO>(siteCMSList.size());
			for (SiteCMS siteCMS : siteCMSList) {
				retList.add(new SiteCMSDTO(siteCMS));
			}
			return retList;
		}
		return null;
	}
}
