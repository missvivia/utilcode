package com.xyl.mmall.backend.vo;

import java.util.List;

public class SizeVO {
	private List<SizeTemplateArchitect> sizeTemplate;

	private List<SizeHeaderVO> sizeHeader;
	
	public List<SizeTemplateArchitect> getSizeTemplate() {
		return sizeTemplate;
	}

	public void setSizeTemplate(List<SizeTemplateArchitect> sizeTemplate) {
		this.sizeTemplate = sizeTemplate;
	}

	public List<SizeHeaderVO> getSizeHeader() {
		return sizeHeader;
	}

	public void setSizeHeader(List<SizeHeaderVO> sizeHeader) {
		this.sizeHeader = sizeHeader;
	}

}
