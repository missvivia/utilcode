package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.DateFormatEnum;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageRefundDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderCancelRType;
import com.xyl.mmall.order.enums.OrderCancelSource;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * CMS订单详情：基本信息
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月24日 下午4:28:04
 *
 */
public class OrderBasicInfoVO {

	// 1. 用户信息
	private String userId;

	// 用户名
	private String userName;

	// 昵称
	private String nickName;

	// 商家Id
	private long businessId;

	// 商家账号
	private String businessAccount;

	// 商家账号
	private String storeName;

	// 2. 订单信息
	private String orderId;

	// 订单parentId
	private long parentId;

	private OrderFormState status; // "订单状态"

	private OrderFormPayMethod method; // "订单支付方式"

	private boolean isPayed;

	private long bookTime; // 下单时间

	private long payTime; // 支付时间

	private long deliverTime;// 发货时间

	private long confirmTime;// 确认收货时间即交易结束时间

	private long cancelTime;// 取消时间

	private long payCloseCD = 0;// 付款剩余时间

	private BigDecimal price = BigDecimal.ZERO; // 订单商品原价

	private BigDecimal orderPay = BigDecimal.ZERO; // 订单实付金额

	private String cancelReason;// 订单取消原因

	private OrderCancelSource cancelSource;// 取消来源

	private String title; // 买家申请的发票台头

	// 3. 优惠券 + 活动信息
	private CouponVO coupon; // 优惠券

	// 优惠价格
	private BigDecimal couponSPrice;

	// 交易号
	private String payOrderSn;

	// 区域Id
	private long districtId;

	// 代客下单账号
	private String proxyAccount;

	// 下面的属性暂时没用到

	private BigDecimal expressPrice = BigDecimal.ZERO; // 快递金额(用户支付)

	private BigDecimal sysExprePay = BigDecimal.ZERO; // 女装支付快递

	private BigDecimal discountPrice = BigDecimal.ZERO; // 商品折扣金额

	private int number; // 商品总数

	private OrderFormSource orderChannel; // 订单渠道

	private String saleAire; // 销售站点

	private String storage; // 发货仓库

	private List<OrderReturnPriceVO> orderReturn = new ArrayList<OrderReturnPriceVO>(); // 订单退款

	private List<PromotionVO> platformAct = new ArrayList<PromotionVO>(); // 平台促销活动

	// 4. 订单是否可以取消
	private boolean canCancel;

	private void beautifyBigDecimal() {
		if (null != price) {
			price = price.setScale(2, RoundingMode.HALF_UP);
		}
		if (null != expressPrice) {
			expressPrice = expressPrice.setScale(2, RoundingMode.HALF_UP);
		}
		if (null != sysExprePay) {
			sysExprePay = sysExprePay.setScale(2, RoundingMode.HALF_UP);
		}
		if (null != orderPay) {
			orderPay = orderPay.setScale(2, RoundingMode.HALF_UP);
		}
		if (null != discountPrice) {
			discountPrice = discountPrice.setScale(2, RoundingMode.HALF_UP);
		}
	}

	public void fillUserInfo(long userId, String userName, String nickName) {
		this.userId = String.valueOf(userId);
		this.userName = userName;
		this.nickName = nickName;
	}

	public void fillBusinessInfo(BusinessDTO businessDTO) {
		this.businessAccount = businessDTO.getBusinessAccount();
		this.storeName = businessDTO.getStoreName();
	}

	public void fillOrderInfo(OrderFormDTO ordDTO, OrderCancelInfoDTO cancelInfo) {
		if (null == ordDTO) {
			return;
		}
		orderId = String.valueOf(ordDTO.getOrderId());
		parentId = ordDTO.getParentId();
		businessId = ordDTO.getBusinessId();
		title = ordDTO.getInvoiceInOrdDTO() != null ? ordDTO.getInvoiceInOrdDTO().getTitle() : null;
		long currTime = System.currentTimeMillis();
		status = ordDTO.getOrderFormState();
		if (ordDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME > currTime
				&& ordDTO.getOrderFormState() == OrderFormState.WAITING_PAY) {
			payCloseCD = ordDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME - currTime;
			payCloseCD = payCloseCD <= 0 ? 0 : payCloseCD;
		}
		if (payCloseCD <= 0 && ordDTO.getOrderFormState() == OrderFormState.WAITING_PAY) {
			status = OrderFormState.CANCEL_ED;
		}
		method = ordDTO.getOrderFormPayMethod();
		if (OrderFormPayMethod.COD == method) {
			isPayed = false;
		} else {
			isPayed = ordDTO.getPayState() == PayState.ONLINE_PAYED;
		}

		bookTime = ordDTO.getOrderTime();
		{
			PayState payState = ordDTO.getPayState();
			if (null != payState && payState != PayState.ONLINE_NOT_PAY && payState != PayState.COD_NOT_PAY
					&& payState != PayState.COD_REFUSE_PAY && payState != PayState.ONLINE_CHANGE) {
				payTime = ordDTO.getPayTime();
			}
		}
		deliverTime = ordDTO.getOmsTime();
		confirmTime = ordDTO.getConfirmTime();
		if (cancelInfo != null) {
			cancelTime = cancelInfo.getCtime();
		}
		price = ordDTO.getCartOriRPrice();
		expressPrice = ordDTO.getExpUserPrice();
		sysExprePay = ordDTO.getExpSysPayPrice();
		orderPay = ordDTO.getCartRPrice();
		double totalPrice = (null == price) ? 0 : price.doubleValue();
		double payPrice = (null == orderPay) ? 0 : orderPay.doubleValue();
		double discount = totalPrice - payPrice;
		if (discount < 0) {
			discount = 0;
		}
		discountPrice = new BigDecimal(discount);
		if (null != orderPay && null != expressPrice) {
			orderPay = orderPay.add(expressPrice);
		}
		number = ordDTO.getSkuCount();
		orderChannel = ordDTO.getOrderFormSource();
		beautifyBigDecimal();
		if (cancelInfo != null) {
			cancelReason = cancelInfo.getReason();
			cancelSource = cancelInfo.getCancelSource();
		}
		districtId = ordDTO.getProvinceId();
		payOrderSn = ordDTO.getPayOrderSn();
	}

	@Deprecated
	public void fillOrderInfo(OrderFormDTO ordDTO, OrderCancelInfoDTO cancelInfo, List<AreaDTO> areaList,
			List<String> warehouseList, List<ReturnPackageDTO> retPkgList, boolean canBeCancelled) {
		if (null == ordDTO) {
			return;
		}
		orderId = String.valueOf(ordDTO.getOrderId());
		status = ordDTO.getOrderFormState();
		method = ordDTO.getOrderFormPayMethod();
		if (OrderFormPayMethod.COD == method) {
			isPayed = false;
		} else {
			isPayed = ordDTO.getPayState() == PayState.ONLINE_PAYED;
		}

		bookTime = ordDTO.getOrderTime();
		{
			PayState payState = ordDTO.getPayState();
			if (null != payState && payState != PayState.ONLINE_NOT_PAY && payState != PayState.COD_NOT_PAY
					&& payState != PayState.COD_REFUSE_PAY && payState != PayState.ONLINE_CHANGE) {
				payTime = ordDTO.getPayTime();
			}
		}
		price = ordDTO.getCartOriRPrice();
		expressPrice = ordDTO.getExpUserPrice();
		sysExprePay = ordDTO.getExpSysPayPrice();
		orderPay = ordDTO.getCartRPrice();
		double totalPrice = (null == price) ? 0 : price.doubleValue();
		double payPrice = (null == orderPay) ? 0 : orderPay.doubleValue();
		double discount = totalPrice - payPrice;
		if (discount < 0) {
			discount = 0;
		}
		discountPrice = new BigDecimal(discount);
		if (null != orderPay && null != expressPrice) {
			orderPay = orderPay.add(expressPrice);
		}
		number = ordDTO.getSkuCount();
		orderChannel = ordDTO.getOrderFormSource();
		StringBuffer strBuf = new StringBuffer();
		saleAire = "";
		if (!CollectionUtil.isEmptyOfList(areaList)) {
			for (AreaDTO area : areaList) {
				if (null == area) {
					continue;
				}
				strBuf.append(area.getAreaName()).append("; ");
			}
			saleAire = strBuf.toString();
		}
		storage = "";
		if (!CollectionUtil.isEmptyOfList(warehouseList)) {
			strBuf.delete(0, strBuf.length());
			for (String whForm : warehouseList) {
				if (null == whForm) {
					continue;
				}
				strBuf.append(whForm).append("; ");
			}
			storage = strBuf.toString();
		}
		orderReturn = composeOrderReturnPrice(ordDTO, cancelInfo, retPkgList);
		canCancel = canBeCancelled;

		beautifyBigDecimal();
	}

	public void fillCouponPromotionInfo(CouponOrder couponOrder, Coupon _coupon, PromotionDTO orderProm,
			Map<Long, PromotionDTO> poProm) {
		coupon = (new CouponVO()).fillWithCouponOrder(couponOrder, _coupon);
		if (null != orderProm) {
			platformAct.add((new PromotionVO()).fillWithPromotion(orderProm));
			return;
		}
		if (null != poProm) {
			for (Entry<Long, PromotionDTO> entry : poProm.entrySet()) {
				PromotionDTO prom = entry.getValue();
				if (null == prom) {
					continue;
				}
				platformAct.add((new PromotionVO()).fillWithPromotion(prom));
			}
		}
	}

	private List<OrderReturnPriceVO> composeOrderReturnPrice(OrderFormDTO ordForm, OrderCancelInfoDTO cancelInfo,
			List<ReturnPackageDTO> retPkgList) {
		List<OrderReturnPriceVO> ret = new ArrayList<OrderReturnPriceVO>();
		if (null == ordForm) {
			return ret;
		}
		BigDecimal hbPrice = BigDecimal.ZERO;
		Map<OrderCancelRType, BigDecimal> ordRetPrices = new HashMap<OrderCancelRType, BigDecimal>();
		// 1. 订单取消状态的退款金额
		if (null != cancelInfo) {
			if (OrderFormPayMethod.COD != ordForm.getOrderFormPayMethod()) {
				OrderCancelRType rtype = cancelInfo.getRtype();
				if (null != rtype) {
					BigDecimal newPrice = ordForm.getCartRPrice().add(ordForm.getExpUserPrice());
					if (newPrice.compareTo(ordForm.getRedCash()) > 0) {
						newPrice = new BigDecimal(newPrice.doubleValue() - ordForm.getRedCash().doubleValue());
						ordRetPrices.put(rtype, newPrice);
					}
				}
			}
			hbPrice = ordForm.getRedCash();
			// 回填数据：红包
			if (null != hbPrice && hbPrice.compareTo(BigDecimal.ZERO) > 0) {
				ret.add(new OrderReturnPriceVO("红包", hbPrice));
			}
		}
		// 2. 非订单取消状态、有取消包裹/退货的退款金额
		else {
			// 2.1 取消包裹
			List<OrderPackageDTO> ordPkgList = ordForm.getOrderPackageDTOList();
			if (!CollectionUtil.isEmptyOfList(ordPkgList)) {
				for (OrderPackageDTO ordPkg : ordPkgList) {
					OrderPackageRefundDTO ordPkgRefund = null;
					if (null == ordPkg || null == (ordPkgRefund = ordPkg.getOrderPackageRefundDTO())) {
						continue;
					}
					OrderCancelRType rtype = OrderCancelRType.ORI;
					if (!ordRetPrices.containsKey(rtype)) {
						ordRetPrices.put(rtype, BigDecimal.ZERO);
					}
					BigDecimal newPrice = ordRetPrices.get(rtype).add(ordPkgRefund.getRealCash());
					ordRetPrices.put(rtype, newPrice);
					hbPrice = hbPrice.add(ordPkgRefund.getRedCash());
				}
			}
			// 2.2 退货
			boolean hitBank = false;
			BigDecimal bankPrice = BigDecimal.ZERO;
			if (!CollectionUtil.isEmptyOfList(retPkgList)) {
				for (ReturnPackageDTO retPkg : retPkgList) {
					RefundType rt = null;
					if (null == retPkg || null == (rt = retPkg.getRefundType())) {
						continue;
					}
					OrderCancelRType ordRT = null;
					BigDecimal newPrice = BigDecimal.ZERO;
					switch (rt) {
					case ORIGINAL_PATH:
						ordRT = OrderCancelRType.ORI;
						if (!ordRetPrices.containsKey(ordRT)) {
							ordRetPrices.put(ordRT, BigDecimal.ZERO);
						}
						newPrice = ordRetPrices.get(ordRT).add(retPkg.getPayedCashPriceToUser());
						ordRetPrices.put(ordRT, newPrice);
						break;
					case WANGYIBAO:
						ordRT = OrderCancelRType.UN_ORI;
						if (!ordRetPrices.containsKey(ordRT)) {
							ordRetPrices.put(ordRT, BigDecimal.ZERO);
						}
						newPrice = ordRetPrices.get(ordRT).add(retPkg.getPayedCashPriceToUser());
						ordRetPrices.put(ordRT, newPrice);
						break;
					case BANKCARD:
						hitBank = true;
						bankPrice = bankPrice.add(retPkg.getPayedCashPriceToUser());
						break;
					default:
						break;
					}
					hbPrice = hbPrice.add(retPkg.getPayedHbPriceToUser());
				}
			}
			// 回填数据：红包
			if (null != hbPrice && hbPrice.compareTo(BigDecimal.ZERO) > 0) {
				ret.add(new OrderReturnPriceVO("红包", hbPrice));
			}
			// 回填数据：银行卡
			if (hitBank && null != bankPrice && bankPrice.compareTo(BigDecimal.ZERO) > 0) {
				ret.add(new OrderReturnPriceVO(RefundType.BANKCARD.getDesc(), bankPrice));
			}
		}

		// 3. 回填数据：原路 + 在线支付
		for (Entry<OrderCancelRType, BigDecimal> entry : ordRetPrices.entrySet()) {
			OrderCancelRType rtype = null;
			;
			if (null == entry || null == (rtype = entry.getKey())) {
				continue;
			}
			BigDecimal price = entry.getValue();
			if (null != price && price.compareTo(BigDecimal.ZERO) > 0) {
				ret.add(new OrderReturnPriceVO(rtype.getDesc(), price));
			}
		}

		return ret;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessAccount() {
		return businessAccount;
	}

	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public OrderFormState getStatus() {
		return status;
	}

	public void setStatus(OrderFormState status) {
		this.status = status;
	}

	public OrderFormPayMethod getMethod() {
		return method;
	}

	public void setMethod(OrderFormPayMethod method) {
		this.method = method;
	}

	public long getBookTime() {
		return bookTime;
	}

	public void setBookTime(long bookTime) {
		this.bookTime = bookTime;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getExpressPrice() {
		return expressPrice;
	}

	public void setExpressPrice(BigDecimal expressPrice) {
		this.expressPrice = expressPrice;
	}

	public BigDecimal getSysExprePay() {
		return sysExprePay;
	}

	public void setSysExprePay(BigDecimal sysExprePay) {
		this.sysExprePay = sysExprePay;
	}

	public BigDecimal getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(BigDecimal orderPay) {
		this.orderPay = orderPay;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public CouponVO getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponVO coupon) {
		this.coupon = coupon;
	}

	public String getPayOrderSn() {
		return payOrderSn;
	}

	public void setPayOrderSn(String payOrderSn) {
		this.payOrderSn = payOrderSn;
	}

	public OrderFormSource getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(OrderFormSource orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getSaleAire() {
		return saleAire;
	}

	public void setSaleAire(String saleAire) {
		this.saleAire = saleAire;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public List<PromotionVO> getPlatformAct() {
		return platformAct;
	}

	public void setPlatformAct(List<PromotionVO> platformAct) {
		this.platformAct = platformAct;
	}

	public List<OrderReturnPriceVO> getOrderReturn() {
		return orderReturn;
	}

	public void setOrderReturn(List<OrderReturnPriceVO> orderReturn) {
		this.orderReturn = orderReturn;
	}

	public boolean isCanCancel() {
		return canCancel;
	}

	public void setCanCancel(boolean canCancel) {
		this.canCancel = canCancel;
	}

	public boolean isPayed() {
		return isPayed;
	}

	public void setPayed(boolean isCOD) {
		this.isPayed = isCOD;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public OrderCancelSource getCancelSource() {
		return cancelSource;
	}

	public void setCancelSource(OrderCancelSource cancelSource) {
		this.cancelSource = cancelSource;
	}

	public long getPayCloseCD() {
		return payCloseCD;
	}

	public void setPayCloseCD(long payCloseCD) {
		this.payCloseCD = payCloseCD;
	}

	public BigDecimal getCouponSPrice() {
		return couponSPrice;
	}

	public void setCouponSPrice(BigDecimal couponSPrice) {
		this.couponSPrice = couponSPrice;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public String getProxyAccount() {
		return proxyAccount;
	}

	public void setProxyAccount(String proxyAccount) {
		this.proxyAccount = proxyAccount;
	}

	public static class CouponVO {
		private long id;

		private long userId;

		private long orderId;

		/**
		 * 有效期开始时间
		 */
		private String start;

		/**
		 * 有效期结束时间
		 */
		private String end;

		/**
		 * 优惠券code"
		 */
		private String couponCode;

		/**
		 * 优惠券简称
		 */
		private String couponName;

		public CouponVO fillWithCouponOrder(CouponOrder couponOrder, Coupon coupon) {
			if (null != couponOrder) {
				this.userId = couponOrder.getUserId();
				this.orderId = couponOrder.getOrderId();
				this.couponCode = couponOrder.getCouponCode();
			}
			if (null != coupon) {
				this.id = coupon.getId();
				this.start = coupon.getStartTime() > 0 ? DateFormatEnum.TYPE5.getFormatDate(coupon.getStartTime()) : "";
				this.end = coupon.getEndTime() > 0 ? DateFormatEnum.TYPE5.getFormatDate(coupon.getEndTime()) : "";
				this.couponName = coupon.getName();
			}
			return this;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public long getOrderId() {
			return orderId;
		}

		public void setOrderId(long orderId) {
			this.orderId = orderId;
		}

		public String getCouponCode() {
			return couponCode;
		}

		public void setCouponCode(String couponCode) {
			this.couponCode = couponCode;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

		public String getCouponName() {
			return couponName;
		}

		public void setCouponName(String couponName) {
			this.couponName = couponName;
		}

	}

	public static class PromotionVO {
		private String name;

		private String desc;

		public PromotionVO fillWithPromotion(PromotionDTO prom) {
			if (null != prom) {
				this.name = prom.getName();
				this.desc = prom.getDescription();
			}
			return this;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public static class OrderReturnPriceVO {
		private String name;

		private BigDecimal value = BigDecimal.ZERO;

		private OrderReturnPriceVO(String name, BigDecimal value) {
			this.name = name;
			this.value = value;
			if (null != this.value) {
				this.value = this.value.setScale(2, RoundingMode.HALF_UP);
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public BigDecimal getValue() {
			return value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}
	}

}
