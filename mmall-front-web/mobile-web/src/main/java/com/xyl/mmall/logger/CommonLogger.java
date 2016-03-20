package com.xyl.mmall.logger;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.AppLog;
import com.xyl.mmall.bi.core.service.NQSBILogMessageProducer;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;

@Service
public class CommonLogger {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private NQSBILogMessageProducer nqsBILogMessageProducer;

	public void logger(MobileHeaderAO ao, String action, String type, long userid, HashMap<String, Object> extr) {
		try {
			HashMap<String, Object> logger = genLogger(ao, action, type);
			logger.put("accountId", String.valueOf(userid));
			logger.putAll(extr);
			printLog(logger);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	public void logger(MobileHeaderAO ao, String action, String type, long userid) {
		try {
			HashMap<String, Object> logger = genLogger(ao, action, type);
			logger.put("accountId", String.valueOf(userid));
			printLog(logger);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	public void logger(MobileHeaderAO ao, String action, String type) {
		try {
			HashMap<String, Object> logger = genLogger(ao, action, type);
			printLog(logger);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	public void printLog(HashMap<String, Object> logger) {
		try {
			AppLog appLog = new AppLog();
			appLog.setLog(logger);
			// appLog.setAccountId(logger.get("accountId").toString());
			// logger.remove("accountId");
			// appLog.setAction(logger.);
			appLog.setTime(Converter.getTime());
			// 先不考虑性能了
			log.info(JsonUtils.toJson(logger));
			nqsBILogMessageProducer.sendBILog(appLog, "");
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	private HashMap<String, Object> genLogger(MobileHeaderAO ao, String action, String type) {
		HashMap<String, Object> mao = ao.getMaper();
		mao.put("action", action);
		mao.put("type", type);
		mao.put("deviceType", "mobile");
		mao.put("clientType", "APP");
		mao.put("provinceCode", ao.getAreaCode());
		return mao;
	}

	public void insertResult(BaseJsonVO vo, String[] name, String[] showName, MobileHeaderAO ao, String action,
			String type, long userid) {
		insertResult(vo, name, showName, ao, action, type, userid, false);
	}

	public void insertResult(BaseJsonVO vo, String[] name, String[] showName, MobileHeaderAO ao, String action,
			String type, long userid, boolean limit) {
		try {
			HashMap<String, Object> logger = genLogger(ao, action, type);
			logger.put("accountId", String.valueOf(userid));
			String result = JsonUtils.toJson(vo);
			if (name != null && name.length > 0 && name.length == showName.length) {
				for (int i = 0; i < name.length; i++) {
					String regex = "\"" + name[i] + "\":\"?([^,\"]+)";

					Pattern pattern = Pattern.compile(regex);
					String results = "";
					Matcher m = pattern.matcher(result);
					while (m.find()) {
						results = results + m.group(1) + "&";
						if (limit)
							break;
					}
					if (results.endsWith("&"))
						results = results.substring(0, results.length() - 1);

					logger.put(showName[i], results);
				}
			}
			printLog(logger);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

}
