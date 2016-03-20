package com.xyl.mmall;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.SalescheduleConfig;
import com.xyl.mmall.saleschedule.dto.BrandDTO;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SalescheduleConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("production")
public class SinglePrdSqlGenTest {

	static {
		System.setProperty("spring.profiles.active", "production");
	}

	@Resource
	private ScheduleService scheduleService;
	
	@Resource
	private BrandService brandService;

	private static int startId = 0;
	private final static String commonHeader = " INSERT INTO Mmall_Content_PresentProduct(Id,Category,Image,Logo,MarketPrice,OrderBy,SaleAreaId,SalePrice,productName,productId) \n    VALUES(";
	private final static String comonnFooter = "); \n";
	
	private static Map<String, String> imgUrlMap = new ConcurrentHashMap<String,String>();
	
	
	private static int getId() {
		startId = startId+10;
		return startId;
	}
	
	private static void initImgUrlMapping() {
		
		try {
			for (int kk = 0; kk < fileName.length; kk++) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName[kk]), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String lineStr = br.readLine();
				while (lineStr != null) {
					sb.append(lineStr);
					lineStr = br.readLine();
				}
				
				JSONObject json = (JSONObject)JSON.parse(sb.toString());
				JSONArray urlList = json.getJSONArray("result");
				for (int i = 0; i < urlList.size(); i++) {
					JSONObject tmpJson = urlList.getJSONObject(i);
					
					String imgName = tmpJson.getString("imgName");
					imgName = imgName.substring(0, imgName.indexOf("."));
					imgUrlMap.put(imgName, tmpJson.getString("imgUrl"));
				}
			
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 单品更新。排除指定省份
	 */
	@Test
	public void testSingleProductForSomeProvinces() {
		initImgUrlMapping();
		try {
			InputStream is = new FileInputStream("C:\\var\\2.2\\1.30单品更新表.xls");
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			
			List<Bean> beanList = new ArrayList<Bean>();
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	            	System.out.println("第" + numSheet + "个sheet为null！！");
	                continue;
	            }
	            
	            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow == null) {
	                	System.out.println("第" + rowNum + "行为null！！");
	                	continue;
	                }
	                HSSFCell cell0 = hssfRow.getCell(0);
	                if (cell0 == null ) {
	                	System.out.println("第" + numSheet + "个sheet, 第" + rowNum + "行结束！！！本sheet读取完毕！！");
	                	break;
	                }
	                HSSFCell cell1 = hssfRow.getCell(1);
	                long provinceCode = (long)cell0.getNumericCellValue();
	                long productId = (long)cell1.getNumericCellValue();
	                System.out.println("处理： "+ provinceCode + " -- " +productId );
	                
	                Map<String, Object> product = scheduleService.getProductById(productId);
	                if (product == null || product.size() == 0) {
	                	System.out.println("Product " + productId + " not exist!!!");
	                	continue;
	                }
	                
	                long brandId = (long)product.get("brandId");
	                BrandDTO brandDTO = brandService.getBrandByBrandId(brandId);
	                if (brandDTO == null) {
	                	System.out.println("Cannot find any brand info for brandId '" + brandId + "' of product(" +productId+")!!");
	                	continue;
	                }
	                Bean bean = new Bean();
	                bean.id = getId();
	                bean.image = imgUrlMap.get(productId + "");
	                bean.logo = brandDTO.getLogo();
	                bean.category = 1; 
	                bean.MarketPrice = ((BigDecimal)product.get("marketPrice")).doubleValue();
	                bean.OrderBy = rowNum;
	                bean.productId = (int)productId;
	                bean.productName = "商场专柜同步 " + product.get("productName").toString();
	                bean.SaleAreaId = provinceCode;
	                bean.SalePrice = ((BigDecimal)product.get("salePrice")).doubleValue();
	                beanList.add(bean);
	            }
			}
			
			StringBuilder sql = new StringBuilder();
			for (Bean bean2 : beanList) {
				sql.append(commonHeader)
				.append(bean2.id).append(",")
				.append(bean2.category).append(",")
				.append("'" +bean2.image + "'").append(",")
				.append("'" +bean2.logo + "'").append(",")
				.append(bean2.MarketPrice).append(",")
				.append(bean2.OrderBy).append(",")
				.append(bean2.SaleAreaId).append(",")
				.append(bean2.SalePrice).append(",")
				.append("'" +bean2.productName + "'").append(",")
				.append(bean2.productId)
				.append(comonnFooter);
			}
			sql.append("\n");
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:\\var\\activity20150126_update.sql")));
			bw.write(sql.toString());
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static final String fileName[] = new String[]{
			"E:\\xyl\\mmall2\\ws_develop\\mmall-parent\\mmall-saleschedule\\mmall-saleschedule-web\\src\\test\\java\\com\\xyl\\mmall\\imgjson0204.txt"
			};
	private static final String srcExcelFileName = "C:\\var\\2.4\\2.4单品更新表.xls";
	private static final String destSQLFileName = "c:\\var\\activity20150204_update_qg.sql";
	
	/**
	 * 单品更新。全国
	 */
	@Test
	public void testSingleProductUpdateForAllProvinces() {
		initImgUrlMapping();
		initProvinceCodeMap();
		try {
			InputStream is = new FileInputStream(srcExcelFileName);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			
			List<Bean> beanList = new ArrayList<Bean>();
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
	            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
	            if (hssfSheet == null) {
	            	System.out.println("第" + numSheet + "个sheet为null！！");
	                continue;
	            }
	            
	            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
	                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
	                if (hssfRow == null) {
	                	System.out.println("第" + rowNum + "行为null！！");
	                	continue;
	                }
	                HSSFCell cell0 = hssfRow.getCell(0);
	                if (cell0 == null ) {
	                	System.out.println("第" + numSheet + "个sheet, 第" + rowNum + "行结束！！！本sheet读取完毕！！");
	                	break;
	                }
	                long productId = (long)cell0.getNumericCellValue();
	                System.out.println("处理： "+ productId );
	                
	                Map<String, Object> product = scheduleService.getProductById(productId);
	                if (product == null || product.size() == 0) {
	                	System.out.println("Product " + productId + " not exist!!!");
	                	continue;
	                }
	                
	                long brandId = (long)product.get("brandId");
	                BrandDTO brandDTO = brandService.getBrandByBrandId(brandId);
	                if (brandDTO == null) {
	                	System.out.println("Cannot find any brand info for brandId '" + brandId + "' of product(" +productId+")!!");
	                	continue;
	                }
	                Bean bean = new Bean();
	               // bean.id = getId();
	                bean.image = imgUrlMap.get(productId + "");
	                bean.logo = brandDTO.getLogo();
	                bean.category = 1; 
	                bean.MarketPrice = ((BigDecimal)product.get("marketPrice")).doubleValue();
	                bean.OrderBy = rowNum;
	                bean.productId = (int)productId;
	                bean.productName = "商场专柜同步 " + product.get("productName").toString();
	                bean.SalePrice = ((BigDecimal)product.get("salePrice")).doubleValue();
	                beanList.add(bean);
	            }
			}
			
			StringBuilder sql = new StringBuilder();
			for (Iterator<Long> iter = provinceCodeMap.keySet().iterator(); iter.hasNext(); ) {
				long key = iter.next();
				for (Bean bean : beanList) {
					bean.SaleAreaId = key;
					bean.id = getId();
				}
				
				for (Bean bean : beanList) {
					sql.append(commonHeader)
					.append(bean.id).append(",")
					.append(bean.category).append(",")
					.append("'" +bean.image + "'").append(",")
					.append("'" +bean.logo + "'").append(",")
					.append(bean.MarketPrice).append(",")
					.append(bean.OrderBy).append(",")
					.append(bean.SaleAreaId).append(",")
					.append(bean.SalePrice).append(",")
					.append("'" +bean.productName + "'").append(",")
					.append(bean.productId)
					.append(comonnFooter);
				}
				sql.append("\n");
			}
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destSQLFileName)));
			bw.write(sql.toString());
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static Map<Long, Long> provinceCodeMap = new HashMap<>();
	private void initProvinceCodeMap() {
		provinceCodeMap.put(11L, 0x01L);		// 北京
		provinceCodeMap.put(12L, 0x01L << 1);	// 天津
		provinceCodeMap.put(13L, 0x01L << 2);	// 河北省
		provinceCodeMap.put(14L, 0x01L << 3);	// 山西省
		provinceCodeMap.put(15L, 0x01L << 4);	// 内蒙古自治区
		provinceCodeMap.put(21L, 0x01L << 5);	// 辽宁省
		provinceCodeMap.put(22L, 0x01L << 6);	// 吉林省
		provinceCodeMap.put(23L, 0x01L << 7);	// 黑龙江省
		provinceCodeMap.put(31L, 0x01L << 8);	// 上海市
		provinceCodeMap.put(32L, 0x01L << 9);	// 江苏省
		provinceCodeMap.put(33L, 0x01L << 10);	// 浙江省
		provinceCodeMap.put(34L, 0x01L << 11);	// 安徽省
		provinceCodeMap.put(35L, 0x01L << 12);	// 福建省
		provinceCodeMap.put(36L, 0x01L << 13);	// 江西省
		provinceCodeMap.put(37L, 0x01L << 14);	// 山东省
		provinceCodeMap.put(41L, 0x01L << 15);	// 河南省
		provinceCodeMap.put(42L, 0x01L << 16);	// 湖北省
		provinceCodeMap.put(43L, 0x01L << 17);	// 湖南省
		provinceCodeMap.put(44L, 0x01L << 18);	// 广东省
		provinceCodeMap.put(45L, 0x01L << 19);	// 广西壮族自治区
		provinceCodeMap.put(46L, 0x01L << 20);	// 海南省
		provinceCodeMap.put(50L, 0x01L << 21);	// 重庆市
		provinceCodeMap.put(51L, 0x01L << 22);	// 四川省
		provinceCodeMap.put(52L, 0x01L << 23);	// 贵州省
		provinceCodeMap.put(53L, 0x01L << 24);	// 云南省
		provinceCodeMap.put(54L, 0x01L << 25);	// 西藏自治区
		provinceCodeMap.put(61L, 0x01L << 26);	// 陕西省
		provinceCodeMap.put(62L, 0x01L << 27);	// 甘肃省
		provinceCodeMap.put(63L, 0x01L << 28);	// 青海省
		provinceCodeMap.put(64L, 0x01L << 29);	// 宁夏回族自治区
		provinceCodeMap.put(65L, 0x01L << 30);	// 新疆维吾尔族自治区
	}
}

class Bean {
	public int id;
	public int category = 0;
	public String image = "";
	public String logo = "";
	public double MarketPrice = 0;
	public long SaleAreaId;
	public int OrderBy = 1;
	public double SalePrice = 0;
	public String productName = "";
	public int productId = 0;
	
}