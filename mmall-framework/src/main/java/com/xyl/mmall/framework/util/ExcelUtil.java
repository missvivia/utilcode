/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.framework.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * ExcelUtil.java created by yydx811 at 2015年8月31日 下午12:17:11 这里对类或者接口作简要描述
 *
 * @author yydx811,lhp
 */
public class ExcelUtil {

	private static Logger logger = Logger.getLogger(ExcelUtil.class);

	/**
	 * 读取excel单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		}
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue());
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return null;
		}
		return cell.getStringCellValue();
	}

	/**
	 * 根据excel版本不同 初始化workbook
	 * 
	 * @param file
	 * @return
	 */
	public static Workbook initWorkbook(MultipartFile file) {
		Workbook workbook = null;
		try {
			workbook = new HSSFWorkbook(file.getInputStream());
		} catch (Exception e1) {
			try {
				workbook = new XSSFWorkbook(file.getInputStream());
			} catch (Exception e) {
				logger.error("Can't init workbook!", e);
			}
		}
		return workbook;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	public static boolean isMergedRegion(Sheet sheet, int row, int column) {

		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {

			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, int row, int column) {

		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {

				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getValue(fCell);
				}
			}
		}

		return null;
	}

	/**
	 * 获取合并行单元格的行数
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static int getMergedRowNum(Sheet sheet, int row, int column) {

		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {

				if (column >= firstColumn && column <= lastColumn) {
					return lastRow;
				}
			}
		}
		return 0;
	}
}
