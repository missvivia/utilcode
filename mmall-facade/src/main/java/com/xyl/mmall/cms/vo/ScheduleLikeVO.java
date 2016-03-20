package com.xyl.mmall.cms.vo;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.dto.ScheduleLikeDTO;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleLikeVO implements Serializable {

	private static final long serialVersionUID = -6973445816607715110L;

	private ScheduleLikeDTO like;

	public ScheduleLikeDTO getLike() {
		return like;
	}

	public void setLike(ScheduleLikeDTO like) {
		this.like = like;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
