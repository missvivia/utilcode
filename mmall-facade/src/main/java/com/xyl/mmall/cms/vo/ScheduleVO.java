package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.dto.PODTO;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleVO implements Serializable {

	private static final long serialVersionUID = -16101841653354093L;

	private PODTO po;

	public PODTO getPo() {
		return po;
	}

	public void setPo(PODTO po) {
		this.po = po;
	}

}
