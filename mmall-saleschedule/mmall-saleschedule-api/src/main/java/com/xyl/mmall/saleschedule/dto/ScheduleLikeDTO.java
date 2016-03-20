package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.saleschedule.meta.ScheduleLike;

/**
 * PO收藏 DTO
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleLikeDTO implements Serializable {

	private static final long serialVersionUID = 2054784133108220062L;

	private ScheduleLike like;

	public ScheduleLike getLike() {
		return like;
	}

	public void setLike(ScheduleLike like) {
		this.like = like;
	}

	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
