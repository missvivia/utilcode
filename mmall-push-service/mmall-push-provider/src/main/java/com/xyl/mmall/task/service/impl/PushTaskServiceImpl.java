package com.xyl.mmall.task.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.push.service.PushConst;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.task.service.PushTaskService;
import com.xyl.mmall.task.bean.Locker;
import com.xyl.mmall.task.bean.PushSenderConfig;
import com.xyl.mmall.task.bean.PushServiceConfig;
import com.xyl.mmall.task.dao.DeviceLocationDao;
import com.xyl.mmall.task.dao.PushManagementDao;

/**
 * 
 * @author jiangww
 *
 */
@Service("pushTaskService")
public class PushTaskServiceImpl implements PushTaskService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PushManagementDao pushManagementDao;
	@Autowired
	private DeviceLocationDao deviceLocationDao;
	@Autowired
	private PushSenderService  pushSenderService;
	@Autowired
	private PushServiceConfig pushServiceConfig;
	
	private UserCopyerService  ucs ;
	private Timer timer  = new Timer();
	@Override
	public boolean push(long start,long end) {
		//初始化线程
		synchronized (Locker.THREAD_LOCK) {
			if(ucs == null && !UserCopyerService.started){
				ucs = new UserCopyerService(deviceLocationDao,pushServiceConfig);
				ucs.start();
			}
		}
		logger.info("task start at " + System.currentTimeMillis());
		List<PushManagement> tasks = pushManagementDao.getPushTaskList(start, end);
		for(PushManagement task: tasks){
			genTask(task);
		}
		return true;
	}
	
	
	private void genTask(PushManagement task){
		PushSenderConfig pushSenderConfig = new PushSenderConfig();
		if(StringUtils.isBlank(task.getAreaCode())){
			//宁可不发，不可错发;
			return;
		}
		
		String areas[]  = task.getAreaCode().split(",");
		/*
		 * 这里可以先不做驱动的判断，作为一个优化
		if(StringUtils.isNotBlank(task.getPlatformType())){
			map.put("platformType", task.getPlatformType().split(","));
		}
		*/	
		String platform = task.getPlatformType();
		platform = platform.toLowerCase();
		if(StringUtils.isBlank(platform)){
			return;
		}
		if(platform.startsWith(","))
			platform = platform.substring(1);
		String platforms[] =  platform.split(",");
		//本版本的临时优化,本版本只允许２种
		if(platforms.length >= 2){
			platform = PushConst.ALL_PLATFORM;
		}else{
			logger.info("platform type:"+ platform);
			platform = platform.replace(",", "");		
		}
		
		pushSenderConfig.setTaskId(task.getId());
		pushSenderConfig.setPlatform(platform);
		pushSenderConfig.setAlert(task.getContent());
		pushSenderConfig.setSummary(task.getLink());
		pushSenderConfig.setContent(task.getTitle());
		pushSenderConfig.setTitle("有新消息");
		//test
		logger.info("send msg:"+ JsonUtils.toJson(pushSenderConfig));
		PushTask pushTask = new PushTask(this, pushSenderConfig, areas);
		timer.schedule(pushTask, new Date(task.getPushTime()));
	}


	public PushManagementDao getPushManagementDao() {
		return pushManagementDao;
	}


	public void setPushManagementDao(PushManagementDao pushManagementDao) {
		this.pushManagementDao = pushManagementDao;
	}


	public DeviceLocationDao getDeviceLocationDao() {
		return deviceLocationDao;
	}


	public void setDeviceLocationDao(DeviceLocationDao deviceLocationDao) {
		this.deviceLocationDao = deviceLocationDao;
	}


	public PushSenderService getPushSenderService() {
		return pushSenderService;
	}


	public void setPushSenderService(PushSenderService pushSenderService) {
		this.pushSenderService = pushSenderService;
	}


	public PushServiceConfig getPushServiceConfig() {
		return pushServiceConfig;
	}


	public void setPushServiceConfig(PushServiceConfig pushServiceConfig) {
		this.pushServiceConfig = pushServiceConfig;
	}
	

}
