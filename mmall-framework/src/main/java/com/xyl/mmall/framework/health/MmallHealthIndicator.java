package com.xyl.mmall.framework.health;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

/**
 * mmall health Indicator.
 * 
 * @author wangfeng
 *
 */
@Component(value = "mmallHealthIndicator")
public class MmallHealthIndicator implements HealthIndicator {

	private static Map<String, Object> healthMap = new ConcurrentHashMap<>();

	private static final String STATUS = "STATUS";

	static {
		// 默认设置为未启动,不代表服务器真实情况.
		healthMap.put(STATUS, Status.DOWN);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.actuate.health.HealthIndicator#health()
	 */
	@Override
	public Health health() {
		return Health.up().build();
	}

	/**
	 * 查询状态.
	 * 
	 * @return
	 */
	public Status status() {
		return (Status) healthMap.get(STATUS);
	}

	/**
	 * 上线.
	 * 
	 * @return
	 */
	public boolean active() {
		healthMap.put(STATUS, Status.UP);
		return true;
	}

	/**
	 * 下线.
	 * 
	 * @return
	 */
	public boolean offline() {
		healthMap.put(STATUS, Status.DOWN);
		return true;
	}

}
