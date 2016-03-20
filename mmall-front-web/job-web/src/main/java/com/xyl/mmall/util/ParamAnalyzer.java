package com.xyl.mmall.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将job平台传过来的参数进行处理
 * 
 * @author hzzhaozhenzuo
 *
 */
public class ParamAnalyzer {

	private static final Logger logger = LoggerFactory.getLogger(ParamAnalyzer.class);

	private static final String TIME_PARAM_SPLIT = "\\@";

	private static final String TIME_PARAM_NOT_CAL = "*";

	private static final Pattern pattern = java.util.regex.Pattern.compile("-?[0-9]*");

	/**
	 * 解析timeParamStr表达式, 表达式采用 【年月 日 小时 分钟 秒】的形式
	 * <p>
	 * 例: [* * -1 * * *] 表示取时间timestamp前一天
	 * <p>
	 * 如timestamp为2014-11-3 10:00:00，则结果为:2014-11-2 10:00:00
	 * 
	 * @param timestamp
	 *            job产生时的时间
	 * @param timeParamStr
	 *            时间表达式
	 * @return
	 */
	public static Date getDateByAnalyzeTimeParam(long timestamp, String timeParamStr) {
		if (timestamp <= 0 || StringUtils.isEmpty(timeParamStr)) {
			logger.warn("params error,timestamp:" + timestamp + ",timeParamStr:" + timeParamStr);
			return null;
		}

		String[] params = timeParamStr.split(TIME_PARAM_SPLIT);
		if (params == null || params.length != 6) {
			logger.error("timeParamStr is not eq 6");
			return null;
		}

		String year = params[0];
		String month = params[1];
		String day = params[2];
		String hour = params[3];
		String minute = params[4];
		String second = params[5];

		int yearNum = getAddNumForField(year);
		int monthNum = getAddNumForField(month);
		int dayNum = getAddNumForField(day);
		int hourNum = getAddNumForField(hour);
		int minuteNum = getAddNumForField(minute);
		int secondNum = getAddNumForField(second);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		calendar.add(Calendar.YEAR, yearNum);
		calendar.add(Calendar.MONTH, monthNum);
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		calendar.add(Calendar.HOUR_OF_DAY, hourNum);
		calendar.add(Calendar.MINUTE, minuteNum);
		calendar.add(Calendar.SECOND, secondNum);

		return calendar.getTime();
	}

	private static int getAddNumForField(String param) {
		if (!check(param)) {
			logger.error("time param error,param:" + param);
			throw new RuntimeException("time param error,param:" + param);
		}
		if (TIME_PARAM_NOT_CAL.equals(param)) {
			return 0;
		}
		return Integer.valueOf(param);
	}

	private static boolean check(String param) {
		if (TIME_PARAM_NOT_CAL.equals(param)) {
			return true;
		}
		Matcher match = pattern.matcher(param);
		return match.matches();
	}

}
