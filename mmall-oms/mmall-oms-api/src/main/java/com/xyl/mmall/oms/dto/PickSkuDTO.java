/**
 * 
 */
package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.meta.PickSkuItemForm;

/**
 * @author hzzengdan
 * 
 */
public class PickSkuDTO extends PickSkuItemForm {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 2334995587018651402L;

	/**
	 * 对应po档期开始时间
	 */
	private long startTime;
	
	/**
	 * 对应po档期结束时间
	 */
	private long endTime;
	
	/**
	 * 品牌名称
	 */
	private String brandName;
	
	/**
	 * 单品多品为导出Excel所用
	 */
	private String mode;
	
	/**
	 * 导出时间
	 */
	private String exportTime;
	
	/**
	 * 仓库地址
	 */
	private String warehouse;
	/**
	 * 构造函数
	 */
	public PickSkuDTO(PickSkuItemForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getExportTime() {
		return exportTime;
	}

	public void setExportTime(String exportTime) {
		this.exportTime = exportTime;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	

}
