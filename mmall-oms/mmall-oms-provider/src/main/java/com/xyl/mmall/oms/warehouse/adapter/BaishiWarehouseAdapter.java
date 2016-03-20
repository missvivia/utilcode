/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dto.warehouse.WMSResponseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.warehouse.adapter.baishi.BSResponse;
import com.xyl.mmall.oms.warehouse.adapter.baishi.BSSyncAsnInfo;
import com.xyl.mmall.oms.warehouse.adapter.baishi.BSSyncSalesOrderInfo;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;

/**
 * 百世物流仓库Adapter
 * 
 * @author hzzengchengyuan
 */
@Service
public class BaishiWarehouseAdapter extends AbstractHttpWarehouseAdapter {

	private static Log LOGGER = LogFactory.getLog(BaishiWarehouseAdapter.class);

	public static final String KEY_CONSTANT_PARTNERID = "partnerId";

	public static final String KEY_CONSTANT_PARTNERKEY = "partnerKey";

	public static final String KEY_CONSTANT_BIZDATA = "bizData";

	public static final String KEY_CONSTANT_MESSAGEID = "msgId";

	public static final String KEY_CONSTANT_MESSAGETYPE = "msgType";

	public static final String VALUE_MESSAGETYPE_SYNC = "sync";

	public static final String VALUE_MESSAGETYPE_ASYNC = "async";

	public static String VALUE_DEFAULT_MESSAGETYPE = VALUE_MESSAGETYPE_ASYNC;

	public static final String KEY_CONSTANT_SERVICETYPE = "serviceType";

	public static final String KEY_CONSTANT_SERVICEVERSION = "serviceVersion";

	public static final String VALUE_DEFAULT_SERVICEVERSION = "1.0";

	public static final String KEY_CONSTANT_NOTIFYURL = "notifyUrl";

	public static final String KEY_CONSTANT_SIGN = "sign";

	// 入库单相关服务名常量
	public static final String SERVICENAME_SHIPORDER = "SyncAsnInfo";

	public static final String SERVICENAME_SHIPORDER_QUERY = "GetAsnStatus";

	public static final String SERVICENAME_SHIPORDER_UPDATE = "UpdateAsnStatus";

	// 销售订单相关服务名常量
	public static final String SERVICENAME_SALESORDER = "SyncSalesOrderInfo";

	public static final String SERVICENAME_SALESORDER_QUERY = "GetSalesOrderStatus";

	public static final String SERVICENAME_SALESORDER_UPDATE = "UpdateSalesOrderStatus";

	/**
	 * 合作伙伴ID值
	 */
	private String partnerId;

	/**
	 * 合作伙伴验证码值
	 */
	private String partnerKey;

	/**
	 * 百世接口地址
	 */
	private String baishiUrl;

	/**
	 * 回调地址
	 */
	private String notifyUrl;

	@Override
	protected void childInit() {
		super.childInit();
		this.notifyUrl = "/oms/baishi";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#ship(WMSShipOrderDTO)
	 */
	@Override
	public WMSResponseDTO ship(WMSShipOrderDTO shipOrder) {
		BSSyncAsnInfo bsAsnInfo = super.map(shipOrder, BSSyncAsnInfo.class);
		bsAsnInfo.setActionType(BSSyncAsnInfo.ACTIONTYPE_ADD);
		return sendPost(SERVICENAME_SHIPORDER, bsAsnInfo, shipOrder.getLogisticNo(), VALUE_DEFAULT_MESSAGETYPE);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#shipOut(com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO)
	 */
	@Override
	public WMSResponseDTO shipOut(WMSShipOutOrderDTO shipOrder) {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#send(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO)
	 */
	@Override
	public WMSResponseDTO send(WMSSalesOrderDTO userOrder) {
		BSSyncSalesOrderInfo form = super.map(userOrder, BSSyncSalesOrderInfo.class);
		form.setActionType(BSSyncSalesOrderInfo.ACTIONTYPE_ADD);
		return sendPost(SERVICENAME_SALESORDER, form, userOrder.getOrderId(), VALUE_DEFAULT_MESSAGETYPE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#cancelSend(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO)
	 */
	@Override
	public WMSResponseDTO cancelSend(WMSSalesOrderDTO userOrder) {
		BSSyncSalesOrderInfo salesOrder = super.map(userOrder, BSSyncSalesOrderInfo.class);
		salesOrder.setActionType(BSSyncSalesOrderInfo.ACTIONTYPE_CANCEL);
		return sendPost(SERVICENAME_SALESORDER, salesOrder, userOrder.getOrderId(), VALUE_DEFAULT_MESSAGETYPE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#returnSales(com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO)
	 */
	@Override
	public WMSResponseDTO returnSales(WMSReturnOrderDTO order) {
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#syncSku(com.xyl.mmall.oms.enums.WarehouseType, java.util.List)
	 */
	@Override
	public WMSResponseDTO syncSku(WMSSkuDetailDTO sku) {
		// TODO Auto-generated method stub
		return null;
	}

	private WMSResponseDTO sendPost(String serviceName, Object object, String messageId, String messageType) {
		WMSResponseDTO response = null;
		try {
			// 1. 将要发送的业务数据对象转换为xml
			String content = super.marshal(object);
			// 2. 组织发送数据
			Map<String, String> config = new HashMap<String, String>();
			config.put(KEY_CONSTANT_PARTNERID, this.partnerId);
			config.put(KEY_CONSTANT_BIZDATA, content);
			config.put(KEY_CONSTANT_PARTNERKEY, this.partnerKey);
			config.put(KEY_CONSTANT_MESSAGEID, messageId);
			config.put(KEY_CONSTANT_MESSAGETYPE, messageType);
			config.put(KEY_CONSTANT_SERVICETYPE, serviceName);
			config.put(KEY_CONSTANT_SERVICEVERSION, VALUE_DEFAULT_SERVICEVERSION);
			config.put(KEY_CONSTANT_NOTIFYURL, notifyUrl);
			// 3. 根据待签名数据计算签名，同时将签名添加到发送数据集合中
			String sign = genSign(config);
			config.put(KEY_CONSTANT_SIGN, sign);
			// 4. 发送post请求
			String result = doPost(config);
			// 5. 组织返回结果对象
			BSResponse bsRep = super.unmarshal(result, BSResponse.class);
			response = restoreWarehouseResponse(bsRep);
		} catch (Exception e) {
			return new WMSResponseDTO(WMSResponseDTO.FLAG_EXCEPTION, e.getMessage());
		}
		// 6. 记录日志
		logResponse(response);
		return response;
	}

	private void logResponse(WMSResponseDTO response) {
		if (response.isFailure()) {
			LOGGER.error(response.getMessage());
		} else if (response.isException()) {
			LOGGER.error("", response.getException());
		}
	}

	private void logCallerResponse(BSResponse response) {
		if (response.isFailure()) {
			LOGGER.error(response.getErrorCode() + ":" + response.getErrorDescription());
		}
	}

	private WMSResponseDTO restoreWarehouseResponse(BSResponse bsRep) {
		WMSResponseDTO response = null;
		if (bsRep != null) {
			if (bsRep.isSuccess()) {
				response = new WMSResponseDTO(WMSResponseDTO.FLAG_SUCESS, bsRep.getNote());
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("错误代码：").append(bsRep.getErrorCode()).append(",描述:").append(bsRep.getErrorDescription());
				response = new WMSResponseDTO(WMSResponseDTO.FLAG_FAILURE, sb.toString());
			}
		}
		return response;
	}

	/**
	 * 根据发送的内容生成签名
	 * 
	 * @return
	 */
	private String genSign(Map<String, String> config) {
		StringBuilder sb = new StringBuilder();
		sb.append(KEY_CONSTANT_PARTNERID).append("=").append(config.get(KEY_CONSTANT_PARTNERID)).append("&");
		sb.append(KEY_CONSTANT_BIZDATA).append("=").append(config.get(KEY_CONSTANT_BIZDATA)).append("&");
		sb.append(KEY_CONSTANT_PARTNERKEY).append("=").append(config.get(KEY_CONSTANT_PARTNERKEY)).append("&");
		sb.append(KEY_CONSTANT_MESSAGEID).append("=").append(config.get(KEY_CONSTANT_MESSAGEID)).append("&");
		sb.append(KEY_CONSTANT_MESSAGETYPE).append("=").append(config.get(KEY_CONSTANT_MESSAGETYPE)).append("&");
		sb.append(KEY_CONSTANT_SERVICETYPE).append("=").append(config.get(KEY_CONSTANT_SERVICETYPE)).append("&");
		sb.append(KEY_CONSTANT_SERVICEVERSION).append("=").append(config.get(KEY_CONSTANT_SERVICEVERSION)).append("&");
		sb.append(KEY_CONSTANT_NOTIFYURL).append("=").append(config.get(KEY_CONSTANT_NOTIFYURL));
		return MD5(sb.toString(), StandardCharsets.UTF_8.name());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapterCallback#onCallback(java.util.Map)
	 */
	@Override
	public Object onCallback(Map<String, Object> params) {
		BSResponse result = new BSResponse();
		try {
			String serviceName = "";
			String content = "<object></object>";
			Object obj = super.unmarshal(content);
			if (SERVICENAME_SHIPORDER_UPDATE.equals(serviceName)) {
				WMSShipOrderUpdateDTO shipOrder = super.map(obj, WMSShipOrderUpdateDTO.class);
				shipOrder.calculateCount();
				boolean success = notifyonShipOrderStateChange(shipOrder);
				result.setFlag(success ? BSResponse.FLAG_SUCCESS : BSResponse.FLAG_FAILURE);
			} else if (SERVICENAME_SALESORDER_UPDATE.equals(serviceName)) {
				WMSSalesOrderDTO salesOrder = super.map(obj, WMSSalesOrderDTO.class);
				salesOrder.calculateCount();
				boolean success = notifySalesOrderStateChange(salesOrder);
				result.setFlag(success ? BSResponse.FLAG_SUCCESS : BSResponse.FLAG_FAILURE);
			} else {
				result.setFlag(BSResponse.FLAG_FAILURE);
				result.setErrorDescription("不支持该服务：" + serviceName);
			}
		} catch (Exception e) {
			result.setFlag(BSResponse.FLAG_FAILURE);
			result.setErrorDescription(e.getMessage());
		}
		logCallerResponse(result);
		return result;
	}

	protected boolean notifyonShipOrderStateChange(WMSShipOrderUpdateDTO shipOrder) throws WarehouseCallerException {
		return true;
	}

	protected boolean notifySalesOrderStateChange(WMSSalesOrderDTO salesOrder) throws WarehouseCallerException {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getUrl()
	 */
	@Override
	protected String getUrl() {
		return this.baishiUrl;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getCharSet()
	 */
	@Override
	protected String getCharSet() {
		return StandardCharsets.UTF_8.name();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getBoundClasses()
	 */
	@Override
	protected Class<?>[] getBoundClasses() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getPackagesToScan()
	 */
	@Override
	protected String[] getPackagesToScan() {
		return new String[] { "com.xyl.mmall.oms.warehouse.adapter.baishi" };
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getWarehouseType()
	 */
	@Override
	public WarehouseType getWarehouseType() {
		return WarehouseType.BAISHI;
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
