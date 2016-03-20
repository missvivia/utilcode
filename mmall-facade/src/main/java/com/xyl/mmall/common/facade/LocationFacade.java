package com.xyl.mmall.common.facade;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.ip.meta.LocationCode;

/**
 * 地理位置接口
 * 
 * @author chengximing
 *
 */
public interface LocationFacade {

	/**
	 * 获得所有省份的code数据
	 * 
	 * @return
	 */
	public List<LocationCode> getAllProvince();

	/**
	 * 获得全国省份的code 按照省份拼音首字母排序分组
	 * 
	 * @return
	 */
	public JSONObject getProvinceListInOrder();

	/**
	 * 通过省份code获得市的列表
	 * 
	 * @param codeProvince
	 * @return
	 */
	public JSONObject getCityList(long provinceCode);

	/**
	 * 通过市的code获得区和县的列表
	 * 
	 * @param cityCode
	 * @return
	 */
	public JSONObject getDistrictList(long cityCode);

	/**
	 * 通过区或者县的code获得街道的列表
	 * 
	 * @param districtCode
	 * @return
	 */
	public JSONObject getStreetList(long districtCode);

	/**
	 * 获得timeStamp之后的新数据
	 * 
	 * @param timeStamp
	 * @return
	 */
	public List<LocationCode> getLocationCodeListAfterTimeStamp(long timeStamp);

	/**
	 * 根据location的code列表获取地区列表
	 * 
	 * @param codeList
	 * @return
	 */
	public List<LocationCode> getLocationCodeListByCodeList(List<Long> codeList);

	/**
	 * 根据供应商Id列表返回id对应的位置名称map
	 * 
	 * @param ids
	 * @return
	 */
	// public Map<Long, String> getNamesByBusinessIdList(List<Long> ids);
	/**
	 * 通过code返回对应的地址名称
	 * 
	 * @param code
	 * @param singleName 
	 * @return
	 */
	public String getLocationNameByCode(long code, boolean singleName);

	/**
	 * 获得全国市的code 按照市拼音首字母排序分组
	 * 
	 * @return
	 */
	public JSONObject getCityListInOrder();
	
	/**
	 * 
	 * @param agentId
	 * @param permissions
	 * @return
	 */
	public List<AreaDTO> getAreaListByAgentId(long agentId, String permissions);
	
	/**
	 * 跟code获取区域信息
	 * @param code
	 * @return LocationCode
	 */
	public LocationCode getLocationCodeByCode(long code);
}
