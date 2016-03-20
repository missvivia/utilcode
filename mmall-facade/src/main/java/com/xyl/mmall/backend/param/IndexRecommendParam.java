package com.xyl.mmall.backend.param;

import java.util.List;

import com.xyl.mmall.itemcenter.dto.SkuRecommendationDTO;

/**
 * 
 * IndexRecommendParam.java created by lhp at 2016年1月1日 下午1:43:29 首页商品推荐参数
 *
 * @author lhp
 */
public class IndexRecommendParam {

	private List<SkuRecommendationDTO> skuRecommendationDTOs;

	public List<SkuRecommendationDTO> getSkuRecommendationDTOs() {
		return skuRecommendationDTOs;
	}

	public void setSkuRecommendationDTOs(List<SkuRecommendationDTO> skuRecommendationDTOs) {
		this.skuRecommendationDTOs = skuRecommendationDTOs;
	}

}
