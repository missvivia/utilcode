/**
 * 
 */
package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.meta.WarehouseReturn;

/**
 * @author hzzengchengyuan
 *
 */
public interface WarehouseReturnDao extends AbstractDao<WarehouseReturn> {
	
	/**
	 * 根据退货单id集合查询退货明细列表
	 * @param ids
	 * @return
	 */
	List<WarehouseReturn> getListByIds(List<Long> ids);
	
	/**
	 * 统计符合条件的未生成退供单的退货需求数量
	 * @param params
	 * @return
	 */
	long countByParams(PoRetrunSkuQueryParamDTO params);
	
	/**
	 * @param params
	 * @return
	 */
	PageableList<WarehouseReturn> querReturn(PoRetrunSkuQueryParamDTO params);
	
	PageableList<Long> queryPoIdFromReturnSku(long limit, long offset);
}
