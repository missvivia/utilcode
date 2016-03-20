package com.xyl.mmall.jms.consumer;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xyl.mmall.framework.codeinfo.MessageCodeInfo;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.oms.meta.OmsOrderPackage;
import com.xyl.mmall.oms.service.OmsOrderFormService;
import com.xyl.mmall.oms.service.OmsOrderPackageService;
import com.xyl.mmall.task.enums.PushMessageType;
import com.xyl.mmall.jms.enums.SmsType;
import com.xyl.mmall.jms.meta.SmsMessage;
import com.xyl.mmall.jms.service.base.BaseMessageListener;
import com.xyl.mmall.jms.service.util.ResourceTextUtil;
import com.xyl.mmall.jms.util.SendSmsUtil;

/**
 * 短信处理
 * 
 * @author hzzhaozhenzuo
 * 
 */
public class SmsMessageListener extends BaseMessageListener<SmsMessage> {

	@Autowired
	private SendSmsUtil sendSmsUtil;

	@Autowired
	private OmsOrderPackageService omsOrderPackageService;

	@Autowired
	private OmsOrderFormService omsOrderFormService;

	@Autowired
	private UserProfileService userProfileService;

	private static final ResourceBundle smsResourceBundle = ResourceTextUtil.getResourceBundleByName("content.sms");

	private static Logger logger = LoggerFactory.getLogger(SmsMessageListener.class);

	@Override
	public boolean handleMessage(SmsMessage message) {
		logger.info("send sms,mobile:" + message.getMobile());

		// group set
		this.fillSmsGroupBySmsType(message);

		if (message.isObtainContentByBiz()) {
			return this.processObtainContentByBiz(message);
		} else {
			return this.processSimple(message);
		}
	}

	private void fillSmsGroupBySmsType(SmsMessage message) {
		String group;
		if (message.getSmsType() == null || SmsType.SUBSCRIBE.equals(message.getSmsType())) {
			group = ResourceTextUtil.getTextFromResourceByKey(smsResourceBundle, "mobile.group.subscribe");
		} else {
			group = ResourceTextUtil.getTextFromResourceByKey(smsResourceBundle, "mobile.group");
		}
		message.setGroup(group);
	}

	private boolean processObtainContentByBiz(SmsMessage message) {
		if (message.getBizUniqueKey() == null || message.getUserId() <= 0) {
			logger.warn("info for send sms is null,BizUniqueKey:" + message.getBizUniqueKey() + ",userId:"
					+ message.getUserId());
			return true;
		}

		if (message.getBizTypeId() == PushMessageType.send_order) {
			return this.processSendGoodsOfOrder(message);
		} else if (message.getBizTypeId() == PushMessageType.order_cancel_after_pay) {
			return this.orderCancelAfterPay(message);
		}
		return true;
	}

	/**
	 * toDo 用户取消订单(付款后)，短信提醒用户
	 * 
	 * @param message
	 * @return
	 */
	public boolean orderCancelAfterPay(SmsMessage message) {
		logger.info("toDo,send sms for orderCancelAfterPay," + message.toString());
		Map<String, Object> otherParamMap = message.getOtherParamMap();
		if (message.getUserId() <= 0 || message.getBizUniqueKey() == null || otherParamMap == null
				|| otherParamMap.get("refundType") == null || otherParamMap.get("money") == null) {
			logger.warn("userId or bizUniqueKey is null," + message.toString());
			return true;
		}
		long orderId = Long.valueOf(message.getBizUniqueKey().toString());
		UserProfileDTO userProfileDTO = this.getUserByUserId(message.getUserId());
		if (userProfileDTO == null || StringUtils.isEmpty(userProfileDTO.getMobile())) {
			return true;
		}

		// 0:原路退回, 1:网易宝
		int refundType = Integer.valueOf(otherParamMap.get("refundType").toString());
		double money = Double.valueOf(otherParamMap.get("money").toString());
		String content = null;

		if (refundType == 0) {
			content = ResourceTextUtil.getTextFromResourceByKey(smsResourceBundle,
					"sms.order.cancel.after.pay.original", String.valueOf(orderId), money);
		} else {
			content = ResourceTextUtil.getTextFromResourceByKey(smsResourceBundle,
					"sms.order.cancel.after.pay.netease", String.valueOf(orderId), money);
		}

		return sendSmsUtil.sendMobileMessage(userProfileDTO.getMobile(), content, message.getLevel(),
				message.getGroup());
	}

	private UserProfileDTO getUserByUserId(long userId) {
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(userId);
		return userProfileDTO;
	}

	/**
	 * 发贷通知处理
	 * 
	 * @return
	 */
	private boolean processSendGoodsOfOrder(SmsMessage message) {
		if (message.getOtherParamMap() == null || message.getOtherParamMap().get(MessageCodeInfo.USER_FORM_ID) == null) {
			logger.warn("user form id is null," + message.toString());
			return true;
		}

		long omsOrderFormId = Long.valueOf(message.getBizUniqueKey().toString());
		long userFormId = Long.valueOf(message.getOtherParamMap().get(MessageCodeInfo.USER_FORM_ID).toString());

		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(message.getUserId());
		if (userProfileDTO == null || StringUtils.isEmpty(userProfileDTO.getMobile())) {
			logger.warn("no mobile fround," + message.toString());
			return true;
		}

		List<OmsOrderPackage> packageList = omsOrderPackageService.getOmsOrderPackageListByOmsOrderFormId(
				omsOrderFormId, message.getUserId());

		StringBuilder buffer = new StringBuilder(32);
		buffer.append(ResourceTextUtil.getTextFromResourceByKey(smsResourceBundle, "sms.goods.send", String.valueOf(userFormId)));

		for (OmsOrderPackage omsOrderPackage : packageList) {
			buffer.append(",");
			buffer.append(omsOrderPackage.getExpressCompany() + "物流快递单号:" + omsOrderPackage.getMailNO());
		}

		return sendSmsUtil.sendMobileMessage(userProfileDTO.getMobile(), buffer.toString(), message.getLevel(),
				message.getGroup());
	}
	
	private boolean processSimple(SmsMessage message) {
		return sendSmsUtil.sendMobileMessage(message.getMobile(), message.getContent(), message.getLevel(),
				message.getGroup());
	}
}
