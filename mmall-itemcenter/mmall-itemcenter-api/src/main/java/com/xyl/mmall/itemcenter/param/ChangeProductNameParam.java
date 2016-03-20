package com.xyl.mmall.itemcenter.param;

import java.io.Serializable;
import java.util.List;

public class ChangeProductNameParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8845306323770952473L;

	private long supplierId;

	private List<Long> pid;

	private String header;

	private String tailer;

	private String replace;

	private String replacement;

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public List<Long> getPid() {
		return pid;
	}

	public void setPid(List<Long> pid) {
		this.pid = pid;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getTailer() {
		return tailer;
	}

	public void setTailer(String tailer) {
		this.tailer = tailer;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
}
