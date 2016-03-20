package com.xyl.mmall.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 省份区域code和省份编码的工具类
 * @author chengximing
 *
 */
public class ProvinceCodeMapUtil {

	private static Map<Long, Long> provinceCodeMap = new HashMap<>();
	
	private static Map<Long, Long> provinceCodeReverseMap = new HashMap<>();
	
	// private static final int MAX_PROVINCE_COUNT = 31;
	
	public static long QUANGUO = 2147483647 << 1;
	
	private static final Logger logger=LoggerFactory.getLogger(ProvinceCodeMapUtil.class);
	
	static {
		// 全国的省份code的关系对照map
		provinceCodeMap.put(11L, 0x01L);		// 北京
		provinceCodeMap.put(12L, 0x01L << 1);	// 天津
		provinceCodeMap.put(13L, 0x01L << 2);	// 河北省
		provinceCodeMap.put(14L, 0x01L << 3);	// 山西省
		provinceCodeMap.put(15L, 0x01L << 4);	// 内蒙古自治区
		provinceCodeMap.put(21L, 0x01L << 5);	// 辽宁省
		provinceCodeMap.put(22L, 0x01L << 6);	// 吉林省
		provinceCodeMap.put(23L, 0x01L << 7);	// 黑龙江省
		provinceCodeMap.put(31L, 0x01L << 8);	// 上海市
		provinceCodeMap.put(32L, 0x01L << 9);	// 江苏省
		provinceCodeMap.put(33L, 0x01L << 10);	// 浙江省
		provinceCodeMap.put(34L, 0x01L << 11);	// 安徽省
		provinceCodeMap.put(35L, 0x01L << 12);	// 福建省
		provinceCodeMap.put(36L, 0x01L << 13);	// 江西省
		provinceCodeMap.put(37L, 0x01L << 14);	// 山东省
		provinceCodeMap.put(41L, 0x01L << 15);	// 河南省
		provinceCodeMap.put(42L, 0x01L << 16);	// 湖北省
		provinceCodeMap.put(43L, 0x01L << 17);	// 湖南省
		provinceCodeMap.put(44L, 0x01L << 18);	// 广东省
		provinceCodeMap.put(45L, 0x01L << 19);	// 广西壮族自治区
		provinceCodeMap.put(46L, 0x01L << 20);	// 海南省
		provinceCodeMap.put(50L, 0x01L << 21);	// 重庆市
		provinceCodeMap.put(51L, 0x01L << 22);	// 四川省
		provinceCodeMap.put(52L, 0x01L << 23);	// 贵州省
		provinceCodeMap.put(53L, 0x01L << 24);	// 云南省
		provinceCodeMap.put(54L, 0x01L << 25);	// 西藏自治区
		provinceCodeMap.put(61L, 0x01L << 26);	// 陕西省
		provinceCodeMap.put(62L, 0x01L << 27);	// 甘肃省
		provinceCodeMap.put(63L, 0x01L << 28);	// 青海省
		provinceCodeMap.put(64L, 0x01L << 29);	// 宁夏回族自治区
		provinceCodeMap.put(65L, 0x01L << 30);	// 新疆维吾尔族自治区
		
		provinceCodeMap.put(1000L, 0x01L << 31);	// 网易 特殊字段
		provinceCodeMap.put(3301L, 0x01L << 32);	// 新疆维吾尔族自治区
		
		for (Long code : provinceCodeMap.keySet()) {
			provinceCodeReverseMap.put(provinceCodeMap.get(code), code);
		}
	}
	
	/**
	 * 传入code list返回对应的省份编码
	 * @param codeList
	 * @return
	 * @throws Exception
	 */
	public static long getProvinceFmtByCodeList(List<Long> codeList) throws Exception {
		long provinceFmt = 0x00;
		for (Long code : codeList) {
			// 排除网易的特殊code 1000
//			if (code.equals(1000L)) {
//				logger.info(" code 1000 for getProvinceFmtByCodeList");
//				continue;
//			}
			Long fmtCode = provinceCodeMap.get(code);
			if (fmtCode == null) {
				logger.error("Input code List contains code which is not in the Province List,code:"+code);
//				throw new Exception("Input code List contains code which is not in the Province List,code:"+code);
			}
			provinceFmt |= fmtCode;
		}
		return provinceFmt;
	}
	
	/**
	 * 传入非零的省份二进制编码，返回对应的code list
	 * 这个是计算二进制一的快速算法，二进制有多少个1会循环几次
	 * @param fmt
	 * @return
	 * @throws Exception
	 */
	public static List<Long> getCodeListByProvinceFmt(long fmt) throws Exception {
		List<Long> retList = new ArrayList<>();
		while (fmt != 0) {
			long fmtleft = fmt & (fmt - 1);
			long singleFmt = fmt - fmtleft;
			Long code = provinceCodeReverseMap.get(singleFmt);
			if (code == null) {
				throw new Exception("Input fmt Province contains code which is not in the Province code");
			}
			retList.add(code);
			fmt = fmtleft;
		}
		return retList;
	}
	
	public static boolean getLocationPermissionState(long fmtFilter, List<Long> cmsPermission, long code) {
		try {
			long fmtPermission = getProvinceFmtByCodeList(cmsPermission);
			if ((fmtFilter & fmtPermission) != fmtFilter) {
				return false;
			} else {
				long singleFmt = provinceCodeMap.get(code);
				if ((fmtFilter & singleFmt) == singleFmt) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 传入省份编码，返回对应的二进制代码
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static Long getProvinceFmtByCode(long code) throws Exception {
		Long fmt = provinceCodeMap.get(code);
			if (fmt == null) {
				throw new Exception("Input fmt Province contains code which is not in the Province code");
			}
		return fmt;
	}
	
}
