package com.xyl.mmall.oms.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.oms.dao.ExpressFeeDao;
import com.xyl.mmall.oms.dto.ExpressFeeDTO;
import com.xyl.mmall.oms.dto.ExpressFeeSearchParam;
import com.xyl.mmall.oms.dto.OmsExpPriceParam;
import com.xyl.mmall.oms.meta.ExpressFee;
import com.xyl.mmall.oms.service.OmsFeeService;

@Service("omsFeeService")
public class OmsFeeServiceImpl implements OmsFeeService {

	@Autowired
	private ExpressFeeDao expressFeeDao;

	private static final long ALL_TARGET_PROVINCE_ID = 0;

	private static final Logger logger = LoggerFactory
			.getLogger(OmsFeeServiceImpl.class);

	@Override
	public ExpressFeeDTO calExpressFee(OmsExpPriceParam param) {
		ExpressFeeDTO expressFeeDTO;
		logger.info("cal express fee,siteId:" + param.getProvinceId()
				+ ",targetProvinceName:" + param.getCa().getProvince()
				+ ",cod:" + param.isCOD());

		// 精确查找
		expressFeeDTO = this.getExpressFeeByParam(
				param.getExpressCompanyCodeSelected(), param.getProvinceId(),
				param.getCa().getProvinceId(), param.isCOD(),
				param.getRemoteAreaPrice());
		if (expressFeeDTO != null) {
			return expressFeeDTO;
		}

		// 尝试找默认记录
		expressFeeDTO = this.getExpressFeeByParam(
				param.getExpressCompanyCodeSelected(), param.getProvinceId(),
				ALL_TARGET_PROVINCE_ID, param.isCOD(),
				param.getRemoteAreaPrice());

		if (expressFeeDTO == null) {
			logger.error("cannot find express fee record,siteId:"
					+ param.getProvinceId() + ",targetProvince:"
					+ param.getCa().getProvince() + ",isCOD:" + param.isCOD());
		}
		return expressFeeDTO;
	}

	private ExpressFeeDTO getExpressFeeByParam(String expressCompanyCode,
			long siteId, long targetProvinceId, boolean isCOD,
			BigDecimal remoteAreaPrice) {
		ExpressFeeSearchParam searchParam = new ExpressFeeSearchParam();
		// toDo,目前物流公司默认为EMS
		searchParam.setExpressCompanyCode(expressCompanyCode);
		searchParam.setSiteId(siteId);
		searchParam.setTargetProvinceId(targetProvinceId);
		searchParam.setCodService(isCOD);

		List<ExpressFee> expressFeeList = expressFeeDao
				.searchExpressFeeByParam(searchParam);
		if (expressFeeList == null || expressFeeList.size() != 1) {
			logger.error("no express fee record or the num is not eq 1,"
					+ searchParam.toString());
			return null;
		}

		ExpressFee expressFeeRes = expressFeeList.get(0);
		logger.info("express price record:" + expressFeeRes.getId());

		ExpressFeeDTO expressFeeDTO = new ExpressFeeDTO();
		expressFeeDTO.setExpressCompanyCode(expressFeeRes
				.getExpressCompanyCode());
		expressFeeDTO.setExpressCompanyName(expressFeeRes
				.getExpressCompanyName());
		expressFeeDTO.setCodService(isCOD);

		// 价格计算
		BigDecimal price = expressFeeRes.getPrice();
		if (remoteAreaPrice != null) {
			price = price.add(remoteAreaPrice);
		}
		expressFeeDTO.setPrice(price);
		return expressFeeDTO;
	}

}
