package com.xyl.mmall.erp.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cms.vo.order.OrderBasicInfoVO;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.mainsite.vo.order.InvoiceInOrdVO;
import com.xyl.mmall.mainsite.vo.order.OrderExpInfoVO;
import com.xyl.mmall.mainsite.vo.order.OrderLogisticsVO;
import com.xyl.mmall.order.api.util.TradeApiUtil;
import com.xyl.mmall.order.dto.InvoiceInOrdDTO;
import com.xyl.mmall.order.dto.InvoiceInOrdSupplierDTO;
import com.xyl.mmall.order.dto.OrderExpInfoDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.InvoiceInOrdSupplierState;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.meta.OrderForm;
import com.xyl.mmall.order.meta.TradeItem;

/**
 * OrderDetailInfoErpVO.java created by yydx811 at 2015年11月2日 下午4:47:03
 * erp订单详情，与cms相同
 *
 * @author yydx811
 */
public class OrderDetailInfoErpVO {

	/**
	 * 订单交易信息
	 */
	private List<TradeInfo> tradeList = new ArrayList<TradeInfo>();
	
	/**
	 * 订单基本信息
	 */
	private OrderBasicInfoVO basicInfo = new OrderBasicInfoVO();
	
	/**
	 * 收货地址
	 */
	private OrderExpInfoVO orderExpInfoVO;
	
	/**
	 * 发票
	 */
	private List<InvoiceInOrdVO>invoiceInOrdVOs;
	
	/**
	 * 物流
	 */
	private List<OrderLogisticsVO> orderLogisticsVOs; 
	
	/**
	 * 订单明细列表
	 */
	private List<OrderCartItemErpVO> cartList;
	
	/**
	 * 备注
	 */
	private String comment;
	
	@Deprecated
	private DeleverInfo deliveryInfo = new DeleverInfo();
	
	@Deprecated
	private InvoiceInfo invoce = new InvoiceInfo();

	public void fillTradeList(OrderForm ordForm, List<TradeItemDTO> tradeItemDTOList, 
			TradeItemDTO hbFakeTradeItem) {
		if(null == ordForm || CollectionUtil.isEmptyOfList(tradeItemDTOList)) {
			return;
		}
		
		OrderFormPayMethod payMethod = ordForm.getOrderFormPayMethod();
		
		List<TradeItem> filteredTradeItemList = new ArrayList<TradeItem>();
		
		TradeItem trade = null;

		if(payMethod == OrderFormPayMethod.COD) {
			PayState[] states = new PayState[] {
					PayState.COD_NOT_PAY, PayState.COD_REFUSE_PAY, PayState.COD_PAYED, PayState.COD_CLOSE
			};
			Map<PayState, List<TradeItem>> tradeMap = TradeApiUtil.getTradeOfCOD(tradeItemDTOList, states);
			if(!CollectionUtil.isEmptyOfMap(tradeMap)) {
				for(Entry<PayState, List<TradeItem>> entry : tradeMap.entrySet()) {
					List<TradeItem> payStateTrades = null;
					if(null == entry || CollectionUtil.isEmptyOfList(payStateTrades = entry.getValue())) {
						continue;
					}
					boolean hit = false;
					for(TradeItem tradeItem : payStateTrades) {
						if(null == tradeItem) {
							continue;
						}
						trade = tradeItem;
						hit = true;
						break;
					}
					if(hit) {
						break;
					}
				}
			}
		} else if(OrderFormPayMethod.isOnlinePayMethod(payMethod)) {
			trade = TradeApiUtil.getTradeOfOnlineAndPayed(tradeItemDTOList);
			if(null == trade) {
				trade = TradeApiUtil.getTradeOfOnlineAndUnpay(tradeItemDTOList);
			}
			if(null == trade) {
				trade = TradeApiUtil.getTradeOfOnlineAndRefund(tradeItemDTOList);
			}
		} else {
			
		}
		
		if(null != trade) {
			filteredTradeItemList.add(trade);
		} else {
			for(TradeItemDTO tradeItem : tradeItemDTOList) {
				if(null == tradeItem) {
					continue;
				}
				filteredTradeItemList.add(tradeItem);
			}
		}
	
		if(null != hbFakeTradeItem) {
			filteredTradeItemList.add(hbFakeTradeItem);
		}
		
		BigDecimal totalCash = BigDecimal.ZERO;
		for(TradeItem tradeItem : filteredTradeItemList) {
			if(null == tradeItem) {
				continue;
			}
			BigDecimal cash = tradeItem.getCash();
			if(null == cash) {
				continue;
			}
			totalCash = totalCash.add(cash);
		}
		double totalCashDouble = totalCash.doubleValue();
		
		for(TradeItem tradeItem : filteredTradeItemList) {
			if(null == tradeItem) {
				continue;
			}
			TradeInfo ti = new TradeInfo();
			double pricePercentage = 0;
			BigDecimal cash = tradeItem.getCash();
			// ugly code: totalCashDouble > 0
			if(null != cash && totalCashDouble > 0) {
				pricePercentage = cash.doubleValue() / totalCashDouble;
			}
			ti.fillWithTradeItem(tradeItem, pricePercentage);
			// ugly code: 是否是hbFakeTradeItem
			if(-1 == tradeItem.getTradeId() && null == tradeItem.getTradeItemPayMethod()) {
				BigDecimal expHbPrice = ordForm.getExpUserPriceOfRed();
				if(null == expHbPrice) {
					expHbPrice = BigDecimal.ZERO;
				} 
				ti.getPartPriceDetail().put("抵扣运费", expHbPrice);
				BigDecimal ordHbPrice = ordForm.getRedCash();
				if(null != ordHbPrice && ordHbPrice.compareTo(expHbPrice) > 0) {
					ordHbPrice = new BigDecimal(ordHbPrice.doubleValue() - expHbPrice.doubleValue()).setScale(2, RoundingMode.HALF_UP);
				} else {
					ordHbPrice = BigDecimal.ZERO;
				}
				ti.getPartPriceDetail().put("抵扣商品", ordHbPrice);
			}
			tradeList.add(ti);
		}
	}
	
	
	
	public List<InvoiceInOrdVO> getInvoiceInOrdVOs() {
		return invoiceInOrdVOs;
	}



	public void setInvoiceInOrdVOs(List<InvoiceInOrdVO> invoiceInOrdVOs) {
		this.invoiceInOrdVOs = invoiceInOrdVOs;
	}



	public List<OrderLogisticsVO> getOrderLogisticsVOs() {
		return orderLogisticsVOs;
	}



	public void setOrderLogisticsVOs(List<OrderLogisticsVO> orderLogisticsVOs) {
		this.orderLogisticsVOs = orderLogisticsVOs;
	}



	public List<OrderCartItemErpVO> getCartList() {
		return cartList;
	}



	public void setCartList(List<OrderCartItemErpVO> cartList) {
		this.cartList = cartList;
	}



	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}



	public OrderExpInfoVO getOrderExpInfoVO() {
		return orderExpInfoVO;
	}



	public void setOrderExpInfoVO(OrderExpInfoVO orderExpInfoVO) {
		this.orderExpInfoVO = orderExpInfoVO;
	}



	public List<TradeInfo> getTradeList() {
		return tradeList;
	}

	public void setTradeList(List<TradeInfo> tradeList) {
		this.tradeList = tradeList;
	}

	public OrderBasicInfoVO getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(OrderBasicInfoVO basicInfo) {
		this.basicInfo = basicInfo;
	}

	public DeleverInfo getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(DeleverInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}	
	
	public InvoiceInfo getInvoce() {
		return invoce;
	}

	public void setInvoce(InvoiceInfo invoce) {
		this.invoce = invoce;
	}

	public static class TradeInfo {
		public static class TradeItemPayMethodVO {
			private int intValue;
			private String desc;
			private TradeItemPayMethodVO(int value, String desc) {
				this.intValue = value;
				this.desc = desc;
			}
			public int getIntValue() {
				return intValue;
			}
			public void setIntValue(int value) {
				this.intValue = value;
			}
			public String getDesc() {
				return desc;
			}
			public void setDesc(String desc) {
				this.desc = desc;
			}
		}
		private String tradeID;
		private BigDecimal deal;
		private TradeItemPayMethodVO method;
		private String percentage;
		private String bankSD;
		private Map<String, BigDecimal> partPriceDetail = new HashMap<String, BigDecimal>();
		public void fillWithTradeItem(TradeItem tradeItem, double pricePercentage) {
			if(null == tradeItem) {
				return;
			}
			this.tradeID = String.valueOf(tradeItem.getTradeId());
			this.deal = tradeItem.getCash();
			if(null != deal) {
				deal = deal.setScale(2, RoundingMode.HALF_UP);
			}
			TradeItemPayMethod tipm = tradeItem.getTradeItemPayMethod();
			// ugly code: 红包
			if(null == tipm) {
				this.method = new TradeItemPayMethodVO(-1, "红包");
			} else {
				this.method = new TradeItemPayMethodVO(tipm.getIntValue(), tipm.getDesc());
			}
			NumberFormat nf = NumberFormat.getPercentInstance(); 
			nf.setMinimumFractionDigits(1);	// 小数点后保留几位
			this.percentage = nf.format(pricePercentage);
			this.bankSD = tradeItem.getOrderSn();
		}
		public String getTradeID() {
			return tradeID;
		}
		public void setTradeID(String tradeID) {
			this.tradeID = tradeID;
		}
		public BigDecimal getDeal() {
			return deal;
		}
		public void setDeal(BigDecimal deal) {
			this.deal = deal;
		}
		public TradeItemPayMethodVO getMethod() {
			return method;
		}
		public void setMethod(TradeItemPayMethodVO method) {
			this.method = method;
		}
		public String getPercentage() {
			return percentage;
		}
		public void setPercentage(String percentage) {
			this.percentage = percentage;
		}
		public String getBankSD() {
			return bankSD;
		}
		public void setBankSD(String bankSD) {
			this.bankSD = bankSD;
		}
		public Map<String, BigDecimal> getPartPriceDetail() {
			return partPriceDetail;
		}
		public void setPartPriceDetail(Map<String, BigDecimal> partPriceDetail) {
			this.partPriceDetail = partPriceDetail;
		}
	}

	public static class DeleverInfo {
		private String name; //"xxx",
		private String mobile; //150000000,
		private String phone; //"0571-12323432",
		private String province;
		private String city;
		private String section;
		private String street;
		private String address; //"浙江杭州xxxxx"
		private boolean canReSet;
		public void fillWithOrderExpInfo(OrderExpInfoDTO orderExpInfo, boolean canReSet) {
			if(null == orderExpInfo) {
				return;
			}
			this.name = orderExpInfo.getConsigneeName();
			this.mobile = orderExpInfo.getConsigneeMobile();
			this.phone = orderExpInfo.getConsigneeTel();
			this.province = orderExpInfo.getProvince();
			this.city = orderExpInfo.getCity();
			this.section = orderExpInfo.getSection();
			this.street = orderExpInfo.getStreet();
			this.address = orderExpInfo.getAddress();
			this.canReSet = canReSet;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getSection() {
			return section;
		}
		public void setSection(String section) {
			this.section = section;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public boolean isCanReSet() {
			return canReSet;
		}
		public void setCanReSet(boolean canReSet) {
			this.canReSet = canReSet;
		}
	}

	public static class InvoiceInfo {

		// 是否开过发票
		private boolean hasInvoice = false;
		
		// 发票状态：是否已经开出
		private boolean yiKaiPiao = false;
		
		// 发票抬头
		private String title;
		
		public void fillInvoice(boolean hasInvoice, InvoiceInOrdDTO ordInvoice, List<InvoiceInOrdSupplierDTO> invoiceList) {
			this.hasInvoice = hasInvoice;
			if(!hasInvoice) {
				return;
			}
			if(null != ordInvoice) {
				this.title = ordInvoice.getTitle();
			}
			if(null != invoiceList) {
				for(InvoiceInOrdSupplierDTO invoice : invoiceList) {
					InvoiceInOrdSupplierState state = null;
					if(null == invoice || null == (state = invoice.getState())) {
						continue;
					}
					if(state == InvoiceInOrdSupplierState.KP_ED) {
						yiKaiPiao = true;
						break;
					}
				}
			}
		}
		
		public boolean isHasInvoice() {
			return hasInvoice;
		}

		public void setHasInvoice(boolean hasInvoice) {
			this.hasInvoice = hasInvoice;
		}

		public boolean isYiKaiPiao() {
			return yiKaiPiao;
		}

		public void setYiKaiPiao(boolean opened) {
			this.yiKaiPiao = opened;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}


	}
}
