/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.meta.WarehouseForm;


/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseDao extends AbstractDao<WarehouseForm> {
	/**
	 * 查询仓库列表
	 */
	public List<WarehouseForm> getList();

	/**
	 * 根据ID获取仓库名称
	 */
	public WarehouseForm getWarehouseById(long id);
	
	/**
	 * 查询仓库id列表
	 * @return
	 */
	public List<Long> getIdList();
	
	/**
	 * 查询出仓库快递公司列表
	 * @return
	 */
	public List<String> getExpressCompanyList();
	
	/**
	 * 查询仓库列表
	 */
	public List<WarehouseForm> getListByProvinceList(List<Long> areaLists);

}
