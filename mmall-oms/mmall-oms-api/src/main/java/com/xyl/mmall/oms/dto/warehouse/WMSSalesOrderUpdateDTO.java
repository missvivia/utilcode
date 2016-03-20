/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.WMSSalesOrderState;

/**
 * 销售订单更新，对应用户订单
 * 
 * @author hzzengchengyuan
 * 
 */
public class WMSSalesOrderUpdateDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 销售订单号
	 */
	private String orderId;

	/**
	 * 订单状态
	 */
	private WMSSalesOrderState state = WMSSalesOrderState.NULL;

	/**
	 * 订单状态更新的操作时间
	 */
	private long operaterTime;
	
	/**
	 * 物流公司代码
	 */
	private String logisticCode;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 订单包裹运单信息
	 */
	private List<WMSPackageDTO> packages;

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the state
	 */
	public WMSSalesOrderState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(WMSSalesOrderState state) {
		this.state = state;
	}

	/**
	 * @return the operaterTime
	 */
	public long getOperaterTime() {
		return operaterTime;
	}

	/**
	 * @param operaterTime the operaterTime to set
	 */
	public void setOperaterTime(long operaterTime) {
		this.operaterTime = operaterTime;
	}

	/**
	 * @return the logisticCode
	 */
	public String getLogisticCode() {
		return logisticCode;
	}

	/**
	 * @param logisticCode
	 *            the logisticCode to set
	 */
	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the packages
	 */
	public List<WMSPackageDTO> getPackages() {
		return packages;
	}

	/**
	 * @param packages
	 *            the packages to set
	 */
	public void setPackages(List<WMSPackageDTO> packages) {
		this.packages = packages;
	}
	
	public void addPackage(WMSPackageDTO pack) {
		if(this.packages == null) {
			this.packages = new ArrayList<WMSPackageDTO>();
		}
		this.packages.add(pack);
	}
	
	public void calculateCount() {
		if (this.getPackages() != null) {
			for (WMSPackageDTO wmsPackage : this.getPackages()) {
				wmsPackage.calculateCount();
			}
		}
	}

	// 定义销售订单属性名常量列表，不全大写主要便于引用时查看
	public static final String field_orderId = "orderId";
	public static final String field_note = "note";
	public static final String field_state = "state";
	public static final String field_operaterTime = "operaterTime";
	public static final String field_logisticCode = "logisticCode";
	public static final String field_packages = "packages";

}
