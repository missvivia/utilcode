package com.xyl.mmall.mobile.facade.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.netease.backend.nkv.client.NkvClient.NkvOption;
import com.netease.backend.nkv.client.Result;
import com.netease.backend.nkv.client.error.NkvFlowLimit;
import com.netease.backend.nkv.client.error.NkvRpcError;
import com.netease.backend.nkv.client.error.NkvTimeout;
import com.netease.backend.nkv.extend.impl.DefaultExtendNkvClient;
import com.netease.print.exceljar.ExcelUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.common.facade.ItemCenterCommonFacade;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.config.NkvConfiguration;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.mobile.facade.MobileIndexFacade;
import com.xyl.mmall.mobile.ios.facade.pageView.common.IosProcPrice;
import com.xyl.mmall.mobile.ios.facade.pageView.index.ADColumn;
import com.xyl.mmall.mobile.ios.facade.pageView.index.ExcelAnnotation;
import com.xyl.mmall.mobile.ios.facade.pageView.index.IndexVO;
import com.xyl.mmall.mobile.ios.facade.pageView.index.MobileIndexSku;
import com.xyl.mmall.mobile.ios.facade.pageView.index.SkuClassify;
import com.xyl.mmall.mobile.ios.facade.pageView.prdctlist.MobileSku;

@Facade
public class MobileIndexFacadeImpl implements MobileIndexFacade {
	private static final Logger logger = LoggerFactory.getLogger(MobileIndexFacadeImpl.class);
	@Autowired
	private ItemProductService itemProductService;

	@Resource(name = "poCommonFacade")
	private ItemCenterCommonFacade commonFacade;

	@Resource
	private DefaultExtendNkvClient defaultExtendNkvClient;

	@Resource
	private NkvConfiguration nkvConfiguration;

	@Resource
	private BusinessService businessService;

	private static final String MOBILE_INDEX_SOURCE = "mobile.index.source";

	public IndexVO getIndexVO(boolean fresh, String path, String FileName) throws Exception {

		String jsonString = getValueByKeyFromCache(MOBILE_INDEX_SOURCE);
		if (jsonString != null && !"".equals(jsonString) && !fresh) {
			return JsonUtils.fromJson(jsonString, IndexVO.class);
		}

		List<ADColumn> adColumns = new ArrayList<>();
		List<SkuClassify> skuClassifies = new ArrayList<>();
		Map<Integer, List<String>> hashMap = new LinkedHashMap<>();
		// 分析数据
		analysisData(adColumns, skuClassifies, hashMap, path, FileName);

		if (skuClassifies.size() != hashMap.size()) {
			return null;
		}
		// composite data 从数据库中组装数据
		compositeAdColumsFormDB(adColumns);
		compositeSkuClassfyFormDB(skuClassifies, hashMap);

		IndexVO indexVO = new IndexVO();

		indexVO.setAds(adColumns);
		indexVO.setSkuClassify(skuClassifies);
		if (CollectionUtils.isNotEmpty(adColumns) || CollectionUtils.isNotEmpty(skuClassifies)) {
			String value = JsonUtils.toJson(indexVO);
			setKVToCache(MOBILE_INDEX_SOURCE, value);
		}
		return indexVO;
	}

	private void compositeAdColumsFormDB(List<ADColumn> adColumns) {
		// TODO Auto-generated method stub
		if (CollectionUtils.isEmpty(adColumns)) {
			return;
		}
		for (ADColumn adColumn : adColumns) {
			// 1表示为店铺类型
			if (adColumn.getType() == 1) {
				BusinessDTO businessDTO = businessService.getBreifBusinessById(adColumn.getId(), 0);
				if (businessDTO != null) {
					adColumn.setStoreName(businessDTO.getStoreName());
				}
			}
		}

	}

	private void compositeSkuClassfyFormDB(List<SkuClassify> skuClassifies, Map<Integer, List<String>> hashMap)
			throws NoSuchFieldException {
		int titleNum = SkuClassify.class.getDeclaredField("title").getAnnotation(ExcelAnnotation.class).value();
		for (int i = 0; i < skuClassifies.size(); i++) {
			SkuClassify skuClassify = skuClassifies.get(i);
			if (skuClassify.getType() == 1) {
				List<Long> list = new ArrayList<>();
				List<ProductSKUDTO> productSKUDTOs = new ArrayList<>();
				List<String> strs = hashMap.get(i + titleNum + 1);
				for (String str : strs) {
					if (NumberUtils.isNumber(str)) {
						ProductSKUDTO productSKUDTO = new ProductSKUDTO();
						productSKUDTO.setId(NumberUtils.toLong(str));
						ProductSKUDTO tem = itemProductService.getProductSKUDTO(productSKUDTO, true);
						if (tem != null) {
							productSKUDTOs.add(tem);
						}
						list.add(NumberUtils.toLong(str));
					}
				}
				List<MobileIndexSku> mobileSkus = new ArrayList<>(productSKUDTOs.size());
				coverToMobileSku(list, productSKUDTOs, mobileSkus);
				skuClassify.setSkus(mobileSkus);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void setKVToCache(String key, String value) {
		try {
			defaultExtendNkvClient.put(nkvConfiguration.rdb_common_namespace, key.getBytes(), value.getBytes(),
					new NkvOption(5000, (short) 0, Integer.MAX_VALUE));
		} catch (NkvRpcError | NkvFlowLimit | NkvTimeout | InterruptedException e) {
			logger.error("################  put value in cache error:", e);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private String getValueByKeyFromCache(String key) {
		try {
			Result<byte[]> brResult = defaultExtendNkvClient.get(nkvConfiguration.rdb_common_namespace, key.getBytes(),
					new NkvOption(5000));
			if (brResult != null && brResult.getResult() != null) {
				byte[] bytes = brResult.getResult();
				return new String(bytes, "UTF-8");
			}

		} catch (Exception e) {
			logger.error("################  get value by key from cache error:  " + e.getMessage());
			return "";
		}
		return "";
	}

	private void coverToMobileSku(List<Long> list, List<ProductSKUDTO> productSKUDTOs,
			List<MobileIndexSku> mobileSkus) {

		MobileIndexSku mobileSku = null;
		for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
			mobileSku = new MobileIndexSku();
			mobileSku.setName(productSKUDTO.getName());
			mobileSku.setSkuId(productSKUDTO.getId());
			mobileSku.setStockCount(productSKUDTO.getSkuNum());
			List<IosProcPrice> priceList = new ArrayList<>();
			IosProcPrice iosProcPrice = null;
			for (ProductPriceDTO productPriceDTO : productSKUDTO.getPriceList()) {
				iosProcPrice = new IosProcPrice();
				iosProcPrice.setPriceId(productPriceDTO.getId());
				iosProcPrice.setPrice(productPriceDTO.getPrice());
				iosProcPrice.setMinNum(productPriceDTO.getMinNumber());
				iosProcPrice.setMaxNum(productPriceDTO.getMaxNumber());
				priceList.add(iosProcPrice);
			}
			mobileSku.setPriceList(priceList);
			mobileSku.setThumb(CollectionUtils.isNotEmpty(productSKUDTO.getPicList())
					? productSKUDTO.getPicList().get(0).getPath() : "");
			mobileSku.setTitle(productSKUDTO.getTitle());
			mobileSku.setUnit(productSKUDTO.getUnit());
			mobileSkus.add(mobileSku);
		}

		Map<Long, Integer> orderStock = null;
		try {
			orderStock = commonFacade.getOrderSkuStock(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			orderStock = new HashMap<Long, Integer>();
		}
		for (MobileSku p : mobileSkus) {
			Integer orderStk = orderStock.get(p.getSkuId());
			if (orderStk == null) {
				p.setStockCount(0);
			} else {
				p.setStockCount(orderStk);
			}
		}
	}

	private static void analysisData(List<ADColumn> adColumns, List<SkuClassify> skuClassifies,
			Map<Integer, List<String>> hashMap, String path, String fileName) throws Exception {
		createDirs(path);
		List<Sheet> sheets = ExcelUtil.getSheetList(path + "/" + fileName);
		if (CollectionUtils.isEmpty(sheets)) {
			return;
		}
		String[][] data = getData(sheets.get(0), 1);
		Map<Class<?>, Map<String, Integer>> hmClass = new HashMap<>();
		Map<String, Integer> hmAdColumn = new HashMap<>();
		for (Field field : ADColumn.class.getDeclaredFields()) {
			ExcelAnnotation ex = field.getAnnotation(ExcelAnnotation.class);
			if (ex != null) {
				hmAdColumn.put(field.getName(), field.getAnnotation(ExcelAnnotation.class).value());
			}
		}
		Map<String, Integer> hmSkuClassfy = new HashMap<>();
		for (Field field : SkuClassify.class.getDeclaredFields()) {
			ExcelAnnotation ex = field.getAnnotation(ExcelAnnotation.class);
			if (ex != null) {
				hmSkuClassfy.put(field.getName(), field.getAnnotation(ExcelAnnotation.class).value());
			}
		}
		
		hmClass.put(ADColumn.class, hmAdColumn);
		hmClass.put(SkuClassify.class, hmSkuClassfy);

		SkuClassify skuClassify = null;
		ADColumn adColumn = null;
		for (int i = 0; i < data.length; i++) {
			adColumn = new ADColumn();
			skuClassify = new SkuClassify();
			lab1: for (int j = 0; j < data[i].length; j++) {
				if (StringUtils.isBlank(data[i][j])) {
					continue;
				}
				for (Map.Entry<Class<?>, Map<String, Integer>> map : hmClass.entrySet()) {
					Class<?> clazz = map.getKey();
					for (Map.Entry<String, Integer> hmTemp : map.getValue().entrySet()) {
						if (j == hmTemp.getValue()) {
							Field f = clazz.getDeclaredField(hmTemp.getKey());
							f.setAccessible(true);
							Object object = null;
							if (f.getType() == Long.class) {
								object = Long.valueOf(data[i][j]);
							} else if (f.getType() == Integer.class) {
								object = Integer.valueOf(data[i][j]);
							} else if (f.getType() == Short.class) {
								object = Short.valueOf(data[i][j]);
							} else {
								object = data[i][j];
							}

							if (clazz == adColumn.getClass()) {
								f.set(adColumn, object);
							} else if (clazz == skuClassify.getClass()) {
								f.set(skuClassify, object);
							}
							continue lab1;
						}

					}
				}

				if (hashMap.get(j) == null) {
					List<String> list = new ArrayList<>();
					hashMap.put(j, list);
					list.add(data[i][j]);
				} else {
					hashMap.get(j).add(data[i][j]);
				}

				// if (StringUtils.isNotBlank(data[i][j])) {
				// if (hmAdColumn.get("id") != null && j == hmAdColumn.get("id")
				// && NumberUtils.isNumber(data[i][j])) {
				// adColumn.setId(NumberUtils.toLong(data[i][j]));
				// } else if (hmAdColumn.get("categoryId") != null && j ==
				// hmAdColumn.get("categoryId")
				// && NumberUtils.isNumber(data[i][j])) {
				// adColumn.setCategoryId(NumberUtils.toLong(data[i][j]));
				// } else if (hmAdColumn.get("picSrc") != null && j ==
				// hmAdColumn.get("picSrc")) {
				// adColumn.setPicSrc(data[i][j]);
				// } else if (hmAdColumn.get("type") != null && j ==
				// hmAdColumn.get("type")
				// && NumberUtils.isNumber(data[i][j])) {
				// adColumn.setType(NumberUtils.toShort(data[i][j]));
				// } else if (hmSkuClassfy.get("title") != null && j ==
				// hmSkuClassfy.get("title")) {
				// skuClassify.setTitle(data[i][j]);
				// } else if (hmSkuClassfy.get("type") != null && j ==
				// hmSkuClassfy.get("type")
				// && NumberUtils.isNumber(data[i][j])) {
				// skuClassify.setType(NumberUtils.toShort(data[i][j]));
				// } else {
				// if (hashMap.get(j) == null) {
				// List<String> list = new ArrayList<>();
				// hashMap.put(j, list);
				// list.add(data[i][j]);
				// } else {
				// hashMap.get(j).add(data[i][j]);
				// }
				// }
				// }
			}
			if (StringUtils.isNotBlank(adColumn.getPicSrc())) {
				adColumns.add(adColumn);
			}

			if (StringUtils.isNotBlank(skuClassify.getTitle())) {
				skuClassifies.add(skuClassify);
			}
		}
	}

	private static void createDirs(String path) {
		// TODO Auto-generated method stub
		File saveDir = new File(path);
		if (!saveDir.exists())
			saveDir.mkdirs();
	}

	private static String[][] getData(Sheet st, int ignoreRows) {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		Cell cell = null;
		// 第一行为标题，不取
		for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
			Row row = st.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			int tempRowSize = row.getLastCellNum();
			if (tempRowSize > rowSize) {
				rowSize = tempRowSize;
			}
			String[] values = new String[rowSize];
			Arrays.fill(values, "");
			boolean hasValue = false;
			for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
				String value = "";
				cell = row.getCell(columnIndex);
				if (cell != null) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							if (date != null) {
								value = new SimpleDateFormat("yyyy-MM-dd").format(date);
							} else {
								value = "";
							}
						} else {
							value = new DecimalFormat("0").format(cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_FORMULA:
						// 导入时如果为公式生成的数据则无值
						if (!cell.getStringCellValue().equals("")) {
							value = cell.getStringCellValue();
						} else {
							value = cell.getNumericCellValue() + "";
						}
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					case Cell.CELL_TYPE_ERROR:
						value = "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						value = (cell.getBooleanCellValue() == true ? "true" : "false");
						break;
					default:
						value = "";
					}
				}
				// if (columnIndex == 0 && value.trim().equals("")) {
				// break;
				// }
				values[columnIndex] = rightTrim(value);
				hasValue = true;
			}
			if (hasValue) {
				result.add(values);
			}
		}
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = (String[]) result.get(i);
		}
		return returnArray;
	}

	private static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}
}
