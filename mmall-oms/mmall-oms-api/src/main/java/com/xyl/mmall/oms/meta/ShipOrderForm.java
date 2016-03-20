/**
 * 发货META
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.enums.ShipStateType;

/**
 * @author hzzengdan
 * 
 */
@AnnonOfClass(desc = "发货单", tableName = "Mmall_Oms_ShipOrder")
public class ShipOrderForm implements Serializable {
	
	/** 序列ID. */
	private static final long serialVersionUID = -2468744225204131492L;

	@AnnonOfField(desc = "发货单编号", primary = true)
	private String shipOrderId;

	@AnnonOfField(desc = "拣货单id")
	private String pickOrderId;

	@AnnonOfField(desc = "商家ID", policy = true)
	private long supplierId;

	@AnnonOfField(desc = "发货时间")
	private long shipTime;

	@AnnonOfField(desc = "预计到货时间")
	private long expectArrivalTime;

	@AnnonOfField(desc = "快递公司", type = "VARCHAR(64)")
	private String courierCompany = "";

	@AnnonOfField(desc = "快递单号", type = "VARCHAR(64)", defa = "", hasDefault = true)
	private String expressNO = "";

	@AnnonOfField(desc = "装箱数")
	private int shipBoxQTY;

	@AnnonOfField(desc = "快递公司电话", defa = "", hasDefault = true, type = "VARCHAR(128)")
	private String courierComPhone = "";

	@AnnonOfField(desc = "司机姓名", defa = "", hasDefault = true, type = "VARCHAR(128)")
	private String driverName = "";

	@AnnonOfField(desc = "司机电话", defa = "", hasDefault = true, type = "VARCHAR(128)")
	private String driverPhone = "";

	@AnnonOfField(desc = "车牌号", defa = "", hasDefault = true, type = "VARCHAR(128)")
	private String licensePlate = "";

	@AnnonOfField(desc = "jit标志(0:JIT模式，1:非JIT模式)")
	private JITFlagType JITFlag;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long modifyTime;

	@AnnonOfField(desc = "发货状态")
	private ShipStateType shipState;

	@AnnonOfField(desc = "总数")
	private int total;

	@AnnonOfField(desc = "sku总类")
	private int skuCount;
	
	@AnnonOfField(desc = "仓库id")
	private long storeAreaId;
	
	@AnnonOfField(desc = "汇总时间")
	private long collectTime;
	
	@AnnonOfField(desc = "归属的拨次")
	private int boci;
	
	@AnnonOfField(desc = "拣货类型单品或多品")
	private PickMoldType pickMoldType;

	// ////////////////
	// 以下是仓库接收入库单的反馈数据
	// ////////////////
	@AnnonOfField(desc = "仓库到货时间")
	private long arrivalTime;

	@AnnonOfField(desc = "仓库实际收货数量")
	private int arrivedCount;
	
	public ShipStateType getShipState() {
		return shipState;
	}

	public void setShipState(ShipStateType shipState) {
		this.shipState = shipState;
	}

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public long getShipTime() {
		return shipTime;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public long getExpectArrivalTime() {
		return expectArrivalTime;
	}

	public void setExpectArrivalTime(long expectArrivalTime) {
		this.expectArrivalTime = expectArrivalTime;
	}

	public String getCourierCompany() {
		return courierCompany;
	}

	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}

	public String getExpressNO() {
		return expressNO;
	}

	public void setExpressNO(String expressNO) {
		this.expressNO = expressNO;
	}

	public int getShipBoxQTY() {
		return shipBoxQTY;
	}

	public void setShipBoxQTY(int shipBoxQTY) {
		this.shipBoxQTY = shipBoxQTY;
	}

	public String getCourierComPhone() {
		return courierComPhone;
	}

	public void setCourierComPhone(String courierComPhone) {
		this.courierComPhone = courierComPhone;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public JITFlagType getJITFlag() {
		return JITFlag;
	}

	public void setJITFlag(JITFlagType jITFlag) {
		JITFlag = jITFlag;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(int sku_count) {
		this.skuCount = sku_count;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public int getArrivedCount() {
		return arrivedCount;
	}

	public void setArrivedCount(int arrivedCount) {
		this.arrivedCount = arrivedCount;
	}

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}

	public int getBoci() {
		return boci;
	}

	public void setBoci(int boci) {
		this.boci = boci;
	}

	public PickMoldType getPickMoldType() {
		return pickMoldType;
	}

	public void setPickMoldType(PickMoldType pickMoldType) {
		this.pickMoldType = pickMoldType;
	}

}
