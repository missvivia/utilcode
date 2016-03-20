package com.xyl.mmall.mobile.web.vo.order;

import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.order.enums.ReturnPackageState;

/**
 * 退货申请
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月29日 下午3:07:32
 *
 */
public class ReturnApplyVO {
	
	private ReturnAddressVO returnInfo;
	
	private ExpressCompany[] expCompanies = ExpressCompany.validValues();
	
	private ReturnOrderSkuInfoList returns;
	
	private ReturnPriceVO returnForm;

	private String orderId;
	
	private String ordPkgId;
	
	private String retPkgId;
	
	private ReturnPackageState returnState;
	
	// ugly code: remember to set fromMyOrder form the Controller level
	private boolean fromMyOrder = false;
	
	public ReturnAddressVO getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(ReturnAddressVO returnInfo) {
		this.returnInfo = returnInfo;
	}

	public ExpressCompany[] getExpCompanies() {
		return expCompanies;
	}

	public void setExpCompanies(ExpressCompany[] expCompanies) {
		this.expCompanies = expCompanies;
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

	public boolean isFromMyOrder() {
		return fromMyOrder;
	}

	public void setFromMyOrder(boolean fromMyOrder) {
		this.fromMyOrder = fromMyOrder;
	}

	public String getOrdPkgId() {
		return ordPkgId;
	}

	public void setOrdPkgId(String orderPackageId) {
		this.ordPkgId = orderPackageId;
	}

	public static class ReturnAddressVO {
		private String returnExpInfoId;	// 退货仓库id
		private String returnWay;    //退货途径："非邮政普通快递（不支持货到付款）",
		private String returnAddress;    //退货地址："杭州下沙经济技术开角伊比利亚工业",
		private String returnPerson;    //收件人："章",
		private String returnZip;    //邮件："31000",
		private String returnPhone;    //联系电话："13576766767"
		public ReturnAddressVO fillWithWarehouse(WarehouseForm warehouse) {
			if(null != warehouse) {
				this.returnExpInfoId = String.valueOf(warehouse.getWarehouseId());
				this.returnWay = warehouse.getType();
				this.returnAddress = warehouse.getFullAddress();
				this.returnPerson = warehouse.getWarehouseName();
				// to be continued：WarehouseForm中没有邮件编码字段
				this.returnZip = "";
				this.returnPhone = warehouse.getPhone();
			}
			return this;
		}
		public String getReturnExpInfoId() {
			return returnExpInfoId;
		}
		public void setReturnExpInfoId(String returnExpInfoId) {
			this.returnExpInfoId = returnExpInfoId;
		}
		public String getReturnWay() {
			return returnWay;
		}
		public void setReturnWay(String returnWay) {
			this.returnWay = returnWay;
		}
		public String getReturnAddress() {
			return returnAddress;
		}
		public void setReturnAddress(String returnAddress) {
			this.returnAddress = returnAddress;
		}
		public String getReturnPerson() {
			return returnPerson;
		}
		public void setReturnPerson(String returnPerson) {
			this.returnPerson = returnPerson;
		}
		public String getReturnZip() {
			return returnZip;
		}
		public void setReturnZip(String returnZip) {
			this.returnZip = returnZip;
		}
		public String getReturnPhone() {
			return returnPhone;
		}
		public void setReturnPhone(String returnPhone) {
			this.returnPhone = returnPhone;
		}
	}
}
