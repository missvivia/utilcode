package com.xyl.mmall.backend.facade.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.netease.print.common.util.CalendarUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.backend.facade.JITSupplyManagerFacade;
import com.xyl.mmall.backend.util.DateUtils;
import com.xyl.mmall.backend.util.TimeUtils;
import com.xyl.mmall.backend.vo.FreightCodVO;
import com.xyl.mmall.backend.vo.FreightReverseVO;
import com.xyl.mmall.backend.vo.FreightUserReturnVO;
import com.xyl.mmall.backend.vo.FreightVO;
import com.xyl.mmall.backend.vo.PoOrderStatisticVo;
import com.xyl.mmall.backend.vo.PoReturnOrderVO;
import com.xyl.mmall.backend.vo.PoReturnQueryParamVO;
import com.xyl.mmall.backend.vo.PoReturnSkuVO;
import com.xyl.mmall.backend.vo.PoStatisticListVo;
import com.xyl.mmall.backend.vo.ScheduleVo;
import com.xyl.mmall.backend.vo.WebPKSearchForm;
import com.xyl.mmall.backend.vo.WebPoSearchForm;
import com.xyl.mmall.backend.vo.WebPoSkuSearchForm;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.PoProductFullDTO;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.oms.dto.BusinessPhoneDTO;
import com.xyl.mmall.oms.dto.FreightCodDTO;
import com.xyl.mmall.oms.dto.FreightDTO;
import com.xyl.mmall.oms.dto.FreightReverseDTO;
import com.xyl.mmall.oms.dto.FreightUserReturnDTO;
import com.xyl.mmall.oms.dto.OrderFormOMSDTO;
import com.xyl.mmall.oms.dto.OrderSkuOMSDTO;
import com.xyl.mmall.oms.dto.PageableList;
import com.xyl.mmall.oms.dto.PersonContactInfoDTO;
import com.xyl.mmall.oms.dto.PickOrderBatchDTO;
import com.xyl.mmall.oms.dto.PickOrderDTO;
import com.xyl.mmall.oms.dto.PickSkuDTO;
import com.xyl.mmall.oms.dto.PoOrderDTO;
import com.xyl.mmall.oms.dto.PoOrderReportFormDTO;
import com.xyl.mmall.oms.dto.PoRetrunOrderQueryParamDTO;
import com.xyl.mmall.oms.dto.PoRetrunSkuQueryParamDTO;
import com.xyl.mmall.oms.dto.PoSkuDetailCountDTO;
import com.xyl.mmall.oms.dto.RejectPackageDTO;
import com.xyl.mmall.oms.dto.ReturnOrderFormDTO;
import com.xyl.mmall.oms.dto.ReturnOrderFormSkuDTO;
import com.xyl.mmall.oms.dto.ReturnPoOrderFormDTO;
import com.xyl.mmall.oms.dto.ReturnPoOrderFormSkuDTO;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.dto.WarehouseDTO;
import com.xyl.mmall.oms.dto.WarehouseReturnDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.enums.BuHuoType;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.OmsConstants;
import com.xyl.mmall.oms.enums.OmsOrderFormState;
import com.xyl.mmall.oms.enums.OmsReturnOrderFormState;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.enums.SupplyType;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.meta.BusinessPhoneForm;
import com.xyl.mmall.oms.meta.BusinessPhoneLogForm;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.PoOrderForm;
import com.xyl.mmall.oms.meta.PoOrderFormSku;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.BusinessPhoneService;
import com.xyl.mmall.oms.service.FreightService;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.oms.service.OmsReturnOrderFormService;
import com.xyl.mmall.oms.service.PickOrderService;
import com.xyl.mmall.oms.service.PickSkuService;
import com.xyl.mmall.oms.service.PoOrderService;
import com.xyl.mmall.oms.service.PoReturnService;
import com.xyl.mmall.oms.service.RejectPackageService;
import com.xyl.mmall.oms.service.ShipOrderService;
import com.xyl.mmall.oms.service.ShipSkuService;
import com.xyl.mmall.oms.service.WarehouseReturnService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.service.WarehouseStockInService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnOrderSkuDTO;
import com.xyl.mmall.order.dto.ReturnPackageDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.enums.SupplyMode;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.meta.ScheduleVice;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

/**
 * @author hzzengdan
 */
@Facade
public class JITSupplyManagerFacadeImpl implements JITSupplyManagerFacade {

	@Autowired
	private PickOrderService pickOrderService;

	@Autowired
	private PickSkuService pickSkuService;

	@Autowired
	private PoOrderService poOrderService;

	@Autowired
	private ShipOrderService shipOrderService;

	@Autowired
	private ShipSkuService shipSkuService;

	@Autowired
	private BusinessPhoneService businessPhoneService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private POProductService poProductService;

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Autowired
	private OmsReturnOrderFormService omsReturnOrderFormService;

	@Autowired
	private WarehouseStockInService warehouseStockInService;

	@Autowired
	private PoReturnService poReturnService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private OmsOrderPackageService omsOrderPackageService;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private FreightService freightService;

	@Autowired
	private WarehouseReturnService warehouseReturnService;
	
	@Autowired
	private RejectPackageService rejectPackageService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPickSkuInfo(java.lang.String)
	 */
	@Override
	public List<PickSkuDTO> getPickSkuInfo(String pickOrderId, long supplierId) {
		List<PickSkuDTO> list = pickSkuService.getPickSkuDetail(pickOrderId, supplierId);
		List<PickSkuDTO> result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curr = new Date();
		if (list != null && list.size() > 0) {
			result = new ArrayList<PickSkuDTO>();
			for (PickSkuDTO pickSku : list) {
				ScheduleVO scheduleVo = scheduleFacade.getScheduleById(Long.valueOf(pickSku.getPoOrderId()));
				if (scheduleVo != null && scheduleVo.getPo() != null && scheduleVo.getPo().getScheduleDTO() != null
						&& scheduleVo.getPo().getScheduleDTO().getSchedule() != null) {
					pickSku.setStartTime(scheduleVo.getPo().getScheduleDTO().getSchedule().getStartTime());
					pickSku.setEndTime(scheduleVo.getPo().getScheduleDTO().getSchedule().getEndTime());
					pickSku.setBrandName(ScheduleUtil.getCombinedBrandName(scheduleVo.getPo().getScheduleDTO()
							.getSchedule().getBrandNameEn(), scheduleVo.getPo().getScheduleDTO().getSchedule()
							.getBrandName()));
					WarehouseForm warehouse = warehouseService.getWarehouseById(pickSku.getStoreAreaId());
					if (warehouse != null) {
						pickSku.setWarehouse(warehouse.getWarehouseName());
					}
				}
				pickSku.setExportTime(sdf.format(curr.getTime()));
				pickSku.setMode(pickSku.getPickMoldType().getDesc());
			}
			// sku分组
			Map<String, List<PickSkuDTO>> map = new HashMap<String, List<PickSkuDTO>>();
			for (PickSkuDTO pickSku : list) {
				if (!map.containsKey(pickSku.getSkuId()))
					map.put(pickSku.getSkuId(), new ArrayList<PickSkuDTO>());
				map.get(pickSku.getSkuId()).add(pickSku);
			}
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String skuId = it.next();
				List<PickSkuDTO> skuList = map.get(skuId);
				PickSkuDTO pickSku = skuList.get(0);
				int count = 0;
				for (int i = 0; i < skuList.size(); i++) {
					count += skuList.get(i).getSkuQuantity();
				}
				pickSku.setSkuQuantity(count);
				result.add(pickSku);
			}
		}
		Collections.sort(result, new PickSkuItemForm());
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getShipOrderSkuList(java.lang.String)
	 */
	@Override
	public List<ShipSkuDTO> getShipOrderSkuList(String shipOrderId) {
		return shipSkuService.getShipSkuDetail(shipOrderId);
	}

	/**
	 * 根据条件获取Po单列表
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPoOrderList(com.xyl.mmall.backend.vo.WebPoSearchForm)
	 */
	@Override
	public List<PoOrderDTO> getPoOrderList(WebPoSearchForm searchVo, long supplierId) {
		return poOrderService.getPoOrdersList(searchVo.getPoOrderId(), searchVo.getCreateStartTime(),
				searchVo.getCreateEndTime(), searchVo.getSaleStartTime(), searchVo.getSaleEndTime(), supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPoOrderStatistic(long)
	 */
	@Override
	public List<PoOrderStatisticVo> getPoOrderStatistic(long poOrderId) {
		// 1.找到po下对应的supplierId
		List<PoOrderStatisticVo> list = new ArrayList<PoOrderStatisticVo>();
		PODTO poDTO = scheduleService.getScheduleById(poOrderId);
		ScheduleDTO scheduleDTO = poDTO.getScheduleDTO();
		Schedule schedule = scheduleDTO.getSchedule();
		SupplyType supplyType = scheduleDTO.getScheduleVice().getSupplyMode() == SupplyMode.SELF ? SupplyType.SELF
				: SupplyType.TOGETHOR;

		List<Long> supplierIds = new ArrayList<Long>();
		supplierIds.add(schedule.getSupplierId());
		if (supplyType == SupplyType.TOGETHOR) {
			supplierIds.add(scheduleDTO.getScheduleVice().getBrandSupplierId());
		}

		for (long supplierId : supplierIds) {
			Map<Long, PoOrderStatisticVo> map = new LinkedHashMap<Long, PoOrderStatisticVo>();
			List<ShipSkuItemForm> shipSkuItemFormList = shipSkuService.getShipSkuListByPoOrderId(poOrderId, supplierId);
			if (CollectionUtils.isEmpty(shipSkuItemFormList)) {
				continue;
			}
			WarehouseForm wf = this.warehouseService.getWarehouseById(shipSkuItemFormList.get(0).getStoreAreaId());
			// 仓库数据
			for (ShipSkuItemForm shipSkuItemForm : shipSkuItemFormList) {
				if (shipSkuItemForm.getShipStates() != ShipStateType.RECEIVED) {
					continue;
				}
				long skuId = Long.parseLong(shipSkuItemForm.getSkuId());
				if (!map.containsKey(skuId))
					map.put(skuId, new PoOrderStatisticVo());
				PoOrderStatisticVo vo = map.get(skuId);
				vo.setSupplierId(supplierId);
				vo.setSkuId(skuId);
				// vo.setArrivedNormalCount(vo.getArrivedNormalCount() +
				// shipSkuItemForm.getArrivedNormalCount());
				vo.setArrivedQuantity(vo.getArrivedQuantity() + shipSkuItemForm.getArrivedQuantity());
				// 残次品
				vo.setArrivedDefectiveCount(vo.getArrivedDefectiveCount() + shipSkuItemForm.getArrivedDefectiveCount());
				vo.setWarehouseForm(wf);
			}
			List<PoReturnOrderVO> orderVOs = getReturnVoByPoOrderId(String.valueOf(poOrderId));
			if (CollectionUtils.isEmpty(orderVOs)) {
				list.addAll(map.values());
				continue;
			}

			Iterator<PoReturnOrderVO> iterator = orderVOs.iterator();
			while (iterator.hasNext()) {
				PoReturnOrderVO povo = iterator.next();
				if (PoReturnOrderState.RECEIPTED != povo.getState()) {
					iterator.remove();
					continue;
				}

				if (povo.getSupplierId() != supplierId)
					continue;
				for (PoReturnSkuVO skuvo : povo.getSkuDetails()) {
					long skuId = skuvo.getSkuId();
					PoOrderStatisticVo vo = map.get(skuId);
					// vo.setArrivedDefectiveCount(vo.getArrivedDefectiveCount()
					// + skuvo.getCount());
					if (skuvo.getType() == ReturnType.TWO || skuvo.getType() == ReturnType.THREE) {
						vo.setArrivedRefundCount(vo.getArrivedRefundCount() + skuvo.getCount());
					}
				}
			}
			list.addAll(map.values());
		}
		// 2.统计shipordersku表里的数据
		return list;
	}

	/**
	 * 根据条件获取po单报表统计
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPoReportList(com.xyl.mmall.backend.vo.WebPoSearchForm)
	 */
	@Override
	public List<PoOrderReportFormDTO> getPoReportList(WebPoSearchForm searchVo, long supplierId) {
		String[] poOrderIdList = null;
		if (searchVo.getPoOrderId() != null && !"".equals(searchVo.getPoOrderId()))
			poOrderIdList = searchVo.getPoOrderId().split(",");
		List<PoOrderReportFormDTO> result = new ArrayList<PoOrderReportFormDTO>();
		List<PoOrderForm> poList = poOrderService.getPickOrderListByMultiplePoOrder(poOrderIdList,
				searchVo.getCreateStartTime(), searchVo.getCreateEndTime(), searchVo.getSaleStartTime(),
				searchVo.getSaleEndTime(), supplierId);
		if (poList != null && poList.size() > 0) {
			for (PoOrderForm po : poList) {
				PoOrderReportFormDTO poOrderReport = new PoOrderReportFormDTO();

				List<PickSkuItemForm> pickList = pickSkuService.getPickSkuByPoOrderIdAndOriSupplierId(
						po.getPoOrderId(), po.getSupplierId());

				List<ShipSkuItemForm> shipList = shipSkuService.getShipSkuListByPoOrderId(po.getPoOrderId());

				Collections.sort(pickList, new PickSkuItemForm());
				Collections.sort(shipList, new ShipSkuItemForm());
				poOrderReport.setPoOrderId(po.getPoOrderId());
				// 上线时间-下线时间
				poOrderReport.setOnlineTime(po.getStartTime());
				poOrderReport.setOfflineTime(po.getEndTime());
				// 拣货信息
				if (pickList != null && pickList.size() > 0) {
					// 最后拣货时间
					long lastPickTime = 0L;
					for (PickSkuItemForm pickItem : pickList) {
						if (pickItem.getPickTime() > lastPickTime)
							lastPickTime = pickItem.getPickTime();
					}
					poOrderReport.setLastPickTime(lastPickTime);
					int unPickNum = 0;
					int cumuPickNum = 0;
					for (PickSkuItemForm pickItem : pickList) {

						if (pickItem.getBuhuo() == BuHuoType.BUHUO)
							continue;

						// 未拣货 pickOrderid=0
						if (pickItem.getJITFlag() == JITFlagType.IS_JIT
								&& pickItem.getPickStates() == PickStateType.UNPICK
								&& !com.xyl.mmall.oms.util.OmsUtil.isPicked(pickItem.getPickOrderId()))
							unPickNum += pickItem.getSkuQuantity();
						// 已拣货 pickOrderid!=0
						if (pickItem.getJITFlag() == JITFlagType.IS_JIT
								&& pickItem.getPickStates() == PickStateType.PICKED
								&& com.xyl.mmall.oms.util.OmsUtil.isPicked(pickItem.getPickOrderId()))
							cumuPickNum += pickItem.getSkuQuantity();

					}
					// 未拣货
					poOrderReport.setUnPickNumber(unPickNum);
					// 累计拣货
					poOrderReport.setCumulativePickNumber(cumuPickNum);
					// 累计捡货已送
					int cumuSendPickNum = 0;
					poOrderReport.setCumulativeSendPickNumber(0);
					poOrderReport.setPickingNumber(cumuPickNum - cumuSendPickNum);
				}
				// 发货信息
				if (shipList != null && shipList.size() > 0) {
					// // 最近发货时间
					// Long lastShipTime = 0L;
					// for (ShipSkuItemForm shipItem : shipList) {
					// if (shipItem.getShipTime() > lastShipTime)
					// lastShipTime = shipItem.getShipTime();
					// }
					// poOrderReport.setLastShipTime(lastShipTime);
					int cumuSendNum = 0;
					int cumulativeArrivalNumber = 0;
					for (ShipSkuItemForm shipItem : shipList) {
						if (shipItem.getJITFlag() == JITFlagType.IS_JIT
								&& shipItem.getShipStates() == ShipStateType.RECEIVED) {
							cumuSendNum += shipItem.getSkuQuantity();
							cumulativeArrivalNumber += shipItem.getArrivedQuantity();
						}
					}
					// 发货中数量
					poOrderReport.setShippingNumber(0);
					// 累计发货
					poOrderReport.setCumulativeShipNumber(cumuSendNum);
					// 累计到货
					poOrderReport.setCumulativeArrivalNumber(cumulativeArrivalNumber);
				}
				// 取消数量(根据omsordersku中的poid找到)
				long cancelSkuTotalCountInPo = omsOrderFormService.getCancelSkuCountInPoId(Long.parseLong(poOrderReport
						.getPoOrderId()));
				poOrderReport.setCancelOrdersNumber((int) cancelSkuTotalCountInPo);

				// 有效数量
				long saleSkuTotalCount = omsOrderFormService.getTotalSoldByPoId(Long.parseLong(poOrderReport
						.getPoOrderId())) - cancelSkuTotalCountInPo;

				poOrderReport.setEffectiveOrdersNumber((int) saleSkuTotalCount);
				// 一退
				poOrderReport.setOneReturnNumber(poReturnService.getNReturnSkuCountByPoOrderId(po.getPoOrderId(),
						ReturnType.ONE));
				// 二退
				poOrderReport.setTwoReturnNumber(poReturnService.getNReturnSkuCountByPoOrderId(po.getPoOrderId(),
						ReturnType.TWO));
				// 三退
				poOrderReport.setThreeReturnNumber(poReturnService.getNReturnSkuCountByPoOrderId(po.getPoOrderId(),
						ReturnType.THREE));
				result.add(poOrderReport);
			}
		}
		return result;
	}

	public List<PoOrderForm> f(String[] poOrderId, String createStartTime, String createEndTime,
			String openSaleStartTime, String openSaleEndTime, String stopSaleStartTime, String stopSaleEndTime) {

		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPoStatistic(com.xyl.mmall.backend.vo.WebPoSkuSearchForm)
	 */
	@Override
	public List<PoStatisticListVo> getPoStatistic(WebPoSkuSearchForm searchVo) {
		String veryBeginningTime = OmsConstants.LONG_BEFORE;
		String veryFutrueTime = OmsConstants.LONG_AFTER;
		if (searchVo.getPickStartTime() == null || "".equals(searchVo.getPickStartTime().trim()))
			searchVo.setPickStartTime(veryBeginningTime);
		if (searchVo.getPickEndTime() == null || "".equals(searchVo.getPickEndTime().trim()))
			searchVo.setPickEndTime(veryFutrueTime);
		if (searchVo.getShipStartTime() == null || "".equals(searchVo.getShipStartTime().trim()))
			searchVo.setShipStartTime(veryBeginningTime);
		if (searchVo.getShipEndTime() == null || "".equals(searchVo.getShipEndTime().trim()))
			searchVo.setShipEndTime(veryFutrueTime);
		List<PickSkuItemForm> pickList = pickSkuService.getPickSkuListByPoOrder(searchVo.getPoOrderId(),
				searchVo.getPickOrderId(), searchVo.getPickStartTime(), searchVo.getPickEndTime());
		List<PoStatisticListVo> result = new ArrayList<PoStatisticListVo>();
		PoStatisticListVo poVo = null;
		if (pickList != null && pickList.size() > 0) {
			for (PickSkuItemForm pickSku : pickList) {
				if (searchVo.getPickStates() == pickSku.getPickStates().getIntValue() || searchVo.getPickStates() == 3) {
					poVo = new PoStatisticListVo();
					poVo.setPickOrderId(pickSku.getPickOrderId());
					poVo.setPickStates(pickSku.getPickStates().getIntValue());
					poVo.setPickTime(pickSku.getPickTime());
					poVo.setSkuId(pickSku.getSkuId());
					poVo.setBarcode(pickSku.getCodeNO());
					poVo.setSkuNum(pickSku.getSkuQuantity());
					poVo.setProductName(pickSku.getProductName());
					ShipOrderForm shipOrder = shipOrderService.getShipOrderByPickOrderId(pickSku.getPickOrderId(),
							pickSku.getSupplierId());
					int shipState = -1;
					if (shipOrder != null) {
						shipState = 1;
						poVo.setShipOrderId(shipOrder.getShipOrderId());
						poVo.setShipStates(shipOrder.getShipState().getIntValue());
						poVo.setShipTime(shipOrder.getShipTime());
					}
					if (searchVo.getShipStates() == shipState || searchVo.getShipStates() == 2) {
						result.add(poVo);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 通过pickId获取单个拣货单
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPickOrderByPkId(java.lang.String)
	 */
	@Override
	public PickOrderDTO getPickOrderByPkId(String pickId, long supplierId) {
		return pickOrderService.getPickOrderByPkId(pickId, supplierId);
	}

	/**
	 * 获取单条发货单
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getShipOrder(java.lang.String)
	 */
	@Override
	public ShipOrderDTO getShipOrder(String shipOrderId, long supplierId) {
		return shipOrderService.getShipOrder(shipOrderId, supplierId);
	}

	@Override
	public boolean updatePickOrder(PickOrderDTO pickOrderDTO) {
		return pickOrderService.updatePickOrder(pickOrderDTO);
	}

	@Override
	public boolean saveShipSkuList(List<ShipSkuDTO> list, String shipOrderId, JITFlagType jitFlagType) {
		return shipSkuService.saveShipSkuList(list, shipOrderId, jitFlagType);
	}

	@Override
	public boolean updateShipSkuItem(ShipSkuDTO shipSkuDTO) {
		return shipSkuService.updateShipSkuItem(shipSkuDTO);

	}

	@Override
	public boolean addShipSkuItem(ShipSkuItemForm itemForm) {
		return shipSkuService.addShipSkuItem(itemForm) != null;

	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPoOrderDetail(java.lang.String)
	 */
	@Override
	public PoSkuDetailCountDTO getPoOrderDetail(String poOrderId) {
		return poOrderService.getPoOrderDetail(poOrderId);
	}

	@Override
	public List<BusinessPhoneDTO> getBusinessPhoneFormByBusinessAccount(String businessAccount) {
		List<BusinessPhoneForm> list = businessPhoneService.getBusinessPhoneFormByBusinessAccount(businessAccount);
		List<BusinessPhoneDTO> result = null;
		if (list != null && list.size() > 0) {
			result = new ArrayList<BusinessPhoneDTO>();
			for (BusinessPhoneForm businessPhone : list) {
				BusinessPhoneDTO businessPhoneDTO = new BusinessPhoneDTO();
				businessPhoneDTO.setPhone(businessPhone.getPhone());
				businessPhoneDTO.setBusinessAccount(businessPhone.getBusinessAccount());
				businessPhoneDTO.setCreateTime(businessPhone.getCreateTime());
				businessPhoneDTO.setModifyTime(businessPhone.getModifyTime());
				businessPhoneDTO.setOrderId(businessPhone.getOrderId());
				Business business = businessService.getBusinessAccount(businessPhone.getBusinessAccount());
				if (business != null)
					businessPhoneDTO.setSupplierName(business.getCompanyName());
				result.add(businessPhoneDTO);
			}
		}
		return result;
	}

	@Override
	public BusinessPhoneForm addBusinessPhoneForm(BusinessPhoneForm businessPhone) {
		return businessPhoneService.addBusinessPhoneForm(businessPhone);
	}

	@Override
	public boolean updateBusinessPhoneForm(BusinessPhoneForm businessPhone) {
		return businessPhoneService.updateBusinessPhoneForm(businessPhone);
	}

	@Override
	public BusinessPhoneLogForm addBusinessPhoneLogForm(BusinessPhoneLogForm businessPhoneLog) {
		return businessPhoneService.addBusinessPhoneLogForm(businessPhoneLog);
	}

	@Override
	public List<BusinessPhoneLogForm> getBusinessPhoneLogForm(String businessAccount) {
		return businessPhoneService.getBusinessPhoneLogForm(businessAccount);
	}

	@Override
	public List<PickOrderForm> addPickOrderDtoForPoOrderId(String poOrderId) {
		List<PickOrderForm> list = new ArrayList<PickOrderForm>();
		return list;
	}

	@Override
	public boolean savePoOrderForm(OrderFormDTO orderDTO) {
		OrderFormOMSDTO orderFormOMSDTO = new OrderFormOMSDTO();
		ReflectUtil.convertObj(orderFormOMSDTO, orderDTO, false);
		ReflectUtil.convertObj(orderFormOMSDTO, orderDTO.getOrderExpInfoDTO(), false);
		// 设置cod的标记
		orderFormOMSDTO.setCashOnDelivery(orderDTO.getOrderFormPayMethod() == OrderFormPayMethod.COD);
		Map<Long, OrderSkuDTO> orderSkuDTOMap = orderDTO.mapOrderSkusBySkuId();
		List<OrderSkuOMSDTO> orderSkuOMSDTOList = new ArrayList<OrderSkuOMSDTO>();
		for (OrderSkuDTO orderSkuDTO : orderSkuDTOMap.values()) {
			OrderSkuOMSDTO orderSkuOMSDTO = new OrderSkuOMSDTO();
			ReflectUtil.convertObj(orderSkuOMSDTO, orderSkuDTO, false);
			orderSkuOMSDTOList.add(orderSkuOMSDTO);
		}
		orderFormOMSDTO.setOrderSkuOMSDTOList(orderSkuOMSDTOList);
		orderFormOMSDTO.setOmsOrderFormState(OmsOrderFormState.TOSEND);
		orderFormOMSDTO.setUserOrderFormId(orderDTO.getOrderId());
		if (StringUtils.isBlank(orderFormOMSDTO.getStreet())) {
			orderFormOMSDTO.setStreet("");
		}
		List<PoOrderForm> toCreatePoOrderFormList = new ArrayList<PoOrderForm>();
		// 1.准备数据
		for (OrderSkuOMSDTO orderSkuOMSDTO : orderFormOMSDTO.getOrderSkuOMSDTOList()) {
			// 1.1 对应的poOrderForm是否已經存在
			long poOrderId = orderSkuOMSDTO.getPoId();
			PoOrderForm poOrderForm = poOrderService.getPoOrderById(String.valueOf(poOrderId));
			if (poOrderForm == null) {

				// 1.2获取po信息，准备初始化一個待拣货数量为0的po单
				PODTO poDTO = scheduleService.getScheduleById(poOrderId);
				if (poDTO == null)
					return true;

				ScheduleDTO scheduleDTO = poDTO.getScheduleDTO();
				Schedule schedule = scheduleDTO.getSchedule();
				ScheduleVice scheduleVice = scheduleDTO.getScheduleVice();
				SupplyType supplyType = scheduleDTO.getScheduleVice().getSupplyMode() == SupplyMode.SELF ? SupplyType.SELF
						: SupplyType.TOGETHOR;
				// 1.3档期下的sku明细
				List<POSkuDTO> poSkuDtoList = poProductService.getSkuDTOListByPo(poOrderId);
				List<PoOrderFormSku> poOrderFormSkuList = new ArrayList<PoOrderFormSku>();
				long virtualStock = 0L;
				for (POSkuDTO poSkuDTO : poSkuDtoList) {
					if (poSkuDTO.getStatus() != StatusType.APPROVAL)
						continue;
					virtualStock += poSkuDTO.getSkuNum() + poSkuDTO.getSupplierSkuNum();
					PoOrderFormSku poOrderFormSku = new PoOrderFormSku();
					poOrderFormSku.setPoSkuId(poSkuDTO.getId());
					poOrderFormSku.setPoId(poOrderId);
					poOrderFormSku.setSupplyType(supplyType);
					poOrderFormSku.setSelfSupplierId(schedule.getSupplierId());
					poOrderFormSku.setBackupSupplierId(scheduleVice.getBrandSupplierId());
					poOrderFormSku.setOriSelfStock(poSkuDTO.getSkuNum());
					poOrderFormSku.setOriBackupStock(poSkuDTO.getSupplierSkuNum());
					poOrderFormSku.setCurSelfStock(poOrderFormSku.getOriSelfStock());
					poOrderFormSku.setCurBackupStock(poOrderFormSku.getOriBackupStock());

					if (scheduleVice.getPoType() == 3) {
						// 共同供货
						poOrderFormSku.setSelfStoreAreaId(scheduleVice.getSupplierStoreId());
						poOrderFormSku.setBackupStoreAreaId(scheduleVice.getBrandStoreId());
					} else if (scheduleVice.getPoType() == 1) {
						// 代理商自己供货
						poOrderFormSku.setSelfStoreAreaId(scheduleVice.getSupplierStoreId());
					} else if (scheduleVice.getPoType() == 2) {
						// 品牌商自己供货
						poOrderFormSku.setSelfStoreAreaId(scheduleVice.getBrandStoreId());
					}
					poOrderFormSkuList.add(poOrderFormSku);
				}
				// 1.4初始化一个po单
				poOrderForm = new PoOrderForm();
				poOrderForm.setPoOrderFormSkuList(poOrderFormSkuList);

				poOrderForm.setSupplyType(supplyType);
				poOrderForm.setPoOrderId(String.valueOf(poOrderId));
				poOrderForm.setStartTime(schedule.getStartTime());
				poOrderForm.setEndTime(schedule.getEndTime());
				poOrderForm.setCreateTime(System.currentTimeMillis());
				poOrderForm.setModifyTime(System.currentTimeMillis());
				poOrderForm.setJITFlag(JITFlagType.IS_JIT);
				// poOrderForm.setCurSupplierAreaId(schedule.getSupplierId());
				poOrderForm.setSupplierId(schedule.getSupplierId());
				poOrderForm.setSupplierName(schedule.getSupplierName());
				poOrderForm.setBrandId(schedule.getBrandId());
				poOrderForm.setBrandName(ScheduleUtil.getCombinedBrandName(schedule.getBrandNameEn(),
						schedule.getBrandName()));
				poOrderForm.setStoreAreaId(poOrderFormSkuList.get(0).getSelfStoreAreaId());
				poOrderForm.setVirtualstock(virtualStock);
				toCreatePoOrderFormList.add(poOrderForm);
			}
			List<Long> skuIds = new ArrayList<Long>();
			skuIds.add(orderSkuOMSDTO.getSkuId());
			// 1.5sku属性信息
			List<POSkuDTO> skuDtolist = this.poProductService.getSkuDTOListBySkuId(skuIds);
			POSkuDTO posku = skuDtolist.get(0);

			PoProductFullDTO poProductDTO = poProductService.getProductFullDTO(posku.getProductId());
			String colorNum = poProductDTO.getColorNum();
			String weight = poProductDTO.getWeight();

			orderSkuOMSDTO.setColorName(posku.getColorName() + "/" + colorNum);
			orderSkuOMSDTO.setProductName(posku.getProductName());
			orderSkuOMSDTO.setSize(posku.getSize());
			orderSkuOMSDTO.setBarCode(posku.getBarCode());
			orderSkuOMSDTO.setSupplierId(posku.getSupplierId());
			orderSkuOMSDTO.setStoreAreaId(poOrderForm.getStoreAreaId());
			orderSkuOMSDTO.setWeight(weight);
		}
		// 2.调用service持久出数据
		return this.poOrderService.savePoOrderForm(orderFormOMSDTO, toCreatePoOrderFormList);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#saveReturnOrderForm(com.xyl.mmall.order.dto.ReturnPackageDTO)
	 */
	@Override
	public boolean saveReturnOrderForm(ReturnPackageDTO returnFormDTO) {
		ReturnOrderFormDTO returnOrderFormDTO = new ReturnOrderFormDTO();
		returnOrderFormDTO.setId(returnFormDTO.getRetPkgId());
		returnOrderFormDTO.setOrderId(returnFormDTO.getOrderId());
		// 查询出oms原始包裹id
		long packageId = omsOrderPackageService.getOmsOrderPackageByExpress(returnFormDTO.getOrdPkgDTO().getMailNO(),
				returnFormDTO.getOrdPkgDTO().getExpressCompanyReturn()).getPackageId();
		returnOrderFormDTO.setReturnPackageId(packageId);
		returnOrderFormDTO.setUserId(returnFormDTO.getUserId());
		returnOrderFormDTO.setCtime(returnFormDTO.getCtime());
		returnOrderFormDTO.setMailNO(returnFormDTO.getMailNO());
		returnOrderFormDTO.setExpressCompany(returnFormDTO.getExpressCompany().getCode());
		returnOrderFormDTO.setConfirmTime(0);
		returnOrderFormDTO.setOmsReturnOrderFormState(OmsReturnOrderFormState.SENDED);
		returnOrderFormDTO.setExtInfo("");
		Map<Long, ReturnOrderSkuDTO> retOrdSkuMap = returnFormDTO.getRetOrdSkuMap();
		List<ReturnOrderFormSkuDTO> list = new ArrayList<ReturnOrderFormSkuDTO>();
		for (Long key : retOrdSkuMap.keySet()) {
			ReturnOrderFormSkuDTO returnOrderFormSkuDTO = new ReturnOrderFormSkuDTO();
			ReturnOrderSkuDTO _returnOrderSkuDTO = retOrdSkuMap.get(key);
			// 用户申请的退货明细
			returnOrderFormSkuDTO.setReturnId(_returnOrderSkuDTO.getRetPkgId());
			returnOrderFormSkuDTO.setOrderId(0L);
			returnOrderFormSkuDTO.setOrderSkuId(_returnOrderSkuDTO.getOrderSkuId());
			returnOrderFormSkuDTO.setUserId(_returnOrderSkuDTO.getUserId());
			returnOrderFormSkuDTO.setCtime(_returnOrderSkuDTO.getCtime());
			returnOrderFormSkuDTO.setReturnCount(_returnOrderSkuDTO.getApplyedReturnCount());
			returnOrderFormSkuDTO.setReason(_returnOrderSkuDTO.getReason());
			returnOrderFormSkuDTO.setSkuId(_returnOrderSkuDTO.getSkuId());
			// sku属性信息
			List<Long> skuIds = new ArrayList<Long>();
			skuIds.add(returnOrderFormSkuDTO.getSkuId());
			List<POSkuDTO> skuDtolist = this.poProductService.getSkuDTOListBySkuId(skuIds);
			POSkuDTO posku = skuDtolist.get(0);
			returnOrderFormSkuDTO.setColorName(posku.getColorName());
			returnOrderFormSkuDTO.setProductName(posku.getProductName());
			returnOrderFormSkuDTO.setSize(posku.getSize());
			returnOrderFormSkuDTO.setBarCode(posku.getBarCode());
			returnOrderFormSkuDTO.setPoId(posku.getPoId());
			list.add(returnOrderFormSkuDTO);

		}
		returnOrderFormDTO.setReturnOrderFormSkuDTOList(list);
		returnOrderFormDTO.setExpressCompany(returnFormDTO.getExpressCompany().getCode());
		return omsReturnOrderFormService.saveReturnOrderForm(returnOrderFormDTO);
	}

	@Override
	public boolean warehouseStockInConfirm(WMSShipOrderUpdateDTO wMSShipOrderUpdateDTO) {
		return warehouseStockInService.warehouseStockInConfirm(wMSShipOrderUpdateDTO);
	}

	public PageableList<PoReturnSkuVO> queryPoReturnSku(PoReturnQueryParamVO voParam) {
		// 1.根据上层条件组合成可以在oms中查询的条件
		PoRetrunSkuQueryParamDTO params = createPoRetrunSkuQueryParam(voParam, true);
		if (params == null) {
			return new PageableList<PoReturnSkuVO>();
		}

		// 2.查询未确认退货单列表
		PageableList<WarehouseReturnDTO> orderDTO = poReturnService.queryReturnSku(params);
		PageableList<PoReturnSkuVO> orderVos = new PageableList<PoReturnSkuVO>(ReflectUtil.convertList(
				PoReturnSkuVO.class, orderDTO.getList(), false), orderDTO.getLimit(), orderDTO.getOffset(),
				orderDTO.getTotal());

		// 3.补全VO信息
		appendPoReturnSkuVO(orderVos.getList());
		return orderVos;
	}

	public PageableList<ScheduleVo> queryScheduleVo(PoReturnQueryParamVO params) {
		PageableList<ScheduleVo> svvo = null;
		if (!StringUtils.isBlank(params.getPoOrderId())) {
			// 判断该po下是否有退货需求
			List<ScheduleVo> list = new ArrayList<ScheduleVo>();
			PoRetrunSkuQueryParamDTO skuParams = new PoRetrunSkuQueryParamDTO();
			skuParams.addPoOrderId(Long.parseLong(params.getPoOrderId()));
			if (poReturnService.queryReturnSku(skuParams).getTotal() > 0) {
				ScheduleVo vo = getScheduleVoByPoId(Long.parseLong(params.getPoOrderId()));
				if (vo != null) {
					list.add(vo);
				}
			}
			svvo = new PageableList<ScheduleVo>(list, params.getLimit(), params.getOffset(), list.size());
		} else {
			PageableList<Long> ids = poReturnService.queryPoIdFromReturnSku(params.getLimit(), params.getOffset());
			if (ids != null && ids.getList() != null) {
				List<ScheduleVo> list = new ArrayList<ScheduleVo>();
				for (Long id : ids.getList()) {
					list.add(getScheduleVoByPoId(id));
				}
				svvo = new PageableList<ScheduleVo>(list, params.getLimit(), params.getOffset(), ids.getTotal());
			} else {
				svvo = new PageableList<ScheduleVo>();
			}
		}
		return svvo;
	}

	public ScheduleVo getScheduleVoByPoId(Long poId) {
		ScheduleDTO scheduleDTO = scheduleService.getScheduleById(poId).getScheduleDTO();
		if (scheduleDTO == null) {
			return null;
		}
		Schedule schedule = scheduleDTO.getSchedule();
		ScheduleVice sv = scheduleDTO.getScheduleVice();
		if (schedule == null || sv == null) {
			return null;
		}
		ScheduleVo vo = new ScheduleVo();
		vo.setScheduleId(sv.getScheduleId());
		vo.setSupplierAcct(sv.getSupplierAcct());
		vo.setBrandName(ScheduleUtil.getCombinedBrandName(schedule.getBrandNameEn(), schedule.getBrandName()));
		vo.setCompanyName(schedule.getSupplierName());
		if (sv.getSupplierStoreId() > 0) {
			WarehouseDTO warehouse = warehouseService.getWarehouseById(sv.getSupplierStoreId());
			String supplierWarehouseName = warehouse == null ? "null" : warehouse.getWarehouseName();
			vo.setSupplierWarehouseName(supplierWarehouseName);
		}
		if (sv.getBrandStoreId() > 0) {
			WarehouseDTO warehouse = warehouseService.getWarehouseById(sv.getBrandStoreId());
			String brandWarehouseName = warehouse == null ? "null" : warehouse.getWarehouseName();
			;
			vo.setBrandWarehouseName(brandWarehouseName);
		}
		vo.setStartTime(schedule.getStartTime());
		vo.setEndTime(schedule.getEndTime());
		// 判断是否可以生成退货单
		long now = System.currentTimeMillis();
		vo.setAbleCreate(vo.getEndTime() > 0 ? (now > (vo.getEndTime() + OmsConstants.MILISEC_30DAY)) : false);
		return vo;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#createPoReturnOrder(java.util.List)
	 */
	public void createPoReturnOrder(PoReturnQueryParamVO voParam) {
		PoRetrunSkuQueryParamDTO params = createPoRetrunSkuQueryParam(voParam, false);
		poReturnService.createPoReturnOrder(params);
	}

	public int ceatePoReturnOrderByPoId(long poId) {
		PoRetrunSkuQueryParamDTO params = new PoRetrunSkuQueryParamDTO();
		params.addPoOrderId(poId);
		List<ReturnPoOrderFormDTO> result = poReturnService.createPoReturnOrder(params);
		return result == null ? 0 : result.size();
	}

	public PageableList<PoReturnOrderVO> queryPoReturnOrder(PoReturnQueryParamVO voParam) {
		PoRetrunOrderQueryParamDTO params = new PoRetrunOrderQueryParamDTO();
		if (voParam.getReturnPoOrderId() > 0) {
			params.addPoReturnOrderId(voParam.getReturnPoOrderId());
		}
		if (!StringUtils.isBlank(voParam.getSupplierAccount())) {
			BusinessDTO b = businessFacade.getBusinessDTOByName(voParam.getSupplierAccount());
			if (b == null) {
				return new PageableList<PoReturnOrderVO>();
			} else {
				params.addSupplierId(b.getId());
			}
		}
		if (!StringUtils.isBlank(voParam.getPoOrderId())) {
			params.setPoOrderId(Long.parseLong(voParam.getPoOrderId()));
		} else {
			params.setPoOrderId(0);
		}
		params.addPoReturnOrderState(voParam.getState());
		params.addWarehouseId(voParam.getWarehouseId());
		params.setLimit(voParam.getLimit());
		params.setOffset(voParam.getOffset());
		if (!StringUtils.isBlank(voParam.getTimeend())) {
			params.setPoStartTimeend(TimeUtils.stringToLong(voParam.getTimeend()));
		}
		if (!StringUtils.isBlank(voParam.getTimestar())) {
			params.setPoStartTimestar(TimeUtils.stringToLong(voParam.getTimestar()));
		}
		if (!voParam.isPageable()) {
			params.unPageable();
		}

		// 4.查询未确认退货单列表
		PageableList<ReturnPoOrderFormDTO> orderDTO = poReturnService.queryReturnOrder(params);
		PageableList<PoReturnOrderVO> orderVos = new PageableList<PoReturnOrderVO>(ReflectUtil.convertList(
				PoReturnOrderVO.class, orderDTO.getList(), false), orderDTO.getLimit(), orderDTO.getOffset(),
				orderDTO.getTotal());

		// 5.补全VO信息
		appendPoReturnOrderVO(orderVos.getList());
		return orderVos;
	}

	public PageableList<PoReturnOrderVO> queryPoReturnOrderFormBackend(PoReturnQueryParamVO voParam) {
		PoRetrunOrderQueryParamDTO params = new PoRetrunOrderQueryParamDTO();
		if (!StringUtils.isBlank(voParam.getPoOrderId())) {
			PoRetrunSkuQueryParamDTO skuParams = new PoRetrunSkuQueryParamDTO();
			skuParams.addPoOrderId(Long.parseLong(voParam.getPoOrderId()));
			skuParams.unPageable();
			PageableList<ReturnPoOrderFormSkuDTO> skus = poReturnService.queryReturnOrderSku(skuParams);
			if (skus != null && skus.getList() != null && skus.getList().size() > 0) {
				for (ReturnPoOrderFormSkuDTO sku : skus.getList()) {
					params.addPoReturnOrderId(sku.getPoReturnOrderId());
				}
			} else {
				return new PageableList<PoReturnOrderVO>();
			}
		} else {
			params.setPoOrderId(0);
		}

		if (voParam.getReturnPoOrderId() > 0 && params.getPoReturnOrderIds() != null
				&& params.getPoReturnOrderIds().size() > 0
				&& !params.getPoReturnOrderIds().contains(voParam.getReturnPoOrderId())) {
			return new PageableList<PoReturnOrderVO>();
		} else if (voParam.getReturnPoOrderId() > 0) {
			params.addPoReturnOrderId(voParam.getReturnPoOrderId());
		}

		if (!StringUtils.isBlank(voParam.getSupplierAccount())) {
			BusinessDTO b = businessFacade.getBusinessDTOByName(voParam.getSupplierAccount());
			if (b == null) {
				return new PageableList<PoReturnOrderVO>();
			} else {
				params.addSupplierId(b.getId());
			}
		}
		params.addPoReturnOrderState(voParam.getState());
		params.addWarehouseId(voParam.getWarehouseId());
		params.setLimit(voParam.getLimit());
		params.setOffset(voParam.getOffset());
		if (!StringUtils.isBlank(voParam.getTimeend())) {
			params.setPoStartTimeend(TimeUtils.stringToLong(voParam.getTimeend()));
		}
		if (!StringUtils.isBlank(voParam.getTimestar())) {
			params.setPoStartTimestar(TimeUtils.stringToLong(voParam.getTimestar()));
		}
		if (!voParam.isPageable()) {
			params.unPageable();
		}

		PageableList<ReturnPoOrderFormDTO> orderDTO = poReturnService.queryReturnOrder(params);
		PageableList<PoReturnOrderVO> orderVos = new PageableList<PoReturnOrderVO>(ReflectUtil.convertList(
				PoReturnOrderVO.class, orderDTO.getList(), false), orderDTO.getLimit(), orderDTO.getOffset(),
				orderDTO.getTotal());

		// 补全VO信息
		appendPoReturnOrderVO(orderVos.getList());
		return orderVos;
	}

	/**
	 * 主要是先根据站点信息查询符合站点的商家列表作为oms查询接口的参数
	 * 
	 * @param voParam
	 * @param isPageable
	 *            是否需要支持分页查询
	 * @return
	 */
	private PoRetrunSkuQueryParamDTO createPoRetrunSkuQueryParam(PoReturnQueryParamVO voParam, boolean isPageable) {
		PoRetrunSkuQueryParamDTO params = new PoRetrunSkuQueryParamDTO();
		POListDTO poList = scheduleFacade.getScheduleListByBrandNameOrSupplierAcct(voParam.getPermission(),
				voParam.getProvince(), voParam.getSupplierAccount(), voParam.getBrandName());
		List<Long> poIdList = new ArrayList<Long>();
		if (poList != null && poList.getPoList() != null) {
			for (PODTO po : poList.getPoList()) {
				poIdList.add(po.getScheduleDTO().getSchedule().getId());
			}
		}
		// 如果传入的参数不为空，且没有符合条件的po，则返回null集合
		if (poIdList.size() == 0
				&& (voParam.getProvince() > 0 || !StringUtils.isBlank(voParam.getSupplierAccount()) || !StringUtils
						.isBlank(voParam.getBrandName()))) {
			return null;
		}
		// 2.比较传入的参数和查询出符合条件的po列表是否存在冲突
		Long paramPoOrderId = 0L;
		if (!StringUtils.isBlank(voParam.getPoOrderId())) {
			try {
				paramPoOrderId = Long.parseLong(voParam.getPoOrderId());
			} catch (Exception e) {
			}
		}
		if (paramPoOrderId > 0 && poIdList.size() > 0 && !poIdList.contains(paramPoOrderId)) {
			return null;
		} else if (paramPoOrderId > 0) {
			params.setPoOrderIds(null);
			params.addPoOrderId(paramPoOrderId);
		}
		// 3.根据po列表查询出所有的sku，然后从sku中获取po退货单的列表并组织其他查询条件
		if (poList != null && poList.getPoList() != null) {
			for (PODTO po : poList.getPoList()) {
				params.addPoOrderId(po.getScheduleDTO().getSchedule().getId());
			}
		}
		if (!StringUtils.isBlank(voParam.getSupplierAccount())) {
			BusinessDTO b = businessFacade.getBusinessDTOByName(voParam.getSupplierAccount());
			if (b == null) {
				return null;
			} else {
				params.addSupplierId(b.getId());
			}
		}

		// 2.组织其他查询条件
		params.addWarehouseId(voParam.getWarehouseId());
		params.setLimit(voParam.getLimit());
		params.setOffset(voParam.getOffset());
		if (!StringUtils.isBlank(voParam.getTimeend())) {
			params.setPoStartTimeend(TimeUtils.stringToLong(voParam.getTimeend()));
		}
		if (!StringUtils.isBlank(voParam.getTimestar())) {
			params.setPoStartTimestar(TimeUtils.stringToLong(voParam.getTimestar()));
		}
		params.addReturnType(voParam.getType());
		if (!isPageable) {
			params.unPageable();
		}
		return params;
	}

	private void appendPoReturnOrderVO(List<PoReturnOrderVO> returnOrderVOs) {
		if (returnOrderVOs != null) {
			for (PoReturnOrderVO returnOrderVO : returnOrderVOs) {
				appendPoReturnOrderVO(returnOrderVO);
			}
		}
	}

	private void appendPoReturnOrderVO(PoReturnOrderVO returnOrderVO) {
		if (returnOrderVO == null) {
			return;
		}
		BusinessDTO b = businessService.getBusinessById(returnOrderVO.getSupplierId(), -1);
		BrandDTO brandDTO = brandService.getBrandByBrandId(b.getActingBrandId());
		returnOrderVO.setBrandId(b.getActingBrandId());
		returnOrderVO.setBrandName(ScheduleUtil.getCombinedBrandName(brandDTO.getBrand().getBrandNameEn(), brandDTO
				.getBrand().getBrandNameZh()));
		StringBuilder sb = new StringBuilder();
		if (b.getAreaIds() != null && b.getAreaIds().size() > 0) {
			returnOrderVO.setProvince(b.getAreaIds().get(0));
		}
		if (b.getAreaNames() != null && b.getAreaNames().size() > 0) {
			int i = 0;
			for (; i < b.getAreaNames().size() - 1; i++) {
				sb.append(b.getAreaNames().get(i)).append(",");
			}
			sb.append(b.getAreaNames().get(i));
			returnOrderVO.setProvinceName(sb.toString());
		}
		returnOrderVO.setCompanyName(b.getCompanyName());
		returnOrderVO.setSupplierAccount(b.getBusinessAccount());

		// 填充用户退货地址
		if (returnOrderVO.getReceiverAddress() == null
				|| PoReturnOrderVO.DEFAULT_NULL_LABLE.equals(returnOrderVO.getReceiverAddress())
				|| returnOrderVO.getReceiverAddress().trim().length() == 0) {
			PersonContactInfoDTO returnPerson = new PersonContactInfoDTO();
			returnPerson.setName(b.getReturnContactName());
			returnPerson.setPhone(b.getReturnContactTel());
			returnPerson.setMobile(b.getReturnContactTel());
			returnPerson.setProvince(b.getReturnProvince());
			returnPerson.setCity(b.getReturnCity());
			returnPerson.setDistrict(b.getReturnCountry());
			returnPerson.setAddress(b.getReturnAddress());
			returnOrderVO.setReceiverAddress(returnPerson.getFullAddress());
		}

		WarehouseForm warehouse = warehouseService.getWarehouseById(returnOrderVO.getWarehouseId());
		returnOrderVO.setWarehouseName(warehouse.getWarehouseName());

	}

	private void appendPoReturnSkuVO(List<PoReturnSkuVO> returnSkuVOs) {
		if (returnSkuVOs != null) {
			for (PoReturnSkuVO returnSku : returnSkuVOs) {
				appendPoReturnSkuVO(returnSku);
			}
		}
	}

	private void appendPoReturnSkuVO(PoReturnSkuVO returnSku) {
		if (returnSku == null) {
			return;
		}
		PoOrderForm poOrderForm = poOrderService.getPoOrderById(returnSku.getPoOrderId());
		BusinessDTO b = businessService.getBusinessById(returnSku.getSupplierId(), -1);
		BrandDTO brandDTO = brandService.getBrandByBrandId(b.getActingBrandId());
		List<Long> skuId = new ArrayList<Long>();
		skuId.add(returnSku.getSkuId());
		List<POSkuDTO> poSku = this.poProductService.getSkuDTOListBySkuId(skuId);
		returnSku.setBarCode(poSku.get(0).getBarCode());
		returnSku.setBrandId(b.getActingBrandId());
		returnSku.setBrandName(ScheduleUtil.getCombinedBrandName(brandDTO.getBrand().getBrandNameEn(), brandDTO
				.getBrand().getBrandNameZh()));
		StringBuilder sb = new StringBuilder();
		if (b.getAreaIds() != null && b.getAreaIds().size() > 0) {
			returnSku.setProvince(b.getAreaIds().get(0));
		}
		if (b.getAreaNames() != null && b.getAreaNames().size() > 0) {
			int i = 0;
			for (; i < b.getAreaNames().size() - 1; i++) {
				sb.append(b.getAreaNames().get(i)).append(",");
			}
			sb.append(b.getAreaNames().get(i));
			returnSku.setProvinceName(sb.toString());
		}
		returnSku.setCompanyName(b.getCompanyName());
		returnSku.setSupplierAccount(b.getBusinessAccount());
		returnSku.setTimeend(poOrderForm.getEndTime());
		returnSku.setTimestar(poOrderForm.getStartTime());
		WarehouseForm warehouse = warehouseService.getWarehouseById(returnSku.getWarehouseId());
		returnSku.setWarehouseName(warehouse.getWarehouseName());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getPoReturnOrderForm(java.lang.String)
	 */
	@Override
	public PoReturnOrderVO getReturnPoOrderForm(long formId) {
		ReturnPoOrderFormDTO returnOrderDto = poReturnService.getPoReturnOrder(formId);
		if (returnOrderDto == null) {
			return null;
		}
		PoReturnOrderVO returnOrderVO = ReflectUtil.convertObj(PoReturnOrderVO.class, returnOrderDto, false);
		if (returnOrderVO.getSkuDetails() != null && returnOrderVO.getSkuDetails().size() > 0) {
			List<PoReturnSkuVO> poReturnSkus = ReflectUtil.convertList(PoReturnSkuVO.class,
					returnOrderVO.getSkuDetails(), false);
			appendPoReturnSkuVO(poReturnSkus);
			returnOrderVO.setSkuDetails(poReturnSkus);
		}
		appendPoReturnOrderVO(returnOrderVO);
		return returnOrderVO;
	}

	@Override
	public PoReturnOrderVO getPoReturnOrderByIdAndSupplierId(long formId, long supplierId) {
		ReturnPoOrderFormDTO returnOrderDto = poReturnService.getPoReturnOrder(formId, supplierId);
		if (returnOrderDto == null) {
			return null;
		}
		PoReturnOrderVO returnOrderVO = ReflectUtil.convertObj(PoReturnOrderVO.class, returnOrderDto, false);
		if (returnOrderVO.getSkuDetails() != null && returnOrderVO.getSkuDetails().size() > 0) {
			List<PoReturnSkuVO> poReturnSkus = ReflectUtil.convertList(PoReturnSkuVO.class,
					returnOrderVO.getSkuDetails(), false);
			appendPoReturnSkuVO(poReturnSkus);
			returnOrderVO.setSkuDetails(poReturnSkus);
		}
		appendPoReturnOrderVO(returnOrderVO);
		return returnOrderVO;
	}

	/**
	 * 商家确认退供单
	 * 
	 * @param returnOrderId
	 */
	public boolean confirmPoReturnOrder(long returnOrderId, long supplierId) {
		ReturnPoOrderFormDTO form = poReturnService.getPoReturnOrder(returnOrderId, supplierId);
		if (form == null) {
			return false;
		}
		Business b = businessService.getBusinessById(supplierId, -1);
		PersonContactInfoDTO returnPerson = new PersonContactInfoDTO();
		returnPerson.setName(b.getReturnContactName());
		returnPerson.setPhone(b.getReturnContactTel());
		returnPerson.setMobile(b.getReturnContactTel());
		returnPerson.setProvince(b.getReturnProvince());
		returnPerson.setCity(b.getReturnCity());
		returnPerson.setDistrict(b.getReturnCountry());
		returnPerson.setAddress(b.getReturnAddress());
		form.setReturnPerson(returnPerson);
		return poReturnService.confirmPoReturnOrder(form);
	}

	/**
	 * 商家确认收到货
	 * 
	 * @param returnOrderId
	 */
	public boolean okPoReturnOrder(long returnOrderId, long supplierId) {
		return poReturnService.okPoReturnOrder(returnOrderId, supplierId);
	}

	@Override
	public List<PoReturnOrderVO> getReturnVoByPoOrderId(String poOrderId) {
		List<ReturnPoOrderFormDTO> poReturns = poReturnService.getPoReturnOrderByPoOrderId(Long.parseLong(poOrderId));
		if (poReturns == null) {
			return null;
		}
		List<PoReturnOrderVO> poReturnVos = ReflectUtil.convertList(PoReturnOrderVO.class, poReturns, false);
		if (poReturnVos != null && poReturnVos.size() > 0) {
			for (PoReturnOrderVO t : poReturnVos) {
				List list = t.getSkuDetails();
				int count = 0;
				List<PoReturnSkuVO> tempList = ReflectUtil.convertList(PoReturnSkuVO.class, list, false);
				if (tempList != null && tempList.size() > 0) {
					for (PoReturnSkuVO poVo : tempList) {
						if (poVo.getPoOrderId().equals(poOrderId)) {
							count += poVo.getCount();
						}
					}
				}
				t.setSkuDetails(tempList);
				t.setCount(count);
			}
			appendPoReturnOrderVO(poReturnVos);
		}
		return poReturnVos;
	}

	@Override
	public List<PickOrderBatchDTO> getPickOrderBatchDTO(Long supplierId, WebPKSearchForm searchVo) {
		SimpleDateFormat sdf = new SimpleDateFormat("拣货日期   yyyy-MM-dd 入库仓库：");
		// step1:获取该用户的所有拣货单
		List<PickOrderDTO> pickList = pickOrderService.getPickListBySupplierIdAndTime(supplierId,
				DateUtils.parseToLongtime(searchVo.getCreateStartTime()),
				DateUtils.parseToLongtime(searchVo.getCreateEndTime()));
		List<PickOrderBatchDTO> result = new ArrayList<PickOrderBatchDTO>();
		// step2:根据时间分组key是时间+仓库
		Map<String, List<PickOrderDTO>> map = new HashMap<String, List<PickOrderDTO>>();
		if (pickList != null) {
			for (PickOrderDTO pickOrder : pickList) {
				WarehouseForm warehouse = this.warehouseService.getWarehouseById(pickOrder.getStoreAreaId());
				String key = sdf.format(new Date(pickOrder.getCreateTime())) + warehouse.getWarehouseName();

				if (!map.containsKey(key)) {
					map.put(key, new ArrayList<PickOrderDTO>());
				}
				map.get(key).add(pickOrder);
			}
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				PickOrderBatchDTO pickOrderBatchDTO = new PickOrderBatchDTO();
				pickOrderBatchDTO.setPickDay(CalendarUtil.getZeroHour(map.get(key).get(0).getCreateTime())
						.getTimeInMillis());
				pickOrderBatchDTO.setTitle(key);
				pickOrderBatchDTO.setPickList(map.get(key));
				result.add(pickOrderBatchDTO);
			}
		}
		Collections.sort(result, new PickOrderBatchDTO());
		// 返回列表
		return result;
	}

	@Override
	public List<ShipOrderForm> getShipOrderBySupplierId(long supplierId) {
		List<ShipOrderForm> list = shipOrderService.getShipOrderBySupplierId(supplierId);
		if (list != null && list.size() > 0) {
			for (ShipOrderForm shipOrder : list) {
				shipOrder.setSkuCount(shipSkuService.getTotalSkuTypeByShipOrderId(shipOrder.getShipOrderId(),
						shipOrder.getSupplierId()));
				shipOrder.setTotal(shipSkuService.getTotalCountByShipOrderId(shipOrder.getShipOrderId(), supplierId));
			}
		}
		return list;
	}

	@Override
	public BusinessPhoneForm getBusinessPhone(String businessAccount, String phone) {
		return businessPhoneService.getPhoneOfAccount(businessAccount, phone);
	}

	@Override
	public WarehouseDTO[] getAllWarehouse() {
		return warehouseService.getAllWarehouse();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#queryFreight(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightVO> queryFreight(String expressCompany, long warehouseId, long startDate, long endDate) {
		List<FreightDTO> fs = freightService.queryFreight(expressCompany, warehouseId, startDate, endDate);
		List<FreightVO> result = ReflectUtil.convertList(FreightVO.class, fs, false);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#queryFreightCod(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightCodVO> queryFreightCod(String expressCompany, long warehouseId, long startDate, long endDate) {
		List<FreightCodDTO> cods = freightService.queryFreightCod(expressCompany, warehouseId, startDate, endDate);
		List<FreightCodVO> result = ReflectUtil.convertList(FreightCodVO.class, cods, false);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#queryFreightReverse(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightReverseVO> queryFreightReverse(String expressCompany, long warehouseId, long startDate,
			long endDate) {
		List<FreightReverseDTO> fs = freightService
				.queryFreightReverse(expressCompany, warehouseId, startDate, endDate);
		List<FreightReverseVO> result = ReflectUtil.convertList(FreightReverseVO.class, fs, false);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#queryFreightUserReturn(java.lang.String,
	 *      long, long, long)
	 */
	@Override
	public List<FreightUserReturnVO> queryFreightUserReturn(String expressCompany, long warehouseId, long startDate,
			long endDate) {
		List<FreightUserReturnDTO> fs = freightService.queryFreightUserReturn(expressCompany, warehouseId, startDate,
				endDate);
		List<FreightUserReturnVO> result = ReflectUtil.convertList(FreightUserReturnVO.class, fs, false);
		return result;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#genTwoReturnJob()
	 */
	@Override
	public boolean genTwoReturnJob() {
		return poReturnService.gen2Return();
	}

	@Override
	public List<ShipOrderForm> queryShipOrderFormByTime(long startTime, long endTime) {
		return this.shipOrderService.getListByCollectTime(startTime, endTime, 0, 0);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#getOmsOrderFormListByTimeRange(long,
	 *      long)
	 */
	@Override
	public List<OmsOrderForm> getOmsOrderFormListByTimeRange(long startTime, long endTime) {
		return this.omsOrderFormService.getOmsOrderFormListByTimeRange(startTime, endTime);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#queryReturnOrderForm(OmsReturnOrderFormState)
	 */
	@Override
	public List<ReturnOrderFormDTO> queryReturnOrderForm(OmsReturnOrderFormState state) {

		List<OmsReturnOrderForm> list = omsReturnOrderFormService.getListByConfirmTimeAndState(
				System.currentTimeMillis(), state, 0);
		List<ReturnOrderFormDTO> tdoList = new ArrayList<ReturnOrderFormDTO>();
		for (OmsReturnOrderForm omsReturnOrderForm : list) {
			ReturnOrderFormDTO dto = new ReturnOrderFormDTO();
			ReflectUtil.convertObj(dto, omsReturnOrderForm, false);
			List<OmsOrderForm> orderFormList = this.omsOrderFormService.getOmsOrderFormByUserOrderFormId(
					omsReturnOrderForm.getOrderId(), omsReturnOrderForm.getUserId());
			dto.setConsigneeName(orderFormList.get(0).getConsigneeName());
			dto.setConsigneeMobile(orderFormList.get(0).getConsigneeMobile());
			if (orderFormList.size() == 1) {
				dto.setWarehouseId(orderFormList.get(0).getStoreAreaId());
				dto.setWarehouseSaleId(OmsIdUtils.genEmsOrderId(
						String.valueOf(orderFormList.get(0).getOmsOrderFormId()), WMSOrderType.SALES));
			}
			tdoList.add(dto);
		}
		return tdoList;
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.backend.facade.JITSupplyManagerFacade#queryRejectPackageByCreateTime(long, long)
	 */
	@Override
	public List<RejectPackageDTO> queryRejectPackageByCreateTime(long startTime, long endTime) {
		return rejectPackageService.queryByCreateTime(startTime, endTime);
	}
	
}
