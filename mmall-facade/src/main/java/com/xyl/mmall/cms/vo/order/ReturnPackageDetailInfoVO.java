package com.xyl.mmall.cms.vo.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cms.vo.order.OrderBasicInfoVO.CouponVO;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月23日 上午8:54:10
 *
 */
public class ReturnPackageDetailInfoVO {
// 1. 退货申请详情 - 基本信息
	private long requestTime; //申请时间，1413099512510
	private String returnId; //退货记录Id
	private String orderId; //原订单Id
	private String ordPkgId; //原包裹Id
	private BigDecimal pay = BigDecimal.ZERO; //实付金额，12
	private BigDecimal expressPay = BigDecimal.ZERO; //运费，12
	private BigDecimal returnPay = BigDecimal.ZERO; //申请退款金额，12
	private String expressCompany; //退货物流公司，"tiantian"
	private String expressNum; //退货物流单号，"xxx"
	private ReturnMethod totalPrice;
	private List<ReturnMethod> returnMethod = new ArrayList<ReturnMethod>(); //申请退款方式 [{"name":"xx"},{"name":"xxx"}],
// 2. 退货申请详情 -  处理信息
	private long dealTime; //最后处理时间，1413099512510
	private String status; //当前状态，"已退货"
	private String abnomalInfo = "无";	// 异常说明
	private BigDecimal acturalReturn = BigDecimal.ZERO; //实际退款金额，12
	private List<ReturnMethod> acturalReturnMethod = new ArrayList<ReturnMethod>(); //实际退款方式 [{"name":"xx"},{"name":"xxx"}],
	private CouponVO returnOrderCoupon;
	private String dealer; //审核人，"dashu"
	private String remark = "无"; //操作说明，"xxxxx"
	
	public void fillWithReturnPackage(ReturnPackageDTO retPkgDTO, ReturnCODBankCardInfoDTO bankCard, 
			CouponOrder couponOrder, Coupon coupon) {
		if(null == retPkgDTO) {
			return;
		}
// 1. 退货申请详情 - 基本信息
		this.requestTime = retPkgDTO.getCtime();
		this.returnId = String.valueOf(retPkgDTO.getRetPkgId());
		this.orderId = String.valueOf(retPkgDTO.getOrderId());
		this.ordPkgId = String.valueOf(retPkgDTO.getOrderPkgId());
		this.pay = retPkgDTO.getApplyedReturnTotalPrice();
		if(null != this.pay) {
			this.pay = this.pay.setScale(2, RoundingMode.HALF_UP);
		}
		OrderFormBriefDTO ordForm = retPkgDTO.getOrdFormBriefDTO();
		if(null != ordForm) {
			this.expressPay = ordForm.getExpUserPrice();
			if(null != this.expressPay) {
				this.expressPay = this.expressPay.setScale(2, RoundingMode.HALF_UP);
			}
		}
		this.returnPay = retPkgDTO.getApplyedReturnTotalPrice();
		if(null != this.returnPay) {
			this.returnPay = this.returnPay.setScale(2, RoundingMode.HALF_UP);
		}
		ExpressCompany ec = retPkgDTO.getExpressCompany();
		if(null != ec) {
			this.expressCompany = ec.getName();
		}
		this.expressNum = retPkgDTO.getMailNO();
		this.totalPrice = ReturnMethod.extractMethods(retPkgDTO, bankCard, this.returnMethod, true);
		
// 2. 退货申请详情 -  处理信息	
		this.dealTime = retPkgDTO.getReturnOperationTime();
		if(0 == dealTime) {
			dealTime = (retPkgDTO.getConfirmTime() > 0) ? retPkgDTO.getConfirmTime() : retPkgDTO.getCtime();
		}
		ReturnPackageState rs = retPkgDTO.getReturnState();
		String extInfo = retPkgDTO.getExtInfo();
		if(null != rs) {
			this.status = rs.getTag();
			if(rs == ReturnPackageState.ABNORMAL_WAITING_AUDIT || rs == ReturnPackageState.ABNORMAL_REFUSED ||
			   rs == ReturnPackageState.ABNORMAL_COD_WAITING_AUDIT || rs == ReturnPackageState.ABNORMAL_COD_REFUSED) {
				if(null != extInfo && !("null".equals(extInfo))) {
					this.abnomalInfo = extInfo;
				}
			}
		}
		ReturnMethod actualTotal = ReturnMethod.extractMethods(retPkgDTO, bankCard, acturalReturnMethod, false);
		this.acturalReturn = null != actualTotal ? actualTotal.getPrice() : retPkgDTO.getPayedTotalPriceToUser();
		if(null != couponOrder && null != coupon) {
			this.returnOrderCoupon = (new CouponVO()).fillWithCouponOrder(couponOrder, coupon);
			
		}
		// ugly code: start
		String names = retPkgDTO.getKfName();
		if(null != names) {
			String[] namesArray = names.split(";");
			if(null != namesArray && namesArray.length > 0) {
				this.dealer = namesArray[0];
			}
		}
		// ugly code: end
		if(null != extInfo && !("null".equals(extInfo))) {
			this.remark = retPkgDTO.getExtInfo();
		}
	}
	
	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrdPkgId() {
		return ordPkgId;
	}

	public void setOrdPkgId(String ordPkgId) {
		this.ordPkgId = ordPkgId;
	}

	public ReturnMethod getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(ReturnMethod totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getPay() {
		return pay;
	}

	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}

	public BigDecimal getExpressPay() {
		return expressPay;
	}

	public void setExpressPay(BigDecimal expressPay) {
		this.expressPay = expressPay;
	}

	public BigDecimal getReturnPay() {
		return returnPay;
	}

	public void setReturnPay(BigDecimal returnPay) {
		this.returnPay = returnPay;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}

	public List<ReturnMethod> getReturnMethod() {
		return returnMethod;
	}

	public void setReturnMethod(List<ReturnMethod> returnMethod) {
		this.returnMethod = returnMethod;
	}

	public long getDealTime() {
		return dealTime;
	}

	public void setDealTime(long dealTime) {
		this.dealTime = dealTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAbnomalInfo() {
		return abnomalInfo;
	}

	public void setAbnomalInfo(String abnomalInfo) {
		this.abnomalInfo = abnomalInfo;
	}

	public BigDecimal getActuralReturn() {
		return acturalReturn;
	}

	public void setActuralReturn(BigDecimal getPay) {
		this.acturalReturn = getPay;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ReturnMethod> getActuralReturnMethod() {
		return acturalReturnMethod;
	}

	public void setActuralReturnMethod(List<ReturnMethod> acturalReturnMethod) {
		this.acturalReturnMethod = acturalReturnMethod;
	}

	public CouponVO getReturnOrderCoupon() {
		return returnOrderCoupon;
	}

	public void setReturnOrderCoupon(CouponVO returnOrderCoupon) {
		this.returnOrderCoupon = returnOrderCoupon;
	}



	public static class ReturnMethod {
		
		private static ReturnMethod extractMethods(ReturnPackageDTO retPkgDTO, 
				ReturnCODBankCardInfoDTO bankCard, List<ReturnMethod> subMethods, boolean applySituation) {
			if(null == retPkgDTO) {
				return new ReturnMethod();
			}
			BigDecimal totalPrice = BigDecimal.ZERO;
			BigDecimal cashPrice = BigDecimal.ZERO;
			BigDecimal hbPrice = BigDecimal.ZERO;
			if(applySituation) {
				totalPrice = retPkgDTO.getApplyedReturnTotalPrice();
				cashPrice = retPkgDTO.getApplyedReturnCashPrice();
				hbPrice = retPkgDTO.getApplyedReturnHbPrice();
			} else {
				if(CollectionUtil.isInArray(ReturnPackageState.stateArrayOfReturned(), retPkgDTO.getReturnState())) {
					totalPrice = retPkgDTO.getPayedTotalPriceToUser();
					cashPrice = retPkgDTO.getPayedCashPriceToUser();
					hbPrice = retPkgDTO.getPayedHbPriceToUser();
				}
			}
			// 1. 总额
			ReturnMethod total = new ReturnMethod("总额", totalPrice, null);
			// 2. 红包	
			if(null != subMethods) {
				ReturnMethod hb = new ReturnMethod("红包", hbPrice, null);
				subMethods.add(hb);
			}
			// 3. 实际退款额
			if(null != subMethods) {
				RefundType rt = retPkgDTO.getRefundType();
				if(null != rt) {
					ReturnMethod refund = new ReturnMethod(rt.getDesc(), cashPrice, null);
					if(rt == RefundType.BANKCARD && null != bankCard) {
						refund.extInfo = bankCard.mergeGeneralInfo();
					}
					subMethods.add(refund);
				}
			}
			return total;
		}
		
		private String name;
		private BigDecimal price = BigDecimal.ZERO;
		private String extInfo;
		public ReturnMethod() {
		}
		public ReturnMethod(String name, BigDecimal price, String extInfo) {
			this.name = name;
			this.price = price;
			if(null != this.price) {
				this.price = this.price.setScale(2, RoundingMode.HALF_UP);
			}
			this.extInfo = extInfo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public String getExtInfo() {
			return extInfo;
		}
		public void setExtInfo(String extInfo) {
			this.extInfo = extInfo;
		}
	}
}
