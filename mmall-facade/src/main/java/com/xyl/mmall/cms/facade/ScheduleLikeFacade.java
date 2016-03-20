package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.ScheduleLikeVO;
import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleLikeFacade {
	/**
	 * 新增关注
	 * 
	 * @param dto
	 *            包含userId和PO id
	 * @return 关注成功返回true，否则返回false
	 */
	ScheduleLikeVO addLike(ScheduleLikeDTO dto);

	/**
	 * 取消关注
	 * 
	 * @param dto
	 *            包含userId和PO id
	 * @return 取消关注成功返回true，否则返回false
	 */
	boolean cancelLike(ScheduleLikeDTO dto);

	/**
	 * 查询某个用户关注的所有PO列表
	 * 
	 * @param userId
	 * @return 返回给定user关注的PO列表
	 */
	List<ScheduleLikeVO> getLikeListByUserId(long userId);

	/**
	 * 查询关注某个PO的所有用户列表
	 * 
	 * @param poId
	 * @return 返回关注给定PO的所有用户列表
	 */
	List<ScheduleLikeVO> getLikeListByPOId(long poId);

//	/**
//	 * 查询用户关注的PO列表
//	 * 
//	 * @param userId
//	 * @param supplierAreaId
//	 *            省份code
//	 * @return
//	 */
//	ScheduleListVO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long startId, int curPage,
//			int pageSize);

	/**
	 * 获取关注某个PO的用户数
	 * 
	 * @param poId
	 * @return
	 */
	long getLikeCntByPOId(long poId);
	
	/**
	 * Check whether a user has liked a PO
	 * 
	 * @param userId
	 * @param poId
	 * @return true if the user liking the PO, false otherwise
	 */
	boolean isLike(long userId, long poId);
	
}
