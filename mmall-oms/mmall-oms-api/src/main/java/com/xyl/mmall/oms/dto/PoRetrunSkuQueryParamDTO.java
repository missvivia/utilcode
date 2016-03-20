/**
 * 
 */
package com.xyl.mmall.oms.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xyl.mmall.oms.enums.PoReturnOrderState;
import com.xyl.mmall.oms.enums.ReturnType;

/**
 * 退货单相关查询范围的参数封装对象，多参数条件同时存在时取交集
 * 
 * @author hzzengchengyuan
 *
 */
public class PoRetrunSkuQueryParamDTO extends PageableQueryDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商(商家)ID组
	 */
	private List<Long> supplierIds;

	/**
	 * poID组
	 */
	private List<Long> poOrderIds;

	/**
	 * 退货单类型
	 */
	private List<ReturnType> types;
	
	private List<PoReturnOrderState> states;

	/**
	 * po档期开始时间的查询时间范围，po.starttime >= poStartTimeRang[0] && po.starttime <
	 * poStartTimeRang[1]
	 */
	private long[] poStartTimeRang;
	
	/**
	 * 所在仓库
	 */
	private List<Long> warehouseIds;

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

	/**
	 * 添加一个档期id参数，如果值<=0 则忽略该参数值
	 * 
	 * @param supplierId
	 */
	public void addPoOrderId(long poOrderId) {
		if (poOrderIds == null) {
			this.poOrderIds = new ArrayList<Long>();
		}
		if (poOrderId > 0 && !this.poOrderIds.contains(poOrderId)) {
			this.poOrderIds.add(poOrderId);
		}
	}

	/**
	 * 添加多个档期id值，如果值<=0 则忽略该参数值
	 * 
	 * @param poOrderIds
	 */
	public void addPoOrderIds(Long[] poOrderIds) {
		if (poOrderIds != null) {
			for (Long id : poOrderIds) {
				addPoOrderId(id);
			}
		}
	}
	
	public void setPoOrderIds(Long[] poOrderIds) {
		if (poOrderIds == null) {
			this.poOrderIds = null;
		} else {
			this.poOrderIds = Arrays.asList(poOrderIds);
		}
	}

	/**
	 * @return the poOrderIds
	 */
	public List<Long> getPoOrderIds() {
		return poOrderIds;
	}

	public Long[] getPoOrderIdArray() {
		return poOrderIds == null ? null : poOrderIds.toArray(new Long[0]);
	}

	/**
	 * 添加一个查询类型，值为null或未{@link ReturnType.NULL}则忽略
	 * 
	 * @param type
	 */
	public void addReturnType(ReturnType type) {
		if (types == null) {
			this.types = new ArrayList<ReturnType>();
		}
		if (type != null && type != ReturnType.NULL && !this.types.contains(type)) {
			this.types.add(type);
		}
	}

	public void addReturnType(String type) {
		addReturnType(ReturnType.genEnumByCodeIgnoreCase(type));
	}

	/**
	 * @return the types
	 */
	public List<ReturnType> getTypes() {
		return types;
	}

	public ReturnType[] getTypeArray() {
		return types == null ? null : types.toArray(new ReturnType[0]);
	}
	
	public void addState(PoReturnOrderState state) {
		if (this.states == null) {
			this.states = new ArrayList<PoReturnOrderState>();
		}
		if (state != null && state != PoReturnOrderState.NULL && !this.states.contains(state)) {
			this.states.add(state);
		}
	}

	public void addState(String state) {
		addState(PoReturnOrderState.genEnumByCodeIgnoreCase(state));
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

}
