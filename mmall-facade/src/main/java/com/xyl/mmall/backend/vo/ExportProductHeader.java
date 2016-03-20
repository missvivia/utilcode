package com.xyl.mmall.backend.vo;

import java.util.List;

import com.xyl.mmall.framework.vo.SimpleIdNamePaire;

public class ExportProductHeader {
	public static String[] FIX_HEADERS = { "货号", "条形码", "商品名称 ", "无线短标题 ", "商品规格", "是否显示尺寸图", "尺码助手id", "性别 ", "商品颜色",
			"商品色号", "正品价", "销售价 ", "供货价", "增值税率", "售卖单位", "最低层类目id", "是否专柜同款 ", "是否航空禁运品", "是否易碎品", "是否大件", "是否贵重品",
			"是否消费税", "洗涤、使用说明", "商品描述", "配件说明", "售后说明", "产地", "长", "宽", "高", "重量" };

	private List<SimpleIdNamePaire> attrHeaders;

	public List<SimpleIdNamePaire> getAttrHeaders() {
		return attrHeaders;
	}

	public void setAttrHeaders(List<SimpleIdNamePaire> attrHeaders) {
		this.attrHeaders = attrHeaders;
	}
}
