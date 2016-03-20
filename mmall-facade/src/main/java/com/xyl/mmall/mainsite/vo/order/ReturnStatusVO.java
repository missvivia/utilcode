package com.xyl.mmall.mainsite.vo.order;

import java.util.List;

import com.xyl.mmall.mainsite.vo.order.ReturnExpInfoVO.ExpInfoVO;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
public class ReturnStatusVO {
	
	private StatusInfo statusInfo;
	
	private ExpInfoVO returnExpress = new ExpInfoVO();
	
	private ReturnPriceVOExtension price = new ReturnPriceVOExtension();
	
	private ReturnOrderSkuInfoList returns = new ReturnOrderSkuInfoList();
	
	private OrderFormPayMethod payMethod;
	
	private String orderId;
	
	private String ordPkgId;
	
	private String retPkgId;
	
	private ReturnPackageState returnState;
	
	// ugly code: remember to set fromMyOrder form the Controller level
	private boolean fromMyOrder = false;
	
	public boolean isFromMyOrder() {
		return fromMyOrder;
	}

	public void setFromMyOrder(boolean fromMyOrder) {
		this.fromMyOrder = fromMyOrder;
	}

	public StatusInfo getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(StatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

	public ReturnPriceVOExtension getPrice() {
		return price;
	}

	public void setPrice(ReturnPriceVOExtension price) {
		this.price = price;
	}

	public ReturnOrderSkuInfoList getReturns() {
		return returns;
	}

	public void setReturns(ReturnOrderSkuInfoList returns) {
		this.returns = returns;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRetPkgId() {
		return retPkgId;
	}

	public void setRetPkgId(String returnId) {
		this.retPkgId = returnId;
	}

	public ReturnPackageState getReturnState() {
		return returnState;
	}

	public void setReturnState(ReturnPackageState returnState) {
		this.returnState = returnState;
	}

	public String getOrdPkgId() {
		return ordPkgId;
	}

	public void setOrdPkgId(String orderPackageId) {
		this.ordPkgId = orderPackageId;
	}

	public OrderFormPayMethod getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(OrderFormPayMethod payMethod) {
		this.payMethod = payMethod;
	}

	public ExpInfoVO getReturnExpress() {
		return returnExpress;
	}

	public void setReturnExpress(ExpInfoVO returnExpress) {
		this.returnExpress = returnExpress;
	}

	public static class StatusInfo {
		public static enum Status {
			RETURNING(0, "等待退货"), SUCCESSFUL(1, "退货退款成功"), FAILED(2, "退货退款失败");
			private int intValue;
			private String status;
			private Status(int intValue, String status) {
				this.intValue = intValue;
				this.status = status;
			}
		}
		private int intValue;
		private String desc;
		private String extInfo = "无";
		public StatusInfo() {
		}
		public StatusInfo(Status status, String extInfo) {
			if(null != status) {
				this.intValue = status.intValue;
				this.desc = status.status;
			}
			if(null != extInfo && !("null".equals(extInfo))) {
				this.extInfo = extInfo;
			}
		}
		public int getIntValue() {
			return intValue;
		}
		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getExtInfo() {
			return extInfo;
		}
		public void setExtInfo(String extInfo) {
			this.extInfo = extInfo;
		}
	}
	
	public static class ReturnPriceVOExtension extends ReturnPriceVO {
		private static final long serialVersionUID = -3205894150428873867L;
		private long returnedTime;
		private String extInfo = "无";
		public void fillWithParams(_ReturnPackagePriceParam priceParam, RefundType rt, String bankCard, 
				List<ReturnCoupon> returnCoupon, long returnedTime, String extInfo) {
			super.fillWithPriceParam(priceParam, rt, bankCard, returnCoupon);
			this.returnedTime = returnedTime;
			if(null != extInfo && !("null".equals(extInfo))) {
				this.extInfo = extInfo;
			}
		}
		public void fillWithReturnPackage(ReturnPackageDTO retPkgDTO, boolean applySituation, String bankCard, 
				List<ReturnCoupon> returnCoupon, long returnedTime, String extInfo) {
			super.fillWithReturnPackage(retPkgDTO, applySituation, bankCard, returnCoupon);
			this.returnedTime = returnedTime;
			if(null != extInfo && !("null".equals(extInfo))) {
				this.extInfo = extInfo;
			}
		}
		public long getReturnedTime() {
			return returnedTime;
		}
		public void setReturnedTime(long returnedTime) {
			this.returnedTime = returnedTime;
		}
		public String getExtInfo() {
			return extInfo;
		}
		public void setExtInfo(String extInfo) {
			this.extInfo = extInfo;
		}
	}
	
}
