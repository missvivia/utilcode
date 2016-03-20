package com.xyl.mmall.oms.warehouse.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.OmsOrderFormDao;
import com.xyl.mmall.oms.dao.OmsOrderPackageDao;
import com.xyl.mmall.oms.meta.OmsOrderForm;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.util.OmsIdUtils;
import com.xyl.mmall.oms.warehouse.adapter.EmsWarehouseAdapter;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsDelivery;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsTraceOperateType;
import com.xyl.mmall.oms.warehouse.adapter.ems.EmsUndelivery;
import com.xyl.mmall.oms.warehouse.util.DateUtils;

@Service
public class EmsTracePage extends AbstractSimulatorPage {
	@Autowired
	private OmsOrderFormDao omsOrderFormDao;

	@Autowired
	private OmsOrderPackageDao orderPackageDao;

	public EmsTracePage() {
		super("模拟ems商家商品入库单确认");
	}

	@Override
	protected void initializeBody(Object data) {
		long orderId = Long.parseLong(OmsIdUtils.backEmsOrderId(data.toString()));
		OmsOrderForm omsOrder = omsOrderFormDao.getObjectById(orderId);
		List<OmsOrderPackage> packages = orderPackageDao.getListByOmsOrderFormId(omsOrder.getOmsOrderFormId(),
				omsOrder.getUserId());
		
		openForm(getServiceUrl());
		appendTd("订单轨迹", 2);
		openTable("属性", "值");
		appendRowInput("事务ID", "rows[0].transaction_id", UUID.randomUUID().toString());
		appendRowInput("订单号", "rows[0].order_id", data.toString());
		appendRowInput("运单号", "rows[0].mailno", packages == null || packages.size() == 0 ? "0" : packages
				.get(0).getMailNO());
		appendRowInput("操作时间", "rows[0].operate_time", DateUtils.format(new Date()));
		openTr().openTd()
		.append("操作类型")
		.closeTd()
		.openTd()
		.appendOption("rows[0].operate_type", "50", "安排投递", "10", "妥投", "40", "封发", "20", "未妥投",
				"00", "收寄", "41", "开拆").append("请自己写类型描述,和这里一致即可").closeTd().closeTr();
		appendRowInput("操作类型描述", "rows[0].operate_desc", "0");
		List<String> tuotou = new ArrayList<String>();
		tuotou.add("");
		tuotou.add("");
		for(EmsDelivery d : EmsDelivery.values()) {
			tuotou.add(d.getLable());
			tuotou.add(d.getLable());
		}
		openTr().openTd()
		.append("妥投信息")
		.closeTd()
		.openTd()
		.appendOption("rows[0].ext_attr1", tuotou.toArray(new String[]{})).append("当操作类型选择："+EmsTraceOperateType.TYPE_10.getDesc()+"时请选择未妥投信息").closeTd().closeTr();
		List<String> weituotou = new ArrayList<String>();
		weituotou.add("");
		weituotou.add("");
		for(EmsUndelivery d : EmsUndelivery.values()) {
			weituotou.add(d.getLable());
			weituotou.add(d.getLable());
		}
		openTr().openTd()
		.append("未妥投信息")
		.closeTd()
		.openTd()
		.appendOption("rows[0].ext_attr2", weituotou.toArray(new String[]{})).append("当操作类型选择："+EmsTraceOperateType.TYPE_20.getDesc()+"时请选择未妥投信息").closeTd().closeTr();
		appendRowInput("操作地点", "rows[0].operate_org", "0");
		appendRowInput("备注 ", "rows[0].remark", "0");
		closeTable();
		closeTr();
		closeForm();
	}

	@Override
	public String serviceName() {
		return EmsWarehouseAdapter.SRVICENAME_SALESORDER_TRACE;
	}

}
