package com.xyl.mmall.task.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.task.meta.DeviceLocation;
import com.xyl.mmall.task.bean.Locker;
import com.xyl.mmall.task.bean.PushServiceConfig;
import com.xyl.mmall.task.bean.SpringContextUtil;
import com.xyl.mmall.task.dao.DeviceLocationDao;

/**
 * 
 * @author jiangww
 *
 */
public class UserCopyerService extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public static volatile boolean started = false;

	protected DeviceLocationDao deviceLocationDao;

	private PushServiceConfig pushServiceConfig;

	public UserCopyerService(DeviceLocationDao deviceLocationDao,PushServiceConfig pushServiceConfig) {
		this.pushServiceConfig = pushServiceConfig;
		this.deviceLocationDao = deviceLocationDao;
	}

	@Override
	public void run() {
		UserCopyerService.started = true;
		while (true) {
			if (!started) {
				break;
			}
			// 这里加锁，因为可能会有好几十分钟的处理时间
			synchronized (Locker.LOCKED) {
				process();
			}
			try {
				Thread.sleep(pushServiceConfig.getUpdateUserInterval());
			} catch (InterruptedException e) {
				logger.error(e.toString());
			}

		}
	}

	private void process() {
		// 获取全部Area
		// 更新全部 AREA
		List<DeviceLocation> list = deviceLocationDao.getAllAreaCode();
		if (list != null) {
			for (DeviceLocation area : list) {
				processArea(area.getAreaCode());
			}

		}
	}

	private void processArea(long areaId) {

		long startId = -1;
		// 循环读取list
		String path = pushServiceConfig.getTempFileFolder();
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		path = path + areaId;
		FileWriter fw = null;
		try {
			fw = new FileWriter(path, false);

			while (true) {
				List<DeviceLocation> areaList = deviceLocationDao.getPushUserByArea(areaId, startId);
				if (areaList == null || areaList.size() == 0) {
					break;
				}
				startId = writeList(fw, areaList);
				fw.flush();
				// 简单的判断，以后要再加，防止死循环
				if (startId <= 0)
					break;
			}
			fw.close();
		} catch (Exception e) {
			logger.error("area file save error id:" + areaId, e);
		}

	}

	/**
	 * 写文件方法
	 * 
	 * @param list
	 * @throws IOException
	 */
	private long writeList(FileWriter fw, List<DeviceLocation> list) throws IOException {
		long id = 0;
		StringBuilder deviceIdList = new StringBuilder();
		for (DeviceLocation dl : list) {
			// 更新start_id　保证最大
			id = dl.getId();
			// 如果Userid为0 则绑定devideId
			if (StringUtils.isNotBlank(dl.getPlatformType())) {
				deviceIdList.append(dl.getDeviceId());
				deviceIdList.append("\r\n");
			}
		}
		fw.write(deviceIdList.toString());
		return id;
	}

}
