package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;

/**
 * 
 * @author hzzhanghui
 *
 */
public final class ScheduleBackendUtil {
	
	public static List<Long> getPOSaleSiteCodeList(List<ScheduleSiteRela> siteRelaList) {
		List<Long> siteFlagCodeList = new ArrayList<Long>();
		for (ScheduleSiteRela siteRela : siteRelaList) {
			siteFlagCodeList.add(siteRela.getSaleSiteId());
		}
		
		return siteFlagCodeList;
	}
	
	public static int getMaxShowOrder(List<ScheduleSiteRela> dbSiteRelaList) {
		int max = 0;
		if (dbSiteRelaList == null || dbSiteRelaList.size() == 0) {
			return max;
		}
		
		for (ScheduleSiteRela siteRela : dbSiteRelaList) {
			if (max < siteRela.getShowOrder()) {
				max = siteRela.getShowOrder();
			}
		}
		
		return max;
	}
}


