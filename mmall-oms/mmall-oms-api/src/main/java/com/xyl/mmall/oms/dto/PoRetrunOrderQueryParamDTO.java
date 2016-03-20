/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.oms.enums.PoReturnOrderState;

/**
 * 退供单相关查询范围的参数封装对象，多参数条件同时存在时取交集
 * 
 * @author hzzengchengyuan
 *
 */
public class PoRetrunOrderQueryParamDTO extends PageableQueryDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商(商家)ID组
	 */
	private List<Long> supplierIds;

	/**
	 * 退供单id
	 */
	private List<Long> poReturnOrderIds;

	/**
	 * po档期开始时间的查询时间范围，po.starttime >= poStartTimeRang[0] && po.starttime <
	 * poStartTimeRang[1]
	 */
	private long[] poStartTimeRang;

	/**
	 * 状态
	 */
	private List<PoReturnOrderState> states;
	
	/**
	 * 所在仓库
	 */
	private List<Long> warehouseIds;
	
	private long poOrderId;

	/**
	 * 添加一个商家id参数，如果值<=0 则忽略该参数值
	 * 
	 * @param supplierId
	 */
	public void addSupplierId(long supplierId) {
		if (supplierIds == null) {
			this.supplierIds = new ArrayList<Long>();
		}
		if (supplierId > 0 && !this.supplierIds.contains(supplierId)) {
			this.supplierIds.add(supplierId);
		}
	}

	/**
	 * 添加多个商家id值，如果值<=0 则忽略该参数值
	 * 
	 * @param supplierIds
	 */
	public void addSupplierIds(Long[] supplierIds) {
		if (supplierIds != null) {
			for (Long id : supplierIds) {
				addSupplierId(id);
			}
		}
	}

	public List<Long> getSupplierIds() {
		return supplierIds;
	}

	public Long[] getSupplierIdArray() {
		return supplierIds == null ? null : supplierIds.toArray(new Long[0]);
	}

	public void setPoStartTimestar(long time) {
		if (poStartTimeRang == null) {
			poStartTimeRang = new long[2];
		}
		if (time > 0) {
			poStartTimeRang[0] = time;
		}
	}

	public void setPoStartTimeend(long time) {
		if (poStartTimeRang == null) {
			poStartTimeRang = new long[2];
		}
		if (time > 0) {
			poStartTimeRang[1] = time;
		}
	}

	/**
	 * @return the poStartTimeRang
	 */
	public long[] getPoStartTimeRang() {
		return poStartTimeRang;
	}

	/**
	 * @param poStartTimeRang
	 *            the poStartTimeRang to set
	 */
	public void setPoStartTimeRang(long[] poStartTimeRang) {
		this.poStartTimeRang = poStartTimeRang;
	}

	/**
	 * 添加一个查询类型，值为null或未{@link PoReturnOrderState.NULL}则忽略
	 * 
	 * @param state
	 */
	public void addPoReturnOrderState(PoReturnOrderState state) {
		if (this.states == null) {
			this.states = new ArrayList<PoReturnOrderState>();
		}
		if (state != null && state != PoReturnOrderState.NULL && !this.states.contains(state)) {
			this.states.add(state);
		}
	}

	public void addPoReturnOrderState(String state) {
		addPoReturnOrderState(PoReturnOrderState.genEnumByCodeIgnoreCase(state));
	}

	/**
	 * @return the types
	 */
	public List<PoReturnOrderState> getStates() {
		return states;
	}

	public PoReturnOrderState[] getStateArray() {
		return states == null ? null : states.toArray(new PoReturnOrderState[0]);
	}

	/**
	 * @return the returnPoOrderIds
	 */
	public Long[] getPoReturnOrderIdArray() {
		return poReturnOrderIds == null ? null : poReturnOrderIds.toArray(new Long[0]);
	}

	public List<Long> getPoReturnOrderIds() {
		return poReturnOrderIds;
	}

	/**
	 * @param returnPoOrderIds
	 *            the returnPoOrderIds to set
	 */
	public void setPoReturnOrderIds(List<Long> returnPoOrderIds) {
		this.poReturnOrderIds = returnPoOrderIds;
	}

	public void addPoReturnOrderId(long returnOrderId) {
		if (returnOrderId == 0) {
			return;
		}
		if (this.poReturnOrderIds == null) {
			this.poReturnOrderIds = new ArrayList<Long>();
		}
		if (!this.poReturnOrderIds.contains(returnOrderId)) {
			this.poReturnOrderIds.add(returnOrderId);
		}
	}
	
	/**
	 * @return the returnPoOrderIds
	 */
	public Long[] getWarehouseIdArray() {
		return warehouseIds == null ? null : warehouseIds.toArray(new Long[0]);
	}

	public List<Long> getWarehouseIds() {
		return warehouseIds;
	}

	/**
	 * @param returnPoOrderIds
	 *            the returnPoOrderIds to set
	 */
	public void setWarehouseIds(List<Long> warehouseIds) {
		this.warehouseIds = warehouseIds;
	}

	public void addWarehouseId(long warehouseId) {
		if (warehouseId <= 0) {
			return;
		}
		if (this.warehouseIds == null) {
			this.warehouseIds = new ArrayList<Long>();
		}
		if (!this.warehouseIds.contains(warehouseId)) {
			this.warehouseIds.add(warehouseId);
		}
	}

	public long getPoOrderId() {
		return poOrderId;
	}

	public void setPoOrderId(long poOrderId) {
		this.poOrderId = poOrderId;
	}

}
