package com.xyl.mmall.task.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.push.meta.PushResult;
import com.netease.push.service.PushResultListener;
import com.xyl.mmall.task.meta.PushManagement;
import com.xyl.mmall.task.bean.Locker;
import com.xyl.mmall.task.bean.PushSenderConfig;
import com.xyl.mmall.task.dao.PushManagementDao;

public class PushCallBackListener implements PushResultListener {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public PushManagementDao dao;
	public PushSenderConfig config;
	private String areaCode;
	private int success = 1;
	private int error = 2;

	public PushCallBackListener(PushSenderConfig config, PushManagementDao dao,
			String areaCode) {
		this.dao = dao;
		this.config = config;
		this.areaCode = areaCode;
	}

	@Override
	public void afterPush(PushResult result) {
		if (result.isSuccessFul()) {
			logger.info("send msg suc! msgId = " + result.getMsgId()
					+ " taskId = " + config.getTaskId());
			updateTag(success);
		} else {
			logger.error("send msg fail! msgId= " + result.getMsgId()
					+ ", errorReason = " + result.getErrorReason());
			updateTag(error);
		}
		// clean();
	}

	/**
	 * 清理文件
	 */
	public void clean() {
		/*
		 * File f = new File(config.getAttachPath()); // f.deleteOnExit(); //
		 * 这里由于SDK的nio bug被逼无奈 FileWriter cleanFile = null; try { cleanFile =
		 * new FileWriter(f, false); logger.info("truncate file " +
		 * f.getPath()); cleanFile.write(""); cleanFile.close(); } catch
		 * (IOException e) { logger.error(e.toString(), e);
		 * 
		 * }
		 */
	}

	/**
	 * 数据库更新
	 * 
	 * @param type
	 */
	public void updateTag(int type) {
		//多线程落锁
		synchronized (Locker.B_LOCK) {
			PushManagement pm = dao.getObjectById(config.getTaskId());
			String errorArea = "";
			if (type == error) {
				errorArea = pm.getPushFailArea();
				if (StringUtils.isBlank(errorArea)) {
					errorArea = areaCode;
				} else {
					errorArea = errorArea + "," + areaCode;
				}

			}
			if (pm.getPushSuccess() == error)
				type = error;

			if (!dao.updatePushSuccess(config.getTaskId(), type, errorArea)) {
				logger.error("table push_management update fail for task= "
						+ config.getTaskId() + ", success = " + type);
			}
		}

	}

}
