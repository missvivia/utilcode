package com.xyl.mmall.excelparse;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;

public class ExcelUtils {
	public static String BLANK = "必填项未录入";

	public static String INVALID = "录入格式错误";

	public static String MAX_LENGTH = "文字必须在_字以内";

	public static String MAX_VALUE = "数值必须小于_";

	public static boolean isRowEmpty(Row row) {
		Iterator<Cell> cellIter = row.cellIterator();
		boolean isRowEmpty = true;
		while (cellIter.hasNext()) {
			Cell cell = cellIter.next();
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				if (cell.getCellType() != Cell.CELL_TYPE_STRING || !StringUtils.isBlank(cell.getStringCellValue())) {
					isRowEmpty = false;
					break;
				}
			}
		}
		return isRowEmpty;
	}

	public static Map<Integer, String> genCellIdxAndFNameMap(Row row) {
		Map<Integer, String> cellIdxAndFNameMap = new TreeMap<Integer, String>();
		int idx = 0;
		Iterator<Cell> iter = row.cellIterator();
		while (iter.hasNext()) {
			Cell cell = iter.next();
			if (cell == null) {
				idx++;
				continue;
			}
			String value = getValueOfCell(cell);
			cellIdxAndFNameMap.put(idx, value);
			idx++;
		}
		return cellIdxAndFNameMap;
	}

	public static String getValueOfCell(Cell cell) {
		String value = null;
		if (cell == null)
			return null;
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			value = NumberToTextConverter.toText(cell.getNumericCellValue());
		} else
			value = cell.toString();

		return value.trim();
	}
}
