/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.service;

import java.util.List;

import com.xyl.mmall.cms.dto.SiteAreaDTO;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * SiteCMSService.java created by yydx811 at 2015年7月16日 下午5:10:04
 * 站点service
 *
 * @author yydx811
 */
public interface SiteCMSService {
	
	/**
	 * 获取站点列表
	 * @param searchValue 搜索条件
	 * @return BasePageParamVO<SiteCMSDTO>
	 */
	public BasePageParamVO<SiteCMSDTO> getSiteCMSList(String searchValue, BasePageParamVO<SiteCMSDTO> pageParamVO);

	/**
	 * 获取站点区域列表
	 * @param siteId 站点id
	 * @return List<SiteAreaDTO>
	 */
	public List<SiteAreaDTO> getSiteAreaList(long siteId);
	
	/**
	 * 根据站点Ids获取站点区域列表
	 * @param siteIds 站点ids
	 * @return List<SiteAreaDTO>
	 */
	public List<SiteAreaDTO> getSiteAreasList(List<Long> siteIds);
	
	/**
	 * 按区域id获取数量
	 * @param areaId
	 * @param siteId
	 * @return int
	 */
	public int getSiteAreaCount(long areaId, long siteId);

	/**
	 * 添加站点
	 * @param siteCMSDTO
	 * @return long id
	 */
	public long addSiteCMS(SiteCMSDTO siteCMSDTO);
	
	/**
	 * 按id获取站点
	 * @param siteId
	 * @param isContainArea
	 * @return SiteCMSDTO
	 */
	public SiteCMSDTO getSiteCMS(long siteId, boolean isContainArea);

	/**
	 * 更新站点
	 * @param siteCMSDTO
	 * @param delList
	 * @return
	 */
	public int updateSiteCMS(SiteCMSDTO siteCMSDTO, List<Long> delList);

	/**
	 * 批量删除站点
	 * @param siteIds
	 * @return int
	 */
	public int deleteBulkSiteCMS(List<Long> siteIds);
	
	/**
	 * 根据id列表获取站点信息
	 * @param siteIds
	 * @return
	 */
	public List<SiteCMSDTO> getSiteCMSList(List<Long> siteIds);
	
	/**
	 * 获取所有站点
	 * @return
	 */
	public List<SiteCMSDTO> getAllSiteCMSList();
}
