package com.xyl.mmall.common.out.facade.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.out.facade.SMSFacade;
import com.xyl.mmall.common.param.SMSParam;
import com.xyl.mmall.common.param.SMSResult;
import com.xyl.mmall.common.util.HttpClientUtil;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.config.SMSConfiguration;
import com.xyl.mmall.framework.util.HttpUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.service.OrderService;

/**
 * 
 * @author author:lhp
 * 
 * @version date:2015年8月11日下午6:37:15
 */
@Facade
public class SMSFacadeImpl implements SMSFacade {

	private static Logger logger = Logger.getLogger(SMSFacadeImpl.class);

	@Autowired
	private SMSConfiguration smsConfiguration;

	@Autowired
	private OrderService orderService;

	@Autowired
	private BusinessService businessService;

	@Override
	public int sendGoodsSingle(SMSParam smsParam) {
		if (smsParam.getOrderNo() <= 0) {
			return -1;// 订单不存在
		}
		OrderFormDTO orderFormDTO = null;
		if (StringUtils.isEmpty(smsParam.getMobile()) || smsParam.getPrice() == null) {
			orderFormDTO = orderService.queryOrderFormByOrderId(smsParam.getOrderNo());
			if (orderFormDTO == null) {
				logger.error("orderId " + smsParam.getOrderNo() + " is not exist");
				return -1;// 订单不存在
			}
			smsParam.setMobile(orderFormDTO.getOrderExpInfoDTO().getConsigneeMobile());
			smsParam.setPrice(orderFormDTO.getCartRPrice());
		}
		if (StringUtils.isEmpty(smsParam.getShopName()) && orderFormDTO != null) {
			BusinessDTO businessDTO = businessService.getBreifBusinessById(orderFormDTO.getBusinessId(), -1);
			if (businessDTO == null) {
				logger.error("orderId " + smsParam.getOrderNo() + " businesser is not exist");
				return -2;// 商家被删除
			}
			smsParam.setShopName(businessDTO.getStoreName());
		}
		Map<String, String> httpParams = new HashMap<String, String>();
		httpParams.put("mobile", smsParam.getMobile());// 手机号
		httpParams.put("orderNo", String.valueOf(smsParam.getOrderNo()));// 订单号
		httpParams.put("price", smsParam.getPrice().toString());// 订单价格
		httpParams.put("shopName", smsParam.getShopName());// 店铺
		String result = "";
		try {
			result = HttpClientUtil.sendHttpPost(smsConfiguration.getSmsGoodsSglUrl(), httpParams);
		} catch (HttpException e) {
			logger.error("orderId " + smsParam.getOrderNo() + " call sms interface HttpException ", e);
			return -3;
		} catch (IOException e) {
			logger.error("orderId " + smsParam.getOrderNo() + " call sms interface IOException ", e);
			return -3;
		}
		SMSResult smsResult = null;
		if (StringUtils.isBlank(result)) {
			logger.error("orderId " + smsParam.getOrderNo() + " call sms return null");
			return -3;
		} else {
			smsResult = JsonUtils.fromJson(result, SMSResult.class);
		}
		return smsResult.isSuccess() ? 1 : -3;
	}

	@Override
	public void sendCode(String mobile, String type) {
		NameValuePair mobilePair = new BasicNameValuePair("mobile", mobile);
		NameValuePair typePair = new BasicNameValuePair("type", type);
		NameValuePair[] param = new NameValuePair[]{mobilePair, typePair};
		sendSMSAsync(param);
		logger.info("Post sendCode request finish!");
	}

	@Override
	public boolean checkCode(String mobile, String code, String type) {
		NameValuePair mobilePair = new BasicNameValuePair("mobile", mobile);
		NameValuePair typePair = new BasicNameValuePair("type", type);
		NameValuePair codePair = new BasicNameValuePair("code", code);
		NameValuePair[] param = new NameValuePair[]{mobilePair, typePair, codePair};
		HttpResponse response = HttpUtil.sendPost(smsConfiguration.getSmsCheckCodeURL(), param);
		logger.info("Post checkCode request finish!");
		return analysisResponse(response);
	}

	/**
	 * temp
	 * @param param
	 */
	private void sendSMSAsync(final NameValuePair[] param) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (analysisResponse(
							HttpUtil.sendPost(smsConfiguration.getSmsSendCodeURL(), param))) {
						logger.info("Send code success!");
					} else {
						logger.error("Send code failed!");
					}
				}
			}).start();
		} catch (Exception e) {
			logger.error("Send code error!", e);
		}
	}
	
	/**
	 * 解析response
	 * @param response
	 * @return
	 */
	private boolean analysisResponse(HttpResponse response) {
		if (response == null) {
			logger.error("Response is null!");
		} else {
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				try {
					String result = EntityUtils.toString(response.getEntity());
					if (StringUtils.isBlank(result)) {
						logger.error("Response entity is blank!");
					} else {
						try {
							JSONObject json = JSONObject.parseObject(result);
							return json.getBooleanValue("success");
						} catch (Exception e) {
							logger.error("Parse json error! Result : " + result);
						}
					}
				} catch (IOException e) {
					logger.error("Read responseEntity error!", e);
				}
			} else {
				logger.error("Httpstatus is " + status + ".");
			}
		}
		return false;
	}
}
