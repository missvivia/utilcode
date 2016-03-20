/**
 * 
 */
package com.xyl.mmall.order.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.meta.OrderForm;

/**
 * 成功下单后的订单DTO
 * 
 * @author dingmingliang
 * 
 */
public class OrderFormDTO extends OrderForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 收货人
	 */
	private String consignee;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 取消时间
	 */
	private long cancelTime;

	/**
	 * 取消原因
	 */
	private String cancelReason;

	/**
	 * 操作用户类型
	 */
	private OperateUserType operateUserType;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public OrderFormDTO(OrderForm obj) {
		ReflectUtil.convertObj(this, obj, false);
		extDTO = OrderFormExtInfoDTO.genOrderFormExtInfoDTOByOrder(this);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static OrderFormDTO genInstance(OrderForm obj) {
		if (obj == null)
			return null;
		return new OrderFormDTO(obj);
	}

	/**
	 * 构造函数
	 */
	public OrderFormDTO() {
	}

	/**
	 * 是否可以取消
	 */
	private boolean canCancel;

	/**
	 * 是否可以修改支付方式为COD
	 */
	private boolean canCOD;

	/**
	 * 订单下的所有包裹信息
	 */
	@Deprecated
	private List<OrderPackageDTO> orderPackageDTOList;

	/**
	 * 订单物流信息
	 */
	private List<OrderLogisticsDTO> orderLogisticsDTOs;

	/**
	 * 订单下所有的购物车对象
	 */
	private List<? extends OrderCartItemDTO> orderCartItemDTOList;

	/**
	 * 订单下商品数量
	 */
	private int skuCount;

	/**
	 * 订单上的赠品对象列表
	 */
	private List<OrderSkuDTO> orderSkuDTOListOfOrdGift;

	/**
	 * 订单快递地址信息
	 */
	private OrderExpInfoDTO orderExpInfoDTO;

	/**
	 * 买家申请的发票信息
	 */
	private InvoiceInOrdDTO invoiceInOrdDTO;

	/**
	 * 商家新增发票信息
	 */
	private List<InvoiceDTO> invoiceDTOs;

	/**
	 * 
	 */
	private OrderFormExtInfoDTO extDTO;

	/**
	 * 支付流水号 支付平台生成的
	 */
	private String payOrderSn;

	public OrderFormExtInfoDTO getExtDTO() {
		return extDTO;
	}

	public void setExtDTO(OrderFormExtInfoDTO extDTO) {
		this.extDTO = extDTO;
	}

	/**
	 * 获取订单名下的所有OrderSkuDTO
	 * 
	 * @return
	 */
	public List<OrderSkuDTO> getAllOrderSkuDTOList() {
		List<OrderSkuDTO> orderSkuDTOList = new ArrayList<>();
		for (OrderCartItemDTO orderCartItem : orderCartItemDTOList) {
			for (OrderSkuDTO orderSku : orderCartItem.getOrderSkuDTOList()) {
				orderSkuDTOList.add(orderSku);
			}
		}
		return orderSkuDTOList;
	}

	/**
	 * 按照OrderSku的主键映射
	 * 
	 * @return
	 */
	public Map<Long, OrderSkuDTO> mapOrderSkusByTheirId() {
		List<OrderSkuDTO> orderSkuDTOList = getAllOrderSkuDTOList();
		if (CollectionUtil.isEmptyOfCollection(orderSkuDTOList))
			return new TreeMap<Long, OrderSkuDTO>();

		return CollectionUtil.convertCollToMap(orderSkuDTOList, "id");
	}

	/**
	 * 按照OrderSku.skuId映射
	 * 
	 * @return
	 */
	public Map<Long, OrderSkuDTO> mapOrderSkusBySkuId() {
		List<OrderSkuDTO> orderSkuDTOList = getAllOrderSkuDTOList();
		if (CollectionUtil.isEmptyOfCollection(orderSkuDTOList))
			return new TreeMap<Long, OrderSkuDTO>();

		return CollectionUtil.convertCollToMap(orderSkuDTOList, "skuId");
	}

	/**
	 * 获取PO列表
	 * 
	 * @return
	 */
	public List<Long> extractPOIdList() {
		Set<Long> poIdSet = new HashSet<Long>();
		for (OrderCartItemDTO orderCartItem : orderCartItemDTOList) {
			for (OrderSkuDTO orderSku : orderCartItem.getOrderSkuDTOList()) {
				poIdSet.add(orderSku.getPoId());
			}
		}
		List<Long> ret = new ArrayList<Long>(poIdSet.size());
		for (Long poId : poIdSet) {
			ret.add(poId);
		}
		return ret;
	}

	/**
	 * key:poId, value:promotionId/promotionIdx
	 * 
	 * @return
	 */
	public Map<Long, Long> mapPromotionByPOId(boolean isPromotionIndex) {
		Map<Long, Long> ret = new HashMap<Long, Long>();
		for (OrderCartItemDTO orderCartItem : orderCartItemDTOList) {
			if (null == orderCartItem) {
				continue;
			}
			long promotionInfo = 0;
			if (isPromotionIndex) {
				promotionInfo = orderCartItem.getPromotionIdx();
			} else {
				promotionInfo = orderCartItem.getPromotionId();
			}
			for (OrderSkuDTO orderSku : orderCartItem.getOrderSkuDTOList()) {
				if (null == orderSku) {
					continue;
				}
				long poId = orderSku.getPoId();
				ret.put(poId, promotionInfo);
			}
		}
		return ret;
	}

	public boolean isCanCOD() {
		return canCOD;
	}

	public void setCanCOD(boolean canCOD) {
		this.canCOD = canCOD;
	}

	public InvoiceInOrdDTO getInvoiceInOrdDTO() {
		return invoiceInOrdDTO;
	}

	public void setInvoiceInOrdDTO(InvoiceInOrdDTO invoiceInOrdDTO) {
		this.invoiceInOrdDTO = invoiceInOrdDTO;
	}

	public boolean canCancel() {
		return canCancel;
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public List<OrderPackageDTO> getOrderPackageDTOList() {
		return orderPackageDTOList;
	}

	public void setOrderPackageDTOList(List<OrderPackageDTO> orderPackageDTOList) {
		this.orderPackageDTOList = orderPackageDTOList;
	}

	public List<? extends OrderCartItemDTO> getOrderCartItemDTOList() {
		return orderCartItemDTOList;
	}

	public void setOrderCartItemDTOList(List<? extends OrderCartItemDTO> orderCartItemDTOList) {
		this.orderCartItemDTOList = orderCartItemDTOList;
	}

	public List<OrderSkuDTO> getOrderSkuDTOListOfOrdGift() {
		return orderSkuDTOListOfOrdGift;
	}

	public void setOrderSkuDTOListOfOrdGift(List<OrderSkuDTO> orderSkuDTOListOfOrdGift) {
		this.orderSkuDTOListOfOrdGift = orderSkuDTOListOfOrdGift;
	}

	public OrderExpInfoDTO getOrderExpInfoDTO() {
		return orderExpInfoDTO;
	}

	public void setOrderExpInfoDTO(OrderExpInfoDTO orderExpInfoDTO) {
		this.orderExpInfoDTO = orderExpInfoDTO;
	}

	public int getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(int skuCount) {
		this.skuCount = skuCount;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<OrderLogisticsDTO> getOrderLogisticsDTOs() {
		return orderLogisticsDTOs;
	}

	public void setOrderLogisticsDTOs(List<OrderLogisticsDTO> orderLogisticsDTOs) {
		this.orderLogisticsDTOs = orderLogisticsDTOs;
	}

	public List<InvoiceDTO> getInvoiceDTOs() {
		return invoiceDTOs;
	}

	public void setInvoiceDTOs(List<InvoiceDTO> invoiceDTOs) {
		this.invoiceDTOs = invoiceDTOs;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public OperateUserType getOperateUserType() {
		return operateUserType;
	}

	public void setOperateUserType(OperateUserType operateUserType) {
		this.operateUserType = operateUserType;
	}

	public String getPayOrderSn() {
		return payOrderSn;
	}

	public void setPayOrderSn(String payOrderSn) {
		this.payOrderSn = payOrderSn;
	}

}
