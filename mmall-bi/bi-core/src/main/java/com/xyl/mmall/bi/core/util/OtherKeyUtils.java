package com.xyl.mmall.bi.core.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.bi.core.aop.BILog;
import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.framework.util.JsonUtils;

/**
 * 特殊页面需要的其它key值.
 * 
 * @author wangfeng
 * 
 */
public final class OtherKeyUtils {

	private static final Logger logger = Logger.getLogger(OtherKeyUtils.class);

	private OtherKeyUtils() {
	}

	/**
	 * 设置日志的特殊参数值作为消费端查询字段otherKey.<br/>
	 * otherKey也可以用多个参数进行拼接,再在日志消费端解析.
	 * 
	 * @param clientType
	 * @param biLog
	 * @param args
	 *            接口请求参数.
	 * @return
	 */
	public static String getOtherKey(ClientType clientType, BILog biLog, Object[] args) {
		String otherKey = "";
		if (clientType == ClientType.WEB) {
			otherKey = getOtherKeyOfWeb(biLog, args);
		}
		return otherKey;
	}

	/**
	 * web端获取otherkey.
	 * 
	 * @param biLog
	 * @param args
	 * @return
	 */
	public static String getOtherKeyOfWeb(BILog biLog, Object[] args) {
		String otherKey = "";
		// 根据type类型获取otherKey
		String type = biLog.type();
		BIType biType = BIType.UNKNOWN.genEnumByValue(type);
		switch (biType) {
		case BRANDDETAILPAGE:
			otherKey = brandDetailPage(args);
			break;
		case ORDERDETAILPAGE:
			// OrderId
			otherKey = args[1].toString();
			break;
		case ORDERCONFIRMPAGE:
			otherKey = orderConfirmPage(args);
			break;
		case ORDERSUBMITPAGE:
			otherKey = orderSubmitPage(args);
			break;
		case CHANGEPAYMENT:
			otherKey = changePayment(args);
			break;
		case TOPAY:
			// OrderId
			otherKey = args[0].toString();
			break;
		case CANCELORDER:
			// OrderId
			otherKey = args[1].toString();
			break;
		case ADDTOCART:
			// skuid
			Model model = (Model) args[0];
			otherKey = ((Long) model.asMap().get("skuid")).toString();
			break;
		case POPAGE:
			otherKey = poPage(args);
			break;
		case FOLLOWBRAND:
		case DEFOLLOWBRAND:
			otherKey = followOrDefollowBrand(args);
			break;
		case GOODSPAGE:
			otherKey = goodsPage(args);
			break;
		case RETURN_GOODS_APPLY_PAGE:
			otherKey = returnGoodsApplyPage(args);
			break;
		case RETURN_GOODS_BACK_PAGE:
			otherKey = returnGoodsBackPage(args);
			break;
		case RETURN_GOODS_SERVICE_PAGE:
			otherKey = returnGoodsServicePage(args);
			break;
		default:
			break;
		}
		return otherKey;
	}

	/**
	 * 品牌mini页.
	 * 
	 * @param args
	 * @return
	 */
	public static String brandDetailPage(Object[] args) {
		// 品牌mini页面的第二个参数是brandId
		return args[1].toString();
	}

	/**
	 * 填写订单地址页面
	 * 
	 * @param args
	 * @return OrderConfirmPageParam(toJson)
	 */
	private static String orderConfirmPage(Object[] args) {
		Model model = (Model) args[0];
		String json = (String) model.asMap().get("orderConfirmPageParam");
		return json;
	}

	/**
	 * 提交订单页面
	 * 
	 * @param args
	 * @return
	 */
	private static String orderSubmitPage(Object[] args) {
		Model model = (Model) args[0];
		Long orderId = (Long) model.asMap().get("orderId");
		return orderId != null ? orderId.toString() : null;
	}

	/**
	 * 修改支付方式页面
	 * 
	 * @param args
	 * @return ChangePaymentParam(toJson)
	 */
	private static String changePayment(Object[] args) {
		Model model = (Model) args[0];
		String json = (String) model.asMap().get("changePaymentParam");
		return json;
	}

	/**
	 * 档期页面.
	 * 
	 * @param args
	 * @return
	 */
	public static String poPage(Object[] args) {
		Model model = (Model) args[0];
		Map<String, Object> map = model.asMap();

		return map.get("bi_supplyId") + "~" + map.get("bi_poId") + "~" + map.get("bi_branId") + "~"
				+ map.get("bi_branName") + "~" + map.get("bi_status");
	}

	/**
	 * 关注或者取消关注品牌的点击行为
	 * 
	 * @param args
	 * @return
	 */
	public static String followOrDefollowBrand(Object[] args) {
		JSONObject param = (JSONObject) args[0];
		Long brandId = param.getLongValue("brandId");
		return brandId.toString();
	}

	/**
	 * 商品详情页
	 * 
	 * @param args
	 * @return
	 */
	public static String goodsPage(Object[] args) {
		HttpServletRequest request = (HttpServletRequest) args[1];
		return request.getParameter("id");
	}

	/**
	 * 退货申请页面
	 * 
	 * @param args
	 * @return
	 */
	public static String returnGoodsApplyPage(Object[] args) {
		Model model = (Model) args[0];
		Map<String, Object> map = model.asMap();
		return String.valueOf(map.get("refundType"));
	}

	/**
	 * 退回商品页面
	 * 
	 * @param args
	 * @return
	 */
	public static String returnGoodsBackPage(Object[] args) {
		Model model = (Model) args[0];
		Map<String, Object> map = model.asMap();
		long userId = 0, orderId = 0;
		try {
			if (map.containsKey("userId")) {
				userId = Long.parseLong(String.valueOf(map.get("userId")));
			}
			if (map.containsKey("orderId")) {
				orderId = Long.parseLong(String.valueOf(map.get("orderId")));
			}
		} catch (Exception e) {
			logger.warn(e);
		}
		return String.valueOf(userId) + " " + String.valueOf(orderId);
	}

	/**
	 * 客服退款页面
	 * 
	 * @param args
	 * @return
	 */
	public static String returnGoodsServicePage(Object[] args) {
		Model model = (Model) args[0];
		Map<String, Object> map = model.asMap();
		return String.valueOf(map.get("status"));
	}
	
	public static String getOtherKeyOfOrder(BILog biLog, Object[] args) {
		String otherKey = "";
		// 根据type类型获取otherKey
		String type = biLog.type();
		BIType biType = BIType.UNKNOWN.genEnumByValue(type);
		switch (biType) {
		case SKU_SELL_STATISTICS_ERP:
			String order = String.valueOf(args[1]);
			try {
				JSONObject orderJson = JSONObject.parseObject(order);
				if (orderJson.getIntValue("orderFormState") == 11) {
					otherKey = String.valueOf(args[0]);
				}
			} catch (Exception e) {
				logger.error("Json error! order : " + order);
				otherKey = null;
			}
			break;
		case SKU_SELL_STATISTICS_CMS:
			try {
				JSONObject json = (JSONObject) JSONObject.toJSON(args[0]);
				if (StringUtils.equals("FINISH_TRADE", String.valueOf(json.get("newState")))) {
					otherKey = String.valueOf(json.get("orderId"));
				} else {
					otherKey = null;
				}
			} catch (Exception e) {
				logger.error("Json error! args : " + JsonUtils.toJson(args));
				otherKey = null;
			}
			break;
		case SKU_SELL_STATISTICS_MAINSITE:
			otherKey = String.valueOf(args[1]);
			break;

		default:
			otherKey = null;
			break;
		}
		
		return otherKey;
	}
	
	public static String getOtherKeyOfCMS(BILog biLog, Object[] args) {
		String otherKey = "";
		// 根据type类型获取otherKey
		String type = biLog.type();
		BIType biType = BIType.UNKNOWN.genEnumByValue(type);
		switch (biType) {
		case CMS_USER_REGIST:
			try {
				JSONObject json = (JSONObject) JSONObject.toJSON(args[0]);
				otherKey = String.valueOf(json.get("uid"));
				long userId = Long.parseLong(otherKey);
				if (userId > 0l) {
					break;
				}
			} catch (Exception e) {
				logger.error("Json error! args : " + JsonUtils.toJson(args));
			}
			otherKey = null;
			break;

		default:
			otherKey = null;
			break;
		}

		return otherKey;
	}
}
