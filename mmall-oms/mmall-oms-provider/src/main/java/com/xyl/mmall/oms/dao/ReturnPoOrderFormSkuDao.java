package com.xyl.mmall.oms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;

/**
 * @author hzzengchengyuan
 *
 */
public interface ReturnPoOrderFormSkuDao extends AbstractDao<ReturnPoOrderFormSku> {
	/**
	 * 所有退供单下所有商品明细
	 * 
	 * @param poReturnOrderId
	 * @return
	 */
	List<ReturnPoOrderFormSku> getListByPoReturnOrderId(long poReturnOrderId);

	/**
	 * @param params
	 * @return
	 */
	PageableList<ReturnPoOrderFormSku> querReturnSku(PoRetrunSkuQueryParamDTO params);

	/**
	 * 根据组合条件统计
	 * 
	 * @param params
	 * @return
	 */
	long countByParams(PoRetrunSkuQueryParamDTO params);
	
	/**
	 * 统计已创建退货单且符合条件的PO退货中中所有商品的sku总数
	 * @param params
	 * @return
	 */
	long countSkuCountByParams(PoRetrunSkuQueryParamDTO params);

	/**
	 * 更新退货单的仓储反馈信息：实际出仓时间、实际发货数量
	 * 
	 * @param sku
	 * @return
	 */
	boolean updatePoReturnOrderSku(ReturnPoOrderFormSku sku);

	/**
	 * 更新PO退货单下所有商品的状态
	 * @param poReturnOrderId
	 * @param oldState
	 * @param newState
	 * @return
	 */
	boolean updateState(long poReturnOrderId, PoReturnOrderState oldState, PoReturnOrderState newState);

	/**
	 * 指定po的N退数量N=1,2,3
	 * 
	 * @param poOrderId
	 * @return
	 */
	public int getTotalNReturnOfPoOrderId(String poOrderId, ReturnType type);

}
