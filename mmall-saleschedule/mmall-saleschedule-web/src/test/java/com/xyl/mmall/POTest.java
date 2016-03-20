package com.xyl.mmall;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.xyl.mmall.SalescheduleConfig;
import com.xyl.mmall.framework.util.ProvinceCodeMapUtil;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SalescheduleConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("production")
public class POTest {

	static {
		System.setProperty("spring.profiles.active", "production");
	}

	@Resource
	private ScheduleService scheduleService;
	
	/**
	 * 档期的销售站点改成全国。
	 * 排除某些省份。
	 */
	@Test
	public void modifySaleSiteForPartQG(){
		initProvinceCodeMap();
		
		StringBuilder sb = new StringBuilder();
		try {
 			InputStream is = new FileInputStream("C:\\var\\修改PO表_修改为全国20150126.xls");
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			
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
	                HSSFCell cell0 = hssfRow.getCell(1);
	                if (cell0 == null ) {
	                	System.out.println("第" + numSheet + "个sheet, 第" + rowNum + "行结束！！！本sheet读取完毕！！");
	                	break;
	                }
	                long poId = (long)cell0.getNumericCellValue();
	                sb.append(poId + ",");
	
	                HSSFCell cell4 = hssfRow.getCell(4);
	                String excludeAreaNameList = cell4.getStringCellValue();
	                System.out.println("Deal: " + poId + " -- " + excludeAreaNameList);
	                if (excludeAreaNameList != null && !"".equals(excludeAreaNameList.trim())) {
	                	String[] arr = excludeAreaNameList.split("~");
	 	                List<Long> excludeCodeList = new ArrayList<Long>();
	 	                for (int i = 0; i < arr.length; i++) {
	 	                	excludeCodeList.add(provinceNameCodeMap.get(arr[i]));
	 	                }
	 	                
	 	                List<Long> qgCodeList = getQGCodeList();
	 	                qgCodeList.removeAll(excludeCodeList);
	 	                
	 	                long fmt = ProvinceCodeMapUtil.getProvinceFmtByCodeList(qgCodeList);
	 	                System.out.println("PO=" + poId + "; fmt=" + fmt + "; codeList=" + qgCodeList + "; size=" + qgCodeList.size()  + "\n");
	 	                scheduleService.updatePOSaleSite(poId, fmt, qgCodeList);
	                } else {
	                	List<Long> qgCodeList = getQGCodeList();
		 	            long fmt = ProvinceCodeMapUtil.getProvinceFmtByCodeList(qgCodeList);
		 	            System.out.println("PO=" + poId + "; fmt=" + fmt + "; codeList=" + qgCodeList + "; size=" + qgCodeList.size()  + "\n");
		 	            scheduleService.updatePOSaleSite(poId, fmt, qgCodeList);
	                }
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(sb.toString());
	}
	
	/**
	 * 档期的销售区域改成全国。
	 */
	@Test
	public void modifySaleSiteForQG(){
		initProvinceCodeMap();
		Long[] arr = new Long[]{6018L, 6026L,6027L,6017L,6021L};
		try {
			for (long poId : arr) {
				List<Long> qgCodeList = getQGCodeList();
	            long fmt = ProvinceCodeMapUtil.getProvinceFmtByCodeList(qgCodeList);
	            System.out.println("PO=" + poId + "; fmt=" + fmt + "; codeList=" + qgCodeList + "; size=" + qgCodeList.size()  + "\n");
	            scheduleService.updatePOSaleSite(poId, fmt, qgCodeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private List<Long> getQGCodeList() {
		List<Long> qgList = new ArrayList<Long>();
		for (Iterator<Long> iter = provinceCodeMap.keySet().iterator(); iter.hasNext();) {
			qgList.add(iter.next());
		}
		
		return qgList;
	}
	
	private static Map<Long, Long> provinceCodeMap = new HashMap<>();
	private static Map<String, Long> provinceNameCodeMap = new HashMap<>();
	
	private void initProvinceCodeMap() {
		provinceCodeMap.put(11L, 0x01L);		// 北京
		provinceNameCodeMap.put("北京", 11L);
		provinceCodeMap.put(12L, 0x01L << 1);	// 天津
		provinceNameCodeMap.put("天津", 12L);
		provinceCodeMap.put(13L, 0x01L << 2);	// 河北省
		provinceNameCodeMap.put("河北", 13L);
		provinceCodeMap.put(14L, 0x01L << 3);	// 山西省
		provinceNameCodeMap.put("山西", 14L);
		provinceCodeMap.put(15L, 0x01L << 4);	// 内蒙古自治区
		provinceNameCodeMap.put("内蒙", 15L);
		provinceCodeMap.put(21L, 0x01L << 5);	// 辽宁省
		provinceNameCodeMap.put("辽宁", 21L);
		provinceCodeMap.put(22L, 0x01L << 6);	// 吉林省
		provinceNameCodeMap.put("吉林", 22L);
		provinceCodeMap.put(23L, 0x01L << 7);	// 黑龙江省
		provinceNameCodeMap.put("黑龙江", 23L);
		provinceCodeMap.put(31L, 0x01L << 8);	// 上海市
		provinceNameCodeMap.put("上海", 31L);
		provinceCodeMap.put(32L, 0x01L << 9);	// 江苏省
		provinceNameCodeMap.put("江苏", 32L);
		provinceCodeMap.put(33L, 0x01L << 10);	// 浙江省
		provinceNameCodeMap.put("浙江", 33L);
		provinceCodeMap.put(34L, 0x01L << 11);	// 安徽省
		provinceNameCodeMap.put("安徽", 34L);
		provinceCodeMap.put(35L, 0x01L << 12);	// 福建省
		provinceNameCodeMap.put("福建", 35L);
		provinceCodeMap.put(36L, 0x01L << 13);	// 江西省
		provinceNameCodeMap.put("江西", 36L);
		provinceCodeMap.put(37L, 0x01L << 14);	// 山东省
		provinceNameCodeMap.put("山东", 37L);
		provinceCodeMap.put(41L, 0x01L << 15);	// 河南省
		provinceNameCodeMap.put("河南", 41L);
		provinceCodeMap.put(42L, 0x01L << 16);	// 湖北省
		provinceNameCodeMap.put("湖北",42L);
		provinceCodeMap.put(43L, 0x01L << 17);	// 湖南省
		provinceNameCodeMap.put("湖南",43L);
		provinceCodeMap.put(44L, 0x01L << 18);	// 广东省
		provinceNameCodeMap.put("广东",44L);
		provinceCodeMap.put(45L, 0x01L << 19);	// 广西壮族自治区
		provinceNameCodeMap.put("广西",45L);
		provinceCodeMap.put(46L, 0x01L << 20);	// 海南省
		provinceNameCodeMap.put("海南",46L);
		provinceCodeMap.put(50L, 0x01L << 21);	// 重庆市
		provinceNameCodeMap.put("重庆",50L);
		provinceCodeMap.put(51L, 0x01L << 22);	// 四川省
		provinceNameCodeMap.put("四川",51L);
		provinceCodeMap.put(52L, 0x01L << 23);	// 贵州省
		provinceNameCodeMap.put("贵州",52L);
		provinceCodeMap.put(53L, 0x01L << 24);	// 云南省
		provinceNameCodeMap.put("云南",53L);
		provinceCodeMap.put(54L, 0x01L << 25);	// 西藏自治区
		provinceNameCodeMap.put("西藏",54L);
		provinceCodeMap.put(61L, 0x01L << 26);	// 陕西省
		provinceNameCodeMap.put("陕西",61L);
		provinceCodeMap.put(62L, 0x01L << 27);	// 甘肃省
		provinceNameCodeMap.put("甘肃",62L);
		provinceCodeMap.put(63L, 0x01L << 28);	// 青海省
		provinceNameCodeMap.put("青海",63L);
		provinceCodeMap.put(64L, 0x01L << 29);	// 宁夏回族自治区
		provinceNameCodeMap.put("宁夏",64L);
		provinceCodeMap.put(65L, 0x01L << 30);	// 新疆维吾尔族自治区
		provinceNameCodeMap.put("新疆",65L);
	}
}
