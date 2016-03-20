package com.xyl.mmall.itemcenter.dao.productsku;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.itemcenter.meta.SkuRecommendation;

/**
 * 
 * SkuRecommendationDao.java created by lhp at 2015年12月31日 上午11:09:31
 * 这里对类或者接口作简要描述
 *
 * @author lhp
 */
public interface SkuRecommendationDao extends AbstractDao<SkuRecommendation> {

	/**
	 * 根据商家Id取得首页商品推荐
	 * 
	 * @param businessId
	 * @return
	 */
	public List<SkuRecommendation> getSKuRecommendationListByBusinessId(long businessId);

}
