package com.xyl.mmall.task.service.impl;

import java.io.File;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.task.exception.PushException;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.task.bean.Locker;
import com.xyl.mmall.task.bean.PushSenderConfig;
import com.xyl.mmall.task.bean.PushServiceConfig;
import com.xyl.mmall.task.dao.PushManagementDao;

/**
 * 
 * @author jiangww
 *
 */
class PushTask extends TimerTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private PushManagementDao pushManagementDao;
	private PushSenderConfig pushSenderConfig;
	private PushServiceConfig pushServiceConfig;
	private PushSenderService pushSenderService;
	private String[] area;


	/**
	 * 数据库循环读的ＩＤ标示
	 */

	public PushTask(PushTaskServiceImpl pushTimerService,
			PushSenderConfig pushSenderConfig, String[] area) {
		this.pushManagementDao = pushTimerService.getPushManagementDao();
		this.pushSenderConfig = pushSenderConfig;
		this.pushServiceConfig = pushTimerService.getPushServiceConfig();
		this.pushSenderService = pushTimerService.getPushSenderService();
		this.area = area;
	}

	@Override
	public void run() {
		logger.info("an task run..");
		PushManagement p = pushManagementDao.getObjectById(pushSenderConfig
				.getTaskId());
		// 这里简单的验证，为了防止有人在加载道缓存的间隔内又再次修改时间
		if (p.getPushTime() > System.currentTimeMillis() + 60 * 1000) {
			return;
		}
		if (p.getPushTime() < System.currentTimeMillis() - 1 * 60 * 60 * 1000) {
			return;
		}
		// 获取定义的临时文件夹
		String filepath = pushServiceConfig.getTempFileFolder();
		if (!filepath.endsWith("/")) {
			filepath = filepath + "/";
		}
		// 加锁，不允许更新文件
		synchronized (Locker.LOCKED) {

			for (String areaCode : area) {
				String path = filepath + areaCode;
				File f = new File(path);
				if (!f.exists())
					continue;

				try {
					pushSenderService.pushGroupMessage(pushSenderConfig, path,areaCode);
				} catch (PushException e) {
					logger.error(e.toString());
				}

			}

		}

	}

}
