/**
 * 
 */
package com.xyl.mmall.mobile.facade;

import java.util.List;

import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.mobile.facade.vo.MobileProductSKUVO;
import com.xyl.mmall.mobile.facade.vo.MobileProductSearchVO;
import com.xyl.mmall.mobile.ios.facade.pageView.prdctlist.MobileSku;

/**
 * @author hzjiangww
 *
 */
public interface MobileProductFacade {
	/**
	 * 获得商品详情页
	 * @param userId
	 * @param areaCode
	 * @return
	 */
	BaseJsonVO getProductDetail(long userId ,int areaCode,long prdtId,long appversion);
	/**
	 * 获得商品列表信息
	 * @param basePageParamVO
	 * @param searchParam
	 * @return
	 */
	List<MobileProductSKUVO> getProudctByParameters(BasePageParamVO<MobileProductSKUVO> basePageParamVO,
			MobileProductSearchVO searchParam);
	
	public List<MobileSku> getSkuByParameters(BasePageParamVO<MobileSku> basePageParamVO,
			MobileProductSearchVO searchParam);
}