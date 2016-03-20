package com.xyl.mmall.oms.warehouse.test;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.ShipOrderDao;
import com.xyl.mmall.oms.dao.ShipSkuDao;
import com.xyl.mmall.oms.meta.ShipOrderForm;
import com.xyl.mmall.oms.meta.ShipSkuItemForm;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.util.DateUtils;

@Service
public class WmsStockInConfirmPage extends AbstractSimulatorPage {
	@Autowired
	private ShipOrderDao shipOrderDao;

	@Autowired
	private ShipSkuDao shipOrderSkuDao;

	public WmsStockInConfirmPage() {
		super("模拟ems商家商品入库单确认");
	}

	@Override
	protected void initializeBody(Object data) {
		ShipOrderForm shipOrder = shipOrderDao.getShipOrderByShipId(OmsIdUtils.backEmsOrderId(data.toString()));
		List<ShipSkuItemForm> skus = shipOrderSkuDao.getShipSkuList(shipOrder.getShipOrderId());
		
		openForm("/omsutil/ems/" + EmsWarehouseAdapter.SERVICENAME_SHIPORDER_CONFIRM);
		openTable("属性", "值");
		appendRowInput("入库单号", "asn_id", data.toString());
		appendRowInput("收货完成时间", "receive_time", DateUtils.format(new Date()));
		appendTd("订单商品明细", 2);
		
		if (skus != null) {
			openTr().openTd(2).openTable(1);
			for (int i = 0; i < skus.size(); i++) {
				ShipSkuItemForm sku = skus.get(i);
				appendRow("商品" + sku.getProductName(), 2);
				openTr().openTd(2).openTable();
				appendRowInput("skuId", "details[" + i + "].sku_code", String.valueOf(sku.getSkuId()));
				appendRowInput("正品数量", "details[" + i + "].count", String.valueOf(sku.getSkuQuantity()));
				appendRowInput("次品数量", "details[" + i + "].junk_count", "0");
				closeTd().closeTr().closeTable();
			}
			closeTable().closeTd().closeTr();
		}
		
		closeTable().closeForm();
	}

	@Override
	public String serviceName() {
		return EmsWarehouseAdapter.SERVICENAME_SHIPORDER_CONFIRM;
	}

}
