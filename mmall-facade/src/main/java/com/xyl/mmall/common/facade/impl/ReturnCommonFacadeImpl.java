package com.xyl.mmall.common.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.ReturnCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.param.RetOrdSkuPriceCalParam;
import com.xyl.mmall.promotion.dto.CouponDTO;
import com.xyl.mmall.promotion.dto.FavorCaculateParamDTO;
import com.xyl.mmall.promotion.dto.PromotionDTO;
import com.xyl.mmall.promotion.dto.PromotionSkuItemDTO;
import com.xyl.mmall.promotion.meta.Coupon;
import com.xyl.mmall.promotion.meta.CouponOrder;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 上午9:05:40
 * 
 */
@Deprecated
@Facade("returnCommonFacade")
public class ReturnCommonFacadeImpl implements ReturnCommonFacade {

	@Resource
	private PromotionFacade promotionFacade;

	@Resource
	private CouponOrderService couponOrderService;

	@Resource
	private ScheduleService scheduleService;

	@Resource
	private WarehouseService warehouseService;

	@Resource
	private OrderFacade orderFacade;
	
	@Autowired
	private CouponService couponService;

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.common.facade.ReturnCommonFacade#extractPromotion(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public Map<Long, PromotionDTO> extractPromotion(OrderFormDTO ordFormDTO) {
		Map<Long, PromotionDTO> ret = new HashMap<Long, PromotionDTO>();
		if(null == ordFormDTO) {
			return ret;
		}
		Map<Long, Long> poPromotionIdMap = ordFormDTO.mapPromotionByPOId(false);
		Map<Long, Long> poPromotionIndexMap = ordFormDTO.mapPromotionByPOId(true);
		for(Entry<Long, Long> entry : poPromotionIdMap.entrySet()) {
			if(null == entry) {
				continue;
			}
			long poId = entry.getKey();
			long promotionId = entry.getValue();
			if(!poPromotionIndexMap.containsKey(poId)) {
				continue;
			}
			long promotionIndex = poPromotionIndexMap.get(poId);
			PromotionDTO promotion = promotionFacade.getPromotionById(promotionId);
			if(null == promotion) {
				continue;
			}
			// rhy说没关系...
			promotion.setIndex((int) promotionIndex);
			ret.put(poId, promotion);
		}
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnCommonFacade#extractRefindItems(java.util.List,
	 *      com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public Map<Long, List<PromotionSkuItemDTO>> extractRefindItems(List<RetOrdSkuPriceCalParam> retOrdSkuList,
			OrderFormDTO ordFormDTO) {
		Map<Long, List<PromotionSkuItemDTO>> items = new HashMap<Long, List<PromotionSkuItemDTO>>();
		if (null == retOrdSkuList || null == ordFormDTO) {
			return items;
		}
		Map<Long, OrderSkuDTO> allSku = ordFormDTO.mapOrderSkusByTheirId();
		if (null == allSku) {
			return items;
		}
		for (RetOrdSkuPriceCalParam retOrdSku : retOrdSkuList) {
			long orderSkuId = retOrdSku.idOfOrderSku();
			OrderSkuDTO ordSkuDTO = allSku.get(orderSkuId);
			if (null == ordSkuDTO) {
				continue;
			}
			// to be continued：ugly code
			int returnCount = (int) retOrdSku.countOfReturn(true);
			PromotionSkuItemDTO psiDTO = orderSkuToPromotionSkuItem(ordSkuDTO, returnCount);
			long poId = ordSkuDTO.getPoId();
			if (!items.containsKey(poId)) {
				items.put(poId, new ArrayList<PromotionSkuItemDTO>());
			}
			items.get(poId).add(psiDTO);
		}
		return items;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnCommonFacade#filterRefindItems(java.util.List,
	 *      com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public Map<Long, List<PromotionSkuItemDTO>> filterRefindItems(List<RetOrdSkuPriceCalParam> retOrdSkuList,
			OrderFormDTO ordFormDTO) {
		Map<Long, List<PromotionSkuItemDTO>> items = new HashMap<Long, List<PromotionSkuItemDTO>>();
		if (null == retOrdSkuList || null == ordFormDTO) {
			return items;
		}
		Map<Long, OrderSkuDTO> allSku = ordFormDTO.mapOrderSkusByTheirId();
		if (null == allSku) {
			return items;
		}
		// orderSkuId -> ReturnOrderSkuDTO
		Map<Long, RetOrdSkuPriceCalParam> mapedEntity = new HashMap<Long, RetOrdSkuPriceCalParam>(retOrdSkuList.size());
		for (RetOrdSkuPriceCalParam retOrdSku : retOrdSkuList) {
			if (null == retOrdSku) {
				continue;
			}
			long orderSkuId = retOrdSku.idOfOrderSku();
			mapedEntity.put(orderSkuId, retOrdSku);
		}
		for (Entry<Long, OrderSkuDTO> entry : allSku.entrySet()) {
			long orderSkuId = entry.getKey();
			OrderSkuDTO ordSkuDTO = entry.getValue();
			if (null == ordSkuDTO) {
				continue;
			}
			PromotionSkuItemDTO psiDTO = null;
			RetOrdSkuPriceCalParam retEntity = mapedEntity.get(orderSkuId);
			if (null == retEntity) {
				// 1. 不在退货申请列表中
				psiDTO = orderSkuToPromotionSkuItem(ordSkuDTO, ordSkuDTO.getTotalCount());
			} else if (retEntity.countOfReturn(true) < ordSkuDTO.getTotalCount()) {
				// 2. 在申请列表中，且没有全部退
				// to be continued：ugly code
				int adjustCount = (int) (ordSkuDTO.getTotalCount() - retEntity.countOfReturn(true));
				psiDTO = orderSkuToPromotionSkuItem(ordSkuDTO, adjustCount);
			} else {
				// 3. 在申请列表中，且全部退
			}
			if (null != psiDTO) {
				long poId = ordSkuDTO.getPoId();
				if (!items.containsKey(poId)) {
					items.put(poId, new ArrayList<PromotionSkuItemDTO>());
				}
				items.get(poId).add(psiDTO);
			}
		}
		return items;
	}

	/**
	 * ordSkuDTO -> PromotionSkuItemDTO
	 * 
	 * @param ordSkuDTO
	 * @param adjustCount
	 * @return
	 */
	@Override
	public PromotionSkuItemDTO orderSkuToPromotionSkuItem(OrderSkuDTO ordSkuDTO, int adjustCount) {
		PromotionSkuItemDTO psiDTO = new PromotionSkuItemDTO();
		if (null == ordSkuDTO) {
			return psiDTO;
		}
		psiDTO.setSkuId(ordSkuDTO.getSkuId());
		psiDTO.setUserId(ordSkuDTO.getUserId());
		psiDTO.setCount(adjustCount);
		psiDTO.setOriRetailPrice(ordSkuDTO.getOriRPrice());
		psiDTO.setRetailPrice(ordSkuDTO.getRprice());
		psiDTO.setCartPrice(ordSkuDTO.getRprice());
		psiDTO.setPromotionDiscountAmount(ordSkuDTO.getHdSPrice());
		psiDTO.setCouponDiscountAmount(ordSkuDTO.getCouponSPrice());
		return psiDTO;
	}

	/**
	 * 构造FavorCaculateParamDTO
	 * 
	 * @param ordFormDTO
	 * @return
	 */
	@Override
	public FavorCaculateParamDTO extractFavorCaculateParam(OrderFormDTO ordFormDTO) {
		FavorCaculateParamDTO param = new FavorCaculateParamDTO();
		if (null == ordFormDTO) {
			return param;
		}
		long userId = ordFormDTO.getUserId();
		long orderId = ordFormDTO.getOrderId();
		List<Long> poIdList = ordFormDTO.extractPOIdList();
		CouponOrder couponOrder = couponOrderService.getCouponOrderByOrderIdOfUseType(userId, orderId);
		if (null != couponOrder) {
			Coupon coupon = couponService.getCouponByCode(couponOrder.getCouponCode(), false);
			CouponDTO couponDTO = new CouponDTO(coupon);
			couponDTO.setUserCouponId(couponOrder.getUserCouponId());
			couponDTO.setIndex(couponOrder.getCouponIdx());
			for(long poId : poIdList) {
				param.getPoCouponMap().put(poId, couponDTO);
			}
		}
		param.setProvince(ordFormDTO.getProvinceId());
		param.setUserId(ordFormDTO.getUserId());
		// param.setPoPromotionMap(promotionFacade.getPromotionListByPos(poIdList, false));
		param.setPoPromotionMap(extractPromotion(ordFormDTO));
		return param;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnCommonFacade#getEarliestPOEndTime(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public long getEarliestPOEndTime(OrderFormDTO ordFormDTO) {
		if (null == ordFormDTO) {
			return 0;
		}
		List<Long> poIdList = ordFormDTO.extractPOIdList();
		if (null == poIdList || 0 == poIdList.size()) {
			return 0;
		}
		List<PODTO> poList = orderFacade.getPOList(ordFormDTO);
		boolean first = true;
		long retEndTime = 0;
		for (PODTO po : poList) {
			ScheduleDTO schedDTO = po.getScheduleDTO();
			if (null == schedDTO || null == schedDTO.getSchedule()) {
				continue;
			}
			long endTime = schedDTO.getSchedule().getEndTime();
			if (first) {
				retEndTime = endTime;
				first = false;
				continue;
			}
			if (endTime < retEndTime) {
				retEndTime = endTime;
			}
		}
		return retEndTime;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnCommonFacade#getDeadlineOfApplyReturn(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	public long getDeadlineOfApplyReturn(OrderFormDTO orderDTO) {
		// 0.参数判断
		if (orderDTO == null)
			return -1L;

		// 1.
		long deadline = 0L;

		return deadline;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnCommonFacade#getReturnWarehouseForm(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public WarehouseForm getReturnWarehouseForm(OrderFormDTO ordFormDTO) {
		if (null == ordFormDTO) {
			return new WarehouseForm();
		}
		List<Long> aireIdList = poAireIdList(ordFormDTO.extractPOIdList());
		if (null == aireIdList || 0 == aireIdList.size()) {
			return new WarehouseForm();
		}
		// 暂时使用这种方案
		return warehouseService.getWarehouseByArea(aireIdList.get(0));
	}

	/**
	 * 根据poId获取仓库站点Id
	 * 
	 * @param poIdList
	 * @return
	 */
	private List<Long> poAireIdList(List<Long> poIdList) {
		List<Long> aireIdList = new ArrayList<Long>();
		if (null == poIdList) {
			return aireIdList;
		}
		ScheduleCommonParamDTO paramDTO = new ScheduleCommonParamDTO();
		paramDTO.poIdList = poIdList;
		POListDTO poListDTO = scheduleService.getScheduleList(paramDTO);
		if (null == poListDTO || null == poListDTO.getPoList()) {
			return aireIdList;
		}
		Set<Long> aireIdSet = new HashSet<Long>();
		for (PODTO po : poListDTO.getPoList()) {
			ScheduleDTO schedDTO = po.getScheduleDTO();
			Schedule sched = null;
			if (null == schedDTO || null == (sched = schedDTO.getSchedule())) {
				continue;
			}
			aireIdSet.add(sched.getStoreAreaId());
		}
		for (long aireId : aireIdSet) {
			aireIdList.add(aireId);
		}
		return aireIdList;
	}
}
