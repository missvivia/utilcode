package com.xyl.mmall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.ItemCenterConfig;
import com.xyl.mmall.excelparse.ExcelField;
import com.xyl.mmall.excelparse.XLSExport;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dao.product.PoProductDao;
import com.xyl.mmall.itemcenter.dao.product.PoProductDetailDao;
import com.xyl.mmall.itemcenter.dao.product.ProductDao;
import com.xyl.mmall.itemcenter.dao.product.ProductParamDao;
import com.xyl.mmall.itemcenter.dao.product.ProductParamOptDao;
import com.xyl.mmall.itemcenter.dao.size.CustomizedSizeValueDao;
import com.xyl.mmall.itemcenter.dao.sku.PoSkuDao;
import com.xyl.mmall.itemcenter.dto.POSkuDTO;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.enums.ProdDetailType;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.intf.SizeValue;
import com.xyl.mmall.itemcenter.meta.Category;
import com.xyl.mmall.itemcenter.meta.PoProduct;
import com.xyl.mmall.itemcenter.meta.PoProductDetail;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;
import com.xyl.mmall.itemcenter.meta.ProductParameter;
import com.xyl.mmall.itemcenter.meta.SkuSpecMap;
import com.xyl.mmall.itemcenter.service.CategoryService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.itemcenter.service.SizeTemplateService;
import com.xyl.mmall.itemcenter.util.GenExcelProduct;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ItemCenterConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("production")
public class AppTest {

	static {
		System.setProperty("spring.profiles.active", "production");
	}

	@Resource
	private CategoryService categoryService;

	@Resource
	private SizeTemplateService sizeTemplateService;

	@Resource
	private POProductService poProductService;

	@Resource
	private ProductService productService;

	@Resource
	private ProductParamDao productParamDao;

	@Resource
	private ProductParamOptDao optDao;

	@Autowired
	private PoSkuDao poSkuDao;

	@Autowired
	private PoProductDetailDao poDetailDao;

	@Autowired
	private PoProductDao poProductDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CustomizedSizeValueDao customizedSizeValueDao;

	private static List<Long> idList = Arrays.asList(2115L, 1815L, 1215L, 1515L, 3315L, 3015L, 2415L, 2715L, 4524L,
			4219L, 1216L, 1516L, 1816L, 2116L, 2140L, 1840L, 1240L, 2141L, 1841L, 1241L, 1541L, 2142L, 1842L);

	@Autowired
	private EhCacheCacheManager ehCacheCacheManager;

	@Test
	public void testEhcache() throws Exception {
		Collection<String> cacheNameList = ehCacheCacheManager.getCacheNames();

		if (cacheNameList == null || cacheNameList.isEmpty()) {
			System.out.println("no");
		}

		StringBuilder builder = new StringBuilder();

		for (String name : cacheNameList) {
			builder.append(this.getListInfoForCache(name));
		}

		System.out.println(builder.toString());
	}

	private String getListInfoForCache(String name) {
		EhCacheCache cache = (EhCacheCache) ehCacheCacheManager.getCache(name);
		Ehcache nativeCache = cache.getNativeCache();
		StringBuilder builder = new StringBuilder();
		builder.append("cache名称:" + name);
		builder.append(",内存使用大小:" + nativeCache.calculateInMemorySize());
		builder.append(",cache对象个数:" + nativeCache.getSize());
		builder.append(",最大使用堆大小:" + nativeCache.getCacheConfiguration().getMaxBytesLocalHeap());
		builder.append("\n");
		return builder.toString();
	}

	@Test
	public void testGet() throws Exception {
		PoSku poSku = poSkuDao.getObjectById(2115L);
		System.out.println(poSku);
		// Thread.sleep(100000);
	}

	@Test
	public void testGetPoSkusByIdList() throws Exception {
		long oldTime = System.currentTimeMillis();
		List<POSkuDTO> res = poProductService.getSkuDTOListBySkuId(idList);
		System.out.println(res.size());
		long cost = System.currentTimeMillis() - oldTime;
		System.out.println("====cost:" + cost);

		Thread.sleep(8000);

		long oldTime2 = System.currentTimeMillis();
		List<POSkuDTO> res2 = poProductService.getSkuDTOListBySkuId(idList);
		System.out.println(res2.size());
		long cost2 = System.currentTimeMillis() - oldTime2;
		System.out.println("====cost2:" + cost2);
	}

	@Test
	public void testGetPoSkusByIdListYouhua() throws Exception {
		// 预热
		List<PoSku> poSkuList = poSkuDao.getPoSkusByIds(idList);
		for (PoSku poSku : poSkuList) {
			PoProduct poProduct = poProductService.getPoProductByPoIdAndProduct(poSku.getProductId(), poSku.getPoId());
			SkuSpecMap skuSpecMap = poProductService.getSkuSpecMapByPoIdProductIdSkuId(poSku.getPoId(),
					poSku.getProductId(), poSku.getId());
			// System.out.println("ok");
		}

		this.youhua();
	}

	private void youhua() {
		long oldTime = System.currentTimeMillis();
		List<PoSku> poSkuList = poSkuDao.getPoSkusByIds(idList);

		List<POSkuDTO> poSkuDTOList = new ArrayList<>();
		for (PoSku poSku : poSkuList) {
			POSkuDTO dto = new POSkuDTO(poSku);
			PoProduct poProduct = poProductService.getPoProductByPoIdAndProduct(poSku.getProductId(), poSku.getPoId());
			SkuSpecMap skuSpecMap = poProductService.getSkuSpecMapByPoIdProductIdSkuId(poSku.getPoId(),
					poSku.getProductId(), poSku.getId());

			dto.setProductName(poProduct.getProductName());
			dto.setProductLinkUrl("/detail?id=" + poProduct.getId());
			dto.setColorName(poProduct.getColorName());
			if (skuSpecMap != null) {
				dto.setSize(skuSpecMap.getValue());
			}
			dto.setThumb(poProduct.getShowPicPath());
			dto.setStatusName(poSku.getStatus().getDesc());
			dto.setBrandId(poProduct.getBrandId());
			poSkuDTOList.add(dto);
		}

		long cost = System.currentTimeMillis() - oldTime;
		System.out.println("====cost for youhua:" + cost + ",size:" + poSkuDTOList.size());

	}

	/**
	 * PoSku sku = new PoSku(); sku.setBarCode(rs.getString("BarCode"));
	 * sku.setBasePrice(rs.getBigDecimal("BasePrice"));
	 * sku.setCTime(rs.getLong("CTime"));
	 * sku.setGoodsNo(rs.getString("GoodsNo")); sku.setId(rs.getLong("Id"));
	 * sku.setMarketPrice(rs.getBigDecimal("MarketPrice"));
	 * sku.setPoId(rs.getLong("PoId"));
	 * sku.setRejectReason(rs.getString("RejectReason"));
	 * sku.setSalePrice(rs.getBigDecimal("SalePrice"));
	 * sku.setSizeIndex(rs.getInt("SizeIndex"));
	 * sku.setSkuNum(rs.getInt("SkuNum"));
	 * sku.setStatus(StatusType.APPROVAL.genEnumByIntValue
	 * (rs.getInt("Status"))); sku.setSubmitTime(rs.getLong("SubmitTime"));
	 * sku.setSupplierId(rs.getLong("SupplierId"));
	 * sku.setUTime(rs.getLong("UTime"));
	 * sku.setProductId(rs.getLong("ProductId")); POSkuDTO dto = new
	 * POSkuDTO(sku); dto.setProductName(rs.getString("ProductName"));
	 * dto.setProductLinkUrl("/detail?id=" + dto.getProductId());
	 * dto.setColorName(rs.getString("ColorName"));
	 * dto.setSize(rs.getString("Size"));
	 * dto.setThumb(rs.getString("ShowPicPath"));
	 * dto.setStatusName(sku.getStatus().getDesc());
	 * dto.setBrandId(rs.getLong("BrandId")); if (list.indexOf(dto) < 0)
	 * list.add(dto);
	 */

	/*
	 * @Test public void saveCartItemTest() { List<CategoryDTO> list =
	 * categoryService.getCategoryListBylowId(149); for (CategoryDTO c : list) {
	 * System.out.println(c.getName());
	 * System.out.println("----------------------------"); List<Category> clist
	 * = c.getSameParentList(); for (Category cc : clist) {
	 * System.out.println(cc.getName()); }
	 * System.out.println("============================");
	 * System.out.println(""); } assertTrue(true); }
	 * 
	 * @Test public void testSizeTemplateColumn() { List<SizeTemplateColumn>
	 * list = sizeTemplateService.getSizeTemplateColumn(26); for
	 * (SizeTemplateColumn s : list) { System.out.print(s.getDetailName() +
	 * "\t"); } System.out.println("============================");
	 * System.out.println(""); }
	 * 
	 * @Test public void testAddNewSizeTemplate(){ SizeTemplateSaveParam param =
	 * new SizeTemplateSaveParam(); param.setLastModifyTime(new
	 * Date().getTime()); param.setLowCategoryId(26);
	 * param.setPicPath("c://ddd"); param.setRemindText("这是提醒！！！");
	 * param.setStandardPic(true); param.setTemplateName("模板名"); SizeTmplTable
	 * st = new SizeTmplTable(6,1); st.setSingleColumnId(0, 1);
	 * st.setSingleColumnId(1, 2); st.setSingleColumnId(2, 3);
	 * st.setSingleColumnId(3, 4); st.setSingleColumnId(4, 5);
	 * st.setSingleColumnId(5, 6); SizeTmplRecord record = new
	 * SizeTmplRecord(1,6); record.setColumnValue(0, "L");
	 * record.setColumnValue(1, "180"); record.setColumnValue(2, "180");
	 * record.setColumnValue(3, "180"); record.setColumnValue(4, "180");
	 * record.setColumnValue(5, "180"); st.addRecord(record);
	 * param.setSizeTable(st); sizeTemplateService.addNewSizeTemplate(param); }
	 * 
	 * @Test public void testSearchSizeTemplate(){ SizeTemplateSearchParam param
	 * = new SizeTemplateSearchParam(); List<SizeTemplateSearchResultDTO> list =
	 * sizeTemplateService.searchSizeTemplate(param, 5, 1);
	 * for(SizeTemplateSearchResultDTO result:list){
	 * System.out.println(result.toString()); } }
	 */
	@Test
	public void scriptToIntialDB() {
		System.out.println("=========== scriptToIntialDB start ========================");
		//
		// productService.intialProductThumb();
		// productService.intialProductFlag();
		// poProductService.intialProductThumb();
		// poProductService.intialSkuSpec();
		// productService.intialProductDetail();
		// poProductService.intialProductDetail();
		System.out.println("=========== scriptToIntialDB end ========================");
	}

	@Test
	public void createExcel() {
		List<Category> clist = categoryService.getCategoryListByLevel(2);
		for (Category c : clist) {
			long cid = c.getId();
			String cName = c.getName();
			cName = cName.replaceAll("/", "&");
			String excelName = cid + "_" + cName + ".xls";
			XLSExport export = new XLSExport(excelName);
			List<Category> subList = categoryService.getSubCategoryList(cid);
			int index = 0;
			for (Category cc : subList) {
				long ccid = cc.getId();
				String ccName = cc.getName();
				ccName = ccName.replaceAll("/", "&");
				export.createSheet(index++, ccName + "_" + ccid);

				List<ProductParamDTO> attrHeader = new ArrayList<ProductParamDTO>();
				String paramValue = cc.getParameter();

				List<Long> paramValueList = JsonUtils.parseArray(paramValue, Long.class);
				if (paramValueList != null && paramValueList.size() > 0) {
					for (Long paramVal : paramValueList) {
						ProductParameter paramter = productParamDao.getObjectById(paramVal);
						ProductParamDTO paire = new ProductParamDTO(paramter);
						List<ProductParamOption> optList = optDao.getOptionList(paramter.getId());
						paire.setOptionList(optList);
						attrHeader.add(paire);
					}
				}

				Field[] fields = GenExcelProduct.class.getDeclaredFields();
				export.createRow(0);
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					ExcelField excelAnnotation = field.getAnnotation(ExcelField.class);
					if (excelAnnotation == null)
						continue;
					int cellIndex = excelAnnotation.cellIndex();
					String title = excelAnnotation.title();
					export.setCell(cellIndex, title);
				}

				int offset = GenExcelProduct.OFFSET;
				if (attrHeader != null && attrHeader.size() > 0) {
					for (ProductParamDTO headerPair : attrHeader) {
						String tmpHeader = headerPair.getName() + "_" + headerPair.getId();
						export.setCell(offset, tmpHeader);
						offset++;
					}
				}

				HSSFCellStyle style = export.createStyle();
				HSSFCellStyle style2 = export.createStyle();
				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style.setFillForegroundColor(HSSFColor.TURQUOISE.index);

				// XSSFCellStyle.ALIGN_CENTER 居中对齐
				// XSSFCellStyle.ALIGN_LEFT 左对齐
				// XSSFCellStyle.ALIGN_RIGHT 右对齐
				style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				// 设置单元格内容垂直对其方式
				// XSSFCellStyle.VERTICAL_TOP 上对齐
				// XSSFCellStyle.VERTICAL_CENTER 中对齐
				// XSSFCellStyle.VERTICAL_BOTTOM 下对齐
				style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
				style.setWrapText(true);
				style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
				style2.setWrapText(true);
				export.createRow(1);
				export.setRowHight(100);
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					ExcelField excelAnnotation = field.getAnnotation(ExcelField.class);
					if (excelAnnotation == null)
						continue;
					int cellIndex = excelAnnotation.cellIndex();
					String desc = excelAnnotation.desc();
					boolean isRequired = excelAnnotation.required();
					if (isRequired) {
						export.setCell(cellIndex, desc, style);
					} else
						export.setCell(cellIndex, desc, style2);
				}

				offset = GenExcelProduct.OFFSET;
				if (attrHeader != null && attrHeader.size() > 0) {
					for (ProductParamDTO headerPair : attrHeader) {
						StringBuffer sb = new StringBuffer();
						boolean isRequired = headerPair.getIsRequired();
						ProdDetailType detailType = headerPair.getDetailType();
						if (detailType == ProdDetailType.TEXT || detailType == ProdDetailType.TEXT_AREA) {
							sb.append("文本输入");
						} else {
							if (detailType == ProdDetailType.SINGLE_SELECT) {
								sb.append("【请选择其一填写】");
							} else
								sb.append("【可多选，填写多个请用英文分号（;）隔开】");
							List<ProductParamOption> optList = headerPair.getOptionList();
							if (optList != null && optList.size() > 0) {
								for (int i = 0; i < optList.size(); i++) {
									ProductParamOption opt = optList.get(i);
									String optValue = opt.getValue();
									if (i == optList.size() - 1)
										sb.append(optValue);
									else
										sb.append(optValue).append("、");
								}
							}
						}
						if (isRequired) {
							export.setCell(offset, sb.toString(), style);
						} else
							export.setCell(offset, sb.toString(), style2);

						offset++;
					}
				}
			}
			FileOutputStream os;
			try {
				os = new FileOutputStream("D:\\report\\" + excelName);
				export.exportXLS(os);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
/*
	@Test
	public void createScript() {
		try {
			FileOutputStream out = new FileOutputStream(new File("E:/女装项目/sql/script.sql"));
			Map<String, Integer> result = poDetailDao.getSameAsShop();
			if (result != null && result.size() > 0) {
				for (String key : result.keySet()) {
					int sameAsShop = result.get(key);
					long pid = Long.valueOf(key.split("_")[0]);
					long poId = Long.valueOf(key.split("_")[1]);
					Long p = poProductDao.getProductId(pid, poId);
					if (p != null) {
						out.write(("UPDATE Mmall_ItemCenter_PoProduct SET SameAsShop = " + sameAsShop + " WHERE PoId = "
								+ poId + " AND Id = " + pid + ";\r\n").getBytes());
					}
				}
			}
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void createScript2() {
		try {
			FileOutputStream out = new FileOutputStream(new File("E:/女装项目/sql/script2.sql"));
			List<Long> result = productDao.getProductId();
			if (result != null && result.size() > 0) {
				for (long pid : result) {
					out.write(("DELETE FROM Mmall_ItemCenter_CustomizedSize WHERE ProductId = " + pid + " AND ColumnId = 2 AND IsInPo = 0;\r\n")
							.getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 23, 0, 2, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 24, 0, 7, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 8, 0, 8, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 9, 0, 9, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 14, 0, 10, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 11, 0, 11, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 15, 0, 12, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 16, 0, 13, 0);\r\n").getBytes());

					Map<Long, String> sm = customizedSizeValueDao.getSize(pid, 0);
					if (!sm.isEmpty()) {
						for (long recordIndex : sm.keySet()) {
							out.write(("DELETE FROM Mmall_ItemCenter_CustomizedSizeValue WHERE ProductId = " + pid
									+ " AND RecordIndex = " + recordIndex + " AND ColumnId = 2 AND IsInPo = 0;\r\n")
									.getBytes());
							String value = sm.get(recordIndex);
							out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSizeValue (ProductId, ColumnId, RecordIndex, IsInPo, Value) VALUES ("
									+ pid + ", 23, " + recordIndex + ", 0, " + value + ");\r\n").getBytes());
						}
					}
				}
			}

			List<Long> result2 = poProductDao.getProductId();
			if (result != null && result.size() > 0) {
				for (long pid : result2) {
					out.write(("DELETE FROM Mmall_ItemCenter_CustomizedSize WHERE ProductId = " + pid + " AND ColumnId = 2 AND IsInPo = 1;\r\n")
							.getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 23, 1, 2, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 24, 1, 7, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 8, 1, 8, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 9, 1, 9, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 14, 1, 10, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 11, 1, 11, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 15, 1, 12, 0);\r\n").getBytes());
					out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSize (ProductId, ColumnId, IsInPo, ColIndex, IsRequired) VALUES ("
							+ pid + ", 16, 1, 13, 0);\r\n").getBytes());

					Map<Long, String> sm = customizedSizeValueDao.getSize(pid, 1);
					if (!sm.isEmpty()) {
						for (long recordIndex : sm.keySet()) {
							out.write(("DELETE FROM Mmall_ItemCenter_CustomizedSizeValue WHERE ProductId = " + pid
									+ " AND RecordIndex = " + recordIndex + " AND ColumnId = 2 AND IsInPo = 1;\r\n")
									.getBytes());
							String value = sm.get(recordIndex);
							out.write(("INSERT INTO Mmall_ItemCenter_CustomizedSizeValue (ProductId, ColumnId, RecordIndex, IsInPo, Value) VALUES ("
									+ pid + ", 23, " + recordIndex + ", 1, " + value + ");\r\n").getBytes());
						}
						List<PoSku> list = poSkuDao.getPoSkuList(pid);
						if (list != null && list.size() > 0) {
							for (PoSku sku : list) {
								long poId = sku.getPoId();
								long index = sku.getSizeIndex();
								SizeValue s = customizedSizeValueDao.getSizeValue(pid, 1, index, 1);
								if (s != null) {
									out.write(("UPDATE Mmall_ItemCenter_SkuSpecMap SET Value = '" + s.getValue()
											+ "' WHERE PoId = " + poId + " AND SkuId = " + sku.getId()
											+ " AND ProductId = " + pid + ";\r\n").getBytes());
								}
							}
						}
					}
				}
			}

			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
*/
	@Test
	public void onlineChangeDB1() {
		List<PoProduct> newList = new ArrayList<PoProduct>();
		List<PoProduct> list = poProductDao.getProduct(5003l);
		for (PoProduct p : list) {
			int index = newList.indexOf(p);
			if (index < 0) {
				newList.add(p);
			} else {
				PoProduct oldP = newList.get(index);
				System.out.println("goodsNo:" + oldP.getGoodsNo() + ";colorNum:" + oldP.getColorNum());
				System.out.println("old pid:" + oldP.getId() + ";new pid:" + p.getId());
				System.out.println("********************");
//				poProductService.deleteProduct(p.getPoId(), p.getId());
			}
		}
	}
	
	@Test
	public void onlineChangeDB0202() {
		try{
			FileOutputStream out = new FileOutputStream(new File("E:/女装项目/sql/script0202.sql"));
			long []poIds = {1007,1001,3002,1006,3004,3003,4003,1002,1005,4008,1004,1,3011,3005,3014,3010,4012,4011,1003,3006,5001,3012,4004,3001,3023,4013,3038,5026,4024,3027,3007,3030,5013,1008,3020,6025,3035,4001,1010,3022,3028,3015,3031,3018,3033,3024,3032,3013,5003,3029,3026,3034,6016,3039,3019,3042,3021,3036,3008,1009,5027,3017,4010,5002,3040,4023,5035,6006,6009,5004,6020,3037,6023,3041,4022,4018,3016,6003,4027,4002,3009,6012,6008,3025,5039,6005,6026,4006,6004,6027,6031,5034,4005,4032,6032,6001,4031,5045,6014,5014,5008,5019,5011,6030,6021,5040,5012,5041,4007,6029,5018,6034,5020,6015,4015,5010,5025,4020,4016,4009,6011,5009,5042,6010,6028,5031,5047,5022,5037,5016,4028,4021,4019,4025,6002,4030,5032,5043,6007,6018,6033,6024,5024,6013,5015,6017,5006,6022,5044,4035,6019,5046,4029,5028,5036,5017,5021,5030,5038,5007,5023,4026,4017,4033,5033,4034,4014,5005,5029};
			for(long poId:poIds){
				int prodNum = poProductService.getProductNum(poId);
				int prodUnSubmit = poProductService.getProductNumOfStatus(poId, StatusType.NOTSUBMIT);
				int prodPend = poProductService.getProductNumOfStatus(poId, StatusType.PENDING);
				int prodReject = poProductService.getProductNumOfStatus(poId, StatusType.REJECT);
//				int prodApproval = poProductService.getProductNumOfStatus(poId, StatusType.APPROVAL);
				
				int skuNum = poProductService.getSkuNum(poId);
				int skuUnSubmit = poProductService.getSkuNumOfStatus(poId, StatusType.NOTSUBMIT);
				int skuPend = poProductService.getSkuNumOfStatus(poId, StatusType.PENDING);
				int skuReject = poProductService.getSkuNumOfStatus(poId, StatusType.REJECT);
//				int skuApproval = poProductService.getSkuNumOfStatus(poId, StatusType.APPROVAL);
				
				if((prodNum==prodUnSubmit)&&(skuNum==skuUnSubmit)){
					out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdqd= "+StatusType.NOTSUBMIT.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdzl= "+StatusType.NOTSUBMIT.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
				}else{
					if(prodPend>0){
						out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdzl= "+StatusType.PENDING.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					}else if((prodReject+prodUnSubmit)>0){
						out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdzl= "+StatusType.REJECT.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					}else{
						out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdzl= "+StatusType.APPROVAL.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					}
					
					if(skuPend>0){
						out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdqd= "+StatusType.PENDING.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					}else if((skuReject+skuUnSubmit)>0){
						out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdqd= "+StatusType.REJECT.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					}else{
						out.write(("update Mmall_SaleSchedule_ScheduleVice set flagAuditPrdqd= "+StatusType.APPROVAL.getIntValue()+" where scheduleId="+poId+";\r\n").getBytes());
					}
				}
			}
			out.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}