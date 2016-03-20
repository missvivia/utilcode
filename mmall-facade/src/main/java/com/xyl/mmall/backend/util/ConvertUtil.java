package com.xyl.mmall.backend.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.backend.vo.ExportProductBodyVO;
import com.xyl.mmall.backend.vo.PoProductSearchVO;
import com.xyl.mmall.backend.vo.ProductEditVO;
import com.xyl.mmall.backend.vo.ProductSearchVO;
import com.xyl.mmall.common.param.ProductAttr;
import com.xyl.mmall.common.util.ItemCenterUtils;
import com.xyl.mmall.excelparse.ExcelBool;
import com.xyl.mmall.excelparse.ExcelEnumInterface;
import com.xyl.mmall.excelparse.ExcelExportField;
import com.xyl.mmall.framework.vo.SimpleIdValuePaire;
import com.xyl.mmall.itemcenter.dto.ExcelExportProduct;
import com.xyl.mmall.itemcenter.dto.ProductDTO;
import com.xyl.mmall.itemcenter.dto.ProductFullDTO;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.enums.StatusType;
import com.xyl.mmall.itemcenter.param.POProductSearchParam;
import com.xyl.mmall.itemcenter.param.ProdParamParam;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.ProductSearchParam;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SkuSaveParam;
import com.xyl.mmall.itemcenter.util.ItemCenterUtil;

public class ConvertUtil {
	private static final Logger logger = LoggerFactory.getLogger(ConvertUtil.class);

	final static int BUFFER_SIZE = 4096;

	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}

	public static ProductSaveParam convertJSONToProductParam(JSONObject json) {
		ProductSaveParam product = new ProductSaveParam();
		long pid = 0;
		if (json.containsKey("id"))
			pid = json.getLongValue("id");
		long lowCategoryId = 27l;
		long scheduleId = 0;
		if (json.containsKey("scheduleId"))
			scheduleId = json.getLongValue("scheduleId");
		String goodsNo = json.getString("goodsNo");
		String productName = json.getString("productName");
		long brandId = json.getLongValue("brandId");
		BigDecimal valueAddedTax = json.getBigDecimal("addedTax");
		String colorNum = json.getString("colorNum");
		String colorName = json.getString("colorName");
		int sizeType = json.getIntValue("sizeType");
		long sizeTemplateId = json.getLongValue("sizeTemplateId");
		BigDecimal marketPrice = json.getBigDecimal("marketPrice");
		BigDecimal salePrice = json.getBigDecimal("salePrice");
		BigDecimal basePrice = json.getBigDecimal("basePrice");
		String customEditHTML = json.getString("customEditHTML");
		int unit = json.getIntValue("unit");
		int airContraband = json.getIntValue("airContraband");
		int fragile = json.getIntValue("fragile");
		int big = json.getIntValue("big");
		int valuables = json.getIntValue("valuables");
		int consumptionTax = json.getIntValue("consumptionTax");
		String careLabel = json.getString("careLabel");
		String productDescp = json.getString("productDescp");
		String accessory = json.getString("accessory");
		String afterMarket = json.getString("afterMarket");
		String producing = json.getString("producing");
		String lenth = json.getString("length");
		String width = json.getString("width");
		String height = json.getString("height");
		String weight = json.getString("weight");

		int sameAsShop = json.getIntValue("sameAsShop");
		String wirelessTitle = json.getString("wirelessTitle");
		boolean isShowSizePic = json.getBooleanValue("isShowSizePic");
		long sizeAssistId = json.getLongValue("helperId");

		product.setId(pid);
		product.setScheduleId(scheduleId);
		product.setBasePrice(basePrice);
		product.setBrandId(brandId);
		product.setColorName(colorName);
		product.setColorNum(colorNum);
		product.setCustomEditHTML(customEditHTML);
		product.setGoodsNo(goodsNo);
		product.setLowCategoryId(lowCategoryId);
		product.setMarketPrice(marketPrice);
		product.setProductName(productName);
		product.setSalePrice(salePrice);
		product.setSizeTemplateId(sizeTemplateId);
		product.setSizeType(sizeType);
		product.setValueAddedTax(valueAddedTax);

		product.setSameAsShop(sameAsShop);
		product.setWirelessTitle(wirelessTitle);
		product.setUnit(unit);
		product.setAirContraband(airContraband);
		product.setFragile(fragile);
		product.setBig(big);
		product.setValuables(valuables);
		product.setConsumptionTax(consumptionTax);
		product.setCareLabel(careLabel);
		product.setProductDescp(productDescp);
		product.setAccessory(accessory);
		product.setAfterMarket(afterMarket);
		product.setProducing(producing);
		product.setLenth(lenth);
		product.setWidth(width);
		product.setHeight(height);
		product.setWeight(weight);
		product.setSizeAssistId(sizeAssistId);
		product.setIsShowSizePic(isShowSizePic);
		List<SkuSaveParam> skuList = new ArrayList<SkuSaveParam>();
		if (sizeType == 3) {
			JSONArray skuArray = json.getJSONArray("skuList");
			for (int i = 0; i < skuArray.size(); i++) {
				JSONObject tmp = skuArray.getJSONObject(i);
				long id = tmp.getLongValue("id");
				String barCode = tmp.getString("barCode");
				List<String> customizedSizeValue = JSONArray.toJavaObject(tmp.getJSONArray("body"), List.class);
				SkuSaveParam sku = new SkuSaveParam();
				sku.setBarCode(barCode);
				sku.setId(id);
				sku.setCustomizedSizeValue(customizedSizeValue);
				skuList.add(sku);
			}
		} else {
			JSONArray skuArray = json.getJSONArray("skuList2");
			for (int i = 0; i < skuArray.size(); i++) {
				JSONObject tmp = skuArray.getJSONObject(i);
				long id = tmp.getLongValue("id");
				String barCode = tmp.getString("barCode");
				int sizeIndex = tmp.getIntValue("sizeId");
				SkuSaveParam sku = new SkuSaveParam();
				sku.setBarCode(barCode);
				sku.setId(id);
				sku.setSizeIndex(sizeIndex);
				skuList.add(sku);
			}
		}
		product.setSKUList(skuList);
		List<SizeColumnParam> list = new ArrayList<SizeColumnParam>();
		JSONArray sizeHeader = json.getJSONArray("sizeHeader");
		for (int i = 0; i < sizeHeader.size(); i++) {
			JSONObject tmp = sizeHeader.getJSONObject(i);
			SizeColumnParam sizeColumnParam = new SizeColumnParam();
			sizeColumnParam.setId(tmp.getLongValue("id"));
			sizeColumnParam.setName(tmp.getString("name"));
			sizeColumnParam.setRequired(tmp.getBoolean("required"));
			list.add(sizeColumnParam);
		}
		product.setSizeHeader(list);

		List<ProdParamParam> productParamList = new ArrayList<ProdParamParam>();
		JSONArray productParamArray = json.getJSONArray("productParamList");
		for (int i = 0; i < productParamArray.size(); i++) {
			JSONObject tmp = productParamArray.getJSONObject(i);
			ProdParamParam prodParam = JSONObject.toJavaObject(tmp, ProdParamParam.class);
			productParamList.add(prodParam);
		}
		product.setProductParamList(productParamList);

		List<String> prodShowPicList = JSONArray.toJavaObject(json.getJSONArray("prodShowPicList"), List.class);
		JSONArray jsona = json.getJSONArray("prodShowPicList");
		product.setProdShowPicList(prodShowPicList);
		List<String> listShowPicList = JSONArray.toJavaObject(json.getJSONArray("listShowPicList"), List.class);
		product.setListShowPicList(listShowPicList);
		List<String> detailShowPicList = JSONArray.toJavaObject(json.getJSONArray("detailShowPicList"), List.class);
		product.setDetailShowPicList(detailShowPicList);
		return product;
	}

	public static ProductSearchParam convertProdSearchVOToDTO(ProductSearchVO searchVO) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ProductSearchParam param = new ProductSearchParam();
		param.setBarCode(searchVO.getBarCode());
		param.setGoodsNo(searchVO.getGoodsNo());
		param.setSupplierId(searchVO.getSupplierId());
		param.setOffset(searchVO.getOffset());
		if (searchVO.getIsBaseInfo() == 0)
			param.setIsBaseInfo(-1);
		else if (searchVO.getIsBaseInfo() == 2)
			param.setIsBaseInfo(0);
		else
			param.setIsBaseInfo(8);

		if (searchVO.getIsDetailInfo() == 0)
			param.setIsDetailInfo(-1);
		else if (searchVO.getIsDetailInfo() == 2)
			param.setIsDetailInfo(0);
		else
			param.setIsDetailInfo(1);

		if (searchVO.getIsPicInfo() == 0)
			param.setIsPicInfo(-1);
		else if (searchVO.getIsPicInfo() == 2)
			param.setIsPicInfo(0);
		else
			param.setIsPicInfo(2);

		if (searchVO.getIsSizeSet() == 0)
			param.setIsSizeSet(-1);
		else if (searchVO.getIsSizeSet() == 2)
			param.setIsSizeSet(0);
		else
			param.setIsSizeSet(4);

		param.setLastId(searchVO.getLastId());
		param.setLimit(searchVO.getLimit());
		param.setLowCategoryId(searchVO.getLowCategoryId());
		param.setProductName(searchVO.getProductName());
		param.setStime(searchVO.getStime());
		param.setEtime(searchVO.getEtime());
		return param;
	}

	public static POProductSearchParam convertProdSearchVOToDTO(PoProductSearchVO searchVO) {
		POProductSearchParam param = new POProductSearchParam();
		param.setPoId(searchVO.getPoId());
		param.setBarCode(searchVO.getBarCode());
		param.setGoodsNo(searchVO.getGoodsNo());
		param.setLastId(searchVO.getLastId());
		param.setLimit(searchVO.getLimit());
		param.setOffset(searchVO.getOffset());
		param.setLowCategoryId(searchVO.getLowCategoryId());
		param.setProductName(searchVO.getProductName());
		if (searchVO.getStatus() == 1) {
			param.setStatus(StatusType.NOTSUBMIT);
		} else if (searchVO.getStatus() == 2) {
			param.setStatus(StatusType.PENDING);
		} else if (searchVO.getStatus() == 3) {
			param.setStatus(StatusType.APPROVAL);
		} else if (searchVO.getStatus() == 4) {
			param.setStatus(StatusType.REJECT);
		} else {
			param.setStatus(StatusType.NULL);
		}
		return param;
	}

	public static ExportProductBodyVO exportProductTransfer(ExcelExportProduct dto, long supId) {
		long sheet = 0;
		if (supId == 1 || supId == 2)
			sheet = 1;
		else if (supId == 3)
			sheet = 2;
		else
			sheet = 3;
		List<ProductAttr> attrList = ItemCenterUtils.attrMap.get(sheet);
		ExportProductBodyVO vo = new ExportProductBodyVO();
		Class targetClass = ExportProductBodyVO.class;
		String attrName = null;
		try {
			Field[] fields = targetClass.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				ExcelExportField annota = field.getAnnotation(ExcelExportField.class);
				if (annota == null)
					continue;
				int cellIndex = annota.cellIndex();
				attrName = field.getName();
				Class fieldTypeClass = field.getType();
				Method setMethod = ReflectUtil.genMethod(targetClass, field, false);
				if (1 <= cellIndex && cellIndex <= 15) {
					try {
						String methodNameOfGet = ReflectUtil.getGetMethodNameByField(field);
						Method method = dto.getClass().getMethod(methodNameOfGet);
						Object result = method.invoke(dto);

						Object value = null;
						if (ExcelEnumInterface.class.isAssignableFrom(fieldTypeClass)) {
							if (result instanceof Boolean) {
								if ((boolean) result) {
									value = ((ExcelEnumInterface) fieldTypeClass.getEnumConstants()[0])
											.genEnumByIntValue(1);
								} else {
									value = ((ExcelEnumInterface) fieldTypeClass.getEnumConstants()[0])
											.genEnumByIntValue(0);
								}
							} else {
								value = ((ExcelEnumInterface) fieldTypeClass.getEnumConstants()[0])
										.genEnumByIntValue((int) result);
							}
						} else
							value = result;
						setMethod.invoke(vo, value);
					} catch (IllegalArgumentException | InvocationTargetException e) {
						logger.warn("copy of attrName::" + attrName + " has skip while extractData from "
								+ dto.getClass() + " to" + targetClass);
						continue;
					}
				} else if (cellIndex > 15) {
					List<ProductParamDTO> paraList = dto.getParamList();
					int attrIndex = cellIndex - 16;
					ProductAttr attr = attrList.get(attrIndex);
					long attrId = attr.getId();
					String attrVal = null;
					for (ProductParamDTO param : paraList) {
						if (param.getId() == attrId) {
							attrVal = param.getParamValue();
							break;
						}
					}
					if (attrVal == null)
						attrVal = "";
					setMethod.invoke(vo, attrVal);
				}
			}
		} catch (Exception e) {
			logger.error("####" + attrName);
			logger.error(e.getMessage(), e);
			throw new ClassCastException(dto.getClass().getName() + "is not a subClass of " + targetClass.getName());
		}
		return vo;
	}

	public static ProductEditVO getProductEditVO(ProductFullDTO product) {
		ProductEditVO vo = new ProductEditVO();
		vo.setBasePrice(product.getBasePrice());
		vo.setBrandId(String.valueOf(product.getBrandId()));
		vo.setSalePrice(product.getSalePrice());
		vo.setMarketPrice(product.getMarketPrice());
		vo.setGoodsNo(product.getGoodsNo());
		vo.setColorName(product.getColorName());
		vo.setColorNum(product.getColorNum());
		vo.setSameAsShop(product.getSameAsShop());
		if (product.isRecommend())
			vo.setIsRecommend(1);
		else
			vo.setIsRecommend(0);
		vo.setWirelessTitle(product.getWirelessTitle());
		vo.setCustomEditHTML(product.getCustomEditHTML());
		vo.setProductId(String.valueOf(product.getId()));
		vo.setProductName(product.getProductName());
		vo.setSizeTemplateId(String.valueOf(product.getSizeTemplateId()));
		vo.setSizeType(product.getSizeType().getIntValue());
		vo.setProdShowPicList(product.getProdShowPicList());
		vo.setListShowPicList(product.getListShowPicList());
		vo.setUnit(product.getUnit());
		vo.setAirContraband(product.getAirContraband());
		vo.setFragile(product.getFragile());
		vo.setBig(product.getBig());
		vo.setValuables(product.getValuables());
		vo.setConsumptionTax(product.getConsumptionTax());
		vo.setCareLabel(product.getCareLabel());
		vo.setProductDescp(product.getProductDescp());
		vo.setAccessory(product.getAccessory());
		vo.setAfterMarket(product.getAfterMarket());
		vo.setProducing(product.getProducing());
		vo.setLength(product.getLenth());
		vo.setWidth(product.getWidth());
		vo.setHeight(product.getHeight());
		vo.setWeight(product.getWeight());
		if (product.getSizeAssistId() != 0)
			vo.setHelperId(String.valueOf(product.getSizeAssistId()));
		if (product.isShowSizePic())
			vo.setIsShowSizePic(1);
		else
			vo.setIsShowSizePic(0);
		if (!StringUtils.isBlank(product.getCustomEditHTML())) {
			try {
				String html = ItemCenterUtil.InputStreamTOString(product.getCustomEditHTML());
				vo.setCustomEditHTML(html);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				vo.setCustomEditHTML(null);
			}
		}
		return vo;
	}
}
