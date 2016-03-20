/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.framework.util;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.constant.MmallConstant;

/**
 * AreaCodeUtil.java created by yydx811 at 2015年6月23日 上午11:25:17 区域code工具类
 *
 * @author yydx811
 */
public class AreaCodeUtil {

	/**
	 * xxxx00代表莫省某城市全区
	 * 
	 * @param districtCode
	 * @return
	 */
	public static long getCityCode(String districtCode) {
		long provinceCode = getProviceCode(districtCode);
		if (MmallConstant.directCityCodeMap.containsKey(provinceCode)) {
			return MmallConstant.directCityCodeMap.get(provinceCode);
		}
		return Long.parseLong(StringUtils.substring(districtCode, 0, 4));
	}

	/**
	 * 00 代表全国
	 * 
	 * @param districtCode
	 * @return
	 */
	public static long getProviceCode(String districtCode) {
		return Long.parseLong(StringUtils.substring(districtCode, 0, 2));
	}

	/**
	 * xxxx00代表莫省某城市全区
	 * 
	 * @param areaId
	 * @return
	 */
	public static long getCityCode(long areaId) {
		long provinceCode = getProvinceCode(areaId) / 10000l;
		if (MmallConstant.directCityCodeMap.containsKey(provinceCode)) {
			return MmallConstant.directCityCodeMap.get(provinceCode) * 100l;
		}
		return areaId / 100l * 100l;
	}

	/**
	 * 00 代表全国
	 * 
	 * @param districtCode
	 * @return
	 */
	public static long getProvinceCode(long areaId) {
		return areaId / 10000l * 10000l;
	}

	/**
	 * 判断区code有没存在
	 * 
	 * @param areaCode
	 * @param districtIds
	 * @return
	 */
	public static boolean isContainArea(String areaCode, String districtIds) {
		// 000000代表全国
		if (districtIds.indexOf(areaCode) >= 0 || districtIds.indexOf("000000") >= 0) {
			return true;
		}
		String[] districtIdArray = StringUtils.split(districtIds, ",");
		for (String districtId : districtIdArray) {
			// 0000代表某个省或直辖市
			if (districtId.indexOf("0000") >= 0) {
				if (getProviceCode(areaCode) == getProviceCode(districtId)) {
					return true;
				}
				continue;
			}
			// 00 代表城市
			if (districtId.indexOf("00") == 4) {
				if (getCityCode(areaCode) == getCityCode(districtId)) {
					return true;
				}
			}

		}
		return false;
	}
}
