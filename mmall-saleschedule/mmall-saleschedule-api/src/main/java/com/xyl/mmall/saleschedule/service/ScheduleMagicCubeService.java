package com.xyl.mmall.saleschedule.service;

import java.util.List;

import com.xyl.mmall.saleschedule.dto.ScheduleMagicCubeDTO;

/**
 * 
 * @author hzzhanghui
 * 
 */
public interface ScheduleMagicCubeService {

	/**
	 * 新增
	 * 
	 * @return 新增成功返回true。否则返回false。
	 */
	boolean saveScheduleMagicCube(ScheduleMagicCubeDTO mc);

	/**
	 * 批量增加
	 * 
	 * @return
	 */
	boolean saveShceduleMagicCubeList(List<ScheduleMagicCubeDTO> mcList);

	/**
	 * 更新
	 * 
	 * @return 更新成功返回true。否则返回false
	 */
	boolean updateScheduleMagicCubeBySupplierId(ScheduleMagicCubeDTO mc);

	/**
	 * 根据Supplier id删除
	 * 
	 * @return 删除成功返回true，否则返回false。
	 */
	boolean deleteScheduleMagicCubeBySupplierId(long supplierId);

	/**
	 * 根据Supplier id查询
	 * 
	 * @return
	 */
	ScheduleMagicCubeDTO getScheduleMagicCubeBySupplierId(long supplierId);
}
