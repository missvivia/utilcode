/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.meta.Point;

/**
 * PointDao.java created by yydx811 at 2015年12月23日 下午12:27:20
 * 积分dao
 *
 * @author yydx811
 */
public interface PointDao extends AbstractDao<Point> {
	/**
	 * 分页查询
	 * @param queryObject
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<Point> getBy(PointDTO queryObject, Integer limit, Integer offset);
	
	/**
	 * 根据ID更新记录部分字段，不更新ID及均衡字段SiteId
	 * @param updateObject
	 * @return
	 */
	boolean updatePartlyById(PointDTO updateObject);
	
	/**
	 * 删除指定ID列表对于的所有记录
	 * @param ids
	 * @return 是否全部删除
	 */
	boolean deleteById(Long[] ids);
}
