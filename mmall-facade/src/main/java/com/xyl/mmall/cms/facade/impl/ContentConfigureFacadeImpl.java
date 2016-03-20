package com.xyl.mmall.cms.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.cms.dto.BusinessDTO;
import com.xyl.mmall.cms.facade.ContentConfigureFacade;
import com.xyl.mmall.cms.service.BusinessService;
import com.xyl.mmall.constant.MmallConstant;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.protocol.ResponseCode;
import com.xyl.mmall.framework.util.ExcelUtil;
import com.xyl.mmall.framework.util.RegexUtils;
import com.xyl.mmall.framework.util.UrlBaseUtil;
import com.xyl.mmall.itemcenter.dto.ProductPriceDTO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.ProductService;
import com.xyl.mmall.mainsite.vo.MainsiteCategoryContentVO;

@Facade
public class ContentConfigureFacadeImpl implements ContentConfigureFacade {

	private static final Logger logger = LoggerFactory.getLogger(ContentConfigureFacadeImpl.class);

	@Autowired
	private ItemProductService itemProductService;

	@Autowired
	private ProductService productService;

	@Autowired
	private BusinessService businessService;

	@Override
	public Map<Integer, String> getMainsiteIndexDataFromSheet(Sheet sheet) {
		// 以列为key存放首页excel数据
		Map<Integer, String> excelColumnValueMap = new HashMap<Integer, String>();
		// 初始化商品sku列
		Map<Integer, List<Long>> skuIdColumnValueMap = new HashMap<Integer, List<Long>>();
		for (Integer cellNum : MmallConstant.SKU_CELL_NUM_SET) {
			skuIdColumnValueMap.put(cellNum, new ArrayList<Long>());
		}
		// 错误信息
		StringBuffer errorSb = new StringBuffer();
		StringBuffer errorPriceSb = new StringBuffer();

		StringBuffer[] sb = new StringBuffer[MmallConstant.MAIN_SITE_INDEX_TEMPLATE_CELLNUM];
		for (int i = 0; i < sb.length; i++) {
			sb[i] = new StringBuffer(256);
		}
		// 行数，第一行为标题头
		int rowNum = sheet.getLastRowNum();
		for (int j = 1; j <= rowNum; j++) {
			Row row = sheet.getRow(j);
			for (int k = 0; k <= MmallConstant.MAIN_SITE_INDEX_TEMPLATE_CELLNUM; k++) {
				Cell cell = row.getCell(k);
				String value = ExcelUtil.getValue(cell);
				if (skuIdColumnValueMap.containsKey(k) && StringUtils.isNotEmpty(value)) {
					if (RegexUtils.isAllNumber(value)) {
						skuIdColumnValueMap.get(k).add(Long.parseLong(value));
					} else {
						errorSb.append("skuId " + value + " 数据格式不对").append("\n");
					}
				}
				if (StringUtils.isNotEmpty(value)) {
					sb[k].append("\"" + value + "\"").append(",");
				}
				// 背景图片只取一张
				if (MmallConstant.BACKGROUND_CELL_NUM_SET.contains(k)) {
					if (StringUtils.isNotEmpty(value) && sb[k].indexOf(",") != sb[k].lastIndexOf(",")) {
						errorSb.append("第" + (k + 1) + "列侧边栏背景图片数量超过2张，只允许上传一张！ ").append("\n");
					}
				}
			}
		}

		for (Map.Entry<Integer, List<Long>> entry : skuIdColumnValueMap.entrySet()) {
			if (CollectionUtil.isEmptyOfList(entry.getValue())) {
				continue;
			}
			// 根据skuId获取商品价格和图片url
			List<ProductSKUDTO> productSKUDTOs = itemProductService.getProductSKUDTOByProdIds(entry.getValue());
			if (CollectionUtil.isEmptyOfList(productSKUDTOs)) {
				errorSb.append("以下商品不存在: " + entry.getValue().toString()).append("\n");
				continue;
			}
			Map<String, List<ProductPriceDTO>> productPriceMap = productService.getProductPriceDTOByProductIds(entry
					.getValue());
			Map<Long, ProductSKUDTO> skuMap = new HashMap<Long, ProductSKUDTO>();
			for (ProductSKUDTO productSKUDTO : productSKUDTOs) {
				BusinessDTO businessDTO = businessService.getBreifBusinessById(productSKUDTO.getBusinessId(), -1);
				if (businessDTO == null) {
					errorSb.append("以下商品不存在: " + productSKUDTO.getId()).append("\n");
				}
				productSKUDTO.setStoreName(businessDTO.getStoreName());
				skuMap.put(productSKUDTO.getId(), productSKUDTO);
			}
			List<ProductPriceDTO> priceDTOs = null;
			String title = "";
			for (Long skuId : entry.getValue()) {
				if (skuMap.get(skuId) == null) {
					errorSb.append(skuId).append("\n");
					continue;
				}
				// 商品Url图片列赋值
				sb[entry.getKey() - 1].append("\"" + skuMap.get(skuId).getShowPicPath() + "\"").append(",");
				// 商品名列赋值
				sb[entry.getKey() + 1].append("\"" + skuMap.get(skuId).getName() + "\"").append(",");
				title = StringUtils.isEmpty(skuMap.get(skuId).getTitle()) ? "" : skuMap.get(skuId).getTitle();
				// 商品副标题列赋值
				sb[entry.getKey() + 2].append("\"" + title + "\"").append(",");
				// 店铺名称
				sb[entry.getKey() + 3].append("\"" + skuMap.get(skuId).getStoreName() + "\"").append(",");
				// 店铺链接
				sb[entry.getKey() + 4].append(
						"\"" + UrlBaseUtil.buildStoreUrl(skuMap.get(skuId).getBusinessId()) + "\"").append(",");
				if (productPriceMap.get(String.valueOf(skuId)) == null) {
					errorPriceSb.append(skuId).append("\n");
					continue;
				}
				priceDTOs = productPriceMap.get(String.valueOf(skuId));
				// 价格列赋值
				sb[entry.getKey() + 5].append("\"" + priceDTOs.get(0).getPrice() + "\"").append(",");
				// 标签 TODO
				// sb[entry.getKey() + 6].append("\"" + "\"").append(",");
			}
		}

		for (int i = 0; i < sb.length; i++) {
			if (sb[i].length() > 0) {
				sb[i].deleteCharAt(sb[i].lastIndexOf(","));
			}
			excelColumnValueMap.put(i, sb[i].toString());
		}
		if (errorPriceSb.length() > 0 || errorSb.length() > 0) {
			excelColumnValueMap.put((int) ResponseCode.RES_ERROR,
					errorSb.toString() + "商品不存在或者已下架或者" + errorPriceSb.toString() + " 获取价格失败！");
		}
		return excelColumnValueMap;
	}

	@Override
	public List<MainsiteCategoryContentVO> getWebMainsiteCategoryDataFromSheet(Sheet sheet) {
		List<MainsiteCategoryContentVO> categoryContentVOs = new ArrayList<MainsiteCategoryContentVO>();
		// 行数，第一行为标题头
		int rowNum = sheet.getLastRowNum();
		boolean isMerge = false;
		Row row = null;
		String name = "", url = "", iconUrl = "";
		// 根据第一行列名称设值对应的index
		row = sheet.getRow(0);
		int catgory1NameColumnIndex = 0, catgory1UrlColumnIndex = 0, catgory1IconColumnIndex = 0, catgory2NameColumnIndex = 0, catgory2UrlColumnIndex = 0, catgory3NameColumnIndex = 0, catgory3UrlColumnIndex = 0;
		for (Cell cell : row) {
			String columnValue = ExcelUtil.getValue(cell);
			if (StringUtils.isEmpty(columnValue)) {
				break;
			}
			if (columnValue.indexOf("Catgory_1_Name") >= 0) {
				// 一级类目列index
				catgory1NameColumnIndex = cell.getColumnIndex();
			}
			if (columnValue.indexOf("Catgory_1_Url") >= 0) {
				// 一级类目链接列index
				catgory1UrlColumnIndex = cell.getColumnIndex();
			}
			if (columnValue.indexOf("Catgory_1_Icon") >= 0) {
				// 一级类目图标url
				catgory1IconColumnIndex = cell.getColumnIndex();
			}
			if (columnValue.indexOf("Catgory_2_Name") >= 0) {
				// 二级类目列index
				catgory2NameColumnIndex = cell.getColumnIndex();
			}
			if (columnValue.indexOf("Catgory_2_Url") >= 0) {
				// 二级类目列index
				catgory2UrlColumnIndex = cell.getColumnIndex();
			}
			if (columnValue.indexOf("Catgory_3_Name") >= 0) {
				// 三级类目列index
				catgory3NameColumnIndex = cell.getColumnIndex();
			}
			if (columnValue.indexOf("Catgory_3_Url") >= 0) {
				// 三级类目列index
				catgory3UrlColumnIndex = cell.getColumnIndex();
			}
		}
		// 读取sheet的值赋值到对象
		for (int j = 1; j <= rowNum;) {
			// 一级类目合并单元格最后的行数
			int num = j;
			MainsiteCategoryContentVO categoryContentVO = new MainsiteCategoryContentVO();
			isMerge = ExcelUtil.isMergedRegion(sheet, j, catgory1NameColumnIndex);
			// 判断是否具有合并单元格,设置一级类目
			if (isMerge) {
				num = ExcelUtil.getMergedRowNum(sheet, j, catgory1NameColumnIndex);
				name = ExcelUtil.getMergedRegionValue(sheet, j, catgory1NameColumnIndex);
				url = ExcelUtil.getMergedRegionValue(sheet, j, catgory1UrlColumnIndex);
				iconUrl = ExcelUtil.getMergedRegionValue(sheet, j, catgory1IconColumnIndex);
				categoryContentVO.setName(name);
				categoryContentVO.setUrl(url);
				categoryContentVO.setIconUrl(iconUrl);
			} else {
				row = sheet.getRow(j);
				name = ExcelUtil.getValue(row.getCell(catgory1NameColumnIndex));
				url = ExcelUtil.getValue(row.getCell(catgory1UrlColumnIndex));
				iconUrl = ExcelUtil.getValue(row.getCell(catgory1IconColumnIndex));
				categoryContentVO.setName(name);
				categoryContentVO.setUrl(url);
				categoryContentVO.setIconUrl(iconUrl);
			}
			// 二级类目 设置二级类目
			for (int k = j; k <= num;) {
				MainsiteCategoryContentVO secCategoryContentVO = new MainsiteCategoryContentVO();
				isMerge = ExcelUtil.isMergedRegion(sheet, k, catgory2NameColumnIndex);
				// 二级类目合并单元格最后的行数
				int secNum = k;
				// 判断是否具有合并单元格
				if (isMerge) {
					secNum = ExcelUtil.getMergedRowNum(sheet, k, catgory2NameColumnIndex);
					name = ExcelUtil.getMergedRegionValue(sheet, k, catgory2NameColumnIndex);
					url = ExcelUtil.getMergedRegionValue(sheet, k, catgory2UrlColumnIndex);
					secCategoryContentVO.setName(name);
					secCategoryContentVO.setUrl(url);
				} else {
					row = sheet.getRow(k);
					name = ExcelUtil.getValue(row.getCell(catgory2NameColumnIndex));
					url = ExcelUtil.getValue(row.getCell(catgory2UrlColumnIndex));
					secCategoryContentVO.setName(name);
					secCategoryContentVO.setUrl(url);
				}
				categoryContentVO.addCategoryContentVOs(secCategoryContentVO);
				// 设置三级类目
				for (int h = k; h <= secNum; ++h) {
					row = sheet.getRow(h);
					MainsiteCategoryContentVO thirdCategoryContentVO = new MainsiteCategoryContentVO();
					name = ExcelUtil.getValue(row.getCell(catgory3NameColumnIndex));
					url = ExcelUtil.getValue(row.getCell(catgory3UrlColumnIndex));
					thirdCategoryContentVO.setName(name);
					thirdCategoryContentVO.setUrl(url);
					secCategoryContentVO.addCategoryContentVOs(thirdCategoryContentVO);
				}
				k = secNum + 1;
			}
			j = num + 1;
			categoryContentVOs.add(categoryContentVO);
		}
		return categoryContentVOs;
	}

	@Override
	public List<ProductSKUDTO> getSKUBy(List<Long> skuIds) {
		if (skuIds == null || skuIds.isEmpty())
			return Collections.emptyList();
		List<ProductSKUDTO> lstSkuDto = itemProductService.getProductSKUDTOByProdIds(skuIds);
		Map<String, List<ProductPriceDTO>> productPriceMap = productService.getProductPriceDTOByProductIds(skuIds);
		for (ProductSKUDTO productSKUDTO : lstSkuDto) {
			BusinessDTO businessDTO = businessService.getBreifBusinessById(productSKUDTO.getBusinessId(), -1);
			if (businessDTO != null) {
				productSKUDTO.setStoreName(businessDTO.getStoreName());
			}
			if (productPriceMap != null) {
				productSKUDTO.setPriceList(productPriceMap.get("" + productSKUDTO.getId()));
			}
		}
		return lstSkuDto;
	}

	@Override
	public Set<Long> validateSkuExists(Collection<Long> skuIds) {
		if (skuIds != null && !skuIds.isEmpty()) {
			Set<Long> invalidSku = new HashSet<>(skuIds.size());
			Iterator<Long> itr = skuIds.iterator();
			while (itr.hasNext()) {
				Long skuId = itr.next();
				ProductSKUDTO skuDto = itemProductService.getProductSKUBreifInfo(skuId);
				if (skuDto == null) {
					invalidSku.add(skuId);
				}
			}
			return invalidSku;
		}
		return Collections.emptySet();
	}

}
