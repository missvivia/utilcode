package com.xyl.mmall.mobile.web.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "urlset")
public class FeedXmlVO {
	
	public FeedXmlVO() {
	}
	
	private List<FeedItemXmlVO> list;

	@XmlElement(name = "url")
	public List<FeedItemXmlVO> getList() {
		return list;
	}
	
	public void setList(List<FeedItemXmlVO> list) {
		this.list = list;
	}
	
	
}
