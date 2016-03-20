package com.xyl.mmall.ip.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.ip.meta.LocationCode;

public interface LocationCodeDao extends AbstractDao<LocationCode> {

	/**
	 * 获得所有省份的code数据
	 * @return
	 */
	public List<LocationCode> getAllProvince();
	/**
	 * 按照顺序获得省份的分组
	 * @return
	 */
	public JSONObject getAllProvinceInOrder();
	/**
	 * 通过省份code返回对应的市的列表
	 * @param provinceCode
	 * @return
	 */
	public List<LocationCode> getCityListByProvinceCode(long provinceCode);
	/**
	 * 通过城市code返回对应的区或者县的列表
	 * @param cityCode
	 * @return
	 */
	public List<LocationCode> getDistrictListByCityCode(long cityCode);
	/**
	 * 通过区code返回街道的列表
	 * @param districtCode
	 * @return
	 */
	public List<LocationCode> getStreetListByDistrictCode(long districtCode);
	/**
	 * 通过code返回对应位置的名称
	 * @param code
	 * @return
	 */
	public String getLocationNameByCode(long code);
	/**
	 * 返回timeStamp之后的新数据
	 * @param timeStamp
	 * @return
	 */
	public List<LocationCode> getLocationCodeListAfterTimeStamp(long timeStamp);
	/**
	 * @param codeList
	 * @return
	 */
	public List<LocationCode> getLocationCodeListByCodeList(List<Long> codeList);
	/**
	 * 通过区域供应商列表返回对应的地址名称
	 * @param ids
	 * @return
	 */
	//public Map<Long, String> getNamesByBusinessIdList(List<Long> ids);
	
	public LocationCode getLocationCode(long code);
	
	/**
	 * 按照顺序获得市的分组
	 * @return
	 */
	public JSONObject getAllCityInOrder();
	
	/**
	 * 获取所有市
	 * @return
	 */
	public List<LocationCode> getAllCities();
	
	/**
	 * 通过code返回对应位置的名称
	 * @param code
	 * @return
	 */
	public String getLocationFullNameByCode(long code);
}
