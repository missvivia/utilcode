package com.xyl.mmall.cart.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.backend.facade.CartDeleteCacheFacade;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;

@Service
@JobPath("/cart/delete")
public class CartDeleteCacheJob extends BaseJob{
	
	private static final Logger logger=LoggerFactory.getLogger(CartDeleteCacheJob.class);
	
	@Autowired
	private CartDeleteCacheFacade cartDeleteCacheFacade;
	
	@Autowired
	private BusinessFacade businessFacade;

	@Override
	public boolean execute(JobParam param) {
		logger.info("try to CartDeleteCacheJob");
		List<AreaDTO> areaList=businessFacade.getAreaList();
		if(areaList==null || areaList.isEmpty()){
			logger.warn("no area found");
			return true;
		}
		
		boolean allSuccessFlag=true;
		for(AreaDTO area:areaList){
			if(!this.processOneArea((int)area.getId())){
				allSuccessFlag=false;
			}
		}
		
		logger.info("end CartDeleteCacheJob");
		return allSuccessFlag;
	}
	
	private boolean processOneArea(int areaId){
		if(!cartDeleteCacheFacade.deleteCartItemShouldRemove(areaId, null)){
			logger.error("fail delete area:"+areaId);
			return false;
		}
		return true;
	}

}
