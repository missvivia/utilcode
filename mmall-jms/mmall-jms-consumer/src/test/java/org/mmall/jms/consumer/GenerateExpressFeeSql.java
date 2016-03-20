package org.mmall.jms.consumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xyl.mmall.framework.enums.ExpressConstant;
import com.xyl.mmall.ip.meta.LocationCode;
import com.xyl.mmall.oms.meta.ExpressFee;

public class GenerateExpressFeeSql {

	private static final String insertSql = "insert into Mmall_Oms_ExpressFee(Id,CodService,ExpressCompanyCode,ExpressCompanyName,Price,"
			+ "SiteId,SiteName,TargetProvinceId,TargetProvinceName,ServiceModeCode,ServiceModeName)values(%s,%s,'%s','%s',%s,%s,'%s',%s,'%s',%s,%s);";

	private static final String ALL_PROVINCE_CHAR = "*";

	private static final long ALL_PROVINCE_ID = 0;

	private static final String OUT_FILE_PATH = "D:\\tmp\\expressFee.sql";

	public static void main(String[] args) {
		String fileName = "src/main/resources/expressFee.xlsx";
		generateSqlForExpressFee(fileName);
	}

	// 物流商 是否COD 站点 目的省份 价格
	public static boolean generateSqlForExpressFee(String fileName) {
		try {
			FileInputStream file = new FileInputStream(new File(fileName));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			int count = 0;
			List<ExpressFee> expressFeeList = new ArrayList<ExpressFee>();

			// location map
			Map<String, LocationCode> locationMap = generateLocationMap();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				if (count++ == 0) {
					continue;
				}

				// For each row, iterate through all the columns
				List<ExpressFee> resultList = fillFieldToExpressFee(row,
						locationMap);
				if (resultList != null && resultList.size() > 0) {
					expressFeeList.addAll(resultList);
				}
			}

			showField(expressFeeList);
			generateSql(expressFeeList);
			check(expressFeeList, locationMap);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static List<ExpressFee> fillFieldToExpressFee(Row row,
			Map<String, LocationCode> locationMap) {

		Iterator<Cell> cellIterator = row.cellIterator();

		int colCount = 0;
		String expressCompanyField = null;
		String codField = null;
		String siteField = null;
		String targetProvinceField = null;
		Double priceField = null;

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (colCount == 0) {
				expressCompanyField = cell.getStringCellValue();
			} else if (colCount == 1) {
				codField = cell.getStringCellValue();
			} else if (colCount == 2) {
				siteField = cell.getStringCellValue();
				checkProvince(siteField, locationMap);
			} else if (colCount == 3) {
				targetProvinceField = cell.getStringCellValue();
			} else if (colCount == 4) {
				priceField = cell.getNumericCellValue();
			} else {
				throw new RuntimeException("只支持,colum值为0,1,2,3,4");
			}
			colCount++;
		}

		if (colCount > 5) {
			throw new RuntimeException("目前只有5列，列值不能大于5,colCount:" + colCount);
		}

		if (StringUtils.isEmpty(expressCompanyField)
				|| StringUtils.isEmpty(codField)
				|| StringUtils.isEmpty(siteField)
				|| StringUtils.isEmpty(targetProvinceField)
				|| priceField == null) {
			throw new RuntimeException("需要的列值不能为空,rowNum:" + row.getRowNum());
		}

		return generateExpressFeeObj(expressCompanyField, codField, siteField,
				targetProvinceField, priceField, locationMap);
	}

	private static List<ExpressFee> generateExpressFeeObj(
			String expressCompanyField, String codField, String siteField,
			String targetProvinceField, Double priceField,
			Map<String, LocationCode> locationMap) {
		List<ExpressFee> expressList = new ArrayList<ExpressFee>();
		String[] targetProvinceArr = targetProvinceField.split("\\/");
		if (targetProvinceArr == null || targetProvinceArr.length <= 0) {
			throw new RuntimeException("目标省份无效,targetProvinceField"
					+ targetProvinceField);
		}
		for (String targetProvince : targetProvinceArr) {
			ExpressFee expressFee = new ExpressFee();

			// 物流公司
			expressFee.setExpressCompanyCode(expressCompanyField);
			expressFee.setExpressCompanyName(ExpressConstant
					.getNameByCode(expressCompanyField));

			// cod字段
			expressFee.setCodService("是".equals(codField));

			// 站点
			LocationCode locationCode = locationMap.get(siteField);
			expressFee.setSiteId(locationCode.getCode());
			expressFee.setSiteName(siteField);

			// 价格
			expressFee.setPrice(new BigDecimal(priceField));

			// 目的省份
			if (ALL_PROVINCE_CHAR.equals(targetProvince)) {
				expressFee.setTargetProvinceId(ALL_PROVINCE_ID);
				expressFee.setTargetProvinceName(ALL_PROVINCE_CHAR);
			} else {
				checkProvince(targetProvince, locationMap);
				LocationCode targetProvinceLocation = locationMap
						.get(targetProvince);
				if (targetProvinceLocation == null) {
					throw new RuntimeException(
							"目标省份，在location中找不到,targetProvince:"
									+ targetProvince);
				}
				expressFee
						.setTargetProvinceId(targetProvinceLocation.getCode());
				expressFee.setTargetProvinceName(targetProvince);
			}
			expressList.add(expressFee);
		}
		return expressList;
	}

	private static Map<String, LocationCode> generateLocationMap()
			throws InstantiationException, IllegalAccessException {
		List<LocationCode> locationList = JdbcOperUtil.getData();
		if (locationList == null || locationList.size() <= 0) {
			throw new RuntimeException("地址表location中找不到地址省份记录");
		}
		Map<String, LocationCode> locationMap = new HashMap<String, LocationCode>();
		for (LocationCode locationCode : locationList) {
			locationMap.put(locationCode.getLocationName(), locationCode);
		}
		return locationMap;
	}

	private static void check(List<ExpressFee> expressFeeList,
			Map<String, LocationCode> locationMap)
			throws InstantiationException, IllegalAccessException {
		if (expressFeeList == null || expressFeeList.size() <= 0) {
			throw new RuntimeException("快递费率记录为空");
		}
	}

	private static void showField(List<ExpressFee> expressFeeList) {
		for (ExpressFee expressFee : expressFeeList) {
			System.out.println(expressFee.getExpressCompanyName() + ","
					+ expressFee.isCodService() + ","
					+ expressFee.getSiteName() + ","
					+ expressFee.getTargetProvinceName() + ","
					+ expressFee.getPrice());
		}
	}

	private static void generateSql(List<ExpressFee> expressFeeList) {
		if (expressFeeList == null || expressFeeList.size() <= 0) {
			throw new RuntimeException("解析到的快递费率为0");
		}
		System.out.println("----------快递费率sql------------");
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < expressFeeList.size(); i++) {
			ExpressFee expressFee = expressFeeList.get(i);
			String content=getInsertSqlByParam(getIdKey(i),
					expressFee.isCodService(),
					expressFee.getExpressCompanyCode(),
					expressFee.getExpressCompanyName(), expressFee.getPrice(),
					expressFee.getSiteId(), expressFee.getSiteName(),
					expressFee.getTargetProvinceId(),
					expressFee.getTargetProvinceName(),
					expressFee.getServiceModeCode(),
					expressFee.getServiceModeName());
			System.out.println(content);
			buffer.append(content+"\n");
		}
		writeToFile(buffer.toString());
	}

	public static boolean writeToFile(String content) {
		boolean flag = true;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(OUT_FILE_PATH);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			flag = false;
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	private static long getIdKey(int count) {
		return Long.valueOf(count + 1);
	}

	private static void checkProvince(String provinceName,
			Map<String, LocationCode> locationMap) {
		if (!locationMap.containsKey(provinceName)
				&& !ALL_PROVINCE_CHAR.equals(provinceName)) {
			throw new RuntimeException("无效省份," + provinceName);
		}
	}

	public static String getInsertSqlByParam(Long Id, boolean CodService,
			String ExpressCompanyCode, String ExpressCompanyName,
			BigDecimal Price, Long SiteId, String SiteName,
			Long TargetProvinceId, String TargetProvinceName,
			String ServiceModeCode, String ServiceModeName) {
		return String.format(insertSql, Id, CodService, ExpressCompanyCode,
				ExpressCompanyName, Price, SiteId, SiteName, TargetProvinceId,
				TargetProvinceName, ServiceModeCode, ServiceModeName);
	}
}