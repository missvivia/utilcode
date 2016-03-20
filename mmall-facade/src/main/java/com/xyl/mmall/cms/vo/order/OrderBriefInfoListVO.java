package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.util.List;

import com.netease.print.common.util.DateFormatEnum;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * 订单信息列表：通过用户信息查询
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月9日 上午10:12:18
 *
 */
public class OrderBriefInfoListVO {

	private int total;

	private List<OrderBriefInfo> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<OrderBriefInfo> getList() {
		return list;
	}

	public void setList(List<OrderBriefInfo> list) {
		this.list = list;
	}

	public static class OrderBriefInfo {

		private String id; // 订单id

		private String userId; // 用户id

		private String userName;// 用户账号

		private long businessId;// 商家Id

		private String businessAccount;// 商家账号

		private long status; // 订单状态

		private long payMethod; // 支付方式 1。货到付款 2.在线支付

		private long payStatus = 0; // 支付状态 0.未支付 1.已支付

		private String orderTime; // 下单时间：1410436682969

		private long time; // 下单时间：1410436682969

		private BigDecimal price; // 订单原价

		private BigDecimal orderPay; // 订单实付

		private BigDecimal expressPay; // 快递实付

		private String logistics = ""; // 物流方式："EMS"

		private boolean isUseCoupon = false;// 是否有优惠券

		private int spsource;// 订单渠道 4 普通订单 7 代客下单

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public long getStatus() {
			return status;
		}

		public void setStatus(long status) {
			this.status = status;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public BigDecimal getOrderPay() {
			return orderPay;
		}

		public void setOrderPay(BigDecimal orderPay) {
			this.orderPay = orderPay;
		}

		public BigDecimal getExpressPay() {
			return expressPay;
		}

		public void setExpressPay(BigDecimal expressPay) {
			this.expressPay = expressPay;
		}

		public String getLogistics() {
			return logistics;
		}

		public void setLogistics(String logistics) {
			this.logistics = logistics;
		}

		public String getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
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

		public long getPayMethod() {
			return payMethod;
		}

		public void setPayMethod(long payMethod) {
			this.payMethod = payMethod;
		}

		public long getPayStatus() {
			return payStatus;
		}

		public void setPayStatus(long payStatus) {
			this.payStatus = payStatus;
		}

		public boolean isUseCoupon() {
			return isUseCoupon;
		}

		public void setUseCoupon(boolean isUseCoupon) {
			this.isUseCoupon = isUseCoupon;
		}

		public int getSpsource() {
			return spsource;
		}

		public void setSpsource(int spsource) {
			this.spsource = spsource;
		}

		/**
		 * OrderFormDTO -> OrderInfoByUserVO
		 * 
		 * @param ordDTO
		 * @return
		 */
		public static OrderBriefInfo orderDTO2VO(OrderFormBriefDTO ordDTO) {
			OrderBriefInfo ordVO = new OrderBriefInfo();
			if (null == ordDTO) {
				return ordVO;
			}
			ordVO.setId(String.valueOf(ordDTO.getOrderId()));
			ordVO.setUserId(String.valueOf(ordDTO.getUserId()));
			long currTime = System.currentTimeMillis();
			ordVO.setStatus(ordDTO.getOrderFormState().getIntValue());
			ordVO.setPayMethod(ordDTO.getOrderFormPayMethod().getIntValue());
			ordVO.setPayStatus(ordDTO.getPayState().getIntValue());
			if (ordDTO.getOrderTime() + ConstValueOfOrder.MAX_PAY_TIME < currTime
					&& ordDTO.getOrderFormState().equals(OrderFormState.WAITING_PAY)) {
				ordVO.setStatus(OrderFormState.CANCEL_ED.getIntValue());
			}
			if (ordDTO.getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
				ordVO.setUseCoupon(true);
			}
			ordVO.setTime(ordDTO.getOrderTime());
			ordVO.setOrderTime(ordDTO.getOrderTime() > 0 ? DateFormatEnum.TYPE5.getFormatDate(ordDTO.getOrderTime())
					: "");
			ordVO.setPrice(ordDTO.getCartOriRPrice());
			ordVO.setOrderPay(ordDTO.getCartRPrice());
			ordVO.setExpressPay(ordDTO.getExpUserPrice());
			ExpressCompany ec = ordDTO.getExpressCompany();
			if (null != ec) {
				ordVO.setLogistics(ec.getShortName());
			}
			ordVO.setBusinessId(ordDTO.getBusinessId());
			ordVO.setBusinessAccount(ordDTO.getBusinessAccount());
			ordVO.setUserName(ordDTO.getUserName());
			ordVO.setSpsource(ordDTO.getSpSource().getIntValue());
			return ordVO;
		}
	}
}
