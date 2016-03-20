package com.xyl.mmall.cms.dto;

import java.util.List;

public class PromotionContentVO {
	private int type;
	private String name;
	private List<PromotionContentDTO> list;
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PromotionContentDTO> getList() {
		return list;
	}
	public void setList(List<PromotionContentDTO> list) {
		this.list = list;
	}
	
	
}
