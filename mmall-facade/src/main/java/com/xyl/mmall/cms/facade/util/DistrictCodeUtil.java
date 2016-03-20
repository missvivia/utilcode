package com.xyl.mmall.cms.facade.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.cms.dto.SendDistrictDTO;
import com.xyl.mmall.constant.MmallConstant;

/**
 * 配送区域code提取工具类 区码由省code+城市code+xx
 * 
 * @author lhp
 *
 */
public class DistrictCodeUtil {

	/**
	 * xx00代表莫省全部城市
	 * 
	 * @param districtCode
	 * @return
	 */
	public static long getCityCode(String districtCode) {
		long provinceCode = getProvinceCode(districtCode);
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
	public static long getProvinceCode(String districtCode) {
		return Long.parseLong(StringUtils.substring(districtCode, 0, 2));
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
				if (getProvinceCode(areaCode) == getProvinceCode(districtId)) {
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

	public static boolean isContainArea(String areaCode, SendDistrictDTO sendDistrictDTO) {
		StringBuffer districtId = new StringBuffer(6);
		// 如果省id是0 表示全国
		if (sendDistrictDTO.getProvinceId() == 0) {
			return true;
		} else {
			// 如果市是0表示全省
			if (sendDistrictDTO.getCityId() == 0) {
				districtId.append(sendDistrictDTO.getProvinceId()).append("0000");
			} else {
				// 如果区是0表示全市
				if (sendDistrictDTO.getDistrictId() == 0) {
					// 市id是负数表示是直辖市
					if (sendDistrictDTO.getCityId() <= 0) {
						districtId.append(sendDistrictDTO.getProvinceId()).append("0000");
					} else {
						districtId.append(sendDistrictDTO.getCityId()).append("00");
					}
				} else {
					// 如果区不为0取区地址
					districtId.append(sendDistrictDTO.getDistrictId());
				}
			}
		}
		return isContainArea(areaCode, districtId.toString());
	}

	public static Map<Long, Integer> getAreaId(SendDistrictDTO sendDistrictDTO) {
		Map<Long, Integer> map = new HashMap<Long, Integer>(1);
		// 如果省id是0 表示全国
		if (sendDistrictDTO.getProvinceId() == 0) {
			map.put(sendDistrictDTO.getProvinceId(), 1);
		} else {
			// 如果市是0表示全省
			if (sendDistrictDTO.getCityId() == 0) {
				map.put(sendDistrictDTO.getProvinceId(), 1);
			} else {
				// 如果区是0表示全市
				if (sendDistrictDTO.getDistrictId() == 0) {
					// 市id是负数表示是直辖市
					if (sendDistrictDTO.getCityId() <= 0) {
						map.put(sendDistrictDTO.getProvinceId(), 1);
					} else {
						map.put(sendDistrictDTO.getCityId(), 2);
					}
				} else {
					// 如果区不为0取区地址
					map.put(sendDistrictDTO.getDistrictId(), 3);
				}
			}
		}
		return map;
	}

	public static String isAreaRepeat(Set<String> areaSet) {
		StringBuffer areaIds = new StringBuffer();
		if (areaSet.contains("000000") && areaSet.size() > 1) {
			return null;
		} else {
			// 省市区map
			Map<String, Map<String, Set<String>>> areaMap = new HashMap<String, Map<String, Set<String>>>();
			for (String area : areaSet) {
				if (area.length() != 6) {
					return null;
				}
				// 分割省市区code
				String province = area.substring(0, 2);
				String city = area.substring(2, 4);
				String district = area.substring(4, 6);
				Map<String, Set<String>> cityMap = null;
				Set<String> districtSet = null;
				// 判断map是否包含省
				if (areaMap.containsKey(province)) {
					cityMap = areaMap.get(province);
					if (city.equals("00")) {
						return null;
					}
					if (cityMap.containsKey("00")) {
						return null;
					}
					// 判断cityMap是否包含市
					if (cityMap.containsKey(city)) {
						districtSet = cityMap.get(city);
						if (district.equals("00")) {
							return null;
						}
						if (districtSet.contains("00")) {
							return null;
						}
						// 判断set是否包含区
						// 因为是set集合所以不会 有重复的
						// if (districtSet.contains(districtSet)) {
						// return null;
						// }
					} else {
						districtSet = new HashSet<String>();
					}
				} else {
					cityMap = new HashMap<String, Set<String>>();
					districtSet = new HashSet<String>();
				}
				districtSet.add(district);
				cityMap.put(city, districtSet);
				areaMap.put(province, cityMap);
				areaIds.append("(").append(area).append(")");
			}
		}
		if (areaIds.length() > 0) {
			return areaIds.toString();
		} else {
			return null;
		}
	}
}
