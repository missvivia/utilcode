package com.xyl.mmall.oms.util;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import com.netease.dbsupport.DBResource;
import com.netease.print.common.constant.CalendarConst;
import com.netease.print.common.util.CalendarUtil;
import com.xyl.mmall.oms.enums.BociTime;
import com.xyl.mmall.oms.enums.BociType;
import com.xyl.mmall.oms.enums.PickMoldType;
import com.xyl.mmall.oms.meta.BociInfo;
import com.xyl.mmall.oms.meta.PickSkuItemForm;

/**
 * @author zb
 *
 */
public class OmsUtil {

	public static long getTimeOutTime() {
		Calendar calendar = CalendarUtil.getZeroHour(System.currentTimeMillis());
		calendar.add(Calendar.DAY_OF_MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, BociTime.THIRD_BATCH.getIntValue());
		return calendar.getTimeInMillis();
	}

	public static boolean isPicked(String pickOrderId) {
		if (pickOrderId.equals("0"))
			return false;
		else
			return true;
	}

	public static BociType getBociFromPickSkuItem(PickSkuItemForm pickSku) {
		Date currDate = new Date(pickSku.getCreateTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= BociTime.THIRD_BATCH.getIntValue() || hour <= BociTime.FIRST_BATCH.getIntValue()) {
			if (pickSku.getPickMoldType() == PickMoldType.SINGLE)
				return BociType.FIRST_SINGLE;
			else
				return BociType.FIRST_DOUBLE;
		} else if (hour > BociTime.FIRST_BATCH.getIntValue() && hour <= BociTime.SECOND_BATCH.getIntValue()) {
			if (pickSku.getPickMoldType() == PickMoldType.SINGLE)
				return BociType.SECOND_SINGLE;
			else
				return BociType.SECOND_DOUBLE;
		} else if (hour > BociTime.SECOND_BATCH.getIntValue() && hour <= BociTime.THIRD_BATCH.getIntValue()) {
			if (pickSku.getPickMoldType() == PickMoldType.SINGLE)
				return BociType.THIRD_SINGLE;
			else
				return BociType.THIRD_DOUBLE;
		}
		return null;
	}

	/**
	 * 根据当前时间获取批次号
	 * 
	 * @return
	 */
	public static int getCurrBoci(long time) {
		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= BociTime.THIRD_BATCH.getIntValue() || hour <= BociTime.FIRST_BATCH.getIntValue())
			return 1;
		else if (hour > BociTime.FIRST_BATCH.getIntValue() && hour <= BociTime.SECOND_BATCH.getIntValue())
			return 2;
		else if (hour > BociTime.SECOND_BATCH.getIntValue() && hour <= BociTime.THIRD_BATCH.getIntValue())
			return 3;
		return 0;
	}

	public static BociInfo getCurrentBoci() {
		return getBoci(System.currentTimeMillis(), null);
	}

	public static BociInfo getBoci(long time, PickMoldType type) {
		Calendar zeroDayTime = CalendarUtil.getZeroHour(time);
		long zeroDayTimeLong = zeroDayTime.getTimeInMillis();
		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		BociTime bociTime = null;
		BociType bociType = null;
		long startTime = 0L;
		long deadLine = 0L;
		int bociInt = 0;
		if (hour < BociTime.FIRST_BATCH.getIntValue()) {
			bociTime = BociTime.FIRST_BATCH;
			startTime = zeroDayTimeLong - ((long) (24 - BociTime.THIRD_BATCH.getIntValue())) * CalendarConst.HOUR_TIME;
			deadLine = zeroDayTimeLong + ((long) bociTime.getIntValue() * CalendarConst.HOUR_TIME) - 1L;
			if (type != null) {
				if (type == PickMoldType.SINGLE)
					bociType = BociType.FIRST_SINGLE;
				else
					bociType = BociType.FIRST_DOUBLE;
			}
			bociInt = 1;
		} else if (hour >= BociTime.FIRST_BATCH.getIntValue() && hour < BociTime.SECOND_BATCH.getIntValue()) {
			bociTime = BociTime.SECOND_BATCH;
			startTime = zeroDayTimeLong + ((long) BociTime.FIRST_BATCH.getIntValue() * CalendarConst.HOUR_TIME);
			deadLine = zeroDayTimeLong + ((long) bociTime.getIntValue() * CalendarConst.HOUR_TIME) - 1L;
			if (type != null) {
				if (type == PickMoldType.SINGLE)
					bociType = BociType.SECOND_SINGLE;
				else
					bociType = BociType.SECOND_DOUBLE;
			}
			bociInt = 2;
		} else if (hour >= BociTime.SECOND_BATCH.getIntValue() && hour < BociTime.THIRD_BATCH.getIntValue()) {
			bociTime = BociTime.THIRD_BATCH;
			startTime = zeroDayTimeLong + ((long) BociTime.SECOND_BATCH.getIntValue() * CalendarConst.HOUR_TIME);
			deadLine = zeroDayTimeLong + ((long) bociTime.getIntValue() * CalendarConst.HOUR_TIME) - 1L;
			if (type != null) {
				if (type == PickMoldType.SINGLE)
					bociType = BociType.THIRD_SINGLE;
				else
					bociType = BociType.THIRD_DOUBLE;
			}
			bociInt = 3;
		} else if (hour >= BociTime.THIRD_BATCH.getIntValue()) {
			bociTime = BociTime.FIRST_BATCH;
			// 最后一个拨次之后的，推倒次日的第一个拨次
			startTime = zeroDayTimeLong + ((long) BociTime.THIRD_BATCH.getIntValue() * CalendarConst.HOUR_TIME);
			deadLine = zeroDayTimeLong + ((long) (BociTime.FIRST_BATCH.getIntValue() + 24) * CalendarConst.HOUR_TIME)
					- 1L;
			if (type != null) {
				if (type == PickMoldType.SINGLE)
					bociType = BociType.FIRST_SINGLE;
				else
					bociType = BociType.FIRST_DOUBLE;
			}
			bociInt = 1;
		}
		return new BociInfo(bociTime, startTime, deadLine, bociType, bociInt);
	}
	
	public static String genePageSql(int curPage, int pageSize) {
		StringBuilder sb = new StringBuilder(" ");
		if (curPage < 0) {
			curPage = 0;
		}

		if (pageSize < 0) {
			pageSize = 0;
		}

		if (curPage == 0 && pageSize == 0) {
			return sb.toString();
		}

		sb.append(" limit " + curPage + "," + pageSize);

		return sb.toString();
	}
	
	public static String getCntSql(String sql) {
		String cntSql = " ";
		if (sql.indexOf("limit") != -1) {
			cntSql = "SELECT COUNT(*) CNT " + (sql.substring(sql.indexOf("FROM"), sql.indexOf("limit")));
		}
		return cntSql;
	}
	
	public static boolean hasNext(int total, int curPage, int pageSize) {
		if (curPage != 0 && pageSize != 0) {
			return curPage * pageSize < total;
		}
		return false;
	}
	
	public static void closeDBQuitely(DBResource dbr, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			//
		}

		try {
			if (dbr != null) {
				dbr.close();
			}
		} catch (Exception e) {
			//
		}
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY, 13);
		// cal.set(Calendar.MINUTE, 50);
		System.out.println(new Date(cal.getTimeInMillis()));
		BociInfo info = getBoci(cal.getTimeInMillis(), null);
		System.out.println(info.getBociTime());
		System.out.println(new Date(info.getBociStartTime()));
		System.out.println(new Date(info.getBociDeadLine()));
		System.out.println(info.getBociType());
		System.out.println(info.getBociInt());
	}

}
