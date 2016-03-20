package com.xyl.mmall.logger;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CrawlerTask
 */

public final class LoggerPool {
	
	private static LoggerPool instance;
	private ThreadPoolExecutor executorService = null;
	private long keepAliveTime = 20;
	private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(9999);
	private TimeUnit unit = TimeUnit.SECONDS;
	
	private LoggerPool(){
		this.start();
	}
	
	public static synchronized LoggerPool getInstance() {
		if (instance == null)
			instance = new LoggerPool();
		return instance;
	}

	public void start() {
		executorService = new ThreadPoolExecutor(999, 1999, keepAliveTime, unit, workQueue);
	}

	public int getActiveCound() {
		return executorService.getActiveCount();
	}

	public long getTaskCount() {
		return executorService.getTaskCount();
	}

	public void submitLogger(Runnable task) {
		if (executorService == null)
			this.start();
		try {
			executorService.execute(task);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		executorService.shutdown();
	}

	public void shutdownNow() {
		executorService.shutdownNow();
	}

}