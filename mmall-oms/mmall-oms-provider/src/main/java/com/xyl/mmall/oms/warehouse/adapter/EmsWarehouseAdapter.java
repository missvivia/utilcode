/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.task.service.PushService;
import com.xyl.mmall.oms.dto.warehouse.WMSOrderTrace;
import com.xyl.mmall.oms.dto.warehouse.WMSPackageDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSResponseDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSShipOutOrderUpdateDTO;
import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.enums.WMSExpressCompany;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.enums.WMSPackageState;
import com.xyl.mmall.oms.enums.WMSReturnOrderState;
import com.xyl.mmall.oms.enums.WMSSalesOrderState;
import com.xyl.mmall.oms.enums.WMSShipOrderState;
import com.xyl.mmall.oms.enums.WMSShipOutOrderState;
import com.xyl.mmall.oms.enums.WarehouseLogState;
import com.xyl.mmall.oms.enums.WarehouseType;
import com.xyl.mmall.oms.meta.WarehouseLog;
import com.xyl.mmall.oms.service.WarehouseLogService;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsDelivery;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsSku;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTrace;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTraceInfo;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTraceInfoResponse;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTraceOperateType;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTraceResponse;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsUndelivery;
import com.xyl.mmall.oms.warehouse.adapter.ems.Response;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsInstoreCancelNotice;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsOmsGoodsUpdate;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsOrderCancelNotice;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsOrderNotice;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsOrderStatusUpdate;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsReturnOrderNotice;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsStockInConfirm;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsStockInNotice;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsStockOutConfirm;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsStockOutNotice;
import com.xyl.mmall.oms.warehouse.adapter.meta.BaseWarehouseInfo;
import com.xyl.mmall.oms.warehouse.exception.WarehouseCallerException;
import com.xyl.mmall.oms.warehouse.util.DateUtils;
import com.xyl.mmall.oms.warehouse.util.JsonLibUtils;

/**
 * @author hzzengchengyuan
 * 
 */
@Service
public class EmsWarehouseAdapter extends AbstractHttpWarehouseAdapter {
	public static final String KEY_CONSTANT_APPKEY = "appkey";

	public static final String KEY_CONSTANT_NOTIFYTIME = "notify_time";

	public static final String KEY_CONSTANT_SERVICE = "service";

	public static final String KEY_CONSTANT_SIGN = "sign";

	public static final String KEY_CONSTANT_CONTENT = "content";

	public static final String KEY_CONSTANT_INPUTCHARSET = "InputCharSet";

	/*
	 * --------入库单相关服务名常量--------
	 */
	/**
	 * 发送入库单服务名常量。商品入库时，外部系统创建入库单，并将该入库单信息通知给仓储WMS系统。
	 */
	public static final String SRVICENAME_SHIPORDER = "WmsStockInNotice";

	/**
	 * 入库单入库确认回传服务名常量。WMS系统在入库单完成入库（上架）之后， 对应入库信息回传给外部系统
	 */
	public static final String SERVICENAME_SHIPORDER_CONFIRM = "WmsStockInConfirm";
	
	/**
	 * 外部系统通过此接口向WMS系统发起取消入库单请求。入库订单在仓储WMS系统未做收货操作之前都可成功取消该入库单。
	 */
	public static final String SRVICENAME_SHIPORDER_CANCEL = "WmsInstoreCancelNotice";

	/*
	 * --------出库单相关服务名常量--------
	 */
	/**
	 * 发送出库单服务名常量。商品出库时(退货出库，非销售订单)，先在外部系统创建出库单，然后将该出库单信息通知给仓储WMS系统。
	 */
	public static final String SERVICENAME_SHIPOUTORDER = "WmsStockOutNotice";

	public static final String SERVICENAME_SHIPOUTORDER_CONFIRM = "WmsStockOutConfirm";

	/*
	 * --------销售订单相关服务名常量--------
	 */
	/**
	 * 发送销售订单服务名常量
	 */
	public static final String SRVICENAME_SALESORDER = "WmsOrderNotice";

	/**
	 * 取消销售订单服务名常量。外部系统通过此接口向WMS系统发起取消订单发货请求。出库订单在仓储WMS系统未做出库完成操作之前都可成功取消该出库单发货。
	 */
	public static final String SRVICENAME_SALESORDER_CANCEL = "WmsOrderCancelNotice";

	/**
	 * 销售订单状态更新服务名常量。仓储WMS系统将物流订单接收成功之后会将后续处理的状态信息回传给外部系统。
	 */
	public static final String SRVICENAME_SALESORDER_UPDATE = "WmsOrderStatusUpdate";

	/**
	 * 仓库发货后，wms通过跟ems的接口获取到订单的运输轨迹，并通过此接口回传给外部系统。因此，此接口只会回传由ems运输的订单，其他顺丰、
	 * 龙邦等运输的订单不会回传。
	 */
	public static final String SRVICENAME_SALESORDER_TRACE = "EmsTrace";

	/*
	 * ------------商品同步服务名常量-------------
	 */
	/**
	 * 外部系统通过调用wms系统里此接口可以更新当前项目下货品档案信息，外部系统根据返回的信息判断货品档案信息是否成功更新至WMS仓储系统。
	 */
	public static final String SERVICENAME_SYNCSKU = "WmsOmsGoodsUpdate";

	@Value("${oms.ems.serviceUrl}")
	protected String EMSURL;

	@Value("${oms.ems.appKey}")
	protected String APPKEY;

	/**
	 * 等同于，APPKEY，用于商品同步
	 */
	protected long PROJECTID;

	@Value("${oms.ems.keyValue}")
	protected String KEYVALUE;

	@Value("${oms.ems.ownerCode}")
	protected String OWNERCODE;

	@Value("${oms.ownerName}")
	protected String OWNERNAME;

	@Value("${oms.ems.warehouseCode}")
	protected String WAREHOUSECODE;

	@Value("${oms.warehouse.traceLog}")
	protected boolean isTrace;
	
	@Value("${oms.debug}")
	protected boolean isDebug = false;

	protected BaseWarehouseInfo baseWarehouseInfo;

	@Autowired
	protected WarehouseLogService logService;
	
	@Autowired
	private PushService pushService;
	
	@Value("${oms.ems.exceptionReceiver}")
	private String exceptionReceiver;
	
	private String[] exceptionReceivers;

	private Map<String,String> appKeyMap = new HashMap<String,String>();
	private Map<String,String> ownerMap = new HashMap<String,String>();
	
	@Override
	protected void childInit() {
		baseWarehouseInfo = new BaseWarehouseInfo();
		baseWarehouseInfo.setServiceUrl(EMSURL);
		baseWarehouseInfo.setAppKey(APPKEY);
		baseWarehouseInfo.setKeyValue(KEYVALUE);
		baseWarehouseInfo.setOwnerCode(OWNERCODE);
		baseWarehouseInfo.setOwnerName(OWNERNAME);
		PROJECTID = Long.parseLong(APPKEY);
		appKeyMap.put("40496312", "1024180");
		appKeyMap.put("64400073", "1024181");
		appKeyMap.put("52840104", "1024182");
		appKeyMap.put("40717521", "1023820");
		ownerMap.put("40496312", "WYYXBJ");
		ownerMap.put("64400073", "WYYXSC");
		ownerMap.put("52840104", "WYYXGD");
		ownerMap.put("40717521", "WYTM");
		if(exceptionReceiver != null && exceptionReceiver.trim().length()>0) {
			exceptionReceivers = exceptionReceiver.split(";");
		} 
	}
	
	private String getAppKeyByWarehouseCode(String warehouseCode) {
		if("35000600002".equals(warehouseCode)) {
			return APPKEY;
		} else {
			return appKeyMap.get(warehouseCode);
		}
	}
	
	private String getOwnerCodeByWarehouseCode(String warehouseCode) {
		if("35000600002".equals(warehouseCode)) {
			return OWNERCODE;
		} else {
			return ownerMap.get(warehouseCode);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#ship(com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO)
	 */
	@Override
	public WMSResponseDTO ship(WMSShipOrderDTO shipOrder) {
		// 映射注入基本仓储信息（如仓储机构编码，货主编码等）到订单对象中
		super.map(baseWarehouseInfo, shipOrder);
		shipOrder.setOrderId(OmsIdUtils.genEmsOrderId(shipOrder.getOrderId(), shipOrder.getOrderType()));
		WmsStockInNotice wmsStockInNotice = super.map(shipOrder, WmsStockInNotice.class);
		wmsStockInNotice.setOrder_type_code(shipOrder.getOrderType().name());
		wmsStockInNotice.setOrder_type_name(shipOrder.getOrderType().getDesc());
		wmsStockInNotice.setOwner_code(getOwnerCodeByWarehouseCode(wmsStockInNotice.getWarehouse_code()));
		WMSResponseDTO response = sendPost(SRVICENAME_SHIPORDER, wmsStockInNotice, getAppKeyByWarehouseCode(wmsStockInNotice.getWarehouse_code()));
		return response;
	}
	
	@Override
	public WMSResponseDTO cancelShip(WMSShipOrderDTO shipOrder) {
		// 映射注入基本仓储信息（如仓储机构编码，货主编码等）到订单对象中
		super.map(baseWarehouseInfo, shipOrder);
		shipOrder.setOrderId(OmsIdUtils.genEmsOrderId(shipOrder.getOrderId(), shipOrder.getOrderType()));
		WmsInstoreCancelNotice wmsStockInCancelNotice = super.map(shipOrder, WmsInstoreCancelNotice.class);
		wmsStockInCancelNotice.setOwner_code(getOwnerCodeByWarehouseCode(wmsStockInCancelNotice.getWarehouse_code()));
		WMSResponseDTO response = sendPost(SRVICENAME_SHIPORDER_CANCEL, wmsStockInCancelNotice, getAppKeyByWarehouseCode(wmsStockInCancelNotice.getWarehouse_code()));
		return response;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#shipOut(com.xyl.mmall.oms.dto.warehouse.WMSShipOrderDTO)
	 */
	@Override
	public WMSResponseDTO shipOut(WMSShipOutOrderDTO shipOutOrder) {
		super.map(baseWarehouseInfo, shipOutOrder);
		WMSOrderType orderType = WMSOrderType.SO;
		shipOutOrder.setOrderId(OmsIdUtils.genEmsOrderId(shipOutOrder.getOrderId(), orderType));
		WmsStockOutNotice wmsStockOutNotice = super.map(shipOutOrder, WmsStockOutNotice.class);
		wmsStockOutNotice.setOrder_type_code(orderType.name());
		wmsStockOutNotice.setOrder_type_name(orderType.getDesc());
		wmsStockOutNotice.setOwner_code(getOwnerCodeByWarehouseCode(wmsStockOutNotice.getWarehouse_code()));
		WMSResponseDTO response = sendPost(SERVICENAME_SHIPOUTORDER, wmsStockOutNotice, getAppKeyByWarehouseCode(wmsStockOutNotice.getWarehouse_code()));
		return response;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#send(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO)
	 */
	@Override
	public WMSResponseDTO send(WMSSalesOrderDTO userOrder) {
		if (userOrder.getLogisticCode() == null || userOrder.getLogisticCode().trim().length() == 0) {
			userOrder.setLogisticCode(WMSExpressCompany.EMS.name());
		}
		super.map(baseWarehouseInfo, userOrder);
		WMSOrderType orderType = WMSOrderType.SALES;
		userOrder.setOrderId(OmsIdUtils.genEmsOrderId(userOrder.getOrderId(), orderType));
		WmsOrderNotice orderNotice = super.map(userOrder, WmsOrderNotice.class);
		orderNotice.setOrder_type_code(orderType.name());
		orderNotice.setOrder_type_name(orderType.getDesc());
		// 因ems是否货到付款以String类型标识，无法自动映射。重设是否货到付款
		orderNotice.setIscod(userOrder.isCod() ? WmsOrderNotice.ISCOD_YES : WmsOrderNotice.ISCOD_NO);
		orderNotice.setOwner_code(getOwnerCodeByWarehouseCode(orderNotice.getWarehouse_code()));
		WMSResponseDTO response = sendPost(SRVICENAME_SALESORDER, orderNotice, getAppKeyByWarehouseCode(orderNotice.getWarehouse_code()));
		return response;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#cancelSend(com.xyl.mmall.oms.dto.warehouse.WMSSalesOrderDTO)
	 */
	@Override
	public WMSResponseDTO cancelSend(WMSSalesOrderDTO userOrder) {
		super.map(baseWarehouseInfo, userOrder);
		userOrder.setOrderId(OmsIdUtils.genEmsOrderId(userOrder.getOrderId(), WMSOrderType.SALES));
		WmsOrderCancelNotice orderCancelNotice = super.map(userOrder, WmsOrderCancelNotice.class);
		orderCancelNotice.setOwner_code(getOwnerCodeByWarehouseCode(orderCancelNotice.getWarehouse_code()));
		WMSResponseDTO response = sendPost(SRVICENAME_SALESORDER_CANCEL, orderCancelNotice, getAppKeyByWarehouseCode(orderCancelNotice.getWarehouse_code()));
		return response;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#returnSales(com.xyl.mmall.oms.dto.warehouse.WMSReturnOrderDTO)
	 */
	@Override
	public WMSResponseDTO returnSales(WMSReturnOrderDTO returnOrder) {
		super.map(baseWarehouseInfo, returnOrder);
		WMSOrderType orderType = returnOrder.getOrderType();
		String nowReturnOrderId = OmsIdUtils.genEmsOrderId(returnOrder.getOrderId(), orderType);
		String oriSalesOrderId = OmsIdUtils.genEmsOrderId(returnOrder.getOriginalOrderId(), WMSOrderType.SALES);
		WmsReturnOrderNotice wmsStockInNotice = super.map(returnOrder, WmsReturnOrderNotice.class);
		wmsStockInNotice.setOrder_type_code(orderType.name());
		wmsStockInNotice.setOrder_type_name(orderType.getDesc());
		// 设置退货单id
		wmsStockInNotice.setAsn_id(nowReturnOrderId);
		// 设置原销售订单id
		wmsStockInNotice.setSale_order_id(oriSalesOrderId);
		wmsStockInNotice.setOwner_code(getOwnerCodeByWarehouseCode(wmsStockInNotice.getWarehouse_code()));
		WMSResponseDTO response = sendPost(SRVICENAME_SHIPORDER, wmsStockInNotice, getAppKeyByWarehouseCode(wmsStockInNotice.getWarehouse_code()));
		return response;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapter#syncSku(com.xyl.mmall.oms.enums.WarehouseType,
	 *      java.util.List)
	 */
	@Override
	public WMSResponseDTO syncSku(WMSSkuDetailDTO sku) {
		WmsOmsGoodsUpdate wmsSku = super.map(sku, WmsOmsGoodsUpdate.class);
		String appKey = getAppKeyByWarehouseCode(sku.getWarehouseCode());
		wmsSku.setPrjct_id(Long.parseLong(appKey));
		WMSResponseDTO response = sendPost(SERVICENAME_SYNCSKU, wmsSku, appKey);
		return response;
	}

	private WMSResponseDTO sendPost(String serviceName, Object object, String appKey) {
		WMSResponseDTO response = null;
		try {
			// 1. 将要发送的业务数据对象转换为xml
			String content = super.marshal(object);
			// 2. 组织发送数据
			Map<String, String> config = new HashMap<String, String>();
			config.put(KEY_CONSTANT_APPKEY, appKey);
			config.put(KEY_CONSTANT_SERVICE, serviceName);
			config.put(KEY_CONSTANT_INPUTCHARSET, getCharSet());

			config.put(KEY_CONSTANT_CONTENT, content);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			config.put(KEY_CONSTANT_NOTIFYTIME, format.format(new Date()));
			// 3. 计算签名，同时将签名添加到发送数据集合中
			String sign = genSignGBK(content, KEYVALUE);
			config.put(KEY_CONSTANT_SIGN, sign);
			// 4. 发送post请求
			String result = super.doPost(config);
			// 5. 组织返回结果对象
			Response rep = super.unmarshal(result, Response.class);
			response = restoreWarehouseResponse(rep);
			// 6. 记录日志
			logXml(serviceName, content, result, rep.isSuccess() ? WarehouseLogState.SUCESS : WarehouseLogState.FAILURE);
		} catch (Exception e) {
			response = new WMSResponseDTO(WMSResponseDTO.FLAG_EXCEPTION, e.getMessage(), e);
			logException(serviceName, object, e);
		}
		return response;
	}

	protected WMSResponseDTO restoreWarehouseResponse(Response rep) {
		WMSResponseDTO response = null;
		if (rep.isSuccess()) {
			response = new WMSResponseDTO(WMSResponseDTO.FLAG_SUCESS, rep.getReason());
		} else {
			response = new WMSResponseDTO(WMSResponseDTO.FLAG_FAILURE, rep.getReason());
		}
		return response;
	}

	public String genSignGBK(String content, String keyValue) {
		String charset = "GBK";
		if (keyValue != null) {
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}

	public String genSignUTF8(String content, String keyValue) {
		String charset = "UTF-8";
		if (keyValue != null) {
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapterCallback#onCallback(java.util.Map)
	 */
	@Override
	public Object onCallback(Map<String, Object> params) {
		String serviceName = (String) params.get(KEY_CONSTANT_SERVICE);
		String appkey = (String) params.get(KEY_CONSTANT_APPKEY);
		String sign = (String) params.get(KEY_CONSTANT_SIGN);
		String content = (String) params.get(KEY_CONSTANT_CONTENT);
		logCallParams(serviceName, params);
		boolean success = true;
		String message = null;

		if (serviceName == null) {
			success = false;
			message = "undefined service['null']";
//		} else if (appkey == null || !appKeyMap.containsValue(appkey)) {
//			success = false;
//			message = "appkey mismatch: " + appkey;
		} else if (!isDebug && (sign == null || !sign.equals(genSignUTF8(content, this.KEYVALUE)))) {
			success = false;
			message = "wrong md5 code.";
		}

		if (!success) {
			return new Response(Response.KEY_FARILURE, message);
		}

		try {
			if (serviceName.equals(SERVICENAME_SHIPORDER_CONFIRM)) {
				success = onWmsShipOrderConfirm(content);
			} else if (serviceName.equals(SERVICENAME_SHIPOUTORDER_CONFIRM)) {
				success = processShipOutOrderConfirm(content);
			} else if (serviceName.equals(SRVICENAME_SALESORDER_UPDATE)) {
				success = processSalesOrderUpdate(content);
			} else if (serviceName.equals(SRVICENAME_SALESORDER_TRACE)) {
				EmsTraceInfoResponse r = processTraceInfo(content);
				logEmsTraceResponse(r);
				return r;
			} else {
				success = false;
				message = "undefined service: " + serviceName;
			}
		} catch (Throwable e) {
			success = false;
			if (e instanceof WarehouseCallerException) {
				message = e.getMessage();
			} else {
				message = "服务处理失败.";
			}
			logException(serviceName, params, e);
		}
		// 记录一下处理结果
		logString(serviceName, message, success ? WarehouseLogState.SUCESS : WarehouseLogState.FAILURE);
		return new Response(success ? Response.KEY_SUCCESS : Response.KEY_FARILURE, message);
	}

	private boolean onWmsShipOrderConfirm(String content) throws Exception {
		WmsStockInConfirm confirm = super.unmarshal(content, WmsStockInConfirm.class);
		WMSOrderType type = OmsIdUtils.judgeEmsWMSOrderType(confirm.getAsn_id());
		if (type == WMSOrderType.RETURN || type == WMSOrderType.R_UA) {
			return processReturnOrderConfirm(confirm);
		} else {
			return processShipOrderConfirm(confirm);
		}
	}

	private boolean processShipOrderConfirm(WmsStockInConfirm confirm) {
		WMSOrderType type = OmsIdUtils.judgeEmsWMSOrderType(confirm.getAsn_id());
		confirm.setAsn_id(OmsIdUtils.backEmsOrderId(confirm.getAsn_id()));
		WMSShipOrderUpdateDTO shipOrder = super.map(confirm, WMSShipOrderUpdateDTO.class);
		if (shipOrder.getOperaterTime() == 0) {
			shipOrder.setOperaterTime(System.currentTimeMillis());
		}
		shipOrder.setOrderType(type);
		shipOrder.setState(WMSShipOrderState.DONE);
		shipOrder.calculateCount();
		return super.onShipOrderStateChange(shipOrder);
	}

	private boolean processReturnOrderConfirm(WmsStockInConfirm confirm) {
		WMSOrderType orderType = OmsIdUtils.judgeEmsWMSOrderType(confirm.getAsn_id());
		confirm.setAsn_id(OmsIdUtils.backEmsOrderId(confirm.getAsn_id()));
		WMSReturnOrderUpdateDTO returnOrder = new WMSReturnOrderUpdateDTO();
		returnOrder.setOperaterTime(DateUtils.parseToLongtime(confirm.getReceive_time()));
		returnOrder.setReceiveTime(DateUtils.parseToLongtime(confirm.getReceive_time()));
		returnOrder.setState(WMSReturnOrderState.DONE);
		returnOrder.setOrderId(confirm.getAsn_id());
		returnOrder.setOrderType(orderType);
		if (confirm.getDetails() != null) {
			for (EmsSku sku : confirm.getDetails()) {
				WMSSkuDetailDTO omsSku = super.map(sku, WMSSkuDetailDTO.class);
				returnOrder.addSkuDetail(omsSku);
			}
		}
		if (returnOrder.getOperaterTime() == 0) {
			returnOrder.setOperaterTime(System.currentTimeMillis());
		}
		returnOrder.calculateCount();
		return super.onReturnOrderStateChange(returnOrder);
	}

	private boolean processShipOutOrderConfirm(String content) throws Exception {
		WmsStockOutConfirm stockOutConfirm = super.unmarshal(content, WmsStockOutConfirm.class);
		stockOutConfirm.setOrder_id(OmsIdUtils.backEmsOrderId(stockOutConfirm.getOrder_id()));
		WMSShipOutOrderUpdateDTO shipOutOrder = super.map(stockOutConfirm, WMSShipOutOrderUpdateDTO.class);
		if (shipOutOrder.getOperaterTime() == 0) {
			shipOutOrder.setOperaterTime(System.currentTimeMillis());
		}
		shipOutOrder.setState(WMSShipOutOrderState.DONE);
		shipOutOrder.calculateCount();
		return super.onShipOutOrderStateChange(shipOutOrder);
	}

	private boolean processSalesOrderUpdate(String content) throws Exception {
		WmsOrderStatusUpdate orderStatus = super.unmarshal(content, WmsOrderStatusUpdate.class);
		orderStatus.setOrder_id(OmsIdUtils.backEmsOrderId(orderStatus.getOrder_id()));
		WMSSalesOrderUpdateDTO salesOrder = super.map(orderStatus, WMSSalesOrderUpdateDTO.class);
		WMSSalesOrderState state = getSalesOrderStateByEmsStatus(orderStatus.getStatus());
		salesOrder.setState(state);
		if (salesOrder.getOperaterTime() == 0) {
			salesOrder.setOperaterTime(System.currentTimeMillis());
		}
		// 如果包裹快递公司为空设置默认快递公司
		if (salesOrder.getLogisticCode() == null || salesOrder.getLogisticCode().isEmpty()) {
			salesOrder.setLogisticCode(WMSExpressCompany.EMS.name());
		}
		if (salesOrder.getPackages() != null) {
			for(WMSPackageDTO pack : salesOrder.getPackages()) {
				if (pack.getExpressCompany() == null || pack.getExpressCompany().isEmpty()) {
					pack.setExpressCompany(salesOrder.getLogisticCode());
				}
			}
		}
		salesOrder.calculateCount();
		return super.onSalesOrderStateChange(salesOrder);
	}

	
	private EmsTraceInfoResponse processTraceInfo(String content)  {
		EmsTraceInfoResponse traceInfoResponse = new EmsTraceInfoResponse(); 
		EmsTraceInfo traceInfo = null;
		try {
			traceInfo = super.unmarshal(content, EmsTraceInfo.class);
			traceInfoResponse = doTraceTransaction(traceInfo);
		} catch (WarehouseCallerException e) {
			logException(SRVICENAME_SALESORDER_TRACE, content, e);
			if (traceInfo != null && traceInfo.getRows() != null) {
				for (EmsTrace trace : traceInfo.getRows()) {
					EmsTraceResponse traceResponse = new EmsTraceResponse();
					traceResponse.setTransaction_id(trace.getTransaction_id());
					traceResponse.setSuccess(EmsTraceResponse.KEY_FARILURE);
					traceInfoResponse.addRow(traceResponse);
				}
			}
		} catch (XmlMappingException | UnsupportedEncodingException e) {
			logException(SRVICENAME_SALESORDER_TRACE, content, e);
		} 
		return traceInfoResponse;
	}
	
	@Transaction
	private EmsTraceInfoResponse doTraceTransaction (EmsTraceInfo traceInfo) throws WarehouseCallerException{
		EmsTraceInfoResponse traceInfoResponse = new EmsTraceInfoResponse();
		if (traceInfo.getRows() != null) {
			List<WMSOrderTrace> orderTraces = new ArrayList<WMSOrderTrace>();
			for (EmsTrace trace : traceInfo.getRows()) {
				// 1.获取订单类型
				WMSOrderType orderType = OmsIdUtils.judgeEmsWMSOrderType(trace.getOrder_id());
				// 2.转换订单id
				trace.setOrder_id(OmsIdUtils.backEmsOrderId(trace.getOrder_id()));
				// 3.将ems的轨迹对象转换为oms轨迹对象
				WMSOrderTrace orderTrace = toWMSOrderTrace(trace);
				// 4.判断数据合法性并
				EmsTraceResponse traceResponse = checkEmsTrace(trace);
				traceInfoResponse.addRow(traceResponse);
				if (traceResponse.isSuccess()) {
					orderTraces.add(orderTrace);
				}
				// 5.如果是销售订单则更新销售订单的状态
				if (orderType == WMSOrderType.SALES) {
					boolean success = processSalesOrderPackageStateUpdate(trace);
					if (!success) {
						traceResponse.setSuccess(EmsTraceResponse.KEY_FARILURE);
					}
				}
			}
			boolean traceProcessSucess = super.onOrderTraceChange(orderTraces);
			// 如果处理不成功，将所有的轨迹处理标记为不成功
			if (!traceProcessSucess) {
				throw new WarehouseCallerException("处理轨迹失败.");
			}
		}
		return traceInfoResponse;
	}

	private boolean processSalesOrderPackageStateUpdate(EmsTrace trace) {
		WMSPackageState state = getWMSPackageState(trace);
		//如果不能取得正确的包裹状态则不更新销订单的状态
		if (state == null) {
			return true;
		}
		WMSSalesOrderUpdateDTO salesOrder = new WMSSalesOrderUpdateDTO();
		salesOrder.setState(WMSSalesOrderState.WAYUPDATE);
		salesOrder.setOrderId(trace.getOrder_id());
		long time = DateUtils.parseToLongtime(trace.getOperate_time());
		if (time == 0) {
			salesOrder.setOperaterTime(System.currentTimeMillis());
		} else {
			salesOrder.setOperaterTime(time);
		}
		// 因为这里ems的轨迹回传的只会是EMS的快递单轨迹，so，写死ems
		salesOrder.setLogisticCode(WMSExpressCompany.EMS.getName());
		WMSPackageDTO pack = new WMSPackageDTO();
		pack.setExpressCompany(salesOrder.getLogisticCode());
		pack.setShipNo(trace.getMailno());
		pack.setState(state);
		salesOrder.addPackage(pack);
		return super.onSalesOrderStateChange(salesOrder);
	}

	private WMSPackageState getWMSPackageState(EmsTrace trace) {
		EmsTraceOperateType operateType = EmsTraceOperateType.getEnumByIntValue(Integer.parseInt(trace.getOperate_type()));
		switch (operateType) {
		case TYPE_50:
			return WMSPackageState.SEND;
		case TYPE_10:
			EmsDelivery acd = EmsDelivery.matchByLable(trace.getExt_attr1());
			if(acd == null) {
				return null;
			} else {
				return EmsDelivery.judgeRealSignedByLable(acd) ? WMSPackageState.SIGNED : WMSPackageState.RETWAREHOUSE;
			}
		case TYPE_20:
			EmsUndelivery unacd = EmsUndelivery.matchByLable(trace.getExt_attr2());
			if(unacd == null) {
				return null;
			} else {
				return WMSPackageState.UNRECEIPTED;
			}
		default:
			return WMSPackageState.WAY;
		}
	}

	private EmsTraceResponse checkEmsTrace(EmsTrace trace) {
		EmsTraceResponse traceResponse = new EmsTraceResponse();
		traceResponse.setTransaction_id(trace.getTransaction_id());
		traceResponse.setSuccess(EmsTraceResponse.KEY_FARILURE);
		if (StringUtils.isBlank(trace.getTransaction_id())) {
			traceResponse.setReason("事务ID为空.");
		} else if (StringUtils.isBlank(trace.getMailno())) {
			traceResponse.setReason("运单号为空.");
		} else if (StringUtils.isBlank(trace.getOperate_time())) {
			traceResponse.setReason("操作时间为空.");
		} else if (StringUtils.isBlank(trace.getOperate_type())) {
			traceResponse.setReason("操作类型为空.");
		} else if (StringUtils.isBlank(trace.getOperate_desc())) {
			traceResponse.setReason("操作描述为空.");
		} else {
			traceResponse.setSuccess(EmsTraceResponse.KEY_SUCCESS);
		}
		return traceResponse;
	}

	private WMSOrderTrace toWMSOrderTrace(EmsTrace trace) {
		WMSOrderTrace orderTrace = super.map(trace, WMSOrderTrace.class);
		if (orderTrace.getLogisticCompany() == null || orderTrace.getLogisticCompany().trim().length() == 0) {
			orderTrace.setLogisticCompany(WMSExpressCompany.EMS.getName());
		}
		if (!StringUtils.isBlank(orderTrace.getSignedInfo())) {
			EmsDelivery details = EmsDelivery.matchByLable(orderTrace.getSignedInfo());
			if(details == null) {
				logString(SRVICENAME_SALESORDER_TRACE, "不能识别签收明细[".concat(orderTrace.getSignedInfo()).concat("]."), WarehouseLogState.EXCEPTION);
				orderTrace.setChildOperate(orderTrace.getSignedInfo());
				orderTrace.setChildOperateDesc(orderTrace.getSignedInfo());
			} else {
				orderTrace.setChildOperate(details.getCode());
				orderTrace.setChildOperateDesc(details.getLable());
			}
		} else if (!StringUtils.isBlank(orderTrace.getUnreceiptedInfo())) {
			EmsUndelivery details = EmsUndelivery.matchByLable(orderTrace.getUnreceiptedInfo());
			if(details == null) {
				logString(SRVICENAME_SALESORDER_TRACE, "不能识别未签收明细[".concat(orderTrace.getUnreceiptedInfo()).concat("]."), WarehouseLogState.EXCEPTION);
				orderTrace.setChildOperate(orderTrace.getUnreceiptedInfo());
				orderTrace.setChildOperateDesc(orderTrace.getUnreceiptedInfo());
			} else {
				orderTrace.setChildOperate(details.getCode());
				orderTrace.setChildOperateDesc(details.getLable());
			}
		} else {

		}
		return orderTrace;
	}

	private WMSSalesOrderState getSalesOrderStateByEmsStatus(String status) {
		WMSSalesOrderState state = WMSSalesOrderState.genEnumNameIgnoreCase(status);
		return state == null ? WMSSalesOrderState.NULL : state;
	}

	private void logException(String serviceName, Object request, Throwable e) {
		String requestDetails = request instanceof String ? JsonLibUtils.xml2json((String)request) : JsonLibUtils.object2json(request);
		String responseDetails = ExceptionUtils.getFullStackTrace(e);
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"request\":").append(requestDetails).append(",");
		sb.append("\"exception\":").append(responseDetails);
		sb.append("}");
		WarehouseLog log = new WarehouseLog();
		log.setType(serviceName);
		log.setTime(DateUtils.format(new Date()));
		log.setDetails(sb.toString());
		log.setWarehouseCode(WarehouseType.EMS.name());
		log.setState(WarehouseLogState.EXCEPTION);
		logService.log(log, true);
		if (exceptionReceivers != null && e instanceof WarehouseCallerException) {
			try {
				for(String exceptionReceiver : exceptionReceivers) {
					pushService.sendMail(MailType.SUBSCRIBE, exceptionReceiver, "EMS[".concat(serviceName).concat("服务]回调异常").concat(DateUtils.format(new Date())), sb.toString());
				}
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}

	private void logCallParams(String serviceName, Map<String, Object> params) {
		if (isTrace) {
			String requestDetails = JsonLibUtils.object2json(params);
			WarehouseLog log = new WarehouseLog();
			log.setType(serviceName);
			log.setTime(DateUtils.format(new Date()));
			log.setDetails(requestDetails);
			log.setWarehouseCode(WarehouseType.EMS.name());
			log.setState(WarehouseLogState.NORMAL);
			logService.log(log, true);
		}
	}

	private void logString(String serviceName, String details, WarehouseLogState state) {
		if (isTrace) {
			StringBuilder sb = new StringBuilder("{");
			sb.append("\"details\":").append(details == null ? "" : details);
			sb.append("}");
			WarehouseLog log = new WarehouseLog();
			log.setType(serviceName);
			log.setTime(DateUtils.format(new Date()));
			log.setDetails(sb.toString());
			log.setWarehouseCode(WarehouseType.EMS.name());
			log.setState(state);
			logService.log(log, true);
		}
	}

	private void logXml(String serviceName, String requestXML, String responseXML, WarehouseLogState state) {
		if (isTrace) {
			String requestDetails = JsonLibUtils.xml2json(requestXML);
			String responseDetails = JsonLibUtils.xml2json(responseXML);
			StringBuilder sb = new StringBuilder("{");
			sb.append("\"request\":").append(requestDetails).append(",");
			sb.append("\"response\":").append(responseDetails);
			sb.append("}");
			WarehouseLog log = new WarehouseLog();
			log.setType(serviceName);
			log.setTime(DateUtils.format(new Date()));
			log.setDetails(sb.toString());
			log.setWarehouseCode(WarehouseType.EMS.name());
			log.setState(state);
			logService.log(log, true);
		}
	}
	
	private void logEmsTraceResponse (EmsTraceInfoResponse response) {
		if (isTrace) {
			try {
				String responseStr = super.marshal(response);
				boolean success = response.getRows() !=null && response.getRows().size() >0 && response.getRows().get(0).isSuccess();
				logString(SRVICENAME_SALESORDER_TRACE, responseStr, success ? WarehouseLogState.SUCESS : WarehouseLogState.FAILURE);
			} catch (Exception e) {
				logException(SRVICENAME_SALESORDER_TRACE, "", e);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.WarehouseAdapterCallback#getWarehouseType()
	 */
	@Override
	public WarehouseType getWarehouseType() {
		return WarehouseType.EMS;
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
		return new String[] { "com.xyl.mmall.oms.warehouse.adapter.ems" };
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getUrl()
	 */
	@Override
	protected String getUrl() {
		return this.EMSURL;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.warehouse.AbstractWarehouseAdapter#getCharSet()
	 */
	@Override
	protected String getCharSet() {
		return "UTF-8";
	}

}
