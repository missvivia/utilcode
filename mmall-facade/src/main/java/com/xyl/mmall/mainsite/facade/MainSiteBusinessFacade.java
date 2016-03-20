package com.xyl.mmall.mainsite.facade;

import com.xyl.mmall.cms.dto.BusinessDTO;

/**
 * mainsite商家信息Facade.
 * 
 * @author wangfeng
 *
 */
public interface MainSiteBusinessFacade {

	/**
	 * 根据站点,品牌以及商家类型(代理商/品牌商)获取商家信息.
	 * 
	 * @param areaId
	 * @param brandId
	 *            品牌id.
	 * @param type
	 *            商家类型1=代理商,2=是品牌商
	 * @return
	 */
	public BusinessDTO getBusinessByAreaIdAndBrandId(Long areaId, Long brandId, int type);

}
