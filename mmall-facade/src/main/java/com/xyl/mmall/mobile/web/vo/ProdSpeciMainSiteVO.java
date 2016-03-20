/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.mobile.web.vo;

import com.xyl.mmall.framework.vo.BaseVersionVO;

/**
 * ProdSpeciMainSiteVO.java created by yydx811 at 2015年5月14日 下午8:51:28
 * 商品规格vo
 *
 * @author yydx811
 */
public class ProdSpeciMainSiteVO extends BaseVersionVO {

	/** 序列化id. */
	private static final long serialVersionUID = -1869351657851630524L;

	/** 规格名称. */
	private String specificationName;
	
	/** 规格显示类型. */
	private int type;

	/** 规格选项值id. */
	private long speciOptionId;

	/** 选项值. */
	private String speciOption;

	/** 规格id. */
	private long speciId;
	
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

	public long getSpeciOptionId() {
		return speciOptionId;
	}

	public void setSpeciOptionId(long speciOptionId) {
		this.speciOptionId = speciOptionId;
	}

	public String getSpeciOption() {
		return speciOption;
	}

	public void setSpeciOption(String speciOption) {
		this.speciOption = speciOption;
	}

	public long getSpeciId() {
		return speciId;
	}

	public void setSpeciId(long speciId) {
		this.speciId = speciId;
	}
}
