package com.xyl.mmall.oms.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.constant.CalendarConst;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dto.ShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.PickOrderService;
import com.xyl.mmall.oms.service.PickSkuService;
import com.xyl.mmall.oms.service.ShipOrderService;
import com.xyl.mmall.oms.service.ShipSkuService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.service.WarehouseStockInService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.util.OmsUtil;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;

/**
 * @author zb
 *
 */
@Service("shipOrderService")
public class ShipOrderServiceImpl implements ShipOrderService {

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private PickSkuDao pickSkuDao;

	@Autowired
	private PickOrderService pickOrderService;

	@Autowired
	private PickSkuService pickSkuService;

	@Autowired
	private ShipSkuService shipSkuService;

	@Autowired
	private WarehouseStockInService warehouseStockInService;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private WarehouseService warehouseService;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.ShipOrderService#getShipOrder(java.lang.String)
	 */
	@Override
	public ShipOrderDTO getShipOrder(String shipId, long supplierId) {
		ShipOrderForm shipOrderForm = shipOrderDao.getShipOrderByShipId(shipId, supplierId);
		ShipOrderDTO sod = new ShipOrderDTO(shipOrderForm);
		return sod;
	}

	@Override
	public ShipOrderForm getShipOrderByPickOrderId(String pickOrderId, long supplierId) {
		return shipOrderDao.getShipOrderByPickOrderId(pickOrderId, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.ShipOrderService#pushShipOrderToWarehose(java.lang.String)
	 */
	@Override
	@Transaction
	public boolean pushShipOrderToWarehose(String shipOrderId, long supplierId) {
		WMSShipOrderDTO wmsShipOrderDTO = shipSkuService.getWmsShipOrderDTOByShipOrderId(shipOrderId, supplierId);
		boolean isSucc = warehouseAdapterBridge.ship(wmsShipOrderDTO).isSucess();
		isSucc = isSucc && this.shipOrderDao.updateShipStateType(shipOrderId, ShipStateType.SEND);
		if (!isSucc) {
			throw new ServiceException("submitShip error. shipOrderDTO shipOrderId=" + shipOrderId);
		}
		return isSucc;
	}

	@Override
	public List<ShipOrderForm> getListByCreateTime(long createTime, ShipStateType state, int limit) {
		return shipOrderDao.getListByCreateTime(createTime, state, limit);
	}

	@Override
	public List<ShipOrderForm> getShipOrderBySupplierId(long supplierId) {
		return shipOrderDao.getListBySupplierId(supplierId);
	}

	@Override
	public void cancelTimeOutShipOrder() {
		long[] timeRange = new long[] { 1415635200000L, OmsUtil.getTimeOutTime() + CalendarConst.HOUR_TIME -1L};
		String minOrderId = "0";
		int limit = 10;
		ShipStateType[] shipStateTypeArray = new ShipStateType[] { ShipStateType.SEND };
		List<ShipOrderForm> list = this.shipOrderDao.getShipOrderFormListByStateWithMinOrderId(minOrderId,
				shipStateTypeArray, timeRange, limit);
		while (CollectionUtils.isNotEmpty(list)) {
			for (ShipOrderForm shipOrderForm : list) {
				// 1.发送入库取消指令
				WMSShipOrderDTO dto = new WMSShipOrderDTO();
				dto.setOrderId(shipOrderForm.getShipOrderId());
				dto.setOrderType(OmsIdUtils.backWMSOrderTypeByShipOrderId(shipOrderForm.getShipOrderId()));
				WarehouseForm warehouse = warehouseService.getWarehouseById(shipOrderForm.getStoreAreaId());
				dto.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
				dto.setWarehouseCode(warehouse.getWarehouseCode());

				boolean isSucc = warehouseAdapterBridge.cancelShip(dto).isSucess();
				// Patch 12月18日之前生成的入库单类型有点问题，需要尝试两种方式
				if (!isSucc) {
					List<ShipSkuItemForm> skuList = this.shipSkuService.getShipSkuList(shipOrderForm.getShipOrderId());
					if (skuList != null) {
						boolean isBuhuo = OmsIdUtils.isBuhuoOrder(shipOrderForm.getShipOrderId());
						WMSOrderType type = (skuList.size() == 1 && skuList.get(0).getSkuQuantity() == 1) ? (isBuhuo ? WMSOrderType.SI_PS
								: WMSOrderType.SI_S)
								: (isBuhuo ? WMSOrderType.SI_PM : WMSOrderType.SI_M);
						dto.setOrderId(shipOrderForm.getShipOrderId());
						dto.setOrderType(type);
						isSucc = warehouseAdapterBridge.cancelShip(dto).isSucess();
					}
				}
				// 2.更新状态
				isSucc = isSucc
						&& this.shipOrderDao.updateShipStateType(shipOrderForm.getShipOrderId(), ShipStateType.CANCEL);
			}
			minOrderId = list.get(0).getShipOrderId();
			list = this.shipOrderDao.getShipOrderFormListByStateWithMinOrderId(minOrderId, shipStateTypeArray,
					timeRange, limit);
		}

	}

	public static void main(String[] args) {
		long[] timeRange = new long[] { 1415635200000L, OmsUtil.getTimeOutTime() + CalendarConst.HOUR_TIME -1L};
		System.out.println(new Date(timeRange[0]));
		System.out.println(new Date(timeRange[1]));
	}

	@Override
	public List<ShipOrderForm> getListByCollectTime(long startCollectTime, long endCollectTime, int limit, int offset) {
		return this.shipOrderDao.getListByCollectTime(startCollectTime, endCollectTime, limit, offset);
	}

	@Override
	public List<ShipOrderForm> getListByCollectTime(long supplierId, long startCollectTime, long endCollectTime) {
		return this.shipOrderDao.getListByCollectTime(supplierId, startCollectTime, endCollectTime);
	}

}
