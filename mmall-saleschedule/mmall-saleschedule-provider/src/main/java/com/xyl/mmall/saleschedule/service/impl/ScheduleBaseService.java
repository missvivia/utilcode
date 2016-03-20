package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleBannerDao;
import com.xyl.mmall.saleschedule.dao.ScheduleChannelDao;
import com.xyl.mmall.saleschedule.dao.ScheduleDao;
import com.xyl.mmall.saleschedule.dao.ScheduleDaoYouhua;
import com.xyl.mmall.saleschedule.dao.ScheduleLikeDao;
import com.xyl.mmall.saleschedule.dao.ScheduleMagicCubeDao;
import com.xyl.mmall.saleschedule.dao.SchedulePageDao;
import com.xyl.mmall.saleschedule.dao.ScheduleSiteRelaDao;
import com.xyl.mmall.saleschedule.dao.ScheduleViceDao;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.dto.ScheduleCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleBaseService {

	@Autowired
	protected ScheduleDao scheduleDao;

	@Autowired
	protected ScheduleChannelDao channelDao;

	@Autowired
	protected SchedulePageDao pageDao;

	@Autowired
	protected ScheduleBannerDao bannerDao;

	@Autowired
	protected ScheduleLikeDao likeDao;

	@Autowired
	protected ScheduleMagicCubeDao magicCubeDao;

	@Autowired
	protected ScheduleViceDao viceDao;

	@Autowired
	protected ScheduleSiteRelaDao siteRelaDao;
	
	@Autowired
	protected ScheduleDaoYouhua youhuaDao;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected int adjustCurPage(int curPage) {
		// return curPage + 1;
		return curPage;
	}

	protected Calendar getTodayLastTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c;
	}

	protected Calendar getTodayBeginTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

	protected void filterForSaleSite(POListDTO oldPOList, long saleSiteFlag) {
		List<PODTO> filteredPOList = new ArrayList<PODTO>();
		for (PODTO poDTO : oldPOList.getPoList()) {
			Schedule s = poDTO.getScheduleDTO().getSchedule();
			if ((s.getSaleSiteFlag() & saleSiteFlag) == saleSiteFlag) {
				filteredPOList.add(poDTO);
			}
		}

		oldPOList.setPoList(filteredPOList);
	}

	/**
	 * 
	 * 
	 * @param poList
	 * @param offset
	 * @param limit
	 */
	protected void POListPager(POListDTO poList, int offset, int limit) {
		if (offset < 0) {
			offset = 0;
		}
		if (limit < 0) {
			limit = 0;
		}

		poList.setTotal(poList.getPoList().size());
		poList.setHasNext(hasNext(poList.getTotal(), offset, limit));

//		if (limit != 0) {
//			List<PODTO> filteredPOList = new ArrayList<PODTO>();
//			for (int i = offset; i < (offset + limit); i++) {
//				if (i < poList.getPoList().size()) {
//					filteredPOList.add(poList.getPoList().get(i));
//				}
//			}
//			poList.setPoList(filteredPOList);
//		}
	}

	private boolean hasNext(int total, int curPage, int pageSize) {
		if (curPage != 0 && pageSize != 0) {
			return curPage * pageSize < total;
		}
		return false;
	}

	protected void setSiteFlagForParamDTO(ScheduleCommonParamDTO paramDTO) {
		try {
			if (paramDTO.allowedAreaIdList != null && paramDTO.allowedAreaIdList.size() > 0) {
				paramDTO.allowedAreaIdListFlag = ProvinceCodeMapUtil.getProvinceFmtByCodeList(paramDTO.allowedAreaIdList);	
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			paramDTO.allowedAreaIdListFlag = -1;
		}

		if (paramDTO.curSupplierAreaId != 0) {
			try {
				paramDTO.saleSiteFlag = ProvinceCodeMapUtil.getProvinceFmtByCode(paramDTO.curSupplierAreaId);
			} catch (Exception e) {
				paramDTO.saleSiteFlag = -1;
			}
		}
	}
	
	protected void batchSetShowOrder(List<PODTO> poList, long saleSiteFlag) {
		try {
			List<Long> saleSiteCodeList = ProvinceCodeMapUtil.getCodeListByProvinceFmt(saleSiteFlag);
			for (PODTO poDTO : poList) {
				Schedule po = poDTO.getScheduleDTO().getSchedule();
				int showOrder = siteRelaDao.getScheduleShowOrder(saleSiteCodeList.get(0), po.getId());
				po.setShowOrder(showOrder);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
