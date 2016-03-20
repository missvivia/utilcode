/**
 * 
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * @author hzzengdan
 *
 */
public class PoReportVo implements Serializable{

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 8458317666548248632L;
	/**po单编号*/
	private String poOrderId;
	/**上线日期*/
	private String startTime;
	/**下线日期*/
	private String endTime;
	/**有效订单数*/
	private int effectiveOrdersNum;
	/**取消的订单数*/
	private int cancelOrdersNum;
	/**未拣货数量*/
	private int unPickedNum;
	/**拣货中数量*/
	private int pickingNum;
	/**最后拣货时间*/
	private String lastPickTime;
	/**累计拣货数量*/
	private int grandPickTotal;
	/**累计拣货数量（已发）*/
	private int grandPickShipedTotal;
	/**发货中数量*/
	private int shippingNum;
	/**累计发货数量*/
	private int grandShipTotal;
	/**累计到货数量*/
	private int grandArrivalTotal;
	/**一退数量*/
	private int oneReturnNum;
	/**二退数量*/
	private int twoReturnNum;
	/**三退数量*/
	private int threeReturnNum;
	public String getPoOrderId() {
		return poOrderId;
	}
	public void setPoOrderId(String poOrderId) {
		this.poOrderId = poOrderId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getEffectiveOrdersNum() {
		return effectiveOrdersNum;
	}
	public void setEffectiveOrdersNum(int effectiveOrdersNum) {
		this.effectiveOrdersNum = effectiveOrdersNum;
	}
	public int getCancelOrdersNum() {
		return cancelOrdersNum;
	}
	public void setCancelOrdersNum(int cancelOrdersNum) {
		this.cancelOrdersNum = cancelOrdersNum;
	}
	public int getUnPickedNum() {
		return unPickedNum;
	}
	public void setUnPickedNum(int unPickedNum) {
		this.unPickedNum = unPickedNum;
	}
	public int getPickingNum() {
		return pickingNum;
	}
	public void setPickingNum(int pickingNum) {
		this.pickingNum = pickingNum;
	}
	public String getLastPickTime() {
		return lastPickTime;
	}
	public void setLastPickTime(String lastPickTime) {
		this.lastPickTime = lastPickTime;
	}
	public int getGrandPickTotal() {
		return grandPickTotal;
	}
	public void setGrandPickTotal(int grandPickTotal) {
		this.grandPickTotal = grandPickTotal;
	}
	public int getGrandPickShipedTotal() {
		return grandPickShipedTotal;
	}
	public void setGrandPickShipedTotal(int grandPickShipedTotal) {
		this.grandPickShipedTotal = grandPickShipedTotal;
	}
	public int getShippingNum() {
		return shippingNum;
	}
	public void setShippingNum(int shippingNum) {
		this.shippingNum = shippingNum;
	}
	public int getGrandShipTotal() {
		return grandShipTotal;
	}
	public void setGrandShipTotal(int grandShipTotal) {
		this.grandShipTotal = grandShipTotal;
	}
	public int getGrandArrivalTotal() {
		return grandArrivalTotal;
	}
	public void setGrandArrivalTotal(int grandArrivalTotal) {
		this.grandArrivalTotal = grandArrivalTotal;
	}
	public int getOneReturnNum() {
		return oneReturnNum;
	}
	public void setOneReturnNum(int oneReturnNum) {
		this.oneReturnNum = oneReturnNum;
	}
	public int getTwoReturnNum() {
		return twoReturnNum;
	}
	public void setTwoReturnNum(int twoReturnNum) {
		this.twoReturnNum = twoReturnNum;
	}
	public int getThreeReturnNum() {
		return threeReturnNum;
	}
	public void setThreeReturnNum(int threeReturnNum) {
		this.threeReturnNum = threeReturnNum;
	}
}
