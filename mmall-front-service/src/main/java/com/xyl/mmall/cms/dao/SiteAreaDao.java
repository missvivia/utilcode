/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.cms.meta.SiteArea;

/**
 * SiteAreaDao.java created by yydx811 at 2015年7月16日 下午5:17:38
 * 站点区域dao
 *
 * @author yydx811
 */
public interface SiteAreaDao extends AbstractDao<SiteArea> {

	/**
	 * 获取站点区域列表
	 * @param siteId
	 * @return List<SiteArea>
	 */
	public List<SiteArea> getSiteAreaList(long siteId);
	
	/**
	 * 按区域id获取数量
	 * @param areaId
	 * @param siteId
	 * @return int
	 */
	public int getSiteAreaCount(long areaId, long siteId);
	
	/**
	 * 批量删除区域
	 * @param siteId
	 * @param areaIds
	 * @return
	 */
	public int deleteBulkSiteArea(long siteId, List<Long> areaIds);
	
	/**
	 * 批量删除区域
	 * @param siteIds
	 * @return int
	 */
	public int deleteBulkSiteArea(List<Long> siteIds);
	
	/**
	 * 根据站点Ids获取站点区域列表
	 * @param siteIds 站点ids
	 * @return List<SiteArea>
	 */
	public List<SiteArea> getSiteAreasList(List<Long> siteIds);
}
