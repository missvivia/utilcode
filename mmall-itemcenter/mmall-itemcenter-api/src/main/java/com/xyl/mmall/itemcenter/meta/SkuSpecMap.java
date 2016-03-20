package com.xyl.mmall.itemcenter.meta;

import java.io.Serializable;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;

/**
 * 记录一个sku下的某个sku规格的值
 * 
 * @author hzhuangluqian
 *
 */
@AnnonOfClass(tableName = "Mmall_ItemCenter_SkuSpecMap", desc = "商品规格映射表")
public class SkuSpecMap implements Serializable {

	private static final long serialVersionUID = -1156050215734696183L;

	@AnnonOfField(desc = "档期id", policy = true)
	private long poId;

	/** skuId，联合主键1 */
	@AnnonOfField(desc = "skuId", primary = true, primaryIndex = 1)
	private long skuId;

	/** 产品规格id，联合主键2 */
	@AnnonOfField(desc = "产品规格id", primary = true, primaryIndex = 2)
	private long skuSpecId;

	/** 商品id */
	@AnnonOfField(desc = "商品id")
	private long productId;

	/** 顺序 */
	@AnnonOfField(desc = "顺序")
	private int viewOrder;

	/** 描述 */
	@AnnonOfField(desc = "描述", notNull = false)
	private String opDesc;

	/** 图片值 */
	@AnnonOfField(desc = "图片值")
	private String value;

	/** 是否可以删除 */
	@AnnonOfField(desc = "是否可以删除")
	private boolean canDelete = true;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getSkuSpecId() {
		return skuSpecId;
	}

	public void setSkuSpecId(long skuSpecId) {
		this.skuSpecId = skuSpecId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getViewOrder() {
		return viewOrder;
	}

	public void setViewOrder(int viewOrder) {
		this.viewOrder = viewOrder;
	}

	public String getOpDesc() {
		return opDesc;
	}

	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

}
