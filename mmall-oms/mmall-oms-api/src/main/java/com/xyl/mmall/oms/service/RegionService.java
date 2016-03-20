/**
 * 
 */
package com.xyl.mmall.oms.service;

import com.xyl.mmall.oms.dto.Region;

/**
 * @author hzzengchengyuan
 *
 */
public interface RegionService {
	/**
	 * 根据code获取三级行政区，包括该三级行政区的上两级行政区
	 * 
	 * @param code
	 * @return
	 */
	Region getThreeRegionByCode(String code);

	/**
	 * @param code
	 * @return
	 */
	Region getRegion(String code);

	/**
	 * 根据code获取城市，不包括上级行政区和子行政区
	 * 
	 * @param code
	 * @return
	 */
	Region getCity(String code);

	/**
	 * 根据code获取省份，不包括子行政区
	 * 
	 * @param code
	 * @return
	 */
	Region getProvince(String code);

	/**
	 * 根据code获取区/县，不包括子行政区
	 * 
	 * @param code
	 * @return
	 */
	Region getSection(String code);
}
