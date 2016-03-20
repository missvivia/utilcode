/**
 * 
 */
package com.xyl.mmall.oms.warehouse.adapter.baishi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import com.xyl.mmall.oms.dto.warehouse.WMSSkuDetailDTO;
import com.xyl.mmall.oms.warehouse.annotation.MapClass;
import com.xyl.mmall.oms.warehouse.annotation.MapField;

/**
 * @author hzzengchengyuan
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@MapClass(value = WMSSkuDetailDTO.class, isAllField = true)
public class BSSkuDetail implements Serializable {
	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 1L;

	/**
	 * 商品编码.是否可以为空：N
	 */
	@MapField("skuId")
	private String itemSkuCode;

	/**
	 * 商品名称.是否可以为空：Y
	 */
	@MapField("name")
	private String itemName;

	/**
	 * 商品数量.是否可以为空：N
	 */
	@MapField("count")
	private int itemQuantity;

	/**
	 * 商品单价（两位小数）.是否可以为空：Y
	 */
	@MapField("price")
	private double itemUnitPrice;

	/**
	 * 备注.是否可以为空：Y
	 */
	@MapField("note")
	private String itemNote;

	/**
	 * 行号.是否可以为空：Y
	 */
	private long lineNo;

	/**
	 * 对应商品状态：默认不填表示良品，Y-良品，其它值表示不良品.是否可以为空：Y
	 */
	private String fixStatusCode;

	/**
	 * 生产日期.是否可以为空：Y
	 */
	private String productionDate;

	/**
	 * 失效日期.是否可以为空：Y
	 */
	private String expiryDate;

	/**
	 * 活动批次代码1.是否可以为空：Y
	 */
	private String lotAtt01;

	/**
	 * 活动批次代码2.是否可以为空：Y
	 */
	private String lotAtt02;

	/**
	 * 活动批次代码3.是否可以为空：Y
	 */
	private String lotAtt03;

	/**
	 * 活动批次代码4.是否可以为空：Y
	 */
	private String lotAtt04;

	// 百世物流的活动批次代码有12个，这里就暂时忽略其他的了

	/**
	 * 供应商代码.是否可以为空：Y
	 */
	private String providerCode;

	/**
	 * 供应商名称.是否可以为空：Y
	 */
	private String providerName;

	/**
	 * 商品包装代码.是否可以为空：Y
	 */
	private String packCode;

	/**
	 * 商品包装单位.是否可以为空：Y
	 */
	private String uomCode;

	/**
	 * 无库存标记. 是否可以为空：Y
	 */
	private String noStackTag;

	/**
	 * @return the itemSkuCode
	 */
	public String getItemSkuCode() {
		return itemSkuCode;
	}

	/**
	 * @param itemSkuCode
	 *            the itemSkuCode to set
	 */
	public void setItemSkuCode(String itemSkuCode) {
		this.itemSkuCode = itemSkuCode;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName
	 *            the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemQuantity
	 */
	public int getItemQuantity() {
		return itemQuantity;
	}

	/**
	 * @param itemQuantity
	 *            the itemQuantity to set
	 */
	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	/**
	 * @return the itemUnitPrice
	 */
	public double getItemUnitPrice() {
		return itemUnitPrice;
	}

	/**
	 * @param itemUnitPrice
	 *            the itemUnitPrice to set
	 */
	public void setItemUnitPrice(double itemUnitPrice) {
		this.itemUnitPrice = itemUnitPrice;
	}

	/**
	 * @return the itemNote
	 */
	public String getItemNote() {
		return itemNote;
	}

	/**
	 * @param itemNote
	 *            the itemNote to set
	 */
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}

	/**
	 * @return the lineNo
	 */
	public long getLineNo() {
		return lineNo;
	}

	/**
	 * @param lineNo
	 *            the lineNo to set
	 */
	public void setLineNo(long lineNo) {
		this.lineNo = lineNo;
	}

	/**
	 * @return the fixStatusCode
	 */
	public String getFixStatusCode() {
		return fixStatusCode;
	}

	/**
	 * @param fixStatusCode
	 *            the fixStatusCode to set
	 */
	public void setFixStatusCode(String fixStatusCode) {
		this.fixStatusCode = fixStatusCode;
	}

	/**
	 * @return the productionDate
	 */
	public String getProductionDate() {
		return productionDate;
	}

	/**
	 * @param productionDate
	 *            the productionDate to set
	 */
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate
	 *            the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the lotAtt01
	 */
	public String getLotAtt01() {
		return lotAtt01;
	}

	/**
	 * @param lotAtt01
	 *            the lotAtt01 to set
	 */
	public void setLotAtt01(String lotAtt01) {
		this.lotAtt01 = lotAtt01;
	}

	/**
	 * @return the lotAtt02
	 */
	public String getLotAtt02() {
		return lotAtt02;
	}

	/**
	 * @param lotAtt02
	 *            the lotAtt02 to set
	 */
	public void setLotAtt02(String lotAtt02) {
		this.lotAtt02 = lotAtt02;
	}

	/**
	 * @return the lotAtt03
	 */
	public String getLotAtt03() {
		return lotAtt03;
	}

	/**
	 * @param lotAtt03
	 *            the lotAtt03 to set
	 */
	public void setLotAtt03(String lotAtt03) {
		this.lotAtt03 = lotAtt03;
	}

	/**
	 * @return the lotAtt04
	 */
	public String getLotAtt04() {
		return lotAtt04;
	}

	/**
	 * @param lotAtt04
	 *            the lotAtt04 to set
	 */
	public void setLotAtt04(String lotAtt04) {
		this.lotAtt04 = lotAtt04;
	}

	/**
	 * @return the providerCode
	 */
	public String getProviderCode() {
		return providerCode;
	}

	/**
	 * @param providerCode
	 *            the providerCode to set
	 */
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	/**
	 * @return the providerName
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * @param providerName
	 *            the providerName to set
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * @return the packCode
	 */
	public String getPackCode() {
		return packCode;
	}

	/**
	 * @param packCode
	 *            the packCode to set
	 */
	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	/**
	 * @return the uomCode
	 */
	public String getUomCode() {
		return uomCode;
	}

	/**
	 * @param uomCode
	 *            the uomCode to set
	 */
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	/**
	 * @return the noStackTag
	 */
	public String getNoStackTag() {
		return noStackTag;
	}

	/**
	 * @param noStackTag
	 *            the noStackTag to set
	 */
	public void setNoStackTag(String noStackTag) {
		this.noStackTag = noStackTag;
	}

}
