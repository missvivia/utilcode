/**
 * 
 */
package com.xyl.mmall.oms.dto.warehouse;

import java.io.Serializable;

import com.xyl.mmall.oms.enums.WarehouseType;

/**
 * sku商品详情对象，oms在跟仓储对接时带有商品的接口都使用该对象，对于各个调用接口可忽略冗余属性
 * @author hzzengchengyuan
 */
public class WMSSkuDetailDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long skuId;

	/**
	 * 仓库类别
	 */
	private WarehouseType wmsType = WarehouseType.EMS;
	
	private String warehouseCode;
	
	/**
	 * 商品货号
	 */
	private String artNo;

	/**
	 * 商品名稱
	 */
	private String name;

	/**
	 * 尺寸
	 */
	private String size;

	/**
	 * 颜色
	 */
	private String color;

	/**
	 * 长（毫米，箱包装尺寸）
	 */
	private long length;

	/**
	 * 宽（毫米，箱包装尺寸）
	 */
	private long width;

	/**
	 * 高（毫米，箱包装尺寸）
	 */
	private long height;

	/**
	 * 重量（克，箱包装重量）
	 */
	private long weight;

	/**
	 * 商品单位
	 */
	private String unit = "件";
	
	/**
	 * 商品单价
	 */
	private double price;

	/**
	 * 商品总数量
	 */
	private int count = -1;

	/**
	 * 不良品数量
	 */
	private int defectiveCount;

	/**
	 * 良品数量
	 */
	private int normalCount;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * @return the skuId
	 */
	public long getSkuId() {
		return skuId;
	}

	/**
	 * @param skuId
	 *            the skuId to set
	 */
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	/**
	 * @return the wmsType
	 */
	public WarehouseType getWmsType() {
		return wmsType;
	}

	/**
	 * @param wmsType the wmsType to set
	 */
	public void setWmsType(WarehouseType wmsType) {
		this.wmsType = wmsType;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the artNo
	 */
	public String getArtNo() {
		return artNo;
	}

	/**
	 * @param artNo
	 *            the artNo to set
	 */
	public void setArtNo(String artNo) {
		this.artNo = artNo;
	}

	/**
	 * @return the skuName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param skuName
	 *            the skuName to set
	 */
	public void setName(String skuName) {
		this.name = skuName;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}

	/**
	 * @return the width
	 */
	public long getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(long width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public long getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(long height) {
		this.height = height;
	}

	/**
	 * @return the weight
	 */
	public long getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(long weight) {
		this.weight = weight;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		if(this.count == -1) {
			calculateCount();
		}
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the defectiveCount
	 */
	public int getDefectiveCount() {
		return defectiveCount;
	}

	/**
	 * @param defectiveCount
	 *            the defectiveCount to set
	 */
	public void setDefectiveCount(int defectiveCount) {
		this.defectiveCount = defectiveCount;
	}

	/**
	 * @return the normalCount
	 */
	public int getNormalCount() {
		return normalCount;
	}

	/**
	 * @param normalCount
	 *            the normalCount to set
	 */
	public void setNormalCount(int normalCount) {
		this.normalCount = normalCount;
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

	public boolean hasDefective() {
		return getDefectiveCount() > 0;
	}
	
	/**
	 * 计算商品数量：if(总数<=0) 总数 = 良品数量 + 不良品数量
	 */
	public void calculateCount() {
		if(this.count <= 0){
			this.count = getNormalCount() + getDefectiveCount();
		}
	}
	
	// 定义sku属性名常量列表，不全大写主要便于引用时查看
	public static final String field_skuId = "skuId";
	public static final String field_artNo = "artNo";
	public static final String field_name = "name";
	public static final String field_size = "size";
	public static final String field_color = "color";
	public static final String field_length = "length";
	public static final String field_width = "width";
	public static final String field_height = "height";
	public static final String field_weight = "weight";
	public static final String field_unit = "unit";
	public static final String field_price = "price";
	public static final String field_count = "count";
	public static final String field_defectiveCount = "defectiveCount";
	public static final String field_normalCount = "normalCount";
	public static final String field_note = "note";
	public static final String field_warehouseCode = "warehouseCode";

}
