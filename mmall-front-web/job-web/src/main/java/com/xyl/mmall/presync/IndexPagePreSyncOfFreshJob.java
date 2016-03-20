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
 * 首页新cache预热
 * @author hzzhaozhenzuo
 *
 */
@Service
@JobPath("/presync/indexpage/fresh")
public class IndexPagePreSyncOfFreshJob extends BasePreSyncJob {

	private static final String index_page_url = ResourceTextUtil.getTextFromResourceByKey(presyncBundle, "index.fresh.url");
	
	private static final String AREA_PARAM_NAME="curSupplierAreaId";
	
	@Autowired
	private BusinessFacade businessFacade;
	
	private static final Logger logger=LoggerFactory.getLogger(IndexPagePreSyncOfFreshJob.class);
	
	@Override
	protected void fillBizDesc() {
		super.setBizDesc("首页新cache预热，为当天档期开始之后的新cache块预热数据");
	}

	@Override
	public boolean execute(JobParam param) {
		logger.info("===begin presync index page fresh");
		
		List<AreaDTO> areaList=businessFacade.getAreaList();
		if(areaList==null || areaList.size()<=0){
			logger.error("cannot find area when presync index page");
			return false;
		}
		
		boolean successFlag=true;
		for(AreaDTO areaDTO:areaList){
			String url=index_page_url+"?"+AREA_PARAM_NAME+"="+areaDTO.getId();
			if(!super.invokeUrl(url, 30)){
				successFlag=false;
				logger.error("===presync index page fresh for area:"+areaDTO.getId()+" error");
			}
		}
		
		logger.info("===end presync index page fresh");
		return successFlag;
	}

}
