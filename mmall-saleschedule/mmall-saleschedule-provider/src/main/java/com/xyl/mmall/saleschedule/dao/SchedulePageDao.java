package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.meta.SchedulePage;

/**
 * Schedule操作DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface SchedulePageDao extends AbstractDao<SchedulePage> {

	/**
	 * 新增
	 * 
	 * @param page
	 *            待新增的page
	 * @return 新增成功返回true。否则返回false。新增成功后入参page带有id。
	 */
	boolean saveSchedulePage(SchedulePage page);

	/**
	 * 更新
	 * 
	 * @param page
	 *            带有id的待更新page
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateSchedulePage(SchedulePage page);

	/**
	 * Update PO fields in page
	 * 
	 * @param page
	 * @return
	 */
	boolean updateSchedulePagePOField(SchedulePage page);

	/**
	 * 根据主键id删除
	 * 
	 * @param id
	 *            page标识
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteSchedulePageById(long id);

	/**
	 * 根据id查询
	 * 
	 * @param id
	 *            page标识
	 * @return 如果数据库有数据返回page对象，否则返回null
	 */
	SchedulePage getSchedulePageById(long id, Long userId);

	/**
	 * 根据pageId或者scheduleId查询档期信息（包括品购页）
	 * 
	 * @param pageId
	 * @param scheduleId
	 * @return
	 */
	PODTO getPOByPageIdOrScheduleId(long pageId, long scheduleId);

	/**
	 * 根据PO id查询
	 * 
	 * @param scheduleId
	 *            page标识
	 * @return 如果数据库有数据返回page对象，否则返回null
	 */
	SchedulePage getSchedulePageByScheduleId(long scheduleId, Long userId);

	/**
	 * 综合 查询
	 */
	POListDTO getSchedulePageList(ScheduleCommonParamDTO paramDTO, long pageId, String brandName);
	
	List<SchedulePage> getSchedulePageList(List<Long> poIdList);

	/**
	 * 更新page状态
	 * 
	 * @param id
	 *            page id
	 * @param status
	 *            page状态
	 * @param desc
	 *            如果拒绝的话需要填写拒绝理由
	 * @return
	 */
	boolean updateStatus(long id, CheckState status, String desc, Long poId);

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
}
