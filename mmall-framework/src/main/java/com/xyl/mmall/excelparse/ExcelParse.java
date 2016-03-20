package com.xyl.mmall.excelparse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.activation.Activator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.daojar.util.ReflectUtil;

public class ExcelParse<T> {
	private static final Logger logger = LoggerFactory.getLogger(ExcelParse.class);

	public T getObject(Row row, Class targetClass) throws ExcelParseExeption {
		Object target = null;
		boolean isExcelExp = false;
		List<ExcelParseExceptionInfo> infoList = new ArrayList<ExcelParseExceptionInfo>();
		try {
			target = targetClass.newInstance();
			Field[] fields = targetClass.getDeclaredFields();
			int rowNum = row.getRowNum() + 1;
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				ExcelField excelAnnotation = field.getAnnotation(ExcelField.class);
				if (excelAnnotation == null)
					continue;
				String title = excelAnnotation.title();
				int cellIndex = excelAnnotation.cellIndex();
				Object value = null;
				if (row.getLastCellNum() >= cellIndex) {
					Cell cell = row.getCell(cellIndex);
					try {
						value = getFieldValue(cell, field.getType());
					} catch (Exception e) {
						isExcelExp = true;
						ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, title, ExcelUtils.INVALID);
						infoList.add(expInfo);
						continue;
					}
				}
				boolean required = excelAnnotation.required();
				if (required && (value == null || (value instanceof String && "".equals(value)))) {
					isExcelExp = true;
					ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, title, ExcelUtils.BLANK);
					infoList.add(expInfo);
					continue;
				} else if (required && value instanceof ExcelEnumInterface
						&& ((ExcelEnumInterface) value).getDesc().equals("NULL")) {
					isExcelExp = true;
					ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, title, ExcelUtils.INVALID);
					infoList.add(expInfo);
					continue;
				}
				String max = excelAnnotation.max();
				if (!"-1".equals(max)) {
					if (field.getType() == BigDecimal.class && value != null) {
						BigDecimal maxValue = new BigDecimal(max);
						if (((BigDecimal) value).compareTo(maxValue) > 0) {
							isExcelExp = true;
							String remind = ExcelUtils.MAX_VALUE.replaceAll("_", max);
							ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, title, remind);
							infoList.add(expInfo);
							continue;
						}
					} else if (value != null) {
						int length = Integer.valueOf(max);
						if (((String) value).length() > length && !StringUtils.isBlank((String) value)) {
							isExcelExp = true;
							String remind = ExcelUtils.MAX_LENGTH.replaceAll("_", max);
							ExcelParseExceptionInfo expInfo = new ExcelParseExceptionInfo(rowNum, title, remind);
							infoList.add(expInfo);
							continue;
						}
					}
				}
				Method setMethod = ReflectUtil.genMethod(targetClass, field, false);
				try {
					setMethod.invoke(target, value);
				} catch (IllegalArgumentException | InvocationTargetException e) {
					logger.error(e.getMessage(), e);
					return null;
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		if (isExcelExp) {
			throw new ExcelParseExeption(infoList);
		}
		return (T) target;
	}

	public static Object getFieldValue(Cell cell, Class fieldTypeClass) {
		if (cell == null)
			return null;
		String strVal = null;
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			strVal = NumberToTextConverter.toText(cell.getNumericCellValue());
		} else
			strVal = cell.toString();

		strVal = strVal.trim();
		if (StringUtils.isBlank(strVal))
			return null;
		Object value = null;
		if (ExcelEnumInterface.class.isAssignableFrom(fieldTypeClass)) {
			// CASE1: ExcelEnum值
			value = ((ExcelEnumInterface) fieldTypeClass.getEnumConstants()[0]).getEnumByDesc(strVal);
		} else if (fieldTypeClass == BigDecimal.class) {
			// CASE2: Bigdecimal
			value = new BigDecimal(strVal);
		} else if (fieldTypeClass == Long.class || fieldTypeClass == long.class) {
			// CASE3: LONG
			value = Long.valueOf(strVal);
		} else if (fieldTypeClass == Integer.class || fieldTypeClass == int.class) {
			// CASE4: INT
			value = Integer.valueOf(strVal);
		} else {
			// CASE7: DEFAULT
			value = strVal;
		}
		return value;
	}

}
