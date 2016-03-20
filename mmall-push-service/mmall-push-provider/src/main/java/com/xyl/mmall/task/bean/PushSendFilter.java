package com.xyl.mmall.task.bean;

import org.springframework.stereotype.Component;

import com.xyl.mmall.task.enums.PushMessageType;

/**
 * 是否需要发送push过滤器
 * 
 * @author hzzhaozhenzuo
 *
 */
@Component
public class PushSendFilter extends BaseDataFilter<Integer> {

	@Override
	public void initData() {
		addDataToPassFilter(PushMessageType.send_order)
				.addDataToPassFilter(PushMessageType.retruns_fail)
				.addDataToPassFilter(PushMessageType.retruns_success)
				.addDataToPassFilter(PushMessageType.give_gift)
				.addDataToPassFilter(PushMessageType.give_coupon)
				.addDataToPassFilter(PushMessageType.cod_fail)
				.addDataToPassFilter(PushMessageType.order_timeout)
				.addDataToPassFilter(PushMessageType.cart_timeout)
				.addDataToPassFilter(PushMessageType.cod_reject)
				.addDataToPassFilter(PushMessageType.package_cancel);
	}

}
