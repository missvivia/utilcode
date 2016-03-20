package com.xyl.mmall.presync;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 首页旧cache预热
 * @author hzzhaozhenzuo
 *
 */
@Service
@JobPath("/presync/indexpage/old")
public class IndexPagePreSyncOfOldJob extends BasePreSyncJob {

	private static final String index_page_url = ResourceTextUtil.getTextFromResourceByKey(presyncBundle, "index.old.url");
	
	private static final String AREA_PARAM_NAME="curSupplierAreaId";
	
	@Autowired
	private BusinessFacade businessFacade;
	
	private static final Logger logger=LoggerFactory.getLogger(IndexPagePreSyncOfOldJob.class);
	
	@Override
	protected void fillBizDesc() {
		super.setBizDesc("首页旧cache预热，为当天档期开始之前的旧cache块预热数据");
	}

	@Override
	public boolean execute(JobParam param) {
		logger.info("===begin presync index page old");
		List<AreaDTO> areaList=businessFacade.getAreaList();
		if(areaList==null || areaList.size()<=0){
			logger.error("cannot find area when presync index old page");
			return false;
		}
		
		boolean successFlag=true;
		for(AreaDTO areaDTO:areaList){
			String url=index_page_url+"?"+AREA_PARAM_NAME+"="+areaDTO.getId();
			if(!super.invokeUrl(url, 30)){
				successFlag=false;
				logger.error("===presync index page old for area:"+areaDTO.getId()+" error");
			}
		}
		logger.info("===end presync index page old");
		return successFlag;
	}

}
