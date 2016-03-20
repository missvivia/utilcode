package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.util.List;

public class UserFavListDTO implements Serializable {
	private static final long serialVersionUID = -2840675436046891866L;
	
	private long userId;
	
	private List<Long> favIdList;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<Long> getFavIdList() {
		return favIdList;
	}

	public void setFavIdList(List<Long> favIdList) {
		this.favIdList = favIdList;
	}

}
