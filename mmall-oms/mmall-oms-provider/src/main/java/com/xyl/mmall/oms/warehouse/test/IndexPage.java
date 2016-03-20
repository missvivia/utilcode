package com.xyl.mmall.oms.warehouse.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormDao;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormSkuDao;
import com.xyl.mmall.oms.dao.PickOrderDao;
import com.xyl.mmall.oms.dao.PickSkuDao;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormDao;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormSkuDao;
import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.enums.WMSOrderType;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;

@Service
public class IndexPage extends AbstractSimulatorPage {
	public static final String SERVICENAME = "/";
	
	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	@Autowired
	private PickOrderDao pickOrderDao;

	@Autowired
	private PickSkuDao pickOrderSkuDao;

	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private ShipSkuDao shipOrderSkuDao;

	@Autowired
	private ReturnPoOrderFormDao poReturnOrderDao;

	@Autowired
	private ReturnPoOrderFormSkuDao poReturnSkuDao;

	@Autowired
	private OmsReturnOrderFormDao returnOrderDao;

	@Autowired
	private OmsReturnOrderFormSkuDao returnSkuDao;

	public IndexPage() {
		super("模拟ems回调");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializeBody(Object data) {
		long orderId = data == null ? 0 : Long.parseLong(((Map<String, Object>) data).get("orderId").toString());
		String requestUrl = ((Map<String, Object>) data).get("requestUrl") != null ? ((Map<String, Object>) data).get(
				"requestUrl").toString() : "http://10.165.124.11:8181/oms/wms/ems/";

		append("操作说明：1、输入单号(用户订单号、用户退货单号、商家退货单号)并点ok -> 2、选择并点击模拟类型(会打开新页面) -> 3、点击ok进行模拟数据发送");
		append("<hr>");
		openForm(getServiceUrl(), "GET")
				.append("<span>请输入单号(用户订单号、用户退货单号、商家退货单号)</span><input name=\"orderId\" value=\"")
				.append(String.valueOf(orderId))
				.append("\" type=\"text\"/>，请求地址(CMS)<input name=\"requestUrl\" value=\"" + requestUrl
						+ "\" type=\"text\"/>");
		append("<hr>");
		closeForm();
		OmsOrderForm a_omsOrder = null;
		String id_ems_order = "";
		ShipOrderForm a_shipOrder = null;
		String id_ems_shipOrder = "";
		ReturnPoOrderForm a_poReturnOrder = null;
		String id_ems_poReturnOrder = "";
		OmsReturnOrderForm a_returnOrder = null;
		String id_ems_returnOrder = "";
		
		if (orderId > 0) {
			openTable();
			Map<String, List<PickSkuItemForm>> pickSkuMap = new HashMap<String, List<PickSkuItemForm>>();
			Map<String, Long> pickIdSupplierIdMap = new HashMap<String, Long>();
			List<PickSkuItemForm> unPickSkus = new ArrayList<PickSkuItemForm>();

			// 销售单
			List<OmsOrderForm> omsOrders = omsOrderFormDao.getListByUserOrderFormId(orderId);
			if (omsOrders != null && omsOrders.size() > 0) {
				a_omsOrder = omsOrders.get(0);
				id_ems_order = OmsIdUtils.genEmsOrderId(String.valueOf(a_omsOrder.getOmsOrderFormId()),
						WMSOrderType.SALES);
				for (OmsOrderForm omsOrder : omsOrders) {
					String tempOrderId = OmsIdUtils.genEmsOrderId(String.valueOf(omsOrder.getOmsOrderFormId()),
							WMSOrderType.SALES);
					appendRow("OMS销售单:" + tempOrderId + " 状态：" + omsOrder.getOmsOrderFormState().getName());
					List<OmsOrderFormSku> skus = omsOrderFormSkuDao.queryByOmsOrderFormId(omsOrder.getOmsOrderFormId(),
							omsOrder.getUserId());
					if (skus != null && skus.size() > 0) {
						openTr().openTd().openTable(1, "SKUID", "产品名称", "数量");
						for (OmsOrderFormSku sku : skus) {
							appendRow(String.valueOf(sku.getSkuId()), sku.getProductName(),
									String.valueOf(sku.getTotalCount()));
						}
						closeTable().closeTd().closeTr();
					}

					List<PickSkuItemForm> tempSkus = pickOrderSkuDao.getByOmsOrderFormId(omsOrder.getOmsOrderFormId());
					if (tempSkus != null) {
						for (PickSkuItemForm pickSku : tempSkus) {
							if (pickSku.getPickOrderId() == null || pickSku.getPickOrderId().trim().length() == 0) {
								if (!isExist(pickSku, unPickSkus)) {
									unPickSkus.add(pickSku);
								}
							} else {
								List<PickSkuItemForm> currentList = null;
								if (!pickSkuMap.containsKey(pickSku.getPickOrderId())) {
									currentList = new ArrayList<PickSkuItemForm>();
									pickSkuMap.put(pickSku.getPickOrderId(), currentList);
									pickIdSupplierIdMap.put(pickSku.getPickOrderId(), pickSku.getSupplierId());
								} else {
									currentList = pickSkuMap.get(pickSku.getPickOrderId());
								}
								currentList.add(pickSku);

							}
						}
					}
				}
			}

			// 捡货单
			for (String pickOrderId : pickSkuMap.keySet()) {
				PickOrderForm pickOrder = pickOrderDao.getPickOrder(pickOrderId, pickIdSupplierIdMap.get(pickOrderId));
				if (pickOrder == null) {
					continue;
				}
				appendRow("OMS捡货单:" + pickOrder.getPickOrderId() + " 状态：" + pickOrder.getPickState().getDesc());
				List<PickSkuItemForm> skus = pickSkuMap.get(pickOrderId);
				if (skus != null && skus.size() > 0) {
					openTr().openTd().openTable(1, "SKUID", "产品名称", "数量");
					for (PickSkuItemForm sku : skus) {
						appendRow(String.valueOf(sku.getSkuId()), sku.getProductName(),
								String.valueOf(sku.getSkuQuantity()));
					}
					closeTable().closeTd().closeTr();
				}
			}
			// 该订单下还未捡货的sku
			if (unPickSkus.size() > 0) {
				append("<tr><td>OMS未捡货</td><td>--</td><td>还未捡货</td>");
				appendRow("OMS还未捡货的商品列表");
				openTr().openTd().openTable(1, "SKUID", "产品名称", "数量");
				for (PickSkuItemForm sku : unPickSkus) {
					appendRow(String.valueOf(sku.getSkuId()), sku.getProductName(),
							String.valueOf(sku.getSkuQuantity()));
				}
				closeTable().closeTd().closeTr();
			}

			// 发货单，发货单
			for (String pickOrderId : pickSkuMap.keySet()) {
				ShipOrderForm shipOrder = shipOrderDao.getShipOrderByPickOrderId(pickOrderId,
						pickIdSupplierIdMap.get(pickOrderId));
				if (shipOrder == null) {
					continue;
				}
				if (a_shipOrder == null) {
					a_shipOrder = shipOrder;
					id_ems_shipOrder = OmsIdUtils.genEmsOrderId(String.valueOf(shipOrder.getShipOrderId()),
							WMSOrderType.SI_S);
				}
				String temp = OmsIdUtils.genEmsOrderId(String.valueOf(shipOrder.getShipOrderId()),
						WMSOrderType.SI_S);
				appendRow("OMS发货单:" + temp + " 状态：" + shipOrder.getShipState().getDesc());

				List<ShipSkuItemForm> skus = shipOrderSkuDao.getShipSkuList(shipOrder.getShipOrderId());
				if (skus != null && skus.size() > 0) {
					openTr().openTd().openTable(1, "SKUID", "产品名称", "数量");
					for (ShipSkuItemForm sku : skus) {
						appendRow(String.valueOf(sku.getSkuId()), sku.getProductName(),
								String.valueOf(sku.getSkuQuantity()));
					}
					closeTable().closeTd().closeTr();
				}
			}

			// 商家退货单
			a_poReturnOrder = poReturnOrderDao.getObjectById(orderId);
			id_ems_poReturnOrder = a_poReturnOrder != null ? OmsIdUtils.genEmsOrderId(
					String.valueOf(a_poReturnOrder.getPoReturnOrderId()), WMSOrderType.SO) : "";
			if (a_poReturnOrder != null) {
				appendRow("OMS商家商品出仓单:" + id_ems_poReturnOrder + " 状态:" + a_poReturnOrder.getState().getDesc(), 3);
				List<ReturnPoOrderFormSku> skus = poReturnSkuDao.getListByPoReturnOrderId(orderId);
				if (skus != null) {
					openTr().openTd().openTable(1, "SKUID", "产品名称", "数量");
					for (ReturnPoOrderFormSku sku : skus) {
						appendRow(String.valueOf(sku.getSkuId()), sku.getProductName(), String.valueOf(sku.getCount()));
					}
				}
				closeTable().closeTd().closeTr();
			}

			// 用户退货单
			a_returnOrder = returnOrderDao.getObjectById(orderId);
			id_ems_returnOrder = a_returnOrder != null ? OmsIdUtils.genEmsOrderId(
					String.valueOf(a_returnOrder.getId()), WMSOrderType.RETURN) : "";
			if (a_returnOrder != null) {
				appendRow("OMS用户退货单:" + id_ems_returnOrder, 3);
				List<OmsReturnOrderFormSku> skus = returnSkuDao.getListByReturnId(a_returnOrder.getId(),
						a_returnOrder.getUserId());
				if (skus != null) {
					openTr().openTd().openTable(1, "SKUID", "产品名称", "数量");
					for (OmsReturnOrderFormSku sku : skus) {
						appendRow(String.valueOf(sku.getSkuId()), sku.getProductName(),
								String.valueOf(sku.getReturnCount()));
					}
				}
				closeTable().closeTd().closeTr();
			}
			closeTable();
			append("以下单号会根据输入自动判断类型，一般情况下是不需手动输入");
			append("<hr>");
		}
		appendSimulatorPageForm("商家商品入库单号", EmsWarehouseAdapter.SERVICENAME_SHIPORDER_CONFIRM, "模拟ems确认商品入库",
				id_ems_shipOrder);
		appendSimulatorPageForm("用户订单号", EmsWarehouseAdapter.SRVICENAME_SALESORDER_UPDATE, "模拟ems更新用户订单的状态",
				id_ems_order);
		appendSimulatorPageForm("订单轨迹,用户退货单号", EmsWarehouseAdapter.SRVICENAME_SALESORDER_TRACE, "模拟ems确回传订单轨迹",
				id_ems_order);
		appendSimulatorPageForm("用户退货单号", UserReturnOrderPage.SERVICE_NAME, "模拟ems确认收到用户的退货",
				id_ems_returnOrder);
		appendSimulatorPageForm("商家商品退货单号", EmsWarehouseAdapter.SERVICENAME_SHIPOUTORDER_CONFIRM, "模拟ems确认商家商品出仓",
				id_ems_poReturnOrder);
	}

	private void appendSimulatorPageForm(String inputLable, String serviceName, String buttonLable, String defaultVal) {
		append("<form target=\"_blank\" action=\"/omsutil/ems/simulatorPage/").append(serviceName)
				.append("\"><span>").append(inputLable).append("</span><input name=\"orderId\" value=\"").append(defaultVal)
				.append("\" type=\"text\"/><button>").append(buttonLable).append("</button></form>");
	}

	private boolean isExist(PickSkuItemForm pickSku, List<PickSkuItemForm> pickSkus) {
		for (PickSkuItemForm temp : pickSkus) {
			if (pickSku.getPickOrderId().equals(temp.getPickOrderId()) && pickSku.getSkuId().equals(temp.getSkuId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String serviceName() {
		return SERVICENAME;
	}

}
