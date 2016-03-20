package com.xyl.mmall.mobile.web.facade.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.xyl.mmall.content.meta.LotteryOrderCnt;

/**
 * Lottery.
 * 
 * @author hzzhanghui
 *
 */
public class MobileLotteryUtil {
	private static final MobileLotteryUtil instance = new MobileLotteryUtil();

	private MobileLotteryUtil() {
	}

	public static MobileLotteryUtil getInstance() {
		return instance;
	}
	
	public int doLottery(List<LotteryOrderCnt> orderCntList) {
		Map<String, Integer> orderCntMap = new ConcurrentHashMap<String, Integer>();
		Map<String, String> pdMap = new ConcurrentHashMap<String, String>();
		for (LotteryOrderCnt orderCnt : orderCntList) {
			orderCntMap.put(orderCnt.getLotteryDate() + "", orderCnt.getOrderCnt());
			pdMap.put(orderCnt.getLotteryDate() + "", orderCnt.getPd());
		}
		
		String key = getCurrDateTime();
		int curDateOrderCnt = 0; 
		try {
			curDateOrderCnt = orderCntMap.get(key);
		} catch(Exception e) {
			// it's not time
			return -1;
		}
		String pd = pdMap.get(key);
		String[] dayPD = pd.split(";");
		String[] dayPD2 = dayPD[0].split(",");
		String[] dayPD3 = dayPD[1].split(",");
		String[] dayPD4 = dayPD[2].split(",");
		int secondStart = Integer.parseInt(dayPD2[0]);
		int secondEnd = Integer.parseInt(dayPD2[1]);
		int thirdStart = Integer.parseInt(dayPD3[0]);
		int thirdEnd = Integer.parseInt(dayPD3[1]);
		int fourthStart = Integer.parseInt(dayPD4[0]);
		int fourthEnd = Integer.parseInt(dayPD4[1]);
		
		Random rnd = new Random();
		int base = rnd.nextInt(curDateOrderCnt);
		
		if (base >= secondStart && base <= secondEnd) {
			return 2;
		}
		
		if (base >= thirdStart && base <= thirdEnd) {
			return 3;
		}
		
		if (base >= fourthStart && base <= fourthEnd) {
			return 4;
		}

		return -1;
	}
	
	private String getCurrDateTime(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis() + "";
	}
}