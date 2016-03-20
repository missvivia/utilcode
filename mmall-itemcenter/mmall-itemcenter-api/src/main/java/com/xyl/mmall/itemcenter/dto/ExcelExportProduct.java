package com.xyl.mmall.itemcenter.dto;

import java.util.List;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.vo.SimpleIdValuePaire;
import com.xyl.mmall.itemcenter.meta.Product;

public class ExcelExportProduct extends ProductFullDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300987281279126980L;

	private String barCode;

	private String spec;

	private List<ProductParamDTO> paramList;

	public ExcelExportProduct(ProductFullDTO obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public List<ProductParamDTO> getParamList() {
		return paramList;
	}

	public void setParamList(List<ProductParamDTO> paramList) {
		this.paramList = paramList;
	}
}
