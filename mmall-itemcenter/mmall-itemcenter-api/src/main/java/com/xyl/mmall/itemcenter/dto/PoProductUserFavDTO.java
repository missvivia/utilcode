package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.PoProductUserFav;

public class PoProductUserFavDTO extends PoProductUserFav{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6777425481048128483L;
	
	public PoProductUserFavDTO() {
	}

	public PoProductUserFavDTO(PoProductUserFav obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

}
