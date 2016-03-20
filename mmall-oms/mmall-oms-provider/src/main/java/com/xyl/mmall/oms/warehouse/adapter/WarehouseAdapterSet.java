/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dto.warehouse.WMSResponseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.WarehouseAdapter;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge;
import com.xyl.mmall.oms.warehouse.WarehouseAdapterCallback;
import com.xyl.mmall.oms.warehouse.exception.WarehouseAdapterCallbackNotFoundException;
import com.xyl.mmall.oms.warehouse.exception.WarehouseAdapterNotFoundException;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * @author hzzengchengyuan
 * 
 */
@Service
public class WarehouseAdapterSet implements WarehouseAdapterBridge {
	private static Log LOGGER = LogFactory.getLog(WarehouseAdapterSet.class);

	private Map<WarehouseType, AbstractWarehouseAdapter> adapters = new HashMap<WarehouseType, AbstractWarehouseAdapter>();

	private Map<WarehouseType, WarehouseAdapterCallback> adapterCallbacks = new HashMap<WarehouseType, WarehouseAdapterCallback>();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#ship(WMSShipOrderDTO)
	 */
	@Override
	public WMSResponseDTO ship(WMSShipOrderDTO shipOrder) {
		shipOrder.calculateCount();
		WarehouseAdapter adapter = getAdapter(shipOrder.getWmsType());
		return adapter.ship(shipOrder);
	}
	
	@Override
	public WMSResponseDTO cancelShip(WMSShipOrderDTO shipOrder) {
		shipOrder.calculateCount();
		WarehouseAdapter adapter = getAdapter(shipOrder.getWmsType());
		return adapter.cancelShip(shipOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#shipOut(com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO)
	 */
	@Override
	public WMSResponseDTO shipOut(WMSShipOutOrderDTO shipOrder) {
		shipOrder.calculateCount();
		WarehouseAdapter adapter = getAdapter(shipOrder.getWmsType());
		return adapter.shipOut(shipOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#send(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO)
	 */
	@Override
	public WMSResponseDTO send(WMSSalesOrderDTO userOrder) {
		userOrder.calculateCount();
		WarehouseAdapter adapter = getAdapter(userOrder.getWmsType());
		return adapter.send(userOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#cancelSend(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO)
	 */
	@Override
	public WMSResponseDTO cancelSend(WMSSalesOrderDTO userOrder) {
		userOrder.calculateCount();
		WarehouseAdapter adapter = getAdapter(userOrder.getWmsType());
		return adapter.cancelSend(userOrder);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#returnSales(com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO)
	 */
	@Override
	public WMSResponseDTO returnSales(WMSReturnOrderDTO returnOrder) {
		returnOrder.calculateCount();
		WarehouseAdapter adapter = getAdapter(returnOrder.getWmsType());
		return adapter.returnSales(returnOrder);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#syncSku(java.util.List)
	 */
	@Override
	public WMSResponseDTO syncSku(WMSSkuDetailDTO sku) {
		sku.calculateCount();
		WarehouseAdapter adapter = getAdapter(sku.getWmsType());
		return adapter.syncSku(sku);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapterBridge#doHttpWarehouseCaller(java.lang.String,
	 *      java.util.Map, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Object doHttpWarehouseCaller(String warehouseCode, Map<String, Object> params) {
		try {
			WarehouseAdapterCallback callBack = getWarehouseAdapterCallback(warehouseCode);
			Object result = callBack.onCallback(params);
			AbstractWarehouseAdapter adapter = getAdapter(warehouseCode);
			return adapter.marshal(result);
		} catch (WarehouseAdapterNotFoundException e) {
			return new WMSResponseDTO(WMSResponseDTO.FLAG_EXCEPTION, "OMS不支持该仓储[" + warehouseCode + "]的回调对接.");
		} catch (XmlMappingException | UnsupportedEncodingException e) {
			LOGGER.error("", e);
			return new WMSResponseDTO(WMSResponseDTO.FLAG_EXCEPTION, "服务出现异常.");
		}

	}

	private AbstractWarehouseAdapter getAdapter(WarehouseType type) {
		AbstractWarehouseAdapter adapter = this.adapters.get(type);
		if (adapter == null) {
			throw new WarehouseAdapterNotFoundException(type);
		}
		return adapter;
	}

	private AbstractWarehouseAdapter getAdapter(String warehouseCode) {
		WarehouseType wType = WarehouseType.genEnumNameIgnoreCase(warehouseCode);
		if (wType == WarehouseType.NULL) {
			throw new WarehouseCallerException("does not support the warehouse :" + warehouseCode);
		}
		return getAdapter(wType);
	}

	private WarehouseAdapterCallback getWarehouseAdapterCallback(String warehouseCode) {
		WarehouseType wType = WarehouseType.genEnumNameIgnoreCase(warehouseCode);
		if (wType == WarehouseType.NULL) {
			throw new WarehouseCallerException("does not support the warehouse :" + warehouseCode);
		}
		WarehouseAdapterCallback adapterCallback = this.adapterCallbacks.get(wType);
		if (adapterCallback == null) {
			throw new WarehouseAdapterCallbackNotFoundException(wType);
		}
		return adapterCallback;
	}

	@Autowired
	public void setWarehouseMarshallers(WarehouseAdapterCallback[] p_marshallers) {
		if (p_marshallers != null) {
			for (WarehouseAdapterCallback marshaller : p_marshallers) {
				if(marshaller.getWarehouseType() != WarehouseType.NULL) {
					this.adapterCallbacks.put(marshaller.getWarehouseType(), marshaller);
				}
			}
		}
	}

	@Autowired
	public void setWarehouseAdapters(AbstractWarehouseAdapter[] p_adapters) {
		if (p_adapters != null) {
			for (AbstractWarehouseAdapter adapter : p_adapters) {
				if(adapter.getWarehouseType() != WarehouseType.NULL) {
					this.adapters.put(adapter.getWarehouseType(), adapter);
				}
			}
		}
	}

}
