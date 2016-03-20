package com.xyl.mmall.common.out.facade.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.common.out.facade.PayFacade;
import com.xyl.mmall.common.param.PayConfirmResult;
import com.xyl.mmall.common.param.PayOrderParam;
import com.xyl.mmall.common.param.PayRecieveGoodsParam;
import com.xyl.mmall.common.util.HttpClientUtil;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.config.PayConfiguration;
import com.xyl.mmall.framework.enums.PayState;
import com.xyl.mmall.framework.util.DateUtils;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.member.dto.UserProfileDTO;
import com.xyl.mmall.member.service.UserProfileService;
import com.xyl.mmall.order.constant.ConstValueOfOrder;
import com.xyl.mmall.order.dto.OrderFormBriefDTO;
import com.xyl.mmall.order.dto.OrderOperateLogDTO;
import com.xyl.mmall.order.enums.OperateLogType;
import com.xyl.mmall.order.enums.OrderFormSource;
import com.xyl.mmall.order.service.OrderBriefService;
import com.xyl.mmall.order.service.OrderService;
import com.xylpay.gateway.client.enums.CharsetTypeEnum;
import com.xylpay.gateway.client.service.ClientSignatureService;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年8月6日下午3:56:24
 */
@Facade
public class PayFacadeImpl implements PayFacade {

	private static Logger logger = LoggerFactory.getLogger(PayFacadeImpl.class);

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private PayConfiguration payConfiguration;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderBriefService orderBriefService;

	@Autowired
	private UserProfileService userProfileService;

	@Override
	public boolean validSignMsg(String sgnMsgSrc, String sgnMsg) {
		return ClientSignatureService.verifySignatureByRSA(sgnMsgSrc, sgnMsg, CharsetTypeEnum.UTF8,
				ClientSignatureService.XYLPAY_PUBKEY);
	}

	@Override
	public PayOrderParam buildPayOrderParam(List<OrderFormBriefDTO> orderBDTOList) {
		if (CollectionUtil.isEmptyOfList(orderBDTOList)) {
			return null;
		}
		PayOrderParam payOrderParam = new PayOrderParam();
		String cashierType = "", userIdStr = "";
		long orderParentId = 0;
		int payTotal = 0, totalAMount = 0, couponTotalAMount = 0;
		int cartRPrice = 0, cartOriRPrice = 0, couponDiscount = 0;
		StringBuffer orderDetailSb = new StringBuffer();
		for (OrderFormBriefDTO orderFormBriefDTO : orderBDTOList) {
			if (!orderFormBriefDTO.getPayState().equals(PayState.ONLINE_NOT_PAY)) {
				return null;// 订单已支付
			}
			cartRPrice = orderFormBriefDTO.getCartRPrice().multiply(new BigDecimal(100)).intValue();
			cartOriRPrice = orderFormBriefDTO.getCartOriRPrice().multiply(new BigDecimal(100)).intValue();
			couponDiscount = orderFormBriefDTO.getCouponDiscount().multiply(new BigDecimal(100)).intValue();
			BusinessDTO businessDTO = businessFacade.getBusinessById(orderFormBriefDTO.getBusinessId());
			if (businessDTO == null) {
				return null;// 商家不存在
			}
			// int businessAccountIndex =
			// businessDTO.getBusinessAccount().indexOf(MmallConstant.BUSINESS_ACCOUNT_SUFFIX);
			// String businessAccount =
			// businessAccountIndex!=-1?businessDTO.getBusinessAccount().substring(0,
			// businessAccountIndex):businessDTO.getBusinessAccount();
			// splitRuleSb.append(orderFormBriefDTO.getOrderId()).append(",")
			// .append(businessAccount).append(",")//dev环境 ，值为1测试，正式环境去正式账号
			// .append(cartRPrice).append(",")
			// .append(couponDiscount).append(",")
			// .append(0).append("|");
			orderParentId = orderFormBriefDTO.getParentId();
			userIdStr = String.valueOf(orderFormBriefDTO.getUserId());
			payTotal += cartRPrice;// 实付总金额
			totalAMount += cartOriRPrice;// 原总价
			couponTotalAMount += couponDiscount;// 优惠券总价
			cashierType = orderFormBriefDTO.getOrderFormSource().equals(OrderFormSource.PC) ? "02" : "01";
		}
		orderDetailSb.append(orderParentId).append(",").append(totalAMount).append(", , ,");

		StringBuffer sb = new StringBuffer(256);
		payOrderParam.setVersion(payConfiguration.getVersion());// 版本
		sb.append("version").append("=").append(payOrderParam.getVersion()).append("&");
		payOrderParam.setSerial_id("bwd"
				+ DateUtils.parseLongToString(DateUtils.DATETIMEFORMAT, System.currentTimeMillis()));// 请求系列号
		sb.append("serial_id").append("=").append(payOrderParam.getSerial_id()).append("&");
		payOrderParam.setStart_time(DateUtils.parseLongToString(DateUtils.DATETIMEFORMAT, System.currentTimeMillis()));// 订单提交时间
		sb.append("start_time").append("=").append(payOrderParam.getStart_time()).append("&");
		payOrderParam.setExpire_time(DateUtils.parseLongToString(DateUtils.DATETIMEFORMAT, System.currentTimeMillis()
				+ ConstValueOfOrder.MAX_PAY_TIME));// 订单失效时间,2个小时
		sb.append("expire_time").append("=").append(payOrderParam.getExpire_time()).append("&");
		sb.append("customer_ip").append("=").append("").append("&");// 可空
		payOrderParam.setOrder_details(orderDetailSb.toString());// 订单明细
		sb.append("order_details").append("=").append(payOrderParam.getOrder_details()).append("&");
		payOrderParam.setTotal_amount(String.valueOf(payTotal));// 订单总金额，实付
		sb.append("total_amount").append("=").append(payOrderParam.getTotal_amount()).append("&");
		payOrderParam.setType(payConfiguration.getType());// 担保交易类
		sb.append("type").append("=").append(payOrderParam.getType()).append("&");
		UserProfileDTO userProfileDTO = userProfileService.findUserProfileById(Long.parseLong(userIdStr));
		if (userProfileDTO == null) {
			logger.error("buildPayOrderParam error " + userIdStr + " user is not exist ");
			return null;
		}
		int userNameIndex = userProfileDTO.getUserName().indexOf(MmallConstant.MAINSITE_ACCOUNT_SUFFIX);
		String userName = userNameIndex != -1 ? userProfileDTO.getUserName().substring(0, userNameIndex)
				: userProfileDTO.getUserName();
		payOrderParam.setBuyer_id(userName);// 买家新云联账号
		sb.append("buyer_id").append("=").append(payOrderParam.getBuyer_id()).append("&");

		sb.append("paymethod").append("=").append("").append("&");// 支付方式 可空
		sb.append("org_code").append("=").append("").append("&");// 目标基金机构代码 可空
		sb.append("currency_code").append("=").append("").append("&");// 交易币种 可空
		sb.append("direct_flag").append("=").append("").append("&");// 是否直连 可空
		sb.append("borrowing_marked").append("=").append("").append("&");// 资金来源借贷标示
																			// 可空
		sb.append("coupon_flag").append("=").append("").append("&");// 红包标示 可空
		sb.append("least_pay").append("=").append("").append("&");// 订单最少支付 可空
		sb.append("coupon").append("=").append("").append("&");// 红包消费列表 可空

		payOrderParam.setReturn_url(payConfiguration.getReturnUrl());// 商户回调地址
		sb.append("return_url").append("=").append(payOrderParam.getReturn_url()).append("&");
		payOrderParam.setNotice_url(payConfiguration.getNoticeUrl());// 商户通知地址
		sb.append("notice_url").append("=").append(payOrderParam.getNotice_url()).append("&");
		payOrderParam.setPartner_id(payConfiguration.getPartnerId());
		sb.append("partner_id").append("=").append(payConfiguration.getPartnerId()).append("&");
		payOrderParam.setCashier_type(cashierType);// 收银台产品,01 微信 02 PC
		sb.append("cashier_type").append("=").append(payOrderParam.getCashier_type()).append("&");
		payOrderParam.setSplit_rule_code("");// 分账规则代码
		sb.append("split_rule_code").append("=").append(payOrderParam.getSplit_rule_code()).append("&");
		payOrderParam.setSplit_rule("");// 分账规则
		sb.append("split_rule").append("=").append(payOrderParam.getSplit_rule()).append("&");
		payOrderParam.setBonus(String.valueOf(couponTotalAMount));// 红包金额，商城抵扣金额
		sb.append("bonus").append("=").append(payOrderParam.getBonus()).append("&");
		payOrderParam.setSettle_amount(String.valueOf(totalAMount));// 订单结算金额
		sb.append("settle_amount").append("=").append(payOrderParam.getSettle_amount()).append("&");
		sb.append("token").append("=").append("").append("&");// 登录标示
		payOrderParam.setRemark("pay");// 扩展字段,随意填的
		sb.append("remark").append("=").append(payOrderParam.getRemark()).append("&");
		payOrderParam.setCharset(payConfiguration.getCharset());// 编码方式
		sb.append("charset").append("=").append(payOrderParam.getCharset()).append("&");

		payOrderParam.setSign_type(payConfiguration.getSignType());// 签名类型
		sb.append("sign_type").append("=").append(payOrderParam.getSign_type());
		logger.info("pay request payOrderParam info:" + sb.toString());
		try {
			String signMsg = ClientSignatureService.genSignByRSA(sb.toString(), CharsetTypeEnum.UTF8);
			payOrderParam.setSign_msg(signMsg);// 签名字符串
		} catch (Exception e) {
			logger.error("gen sign msg ", e.getMessage());
			e.printStackTrace();
			return null;
		}
		payOrderParam.setRequestUrl(payConfiguration.getPayUrl());
		return payOrderParam;
	}

	/**
	 * 确认收货
	 * 
	 * @return
	 */
	public int confirmRecieveGoods(PayRecieveGoodsParam payRecieveGoodsParam) {
		StringBuffer sb = new StringBuffer(256);
		Map<String, String> httpParams = new HashMap<String, String>();
		sb.append("version").append("=").append(payConfiguration.getVersion()).append("&");
		httpParams.put("version", payConfiguration.getVersion());// 版本

		String serialId = "bwd" + DateUtils.parseLongToString(DateUtils.DATETIMEFORMAT, System.currentTimeMillis());
		sb.append("serial_id").append("=").append(serialId).append("&");
		httpParams.put("serial_id", serialId);// 请求系列号

		sb.append("partner_id").append("=").append(payConfiguration.getPartnerId()).append("&");
		httpParams.put("partner_id", payConfiguration.getPartnerId());// 商户Id

		String orderNo = String.valueOf(payRecieveGoodsParam.getOrderId());
		sb.append("order_no").append("=").append(orderNo).append("&");
		httpParams.put("order_no", orderNo);// 分账订单号

		sb.append("charset").append("=").append(payConfiguration.getCharset()).append("&");
		httpParams.put("charset", payConfiguration.getCharset());// 编码方式

		sb.append("sign_type").append("=").append(payConfiguration.getSignType());
		httpParams.put("sign_type", payConfiguration.getSignType());// 签名类型
		logger.info("confirm goods request:" + sb.toString());

		try {
			String signMsg = ClientSignatureService.genSignByRSA(sb.toString(), CharsetTypeEnum.UTF8);
			httpParams.put("sign_msg", signMsg);// 签名字符串
		} catch (Exception e) {
			logger.error("gen sign msg ", e.getMessage());
			e.printStackTrace();
			return -1;// 加签失败
		}
		String result = "", errorMsg = "";
		try {
			result = HttpClientUtil.sendHttpPost(payConfiguration.getConfirmUrl(), httpParams);
		} catch (HttpException e) {
			logger.error("orderId " + payRecieveGoodsParam.getOrderId() + "call pay interface HttpException ",
					e.getMessage());
			e.printStackTrace();
			errorMsg = "请求支付确认收货接口失败,可能网络原因";
			logPayError(payRecieveGoodsParam, errorMsg);
			return -2;// 请求确认收货接口失败
		} catch (IOException e) {
			logger.error("orderId " + payRecieveGoodsParam.getOrderId() + "call pay interface IOException ",
					e.getMessage());
			e.printStackTrace();
			errorMsg = "请求支付确认收货接口失败,可能网络原因";
			logPayError(payRecieveGoodsParam, errorMsg);
			return -2;
		}

		logger.info("confirm goods response:" + result);

		PayConfirmResult payConfirmResult = null;
		if (StringUtils.isEmpty(result)) {
			errorMsg = "请求支付确认收货接口返回接口为空，支付平台响应失败";
			logPayError(payRecieveGoodsParam, errorMsg);
			return -2;
		}

		payConfirmResult = JsonUtils.fromJson(result, PayConfirmResult.class);
		StringBuffer signSB = new StringBuffer();
		signSB.append("order_no=").append(payConfirmResult.getOrder_no()).append("&");
		signSB.append("result_code=").append(payConfirmResult.getResult_code()).append("&");
		signSB.append("state_code=").append(payConfirmResult.getState_code()).append("&");
		signSB.append("complete_time=").append(payConfirmResult.getComplete_time()).append("&");
		signSB.append("charset=").append(payConfirmResult.getCharset()).append("&");
		signSB.append("sign_type=").append(payConfirmResult.getSign_type());

		if (!payConfirmResult.getResult_code().equals("0000") || !payConfirmResult.getState_code().equals("2")) {
			logger.error("orderId " + payRecieveGoodsParam.getOrderId() + " return result failure message: "
					+ signSB.toString());
			errorMsg = "确认收货不成功,resultCode=" + payConfirmResult.getResult_code() + " stateCode:"
					+ payConfirmResult.getState_code();
			logPayError(payRecieveGoodsParam, errorMsg);
			return -3;// 确认收货不成功
		}
		if (!validSignMsg(signSB.toString(), payConfirmResult.getSign_msg())) {
			logger.error("orderId " + payRecieveGoodsParam.getOrderId() + "check sign failure message: "
					+ signSB.toString());
			errorMsg = "验证签名失败，可能被攻击";
			logPayError(payRecieveGoodsParam, errorMsg);
			return -4;// 验签失败
		}
		return 1;// 成功
	}

	/**
	 * 记录支付确认支付异常
	 * 
	 * @param payRecieveGoodsParam
	 * @param errorMsg
	 */
	private void logPayError(PayRecieveGoodsParam payRecieveGoodsParam, String errorMsg) {
		try {
			OrderFormBriefDTO orderFormBriefDTO = orderBriefService.queryOrderFormBrief(
					payRecieveGoodsParam.getUserId(), payRecieveGoodsParam.getOrderId(), true);
			OrderOperateLogDTO operateLogDTO = new OrderOperateLogDTO();
			operateLogDTO.setOperatorId(payRecieveGoodsParam.getUserId());
			operateLogDTO.setBusinessId(orderFormBriefDTO.getBusinessId());
			operateLogDTO.setOrderId(payRecieveGoodsParam.getOrderId());
			operateLogDTO.setNote("调用支付平台放款异常,需运营咨询支付平台");
			operateLogDTO.setOperatorType(3);
			operateLogDTO.setType(OperateLogType.ORDER_PAY.getIntValue());
			operateLogDTO.setPreContent("");
			operateLogDTO.setCurContent("放款失败原因:" + errorMsg);
			orderService.addOrderOperateLog(operateLogDTO);
		} catch (Exception e) {
			logger.error("orderId" + payRecieveGoodsParam.getOrderId() + " save pay log error:", e.getMessage());
		}
	}

}
