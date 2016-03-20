package com.xyl.mmall.bi.service.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;

import com.xyl.mmall.bi.core.enums.BIType;
import com.xyl.mmall.bi.core.enums.ClientType;
import com.xyl.mmall.bi.service.SpringContextService;

/**
 * 
 * 
 * @author wangfeng
 * 
 */
public class BILogServiceFactory {

	private static Map<String, String> serviceNameDefinitionMap = new ConcurrentHashMap<String, String>();

	private static final String SIMPLE_BILOG_SERVICE_NAME = "simpleBILogService";

	private static final String APP_BILOG_SERVICE_NAME = "appBILogService";

	private static final String SERVICE_NAME_KEY_SPLIT = "###";

	static {
		registerServiceNameDefinition();
	}

	/**
	 * 注册所有打印日志的业务类.
	 */
	public static void registerServiceNameDefinition() {
		// key=clienttype+split+bitype value=serviceName
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.BRANDDETAILPAGE), "brandLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.FOLLOWBRAND), "brandLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.DEFOLLOWBRAND), "brandLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.ORDERDETAILPAGE),
				"orderDetailPageLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.ORDERCONFIRMPAGE),
				"orderConfirmPageLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.ORDERSUBMITPAGE),
				"orderSubmitPageLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.CHANGEPAYMENT), "changePaymentLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.TOPAY), "toPayLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.CANCELORDER), "cancelOrderLogService");

		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.GOODSPAGE), "goodsPageLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.POPAGE), "poService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.ADDTOCART), "cartLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.RETURN_GOODS_APPLY_PAGE),
				"returnGoodsApplyLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.RETURN_GOODS_BACK_PAGE),
				"returnGoodsBackLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.WEB, BIType.RETURN_GOODS_SERVICE_PAGE),
				"returnGoodsServiceLogService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.ORDER, BIType.SKU_SELL_STATISTICS_CMS),
				"skuStatisticsService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.ORDER, BIType.SKU_SELL_STATISTICS_ERP),
				"skuStatisticsService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.ORDER, BIType.SKU_SELL_STATISTICS_MAINSITE),
				"skuStatisticsService");
		serviceNameDefinitionMap.put(genServiceName(ClientType.CMS, BIType.CMS_USER_REGIST), 
				"cmsUserRegistService");
	}

	/**
	 * 返回打印日志的业务类实例.
	 * 
	 * @param clientType
	 * @param type
	 * @return
	 */
	public static BILogService getInstance(ClientType clientType, BIType type) {
		String serviceName = getServiceName(clientType, type);
		BILogService biLogService = null;
		try {
			biLogService = (BILogService) SpringContextService.getBean(serviceName);
		} catch (BeansException e) {
			biLogService = (BILogService) SpringContextService.getBean(SIMPLE_BILOG_SERVICE_NAME);
		}
		return biLogService;
	}

	/**
	 * 获取打印日志业务类类名.
	 * 
	 * @param clientType
	 *            app/web端.
	 * @param type
	 *            具体页面/操作.
	 * @return
	 */
	public static String getServiceName(ClientType clientType, BIType type) {
		// 1.app打印日志业务类
		if (clientType == ClientType.APP)
			return APP_BILOG_SERVICE_NAME;
		else if (clientType == ClientType.ORDER) {
			// 2.Order处理统计
			String serviceNameKey = genServiceName(clientType, type);
			String serviceName = serviceNameDefinitionMap.get(serviceNameKey);
			return serviceName;
		} else if (clientType == ClientType.CMS) {
			String serviceNameKey = genServiceName(clientType, type);
			String serviceName = serviceNameDefinitionMap.get(serviceNameKey);
			return serviceName;
		} else {
			// 3.其他打印日志业务类
			return SIMPLE_BILOG_SERVICE_NAME;
		}
//		String serviceNameKey = genServiceName(clientType, type);
//		String serviceName = serviceNameDefinitionMap.get(serviceNameKey);
//		if (StringUtils.isBlank(serviceName))
//			serviceName = SIMPLE_BILOG_SERVICE_NAME;
	}

	public static String genServiceName(ClientType clientType, BIType type) {
		return clientType.getValue() + SERVICE_NAME_KEY_SPLIT + type.getValue();
	}
}
