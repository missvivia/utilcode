package com.xyl.mmall.excelparse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.netease.print.daojar.util.ReflectUtil;

public class XLSExport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4891332153592637258L;

	// 设置cell编码解决中文高位字节截断
	private static short XLS_ENCODING = HSSFCell.ENCODING_UTF_16;

	// // 定制日期格式
	// private static String DATE_FORMAT = " mm/dd/yy "; // "m/d/yy h:mm"

	// 定制浮点数格式
	private static String NUMBER_FORMAT = " #,##0.00 ";

	private String xlsFileName;

	private HSSFWorkbook workbook;

	private HSSFSheet sheet;

	private HSSFRow row;

	/**
	 * 初始化Excel
	 * 
	 * @param fileName
	 *            导出文件名
	 */
	public XLSExport(String fileName) {
		this.xlsFileName = fileName;
		this.workbook = new HSSFWorkbook();
	}

	public XLSExport(InputStream ins) {
		try {
			this.workbook = new HSSFWorkbook(ins);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化Excel
	 * 
	 * @param fileName
	 *            导出文件名
	 */
	public XLSExport(String fileName, String sheetName) {
		this.xlsFileName = fileName;
		this.workbook = new HSSFWorkbook();
		this.sheet = workbook.createSheet(sheetName);
	}

	public boolean changeSheet(String name) {
		HSSFSheet tmpSheet = workbook.getSheet(name);
		if (tmpSheet == null)
			return false;
		else {
			this.sheet = tmpSheet;
			return true;
		}
	}
	
	public boolean changeSheet(int index) {
		HSSFSheet tmpSheet = workbook.getSheetAt(index);
		if (tmpSheet == null)
			return false;
		else {
			this.sheet = tmpSheet;
			return true;
		}
	}

	public void createSheet(String name) {
		if (this.workbook == null) {
			this.workbook = new HSSFWorkbook();
		}
		HSSFSheet tmpSheet = workbook.getSheet(name);
		if (tmpSheet == null)
			this.sheet = workbook.createSheet(name);
		else {
			this.sheet = tmpSheet;
		}
	}

	/**
	 * 导出Excel文件
	 * 
	 * @throws XLSException
	 */
	public void exportXLS() {
		try {
			FileOutputStream fOut = new FileOutputStream(xlsFileName);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	/**
	 * 导出Excel文件
	 * 
	 * @throws XLSException
	 */
	public void exportXLS(OutputStream out) {
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public void createSheet(int index, String sheetName) {
		this.sheet = this.workbook.createSheet(sheetName);
	}

	/**
	 * 增加一行
	 * 
	 * @param index
	 *            行号
	 */
	public void createRow(int index) {
		this.row = this.sheet.createRow(index);
	}

	public void deleteRow(HSSFRow row) {
		if (this.sheet == null)
			throw new RuntimeException("sheet is null!");
		this.sheet.removeRow(row);
	}

	public void deleteRow(int index){
		if (this.sheet == null)
			throw new RuntimeException("sheet is null!");
		deleteRow(sheet.getRow(index));
	}
	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, String value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellType(XLS_ENCODING);
		cell.setCellValue(value);
	}

	public void setCell(int index, String value, HSSFCellStyle style) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellType(XLS_ENCODING);
		cell.setCellStyle(style);
		cell.setCellValue(value);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, Calendar value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(XLS_ENCODING);
		cell.setCellValue(value.getTime());
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		// cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));
		// // 设置cell样式为定制的日期格式
		cell.setCellStyle(cellStyle); // 设置该cell日期的显示格式
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, int value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	/**
	 * 设置单元格
	 * 
	 * @param index
	 *            列号
	 * @param value
	 *            单元格填充值
	 */
	public void setCell(int index, double value) {
		HSSFCell cell = this.row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		HSSFCellStyle cellStyle = workbook.createCellStyle(); // 建立新的cell样式
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT)); // 设置cell样式为定制的浮点数格式
		cell.setCellStyle(cellStyle); // 设置该cell浮点数的显示格式
	}

	public HSSFRow getHeaderBySheetName(String name) {
		HSSFSheet sheet = this.workbook.getSheet(name);
		if (sheet != null)
			return sheet.getRow(0);
		return null;
	}

	public InputStream getInputStream() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}

	public HSSFCellStyle createStyle() {
		return workbook.createCellStyle();

	}

	public void setRowHight(float height) {
		if (this.row != null)
			this.row.setHeightInPoints(height);
	}

	public void createEXCEL(List<?> data, Class type) {
		if (this.workbook == null) {
			this.workbook = new HSSFWorkbook();
		}
		if (this.sheet == null)
			this.sheet = workbook.createSheet();
		Field[] fields = type.getDeclaredFields();
		int rowOffset = 0;
		this.createRow(rowOffset++);
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			ExcelExportField excelAnnotation = field.getAnnotation(ExcelExportField.class);
			if (excelAnnotation == null)
				continue;
			int cellIndex = excelAnnotation.cellIndex();
			String title = excelAnnotation.desc();
			this.setCell(cellIndex, title);
		}

		if (data != null && data.size() > 0) {
			for (Object obj : data) {
				this.createRow(rowOffset++);
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					ExcelExportField excelAnnotation = field.getAnnotation(ExcelExportField.class);
					if (excelAnnotation == null)
						continue;
					int cellIndex = excelAnnotation.cellIndex();
					Method getMethod = ReflectUtil.genMethod(type, field, true);
					Object result = null;

					try {
						result = getMethod.invoke(obj);
					} catch (Exception e) {
						throw new RuntimeException("create excel failed");
					}
					if (result != null) {
						String val = result.toString();
						if (!StringUtils.isBlank(val)) {
							this.setCell(cellIndex, val);
						}
					}

				}
			}
		}
	}

	public String getXlsFileName() {
		return xlsFileName;
	}

	public void setXlsFileName(String xlsFileName) {
		this.xlsFileName = xlsFileName;
	}

}