package com.xyl.mmall.cms.vo.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatUtil;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.api.util.TradeApiUtil;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.TradeItem;

/**
 * 财务-订单.
 * 
 * @author wangfeng
 *
 */
public class FinanceOrderVO implements Serializable {

	private static final long serialVersionUID = 3214246297529438554L;

	/** 订单号. */
	private long orderId;

	/** 订单状态. */
	private String orderFormState;

	/** 订单省份. */
	private String orderProvince;

	/** 优惠描述. */
	private String couponDesc = "";

	/** 订单实付金额. */
	private BigDecimal userTotalPrice = BigDecimal.ZERO;

	/** 下单时间. */
	private String orderDate;

	/** 交易号. */
	private String tradeId = "";

	/** 收款渠道. */
	private String payMethodArr = "";

	/** 用户支付运费. */
	private BigDecimal expUserPrice = BigDecimal.ZERO;

	/** 七天无理由退货期已满. */
	private String qttk = "";

	private List<FinanceOrderPackageVO> financeOrderPackageList;

	private Set<Long> productIdSet;

	private Set<Long> poIdSet;

	public FinanceOrderVO() {
		super();
	}

	public FinanceOrderVO(OrderFormDTO dto, Map<Long, String> provinceCodeNameMap, List<TradeItemDTO> treadeItemList,
			String counponInfo) {
		super();
		this.orderId = dto.getOrderId();
		this.orderFormState = dto.getOrderFormState().getDesc();
		this.orderProvince = provinceCodeNameMap.get(Long.valueOf(dto.getProvinceId()));
		this.userTotalPrice = dto.getCartRPrice().add(dto.getExpUserPrice());
		this.orderDate = DateFormatUtil.getFormatDateType5(dto.getOrderTime());
		this.expUserPrice = dto.getExpUserPrice();
		this.couponDesc = counponInfo;
		// 交易相关信息
		setTradeItemInfo(treadeItemList);
		// 包裹相关
		financeOrderPackageList = new ArrayList<>();
		List<OrderPackageDTO> orderPackageDTOList = dto.getOrderPackageDTOList();
		if (CollectionUtil.isNotEmptyOfList(orderPackageDTOList)) {
			for (OrderPackageDTO orderPackageDTO : orderPackageDTOList) {
				FinanceOrderPackageVO financeOrderPackageVO = new FinanceOrderPackageVO(orderPackageDTO);
				financeOrderPackageList.add(financeOrderPackageVO);
			}
		}
		// 七天无理由退货期已满.
		long currentTime = System.currentTimeMillis();
		boolean isQttk = (currentTime - dto.getConfirmTime() - (7 * 24 * 60 * 60 * 1000L)) > 0L;
		if (isQttk && OrderFormState.FINISH_DELIVE == dto.getOrderFormState()) {
			this.qttk = "是";
		} else if (!isQttk && OrderFormState.FINISH_DELIVE == dto.getOrderFormState()) {
			this.qttk = "否";
		}
	}

	public void setTradeItemInfo(List<TradeItemDTO> treadeItemList) {
		StringBuilder payMethodBuilder = new StringBuilder(128);
		// 1.在线支付
		TradeItem tradeItemOfOnline = TradeApiUtil.getTradeOfOnlineAndPayed(treadeItemList);
		tradeItemOfOnline = tradeItemOfOnline != null ? tradeItemOfOnline : TradeApiUtil
				.getTradeOfOnlineAndRefund(treadeItemList);
		if (tradeItemOfOnline != null) {
			this.tradeId = String.valueOf(tradeItemOfOnline.getTradeId());
			payMethodBuilder.append(tradeItemOfOnline.getTradeItemPayMethod().getDesc() + ";");
		}
		// 2.货到付款
		PayState[] payStateArray = new PayState[] { PayState.COD_PAYED, PayState.COD_NOT_PAY };
		Map<PayState, List<TradeItem>> resultMap = TradeApiUtil.getTradeOfCOD(treadeItemList, payStateArray);
		if (CollectionUtil.isNotEmptyOfMap(resultMap)) {
			payMethodBuilder.append(TradeItemPayMethod.COD.getDesc() + ";");
		}

		this.payMethodArr = payMethodBuilder.toString();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderFormState() {
		return orderFormState;
	}

	public void setOrderFormState(String orderFormState) {
		this.orderFormState = orderFormState;
	}

	public String getOrderProvince() {
		return orderProvince;
	}

	public void setOrderProvince(String orderProvince) {
		this.orderProvince = orderProvince;
	}

	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}

	public BigDecimal getUserTotalPrice() {
		return userTotalPrice;
	}

	public void setUserTotalPrice(BigDecimal userTotalPrice) {
		this.userTotalPrice = userTotalPrice;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getPayMethodArr() {
		return payMethodArr;
	}

	public void setPayMethodArr(String payMethodArr) {
		this.payMethodArr = payMethodArr;
	}

	public BigDecimal getExpUserPrice() {
		return expUserPrice;
	}

	public void setExpUserPrice(BigDecimal expUserPrice) {
		this.expUserPrice = expUserPrice;
	}

	public String getQttk() {
		return qttk;
	}

	public void setQttk(String qttk) {
		this.qttk = qttk;
	}

	public List<FinanceOrderPackageVO> getFinanceOrderPackageList() {
		return financeOrderPackageList;
	}

	public void setFinanceOrderPackageList(List<FinanceOrderPackageVO> financeOrderPackageList) {
		this.financeOrderPackageList = financeOrderPackageList;
	}

	public Set<Long> getProductIdSet() {
		productIdSet = new HashSet<Long>();
		if (CollectionUtil.isNotEmptyOfList(financeOrderPackageList)) {
			for (FinanceOrderPackageVO financeOrderPackageVO : financeOrderPackageList) {
				List<FinanceOrderSkuVO> financeOrderSkuVOList = financeOrderPackageVO.getFinanceOrderSkuList();
				for (FinanceOrderSkuVO financeOrderSkuVO : financeOrderSkuVOList) {
					productIdSet.add(financeOrderSkuVO.getProductId());
				}
			}
		}
		return productIdSet;
	}

	public void setProductIdSet(Set<Long> productIdSet) {
		this.productIdSet = productIdSet;
	}

	public Set<Long> getPoIdSet() {
		poIdSet = new HashSet<Long>();
		if (CollectionUtil.isNotEmptyOfList(financeOrderPackageList)) {
			for (FinanceOrderPackageVO financeOrderPackageVO : financeOrderPackageList) {
				List<FinanceOrderSkuVO> financeOrderSkuVOList = financeOrderPackageVO.getFinanceOrderSkuList();
				for (FinanceOrderSkuVO financeOrderSkuVO : financeOrderSkuVOList) {
					poIdSet.add(financeOrderSkuVO.getPoId());
				}
			}
		}
		return poIdSet;
	}

	public void setPoIdSet(Set<Long> poIdSet) {
		this.poIdSet = poIdSet;
	}

}
