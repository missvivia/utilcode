package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.enums.ScheduleLikeState;
import com.xyl.mmall.saleschedule.meta.ScheduleLike;

/**
 * PO收藏操作DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleLikeDao extends AbstractDao<ScheduleLike> {

	/**
	 * 新增
	 * 
	 * @param like
	 *            待新增的like
	 * @return 新增成功返回true。否则返回false。新增成功后入参like带有id。
	 */
	boolean saveScheduleLike(ScheduleLike like);

	/**
	 * 批量增加like
	 * 
	 * @param likeList
	 * @return 添加成功返回true，否则返回false
	 */
	boolean saveShceduleLikeList(List<ScheduleLike> likeList);

	/**
	 * 更新
	 * 
	 * @param like
	 *            带有id的待更新like
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateScheduleLike(ScheduleLike like);

	/**
	 * 根据id删除一个like
	 * 
	 * @param id
	 *            like id标识
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteScheduleLikeById(long id);

	/**
	 * 根据PO id和userId删除一个like
	 * 
	 * @param scheduleId
	 *            PO id
	 * @param userId
	 * @return 删除成功返回true，否则返回false
	 */
	boolean deleteScheduleLikeByScheduleIdAndUserId(long scheduleId, long userId);
	
	/**
	 * Delete all like on specific PO
	 * 
	 * @param scheduleId
	 * @return
	 */
	boolean deleteScheduleLikeByScheduleId(long scheduleId);
	
	/**
	 * Delete all user's like
	 * @param userId
	 * @return
	 */
	boolean deleteScheduleLikeByUserId(long userId);

	/**
	 * 根据id查询like信息
	 * 
	 * @param id
	 *            like标识
	 * @return 如果数据库有数据返回like对象，否则返回null
	 */
	ScheduleLike getScheduleLikeById(long id);

	/**
	 * 根据PO id和userId查询
	 * 
	 * @param scheduleId
	 * @param userId
	 * @return 根据PO id和userId查询唯一一条关注记录
	 */
	ScheduleLike getScheduleLikeByScheduleIdAndUserId(long scheduleId, long userId);

	/**
	 * 查询指定状态的like列表
	 * 
	 * @param status
	 *            like状态
	 * @return 返回指定状态下的like列表。如果没有查到数据，则返回size=0的列表。不会返回null。
	 */
	List<ScheduleLike> getScheduleLikeListByStatus(ScheduleLikeState status);

	/**
	 * 查询用户关注的PO列表
	 * 
	 * @param userId
	 * @return 返回用户关注的PO列表
	 */
	List<ScheduleLike> getScheduleLikeListByUserId(long userId);
	
//	/**
//	 * 查询用户关注的PO列表
//	 * 
//	 * @param userId
//	 * @param supplierAreaId
//	 * @return
//	 */
//	POListDTO getLikedPOListByUserIdAndSiteId(long userId, long supplierAreaId, long startId, int curPage, int pageSize);
	
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

	/**
	 * 
	 * 查询关注某个PO的所有用户列表
	 * 
	 * @param scheduleId
	 * @return 返回关注某个PO的用户列表
	 */
	List<ScheduleLike> getScheduleLikeListByScheduleId(long scheduleId);
	
	/**
	 * 获取关注某个PO的用户数
	 * 
	 * @param poId
	 * @return
	 */
	long getLikeCntByPOId(long poId);
	
}
