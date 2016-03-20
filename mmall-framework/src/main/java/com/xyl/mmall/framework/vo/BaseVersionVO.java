/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.framework.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * BaseVersionVO.java created by yydx811 at 2015年5月14日 下午9:03:46
 * 对应baseVersion
 *
 * @author yydx811
 */
public class BaseVersionVO implements Serializable {

	/** 序列化id. */
	private static final long serialVersionUID = 8414237519276139410L;

	private String creator;
	
	private String lastUpdateOperator;
	
	private Date createTime;
	
	private Date updateTime;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastUpdateOperator() {
		return lastUpdateOperator;
	}

	public void setLastUpdateOperator(String lastUpdateOperator) {
		this.lastUpdateOperator = lastUpdateOperator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
