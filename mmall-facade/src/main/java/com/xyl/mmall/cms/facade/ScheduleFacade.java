package com.xyl.mmall.cms.facade;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.backend.vo.IdNameBean;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.vo.POSortVO;
import com.xyl.mmall.cms.vo.POStatusGetVO;
import com.xyl.mmall.cms.vo.ScheduleListVO;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleChannelDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleState;
import com.xyl.mmall.saleschedule.meta.Schedule;

public interface ScheduleFacade {
	
	/**
	 * get all store area list
	 * @return
	 */
	List<IdNameBean> getStoreAreaList();
	
	/**
	 * Get all province code and name list
	 * 
	 * @return
	 */
	List<IdNameBean> getAllProvince() ;
	
	/**
	 * Get allowed area list for specific user
	 * 
	 * @param userId
	 * @param permission
	 * @return
	 */
	List<AreaDTO> getAllowedAreaList(String permission);
	
	/**
	 * Check whether current user has permission to access a PO
	 * @param po
	 * @param accessPermission
	 * @return True if current has permission, False otherwise
	 */
	boolean canAccessPO(ScheduleDTO po, String accessPermission);
	
	/**
	 * 新增PO
	 * 
	 * @param poDto
	 * @return
	 */
	ScheduleVO saveSchedule(PODTO poDTO);

	/**
	 * 更新PO
	 * 
	 * @param poDto
	 * @return
	 */
	boolean updateSchedule(PODTO poDTO);

	/**
	 * 新建档期审核拒绝
	 * 
	 * @param id
	 *            档期id
	 * @param desc
	 *            拒绝原因
	 * @return
	 */
	boolean auditScheduleReject(long id, String desc);

	/**
	 * 新建档期审核通过
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
	 * Batch update show orders of PO list
	 * 
	 * @param poList
	 * @return
	 */
	boolean batchUpdatePOOrder(POSortVO vo);
	
	/**
	 * PO添加商品提交审核之前需要，检查PO里面是否添加过商品
	 * 
	 * @param poId
	 * @return
	 */
	boolean isProductInPO(long poId);

	/**
	 * PO添加商品提交审核后，调用商品服务更新状态
	 * 
	 * @param id
	 * @return
	 */
	void updatePrdListSubmitStatus(long id);

	/**
	 * PO添加商品列表后提交审核
	 * 
	 * @param id
	 *            PO id
	 */
//	boolean auditPOProductListSubmit(long poId);

	/**
	 * PO添加商品列表审核通过
	 * 
	 * @param poId
	 * @return
	 */
	boolean auditPOProductListPass(long poId);

	/**
	 * PO添加商品列表审核不通过
	 * 
	 * @param poId
	 * @param reason
	 * @return
	 */
//	boolean auditPOProductListReject(long poId, String reason);

	/**
	 * make PO online
	 * 
	 * @param poId
	 * @return
	 */
	boolean auditPOOnline(long poId);

	/**
	 * make PO offline
	 * 
	 * @param poId
	 * @return
	 */
	boolean auditPOOffline(long poId);

	boolean deleteScheduleById(long id);

	boolean adjustScheduleDate(long id, long newStartTime, long newEndTime, String desc, String poFollowerName,long  poFollowerId);

	ScheduleVO getScheduleById(long id);

	//ScheduleListVO batchGetScheduleListByIdList(List<Long> poIdList, long supplierAreaId, int curPage, int pageSize);

	/**
	 * 根据站点id和时间段查询有效的PO列表.
	 */
	ScheduleListVO getScheduleList(ScheduleCommonParamDTO paramDTO);

	/**
	 * Get PO list by specific parameters.
	 * 
	 * @param paramDTO
	 * @return
	 */
	ScheduleListVO getScheduleListCommon(ScheduleCommonParamDTO paramDTO);
	
//	/**
//	 * 根据 站点id、供应商id和品牌名称查询PO列表. 商品審核查詢時調用
//	 */
//	JSONObject getScheduleList(long curSupplierAreaId, long supplierId, String brandName, long startDate, long endDate,
//			int curPage, int pageSize);

	/**
	 * 综合查询PO列表
	 */
	ScheduleListVO getScheduleList(ScheduleCommonParamDTO paramDTO, List<ScheduleState> statusList, boolean isCheck);

	/**
	 * path: schedule/list
	 * 	
	 * @param paramDTO
	 * @param type 1-编号 2-品牌名称 3-商家账号
	 * @param val
	 * @return
	 */
	ScheduleListVO getScheduleListForCMS(ScheduleCommonParamDTO paramDTO, int type, Object val);
	
	/**
	 * 
	 * @param poList
	 * @return
	 */
	Map<Long, String> getSupplierIdAccountMap(POListDTO poList, int flag);
	
	/**
	 * 查詢PO及其banner列表
	 * 
	 * @param paramDTO
	 * @param statusList
	 * @param isCheck
	 * @return
	 */
	ScheduleListVO getScheduleBannerList(ScheduleCommonParamDTO paramDTO);

	/**
	 * For Backend系统
	 */
	ScheduleListVO getScheduleListForOMS(ScheduleCommonParamDTO paramDTO,
			long startTimeBegin, long startTimeEnd, long endTimeBegin, long endTimeEnd, long createTimeBegin,
			long createTimeEnd);

	// for MainSite and mobile
	//ScheduleListVO getScheduleList(ScheduleCommonParamDTO paramDTO, long chlId, boolean queryOffsetFlag);

	// others
	List<ScheduleStatusDTO> getScheduleStateList();

	List<ScheduleStatusDTO> getScheduleStateForBackend();

	boolean auditScheduleForBackend(long id, ScheduleState status, String desc);

	List<ScheduleChannelDTO> getScheduleChannelList();

	JSONArray getProdCategoryList();

	JSONArray getProdDetailAuditStatusList();

	// //////////////////////
	// SchedulePage
	// //////////////////////
	// Backend query page list
	ScheduleListVO getScheduleListForPOPages(ScheduleCommonParamDTO paramDTO, int type, Object key);

	// Backend | CMS get page status list
	List<ScheduleStatusDTO> getSchedulePageStatusList();
	
	List<IdNameBean> getPOAuditStatusListForApprover();
	List<IdNameBean> getPOAduitStatusListForApplier();
	
	List<IdNameBean> getPOPrdAuditStatusListForApprover();

	// Backend audit page submit
	boolean auditSchedulePageSubmit(long pageId, Long poId);

	// CMS audit page pass
	boolean auditSchedulePagePass(long pageId, long poId);

	// CMS audit page reject
	boolean auditSchedulePageReject(long pageId, long poId, String desc);

	// CMS query page list
	ScheduleListVO getSchedulePageList(ScheduleCommonParamDTO paramDTO, long pageId, String brandName);

	// ///////////////////////////////
	// Mobile
	// ///////////////////////////////
	/**
	 * 查询某个频道在线的所有PO列表
	 * 
	 * @param userId
	 *            每个PO是否有该用户关注
	 * @param chlId
	 *            0-所有 1-首页 2-女装。。。
	 * @param saleSiteCode
	 *            销售站点code
	 * @param startId
	 *            查询开始点
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	ScheduleListVO getScheduleListForChl(UserLoginBean userLoginBean, long chlId, long saleSiteCode, long startId, int curPage,
			int pageSize);

	/**
	 * 查询未来将上线的PO列表
	 * 
	 * @param userId
	 *            每个PO是否有该用户关注
	 * @param chlId
	 *            0-所有，1-首页 2-女装
	 * @param saleSiteCode
	 *            销售站点code
	 * @param daysAfter
	 *            1-明天 2-后天 3-大后天。。。
	 * @param retSize
	 *            要返回的size
	 * @return
	 */
	ScheduleListVO getScheduleListForFuture(long userId, long chlId, long saleSiteCode, int daysAfter, int retSize);

	/**
	 * 根据一组商家id查询对应的品牌列表
	 * 
	 * @param supplierIdList
	 *            商家id列表
	 * @return
	 */
	List<BrandDTO> getBrandListBySupplierIdList(List<Long> supplierIdList);

	/**
	 * 编辑品购页
	 * 
	 * @param poDto
	 * @return
	 */
	boolean updateSchedulePage(PODTO poDTO);

	/**
	 * 根据page id获取page信息
	 * 
	 * @param pageId
	 * @return
	 */
	PODTO getSchedulePageById(long pageId);
	
	/**
	 * 根据档期id获取page信息
	 * 
	 * @param id
	 * @return
	 */
	PODTO getSchedulePageByScheduleId(long poId);

	/**
	 * 根据一组产品id获取对应的产品列表
	 * 
	 * @param prdIdList
	 * @return
	 */
	JSONArray getPrdListByPrdIdList(List<Long> prdIdList, PODTO poDTO);
	
	/**
	 * 根据一组产品id获取对应的产品列表
	 * 
	 * @param prdIdList
	 * @return
	 */
	JSONObject getPrdListByPrdIdListForMainSite(List<Long> prdIdList, PODTO poDTO);
	
//	/**
//	 * 根据起始时间，结束时间查询用户关注的活动中处于这个时间段的所有活动
//	 * 即如果用户关注的活动已过期则不显示
//	 * 注意这里的参数 endTime是开区间
//	 * @param startTime
//	 * @param endTime
//	 * @param userId
//	 * @param chlId
//	 * @param curSupplierAreaId
//	 * @param retSize
//	 * @return
//	 */
//	ScheduleListVO getScheduleListByTime2(long startTime, long endTime,long userId, long chlId, long curSupplierAreaId, int retSize);
	
	/**
	 * Get PO list
	 * 
	 * @param vo
	 *            .curSupplierAreaId Province ID
	 * @param vo
	 *            .startDate Where po.startTime>=?
	 * @param vo
	 *            .endDate Where po.startTime<=?
	 * @return
	 */
	JSONObject getScheduleListByStartEndTime(POStatusGetVO vo);

	/**
	 * 获取 指定区间内即将上线已经通过的档期
	 * 注意：这里要求档期的状态为202
	 * @param startTime
	 * @param endTime 这里的endTime为开区间，即  <endTime
	 * @return
	 */
	public List<Schedule> getScheduleListByTime(long siteId, long startTime, long endTime);
	
	/**
	 * for Backend home page.path: /index
	 * 
	 * @return
	 */
	JSONObject getStatisticData(ScheduleCommonParamDTO paramDTO, String url);
	
	/**
	 * for Backend home page. path: /index
	 * 
	 * @return
	 */
	JSONArray getOnlinePOData(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * 
	 * @param supplierId
	 * @return
	 */
	int getInvoiceInOrdSupplierCountOfInit(long supplierId);
	
	/**
	 * for Backend home page.path: /index
	 * 
	 * @return
	 */
	JSONArray getOfflinePOData(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * For CMS home page. path: /index
	 * @param paramDTO
	 * @return
	 */
	JSONArray getCheckingPOSelfData(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * For CMS home page. path: /index
	 * @param paramDTO
	 * @return
	 */
	JSONArray getCheckingPOOthersData(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * For CMS home page. path: /index
	 * @param paramDTO
	 * @return
	 */
	JSONObject getPOPromotionData(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * For CMS home page. path: /index
	 * @param paramDTO
	 * @return
	 */
	JSONArray getCheckingOrderData(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * Timer - set overdue PO offline
	 */
	void scheduleTimerOffline();
	
	/**
	 * Timer - auto unlike offline PO 
	 * @param paramDTO
	 */
	void scheduelTimerUnlike();
	
	/**
	 * 根据po id集合查询po列表
	 * @param idList
	 * @return
	 */
	List<Schedule> getScheduleByIdList(List<Long> idList);
	
	/**
	 * 
	 * @param poId
	 * @return
	 */
	List<String> getPromotionByPO(long poId);

	/**
	 * 
	 * @param permission
	 *            such as "schedule:return"
	 * @param siteCode
	 *            Id of site
	 * @param supplierAcct
	 *            supplier account, such as 'abc@163.com'
	 * @param brandName
	 * @return
	 */
	POListDTO getScheduleListByBrandNameOrSupplierAcct(String permission, long siteCode, String supplierAcct,
			String brandName);

	POListDTO getScheduleListByBrandIdList(ScheduleCommonParamDTO paramDTO);
	
	/**
	 * CMS 商品资料or商品清单审核，查询档期列表。
	 * 
	 * @param paramDTO
	 * @param status
	 * @param isPrdList True:商品清单；False:商品资料
	 * @return
	 */
	ScheduleListVO getScheduleListForPrdOrListAudit(ScheduleCommonParamDTO paramDTO, Integer status, boolean isPrdList);

	List<PODTO> getOnlineScheduleList();
	
	List<POProductDTO> getPoProductByPo(long poId);
	
	Map<Long, Integer> getInventoryCount(List<Long> skuIds);
	
	/**
	 * 最新特卖，外部传入时间参数
	 * @param chlId
	 * @param saleSiteFlag
	 * @param curDate
	 * @return
	 */
	ScheduleListVO getScheduleListForChl(long chlId, long saleSiteFlag, long curDate);

	/**
	 * 特卖预告，外部提供时间参数
	 * @param chlId
	 * @param saleSiteFlag
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	ScheduleListVO getScheduleListForFuture(long chlId, long saleSiteFlag, long startTime, long endTime);

	/**
	 * For POPrdInvalidJob.java
	 * 
	 * @param isPrdList
	 * @return
	 */
	boolean invalidPrdsForExpiredPO(boolean isPrdList);
	
	Schedule getScheduleByScheduleId(long id);
}