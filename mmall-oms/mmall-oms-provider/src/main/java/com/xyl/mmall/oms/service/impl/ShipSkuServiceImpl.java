package com.xyl.mmall.oms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.dto.ShipSkuDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.meta.WarehouseForm;
import com.xyl.mmall.oms.service.ShipSkuService;
import com.xyl.mmall.oms.service.WarehouseService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;

/**
 * @author zb
 *
 */
@Service("shipSkuService")
public class ShipSkuServiceImpl implements ShipSkuService {

	@Autowired
	private ShipSkuDao shipSkuDao;

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private WarehouseAdapterBridge warehouseAdapterBridge;

	@Autowired
	private WarehouseService warehouseService;

	@Override
	public List<ShipSkuDTO> getShipSkuDetail(String shipId) {
		List<ShipSkuItemForm> ssif = shipSkuDao.getShipSkuList(shipId);
		List<ShipSkuDTO> ssd = new ArrayList<ShipSkuDTO>();
		for (ShipSkuItemForm k : ssif) {
			ssd.add(new ShipSkuDTO(k));
		}
		return ssd;
	}

	@Override
	public boolean updateShipSkuItem(ShipSkuDTO shipSkuDTO) {
		return shipSkuDao.updateObjectByKey(shipSkuDTO);
	}

	@Override
	public ShipSkuItemForm addShipSkuItem(ShipSkuItemForm itemForm) {
		return shipSkuDao.addObject(itemForm);
	}

	@Override
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(String poOrderId) {
		return shipSkuDao.getShipSkuListByPoOrderId(poOrderId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.ShipSkuService#getTotalCountByShipOrderId(java.lang.String)
	 */
	@Override
	public int getTotalCountByShipOrderId(String shipOrderId, long supplierId) {
		return shipSkuDao.getTotalCountByShipOrderId(shipOrderId, supplierId);
	}

	/**
	 * @param shipOrderId
	 * @param supplierId
	 * @return
	 */
	@Override
	public int getTotalSkuTypeByShipOrderId(String shipOrderId, long supplierId) {
		return shipSkuDao.getTotalSkuTypeByShipOrderId(shipOrderId, supplierId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.ShipSkuService#getWmsShipOrderDTOByShipOrderId(java.lang.String)
	 */
	@Override
	public WMSShipOrderDTO getWmsShipOrderDTOByShipOrderId(String shipOrderId, long supplierId) {
		ShipOrderForm shipOrderForm = shipOrderDao.getShipOrderByShipId(shipOrderId, supplierId);
		WMSShipOrderDTO wmsShipOrderDTO = new WMSShipOrderDTO();

		WarehouseForm warehouse = warehouseService.getWarehouseById(shipOrderForm.getStoreAreaId());
		wmsShipOrderDTO.setWmsType(WarehouseType.genEnumNameIgnoreCase(warehouse.getExpressCompany()));
		wmsShipOrderDTO.setWarehouseCode(warehouse.getWarehouseCode());
		wmsShipOrderDTO.setSenderCode(String.valueOf((supplierId)));
		List<ShipSkuItemForm> skuList = shipSkuDao.getShipSkuList(shipOrderId);
		wmsShipOrderDTO.setCount(getTotalCountByShipOrderId(shipOrderId, shipOrderForm.getSupplierId()));
		wmsShipOrderDTO.setSkuCount(getTotalSkuTypeByShipOrderId(shipOrderId, shipOrderForm.getSupplierId()));
		List<WMSSkuDetailDTO> skuDetails = new ArrayList<WMSSkuDetailDTO>();
		if (skuList != null && skuList.size() > 0) {
			for (ShipSkuItemForm shipSku : skuList) {
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
		wmsShipOrderDTO.setOrderType(OmsIdUtils.backWMSOrderTypeByShipOrderId(shipOrderId));
		wmsShipOrderDTO.setSkuDetails(skuDetails);
		wmsShipOrderDTO.setBoxCount(shipOrderForm.getShipBoxQTY());
		wmsShipOrderDTO.setCarNumber(shipOrderForm.getLicensePlate());
		wmsShipOrderDTO.setDriverName(shipOrderForm.getDriverName());
		wmsShipOrderDTO.setDriverPhone(shipOrderForm.getDriverPhone());
		wmsShipOrderDTO.setOrderId(shipOrderForm.getShipOrderId());
		wmsShipOrderDTO.setLogisticCompany(shipOrderForm.getCourierCompany());
		wmsShipOrderDTO.setLogisticCode(shipOrderForm.getCourierCompany());
		return wmsShipOrderDTO;
	}

	@Transaction
	@Override
	public boolean saveShipSkuList(List<ShipSkuDTO> list, String shipOrderId, JITFlagType jitFlagType) {

		for (ShipSkuDTO shipSkuDTO : list) {
			if (shipSkuDTO.getJITFlag() == null) {
				shipSkuDTO.setJITFlag(jitFlagType);
			}
			if (shipSkuDTO.getShipStates() == null) {
				shipSkuDTO.setShipStates(ShipStateType.UNSEDN);
			}
			shipSkuDTO.setShipOrderId(shipOrderId);
			this.addShipSkuItem(shipSkuDTO);
		}
		return true;
	}

	@Override
	public List<ShipSkuItemForm> getShipSkuListByPoOrderId(long poOrderId, long supplierId) {
		return this.shipSkuDao.getShipSkuListByPoOrderId(poOrderId, supplierId);
	}

	@Override
	public List<ShipSkuItemForm> getShipSkuList(String shipOrderId) {
		return shipSkuDao.getShipSkuList(shipOrderId);
	}

}
