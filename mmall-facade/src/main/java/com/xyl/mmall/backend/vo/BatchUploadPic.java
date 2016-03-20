package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.itemcenter.param.ProductSaveParam;

public class BatchUploadPic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -38438145578081632L;

	private String goodsNo;

	private String colorNum;

	private List<String> productShowPicList;

	private List<String> productListPicList;

	public BatchUploadPic() {
		productShowPicList = new ArrayList<String>();
		productListPicList = new ArrayList<String>();
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

	public List<String> getProductShowPicList() {
		return productShowPicList;
	}

	public void setProductShowPicList(List<String> productShowPicList) {
		this.productShowPicList = productShowPicList;
	}

	public List<String> getProductListPicList() {
		return productListPicList;
	}

	public void setProductListPicList(List<String> productListPicList) {
		this.productListPicList = productListPicList;
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
		BatchUploadPic other = (BatchUploadPic) obj;
		if (!goodsNo.equals(other.getGoodsNo()))
			return false;
		if (!colorNum.equals(other.getColorNum()))
			return false;
		return true;
	}
}
