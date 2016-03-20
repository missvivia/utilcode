package com.xyl.mmall.oms.warehouse.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dto.warehouse.WMSResponseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.warehouse.adapter.sf.SFIOutsideService;

/**
 * @author hzzengchengyuan
 *
 */
@Service
public class SFWarehouseAdapter extends AbstractJaxwsWarehouseAdapter<SFIOutsideService> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SFWarehouseAdapter.class);

	@Override
	public WMSResponseDTO ship(WMSShipOrderDTO shipOrder) {
		return null;
	}

	@Override
	public WMSResponseDTO shipOut(WMSShipOutOrderDTO shipOrder) {
		return null;
	}

	@Override
	public WMSResponseDTO send(WMSSalesOrderDTO userOrder) {
		SFIOutsideService service = getService();
		return null;
	}

	@Override
	public WMSResponseDTO cancelSend(WMSSalesOrderDTO userOrder) {
		return null;
	}

	@Override
	public WMSResponseDTO returnSales(WMSReturnOrderDTO order) {
		return null;
	}

	@Override
	public WMSResponseDTO syncSku(WMSSkuDetailDTO sku) {
		return null;
	}

	@Override
	public Object onCallback(Map<String, Object> params) {
		return null;
	}

	@Override
	public WarehouseType getWarehouseType() {
		return WarehouseType.SF;
	}

	@Override
	protected URL getWsdlDocumentUrl() {
		try {
			return new URL(getUrl().concat("?wsdl"));
		} catch (MalformedURLException e) {
			LOGGER.error("", e);
			return null;
		}
	}

	@Override
	protected String getPortName() {
		return "OutsideToLscmServicePort";
	}

	@Override
	protected String getServiceName() {
		return "OutsideToLscmServiceService";
	}

	@Override
	protected Class<?>[] getBoundClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getPackagesToScan() {
		return new String[] { "com.xyl.mmall.oms.warehouse.adapter.sf" };
	}

	@Override
	protected String getUrl() {
		return "http://219.134.187.154:9154/bsp-wms/ws/OutsideToLscmServiceImpl";
	}

	@Override
	protected String getCharSet() {
		return "UTF-8";
	}

	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#cancelShip(com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO)
	 */
	@Override
	public WMSResponseDTO cancelShip(WMSShipOrderDTO shipOrder) {
		// TODO Auto-generated method stub
		return null;
	}

}
