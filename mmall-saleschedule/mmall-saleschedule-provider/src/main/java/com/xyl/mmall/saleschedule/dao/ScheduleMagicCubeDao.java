package com.xyl.mmall.saleschedule.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.saleschedule.meta.ScheduleMagicCube;

/**
 * ScheduleMagicCube操作DAO
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleMagicCubeDao extends AbstractDao<ScheduleMagicCube> {

	/**
	 * 新增
	 * 
	 * @return 新增成功返回true。否则返回false。新增成功后入参mc带有id。
	 */
	boolean saveScheduleMagicCube(ScheduleMagicCube mc);

	/**
	 * 批量增加
	 * 
	 * @return 
	 */
	boolean saveShceduleMagicCubeList(List<ScheduleMagicCube> mcList);

	/**
	 * 更新
	 * 
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateScheduleMagicCubeBySupplierId(ScheduleMagicCube mc);

	/**
	 * 根据Supplier Id删除
	 * 
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteScheduleMagicCubeBySupplierId(long supplierId);

	/**
	 * 根据supplier id查询
	 * 
	 * @return
	 */
	ScheduleMagicCube getScheduleMagicCubeBySupplierId(long supplierId);
	
}
