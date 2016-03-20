package com.xyl.mmall.itemcenter.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.itemcenter.meta.SkuRecommendation;

/**
 * 
 * SkuRecommendationDTO.java created by lhp at 2016年1月1日 上午10:27:26 首页商品推荐类
 *
 * @author lhp
 */
public class SkuRecommendationDTO extends SkuRecommendation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5833730039302653759L;

	/**
	 * 商品状态信息 : 删除,下架
	 */
	private String productStatusMsg;

	public SkuRecommendationDTO() {

	}

	public SkuRecommendationDTO(SkuRecommendation obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	public String getProductStatusMsg() {
		return productStatusMsg;
	}

	public void setProductStatusMsg(String productStatusMsg) {
		this.productStatusMsg = productStatusMsg;
	}

}
