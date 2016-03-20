package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 尺寸模板的尺寸信息记录对象
 * 
 * @author hzhuangluqian
 *
 */
public class SizeTmplTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7691875215743880435L;

	/** 尺寸模板尺寸模板详情表格的表头字段列表 */
	private List<SizeColumnParam> header;

	/** 尺寸模板尺寸模板详情表格的记录列表 */
	private List<List<String>> body;

	public List<SizeColumnParam> getHeader() {
		return header;
	}

	public void setHeader(List<SizeColumnParam> header) {
		this.header = header;
	}

	public List<List<String>> getBody() {
		return body;
	}

	public void setBody(List<List<String>> body) {
		this.body = body;
	}

}
