/**
 * 拣货META
 */
package com.xyl.mmall.oms.meta;

import java.io.Serializable;
import java.util.List;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.JITFlagType;
import com.xyl.mmall.oms.enums.PickStateType;

/**
 * 拣货单
 * 
 * @author hzzengdan
 * @date 2014-09-09
 */
@AnnonOfClass(desc = "拣货单", tableName = "Mmall_Oms_PickOrder")
public class PickOrderForm implements Serializable {
	
	/** 序列ID. */
	private static final long serialVersionUID = -1493187938718481910L;

	@AnnonOfField(desc = "拣货单编号", primary = true, type = "varchar(128)")
	private String pickOrderId;

	@AnnonOfField(desc = "产品的供应商Id", policy = true)
	private long supplierId;

	@AnnonOfField(desc = "拣货状态")
	private PickStateType pickState;

	@AnnonOfField(desc = "jit标志(0:JIT模式，1:非JIT模式)")
	private JITFlagType JITFlag;

	@AnnonOfField(desc = "导出次数")
	private int exportTimes;

	@AnnonOfField(desc = "首次导出时间")
	private long firstExportTime;

	@AnnonOfField(desc = "创建时间")
	private long createTime;

	@AnnonOfField(desc = "修改时间")
	private long modifyTime;

	@AnnonOfField(desc = "拣货总数")
	private int pickTotalQuantity;
	
	@AnnonOfField(desc = "仓库id")
	private long storeAreaId;

	private List<PickSkuItemForm> pickSkuList;

	public long getFirstExportTime() {
		return firstExportTime;
	}

	public void setFirstExportTime(long firstExportTime) {
		this.firstExportTime = firstExportTime;
	}

	public int getExportTimes() {
		return exportTimes;
	}

	public void setExportTimes(int exportTimes) {
		this.exportTimes = exportTimes;
	}

	public String getPickOrderId() {
		return pickOrderId;
	}

	public void setPickOrderId(String pickOrderId) {
		this.pickOrderId = pickOrderId;
	}

	public PickStateType getPickState() {
		return pickState;
	}

	public void setPickState(PickStateType pickState) {
		this.pickState = pickState;
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

	public int getPickTotalQuantity() {
		return pickTotalQuantity;
	}

	public void setPickTotalQuantity(int pickTotalQuantity) {
		this.pickTotalQuantity = pickTotalQuantity;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public List<PickSkuItemForm> getPickSkuList() {
		return pickSkuList;
	}

	public void setPickSkuList(List<PickSkuItemForm> pickSkuList) {
		this.pickSkuList = pickSkuList;
	}

	public long getStoreAreaId() {
		return storeAreaId;
	}

	public void setStoreAreaId(long storeAreaId) {
		this.storeAreaId = storeAreaId;
	}

}
