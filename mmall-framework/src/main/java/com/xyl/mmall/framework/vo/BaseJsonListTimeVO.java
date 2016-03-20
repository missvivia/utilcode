package com.xyl.mmall.framework.vo;

import java.io.Serializable;
import java.util.List;

public class BaseJsonListTimeVO extends BaseJsonListVO implements Serializable {

	private static final long serialVersionUID = -5266912696546343633L;

	private long timestamp;

	public BaseJsonListTimeVO() {
		super();
	}

	public BaseJsonListTimeVO(List<?> list) {
		super(list);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
