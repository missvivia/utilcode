/**
 * 
 */
package com.xyl.mmall.promotion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.util.TransformType;
import com.xyl.mmall.promotion.dao.PointDao;
import com.xyl.mmall.promotion.dao.PointHistoryDao;
import com.xyl.mmall.promotion.dao.UserPointDao;
import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.enums.AuditState;
import com.xyl.mmall.promotion.enums.PointHistoryType;
import com.xyl.mmall.promotion.meta.Point;
import com.xyl.mmall.promotion.meta.PointHistory;
import com.xyl.mmall.promotion.meta.UserPoint;
import com.xyl.mmall.promotion.service.PointService;

/**
 * @author jmy
 *
 */
@Service("pointService")
public class PointServiceImpl implements PointService {
	private static final String USERIDS_SEPARATOR = ",";
	@Autowired
	private PointDao pointDao;
	@Autowired
	private PointHistoryDao historyDao;
	@Autowired
	private UserPointDao userPointDao;

	/* (non-Javadoc)
	 * @see com.xyl.mmall.promotion.service.PointService#addPoint(com.xyl.mmall.promotion.meta.Point)
	 */
	@Override
	public Point addPoint(Point point) {
		return pointDao.addObject(point);
	}

//	/* (non-Javadoc)
//	 * @see com.xyl.mmall.promotion.service.PointService#deletePointById(long)
//	 */
//	@Override
//	public boolean deletePointById(long id) {
//		return pointDao.deleteById(id);
//	}
//
//	@Override
//	public boolean deletePointById(List<Long> ids) {
//		return pointDao.deleteById(ids.toArray(new Long[0]));
//	}

	/* (non-Javadoc)
	 * @see com.xyl.mmall.promotion.service.PointService#updatePointById(com.xyl.mmall.promotion.meta.Point)
	 */
	@Override
	public Point updatePointPartlyById(PointDTO point) {
		if (pointDao.updatePartlyById(point)) {
			return pointDao.getObjectById(point.getId());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.xyl.mmall.promotion.service.PointService#getPointById(int)
	 */
	@Override
	public Point getPointById(long id) {
		return pointDao.getObjectById(id);
	}

	/* (non-Javadoc)
	 * @see com.xyl.mmall.promotion.service.PointService#getPointList(com.xyl.mmall.promotion.dto.PointDTO, int, int)
	 */
	@Override
	public List<PointDTO> getPointList(PointDTO point, int limit, int offset) {
		List<Point> lstPoint = pointDao.getBy(point, limit, offset);
		return convert2DTO(lstPoint);
	}
	
	private List<PointDTO> convert2DTO(List<Point> lstPoint) {
		if (lstPoint == null) return null;
		List<PointDTO> result = new ArrayList<>(lstPoint.size());
		for (Point p : lstPoint) {
			result.add(new PointDTO(p));
		}
		return result;
	}

	@Override
	public Point updateStateById(long id, AuditState state, long userId) {
		if (state == null) {
			throw new IllegalArgumentException("未指定更新状态");
		}
		Point point = pointDao.getObjectById(id);
		if (point == null) return null;
		//状态不变化
		if (state.getType() == point.getAuditState()) return point;
		//状态变更约束
		if (point.getAuditState() == AuditState.PASSED.getType()) {
			throw new IllegalStateException("已审核通过");
		}
		if ((state == AuditState.PASSED || state == AuditState.REJECTED) 
				&& point.getAuditState() != AuditState.SUBMITTED.getType()) {
			throw new IllegalStateException("未提交状态，不能审核");
		}
		if (state == AuditState.SUBMITTED
				&& (point.getAuditState() != AuditState.INIT.getType() || point.getAuditState() != AuditState.REJECTED.getType())) {
			throw new IllegalStateException("非可提交状态");
		}
		
		PointDTO updateObject = new PointDTO();
		updateObject.setId(id);
		updateObject.setAuditState(state.getType());
		if (state == AuditState.PASSED || state == AuditState.REJECTED) {
			updateObject.setAuditUserId(userId);
			updateObject.setAuditTime(new Date().getTime());
		}
		if (pointDao.updatePartlyById(updateObject)) {
			//审核通过
			if (state == AuditState.PASSED) {
				Long[] userIds = getUserIds(point.getUsers());
				if (userIds.length > 0) {
					for (Long uId : userIds) {
						//修改/新建用户积分
						UserPoint userPoint = new UserPoint();
						userPoint.setPoint(point.getPointDelta());
						userPoint.setUserId(uId);
						userPointDao.saveObject(userPoint);
						
						//添加积分记录
						PointHistory history = new PointHistory();
						history.setName(point.getName());
						history.setPointDelta(point.getPointDelta());
						history.setType(PointHistoryType.CMS.getType());
						history.setUserId(uId);
						historyDao.addObject(history);
					}
				}
				point.setAuditState(state.getType());//用于返回更新后状态的对象
			}
		}
		return point;
	}
	
	private static Long[] getUserIds(String users) {
		return TransformType.transform(StringUtils.split(users, USERIDS_SEPARATOR), false);
	}
	
	private static String transformCommaSplited(Long[] userIds) {
		if (userIds == null || userIds.length == 0) return "";
		StringBuilder s = new StringBuilder();
		s.append(userIds[0]);
		for (int i = 1; i < userIds.length; i ++) {
			s.append(USERIDS_SEPARATOR);
			s.append(userIds[i]);
		}
		return s.toString();
	}

}
