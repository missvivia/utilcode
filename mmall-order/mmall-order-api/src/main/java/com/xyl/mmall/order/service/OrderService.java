package com.xyl.mmall.order.service;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelState;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.OrderSku;
import com.xyl.mmall.order.param.OrderExpInfoChangeParam;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.order.param.OrderServiceSetStateToEPayedParam;

/**
 * 下单后的订单信息服务
 * 
 * @author dingmingliang
 * 
 */
public interface OrderService {

	/**
	 * 获得一个分配的Id
	 * 
	 * @return
	 */
	public long allocateRecordId();

	/**
	 * 查询订单信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param isVisible
	 * @return
	 */
	public OrderFormDTO queryOrderForm(long userId, long orderId, Boolean isVisible);
	
    /**
     * 查询订单信息列表
     * 
     * @param userId
     * @param parentId
     * @param isVisible
     * @return
     */
    public List<OrderFormDTO> queryOrderFormList(long userId, long parentId, Boolean isVisible);

	/**
	 * 查询某个用户，某些状态的订单
	 * 
	 * @param userId
	 * @param isVisible
	 *            null: 忽略是否可见的条件
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param param
	 * @return
	 */
	public List<OrderFormDTO> queryOrderFormListByState(long userId, Boolean isVisible, OrderFormState[] stateArray,
			DDBParam param);

	/**
	 * 查询某个用户的订单(支持订单Id的模糊查询)
	 * 
	 * @param userId
	 * @param orderIdOfPart
	 *            订单Id的模糊查询
	 * @param isVisible
	 *            null: 忽略是否可见的条件
	 * @param orderTimeRange
	 *            下单时间范围
	 * @param param
	 * @return
	 */
	public RetArg queryOrderFormListByLikeOrderId(long userId, Boolean isVisible, Long orderIdOfPart,
			long[] orderTimeRange, DDBParam param);

	/**
	 * 查询某个用户，某些状态的订单
	 * 
	 * @param userId
	 * @param isVisible
	 *            null: 忽略是否可见的条件
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormListByState2(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange, DDBParam param);

	/**
	 * 查询某个用户，某些状态的订单的个数
	 * 
	 * @param userId
	 * @param isVisible
	 *            null: 忽略是否可见的条件
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param orderTimeRange
	 * @return
	 */
	public int queryOrderFormListCountByState2(long userId, Boolean isVisible, OrderFormState[] stateArray,
			long[] orderTimeRange,Boolean isOnPay);
	
	
	/**
	 * 查询某个商家，某些状态的订单的个数
	 * 
	 * @param userId
	 * @param isVisible
	 *            null: 忽略是否可见的条件
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param orderTimeRange
	 * @return
	 */
	public int queryBusiOrderFormListCountByState(long businessId, Boolean isVisible,
			OrderFormState[] stateArray, long[] orderTimeRange);

	/**
	 * 查询某些状态的订单
	 * 
	 * @param minOrderId
	 * @param stateArray
	 *            null: 全部状态的订单
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormListByStateWithMinOrderId(long minOrderId, OrderFormState[] stateArray, DDBParam param);

	/**
	 * 查询订单<br>
	 * 1. 未取消<br>
	 * 2. 已在线支付 or COD审核通过且未拒付
	 * 
	 * @param minOrderId
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormListByType1WithMinOrderId(long minOrderId, long[] orderTimeRange, DDBParam param);
	
	/**
	 * 设置订单状态为ALL_DELIVE(更新前状态为WAITING_DELIVE)
	 * 
	 * @param userId
	 * @param orderId
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败<br>
	 *         1:更新成功<br>
	 *         2:重复更新
	 */
	public int setOrderFormStateToDelive(long userId, long orderId);


	/**
	 * 设置订单状态为WAITING_DELIVE(更新前状态为WAITING_SEND_ORDER)
	 * 
	 * @param userId
	 * @param orderId
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败<br>
	 *         1:更新成功<br>
	 *         2:重复更新
	 */
	public int setOrderFormStateToWaitingDelive(long userId, long orderId);

	/**
	 * 设置订单状态为FINISH_DELIVE(更新前状态为PART_DELIVE|ALL_DELIVE)
	 * 
	 * @param userId
	 * @param orderId
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败<br>
	 *         1:更新成功<br>
	 *         2:重复更新
	 * @return
	 */
	public int setOrderFormStateToFinishDelive(long userId, long orderId);

	/**
	 * 设置订单状态为CANCEL_ED(更新前状态为PART_DELIVE|ALL_DELIVE)
	 * 
	 * @param userId
	 * @param orderId
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败<br>
	 *         1:更新成功<br>
	 *         2:重复更新
	 * @return
	 */
	public int setOrderFormStateToCancel(long userId, long orderId);

	/**
	 * 1.设置订单状态为 WAITING_CANCEL_OMSORDER 之前的值<BR>
	 * 2.设置ExtInfo字段为取消失败
	 * 
	 * @param userId
	 * @param orderId
	 * @param newState
	 * @return -1:获取订单锁失败<br>
	 *         0:更新失败 <br>
	 *         1:更新成功 <br>
	 *         2:重复更新
	 */
	public int setOrderFormStateToCancelRevert(long userId, long orderId, OrderFormState newState);

	/**
	 * 根据快递单号查询订单
	 * 
	 * @param mailNO
	 * @param isVisible
	 * @param param
	 * @return
	 */
	public List<OrderFormDTO> queryOrderFormListByMailNO(String mailNO, Boolean isVisible, DDBParam param);

	/**
	 * 查询“全部订单”
	 * 
	 * @param userId
	 * @param isVisible
	 * @param orderTimeRange
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryAllOrderFormList(long userId, Boolean isVisible, long[] orderTimeRange, DDBParam param);

	/**
	 * 查询“全部订单”的个数
	 * 
	 * @param userId
	 * @param isVisible
	 * @param orderTimeRange
	 * @return
	 */
	public int queryAllOrderFormListCount(long userId, Boolean isVisible, long[] orderTimeRange);

	/**
	 * 按照订单Id查询订单
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderFormDTO queryOrderFormByOrderId(long orderId);
	
    /**
     * 根据parentId获取订单列表
     * 
     * @param parentId
     * @return
     */
    public List<OrderFormDTO> queryOrderFormListByParentId(long parentId);

	/**
	 * 修改订单的支付方式为货到付款
	 * 
	 * @param userId
	 * @param orderId
	 * @return 2: 已经是COD<BR>
	 *         1: 成功<br>
	 *         0: 失败<br>
	 *         -1: TCC处理中<br>
	 *         -2: 参数错误(收货地址不存在)<br>
	 *         -3: 参数错误(订单已取消)<br>
	 *         -4: 不允许更新为COD<br>
	 *         -5: 参数错误(订单已支付)<br>
	 * 
	 */
	public int changePaymethodToCOD(long userId, long orderId);

	/**
	 * 1.更新订单状态为支付成功<br>
	 * 2.更新交易状态为支付成功(网易宝交易)<br>
	 * 
	 * @param param
	 * @return -2: 没有网易宝交易<br>
	 *         -1: 参数错误<br>
	 *         0: 失败<br>
	 *         1: 成功<br>
	 *         2: 成功(重复更新)
	 */
	public int setStateToEPayed(OrderServiceSetStateToEPayedParam param);

	/**
	 * 设置订单可见状态
	 * 
	 * @param userId
	 * @param orderId
	 * @param isVisible
	 * @return
	 */
	public boolean updateIsVisible(long userId, long orderId, boolean isVisible);

	/**
	 * 修改订单收货地址
	 * 
	 * @param orderId
	 * @param param
	 * @return
	 */
	public int updateOrderExpInfo(long orderId, long userId, OrderExpInfoChangeParam param);

	/**
	 * 获取订单取消信息
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public OrderCancelInfoDTO getOrderCancelInfo(long userId, long orderId);

	/**
	 * 获取订单取消信息记录集合
	 * 
	 * @param minOrderId
	 * @param cancelState
	 * @param param
	 * @return
	 */
	public List<OrderCancelInfoDTO> getOrderCancelInfoDTOList(long minOrderId, OrderCancelState cancelState,
			DDBParam param);

	/**
	 * 获取订单取消信息记录集合
	 * 
	 * @param minOrderId
	 * @param cancelState
	 * @param param
	 * @return RetArg.List<OrderCancelInfoDTO> RetArg.DDBParam
	 */
	public RetArg getOrderCancelInfoDTOListByRType(long minOrderId, OrderCancelRType cancelRType, DDBParam param);

	/**
	 * 更新OrderCancelInfoDTO上的retry参数
	 * 
	 * @param isUpdateRetryFlag
	 *            是否更新RetryFlag字段
	 * @param cancelDTO
	 * @param retryFlagOfOld
	 *            更新RetryFlag字段时,原始的RetryFlag字段值
	 * @return 1: 成功<br>
	 *         0: 失败<br>
	 *         -1: 参数不正确
	 */
	public int updateOrderCancelInfoOfRetryInfo(boolean isUpdateRetryFlag, OrderCancelInfoDTO cancelDTO,
			Long retryFlagOfOld);

	/**
	 * 根据订单取消记录,取消交易<br>
	 * 1. 取消订单对应的交易<br>
	 * 2. 更新OrderCancelInfoDTO.OrderCancelState
	 * 
	 * @param cancelDTO
	 * @return
	 */
	public boolean cancelTradeByOrderCancelInfo(OrderCancelInfoDTO cancelDTO);

	/**
	 * 设置retryFlag==0的取消任务状态为完成
	 * 
	 * @return
	 */
	public boolean setOrderCancelToDone();

	/**
	 * 更新OrderCancelRType
	 * 
	 * @param cancelDTO
	 * @param oldRTypes
	 * @param newRType
	 * @return
	 */
	public boolean setOrderCancelRType(OrderCancelInfoDTO cancelDTO, OrderCancelRType[] oldRTypes,
			OrderCancelRType newRType);

	/**
	 * 订单的收货地址是否可以更改
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	public boolean canOrderExpInfoBeChanged(OrderFormDTO ordFormDTO);
	
	
	/**
	 * 取消订单
	 * @param orderCancelInfoDTO
	 * @return
	 */
	public boolean cancelOrder(OrderCancelInfoDTO orderCancelInfoDTO);
	
	/**
	 * 交易完成
	 * @return
	 */
	public int setOrderFormStateToFinish(long orderId,long userId);
	
	/**
	 * 发货
	 */
	public int deliverGoods(OrderLogisticsDTO orderLogisticsDTO);
	
	/**
	 * 更新备注
	 * @param userId
	 * @param orderId
	 * @param businessId
	 * @param comment
	 * @return
	 */
	public boolean updateOrderFormCommnet(long userId, long orderId,long businessId, String comment);
	
	/**
	 * 查询订单操作日志
	 * @param operateLogDTO
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<OrderOperateLogDTO> queryOperateLog(OrderOperateLogDTO operateLogDTO, String startTime, String endTime);
	
	/**
	 * 更新订单信息：包含物流，发票，金额修改,收货地址
	 * @param orderFormDTO
	 * @return
	 */
	public boolean updateOrderForm(OrderFormDTO orderFormDTO);
	
	
	/**
	 * 新增发票
	 * @param invoiceDTO
	 * @return
	 */
	public int addInvoice(InvoiceDTO invoiceDTO);
	
	
	/**
	 * 更新发票
	 * @param invoiceDTO
	 * @return
	 */
	public int updateInvoice(InvoiceDTO invoiceDTO);
	
	/**
	 * 新增或者更新物流信息
	 * @param orderLogisticsDTO
	 * @return
	 */
	public int addOrUpdateOrderLogistics(OrderLogisticsDTO orderLogisticsDTO);
	
	/**
	 * 修改订单状态
	 * @param param
	 * @return
	 */
	public int modifyOrderFormState(OrderOperateParam param);
	
	/**
	 * 新增订单评论
	 * @param param
	 * @return
	 */
	public int addOrUpdateOrderCommnet(OrderOperateParam param);
	
	/**
	 * 记录订单操作日志
	 * @param dto
	 */
	public void addOrderOperateLog(OrderOperateLogDTO dto) throws ServiceException;
	
	/**
	 * 批量插入操作日志
	 * @param list
	 */
	public void addOrderOperateLogs(List<OrderOperateLogDTO> list) throws ServiceException;
	
	/**
	 * 根据parentId取订单Ids
	 * @param parentId
	 * @return
	 */
	public List<Long> getSubOrderIdsByParentId(long parentId);
	
	/**
	 * 获取一定数量的parentId
	 * @param parentId 获取到的parentId大于此parentId
	 * @param count 获取的数量
	 * @return
	 */
	public List<Long> getParentIds(long parentId, int count);
	
	/**
	 * 根据parentId获取订单列表
	 * @param parentId
	 * @param isVisible
	 * @return
	 */
    public List<OrderFormDTO> getOrderFormByParentId(long parentId, boolean isVisible);
	
	
	/**
	 * 查询某个用户，某些状态的订单
	 * 
	 * @param orderSearchParam
	 * @param param
	 * @return RetArg.ArrayList: List(OrderFormDTO)<br>
	 *         RetArg.DDBParam: 数据库查询参数
	 */
	public RetArg queryOrderFormListByOrderSearchParam(OrderSearchParam orderSearchParam);
	
	/**
	 * 根据订单Id取得同一parentId的订单个数
	 * @return
	 */
	public int countRelateOrderByorderId(long orderId);
	
	/**
	 * 获取商品快照
	 * @param userId
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	public OrderSkuDTO getOrderSku(long userId, long orderId, long skuId);
	
	/**
	 * 获取订单信息(orderform表)
	 * @param orderId
	 * @return
	 */
	public OrderForm getOrderForm(long orderId);
	
	/**
	 * 获取订单sku列表
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public List<OrderSku> getOrderSKUListByOrderIdAndUserId(long orderId, long userId);
}
