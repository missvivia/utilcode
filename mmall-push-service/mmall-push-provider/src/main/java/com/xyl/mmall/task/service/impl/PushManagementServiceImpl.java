package com.xyl.mmall.task.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.task.dto.PushManagementDTO;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.task.service.PushManagementService;
import com.xyl.mmall.task.dao.PushManagementDao;

/**
 * 
 * @author jiangww
 *
 */
@Service("pushManagementService")
public class PushManagementServiceImpl implements PushManagementService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	PushManagementDao pushManagementDao;
	@Override
	public RetArg getPushConfigList(PushManagementDTO pushManagementAo, DDBParam param) {
		if(pushManagementAo == null){
			logger.warn("input push management is Null");
			pushManagementAo = new PushManagementDTO();
		}
		if(param == null){
			logger.warn("input push param is Null");
			param = new DDBParam("pushTime", false, 10, 0);
		}
		
		List<PushManagement> pushManagementList =  pushManagementDao.getPushConfigList(pushManagementAo, param);
		List<PushManagementDTO> dtos = new ArrayList<PushManagementDTO>();
		if(pushManagementList != null)
			for(PushManagement pm : pushManagementList){
				dtos.add(new PushManagementDTO(pm));
			}
		
		RetArg retArg = new RetArg();
		RetArgUtil.put(retArg, dtos);
		RetArgUtil.put(retArg,  param);
		return retArg;
	}

	@Override
	public PushManagementDTO getPushConfigById(long id) {
		return new PushManagementDTO(pushManagementDao.getObjectById(id));
	}

	@Override
	public PushManagementDTO addPushConfig(PushManagement pushManagement) {	
		if(pushManagement == null){
			logger.error("input push param is Null in add");
			throw new ServiceException("add an null PushManagement");
		}
		return new PushManagementDTO(pushManagementDao.addObject(pushManagement));
	}

	@Override
	public boolean deletePushConfigById(long id) {	
		return pushManagementDao.deleteById(id);
	}

	@Override
	public boolean updatePushManagement(long id, PushManagementDTO pushManagementDTO) {
		if(pushManagementDTO == null){
			logger.error("input push param is Null in update");
			throw new ServiceException("update an null PushManagement");
		}		
		return pushManagementDao.updatePushManagement(id,pushManagementDTO);
	}

	

}
