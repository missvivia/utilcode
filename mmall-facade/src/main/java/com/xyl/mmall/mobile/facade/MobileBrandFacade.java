/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.param.MobilePageCommonAO;
import com.xyl.mmall.mobile.ios.facade.param.BrandSearchVO;
import com.xyl.mmall.saleschedule.meta.Brand;

/**
 * @author hzjiangww
 *
 */
public interface MobileBrandFacade {
	/**
	 * 获取品牌列表
	 * @param type
	 * @param ao
	 * @return
	 */
	BaseJsonVO getBrandList(long userId,int areaId,Integer type,MobilePageCommonAO ao);
	
	BaseJsonVO getBrandDetail(long userId,long brandId,int areaCode,int os);
	
	BaseJsonVO getShopByCity(long brandId,int areaId,long district,int os);
	
	public List<Brand> getBrandListInOrderByCategory(BrandSearchVO brandSearchVO, long areaId) throws Exception;
}