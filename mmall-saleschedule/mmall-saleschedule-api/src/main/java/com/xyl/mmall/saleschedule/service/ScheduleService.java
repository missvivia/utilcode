package com.xyl.mmall.saleschedule.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * 档期服务
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleService {
	
	/**
	 * 新增PO
	 * 
	 * @param poDto
	 * @return
	 */
	PODTO saveSchedule(PODTO poDto);

	/**
	 * 更新PO。PO里面要有id
	 * 
	 * @param poDto
	 * @return
	 */
	boolean updateSchedule(PODTO poDto);

	/**
	 * 创建PO审核不通过
	 * 
	 * @param id
	 *            档期id
	 * @param desc
	 *            审核拒绝理由
	 * @return
	 */
	boolean auditScheduleReject(long id, String desc);

	/**
	 * 创建PO审核通过
	 * 
	 * @param id
	 *            档期id
	 * @return
	 */
	boolean auditSchedulePass(long id);

	/**
	 * PO submit
	 * 
	 * @param id
	 * @return
	 */
	boolean auditScheduleSubmit(long id);

	/**
	 * PO offline
	 * 
	 * @param id
	 *            档期id
	 * @return
	 */
	boolean makeScheduleOffline(long id);

	/**
	 * Update status for auditing PO product
	 * 
	 * @param poId
	 * @param status
	 *            1-new 2-checking 3-passed 4-rejected
	 * @return
	 */
	boolean updatePOPrdStatus(long poId, int status);

	/**
	 * Batch version of method 'boolean updatePOPrdStatus(long poId, int
	 * status)'
	 * 
	 * @param poIdList
	 * @param status
	 * @return
	 */
	boolean batchUpdatePOPrdStatus(List<Long> poIdList, int status);

	/**
	 * PO添加商品列表审核通过
	 * 
	 * @param poId
	 * @return
	 */
	boolean auditPOProductListPass(long poId);

	/**
	 * Batch update show orders of PO list
	 * 
	 * @param poList
	 * @return
	 */
	boolean batchUpdatePOOrder(List<Schedule> poList);

	/**
	 * 根据po id删除一个PO
	 * 
	 * @param id
	 *            档期id
	 * @return
	 */
	boolean deleteScheduleById(long id);

	/**
	 * 调整PO的日期
	 * 
	 * @param id
	 *            档期id
	 * @param newStartTime
	 * @param newEndTime
	 * @param desc
	 * @return
	 */
	boolean adjustScheduleDate(long id, long newStartTime, long newEndTime, String desc, String poFollowerName, long  poFollowerId);

	/**
	 * 根据po id查询PO信息
	 * 
	 * @param id
	 *            档期id
	 * @return
	 */
	PODTO getScheduleById(long id);
	
	/**
	 * Used for CMS. with no cache.
	 * @param id
	 * @return
	 */
	PODTO getScheduleByIdForCMSWithNoCache(long id) ;
	
	/**
	 * Get po list.
	 * Note: plz check the SQL before use this method. Anything wrong plz PaoPao me.
	 * 
	 * @param poIdList
	 * @return
	 */
	POListDTO batchGetScheduleListByIdList(List<Long> poIdList);

	/**
	 * 根据站点id和时间段查询有效的PO列表.
	 * 
	 * @param curSupplierAreaId
	 *            province id
	 * @param startDate
	 * @param endDate
	 * @param curPage
	 *            same as 'offset'
	 * @param pageSize
	 * @return
	 */
	POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO);

	/**
	 * Common query
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleListCommon(ScheduleCommonParamDTO paramDTO);

	/**
	 * 查询品牌当天以及即将上线的所有PO列表
	 * 
	 * @param brandId
	 * @param saleSiteFlag
	 *            销售站点异或标记值
	 * @return
	 */
	POListDTO getScheduleList(long brandId, long saleSiteFlag);

	/**
	 * 返回未来时间的所有PO列表
	 * 
	 * @param brandId
	 * @param saleSiteFlag
	 *            销售站点异或标记值
	 * @param dayAfter
	 * @return
	 */
	POListDTO getScheduleListFuture(long brandId, long saleSiteFlag, int dayAfter);

	/**
	 * 
	 * 根据 站点id、供应商id和品牌名称查询PO列表
	 * 
	 * @param saleAreaId
	 * @param supplierId
	 * @param statusList
	 * @param startDate
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	POListDTO getScheduleList(long saleAreaId, long supplierId, String brandName, long startDate, long endDate,
			int curPage, int pageSize);

	/**
	 * 综合查询
	 * 
	 * @param curSupplierAreaId
	 * @param supplierId
	 * @param statusList
	 * @param startDate
	 * @param endDate
	 * @param curPage
	 * @param pageSize
	 * @param isCheck
	 * @return
	 */
	POListDTO getScheduleList(ScheduleCommonParamDTO paramDTO, List<ScheduleState> statusList, boolean isCheck);

	POListDTO getScheduleListByBrandNameOrSupplierAcct(ScheduleCommonParamDTO paramDTO, String supplierAcct,
			String brandName);

	/**
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleListByBrandIdList(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * 查詢PO及其banner列表
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO);

	/**
	 * 档期列表查询。 所有条件之间是AND关系。 poId是模糊查询
	 * 
	 * @param poId
	 *            档期id
	 * @param statusList
	 *            状态列表
	 * @param startTimeBegin
	 *            档期开始时间的上限
	 * @param startTimeEnd
	 *            档期开始时间的下限
	 * @param endTimeBegin
	 *            档期结束时间的上限
	 * @param endTimeEnd
	 *            档期结束时间的下限
	 * @param createTimeBegin
	 *            档期创建时间的上限
	 * @param createTimeEnd
	 *            档期创建时间的下限
	 * @param curPage
	 *            分页。档期页数。第一页是0，第二页是1,以此类推。
	 * @param pageSize
	 *            分页。每页需要显示的条数。比如每页显示10条
	 * @return 返回符合条件的档期列表
	 */
	POListDTO getScheduleListForOMS(ScheduleCommonParamDTO paramDTO, long startTimeBegin, long startTimeEnd,
			long endTimeBegin, long endTimeEnd, long createTimeBegin, long createTimeEnd);

	/**
	 * 对应CMS页面路径： schedule/list
	 * 
	 * @param paramDTO
	 * @param type
	 *            1-po编号 2-品牌名称 3-商家账号
	 * @param val
	 *            对应type的值
	 * @return
	 */
	POListDTO getScheduleListForCMS(ScheduleCommonParamDTO paramDTO, int type, Object val);

	/**
	 * 品购页列表
	 * 
	 * @param type
	 *            1-根据名称title查询 2-根据PO id查询
	 * @param key
	 *            type为1是表示title，为2时表示PO id
	 * @return
	 */
	POListDTO getScheduleListForPOPages(ScheduleCommonParamDTO paramDTO, int type, Object key);

	/**
	 * 获取频道列表
	 * 
	 * @return
	 */
	List<ScheduleChannelDTO> getScheduleChannelList();

	/**
	 * 获取PO状态列表
	 * 
	 * @return
	 */
	List<ScheduleStatusDTO> getScheduleStateList();

	/**
	 * 获取PO状态列表
	 * 
	 * @return
	 */
	List<ScheduleStatusDTO> getScheduleStateListForBackend();

	/**
	 * PO审核
	 * 
	 * @param id
	 * @param status
	 * @param desc
	 * @return
	 */
	boolean auditScheduleForBackend(long id, ScheduleState status, String desc);

	/**
	 * 查询某个频道在线的所有PO列表
	 * 
	 * @param chlId
	 *            0-所有 1-首页 2-女装。。。
	 * @param saleSiteFlag
	 *            销售站点异或标记值
	 * @param startId
	 *            查询开始点
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	POListDTO getScheduleListForChl(long chlId, long saleSiteFlag, long startId, int curPage, int pageSize);

	/**
	 * 查询未来将上线的PO列表
	 * 
	 * @param chlId
	 *            0-所有，1-首页 2-女装
	 * @param saleSiteFlag
	 *            销售站点异或标记值
	 * @param daysAfter
	 *            1-明天 2-后天 3-大后天。。。
	 * @param retSize
	 *            要返回的size
	 * @return
	 */
	POListDTO getScheduleListForFuture(long chlId, long saleSiteFlag, int daysAfter, int retSize);

	/**
	 * Get PO list
	 * 
	 * @param paramDTO
	 *            .curSupplierAreaId Province ID
	 * @param paramDTO
	 *            .startDate Where po.startTime>=?
	 * @param paramDTO
	 *            .endDate Where po.startTime<=?
	 * @return
	 */
	POListDTO getScheduleListByStartEndTime(ScheduleCommonParamDTO paramDTO);

	/**
	 * 
	 * @param paramDTO
	 * @param type
	 * @return
	 */
	POListDTO getScheduleListByStartEndTimeWithType(ScheduleCommonParamDTO paramDTO, int type);

	/**
	 * @param saleSiteFlag
	 *            销售站点标记值
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Schedule> getScheduleListByTime(long saleSiteFlag, long startTime, long endTime);

	/**
	 * Get online in sale PO list
	 * 
	 * @param paramDTO
	 * @return
	 */
	List<PODTO> getOnlinePOList(long areaId, long supplierId);

	/**
	 * 根据po id集合查询po列表
	 * 
	 * @param idList
	 * @return
	 */
	List<Schedule> getScheduleByIdList(List<Long> idList);
	
	/**
	 * 
	 * @param poIdList
	 * @return
	 */
	Map<Long, List<Long>> getWarehouseListByPOIdList(List<Long> poIdList);
	
	/**
	 * 最新特卖，外部传入时间参数
	 * @param chlId
	 * @param saleSiteFlag
	 * @param curDate
	 * @return
	 */
	POListDTO getScheduleListForChl(long chlId, long saleSiteFlag, long curDate);

	/**
	 * 特卖预告，外部提供时间参数
	 * @param chlId
	 * @param saleSiteFlag
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	POListDTO getScheduleListForFuture(long chlId, long saleSiteFlag, long startTime, long endTime);

	/**
	 * Get PO list within same day compare with givin PO
	 * @param comparePO
	 * @return
	 */
	List<Schedule> getSameDayPOList(Schedule comparePO);
	
	/**
	 * 更新档期表中表达“档期商品资料审核的状态”.
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	boolean updatePrdzlStatus(long id, int status);
	
	/**
	 * 更新档期表中表达“档期商品清单审核的状态”.
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	boolean updatePrdqdStatus(long id, int status);
	
	/**
	 * 
	 * @param paramDTO
	 * @return
	 */
	POListDTO getScheduleListForPrdOrListAudit(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * This method is used to clear all PO cache.
	 * Called by job project
	 */
	void clearCache();
	
	/////////////////////////////////
	// For UT
	////////////////////////////////
	/**
	 * For test.
	 * 
	 * @param poList
	 * @return
	 */
	boolean test();
	
	Map<String, Object> getProductById(long productId);
	
	boolean updatePOSaleSite(long poId, long saleSiteFlag, List<Long> saleSiteList);

	Schedule getScheduleByScheduleId(long id);
}
