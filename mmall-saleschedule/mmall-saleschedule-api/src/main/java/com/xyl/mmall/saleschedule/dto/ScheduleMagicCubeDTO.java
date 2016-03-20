package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.xyl.mmall.saleschedule.meta.ScheduleMagicCube;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleMagicCubeDTO implements Serializable {

	private static final long serialVersionUID = -2198961247279484303L;

	private ScheduleMagicCube magicCube;

	public ScheduleMagicCube getMagicCube() {
		return magicCube;
	}

	public void setMagicCube(ScheduleMagicCube magicCube) {
		this.magicCube = magicCube;
	}

	@Override
	public String toString() {
		return "ScheduleMagicCubeDTO [magicCube=" + magicCube + "]";
	}

}
