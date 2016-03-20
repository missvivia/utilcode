package com.xyl.mmall.oms.warehouse.test;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderFormSkuDao;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderFormSku;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.util.DateUtils;

@Service
public class WmsOrderStatusUpdatePage extends AbstractSimulatorPage {
	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderFormSkuDao omsOrderFormSkuDao;

	public WmsOrderStatusUpdatePage() {
		super("模拟ems销售订单（用户订单）状态更新");
	}

	@Override
	protected void initializeBody(Object data) {
		long orderId = Long.parseLong(OmsIdUtils.backEmsOrderId(data.toString()));
		OmsOrderForm omsOrder = omsOrderFormDao.getObjectById(orderId);
		List<OmsOrderFormSku> skus = omsOrderFormSkuDao.queryByOmsOrderFormId(omsOrder.getOmsOrderFormId(),
				omsOrder.getUserId());
		openForm(getServiceUrl()).openTable();
		appendRowInput("入库单号", "order_id", data.toString());
		appendRowInput("时间", "operate_time", DateUtils.format(new Date()));
		appendRowInput("快递公司编码", "LogisticProviderId", "EMS");
		openTr().openTd()
				.append("当前执行的操作")
				.closeTd()
				.openTd()
				.appendOption("status", "RECVFAILED", "接单失败", "RECVFAILED", "接单失败", "PRINT", "打单", "PICK", "拣货",
						"CHECK", "复核", "PACKAGE", "打包", "SHIP", "发货", "CANCEL", "取消发货").closeTd().closeTr();
		appendRow("订单商品包裹明细", 2);

		// 包裹明细开始
		openTr().openTd(2).openTable(1);
		appendRow("包裹", 2);
		appendRowInput("快递单号", "list[0].mailno", "0");
		appendRowInput("包裹重量", "list[0].weight", "0.0");
		if (skus != null) {
			for (int i = 0; i < skus.size(); i++) {
				OmsOrderFormSku sku = skus.get(i);
				appendRow("商品" + sku.getProductName(), 2);
				openTr().openTd(2).openTable();
				appendRowInput("skuId", "list[0].list[" + i + "].sku_code", String.valueOf(sku.getSkuId()));
				appendRowInput("正品数量", "list[0].list[" + i + "].count", String.valueOf(sku.getTotalCount()));
				appendRowInput("次品数量", "list[0].list[" + i + "].junk_count", "0");
				closeTd().closeTr().closeTable();
			}
		}
		closeTable().closeTd().closeTr();
		// 包裹明细结束
		closeTable().closeForm();
	}

	@Override
	public String serviceName() {
		return EmsWarehouseAdapter.SRVICENAME_SALESORDER_UPDATE;
	}

}
