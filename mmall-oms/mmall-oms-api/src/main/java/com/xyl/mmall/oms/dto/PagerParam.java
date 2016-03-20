package com.xyl.mmall.oms.dto;

import java.io.Serializable;

/**
 * 
 * @author hzzhanghui
 *
 */
public class PagerParam implements Serializable{

	private static final long serialVersionUID = 1868429507117745208L;

	public int total;
	
	public boolean hasNext;
	
	public int offset;
	
	public int pageSize;

	@Override
	public String toString() {
		return "PagerParam [total=" + total + ", hasNext=" + hasNext + ", offset=" + offset + ", pageSize=" + pageSize
				+ "]";
	}
}
