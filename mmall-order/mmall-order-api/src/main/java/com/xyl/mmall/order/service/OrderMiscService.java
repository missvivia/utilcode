package com.xyl.mmall.order.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.CancelOmsOrderTaskState;
import com.xyl.mmall.order.param.OrderSkuQueryParam1;

/**
 * @author dingmingliang
 * 
 */
public interface OrderMiscService {

	/**
	 * 1.更新订单状态为 WAITING_CANCEL_OMSORDER<br>
	 * 2.添加CancelOmsOrderTask到数据库
	 * 
	 * @param cancelTaskDTO
	 * @return
	 */
	public CancelOmsOrderTaskDTO addCancelOmsOrderTask(CancelOmsOrderTaskDTO cancelTaskDTO);

	/**
	 * 更新CancelOmsOrderTask.state字段
	 * 
	 * @param cancelTaskDTO
	 * @param oldState
	 * @return
	 */
	public boolean updateCancelOmsOrderTaskState(CancelOmsOrderTaskDTO cancelTaskDTO, CancelOmsOrderTaskState oldState);

	/**
	 * 查询某个状态的CancelOmsOrderTaskDTO列表
	 * 
	 * @param minOrderId
	 * @param taskStateArray
	 * @param param
	 * @return
	 */
	public List<CancelOmsOrderTaskDTO> queryCancelOmsOrderTaskDTOList(long minOrderId,
			CancelOmsOrderTaskState[] taskStateArray, DDBParam param);

	/**
	 * 查询某个CancelOmsOrderTaskDTO记录
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public CancelOmsOrderTaskDTO queryCancelOmsOrderTaskDTO(long orderId, long userId);

	/**
	 * 查询符合条件的OrderSkuDTO
	 * 
	 * @param poId
	 * @param paramColl
	 * @return Key: orderId
	 */
	public Map<Long, List<OrderSkuDTO>> getOrderSkuDTOListMap(long poId, Collection<OrderSkuQueryParam1> paramColl);

	/**
	 * 获得已经取消的OrderSku明细(包含包裹取消和退货)
	 * 
	 * @param orderId
	 * @param userId
	 * @param poId
	 * @return RetArg.ArrayList: List(StCancelOrderSkuResult)<br>
	 *         RetArg.Integer: 返回值(0:参数错误,1:查询成功)
	 */
	public RetArg stCancelOrderSku(long orderId, long userId, Long poId);
}
