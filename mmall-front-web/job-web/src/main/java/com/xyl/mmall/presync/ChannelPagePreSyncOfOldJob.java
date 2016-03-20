package com.xyl.mmall.presync;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 频道页旧cache块预热
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Service
@JobPath("/presync/channelpage/old")
public class ChannelPagePreSyncOfOldJob extends BasePreSyncJob {

	private static final String channel_page_url = ResourceTextUtil.getTextFromResourceByKey(presyncBundle,
			"channel.old.url");

	private static final String AREA_PARAM_NAME = "curSupplierAreaId";

	@Autowired
	private BusinessFacade businessFacade;

	private static final Logger logger = LoggerFactory.getLogger(ChannelPagePreSyncOfOldJob.class);

	private static final int ADDRESS_TYPE = 2;

	private static final int GENTLEMEN_TYPE = 3;

	private static final int KIDSWEAR_TYPE = 5;

	private static final String channelTypeParamName = "channelType";

	private static final String areaParamName = "curSupplierAreaId";

	@Override
	protected void fillBizDesc() {
		super.setBizDesc("频道页旧cache预热，为当天档期开始之前的旧cache块预热数据");
	}

	@Override
	public boolean execute(JobParam param) {
		logger.info("===begin presync channel page old");
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.size() <= 0) {
			logger.error("cannot find area when presync channel old page");
			return false;
		}

		// 女装
		boolean addressFlag = this.invokeByChannelType(ADDRESS_TYPE, areaList);

		// 男装
		boolean memFlag = this.invokeByChannelType(GENTLEMEN_TYPE, areaList);

		// 童装
		boolean kidFlag = this.invokeByChannelType(KIDSWEAR_TYPE, areaList);

		logger.info("===end presync channel page old");

		return addressFlag && memFlag && kidFlag;
	}

	private boolean invokeByChannelType(int channelType, List<AreaDTO> areaList) {
		boolean successFlag = true;
		for (AreaDTO areaDTO : areaList) {
			String url = channel_page_url + "?" + AREA_PARAM_NAME + "=" + areaDTO.getId() + "&" + channelTypeParamName
					+ "=" + channelType;
			if (!super.invokeUrl(url, 30)) {
				successFlag = false;
				logger.error("===presync channel page old for area:" + areaDTO.getId() + " error");
			}
		}
		return successFlag;
	}

}
