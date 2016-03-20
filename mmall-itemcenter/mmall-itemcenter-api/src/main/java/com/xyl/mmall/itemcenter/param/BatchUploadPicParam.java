package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BatchUploadPicParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3446562413900494839L;

	private String goodsNo;

	private String colorNum;

	private List<String> prodShowPicList;

	private List<String> listShowPicList;

	private List<String> htmlPicList;
	
	public BatchUploadPicParam() {
		prodShowPicList = new ArrayList<String>();
		listShowPicList = new ArrayList<String>();
		htmlPicList = new ArrayList<String>();
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getColorNum() {
		return colorNum;
	}

	public void setColorNum(String colorNum) {
		this.colorNum = colorNum;
	}

	public List<String> getProdShowPicList() {
		return prodShowPicList;
	}

	public void setProdShowPicList(List<String> prodShowPicList) {
		this.prodShowPicList = prodShowPicList;
	}

	public List<String> getListShowPicList() {
		return listShowPicList;
	}

	public void setListShowPicList(List<String> listShowPicList) {
		this.listShowPicList = listShowPicList;
	}

	public List<String> getHtmlPicList() {
		return htmlPicList;
	}

	public void setHtmlPicList(List<String> htmlPicList) {
		this.htmlPicList = htmlPicList;
	}

	@Override
	public int hashCode() {
		int elm1 = goodsNo.hashCode();
		int result = 1;
		result = 31 * result + elm1;
		int elm2 = colorNum.hashCode();
		result = 31 * result + elm2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BatchUploadPicParam other = (BatchUploadPicParam) obj;
		if (!goodsNo.equals(other.getGoodsNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		return true;
	}
}
