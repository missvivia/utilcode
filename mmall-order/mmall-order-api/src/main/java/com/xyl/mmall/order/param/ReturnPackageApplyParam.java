package com.xyl.mmall.order.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.order.enums.RefundType;

/**
 * 申请退货时提供的参数
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月20日 下午1:41:19
 *
 */
public class ReturnPackageApplyParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4168527426990063036L;

	// 退货的OrderSku列表
	private List<ReturnOrderSkuParam> retOrderSkuParamList = new ArrayList<ReturnOrderSkuParam>();;
	
	// 退款方式
	private RefundType refundType;
	
	// 货到付款退货场景下的银行卡信息(refundType=BANKCARD时有效)
	private ReturnBankCardParam retBankCard;
	
	// 所属订单是否使用了优惠券
	private boolean couponUsedInOrder = false;
	
	public boolean checkSelf() {
		if(CollectionUtil.isEmptyOfList(retOrderSkuParamList)) {
			return false;
		}
		if(null == refundType) {
			return false;
		}
		if(refundType == RefundType.BANKCARD && null == retBankCard) {
			return false;
		}
		return true;
	}
	
	public List<ReturnOrderSkuParam> getRetOrderSkuParamList() {
		return retOrderSkuParamList;
	}

	public void setRetOrderSkuParamList(List<ReturnOrderSkuParam> retOrderSkuParamList) {
		this.retOrderSkuParamList = retOrderSkuParamList;
	}

	public RefundType getRefundType() {
		return refundType;
	}

	public void setRefundType(RefundType refundType) {
		this.refundType = refundType;
	}

	public ReturnBankCardParam getRetBankCard() {
		return retBankCard;
	}

	public void setRetBankCard(ReturnBankCardParam retBankCard) {
		this.retBankCard = retBankCard;
	}

	public boolean isCouponUsedInOrder() {
		return couponUsedInOrder;
	}

	public void setCouponUsedInOrder(boolean couponUsedInOrder) {
		this.couponUsedInOrder = couponUsedInOrder;
	}

}
