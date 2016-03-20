package com.xyl.mmall.cms.facade;

import java.util.List;

import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.cms.facade.param.FrontOrderExpInfoUpdateParam;
import com.xyl.mmall.cms.vo.OrderOperateLogVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.OrderCategory;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.TimeRangeCategory;
import com.xyl.mmall.cms.vo.order.CmsOrderManageQueryTypeListUtil.OrderQueryCategoryListVO.UserCategory;
import com.xyl.mmall.cms.vo.order.InvoiceListVO;
import com.xyl.mmall.cms.vo.order.OrderBriefInfoListVO;
import com.xyl.mmall.cms.vo.order.OrderDetailInfoVO;
import com.xyl.mmall.cms.vo.order.OrderPackageExpInfoVO;
import com.xyl.mmall.cms.vo.order.OrderPackageSkuInfoVO;
import com.xyl.mmall.cms.vo.order.TradeDetailInfoVO;
import com.xyl.mmall.cms.vo.order.TradeOrderInfoListVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.mainsite.vo.order.OrderFormVO;
import com.xyl.mmall.order.dto.InvoiceDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.param.OrderSearchParam;

/**
 * 订单管理：订单查询
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 上午9:11:18
 *
 */
public interface OrderQueryFacade {

	/**
	 * 获取Cms订单查询类型列表
	 * 
	 * @return
	 */
	public OrderQueryCategoryListVO getCmsOrderQueryTypeList();
	
	/**
	 * 交易详情
	 * 
	 * @param tradeId
	 * @param orderId
	 * @param userId
	 * @param  userName
	 * @return
	 */
	public TradeDetailInfoVO queryTrade(long tradeId, long orderId, long userId, String userName);
	
	/**
	 * 按用户信息查询
	 * 
	 * @param ut
	 * @param value
	 * @param limit
	 * @param offset
	 * @return
	 */
	public OrderBriefInfoListVO queryByUserInfo(UserCategory ut, String value, int limit, int offset);
	
	/**
	 * 按时间范围查询
	 * 
	 * @param tr
	 * @param start
	 * @param end
	 * @param limit
	 * @param offset
	 * @return
	 */
	public OrderBriefInfoListVO queryByTimeRange(TimeRangeCategory tr, long start, long end, int limit, int offset);
	
	/**
	 * CMS订单查询：订单是否存在
	 * 
	 * @param ott
	 * @param value
	 * @return
	 */
	public boolean orderExists(OrderCategory ott, String value);
	
	/**
	 * CMS订单详情：基本信息 + 交易信息
	 * 
	 * @param ott
	 * @param value
	 * @return
	 */
	public OrderDetailInfoVO queryOrderDetailInfo(OrderCategory ott, String value);
	
	/**
	 * CMS订单详情：交易支付记录相关的订单
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public TradeOrderInfoListVO queryTradeOrderInfo(long userId, long orderId);
	
	/**
	 * CMS订单详情：配送信息
	 * 
	 * @param ott
	 * @param value
	 * @return
	 */
	public OrderPackageExpInfoVO queryOrderPackageExpInfo(OrderCategory ott, String value);
	
	/**
	 * 商品信息
	 * 
	 * @param ott
	 * @param value
	 * @return
	 */
	public OrderPackageSkuInfoVO queryOrderPackageSkuInfo(OrderCategory ott, String value);
	
	/**
	 * 更新收货地址
	 * 
	 * @param param
	 * @return
	 */
	public int updateOrderExpInfo(FrontOrderExpInfoUpdateParam param);
	
	/**
	 * 取消订单
	 * 
	 * @param orderId
	 * @param userId
	 * @param rtype：取消后的金额返回方式(0:原路退回,1:网易宝)
	 * @return
	 */
	public boolean cancelOrder(long orderId, long userId, OrderCancelRType rtype);
	
	/**
	 * 补开发票
	 * 
	 * @param orderId
	 * @param userId
	 * @param title
	 * @param associated
	 * @return
	 */
	public boolean addInvoice(long orderId, long userId, String title, boolean associated);
	
	/**
	 * 修改发票
	 * 
	 * @param orderId
	 * @param userId
	 * @param title
	 * @param associated
	 * @return
	 */
	public boolean updateInvoice(long orderId, long userId, String title, boolean associated);
	
	/**
	 * 发票列表
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public InvoiceListVO getInvoiceList(long orderId, long userId);

	/**
	 * 客服重新开启退货
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public RetArg kfReopenReturn(long ordPkgId, long userId);
	
	/**
	 * 设置包裹状态：丢件
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public RetArg setPackageLost(long ordPkgId, long userId);
	
	/**
	 * 设置包裹状态：拒收
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public RetArg setPackageRefused(long ordPkgId, long userId);
	
	/**
	 * 设置包裹状态：取消
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public RetArg cancelPackage(long ordPkgId, long userId);
	
	/**
	 * 设置包裹状态：已签收
	 * 
	 * @param ordPkgId
	 * @param userId
	 * @return
	 */
	public RetArg consignPackage(long ordPkgId, long userId);
	
	/**
	 * 根据订单搜索条件分页取订单
	 * @param searchParam
	 * @return
	 */
	public BasePageParamVO<OrderFormVO> searchOrderForm(OrderSearchParam searchParam);
	
	
	public RetArg queryOrderFormByOrderId(long orderId);
	
	
	/**
	 * 商家后台新增发票
	 * 
	 * @param orderId
	 * @param userId
	 * @param businesssId
	 * @param invoiceNo
	 * @param operator
	 * @return
	 */
	public boolean addInvoiceInBusiness(long orderId, long userId, long businesssId,String invoiceNo,long operator);
	
	/**
	 * 商家后台更新发票状态状态
	 * @param invoiceId
	 * @param businessId
	 * @param state
	 * @return
	 */
	public boolean updateInvoiceStateInBusiness(InvoiceDTO invoiceDTO);
	
	/**
	 * 查询订单操作日志
	 * @param operateLogDTO
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<OrderOperateLogVO> queryOperateLog(OrderOperateLogDTO operateLogDTO, String startTime, String endTime);
	
	
	/**
	 * 根据订单搜索条件查询
	 * 
	 * @param searchParam
	 * @return
	 */
	public OrderBriefInfoListVO queryOrderByOrderSearchParam(OrderSearchParam searchParam);
	
	/**
	 * 根据订单Id查看订单信息
	 * @param orderId
	 * @return
	 */
	public OrderDetailInfoVO queryOrderDetailInfoByOrderId(long orderId);
	
	/**
	 * 获取限购商品订单列表
	 * @param searchParam
	 * @param skuId
	 * @return RetArg
	 */
	public RetArg querySKULimitOrderList(OrderSearchParam searchParam, long skuId);
	
	/**
	 * 获取订单sku信息
	 * @param skuId
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public OrderSkuDTO getOrderSKU(long skuId, long orderId, long userId);
}
