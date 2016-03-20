package com.xyl.mmall.base;

import java.io.Serializable;
import java.util.Map;

public class JobParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 通用参数
	 * 主要包含job server平台传入的参数:任务id,timstamp等
	 */
	private JobCommonParam commonParam;
	
	/**
	 * 含有全部参数，包含之后业务方制定的参数
	 */
	private Map<String,Object>  paramMap;
	
	public JobCommonParam getCommonParam() {
		return commonParam;
	}

	public void setCommonParam(JobCommonParam commonParam) {
		this.commonParam = commonParam;
	}

	public String toString() {
		return "id:" + commonParam.getId() + "uuid:" + commonParam.getUuid() + ",signature:"
				+ commonParam.getSignature() + ",nonce:" + commonParam.getNonce() + ",time:"
				+ commonParam.getTimestamp();
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
}
