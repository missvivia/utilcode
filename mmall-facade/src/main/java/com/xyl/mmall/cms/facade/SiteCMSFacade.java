/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.vo.SiteAreaVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * SiteCMSFacade.java created by yydx811 at 2015年7月16日 下午5:06:53
 * 站点facade
 *
 * @author yydx811
 */
public interface SiteCMSFacade {

	/**
	 * 获取站点列表
	 * @param searchValue 搜索条件
	 * @return List<SiteCMSVO>
	 */
	public List<SiteCMSVO> getSiteCMSList(String searchValue, BasePageParamVO<SiteCMSVO> basePageParamVO);
	
	/**
	 * 获取区域列表
	 * @param parentId 
	 * @param siteId
	 * @return List<SiteAreaVO>
	 */
	public List<SiteAreaVO> getSiteAreaList(long parentId, long siteId);
	
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
	 * @return long
	 */
	public long addSiteCMS(SiteCMSDTO siteCMSDTO);
	
	/**
	 * 按id获取站点
	 * @param siteId
	 * @param isContainArea
	 * @return SiteCMSVO
	 */
	public SiteCMSVO getSiteCMS(long siteId, boolean isContainArea);
	
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
	 * 根据用户Id获取站点区域列表
	 * @param userId
	 * @return
	 */
	public RetArg getAgentAreaInfoByUserId(long userId) ;
	
	/**
	 * 根据用户Id获取站点（或包含区域）列表
	 * @param userId
	 * @param isContainArea 是否包含区域
	 * @return
	 */
	List<SiteCMSVO> getAgentSiteOf(long userId, boolean isContainArea);
}
