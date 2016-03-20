package com.xyl.mmall.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.exception.AppException;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.vo.BaseJsonVO;

public class ItemCenterExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterExceptionHandler.class);

	public static BaseJsonVO errorBaseJsonVO(ErrorCode code) {
		BaseJsonVO vo = new BaseJsonVO(code);
		return vo;
	}

	public static BaseJsonVO SimpleErrorBaseJsonVO(ErrorCode code, String extra) {
		BaseJsonVO vo = errorBaseJsonVO(code);
		vo.setMessage(extra);
		return vo;
	}

	public static BaseJsonVO errorBaseJsonVO(ErrorCode code, String extra) {
		BaseJsonVO vo = errorBaseJsonVO(code);
		if (org.apache.commons.lang.StringUtils.isNotBlank(extra) && extra.length() > 100) {
			extra = extra.substring(0, 97) + "...";
		}
		vo.setMessage(code.getDesc() + ":" + extra);
		return vo;
	}

	public static AppException getThrowAppException(Exception e) {
		logger.error(e.getMessage(), e);
		if (e instanceof AppException)
			return (AppException) e;
		else if (e instanceof ServiceException)
			return new AppException(ErrorCode.SERVICE_ERROR.getDesc() + "::" + e.getMessage(), e);
		else
			return new AppException(ErrorCode.ITEM_CENTER_ERROR.getDesc() + "::" + e.getMessage(), e);
	}

	public static BaseJsonVO getAjaxExceptionJsonVO(Exception e) {
		logger.error("line.separator");
		logger.error(System.getProperty("line.separator"));
		logger.error(e.getMessage(), e);
		String msg = StringUtils.substringBetween(e.getMessage(), ":", System.getProperty("line.separator"));
		if (e instanceof ServiceException) {
			return ItemCenterExceptionHandler.SimpleErrorBaseJsonVO(ErrorCode.SERVICE_ERROR, msg);
		} else {
			return ItemCenterExceptionHandler.SimpleErrorBaseJsonVO(ErrorCode.ITEM_CENTER_ERROR, msg);
		}
	}

	public static BaseJsonVO getAjaxJsonVO(int errorCode) {
		ErrorCode code = ErrorCode.getErrorCodeByIntValue(errorCode);
		return new BaseJsonVO(code);
	}

	public static ServiceException getServiceException(Exception e) {
		logger.error(e.getMessage(), e);
		if (e instanceof ServiceException)
			return (ServiceException) e;
		else
			return new ServiceException(e.getMessage(), e);
	}
}
