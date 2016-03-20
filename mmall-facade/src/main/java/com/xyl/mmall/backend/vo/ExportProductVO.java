package com.xyl.mmall.backend.vo;

import java.io.Serializable;
import java.util.List;

public class ExportProductVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3670566431548912125L;

	private ExportProductHeader header;

	private List<ExportProductBodyVO> body;

	public ExportProductHeader getHeader() {
		return header;
	}

	public void setHeader(ExportProductHeader header) {
		this.header = header;
	}

	public List<ExportProductBodyVO> getBody() {
		return body;
	}

	public void setBody(List<ExportProductBodyVO> body) {
		this.body = body;
	}
}
