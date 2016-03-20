/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import com.xyl.mmall.framework.vo.BaseVersionVO;
import com.xyl.mmall.itemcenter.dto.ProdPicDTO;

/**
 * ProdPicVO.java created by yydx811 at 2015年5月15日 下午4:25:23
 * 商品图片vo
 *
 * @author yydx811
 */
public class ProdPicVO extends BaseVersionVO {

	/** 序列化id. */
	private static final long serialVersionUID = -8662598202894074085L;

	/** 商品图片id. */
	private long prodPicId;

	/** 商品id. */
	private long skuId;

	/** 图片的类型. */
	private int picType;

	/** 图片路径. */
	private String picPath;

	public ProdPicVO() {
	}

	public ProdPicVO(ProdPicDTO obj) {
		this.prodPicId = obj.getId();
		this.skuId = obj.getProductSKUId();
		this.picType = obj.getType();
		this.picPath = obj.getPath();
		this.setCreateTime(obj.getCreateTime());
		this.setUpdateTime(obj.getUpdateTime());
	}
	
	public ProdPicDTO convertToDTO() {
		ProdPicDTO picDTO = new ProdPicDTO();
		picDTO.setId(prodPicId);
		picDTO.setProductSKUId(skuId);
		picDTO.setType(picType);
		picDTO.setPath(picPath);
		return picDTO;
	}
	
	public long getProdPicId() {
		return prodPicId;
	}

	public void setProdPicId(long prodPicId) {
		this.prodPicId = prodPicId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getPicType() {
		return picType;
	}

	public void setPicType(int picType) {
		this.picType = picType;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
