package com.xyl.mmall.cms.dto;

import java.util.Comparator;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.enums.PromotionContentStatus;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.saleschedule.meta.ScheduleBanner;

/**
 * 
 * @author hzliujie
 * @create 2014年9月24日
 *
 */
public class PromotionContentDTO extends PromotionContent implements Comparator<PromotionContentDTO> {

	
	private static final long serialVersionUID = 1L;
	private ScheduleBanner content;
	private int type;
	private String name;
	private String brandName;
	private PromotionContentStatus status;
	private Long searchTime;

	public PromotionContentStatus getStatus() {
		return status;
	}

	public void setStatus(PromotionContentStatus status) {
		this.status = status;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	private String supplierName;
	
	public PromotionContentDTO(PromotionContent obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public PromotionContentDTO() {
	}
	
	public ScheduleBanner getContent() {
		return content;
	}

	public void setContent(ScheduleBanner content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(Long searchTime) {
		this.searchTime = searchTime;
	}



	@Override
	public int compare(PromotionContentDTO o1, PromotionContentDTO o2) {
		return o1.getSequence()-o2.getSequence();
	}
	 
	
	
}
