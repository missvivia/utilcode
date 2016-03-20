package com.xyl.mmall.saleschedule.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.saleschedule.dao.ActiveTellDao;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.dto.ActiveTellDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;
import com.xyl.mmall.saleschedule.service.ActiveTellService;

@Service
public class ActiveTellServiceImpl implements ActiveTellService{
	
	@Autowired
	private ActiveTellDao activeTellDao;

	@Override
	public ActiveTellDTO saveActiveTell(ActiveTellDTO activeTellDTO) {
		ActiveTellDTO res=new ActiveTellDTO();
		if(activeTellDTO==null || activeTellDTO.getActiveTell()==null){
			return res;
		}
		res.setActiveTell(activeTellDao.addObject(activeTellDTO.getActiveTell()));
		return res;
	}

	@Override
	public List<ActiveTell> getActiveTellByParam(ActiveTellCommonParamDTO paramSo) {
		return activeTellDao.getActiveTellByParam(paramSo);
	}

	@Override
	public boolean removeActiveTell(List<ActiveTell> activeTellList) {
		if(activeTellList==null || activeTellList.isEmpty()){
			return true;
		}
		
		boolean flag=true;
		for(ActiveTell tell:activeTellList){
			if(!activeTellDao.deleteObjectByKey(tell)){
				flag=false;
			}
		}
		return flag;
	}

}
