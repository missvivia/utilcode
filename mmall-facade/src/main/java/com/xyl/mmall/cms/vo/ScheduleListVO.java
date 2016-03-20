package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.dto.POListDTO;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleListVO implements Serializable {

	private static final long serialVersionUID = -7771753889710441436L;

	private POListDTO poList;

	public POListDTO getPoList() {
		return poList;
	}

	public void setPoList(POListDTO poList) {
		this.poList = poList;
	}

	@Override
	public String toString() {
		return "ScheduleListVO [poList=" + poList + "]";
	}

}
