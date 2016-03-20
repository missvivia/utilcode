package com.xyl.mmall.common.facade;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.netease.print.common.meta.RetArg;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
import com.xyl.mmall.common.enums.OrderQueryType;
import com.xyl.mmall.common.param.OrderFacadeComposeOrderParam;
import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.order.dto.CancelOmsOrderTaskDTO;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderCartItemBriefDTO;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderLogisticsDTO;
import com.xyl.mmall.order.dto.OrderPackageBriefDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.param.OrderOperateParam;
import com.xyl.mmall.order.param.OrderSearchParam;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.saleschedule.dto.PODTO;

/**
 * 订单相关的服务
 * 
 * @author dingmingliang
 * 
 */
public interface OrderFacade {

	/**
	 * 组单
	 * 
	 * @param param
	 * @return RetArg.OrderCalServiceGenOrderResult<br>
	 *         RetArg.FavorCaculateResultDTO
	 */
	public RetArg composeOrder(OrderFacadeComposeOrderParam param, PlatformType platformtype);

	/**
	 * 取消订单(不通知oms取消订单)
	 * 
	 * @param cancelDTO
	 * @return RetArg.OrderFormDTO<br>
	 *         RetArg.Boolean: 是否成功<br>
	 *         RetArg.BigDecimal: 在线支付退款金额
	 */
	public RetArg cancelOrder(OrderCancelInfoDTO cancelDTO);

	/**
	 * 取消订单(不通知oms取消订单)
	 * 
	 * @param cancelDTO
	 * @param isPassCanCancelFlag
	 *            是否忽略canCancel标记位的判断
	 * @return RetArg.OrderFormDTO<br>
	 *         RetArg.Boolean: 是否成功<br>
	 *         RetArg.BigDecimal: 在线支付退款金额
	 */
	//public RetArg cancelOrder(OrderCancelInfoDTO cancelDTO, boolean isPassCanCancelFlag);

	/**
	 * 取消订单(不通知oms取消订单)
	 * 
	 * @param cancelTaskDTO
	 * @return RetArg.OrderFormDTO<br>
	 *         RetArg.Boolean: 是否成功<br>
	 *         RetArg.BigDecimal: 在线支付退款金额
	 */
	public RetArg cancelOrder(CancelOmsOrderTaskDTO cancelTaskDTO);

	/**
	 * 设置订单状态为 WAITING_CANCEL_OMSORDER 之前的值
	 * 
	 * @param userId
	 * @param orderId
	 * @param newState
	 * @return
	 */
	public boolean setOrderFormToCancelRevert(CancelOmsOrderTaskDTO cancelTaskDTO);

	/**
	 * 添加取消oms订单的任务
	 * 
	 * @param cancelTaskDTO
	 * @return RetArg.OrderFormDTO<br>
	 *         RetArg.Boolean: 是否成功<br>
	 */
	public RetArg addCancelOmsOrderTask(CancelOmsOrderTaskDTO cancelTaskDTO);

	/**
	 * 取消交易
	 * 
	 * @param cancelDTO
	 * @return
	 */
	public boolean cancelTrade(OrderCancelInfoDTO cancelDTO);

	/**
	 * 取消OMS订单<br>
	 * 1.通知oms取消订单(兼容不需要取消的情况)<br>
	 * 2.更新OrderCancelInfo的字段信息
	 * 
	 * @param cancelDTO
	 * @return
	 */
	public boolean cancelOmsOrder(OrderCancelInfoDTO cancelDTO);

	/**
	 * 取消OMS订单<br>
	 * 1.通知oms取消订单(兼容不需要取消的情况)<br>
	 * 2.更新CancelOmsOrderTask的字段信息
	 * 
	 * @param cancelTaskDTO
	 * @return RetArg.Boolean: 是否取消成功<br>
	 *         RetArg.CancelOmsOrderTaskDTO
	 */
	public RetArg cancelOmsOrder(CancelOmsOrderTaskDTO cancelTaskDTO);

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
	 */
	public int changePaymethodToCOD(long userId, long orderId);

	/**
	 * 下单
	 * 
	 * @param orderCalDTO
	 * @param fcResultDTO
	 * @param cartIds
	 * @return RetArg.Boolean: 是否成功
	 */
	public RetArg makeOrder(OrderFormCalDTO orderCalDTO, FavorCaculateResultDTO fcResultDTO, String cartIds);
	
    /**
     * 新下单接口
     * 
     * @param orderCalDTO
     * @param fcResultDTO
     * @param cartIds
     * @return RetArg.Boolean: 是否成功
     */
    public RetArg makeMmallOrder(Map<Long, OrderFormCalDTO> orderFormCalDTOMap,
            Map<Long, FavorCaculateResultDTO> fcResultDTO, Map<Long, StringBuilder> businessIds);

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
	 * 查询订单信息
	 * 
	 * @param userId
	 * @param queryType
	 * @param search
	 * @param param
	 * @param isGetStoreInfo
	 * @return RetArg.List(OrderFormDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg queryOrderList(long userId, OrderQueryType queryType, String search, DDBParam param,boolean isGetStoreInfo);

	/**
	 * 查询我的订单Tab计数
	 * 
	 * @param userId
	 * @return
	 */
	public Map<OrderQueryType, Integer> queryOrderListCount(long userId);

	/**
	 * 查询订单信息
	 * 
	 * @param userId
	 * @param queryType
	 * @param search
	 * @param orderTimeRange
	 *            下单的时间范围
	 * @param param
	 * @return RetArg.List(OrderFormDTO)<br>
	 *         RetArg.DDBParam
	 */
	public RetArg queryOrderList(long userId, OrderQueryType queryType, String search, long[] orderTimeRange,
			DDBParam param);

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
	 * 查询订单信息
	 * 
	 * @param userId
	 * @param orderId
	 * @param isVisible
	 * @return
	 */
	public OrderFormBriefDTO queryOrderFormBriefDTO(long userId, long orderId, Boolean isVisible);
	
    /**
     * 根据userid和parentid获取订单信息列表
     * 
     * @param userId
     * @param parentId
     * @param isVisible
     * @return
     */
    public List<OrderFormBriefDTO> queryOrderFormBriefDTOList(long userId, long parentId,
            Boolean isVisible);

	/**
	 * 获得指定SkuId的库存
	 * 
	 * @param skuIdColl
	 * @return
	 */
	public List<SkuOrderStockDTO> getSkuOrderStockDTOListBySkuIds(Collection<Long> skuIdColl);

	/**
	 * 查询订单的优惠券
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public Coupon getCoupon(long userId, long orderId);

	/**
	 * 获取PO列表
	 * 
	 * @param ordForm
	 * @return
	 */
	@Deprecated
	public List<PODTO> getPOList(OrderFormDTO ordForm);

	/**
	 * 获取销售站点列表
	 * 
	 * @param ordForm
	 * @return
	 */
	@Deprecated
	public List<Long> getSaleAreaIdList(OrderFormDTO ordForm);

	/**
	 * 获取product列表
	 * 
	 * @param ordForm
	 * @return
	 */
	public Map<Long, POProductDTO> getProducts(OrderFormDTO ordForm);

	/**
	 * 读取订单明细
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public List<OrderCartItemBriefDTO> queryOrderCartItemBriefDTOList(long userId, long orderId);

	/**
	 * 获取订单包裹
	 * 
	 * @param userId
	 * @param packageId
	 * @return
	 */
	@Deprecated
	public OrderPackageBriefDTO queryOrderPackageBriefDTO(long userId, long packageId);

	/**
	 * 从订单中获取Promotion，填充PromotionDTO.index字段
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	public Map<Long, PromotionDTO> extractPromotion(OrderFormDTO ordFormDTO);
	
	/**
	 * 确认收货
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public int confirmOrder(long orderId,long userId);
	
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
	 * @param orderLogistics
	 * @return
	 */
	public int addOrUpdateOrderLogistics(OrderLogisticsDTO orderLogisticsDTO);
	
	
	/**
	 * 修改订单状态
	 * @param param
	 * @return
	 */
	public int modifyOrderState(OrderOperateParam param);
	
	/**
	 * 更新收货地址
	 * 
	 * @param param
	 * @return
	 */
	public int updateOrderExpInfo(FrontOrderExpInfoUpdateParam param);
	
	
	/**
	 * 新增备注
	 * @param param
	 * @return
	 */
	public int addOrUpdateOrderFormComment(OrderOperateParam param);
	
	
	/**
	 * 查询订单信息
	 * @param userId
	 * @param orderId
	 * @param isVisible
	 * @return
	 */
	public RetArg queryOrderFormByOrderIdAndUserId(long userId, long orderId, Boolean isVisible);
	
	/**
	 * 取子订单Ids
	 * @return
	 */
	public List<Long> getSubOrderIds(long parentId);
	
	/**
	 * 获取一定数量的parentId
	 * @param parentId 获取到的parentId大于此parentId
	 * @param count 获取的数量
	 * @return
	 */
	public List<Long> getParentIds(long parentId, int count);
	
	/**
	 * 根据parentId获取子订单列表
	 * @param parentId
	 * @param isVisible
	 * @return
	 */
	public List<OrderFormDTO> queryOrderFormListByParentId(long parentId, boolean isVisible);
	
	/**
	 * 取消相关订单
	 * @param cancelDTOs
	 * @return
	 */
	public RetArg cancelOrders(List<OrderCancelInfoDTO> cancelDTOs);
	
	public RetArg queryNewOrderList(OrderSearchParam orderSearchParam);
	
	/**
	 * 获取快照
	 * @param userId
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	public OrderSkuDTO getSkuSnapShot(long userId, long orderId, long skuId);
}
