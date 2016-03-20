package com.xyl.mmall.oms.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.meta.PickOrderForm;
import com.xyl.mmall.oms.meta.ShipOrderForm;

/**
 * 发货单DTO
 * 
 * @author hzzengdan
 * @date 2014-09-04
 */
public class ShipOrderDTO extends ShipOrderForm {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 6077716490667799639L;

	/**
	 * 发货技术时间 查询时使用
	 */
	private long shipEndTime;

	/**
	 * 期望到达时间，查询用
	 */
	private long expectArrivalEndTime;

	/**
	 * 实际到货时间
	 */
	private long arrivalEndTime;

	/**
	 * 页面发货时间
	 */
	private String viewShipTime;

	/**
	 * 页面期待到达时间
	 */
	private String viewExpectArrivalTime;

	/**
	 * 总件数
	 */
	private int count;

	/**
	 * 入库单对应的商品种类数
	 */
	private int sku_count;

	private String brandName;

	private String account;

	private String warehouseName;

	private String time;

	private String state;

	private String arrivedTimeStr;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public ShipOrderDTO() {
	}

	/**
	 * 构造函数
	 */
	public ShipOrderDTO(ShipOrderForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public ShipOrderDTO(PickOrderForm pickOrder) {
		ReflectUtil.convertObj(this, pickOrder, false);
	}

	public long getShipEndTime() {
		return shipEndTime;
	}

	public void setShipEndTime(long shipEndTime) {
		this.shipEndTime = shipEndTime;
	}

	public long getExpectArrivalEndTime() {
		return expectArrivalEndTime;
	}

	public void setExpectArrivalEndTime(long expectArrivalEndTime) {
		this.expectArrivalEndTime = expectArrivalEndTime;
	}

	public long getArrivalEndTime() {
		return arrivalEndTime;
	}

	public void setArrivalEndTime(long arrivalEndTime) {
		this.arrivalEndTime = arrivalEndTime;
	}

	public String getViewShipTime() {
		return viewShipTime;
	}

	public void setViewShipTime(String viewShipTime) {
		this.viewShipTime = viewShipTime;
	}

	public String getViewExpectArrivalTime() {
		return viewExpectArrivalTime;
	}

	public void setViewExpectArrivalTime(String viewExpectArrivalTime) {
		this.viewExpectArrivalTime = viewExpectArrivalTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSkuCount() {
		return sku_count;
	}

	public void setSkuCount(int sku_count) {
		this.sku_count = sku_count;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.getCreateTime()));
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return this.getShipState().getDesc();
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getArrivedTimeStr() {
		if (this.getArrivalTime() != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(new Date(this.getArrivalTime()));
		}
		return arrivedTimeStr;
	}

	public void setArrivedTimeStr(String arrivedTimeStr) {
		this.arrivedTimeStr = arrivedTimeStr;
	}

}
