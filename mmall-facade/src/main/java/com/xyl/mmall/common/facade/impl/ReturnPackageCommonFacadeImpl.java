package com.xyl.mmall.common.facade.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.backend.facade.promotion.PromotionFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.common.facade.ReturnPackageCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam;
import com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam.RetOrdSkuEntity;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.order.api.util.OrderApiUtil;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.dto.OrderPackageSimpleDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.enums.ReturnOrderSkuNumState;
import com.xyl.mmall.order.meta.ReturnForm;
import com.xyl.mmall.order.service.OrderPackageSimpleService;
import com.xyl.mmall.order.service.ReturnFormService;
import com.xyl.mmall.order.service.ReturnPackageQueryService;
import com.xyl.mmall.promotion.service.CouponOrderService;
import com.xyl.mmall.promotion.service.CouponService;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年10月27日 上午9:05:40
 * 
 */
@Facade("returnPackageCommonFacade")
public class ReturnPackageCommonFacadeImpl implements ReturnPackageCommonFacade {

	@Resource
	private PromotionFacade promotionFacade;

	@Resource
	private ReturnFormService retFormService;

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

	@Autowired
	private OrderPackageSimpleService ordPkgSimpleService;

	@Autowired
	private ReturnPackageQueryService retPkgQueryService;

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#totalOrderApplyedReturn(com.xyl.mmall.order.dto.OrderPackageSimpleDTO,
	 *      com.xyl.mmall.mainsite.facade.param._FrontReturnApplyParam)
	 */
	@Override
	public boolean totalOrderApplyedReturn(OrderPackageSimpleDTO ordPkgDTO, _FrontReturnApplyParam param) {
		if (null == ordPkgDTO || null == param) {
			return false;
		}
		Map<Long, OrderSkuDTO> orderSkuMap = ordPkgDTO.getOrderSkuMap();
		List<RetOrdSkuEntity> entityList = param.getList();
		if (CollectionUtil.isEmptyOfMap(orderSkuMap) || CollectionUtil.isEmptyOfList(entityList)) {
			return false;
		}
		Map<Long, RetOrdSkuEntity> entityMap = new HashMap<Long, RetOrdSkuEntity>();
		for (RetOrdSkuEntity entity : entityList) {
			if (null == entity) {
				continue;
			}
			entityMap.put(entity.getProductid(), entity);
		}
		for (Entry<Long, OrderSkuDTO> entry : orderSkuMap.entrySet()) {
			OrderSkuDTO ordSkuDTO = entry.getValue();
			if (null == ordSkuDTO) {
				continue;
			}
			RetOrdSkuEntity entity = entityMap.get(ordSkuDTO.getId());
			if (null == entity || entity.getQuantity() != ordSkuDTO.getTotalCount()) {
				return false;
			}
		}
		long orderId = ordPkgDTO.getOrderId();
		long userId = ordPkgDTO.getUserId();
		int ordPkgNum = ordPkgSimpleService.queryOrderPackageNum(userId, orderId, OrderPackageState.values());
		ReturnForm retForm = retFormService.queryReturnFormByUserIdAndOrderId(userId, orderId);
		// 1. 订单第一次申请退货
		if (null == retForm) {
			return 1 == ordPkgNum;
		}
		// 2. 订单的包裹之前申请过退货
		// 2.1 订单只有一个包裹，该包裹之前中途取消了退货
		if (1 == ordPkgNum) {
			return true;
		}
		// 2.2 检查其他包裹
		// 2.2.1 之前的退货包裹是否都是“全部退货”?
		if (ReturnOrderSkuNumState.APPLY_PACKAGE_FULL_RETURN != retForm.getApplyedNumState()) {
			return false;
		}
		// 2.2.2 该退货包裹是否是最后一个包裹？
		List<ReturnPackageDTO> retPkgDTOList = retPkgQueryService.queryReturnPackageByUserIdAndOrderId(userId, orderId,
				false, null);
		if (CollectionUtil.isEmptyOfList(retPkgDTOList)) {
			// must be a bug here
			return true;
		}
		int retPkgNum = retPkgDTOList.size();
		// 2.2.2.1 不是最后一个包裹
		if (ordPkgNum - retPkgNum > 1) {
			return false;
		}
		// 2.2.2.2 是最后一个包裹
		boolean result = ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN != retForm.getConfirmedNumState();
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#totalOrderApplyedReturn(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	@Override
	public boolean totalOrderApplyedReturn(OrderPackageSimpleDTO ordPkgDTO) {
		if (null == ordPkgDTO) {
			return false;
		}
		long orderId = ordPkgDTO.getOrderId();
		long userId = ordPkgDTO.getUserId();
		ReturnForm retForm = retFormService.queryReturnFormByUserIdAndOrderId(userId, orderId);
		if (null == retForm) {
			return false;
		}
		boolean result = (ReturnOrderSkuNumState.APPLY_ORDER_FULL_RETURN == retForm.getApplyedNumState() && ReturnOrderSkuNumState.CONFIRM_PACKAGE_PART_RETURN != retForm
				.getConfirmedNumState());
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#totalOrderConfirmedReturn(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	@Override
	public boolean totalOrderConfirmedReturn(OrderPackageSimpleDTO ordPkgDTO) {
		if (null == ordPkgDTO) {
			return false;
		}
		long orderId = ordPkgDTO.getOrderId();
		long userId = ordPkgDTO.getUserId();
		ReturnForm retForm = retFormService.queryReturnFormByUserIdAndOrderId(userId, orderId);
		if (null == retForm) {
			return false;
		}
		boolean result = (ReturnOrderSkuNumState.APPLY_ORDER_FULL_RETURN == retForm.getApplyedNumState() && ReturnOrderSkuNumState.CONFIRM_ORDER_FULL_RETURN == retForm
				.getConfirmedNumState());
		return result;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#getPOList(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	@Override
	public List<PODTO> getPOList(OrderPackageSimpleDTO ordPkgDTO) {
		if (null == ordPkgDTO) {
			return new ArrayList<PODTO>();
		}
		List<Long> poIdList = ordPkgDTO.extractPOIdList();
		if (null == poIdList || 0 == poIdList.size()) {
			return new ArrayList<PODTO>();
		}
		POListDTO poListDTO = scheduleService.batchGetScheduleListByIdList(poIdList);
		if (null == poListDTO || null == poListDTO.getPoList()) {
			return new ArrayList<PODTO>();
		}
		return poListDTO.getPoList();
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#getEarliestPOEndTime(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	@Override
	public long getEarliestPOEndTime(OrderPackageSimpleDTO ordPkgDTO) {
		if (null == ordPkgDTO) {
			return 0;
		}
		List<Long> poIdList = ordPkgDTO.extractPOIdList();
		if (null == poIdList || 0 == poIdList.size()) {
			return 0;
		}
		List<PODTO> poList = getPOList(ordPkgDTO);
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
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#getDeadlineOfApplyReturn(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	public long getDeadlineOfApplyReturn(OrderFormDTO orderDTO) {
		// 0.参数判断
		if (orderDTO == null)
			return -1L;
		// 过滤已经取消的包裹
		List<OrderPackageDTO> opDTOList = new ArrayList<>();
		for (OrderPackageDTO packageDTO : orderDTO.getOrderPackageDTOList()) {
			OrderPackageState opState = packageDTO.getOrderPackageState();
			boolean isCancel = OrderPackageState.isCancel(opState);
			if (!isCancel)
				opDTOList.add(packageDTO);
		}
		if (CollectionUtil.isEmptyOfCollection(opDTOList))
			return 1;

		// 1.获得最晚的发货时间+7天
		long deadlineOfExp = 0L;
		for (OrderPackageDTO packageDTO : opDTOList) {
			long expSTime = packageDTO.getExpSTime();
			deadlineOfExp = deadlineOfExp > 0 && deadlineOfExp > expSTime ? deadlineOfExp : expSTime;
		}
		if (deadlineOfExp > 0)
			deadlineOfExp = deadlineOfExp + CalendarConst.DAY_TIME * 7;
		else
			return -2L;

		// 2.订单里最早结束PO的20天后
		long deadlineOfPO = 0L;
		for (OrderPackageDTO packageDTO : opDTOList) {
			OrderPackageSimpleDTO packageSDTO = OrderApiUtil.convertToOrderPackageSimpleDTO(packageDTO);
			long poEndTime = getEarliestPOEndTime(packageSDTO);
			deadlineOfPO = deadlineOfPO > 0 && deadlineOfPO > poEndTime ? deadlineOfPO : poEndTime;
		}
		if (deadlineOfPO > 0)
			deadlineOfPO = deadlineOfPO + CalendarConst.DAY_TIME * 20;

		// 3.最终结果(Max(deadlineOfExp,deadlineOfPO))
		long deadline = deadlineOfExp > deadlineOfPO ? deadlineOfExp : deadlineOfPO;
		deadline = deadline > 0 ? deadline : -2L;
		return deadline;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#getReturnWarehouseForm(com.xyl.mmall.order.dto.OrderPackageSimpleDTO)
	 */
	@Override
	public WarehouseForm getReturnWarehouseForm(OrderPackageSimpleDTO ordPkgDTO) {
		if (null == ordPkgDTO) {
			return new WarehouseForm();
		}
		long warehouseId = ordPkgDTO.getWarehouseId();
		WarehouseForm ret = warehouseService.getWarehouseById(warehouseId);
		return ret;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.common.facade.ReturnPackageCommonFacade#canReopenReturnShowToKF(com.xyl.mmall.order.dto.OrderFormDTO)
	 */
	@Override
	public Map<Long, Boolean> canReopenReturnShowToKF(OrderFormDTO ordForm) {
		Map<Long, Boolean> ret = new HashMap<Long, Boolean>();
		List<OrderPackageDTO> orderPackageDTOList = null;
		if (null == ordForm || CollectionUtil.isEmptyOfList(orderPackageDTOList = ordForm.getOrderPackageDTOList())) {
			return ret;
		}
		for (OrderPackageDTO ordPkg : orderPackageDTOList) {
			OrderPackageSimpleDTO simpleOrdPkg = null;
			if (null == ordPkg || null == (simpleOrdPkg = OrderApiUtil.convertToOrderPackageSimpleDTO(ordPkg))) {
				continue;
			}
			long earliestPOEndTime = getEarliestPOEndTime(simpleOrdPkg);
			boolean show = ordPkgSimpleService.canReopenReturnShowToKF(simpleOrdPkg, earliestPOEndTime);
			ret.put(ordPkg.getPackageId(), show);
		}
		return ret;
	}

}
