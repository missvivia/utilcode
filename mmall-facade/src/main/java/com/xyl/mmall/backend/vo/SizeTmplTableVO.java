package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.util.List;

public class SizeTmplTableVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -822970153162152982L;
	private List<SizeHeaderVO> header;
	private List<List<String>> body;
	public List<SizeHeaderVO> getHeader() {
		return header;
	}
	public void setHeader(List<SizeHeaderVO> header) {
		this.header = header;
	}
	public List<List<String>> getBody() {
		return body;
	}
	public void setBody(List<List<String>> body) {
		this.body = body;
	}
	
}
