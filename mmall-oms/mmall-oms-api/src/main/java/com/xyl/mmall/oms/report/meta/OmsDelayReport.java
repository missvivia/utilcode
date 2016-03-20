package com.xyl.mmall.oms.report.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.netease.print.daojar.util.ReflectUtil;

/**
 * 
 * @author hzzhanghui
 * 
 */
@AnnonOfClass(desc = "仓库延迟订单统计报表", tableName = "Mmall_Oms_OmsDelayReport", policy = "warehouseId")
public class OmsDelayReport implements Serializable {

	private static final long serialVersionUID = 5327032858758780076L;

	@AnnonOfField(primary = true, desc = "主键", autoAllocateId = true)
	private long id;

	@AnnonOfField(desc = "仓库id")
	private long warehouseId;

	@AnnonOfField(desc = "报表生成时间")
	private long makeTime;

	@AnnonOfField(desc = "仓储服务商,如EMS，顺丰", notNull = false, defa = "", type = "VARCHAR(64)")
	private String warehouseOwner;

	@AnnonOfField(desc = "仓储名称", notNull = false, defa = "", type = "VARCHAR(128)")
	private String warehouseName;

	@AnnonOfField(desc = "订单号", notNull = false, defa = "0")
	private long omsOrderId;

	@AnnonOfField(desc = "订单产生时间", notNull = false, defa = "0")
	private long createTime;

	@AnnonOfField(desc = "订单状态名称", notNull = false, defa = "", type = "VARCHAR(128)")
	private String omsOrderFormState;

	@AnnonOfField(desc = "订单发货时间", notNull = false, defa = "0")
	private long shipTime;

	@AnnonOfField(desc = "收货人", notNull = false, defa = "", type = "VARCHAR(128)")
	private String consigneeName;

	@AnnonOfField(desc = "手机号", notNull = false, defa = "", type = "VARCHAR(128)")
	private String consigneeMobile;

	@AnnonOfField(desc = "快递单号", notNull = false, defa = "", type = "VARCHAR(256)")
	private String mailNO;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public long getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(long makeTime) {
		this.makeTime = makeTime;
	}

	public String getWarehouseOwner() {
		return warehouseOwner;
	}

	public void setWarehouseOwner(String warehouseOwner) {
		this.warehouseOwner = warehouseOwner;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public long getOmsOrderId() {
		return omsOrderId;
	}

	public void setOmsOrderId(long omsOrderId) {
		this.omsOrderId = omsOrderId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getOmsOrderFormState() {
		return omsOrderFormState;
	}

	public void setOmsOrderFormState(String omsOrderFormState) {
		this.omsOrderFormState = omsOrderFormState;
	}

	public long getShipTime() {
		return shipTime;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (makeTime ^ (makeTime >>> 32));
		result = prime * result + (int) (omsOrderId ^ (omsOrderId >>> 32));
		result = prime * result + (int) (warehouseId ^ (warehouseId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OmsDelayReport other = (OmsDelayReport) obj;
		if (makeTime != other.makeTime)
			return false;
		if (omsOrderId != other.omsOrderId)
			return false;
		if (warehouseId != other.warehouseId)
			return false;
		return true;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
