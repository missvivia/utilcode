/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.backend.vo;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.xyl.mmall.framework.vo.BaseVersionVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDetailDTO;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;

/**
 * ProductSKUDetailVO.java created by yydx811 at 2015年5月15日 下午4:43:12
 * 商品详情vo
 *
 * @author yydx811
 */
public class ProductSKUDetailVO extends BaseVersionVO {

	/** 序列化id. */
	private static final long serialVersionUID = 5258586504541635070L;

	/** 详情id. */
	private long detailId;

	/** 商品id. */
	private long skuId;

	/** 用户富文本框编辑的HTML. */
	private String customEditHTML;

	/** 商品参数详情. */
	private String prodParamJson;

	public ProductSKUDetailVO() {
	}

	public ProductSKUDetailVO(ProductSKUDetailDTO obj) {
		this.detailId = obj.getId();
		this.skuId = obj.getProductSKUId();
		Map<String, String> map = ItemCenterUtil.getEditHTML_Param(obj.getCustomEditHTML(), null);
		if (!CollectionUtils.isEmpty(map)) {
			this.customEditHTML = map.get("html");
		}
		this.prodParamJson = obj.getProdParam();
		this.setCreateTime(obj.getCreateTime());
		this.setUpdateTime(obj.getUpdateTime());
	}
	
	public ProductSKUDetailDTO convertToDTO() {
		ProductSKUDetailDTO skuDetailDTO = new ProductSKUDetailDTO();
		skuDetailDTO.setId(detailId);
		skuDetailDTO.setProductSKUId(skuId);
		skuDetailDTO.setCustomEditHTML(customEditHTML);
		skuDetailDTO.setProdParam(prodParamJson);
		return skuDetailDTO;
	}
	
	public long getDetailId() {
		return detailId;
	}

	public void setDetailId(long detailId) {
		this.detailId = detailId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public String getCustomEditHTML() {
		return customEditHTML;
	}

	public void setCustomEditHTML(String customEditHTML) {
		this.customEditHTML = customEditHTML;
	}

	public String getProdParamJson() {
		return prodParamJson;
	}

	public void setProdParamJson(String prodParamJson) {
		this.prodParamJson = prodParamJson;
	}
}
