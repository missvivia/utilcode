package com.xyl.mmall.logger;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;

public class ThreadRunnableLogger implements Runnable {

	private BaseJsonVO vo;

	private HashMap<String, String> logger;

	private String[] name;

	private String[] showName;

	public ThreadRunnableLogger(BaseJsonVO vo, String[] name, String[] showName, HashMap<String, String> logger) {
		this.vo = vo;
		this.logger = logger;
		this.name = name;
		this.showName = showName;
	}

	@Override
	public void run() {
		String result = JsonUtils.toJson(vo);
		if (name != null && name.length > 0 && name.length == showName.length) {
			for (int i = 0; i < name.length; i++) {
				String regex = "\"" + name[i] + "\":\"?([^,\"]+)";

				Pattern pattern = Pattern.compile(regex);
				String results = "";
				Matcher m = pattern.matcher(result);
				while (m.find()) {
					results = results + m.group(1) + "&";
				}
				if (results.endsWith("&"))
					results = results.substring(0, results.length() - 1);

				logger.put(showName[i], results);
			}
		}
		//CommonLogger.printLog(logger);
	}

}
