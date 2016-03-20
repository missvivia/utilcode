/**
 * 
 */
package com.xyl.mmall.promotion.service;

import java.util.List;

import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.enums.AuditState;
import com.xyl.mmall.promotion.meta.Point;


/**
 * @author jmy
 *
 */
public interface PointService {
	
	Point addPoint(Point point);
	
//	boolean deletePointById(long id);
	
//	boolean deletePointById(List<Long> ids);
	
	/**
	 * 根据ID更新
	 * @param point
	 * @return 更新成功后返回新对象，更新失败返回null
	 */
	Point updatePointPartlyById(PointDTO point);
	
	/**
	 * 更新调整状态，如果审核通过，修改用户积分，并记录调整历史
	 * @param id
	 * @param state
	 * @param userId
	 * @return
	 */
	Point updateStateById(long id, AuditState state, long userId);
	
	Point getPointById(long id);
	
	List<PointDTO> getPointList(PointDTO point, int limit, int offset);
}
