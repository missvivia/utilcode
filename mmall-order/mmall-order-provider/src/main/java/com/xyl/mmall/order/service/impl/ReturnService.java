package com.xyl.mmall.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.order.dao.DeprecatedReturnCODBankCardInfoDao;
import com.xyl.mmall.order.dao.DeprecatedReturnFormDao;
import com.xyl.mmall.order.dao.DeprecatedReturnOrderSkuDao;
import com.xyl.mmall.order.dao.OrderFormDao;
import com.xyl.mmall.order.dao.OrderPackageDao;
import com.xyl.mmall.order.dao.OrderSkuDao;
import com.xyl.mmall.order.dto.DeprecatedReturnFormDTO;
import com.xyl.mmall.order.dto.DeprecatedReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.meta.DeprecatedReturnForm;
import com.xyl.mmall.order.meta.DeprecatedReturnOrderSku;
import com.xyl.mmall.order.service.OrderService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年9月16日 下午7:25:03
 *
 */
@Deprecated
class ReturnService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected OrderFormDao orderFormDao;
	
	@Autowired
	protected OrderPackageDao orderPackageDao;
	
	@Autowired
	protected OrderSkuDao orderSkuDao;
	
	@Autowired
	protected DeprecatedReturnFormDao returnFormDao;
	
	@Autowired
	protected DeprecatedReturnOrderSkuDao returnOrderSkuDao;
	
	@Autowired
	protected DeprecatedReturnCODBankCardInfoDao codRetBankCardDao;
	
	@Autowired
	protected OrderService orderService;
	
	/**
	 * List<ReturnOrderSku> -> List<ReturnOrderSkuDTO> 
	 * 
	 * @param retOrdSkuList：ReturnOrderSku列表
	 * @param allOrdSku：订单中所有的OrderSku
	 * @return
	 */
	protected List<DeprecatedReturnOrderSkuDTO> convertToRetOrdSkuDTOList(
			List<DeprecatedReturnOrderSku> retOrdSkuList, Map<Long, OrderSkuDTO> allOrdSku) {
		if(null == retOrdSkuList || null == allOrdSku) {
			logger.warn("convertToRetOrdSkuDTOList(...) : retOrdSkuList or allOrdSku is null");
			return new ArrayList<DeprecatedReturnOrderSkuDTO>();
		}
		List<DeprecatedReturnOrderSkuDTO> ret = new ArrayList<DeprecatedReturnOrderSkuDTO>(retOrdSkuList.size());
		for(DeprecatedReturnOrderSku retOrderSku : retOrdSkuList) {
			OrderSkuDTO ordSkuDTO = null;
			if(null == retOrderSku || null == (ordSkuDTO = allOrdSku.get(retOrderSku.getOrderSkuId()))) {
				logger.warn("null retOrderSku in retOrdSkuList or null ordSkuDTO in allOrdSku");
				continue;
			}
			DeprecatedReturnOrderSkuDTO retOrderSkuDTO = new DeprecatedReturnOrderSkuDTO(retOrderSku);
			retOrderSkuDTO.setOrdSkuDTO(ordSkuDTO);
			ret.add(retOrderSkuDTO);
		}
		return ret;
	}
	
	/**
	 * ReturnForm -> ReturnFormDTO
	 * 
	 * @param retForm
	 * @return
	 */
	protected DeprecatedReturnFormDTO convertReturnFormMetaToDTO(DeprecatedReturnForm retForm) {
		if(null == retForm) {
			return null;
		}
		long orderId = retForm.getOrderId();
		long userId = retForm.getUserId();
		DeprecatedReturnFormDTO retFormDTO = new DeprecatedReturnFormDTO(retForm);
		/** 1. 退货关联的Order */
		OrderFormDTO orderFormDTO = orderService.queryOrderForm(userId, orderId, null);
		if(null == orderFormDTO) {
			logger.warn("no order for [userId:" + userId + ", orderId" + orderId + "]");
			return retFormDTO;
		}
		retFormDTO.setOrderFormDTO(orderFormDTO);
		/** 2. 订单中退回的OrderSku */
		Map<Long, OrderSkuDTO> allOrdSku = orderFormDTO.mapOrderSkusByTheirId();
		List<DeprecatedReturnOrderSku> retOrderSkuList = returnOrderSkuDao.queryRetOrdSkuListByReturnId(retForm.getId());
		retFormDTO.setRetOrderSkuList(convertToRetOrdSkuDTOList(retOrderSkuList, allOrdSku));
		return retFormDTO;
	}
	
	/**
	 * List<ReturnForm> -> List<ReturnFormDTO>
	 * 
	 * @param retFormList
	 * @return
	 */
	protected List<DeprecatedReturnFormDTO> convertToRetFormDTOList(List<DeprecatedReturnForm> retFormList) {
		if(null == retFormList) {
			logger.warn("convertToRetFormDTOList(...) : retFormList is null");
			return new ArrayList<DeprecatedReturnFormDTO>();
		}
		List<DeprecatedReturnFormDTO> retFormDTOList = new ArrayList<DeprecatedReturnFormDTO>(retFormList.size());
		for(DeprecatedReturnForm retForm : retFormList) {
			DeprecatedReturnFormDTO retFormDTO = null;
			if(null == retForm || null == (retFormDTO = convertReturnFormMetaToDTO(retForm))) {
				logger.warn("null retForm in retFormList or null returned from convertReturnFormMetaToDTO(retForm)");
				continue;
			}
			retFormDTOList.add(retFormDTO);
		}
		return retFormDTOList;
	}
	
}
