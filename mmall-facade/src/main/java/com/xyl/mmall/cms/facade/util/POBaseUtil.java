package com.xyl.mmall.cms.facade.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.AbstractEnumInterface;
import com.xyl.mmall.mainsite.util.QrqmUtils.UserLoginBean;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * 
 * @author hzzhanghui
 * 
 */
public final class POBaseUtil {

	private static final Logger logger = LoggerFactory.getLogger(POBaseUtil.class);

	private POBaseUtil() {

	}

	public static final String NULL_STR = "";

	public static final List<String> specialLongFieldsList = new ArrayList<String>();

	public static Map<Long, List<Long>> todayDyingPOSiteMap = new ConcurrentHashMap<Long, List<Long>>();

	static {
		specialLongFieldsList.add("id");
		specialLongFieldsList.add("userId");
		specialLongFieldsList.add("curSupplierAreaId");
		specialLongFieldsList.add("supplierId");
		specialLongFieldsList.add("brandId");
		specialLongFieldsList.add("saleAreaId");
		specialLongFieldsList.add("storeAreaId");
		specialLongFieldsList.add("pageId");
		specialLongFieldsList.add("bannerId");
		specialLongFieldsList.add("scheduleId");
		specialLongFieldsList.add("homeBannerImgId");
		specialLongFieldsList.add("preBannerImgId");
		specialLongFieldsList.add("auditUserId");
		specialLongFieldsList.add("iconId");
		specialLongFieldsList.add("bgImgId");
		specialLongFieldsList.add("headerImgId");
		specialLongFieldsList.add("imgId");
	}

	@SuppressWarnings("rawtypes")
	private static boolean isAbstractEnumInterface(Class clz) {
		Class[] interfaces = clz.getInterfaces();
		for (Class item : interfaces) {
			if (item == AbstractEnumInterface.class) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Convert a java object to JSONObject
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static JSONObject toJSON(Object obj) {
		JSONObject result = new JSONObject();

		if (obj == null) {
			return result;
		}
		try {
			Class<?> clz = obj.getClass();
			List<FieldInfo> getters = TypeUtils.computeGetters(clz, null);
			for (FieldInfo field : getters) {
				String fieldName = field.getName();
				Class<?> fieldClz = field.getFieldClass();
				if (fieldClz == long.class) {
					if (specialLongFieldsList.contains(fieldName)) {
						result.put(fieldName, field.getField().getLong(obj) + NULL_STR);
					} else {
						result.put(field.getName(), field.getField().get(obj));
					}
					continue;
				}
				if (isAbstractEnumInterface(fieldClz)) {
					AbstractEnumInterface valObj = ((AbstractEnumInterface) (field.getField().get(obj)));
					if (valObj != null) {
						int val = valObj.getIntValue();
						result.put(field.getName(), val);
					} else {
						result.put(field.getName(), -1);
					}
					continue;
				}

				result.put(field.getName(), field.getField().get(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	/**
	 * 
	 * 
	 * @param schedule
	 * @return
	 */
	private static boolean isPOValid(Schedule schedule) {

		// 1. first to calculate the new endTime of PO
		calPOQrqmEndTime(schedule);

		// 2. second check whether the PO is a valid PO ('Valid' means we can
		// buy products from it)
		long now = System.currentTimeMillis();
		final int P = ScheduleUtil.PO_NORMAL_DURATION; // PO normal show period
		final int T = ScheduleUtil.PO_FINAL_SEE_DURATION; // PO final see period

		if (schedule.getEndTime() < now) { // PO offline
			return false;
		}

		if (schedule.getStartTime() > now) { // PO not start
			return false;
		}

		if (ScheduleUtil.daysBetween(schedule.getStartTime(), now) > (P + T)) {
			return false;
		}

		return true;
	}

	public static void calPOQrqmEndTimeForMobile(Schedule schedule, long lastLogin, List<Long> dyingPOIdList) {
//		UserLoginBean loginBean = new UserLoginBean();
//		loginBean.lastLogin = lastLogin;
//		loginBean.dyingPOIdList = dyingPOIdList;
//		_calPOQrqmEndTime(schedule, loginBean);
	}

	/**
	 * 
	 */
	public static void calPOQrqmEndTime(Schedule schedule) {
		
		//_calPOQrqmEndTime(schedule, null);
	}

	private static void _calPOQrqmEndTime(Schedule schedule, UserLoginBean loginBean) {
//		// 1. first to calculate the new endTime of PO
//		ScheduleUtil.setPOShowTime(schedule);
//		long now = System.currentTimeMillis();
////		final int P = ScheduleUtil.PO_NORMAL_DURATION; // PO normal show period
////		final int T = ScheduleUtil.PO_FINAL_SEE_DURATION; // PO final see period
//		int P = schedule.getNormalShowPeriod();
//		int T = schedule.getExtShowPeriod();
//		long poStartTime = schedule.getStartTime();
//		int diff = ScheduleUtil.daysBetween(poStartTime, now);
//
//		UserLoginBean userLoginBean = loginBean;
//		if (userLoginBean == null) {
//			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//					.getRequest();
//			userLoginBean = QrqmUtils.getCurIPLastLoginTimeWithoutResp(request);
//		}
//
//		if (schedule.getEndTime() < now) { // PO offline
//			if (userLoginBean.lastLogin != -1 && ScheduleUtil.daysBetween(poStartTime, userLoginBean.lastLogin) <= P) {
//				schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P - 1));
//			}
//			return;
//		}
//
//		if (schedule.getStartTime() > now) { // PO not start
//			return;
//		}
//
//		if (userLoginBean.lastLogin == -1) {
//			if (diff <= P) {
//				schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P - 1));
//			} else if (diff <= (P + T)) {
//				schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, (P + T) - 1));
//			} else {
//				schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, (P + T) - 1));
//			}
//			return;
//		} else {
//			if (userLoginBean.dyingPOIdList != null && userLoginBean.dyingPOIdList.contains(schedule.getId())) {
//				if (diff == P) {
//					schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P - 1));
//				} else {
//					schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P - 1));
//				}
//				return;
//			} else {
//				// go following flow
//			}
//		}
//
//		long lastLoginTime = userLoginBean.lastLogin;
//
//		// first login
//		if (lastLoginTime < 0 || ScheduleUtil.daysBetween(lastLoginTime, now) >= ScheduleUtil.PO_MAX_LOGIN_INTERVAL) {
//			// In P period
//			int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//			if (daysBetween <= P) {
//				// calculate newly end time
//				schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P - daysBetween));
//			} else {
//				// Not in P but in T
//				if (daysBetween <= (P + T)) {
//					schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, (P + T) - daysBetween));
//				} else {
//					// excluded
//					schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, (P + T) - 1));
//				}
//			}
//			return;
//		} else { // not first login
//			long L1 = lastLoginTime; // user last login time
//			long L2 = now; // user now login time
//			if (lastLoginTime < 0) {
//				L1 = now;
//			}
//
//			if (L1 < poStartTime) { // new online PO
//				if (ScheduleUtil.daysBetween(poStartTime, L2) <= (P + T)) {
//					// for newly online PO, L2 can see it in its (P+T) period
//
//					if (ScheduleUtil.isInSameDay(L1, poStartTime) && (ScheduleUtil.daysBetween(poStartTime, L2) > P)) {
//						// special case
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P - 1));
//						return;
//					}
//
//					int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//					if (daysBetween < 0) {
//						daysBetween = ScheduleUtil.daysBetween(now, poStartTime);
//					}
//					if (ScheduleUtil.daysBetween(poStartTime, L2) <= P) {
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P - daysBetween));
//					} else {
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P + T - daysBetween));
//					}
//				} else {
//					// This login will be the first login. so flow will not
//					// come here
//				}
//			} else {
//				// L1 in P period
//				if (ScheduleUtil.daysBetween(poStartTime, L1) <= P) {
//					// L2 also in P
//					if (ScheduleUtil.daysBetween(poStartTime, L2) <= P) {
//						int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P - daysBetween));
//					} else {
//						// L2 has seen PO in P, cannot see such PO in its T
//						// period
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P - 1));
//					}
//					return;
//				} else { // L1 not in P, but L1 must in T, or it should be
//							// user first login
//
//					int diff2 = ScheduleUtil.daysBetween(poStartTime, now);
//					if (diff2 > (P + T)) { // overflow longest period
//						// user has seen PO in T period, and the T has
//						// passed, so not show again
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(poStartTime, P + T - 1));
//					} else {
//						int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, (P + T) - daysBetween));
//					}
//				}
//			}
//		}
	}

	/**
	 * Filter different PO list for different user.
	 * 
	 * How to use:
	 *   POListDTO poList = ...;  // get PO list
	 *   // you need to get HttpServletRequest and HttpServletResponse object
	 *   UserLoginBean userLoginBean = QrqmUtils.getCurIPLastLoginTime(request, response);
	 *   poList = POBaseUtil.filterForEveryOne(userLoginBean, poList);
	 *   // after call this method, plz call following:
	 *   QrqmUtils.writeDyingPOListToCookie(userLoginBean);
	 * @param userId
	 * @param poList
	 * @return
	 */
	public static POListDTO filterForEveryOne(UserLoginBean userLoginBean, POListDTO poList) {
		return poList;
//		logger.info("userLoginBean: " + userLoginBean);
//		List<PODTO> filteredPOList = new ArrayList<PODTO>();
//
//		int P = ScheduleUtil.PO_NORMAL_DURATION; // PO normal show period
//		int T = ScheduleUtil.PO_FINAL_SEE_DURATION; // PO final see period
//		// final int TT = ScheduleUtil.PO_EXTENSION_DURATION; // PO extension
//
//		long now = System.currentTimeMillis();
//
//		long lastLoginTime = userLoginBean.lastLogin;
//		for (PODTO po : poList.getPoList()) {
//			Schedule schedule = po.getScheduleDTO().getSchedule();
//			
//			// new requirement
//			P = schedule.getNormalShowPeriod();
//			T = schedule.getExtShowPeriod();
//			
//			ScheduleUtil.setPOShowTime(schedule);
//			long poStartTime = schedule.getStartTime();
//			long poEndTime = schedule.getEndTime();
//
//			// 1. deal with last dying PO list
//			int diff = ScheduleUtil.daysBetween(poStartTime, now);
//			Long poId = schedule.getId();
//			List<Long> deadPOIdList = new ArrayList<Long>();
//			if (userLoginBean.dyingPOIdList != null && userLoginBean.dyingPOIdList.size() != 0) {
//				if (userLoginBean.dyingPOIdList.contains(poId)) {
//					if (diff > P && diff <= (P + T)) {
//						// System.out.println("Won't show PO " + poId);
//						continue;
//					} else if (diff > (P + T)) {
//						deadPOIdList.add(poId);
//					}
//				}
//			}
//			if (deadPOIdList.size() > 0) { // remove old dead PO id list
//				userLoginBean.dyingPOIdList.removeAll(deadPOIdList);
//			}
//
//			// 2. add new dying PO id
//			if (diff == P) {
//				if (userLoginBean.dyingPOIdList == null) {
//					userLoginBean.dyingPOIdList = new ArrayList<Long>();
//				}
//				if (!userLoginBean.dyingPOIdList.contains(poId)) {
//					userLoginBean.dyingPOIdList.add(poId);
//				}
//			}
//
//			if (poEndTime < now) { // PO offline
//				continue;
//			}
//
//			if (poStartTime > now) { // PO not start
//				continue;
//			}
//
//			if (ScheduleUtil.daysBetween(poStartTime, now) > (P + T)) {
//				continue;
//			}
//
//			// first login
//			if (lastLoginTime < 0 || ScheduleUtil.daysBetween(lastLoginTime, now) >= ScheduleUtil.PO_MAX_LOGIN_INTERVAL) {
//				// In P period
//				int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//				if (daysBetween <= P) {
//					// calculate newly end time
//					schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P - daysBetween));
//					filteredPOList.add(po);
//				} else {
//					// Not in P but in T
//					if (daysBetween <= (P + T)) {
//						schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, (P + T) - daysBetween));
//						filteredPOList.add(po);
//					} else {
//						// excluded
//					}
//				}
//			} else { // not first login
//				long L1 = lastLoginTime; // user last login time
//				long L2 = now; // user now login time
//				if (lastLoginTime < 0) {
//					L1 = now;
//				}
//
//				if (L1 < poStartTime) { // new online PO
//					// if (L1 <
//					// ScheduleUtil.getSpecificBeginTime(poStartTime).getTimeInMillis())
//					// {
//					if (ScheduleUtil.daysBetween(poStartTime, L2) <= (P + T)) {
//						// for newly online PO, L2 can see it in its (P+T)
//						// period
//
//						if (ScheduleUtil.isInSameDay(L1, poStartTime)
//								&& (ScheduleUtil.daysBetween(poStartTime, L2) > P)) {
//							// special case
//							continue;
//						}
//
//						int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//						if (daysBetween < 0) {
//							daysBetween = ScheduleUtil.daysBetween(now, poStartTime);
//						}
//						if (ScheduleUtil.daysBetween(poStartTime, L2) <= P) {
//							schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P - daysBetween));
//						} else {
//							schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P + T - daysBetween));
//						}
//
//						filteredPOList.add(po);
//					} else {
//						// This login will be the first login. so flow will not
//						// come here
//					}
//				} else {
//					// L1 in P period
//					if (ScheduleUtil.daysBetween(poStartTime, L1) <= P) {
//						// L2 also in P
//						if (ScheduleUtil.daysBetween(poStartTime, L2) <= P) {
//							int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//							schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, P - daysBetween));
//							filteredPOList.add(po);
//						} else {
//							// L2 has seen PO in P, cannot see such PO in its T
//							// period
//						}
//					} else { // L1 not in P, but L1 must in T, or it should be
//								// user first login
//
//						int diff2 = ScheduleUtil.daysBetween(poStartTime, now);
//						if (diff2 > (P + T)) { // overflow longest period
//							// user has seen PO in T period, and the T has
//							// passed, so not show again
//						} else {
//							int daysBetween = ScheduleUtil.daysBetween(poStartTime, now);
//							schedule.setEndTime(ScheduleUtil.calculatePONewEndTime(now, (P + T) - daysBetween));
//							filteredPOList.add(po);
//						}
//						// if (diff > TT) {
//						// // user has seen PO in T period, and the T has
//						// // passed, so not show again
//						// } else {
//						// int daysBetween =
//						// ScheduleUtil.daysBetween(poStartTime, now);
//						// schedule.setEndTime(ScheduleUtil.calculatePONewEndTime((P+T+TT-1)-daysBetween));
//						// filteredPOList.add(po);
//						// }
//					}
//				}
//			}
//		}
//
//		POListDTO result = new POListDTO();
//		result.setPoList(filteredPOList);
//		return result;
	}

	public static boolean isInteger(String s) {
		try {
			if ((s != null) && (s != "")) {
				return s.matches("^[0-9]*$");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDecimalOrInteger(String str) {
		try {
			return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static RetArg getOnlineScheduleSizeAndNextPoTime(List<ScheduleDTO> dtoList) {
		int onlineSize = 0;
		long nextPoTime = 0;
		long nextPoEndTime = 0;
		RetArg retArg = new RetArg();
		long current = System.currentTimeMillis();
		for (ScheduleDTO dto : dtoList) {
			long startTime = dto.getSchedule().getStartTime();
			long endTime = dto.getSchedule().getEndTime();
			if (startTime <= current && endTime >= current) {
				onlineSize++;
			} else if (startTime > current) {
				if (nextPoTime == 0) {
					nextPoTime = startTime;
					nextPoEndTime = endTime;
				} else if (nextPoTime > startTime) {
					nextPoTime = startTime;
					nextPoEndTime = endTime;
				}
			}
		}
		RetArgUtil.put(retArg, onlineSize);
		if (onlineSize == 0 && nextPoTime - current <= 4 * 24 * 3600 * 1000L) {
			RetArgUtil.put(retArg, nextPoTime, 0);
			RetArgUtil.put(retArg, nextPoEndTime, 1);
		} else {
			RetArgUtil.put(retArg, 0L, 0);
			RetArgUtil.put(retArg, 0L, 1);
		}
		return retArg;
	}
}
