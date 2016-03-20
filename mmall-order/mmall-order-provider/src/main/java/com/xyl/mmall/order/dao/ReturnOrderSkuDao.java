package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState;
import com.xyl.mmall.order.meta.ReturnOrderSku;
import com.xyl.mmall.order.param.ReturnConfirmParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:14:37
 *
 */
public interface ReturnOrderSkuDao extends AbstractDao<ReturnOrderSku> {
	
	/**
	 * 通过退货记录id获取退货OrderSku记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<ReturnOrderSku> queryObjectsByRetPkgId(long retPkgId, long userId);
	
	/**
	 * 通过退货记录id+指定状态获取退货OrderSku记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<ReturnOrderSku> queryObjectsByRetPkgIdWithState(long retPkgId, long userId, ReturnOrderSkuConfirmState[] stateArray);
	
	/**
	 * 更新仓库收到退货信息
	 * 
	 * @param retOrdSku
	 * @param param
	 * @return
	 */
	public boolean confirmReturnOrderSku(ReturnOrderSku retOrdSku, ReturnConfirmParam param);

	/**
	 * 更新_ReturnOrderSkuConfirmState
	 * 
	 * @param retOrdSku
	 * @return
	 */
	public boolean updateReturnOrderSkuConfirmState(ReturnOrderSku retOrdSku, ReturnOrderSkuConfirmState newState, 
			ReturnOrderSkuConfirmState[] stateArray);
}