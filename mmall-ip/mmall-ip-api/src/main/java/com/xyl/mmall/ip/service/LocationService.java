package com.xyl.mmall.ip.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.netease.print.common.meta.RetArg;
import com.xyl.mmall.ip.enums.ExpressType;
import com.xyl.mmall.ip.enums.RetState;
import com.xyl.mmall.ip.meta.LocationCode;

public interface LocationService {

	public Map<Long, String> getProvinceCodeNameMap();

	/**
	 * 获得所有省份的code数据
	 * 
	 * @return
	 */
	public List<LocationCode> getAllProvince();

	/**
	 * 按照省份的拼音首字母获得所有的省份的排序分组的列表
	 * 
	 * @return
	 */
	public JSONObject getAllProvinceInOrder();

	/**
	 * 通过省份code返回对应的市的列表
	 * 
	 * @param provinceCode
	 * @return
	 */
	public List<LocationCode> getCityListByProvinceCode(long provinceCode);

	/**
	 * 通过城市code返回对应的区或者县的列表
	 * 
	 * @param cityCode
	 * @return
	 */
	public List<LocationCode> getDistrictListByCityCode(long cityCode);

	/**
	 * 通过区code返回街道的列表
	 * 
	 * @param districtCode
	 * @return
	 */
	public List<LocationCode> getStreetListByDistrictCode(long districtCode);

	/**
	 * 通过code返回对应位置的名称
	 * 
	 * @param code
	 * @param singleName 是否单独名称
	 * @return
	 */
	public String getLocationNameByCode(long code, boolean singleName);

	/**
	 * 返回timeStamp之后的新数据
	 * 
	 * @param timeStamp
	 * @return
	 */
	public List<LocationCode> getLocationCodeListAfterTimeStamp(long timeStamp);

	/**
	 * 通过code列表获取地区列表
	 * 
	 * @param codeList
	 * @return
	 */
	public List<LocationCode> getLocationCodeListByCodeList(List<Long> codeList);
	/**
	 * 更具供应商id列表返回对应的名字
	 * 
	 * @param ids
	 * @return
	 */
	// public Map<Long, String> getNamesByBusinessIdList(List<Long> ids);
	/**
	 * 判断当前区域的cod情况
	 * @param districtCode
	 * @param type
	 * @return
	 */
	public RetState isDistrictCod(long districtCode, ExpressType type);
	/**
	 * 判断当前街道的cod情况
	 * @param streetCode
	 * @param type
	 * @return
	 */
	public RetState isStreetCod(long streetCode, ExpressType type);
	/**
	 * 判断当前区域是否为偏远地区
	 * @param districtCode
	 * @param type
	 * @return
	 */
	public RetArg isDistrictRemote(long districtCode, ExpressType type);
	/**
	 * 判断当前街城市否为偏远地区
	 * @param cityCode
	 * @param type
	 * @return
	 */
	public RetArg isCityRemote(long cityCode, ExpressType type);
	/**
	 * 传入三级code，返回是不是偏远地区
	 * @param provinceCode
	 * @param cityCode
	 * @param districtCode
	 * @param type
	 * @return
	 */
	public RetArg isLocationRemote(long provinceCode, long cityCode, long districtCode, ExpressType type);
	/**
	 * 判断地址是否是cod地址
	 * @param provinceCode
	 * @param cityCode
	 * @param districtCode
	 * @param streetCode
	 * @param type
	 * @return
	 */
	public boolean isLocationCod(long provinceCode, long cityCode,
			long districtCode, long streetCode, ExpressType type);
	
	public LocationCode getLocationCode(long code);

	/**
	 * 按照市的拼音首字母获得所有的市的排序分组的列表
	 * 
	 * @return
	 */
	public JSONObject getAllCityInOrder();

	/**
	 * 获取市code-name map
	 * @return
	 */
	public Map<Long, String> getCityCodeNameMap();
}
