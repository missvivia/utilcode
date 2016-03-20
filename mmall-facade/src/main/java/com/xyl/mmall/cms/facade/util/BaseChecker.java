package com.xyl.mmall.cms.facade.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * 
 * @author hzzhanghui
 * 
 */
public class BaseChecker {

	protected Logger logger = null;

	public BaseChecker(Logger logger) {
		this.logger = logger;
	}

	public static class ErrChecker {
		public boolean check = true;

		public String msg = null;
	}

	public ErrChecker checkLong(Long val, String msg) {
		ErrChecker checker = new ErrChecker();
		if (val == null || val == 0) {
			return buildErrChecker(checker, msg);
		}
		return checker;
	}

	public ErrChecker checkObjectNull(Object obj, String msg) {
		ErrChecker checker = new ErrChecker();
		if (obj == null) {
			return buildErrChecker(checker, msg);
		}

		return checker;
	}

	public ErrChecker checkBoolean(Boolean val, String msg) {
		ErrChecker checker = new ErrChecker();
		if (!val) {
			return buildErrChecker(checker, msg);
		}
		return checker;
	}

	public ErrChecker checkStringEmpty(String str, String msg) {
		ErrChecker checker = new ErrChecker();
		if (StringUtils.isBlank(str)) {
			return buildErrChecker(checker, msg);
		}

		return checker;
	}

	@SuppressWarnings("rawtypes")
	public ErrChecker checkListEmpty(List list, String msg) {
		ErrChecker checker = new ErrChecker();
		if (list == null || list.size() == 0) {
			return buildErrChecker(checker, msg);
		}

		return checker;
	}
	
	@SuppressWarnings("rawtypes")
	public ErrChecker checkMapEmpty(Map map, String msg) {
		ErrChecker checker = new ErrChecker();
		if (map == null || map.size() == 0) {
			return buildErrChecker(checker, msg);
		}

		return checker;
	}

	protected ErrChecker buildErrChecker(ErrChecker checker, String msg) {
		checker.msg = msg;
		checker.check = false;
		logger.error(msg);
		return checker;
	}
}
