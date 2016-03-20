package com.xyl.mmall.order.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState;
import com.xyl.mmall.order.meta.DeprecatedReturnOrderSku;
import com.xyl.mmall.order.param.ReturnConfirmParam;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午5:14:37
 *
 */
@Deprecated
public interface DeprecatedReturnOrderSkuDao extends AbstractDao<DeprecatedReturnOrderSku> {
	
	/**
	 * 通过退货记录id获取退货OrderSku记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByReturnId(long returnId);
	
	/**
	 * 通过退货记录id+指定状态获取退货OrderSku记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByReturnIdWithState(long returnId, DeprecatedReturnOrderSkuState[] stateArray);
	
	/**
	 * 查询指定状态的退货OrderSku记录
	 * 
	 * @param stateArray
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByState(DeprecatedReturnOrderSkuState[] stateArray, DDBParam param);
	
	/**
	 * 查询指定用户指定状态的退货OrderSku记录
	 * 
	 * @param stateArray
	 * @param userId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByStateWithUserId(DeprecatedReturnOrderSkuState[] stateArray, long userId, 
			DDBParam param);
	
	/**
	 * 查询指定订单指定状态的退货OrderSku记录
	 * 
	 * @param stateArray
	 * @param orderId
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByStateWithOrderId(DeprecatedReturnOrderSkuState[] stateArray, long orderId, 
			DDBParam param);
	
	/**
	 * 查询指定时间是定状态的退货OrderSku记录
	 * 
	 * @param stateArray
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List<DeprecatedReturnOrderSku> queryRetOrdSkuListByStateWithTimeRange(DeprecatedReturnOrderSkuState[] stateArray, long start, long end, 
			DDBParam param);
	
	/**
	 * 根据主键查找记录
	 * 
	 * @param orderId
	 * @param orderSkuId
	 * @return
	 */
	public DeprecatedReturnOrderSku queryRetOrdSkuByOrderIdAndOrderSkuId(long orderId, long orderSkuId);
	
	/**
	 * 更新仓库收到退货信息
	 * 
	 * @param retOrdSku
	 * @param param
	 * @return
	 */
	public boolean confirmReturnOrderSku(DeprecatedReturnOrderSku retOrdSku, ReturnConfirmParam param);

	/**
	 * 更新为已退款
	 * 
	 * @param retOrdSku
	 * @return
	 */
	public boolean setConfirmedRetOrdSkuToReturned(DeprecatedReturnOrderSku retOrdSku);
	
	/**
	 * 取消退货
	 * 
	 * @param retId
	 * @param userId
	 * @return
	 */
	public boolean deprecateRetOrdSku(long retId, long userId);
}