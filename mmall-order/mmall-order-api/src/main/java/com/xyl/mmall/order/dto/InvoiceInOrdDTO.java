package com.xyl.mmall.order.dto;

import org.apache.commons.lang.StringUtils;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.order.meta.InvoiceInOrd;

/**
 * @author dingmingliang
 * 
 */
public class InvoiceInOrdDTO extends InvoiceInOrd {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;
	
	/**
	 * 修改前的发票
	 */
	private InvoiceInOrdDTO modifiedDto;

	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public InvoiceInOrdDTO(InvoiceInOrd obj) {
		ReflectUtil.convertObj(this, obj, false);
		if(StringUtils.isNotEmpty(obj.getModifiedValue())){
			modifiedDto = JsonUtils.fromJson(obj.getModifiedValue(), InvoiceInOrdDTO.class);
		}
	}

	/**
	 * 构造函数
	 */
	public InvoiceInOrdDTO() {
		super();
	}

	/**
	 * @param obj
	 * @return
	 */
	public static InvoiceInOrdDTO getInstance(InvoiceInOrd obj) {
		return obj == null ? null : new InvoiceInOrdDTO(obj);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}

	public InvoiceInOrdDTO getModifiedDto() {
		return modifiedDto;
	}

	public void setModifiedDto(InvoiceInOrdDTO modifiedDto) {
		this.modifiedDto = modifiedDto;
	}
	
	
}
