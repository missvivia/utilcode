package com.xyl.mmall.bi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.xyl.mmall.bi.core.constant.BILogKey;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.meta.BasicLog;

/**
 * 
 * @author wangfeng
 * 
 */
public final class BILogUtils {

	private BILogUtils() {
	}

	/**
	 * 日志通用信息map.
	 * 
	 * @param basicLog
	 * @param logMap
	 * @return
	 */
	public static Map<String, Object> getBasicLogMap(BasicLog basicLog, Map<String, Object> logMap) {
		Map<String, Object> infoMap = new LinkedHashMap<>();
		infoMap.put(BILogKey.TIME, basicLog.getTime());
		infoMap.put(BILogKey.ACTION, basicLog.getAction().getValue());
		infoMap.put(BILogKey.TYPE, basicLog.getType().getValue());

		// 根据ClientType获取参数.
		ClientType clientType = basicLog.getClientType();
		infoMap.put(BILogKey.CLIENT_TYPE, clientType.getValue());
		if (ClientType.ORDER != clientType) {
			// 非销售数据日志通用字段.
			infoMap.put(BILogKey.ACCOUNT_ID, basicLog.getAccountId());
			infoMap.put(BILogKey.DEVICE_TYPE, basicLog.getDeviceType());
			infoMap.put(BILogKey.DEVICE_OS, basicLog.getDeviceOs());
			infoMap.put(BILogKey.IP, basicLog.getIp());
			infoMap.put(BILogKey.PROVINCE_CODE, basicLog.getProvinceCode());
		}
		if (ClientType.WEB == clientType || ClientType.WAP == clientType) {
			// mainsite和wap通用字段.
			infoMap.put(BILogKey.BROWSER, logMap.get(BILogKey.BROWSER));
			infoMap.put(BILogKey.COOKIE, logMap.get(BILogKey.COOKIE));
			infoMap.put(BILogKey.REFERER, logMap.get(BILogKey.REFERER));
		} else if (clientType == ClientType.APP) {
			// app通用字段.
			infoMap.put(BILogKey.LONGITUDE, logMap.get(BILogKey.LONGITUDE));
			infoMap.put(BILogKey.LATITUDE, logMap.get(BILogKey.LATITUDE));
			infoMap.put(BILogKey.DEVICE_MODEL, logMap.get(BILogKey.DEVICE_MODEL));
			infoMap.put(BILogKey.DEVICE_RESOLUTION, logMap.get(BILogKey.DEVICE_RESOLUTION));
			infoMap.put(BILogKey.DEVICE_PLATFORM, logMap.get(BILogKey.DEVICE_PLATFORM));
			infoMap.put(BILogKey.DEVICE_NETWORK, logMap.get(BILogKey.DEVICE_NETWORK));
			infoMap.put(BILogKey.DEVICE_OS_VERSION, logMap.get(BILogKey.DEVICE_OS_VERSION));
			infoMap.put(BILogKey.DEVICE_UDID, logMap.get(BILogKey.DEVICE_UDID));
			infoMap.put(BILogKey.APP_CHANNEL, logMap.get(BILogKey.APP_CHANNEL));
			infoMap.put(BILogKey.APP_VERSION, logMap.get(BILogKey.APP_VERSION));
		}
		return infoMap;
	}

}
