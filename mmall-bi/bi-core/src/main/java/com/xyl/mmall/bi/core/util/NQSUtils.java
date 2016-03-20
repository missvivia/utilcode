package com.xyl.mmall.bi.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.cloud.nqs.client.consumer.MessageConsumer;
import com.netease.cloud.nqs.client.producer.MessageProducer;

/**
 * NQS工具类.
 * 
 * @author wangfeng
 * 
 */
public final class NQSUtils {

	private static Logger logger = LoggerFactory.getLogger(NQSUtils.class);

	private NQSUtils() {
	}

	public static void closeMessageProducer(MessageProducer producer) {
		if (producer != null) {
			try {
				producer.shutdown();
			} catch (Exception e) {
				logger.trace("Could not close NQS MessageProducer", e);
			}
		}
	}

	public static void closeMessageConsumer(MessageConsumer consumer) {
		if (consumer != null) {
			try {
				consumer.shutdown();
			} catch (Exception e) {
				logger.trace("Could not close NQS MessageConsumer", e);
			}
		}
	}

}
