package com.xyl.mmall.jms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.mainsite.facade.ActiveTellFacade;
import com.xyl.mmall.saleschedule.dto.ActiveTellCommonParamDTO;
import com.xyl.mmall.saleschedule.meta.ActiveTell;
import com.xyl.mmall.jms.param.CancelSmsSo;

@Controller
public class CancelSmsController {

	private static final Logger logger = LoggerFactory
			.getLogger(CancelSmsController.class);

	@Autowired
	private ActiveTellFacade activeTellFacade;

	private static final int PHONE_LENGTH = 11;

	private static final String CANCEL_CODE = "td";

	@RequestMapping("/sms/cancel")
	@ResponseBody
	public BaseJsonVO cancelSms(CancelSmsSo param) {
		logger.info("cancel sms ok,phone:" + param.getPhone() + ",code:"
				+ param.getCode() + ",subnum:" + param.getSubnum()
				+ ",message:" + param.getMessage());
		BaseJsonVO vo = new BaseJsonVO();

		if (!CANCEL_CODE.equalsIgnoreCase(param.getCode())) {
			logger.warn("bad param code:" + param.getCode());
			vo.setCode(ErrorCode.NO_MATCH);
			return vo;
		}

		String phoneParam = this.getPhone(param.getPhone());

		ActiveTellCommonParamDTO so = new ActiveTellCommonParamDTO();
		so.setTellTargetValue(phoneParam);
		List<ActiveTell> tellList = activeTellFacade.getActiveTellByParam(so);
		if (tellList == null || tellList.size() <= 0) {
			logger.warn("not found record for cancel sms,phone:" + phoneParam);
			vo.setCode(ErrorCode.CAN_NOT_FIND_OBJECT);
			return vo;
		}

		this.logInfo(tellList);

		boolean successFlag = activeTellFacade.removeActiveTell(tellList);

		vo.setCode(successFlag ? ErrorCode.SUCCESS : ErrorCode.DELETE_FAILED);
		return vo;
	}

	private String getPhone(String phone) {
		int startPos = phone.length() - PHONE_LENGTH;
		return phone.substring(startPos);
	}

	private void logInfo(List<ActiveTell> activeTellList) {
		for (ActiveTell activeTell : activeTellList) {
			logger.info("cancel id:" + activeTell.getId() + ",phone:"
					+ activeTell.getTellTargetValue());
		}
	}

}
