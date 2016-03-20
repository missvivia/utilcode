package com.xyl.mmall.saleschedule.service;

import java.util.List;
import java.util.Map;

import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;

/**
 * PO收藏服务
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleLikeService {

	/**
	 * 新增关注
	 * 
	 * 
	 * @param dto
	 *            包含userId和PO id
	 * @return 关注成功返回true，否则返回false
	 */
	ScheduleLikeDTO like(ScheduleLikeDTO dto);

	/**
	 * 取消关注
	 * 
	 * @param dto
	 *            包含userId和PO id
	 * @return 取消关注成功返回true，否则返回false
	 */
	boolean unLike(ScheduleLikeDTO dto);
	
	/**
	 * Delete all like on specific PO
	 * 
	 * @param poId
	 * @return
	 */
	boolean unlikeByPoId(long poId);
	
	/**
	 * Delete all user's like
	 * @param userId
	 * @return
	 */
	boolean unlikeByUserId(long userId);

	/**
	 * Check whether a user has liked a PO
	 * 
	 * @param userId
	 * @param poId
	 * @return true if the user liking the PO, false otherwise
	 */
	boolean isLike(long userId, long poId);

	/**
	 * 查询某个用户关注的所有PO列表
	 * 
	 * @param userId
	 * @return 返回给定user关注的PO列表
	 */
	List<ScheduleLikeDTO> getLikeListByUserId(long userId);

//	/**
//	 * 获取用户关注的PO列表
//	 * 
//	 * @param userId
//	 *            用户id
//	 * @param supplierAreaId
//	 *            站点
//	 * @param timestamp
//	 *            表示从哪个时间段开始取数据。用于SQL子句:startTime>=timestamp
//	 * @param limit
//	 *            需要返回的记录条数。用于SQL子句 limit N
//	 * @return
//	 */
//	POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long timestamp, int limit);
//
//	/**
//	 * 查询用户关注的PO列表
//	 * 
//	 * @param userId
//	 * @param supplierAreaId
//	 *            省份code
//	 * @return
//	 */
//	POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long startId, int curPage, int pageSize);

	/**
	 * 查询关注某个PO的所有用户列表
	 * 
	 * @param poId
	 * @return 返回关注给定PO的所有用户列表
	 */
	List<ScheduleLikeDTO> getLikeListByPOId(long poId);

	/**
	 * 获取关注某个PO的用户数
	 * 
	 * @param poId
	 * @return
	 */
	long getLikeCntByPOId(long poId);

	/**
	 * Batch check like status for each PO that the user whether like it or not
	 * 
	 * @param poIds
	 * @param userId
	 * @return map. key is poId, value is true|false
	 */
	Map<Long, Boolean> batchCheckLikeStatus(List<Long> poIds, Long userId);

}
