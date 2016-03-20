package com.xyl.mmall.saleschedule.service;

import java.util.List;

import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.SchedulePageDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleStatusDTO;

/**
 * 档期品购页服务
 * 
 * @author hzzhanghui
 * 
 */
public interface SchedulePageService {

	/**
	 * 新增
	 * 
	 * @param poDto
	 *            含有page对象的DTO
	 * @return 添加成功返回true并返回带id的page。否则返回false
	 */
	PODTO saveSchedulePage(PODTO poDto);

	/**
	 * 更新
	 * 
	 * @param poDto
	 * @return 更新成功返回true，否则返回false
	 */
	boolean updateSchedulePage(PODTO poDto);

	/**
	 * 审核拒绝
	 * 
	 * @param id
	 * @param desc
	 *            拒绝理由
	 * @return
	 */
	boolean auditSchedulePageReject(long id, long poId, String desc);

	/**
	 * 审核通过
	 * 
	 * @param id
	 * @return
	 */
	boolean auditSchedulePagePass(long id, long poId);

	/**
	 * 提交审核
	 * 
	 * @param id
	 * @return
	 */
	boolean auditSchedulePageSubmit(long id, Long poId);

	/**
	 * 根据id删除page
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteSchedulePageById(long id);

	/**
	 * 根据id查询page
	 * 
	 * @param id
	 * @return
	 */
	PODTO getSchedulePageById(long id);

	/**
	 * 根据PO查询page
	 */
	PODTO getSchedulePageByScheduleId(long id);

	/**
	 * 综合查询
	 * 
	 * @return
	 */
	POListDTO getSchedulePageList(ScheduleCommonParamDTO paramDTO, long pageId, String brandName);
	
	/**
	 * Get page list
	 * 
	 * @param poIdList
	 * @return
	 */
	List<SchedulePageDTO> getSchedulePageList(List<Long> poIdList);

	/**
	 * 查询品购页状态列表
	 * 
	 * @return
	 */
	List<ScheduleStatusDTO> getSchedulePageStatusList();
	
	/**
	 * 更新档期品购页商品列表的排序方式
	 * 
	 * @param poId
	 *            档期id
	 * @param type
	 *            默认是0
	 * @return 更新成功返回true，否则返回false
	 */
	boolean updatePOPagePrdListOrderType(long poId, int type);

	/**
	 * 获取档期品购页的商品列表排序方式
	 * 
	 * @param poId
	 *            档期id
	 * @return 排序方式
	 */
	int getPOPagePrdListOrderType(long poId);
}
