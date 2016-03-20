package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.order.dao.ReturnCODBankCardInfoDao;
import com.xyl.mmall.order.dao.ReturnOrderSkuDao;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnCODBankCardInfoDTO;
import com.xyl.mmall.order.dto.ReturnFormDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.meta.ReturnCODBankCardInfo;
import com.xyl.mmall.order.meta.ReturnOrderSku;
import com.xyl.mmall.order.meta.ReturnPackage;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.ReturnFormService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
class ReturnPacakgeService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected OrderBriefService orderBriefService;
	
	@Autowired
	protected ReturnFormService retFormService;
	
	@Autowired
	protected OrderPackageSimpleService _ordPkgSimpleService;

	@Autowired
	protected ReturnOrderSkuDao returnOrderSkuDao;
	
	@Autowired
	protected ReturnCODBankCardInfoDao codRetBankCardDao;

	/**
	 * List<_ReturnOrderSku> -> Map<Long, _ReturnOrderSkuDTO> 
	 * 
	 * @param retOrdSkuList：_ReturnOrderSku列表
	 * @param allOrdSku：包裹中所有的OrderSku
	 * @return
	 */
	protected Map<Long, ReturnOrderSkuDTO> convertToRetOrdSkuDTOMap(
			List<ReturnOrderSku> retOrdSkuList, Map<Long, OrderSkuDTO> allOrdSku) {
		Map<Long, ReturnOrderSkuDTO> ret = new HashMap<Long, ReturnOrderSkuDTO>();
		if(null == retOrdSkuList || null == allOrdSku) {
			logger.warn("convertToRetOrdSkuDTOList(...) : retOrdSkuList or allOrdSku is null");
			return ret;
		}
		for(ReturnOrderSku retOrderSku : retOrdSkuList) {
			if(null == retOrderSku) {
				logger.warn("null retOrderSku in retOrdSkuList");
				continue;
			}
			long ordSkuId = retOrderSku.getOrderSkuId();
			OrderSkuDTO ordSkuDTO = allOrdSku.get(ordSkuId);
			if(null == ordSkuDTO) {
				logger.warn("no ordSkuDTO in allOrdSku for [orderSkuId: " + ordSkuId + "]");
				continue;
			}
			ReturnOrderSkuDTO retOrderSkuDTO = new ReturnOrderSkuDTO(retOrderSku);
			retOrderSkuDTO.setOrdSkuDTO(ordSkuDTO);
			ret.put(ordSkuId, retOrderSkuDTO);
		}
		return ret;
	}
	
	/**
	 * _ReturnPackage -> _ReturnPackageDTO
	 * 
	 * @param retPkg
	 * @return
	 */
	protected ReturnPackageDTO convertReturnPackageMetaToDTO(ReturnPackage retPkg) {
		if(null == retPkg) {
			return null;
		}
		ReturnPackageDTO retPkgDTO = new ReturnPackageDTO(retPkg);
		long userId = retPkgDTO.getUserId();
		long retPkgId = retPkgDTO.getRetPkgId();
		//1. 退货关联的OrderFormBriefDTO
		long orderId = retPkgDTO.getOrderId();
		OrderFormBriefDTO orderFormBriefDTO = orderBriefService.queryOrderFormBrief(userId, orderId, null);
		if(null == orderFormBriefDTO) {
			logger.warn("no order for [userId:" + userId + ", retPkgId:" + retPkgId + ", orderId:" + orderId + "]");
		} else {
			retPkgDTO.setOrdFormBriefDTO(orderFormBriefDTO);
		}
		// 2. COD订单的_ReturnCODBankCardInfoDTO
		OrderFormPayMethod payMethod = orderFormBriefDTO.getOrderFormPayMethod();
		if(null != payMethod && payMethod == OrderFormPayMethod.COD) {
			long bankCardRecordId = retPkgDTO.getBankCardInfoId();
			ReturnCODBankCardInfo bankCard = codRetBankCardDao.getObjectByIdAndUserId(bankCardRecordId, userId);
			if(null == bankCard) {
				logger.warn("no bank card for [userId:" + userId + ", retPkgId:" + retPkgId + ", bankCard:" + "]");
			} else {
				retPkgDTO.setBankCard(new ReturnCODBankCardInfoDTO(bankCard));
			}
		}
		// 3. 退货关联的_ReturnFormDTO
		ReturnFormDTO retFormDTO = retFormService.queryReturnFormByUserIdAndOrderId(userId, orderId);
		if(null == retFormDTO) {
			logger.warn("no ReturnForm for [userId:" + userId + ", retPkgId:" + retPkgId + ", orderId:" + orderId + "]");
		} else {
			retPkgDTO.setRetFormDTO(retFormDTO);
		}
		// 4. 退货关联的_OrderPackageSimpleDTO
		long ordPkgId = retPkgDTO.getOrderPkgId();
		OrderPackageSimpleDTO ordPkgDTO = _ordPkgSimpleService.queryOrderPackageSimple(userId, ordPkgId);
		if(null == ordPkgDTO) {
			logger.warn("no OrderPackage for [userId:" + userId + ", retPkgId:" + retPkgId + ", ordPkgId:" + ordPkgId + "]");
		} else {
			retPkgDTO.setOrdPkgDTO(ordPkgDTO);
		}
		// 5. 退货关联的retOrdSkuMap（Map<Long, _ReturnOrderSkuDTO>）
		List<ReturnOrderSku> retOrdSkuList = returnOrderSkuDao.queryObjectsByRetPkgId(retPkgId, userId);
		if(null == retOrdSkuList || 0 == retOrdSkuList.size()) {
			logger.warn("no _ReturnOrderSku for [userId:" + userId + ", retPkgId:" + retPkgId + "]");
		} else {
			Map<Long, OrderSkuDTO> ordSkuMap = null;
			if(null != ordPkgDTO) {
				ordSkuMap = ordPkgDTO.getOrderSkuMap();
			} 
			retPkgDTO.setRetOrdSkuMap(convertToRetOrdSkuDTOMap(retOrdSkuList, ordSkuMap));
		}
		return retPkgDTO;
	}
	
	/**
	 * List<_ReturnPackage> -> List<_ReturnPackageDTO>
	 * 
	 * @param retPkgList
	 * @return
	 */
	protected List<ReturnPackageDTO> convertReturnPackageMetaListToDTOList(List<ReturnPackage> retPkgList) {
		List<ReturnPackageDTO> retPkgDTOList = new ArrayList<ReturnPackageDTO>(0);
		if(null == retPkgList) {
			logger.warn("convertToRetPkgDTOList(...) : retPkgList is null");
			return retPkgDTOList;
		}
		for(ReturnPackage retPkg : retPkgList) {
			ReturnPackageDTO retPkgDTO = null;
			if(null == retPkg || null == (retPkgDTO = convertReturnPackageMetaToDTO(retPkg))) {
				logger.warn("null retPkg in retPkgList or null returned from convertReturnPackageMetaToDTO(retPkg)");
				continue;
			}
			retPkgDTOList.add(retPkgDTO);
		}
		return retPkgDTOList;
	}
	
}
