package com.xyl.mmall.oms.meta;

import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.meta.annotation.AnnonOfField;
import com.xyl.mmall.oms.enums.OmsSkuState;

/**
 * @author zb
 *
 */
@AnnonOfClass(desc = "oms sku记录表", tableName = "Mmall_Oms_OmsSku")
public class OmsSku {

	@AnnonOfField(desc = "skuId", primary = true, policy = true)
	private long skuId;

	@AnnonOfField(desc = "产品的供应商Id")
	private long supplierId;

	@AnnonOfField(desc = "产品Id")
	private long productId;

	@AnnonOfField(desc = "poId")
	private long poId;

	/** 所属的商品名 */
	@AnnonOfField(desc = "所属的商品名", type = "varchar(128)")
	private String productName = "";

	/** 所属的商品颜色 */
	@AnnonOfField(desc = "所属的商品颜色", type = "varchar(128)")
	private String colorName = "";

	/** 尺码 */
	@AnnonOfField(desc = "尺码", type = "varchar(128)")
	private String size = "";

	/** 条形码 */
	@AnnonOfField(desc = "条形码")
	private String barCode = "";

	/**
	 * 状态
	 */
	@AnnonOfField(desc = "状态")
	private OmsSkuState state;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getPoId() {
		return poId;
	}

	public void setPoId(long poId) {
		this.poId = poId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public OmsSkuState getState() {
		return state;
	}

	public void setState(OmsSkuState state) {
		this.state = state;
	}

}
