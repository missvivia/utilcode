package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.meta.ActiveTell;

public class ActiveTellDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ActiveTell activeTell;

	public ActiveTell getActiveTell() {
		return activeTell;
	}

	public void setActiveTell(ActiveTell activeTell) {
		this.activeTell = activeTell;
	}
}
