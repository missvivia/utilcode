package com.xyl.mmall.oms.facade.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.RetArgUtil;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.ExpressConstant;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.service.LocationService;
import com.xyl.mmall.oms.dto.ExpressFeeDTO;
import com.xyl.mmall.oms.dto.OmsExpPriceParam;
import com.xyl.mmall.oms.facade.OmsFeeFacade;
import com.xyl.mmall.oms.service.OmsFeeService;

@Facade
public class OmsFeeFacadeImpl implements OmsFeeFacade {

	@Autowired
	private OmsFeeService omsFeeService;

	@Autowired
	private LocationService locationService;

	private static final ExpressType defaultExpressType = ExpressType.NULL.genEnumByIntValue(ExpressType.TYPE_EMS
			.getIntValue());

	@Override
	public ExpressFeeDTO calExpressFee(OmsExpPriceParam param) {
		// toDo,目前默认选择EMS
		try {
			param.setExpressCompanyCodeSelected(ExpressConstant.EMS.getCode());
			RetArg retArg = locationService.isLocationRemote(param.getCa().getProvinceId(), param.getCa().getCityId(),
					param.getCa().getSectionId(), defaultExpressType);
			BigDecimal remoteAreaPrice = RetArgUtil.get(retArg, BigDecimal.class);
			param.setAcceptAddressRemote(RetArgUtil.get(retArg, Boolean.class));
			param.setRemoteAreaPrice(remoteAreaPrice);
			ExpressFeeDTO expressFeeDTO = omsFeeService.calExpressFee(param);
			return expressFeeDTO;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
