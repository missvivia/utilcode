package com.xyl.mmall.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.xyl.mmall.backend.vo.SizeAssistAxis;
import com.xyl.mmall.backend.vo.SizeAssistVO;
import com.xyl.mmall.backend.vo.SizeHeaderVO;
import com.xyl.mmall.backend.vo.SizeTmplTableVO;
import com.xyl.mmall.common.param.ProductAttr;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.itemcenter.dto.BaseSkuDTO;
import com.xyl.mmall.itemcenter.dto.CategoryArchitect;
import com.xyl.mmall.itemcenter.dto.ProductParamDTO;
import com.xyl.mmall.itemcenter.enums.ProdDetailType;
import com.xyl.mmall.itemcenter.meta.ProductParamOption;
import com.xyl.mmall.itemcenter.meta.SizeAssist;
import com.xyl.mmall.itemcenter.param.SizeColumnParam;
import com.xyl.mmall.itemcenter.param.SizeTable;
import com.xyl.mmall.mainsite.vo.BaseNameValueVO;

public class ItemCenterUtils {
	public static final Map<Long, List<ProductAttr>> attrMap = new HashMap<Long, List<ProductAttr>>();
	static {
		List<ProductAttr> attr1 = new ArrayList<ProductAttr>();
		attr1.add(new ProductAttr(409, true, "年份"));
		attr1.add(new ProductAttr(44, true, "适用季节"));
		attr1.add(new ProductAttr(45, true, "适用性别"));
		attr1.add(new ProductAttr(410, true, "成分含量"));
		attr1.add(new ProductAttr(30, false, "填充物"));
		attrMap.put(1l, attr1);

		List<ProductAttr> attr2 = new ArrayList<ProductAttr>();
		attr2.add(new ProductAttr(409, true, "年份"));
		attr2.add(new ProductAttr(44, true, "适用季节"));
		attr2.add(new ProductAttr(411, true, "适用性别"));
		attr2.add(new ProductAttr(410, true, "成分含量"));
		attr2.add(new ProductAttr(30, false, "填充物"));
		attrMap.put(2l, attr2);

		List<ProductAttr> attr3 = new ArrayList<ProductAttr>();
		attr3.add(new ProductAttr(409, true, "年份"));
		attr3.add(new ProductAttr(44, true, "适用季节"));
		attr3.add(new ProductAttr(412, true, "适用性别"));
		attr3.add(new ProductAttr(410, true, "成分含量"));
		attr3.add(new ProductAttr(231, false, "填充物"));
		attrMap.put(3l, attr3);
	}

	public static List<BaseNameValueVO> productParamVOTransfer(List<ProductParamDTO> list) {
		List<BaseNameValueVO> prodParamList = new ArrayList<BaseNameValueVO>();
		for (ProductParamDTO p : list) {
			String paramValue = p.getParamValue();
			if (!StringUtils.isBlank(paramValue)) {
				BaseNameValueVO pvo = new BaseNameValueVO();
				pvo.setName(p.getName());
				pvo.setType(p.getDetailType().getIntValue());
				if (p.getDetailType() == ProdDetailType.SINGLE_SELECT) {
					long optId = Long.valueOf(paramValue);
					List<ProductParamOption> optList = p.getOptionList();
					for (ProductParamOption opt : optList) {
						if (opt.getId() == optId) {
							pvo.setValue(opt.getValue());
						}
					}
				} else if (p.getDetailType() == ProdDetailType.MULTI_SELECT) {
					List<Long> paramValues = JsonUtils.parseArray(p.getParamValue(), Long.class);
					List<ProductParamOption> optList = p.getOptionList();
					StringBuffer sb = new StringBuffer();
					if (paramValues != null && paramValues.size() > 0) {
						for (long sOptId : paramValues) {
							for (ProductParamOption opt : optList) {
								if (opt.getId() == sOptId) {
									sb.append(opt.getValue()).append("，");
								}
							}
						}
						pvo.setValue(sb.toString().trim());
					} else
						continue;

				} else {
					pvo.setValue(p.getParamValue());
				}
				prodParamList.add(pvo);
			}
		}
		return prodParamList;
	}

	public static List<Long> getSkuList(List<? extends BaseSkuDTO> skuList) {
		List<Long> retList = new ArrayList<Long>();
		for (BaseSkuDTO sku : skuList) {
			retList.add(sku.getId());
		}
		return retList;
	}

	public static int getStockType(int cartStock, int orderStock) {
		if (cartStock == 0 && orderStock == 0) {
			return 3;
		} else if (cartStock == 0 && orderStock > 0) {
			return 2;
		} else
			return 1;
	}

	public static SizeAssistVO getSizeAssistVO(SizeAssist assist) {
		SizeAssistVO vo = new SizeAssistVO();
		vo.setId(String.valueOf(assist.getId()));
		vo.setName(assist.getName());
		SizeAssistAxis haxis = new SizeAssistAxis();
		haxis.setName(assist.getHaxisName());
		List<?> haxisValue = JsonUtils.parseArray(assist.getHaxisValue(), double.class);
		haxis.setList(haxisValue);
		vo.setHaxis(haxis);
		SizeAssistAxis vaxis = new SizeAssistAxis();
		vaxis.setName(assist.getVaxisName());
		List<?> vaxisValue = JsonUtils.parseArray(assist.getVaxisValue(), double.class);
		vaxis.setList(vaxisValue);
		vo.setVaxis(vaxis);
		if (!StringUtils.isBlank(assist.getBody())) {
			List body = JsonUtils.parseArray(assist.getBody(), List.class);
			vo.setBody(body);
		} else {
			vo.setBody(new ArrayList<List<?>>());
		}
		return vo;
	}

	/**
	 * 默认选择类目
	 * 
	 * @param retList
	 * @param ca
	 */
	public static void genSelectCategories(List<String> retList, CategoryArchitect ca) {
		retList.add(String.valueOf(ca.getId()));
		List<CategoryArchitect> cList = ca.getList();
		if (cList != null && cList.size() > 0) {
			CategoryArchitect nca = cList.get(0);
			genSelectCategories(retList, nca);
		}
	}

	public static double formatDouble1(double d) {
		return (double) Math.round(d * 100) / 100;
	}

	public static SizeTmplTableVO sizeTableVOTransfer(SizeTable table) {
		SizeTmplTableVO vo = new SizeTmplTableVO();
		List<SizeHeaderVO> headerList = new ArrayList<SizeHeaderVO>();
		List<SizeColumnParam> headers = table.getSizeHeader();
		if (headers != null && headers.size() > 0) {
			for (SizeColumnParam sizeColumn : headers) {
				SizeHeaderVO headervo = new SizeHeaderVO();
				headervo.setId(String.valueOf(sizeColumn.getId()));
				headervo.setName(sizeColumn.getName());
				headervo.setRequired(sizeColumn.getIsRequired());
				headervo.setUnit(sizeColumn.getUnit());
				headerList.add(headervo);
			}
		}
		vo.setHeader(headerList);
		List<List<String>> body = new ArrayList<List<String>>();
		List<Long> recordList = table.getRecordList();
		if (recordList != null && recordList.size() > 0) {
			for (long recordIndx : recordList) {
				List<String> record = new ArrayList<String>();
				if (headers != null && headers.size() > 0) {
					for (SizeColumnParam sizeColumn : headers) {
						String key = recordIndx + "+" + sizeColumn.getId();
						record.add(table.getValueMap().get(key));
					}
				}
				body.add(record);
			}
		}
		vo.setBody(body);
		return vo;
	}
}
