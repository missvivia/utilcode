package com.xyl.mmall.order.util;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.enums.DeprecatedReturnCouponHbRecycleState;
import com.xyl.mmall.order.enums.DeprecatedReturnOrderSkuState;
import com.xyl.mmall.order.enums.DeprecatedReturnState;
import com.xyl.mmall.order.enums.ExpressCompany;
import com.xyl.mmall.order.meta.DeprecatedReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;
import com.xyl.mmall.order.meta.DeprecatedReturnOrderSku;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam;
import com.xyl.mmall.order.param.DeprecatedReturnOrderSkuParam;
import com.xyl.mmall.order.param.ReturnBankCardParam;
import com.xyl.mmall.order.param.DeprecatedReturnFormApplyParam.PriceParam;

/**
 * 退货服务相关的工厂
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月25日 下午4:29:40
 *
 */
@Deprecated
public class DeprecatedReturnEntiyFactory {

	/**
	 * 
	 */
	private DeprecatedReturnEntiyFactory() {
	}
	
	/**
	 * 创建ReturnForm
	 * 
	 * @param userId
	 * @param orderId
	 * @param applyParam
	 * @return
	 */
	public static DeprecatedReturnForm createApplyReturnForm(long userId, long orderId, 
			DeprecatedReturnFormApplyParam applyParam) {
		DeprecatedReturnForm retForm = new DeprecatedReturnForm();
		
		PriceParam priceParam = applyParam.getPriceParam();
		
		// 1. meta中的字段 - part1 
		// 快递信息以及仓库收货时间需要后续更新，但是这里不能为空，用临时值填充
		retForm.setOrderId(orderId);
		retForm.setUserId(userId);
		retForm.setCtime(System.currentTimeMillis());
		retForm.setMailNO("等待用户填写");
		retForm.setExpressCompany(ExpressCompany.UNKNOWN);
		retForm.setConfirmTime(0);
		
		// 2. 退货订单的原始信息明细(主站显示给用户看的数据)	
		retForm.setGoodsTotalRPrice(priceParam.getGoodsTotalRPrice());
		retForm.setHdYPrice(priceParam.getHdYPrice());
		retForm.setCouponYPrice(priceParam.getCouponYPrice());
		retForm.setExpPrice(priceParam.getExpPrice());
		
		// 3. 实际要退款明细的组成(运维系统里用到的实际金额)
		retForm.setPayedCashPrice(priceParam.getPayedCashPrice());
//		retForm.setHbPrice(priceParam.getHbPrice());
//		retForm.setReturnCashPrice(priceParam.getReturnCashPrice());
		retForm.setExpSubsidyPrice(priceParam.getExpSubsidyPrice());
		
		// 4. 退货地址、状态，退款方式
		retForm.setReturnExpInfoId("");
		retForm.setReturnState(DeprecatedReturnState.APPLY_RETURN);
		retForm.setRefundType(priceParam.getRefundType());
		retForm.setExtInfo("");
		
		// 5. 是否退优惠券+红包
		if(priceParam.isRecycle()) {
			retForm.setCouponHbRecycleState(DeprecatedReturnCouponHbRecycleState.WAITING_RETURN);
		} else {
			retForm.setCouponHbRecycleState(DeprecatedReturnCouponHbRecycleState.DO_NOT_RETURN);
		}
		
		return retForm;
	}

	
	/**
	 * 创建ReturnOrderSku
	 * 
	 * @param orderId
	 * @param userId
	 * @param returnId
	 * @param param
	 * @return
	 */
	public static DeprecatedReturnOrderSku createReturnOrderSku(long orderId, long userId, 
			long returnId, DeprecatedReturnOrderSkuParam param) {
		DeprecatedReturnOrderSku retOrdSku = new DeprecatedReturnOrderSku();
		// part-1：用户申请退货时填入(meta)
		retOrdSku.setOrderId(orderId);
		retOrdSku.setOrderSkuId(param.getOrderSkuId());
		retOrdSku.setSkuId(param.getSkuId());
		retOrdSku.setSkuId(param.getSkuId());
		retOrdSku.setUserId(userId);
		retOrdSku.setReturnId(returnId);
		retOrdSku.setCtime(System.currentTimeMillis());
		retOrdSku.setReturnCount(param.getCount());
		retOrdSku.setReturnPrice(param.getTotalReturnPrice());
		retOrdSku.setReason(param.getReason());
		// part-2：仓库收到退货时填入
		retOrdSku.setConfirmCount(0);
		retOrdSku.setRetOrdSkuState(DeprecatedReturnOrderSkuState.NOT_CONFIRMED);
		retOrdSku.setConfirmInfo("");
		// part-3：客服退款/拒绝时填入
		retOrdSku.setKfId(0);
		retOrdSku.setActualReturnPrice(param.getTotalReturnPrice());
		retOrdSku.setExtInfo("");
		return retOrdSku;
	}
	
	
	/**
	 * 批量创建ReturnOrderSku
	 * 
	 * @param orderId
	 * @param userId
	 * @param returnId
	 * @param paramList
	 * @return
	 */
	public static List<DeprecatedReturnOrderSku> createReturnOrderSkuList(long orderId, long userId, 
			long returnId, List<DeprecatedReturnOrderSkuParam> paramList) {
		List<DeprecatedReturnOrderSku> retOrdSkuList = new ArrayList<DeprecatedReturnOrderSku>(paramList.size());
		for(DeprecatedReturnOrderSkuParam param : paramList) {
			DeprecatedReturnOrderSku retOrdSku = createReturnOrderSku(orderId, userId, returnId, param);
			retOrdSkuList.add(retOrdSku);
		}
		return retOrdSkuList;
	}
	
	/**
	 * 
	 * @param orderId
	 * @param userId
	 * @param returnId
	 * @return
	 */
	public static DeprecatedReturnCODBankCardInfo createCODRetBankCard(long orderId, long userId, 
			long returnId, ReturnBankCardParam retBankCard) {
		DeprecatedReturnCODBankCardInfo retObj = new DeprecatedReturnCODBankCardInfo();
		retObj.setRetId(returnId);
		retObj.setOrderId(orderId);
		retObj.setUserId(userId);
		retObj.setBankCardNO(retBankCard.getBankCardNO());
		retObj.setBankCardOwnerName(retBankCard.getBankCardOwnerName());
		retObj.setBankCardAddress(retBankCard.getBankCardAddress());
		retObj.setBankType(retBankCard.getBankType());
		retObj.setBankBranch(retBankCard.getBankBranch());
		return retObj;
	}
}
