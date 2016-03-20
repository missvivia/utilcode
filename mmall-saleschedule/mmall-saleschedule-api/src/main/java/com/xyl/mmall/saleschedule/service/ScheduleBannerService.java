package com.xyl.mmall.saleschedule.service;

import java.util.List;

import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleBannerDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;

/**
 * 档期Banner服务
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleBannerService {

	/**
	 * 新增
	 * 
	 * @param poDto
	 * @return
	 */
	PODTO saveScheduleBanner(PODTO poDto);

	/**
	 * 修改
	 * 
	 * @param poDto
	 * @return
	 */
	boolean updateScheduleBanner(PODTO poDto);

	/**
	 * banner审核拒绝
	 * 
	 * @param poDTO
	 * @return
	 */
	boolean auditScheduleBannerReject(PODTO poDTO);

	/**
	 * banner审核通过
	 * 
	 * @param poDTO
	 * @return
	 */
	boolean auditScheduleBannerPass(PODTO poDTO);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteScheduleBannerById(long id);

	/**
	 * 根据PO id查询banner信息
	 * 
	 * @param scheduleId
	 *            PO标识
	 * @return 返回和PO对应的banner信息，如果差不多返回null
	 */
	PODTO getScheduleBannerByScheduleId(long scheduleId);

	/**
	 * 根据banner id获取banner信息
	 * 
	 * @param id
	 * @return
	 */
	PODTO getScheduleBannerById(long id);

//	/**
//	 * 根据状态查询banner列表
//	 * 
//	 * @param status
//	 * @return
//	 */
//	POListDTO getScheduleBannerListByStatus(CheckState status);
//
//	/**
//	 * 根据状态查询banner列表
//	 * 
//	 * @param status
//	 * @param curPage
//	 * @param pageSize
//	 * @return
//	 */
//	POListDTO getScheduleBannerListByStatus(CheckState status, int curPage, int pageSize);

	/**
	 * Get banner list
	 * 
	 * @param poIdList
	 * @return
	 */
	List<ScheduleBannerDTO> getScheduleBannerList(List<Long> poIdList);

//	/**
//	 * 根据开始时间查询banner列表
//	 * 
//	 * @param startDate
//	 * @return
//	 */
//	POListDTO getScheduleBannerListByStartDate(long startDate);
//
//	/**
//	 * 根据开始时间查询banner列表
//	 * 
//	 * @param startDate
//	 * @param curPage
//	 * @param pageSize
//	 * @return
//	 */
//	POListDTO getScheduleBannerListByStartDate(long startDate, int curPage, int pageSize);

//	/**
//	 * 根据开始结束时间查询banner列表
//	 * 
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
//	POListDTO getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate);
//
//	/**
//	 * 根据开始结束时间查询banner列表
//	 * 
//	 * @param startDate
//	 * @param endDate
//	 * @param curPage
//	 * @param pageSize
//	 * @return
//	 */
//	POListDTO getScheduleBannerListByStartDateAndEndDate(long startDate, long endDate, int curPage, int pageSize);

	/**
	 * 综合查询
	 * 
	 */
	POListDTO getScheduleBannerList(ScheduleCommonParamDTO paramDTO, String supplierName, String brandName);

	/**
	 * 
	 * @return
	 */
	POListDTO getScheduleBannerListWithSupplierIdList(ScheduleCommonParamDTO paramDTO, String brandName);

}
