/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.util.Map;

import com.xyl.mmall.framework.vo.BaseVersionVO;

/**
 * ProdSpeciBackendVO.java created by yydx811 at 2015年5月14日 下午8:51:28
 * 商品规格vo
 *
 * @author yydx811
 */
public class ProdSpeciBackendVO extends BaseVersionVO {

	/** 序列化id. */
	private static final long serialVersionUID = -1869351657851630524L;

	/** 商品id. */
	private long skuId;

	/** 规格选项名. */
	private String speciOptionName;
	
	/** 规格-选项id对应map. */
	private Map<Long, Long> speciIdMap;
	
	/** 库存数量. */
	private int productNum;

	/** 不足提醒. */
	private int attentionNum = -1;
	
	/** 商品内码. */
	private String prodInnerCode;
	
	/** 规格名称. */
	private String specificationName;
	
	/** 规格显示类型. */
	private int type;

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getSpeciOptionName() {
		return speciOptionName;
	}

	public void setSpeciOptionName(String speciOptionName) {
		this.speciOptionName = speciOptionName;
	}

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public int getAttentionNum() {
		return attentionNum;
	}

	public void setAttentionNum(int attentionNum) {
		this.attentionNum = attentionNum;
	}

	public Map<Long, Long> getSpeciIdMap() {
		return speciIdMap;
	}

	public void setSpeciIdMap(Map<Long, Long> speciIdMap) {
		this.speciIdMap = speciIdMap;
	}

	public String getProdInnerCode() {
		return prodInnerCode;
	}

	public void setProdInnerCode(String prodInnerCode) {
		this.prodInnerCode = prodInnerCode;
	}

	public String getSpecificationName() {
		return specificationName;
	}

	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
