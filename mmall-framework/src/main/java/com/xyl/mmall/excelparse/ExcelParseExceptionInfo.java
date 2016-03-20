package com.xyl.mmall.excelparse;

public class ExcelParseExceptionInfo {
	private int rowNum;

	private String columnName;

	private String errMsg;

	public ExcelParseExceptionInfo(int rowNum, String columnName, String errMsg) {
		this.rowNum = rowNum;
		this.columnName = columnName;
		this.errMsg = errMsg;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
