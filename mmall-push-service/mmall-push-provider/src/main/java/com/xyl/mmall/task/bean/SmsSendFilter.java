package com.xyl.mmall.task.bean;

import org.springframework.stereotype.Component;

import com.xyl.mmall.task.enums.PushMessageType;

/**
 * 短信发送类型过滤器
 * @author hzzhaozhenzuo
 *
 */
@Component
public class SmsSendFilter extends BaseDataFilter<Integer> {

	@Override
	public void initData() {
		super.addDataToPassFilter(PushMessageType.send_order)
				.addDataToPassFilter(PushMessageType.order_cancel_after_pay);
	}

}
