package com.xyl.mmall.mobile.web.vo.order;

import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
public class ReturnExpInfoVO {
	
	private ExpInfoVO returnExpress = new ExpInfoVO();
	
	private ReturnOrderSkuInfoList returns = new ReturnOrderSkuInfoList();
	
	private ReturnPriceVO returnForm = new ReturnPriceVO();
	
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

	public ExpInfoVO getReturnExpress() {
		return returnExpress;
	}

	public void setReturnExpress(ExpInfoVO returnExpress) {
		this.returnExpress = returnExpress;
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

	public String getOrdPkgId() {
		return ordPkgId;
	}

	public void setOrdPkgId(String orderPackageId) {
		this.ordPkgId = orderPackageId;
	}

	public String getRetPkgId() {
		return retPkgId;
	}

	public void setRetPkgId(String returnId) {
		this.retPkgId = returnId;
	}

	public ReturnPriceVO getReturnForm() {
		return returnForm;
	}

	public void setReturnForm(ReturnPriceVO returnForm) {
		this.returnForm = returnForm;
	}
	
	public ReturnPackageState getReturnState() {
		return returnState;
	}

	public void setReturnState(ReturnPackageState returnState) {
		this.returnState = returnState;
	}

	public OrderFormPayMethod getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(OrderFormPayMethod payMethod) {
		this.payMethod = payMethod;
	}

	public static class ExpInfoVO {
		private String way;    //退货方式
		private String express;    //物流公司
		private String mailNO;    //快递单号
		public void fillWithReturnPackage(ReturnPackageDTO retPkg) {
			way = "自助退货";
			if(null == retPkg) {
				return;
			}
			ExpressCompany ec = retPkg.getExpressCompany();
			if(null != ec) {
				express = ec.getName();
			}
			mailNO = retPkg.getMailNO();
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
