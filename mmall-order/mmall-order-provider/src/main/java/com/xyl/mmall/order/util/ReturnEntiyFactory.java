package com.xyl.mmall.order.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.enums.ExpressCompany;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam;
import com.xyl.mmall.order.api.util.ReturnPriceCalculator._ReturnPackagePriceParam.RetOrdSkuPriceParam;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.enums.HBRecycleState;
import com.xyl.mmall.order.enums.RefundType;
import com.xyl.mmall.order.enums.ReturnCouponRecycleState;
import com.xyl.mmall.order.enums.ReturnOrderSkuConfirmState;
import com.xyl.mmall.order.enums.ReturnPackageState;
import com.xyl.mmall.order.meta.HBRecycleLog;
import com.xyl.mmall.order.meta.ReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.ReturnForm;
import com.xyl.mmall.order.meta.ReturnOrderSku;
import com.xyl.mmall.order.meta.ReturnPackage;
import com.xyl.mmall.order.param.ReturnBankCardParam;
import com.xyl.mmall.order.param.ReturnOrderSkuParam;

/**
 * 退货服务相关的工厂
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月25日 下午4:29:40
 *
 */
public class ReturnEntiyFactory {

	/**
	 * 
	 */
	private ReturnEntiyFactory() {
	}
	
	public static ReturnForm createEmptyReturnForm(long userId, long orderId, boolean couponUsedInOrder) {
		ReturnForm retForm = new ReturnForm();
		retForm.setOrderId(orderId);
		retForm.setUserId(userId);
		retForm.setCtime(System.currentTimeMillis());
		if(couponUsedInOrder) {
			retForm.setCouponHbRecycleState(ReturnCouponRecycleState.WAITING_RECYCEL);
		} else {
			retForm.setCouponHbRecycleState(ReturnCouponRecycleState.NONE);
		}
		return retForm;
	}
	
	public static ReturnPackage createApplyReturnPackage(OrderPackageSimpleDTO ordPkgDTO, 
			List<ReturnOrderSkuParam> retOrderSkuParamList, RefundType rt, long bankCardInfoId) {
		if(null == ordPkgDTO || CollectionUtil.isEmptyOfList(retOrderSkuParamList) || null == rt) {
			return null;
		}
		_ReturnPackagePriceParam priceParam = ReturnPriceCalculator.compute(ordPkgDTO, retOrderSkuParamList);
		if(null == priceParam) {
			return null;
		}
// 1. 退货包裹明细：非金额部分
		ReturnPackage retPkg = new ReturnPackage();
		retPkg.setUserId(ordPkgDTO.getUserId());
		retPkg.setOrderId(ordPkgDTO.getOrderId());
		retPkg.setOrderPkgId(ordPkgDTO.getPackageId());
		retPkg.setCtime(System.currentTimeMillis());
		retPkg.setRefundType(rt);
		if(rt == RefundType.BANKCARD) {
			retPkg.setBankCardInfoId(bankCardInfoId);
		}
		retPkg.setReturnExpInfoId("退货地址Id（待填写）");
		retPkg.setMailNO("退货-快递号（待填写）");
		retPkg.setExpressCompany(ExpressCompany.NULL);
// 2. 退货包裹明细：金额部分		
		retPkg.setApplyedReturnTotalPrice(priceParam.getReturnTotalPrice());
		retPkg.setApplyedReturnHbPrice(priceParam.getReturnHbPrice());
		retPkg.setApplyedReturnCashPrice(priceParam.getReturnCashPrice());
		retPkg.setExpSubsidyPrice(priceParam.getExpSubsidyPrice());
// 4. 相关状态
		retPkg.setReturnState(ReturnPackageState.APPLY_RETURN);
		return retPkg;
	}
	
	public static List<ReturnOrderSku> createApplyRetOrdSku(long retPkgId, 
			OrderPackageSimpleDTO ordPkgDTO, List<ReturnOrderSkuParam> retOrderSkuParamList) {
		if(null == ordPkgDTO || CollectionUtil.isEmptyOfList(retOrderSkuParamList)) {
			return null;
		}
		_ReturnPackagePriceParam priceParam = ReturnPriceCalculator.compute(ordPkgDTO, retOrderSkuParamList);
		Map<Long, RetOrdSkuPriceParam> retOrdSkuPriceMap = null;
		if(null == priceParam || null == (retOrdSkuPriceMap = priceParam.getRetOrdSkuPriceMap())) {
			return null;
		}
		List<ReturnOrderSku> retOrdSkuList = new ArrayList<ReturnOrderSku>();
		for(ReturnOrderSkuParam retOrdSkuParam : retOrderSkuParamList) {
			if(null == retOrdSkuParam) {
				continue;
			}
			long orderSkuId = retOrdSkuParam.getOrderSkuId();
			RetOrdSkuPriceParam retOrdSkuPriceParam = retOrdSkuPriceMap.get(orderSkuId);
			OrderSkuDTO ordSku = null;
			if(null == retOrdSkuPriceParam || null == (ordSku = retOrdSkuPriceParam.getOrdSku())) {
				continue;
			}
			ReturnOrderSku retOrdSku = new ReturnOrderSku();
			retOrdSku.setRetPkgId(retPkgId);
			retOrdSku.setOrderSkuId(ordSku.getId());
			retOrdSku.setUserId(ordSku.getUserId());
			retOrdSku.setRetOrdSkuState(ReturnOrderSkuConfirmState.NOT_CONFIRMED);
			retOrdSku.setPoId(ordSku.getPoId());
			retOrdSku.setSkuId(ordSku.getSkuId());
			retOrdSku.setCtime(System.currentTimeMillis());
			retOrdSku.setApplyedReturnCount(retOrdSkuParam.getRetCount());
			retOrdSku.setReason(retOrdSkuParam.getReason());
			retOrdSkuList.add(retOrdSku);
		}
		return 0 == retOrdSkuList.size() ? null : retOrdSkuList;
	}
	
	public static ReturnCODBankCardInfo createCODRetBankCard(long userId, ReturnBankCardParam retBankCard) {
		ReturnCODBankCardInfo retObj = new ReturnCODBankCardInfo();
		retObj.setUserId(userId);
		retObj.setBankCardNO(retBankCard.getBankCardNO());
		retObj.setBankCardOwnerName(retBankCard.getBankCardOwnerName());
		retObj.setBankCardAddress(retBankCard.getBankCardAddress());
		retObj.setBankType(retBankCard.getBankType());
		retObj.setBankBranch(retBankCard.getBankBranch());
		return retObj;
	}
	
	public static HBRecycleLog createHBRecycleLog(ReturnPackage retPkg, HBRecycleState initState) {
		if(null == retPkg) {
			return null;
		}
		HBRecycleLog log = new HBRecycleLog();
		log.setRetPkgId(retPkg.getRetPkgId());
		log.setUserId(retPkg.getUserId());
		log.setOrderId(retPkg.getOrderId());
		log.setOrderPkgId(retPkg.getOrderPkgId());
		if(null != initState) {
			log.setHbRecycleState(initState);
		}
		log.setApplyedReturnHbPrice(retPkg.getApplyedReturnHbPrice());
		log.setPayedHbPriceToUser(retPkg.getPayedHbPriceToUser());
		long currentTime = System.currentTimeMillis();
		log.setCreateTime(currentTime);
		log.setUpdateTime(currentTime);
		return log;
	}
}
