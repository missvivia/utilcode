package com.xyl.mmall.oms.warehouse.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.testng.log4testng.Logger;

import com.xyl.mmall.oms.meta.PickSkuItemForm;
import com.xyl.mmall.oms.service.impl.PickOrderServiceImpl;
import com.xyl.mmall.oms.service.impl.PickSkuServiceImpl;
import com.xyl.mmall.oms.util.OmsUtil;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsSku;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTrace;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTraceInfo;
import com.xyl.mmall.oms.warehouse.adapter.ems.ShipDetail;
import com.xyl.mmall.oms.warehouse.adapter.ems.SkuDetail;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsOrderStatusUpdate;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsStockInConfirm;
import com.xyl.mmall.oms.warehouse.adapter.ems.WmsStockOutConfirm;

/**
 * @author hzzengchengyuan
 *
 */
@Controller
@RequestMapping("/omsutil/ems")
public class OmsUtilController {
	private Logger logger = Logger.getLogger(OmsUtilController.class);

	@Resource(name = "EmsCallSimulator")
	private EmsCallSimulator simulator;

	@Autowired
	private PickSkuServiceImpl pickSkuService;

	@Autowired
	private PickOrderServiceImpl pickOrderService;

	private Map<String, SimulatorPage> simulatorPages = new HashMap<String, SimulatorPage>();

	@RequestMapping("/")
	public void index(@RequestParam(required = false) Long orderId, @RequestParam(required = false) String requestUrl,
			HttpServletResponse response) throws IOException {
		if (requestUrl != null && requestUrl.trim().length() > 0) {
			simulator.setUrl(requestUrl);
		}
		long oi = orderId == null ? 0 : orderId;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", oi);
		map.put("requestUrl", requestUrl);
		response.setContentType("text/html");
		response.getWriter().write(getSimulatorPage(IndexPage.SERVICENAME).initialize(map).toHTML());
	}

	@RequestMapping("/simulatorPage/{page}")
	public void getSimulatorPage(@PathVariable("page") String pageName, @RequestParam String orderId,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.getWriter().write(getSimulatorPage(pageName).initialize(orderId).toHTML());
	}

	@RequestMapping(EmsWarehouseAdapter.SERVICENAME_SHIPORDER_CONFIRM)
	public void wmsStockInConfirm(WmsStockInConfirm confirm, HttpServletResponse response) throws IOException {
		// 剔除不符合条件的数据
		List<EmsSku> skus = confirm.getDetails();
		List<EmsSku> newSkus = new ArrayList<EmsSku>();
		if (skus != null) {
			for (EmsSku sku : skus) {
				if (isBlank(sku.getSku_code())) {
				} else {
					newSkus.add(sku);
				}
			}
		}
		confirm.setDetails(newSkus);
		simulator.doSimulator(EmsWarehouseAdapter.SERVICENAME_SHIPORDER_CONFIRM, confirm);
		response.setContentType("text/html");
		response.getWriter().write(simulator.toHTML());
	}

	@RequestMapping(EmsWarehouseAdapter.SERVICENAME_SHIPOUTORDER_CONFIRM)
	public void wmsStockOutConfirm(WmsStockOutConfirm confirm, HttpServletResponse response) throws IOException {
		// 剔除不符合条件的数据
		List<SkuDetail> skus = confirm.getList();
		List<SkuDetail> newSkus = new ArrayList<SkuDetail>();
		if (skus != null) {
			for (SkuDetail sku : skus) {
				if (isBlank(sku.getSku_code())) {
				} else {
					newSkus.add(sku);
				}
			}
		}
		confirm.setList(newSkus);
		simulator.doSimulator(EmsWarehouseAdapter.SERVICENAME_SHIPOUTORDER_CONFIRM, confirm);
		response.setContentType("text/html");
		response.getWriter().write(simulator.toHTML());
	}

	@RequestMapping(EmsWarehouseAdapter.SRVICENAME_SALESORDER_UPDATE)
	public void wmsOrderStatusUpdate(WmsOrderStatusUpdate orderStatusUpdate, HttpServletResponse response)
			throws IOException {
		// 剔除不符合条件的数据
		List<ShipDetail> shipDetails = orderStatusUpdate.getList();
		orderStatusUpdate.setList(null);
		if (shipDetails != null) {
			List<ShipDetail> newShips = new ArrayList<ShipDetail>();
			for (ShipDetail ship : shipDetails) {
				if (isBlank(ship.getMailno())) {
				} else {
					List<SkuDetail> skus = ship.getList();
					List<SkuDetail> newSkus = new ArrayList<SkuDetail>();
					if (skus != null) {
						for (SkuDetail sku : skus) {
							if (isBlank(sku.getSku_code())) {

							} else {
								newSkus.add(sku);
							}
						}
					}
					ship.setList(newSkus);
					newShips.add(ship);
				}
			}
			orderStatusUpdate.setList(newShips);
		}
		simulator.doSimulator(EmsWarehouseAdapter.SRVICENAME_SALESORDER_UPDATE, orderStatusUpdate);
		response.setContentType("text/html");
		response.getWriter().write(simulator.toHTML());
	}

	@RequestMapping(EmsWarehouseAdapter.SRVICENAME_SALESORDER_TRACE)
	public void emsTrace(EmsTraceInfo emsTraceInfo, HttpServletResponse response) throws IOException {
		// 剔除不符合条件的数据
		List<EmsTrace> traces = emsTraceInfo.getRows();
		emsTraceInfo.setRows(null);
		if (traces != null) {
			List<EmsTrace> newTrace = new ArrayList<EmsTrace>();
			for (EmsTrace trace : traces) {
				if (isBlank(trace.getMailno()) && isBlank(trace.getOrder_id())) {
				} else {
					newTrace.add(trace);
				}
			}
			emsTraceInfo.setRows(newTrace);
		}
		simulator.doSimulator(EmsWarehouseAdapter.SRVICENAME_SALESORDER_TRACE, emsTraceInfo);
		response.setContentType("text/html");
		response.getWriter().write(simulator.toHTML());
	}

	private boolean isBlank(String text) {
		return text == null || text.trim().length() == 0 || text.trim().equals("null") || text.trim().equals("0");
	}

	public SimulatorPage getSimulatorPage(String serviceName) {
		return this.simulatorPages.get(serviceName.toUpperCase());
	}

	@Autowired
	public void setSimulatorPages(SimulatorPage[] p_simulatorPages) {
		if (p_simulatorPages != null) {
			for (SimulatorPage page : p_simulatorPages) {
				this.simulatorPages.put(page.serviceName().toUpperCase(), page);
			}
		}
	}
	
	
	
	@RequestMapping("/generatePickOrder")
	public void generatePickOrder() {
		int limit = 20;
		Set<Long> processedSuppilerIdSet = new LinkedHashSet<Long>();
		// long startCreateTime = OmsUtil.getCurrentBoci().getBociStartTime();
		long startCreateTime = System.currentTimeMillis();
		long currentBociDeadLine = startCreateTime;
		List<PickSkuItemForm> list = this.pickSkuService.getUnPickListByCreateTime(startCreateTime, limit);
		while (!list.isEmpty()) {
			for (PickSkuItemForm pickSkuItemForm : list) {
				try {
					long supplierId = pickSkuItemForm.getSupplierId();
					if (processedSuppilerIdSet.contains(supplierId))
						continue;
					else
						processedSuppilerIdSet.add(supplierId);
					pickOrderService.createPickOrderFormAndShipOrder(supplierId, currentBociDeadLine);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			startCreateTime = list.get(list.size() - 1).getCreateTime();
			list = this.pickSkuService.getUnPickListByCreateTime(startCreateTime, limit);
		}

	}
}
