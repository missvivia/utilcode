package com.xyl.mmall.order.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.common.meta.BaseVersion;
import com.xyl.mmall.framework.enums.ExpressCompany;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年6月1日上午10:19:38
 */
@AnnonOfClass(desc = "订单物流", tableName = "Mmall_Order_Logistics", dbCreateTimeName = "CreateTime")
public class OrderLogistics extends BaseVersion {

	private static final long serialVersionUID = -7727145032048840942L;
	
	@AnnonOfField(desc = "Id", primary = true, autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "订单Id", policy = true)
	private long orderId;

	@AnnonOfField(desc = "快递号", type = "VARCHAR(32)", notNull = false)
	private String mailNO;

	@AnnonOfField(desc = "快递公司")
	private ExpressCompany expressCompany;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public ExpressCompany getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(ExpressCompany expressCompany) {
		this.expressCompany = expressCompany;
	}
}
