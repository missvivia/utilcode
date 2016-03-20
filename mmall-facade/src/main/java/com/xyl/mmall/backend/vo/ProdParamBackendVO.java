/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.util.List;

import com.xyl.mmall.framework.vo.BaseVersionVO;
import com.xyl.mmall.itemcenter.dto.ProdParamDTO;

/**
 * ProdParamBackendVO.java created by yydx811 at 2015年5月15日 上午10:50:31
 * 商品属性
 *
 * @author yydx811
 */
public class ProdParamBackendVO extends BaseVersionVO {

	/** 序列化id. */
	private static final long serialVersionUID = -6310225962652511126L;

	/** 商品id. */
	private long skuId;
	
	/** 商品属性id. */
	private long prodParamId;

	/** 模型属性id. */
	private long parameterId;

	/** 模型属性名称. */
	private String paramName;
	
	/** 操作样式,单选1,多选2. */
	private int single;

	/** 选项列表. */
	private List<ProdParamOptionVO> optionList;

	public ProdParamBackendVO() {
	}

	public ProdParamBackendVO(ProdParamDTO obj) {
		this.prodParamId = obj.getId();
		this.parameterId = obj.getModelParamId();
		this.skuId = obj.getProductSKUId();
		this.setCreateTime(obj.getCreateTime());
		this.setUpdateTime(obj.getUpdateTime());
	}
	
	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public long getProdParamId() {
		return prodParamId;
	}

	public void setProdParamId(long prodParamId) {
		this.prodParamId = prodParamId;
	}

	public long getParameterId() {
		return parameterId;
	}

	public void setParameterId(long parameterId) {
		this.parameterId = parameterId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public int getSingle() {
		return single;
	}

	public void setSingle(int single) {
		this.single = single;
	}

	public List<ProdParamOptionVO> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<ProdParamOptionVO> optionList) {
		this.optionList = optionList;
	}
}
