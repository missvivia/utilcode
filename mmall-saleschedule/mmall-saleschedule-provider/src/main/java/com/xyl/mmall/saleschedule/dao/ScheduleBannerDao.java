package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;

/**
 * Schedule操作DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleBannerDao extends AbstractDao<ScheduleBanner> {

	/**
	 * 新增
	 * 
	 * @param banner
	 *            待新增的banner
	 * @return 新增成功返回true。否则返回false。新增成功后入参banner带有id。
	 */
	boolean saveScheduleBanner(ScheduleBanner banner);

	/**
	 * 批量增加banner
	 * 
	 * @param bannerList
	 * @return
	 */
	boolean saveShceduleBannerList(List<ScheduleBanner> bannerList);

	/**
	 * 更新
	 * 
	 * @param banner
	 *            带有id的待更新banner
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateScheduleBanner(ScheduleBanner banner);

	/**
	 * Update PO fields in banner
	 * 
	 * @param banner
	 * @return
	 */
	boolean updateScheduleBannerPOField(ScheduleBanner banner);

	/**
	 * Banner audit
	 * 
	 * @param banner
	 * @return
	 */
	boolean auditScheduleBanner(ScheduleBanner banner);
	
	/**
	 * 根据id删除一个banner
	 * 
	 * @param id
	 *            Banner id标识
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteScheduleBannerById(long id);

	/**
	 * 根据id查询banner信息
	 * 
	 * @param id
	 *            banner标识
	 * @return 如果数据库有数据返回Banner对象，否则返回null
	 */
	ScheduleBanner getScheduleBannerById(long id);
	
	/**
	 * 根据PO id查询banner信息
	 * @param scheduleId PO标识
	 * @return 返回和PO对应的banner信息，如果差不多返回null
	 */
	ScheduleBanner getScheduleBannerByScheduleId(long scheduleId);

	/**
	 * Get banner list
	 * @param poIdList
	 * @return
	 */
	List<ScheduleBanner> getScheduleBannerList(List<Long> poIdList);

	/**
	 * 综合查询
	 * 
	 * @return
	 */
	POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO, String supplierName, String brandName);
	
	/**
	 * @return
	 */
	POListDTO getScheduleBannerListWithSupplierIdList(ScheduleCommonParamDTO paramDTO, String brandName);
}
