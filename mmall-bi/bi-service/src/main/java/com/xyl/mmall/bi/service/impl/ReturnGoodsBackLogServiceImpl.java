package com.xyl.mmall.bi.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.bi.core.meta.BasicLog;
import com.xyl.mmall.bi.service.log.BILogService;
import com.xyl.mmall.bi.util.BILogUtils;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.OrderPackageDTO;
import com.xyl.mmall.order.enums.OrderPackageState;
import com.xyl.mmall.order.service.OrderService;

/**
 * 退回商品页面
 * 
 * @author hzwangjianyi@corp.netease.com
 * @create 2014年11月9日 下午6:29:20
 *
 */
@Service("returnGoodsBackLogService")
public class ReturnGoodsBackLogServiceImpl implements BILogService {

	private static Logger logger = LoggerFactory.getLogger(ReturnGoodsBackLogServiceImpl.class);
	
	@Resource
	private OrderService orderService;
	
	@Override
	public void logInfo(BasicLog basicLog, Map<String, Object> logMap, String otherKey) {
		Map<String, Object> infoMap = BILogUtils.getBasicLogMap(basicLog, logMap);
		int returyType = 0;
		do {
			if(null == otherKey) {
				break;
			}
			String[] pair = otherKey.split(" ");
			if(2 != pair.length) {
				break;
			}
			try {
				long userId = Long.parseLong(pair[0]);
				long orderId = Long.parseLong(pair[1]);
				returyType = getReturnType(userId, orderId);
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		} while (false);
		infoMap.put("returnType", returyType);
		logger.info(JsonUtils.toJson(infoMap));
	}

	/**
	 * for log: 退回类型
	 * @param userId
	 * @param orderId
	 * @return
	 */
	private int getReturnType(long userId, long orderId) {
		OrderFormDTO ordForm = orderService.queryOrderForm(userId, orderId, null);
		if(null == ordForm) {
			return 0;
		}
		List<OrderPackageDTO> pkgList = ordForm.getOrderPackageDTOList();
		if(null == pkgList) {
			return 0;
		}
		boolean allRefused = true;
		for(OrderPackageDTO pkg : pkgList) {
			OrderPackageState state = null;
			if(null == pkg || null == (state = pkg.getOrderPackageState())) {
				continue;
			}
			if(OrderPackageState.SIGN_REFUSED != state) {
				allRefused = false;
				break;
			}
		}
		if(allRefused) {
			return 2;
		}
		return 1;
	}
	
}
