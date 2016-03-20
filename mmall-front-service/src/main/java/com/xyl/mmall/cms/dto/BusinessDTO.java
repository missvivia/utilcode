package com.xyl.mmall.cms.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.netease.print.daojar.util.ReflectUtil;
import com.xyl.mmall.cms.meta.Business;

/**
 * 商家信息
 * 
 * @author hzchaizhf
 * @create 2014年9月16日
 *
 */
public class BusinessDTO extends Business implements Comparator<BusinessDTO>{

	private static final long serialVersionUID = 20140916L;
	
	private String actingBrandName;
	
	private String warehouseName;
	
	private List<String> areaNames;
	
	//编辑时配送区域删除ids
	private List<Long> areaIds;
	
	private List<SendDistrictDTO>sendDistrictDTOs;
	
	/**
	 * 指定用户关系
	 */
	private List<BusiUserRelationDTO>busiUserRelations;
	
	private Map<Long,String> areaDetail;
	
	/**
	 * 编辑时供应商特许经营类型是否改变  默认N， 改变Y
	 */
	private String typeIsChange = "N";
	
	/**
	 * 商家密码
	 */
	private String password;
	
	/**
	 * 编辑时密码是否修改  默认N， 改变Y
	 */
	private String passwordIsChange = "N";
	
	public String getTypeIsChange() {
		return typeIsChange;
	}

	public void setTypeIsChange(String typeIsChange) {
		this.typeIsChange = typeIsChange;
	}

	/**
	 * 默认构造函数
	 */
	public BusinessDTO() {
	}
	
	/**
	 * 构造函数
	 * 
	 * @param obj
	 */
	public BusinessDTO(Business obj) {
		ReflectUtil.convertObj(this, obj, false);
	}
	
	public List<SendDistrictDTO> getSendDistrictDTOs() {
		return sendDistrictDTOs;
	}

	public void setSendDistrictDTOs(List<SendDistrictDTO> sendDistrictDTOs) {
		this.sendDistrictDTOs = sendDistrictDTOs;
	}

	public List<BusiUserRelationDTO> getBusiUserRelations() {
		return busiUserRelations;
	}

	public void setBusiUserRelations(List<BusiUserRelationDTO> busiUserRelations) {
		this.busiUserRelations = busiUserRelations;
	}
	
	public String getActingBrandName() {
		return actingBrandName;
	}

	public void setActingBrandName(String actingBrandName) {
		this.actingBrandName = actingBrandName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public List<String> getAreaNames() {
		return areaNames;
	}

	public void setAreaNames(List<String> areaNames) {
		this.areaNames = areaNames;
	}

	public List<Long> getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(List<Long> areaIds) {
		this.areaIds = areaIds;
	}
	
	public Map<Long, String> getAreaDetail() {
		return areaDetail;
	}

	public void setAreaDetail(Map<Long, String> areaDetail) {
		this.areaDetail = areaDetail;
	}

	@Override
	public int compare(BusinessDTO o1, BusinessDTO o2) {
		return (int) (-o1.getCreateTime()+o2.getCreateTime());
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordIsChange() {
		return passwordIsChange;
	}

	public void setPasswordIsChange(String passwordIsChange) {
		this.passwordIsChange = passwordIsChange;
	}
	
	
	
	
}
