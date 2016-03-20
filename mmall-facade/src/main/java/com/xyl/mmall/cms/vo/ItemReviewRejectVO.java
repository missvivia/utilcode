package com.xyl.mmall.cms.vo;

import java.util.List;

public class ItemReviewRejectVO {
	private List<Long> ids;

	/**
	 * 档期id
	 */
	private List<Long> poList;

	private String reason;

	private String descp;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public List<Long> getPoList() {
		return poList;
	}

	public void setPoList(List<Long> poList) {
		this.poList = poList;
	}

}
