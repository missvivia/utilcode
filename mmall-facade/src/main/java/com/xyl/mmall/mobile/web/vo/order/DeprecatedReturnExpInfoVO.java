package com.xyl.mmall.mobile.web.vo.order;

import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.ExpressCompany;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
@Deprecated
public class DeprecatedReturnExpInfoVO {
	
	private ExpInfoVO returnExpress = new ExpInfoVO();
	
	private DeprecatedReturnOrderSkuInfoList returns = new DeprecatedReturnOrderSkuInfoList();
	
	private DeprecatedReturnPriceVO returnForm = new DeprecatedReturnPriceVO();

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

	public ExpInfoVO getReturnExpress() {
		return returnExpress;
	}

	public void setReturnExpress(ExpInfoVO returnExpress) {
		this.returnExpress = returnExpress;
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

	public DeprecatedReturnPriceVO getReturnForm() {
		return returnForm;
	}

	public void setReturnForm(DeprecatedReturnPriceVO returnForm) {
		this.returnForm = returnForm;
	}
	
	public DeprecatedReturnState getReturnState() {
		return returnState;
	}

	public void setReturnState(DeprecatedReturnState returnState) {
		this.returnState = returnState;
	}

	public static class ExpInfoVO {
		private String way;    //退货方式
		private String express;    //物流公司
		private String mailNO;    //快递单号
		public void fillWithReturnForm(DeprecatedReturnFormDTO retForm) {
			way = "自助退货";
			if(null == retForm) {
				return;
			}
			ExpressCompany ec = retForm.getExpressCompany();
			if(null != ec) {
				express = ec.getShortName();
			}
			mailNO = retForm.getMailNO();
		}
		public String getWay() {
			return way;
		}
		public void setWay(String way) {
			this.way = way;
		}
		public String getExpress() {
			return express;
		}
		public void setExpress(String express) {
			this.express = express;
		}
		public String getMailNO() {
			return mailNO;
		}
		public void setMailNO(String mailNO) {
			this.mailNO = mailNO;
		}
	}
}
