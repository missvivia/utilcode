package com.xyl.mmall.mainsite.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "url")
public class FeedItemXmlVO {
	
	public FeedItemXmlVO() {
	}
	
	private String clickUrl;

	private FeedDataXmlVO data; 

	@XmlElement
	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	@XmlElement
	public FeedDataXmlVO getData() {
		return data;
	}

	public void setData(FeedDataXmlVO data) {
		this.data = data;
	}
	
}
