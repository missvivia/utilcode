/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.io.Serializable;

/**
 * ProdParamOptionVO.java created by yydx811 at 2015年5月15日 上午11:39:02
 * 商品属性选项列表
 *
 * @author yydx811
 */
public class ProdParamOptionVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = -8569381045087810659L;

	/** 选项id. */
	private long paramOptionId;
	
	/** 选项值. */
	private String paramOption;

	/** 是否选中，1选中，2未选中. */
	private int isCheck;

	public long getParamOptionId() {
		return paramOptionId;
	}

	public void setParamOptionId(long paramOptionId) {
		this.paramOptionId = paramOptionId;
	}

	public String getParamOption() {
		return paramOption;
	}

	public void setParamOption(String paramOption) {
		this.paramOption = paramOption;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
}
