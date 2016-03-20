package com.xyl.mmall.task.bean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.push.config.PusherConfiguration;
import com.netease.push.http.ClientConfiguration;

/**
 * push 服务器配置文件
 * 
 * @author jiangww
 *
 */
public class PushServiceConfig{

	private String tempFileFolder;
	
	@Autowired
	private PushPropertyConfiguration propertyConfiguration;

	private int connectionTimeout = 20000;

	private int maxConnections = 50;

	private int maxPreRout = 50;

	private int poolSize = 5;

	private int keepAliveTime = 5;

	private int queueSize = 200;

	private boolean startOn = false;

	// 获取2小时内的任务
	private long sleepInterval = 30 * 60 * 1000;

	private long taskInterval = 1 * 60 * 60 * 1000;

	private long updateUserInterval = 30 * 60 * 1000;

	public static String split_tag = "#@#";

	public static String type_1 = "订单发货";

	public static String type_2 = "退货成功";

	public static String type_3 = "退货拒绝";

	public static String type_4 = "福利来了";

	public static String type_5 = "福利来了";

	public static String type_6 = "订单出错";

	public static String type_7 = "付款提醒";
	
	public static String type_8 = "购物袋提醒";
	
	public static String type_9 = "订单已取消";
	
	//public static String type_10 = "订单出错";
	
	public static String type_11 = "包裹被取消";
	
	public static String type_12 = "特卖即将开始";
	
	public static String type_13 = "分享新红包";
	/**
	 * 判断重要参数是否为空
	 * 
	 * @return
	 */
	public boolean isLostConfig() {
		return this.getAppKey() == null || this.getAppSecret() == null || this.getProxyUrl() == null || this.getDomain() == null
				|| this.tempFileFolder == null;
	}

	/**
	 * 获得连接配置
	 * 
	 * @return
	 */
	public ClientConfiguration getClientConfiguration() {
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setConnectionTimeout(this.connectionTimeout);
		clientConfig.setMaxConnections(this.maxConnections);
		clientConfig.setMaxPreRoute(this.maxPreRout);
		return clientConfig;
	}

	/**
	 * 活动线程池配置
	 * 
	 * @return
	 */
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return new ThreadPoolExecutor(poolSize, poolSize * 2, keepAliveTime, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(queueSize));

	}

	/**
	 * 获得ｐｕｓｈ　ＳＤＫ配置
	 * 
	 * @return
	 */
	public PusherConfiguration getPusherConfiguration() {
		PusherConfiguration pushConfig = new PusherConfiguration(this.getDomain(), this.getAppKey(), this.getAppSecret(),
				this.getProxyUrl());
		pushConfig.setClientConfiguration(getClientConfiguration());
		pushConfig.setAsyncPusherPool(getThreadPoolExecutor());
		return pushConfig;
	}

	public String getProxyUrl() {
		return propertyConfiguration.getProxyUrl();
	}

	public String getDomain() {
		return propertyConfiguration.getDomain();
	}

	public String getAppKey() {
		return propertyConfiguration.getAppkey();
	}

	public String getAppSecret() {
		return propertyConfiguration.getAppSecret();
	}

	public String getTempFileFolder() {
		return tempFileFolder;
	}

	public void setTempFileFolder(String tempFileFolder) {
		this.tempFileFolder = tempFileFolder;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getMaxPreRout() {
		return maxPreRout;
	}

	public void setMaxPreRout(int maxPreRout) {
		this.maxPreRout = maxPreRout;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public long getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

	public long getTaskInterval() {
		return taskInterval;
	}

	public void setTaskInterval(long taskInterval) {
		this.taskInterval = taskInterval;
	}

	public long getUpdateUserInterval() {
		return updateUserInterval;
	}

	public void setUpdateUserInterval(long updateUserInterval) {
		this.updateUserInterval = updateUserInterval;
	}

	public boolean isStartOn() {
		return startOn;
	}

	public void setStartOn(boolean startOn) {
		this.startOn = startOn;
	}

	public String getSplit_tag() {
		return split_tag;
	}

	public void setSplit_tag(String split_tag) {
		PushServiceConfig.split_tag = split_tag;
	}

	public static String getType_1() {
		return type_1;
	}

	public static void setType_1(String type_1) {
		PushServiceConfig.type_1 = type_1;
	}

	public static String getType_2() {
		return type_2;
	}

	public static void setType_2(String type_2) {
		PushServiceConfig.type_2 = type_2;
	}

	public static String getType_3() {
		return type_3;
	}

	public static void setType_3(String type_3) {
		PushServiceConfig.type_3 = type_3;
	}

	public static String getType_4() {
		return type_4;
	}

	public static void setType_4(String type_4) {
		PushServiceConfig.type_4 = type_4;
	}

	public static String getType_5() {
		return type_5;
	}

	public static void setType_5(String type_5) {
		PushServiceConfig.type_5 = type_5;
	}

	public PushPropertyConfiguration getPropertyConfiguration() {
		return propertyConfiguration;
	}

	public void setPropertyConfiguration(PushPropertyConfiguration propertyConfiguration) {
		this.propertyConfiguration = propertyConfiguration;
	}

}

