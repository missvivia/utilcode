package com.xyl.mmall.oms.warehouse.test;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.ReturnPoOrderFormDao;
import com.xyl.mmall.oms.dao.ReturnPoOrderFormSkuDao;
import com.xyl.mmall.oms.meta.ReturnPoOrderForm;
import com.xyl.mmall.oms.meta.ReturnPoOrderFormSku;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.util.DateUtils;

@Service
public class WmsStockOutConfirmPage extends AbstractSimulatorPage {
	@Autowired
	private ReturnPoOrderFormDao poReturnOrderDao;

	@Autowired
	private ReturnPoOrderFormSkuDao poReturnSkuDao;

	public WmsStockOutConfirmPage() {
		super("模拟ems商家商品退仓单出仓确认");
	}

	@Override
	protected void initializeBody(Object data) {
		long orderId = data == null ? 0 : Long.parseLong(OmsIdUtils.backEmsOrderId(data.toString()));
		ReturnPoOrderForm a_poReturnOrder = poReturnOrderDao.getObjectById(orderId);

		openForm(getServiceUrl());
		appendRowInput("入库单号", "order_id", data.toString());
		appendRowInput("收货完成时间", "consign_time", DateUtils.format(new Date()));
		appendTd("订单商品明细", 2);
		if (a_poReturnOrder != null) {
			openTr().openTd(2).openTable(1);
			List<ReturnPoOrderFormSku> skus = poReturnSkuDao.getListByPoReturnOrderId(orderId);
			if (skus != null) {
				for (int i = 0; i < skus.size(); i++) {
					ReturnPoOrderFormSku sku = skus.get(i);
					appendRow("商品" + sku.getProductName(), 2);
					openTr().openTd(2).openTable();
					appendRowInput("skuId", "list[" + i + "].sku_code", String.valueOf(sku.getSkuId()));
					appendRowInput("正品数量", "list[" + i + "].count", String.valueOf(sku.getNormalCount()));
					appendRowInput("次品数量", "list[" + i + "].junk_count", String.valueOf(sku.getDefectiveCount()));
					closeTd().closeTr().closeTable();
				}
			}
			closeTable().closeTd().closeTr();
		}
		closeTable().closeForm();

	}

	@Override
	public String serviceName() {
		return EmsWarehouseAdapter.SERVICENAME_SHIPOUTORDER_CONFIRM;
	}

}
