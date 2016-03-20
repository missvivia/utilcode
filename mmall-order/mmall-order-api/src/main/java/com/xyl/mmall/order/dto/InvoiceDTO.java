package com.xyl.mmall.order.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.order.enums.OperateUserType;
import com.xyl.mmall.order.meta.Invoice;

/**
 * 
 * @author author:lhp
 *
 * @version date:2015年6月5日下午1:24:11
 */
public class InvoiceDTO extends Invoice{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5323233050158892734L;
	
	/**
	 * 操作备注
	 */
	private String comment;
	
	/**
	 * 操作用户类型
	 */
	private OperateUserType operateUserType;
	

	public InvoiceDTO(){
		
	}
	
	public InvoiceDTO(Invoice invoice){
		ReflectUtil.convertObj(this, invoice, false);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public OperateUserType getOperateUserType() {
		return operateUserType;
	}

	public void setOperateUserType(OperateUserType operateUserType) {
		this.operateUserType = operateUserType;
	}

	
	

}
