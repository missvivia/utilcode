package com.xyl.mmall.saleschedule.util;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.saleschedule.dto.SearchBaseParamDTO;

public class SalesCommonSqlUtil {

	private static final int DEFAULT_BUFFER_SIZE = 256;

	private static final String BLANK_STR = " ";

	private static final String ORDER_BY_PREFIX = " order by";

	private static final String DESC_PREFIX = " desc";

	private static final String LIMIT_PREFIX = " limit";

	private static final String COL_PREFIX = ",";

	public static String genOrderAndLimitSqlByparam(SearchBaseParamDTO param) {
		StringBuilder buffer = new StringBuilder(DEFAULT_BUFFER_SIZE);

		if (!StringUtils.isEmpty(param.getOrderColumn())) {
			buffer.append(ORDER_BY_PREFIX + BLANK_STR + param.getOrderColumn());
			if (!param.isAsc()) {
				buffer.append(DESC_PREFIX);
			}
		}

		if (param.getOffset() != null && (param.getLimit() != null && param.getLimit() > 0)) {
			// 分页，即传offset，又传limit
			buffer.append(LIMIT_PREFIX + BLANK_STR + param.getOffset() + COL_PREFIX + param.getLimit());
		} else if ((param.getLimit() != null && param.getLimit() > 0) && param.getOffset() == null) {
			// 只要求限制条数，offset为从0开始
			buffer.append(LIMIT_PREFIX + BLANK_STR + param.getLimit());
		}
		return buffer.toString();
	}

}
