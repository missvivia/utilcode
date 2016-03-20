package com.xyl.mmall.saleschedule.dao;

import java.util.List;
import java.util.Map;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * Schedule操作DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleDao extends AbstractDao<Schedule> {

	/**
	 * 新增Schedule
	 * 
	 * @param schedule
	 *            待新增的schedule
	 * @return 新增成功返回true。否则返回false。新增成功后入参schedule带有id。
	 */
	boolean saveSchedule(Schedule schedule);

	/**
	 * 更新Schedule
	 * 
	 * @param schedule
	 *            带有id的待更新schedule
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateSchedule(Schedule schedule);

	/**
	 * 更新Schedule的pageId和bannerId
	 * 
	 * @param schedule
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateSchedulePageIdAndBannerId(Schedule schedule);
	
	/**
	 * Get PO list within same day compare with givin PO
	 * @param comparePO
	 * @return
	 */
	List<Schedule> getSameDayPOList(Schedule comparePO);

	/**
	 * 查询品牌当天以及即将上线的所有PO列表
	 * 
	 * @param brandId
	 * @param curSupplierAreaId
	 * @return
	 */
	POListDTO getScheduleList(long brandId);
	/**
	 * 查询即将上线的PO列表
	 * @param brandId
	 * @param dayAfter
	 * @return
	 */
	POListDTO getScheduleListFuture(long brandId, int dayAfter);

	/**
	 * 根据schedule id删除一个schedule
	 * 
	 * @param id
	 *            schedule标识
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteScheduleById(long id);

	/**
	 * 调整档期日期
	 * 
	 * @param id
	 *            档期id
	 * @param newStartTime
	 *            新的档期开始日期
	 * @param newEndTime
	 *            新的档期结束日期
	 * @param desc
	 *            调整原因
	 * @return 更新成功返回true，否则返回false
	 */
	boolean adjustScheduleDate(long id, long newStartTime, long newEndTime, String desc);

	/**
	 * 根据schedule id查询schedule信息
	 * 
	 * @param id
	 *            schedule标识
	 * @return 如果数据库有数据返回schedule对象，否则返回null
	 */
	Schedule getScheduleById(long id);
		
	/**
	 * 
	 * @param scheduleId
	 * @return
	 */
	ScheduleDTO getScheduleByScheduleId(long scheduleId);

	/**
	 * 
	 * @param paramDTO
	 * @param statusList
	 * @param isCheck
	 * @return
	 */
	POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO, List<ScheduleState> statusList, boolean isCheck);

	POListDTO getScheduleListByBrandNameOrSupplierAcct(ScheduleCommonParamDTO paramDTO, String supplierAcct,
			String brandName);
	/**
	 * 查询PO及其banner列表
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO);

	/**
	 * 根据站点id和时间段查询有效的PO列表.
	 * 
	 * @param curSupplierAreaId
	 * @param supplierId
	 * @param statusList
	 * @param startDate
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * Common query. 
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleListCommon(ScheduleCommonParamDTO paramDTO);

	POListDTO getScheduleListByPOIdList(List<Long> poIdList);
	
	/**
	 * 批量查询PO列表
	 */
	POListDTO batchGetScheduleListByIdList(List<Long> poIdList);

	POListDTO getScheduleList(long siteFlag, long supplierId, String brandName, long startDate, long endDate);


	POListDTO getScheduleListForOMS(ScheduleCommonParamDTO paramDTO,
			long startTimeBegin, long startTimeEnd, long endTimeBegin, long endTimeEnd, long createTimeBegin,
			long createTimeEnd);
	
	POListDTO getScheduleListForCMS(ScheduleCommonParamDTO paramDTO, int type, Object val);

	POListDTO getScheduleListForPOPages(ScheduleCommonParamDTO paramDTO, int type, Object key);

	/**
	 * 更新PO状态
	 * 
	 * @param id
	 *            PO id
	 * @param status
	 *            PO状态
	 * @param desc
	 *            如果拒绝的话需要填写拒绝理由
	 * @return
	 */
	boolean updateStatus(long id, ScheduleState status, String desc, Long userId);

	/**
	 * 更新档期状态--添加商品子状态
	 * 
	 * @param id
	 *            PO id
	 * @param status
	 *            PO状态
	 * @param desc
	 *            如果拒绝的话需要填写拒绝理由
	 * @return
	 */
	boolean updatePrdListAuditStatus(long id, int status);

	/**
	 * 更新档期状态--banner子状态
	 * 
	 * @param id
	 *            PO id
	 * @param status
	 *            PO状态
	 * @param desc
	 *            如果拒绝的话需要填写拒绝理由
	 * @return
	 */
	boolean updateBannerAuditStatus(long poId, long bannerId, int status);

	/**
	 * 更新档期状态--品购页子状态
	 * 
	 * @param id
	 *            PO id
	 * @param status
	 *            PO状态
	 * @param desc
	 *            如果拒绝的话需要填写拒绝理由
	 * @return
	 */
	boolean updatePageAuditStatus(long poId, long pageId, int status);

	/**
	 * 查询某个频道在线的所有PO列表
	 * 
	 * @param sqlPart
	 *            和channel表关联查询时的部分sql
	 * @param saleSiteCode
	 *            销售站点code
	 * @param startId
	 *            查询开始点
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	POListDTO getScheduleListForChl(String sqlPart, long saleSiteCode, long startId, int curPage, int pageSize);

	/**
	 * 查询未来将上线的PO列表
	 * 
	 * @param sqlPart
	 *            和channel表关联查询时的部分sql
	 * @param saleSiteCode
	 *            销售站点code
	 * @param daysAfter
	 *            1-明天 2-后天 3-大后天。。。
	 * @param retSize
	 *            要返回的size
	 * @return
	 */
	POListDTO getScheduleListForFuture(String sqlPart, long saleSiteCode, int daysAfter, int retSize);

//	/**
//	 * 查询PO列表。 for Mobile side
//	 * 
//	 * @param userId
//	 * @param areaId
//	 *            站点id
//	 * @param sqlPart
//	 *            频道相关sql
//	 * @param branndId
//	 *            品牌id
//	 * @param timestamp
//	 *            PO开始时间
//	 * @param limit
//	 *            返回PO数量
//	 * @return
//	 */
//	POListDTO getScheduleList(long areaId, String sqlPart, long brandId, long timestamp, int limit);

//	/**
//	 * 根据时间查询开始上线时间为某一时间段的档期记录
//	 * 
//	 * @param startTime
//	 * @param endTime
//	 * @param sqlPart
//	 * @param curSupplierAreaId
//	 * @param retSize
//	 * @return
//	 */
//	public POListDTO getScheduleListByTime(long startTime, long endTime, String sqlPart, long curSupplierAreaId,
//			int retSize);

	/**
	 * Get PO list for each brand
	 * 
	 * @param brandIdList
	 * @param areaId
	 * @return PO list for each brand
	 */
	POListDTO getScheduleListByBrandIdList(ScheduleCommonParamDTO paramDTO);

//	/**
//	 * Batch get PO status when adding products
//	 * 
//	 * @return
//	 */
//	Map<Long, Integer> batchGetPOAddPrdListStatus(ScheduleCommonParamDTO paramDTO);
	
//	/**
//	 * Get User liked PO list
//	 * 
//	 * @param paramDTO.poIdList  
//	 * @param paramDTO.curPage  The same as offset
//	 * @param paramDTO.pageSize 
//	 * @return
//	 */
//	POListDTO getScheduleListForUserLiked(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * Get PO list 
	 * 
	 * @param paramDTO.curSupplierAreaId Province ID
	 * @param paramDTO.startDate  Where po.startTime>=?
	 * @param paramDTO.endDate    Where po.startTime<=?
	 * @return
	 */
	POListDTO getScheduleListByStartEndTime(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleListByStartEndTimeWithType(ScheduleCommonParamDTO paramDTO, int type);
	
	/**
	 * 获取指定时间的档期
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Schedule> getScheduleListByTime(long areaid,long startTime, long endTime);
	
	/**
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleListForPrdOrListAudit(ScheduleCommonParamDTO paramDTO);
	
	Map<String, Object> getProductById(long productId) ;
	
	boolean updatePOSaleSiteFlag(long poId, long saleSiteFlag);
}
