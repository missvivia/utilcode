package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.enums.ShipStateType;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.WarehouseForm;

/**
 * 拣货单DTO
 * 
 * @author hzzengdan
 * @date 2014-09-04
 */
public class PickOrderDTO extends PickOrderForm {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = -5566867510053036110L;

	/**
	 * 档期开始结束时间，查询时使用
	 */
	private long comStartEndTime;

	/**
	 * 档期结束结束时间，查询用
	 */
	private long comEndEndTime;

	/**
	 * 创建结束时间
	 */
	private long createEndTime;

	/**
	 * 导出状态
	 */
	private int exportState;

	/**
	 * 关联发货单
	 */
	private String shipOrderId;

	/**
	 * 关联运单号
	 */
	private String expressNo;

	/**
	 * 发货时间
	 */
	private long shipTime;

	/**
	 * 发货状态
	 */
	private ShipStateType shipState;

	/**
	 * 仓库信息
	 */
	private WarehouseForm warehouseForm;

	public WarehouseForm getWarehouseForm() {
		return warehouseForm;
	}

	public void setWarehouseForm(WarehouseForm warehouseForm) {
		this.warehouseForm = warehouseForm;
	}

	/**
	 * 构造函数
	 */
	public PickOrderDTO(PickOrderForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public long getComStartEndTime() {
		return comStartEndTime;
	}

	public void setComStartEndTime(long comStartEndTime) {
		this.comStartEndTime = comStartEndTime;
	}

	public long getComEndEndTime() {
		return comEndEndTime;
	}

	public void setComEndEndTime(long comEndEndTime) {
		this.comEndEndTime = comEndEndTime;
	}

	public long getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(long createEndTime) {
		this.createEndTime = createEndTime;
	}

	public int getExportState() {
		return exportState;
	}

	public void setExportState(int exportState) {
		this.exportState = exportState;
	}

	public String getShipOrderId() {
		return shipOrderId;
	}

	public void setShipOrderId(String shipOrderId) {
		this.shipOrderId = shipOrderId;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public long getShipTime() {
		return shipTime;
	}

	public void setShipTime(long shipTime) {
		this.shipTime = shipTime;
	}

	public ShipStateType getShipState() {
		return shipState;
	}

	public void setShipState(ShipStateType shipState) {
		this.shipState = shipState;
	}

}
