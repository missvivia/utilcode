package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.IReThrowException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsSkuDao;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.dao.WarehouseReturnDao;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.BuHuoType;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.PickStateType;
import com.xyl.mmall.oms.enums.ReturnType;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WMSShipOrderState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.meta.WarehouseReturn;
import com.xyl.mmall.oms.service.ShipOrderService;
import com.xyl.mmall.oms.service.ShipSkuService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.service.WarehouseStockInService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * 
 * @author hzliujie 2014年10月22日 下午4:27:46
 */
@Service("warehouseStockInService")
public class WarehouseStockInServiceImpl implements WarehouseStockInService {
	@Autowired
	private WarehouseReturnDao warehouseReturnDao;

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private ShipSkuDao shipSkuDao;

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private ShipSkuService shipSkuService;

	@Autowired
	private ShipOrderService shipOrderService;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private OmsSkuDao omsSkuDao;

	@Autowired
	private WarehouseService warehouseService;

	private void richBuhuoSku(PickSkuItemForm pickSkuItemForm) {
		// 先补全数据
		PickSkuItemForm shipSku = pickSkuDao.getShipSkuBySkuIdAndPoId(pickSkuItemForm.getSkuId(),
				pickSkuItemForm.getPoOrderId());
		pickSkuItemForm.setPickOrderId("0");
		pickSkuItemForm.setPickStates(PickStateType.UNPICK);
		pickSkuItemForm.setPickTime(0L);
		pickSkuItemForm.setBuhuo(BuHuoType.BUHUO);
		pickSkuItemForm.setOmsOrderFormId(shipSku.getOmsOrderFormId());
		pickSkuItemForm.setCodeNO(shipSku.getCodeNO());
		pickSkuItemForm.setColor(shipSku.getColor());
		pickSkuItemForm.setSize(shipSku.getSize());
		pickSkuItemForm.setProductName(shipSku.getProductName());
		pickSkuItemForm.setCreateTime(System.currentTimeMillis());
		pickSkuItemForm.setOriSupplierId(shipSku.getOriSupplierId());

	}

	private ShipSkuItemForm seekShipSkuItemForm(List<ShipSkuItemForm> shipSkus, String skuId) {
		for (ShipSkuItemForm shipSku : shipSkus) {
			if (shipSku.getSkuId().equals(skuId)) {
				return shipSku;
			}
		}
		return null;
	}

	/**
	 * 商品入库确认操作
	 * 
	 * @param wMSShipOrderUpdateDTO
	 * @return
	 */
	private boolean doStockInConfirm(WMSShipOrderUpdateDTO wMSShipOrderUpdateDTO) {
		// 1.基本检查
		String emsOrderId = OmsIdUtils.genEmsOrderId(wMSShipOrderUpdateDTO.getOrderId(),
				wMSShipOrderUpdateDTO.getOrderType());
		if (wMSShipOrderUpdateDTO.getSkuDetails() == null) {
			throw new WarehouseCallerException("%s[%s]没有SKU明细.", wMSShipOrderUpdateDTO.getOrderType().getDesc(), emsOrderId);
		}
		ShipOrderForm shipOrder = shipOrderDao.getShipOrderByShipId(wMSShipOrderUpdateDTO.getOrderId());
		if (shipOrder == null) {
			throw new WarehouseCallerException("%s[%s]有误， 不存在该入库单.", wMSShipOrderUpdateDTO.getOrderType().getDesc(), emsOrderId);
		}
		if (shipOrder.getShipState() == ShipStateType.RECEIVED) {
			return true;
		}
		boolean isSingleOrder = OmsIdUtils.backPickMoldType(shipOrder.getShipOrderId()) == PickMoldType.SINGLE;
		List<ShipSkuItemForm> shipSkus = shipSkuDao.getShipSkuList(shipOrder.getShipOrderId());
		// 2.更新入库单数据和状态
		shipOrder.setArrivalTime(wMSShipOrderUpdateDTO.getOperaterTime());
		shipOrder.setArrivedCount(wMSShipOrderUpdateDTO.getCount());
		shipOrder.setShipState(ShipStateType.RECEIVED);
		boolean isSuccess = shipOrderDao.updateWarehoureInfo(shipOrder);
		List<Long> wmsSkus = new ArrayList<Long>(); 
		for (WMSSkuDetailDTO wmsSku : wMSShipOrderUpdateDTO.getSkuDetails()) {
			if (wmsSkus.contains(wmsSku.getSkuId())) {
				throw new WarehouseCallerException("入库单[%s]sku[%s]重复.", emsOrderId, wmsSku.getSkuId());
			} else {
				wmsSkus.add(wmsSku.getSkuId());
			}
			ShipSkuItemForm shipSku = new ShipSkuItemForm();
			shipSku.setSkuId(String.valueOf(wmsSku.getSkuId()));
			shipSku.setShipOrderId(shipOrder.getShipOrderId());
			shipSku.setShipStates(ShipStateType.RECEIVED);
			shipSku.setArrivedQuantity(wmsSku.getCount());
			shipSku.setArrivedNormalCount(wmsSku.getNormalCount());
			shipSku.setArrivedDefectiveCount(wmsSku.getDefectiveCount());
			// 如果商品不存在，则是多发的商品，不需要更新
			if (seekShipSkuItemForm(shipSkus, shipSku.getSkuId()) != null) {
				isSuccess &= shipSkuDao.updateWarehoureInfo(shipSku);
			} else if (omsSkuDao.getObjectById(wmsSku.getSkuId()) == null) {
				throw new WarehouseCallerException("入库单号[%s]的sku[%s]不存在.", emsOrderId, wmsSku.getSkuId());
			}
		}
		// 3.补货和退货流程
		List<PickSkuItemForm> buhuoPickSku = new ArrayList<PickSkuItemForm>();
		int totalBuhuoCount = 0;
		for (WMSSkuDetailDTO wmsSku : wMSShipOrderUpdateDTO.getSkuDetails()) {
			// --3.1 计算应该补货和退货的商品数量
			String skuId = String.valueOf(wmsSku.getSkuId());
			ShipSkuItemForm shipSku = seekShipSkuItemForm(shipSkus, skuId);
			int buhuoCount = shipSku == null ? 0 : (shipSku.getSkuQuantity() - wmsSku.getCount() + wmsSku
					.getDefectiveCount());
			buhuoCount = buhuoCount < 0 ? 0 : buhuoCount;

			int returnCount = shipSku == null ? wmsSku.getCount() : (wmsSku.getCount() + buhuoCount - shipSku
					.getSkuQuantity());

			if (shipSku == null) {
				throw new WarehouseCallerException("入库单[%s]不需要该sku[%s].", emsOrderId, wmsSku.getSkuId());
			}
			if (wmsSku.getCount() > shipSku.getSkuQuantity()) {
				throw new WarehouseCallerException("入库单[%s]的sku[%s]数量超出，应为:[%s],却为:[%s]", emsOrderId, wmsSku.getSkuId(),shipSku.getSkuQuantity(),wmsSku.getCount());
			}
			totalBuhuoCount += buhuoCount;
			// --3.2发货不足(包括少发货或有次品)，则补货
			if (buhuoCount > 0) {
				PickSkuItemForm pickSkuItemForm = new PickSkuItemForm();
				pickSkuItemForm.setSkuId(skuId);
				pickSkuItemForm.setPoOrderId(shipSku == null ? "" : shipSku.getPoOrderId());
				pickSkuItemForm.setSkuQuantity(buhuoCount);
				pickSkuItemForm.setPickMoldType(isSingleOrder ? PickMoldType.SINGLE : PickMoldType.MANY);
				pickSkuItemForm.setJITFlag(shipOrder.getJITFlag());
				pickSkuItemForm.setStoreAreaId(shipOrder.getStoreAreaId());
				pickSkuItemForm.setSupplierId(shipOrder.getSupplierId());
				richBuhuoSku(pickSkuItemForm);
				buhuoPickSku.add(pickSkuItemForm);
			}
			// --3.3多发的货物，回退
			if (returnCount > 0) {
				WarehouseReturn warehouseReturn = new WarehouseReturn();
				OmsOrderFormSku orderSku = omsOrderFormSkuDao.getBySupplierIdAndSkuId(shipOrder.getSupplierId(),
						wmsSku.getSkuId());
				warehouseReturn.setWarehouseId(orderSku.getStoreAreaId());
				warehouseReturn.setSkuId(wmsSku.getSkuId());
				warehouseReturn.setPoOrderId(String.valueOf(orderSku.getPoId()));
				warehouseReturn.setProductName(orderSku.getProductName());
				warehouseReturn.setCount(returnCount);
				warehouseReturn.setDefectiveCount(wmsSku.getDefectiveCount());
				warehouseReturn.setNormalCount(returnCount - wmsSku.getDefectiveCount());
				warehouseReturn.setType(ReturnType.ONE);
				warehouseReturn.setSupplierId(shipOrder.getSupplierId());
				warehouseReturn.setCreateTime(System.currentTimeMillis());
				isSuccess &= warehouseReturnDao.addObject(warehouseReturn) != null;
			}
		}
		// --3.4部分商品未发货的补货情况，则补货
		for (ShipSkuItemForm shipSku : shipSkus) {
			boolean isExist = false;
			for (WMSSkuDetailDTO wmsSku : wMSShipOrderUpdateDTO.getSkuDetails()) {
				if (shipSku.getSkuId().equals(String.valueOf(wmsSku.getSkuId()))) {
					isExist = true;
				}
			}
			if (!isExist) {
				totalBuhuoCount += shipSku.getSkuQuantity();
				PickSkuItemForm pickSkuItemForm = new PickSkuItemForm();
				pickSkuItemForm.setSkuId(shipSku.getSkuId());
				pickSkuItemForm.setPoOrderId(shipSku.getPoOrderId());
				pickSkuItemForm.setSkuQuantity(shipSku.getSkuQuantity());
				pickSkuItemForm.setPickMoldType(isSingleOrder ? PickMoldType.SINGLE : PickMoldType.MANY);
				pickSkuItemForm.setJITFlag(shipOrder.getJITFlag());
				pickSkuItemForm.setStoreAreaId(shipOrder.getStoreAreaId());
				pickSkuItemForm.setSupplierId(shipOrder.getSupplierId());
				richBuhuoSku(pickSkuItemForm);
				buhuoPickSku.add(pickSkuItemForm);
			}
		}

		// 4. 创建补货拣货单，并保存补货单和明细。补货单则不能再补货
		out: if (buhuoPickSku.size() > 0 && !OmsIdUtils.isBuhuoOrder(shipOrder.getShipOrderId())) {
			// 判断该入库单是否是大前天的，如果是，则不再给供应商机会补货
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -2);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			boolean isShipTimeOut = OmsIdUtils.getDateLong(shipOrder.getPickOrderId()) < cal.getTimeInMillis();
			if (isShipTimeOut) {
				break out;
			}
			PickOrderForm pickOrder = new PickOrderForm();
			pickOrder.setPickOrderId(OmsIdUtils.genPickBuhuoId(shipOrder.getShipOrderId()));
			pickOrder.setSupplierId(shipOrder.getSupplierId());
			pickOrder.setPickState(PickStateType.UNPICK);
			pickOrder.setJITFlag(shipOrder.getJITFlag());
			pickOrder.setPickTotalQuantity(totalBuhuoCount);
			pickOrder.setCreateTime(System.currentTimeMillis());
			pickOrder.setModifyTime(System.currentTimeMillis());
			pickOrder.setStoreAreaId(shipOrder.getStoreAreaId());
			saveBuhuoPick(pickOrder, buhuoPickSku);

			// 5. 创建补货发货单，并推送给wms
			if (isSuccess) {
				isSuccess = isSuccess && genShipOrder(pickOrder, buhuoPickSku);
			}
		}

		if (!isSuccess) {
			throw new ServiceException("doStockInConfirm [" + wMSShipOrderUpdateDTO.getOrderId() + "] fail.");
		}
		return true;
	}

	private boolean genShipOrder(PickOrderForm pickOrder, List<PickSkuItemForm> buhuoPickSku) {
		boolean isSuccess = true;
		ShipOrderDTO shipOrderDTO = new ShipOrderDTO(pickOrder);
		shipOrderDTO.setSupplierId(pickOrder.getSupplierId());
		shipOrderDTO.setPickOrderId(pickOrder.getPickOrderId());
		shipOrderDTO.setShipOrderId(pickOrder.getPickOrderId());
		shipOrderDTO.setShipState(ShipStateType.SEND);
		shipOrderDTO.setJITFlag(pickOrder.getJITFlag());
		shipOrderDTO.setCreateTime(System.currentTimeMillis());
		shipOrderDTO.setShipTime(System.currentTimeMillis());
		//
		shipOrderDTO.setCollectTime(System.currentTimeMillis());
		shipOrderDTO.setStoreAreaId(pickOrder.getStoreAreaId());
		shipOrderDTO.setShipTime(System.currentTimeMillis());
		Map<Long, ShipSkuDTO> map = new LinkedHashMap<Long, ShipSkuDTO>();
		List<ShipSkuDTO> list = new ArrayList<ShipSkuDTO>();
		int skuCount = 0;
		long totalCount = 0;
		if (buhuoPickSku != null && buhuoPickSku.size() > 0) {
			for (PickSkuItemForm pickSku : buhuoPickSku) {
				long skuId = Long.parseLong(pickSku.getSkuId());
				ShipSkuDTO shipSkuDTO = map.get(skuId);
				if (shipSkuDTO == null) {
					skuCount++;
					shipSkuDTO = new ShipSkuDTO(pickSku);
					shipSkuDTO.setShipOrderId(shipOrderDTO.getShipOrderId());
					shipSkuDTO.setShipStates(ShipStateType.SEND);
					shipSkuDTO.setShipTime(System.currentTimeMillis());
					shipSkuDTO.setSupplierId(pickSku.getSupplierId());
					shipSkuDTO.setSkuId(pickSku.getSkuId());
					shipSkuDTO.setCodeNO(pickSku.getCodeNO());
					shipSkuDTO.setColor(pickSku.getColor());
					shipSkuDTO.setJITFlag(pickSku.getJITFlag());
					shipSkuDTO.setPickMoldType(pickSku.getPickMoldType());
					shipSkuDTO.setPoOrderId(pickSku.getPoOrderId());
					shipSkuDTO.setProductName(pickSku.getProductName());
					shipSkuDTO.setSize(pickSku.getSize());
					shipSkuDTO.setSkuQuantity(pickSku.getSkuQuantity());
					shipSkuDTO.setStoreAreaId(pickSku.getStoreAreaId());
					map.put(skuId, shipSkuDTO);
					list.add(shipSkuDTO);
				} else {
					shipSkuDTO.setSkuQuantity(shipSkuDTO.getSkuQuantity() + pickSku.getSkuQuantity());
				}
				totalCount = totalCount + pickSku.getSkuQuantity();
			}
			isSuccess = isSuccess
					&& shipSkuService.saveShipSkuList(list, shipOrderDTO.getShipOrderId(), JITFlagType.IS_JIT);
		}
		shipOrderDTO.setTotal((int) totalCount);
		shipOrderDTO.setSkuCount(skuCount);
		isSuccess = isSuccess && shipOrderDao.addObject(shipOrderDTO) != null;
		if (isSuccess) {
			isSuccess = isSuccess && sendShipOrderToWMS(shipOrderDTO, list);
		}
		return isSuccess;
	}

	private boolean sendShipOrderToWMS(ShipOrderDTO shipOrder, List<ShipSkuDTO> shipSkus) {
		WMSShipOrderDTO wmsShipOrderDTO = new WMSShipOrderDTO();
		wmsShipOrderDTO.setCount(shipOrder.getCount());
		wmsShipOrderDTO.setSkuCount(shipOrder.getSkuCount());
		List<WMSSkuDetailDTO> skuDetails = new ArrayList<WMSSkuDetailDTO>();
		if (shipSkus != null && shipSkus.size() > 0) {
			for (ShipSkuItemForm shipSku : shipSkus) {
				WMSSkuDetailDTO wmsSkuDetailDTO = new WMSSkuDetailDTO();
				wmsSkuDetailDTO.setArtNo(shipSku.getCodeNO());
				wmsSkuDetailDTO.setColor(shipSku.getColor());
				wmsSkuDetailDTO.setCount(shipSku.getSkuQuantity());
				wmsSkuDetailDTO.setNormalCount(shipSku.getSkuQuantity());
				wmsSkuDetailDTO.setName(shipSku.getProductName());
				wmsSkuDetailDTO.setSkuId(Long.valueOf(shipSku.getSkuId()));
				wmsSkuDetailDTO.setSize(shipSku.getSize());
				skuDetails.add(wmsSkuDetailDTO);
			}
		}
		WMSOrderType type = OmsIdUtils.backWMSOrderTypeByShipOrderId(shipOrder.getShipOrderId());
		WarehouseForm warehouse = warehouseService.getWarehouseById(shipOrder.getStoreAreaId());
		wmsShipOrderDTO.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
		wmsShipOrderDTO.setWarehouseCode(warehouse.getWarehouseCode());
		wmsShipOrderDTO.setOrderType(type);
		wmsShipOrderDTO.setSkuDetails(skuDetails);
		wmsShipOrderDTO.setBoxCount(shipOrder.getShipBoxQTY());
		wmsShipOrderDTO.setCarNumber(shipOrder.getLicensePlate());
		wmsShipOrderDTO.setDriverName(shipOrder.getDriverName());
		wmsShipOrderDTO.setDriverPhone(shipOrder.getDriverPhone());
		wmsShipOrderDTO.setOrderId(shipOrder.getShipOrderId());
		wmsShipOrderDTO.setLogisticCompany(shipOrder.getCourierCompany());
		wmsShipOrderDTO.setLogisticCode(shipOrder.getCourierCompany());
		return warehouseAdapterBridge.ship(wmsShipOrderDTO).isSucess();
	}

	/**
	 * 保存补货单和补货单明细
	 * 
	 * @param pickOrder
	 * @param buhuoPickSku
	 */
	@Transaction
	public void saveBuhuoPick(PickOrderForm pickOrder, List<PickSkuItemForm> buhuoPickSku) {
		// 先保存补货单
		PickOrderForm save = pickOrderDao.addObject(pickOrder);
		if (save == null) {
			throw new ServiceException("save buhuoPick[" + pickOrder.getPickOrderId() + "] fail.");
		}
		String pickOrderId = pickOrder.getPickOrderId();
		long currTime = System.currentTimeMillis();
		for (PickSkuItemForm pickSku : buhuoPickSku) {
			pickSku.setPickOrderId(pickOrderId);
			pickSku.setPickTime(currTime);
			pickSku.setPickStates(PickStateType.PICKED);
			PickSkuItemForm saveSku = pickSkuDao.addObject(pickSku);
			if (saveSku == null) {
				throw new ServiceException("save buhuoPick[" + pickOrder.getPickOrderId() + "] sku["
						+ pickSku.getSkuId() + "] fail.");
			}
		}
	}

	@Override
	@Transaction
	public boolean warehouseStockInConfirm(WMSShipOrderUpdateDTO wMSShipOrderUpdateDTO) {
		if (wMSShipOrderUpdateDTO.getState() == WMSShipOrderState.DONE) {
			try {
				return doStockInConfirm(wMSShipOrderUpdateDTO);
			} catch (Exception e) {
				if (e instanceof IReThrowException) {
					throw e;
				} else {
					throw new ServiceException(e);
				}
			}
		} else {
			return true;
		}
	}

}
