/**
 * 
 */
package com.xyl.mmall.framework.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.activation.UnsupportedDataTypeException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.SheetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author jmy
 *
 */
public class ExcelDataExtracter implements IExcelDataExtracter {
	/**只包含字母、下划线或数字且不以数字开头的字符串的正则表达式*/
	public static final String REGEX_VALID_ID = "^[a-zA-Z_]\\w*$";
	public static final Pattern PATTERN_VALID_ID = Pattern.compile(REGEX_VALID_ID);
	public static final String ID_TYPE_DELIMITER = ":";
	
	/**Excel中定义元数据的sheet名称*/
	public static final String METADATA_SHEET_NAME = "MetaData";
	public static final int METADATA_FIRST_ROW = 2;
	public static final int METADATA_FIRST_COL = 0;
	/**数据表格的表头行数，当前为两行，第一行为columnId:DataType定义，第二行为表头说明*/
	public static final int METADATA_TABLE_HEADER_ROWNUM = 2;

	private static final Logger logger = LoggerFactory.getLogger(ExcelDataExtracter.class);
	
	private Workbook wb;
	private Sheet metaDataSheet;
	private Map<String, MetaData> metadata;
	private Map<String, Object> data;
	
	/**
	 * 从输入流中读入Excel数据文件，元信息保存在名为{@value METADATA_SHEET_NAME}}的或第一个sheet中，元数据配置从第三行(rowIndex=2)开始。
	 * 
	 * @param inp
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws IllegalConfigException 
	 */
	public ExcelDataExtracter(File file) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalConfigException {
		try (InputStream inp = new FileInputStream(file)) {
			this.wb = WorkbookFactory.create(inp);
			this.metaDataSheet = this.findMetaDataSheet(this.wb);
			this.extractMetaData();
			this.extractAllData();
		}
	}
	
	public ExcelDataExtracter(File file, String passwd) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalConfigException {
		try (InputStream inp = new FileInputStream(file)) {
			this.wb = WorkbookFactory.create(inp, passwd);
			this.metaDataSheet = this.findMetaDataSheet(this.wb);
			this.extractMetaData();
			this.extractAllData();
		}
	}
	
	/**
	 * 从输入流中读入Excel数据文件，元信息保存在名为{@value METADATA_SHEET_NAME}}的或第一个sheet中，元数据配置从第三行(rowIndex=2)开始。
	 * <p>
	 * 调用方维护输入流构造参数<code>inp</code>的资源是否释放
	 * 
	 * @param inp
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws IllegalConfigException 
	 */
	public ExcelDataExtracter(InputStream inp) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalConfigException {
		this.wb = WorkbookFactory.create(inp);
		this.metaDataSheet = this.findMetaDataSheet(this.wb);
		this.extractMetaData();
		this.extractAllData();
	}
	
	public ExcelDataExtracter(InputStream inp, String passwd) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalConfigException {
		this.wb = WorkbookFactory.create(inp, passwd);
		this.metaDataSheet = this.findMetaDataSheet(this.wb);
		this.extractMetaData();
		this.extractAllData();
	}
	
	protected MetaData validateMetaData(Row row) throws IllegalConfigException, UnsupportedDataTypeException {
		MetaData md = new MetaData();
		Cell cId = row.getCell(METADATA_FIRST_COL);
		String id = validateIDCell(cId);
		if (id == null) return null;
		md.setId(id);
		Cell cLayout = row.getCell(METADATA_FIRST_COL + 1);
		Layout layout = validateLayoutCell(cLayout);
		md.setLayout(layout == null ? Layout.SINGLE : layout);
		Cell cDataType = row.getCell(METADATA_FIRST_COL + 2);
		Cell cContraint = row.getCell(METADATA_FIRST_COL + 3);
		Constraint constraint = this.validateConstraint(cContraint);
		md.setConstraint(constraint == null ? Constraint.NULLABLE : constraint);
		Cell cValue = row.getCell(METADATA_FIRST_COL + 4);
		Cell cStart = row.getCell(METADATA_FIRST_COL + 5);
		Cell cEnd = row.getCell(METADATA_FIRST_COL + 6);
		Cell cDesc = row.getCell(METADATA_FIRST_COL + 7);
		DataType dataType = null;
		CellReference crStart = null;
		CellReference crEnd = null;
		int firstRowIndex, firstColIndex, lastRowIndex, lastColIndex;
		switch (layout) {
			case SINGLE:
				dataType = validateDataType(cDataType);
				dataType = dataType == null ? DataType.STRING : dataType;
				md.setDataType(dataType);
				
				//取value值
				Object value = this.getCellRealValue(cValue, dataType);
				if (value == null) {
					crStart = validateCellReference(cStart);
					value = this.getCellRealValue(cStart, dataType);
				}
				md.setValue(value);
				break;
			case ARRAY:
				dataType = validateDataType(cDataType);
				dataType = dataType == null ? DataType.STRING : dataType;
				md.setDataType(dataType);
				crStart = validateCellReference(cStart);
				//如果start没有定义，不再解析end
				if (crStart == null) break;
				crEnd = validateCellReference(cEnd);	
				if (crEnd == null) {
					String msg = String.format("第%s行变量%s的end配置项未定义", row.getRowNum() + 1, id);
					throw new IllegalConfigException(msg);
				}
				firstRowIndex = crStart.getRow();
				firstColIndex = crStart.getCol();
				lastRowIndex = crEnd.getRow();
				lastColIndex = crEnd.getCol();
				if (!(lastRowIndex == firstRowIndex && lastColIndex >= firstColIndex) 
						&& !(lastRowIndex >= firstRowIndex && lastColIndex == firstColIndex)) {
					String msg = String.format("数组变量%sstart[%s]和end[%s]配置，必须是单行或单列且分别为首（左或上）尾（右或下）单元格", id, crStart.formatAsString(), crEnd.formatAsString());
					throw new IllegalConfigException(msg);
				}
				md.setStart(crStart);
				md.setEnd(crEnd);
				break;
			case TABLE:
				crStart = validateCellReference(cStart);
				//如果start没有定义，不再解析end和column
				if (crStart == null) break;
				crEnd = validateCellReference(cEnd);	
				if (crEnd == null) {
					String msg = String.format("第%s行变量%s的end配置项未定义", row.getRowNum() + 1, id);
					throw new IllegalConfigException(msg);
				}
				
				//判断是否在同一个sheet中
				Sheet refSheet = null;
				if (crStart.getSheetName() == null) {
					refSheet = this.metaDataSheet;
				} else {
					refSheet = this.wb.getSheet(crStart.getSheetName());
					if (refSheet == null) {
						String msg = String.format("第%s行变量%s的Layout为table的start配置项找不到引用sheet[%s]", row.getRowNum() + 1, id, crStart.getSheetName());
						throw new IllegalConfigException(msg);
					}
				}
				if (!refSheet.getSheetName().equals(crEnd.getSheetName() == null ? this.metaDataSheet.getSheetName() : crEnd.getSheetName())) {
					String msg = String.format("第%s行变量%s的Layout为table的end配置项与start不在同一sheet[%s]内", row.getRowNum() + 1, id, crStart.getSheetName());
					throw new IllegalConfigException(msg);
				}
				
				//校验start和end是否分别为左上和右下单元格引用
				firstRowIndex = crStart.getRow();
				firstColIndex = crStart.getCol();
				lastRowIndex = crEnd.getRow();
				lastColIndex = crEnd.getCol();
				if (lastRowIndex < firstRowIndex || lastColIndex < firstColIndex) {
					String msg = String.format("第%s行变量%s的Layout为table的start[%s]和end[%s]配置，必须分别是左上单元格和右下单元格", row.getRowNum() + 1, id, crStart.formatAsString(), crEnd.formatAsString());
					throw new IllegalConfigException(msg);
				}
				
				md.setStart(crStart);
				md.setEnd(crEnd);
				
				//抽取table列的元数据
				Row refHeaderRow = refSheet.getRow(firstRowIndex);
				Row refHeaderDescRow = refSheet.getRow(firstRowIndex+1);
				Cell refHeaderCell = null;
				List<MetaData> lstTableMD = new ArrayList<>(lastColIndex-firstColIndex+1);
				boolean hasTableData = (lastRowIndex - firstRowIndex) > (METADATA_TABLE_HEADER_ROWNUM-1);
				try {
					//遍历数据表格定义columnId:DataType的行头单元格
					for (int c = firstColIndex; c <= lastColIndex; c ++) {
						MetaData cMd = new MetaData();
						refHeaderCell = refHeaderRow.getCell(c);
						String[] colDef = refHeaderCell.getStringCellValue().split(ID_TYPE_DELIMITER);
						if (colDef.length == 0 || colDef[0] == null || colDef[0].trim().length() == 0) {
							throw new IllegalConfigException("未定义列变量ID");
						}
						cMd.setId(colDef[0]);
						if (colDef.length < 2 || colDef[1] == null || colDef[1].trim().length() == 0) {
							cMd.setDataType(DataType.STRING);
						} else {
							cMd.setDataType(DataType.valueOf(colDef[1].toUpperCase()));
						}
						if (colDef.length < 3 || colDef[2] == null || colDef[2].trim().length() == 0) {
							cMd.setConstraint(Constraint.NULLABLE);
						} else {
							cMd.setConstraint(Constraint.valueOf(colDef[2].toUpperCase()));
						}
						cMd.setLayout(Layout.ARRAY);
						if (hasTableData) {
							cMd.setStart(new CellReference(refSheet.getSheetName(), firstRowIndex+METADATA_TABLE_HEADER_ROWNUM, c, false, false));
							cMd.setEnd(new CellReference(refSheet.getSheetName(), lastRowIndex, c, false, false) );
						}
						Object descValue = this.getCellRealValue(refHeaderDescRow.getCell(c), DataType.STRING);
						cMd.setDesc(descValue == null ? null : descValue.toString());
						lstTableMD.add(cMd);
					}
				} catch (Exception e) {
					Object headValue = this.getCellSimpleValue(refHeaderCell);
					String msg = String.format("%s单元格定义错误：%s", new CellReference(refSheet.getSheetName(), refHeaderCell.getRowIndex(), refHeaderCell.getColumnIndex(), false, false).formatAsString(), headValue);
					throw new IllegalConfigException(msg);
				}
				md.setSubMetaData(lstTableMD);//设置表头定义元数据
				break;
			default:
				throw new UnsupportedDataTypeException(layout.name());
		}
		Object desc = this.getCellRealValue(cDesc, DataType.STRING);
		md.setDesc(desc == null ? null : desc.toString());
		return md;
	}
	
	/**
	 * 获取指定单元格相应类型的数据对象
	 * @param cell
	 * @param dataType
	 * @return
	 * @throws IllegalConfigException
	 * @throws UnsupportedDataTypeException 
	 */
	protected Object getCellRealValue(Cell cell, DataType dataType) throws IllegalConfigException, UnsupportedDataTypeException {
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;
		if (dataType == null) {
			CellReference cr = new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false);
			String msg =  String.format("%s未定义数据类型", cr.formatAsString());
			throw new IllegalConfigException(msg);
		}
		try {
			switch (dataType) {
				case  STRING:
					try {
						return cell.getStringCellValue();
					} catch (IllegalStateException e) {
						Object v = this.getCellSimpleValue(cell);
						if (v == null) return null;
						if (v instanceof Double) {
							return v.toString().replaceAll("\\.0+$", "");//去除浮点数时小数点及后面所有连续至尾部的0
						} else {
							return null;
						}
					}
				case  NUMERIC:
					Double num = cell.getNumericCellValue();
					if (num != null && num.toString().matches("\\d+\\.0+$")) {//如果小数为0，返回Long类型
						return num.longValue();
					}
					return num;
				case  DATE:
					return cell.getDateCellValue();
				default:
					throw new UnsupportedDataTypeException(dataType.name());
			}
		} catch (IllegalStateException e) {
			CellReference cr = new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false);
			String msg =  String.format("%s数据[%s]错误，与声明数据类型[%s]不一致", cr.formatAsString(), this.getCellSimpleValue(cell), dataType);
			throw new IllegalConfigException(msg, e);
		}
	}
	
	protected Object getCellSimpleValue(Cell cell) throws UnsupportedDataTypeException {
		if (cell == null) return null;
		switch(cell.getCellType()) {
			case	Cell.CELL_TYPE_BLANK:
				return null;
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_ERROR:
				return cell.getErrorCellValue();
			default:
				throw new UnsupportedDataTypeException("CELL_TYPE: " + cell.getCellType());
		}
	}

	protected Constraint validateConstraint(Cell cell) throws IllegalConfigException {
		if (cell == null) return null;
		try {
			if ((cell.getCellType() == Cell.CELL_TYPE_STRING 
					|| cell.getCellType() == Cell.CELL_TYPE_FORMULA)) {
				return Constraint.valueOf(cell.getStringCellValue().toUpperCase());
			}
		} catch (Exception e) {
			String msg = String.format("%s必填且只能选择%s", new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false).formatAsString(), Arrays.toString(DataType.values()));
			logger.warn(msg);
			throw new IllegalConfigException(msg, e);
		}
		return null;
	}

	protected CellReference validateCellReference(Cell cell) throws IllegalConfigException {
		if (cell == null) return null;
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				return new CellReference(cell.getCellFormula());
			}
		} catch (Exception e) {
			String msg = String.format("%s必须是单元格引用", new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false).formatAsString());
			logger.warn(msg);
			throw new IllegalConfigException(msg, e);
		}
		return null;
	}

	protected DataType validateDataType(Cell cell) throws IllegalConfigException {
		if (cell == null) return null;
		try {
			if ((cell.getCellType() == Cell.CELL_TYPE_STRING 
					|| cell.getCellType() == Cell.CELL_TYPE_FORMULA)) {
				return DataType.valueOf(cell.getStringCellValue().toUpperCase());
			}
		} catch (Exception e) {
			String msg = String.format("%s必填且只能选择%s", new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false).formatAsString(), Arrays.toString(DataType.values()));
			logger.warn(msg);
			throw new IllegalConfigException(msg, e);
		}
		return null;
	}

	protected Layout validateLayoutCell(Cell cell) throws IllegalConfigException {
		if (cell == null) return null;
		try {
			if ((cell.getCellType() == Cell.CELL_TYPE_STRING 
					|| cell.getCellType() == Cell.CELL_TYPE_FORMULA)) {
				return Layout.valueOf(cell.getStringCellValue().toUpperCase());
			}
		} catch (Exception e) {
			String msg = String.format("%s只能选择%s", new CellReference(cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), false, false).formatAsString(), Arrays.toString(Layout.values()));
			logger.warn(msg);
			throw new IllegalConfigException(msg, e);
		}
		return null;
	}
	
	protected String validateIDCell(Cell cell) throws IllegalConfigException {
		if (cell == null) return null;
		try {
			if (!((cell.getCellType() == Cell.CELL_TYPE_STRING 
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA)
					&& PATTERN_VALID_ID.matcher(cell.getStringCellValue()).matches())) {
				String msg = String.format("第%s行ID变量名必须只包含字母、下划线或数字且不以数字开头", cell.getRowIndex()+1);
				logger.warn(msg);
				throw new IllegalConfigException(msg);
			}
		} catch (Exception e) {
			String msg = String.format("第%s行ID变量名必须只包含字母、下划线或数字且不以数字开头", cell.getRowIndex()+1);
			logger.warn(msg);
			throw new IllegalConfigException(msg, e);
		}
		return cell.getStringCellValue().trim();
	}
	
	/**
	 * 从WorkBook中获取名为{@value METADATA_SHEET_NAME}的定义元信息的sheet，若无，返回第一个sheet。
	 * @param wb
	 * @return
	 */
	protected Sheet findMetaDataSheet(Workbook wb) {
		Sheet metadataSheet = wb.getSheet(METADATA_SHEET_NAME);
		if (metadataSheet != null) {
			return metadataSheet;
		} 
		return wb.getSheetAt(0);
	}

	protected void extractMetaData() throws IllegalConfigException, UnsupportedDataTypeException {
		int lastRowNum = this.metaDataSheet.getLastRowNum();
		int iMDSize = lastRowNum - METADATA_FIRST_ROW +1;//元数据定义sheet元数据行数
		if (iMDSize < 1) return;
		Map<String, MetaData> temp = new LinkedHashMap<>(iMDSize);
		for  (int r = METADATA_FIRST_ROW; r <= lastRowNum; r ++) {
			Row row = this.metaDataSheet.getRow(r);
			Cell cell = row.getCell(METADATA_FIRST_COL);
			String id = this.validateIDCell(cell);
			if (id == null) continue;
			if (temp.containsKey(id)) {
				String msg = String.format("第%s行ID[%s]重复", r+1, id);
				throw new IllegalConfigException(msg);
			}
			MetaData md = this.validateMetaData(row);
			if (md != null) {
				temp.put(id, md);
			}			
		}
		this.metadata = temp;
	}

	protected void extractAllData() throws IllegalConfigException, UnsupportedDataTypeException {	
		Map<String, Object> temp = new LinkedHashMap<>(this.metadata.size());
		for  (Iterator<Entry<String, MetaData>> itr = this.metadata.entrySet().iterator(); itr.hasNext();) {
			Entry<String, MetaData> entry = itr.next();
			temp.put(entry.getKey(), this.extractData(entry.getValue()));
		}
		this.data = temp;
	}

	protected Object extractData(MetaData md) throws IllegalConfigException, UnsupportedDataTypeException {
		Constraint constraint = md.getConstraint();
		switch (md.getLayout()) {
			case	SINGLE:
				Object singleValue = md.getValue();
				if ((constraint == Constraint.NONNULL && singleValue == null) 
						||(DataType.STRING == md.getDataType() && constraint == Constraint.NONEMPTY && (singleValue == null || singleValue.toString().trim().length() == 0))) {
					String msg = String.format("%s变量定义不满足约束条件：%s", md.getId(), constraint.name());
					throw new IllegalConfigException(msg);
				}
				return singleValue;
			case ARRAY:
				List<Object> lstValue = this.getArrayData(md);
				if ((constraint == Constraint.NONNULL || constraint == Constraint.NONEMPTY) && lstValue == null) {
					String msg = String.format("%s变量定义不满足约束条件：%s", md.getId(), constraint.name());
					throw new IllegalConfigException(msg);
				}
				if (constraint == Constraint.NONEMPTY) {
					boolean hasNonnullEle = false;
					Iterator<Object> itr = lstValue.listIterator();
					while (itr.hasNext()) {
						if (itr.next() != null) {
							hasNonnullEle = true;
							break;
						}
					}
					if (!hasNonnullEle) {
						String msg = String.format("%s变量定义不满足约束条件：%s", md.getId(), constraint.name());
						throw new IllegalConfigException(msg);
					}
				}
				return lstValue;
			case TABLE:
				List<Map<String, Object>> tableValue= this.getTableData(md);
				if ((constraint == Constraint.NONNULL || constraint == Constraint.NONEMPTY) && tableValue == null) {
					String msg = String.format("%s变量定义不满足约束条件：%s", md.getId(), constraint.name());
					throw new IllegalConfigException(msg);
				}
				if (constraint == Constraint.NONEMPTY) {
					boolean hasNonnullEle = false;
					Iterator<Map<String, Object>> itr = tableValue.listIterator();
					while (itr.hasNext()) {
						if (itr.next() != null) {
							hasNonnullEle = true;
							break;
						}
					}
					if (!hasNonnullEle) {
						String msg = String.format("%s变量定义不满足约束条件：%s", md.getId(), constraint.name());
						throw new IllegalConfigException(msg);
					}
				}
				return tableValue;
			default:
				throw new UnsupportedDataTypeException(md.getLayout().name());
		}
	}
	
	/**
	 * 根据数组元数据获取对应数组数据
	 * @param md
	 * @return
	 * @throws IllegalConfigException
	 * @throws UnsupportedDataTypeException 
	 */
	protected List<Object> getArrayData(MetaData md) throws IllegalConfigException, UnsupportedDataTypeException {
		if (md.getStart() == null) return null;
		int len = md.getEnd().getRow() - md.getStart().getRow() + md.getEnd().getCol() - md.getStart().getCol() + 1;//数组长度
		List<Object> lstArr = new ArrayList<>(len);
		Sheet sheet = this.wb.getSheet(md.getStart().getSheetName());
		for (int r = md.getStart().getRow(), lastRow = md.getEnd().getRow(), index = 0; r <= lastRow && index < len; r ++) {
			for (int c = md.getStart().getCol(), lastCol = md.getEnd().getCol(); c <= lastCol && index < len; c ++, index ++) {
				Object v = this.getCellRealValue(SheetUtil.getCellWithMerges(sheet, r, c), md.getDataType());
				if (v != null) {
					lstArr.add(v);
				}
			}
		}
		return lstArr;
	}
	
	/**
	 * 根据列表元数据获取对应表格数据
	 * @param md
	 * @return
	 * @throws IllegalConfigException
	 * @throws UnsupportedDataTypeException 
	 */
	protected List<Map<String, Object>> getTableData(MetaData md) throws IllegalConfigException, UnsupportedDataTypeException {
		if (md.getStart() == null) return null;
		Sheet sheet = this.wb.getSheet(md.getStart().getSheetName());//列表数据所在sheet
		int rowNum = md.getEnd().getRow() - md.getStart().getRow() - METADATA_TABLE_HEADER_ROWNUM + 1;//行数
		int colNum = md.getEnd().getCol() - md.getStart().getCol() + 1;//列数
		List<Map<String, Object>> table = new ArrayList<>(rowNum);//数据列表
		MetaData[] subMd = md.getSubMetaData().toArray(new MetaData[0]);//列的元数据
		Map<String, Object> mapRow = null;//行数据
		StringBuilder sb = new StringBuilder();
		for (int r = md.getStart().getRow() + METADATA_TABLE_HEADER_ROWNUM, lastRow = md.getEnd().getRow(); r <= lastRow; r ++) {
			mapRow = new LinkedHashMap<>(colNum);
			for (int c = md.getStart().getCol(), lastCol = md.getEnd().getCol(), index = 0; c <= lastCol; c ++, index ++) {
				Cell cell = SheetUtil.getCellWithMerges(sheet, r, c);
				Object v = this.getCellRealValue(cell, subMd[index].getDataType());
				if (v != null) {//空值不存储
					mapRow.put(subMd[index].getId(), v);
					if (subMd[index].getConstraint() == Constraint.NONEMPTY && v.toString().length() == 0) {
						sb.append(String.format("%s变量定义不满足约束条件：%s", new CellReference(sheet.getSheetName(), r, c, false, false).formatAsString(), subMd[index].getConstraint().name()));
					}
				} else if (subMd[index].getConstraint() == Constraint.NONNULL || subMd[index].getConstraint() == Constraint.NONEMPTY) {
					sb.append(String.format("%s变量定义不满足约束条件：%s", new CellReference(sheet.getSheetName(), r, c, false, false).formatAsString(), subMd[index].getConstraint().name()));
				}
			}
			if (!mapRow.isEmpty()) {//跳过空行
				String errMsg = sb.toString();
				if (errMsg.trim().length() != 0) {
					throw new IllegalConfigException(errMsg);
				}
				table.add(mapRow);
			}
		}
		return table;
	}

	@Override
	public Map<String, MetaData> getMetaData() {
		return this.metadata;
	}

	@Override
	public Map<String, Object> getAllData() {
		return this.data;
	}

	@Override
	public Object getData(String id) {
		return this.data.get(id);
	}

	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException, IllegalConfigException {
		IExcelDataExtracter xlsx = new ExcelDataExtracter(new File("src/main/resources/poi/data.xlsx"));
		System.out.println(JSON.toJSONString(xlsx.getMetaData(), SerializerFeature.SortField));
		System.out.println(JSON.toJSONString(xlsx.getAllData(), SerializerFeature.SortField));
		
		IExcelDataExtracter xls = new ExcelDataExtracter(new File("src/main/resources/poi/data.xls"));
		System.out.println(JSON.toJSONString(xls.getMetaData(), SerializerFeature.SortField));
		System.out.println(JSON.toJSONString(xls.getAllData(), SerializerFeature.SortField));
		
		IExcelDataExtracter wap = new ExcelDataExtracter(new File("src/main/resources/poi/wapIndex.xlsx"));
		System.out.println(JSON.toJSONString(wap.getMetaData(), SerializerFeature.SortField));
		System.out.println(JSON.toJSONString(wap.getAllData(), SerializerFeature.SortField));
	}
	
}
