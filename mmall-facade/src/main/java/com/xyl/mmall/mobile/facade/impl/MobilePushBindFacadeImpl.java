/**
 * 
 */
package com.xyl.mmall.mobile.facade.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.tools.location.search.util.AreaCode;
import com.netease.tools.location.search.util.AreaUtil;
import com.vividsolutions.jts.io.ParseException;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.framework.enums.MobileErrorCode;
import com.xyl.mmall.framework.util.JsonUtils;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.ip.service.AreaOnlineService;
import com.xyl.mmall.ip.service.IPService;
import com.xyl.mmall.mainsite.facade.IPServiceFacade;
import com.xyl.mmall.member.service.MobileInfoService;
import com.xyl.mmall.mobile.facade.MobilePushBindFacade;
import com.xyl.mmall.mobile.facade.converter.Converter;
import com.xyl.mmall.task.dto.DeviceLocationDTO;
import com.xyl.mmall.task.service.DriverService;

/**
 * @author hzjiangww
 *
 */
@Facade
public class MobilePushBindFacadeImpl implements MobilePushBindFacade {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	DriverService driverService;

	@Resource
	MobileInfoService mobileInfoService;

	@Autowired
	private AreaOnlineService areaOnlineService;

	@Autowired
	private IPService ipService;

	@Autowired
	private IPServiceFacade ipServiceFacade;

	@Override
	public BaseJsonVO bindReceiver(String deviceId, long userId, int areaCode, Double latitude, Double longitude) {
		logger.info("deviceId:<" + deviceId + ">,userId:<" + userId + ">,areaCode:<" + areaCode + ">");
		if (deviceId == null) {
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "deviceId is null");
		}

		try {
			if (areaCode == 0) {
				if (latitude != null && longitude != null) {
					try {
						AreaCode area = AreaUtil.getAreaByGis(longitude, latitude);
						if (area != AreaCode.UnKnow) {
							areaCode = area.getCode();
						}
					} catch (ParseException e) {
						logger.error(e.toString());
					}
				}
			}
			HashMap<String, Object> key = driverService.genSign(deviceId);
			if (key == null)
				return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL, "gen sign error!");
			DeviceLocationDTO dto = new DeviceLocationDTO();
			dto.setAreaCode(areaCode);
			dto.setUserId(userId);
			dto.setUpdateTime(Converter.getTime());

			if (deviceId.endsWith("-@disable")) {
				dto.setPlatformType("");
				deviceId = deviceId.replaceAll("-@disable", "");
				dto.setDeviceId(deviceId);
			} else {
				dto.setPlatformType("ios,android");
				dto.setDeviceId(deviceId);
			}
			logger.info("deviceId:" + deviceId + "-" +JsonUtils.toJson(dto));
			driverService.addOrUpdateId(dto);

			return Converter.converterBaseJsonVO(key);
		} catch (Exception e) {
			logger.error(e.toString());
			return Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_ERROR, e.toString());
		}

	}

	@Override
	public BaseJsonVO getProvinceCode(String ip, Double latitude, Double longitude) {
		logger.info("ip:<" + ip + "> receive postion:" + latitude + "," + longitude);
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			String name = ipService.getProvince(ip);
			long code = ipServiceFacade.getProvinceCode(name);
			if (StringUtils.isBlank(name)||code <= 0l) {
				return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_FOUND);
			}
			boolean support = areaOnlineService.areaExist(code);
			if (!support) {
				return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_SUPPORT);
			}
			result.put("areaCode", String.valueOf(code));
			//result.put("ip", ip);
			return Converter.converterBaseJsonVO(result);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		/*
		 * if (latitude == null || longitude == null) { if (ip == null) return
		 * Converter.errorBaseJsonVO(MobileErrorCode.SERVICE_EXEC_FAIL,
		 * "latitude and longitude is null"); } else { try { AreaCode area =
		 * AreaUtil.getAreaByGis(longitude, latitude); if (area ==
		 * AreaCode.UnKnow) { return
		 * Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_FOUND); }
		 * 
		 * boolean support = areaOnlineService.areaExist(area.getCode());
		 * if(!support){ return
		 * Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_SUPPORT); }
		 * result.put("areaCode", Converter.AreaCode(area)); return
		 * Converter.converterBaseJsonVO(result); } catch (ParseException e) {
		 * logger.error(e.toString()); } } // TODO 验证IP
		 * logger.error("can not find postion:" + latitude + "," + longitude);
		 */
		return Converter.errorBaseJsonVO(MobileErrorCode.AREA_NOT_FOUND, "can not find your postion");
	}

	public static void main(String args[]) throws ParseException {
		AreaCode area = AreaUtil.getAreaByGis(-90, 30);
		System.out.println(area.getName());
	}
}
