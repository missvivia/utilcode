package com.xyl.mmall.order.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;
import com.xyl.mmall.order.meta.ReturnPackage;

/**
 * 退货包裹记录
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
public class ReturnPackageDTO extends ReturnPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8339295872930363640L;
	
	/**
	 * 
	 */
	public ReturnPackageDTO() {
	}
	
	/**
	 * 
	 * @param obj
	 */
	public ReturnPackageDTO(ReturnPackage obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	private OrderFormBriefDTO ordFormBriefDTO;

	private ReturnCODBankCardInfoDTO bankCard;

	private ReturnFormDTO retFormDTO;
	
	private OrderPackageSimpleDTO ordPkgDTO;
	
	/**
	 * key: orderSkuId, value: _ReturnOrderSkuDTO
	 */
	private Map<Long, ReturnOrderSkuDTO> retOrdSkuMap = new HashMap<Long, ReturnOrderSkuDTO>();
	
	public ReturnOrderSkuNumState applyedSkuNumState() {
		if(null == ordPkgDTO) {
			return null;
		}
		Map<Long, OrderSkuDTO> orderSkuMap = ordPkgDTO.getOrderSkuMap();
		for(Entry<Long, OrderSkuDTO> entry :orderSkuMap.entrySet()) {
			if(null == entry) {
				continue;
			}
			long orderSkuId = entry.getKey();
			OrderSkuDTO orderSkuDTO = entry.getValue();
			if(!retOrdSkuMap.containsKey(orderSkuId)) {
				return ReturnOrderSkuNumState.APPLY_PACKAGE_PART_RETURN;
			}
			ReturnOrderSkuDTO retSkuDTO = retOrdSkuMap.get(orderSkuId);
			if(orderSkuDTO.getTotalCount() > retSkuDTO.getApplyedReturnCount()) {
				return ReturnOrderSkuNumState.APPLY_PACKAGE_PART_RETURN;
			}
		}
		return ReturnOrderSkuNumState.APPLY_PACKAGE_FULL_RETURN;
	}
	
	public ReturnOrderSkuNumState confirmedSkuNumState() {
		ReturnOrderSkuNumState applyedState = null;
		if(null == ordPkgDTO || null == (applyedState = applyedSkuNumState())) {
			return null;
		}
		if(applyedState == ReturnOrderSkuNumState.APPLY_PACKAGE_PART_RETURN) {
			return ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN;
		}
		Map<Long, OrderSkuDTO> orderSkuMap = ordPkgDTO.getOrderSkuMap();
		for(Entry<Long, OrderSkuDTO> entry :orderSkuMap.entrySet()) {
			if(null == entry) {
				continue;
			}
			long orderSkuId = entry.getKey();
			OrderSkuDTO orderSkuDTO = entry.getValue();
			if(!retOrdSkuMap.containsKey(orderSkuId)) {
				return ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN;
			}
			ReturnOrderSkuDTO retSkuDTO = retOrdSkuMap.get(orderSkuId);
			if(orderSkuDTO.getTotalCount() > retSkuDTO.getConfirmCount()) {
				return ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN;
			}
		}
		return ReturnOrderSkuNumState.CONFIRM_PACKAGE_FULL_RETURN;
	}
	
	public List<Long> extractPOIdList() {
		List<Long> poIdList = new ArrayList<Long>();
		Map<Long, OrderSkuDTO> ordSkuMap = null;
		if(null == ordPkgDTO || CollectionUtil.isEmptyOfMap(ordSkuMap = ordPkgDTO.getOrderSkuMap())) {
			return poIdList;
		}
		Set<Long> poIdSet = new HashSet<Long>();
		for(Entry<Long, OrderSkuDTO> entry : ordSkuMap.entrySet()) {
			OrderSkuDTO ordSku = entry.getValue();
			if(null != ordSku) {
				poIdSet.add(ordSku.getPoId());
			}
		}
		for(long poId : poIdSet) {
			poIdList.add(poId);
		}
		return poIdList;
	}
	
	public Map<Long, ReturnOrderSkuDTO> getRetOrdSkuMap() {
		return retOrdSkuMap;
	}

	public void setRetOrdSkuMap(Map<Long, ReturnOrderSkuDTO> retOrdSkuMap) {
		this.retOrdSkuMap = retOrdSkuMap;
	}

	public OrderFormBriefDTO getOrdFormBriefDTO() {
		return ordFormBriefDTO;
	}

	public void setOrdFormBriefDTO(OrderFormBriefDTO ordFormBriefDTO) {
		this.ordFormBriefDTO = ordFormBriefDTO;
	}

	public ReturnFormDTO getRetFormDTO() {
		return retFormDTO;
	}

	public void setRetFormDTO(ReturnFormDTO retFormDTO) {
		this.retFormDTO = retFormDTO;
	}

	public ReturnCODBankCardInfoDTO getBankCard() {
		return bankCard;
	}

	public void setBankCard(ReturnCODBankCardInfoDTO bankCard) {
		this.bankCard = bankCard;
	}

	public OrderPackageSimpleDTO getOrdPkgDTO() {
		return ordPkgDTO;
	}

	public void setOrdPkgDTO(OrderPackageSimpleDTO ordPkgDTO) {
		this.ordPkgDTO = ordPkgDTO;
	}

}
