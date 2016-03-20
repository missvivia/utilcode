package com.xyl.mmall.mobile.web.vo.order;

import com.xyl.mmall.order.dto.DeprecatedReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam.PriceParam;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
@Deprecated
public class DeprecatedReturnStatusVO {
	
	private StatusInfo statusInfo;
	
	private ReturnPriceVOExtension price = new ReturnPriceVOExtension();
	
	private DeprecatedReturnOrderSkuInfoList returns = new DeprecatedReturnOrderSkuInfoList();
	
	private String orderId;
	
	private String returnId;
	
	private DeprecatedReturnState returnState;
	
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

	public DeprecatedReturnOrderSkuInfoList getReturns() {
		return returns;
	}

	public void setReturns(DeprecatedReturnOrderSkuInfoList returns) {
		this.returns = returns;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public DeprecatedReturnState getReturnState() {
		return returnState;
	}

	public void setReturnState(DeprecatedReturnState returnState) {
		this.returnState = returnState;
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
	
	public static class ReturnPriceVOExtension extends DeprecatedReturnPriceVO {
		private static final long serialVersionUID = -3205894150428873867L;
		private long returnedTime;
		private String extInfo = "无";
		public void fillWithParams(PriceParam priceParam, long returnedTime, String extInfo) {
			super.fillWithPriceParam(priceParam);
			this.returnedTime = returnedTime;
			if(null != extInfo && !("null".equals(extInfo))) {
				this.extInfo = extInfo;
			}
		}
		public void fillWithParams(DeprecatedReturnFormDTO retFormDTO, 
				DeprecatedReturnCODBankCardInfoDTO bankCard, long returnedTime, String extInfo) {
			super.fillWithReturnForm(retFormDTO, bankCard);
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
