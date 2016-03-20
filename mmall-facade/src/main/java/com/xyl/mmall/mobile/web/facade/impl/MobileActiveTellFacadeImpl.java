package com.xyl.mmall.mobile.web.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.mobile.web.facade.MobileActiveTellFacade;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ActiveTellDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;
import com.xyl.mmall.saleschedule.service.ActiveTellService;

@Facade("mobileActiveTellFacade")
public class MobileActiveTellFacadeImpl implements MobileActiveTellFacade{

	@Autowired
	private ActiveTellService activeTellService;
	
	@Override
	public ActiveTellDTO saveActiveTell(ActiveTellDTO activeTellDTO) {
		return activeTellService.saveActiveTell(activeTellDTO);
	}

	@Override
	public List<ActiveTell> getActiveTellByParam(ActiveTellCommonParamDTO paramSo) {
		return activeTellService.getActiveTellByParam(paramSo);
	}

	@Override
	public boolean removeActiveTell(List<ActiveTell> activeTellList) {
		return activeTellService.removeActiveTell(activeTellList);
	}

}
