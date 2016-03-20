/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.ParamNullException;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mobile.facade.MobilePayFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.mobile.facade.converter.MobileChecker;
import com.xyl.mmall.mobile.facade.param.MobileOrderCommitAO;
import com.xyl.mmall.mobile.facade.vo.MobileOrderCompletVO;
import com.xyl.mmall.order.dto.OrderFormCalDTO;
import com.xyl.mmall.order.dto.OrderFormDTO;
import com.xyl.mmall.order.dto.TradeItemDTO;
import com.xyl.mmall.order.enums.OrderFormPayMethod;
import com.xyl.mmall.order.enums.OrderFormState;
import com.xyl.mmall.order.enums.TradeItemPayMethod;
import com.xyl.mmall.order.param.OrderServiceSetStateToEPayedParam;
import com.xyl.mmall.order.service.TradeService;
import com.xyl.mmall.promotion.dto.FavorCaculateResultDTO;
import com.xyl.mmall.promotion.service.RebateService;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobilePayFacadeImpl implements MobilePayFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());




	
}
