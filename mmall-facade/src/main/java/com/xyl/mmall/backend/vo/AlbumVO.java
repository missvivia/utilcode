package com.xyl.mmall.backend.vo;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.photomgr.dto.AlbumDTO;

public class AlbumVO implements java.io.Serializable {

	private static final long serialVersionUID = -9030869477460554900L;
	
	private AlbumDTO dto;

	public AlbumDTO getDto() {
		return dto;
	}

	public void setDto(AlbumDTO dto) {
		this.dto = dto;
	}
	
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
