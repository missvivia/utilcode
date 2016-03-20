package com.xyl.mmall.bi.listener;

import java.util.Map;

import com.xyl.mmall.bi.core.constant.BILogKey;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.core.enums.OpAction;
import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogClient;
import com.xyl.mmall.jms.service.base.BaseMessageListener;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
@SuppressWarnings("rawtypes")
public class NQSMessageHandler extends BaseMessageListener<Map> {

	private BILogClient biLogClient;

	// private static Logger logger =
	// LoggerFactory.getLogger(NQSMessageHandler.class);

	public void setBiLogClient(BILogClient biLogClient) {
		this.biLogClient = biLogClient;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Map messageMap) {
		String type = (String) messageMap.get(BILogKey.TYPE);
		long time = (long) messageMap.get(BILogKey.TIME);
		String action = (String) messageMap.get(BILogKey.ACTION);
		String accountId = (String) messageMap.get(BILogKey.ACCOUNT_ID);
		String clientType = (String) messageMap.get(BILogKey.CLIENT_TYPE);
		String deviceOs = (String) messageMap.get(BILogKey.DEVICE_OS);
		String deviceType = (String) messageMap.get(BILogKey.DEVICE_TYPE);
		String ip = (String) messageMap.get(BILogKey.IP);
		String otherKey = (String) messageMap.get(BILogKey.OTHER_KEY);
		String provinceCode = (String) messageMap.get(BILogKey.PROVINCE_CODE);

		// 2.设置日志通用信息实体.
		BasicLog basicLog = new BasicLog();
		basicLog.setAccountId(accountId);
		basicLog.setAction(OpAction.UNKNOWN.genEnumByValue(action));
		basicLog.setTime(time);
		basicLog.setType(BIType.UNKNOWN.genEnumByValue(type));
		basicLog.setClientType(ClientType.NULL.genEnumByValue(clientType));
		basicLog.setDeviceOs(deviceOs);
		basicLog.setDeviceType(deviceType);
		basicLog.setIp(ip);
		basicLog.setProvinceCode(provinceCode);
		if (messageMap.containsKey(BILogKey.REFERER)) {
			basicLog.setReferer((String) messageMap.get(BILogKey.REFERER));
		}

		// 3.打印日志
		biLogClient.logInfo(basicLog, messageMap, otherKey);
		return true;
	}

}
