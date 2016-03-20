package com.xyl.mmall.oms.warehouse.test;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.OmsReturnOrderFormDao;
import com.xyl.mmall.oms.dao.OmsReturnOrderFormSkuDao;
import com.xyl.mmall.oms.meta.OmsReturnOrderForm;
import com.xyl.mmall.oms.meta.OmsReturnOrderFormSku;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.util.DateUtils;

@Service
public class UserReturnOrderPage extends AbstractSimulatorPage {
	public static final String SERVICE_NAME = "userReturnOrder";

	@Autowired
	private OmsReturnOrderFormDao returnOrderDao;

	@Autowired
	private OmsReturnOrderFormSkuDao returnSkuDao;

	public UserReturnOrderPage() {
		super("模拟ems用户退货入库单确认");
	}

	@Override
	protected void initializeBody(Object data) {
		long orderId = Long.parseLong(OmsIdUtils.backEmsOrderId(data.toString()));
		OmsReturnOrderForm a_returnOrder = returnOrderDao.getObjectById(orderId);
		openForm(getBaseUrl().concat("/").concat(EmsWarehouseAdapter.SERVICENAME_SHIPORDER_CONFIRM));
		openTable("属性", "值");
		appendRowInput("入库单号", "asn_id", data.toString());
		appendRowInput("收货完成时间", "receive_time", DateUtils.format(new Date()));
		appendTd("订单商品明细", 2);
		List<OmsReturnOrderFormSku> skus = returnSkuDao.getListByReturnId(a_returnOrder.getId(),
				a_returnOrder.getUserId());
		if (skus != null) {
			openTr().openTd(2).openTable(1);
			for (int i = 0; i < skus.size(); i++) {
				OmsReturnOrderFormSku sku = skus.get(i);
				appendRow("商品" + sku.getProductName(), 2);
				openTr().openTd(2).openTable();
				appendRowInput("skuId", "details[" + i + "].sku_code", String.valueOf(sku.getSkuId()));
				appendRowInput("正品数量", "details[" + i + "].count", String.valueOf(sku.getReturnCount()));
				appendRowInput("次品数量", "details[" + i + "].junk_count", "0");
				closeTd().closeTr().closeTable();
			}
			closeTable().closeTd().closeTr();
		}
		closeTable().closeForm();
	}

	@Override
	public String serviceName() {
		return SERVICE_NAME;
	}

}
