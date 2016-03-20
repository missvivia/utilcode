package com.xyl.mmall.itemcenter.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dto.BaseSearchResult;
import com.xyl.mmall.itemcenter.meta.ProdPicMap;
import com.xyl.mmall.itemcenter.param.ProductSaveParam;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SkuSaveParam;

public class ItemCenterUtil {
	private static final Logger logger = LoggerFactory.getLogger(ItemCenterUtil.class);

	final static int BUFFER_SIZE = 4096;

	public static Object extractData(Object source, Class targetClass) {
		Method methodOfSet = null;
		try {
			Object target = targetClass.newInstance();
			Field[] fields = targetClass.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				try {
					// 生成需要比较字段的读取方法名称
					String methodNameOfGet = ReflectUtil.getGetMethodNameByField(field), methodNameOfSet = ReflectUtil
							.getSetMethodNameByField(field);
					// 得到需要调用的Method
					Method methodOfGet = source.getClass().getMethod(methodNameOfGet);
					methodOfSet = targetClass.getMethod(methodNameOfSet, field.getType());
					// 调用Mehtod的invoke方法获得返回值
					Object result = methodOfGet.invoke(source);
					if (result != null) {
						if (result.getClass().getName().equals("java.lang.Long")
								&& field.getType().getName().equals("java.lang.String")) {

							String s = String.valueOf(result);
							try {
								methodOfSet.invoke(target, s);
							} catch (Exception e) {
								logger.warn("invoke method::" + methodOfSet.getName() + " failed");
								continue;
							}
						} else {
							try {
								methodOfSet.invoke(target, result);
							} catch (Exception e) {
								logger.warn("invoke method::" + methodOfSet.getName() + " failed");
								continue;
							}
						}
					}

				} catch (NoSuchMethodException e) {
					continue;
				}
			}
			return target;
		} catch (Exception e) {
			throw new ClassCastException(methodOfSet.getName() + " is not compatible");
		}
	}

	public static Object copyData(Object source, Object target) {
		if (target == null || source == null) {
			return null;
		}
		Method methodOfSet = null;
		Class targetClass = target.getClass();
		try {
			Field[] fields = targetClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				try {
					// 生成需要比较字段的读取方法名称
					String methodNameOfGet = ReflectUtil.getGetMethodNameByField(field), methodNameOfSet = ReflectUtil
							.getSetMethodNameByField(field);
					// 得到需要调用的Method
					Method methodOfGet = source.getClass().getMethod(methodNameOfGet);
					methodOfSet = targetClass.getMethod(methodNameOfSet, field.getType());
					// 调用Mehtod的invoke方法获得返回值
					Object result = methodOfGet.invoke(source);
					if (result != null) {
						if (result.getClass().getName().equals("java.lang.Long")
								&& field.getType().getName().equals("java.lang.String")) {

							String s = String.valueOf(result);
							try {
								methodOfSet.invoke(target, s);
							} catch (Exception e) {
								logger.warn("invoke method::" + methodOfSet.getName() + " failed");
								continue;
							}
						} else {
							try {
								methodOfSet.invoke(target, result);
							} catch (Exception e) {
								logger.warn("invoke method::" + methodOfSet.getName() + " failed");
								continue;
							}
						}
					}
				} catch (NoSuchMethodException e) {
					continue;
				}
			}
			return target;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ClassCastException(source.getClass().getName() + "is not a subClass of " + targetClass.getName());
		}
	}

	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}

	public static ProdPicMap genProdPicMap(List<String> pathList) {
		ProdPicMap obj = new ProdPicMap();
		StringBuffer sb = new StringBuffer("");
		int lastVersion = 0;
		if (pathList != null && pathList.size() > 0) {
			for (int i = 0; i < pathList.size(); i++) {
				String path = pathList.get(i);
				int tempVersion = 0;
				if (StringUtils.startsWith(path, ConstantsUtil.NOS_URL1)) {
					tempVersion = 0;
					path = StringUtils.remove(path, ConstantsUtil.NOS_URL1);
				} else if (StringUtils.startsWith(path, ConstantsUtil.NOS_URL3)) {
					tempVersion = 1;
					path = StringUtils.remove(path, ConstantsUtil.NOS_URL3);
				} else
					throw new RuntimeException("Invalid nos link!");
				if (i == 0)
					lastVersion = tempVersion;
				if (lastVersion != tempVersion)
					throw new RuntimeException("not same nos link!");
				if (i == pathList.size() - 1) {
					sb.append(path);
				} else {
					sb.append(path + ";");
				}
			}
			obj.setNosVersion(lastVersion);
			obj.setPicPath(sb.toString());
			return obj;
		} else
			return null;
	}

	public static List<String> genProdPicPath(ProdPicMap pic) {
		List<String> pathList = new ArrayList<String>();
		if (pic != null) {
			String path = pic.getPicPath();
			int version = pic.getNosVersion();
			if (!StringUtils.isBlank(path)) {
				String[] pathAry = path.split(ProdPicMap.split);
				for (int i = 0; i < pathAry.length; i++) {
					String showPath = pathAry[i];
					if (!StringUtils.isBlank(showPath) && showPath.indexOf(ConstantsUtil.NOS_URL2) < 0) {
						if (version == 0)
							showPath = ConstantsUtil.NOS_URL1 + showPath;
						else
							showPath = ConstantsUtil.NOS_URL3 + showPath;
					}
					pathList.add(showPath);
				}
			}
		}
		return pathList;
	}

	public static String genProdPicPath(String showPath) {
		if (!StringUtils.isBlank(showPath) && showPath.indexOf(ConstantsUtil.NOS_URL2) < 0)
			showPath = ConstantsUtil.NOS_URL1 + showPath;
		return showPath;
	}

	public static String getSpecOptionValue(List<SizeColumnParam> colList, List<String> sizeValue) {
		int index = -1;
		for (int j = 0; j < colList.size(); j++) {
			SizeColumnParam column = colList.get(j);
			if (column.isRequired()) {
				index = j;
				break;
			}
		}
		if (index >= 0)
			return sizeValue.get(index);
		else
			return "";
	}

	public static long getTime() {
		return new Date().getTime();
	}

	public static Map<String, Object> checkSaveProductParam(ProductSaveParam productSave) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("result", true);
		if (StringUtils.isBlank(productSave.getGoodsNo())) {
			retMap.put("result", false);
			retMap.put("货号", "必填项未录入");
		} else if (productSave.getGoodsNo().length() > 32) {
			retMap.put("result", false);
			retMap.put("货号", "文字必须在32字以内");
		}

		if (StringUtils.isBlank(productSave.getProductName())) {
			retMap.put("result", false);
			retMap.put("商品名称", "必填项未录入");
		} else if (productSave.getProductName().length() > 30) {
			retMap.put("result", false);
			retMap.put("商品名称", "文字必须在30字以内");
		}

		if (StringUtils.isBlank(productSave.getWirelessTitle())) {
			retMap.put("result", false);
			retMap.put("移动短标题", "必填项未录入");
		} else if (productSave.getWirelessTitle().length() > 20) {
			retMap.put("result", false);
			retMap.put("移动短标题", "文字必须在20字以内");
		}

		if (StringUtils.isBlank(productSave.getColorName())) {
			retMap.put("result", false);
			retMap.put("商品颜色", "必填项未录入");
		} else if (productSave.getColorName().length() > 12) {
			retMap.put("result", false);
			retMap.put("商品颜色", "文字必须在12字以内");
		}

		if (StringUtils.isBlank(productSave.getColorNum())) {
			retMap.put("result", false);
			retMap.put("商品色号", "必填项未录入");
		} else if (productSave.getColorNum().length() > 12) {
			retMap.put("result", false);
			retMap.put("商品色号", "文字必须在12字符以内");
		}

		if (productSave.getMarketPrice() == null) {
			retMap.put("result", false);
			retMap.put("商品挂牌价", "必填项未录入");
		} else if (productSave.getMarketPrice().floatValue() > 9999999) {
			retMap.put("result", false);
			retMap.put("商品挂牌价", "超过最大上限9999999");
		}

		if (productSave.getSalePrice() == null) {
			retMap.put("result", false);
			retMap.put("商品折扣价", "必填项未录入");
		} else if (productSave.getSalePrice().floatValue() > 9999999) {
			retMap.put("result", false);
			retMap.put("商品折扣价", "超过最大上限9999999");
		}

		if (productSave.getUnit() <= 0) {
			retMap.put("result", false);
			retMap.put("售卖单位", "必填项未录入");
		}

		if (StringUtils.isBlank(productSave.getCareLabel())) {
			retMap.put("result", false);
			retMap.put("洗涤/使用说明", "必填项未录入");
		} else if (productSave.getCareLabel().length() > 180) {
			retMap.put("result", false);
			retMap.put("洗涤/使用说明", "文字必须在180字以内");
		}

		if (StringUtils.isBlank(productSave.getProductDescp())) {
			retMap.put("result", false);
			retMap.put("商品描述", "必填项未录入");
		} else if (productSave.getProductDescp().length() > 180) {
			retMap.put("result", false);
			retMap.put("商品描述", "文字必须在180字以内");
		}

		if (!StringUtils.isBlank(productSave.getAccessory()) && productSave.getAccessory().length() > 180) {
			retMap.put("result", false);
			retMap.put("配件备注", "文字必须在180字以内");
		}
		if (!StringUtils.isBlank(productSave.getAfterMarket()) && productSave.getAfterMarket().length() > 180) {
			retMap.put("result", false);
			retMap.put("售后服务说明", "文字必须在180字以内");
		}

		if (StringUtils.isBlank(productSave.getProducing())) {
			retMap.put("result", false);
			retMap.put("产地", "必填项未录入");
		} else if (productSave.getProducing().length() > 180) {
			retMap.put("result", false);
			retMap.put("产地", "文字必须在180字以内");
		}
		if (!StringUtils.isBlank(productSave.getLenth())) {
			if (!isNumeric(productSave.getLenth())) {
				retMap.put("result", false);
				retMap.put("长", "必须为数字");
			} else if (productSave.getLenth().length() > 5) {
				retMap.put("result", false);
				retMap.put("长", "文字必须在5个字以内");
			}
		}

		if (!StringUtils.isBlank(productSave.getWeight())) {
			if (!isNumeric(productSave.getWeight())) {
				retMap.put("result", false);
				retMap.put("重量", "必须为数字");
			} else if (productSave.getWeight().length() > 5) {
				retMap.put("result", false);
				retMap.put("重量", "文字必须在5个字以内");
			}

		}

		if (!StringUtils.isBlank(productSave.getWidth())) {
			if (!isNumeric(productSave.getWidth())) {
				retMap.put("result", false);
				retMap.put("宽", "必须为数字");
			} else if (productSave.getWidth().length() > 5) {
				retMap.put("result", false);
				retMap.put("宽", "文字必须在5个字以内");
			}
		}

		if (!StringUtils.isBlank(productSave.getHeight())) {
			if (!isNumeric(productSave.getHeight())) {
				retMap.put("result", false);
				retMap.put("高", "必须为数字");
			} else if (productSave.getHeight().length() > 5) {
				retMap.put("result", false);
				retMap.put("高", "文字必须在5个字符");
			}
		}

		List<SkuSaveParam> skuList = productSave.getSKUList();
		if (skuList == null || skuList.size() == 0) {
			retMap.put("result", false);
			retMap.put("条形码", "至少录入一条");
		}
		for (SkuSaveParam sku : skuList) {
			String barCode = sku.getBarCode();
			if (StringUtils.isBlank(barCode)) {
				retMap.put("result", false);
				retMap.put("条形码", "必填项未录入");
			} else if (barCode.length() > 32) {
				retMap.put("result", false);
				retMap.put("条形码", "文字必须在32个字之内");
			}
			List<String> sizes = sku.getCustomizedSizeValue();
			if (sizes != null && sizes.size() > 0) {
				for (String size : sizes) {
					if (!StringUtils.isBlank(size) && size.length() > 10) {
						retMap.put("result", false);
						retMap.put("尺码", "文字必须在10个字之内");
					}
				}
			}
		}
		return retMap;
	}

	public static String InputStreamTOString(String urlStr) throws Exception {

		URL url = new URL(urlStr);
		HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		InputStream is = httpUrl.getInputStream();

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = is.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		httpUrl.disconnect();
		return new String(outStream.toByteArray());
	}

	public static Map<String, String> getEditHTML_Param(String url, String paramter) {
		Map<String, String> retMap = null;
		if (!StringUtils.isBlank(url)) {
			String nosStr = null;
			try {
				nosStr = InputStreamTOString(url);
				retMap = JsonUtils.fromJson(nosStr, Map.class);
			} catch (Exception e) {
				retMap = new HashMap<String, String>();
				retMap.put("html", nosStr);
				retMap.put("parameter", paramter);
			}

			if (retMap == null) {
				retMap = new HashMap<String, String>();
				retMap.put("html", nosStr);
				retMap.put("parameter", paramter);
			}
		} else {
			retMap = new HashMap<String, String>();
			retMap.put("html", url);
			retMap.put("parameter", paramter);
		}
		return retMap;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Pattern pattern2 = Pattern.compile("[0-9]+");
		Matcher isNum1 = pattern.matcher(str);
		Matcher isNum2 = pattern2.matcher(str);
		if (!isNum1.matches() && !isNum2.matches()) {
			return false;
		}
		return true;
	}

	public static <T> BaseSearchResult<T> genSearchResult(List<T> totalResult, int offset, int limit) {
		BaseSearchResult<T> result = new BaseSearchResult<T>();
		List<T> retList = new ArrayList<T>();
		int copyLimit = limit;
		boolean hasNext = false;
		if (offset <= totalResult.size() - 1) {
			for (int i = offset; i < totalResult.size(); i++) {
				T product = totalResult.get(i);
				retList.add(product);
				copyLimit--;
				if (copyLimit == 0)
					break;
			}
		}
		if ((limit + offset) < totalResult.size())
			hasNext = true;
		result.setHasNext(hasNext);
		result.setList(retList);
		result.setTotal(totalResult.size());
		return result;
	}
}
