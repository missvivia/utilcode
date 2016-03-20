package com.xyl.mmall.oms.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.SupplyType;

/**
 * @author zb<br>
 *         一个po下的sku信息
 */
@AnnonOfClass(desc = "po单明细", tableName = "Mmall_Oms_PoOrderFormSku")
public class PoOrderFormSku implements Serializable{

	private static final long serialVersionUID = 1L;

	@AnnonOfField(desc = "posku", primary = true)
	private long poSkuId;

	@AnnonOfField(desc = "poid", policy = true)
	private long poId;

	@AnnonOfField(desc = "供货方式")
	private SupplyType supplyType;

	@AnnonOfField(desc = "代理商自己的id")
	private long selfSupplierId;

	@AnnonOfField(desc = "支援的品牌商id")
	private long backupSupplierId;

	@AnnonOfField(desc = "原始的商家库存数量")
	private long oriSelfStock;

	@AnnonOfField(desc = "原始的品牌商支援库存数量")
	private long oriBackupStock;

	@AnnonOfField(desc = "当前商家库存数量")
	private long curSelfStock;

	@AnnonOfField(desc = "当前品牌商支援库存数量")
	private long curBackupStock;

	@AnnonOfField(desc = "代理商入库仓库id")
	private long selfStoreAreaId;

	@AnnonOfField(desc = "品牌商入库仓库id")
	private long backupStoreAreaId;
	
	public long getPoSkuId() {
		return poSkuId;
	}

	public void setPoSkuId(long poSkuId) {
		this.poSkuId = poSkuId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public SupplyType getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(SupplyType supplyType) {
		this.supplyType = supplyType;
	}

	public long getBackupSupplierId() {
		return backupSupplierId;
	}

	public void setBackupSupplierId(long backupSupplierId) {
		this.backupSupplierId = backupSupplierId;
	}

	public long getOriSelfStock() {
		return oriSelfStock;
	}

	public void setOriSelfStock(long oriSelfStock) {
		this.oriSelfStock = oriSelfStock;
	}

	public long getOriBackupStock() {
		return oriBackupStock;
	}

	public void setOriBackupStock(long oriBackupStock) {
		this.oriBackupStock = oriBackupStock;
	}

	public long getCurSelfStock() {
		return curSelfStock;
	}

	public void setCurSelfStock(long curSelfStock) {
		this.curSelfStock = curSelfStock;
	}

	public long getCurBackupStock() {
		return curBackupStock;
	}

	public void setCurBackupStock(long curBackupStock) {
		this.curBackupStock = curBackupStock;
	}

	public long getSelfStoreAreaId() {
		return selfStoreAreaId;
	}

	public void setSelfStoreAreaId(long selfStoreAreaId) {
		this.selfStoreAreaId = selfStoreAreaId;
	}

	public long getBackupStoreAreaId() {
		return backupStoreAreaId;
	}

	public void setBackupStoreAreaId(long backupStoreAreaId) {
		this.backupStoreAreaId = backupStoreAreaId;
	}

	public long getSelfSupplierId() {
		return selfSupplierId;
	}

	public void setSelfSupplierId(long selfSupplierId) {
		this.selfSupplierId = selfSupplierId;
	}

}
