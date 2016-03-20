package com.xyl.mmall.oms.dto;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.oms.meta.PoOrderForm;

/**
 * PO单列表Dto
 * 
 * @author zengdan
 * @date 2014-09-03
 */
public class PoOrderDTO extends PoOrderForm {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 633491723721019295L;

	public PoOrderDTO(PoOrderForm obj) {
		ReflectUtil.convertObj(this, obj, false);
	}

	/**
	 * 未拣货数量
	 */
	private int unPickAmount;
	
	private int soldAmount ;

	public int getUnPickAmount() {
		return unPickAmount;
	}

	public void setUnPickAmount(int unPickAmount) {
		this.unPickAmount = unPickAmount;
	}

	public int getSoldAmount() {
		return soldAmount;
	}

	public void setSoldAmount(int soldAmount) {
		this.soldAmount = soldAmount;
	}


}
