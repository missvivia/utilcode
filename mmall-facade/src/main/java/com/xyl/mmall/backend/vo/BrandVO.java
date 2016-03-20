package com.xyl.mmall.backend.vo;

import java.io.Serializable;

public class BrandVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7758821188170281487L;

	private long id;

//	private String name;
	
	private String nameZH;
	
	private String nameEN;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	//这个位混合后的输出。
	public String getName() {
		return this.nameEN+" "+this.nameZH;
	}

	public String getNameZH() {
		return nameZH;
	}

	public void setNameZH(String nameZH) {
		this.nameZH = nameZH;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}
	
	
//
//	public void setName(String name) {
//		this.name = name;
//	}
}
