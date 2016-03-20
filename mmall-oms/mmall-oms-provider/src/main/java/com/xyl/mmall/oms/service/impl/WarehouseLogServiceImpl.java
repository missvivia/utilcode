/**
 * 
 */
package com.xyl.mmall.oms.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.WarehouseLogDao;
import com.xyl.mmall.oms.meta.WarehouseLog;
import com.xyl.mmall.oms.service.WarehouseLogService;

/**
 * @author hzzengchengyuan
 *
 */
@Service
public class WarehouseLogServiceImpl implements WarehouseLogService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseLogServiceImpl.class);

	@Autowired
	private WarehouseLogDao warehouseLogDao;

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.oms.service.WarehouseLogService#log(com.xyl.mmall.oms.meta.WarehouseLog,
	 *      boolean)
	 */
	@Override
	public void log(WarehouseLog log, boolean isAsyn) {
		if (isAsyn) {
			asynLog(log);
		} else {
			doLog(log);
		}
	}

	private void doLog(WarehouseLog log) {
		warehouseLogDao.addObject(log);
	}

	private void asynLog(WarehouseLog log) {
		executorService.submit(new LogTask(log));
	}

	class LogTask implements Runnable {
		private WarehouseLog log;

		public LogTask(WarehouseLog log) {
			this.log = log;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try{
				WarehouseLogServiceImpl.this.doLog(this.log);
			}catch(Throwable e) {
				LOGGER.error("", e);
			}
		}

	}

}
